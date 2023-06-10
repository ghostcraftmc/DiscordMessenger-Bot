package org.zibble.consolelogger.components.action.readable

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.action.ReadableAction
import org.zibble.consolelogger.components.action.sendable.ActionReplyAction
import org.zibble.consolelogger.components.readable.DiscordMessage

class ReplyMessageAction(
    id: Long,
    val channelId: Long,
    val messageId: Long,
    val message: DiscordMessage
) : ReadableAction(id, "replyMessage", "Reply Message") {

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getTextChannelById(channelId)?.let {
            it.retrieveMessageById(messageId).queue { msg ->
                msg.reply(message.toNative()).queue {
                    RedisListener.sendAction(ActionReplyAction(id, JsonObject().apply {
                        addProperty("id", it.id)
                    }))
                }
            }

            return true
        }

        return false
    }

}