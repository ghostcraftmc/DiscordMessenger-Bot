package org.zibble.consolelogger.components.entity

import org.zibble.consolelogger.components.Component

data class SelectMenu(
    val id: String,
    val placeholder: String? = null,
    val minValues: Int,
    val maxValues: Int,
    val disabled: Boolean = false,
    val options: List<SelectOption>
) : Component {

    companion object {
        fun fromNative(menu: net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu): SelectMenu {
            return SelectMenu(
                menu.id!!,
                menu.placeholder,
                menu.minValues,
                menu.maxValues,
                menu.isDisabled,
                menu.options.map { SelectOption.fromNative(it) }
            )
        }
    }

    override fun getType(): Component.Type = Component.Type.SELECT_MENU

    override fun toNative(): net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu {
        return net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu.create(id)
            .setPlaceholder(placeholder)
            .setMinValues(minValues)
            .setMaxValues(maxValues)
            .setDisabled(disabled)
            .addOptions(options.map { it.toNative() })
            .build()
    }

}