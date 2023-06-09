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
import org.zibble.consolelogger.components.action.WebhookUrl
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

class DiscordHook(
    token: String,
    servers: List<Config.Servers>,
    prefix: String,
    guildId: String,
    commands: List<Config.SlashCommand>,
    legacyCommands: List<String>,
    button: List<Config.Button>,
    selectMenu: List<Config.SelectMenu>
) {

    companion object {
        lateinit var discordHook: DiscordHook
    }

    var jda: JDA
    val channels: MutableMap<TextChannel, Role> = HashMap()
    val guild: Guild
    val webhookClients: MutableMap<WebhookUrl, WebhookClient> = HashMap()

    init {
        discordHook = this
        jda = JDABuilder
            .createDefault(token)
            .addEventListeners(DiscordListener(this, prefix, commands, legacyCommands, button, selectMenu))
            .build()
            .awaitReady()

        guild = jda.getGuildById(guildId)!!

        for (cmd in commands) {
            val slashCmd = Commands.slash(cmd.name!!, cmd.description!!)
            if (cmd.subGroup.isNotEmpty()) {
                slashCmd.addSubcommandGroups(cmd.subGroup.map {
                    SubcommandGroupData(it.name!!, it.description!!).apply {
                        if (it.subCommand.isNotEmpty()) {
                            slashCmd.addSubcommands(it.subCommand.map {
                                SubcommandData(it.name!!, it.description!!).apply {
                                    for (option in it.options) {
                                        this.addOption(
                                            OptionType.fromKey(option.type.key),
                                            option.name!!,
                                            option.description!!,
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
                    SubcommandData(it.name!!, it.description!!).apply {
                        for (option in it.options) {
                            this.addOption(
                                OptionType.fromKey(option.type.key),
                                option.name!!,
                                option.description!!,
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
                        option.name!!,
                        option.description!!,
                        option.required,
                        option.autoComplete
                    )
                }
            }
            guild.upsertCommand(slashCmd).queue()
        }

        for (server in servers) {
            jda.getTextChannelById(server.channelID!!)?.let { channel: TextChannel ->
                jda.getRoleById(server.authorizationRoleID!!)?.let {
                    channels[channel] = it
                }
            }
        }
    }

    fun sendMessage(channelId: String, message: MessageCreateData) {
        val channel = jda.getTextChannelById(channelId)
        channel?.sendMessage(message)?.queue()
    }

    fun registerWebhook(webhookUrl: WebhookUrl): Boolean {
        if (webhookClients.containsKey(webhookUrl)) return false
        val client = WebhookClientBuilder(webhookUrl.url)
            .setThreadFactory(threadFactory("WebhookClient", false))
            .setWait(true)
            .build()
        webhookClients[webhookUrl] = client
        return true
    }

    fun sendWebhookMessage(webhookUrl: WebhookUrl, message: WebhookMessage): CompletableFuture<ReadonlyMessage> {
        val webhook = webhookClients[webhookUrl] ?: return CompletableFuture.completedFuture(null)
        return webhook.send(message)
    }

}