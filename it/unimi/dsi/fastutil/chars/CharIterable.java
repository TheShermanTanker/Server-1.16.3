package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.IntConsumer;

public interface CharIterable extends Iterable<Character> {
    CharIterator iterator();
    
    default void forEach(final IntConsumer action) {
        Objects.requireNonNull(action);
        final CharIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept((int)iterator.nextChar());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Character> action) {
        this.forEach((IntConsumer)new IntConsumer() {
            public void accept(final int key) {
                action.accept((char)key);
            }
        });
    }
}
