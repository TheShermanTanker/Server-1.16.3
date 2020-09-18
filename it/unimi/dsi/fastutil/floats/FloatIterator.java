package it.unimi.dsi.fastutil.floats;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.Iterator;

public interface FloatIterator extends Iterator<Float> {
    float nextFloat();
    
    @Deprecated
    default Float next() {
        return this.nextFloat();
    }
    
    default void forEachRemaining(final FloatConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextFloat());
        }
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Float> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextFloat();
        }
        return n - i - 1;
    }
}
