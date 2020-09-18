package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.LongConsumer;

public interface LongIterable extends Iterable<Long> {
    LongIterator iterator();
    
    default void forEach(final LongConsumer action) {
        Objects.requireNonNull(action);
        final LongIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextLong());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Long> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }
}
