package org.zibble.consolelogger.components.entity

enum class ButtonStyle(val id: Int) {

    UNKNOWN(-1),
    PRIMARY(1),
    SECONDARY(2),
    SUCCESS(3),
    DANGER(4),
    LINK(5),
    ;

    companion object {
        fun fromKey(key: Int): ButtonStyle {
            return ButtonStyle.values().firstOrNull { it.id == key } ?: UNKNOWN
        }
    }

}