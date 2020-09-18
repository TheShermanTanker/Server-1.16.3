package it.unimi.dsi.fastutil.bytes;

import java.util.Objects;
import it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntConsumer;
import java.util.function.Consumer;

@FunctionalInterface
public interface ByteConsumer extends Consumer<Byte>, IntConsumer {
    void accept(final byte byte1);
    
    @Deprecated
    default void accept(final int t) {
        this.accept(SafeMath.safeIntToByte(t));
    }
    
    @Deprecated
    default void accept(final Byte t) {
        this.accept((byte)t);
    }
    
    default ByteConsumer andThen(final ByteConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }
    
    @Deprecated
    default ByteConsumer andThen(final IntConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept((int)t);
        };
    }
    
    @Deprecated
    default Consumer<Byte> andThen(final Consumer<? super Byte> after) {
        return (Consumer<Byte>)super.andThen((Consumer)after);
    }
}
