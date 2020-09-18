package it.unimi.dsi.fastutil.chars;

import java.util.Objects;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface CharConsumer extends Consumer<Character>, IntConsumer {
    void accept(final char character);
    
    @Deprecated
    default void accept(final int t) {
        this.accept(SafeMath.safeIntToChar(t));
    }
    
    @Deprecated
    default void accept(final Character t) {
        this.accept((char)t);
    }
    
    default CharConsumer andThen(final CharConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default CharConsumer andThen(final IntConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept((int)t);
        };
    }
    
    @Deprecated
    default Consumer<Character> andThen(final Consumer<? super Character> after) {
        return (Consumer<Character>)super.andThen((Consumer)after);
    }
}
