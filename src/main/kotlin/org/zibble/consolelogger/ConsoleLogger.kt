package org.zibble.consolelogger

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.lettuce.core.RedisClient
import org.zibble.consolelogger.components.Component
import org.zibble.consolelogger.util.gson.ByteArrayAdaptor
import org.zibble.consolelogger.util.gson.ColorAdaptor
import org.zibble.consolelogger.util.gson.ComponentAdaptor
import org.zibble.consolelogger.util.gson.OffsetDateTimeAdaptor
import java.awt.Color
import java.io.File
import java.io.FileOutputStream
import java.time.OffsetDateTime

class ConsoleLogger(config: Config) {

    object Constant {
        val GSON: Gson = GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, OffsetDateTimeAdaptor())
            .registerTypeAdapter(Color::class.java, ColorAdaptor())
            .registerTypeAdapter(ByteArray::class.java, ByteArrayAdaptor())
            .registerTypeAdapter(Component::class.java, ComponentAdaptor())
            .serializeNulls()
            .setLenient()
            .create()
        const val REDIS_CHANNEL = "discord-logger-v2"
        lateinit var instance: ConsoleLogger
    }

    val user = if (config.redis.username.isNullOrBlank()) {
        config.redis.password
    } else {
        config.redis.username + ":" + config.redis.password
    }
    val redis: RedisClient =
        RedisClient.create("redis://$user@${config.redis.host}:${config.redis.port}")
    val discordHook: DiscordHook = DiscordHook(
        config.discordToken!!,
        config.servers,
        config.prefix!!,
        config.guildId!!,
        config.listenableCommands,
        config.legacyCommands,
        config.buttons,
        config.selectMenu
    )

    lateinit var pteroHook: PteroHook

    init {
        val connection = redis.connectPubSub()
        println("Subscribing to redis channel: ${Constant.REDIS_CHANNEL}")
        connection.addListener(RedisListener())
        connection.sync().subscribe(Constant.REDIS_CHANNEL)
    }

    fun registerPteroHook(config: Config) {
        pteroHook = PteroHook(
            config.pteroURL!!,
            config.pteroToken!!,
            config.servers
        )

        pteroHook.buildWebSocket()
    }

}

fun main() {
    val file = File("config.json")
    if (!file.exists()) {
        file.createNewFile()
        FileOutputStream(file).use { fos ->
            ConsoleLogger::class.java.getResourceAsStream("/config.json")!!.use {
                fos.write(it.readBytes())
            }
        }
    }
    val config = ConsoleLogger.Constant.GSON.fromJson(file.readText(), Config::class.java)
    ConsoleLogger.Constant.instance = ConsoleLogger(config)

    if (!config.pteroToken.isNullOrBlank()) {
        ConsoleLogger.Constant.instance.registerPteroHook(config)
    }

    println("BOT STARTED!!!")
}