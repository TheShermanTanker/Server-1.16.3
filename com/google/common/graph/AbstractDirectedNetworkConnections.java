package com.google.common.graph;

import java.util.Collections;
import javax.annotation.Nullable;
import com.google.common.math.IntMath;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.Iterables;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.Map;

abstract class AbstractDirectedNetworkConnections<N, E> implements NetworkConnections<N, E> {
    protected final Map<E, N> inEdgeMap;
    protected final Map<E, N> outEdgeMap;
    private int selfLoopCount;
    
    protected AbstractDirectedNetworkConnections(final Map<E, N> inEdgeMap, final Map<E, N> outEdgeMap, final int selfLoopCount) {
        this.inEdgeMap = Preconditions.<Map<E, N>>checkNotNull(inEdgeMap);
        this.outEdgeMap = Preconditions.<Map<E, N>>checkNotNull(outEdgeMap);
        this.selfLoopCount = Graphs.checkNonNegative(selfLoopCount);
        Preconditions.checkState(selfLoopCount <= inEdgeMap.size() && selfLoopCount <= outEdgeMap.size());
    }
    
    public Set<N> adjacentNodes() {
        return Sets.union(this.predecessors(), this.successors());
    }
    
    public Set<E> incidentEdges() {
        return (Set<E>)new AbstractSet<E>() {
            public UnmodifiableIterator<E> iterator() {
                final Iterable<E> incidentEdges = (Iterable<E>)((AbstractDirectedNetworkConnections.this.selfLoopCount == 0) ? Iterables.concat((java.lang.Iterable<?>)AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), (java.lang.Iterable<?>)AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()) : Sets.union((java.util.Set<?>)AbstractDirectedNetworkConnections.this.inEdgeMap.keySet(), (java.util.Set<?>)AbstractDirectedNetworkConnections.this.outEdgeMap.keySet()));
                return Iterators.<E>unmodifiableIterator((java.util.Iterator<? extends E>)incidentEdges.iterator());
            }
            
            public int size() {
                return IntMath.saturatedAdd(AbstractDirectedNetworkConnections.this.inEdgeMap.size(), AbstractDirectedNetworkConnections.this.outEdgeMap.size() - AbstractDirectedNetworkConnections.this.selfLoopCount);
            }
            
            public boolean contains(@Nullable final Object obj) {
                return AbstractDirectedNetworkConnections.this.inEdgeMap.containsKey(obj) || AbstractDirectedNetworkConnections.this.outEdgeMap.containsKey(obj);
            }
        };
    }
    
    public Set<E> inEdges() {
        return (Set<E>)Collections.unmodifiableSet(this.inEdgeMap.keySet());
    }
    
    public Set<E> outEdges() {
        return (Set<E>)Collections.unmodifiableSet(this.outEdgeMap.keySet());
    }
    
    public N oppositeNode(final Object edge) {
        return Preconditions.<N>checkNotNull(this.outEdgeMap.get(edge));
    }
    
    public N removeInEdge(final Object edge, final boolean isSelfLoop) {
        if (isSelfLoop) {
            Graphs.checkNonNegative(--this.selfLoopCount);
        }
        final N previousNode = (N)this.inEdgeMap.remove(edge);
        return Preconditions.<N>checkNotNull(previousNode);
    }
    
    public N removeOutEdge(final Object edge) {
        final N previousNode = (N)this.outEdgeMap.remove(edge);
        return Preconditions.<N>checkNotNull(previousNode);
    }
    
    public void addInEdge(final E edge, final N node, final boolean isSelfLoop) {
        if (isSelfLoop) {
            Graphs.checkPositive(++this.selfLoopCount);
        }
        final N previousNode = (N)this.inEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
    
    public void addOutEdge(final E edge, final N node) {
        final N previousNode = (N)this.outEdgeMap.put(edge, node);
        Preconditions.checkState(previousNode == null);
    }
}
