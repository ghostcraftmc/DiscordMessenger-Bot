package org.zibble.consolelogger

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.WebhookClientBuilder
import club.minnced.discord.webhook.receive.ReadonlyMessage
import club.minnced.discord.webhook.send.WebhookMessage
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import okhttp3.internal.threadFactory
import java.util.concurrent.CompletableFuture

class DiscordHook(
    token: String,
    servers: List<Servers>,
    prefix: String,
    guildId: String,
    webhooks: Map<Int, String>,
    listenableChannels: List<Long>,
    commands: List<SlashCommand>,
    legacyCommands: List<String>,
    button: List<Button>,
    selectMenu: List<SelectMenu>
) {

    companion object {
        lateinit var discordHook: DiscordHook
    }

    var jda: JDA
    val channels: MutableMap<TextChannel, Role> = HashMap()
    val guild: Guild
    val webhookClients: MutableMap<Int, WebhookClient> = HashMap()

    init {
        discordHook = this
        jda = JDABuilder
            .createDefault(token)
            .addEventListeners(DiscordListener(this, prefix, listenableChannels, commands, legacyCommands, button, selectMenu))
            .build()
            .awaitReady()

        guild = jda.getGuildById(guildId)!!

        for (webhook in webhooks) {
            registerWebhook(webhook.key, webhook.value)
        }

        for (cmd in commands) {
            val slashCmd = Commands.slash(cmd.name, cmd.description)
            if (cmd.subGroup.isNotEmpty()) {
                slashCmd.addSubcommandGroups(cmd.subGroup.map {
                    SubcommandGroupData(it.name, it.description).apply {
                        if (it.subCommand.isNotEmpty()) {
                            slashCmd.addSubcommands(it.subCommand.map {
                                SubcommandData(it.name, it.description).apply {
                                    for (option in it.options) {
                                        this.addOption(
                                            OptionType.fromKey(option.type.key),
                                            option.name,
                                            option.description,
                                            option.required,
                                            option.autoComplete
                                        )
                                    }
                                }
                            })
                        }
                    }
                })
            }
            if (cmd.subCommand.isNotEmpty()) {
                slashCmd.addSubcommands(cmd.subCommand.map {
                    SubcommandData(it.name, it.description).apply {
                        for (option in it.options) {
                            this.addOption(
                                OptionType.fromKey(option.type.key),
                                option.name,
                                option.description,
                                option.required,
                                option.autoComplete
                            )
                        }
                    }
                })
            }
            if (cmd.options.isNotEmpty()) {
                for (option in cmd.options) {
                    slashCmd.addOption(
                        OptionType.fromKey(option.type.key),
                        option.name,
                        option.description,
                        option.required,
                        option.autoComplete
                    )
                }
            }
            guild.upsertCommand(slashCmd).queue()
        }

        for (server in servers) {
            jda.getTextChannelById(server.channelID)?.let { channel: TextChannel ->
                jda.getRoleById(server.authorizationRoleID)?.let {
                    channels[channel] = it
                }
            }
        }
    }

    fun sendMessage(channelId: String, message: MessageCreateData) {
        val channel = jda.getTextChannelById(channelId)
        channel?.sendMessage(message)?.queue()
    }

    private fun registerWebhook(id: Int, webhookUrl: String): Boolean {
        if (webhookClients.containsKey(id)) return false
        val client = WebhookClientBuilder(webhookUrl)
            .setThreadFactory(threadFactory("WebhookClient $id", false))
            .setWait(true)
            .build()
        webhookClients[id] = client
        return true
    }

    fun sendWebhookMessage(webhookId: Int, message: WebhookMessage): CompletableFuture<ReadonlyMessage> {
        val webhook = webhookClients[webhookId] ?: return CompletableFuture.completedFuture(null)
        return webhook.send(message)
    }

}