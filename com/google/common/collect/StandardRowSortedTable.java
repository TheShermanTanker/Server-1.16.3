package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import com.google.common.base.Supplier;
import java.util.Map;
import java.util.SortedMap;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class StandardRowSortedTable<R, C, V> extends StandardTable<R, C, V> implements RowSortedTable<R, C, V> {
    private static final long serialVersionUID = 0L;
    
    StandardRowSortedTable(final SortedMap<R, Map<C, V>> backingMap, final Supplier<? extends Map<C, V>> factory) {
        super((Map)backingMap, factory);
    }
    
    private SortedMap<R, Map<C, V>> sortedBackingMap() {
        return (SortedMap<R, Map<C, V>>)this.backingMap;
    }
    
    @Override
    public SortedSet<R> rowKeySet() {
        return (SortedSet<R>)this.rowMap().keySet();
    }
    
    @Override
    public SortedMap<R, Map<C, V>> rowMap() {
        return (SortedMap<R, Map<C, V>>)super.rowMap();
    }
    
    SortedMap<R, Map<C, V>> createRowMap() {
        return (SortedMap<R, Map<C, V>>)new RowSortedMap();
    }
    
    private class RowSortedMap extends RowMap implements SortedMap<R, Map<C, V>> {
        public SortedSet<R> keySet() {
            return (SortedSet<R>)super.keySet();
        }
        
        SortedSet<R> createKeySet() {
            return (SortedSet<R>)new Maps.SortedKeySet((java.util.SortedMap<Object, Object>)this);
        }
        
        public Comparator<? super R> comparator() {
            return StandardRowSortedTable.this.sortedBackingMap().comparator();
        }
        
        public R firstKey() {
            return (R)StandardRowSortedTable.this.sortedBackingMap().firstKey();
        }
        
        public R lastKey() {
            return (R)StandardRowSortedTable.this.sortedBackingMap().lastKey();
        }
        
        public SortedMap<R, Map<C, V>> headMap(final R toKey) {
            Preconditions.<R>checkNotNull(toKey);
            return new StandardRowSortedTable((java.util.SortedMap<Object, java.util.Map<Object, Object>>)StandardRowSortedTable.this.sortedBackingMap().headMap(toKey), StandardRowSortedTable.this.factory).rowMap();
        }
        
        public SortedMap<R, Map<C, V>> subMap(final R fromKey, final R toKey) {
            Preconditions.<R>checkNotNull(fromKey);
            Preconditions.<R>checkNotNull(toKey);
            return new StandardRowSortedTable((java.util.SortedMap<Object, java.util.Map<Object, Object>>)StandardRowSortedTable.this.sortedBackingMap().subMap(fromKey, toKey), StandardRowSortedTable.this.factory).rowMap();
        }
        
        public SortedMap<R, Map<C, V>> tailMap(final R fromKey) {
            Preconditions.<R>checkNotNull(fromKey);
            return new StandardRowSortedTable((java.util.SortedMap<Object, java.util.Map<Object, Object>>)StandardRowSortedTable.this.sortedBackingMap().tailMap(fromKey), StandardRowSortedTable.this.factory).rowMap();
        }
    }
}
