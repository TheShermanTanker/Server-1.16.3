package com.google.common.graph;

import java.util.Collections;
import com.google.common.collect.BiMap;
import java.util.Set;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.HashBiMap;
import java.util.Map;

final class DirectedNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E> {
    protected DirectedNetworkConnections(final Map<E, N> inEdgeMap, final Map<E, N> outEdgeMap, final int selfLoopCount) {
        super(inEdgeMap, outEdgeMap, selfLoopCount);
    }
    
    static <N, E> DirectedNetworkConnections<N, E> of() {
        return new DirectedNetworkConnections<N, E>((java.util.Map<E, N>)HashBiMap.create(2), (java.util.Map<E, N>)HashBiMap.create(2), 0);
    }
    
    static <N, E> DirectedNetworkConnections<N, E> ofImmutable(final Map<E, N> inEdges, final Map<E, N> outEdges, final int selfLoopCount) {
        return new DirectedNetworkConnections<N, E>((java.util.Map<E, N>)ImmutableBiMap.copyOf((java.util.Map<?, ?>)inEdges), (java.util.Map<E, N>)ImmutableBiMap.copyOf((java.util.Map<?, ?>)outEdges), selfLoopCount);
    }
    
    public Set<N> predecessors() {
        return (Set<N>)Collections.unmodifiableSet(((BiMap)this.inEdgeMap).values());
    }
    
    public Set<N> successors() {
        return (Set<N>)Collections.unmodifiableSet(((BiMap)this.outEdgeMap).values());
    }
    
    public Set<E> edgesConnecting(final Object node) {
        return (Set<E>)new EdgesConnecting((java.util.Map<?, Object>)((BiMap)this.outEdgeMap).inverse(), node);
    }
}
