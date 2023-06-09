package org.zibble.consolelogger.components.readable

import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.entity.SelectMenu

data class SelectMenuReply(
    val button: SelectMenu,
    val discordMessage: DiscordMessage,
    val ephemeral: Boolean
) : JsonSerializable