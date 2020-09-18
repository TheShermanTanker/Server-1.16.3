package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Queue;
import java.util.Collections;
import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import com.google.common.base.Preconditions;
import java.util.Set;
import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.HashSet;
import com.google.common.base.Objects;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.annotations.Beta;

@Beta
public final class Graphs {
    private Graphs() {
    }
    
    public static boolean hasCycle(final Graph<?> graph) {
        final int numEdges = graph.edges().size();
        if (numEdges == 0) {
            return false;
        }
        if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
            return true;
        }
        final Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
        for (final Object node : graph.nodes()) {
            if (subgraphHasCycle(graph, visitedNodes, node, null)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasCycle(final Network<?, ?> network) {
        return (!network.isDirected() && network.allowsParallelEdges() && network.edges().size() > network.asGraph().edges().size()) || hasCycle(network.asGraph());
    }
    
    private static boolean subgraphHasCycle(final Graph<?> graph, final Map<Object, NodeVisitState> visitedNodes, final Object node, @Nullable final Object previousNode) {
        final NodeVisitState state = (NodeVisitState)visitedNodes.get(node);
        if (state == NodeVisitState.COMPLETE) {
            return false;
        }
        if (state == NodeVisitState.PENDING) {
            return true;
        }
        visitedNodes.put(node, NodeVisitState.PENDING);
        for (final Object nextNode : graph.successors(node)) {
            if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode) && subgraphHasCycle(graph, visitedNodes, nextNode, node)) {
                return true;
            }
        }
        visitedNodes.put(node, NodeVisitState.COMPLETE);
        return false;
    }
    
    private static boolean canTraverseWithoutReusingEdge(final Graph<?> graph, final Object nextNode, @Nullable final Object previousNode) {
        return graph.isDirected() || !Objects.equal(previousNode, nextNode);
    }
    
    public static <N> Graph<N> transitiveClosure(final Graph<N> graph) {
        final MutableGraph<N> transitiveClosure = GraphBuilder.<N>from(graph).allowsSelfLoops(true).<N>build();
        if (graph.isDirected()) {
            for (final N node : graph.nodes()) {
                for (final N reachableNode : Graphs.<N>reachableNodes(graph, node)) {
                    transitiveClosure.putEdge(node, reachableNode);
                }
            }
        }
        else {
            final Set<N> visitedNodes = (Set<N>)new HashSet();
            for (final N node2 : graph.nodes()) {
                if (!visitedNodes.contains(node2)) {
                    final Set<N> reachableNodes = Graphs.<N>reachableNodes(graph, node2);
                    visitedNodes.addAll((Collection)reachableNodes);
                    int pairwiseMatch = 1;
                    for (final N nodeU : reachableNodes) {
                        for (final N nodeV : Iterables.limit((java.lang.Iterable<Object>)reachableNodes, pairwiseMatch++)) {
                            transitiveClosure.putEdge(nodeU, nodeV);
                        }
                    }
                }
            }
        }
        return transitiveClosure;
    }
    
    public static <N> Set<N> reachableNodes(final Graph<N> graph, final Object node) {
        Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
        final Set<N> visitedNodes = (Set<N>)new LinkedHashSet();
        final Queue<N> queuedNodes = (Queue<N>)new ArrayDeque();
        visitedNodes.add(node);
        queuedNodes.add(node);
        while (!queuedNodes.isEmpty()) {
            final N currentNode = (N)queuedNodes.remove();
            for (final N successor : graph.successors(currentNode)) {
                if (visitedNodes.add(successor)) {
                    queuedNodes.add(successor);
                }
            }
        }
        return (Set<N>)Collections.unmodifiableSet((Set)visitedNodes);
    }
    
    public static boolean equivalent(@Nullable final Graph<?> graphA, @Nullable final Graph<?> graphB) {
        return graphA == graphB || (graphA != null && graphB != null && graphA.isDirected() == graphB.isDirected() && graphA.nodes().equals(graphB.nodes()) && graphA.edges().equals(graphB.edges()));
    }
    
    public static boolean equivalent(@Nullable final ValueGraph<?, ?> graphA, @Nullable final ValueGraph<?, ?> graphB) {
        if (graphA == graphB) {
            return true;
        }
        if (graphA == null || graphB == null) {
            return false;
        }
        if (graphA.isDirected() != graphB.isDirected() || !graphA.nodes().equals(graphB.nodes()) || !graphA.edges().equals(graphB.edges())) {
            return false;
        }
        for (final EndpointPair<?> edge : graphA.edges()) {
            if (!graphA.edgeValue(edge.nodeU(), edge.nodeV()).equals(graphB.edgeValue(edge.nodeU(), edge.nodeV()))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean equivalent(@Nullable final Network<?, ?> networkA, @Nullable final Network<?, ?> networkB) {
        if (networkA == networkB) {
            return true;
        }
        if (networkA == null || networkB == null) {
            return false;
        }
        if (networkA.isDirected() != networkB.isDirected() || !networkA.nodes().equals(networkB.nodes()) || !networkA.edges().equals(networkB.edges())) {
            return false;
        }
        for (final Object edge : networkA.edges()) {
            if (!networkA.incidentNodes(edge).equals(networkB.incidentNodes(edge))) {
                return false;
            }
        }
        return true;
    }
    
    public static <N> Graph<N> transpose(final Graph<N> graph) {
        if (!graph.isDirected()) {
            return graph;
        }
        if (graph instanceof TransposedGraph) {
            return (Graph<N>)((TransposedGraph)graph).graph;
        }
        return new TransposedGraph<N>(graph);
    }
    
    public static <N, V> ValueGraph<N, V> transpose(final ValueGraph<N, V> graph) {
        if (!graph.isDirected()) {
            return graph;
        }
        if (graph instanceof TransposedValueGraph) {
            return (ValueGraph<N, V>)((TransposedValueGraph)graph).graph;
        }
        return new TransposedValueGraph<N, V>(graph);
    }
    
    public static <N, E> Network<N, E> transpose(final Network<N, E> network) {
        if (!network.isDirected()) {
            return network;
        }
        if (network instanceof TransposedNetwork) {
            return (Network<N, E>)((TransposedNetwork)network).network;
        }
        return new TransposedNetwork<N, E>(network);
    }
    
    public static <N> MutableGraph<N> inducedSubgraph(final Graph<N> graph, final Iterable<? extends N> nodes) {
        final MutableGraph<N> subgraph = GraphBuilder.<N>from(graph).<N>build();
        for (final N node : nodes) {
            subgraph.addNode(node);
        }
        for (final N node : subgraph.nodes()) {
            for (final N successorNode : graph.successors(node)) {
                if (subgraph.nodes().contains(successorNode)) {
                    subgraph.putEdge(node, successorNode);
                }
            }
        }
        return subgraph;
    }
    
    public static <N, V> MutableValueGraph<N, V> inducedSubgraph(final ValueGraph<N, V> graph, final Iterable<? extends N> nodes) {
        final MutableValueGraph<N, V> subgraph = ValueGraphBuilder.from((Graph<Object>)graph).<N, V>build();
        for (final N node : nodes) {
            subgraph.addNode(node);
        }
        for (final N node : subgraph.nodes()) {
            for (final N successorNode : graph.successors(node)) {
                if (subgraph.nodes().contains(successorNode)) {
                    subgraph.putEdgeValue(node, successorNode, graph.edgeValue(node, successorNode));
                }
            }
        }
        return subgraph;
    }
    
    public static <N, E> MutableNetwork<N, E> inducedSubgraph(final Network<N, E> network, final Iterable<? extends N> nodes) {
        final MutableNetwork<N, E> subgraph = NetworkBuilder.<N, E>from(network).<N, E>build();
        for (final N node : nodes) {
            subgraph.addNode(node);
        }
        for (final N node : subgraph.nodes()) {
            for (final E edge : network.outEdges(node)) {
                final N successorNode = network.incidentNodes(edge).adjacentNode(node);
                if (subgraph.nodes().contains(successorNode)) {
                    subgraph.addEdge(node, successorNode, edge);
                }
            }
        }
        return subgraph;
    }
    
    public static <N> MutableGraph<N> copyOf(final Graph<N> graph) {
        final MutableGraph<N> copy = GraphBuilder.<N>from(graph).expectedNodeCount(graph.nodes().size()).<N>build();
        for (final N node : graph.nodes()) {
            copy.addNode(node);
        }
        for (final EndpointPair<N> edge : graph.edges()) {
            copy.putEdge(edge.nodeU(), edge.nodeV());
        }
        return copy;
    }
    
    public static <N, V> MutableValueGraph<N, V> copyOf(final ValueGraph<N, V> graph) {
        final MutableValueGraph<N, V> copy = ValueGraphBuilder.from((Graph<Object>)graph).expectedNodeCount(graph.nodes().size()).<N, V>build();
        for (final N node : graph.nodes()) {
            copy.addNode(node);
        }
        for (final EndpointPair<N> edge : graph.edges()) {
            copy.putEdgeValue(edge.nodeU(), edge.nodeV(), graph.edgeValue(edge.nodeU(), edge.nodeV()));
        }
        return copy;
    }
    
    public static <N, E> MutableNetwork<N, E> copyOf(final Network<N, E> network) {
        final MutableNetwork<N, E> copy = NetworkBuilder.<N, E>from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).<N, E>build();
        for (final N node : network.nodes()) {
            copy.addNode(node);
        }
        for (final E edge : network.edges()) {
            final EndpointPair<N> endpointPair = network.incidentNodes(edge);
            copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
        }
        return copy;
    }
    
    @CanIgnoreReturnValue
    static int checkNonNegative(final int value) {
        Preconditions.checkArgument(value >= 0, "Not true that %s is non-negative.", value);
        return value;
    }
    
    @CanIgnoreReturnValue
    static int checkPositive(final int value) {
        Preconditions.checkArgument(value > 0, "Not true that %s is positive.", value);
        return value;
    }
    
    @CanIgnoreReturnValue
    static long checkNonNegative(final long value) {
        Preconditions.checkArgument(value >= 0L, "Not true that %s is non-negative.", value);
        return value;
    }
    
    @CanIgnoreReturnValue
    static long checkPositive(final long value) {
        Preconditions.checkArgument(value > 0L, "Not true that %s is positive.", value);
        return value;
    }
    
    private static class TransposedGraph<N> extends AbstractGraph<N> {
        private final Graph<N> graph;
        
        TransposedGraph(final Graph<N> graph) {
            this.graph = graph;
        }
        
        public Set<N> nodes() {
            return this.graph.nodes();
        }
        
        @Override
        protected long edgeCount() {
            return this.graph.edges().size();
        }
        
        public boolean isDirected() {
            return this.graph.isDirected();
        }
        
        public boolean allowsSelfLoops() {
            return this.graph.allowsSelfLoops();
        }
        
        public ElementOrder<N> nodeOrder() {
            return this.graph.nodeOrder();
        }
        
        public Set<N> adjacentNodes(final Object node) {
            return this.graph.adjacentNodes(node);
        }
        
        public Set<N> predecessors(final Object node) {
            return this.graph.successors(node);
        }
        
        public Set<N> successors(final Object node) {
            return this.graph.predecessors(node);
        }
    }
    
    private static class TransposedValueGraph<N, V> extends AbstractValueGraph<N, V> {
        private final ValueGraph<N, V> graph;
        
        TransposedValueGraph(final ValueGraph<N, V> graph) {
            this.graph = graph;
        }
        
        public Set<N> nodes() {
            return this.graph.nodes();
        }
        
        @Override
        protected long edgeCount() {
            return this.graph.edges().size();
        }
        
        public boolean isDirected() {
            return this.graph.isDirected();
        }
        
        public boolean allowsSelfLoops() {
            return this.graph.allowsSelfLoops();
        }
        
        public ElementOrder<N> nodeOrder() {
            return this.graph.nodeOrder();
        }
        
        public Set<N> adjacentNodes(final Object node) {
            return this.graph.adjacentNodes(node);
        }
        
        public Set<N> predecessors(final Object node) {
            return this.graph.successors(node);
        }
        
        public Set<N> successors(final Object node) {
            return this.graph.predecessors(node);
        }
        
        @Override
        public V edgeValue(final Object nodeU, final Object nodeV) {
            return this.graph.edgeValue(nodeV, nodeU);
        }
        
        @Override
        public V edgeValueOrDefault(final Object nodeU, final Object nodeV, @Nullable final V defaultValue) {
            return this.graph.edgeValueOrDefault(nodeV, nodeU, defaultValue);
        }
    }
    
    private static class TransposedNetwork<N, E> extends AbstractNetwork<N, E> {
        private final Network<N, E> network;
        
        TransposedNetwork(final Network<N, E> network) {
            this.network = network;
        }
        
        public Set<N> nodes() {
            return this.network.nodes();
        }
        
        public Set<E> edges() {
            return this.network.edges();
        }
        
        public boolean isDirected() {
            return this.network.isDirected();
        }
        
        public boolean allowsParallelEdges() {
            return this.network.allowsParallelEdges();
        }
        
        public boolean allowsSelfLoops() {
            return this.network.allowsSelfLoops();
        }
        
        public ElementOrder<N> nodeOrder() {
            return this.network.nodeOrder();
        }
        
        public ElementOrder<E> edgeOrder() {
            return this.network.edgeOrder();
        }
        
        public Set<N> adjacentNodes(final Object node) {
            return this.network.adjacentNodes(node);
        }
        
        public Set<N> predecessors(final Object node) {
            return this.network.successors(node);
        }
        
        public Set<N> successors(final Object node) {
            return this.network.predecessors(node);
        }
        
        public Set<E> incidentEdges(final Object node) {
            return this.network.incidentEdges(node);
        }
        
        public Set<E> inEdges(final Object node) {
            return this.network.outEdges(node);
        }
        
        public Set<E> outEdges(final Object node) {
            return this.network.inEdges(node);
        }
        
        public EndpointPair<N> incidentNodes(final Object edge) {
            final EndpointPair<N> endpointPair = this.network.incidentNodes(edge);
            return EndpointPair.<N>of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
        }
        
        @Override
        public Set<E> adjacentEdges(final Object edge) {
            return this.network.adjacentEdges(edge);
        }
        
        public Set<E> edgesConnecting(final Object nodeU, final Object nodeV) {
            return this.network.edgesConnecting(nodeV, nodeU);
        }
    }
    
    private enum NodeVisitState {
        PENDING, 
        COMPLETE;
    }
}
