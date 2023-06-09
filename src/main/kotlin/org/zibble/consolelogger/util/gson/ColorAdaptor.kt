package org.zibble.consolelogger.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.awt.Color

class ColorAdaptor : TypeAdapter<Color>() {

    override fun write(out: JsonWriter, value: Color?) {
        if (value == null) {
            out.nullValue()
            return
        }

        out.value(Integer.toHexString(value.rgb))
    }

    override fun read(`in`: JsonReader): Color? {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.skipValue()
            return null
        }
        return Color(Integer.parseUnsignedInt(`in`.nextString(), 16))
    }

}