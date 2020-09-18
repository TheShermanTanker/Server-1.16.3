package it.unimi.dsi.fastutil.chars;

import java.util.function.Consumer;
import java.util.Objects;
import java.util.Iterator;

public interface CharIterator extends Iterator<Character> {
    char nextChar();
    
    @Deprecated
    default Character next() {
        return this.nextChar();
    }
    
    default void forEachRemaining(final CharConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextChar());
        }
    }
    
    @Deprecated
    default void forEachRemaining(final Consumer<? super Character> action) {
        Objects.requireNonNull(action);
        this.forEachRemaining(action::accept);
    }
    
    default int skip(final int n) {
        if (n < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Argument must be nonnegative: ").append(n).toString());
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextChar();
        }
        return n - i - 1;
    }
}
