package org.zibble.consolelogger.components

import com.google.gson.JsonElement

interface JsonSerializable {

    fun toJson(): JsonElement = org.zibble.consolelogger.ConsoleLogger.Constant.GSON.toJsonTree(this)

    fun toJsonString(): String = org.zibble.consolelogger.ConsoleLogger.Constant.GSON.toJson(this)

}