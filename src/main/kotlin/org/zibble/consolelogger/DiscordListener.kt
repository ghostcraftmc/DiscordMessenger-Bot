package org.zibble.consolelogger

import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.zibble.consolelogger.components.action.sendable.ChannelMessageAction
import org.zibble.consolelogger.components.entity.*
import org.zibble.consolelogger.components.messagable.*
import org.zibble.consolelogger.components.readable.DiscordMessage

class DiscordListener(
    val discordHook: DiscordHook,
    val prefix: String,
    val listenableChannel: List<Long>,
    val commands: List<SlashCommand>,
    val legacyCommands: List<String>,
    val buttons: List<Button>,
    val selectMenus: List<SelectMenu>
) : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.channel !is TextChannel) return

        if (legacyCommands.any { event.message.contentRaw.startsWith(it) }) {
            RedisListener.sendCommand(LegacyCommand.fromNative(event.message))
        }

        if (event.member != null && event.member!!.user.isBot) return
        if (event.message.contentRaw.startsWith(prefix)) {
            for (entry: Map.Entry<TextChannel, Role> in discordHook.channels.entries) {
                if (entry.key.id == event.channel.id) {
                    var auth = false
                    if (!event.isWebhookMessage) {
                        for (role in event.member!!.roles) {
                            if (role.id == entry.value.id) {
                                auth = true
                                break
                            }
                        }
                    } else {
                        auth = true
                    }
                    if (!auth) return
                    PteroHook.pteroHook.sendCommand(
                        PteroHook.pteroHook.getServerForChannel(event.channel.id),
                        event.message.contentRaw.substring(prefix.length)
                    )
                    break
                }
            }

            return
        }

        if (listenableChannel.contains(event.channel.idLong)) {
            RedisListener.sendAction(ChannelMessageAction(
                event.channel.idLong,
                User.fromNative(event.author, event.member!!, event.member!!.roles),
                DiscordMessage.fromNative(event.message)
            ))
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val cmd = commands.firstOrNull { event.name == it.name }
        if (cmd != null) {
            event.deferReply(cmd.ephemeral)
            RedisListener.sendCommand(org.zibble.consolelogger.components.messagable.SlashCommand.fromNative(event)) {
                event.hook.sendMessage(it.message.toNative())
                    .setEphemeral(it.ephemeral)
                    .queue()
            }
        }
    }

    override fun onCommandAutoCompleteInteraction(event: CommandAutoCompleteInteractionEvent) {
        val cmd = commands.firstOrNull { it.name == event.name }
        if (cmd != null) {
            val args = event.fullCommandName.split(" ").drop(1).toMutableList()
            val option = if (args.size > 1) {
                val subGroup = cmd.subGroup.firstOrNull { it.name == args[0] } ?: return
                val subCmd = subGroup.subCommand.firstOrNull { it.name == args[1] } ?: return
                subCmd.options.firstOrNull { it.name == event.focusedOption.name } ?: return
            } else {
                val subCmd = cmd.subCommand.firstOrNull { it.name == args[0] } ?: return
                subCmd.options.firstOrNull { it.name == event.focusedOption.name } ?: return
            }
            when (option.type) {
                OptionType.INTEGER -> event.replyChoiceLongs(option.autoFills.map { it.toLong() })
                OptionType.NUMBER -> event.replyChoiceDoubles(option.autoFills.map { it.toDouble() })
                else -> event.replyChoiceStrings(option.autoFills)
            }
        }
    }

    override fun onButtonInteraction(event: ButtonInteractionEvent) {
        val button = buttons.firstOrNull {
            event.button.id != null && it.id.toRegex().matches(event.button.id!!)
        }
        if (button != null) {
            if (button.replyable)
                event.deferReply(button.ephemeral).queue()

            RedisListener.sendButtonInteraction(
                ButtonInteraction(
                    org.zibble.consolelogger.components.entity.Button.fromNative(event.button),
                    User.fromNative(event.user, event.member!!, event.member!!.roles),
                    MessageChannel.fromNative(event.channel as TextChannel),
                    event.messageIdLong,
                )
            ) {
                if (button.replyable) {
                    event.hook.sendMessage(it.discordMessage.toNative()).setEphemeral(it.ephemeral).queue()
                }
            }
        }
    }

    override fun onStringSelectInteraction(event: StringSelectInteractionEvent) {
        val menu = selectMenus.firstOrNull { it.id.toRegex().matches(event.selectMenu.id!!) }
        if (menu != null) {
            if (menu.replyable)
                event.deferReply(menu.ephemeral).queue()

            RedisListener.sendSelectMenuInteraction(
                SelectMenuInteraction(
                    org.zibble.consolelogger.components.entity.SelectMenu.fromNative(event.selectMenu),
                    User.fromNative(event.user, event.member!!, event.member!!.roles),
                    MessageChannel.fromNative(event.channel as TextChannel),
                    event.messageIdLong,
                    event.selectedOptions.map { SelectOption.fromNative(it) }
                )
            ) {
                if (menu.replyable)
                    event.hook.sendMessage(it.discordMessage.toNative()).setEphemeral(it.ephemeral).queue()
            }
        }
    }
}