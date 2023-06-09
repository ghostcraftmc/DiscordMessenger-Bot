package org.zibble.consolelogger.components.messagable

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.entity.MessageChannel
import org.zibble.consolelogger.components.entity.User
import java.time.OffsetDateTime

data class SlashCommand(
    private val channel: MessageChannel,
    val name: String,
    private val id: Long,
    val type: CommandType = CommandType.SLASH,
    private val user: User,
    val subCommandName: String? = null,
    val subCommandGroup: String? = null,
    val optionMappings: List<OptionMapping>,
    private val sentTime: OffsetDateTime,
    private val publicPermission: Long
) : Command {

    companion object {
        fun fromNative(event: SlashCommandInteractionEvent): SlashCommand {
            return SlashCommand(
                channel = MessageChannel.fromNative(event.channel as TextChannel),
                name = event.name,
                id = event.commandIdLong,
                type = CommandType.SLASH,
                user = User.fromNative(
                    event.user,
                    event.member!!,
                    if (event.member == null) emptyList() else event.member!!.roles
                ),
                subCommandName = event.subcommandName,
                subCommandGroup = event.subcommandGroup,
                optionMappings = event.options.map { OptionMapping.fromNative(it) },
                sentTime = event.timeCreated,
                publicPermission = event.guild!!.publicRole.permissionsRaw
            )
        }
    }

    data class OptionMapping(
        val optionType: OptionType,
        val name: String,
        val value: Any
    ) : JsonSerializable {

        companion object {
            fun fromNative(optionMapping: net.dv8tion.jda.api.interactions.commands.OptionMapping): OptionMapping {
                return OptionMapping(
                    OptionType.fromKey(optionMapping.type.key),
                    optionMapping.name,
                    optionMapping.asString
                )
            }
        }

    }

    override fun getId(): Long = id

    override fun getChannel(): MessageChannel = channel

    override fun getUser(): User = user

    override fun getSentTime(): OffsetDateTime = sentTime

    override fun getPublicPermission(): Long = publicPermission

}