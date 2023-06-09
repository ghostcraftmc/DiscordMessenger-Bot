package org.zibble.consolelogger.components.readable

import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.entity.Button

data class ButtonReply(
    val button: Button,
    val discordMessage: DiscordMessage,
    val ephemeral: Boolean
) : JsonSerializable