package net.minecraft.resources;

import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Registry;
import net.minecraft.core.MappedRegistry;
import com.mojang.serialization.Codec;

public final class RegistryDataPackCodec<E> implements Codec<MappedRegistry<E>> {
    private final Codec<MappedRegistry<E>> directCodec;
    private final ResourceKey<? extends Registry<E>> registryKey;
    private final Codec<E> elementCodec;
    
    public static <E> RegistryDataPackCodec<E> create(final ResourceKey<? extends Registry<E>> vj, final Lifecycle lifecycle, final Codec<E> codec) {
        return new RegistryDataPackCodec<E>(vj, lifecycle, codec);
    }
    
    private RegistryDataPackCodec(final ResourceKey<? extends Registry<E>> vj, final Lifecycle lifecycle, final Codec<E> codec) {
        this.directCodec = MappedRegistry.<E>directCodec(vj, lifecycle, codec);
        this.registryKey = vj;
        this.elementCodec = codec;
    }
    
    public <T> DataResult<T> encode(final MappedRegistry<E> gi, final DynamicOps<T> dynamicOps, final T object) {
        return this.directCodec.<T>encode(gi, dynamicOps, object);
    }
    
    public <T> DataResult<Pair<MappedRegistry<E>, T>> decode(final DynamicOps<T> dynamicOps, final T object) {
        final DataResult<Pair<MappedRegistry<E>, T>> dataResult4 = this.directCodec.<T>decode(dynamicOps, object);
        if (dynamicOps instanceof RegistryReadOps) {
            return dataResult4.<Pair<MappedRegistry<E>, T>>flatMap((java.util.function.Function<? super Pair<MappedRegistry<E>, T>, ? extends DataResult<Pair<MappedRegistry<E>, T>>>)(pair -> ((RegistryReadOps)dynamicOps).<E>decodeElements(pair.getFirst(), this.registryKey, this.elementCodec).map((java.util.function.Function<? super MappedRegistry<E>, ?>)(gi -> Pair.<MappedRegistry, Object>of(gi, pair.getSecond())))));
        }
        return dataResult4;
    }
    
    public String toString() {
        return new StringBuilder().append("RegistryDataPackCodec[").append(this.directCodec).append(" ").append(this.registryKey).append(" ").append(this.elementCodec).append("]").toString();
    }
}
