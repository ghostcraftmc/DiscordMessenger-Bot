package org.zibble.consolelogger.components.entity

import java.util.*
import java.util.stream.Collectors

enum class Permission(val offset: Int, val isGuild: Boolean, val isChannel: Boolean, val display: String) {

    // General Server / Channel Permissions
    MANAGE_CHANNEL(4, true, true, "Manage Channels"),
    MANAGE_SERVER(5, true, false, "Manage Server"),
    VIEW_AUDIT_LOGS(7, true, false, "View Audit Logs"),
    VIEW_CHANNEL(10, true, true, "View Channel(s)"),
    VIEW_GUILD_INSIGHTS(19, true, false, "View Server Insights"),
    MANAGE_ROLES(28, true, false, "Manage Roles"),
    MANAGE_PERMISSIONS(28, false, true, "Manage Permissions"),
    MANAGE_WEBHOOKS(29, true, true, "Manage Webhooks"),
    MANAGE_EMOTES_AND_STICKERS(30, true, false, "Manage Emojis and Stickers"),

    // Membership Permissions
    CREATE_INSTANT_INVITE(0, true, true, "Create Instant Invite"),
    KICK_MEMBERS(1, true, false, "Kick Members"),
    BAN_MEMBERS(2, true, false, "Ban Members"),
    NICKNAME_CHANGE(26, true, false, "Change Nickname"),
    NICKNAME_MANAGE(27, true, false, "Manage Nicknames"),
    MODERATE_MEMBERS(40, true, false, "Timeout Members"),

    // Text Permissions
    MESSAGE_ADD_REACTION(6, true, true, "Add Reactions"),
    MESSAGE_SEND(11, true, true, "Send Messages"),
    MESSAGE_TTS(12, true, true, "Send TTS Messages"),
    MESSAGE_MANAGE(13, true, true, "Manage Messages"),
    MESSAGE_EMBED_LINKS(14, true, true, "Embed Links"),
    MESSAGE_ATTACH_FILES(15, true, true, "Attach Files"),
    MESSAGE_HISTORY(16, true, true, "Read History"),
    MESSAGE_MENTION_EVERYONE(17, true, true, "Mention Everyone"),
    MESSAGE_EXT_EMOJI(18, true, true, "Use External Emojis"),
    USE_APPLICATION_COMMANDS(31, true, true, "Use Application Commands"),
    MESSAGE_EXT_STICKER(37, true, true, "Use External Stickers"),

    // Thread Permissions
    MANAGE_THREADS(34, true, true, "Manage Threads"),
    CREATE_PUBLIC_THREADS(35, true, true, "Create Public Threads"),
    CREATE_PRIVATE_THREADS(36, true, true, "Create Private Threads"),
    MESSAGE_SEND_IN_THREADS(38, true, true, "Send Messages in Threads"),

    // Voice Permissions
    PRIORITY_SPEAKER(8, true, true, "Priority Speaker"),
    VOICE_STREAM(9, true, true, "Video"),
    VOICE_CONNECT(20, true, true, "Connect"),
    VOICE_SPEAK(21, true, true, "Speak"),
    VOICE_MUTE_OTHERS(22, true, true, "Mute Members"),
    VOICE_DEAF_OTHERS(23, true, true, "Deafen Members"),
    VOICE_MOVE_OTHERS(24, true, true, "Move Members"),
    VOICE_USE_VAD(25, true, true, "Use Voice Activity"),
    VOICE_START_ACTIVITIES(39, true, true, "Launch Activities in Voice Channels"),

    // Stage Channel Permissions
    REQUEST_TO_SPEAK(32, true, true, "Request to Speak"),

    // Advanced Permissions
    ADMINISTRATOR(3, true, false, "Administrator"),


    UNKNOWN(-1, false, false, "Unknown");

    companion object {
        val EMPTY_PERMISSIONS: Array<Permission> = arrayOf()
        val ALL: Array<Permission> = values()
        const val PUBLIC_PERMISSION_RAW = 0
        val ALL_PERMISSIONS = getRaw(*ALL)
        val ALL_CHANNEL_PERMISSIONS =
            getRaw(Arrays.stream(ALL).filter(Permission::isChannel).collect(Collectors.toList()))
        val ALL_GUILD_PERMISSIONS = getRaw(Arrays.stream(ALL).filter(Permission::isGuild).collect(Collectors.toList()))
        val ALL_TEXT_PERMISSIONS = getRaw(
            MESSAGE_ADD_REACTION, MESSAGE_SEND, MESSAGE_TTS, MESSAGE_MANAGE,
            MESSAGE_EMBED_LINKS, MESSAGE_ATTACH_FILES, MESSAGE_EXT_EMOJI, MESSAGE_EXT_STICKER,
            MESSAGE_HISTORY, MESSAGE_MENTION_EVERYONE, USE_APPLICATION_COMMANDS,
            MANAGE_THREADS, CREATE_PUBLIC_THREADS, CREATE_PRIVATE_THREADS, MESSAGE_SEND_IN_THREADS
        )
        val ALL_VOICE_PERMISSIONS = getRaw(
            VOICE_STREAM, VOICE_CONNECT, VOICE_SPEAK, VOICE_MUTE_OTHERS,
            VOICE_DEAF_OTHERS, VOICE_MOVE_OTHERS, VOICE_USE_VAD,
            PRIORITY_SPEAKER, REQUEST_TO_SPEAK, VOICE_START_ACTIVITIES
        )

        fun getFromOffset(offset: Int): Permission {
            for (perm in ALL) {
                if (perm.offset == offset) return perm
            }
            return UNKNOWN
        }

        fun getPermissions(permissions: Long): EnumSet<Permission> {
            if (permissions == 0L) return EnumSet.noneOf(Permission::class.java)
            val perms = EnumSet.noneOf(Permission::class.java)
            for (perm in Permission.values()) {
                if (perm != UNKNOWN && permissions and perm.raw == perm.raw)
                    perms.add(perm)
            }
            return perms
        }

        fun getRaw(vararg permissions: Permission): Long {
            var raw: Long = 0
            for (perm in permissions) {
                if (perm != UNKNOWN)
                    raw = raw or perm.raw
            }
            return raw
        }

        fun getRaw(permissions: Collection<Permission>): Long {
            return getRaw(*permissions.toTypedArray())
        }
    }

    var raw: Long = 1L shl offset

    fun isText(): Boolean {
        return raw and ALL_TEXT_PERMISSIONS == raw
    }

    fun isVoice(): Boolean {
        return raw and ALL_VOICE_PERMISSIONS == raw
    }

}