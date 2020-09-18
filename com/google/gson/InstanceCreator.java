package com.google.gson;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {
    T createInstance(final Type type);
}