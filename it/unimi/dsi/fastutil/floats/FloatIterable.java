package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.DoubleConsumer;

public interface FloatIterable extends Iterable<Float> {
    FloatIterator iterator();
    
    default void forEach(final DoubleConsumer action) {
        Objects.requireNonNull(action);
        final FloatIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept((double)iterator.nextFloat());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Float> action) {
        this.forEach((DoubleConsumer)new DoubleConsumer() {
            public void accept(final double key) {
                action.accept((float)key);
            }
        });
    }
}
