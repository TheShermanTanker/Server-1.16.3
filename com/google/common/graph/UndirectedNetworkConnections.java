package com.google.common.graph;

import java.util.Collections;
import com.google.common.collect.BiMap;
import java.util.Set;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.HashBiMap;
import java.util.Map;

final class UndirectedNetworkConnections<N, E> extends AbstractUndirectedNetworkConnections<N, E> {
    protected UndirectedNetworkConnections(final Map<E, N> incidentEdgeMap) {
        super(incidentEdgeMap);
    }
    
    static <N, E> UndirectedNetworkConnections<N, E> of() {
        return new UndirectedNetworkConnections<N, E>((java.util.Map<E, N>)HashBiMap.create(2));
    }
    
    static <N, E> UndirectedNetworkConnections<N, E> ofImmutable(final Map<E, N> incidentEdges) {
        return new UndirectedNetworkConnections<N, E>((java.util.Map<E, N>)ImmutableBiMap.copyOf((java.util.Map<?, ?>)incidentEdges));
    }
    
    public Set<N> adjacentNodes() {
        return (Set<N>)Collections.unmodifiableSet(((BiMap)this.incidentEdgeMap).values());
    }
    
    public Set<E> edgesConnecting(final Object node) {
        return (Set<E>)new EdgesConnecting((java.util.Map<?, Object>)((BiMap)this.incidentEdgeMap).inverse(), node);
    }
}
