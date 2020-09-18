package com.mojang.serialization;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.stream.Stream;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public final class KeyCompressor<T> {
    private final Int2ObjectMap<T> decompress;
    private final Object2IntMap<T> compress;
    private final Object2IntMap<String> compressString;
    private final int size;
    private final DynamicOps<T> ops;
    
    public KeyCompressor(final DynamicOps<T> ops, final Stream<T> keyStream) {
        this.decompress = new Int2ObjectArrayMap<T>();
        this.compress = new Object2IntArrayMap<T>();
        this.compressString = new Object2IntArrayMap<String>();
        this.ops = ops;
        this.compressString.defaultReturnValue(-1);
        keyStream.forEach(key -> {
            if (this.compress.containsKey(key)) {
                return;
            }
            final int next = this.compress.size();
            this.compress.put((T)key, next);
            ops.getStringValue(key).result().ifPresent(k -> this.compressString.put(k, next));
            this.decompress.put(next, (T)key);
        });
        this.size = this.compress.size();
    }
    
    public T decompress(final int key) {
        return this.decompress.get(key);
    }
    
    public int compress(final String key) {
        final int id = this.compressString.getInt(key);
        return (id == -1) ? this.compress(this.ops.createString(key)) : id;
    }
    
    public int compress(final T key) {
        return (int)this.compress.get(key);
    }
    
    public int size() {
        return this.size;
    }
}
