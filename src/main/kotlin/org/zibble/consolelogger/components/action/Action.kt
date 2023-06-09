package org.zibble.consolelogger.components.action

import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.components.JsonSerializable

abstract class Action(
    val id: Long
) : JsonSerializable {

    companion object {
        val actionTypes = mapOf(
            "assignRole" to AssignRoleAction::class.java,
            "deleteMessage" to DeleteMessageAction::class.java,
            "editMessage" to EditMessageAction::class.java,
            "reactEmote" to ReactEmoteAction::class.java,
            "registerWebhook" to RegisterWebhookClientAction::class.java,
            "removeRole" to RemoveRoleAction::class.java,
            "replyMessage" to ReplyMessageAction::class.java,
            "sendMessage" to SendMessageAction::class.java,
            "sendWebhookMessage" to SendWebhookMessageAction::class.java,
        )
    }

    abstract fun getKey(): String

    abstract fun getName(): String

    abstract fun handle(discordHook: DiscordHook): Boolean

}