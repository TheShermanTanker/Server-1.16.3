package com.google.common.graph;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import com.google.common.collect.Maps;
import com.google.common.base.Function;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;

@Beta
public final class ImmutableValueGraph<N, V> extends ValueBackedImpl<N, V> implements ValueGraph<N, V> {
    private ImmutableValueGraph(final ValueGraph<N, V> graph) {
        super(ValueGraphBuilder.from((Graph<Object>)graph), ImmutableValueGraph.<N, V>getNodeConnections(graph), graph.edges().size());
    }
    
    public static <N, V> ImmutableValueGraph<N, V> copyOf(final ValueGraph<N, V> graph) {
        return (graph instanceof ImmutableValueGraph) ? ((ImmutableValueGraph)graph) : new ImmutableValueGraph<N, V>((ValueGraph<N, V>)graph);
    }
    
    @Deprecated
    public static <N, V> ImmutableValueGraph<N, V> copyOf(final ImmutableValueGraph<N, V> graph) {
        return Preconditions.<ImmutableValueGraph<N, V>>checkNotNull(graph);
    }
    
    private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(final ValueGraph<N, V> graph) {
        final ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.<N, GraphConnections<N, V>>builder();
        for (final N node : graph.nodes()) {
            nodeConnections.put(node, ImmutableValueGraph.<N, V>connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }
    
    private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
        final Function<N, V> successorNodeToValueFn = new Function<N, V>() {
            public V apply(final N successorNode) {
                return graph.edgeValue(node, successorNode);
            }
        };
        return (GraphConnections<N, V>)(graph.isDirected() ? DirectedGraphConnections.<N, Object>ofImmutable(graph.predecessors(node), (java.util.Map<N, Object>)Maps.<N, V>asMap((java.util.Set<N>)graph.successors(node), successorNodeToValueFn)) : UndirectedGraphConnections.ofImmutable((java.util.Map<Object, Object>)Maps.<N, V>asMap((java.util.Set<N>)graph.adjacentNodes(node), successorNodeToValueFn)));
    }
    
    @Override
    public V edgeValue(final Object nodeU, final Object nodeV) {
        return this.backingValueGraph.edgeValue(nodeU, nodeV);
    }
    
    @Override
    public V edgeValueOrDefault(final Object nodeU, final Object nodeV, @Nullable final V defaultValue) {
        return this.backingValueGraph.edgeValueOrDefault(nodeU, nodeV, defaultValue);
    }
    
    public String toString() {
        return this.backingValueGraph.toString();
    }
}
