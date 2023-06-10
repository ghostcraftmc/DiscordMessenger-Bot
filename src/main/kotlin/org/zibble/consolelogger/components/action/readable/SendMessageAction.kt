package org.zibble.consolelogger.components.action.readable

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.action.ReadableAction
import org.zibble.consolelogger.components.action.sendable.ActionReplyAction
import org.zibble.consolelogger.components.readable.DiscordMessage

class SendMessageAction(
    id: Long,
    val channelId: Long,
    val message: DiscordMessage
) : ReadableAction(id, "sendMessage", "Send Message") {

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.jda.getTextChannelById(channelId)?.let {
            it.sendMessage(message.toNative()).queue { msg ->
                RedisListener.sendAction(ActionReplyAction(id, JsonObject().apply {
                    addProperty("id", msg.id)
                }))
            }
            return true
        }

        return false
    }

}