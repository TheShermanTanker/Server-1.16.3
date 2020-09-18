package it.unimi.dsi.fastutil.booleans;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.Iterator;

public interface BooleanIterator extends Iterator<Boolean> {
    boolean nextBoolean();
    
    @Deprecated
    default Boolean next() {
        return this.nextBoolean();
    }
    
    default void forEachRemaining(final BooleanConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextBoolean());
        }
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Boolean> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextBoolean();
        }
        return n - i - 1;
    }
}
