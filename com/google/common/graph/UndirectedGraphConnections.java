package com.google.common.graph;

import java.util.Collections;
import java.util.Set;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import com.google.common.base.Preconditions;
import java.util.Map;

final class UndirectedGraphConnections<N, V> implements GraphConnections<N, V> {
    private final Map<N, V> adjacentNodeValues;
    
    private UndirectedGraphConnections(final Map<N, V> adjacentNodeValues) {
        this.adjacentNodeValues = Preconditions.<Map<N, V>>checkNotNull(adjacentNodeValues);
    }
    
    static <N, V> UndirectedGraphConnections<N, V> of() {
        return new UndirectedGraphConnections<N, V>((java.util.Map<N, V>)new HashMap(2, 1.0f));
    }
    
    static <N, V> UndirectedGraphConnections<N, V> ofImmutable(final Map<N, V> adjacentNodeValues) {
        return new UndirectedGraphConnections<N, V>((java.util.Map<N, V>)ImmutableMap.copyOf((java.util.Map<?, ?>)adjacentNodeValues));
    }
    
    public Set<N> adjacentNodes() {
        return (Set<N>)Collections.unmodifiableSet(this.adjacentNodeValues.keySet());
    }
    
    public Set<N> predecessors() {
        return this.adjacentNodes();
    }
    
    public Set<N> successors() {
        return this.adjacentNodes();
    }
    
    public V value(final Object node) {
        return (V)this.adjacentNodeValues.get(node);
    }
    
    public void removePredecessor(final Object node) {
        final V unused = this.removeSuccessor(node);
    }
    
    public V removeSuccessor(final Object node) {
        return (V)this.adjacentNodeValues.remove(node);
    }
    
    public void addPredecessor(final N node, final V value) {
        final V unused = this.addSuccessor(node, value);
    }
    
    public V addSuccessor(final N node, final V value) {
        return (V)this.adjacentNodeValues.put(node, value);
    }
}
