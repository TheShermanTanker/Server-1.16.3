package com.google.common.graph;

import com.google.errorprone.annotations.CompatibleWith;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
public interface MutableNetwork<N, E> extends Network<N, E> {
    @CanIgnoreReturnValue
    boolean addNode(final N object);
    
    @CanIgnoreReturnValue
    boolean addEdge(final N object1, final N object2, final E object3);
    
    @CanIgnoreReturnValue
    boolean removeNode(@CompatibleWith("N") final Object object);
    
    @CanIgnoreReturnValue
    boolean removeEdge(@CompatibleWith("E") final Object object);
}
