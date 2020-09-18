package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T> {
    JsonElement serialize(final T object, final Type type, final JsonSerializationContext jsonSerializationContext);
}
