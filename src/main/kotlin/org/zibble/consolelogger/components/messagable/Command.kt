package org.zibble.consolelogger.components.messagable

import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.entity.MessageChannel
import org.zibble.consolelogger.components.entity.User
import java.time.OffsetDateTime

interface Command : JsonSerializable {

    fun getId(): Long

    fun getChannel(): MessageChannel

    fun getUser(): User

    fun getSentTime(): OffsetDateTime

    fun getPublicPermission(): Long

}