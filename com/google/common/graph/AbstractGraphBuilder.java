package com.google.common.graph;

import com.google.common.base.Optional;

abstract class AbstractGraphBuilder<N> {
    final boolean directed;
    boolean allowsSelfLoops;
    ElementOrder<N> nodeOrder;
    Optional<Integer> expectedNodeCount;
    
    AbstractGraphBuilder(final boolean directed) {
        this.allowsSelfLoops = false;
        this.nodeOrder = ElementOrder.<N>insertion();
        this.expectedNodeCount = Optional.<Integer>absent();
        this.directed = directed;
    }
}
