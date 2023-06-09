package org.zibble.consolelogger.components

interface NativeSerializer<T> {

    fun toNative(): T

}

interface WebhookSerializable<T> {

    fun toWebhookEntity(): T

}