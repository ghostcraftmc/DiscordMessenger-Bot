package org.zibble.consolelogger.components.action

import com.google.gson.JsonObject
import org.zibble.consolelogger.DiscordHook
import org.zibble.consolelogger.RedisListener

class RegisterWebhookClientAction(
    id: Long,
    val webhookUrl: WebhookUrl
) : Action(id) {

    override fun getKey(): String = "registerWebhook"

    override fun getName(): String = "Register Webhook Client"

    override fun handle(discordHook: DiscordHook): Boolean {
        if (discordHook.registerWebhook(webhookUrl)) {
            RedisListener.sendActionReply(this, JsonObject().apply {
                addProperty("success", true)
                addProperty("message", "Successfully registered webhook client.")
            })
        } else {
            RedisListener.sendActionReply(this, JsonObject().apply {
                addProperty("success", false)
                addProperty("message", "Webhook Client already exists.")
            })
        }
        return true
    }

}