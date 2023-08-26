package util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId

class ObjectIdSerializer: JsonSerializer<ObjectId>() {

    override fun serialize(value: ObjectId, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeString(value.toString())
    }
}

class ObjectIdDeserializer: JsonDeserializer<ObjectId>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ObjectId {
        val objectIdString = p.text.trim()
        return ObjectId(objectIdString)
    }

}