package it.unimi.dsi.fastutil.floats;

import java.util.Objects;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoubleConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface FloatConsumer extends Consumer<Float>, DoubleConsumer {
    void accept(final float float1);
    
    @Deprecated
    default void accept(final double t) {
        this.accept(SafeMath.safeDoubleToFloat(t));
    }
    
    @Deprecated
    default void accept(final Float t) {
        this.accept((float)t);
    }
    
    default FloatConsumer andThen(final FloatConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default FloatConsumer andThen(final DoubleConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept((double)t);
        };
    }
    
    @Deprecated
    default Consumer<Float> andThen(final Consumer<? super Float> after) {
        return (Consumer<Float>)super.andThen((Consumer)after);
    }
}
