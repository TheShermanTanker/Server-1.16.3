package it.unimi.dsi.fastutil.ints;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.PrimitiveIterator;

public interface IntIterator extends PrimitiveIterator.OfInt {
    int nextInt();
    
    @Deprecated
    default Integer next() {
        return this.nextInt();
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Integer> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextInt();
        }
        return n - i - 1;
    }
}
