package net.minecraft.core;

import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.RegistryDataPackCodec;
import java.util.List;
import com.mojang.serialization.Codec;
import java.util.Collection;
import java.util.Random;
import java.util.Collections;
import java.util.Set;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import java.util.Objects;
import java.util.Iterator;
import java.util.Optional;
import javax.annotation.Nullable;
import java.util.OptionalInt;
import org.apache.commons.lang3.Validate;
import com.mojang.datafixers.kinds.App;
import java.util.function.Function;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.MapCodec;
import com.google.common.collect.Maps;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenCustomHashMap;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.mojang.serialization.Lifecycle;
import java.util.Map;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import com.google.common.collect.BiMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.apache.logging.log4j.Logger;

public class MappedRegistry<T> extends WritableRegistry<T> {
    protected static final Logger LOGGER;
    private final ObjectList<T> byId;
    private final Object2IntMap<T> toId;
    private final BiMap<ResourceLocation, T> storage;
    private final BiMap<ResourceKey<T>, T> keyStorage;
    private final Map<T, Lifecycle> lifecycles;
    private Lifecycle elementsLifecycle;
    protected Object[] randomCache;
    private int nextId;
    
    public MappedRegistry(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle) {
        super(vj, lifecycle);
        this.byId = new ObjectArrayList<T>(256);
        (this.toId = new Object2IntOpenCustomHashMap<T>(Util.<T>identityStrategy())).defaultReturnValue(-1);
        this.storage = HashBiMap.create();
        this.keyStorage = HashBiMap.create();
        this.lifecycles = Maps.newIdentityHashMap();
        this.elementsLifecycle = lifecycle;
    }
    
    public static <T> MapCodec<RegistryEntry<T>> withNameAndId(final ResourceKey<? extends Registry<T>> vj, final MapCodec<T> mapCodec) {
        return RecordCodecBuilder.<RegistryEntry<T>>mapCodec((java.util.function.Function<RecordCodecBuilder.Instance<RegistryEntry<T>>, ? extends App<RecordCodecBuilder.Mu<RegistryEntry<T>>, RegistryEntry<T>>>)(instance -> instance.<ResourceKey, Integer, Object>group(ResourceLocation.CODEC.xmap((java.util.function.Function<? super ResourceLocation, ?>)ResourceKey.elementKey(vj), (java.util.function.Function<? super Object, ? extends ResourceLocation>)ResourceKey::location).fieldOf("name").forGetter((java.util.function.Function<Object, Object>)(a -> a.key)), Codec.INT.fieldOf("id").forGetter((java.util.function.Function<Object, Object>)(a -> a.id)), (App<Object, Object>)mapCodec.forGetter(a -> a.value)).<RegistryEntry>apply(instance, RegistryEntry::new)));
    }
    
    @Override
    public <V extends T> V registerMapping(final int integer, final ResourceKey<T> vj, final V object, final Lifecycle lifecycle) {
        return this.<V>registerMapping(integer, vj, object, lifecycle, true);
    }
    
    private <V extends T> V registerMapping(final int integer, final ResourceKey<T> vj, final V object, final Lifecycle lifecycle, final boolean boolean5) {
        Validate.<ResourceKey<T>>notNull(vj);
        Validate.<V>notNull(object);
        this.byId.size(Math.max(this.byId.size(), integer + 1));
        this.byId.set(integer, object);
        this.toId.put(object, integer);
        this.randomCache = null;
        if (boolean5 && this.keyStorage.containsKey(vj)) {
            MappedRegistry.LOGGER.debug("Adding duplicate key '{}' to registry", vj);
        }
        if (this.storage.containsValue(object)) {
            MappedRegistry.LOGGER.error("Adding duplicate value '{}' to registry", object);
        }
        this.storage.put(vj.location(), object);
        this.keyStorage.put(vj, object);
        this.lifecycles.put(object, lifecycle);
        this.elementsLifecycle = this.elementsLifecycle.add(lifecycle);
        if (this.nextId <= integer) {
            this.nextId = integer + 1;
        }
        return object;
    }
    
    @Override
    public <V extends T> V register(final ResourceKey<T> vj, final V object, final Lifecycle lifecycle) {
        return this.<V>registerMapping(this.nextId, vj, object, lifecycle);
    }
    
    @Override
    public <V extends T> V registerOrOverride(final OptionalInt optionalInt, final ResourceKey<T> vj, final V object, final Lifecycle lifecycle) {
        Validate.<ResourceKey<T>>notNull(vj);
        Validate.<V>notNull(object);
        final T object2 = (T)this.keyStorage.get(vj);
        int integer7;
        if (object2 == null) {
            integer7 = (optionalInt.isPresent() ? optionalInt.getAsInt() : this.nextId);
        }
        else {
            integer7 = this.toId.getInt(object2);
            if (optionalInt.isPresent() && optionalInt.getAsInt() != integer7) {
                throw new IllegalStateException("ID mismatch");
            }
            this.toId.removeInt(object2);
            this.lifecycles.remove(object2);
        }
        return this.<V>registerMapping(integer7, vj, object, lifecycle, false);
    }
    
    @Nullable
    @Override
    public ResourceLocation getKey(final T object) {
        return (ResourceLocation)this.storage.inverse().get(object);
    }
    
    @Override
    public Optional<ResourceKey<T>> getResourceKey(final T object) {
        return (Optional<ResourceKey<T>>)Optional.ofNullable(this.keyStorage.inverse().get(object));
    }
    
    @Override
    public int getId(@Nullable final T object) {
        return this.toId.getInt(object);
    }
    
    @Nullable
    @Override
    public T get(@Nullable final ResourceKey<T> vj) {
        return (T)this.keyStorage.get(vj);
    }
    
    @Nullable
    public T byId(final int integer) {
        if (integer < 0 || integer >= this.byId.size()) {
            return null;
        }
        return (T)this.byId.get(integer);
    }
    
    public Lifecycle lifecycle(final T object) {
        return (Lifecycle)this.lifecycles.get(object);
    }
    
    @Override
    public Lifecycle elementsLifecycle() {
        return this.elementsLifecycle;
    }
    
    public Iterator<T> iterator() {
        return Iterators.filter((java.util.Iterator<Object>)this.byId.iterator(), Objects::nonNull);
    }
    
    @Nullable
    @Override
    public T get(@Nullable final ResourceLocation vk) {
        return (T)this.storage.get(vk);
    }
    
    @Override
    public Set<ResourceLocation> keySet() {
        return (Set<ResourceLocation>)Collections.unmodifiableSet(this.storage.keySet());
    }
    
    @Override
    public Set<Map.Entry<ResourceKey<T>, T>> entrySet() {
        return (Set<Map.Entry<ResourceKey<T>, T>>)Collections.unmodifiableMap((Map)this.keyStorage).entrySet();
    }
    
    @Nullable
    public T getRandom(final Random random) {
        if (this.randomCache == null) {
            final Collection<?> collection3 = this.storage.values();
            if (collection3.isEmpty()) {
                return null;
            }
            this.randomCache = collection3.toArray(new Object[collection3.size()]);
        }
        return Util.<T>getRandom(this.randomCache, random);
    }
    
    public static <T> Codec<MappedRegistry<T>> networkCodec(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle, final Codec<T> codec) {
        return MappedRegistry.<T>withNameAndId(vj, codec.fieldOf("element")).codec().listOf().<MappedRegistry<T>>xmap((java.util.function.Function<? super java.util.List<RegistryEntry<T>>, ? extends MappedRegistry<T>>)(list -> {
            final MappedRegistry<T> gi4 = new MappedRegistry<T>(vj, lifecycle);
            for (final RegistryEntry<T> a6 : list) {
                gi4.<T>registerMapping(a6.id, a6.key, a6.value, lifecycle);
            }
            return gi4;
        }), (java.util.function.Function<? super MappedRegistry<T>, ? extends java.util.List<RegistryEntry<T>>>)(gi -> {
            final ImmutableList.Builder<RegistryEntry<T>> builder2 = ImmutableList.<RegistryEntry<T>>builder();
            for (final T object4 : gi) {
                builder2.add(new RegistryEntry<T>((ResourceKey<T>)gi.getResourceKey(object4).get(), gi.getId(object4), object4));
            }
            return builder2.build();
        }));
    }
    
    public static <T> Codec<MappedRegistry<T>> dataPackCodec(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle, final Codec<T> codec) {
        return RegistryDataPackCodec.<T>create(vj, lifecycle, codec);
    }
    
    public static <T> Codec<MappedRegistry<T>> directCodec(final ResourceKey<? extends Registry<T>> vj, final Lifecycle lifecycle, final Codec<T> codec) {
        return Codec.<Object, T>unboundedMap(ResourceLocation.CODEC.xmap((java.util.function.Function<? super ResourceLocation, ?>)ResourceKey.elementKey(vj), (java.util.function.Function<? super Object, ? extends ResourceLocation>)ResourceKey::location), codec).<MappedRegistry<T>>xmap((java.util.function.Function<? super Object, ? extends MappedRegistry<T>>)(map -> {
            final MappedRegistry<T> gi4 = new MappedRegistry<T>(vj, lifecycle);
            map.forEach((vj, object) -> gi4.register(vj, object, lifecycle));
            return gi4;
        }), (java.util.function.Function<? super MappedRegistry<T>, ?>)(gi -> ImmutableMap.copyOf((java.util.Map<?, ?>)gi.keyStorage)));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class RegistryEntry<T> {
        public final ResourceKey<T> key;
        public final int id;
        public final T value;
        
        public RegistryEntry(final ResourceKey<T> vj, final int integer, final T object) {
            this.key = vj;
            this.id = integer;
            this.value = object;
        }
    }
}
