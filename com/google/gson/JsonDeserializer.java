package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializer<T> {
    T deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException;
}
