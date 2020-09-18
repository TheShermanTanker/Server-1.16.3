package it.unimi.dsi.fastutil.doubles;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.PrimitiveIterator;

public interface DoubleIterator extends PrimitiveIterator.OfDouble {
    double nextDouble();
    
    @Deprecated
    default Double next() {
        return this.nextDouble();
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Double> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextDouble();
        }
        return n - i - 1;
    }
}
