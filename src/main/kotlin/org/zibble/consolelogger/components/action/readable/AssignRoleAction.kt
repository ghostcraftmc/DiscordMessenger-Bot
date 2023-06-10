package org.zibble.consolelogger.components.action.readable

import com.google.gson.JsonObject
import net.dv8tion.jda.api.entities.UserSnowflake
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.action.ReadableAction
import org.zibble.consolelogger.components.action.sendable.ActionReplyAction

class AssignRoleAction(
    id: Long,
    val memberId: Long,
    val roleId: Long
) : ReadableAction(id, "assignRole", "Assign Role") {

    override fun handle(discordHook: DiscordHook): Boolean {
        val role = discordHook.guild.getRoleById(roleId)
        if (role != null) {
            discordHook.guild.addRoleToMember(UserSnowflake.fromId(memberId), role).queue {
                RedisListener.sendAction(ActionReplyAction(id, JsonObject()))
            }
            return true
        }
        return false
    }
}