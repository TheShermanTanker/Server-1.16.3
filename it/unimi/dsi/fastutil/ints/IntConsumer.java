package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface IntConsumer extends Consumer<Integer>, java.util.function.IntConsumer {
    @Deprecated
    default void accept(final Integer t) {
        this.accept((int)t);
    }
    
    default IntConsumer andThen(final java.util.function.IntConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default Consumer<Integer> andThen(final Consumer<? super Integer> after) {
        return (Consumer<Integer>)super.andThen((Consumer)after);
    }
}
