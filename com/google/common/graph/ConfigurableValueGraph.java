package com.google.common.graph;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;

class ConfigurableValueGraph<N, V> extends AbstractValueGraph<N, V> {
    private final boolean isDirected;
    private final boolean allowsSelfLoops;
    private final ElementOrder<N> nodeOrder;
    protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
    protected long edgeCount;
    
    ConfigurableValueGraph(final AbstractGraphBuilder<? super N> builder) {
        this(builder, builder.nodeOrder.<Object, GraphConnections<Object, V>>createMap(builder.expectedNodeCount.or(10)), 0L);
    }
    
    ConfigurableValueGraph(final AbstractGraphBuilder<? super N> builder, final Map<N, GraphConnections<N, V>> nodeConnections, final long edgeCount) {
        this.isDirected = builder.directed;
        this.allowsSelfLoops = builder.allowsSelfLoops;
        this.nodeOrder = builder.nodeOrder.<N>cast();
        this.nodeConnections = ((nodeConnections instanceof TreeMap) ? new MapRetrievalCache<N, GraphConnections<N, V>>(nodeConnections) : new MapIteratorCache<N, GraphConnections<N, V>>(nodeConnections));
        this.edgeCount = Graphs.checkNonNegative(edgeCount);
    }
    
    public Set<N> nodes() {
        return this.nodeConnections.unmodifiableKeySet();
    }
    
    public boolean isDirected() {
        return this.isDirected;
    }
    
    public boolean allowsSelfLoops() {
        return this.allowsSelfLoops;
    }
    
    public ElementOrder<N> nodeOrder() {
        return this.nodeOrder;
    }
    
    public Set<N> adjacentNodes(final Object node) {
        return this.checkedConnections(node).adjacentNodes();
    }
    
    public Set<N> predecessors(final Object node) {
        return this.checkedConnections(node).predecessors();
    }
    
    public Set<N> successors(final Object node) {
        return this.checkedConnections(node).successors();
    }
    
    @Override
    public V edgeValueOrDefault(final Object nodeU, final Object nodeV, @Nullable final V defaultValue) {
        final GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
        if (connectionsU == null) {
            return defaultValue;
        }
        final V value = connectionsU.value(nodeV);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    @Override
    protected long edgeCount() {
        return this.edgeCount;
    }
    
    protected final GraphConnections<N, V> checkedConnections(final Object node) {
        final GraphConnections<N, V> connections = this.nodeConnections.get(node);
        if (connections == null) {
            Preconditions.checkNotNull(node);
            throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
        }
        return connections;
    }
    
    protected final boolean containsNode(@Nullable final Object node) {
        return this.nodeConnections.containsKey(node);
    }
}
