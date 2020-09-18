package com.google.common.graph;

import com.google.common.math.IntMath;
import javax.annotation.Nullable;
import com.google.common.primitives.Ints;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractGraph<N> implements Graph<N> {
    protected long edgeCount() {
        long degreeSum = 0L;
        for (final N node : this.nodes()) {
            degreeSum += this.degree(node);
        }
        Preconditions.checkState((degreeSum & 0x1L) == 0x0L);
        return degreeSum >>> 1;
    }
    
    public Set<EndpointPair<N>> edges() {
        return (Set<EndpointPair<N>>)new AbstractSet<EndpointPair<N>>() {
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return EndpointPairIterator.of((Graph<Object>)AbstractGraph.this);
            }
            
            public int size() {
                return Ints.saturatedCast(AbstractGraph.this.edgeCount());
            }
            
            public boolean contains(@Nullable final Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                final EndpointPair<?> endpointPair = obj;
                return AbstractGraph.this.isDirected() == endpointPair.isOrdered() && AbstractGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
        };
    }
    
    public int degree(final Object node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
        }
        final Set<N> neighbors = this.adjacentNodes(node);
        final int selfLoopCount = (this.allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
        return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
    }
    
    public int inDegree(final Object node) {
        return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
    }
    
    public int outDegree(final Object node) {
        return this.isDirected() ? this.successors(node).size() : this.degree(node);
    }
    
    public String toString() {
        final String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { this.isDirected(), this.allowsSelfLoops() });
        return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, this.nodes(), this.edges() });
    }
}
