package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.Objects;
import java.util.function.IntConsumer;

public interface ByteIterable extends Iterable<Byte> {
    ByteIterator iterator();
    
    default void forEach(final IntConsumer action) {
        Objects.requireNonNull(action);
        final ByteIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept((int)iterator.nextByte());
        }
    }
    
    @Deprecated
    default void forEach(final Consumer<? super Byte> action) {
        this.forEach((IntConsumer)new IntConsumer() {
            public void accept(final int key) {
                action.accept((byte)key);
            }
        });
    }
}
