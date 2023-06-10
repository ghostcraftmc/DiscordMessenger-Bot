package org.zibble.consolelogger.components.action.sendable

import com.google.gson.JsonObject
import org.zibble.consolelogger.components.action.SendableAction

data class ActionReplyAction(
    val actionId: Long,
    val content: JsonObject
) : SendableAction("actionReply", "Action Reply")