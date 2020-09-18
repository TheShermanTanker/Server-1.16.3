package com.google.common.graph;

import com.google.errorprone.annotations.CompatibleWith;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
public interface MutableGraph<N> extends Graph<N> {
    @CanIgnoreReturnValue
    boolean addNode(final N object);
    
    @CanIgnoreReturnValue
    boolean putEdge(final N object1, final N object2);
    
    @CanIgnoreReturnValue
    boolean removeNode(@CompatibleWith("N") final Object object);
    
    @CanIgnoreReturnValue
    boolean removeEdge(@CompatibleWith("N") final Object object1, @CompatibleWith("N") final Object object2);
}
