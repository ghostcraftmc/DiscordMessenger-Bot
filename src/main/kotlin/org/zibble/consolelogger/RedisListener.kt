package org.zibble.consolelogger

import com.google.gson.JsonObject
import io.lettuce.core.api.async.RedisAsyncCommands
import io.lettuce.core.pubsub.RedisPubSubAdapter
import org.zibble.consolelogger.components.action.Action
import org.zibble.consolelogger.components.action.reply.ActionReply
import org.zibble.consolelogger.components.messagable.ButtonInteraction
import org.zibble.consolelogger.components.messagable.Command
import org.zibble.consolelogger.components.messagable.SelectMenuInteraction
import org.zibble.consolelogger.components.readable.ButtonReply
import org.zibble.consolelogger.components.readable.CommandReply
import org.zibble.consolelogger.components.readable.SelectMenuReply

class RedisListener : RedisPubSubAdapter<String, String>() {

    companion object {
        const val COMMAND_REPLY = "commandReply"
        const val COMMAND = "command"
        const val ACTION_REPLY = "actionReply"
        const val BUTTON_INTERACTION = "buttonInteraction"
        const val BUTTON_REPLY = "buttonReply"
        const val SELECT_MENU_INTERACTION = "selectMenuInteraction"
        const val SELECT_MENU_REPLY = "selectMenuReply"

        lateinit var INSTANCE: RedisListener
        val redisAsyncCommands: RedisAsyncCommands<String, String> by lazy {
            ConsoleLogger.Constant.instance.redis.connect().async()
        }

        fun sendMessage(json: JsonObject) {
            sendMessage(json.toString())
        }

        fun sendMessage(message: String) {
            println("Sending message to redis:\n${message}")
            redisAsyncCommands.publish(
                ConsoleLogger.Constant.REDIS_CHANNEL, message
            )
        }

        fun sendCommand(command: Command, callback: (CommandReply) -> Unit) {
            INSTANCE.commandsCallbacks[command.getId()] = callback
            sendMessage(JsonObject().apply {
                add(COMMAND, command.toJson())
            })
        }

        fun sendActionReply(action: Action, reply: JsonObject) {
            sendMessage(JsonObject().apply {
                add(ACTION_REPLY, ActionReply(action.id, reply).toJson())
            })
        }

        fun sendButtonInteraction(interaction: ButtonInteraction, callback: (ButtonReply) -> Unit) {
            INSTANCE.buttonsCallback[interaction.button.custom_id!!] = callback
            sendMessage(JsonObject().apply {
                add(BUTTON_INTERACTION, interaction.toJson())
            })
        }

        fun sendSelectMenuInteraction(interaction: SelectMenuInteraction, callback: (SelectMenuReply) -> Unit) {
            INSTANCE.selectMenuCallback[interaction.menu.id] = callback
            sendMessage(JsonObject().apply {
                add(SELECT_MENU_INTERACTION, interaction.toJson())
            })
        }
    }

    val commandsCallbacks: MutableMap<Long, (CommandReply) -> Unit> = HashMap()
    val buttonsCallback: MutableMap<String, (ButtonReply) -> Unit> = HashMap()
    val selectMenuCallback: MutableMap<String, (SelectMenuReply) -> Unit> = HashMap()

    init {
        INSTANCE = this
    }

    override fun message(channel: String, message: String) {
        if (channel != ConsoleLogger.Constant.REDIS_CHANNEL) return

        val json = ConsoleLogger.Constant.GSON.fromJson(message, JsonObject::class.java)
        if (json.has(COMMAND_REPLY)) {
            println("Received command reply from redis:\n$message")
            ConsoleLogger.Constant.GSON.fromJson(json[COMMAND_REPLY], CommandReply::class.java).let {
                commandsCallbacks.remove(it.commandId)?.invoke(it)
            }
        } else if (json.has(BUTTON_REPLY)) {
            println("Received button reply from redis:\n$message")
            ConsoleLogger.Constant.GSON.fromJson(json[BUTTON_REPLY], ButtonReply::class.java).let {
                buttonsCallback.remove(it.button.custom_id)?.invoke(it)
            }
        } else if (json.has(SELECT_MENU_REPLY)) {
            println("Received select menu reply from redis:\n$message")
            ConsoleLogger.Constant.GSON.fromJson(json[SELECT_MENU_REPLY], SelectMenuReply::class.java).let {
                selectMenuCallback.remove(it.button.id)?.invoke(it)
            }
        } else {
            Action.actionTypes.forEach { (k, v) ->
                if (json.has(k)) {
                    println("Received action from redis:\n$message")
                    val action = ConsoleLogger.Constant.GSON.fromJson(json[k], v)
                    val success = action.handle(DiscordHook.discordHook)

                    if (!success) {
                        println("Error handling action: ${action.getName()}")
                    }
                }
            }
        }
    }

}