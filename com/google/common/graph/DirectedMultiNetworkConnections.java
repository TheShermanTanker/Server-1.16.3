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

final class DirectedMultiNetworkConnections<N, E> extends AbstractDirectedNetworkConnections<N, E> {
    @LazyInit
    private transient Reference<Multiset<N>> predecessorsReference;
    @LazyInit
    private transient Reference<Multiset<N>> successorsReference;
    
    private DirectedMultiNetworkConnections(final Map<E, N> inEdges, final Map<E, N> outEdges, final int selfLoopCount) {
        super(inEdges, outEdges, selfLoopCount);
    }
    
    static <N, E> DirectedMultiNetworkConnections<N, E> of() {
        return new DirectedMultiNetworkConnections<N, E>((java.util.Map<E, N>)new HashMap(2, 1.0f), (java.util.Map<E, N>)new HashMap(2, 1.0f), 0);
    }
    
    static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(final Map<E, N> inEdges, final Map<E, N> outEdges, final int selfLoopCount) {
        return new DirectedMultiNetworkConnections<N, E>((java.util.Map<E, N>)ImmutableMap.copyOf((java.util.Map<?, ?>)inEdges), (java.util.Map<E, N>)ImmutableMap.copyOf((java.util.Map<?, ?>)outEdges), selfLoopCount);
    }
    
    public Set<N> predecessors() {
        return (Set<N>)Collections.unmodifiableSet((Set)this.predecessorsMultiset().elementSet());
    }
    
    private Multiset<N> predecessorsMultiset() {
        Multiset<N> predecessors = DirectedMultiNetworkConnections.<Multiset<N>>getReference(this.predecessorsReference);
        if (predecessors == null) {
            predecessors = HashMultiset.create((java.lang.Iterable<?>)this.inEdgeMap.values());
            this.predecessorsReference = (Reference<Multiset<N>>)new SoftReference(predecessors);
        }
        return predecessors;
    }
    
    public Set<N> successors() {
        return (Set<N>)Collections.unmodifiableSet((Set)this.successorsMultiset().elementSet());
    }
    
    private Multiset<N> successorsMultiset() {
        Multiset<N> successors = DirectedMultiNetworkConnections.<Multiset<N>>getReference(this.successorsReference);
        if (successors == null) {
            successors = HashMultiset.create((java.lang.Iterable<?>)this.outEdgeMap.values());
            this.successorsReference = (Reference<Multiset<N>>)new SoftReference(successors);
        }
        return successors;
    }
    
    public Set<E> edgesConnecting(final Object node) {
        return (Set<E>)new MultiEdgesConnecting<E>(this.outEdgeMap, node) {
            public int size() {
                return DirectedMultiNetworkConnections.this.successorsMultiset().count(node);
            }
        };
    }
    
    @Override
    public N removeInEdge(final Object edge, final boolean isSelfLoop) {
        final N node = super.removeInEdge(edge, isSelfLoop);
        final Multiset<N> predecessors = DirectedMultiNetworkConnections.<Multiset<N>>getReference(this.predecessorsReference);
        if (predecessors != null) {
            Preconditions.checkState(predecessors.remove(node));
        }
        return node;
    }
    
    @Override
    public N removeOutEdge(final Object edge) {
        final N node = super.removeOutEdge(edge);
        final Multiset<N> successors = DirectedMultiNetworkConnections.<Multiset<N>>getReference(this.successorsReference);
        if (successors != null) {
            Preconditions.checkState(successors.remove(node));
        }
        return node;
    }
    
    @Override
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        super.addInEdge(edge, node, isSelfLoop);
        final Multiset<N> predecessors = DirectedMultiNetworkConnections.<Multiset<N>>getReference(this.predecessorsReference);
        if (predecessors != null) {
            Preconditions.checkState(predecessors.add(node));
        }
    }
    
    @Override
    public void addOutEdge(final E edge, final N node) {
        super.addOutEdge(edge, node);
        final Multiset<N> successors = DirectedMultiNetworkConnections.<Multiset<N>>getReference(this.successorsReference);
        if (successors != null) {
            Preconditions.checkState(successors.add(node));
        }
    }
    
    @Nullable
    private static <T> T getReference(@Nullable final Reference<T> reference) {
        return (T)((reference == null) ? null : reference.get());
    }
}
