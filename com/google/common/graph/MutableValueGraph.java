package com.google.common.graph;

import com.google.errorprone.annotations.CompatibleWith;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
public interface MutableValueGraph<N, V> extends ValueGraph<N, V> {
    @CanIgnoreReturnValue
    boolean addNode(final N object);
    
    @CanIgnoreReturnValue
    V putEdgeValue(final N object1, final N object2, final V object3);
    
    @CanIgnoreReturnValue
    boolean removeNode(@CompatibleWith("N") final Object object);
    
    @CanIgnoreReturnValue
    V removeEdge(@CompatibleWith("N") final Object object1, @CompatibleWith("N") final Object object2);
}
