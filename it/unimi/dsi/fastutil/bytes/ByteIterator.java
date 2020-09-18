package it.unimi.dsi.fastutil.bytes;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.Iterator;

public interface ByteIterator extends Iterator<Byte> {
    byte nextByte();
    
    @Deprecated
    default Byte next() {
        return this.nextByte();
    }
    
    default void forEachRemaining(final ByteConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextByte());
        }
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Byte> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextByte();
        }
        return n - i - 1;
    }
}
