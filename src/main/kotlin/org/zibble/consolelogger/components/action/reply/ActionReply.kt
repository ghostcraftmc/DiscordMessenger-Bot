package org.zibble.consolelogger.components.action.reply

import com.google.gson.JsonObject
import org.zibble.consolelogger.components.JsonSerializable

data class ActionReply(
    val actionId: Long,
    val content: JsonObject
) : JsonSerializable