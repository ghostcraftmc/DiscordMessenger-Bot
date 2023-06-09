package org.zibble.consolelogger.components.entity

import net.dv8tion.jda.api.entities.Member
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.util.checkPermission
import java.util.*

data class User(
    val id: Long,
    val discriminator: String,
    val name: String,
    val avatarId: String?,
    val bot: Boolean,
    val owner: Boolean,
    val roles: Collection<Role>,
    val effectivePermission: Long
) : JsonSerializable {

    companion object {
        fun fromNative(
            user: net.dv8tion.jda.api.entities.User,
            member: Member,
            roles: Collection<net.dv8tion.jda.api.entities.Role>
        ): User {
            return User(
                user.idLong,
                user.discriminator,
                user.name,
                user.avatarId,
                user.isBot,
                member.isOwner,
                roles.map { Role.fromNative(it) },
                net.dv8tion.jda.internal.utils.PermissionUtil.getEffectivePermission(member)
            )
        }
    }

    fun getPermissions(): EnumSet<Permission> {
        return Permission.getPermissions(effectivePermission)
    }

    fun hasPermission(vararg permissions: Permission): Boolean {
        return this.checkPermission(*permissions)
    }

}