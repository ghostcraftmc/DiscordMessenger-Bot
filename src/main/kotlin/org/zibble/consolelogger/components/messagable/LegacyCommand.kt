package org.zibble.consolelogger.components.messagable

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.zibble.consolelogger.components.entity.MessageChannel
import org.zibble.consolelogger.components.entity.User
import java.time.OffsetDateTime

data class LegacyCommand(
    private val id: Long,
    val content: String,
    private val channel: MessageChannel,
    private val user: User,
    val isFromWebhook: Boolean,
    val isMentionsEveryone: Boolean,
    private val sentTime: OffsetDateTime,
    private val publicPermission: Long
) : Command {

    companion object {
        fun fromNative(message: Message): LegacyCommand {
            return LegacyCommand(
                message.idLong,
                message.contentRaw,
                MessageChannel.fromNative(message.channel as TextChannel),
                User.fromNative(
                    message.author,
                    message.member!!,
                    message.member!!.roles
                ),
                message.isWebhookMessage,
                message.mentions.mentionsEveryone(),
                message.timeCreated,
                message.guild.publicRole.permissionsRaw
            )
        }
    }

    override fun getId(): Long = id

    override fun getChannel(): MessageChannel = channel

    override fun getUser(): User = user

    override fun getSentTime(): OffsetDateTime = sentTime

    override fun getPublicPermission(): Long = publicPermission
}