package org.zibble.consolelogger.components.action.readable

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.action.ReadableAction
import org.zibble.consolelogger.components.action.sendable.ActionReplyAction

class DeleteMessageAction(
    id: Long,
    val channelId: Long,
    val messageId: Long
) : ReadableAction(id, "deleteMessage", "Delete Message") {

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getGuildChannelById(channelId)?.let { channel ->
            channel as TextChannel
            channel.deleteMessageById(messageId).queue {
                RedisListener.sendAction(ActionReplyAction(id, JsonObject()))
            }
            return true
        }

        return false
    }
}