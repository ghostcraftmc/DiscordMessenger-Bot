package org.zibble.consolelogger

import com.google.gson.annotations.SerializedName
import org.zibble.consolelogger.components.messagable.OptionType

data class Config(
    @SerializedName("ptero-url") val pteroURL: String,
    @SerializedName("ptero-token") val pteroToken: String,
    @SerializedName("discord-token") val discordToken: String,
    @SerializedName("guild-id") val guildId: String,
    @SerializedName("command-prefix") val prefix: String,
    @SerializedName("server-console-mappings") val servers: List<Servers>,
    @SerializedName("redis") val redis: Redis,
    @SerializedName("webhook-mapping") val webhooks: Map<String, String>,
    @SerializedName("listen-command") val listenableCommands: List<SlashCommand>,
    @SerializedName("legacy-command") val legacyCommands: List<String>,
    @SerializedName("listen-channel") val listenableChannels: List<Long>,
    @SerializedName("buttons") val buttons: List<Button>,
    @SerializedName("select-menu") val selectMenu: List<SelectMenu>
)

data class SelectMenu(
    @SerializedName("id") val id: String,
    @SerializedName("replyable") val replyable: Boolean,
    @SerializedName("ephemeral") val ephemeral: Boolean
)

data class Button(
    @SerializedName("id") val id: String,
    @SerializedName("replyable") val replyable: Boolean,
    @SerializedName("ephemeral") val ephemeral: Boolean
)

data class SlashCommand(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("sub-group") val subGroup: ArrayList<SubCommandGroup>,
    @SerializedName("sub-command") val subCommand: ArrayList<SubCommand>,
    @SerializedName("options") val options: List<Option>,
    @SerializedName("ephemeral") val ephemeral: Boolean
)

data class SubCommandGroup(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("sub-command") val subCommand: ArrayList<SubCommand>
)

data class SubCommand(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("options") val options: List<Option>
)

data class Option(
    @SerializedName("type") val type: OptionType,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("required") val required: Boolean,
    @SerializedName("auto-complete") val autoComplete: Boolean,
    @SerializedName("auto-fills") val autoFills: List<String>
)

data class Servers(
    @SerializedName("server-id") val serverID: String,
    @SerializedName("channel-id") val channelID: String,
    @SerializedName("authorization-role-id") val authorizationRoleID: String
)

data class Redis(
    @SerializedName("host") val host: String,
    @SerializedName("port") val port: Int,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)