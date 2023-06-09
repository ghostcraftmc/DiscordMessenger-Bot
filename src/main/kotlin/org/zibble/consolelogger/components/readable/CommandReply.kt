package org.zibble.consolelogger.components.readable

import org.zibble.consolelogger.components.JsonSerializable

data class CommandReply(
    val commandId: Long,
    val message: DiscordMessage,
    val ephemeral: Boolean = false
) : JsonSerializable