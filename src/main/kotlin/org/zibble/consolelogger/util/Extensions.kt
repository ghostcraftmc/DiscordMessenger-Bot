package org.zibble.consolelogger.util

import net.dv8tion.jda.api.entities.emoji.CustomEmoji
import net.dv8tion.jda.api.entities.emoji.EmojiUnion
import org.zibble.consolelogger.components.entity.Emoji
import org.zibble.consolelogger.components.entity.Permission
import org.zibble.consolelogger.components.entity.User

fun User.checkPermission(vararg permissions: Permission): Boolean {
    val effectivePerms = this.effectivePermission
    return (isApplied(effectivePerms, Permission.ADMINISTRATOR.raw)
            || isApplied(effectivePerms, Permission.getRaw(*permissions)))
}

private fun isApplied(permissions: Long, perms: Long): Boolean {
    return permissions and perms == perms
}

fun EmojiUnion.asEmoji(): Emoji {
    return if (this is CustomEmoji) {
        val emoji = this.asCustom()
        Emoji.fromEmote(emoji.name, emoji.idLong, emoji.isAnimated)
    } else {
        val emoji = this.asUnicode()
        Emoji.fromUnicode(emoji.name)
    }
}