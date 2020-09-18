package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

class ConfigurableNetwork<N, E> extends AbstractNetwork<N, E> {
    private final boolean isDirected;
    private final boolean allowsParallelEdges;
    private final boolean allowsSelfLoops;
    private final ElementOrder<N> nodeOrder;
    private final ElementOrder<E> edgeOrder;
    protected final MapIteratorCache<N, NetworkConnections<N, E>> nodeConnections;
    protected final MapIteratorCache<E, N> edgeToReferenceNode;
    
    ConfigurableNetwork(final NetworkBuilder<? super N, ? super E> builder) {
        this(builder, builder.nodeOrder.<Object, NetworkConnections<Object, Object>>createMap(builder.expectedNodeCount.or(10)), builder.edgeOrder.createMap(builder.expectedEdgeCount.or(20)));
    }
    
    ConfigurableNetwork(final NetworkBuilder<? super N, ? super E> builder, final Map<N, NetworkConnections<N, E>> nodeConnections, final Map<E, N> edgeToReferenceNode) {
        this.isDirected = builder.directed;
        this.allowsParallelEdges = builder.allowsParallelEdges;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = builder.nodeOrder.<N>cast();
        this.edgeOrder = builder.edgeOrder.<E>cast();
        this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache<N, NetworkConnections<N, E>>(nodeConnections) : new MapIteratorCache<N, NetworkConnections<N, E>>(nodeConnections));
        this.edgeToReferenceNode = new MapIteratorCache<E, N>(edgeToReferenceNode);
    }
    
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }
    
    public Set<E> edges() {
        return this.edgeToReferenceNode.unmodifiableKeySet();
    }
    
    public boolean isDirected() {
        return this.isDirected;
    }
    
    public boolean allowsParallelEdges() {
        return this.allowsParallelEdges;
    }
    
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }
    
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }
    
    public ElementOrder<E> edgeOrder() {
        return this.edgeOrder;
    }
    
    public Set<E> incidentEdges(final Object node) {
        return this.checkedConnections(node).incidentEdges();
    }
    
    public EndpointPair<N> incidentNodes(final Object edge) {
        final N nodeU = this.checkedReferenceNode(edge);
        final N nodeV = this.nodeConnections.get(nodeU).oppositeNode(edge);
        return EndpointPair.<N>of(this, nodeU, nodeV);
    }
    
    public Set<N> adjacentNodes(final Object node) {
        return this.checkedConnections(node).adjacentNodes();
    }
    
    public Set<E> edgesConnecting(final Object nodeU, final Object nodeV) {
        final NetworkConnections<N, E> connectionsU = this.checkedConnections(nodeU);
        if (!this.allowsSelfLoops && nodeU == nodeV) {
            return ImmutableSet.of();
        }
        Preconditions.checkArgument(this.containsNode(nodeV), "Node %s is not an element of this graph.", nodeV);
        return connectionsU.edgesConnecting(nodeV);
    }
    
    public Set<E> inEdges(final Object node) {
        return this.checkedConnections(node).inEdges();
    }
    
    public Set<E> outEdges(final Object node) {
        return this.checkedConnections(node).outEdges();
    }
    
    public Set<N> predecessors(final Object node) {
        return this.checkedConnections(node).predecessors();
    }
    
    public Set<N> successors(final Object node) {
        return this.checkedConnections(node).successors();
    }
    
    protected final NetworkConnections<N, E> checkedConnections(final Object node) {
        final NetworkConnections<N, E> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
        }
        return connections;
    }
    
    protected final N checkedReferenceNode(final Object edge) {
        final N referenceNode = this.edgeToReferenceNode.get(edge);
        if (referenceNode == null) {
            Preconditions.checkNotNull(edge);
            throw new IllegalArgumentException(String.format("Edge %s is not an element of this graph.", new Object[] { edge }));
        }
        return referenceNode;
    }
    
    protected final boolean containsNode(@Nullable final Object node) {
        return this.nodeConnections.containsKey(node);
    }
    
    protected final boolean containsEdge(@Nullable final Object edge) {
        return this.edgeToReferenceNode.containsKey(edge);
    }
}
