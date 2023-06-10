package org.zibble.consolelogger.components.action.sendable

import org.zibble.consolelogger.components.action.SendableAction
import org.zibble.consolelogger.components.entity.User
import org.zibble.consolelogger.components.readable.DiscordMessage

data class ChannelMessageAction(
    val channelId: Long,
    val user: User,
    val message: DiscordMessage
) : SendableAction("channelMessageAction", "Channel Message")