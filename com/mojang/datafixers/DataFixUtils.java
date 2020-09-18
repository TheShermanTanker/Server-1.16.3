package com.mojang.datafixers;

import java.util.function.UnaryOperator;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DataFixUtils {
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION;
    
    private DataFixUtils() {
    }
    
    public static int smallestEncompassingPowerOfTwo(final int input) {
        int result = input - 1;
        result |= result >> 1;
        result |= result >> 2;
        result |= result >> 4;
        result |= result >> 8;
        result |= result >> 16;
        return result + 1;
    }
    
    private static boolean isPowerOfTwo(final int input) {
        return input != 0 && (input & input - 1) == 0x0;
    }
    
    public static int ceillog2(int input) {
        input = (isPowerOfTwo(input) ? input : smallestEncompassingPowerOfTwo(input));
        return DataFixUtils.MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)(input * 125613361L >> 27) & 0x1F];
    }
    
    public static <T> T make(final Supplier<T> factory) {
        return (T)factory.get();
    }
    
    public static <T> T make(final T t, final Consumer<T> consumer) {
        consumer.accept(t);
        return t;
    }
    
    public static <U> U orElse(final Optional<? extends U> optional, final U other) {
        if (optional.isPresent()) {
            return (U)optional.get();
        }
        return other;
    }
    
    public static <U> U orElseGet(final Optional<? extends U> optional, final Supplier<? extends U> other) {
        if (optional.isPresent()) {
            return (U)optional.get();
        }
        return (U)other.get();
    }
    
    public static <U> Optional<U> or(final Optional<? extends U> optional, final Supplier<? extends Optional<? extends U>> other) {
        if (optional.isPresent()) {
            return (Optional<U>)optional.map(u -> u);
        }
        return (Optional<U>)((Optional)other.get()).map(u -> u);
    }
    
    public static byte[] toArray(final ByteBuffer input) {
        byte[] bytes;
        if (input.hasArray()) {
            bytes = input.array();
        }
        else {
            bytes = new byte[input.capacity()];
            input.get(bytes, 0, bytes.length);
        }
        return bytes;
    }
    
    public static int makeKey(final int version) {
        return makeKey(version, 0);
    }
    
    public static int makeKey(final int version, final int subVersion) {
        return version * 10 + subVersion;
    }
    
    public static int getVersion(final int key) {
        return key / 10;
    }
    
    public static int getSubVersion(final int key) {
        return key % 10;
    }
    
    public static <T> UnaryOperator<T> consumerToFunction(final Consumer<T> consumer) {
        return (UnaryOperator<T>)(s -> {
            consumer.accept(s);
            return s;
        });
    }
    
    static {
        MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[] { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };
    }
}
