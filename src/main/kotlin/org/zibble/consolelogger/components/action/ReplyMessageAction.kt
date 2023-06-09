package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.readable.DiscordMessage

class ReplyMessageAction(
    id: Long,
    val channelId: Long,
    val messageId: Long,
    val message: DiscordMessage
) : Action(id) {

    override fun getKey(): String = "replyMessage"

    override fun getName(): String = "Reply Message"

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getTextChannelById(channelId)?.let {
            it.retrieveMessageById(messageId).queue { msg ->
                msg.reply(message.toNative()).queue {
                    RedisListener.sendActionReply(this, JsonObject().apply {
                        addProperty("id", it.id)
                    })
                }
            }

            return true
        }

        return false
    }

}