package org.zibble.consolelogger.components.entity

import net.dv8tion.jda.api.interactions.components.selections.SelectOption
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.NativeSerializer
import org.zibble.consolelogger.util.asEmoji

data class SelectOption(
    val label: String,
    val value: String,
    val description: String? = null,
    val default: Boolean = false,
    val emoji: Emoji? = null
) : JsonSerializable, NativeSerializer<SelectOption> {

    companion object {
        const val LABEL_MAX_LENGTH = 100
        const val VALUE_MAX_LENGTH = 100
        const val DESCRIPTION_MAX_LENGTH = 100

        fun fromNative(option: SelectOption): org.zibble.consolelogger.components.entity.SelectOption {
            return SelectOption(
                option.label,
                option.value,
                option.description,
                option.isDefault,
                option.emoji?.asEmoji()
            )
        }
    }

    override fun toNative(): SelectOption {
        return SelectOption.of(label, value)
            .withDescription(description)
            .withDefault(default)
            .withEmoji(emoji?.toNative())
    }

}