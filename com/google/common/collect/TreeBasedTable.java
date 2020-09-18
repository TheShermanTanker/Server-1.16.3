package com.google.common.collect;

import java.util.NoSuchElementException;
import java.io.Serializable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import com.google.common.base.Function;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import com.google.common.base.Supplier;
import java.util.SortedMap;
import java.util.TreeMap;
import com.google.common.base.Preconditions;
import java.util.Comparator;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(serializable = true)
public class TreeBasedTable<R, C, V> extends StandardRowSortedTable<R, C, V> {
    private final Comparator<? super C> columnComparator;
    private static final long serialVersionUID = 0L;
    
    public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
        return new TreeBasedTable<R, C, V>((java.util.Comparator<? super R>)Ordering.<Comparable>natural(), (java.util.Comparator<? super C>)Ordering.<Comparable>natural());
    }
    
    public static <R, C, V> TreeBasedTable<R, C, V> create(final Comparator<? super R> rowComparator, final Comparator<? super C> columnComparator) {
        Preconditions.<Comparator<? super R>>checkNotNull(rowComparator);
        Preconditions.<Comparator<? super C>>checkNotNull(columnComparator);
        return new TreeBasedTable<R, C, V>(rowComparator, columnComparator);
    }
    
    public static <R, C, V> TreeBasedTable<R, C, V> create(final TreeBasedTable<R, C, ? extends V> table) {
        final TreeBasedTable<R, C, V> result = new TreeBasedTable<R, C, V>(table.rowComparator(), table.columnComparator());
        result.putAll((Table)table);
        return result;
    }
    
    TreeBasedTable(final Comparator<? super R> rowComparator, final Comparator<? super C> columnComparator) {
        super((SortedMap)new TreeMap((Comparator)rowComparator), new Factory(columnComparator));
        this.columnComparator = columnComparator;
    }
    
    @Deprecated
    public Comparator<? super R> rowComparator() {
        return this.rowKeySet().comparator();
    }
    
    @Deprecated
    public Comparator<? super C> columnComparator() {
        return this.columnComparator;
    }
    
    public SortedMap<C, V> row(final R rowKey) {
        return (SortedMap<C, V>)new TreeRow(rowKey);
    }
    
    @Override
    public SortedSet<R> rowKeySet() {
        return super.rowKeySet();
    }
    
    @Override
    public SortedMap<R, Map<C, V>> rowMap() {
        return super.rowMap();
    }
    
    @Override
    Iterator<C> createColumnKeyIterator() {
        final Comparator<? super C> comparator = this.columnComparator();
        final Iterator<C> merged = Iterators.mergeSorted(Iterables.transform((java.lang.Iterable<Object>)this.backingMap.values(), new Function<Map<C, V>, Iterator<C>>() {
            public Iterator<C> apply(final Map<C, V> input) {
                return (Iterator<C>)input.keySet().iterator();
            }
        }), (java.util.Comparator<? super Object>)comparator);
        return (Iterator<C>)new AbstractIterator<C>() {
            C lastValue;
            
            @Override
            protected C computeNext() {
                while (merged.hasNext()) {
                    final C next = (C)merged.next();
                    final boolean duplicate = this.lastValue != null && comparator.compare(next, this.lastValue) == 0;
                    if (!duplicate) {
                        return this.lastValue = next;
                    }
                }
                this.lastValue = null;
                return this.endOfData();
            }
        };
    }
    
    private static class Factory<C, V> implements Supplier<TreeMap<C, V>>, Serializable {
        final Comparator<? super C> comparator;
        private static final long serialVersionUID = 0L;
        
        Factory(final Comparator<? super C> comparator) {
            this.comparator = comparator;
        }
        
        public TreeMap<C, V> get() {
            return (TreeMap<C, V>)new TreeMap((Comparator)this.comparator);
        }
    }
    
    private class TreeRow extends Row implements SortedMap<C, V> {
        @Nullable
        final C lowerBound;
        @Nullable
        final C upperBound;
        transient SortedMap<C, V> wholeRow;
        
        TreeRow(final TreeBasedTable treeBasedTable, final R rowKey) {
            this(treeBasedTable, rowKey, null, null);
        }
        
        TreeRow(@Nullable final R rowKey, @Nullable final C lowerBound, final C upperBound) {
            (StandardTable<R, C, V>)TreeBasedTable.this.super(rowKey);
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
            Preconditions.checkArgument(lowerBound == null || upperBound == null || this.compare(lowerBound, upperBound) <= 0);
        }
        
        public SortedSet<C> keySet() {
            return (SortedSet<C>)new Maps.SortedKeySet((java.util.SortedMap<Object, Object>)this);
        }
        
        public Comparator<? super C> comparator() {
            return TreeBasedTable.this.columnComparator();
        }
        
        int compare(final Object a, final Object b) {
            final Comparator<Object> cmp = (Comparator<Object>)this.comparator();
            return cmp.compare(a, b);
        }
        
        boolean rangeContains(@Nullable final Object o) {
            return o != null && (this.lowerBound == null || this.compare(this.lowerBound, o) <= 0) && (this.upperBound == null || this.compare(this.upperBound, o) > 0);
        }
        
        public SortedMap<C, V> subMap(final C fromKey, final C toKey) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.<C>checkNotNull(fromKey)) && this.rangeContains(Preconditions.<C>checkNotNull(toKey)));
            return (SortedMap<C, V>)new TreeRow((R)this.rowKey, fromKey, toKey);
        }
        
        public SortedMap<C, V> headMap(final C toKey) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.<C>checkNotNull(toKey)));
            return (SortedMap<C, V>)new TreeRow((R)this.rowKey, this.lowerBound, toKey);
        }
        
        public SortedMap<C, V> tailMap(final C fromKey) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.<C>checkNotNull(fromKey)));
            return (SortedMap<C, V>)new TreeRow((R)this.rowKey, fromKey, this.upperBound);
        }
        
        public C firstKey() {
            final SortedMap<C, V> backing = this.backingRowMap();
            if (backing == null) {
                throw new NoSuchElementException();
            }
            return (C)this.backingRowMap().firstKey();
        }
        
        public C lastKey() {
            final SortedMap<C, V> backing = this.backingRowMap();
            if (backing == null) {
                throw new NoSuchElementException();
            }
            return (C)this.backingRowMap().lastKey();
        }
        
        SortedMap<C, V> wholeRow() {
            if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
                this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey);
            }
            return this.wholeRow;
        }
        
        SortedMap<C, V> backingRowMap() {
            return (SortedMap<C, V>)super.backingRowMap();
        }
        
        SortedMap<C, V> computeBackingRowMap() {
            SortedMap<C, V> map = this.wholeRow();
            if (map != null) {
                if (this.lowerBound != null) {
                    map = (SortedMap<C, V>)map.tailMap(this.lowerBound);
                }
                if (this.upperBound != null) {
                    map = (SortedMap<C, V>)map.headMap(this.upperBound);
                }
                return map;
            }
            return null;
        }
        
        @Override
        void maintainEmptyInvariant() {
            if (this.wholeRow() != null && this.wholeRow.isEmpty()) {
                TreeBasedTable.this.backingMap.remove(this.rowKey);
                this.wholeRow = null;
                this.backingRowMap = null;
            }
        }
        
        @Override
        public boolean containsKey(final Object key) {
            return this.rangeContains(key) && super.containsKey(key);
        }
        
        @Override
        public V put(final C key, final V value) {
            Preconditions.checkArgument(this.rangeContains(Preconditions.<C>checkNotNull(key)));
            return super.put(key, value);
        }
    }
}
