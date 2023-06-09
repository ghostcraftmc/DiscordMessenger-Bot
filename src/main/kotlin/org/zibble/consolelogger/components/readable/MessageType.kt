package org.zibble.consolelogger.components.readable

import net.dv8tion.jda.api.entities.Message

enum class MessageType(val id: Int, val isSystem: Boolean = true) {
    /**
     * The normal text messages received when a user or bot sends a Message.
     */
    DEFAULT(0, false),

    /**
     * Specialized messages used for Groups as a System-Message showing that a new User has been added to the Group.
     * Also used in message threads to indicate a member has joined that thread.
     */
    RECIPIENT_ADD(1),

    /**
     * Specialized messages used for Groups as a System-Message showing that a new User has been removed from the Group.
     * Also used in message threads to indicate a member has left that thread.
     */
    RECIPIENT_REMOVE(2),

    /**
     * Specialized message used for Groups as a System-Message showing that a Call was started.
     */
    CALL(3),

    /**
     * Specialized message used for Groups as a System-Message showing that the name of the Group was changed.
     * Also used in message threads to indicate the name of that thread has changed.
     */
    CHANNEL_NAME_CHANGE(4),

    /**
     * Specialized message used for Groups as a System-Message showing that the icon of the Group was changed.
     */
    CHANNEL_ICON_CHANGE(5),

    /**
     * Specialized message used in MessageChannels as a System-Message to announce new pins
     */
    CHANNEL_PINNED_ADD(6),

    /**
     * Specialized message used to welcome new members in a Guild
     */
    GUILD_MEMBER_JOIN(7),

    /**
     * Specialized message used to announce a new booster
     */
    GUILD_MEMBER_BOOST(8),

    /**
     * Specialized message used to announce the server has reached tier 1
     */
    GUILD_BOOST_TIER_1(9),

    /**
     * Specialized message used to announce the server has reached tier 2
     */
    GUILD_BOOST_TIER_2(10),

    /**
     * Specialized message used to announce the server has reached tier 3
     */
    GUILD_BOOST_TIER_3(11),

    /**
     * Specialized message used to announce when a crosspost webhook is added to a channel
     */
    CHANNEL_FOLLOW_ADD(12),

    /**
     * System message related to discovery qualifications.
     */
    GUILD_DISCOVERY_DISQUALIFIED(14),

    /**
     * System message related to discovery qualifications.
     */
    GUILD_DISCOVERY_REQUALIFIED(15),

    /**
     * System message related to discovery qualifications.
     */
    GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16),

    /**
     * System message related to discovery qualifications.
     */
    GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17),

    /**
     * This is sent to a TextChannel when a message thread is created if the message from which the thread was started is "old".
     * The definition of "old" is loose, but is currently a very liberal definition.
     */
    THREAD_CREATED(18),

    /**
     * Reply to another message. This usually comes with a [referenced message][Message.getReferencedMessage].
     */
    INLINE_REPLY(19, false),

    /**
     * This message was created by an interaction. Usually in combination with Slash Commands.
     */
    SLASH_COMMAND(20, false),

    /**
     * A new message sent as the first message in threads that are started from an existing message in the parent channel.
     * It only contains a message reference field that points to the message from which the thread was started.
     */
    THREAD_STARTER_MESSAGE(21),

    /**
     * The "Invite your friends" messages that are sent to guild owners in new servers.
     */
    GUILD_INVITE_REMINDER(22),

    /**
     * This message was created by an interaction. Usually in combination with Context Menus.
     */
    CONTEXT_COMMAND(23, false),

    /**
     * Unknown MessageType.
     */
    UNKNOWN(-1);

    companion object {
        fun fromId(id: Int): MessageType {
            return MessageType.values().firstOrNull { it.id == id } ?: UNKNOWN
        }
    }

}