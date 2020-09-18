package it.unimi.dsi.fastutil.booleans;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface BooleanConsumer extends Consumer<Boolean> {
    void accept(final boolean boolean1);
    
    @Deprecated
    default void accept(final Boolean t) {
        this.accept((boolean)t);
    }
    
    default BooleanConsumer andThen(final BooleanConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default Consumer<Boolean> andThen(final Consumer<? super Boolean> after) {
        return (Consumer<Boolean>)super.andThen((Consumer)after);
    }
}
