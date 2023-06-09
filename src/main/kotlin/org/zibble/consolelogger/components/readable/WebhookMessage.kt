package org.zibble.consolelogger.components.readable

import club.minnced.discord.webhook.send.WebhookMessage
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.WebhookSerializable
import org.zibble.consolelogger.components.messagable.MentionType
import org.zibble.consolelogger.components.messagable.flattenToWebhookEntity

data class WebhookMessage(
    val username: String?,
    val avatarUrl: String?,
    val content: String?,
    val embeds: Collection<DiscordEmbed>,
    val isTTs: Boolean,
    val allowedMessage: Collection<MentionType>
) : JsonSerializable, WebhookSerializable<WebhookMessage> {

    override fun toWebhookEntity(): WebhookMessage {
        return WebhookMessageBuilder()
            .setUsername(username)
            .setAvatarUrl(avatarUrl)
            .setContent(content)
            .addEmbeds(embeds.map { it.toWebhookEntity() })
            .setTTS(isTTs)
            .setAllowedMentions(allowedMessage.flattenToWebhookEntity())
            .build()
    }

}