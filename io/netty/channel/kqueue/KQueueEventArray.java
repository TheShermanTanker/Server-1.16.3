package io.netty.channel.kqueue;

import io.netty.util.internal.PlatformDependent;

final class KQueueEventArray {
    private static final int KQUEUE_EVENT_SIZE;
    private static final int KQUEUE_IDENT_OFFSET;
    private static final int KQUEUE_FILTER_OFFSET;
    private static final int KQUEUE_FFLAGS_OFFSET;
    private static final int KQUEUE_FLAGS_OFFSET;
    private static final int KQUEUE_DATA_OFFSET;
    private long memoryAddress;
    private int size;
    private int capacity;
    
    KQueueEventArray(final int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException(new StringBuilder().append("capacity must be >= 1 but was ").append(capacity).toString());
        }
        this.memoryAddress = PlatformDependent.allocateMemory(capacity * KQueueEventArray.KQUEUE_EVENT_SIZE);
        this.capacity = capacity;
    }
    
    long memoryAddress() {
        return this.memoryAddress;
    }
    
    int capacity() {
        return this.capacity;
    }
    
    int size() {
        return this.size;
    }
    
    void clear() {
        this.size = 0;
    }
    
    void evSet(final AbstractKQueueChannel ch, final short filter, final short flags, final int fflags) {
        this.checkSize();
        evSet(this.getKEventOffset(this.size++), ch, ch.socket.intValue(), filter, flags, fflags);
    }
    
    private void checkSize() {
        if (this.size == this.capacity) {
            this.realloc(true);
        }
    }
    
    void realloc(final boolean throwIfFail) {
        final int newLength = (this.capacity <= 65536) ? (this.capacity << 1) : (this.capacity + this.capacity >> 1);
        final long newMemoryAddress = PlatformDependent.reallocateMemory(this.memoryAddress, newLength * KQueueEventArray.KQUEUE_EVENT_SIZE);
        if (newMemoryAddress != 0L) {
            this.memoryAddress = newMemoryAddress;
            this.capacity = newLength;
            return;
        }
        if (throwIfFail) {
            throw new OutOfMemoryError(new StringBuilder().append("unable to allocate ").append(newLength).append(" new bytes! Existing capacity is: ").append(this.capacity).toString());
        }
    }
    
    void free() {
        PlatformDependent.freeMemory(this.memoryAddress);
        final int n = 0;
        this.capacity = n;
        this.size = n;
        this.memoryAddress = n;
    }
    
    long getKEventOffset(final int index) {
        return this.memoryAddress + index * KQueueEventArray.KQUEUE_EVENT_SIZE;
    }
    
    short flags(final int index) {
        return PlatformDependent.getShort(this.getKEventOffset(index) + KQueueEventArray.KQUEUE_FLAGS_OFFSET);
    }
    
    short filter(final int index) {
        return PlatformDependent.getShort(this.getKEventOffset(index) + KQueueEventArray.KQUEUE_FILTER_OFFSET);
    }
    
    short fflags(final int index) {
        return PlatformDependent.getShort(this.getKEventOffset(index) + KQueueEventArray.KQUEUE_FFLAGS_OFFSET);
    }
    
    int fd(final int index) {
        return PlatformDependent.getInt(this.getKEventOffset(index) + KQueueEventArray.KQUEUE_IDENT_OFFSET);
    }
    
    long data(final int index) {
        return PlatformDependent.getLong(this.getKEventOffset(index) + KQueueEventArray.KQUEUE_DATA_OFFSET);
    }
    
    AbstractKQueueChannel channel(final int index) {
        return getChannel(this.getKEventOffset(index));
    }
    
    private static native void evSet(final long long1, final AbstractKQueueChannel abstractKQueueChannel, final int integer3, final short short4, final short short5, final int integer6);
    
    private static native AbstractKQueueChannel getChannel(final long long1);
    
    static native void deleteGlobalRefs(final long long1, final long long2);
    
    static {
        KQUEUE_EVENT_SIZE = Native.sizeofKEvent();
        KQUEUE_IDENT_OFFSET = Native.offsetofKEventIdent();
        KQUEUE_FILTER_OFFSET = Native.offsetofKEventFilter();
        KQUEUE_FFLAGS_OFFSET = Native.offsetofKEventFFlags();
        KQUEUE_FLAGS_OFFSET = Native.offsetofKEventFlags();
        KQUEUE_DATA_OFFSET = Native.offsetofKeventData();
    }
}
