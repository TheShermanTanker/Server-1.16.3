package io.netty.util.internal.shaded.org.jctools.queues;

import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;

public final class CircularArrayOffsetCalculator {
    public static <E> E[] allocate(final int capacity) {
        return (E[])new Object[capacity];
    }
    
    public static long calcElementOffset(final long index, final long mask) {
        return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((index & mask) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT);
    }
}
