package com.google.common.graph;

import java.util.Map;
import com.google.common.base.Function;
import java.util.Set;
import com.google.common.collect.Maps;
import com.google.common.base.Functions;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.annotations.Beta;

@Beta
public abstract class ImmutableGraph<N> extends ForwardingGraph<N> {
    ImmutableGraph() {
    }
    
    public static <N> ImmutableGraph<N> copyOf(final Graph<N> graph) {
        return (ImmutableGraph<N>)((graph instanceof ImmutableGraph) ? ((ImmutableGraph)graph) : new ValueBackedImpl(GraphBuilder.<N>from(graph), ImmutableGraph.<N>getNodeConnections(graph), graph.edges().size()));
    }
    
    @Deprecated
    public static <N> ImmutableGraph<N> copyOf(final ImmutableGraph<N> graph) {
        return Preconditions.<ImmutableGraph<N>>checkNotNull(graph);
    }
    
    private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(final Graph<N> graph) {
        final ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.<N, GraphConnections<N, GraphConstants.Presence>>builder();
        for (final N node : graph.nodes()) {
            nodeConnections.put(node, ImmutableGraph.<N>connectionsOf(graph, node));
        }
        return nodeConnections.build();
    }
    
    private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(final Graph<N> graph, final N node) {
        final Function<Object, GraphConstants.Presence> edgeValueFn = Functions.<GraphConstants.Presence>constant(GraphConstants.Presence.EDGE_EXISTS);
        return (GraphConnections<N, GraphConstants.Presence>)(graph.isDirected() ? DirectedGraphConnections.<N, Object>ofImmutable(graph.predecessors(node), (java.util.Map<N, Object>)Maps.<N, V>asMap((java.util.Set<N>)graph.successors(node), edgeValueFn)) : UndirectedGraphConnections.ofImmutable((java.util.Map<Object, Object>)Maps.<N, V>asMap((java.util.Set<N>)graph.adjacentNodes(node), edgeValueFn)));
    }
    
    static class ValueBackedImpl<N, V> extends ImmutableGraph<N> {
        protected final ValueGraph<N, V> backingValueGraph;
        
        ValueBackedImpl(final AbstractGraphBuilder<? super N> builder, final ImmutableMap<N, GraphConnections<N, V>> nodeConnections, final long edgeCount) {
            this.backingValueGraph = new ConfigurableValueGraph<N, V>(builder, (java.util.Map<N, GraphConnections<N, V>>)nodeConnections, edgeCount);
        }
        
        @Override
        protected Graph<N> delegate() {
            return this.backingValueGraph;
        }
    }
}
