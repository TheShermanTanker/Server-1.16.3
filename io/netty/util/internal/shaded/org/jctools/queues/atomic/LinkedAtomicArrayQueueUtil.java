package io.netty.util.internal.shaded.org.jctools.queues.atomic;

import java.util.concurrent.atomic.AtomicReferenceArray;

final class LinkedAtomicArrayQueueUtil {
    private LinkedAtomicArrayQueueUtil() {
    }
    
    public static <E> E lvElement(final AtomicReferenceArray<E> buffer, final int offset) {
        return AtomicReferenceArrayQueue.<E>lvElement(buffer, offset);
    }
    
    public static <E> E lpElement(final AtomicReferenceArray<E> buffer, final int offset) {
        return AtomicReferenceArrayQueue.<E>lpElement(buffer, offset);
    }
    
    public static <E> void spElement(final AtomicReferenceArray<E> buffer, final int offset, final E value) {
        AtomicReferenceArrayQueue.<E>spElement(buffer, offset, value);
    }
    
    public static <E> void svElement(final AtomicReferenceArray<E> buffer, final int offset, final E value) {
        AtomicReferenceArrayQueue.<E>svElement(buffer, offset, value);
    }
    
    static <E> void soElement(final AtomicReferenceArray buffer, final int offset, final Object value) {
        buffer.lazySet(offset, value);
    }
    
    static int calcElementOffset(final long index, final long mask) {
        return (int)(index & mask);
    }
    
    static <E> AtomicReferenceArray<E> allocate(final int capacity) {
        return (AtomicReferenceArray<E>)new AtomicReferenceArray(capacity);
    }
    
    static int length(final AtomicReferenceArray<?> buf) {
        return buf.length();
    }
    
    static int modifiedCalcElementOffset(final long index, final long mask) {
        return (int)(index & mask) >> 1;
    }
    
    static int nextArrayOffset(final AtomicReferenceArray<?> curr) {
        return length(curr) - 1;
    }
}
