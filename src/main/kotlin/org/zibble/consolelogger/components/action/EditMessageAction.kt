package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.readable.DiscordMessage

class EditMessageAction(
    id: Long,
    val channelId: Long,
    val messageId: Long,
    val newMessage: DiscordMessage
) : Action(id) {

    override fun getKey(): String = "editMessage"

    override fun getName(): String = "Edit Message"

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getGuildChannelById(channelId)?.let { channel ->
            channel as TextChannel
            channel.editMessageById(messageId, newMessage.toMessageEditData()).queue {
                RedisListener.sendActionReply(this, JsonObject().apply {
                    addProperty("id", it.id)
                })
            }
            return true
        }

        return false
    }

}