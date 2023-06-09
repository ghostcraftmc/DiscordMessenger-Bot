package org.zibble.consolelogger.components.messagable

enum class OptionType(
    val key: Int, val supportsChoices: Boolean = false
) {

    UNKNOWN(-1),
    SUB_COMMAND(1),
    SUB_COMMAND_GROUP(2),
    STRING(3, true),
    INTEGER(4, true),
    BOOLEAN(5),
    USER(6),
    CHANNEL(7),
    ROLE(8),
    MENTIONABLE(9),
    NUMBER(10, true),
    ;

    companion object {
        fun fromKey(key: Int): OptionType {
            return OptionType.values().firstOrNull { it.key == key } ?: UNKNOWN
        }
    }
}