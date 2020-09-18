package net.minecraft.world.entity.ai.behavior;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.stream.Stream;
import java.util.Comparator;
import java.util.function.Function;
import com.mojang.serialization.Codec;
import com.google.common.collect.Lists;
import java.util.Random;
import java.util.List;

public class WeightedList<U> {
    protected final List<WeightedEntry<U>> entries;
    private final Random random;
    
    public WeightedList() {
        this((List)Lists.newArrayList());
    }
    
    private WeightedList(final List<WeightedEntry<U>> list) {
        this.random = new Random();
        this.entries = Lists.newArrayList((java.lang.Iterable<?>)list);
    }
    
    public static <U> Codec<WeightedList<U>> codec(final Codec<U> codec) {
        return WeightedEntry.<U>codec(codec).listOf().<WeightedList<U>>xmap((java.util.function.Function<? super java.util.List<WeightedEntry<U>>, ? extends WeightedList<U>>)WeightedList::new, (java.util.function.Function<? super WeightedList<U>, ? extends java.util.List<WeightedEntry<U>>>)(aum -> aum.entries));
    }
    
    public WeightedList<U> add(final U object, final int integer) {
        this.entries.add(new WeightedEntry((Object)object, integer));
        return this;
    }
    
    public WeightedList<U> shuffle() {
        return this.shuffle(this.random);
    }
    
    public WeightedList<U> shuffle(final Random random) {
        this.entries.forEach(a -> a.setRandom(random.nextFloat()));
        this.entries.sort(Comparator.comparingDouble(object -> ((WeightedEntry<Object>)object).getRandWeight()));
        return this;
    }
    
    public boolean isEmpty() {
        return this.entries.isEmpty();
    }
    
    public Stream<U> stream() {
        return (Stream<U>)this.entries.stream().map(WeightedEntry::getData);
    }
    
    public U getOne(final Random random) {
        return (U)this.shuffle(random).stream().findFirst().orElseThrow(RuntimeException::new);
    }
    
    public String toString() {
        return new StringBuilder().append("WeightedList[").append(this.entries).append("]").toString();
    }
    
    public static class WeightedEntry<T> {
        private final T data;
        private final int weight;
        private double randWeight;
        
        private WeightedEntry(final T object, final int integer) {
            this.weight = integer;
            this.data = object;
        }
        
        private double getRandWeight() {
            return this.randWeight;
        }
        
        private void setRandom(final float float1) {
            this.randWeight = -Math.pow((double)float1, (double)(1.0f / this.weight));
        }
        
        public T getData() {
            return this.data;
        }
        
        public String toString() {
            return new StringBuilder().append("").append(this.weight).append(":").append(this.data).toString();
        }
        
        public static <E> Codec<WeightedEntry<E>> codec(final Codec<E> codec) {
            return new Codec<WeightedEntry<E>>() {
                public <T> DataResult<Pair<WeightedEntry<E>, T>> decode(final DynamicOps<T> dynamicOps, final T object) {
                    final Dynamic<T> dynamic4 = new Dynamic<T>(dynamicOps, object);
                    return dynamic4.get("data").flatMap((java.util.function.Function<? super Dynamic<T>, ? extends DataResult<Object>>)codec::parse).map((java.util.function.Function<? super Object, ?>)(object -> new WeightedEntry(object, dynamic4.get("weight").asInt(1)))).<Pair<WeightedEntry<E>, T>>map((java.util.function.Function<? super Object, ? extends Pair<WeightedEntry<E>, T>>)(a -> Pair.<WeightedEntry, Object>of(a, dynamicOps.empty())));
                }
                
                public <T> DataResult<T> encode(final WeightedEntry<E> a, final DynamicOps<T> dynamicOps, final T object) {
                    return dynamicOps.mapBuilder().add("weight", dynamicOps.createInt(((WeightedEntry<Object>)a).weight)).add("data", (DataResult<T>)codec.encodeStart(dynamicOps, ((WeightedEntry<Object>)a).data)).build(object);
                }
            };
        }
    }
}
