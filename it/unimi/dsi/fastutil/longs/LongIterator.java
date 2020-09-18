package it.unimi.dsi.fastutil.longs;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.PrimitiveIterator;

public interface LongIterator extends PrimitiveIterator.OfLong {
    long nextLong();
    
    @Deprecated
    default Long next() {
        return this.nextLong();
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Long> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextLong();
        }
        return n - i - 1;
    }
}
