package com.google.common.graph;

import javax.annotation.Nullable;
import com.google.errorprone.annotations.CompatibleWith;
import com.google.common.annotations.Beta;

@Beta
public interface ValueGraph<N, V> extends Graph<N> {
    V edgeValue(@CompatibleWith("N") final Object object1, @CompatibleWith("N") final Object object2);
    
    V edgeValueOrDefault(@CompatibleWith("N") final Object object1, @CompatibleWith("N") final Object object2, @Nullable final V object3);
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
}
