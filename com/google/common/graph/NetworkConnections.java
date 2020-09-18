package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

interface NetworkConnections<N, E> {
    Set<N> adjacentNodes();
    
    Set<N> predecessors();
    
    Set<N> successors();
    
    Set<E> incidentEdges();
    
    Set<E> inEdges();
    
    Set<E> outEdges();
    
    Set<E> edgesConnecting(final Object object);
    
    N oppositeNode(final Object object);
    
    @CanIgnoreReturnValue
    N removeInEdge(final Object object, final boolean boolean2);
    
    @CanIgnoreReturnValue
    N removeOutEdge(final Object object);
    
    void addInEdge(final E object1, final N object2, final boolean boolean3);
    
    void addOutEdge(final E object1, final N object2);
}
