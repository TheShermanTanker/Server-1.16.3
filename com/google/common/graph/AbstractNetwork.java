package com.google.common.graph;

import com.google.common.collect.Maps;
import java.util.Map;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.math.IntMath;
import javax.annotation.Nullable;
import com.google.common.collect.Iterators;
import com.google.common.base.Function;
import java.util.Iterator;
import java.util.AbstractSet;
import java.util.Set;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractNetwork<N, E> implements Network<N, E> {
    public Graph<N> asGraph() {
        return new AbstractGraph<N>() {
            public Set<N> nodes() {
                return AbstractNetwork.this.nodes();
            }
            
            @Override
            public Set<EndpointPair<N>> edges() {
                if (AbstractNetwork.this.allowsParallelEdges()) {
                    return super.edges();
                }
                return (Set<EndpointPair<N>>)new AbstractSet<EndpointPair<N>>() {
                    public Iterator<EndpointPair<N>> iterator() {
                        return Iterators.<Object, EndpointPair<N>>transform((java.util.Iterator<Object>)AbstractNetwork.this.edges().iterator(), new Function<E, EndpointPair<N>>() {
                            public EndpointPair<N> apply(final E edge) {
                                return AbstractNetwork.this.incidentNodes(edge);
                            }
                        });
                    }
                    
                    public int size() {
                        return AbstractNetwork.this.edges().size();
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
            
            public ElementOrder<N> nodeOrder() {
                return AbstractNetwork.this.nodeOrder();
            }
            
            public boolean isDirected() {
                return AbstractNetwork.this.isDirected();
            }
            
            public boolean allowsSelfLoops() {
                return AbstractNetwork.this.allowsSelfLoops();
            }
            
            public Set<N> adjacentNodes(final Object node) {
                return AbstractNetwork.this.adjacentNodes(node);
            }
            
            public Set<N> predecessors(final Object node) {
                return AbstractNetwork.this.predecessors(node);
            }
            
            public Set<N> successors(final Object node) {
                return AbstractNetwork.this.successors(node);
            }
        };
    }
    
    public int degree(final Object node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.inEdges(node).size(), this.outEdges(node).size());
        }
        return IntMath.saturatedAdd(this.incidentEdges(node).size(), this.edgesConnecting(node, node).size());
    }
    
    public int inDegree(final Object node) {
        return this.isDirected() ? this.inEdges(node).size() : this.degree(node);
    }
    
    public int outDegree(final Object node) {
        return this.isDirected() ? this.outEdges(node).size() : this.degree(node);
    }
    
    public Set<E> adjacentEdges(final Object edge) {
        final EndpointPair<?> endpointPair = this.incidentNodes(edge);
        final Set<E> endpointPairIncidentEdges = Sets.union(this.incidentEdges(endpointPair.nodeU()), this.incidentEdges(endpointPair.nodeV()));
        return Sets.difference(endpointPairIncidentEdges, ImmutableSet.of(edge));
    }
    
    public String toString() {
        final String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", new Object[] { this.isDirected(), this.allowsParallelEdges(), this.allowsSelfLoops() });
        return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, this.nodes(), this.edgeIncidentNodesMap() });
    }
    
    private Map<E, EndpointPair<N>> edgeIncidentNodesMap() {
        final Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>() {
            public EndpointPair<N> apply(final E edge) {
                return AbstractNetwork.this.incidentNodes(edge);
            }
        };
        return Maps.<E, EndpointPair<N>>asMap(this.edges(), edgeToIncidentNodesFn);
    }
}
