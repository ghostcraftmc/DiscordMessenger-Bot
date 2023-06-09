package org.zibble.consolelogger.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.zibble.consolelogger.components.Component
import org.zibble.consolelogger.components.entity.*

class ComponentAdaptor : TypeAdapter<Component>() {

    override fun write(out: JsonWriter, value: Component) {
        out.beginObject()
        out.name("type").value(value.getType().name)
        if (value is Button) {
            out.name("style").value(value.style.name)
            out.name("label").value(value.label)
            out.name("custom_id").value(value.custom_id)
            out.name("url").value(value.url)
            out.name("disabled").value(value.disabled)
            out.name("emoji")
            if (value.emoji == null) {
                out.nullValue()
            } else {
                out.beginObject()
                out.name("name").value(value.emoji.name)
                out.name("id").value(value.emoji.id)
                out.name("animated").value(value.emoji.animated)
                out.endObject()
            }
        } else {
            value as SelectMenu
            out.name("id").value(value.id)
            out.name("placeholder").value(value.placeholder)
            out.name("minValues").value(value.minValues)
            out.name("maxValues").value(value.maxValues)
            out.name("disabled").value(value.disabled)
            out.name("options")
            out.beginArray()
            for (option in value.options) {
                out.beginObject()
                out.name("label").value(option.label)
                out.name("value").value(option.value)
                out.name("description").value(option.description)
                out.name("default").value(option.default)
                out.name("emoji")
                if (option.emoji == null) {
                    out.nullValue()
                } else {
                    out.beginObject()
                    out.name("name").value(option.emoji.name)
                    out.name("id").value(option.emoji.id)
                    out.name("animated").value(option.emoji.animated)
                    out.endObject()
                }
                out.endObject()
            }
            out.endArray()
        }
        out.endObject()
    }

    override fun read(input: JsonReader): Component {
        input.beginObject()
        input.nextName()
        val type = Component.Type.valueOf(input.nextString())
        val component: Component
        if (type == Component.Type.BUTTON) {
            var style: ButtonStyle = ButtonStyle.PRIMARY
            var label = ""
            var custom_id: String? = null
            var url: String? = null
            var disabled = false
            var emoji: Emoji? = null
            while (input.hasNext()) {
                when (input.nextName()) {
                    "style" -> style = ButtonStyle.valueOf(input.nextString())
                    "label" -> label = input.nextString()
                    "custom_id" -> custom_id = getOrNull(input) { input.nextString() }
                    "url" -> url = getOrNull(input) { input.nextString() }
                    "disabled" -> disabled = input.nextBoolean()
                    "emoji" -> emoji = getOrNull(input) {
                        input.beginObject()
                        var name = ""
                        var id = 0L
                        var animated = false
                        while (input.hasNext()) {
                            when (input.nextName()) {
                                "name" -> name = input.nextString()
                                "id" -> id = input.nextLong()
                                "animated" -> animated = input.nextBoolean()
                                else -> input.skipValue()
                            }
                        }
                        input.endObject()
                        Emoji(name, id, animated)
                    }

                    else -> input.skipValue()
                }
            }

            component = Button(style, label, custom_id, url, disabled, emoji)
        } else {
            var id = ""
            var placeholder: String? = null
            var minValues = 0
            var maxValues = 0
            var disabled = false
            val options: MutableList<SelectOption> = mutableListOf()
            while (input.hasNext()) {
                when (input.nextName()) {
                    "id" -> id = input.nextString()
                    "placeholder" -> placeholder = getOrNull(input) { input.nextString() }
                    "minValues" -> minValues = input.nextInt()
                    "maxValues" -> maxValues = input.nextInt()
                    "disabled" -> disabled = input.nextBoolean()
                    "options" -> {
                        input.beginArray()
                        while (input.hasNext()) {
                            input.beginObject()
                            var label = ""
                            var value = ""
                            var description: String? = null
                            var default = false
                            var emoji: Emoji? = null
                            while (input.hasNext()) {
                                when (input.nextName()) {
                                    "label" -> label = input.nextString()
                                    "value" -> value = input.nextString()
                                    "description" -> description = getOrNull(input) { input.nextString() }
                                    "default" -> default = input.nextBoolean()
                                    "emoji" -> emoji = getOrNull(input) {
                                        input.beginObject()
                                        var name = ""
                                        var id = 0L
                                        var animated = false
                                        while (input.hasNext()) {
                                            when (input.nextName()) {
                                                "name" -> name = input.nextString()
                                                "id" -> id = input.nextLong()
                                                "animated" -> animated = input.nextBoolean()
                                                else -> input.skipValue()
                                            }
                                        }
                                        input.endObject()
                                        Emoji(name, id, animated)
                                    }

                                    else -> input.skipValue()
                                }
                            }
                            input.endObject()
                            options.add(SelectOption(label, value, description, default, emoji))
                        }
                        input.endArray()
                    }

                    else -> input.skipValue()
                }
            }

            component = SelectMenu(id, placeholder, minValues, maxValues, disabled, options)
        }

        input.endObject()
        return component
    }

    fun <T> getOrNull(input: JsonReader, reader: () -> T): T? {
        return if (input.peek() == JsonToken.NULL) {
            input.skipValue()
            null
        } else {
            reader()
        }
    }

}