package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializationContext {
     <T> T deserialize(final JsonElement jsonElement, final Type type) throws JsonParseException;
}
