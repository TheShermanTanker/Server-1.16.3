package com.mojang.serialization;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Keyable {
     <T> Stream<T> keys(final DynamicOps<T> dynamicOps);
    
    default Keyable forStrings(final Supplier<Stream<String>> keys) {
        return new Keyable() {
            public <T> Stream<T> keys(final DynamicOps<T> ops) {
                return (Stream<T>)((Stream)keys.get()).map(ops::createString);
            }
        };
    }
}
