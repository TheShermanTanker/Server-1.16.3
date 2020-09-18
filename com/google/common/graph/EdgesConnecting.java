package com.google.common.graph;

import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.collect.Iterators;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.AbstractSet;

final class EdgesConnecting<E> extends AbstractSet<E> {
    private final Map<?, E> nodeToOutEdge;
    private final Object targetNode;
    
    EdgesConnecting(final Map<?, E> nodeToEdgeMap, final Object targetNode) {
        this.nodeToOutEdge = Preconditions.<Map<?, E>>checkNotNull(nodeToEdgeMap);
        this.targetNode = Preconditions.checkNotNull(targetNode);
    }
    
    public UnmodifiableIterator<E> iterator() {
        final E connectingEdge = this.getConnectingEdge();
        return (connectingEdge == null) ? ImmutableSet.<E>of().iterator() : Iterators.<E>singletonIterator(connectingEdge);
    }
    
    public int size() {
        return (this.getConnectingEdge() != null) ? 1 : 0;
    }
    
    public boolean contains(@Nullable final Object edge) {
        final E connectingEdge = this.getConnectingEdge();
        return connectingEdge != null && connectingEdge.equals(edge);
    }
    
    @Nullable
    private E getConnectingEdge() {
        return (E)this.nodeToOutEdge.get(this.targetNode);
    }
}
