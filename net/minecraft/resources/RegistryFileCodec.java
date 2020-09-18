package net.minecraft.resources;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Either;
import java.util.function.Function;
import java.util.List;
import net.minecraft.core.Registry;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public final class RegistryFileCodec<E> implements Codec<Supplier<E>> {
    private final ResourceKey<? extends Registry<E>> registryKey;
    private final Codec<E> elementCodec;
    private final boolean allowInline;
    
    public static <E> RegistryFileCodec<E> create(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        return RegistryFileCodec.<E>create(vj, codec, true);
    }
    
    public static <E> Codec<List<Supplier<E>>> homogeneousList(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec) {
        return Codec.<java.util.List<Object>, java.util.List<Object>>either(RegistryFileCodec.<E>create(vj, codec, false).listOf(), codec.xmap((java.util.function.Function<? super E, ?>)(object -> () -> object), (java.util.function.Function<? super Object, ? extends E>)Supplier::get).listOf()).<List<Supplier<E>>>xmap((java.util.function.Function<? super Either<java.util.List<Object>, java.util.List<Object>>, ? extends List<Supplier<E>>>)(either -> either.<List>map(list -> list, list -> list)), (java.util.function.Function<? super List<Supplier<E>>, ? extends Either<java.util.List<Object>, java.util.List<Object>>>)Either::left);
    }
    
    private static <E> RegistryFileCodec<E> create(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec, final boolean boolean3) {
        return new RegistryFileCodec<E>(vj, codec, boolean3);
    }
    
    private RegistryFileCodec(final ResourceKey<? extends Registry<E>> vj, final Codec<E> codec, final boolean boolean3) {
        this.registryKey = vj;
        this.elementCodec = codec;
        this.allowInline = boolean3;
    }
    
    public <T> DataResult<T> encode(final Supplier<E> supplier, final DynamicOps<T> dynamicOps, final T object) {
        if (dynamicOps instanceof RegistryWriteOps) {
            return ((RegistryWriteOps)dynamicOps).encode(supplier.get(), object, this.registryKey, this.elementCodec);
        }
        return this.elementCodec.<T>encode(supplier.get(), dynamicOps, object);
    }
    
    public <T> DataResult<Pair<Supplier<E>, T>> decode(final DynamicOps<T> dynamicOps, final T object) {
        if (dynamicOps instanceof RegistryReadOps) {
            return ((RegistryReadOps)dynamicOps).<E>decodeElement(object, this.registryKey, this.elementCodec, this.allowInline);
        }
        return this.elementCodec.<T>decode(dynamicOps, object).<Pair<Supplier<E>, T>>map((java.util.function.Function<? super Pair<Object, T>, ? extends Pair<Supplier<E>, T>>)(pair -> pair.mapFirst(object -> () -> object)));
    }
    
    public String toString() {
        return new StringBuilder().append("RegistryFileCodec[").append(this.registryKey).append(" ").append(this.elementCodec).append("]").toString();
    }
}
