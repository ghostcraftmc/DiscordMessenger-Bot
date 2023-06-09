package org.zibble.consolelogger.components.messagable

import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.entity.MessageChannel
import org.zibble.consolelogger.components.entity.SelectMenu
import org.zibble.consolelogger.components.entity.SelectOption
import org.zibble.consolelogger.components.entity.User

data class SelectMenuInteraction(
    val menu: SelectMenu,
    val user: User,
    val channel: MessageChannel,
    val messageId: Long,
    val selectedOptions: List<SelectOption>
) : JsonSerializable