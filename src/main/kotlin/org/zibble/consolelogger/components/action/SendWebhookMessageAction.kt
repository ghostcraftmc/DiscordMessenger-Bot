package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.readable.WebhookMessage

class SendWebhookMessageAction(
    id: Long,
    val webhookUrl: WebhookUrl,
    val message: WebhookMessage
) : Action(id) {

    override fun getKey(): String = "sendWebhookMessage"

    override fun getName(): String = "Send Webhook Message"

    override fun handle(discordHook: DiscordHook): Boolean {
        discordHook.sendWebhookMessage(webhookUrl, message.toWebhookEntity()).thenAccept {
            RedisListener.sendActionReply(this, JsonObject().apply {
                if (it == null) {
                    addProperty("id", -1)
                } else {
                    addProperty("id", it.id)
                }
            })
        }
        return true
    }

}

data class WebhookUrl(
    val url: String
) : JsonSerializable {

    companion object {
        fun of(url: String) : WebhookUrl = WebhookUrl(
            url
        )

        fun of(id: Long, token: String): WebhookUrl = WebhookUrl(
            "https://discord.com/api/webhooks/$id/$token"
        )
    }

}