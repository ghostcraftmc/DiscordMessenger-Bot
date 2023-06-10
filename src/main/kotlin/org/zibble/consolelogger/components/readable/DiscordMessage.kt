package org.zibble.consolelogger.components.readable

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import net.dv8tion.jda.api.utils.messages.MessageCreateData
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder
import net.dv8tion.jda.api.utils.messages.MessageEditData
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.NativeSerializer
import org.zibble.consolelogger.components.entity.ActionRow
import org.zibble.consolelogger.components.messagable.MentionType

data class DiscordMessage(
    val content: String?,
    val embeds: Collection<DiscordEmbed>,
    var isTTS: Boolean,
    val allowedMentions: Collection<MentionType>,
    val actionRow: Collection<ActionRow>
) : JsonSerializable, NativeSerializer<MessageCreateData> {

    companion object {
        fun fromNative(data: Message): DiscordMessage {
            return DiscordMessage(
                data.contentRaw,
                data.embeds.map { DiscordEmbed.fromNative(it) },
                data.isTTS,
                setOf(MentionType.EVERYONE),
                data.actionRows.map { ActionRow.fromNative(it) }
            )
        }
    }

    override fun toNative(): MessageCreateData {
        return MessageCreateBuilder()
            .setTTS(isTTS)
            .setEmbeds(embeds.map { it.toNative() })
            .setAllowedMentions(allowedMentions.map { it.toNative() })
            .setContent(content)
            .addComponents(actionRow.map { it.toNative() })
            .build()
    }

    fun toMessageEditData(): MessageEditData {
        return MessageEditBuilder()
            .setEmbeds(embeds.map { it.toNative() })
            .setAllowedMentions(allowedMentions.map { it.toNative() })
            .setContent(content)
            .setComponents(actionRow.map { it.toNative() })
            .build()
    }

}