package org.zibble.consolelogger.util.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.OffsetDateTime

class OffsetDateTimeAdaptor : TypeAdapter<OffsetDateTime>() {

    override fun write(out: JsonWriter, value: OffsetDateTime?) {
        if (value == null) {
            out.nullValue()
            return
        }
        out.value(value.toString())
    }

    override fun read(`in`: JsonReader): OffsetDateTime? {
        if (`in`.peek() == JsonToken.NULL) {
            `in`.skipValue()
            return null
        }
        return OffsetDateTime.parse(`in`.nextString())
    }

}