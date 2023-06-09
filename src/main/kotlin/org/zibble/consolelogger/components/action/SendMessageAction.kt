package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.readable.DiscordMessage

class SendMessageAction(
    id: Long,
    val channelId: Long,
    val message: DiscordMessage
) : Action(id) {

    override fun getKey(): String = "sendMessage"

    override fun getName(): String = "Send Message"

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.jda.getTextChannelById(channelId)?.let {
            it.sendMessage(message.toNative()).queue { msg ->
                RedisListener.sendActionReply(this, JsonObject().apply {
                    addProperty("id", msg.id)
                })
            }
            return true
        }

        return false
    }

}