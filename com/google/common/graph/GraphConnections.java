package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Set;

interface GraphConnections<N, V> {
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    @Nullable
    V value(final Object object);
    
    void removePredecessor(final Object object);
    
    @CanIgnoreReturnValue
    V removeSuccessor(final Object object);
    
    void addPredecessor(final N object1, final V object2);
    
    @CanIgnoreReturnValue
    V addSuccessor(final N object1, final V object2);
}
