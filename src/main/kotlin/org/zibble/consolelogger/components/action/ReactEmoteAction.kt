package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.entity.Emoji

class ReactEmoteAction(
    id: Long,
    val channelId: Long,
    val messageId: Long,
    val emote: Emoji
) : Action(id) {

    override fun getKey(): String = "reactEmote"

    override fun getName(): String = "React Emote"

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.guild.getGuildChannelById(channelId)?.let { channel ->
            channel as TextChannel
            channel.addReactionById(messageId, emote.toNative()).queue {
                RedisListener.sendActionReply(this, JsonObject())
            }
            return true
//            if (emote.unicode) {
//                channel.addReactionById(messageId, emote.value).queue {
//                    RedisListener.sendActionReply(this, JsonObject())
//                }
//                return true
//            } else {
//                var jdaEmote = jda.getEmoteById(emote.value)
//                if (jdaEmote != null) {
//                    addReaction(channel, messageId, jdaEmote)
//                    return true
//                } else {
//                    if (guild.idLong == emote.guildId) {
//                        jdaEmote = guild.getEmoteById(emote.value)
//                        if (jdaEmote != null) {
//                            addReaction(channel, messageId, jdaEmote)
//                            return true
//                        } else {
//                            return false
//                        }
//                    } else {
//                        jdaEmote = jda.getGuildById(emote.guildId)?.getEmoteById(emote.value)
//                        if (jdaEmote != null) {
//                            addReaction(channel, messageId, jdaEmote)
//                            return true
//                        } else {
//                            return false
//                        }
//                    }
//                }
//            }
        }

        return false
    }

}