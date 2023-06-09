package org.zibble.consolelogger.components.messagable

import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.entity.Button
import org.zibble.consolelogger.components.entity.MessageChannel
import org.zibble.consolelogger.components.entity.User

data class ButtonInteraction(
    val button: Button,
    val user: User,
    val channel: MessageChannel,
    val messageId: Long
) : JsonSerializable