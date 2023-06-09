package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener

class DeleteMessageAction(
    id: Long,
    val channelId: Long,
    val messageId: Long
) : Action(id) {

    override fun getKey(): String = "deleteMessage"

    override fun getName(): String = "Delete Message"

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getGuildChannelById(channelId)?.let { channel ->
            channel as TextChannel
            channel.deleteMessageById(messageId).queue {
                RedisListener.sendActionReply(this, JsonObject())
            }
            return true
        }

        return false
    }
}