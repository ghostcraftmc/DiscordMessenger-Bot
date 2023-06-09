package org.zibble.consolelogger.components.entity

import org.zibble.consolelogger.components.JsonSerializable
import java.util.*

data class Role(
    val id: Long,
    val name: String,
    val rawPermissions: Long,
    val color: Int,
    val position: Int
) : JsonSerializable {

    companion object {
        fun fromNative(role: net.dv8tion.jda.api.entities.Role): Role {
            return Role(role.idLong, role.name, role.permissionsRaw, role.colorRaw, role.position)
        }
    }

    fun getPermissions(): EnumSet<Permission> {
        return Permission.getPermissions(rawPermissions)
    }

    fun hasPermission(vararg permissions: Permission): Boolean {
        val effectivePerms = rawPermissions or Permission.PUBLIC_PERMISSION_RAW.toLong()
        for (perm in permissions) {
            if (effectivePerms and perm.raw != perm.raw) return false
        }
        return true
    }

}