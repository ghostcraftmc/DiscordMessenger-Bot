package org.zibble.consolelogger.components.action.readable

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.action.ReadableAction
import org.zibble.consolelogger.components.action.sendable.ActionReplyAction
import org.zibble.consolelogger.components.readable.DiscordMessage

class EditMessageAction(
    id: Long,
    val channelId: Long,
    val messageId: Long,
    val newMessage: DiscordMessage
) : ReadableAction(id, "editMessage", "Edit Message") {

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getGuildChannelById(channelId)?.let { channel ->
            channel as TextChannel
            channel.editMessageById(messageId, newMessage.toMessageEditData()).queue {
                RedisListener.sendAction(ActionReplyAction(id, JsonObject().apply {
                    addProperty("id", it.id)
                }))
            }
            return true
        }

        return false
    }

}