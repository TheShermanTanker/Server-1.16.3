package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import com.google.common.annotations.Beta;

@Beta
public interface Network<N, E> {
    Set<N> nodes();
    
    Set<E> edges();
    
    Graph<N> asGraph();
    
    boolean isDirected();
    
    boolean allowsParallelEdges();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    ElementOrder<E> edgeOrder();
    
    Set<N> adjacentNodes(@CompatibleWith("N") final Object object);
    
    Set<N> predecessors(@CompatibleWith("N") final Object object);
    
    Set<N> successors(@CompatibleWith("N") final Object object);
    
    Set<E> incidentEdges(@CompatibleWith("N") final Object object);
    
    Set<E> inEdges(@CompatibleWith("N") final Object object);
    
    Set<E> outEdges(@CompatibleWith("N") final Object object);
    
    int degree(@CompatibleWith("N") final Object object);
    
    int inDegree(@CompatibleWith("N") final Object object);
    
    int outDegree(@CompatibleWith("N") final Object object);
    
    EndpointPair<N> incidentNodes(@CompatibleWith("E") final Object object);
    
    Set<E> adjacentEdges(@CompatibleWith("E") final Object object);
    
    Set<E> edgesConnecting(@CompatibleWith("N") final Object object1, @CompatibleWith("N") final Object object2);
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
}
