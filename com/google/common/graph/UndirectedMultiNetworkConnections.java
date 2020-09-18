package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.lang.ref.SoftReference;
import com.google.common.collect.HashMultiset;
import java.util.Collections;
import java.util.Set;
import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.common.collect.Multiset;
import java.lang.ref.Reference;

final class UndirectedMultiNetworkConnections<N, E> extends AbstractUndirectedNetworkConnections<N, E> {
    @LazyInit
    private transient Reference<Multiset<N>> adjacentNodesReference;
    
    private UndirectedMultiNetworkConnections(final Map<E, N> incidentEdges) {
        super(incidentEdges);
    }
    
    static <N, E> UndirectedMultiNetworkConnections<N, E> of() {
        return new UndirectedMultiNetworkConnections<N, E>((java.util.Map<E, N>)new HashMap(2, 1.0f));
    }
    
    static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(final Map<E, N> incidentEdges) {
        return new UndirectedMultiNetworkConnections<N, E>((java.util.Map<E, N>)ImmutableMap.copyOf((java.util.Map<?, ?>)incidentEdges));
    }
    
    public Set<N> adjacentNodes() {
        return (Set<N>)Collections.unmodifiableSet((Set)this.adjacentNodesMultiset().elementSet());
    }
    
    private Multiset<N> adjacentNodesMultiset() {
        Multiset<N> adjacentNodes = UndirectedMultiNetworkConnections.<Multiset<N>>getReference(this.adjacentNodesReference);
        if (adjacentNodes == null) {
            adjacentNodes = HashMultiset.create((java.lang.Iterable<?>)this.incidentEdgeMap.values());
            this.adjacentNodesReference = (Reference<Multiset<N>>)new SoftReference(adjacentNodes);
        }
        return adjacentNodes;
    }
    
    public Set<E> edgesConnecting(final Object node) {
        return (Set<E>)new MultiEdgesConnecting<E>(this.incidentEdgeMap, node) {
            public int size() {
                return UndirectedMultiNetworkConnections.this.adjacentNodesMultiset().count(node);
            }
        };
    }
    
    @Override
    public N removeInEdge(final Object edge, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            return this.removeOutEdge(edge);
        }
        return null;
    }
    
    @Override
    public N removeOutEdge(final Object edge) {
        final N node = super.removeOutEdge(edge);
        final Multiset<N> adjacentNodes = UndirectedMultiNetworkConnections.<Multiset<N>>getReference(this.adjacentNodesReference);
        if (adjacentNodes != null) {
            Preconditions.checkState(adjacentNodes.remove(node));
        }
        return node;
    }
    
    @Override
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        if (!isSelfLoop) {
            this.addOutEdge(edge, node);
        }
    }
    
    @Override
    public void addOutEdge(final E edge, final N node) {
        super.addOutEdge(edge, node);
        final Multiset<N> adjacentNodes = UndirectedMultiNetworkConnections.<Multiset<N>>getReference(this.adjacentNodesReference);
        if (adjacentNodes != null) {
            Preconditions.checkState(adjacentNodes.add(node));
        }
    }
    
    @Nullable
    private static <T> T getReference(@Nullable final Reference<T> reference) {
        return (T)((reference == null) ? null : reference.get());
    }
}
