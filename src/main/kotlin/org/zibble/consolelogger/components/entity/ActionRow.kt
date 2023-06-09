package org.zibble.consolelogger.components.entity

import net.dv8tion.jda.api.interactions.components.ActionRow
import org.zibble.consolelogger.components.Component
import org.zibble.consolelogger.components.JsonSerializable
import org.zibble.consolelogger.components.NativeSerializer

data class ActionRow(
    val components: List<Component>
) : JsonSerializable, NativeSerializer<ActionRow> {

    override fun toNative(): ActionRow {
        return ActionRow.of(components.map { it.toNative() })
    }

}