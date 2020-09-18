package com.google.gson;

import com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
     <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken);
}
