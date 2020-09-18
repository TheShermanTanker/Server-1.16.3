package it.unimi.dsi.fastutil.doubles;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface DoubleConsumer extends Consumer<Double>, java.util.function.DoubleConsumer {
    @Deprecated
    default void accept(final Double t) {
        this.accept((double)t);
    }
    
    default DoubleConsumer andThen(final java.util.function.DoubleConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default Consumer<Double> andThen(final Consumer<? super Double> after) {
        return (Consumer<Double>)super.andThen((Consumer)after);
    }
}
