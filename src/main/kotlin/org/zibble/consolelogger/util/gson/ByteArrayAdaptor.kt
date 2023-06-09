package org.zibble.consolelogger.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.util.*

class ByteArrayAdaptor : TypeAdapter<ByteArray>() {

    override fun write(out: JsonWriter, value: ByteArray?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(String(Base64.getEncoder().encode(value)))
    }

    override fun read(`in`: JsonReader): ByteArray? {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.skipValue()
            return null
        }
        return Base64.getDecoder().decode(`in`.nextString().toByteArray())
    }

}