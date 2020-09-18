package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;

public interface BooleanIterable extends Iterable<Boolean> {
    BooleanIterator iterator();
    
    default void forEach(final BooleanConsumer action) {
        Objects.requireNonNull(action);
        final BooleanIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextBoolean());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Boolean> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }
}
