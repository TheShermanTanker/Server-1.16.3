package com.google.common.graph;

import java.util.Collections;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.Map;

abstract class AbstractUndirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    protected final Map<E, N> incidentEdgeMap;
    
    protected AbstractUndirectedNetworkConnections(final Map<E, N> incidentEdgeMap) {
        this.incidentEdgeMap = Preconditions.<Map<E, N>>checkNotNull(incidentEdgeMap);
    }
    
    public Set<N> predecessors() {
        return this.adjacentNodes();
    }
    
    public Set<N> successors() {
        return this.adjacentNodes();
    }
    
    public Set<E> incidentEdges() {
        return (Set<E>)Collections.unmodifiableSet(this.incidentEdgeMap.keySet());
    }
    
    public Set<E> inEdges() {
        return this.incidentEdges();
    }
    
    public Set<E> outEdges() {
        return this.incidentEdges();
    }
    
    public N oppositeNode(final Object edge) {
        return Preconditions.<N>checkNotNull(this.incidentEdgeMap.get(edge));
    }
    
    public N removeInEdge(final Object edge, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            return this.removeOutEdge(edge);
        }
        return null;
    }
    
    public N removeOutEdge(final Object edge) {
        final N previousNode = (N)this.incidentEdgeMap.remove(edge);
        return Preconditions.<N>checkNotNull(previousNode);
    }
    
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            this.addOutEdge(edge, node);
        }
    }
    
    public void addOutEdge(final E edge, final N node) {
        final N previousNode = (N)this.incidentEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
}
