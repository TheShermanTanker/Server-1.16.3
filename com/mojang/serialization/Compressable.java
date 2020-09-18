package com.mojang.serialization;

public interface Compressable extends Keyable {
     <T> KeyCompressor<T> compressor(final DynamicOps<T> dynamicOps);
}
