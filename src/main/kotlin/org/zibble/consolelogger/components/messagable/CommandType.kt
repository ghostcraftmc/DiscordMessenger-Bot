package org.zibble.consolelogger.components.messagable

enum class CommandType(val id: Int) {

    UNKNOWN(-1),
    SLASH(1),
    USER(2),
    MESSAGE(3),
    ;

}