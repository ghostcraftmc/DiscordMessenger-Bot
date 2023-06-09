package org.zibble.consolelogger.components.messagable

import club.minnced.discord.webhook.send.AllowedMentions
import net.dv8tion.jda.api.entities.Message
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.NativeSerializer
import org.zibble.consolelogger.components.WebhookSerializable

enum class MentionType : JsonSerializable, NativeSerializer<Message.MentionType>, WebhookSerializable<AllowedMentions> {

    USER,
    ROLE,
    CHANNEL,
    EMOTE,
    HERE,
    EVERYONE,
    ;

    override fun toNative(): Message.MentionType {
        return Message.MentionType.valueOf(name)
    }

    override fun toWebhookEntity(): AllowedMentions {
        return if (this == EVERYONE) {
            AllowedMentions.all()
        } else if (this == USER) {
            AllowedMentions().withParseUsers(true)
        } else if (this == ROLE) {
            AllowedMentions().withParseRoles(true)
        } else if (this == HERE) {
            AllowedMentions().withParseEveryone(true)
        } else {
            AllowedMentions()
        }
    }

}

fun Collection<MentionType>.flattenToWebhookEntity(): AllowedMentions {
    if (contains(MentionType.EVERYONE)) {
        return AllowedMentions.all()
    }

    val allowedMentions = AllowedMentions()
    if (contains(MentionType.USER)) {
        allowedMentions.withParseUsers(true)
    }
    if (contains(MentionType.ROLE)) {
        allowedMentions.withParseRoles(true)
    }
    if (contains(MentionType.HERE)) {
        allowedMentions.withParseEveryone(true)
    }

    return allowedMentions
}