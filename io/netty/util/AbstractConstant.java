package io.netty.util;

import java.util.concurrent.atomic.AtomicLong;

public abstract class AbstractConstant<T extends AbstractConstant<T>> implements Constant<T> {
    private static final AtomicLong uniqueIdGenerator;
    private final int id;
    private final String name;
    private final long uniquifier;
    
    protected AbstractConstant(final int id, final String name) {
        this.id = id;
        this.name = name;
        this.uniquifier = AbstractConstant.uniqueIdGenerator.getAndIncrement();
    }
    
    public final String name() {
        return this.name;
    }
    
    public final int id() {
        return this.id;
    }
    
    public final String toString() {
        return this.name();
    }
    
    public final int hashCode() {
        return super.hashCode();
    }
    
    public final boolean equals(final Object obj) {
        return super.equals(obj);
    }
    
    public final int compareTo(final T o) {
        if (this == o) {
            return 0;
        }
        final AbstractConstant<T> other = o;
        final int returnCode = this.hashCode() - other.hashCode();
        if (returnCode != 0) {
            return returnCode;
        }
        if (this.uniquifier < other.uniquifier) {
            return -1;
        }
        if (this.uniquifier > other.uniquifier) {
            return 1;
        }
        throw new Error("failed to compare two different constants");
    }
    
    static {
        uniqueIdGenerator = new AtomicLong();
    }
}
