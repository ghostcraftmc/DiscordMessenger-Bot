package org.zibble.consolelogger.components.action.readable

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.action.ReadableAction
import org.zibble.consolelogger.components.action.sendable.ActionReplyAction
import org.zibble.consolelogger.components.readable.WebhookMessage

class SendWebhookMessageAction(
    id: Long,
    val webhookId: Int,
    val message: WebhookMessage
) : ReadableAction(id, "sendWebhookMessage", "Send Webhook Message") {

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.sendWebhookMessage(webhookId, message.toWebhookEntity()).thenAccept {
            RedisListener.sendAction(ActionReplyAction(id, JsonObject().apply {
                if (it == null) {
                    addProperty("id", -1)
                } else {
                    addProperty("id", it.id)
                }
            }))
        }
        return true
    }

}