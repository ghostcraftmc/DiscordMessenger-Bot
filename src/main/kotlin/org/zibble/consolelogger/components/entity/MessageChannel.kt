package org.zibble.consolelogger.components.entity

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.components.JsonSerializable

data class MessageChannel(
    val id: Long,
    val topic: String? = null,
    val parentCategoryId: Long,
    val position: Int,
    val latestMessageId: Long,
    val slowmode: Int = 0,
    val nsfw: Boolean = false
) : JsonSerializable {

    companion object {
        fun fromNative(native: TextChannel): MessageChannel {
            return MessageChannel(
                id = native.idLong,
                topic = native.topic,
                parentCategoryId = native.parentCategoryIdLong,
                position = native.position,
                latestMessageId = native.latestMessageIdLong,
                slowmode = native.slowmode,
                nsfw = native.isNSFW
            )
        }
    }

}