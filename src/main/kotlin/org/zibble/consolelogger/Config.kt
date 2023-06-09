package org.zibble.consolelogger

import com.google.gson.annotations.SerializedName
import org.zibble.consolelogger.components.messagable.OptionType

class Config {

    @SerializedName("ptero_url")
    var pteroURL: String? = null

    @SerializedName("ptero_token")
    var pteroToken: String? = null

    @SerializedName("discord_token")
    var discordToken: String? = null

    @SerializedName("guild_id")
    var guildId: String? = null

    @SerializedName("command_prefix")
    var prefix: String? = null

    @SerializedName("server-console-mappings")
    var servers: List<Servers> = ArrayList()

    @SerializedName("redis")
    var redis = Redis()

    @SerializedName("listen-command")
    var listenableCommands: List<SlashCommand> = ArrayList()

    @SerializedName("legacy-command")
    var legacyCommands: List<String> = ArrayList()

    @SerializedName("buttons")
    var buttons: List<Button> = ArrayList()

    @SerializedName("select-menu")
    var selectMenu: List<SelectMenu> = ArrayList()

    class SelectMenu {

        @SerializedName("id")
        var id: String? = null

        @SerializedName("replyable")
        var replyable = false

        @SerializedName("ephemeral")
        var ephemeral = false

    }

    class Button {

        @SerializedName("id")
        var id: String? = null

        @SerializedName("replyable")
        var replyable = false

        @SerializedName("ephemeral")
        var ephemeral = false

    }

    class SlashCommand {

        @SerializedName("name")
        var name: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("sub-group")
        var subGroup: ArrayList<SubCommandGroup> = ArrayList()

        @SerializedName("sub-command")
        var subCommand: ArrayList<SubCommand> = ArrayList()

        @SerializedName("options")
        var options: ArrayList<Option> = ArrayList()

        @SerializedName("ephemeral")
        var ephemeral = false

        class SubCommandGroup {

            @SerializedName("name")
            var name: String? = null

            @SerializedName("description")
            var description: String? = null

            @SerializedName("sub-command")
            var subCommand: ArrayList<SubCommand> = ArrayList()

        }

        class SubCommand {

            @SerializedName("name")
            var name: String? = null

            @SerializedName("description")
            var description: String? = null

            @SerializedName("options")
            var options: ArrayList<Option> = ArrayList()

        }

        class Option {

            @SerializedName("type")
            var type: OptionType = OptionType.UNKNOWN

            @SerializedName("name")
            var name: String? = null

            @SerializedName("description")
            var description: String? = null

            @SerializedName("required")
            var required = false

            @SerializedName("auto-complete")
            var autoComplete = false

            @SerializedName("auto-fills")
            var autoFills: List<String> = ArrayList()

        }

    }

    class Servers {
        @SerializedName("server_id")
        var serverID: String? = null

        @SerializedName("channel_id")
        var channelID: String? = null

        @SerializedName("authorization_role_id")
        var authorizationRoleID: String? = null
    }

    class Redis {
        @SerializedName("host")
        var host: String? = null

        @SerializedName("port")
        var port = 0

        @SerializedName("password")
        var password: String? = null
    }

}