package com.google.common.graph;

import java.util.Set;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;

@Beta
public final class ImmutableNetwork<N, E> extends ConfigurableNetwork<N, E> {
    private ImmutableNetwork(final Network<N, E> network) {
        super(NetworkBuilder.from(network), ImmutableNetwork.<N, E>getNodeConnections(network), ImmutableNetwork.<N, E>getEdgeToReferenceNode(network));
    }
    
    public static <N, E> ImmutableNetwork<N, E> copyOf(final Network<N, E> network) {
        return (network instanceof ImmutableNetwork) ? ((ImmutableNetwork)network) : new ImmutableNetwork<N, E>((Network<N, E>)network);
    }
    
    @Deprecated
    public static <N, E> ImmutableNetwork<N, E> copyOf(final ImmutableNetwork<N, E> network) {
        return Preconditions.<ImmutableNetwork<N, E>>checkNotNull(network);
    }
    
    @Override
    public ImmutableGraph<N> asGraph() {
        final Graph<N> asGraph = super.asGraph();
        return new ImmutableGraph<N>() {
            @Override
            protected Graph<N> delegate() {
                return asGraph;
            }
        };
    }
    
    private static <N, E> Map<N, NetworkConnections<N, E>> getNodeConnections(final Network<N, E> network) {
        final ImmutableMap.Builder<N, NetworkConnections<N, E>> nodeConnections = ImmutableMap.<N, NetworkConnections<N, E>>builder();
        for (final N node : network.nodes()) {
            nodeConnections.put(node, ImmutableNetwork.<N, E>connectionsOf(network, node));
        }
        return (Map<N, NetworkConnections<N, E>>)nodeConnections.build();
    }
    
    private static <N, E> Map<E, N> getEdgeToReferenceNode(final Network<N, E> network) {
        final ImmutableMap.Builder<E, N> edgeToReferenceNode = ImmutableMap.<E, N>builder();
        for (final E edge : network.edges()) {
            edgeToReferenceNode.put(edge, network.incidentNodes(edge).nodeU());
        }
        return (Map<E, N>)edgeToReferenceNode.build();
    }
    
    private static <N, E> NetworkConnections<N, E> connectionsOf(final Network<N, E> network, final N node) {
        if (network.isDirected()) {
            final Map<E, N> inEdgeMap = Maps.<E, N>asMap(network.inEdges(node), ImmutableNetwork.sourceNodeFn(network));
            final Map<E, N> outEdgeMap = Maps.<E, N>asMap(network.outEdges(node), ImmutableNetwork.targetNodeFn(network));
            final int selfLoopCount = network.edgesConnecting(node, node).size();
            return (NetworkConnections<N, E>)(network.allowsParallelEdges() ? DirectedMultiNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount) : DirectedNetworkConnections.<N, E>ofImmutable(inEdgeMap, outEdgeMap, selfLoopCount));
        }
        final Map<E, N> incidentEdgeMap = Maps.<E, N>asMap(network.incidentEdges(node), ImmutableNetwork.adjacentNodeFn(network, (V)node));
        return (NetworkConnections<N, E>)(network.allowsParallelEdges() ? UndirectedMultiNetworkConnections.<N, E>ofImmutable(incidentEdgeMap) : UndirectedNetworkConnections.<N, E>ofImmutable(incidentEdgeMap));
    }
    
    private static <N, E> Function<E, N> sourceNodeFn(final Network<N, E> network) {
        return new Function<E, N>() {
            public N apply(final E edge) {
                return network.incidentNodes(edge).source();
            }
        };
    }
    
    private static <N, E> Function<E, N> targetNodeFn(final Network<N, E> network) {
        return new Function<E, N>() {
            public N apply(final E edge) {
                return network.incidentNodes(edge).target();
            }
        };
    }
    
    private static <N, E> Function<E, N> adjacentNodeFn(final Network<N, E> network, final N node) {
        return new Function<E, N>() {
            public N apply(final E edge) {
                return network.incidentNodes(edge).adjacentNode(node);
            }
        };
    }
}
