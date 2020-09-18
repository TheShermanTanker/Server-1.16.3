package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.IntConsumer;

public interface ShortIterable extends Iterable<Short> {
    ShortIterator iterator();
    
    default void forEach(final IntConsumer action) {
        Objects.requireNonNull(action);
        final ShortIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept((int)iterator.nextShort());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Short> action) {
        this.forEach((IntConsumer)new IntConsumer() {
            public void accept(final int key) {
                action.accept((short)key);
            }
        });
    }
}
