package io.netty.util.internal.shaded.org.jctools.util;

public final class Pow2 {
    public static final int MAX_POW2 = 1073741824;
    
    public static int roundToPowerOfTwo(final int value) {
        if (value > 1073741824) {
            throw new IllegalArgumentException(new StringBuilder().append("There is no larger power of 2 int for value:").append(value).append(" since it exceeds 2^31.").toString());
        }
        if (value < 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Given value:").append(value).append(". Expecting value >= 0.").toString());
        }
        final int nextPow2 = 1 << 32 - Integer.numberOfLeadingZeros(value - 1);
        return nextPow2;
    }
    
    public static boolean isPowerOfTwo(final int value) {
        return (value & value - 1) == 0x0;
    }
    
    public static long align(final long value, final int alignment) {
        if (!isPowerOfTwo(alignment)) {
            throw new IllegalArgumentException(new StringBuilder().append("alignment must be a power of 2:").append(alignment).toString());
        }
        return value + (alignment - 1) & (long)~(alignment - 1);
    }
}
