package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import com.google.common.annotations.Beta;

@Beta
public interface Graph<N> {
    Set<N> nodes();
    
    Set<EndpointPair<N>> edges();
    
    boolean isDirected();
    
    boolean allowsSelfLoops();
    
    ElementOrder<N> nodeOrder();
    
    Set<N> adjacentNodes(@CompatibleWith("N") final Object object);
    
    Set<N> predecessors(@CompatibleWith("N") final Object object);
    
    Set<N> successors(@CompatibleWith("N") final Object object);
    
    int degree(@CompatibleWith("N") final Object object);
    
    int inDegree(@CompatibleWith("N") final Object object);
    
    int outDegree(@CompatibleWith("N") final Object object);
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
}
