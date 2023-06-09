package org.zibble.consolelogger.components.readable

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import org.zibble.consolelogger.components.NativeSerializer
import org.zibble.consolelogger.components.WebhookSerializable
import java.awt.Color
import java.time.OffsetDateTime

data class DiscordEmbed(
    val timestamp: OffsetDateTime?,
    val color: Color?,
    val description: String?,
    val thumbnailUrl: String?,
    val imageUrl: String?,
    val footer: EmbedFooter?,
    val title: EmbedTitle?,
    val author: EmbedAuthor?,
    val fields: Collection<EmbedField>
) : NativeSerializer<MessageEmbed>, WebhookSerializable<WebhookEmbed> {

    override fun toNative(): MessageEmbed {
        val builder = EmbedBuilder()
        if (timestamp != null) {
            builder.setTimestamp(timestamp)
        }
        if (color != null) {
            builder.setColor(color)
        }
        if (description != null) {
            builder.setDescription(description)
        }
        if (thumbnailUrl != null) {
            builder.setThumbnail(thumbnailUrl)
        }
        if (imageUrl != null) {
            builder.setImage(imageUrl)
        }
        if (footer != null) {
            builder.setFooter(footer.text, footer.icon)
        }
        if (author != null) {
            builder.setAuthor(author.name, author.url, author.iconUrl)
        }
        if (!fields.isEmpty()) {
            for (field in fields) {
                builder.addField(field.toNative())
            }
        }
        if (title != null) {
            builder.setTitle(title.text, title.url)
        }
        return builder.build()
    }

    override fun toWebhookEntity(): WebhookEmbed {
        val builder = WebhookEmbedBuilder()
        if (timestamp != null) {
            builder.setTimestamp(timestamp)
        }
        if (color != null) {
            builder.setColor(color.rgb)
        }
        if (description != null) {
            builder.setDescription(description)
        }
        if (thumbnailUrl != null) {
            builder.setThumbnailUrl(thumbnailUrl)
        }
        if (imageUrl != null) {
            builder.setImageUrl(imageUrl)
        }
        if (footer != null) {
            builder.setFooter(footer.toWebhookEntity())
        }
        if (author != null) {
            builder.setAuthor(author.toWebhookEntity())
        }
        if (!fields.isEmpty()) {
            for (field in fields) {
                builder.addField(field.toWebhookEntity())
            }
        }
        if (title != null) {
            builder.setTitle(title.toWebhookEntity())
        }
        return builder.build()
    }

    data class EmbedTitle(val text: String, val url: String?) :
        NativeSerializer<MessageEmbed.Provider>, WebhookSerializable<WebhookEmbed.EmbedTitle> {

        override fun toNative(): MessageEmbed.Provider {
            return MessageEmbed.Provider(text, url)
        }

        override fun toWebhookEntity(): WebhookEmbed.EmbedTitle {
            return WebhookEmbed.EmbedTitle(text, url)
        }

    }

    data class EmbedFooter(val text: String, val icon: String?) :
        NativeSerializer<MessageEmbed.Footer>, WebhookSerializable<WebhookEmbed.EmbedFooter> {

        override fun toNative(): MessageEmbed.Footer {
            return MessageEmbed.Footer(text, icon, null)
        }

        override fun toWebhookEntity(): WebhookEmbed.EmbedFooter {
            return WebhookEmbed.EmbedFooter(text, icon)
        }

    }

    data class EmbedAuthor(val name: String, val iconUrl: String?, val url: String?) :
        NativeSerializer<MessageEmbed.AuthorInfo>, WebhookSerializable<WebhookEmbed.EmbedAuthor> {

        override fun toNative(): MessageEmbed.AuthorInfo {
            return MessageEmbed.AuthorInfo(name, url, iconUrl, null)
        }

        override fun toWebhookEntity(): WebhookEmbed.EmbedAuthor {
            return WebhookEmbed.EmbedAuthor(name, url, iconUrl)
        }

    }

    data class EmbedField(val name: String, val value: String, val isInline: Boolean = false) :
        NativeSerializer<MessageEmbed.Field>, WebhookSerializable<WebhookEmbed.EmbedField> {

        override fun toNative(): MessageEmbed.Field {
            return MessageEmbed.Field(name, value, isInline)
        }

        override fun toWebhookEntity(): WebhookEmbed.EmbedField {
            return WebhookEmbed.EmbedField(isInline, name, value)
        }

    }

}