package net.minecraft.util;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.datafixers.util.Either;
import java.util.Objects;
import java.util.Random;
import com.mojang.serialization.DataResult;
import java.util.function.Function;
import com.mojang.serialization.Codec;

public class UniformInt {
    public static final Codec<UniformInt> CODEC;
    private final int baseValue;
    private final int spread;
    
    public static Codec<UniformInt> codec(final int integer1, final int integer2, final int integer3) {
        final Function<UniformInt, DataResult<UniformInt>> function4 = (Function<UniformInt, DataResult<UniformInt>>)(aft -> {
            if (aft.baseValue < integer1 || aft.baseValue > integer2) {
                return DataResult.error(new StringBuilder().append("Base value out of range: ").append(aft.baseValue).append(" [").append(integer1).append("-").append(integer2).append("]").toString());
            }
            if (aft.spread <= integer3) {
                return DataResult.<UniformInt>success(aft);
            }
            return DataResult.error(new StringBuilder().append("Spread too big: ").append(aft.spread).append(" > ").append(integer3).toString());
        });
        return UniformInt.CODEC.<UniformInt>flatXmap((java.util.function.Function<? super UniformInt, ? extends DataResult<? extends UniformInt>>)function4, (java.util.function.Function<? super UniformInt, ? extends DataResult<? extends UniformInt>>)function4);
    }
    
    private UniformInt(final int integer1, final int integer2) {
        this.baseValue = integer1;
        this.spread = integer2;
    }
    
    public static UniformInt fixed(final int integer) {
        return new UniformInt(integer, 0);
    }
    
    public static UniformInt of(final int integer1, final int integer2) {
        return new UniformInt(integer1, integer2);
    }
    
    public int sample(final Random random) {
        if (this.spread == 0) {
            return this.baseValue;
        }
        return this.baseValue + random.nextInt(this.spread + 1);
    }
    
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        final UniformInt aft3 = (UniformInt)object;
        return this.baseValue == aft3.baseValue && this.spread == aft3.spread;
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.baseValue, this.spread });
    }
    
    public String toString() {
        return new StringBuilder().append("[").append(this.baseValue).append('-').append(this.baseValue + this.spread).append(']').toString();
    }
    
    static {
        CODEC = Codec.<Integer, Object>either(Codec.INT, RecordCodecBuilder.create((java.util.function.Function<RecordCodecBuilder.Instance<Object>, ? extends App<RecordCodecBuilder.Mu<Object>, Object>>)(instance -> instance.group(Codec.INT.fieldOf("base").forGetter((java.util.function.Function<Object, Object>)(aft -> aft.baseValue)), Codec.INT.fieldOf("spread").forGetter((java.util.function.Function<Object, Object>)(aft -> aft.spread))).apply(instance, (java.util.function.BiFunction<Object, Object, Object>)UniformInt::new))).comapFlatMap((java.util.function.Function<? super Object, ? extends DataResult<?>>)(aft -> {
            if (aft.spread < 0) {
                return DataResult.error(new StringBuilder().append("Spread must be non-negative, got: ").append(aft.spread).toString());
            }
            return DataResult.<UniformInt>success(aft);
        }), (java.util.function.Function<? super Object, ?>)Function.identity())).<UniformInt>xmap((java.util.function.Function<? super Either<Integer, Object>, ? extends UniformInt>)(either -> either.<UniformInt>map(UniformInt::fixed, aft -> aft)), (java.util.function.Function<? super UniformInt, ? extends Either<Integer, Object>>)(aft -> (aft.spread == 0) ? Either.<Integer, Object>left(aft.baseValue) : Either.<Object, UniformInt>right(aft)));
    }
}
