package it.unimi.dsi.fastutil.shorts;

import java.util.Objects;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface ShortConsumer extends Consumer<Short>, IntConsumer {
    void accept(final short short1);
    
    @Deprecated
    default void accept(final int t) {
        this.accept(SafeMath.safeIntToShort(t));
    }
    
    @Deprecated
    default void accept(final Short t) {
        this.accept((short)t);
    }
    
    default ShortConsumer andThen(final ShortConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default ShortConsumer andThen(final IntConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept((int)t);
        };
    }
    
    @Deprecated
    default Consumer<Short> andThen(final Consumer<? super Short> after) {
        return (Consumer<Short>)super.andThen((Consumer)after);
    }
}
