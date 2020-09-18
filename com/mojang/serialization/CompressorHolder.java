package com.mojang.serialization;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;

public abstract class CompressorHolder implements Compressable {
    private final Map<DynamicOps<?>, KeyCompressor<?>> compressors;
    
    public CompressorHolder() {
        this.compressors = (Map<DynamicOps<?>, KeyCompressor<?>>)new Object2ObjectArrayMap();
    }
    
    public <T> KeyCompressor<T> compressor(final DynamicOps<T> ops) {
        return (KeyCompressor<T>)this.compressors.computeIfAbsent(ops, k -> new KeyCompressor(ops, this.<T>keys((DynamicOps<T>)ops)));
    }
}
