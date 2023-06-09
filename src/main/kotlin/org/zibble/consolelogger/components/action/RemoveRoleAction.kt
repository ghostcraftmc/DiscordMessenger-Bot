package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.UserSnowflake
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener

class RemoveRoleAction(
    id: Long,
    val memberId: Long,
    val roleId: Long
) : Action(id) {

    override fun getKey(): String = "removeRole"

    override fun getName(): String = "Remove Role"

    override fun handle(discordHook: DiscordHook): Boolean {
        val role = discordHook.guild.getRoleById(roleId)
        if (role != null) {
            discordHook.guild.removeRoleFromMember(UserSnowflake.fromId(memberId), role).queue {
                RedisListener.sendActionReply(this, JsonObject())
            }
            return true
        }

        return false
    }

}