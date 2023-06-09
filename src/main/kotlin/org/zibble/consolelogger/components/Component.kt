package org.zibble.consolelogger.components

import net.dv8tion.jda.api.interactions.components.ItemComponent

interface Component : JsonSerializable, NativeSerializer<ItemComponent> {

    fun getType(): Type

    enum class Type(val id: Int, val maxPerRow: Int) {
        BUTTON(2, 5),
        SELECT_MENU(3, 1),
        ;
    }

}