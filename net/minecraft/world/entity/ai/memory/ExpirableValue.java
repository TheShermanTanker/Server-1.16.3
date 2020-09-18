package net.minecraft.world.entity.ai.memory;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.Optional;
import com.mojang.datafixers.kinds.App;
import java.util.function.Function;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class ExpirableValue<T> {
    private final T value;
    private long timeToLive;
    
    public ExpirableValue(final T object, final long long2) {
        this.value = object;
        this.timeToLive = long2;
    }
    
    public void tick() {
        if (this.canExpire()) {
            --this.timeToLive;
        }
    }
    
    public static <T> ExpirableValue<T> of(final T object) {
        return new ExpirableValue<T>(object, Long.MAX_VALUE);
    }
    
    public static <T> ExpirableValue<T> of(final T object, final long long2) {
        return new ExpirableValue<T>(object, long2);
    }
    
    public T getValue() {
        return this.value;
    }
    
    public boolean hasExpired() {
        return this.timeToLive <= 0L;
    }
    
    public String toString() {
        return this.value.toString() + (this.canExpire() ? new StringBuilder().append(" (ttl: ").append(this.timeToLive).append(")").toString() : "");
    }
    
    public boolean canExpire() {
        return this.timeToLive != Long.MAX_VALUE;
    }
    
    public static <T> Codec<ExpirableValue<T>> codec(final Codec<T> codec) {
        return RecordCodecBuilder.<ExpirableValue<T>>create((java.util.function.Function<RecordCodecBuilder.Instance<ExpirableValue<T>>, ? extends App<RecordCodecBuilder.Mu<ExpirableValue<T>>, ExpirableValue<T>>>)(instance -> instance.<Object, java.util.Optional<Object>>group((App<Object, Object>)codec.fieldOf("value").forGetter(axz -> axz.value), Codec.LONG.optionalFieldOf("ttl").forGetter((java.util.function.Function<Object, java.util.Optional<Object>>)(axz -> axz.canExpire() ? Optional.of(axz.timeToLive) : Optional.empty()))).apply(instance, (java.util.function.BiFunction<Object, java.util.Optional<Object>, Object>)((object, optional) -> new ExpirableValue((T)object, (long)optional.orElse(Long.MAX_VALUE))))));
    }
}
