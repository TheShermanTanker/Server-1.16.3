package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.DoubleConsumer;

public interface DoubleIterable extends Iterable<Double> {
    DoubleIterator iterator();
    
    default void forEach(final DoubleConsumer action) {
        Objects.requireNonNull(action);
        final DoubleIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextDouble());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Double> action) {
        Objects.requireNonNull(action);
        this.forEach(action::accept);
    }
}
