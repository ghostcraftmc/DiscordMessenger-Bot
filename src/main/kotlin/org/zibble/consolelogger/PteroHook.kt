package org.zibble.consolelogger

import com.mattmalec.pterodactyl4j.PteroBuilder
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.PteroClient
import com.mattmalec.pterodactyl4j.client.ws.events.output.OutputEvent
import com.mattmalec.pterodactyl4j.client.ws.hooks.ClientSocketListenerAdapter
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class PteroHook(url: String, token: String, servers: List<Config.Servers>) {

    companion object {
        lateinit var pteroHook: PteroHook
    }

    val client: PteroClient
    private val servers: MutableMap<String, ClientServer> = HashMap()

    init {
        pteroHook = this
        client = PteroBuilder.createClient(url, token)
        for (server in servers) {
            this.servers[server.channelID!!] = client.retrieveServerByIdentifier(server.serverID).execute()
        }
    }

    fun buildWebSocket() {
        for (entry: Map.Entry<String, ClientServer> in servers) {
            entry.value.webSocketBuilder.addEventListeners(PteroListener(entry.key, entry.value)).build()
        }
    }

    fun sendCommand(server: ClientServer?, command: String?) {
        server?.sendCommand(command)?.executeAsync()
    }

    fun getServerForChannel(channelId: String): ClientServer? {
        return servers[channelId]
    }

    class PteroListener(private val channel: String, private val server: ClientServer) : ClientSocketListenerAdapter() {

        private val queue: Queue<String> = ArrayDeque()
        private var length = 0
        private var lastSent = Instant.now()

        override fun onOutput(event: OutputEvent) {
            val split = event.line.split(">\\.\\.\\.\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var line = split[split.size - 1]
            line = line.substring(4).replace("\u001b[m", "\u001b[0m")
            if (length + line.length >= 1990) {
                sendQueued()
            }
            queue.add(line)
            length += line.length
            if (lastSent.until(Instant.now(), ChronoUnit.SECONDS) >= 2) {
                sendQueued()
            }
        }

        private fun sendQueued() {
            lastSent = Instant.now()
            val builder = StringBuilder()
            while (!queue.isEmpty()) {
                builder.append(queue.poll())
                    .append("\n")
            }
            length = 0
            val message = MessageCreateBuilder()
                .addContent("```ansi\n")
                .addContent(builder.toString())
                .addContent("```")
                .build()
            DiscordHook.discordHook.sendMessage(channel, message)
        }
    }
}