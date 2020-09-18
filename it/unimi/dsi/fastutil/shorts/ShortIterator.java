package it.unimi.dsi.fastutil.shorts;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.Iterator;

public interface ShortIterator extends Iterator<Short> {
    short nextShort();
    
    @Deprecated
    default Short next() {
        return this.nextShort();
    }
    
    default void forEachRemaining(final ShortConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextShort());
        }
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Short> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextShort();
        }
        return n - i - 1;
    }
}
