package org.zibble.consolelogger.components.entity

import net.dv8tion.jda.api.interactions.components.buttons.Button
import org.zibble.consolelogger.components.Component
import org.zibble.consolelogger.util.asEmoji

data class Button(
    val style: ButtonStyle,
    val label: String = "",
    val custom_id: String? = null,
    val url: String? = null,
    val disabled: Boolean = false,
    val emoji: Emoji? = null
) : Component {

    companion object {
        const val LABEL_MAX_LENGTH = 80
        const val ID_MAX_LENGTH = 100
        const val URL_MAX_LENGTH = 512

        fun fromNative(button: Button): org.zibble.consolelogger.components.entity.Button {
            return Button(
                ButtonStyle.fromKey(button.style.key),
                button.label,
                button.id,
                button.url,
                button.isDisabled,
                button.emoji?.asEmoji()
            )
        }
    }

    override fun toNative(): Button {
        val button = if (style == ButtonStyle.LINK) {
            if (emoji == null) {
                Button.link(url!!, label)
            } else {
                Button.link(url!!, emoji.toNative())
            }
        } else {
            Button.of(
                net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle.fromKey(style.id),
                custom_id!!,
                label,
                emoji?.toNative()
            )
        }

        return button.withDisabled(disabled)
    }

    override fun getType(): Component.Type = Component.Type.BUTTON

}