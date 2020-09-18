package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializationContext {
    JsonElement serialize(final Object object);
    
    JsonElement serialize(final Object object, final Type type);
}
