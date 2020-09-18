package com.google.common.collect;

import java.util.Collection;
import java.util.Set;
import java.util.Map;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public interface Table<R, C, V> {
    boolean contains(@Nullable @CompatibleWith("R") final Object object1, @Nullable @CompatibleWith("C") final Object object2);
    
    boolean containsRow(@Nullable @CompatibleWith("R") final Object object);
    
    boolean containsColumn(@Nullable @CompatibleWith("C") final Object object);
    
    boolean containsValue(@Nullable @CompatibleWith("V") final Object object);
    
    V get(@Nullable @CompatibleWith("R") final Object object1, @Nullable @CompatibleWith("C") final Object object2);
    
    boolean isEmpty();
    
    int size();
    
    boolean equals(@Nullable final Object object);
    
    int hashCode();
    
    void clear();
    
    @Nullable
    @CanIgnoreReturnValue
    V put(final R object1, final C object2, final V object3);
    
    void putAll(final Table<? extends R, ? extends C, ? extends V> table);
    
    @Nullable
    @CanIgnoreReturnValue
    V remove(@Nullable @CompatibleWith("R") final Object object1, @Nullable @CompatibleWith("C") final Object object2);
    
    Map<C, V> row(final R object);
    
    Map<R, V> column(final C object);
    
    Set<Cell<R, C, V>> cellSet();
    
    Set<R> rowKeySet();
    
    Set<C> columnKeySet();
    
    Collection<V> values();
    
    Map<R, Map<C, V>> rowMap();
    
    Map<C, Map<R, V>> columnMap();
    
    public interface Cell<R, C, V> {
        @Nullable
        R getRowKey();
        
        @Nullable
        C getColumnKey();
        
        @Nullable
        V getValue();
        
        boolean equals(@Nullable final Object object);
        
        int hashCode();
    }
}
