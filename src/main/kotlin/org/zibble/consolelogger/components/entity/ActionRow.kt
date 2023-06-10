package org.zibble.consolelogger.components.entity

import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu
import org.zibble.consolelogger.components.Component
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.NativeSerializer

data class ActionRow(
    val components: List<Component>
) : JsonSerializable, NativeSerializer<ActionRow> {

    companion object {
        fun fromNative(data: ActionRow): org.zibble.consolelogger.components.entity.ActionRow {
            return ActionRow(data.buttons.map {
                Button.fromNative(it)
            } + data.actionComponents.filterIsInstance<StringSelectMenu>()
                .map { SelectMenu.fromNative(it) })
        }
    }

    override fun toNative(): ActionRow {
        return ActionRow.of(components.map { it.toNative() })
    }

}