package io.netty.channel.kqueue;

import io.netty.util.internal.PlatformDependent;
import io.netty.channel.unix.Limits;

final class NativeLongArray {
    private long memoryAddress;
    private int capacity;
    private int size;
    
    NativeLongArray(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException(new StringBuilder().append("capacity must be >= 1 but was ").append(capacity).toString());
        }
        this.memoryAddress = PlatformDependent.allocateMemory(capacity * Limits.SIZEOF_JLONG);
        this.capacity = capacity;
    }
    
    void add(final long value) {
        this.checkSize();
        PlatformDependent.putLong(this.memoryOffset(this.size++), value);
    }
    
    void clear() {
        this.size = 0;
    }
    
    boolean isEmpty() {
        return this.size == 0;
    }
    
    void free() {
        PlatformDependent.freeMemory(this.memoryAddress);
        this.memoryAddress = 0L;
    }
    
    long memoryAddress() {
        return this.memoryAddress;
    }
    
    long memoryAddressEnd() {
        return this.memoryOffset(this.size);
    }
    
    private long memoryOffset(final int index) {
        return this.memoryAddress + index * Limits.SIZEOF_JLONG;
    }
    
    private void checkSize() {
        if (this.size == this.capacity) {
            this.realloc();
        }
    }
    
    private void realloc() {
        final int newLength = (this.capacity <= 65536) ? (this.capacity << 1) : (this.capacity + this.capacity >> 1);
        final long newMemoryAddress = PlatformDependent.reallocateMemory(this.memoryAddress, newLength * Limits.SIZEOF_JLONG);
        if (newMemoryAddress == 0L) {
            throw new OutOfMemoryError(new StringBuilder().append("unable to allocate ").append(newLength).append(" new bytes! Existing capacity is: ").append(this.capacity).toString());
        }
        this.memoryAddress = newMemoryAddress;
        this.capacity = newLength;
    }
    
    public String toString() {
        return new StringBuilder().append("memoryAddress: ").append(this.memoryAddress).append(" capacity: ").append(this.capacity).append(" size: ").append(this.size).toString();
    }
}
