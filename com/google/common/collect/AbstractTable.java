package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.function.Function;
import java.util.Spliterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class AbstractTable<R, C, V> implements Table<R, C, V> {
    private transient Set<Cell<R, C, V>> cellSet;
    private transient Collection<V> values;
    
    public boolean containsRow(@Nullable final Object rowKey) {
        return Maps.safeContainsKey(this.rowMap(), rowKey);
    }
    
    public boolean containsColumn(@Nullable final Object columnKey) {
        return Maps.safeContainsKey(this.columnMap(), columnKey);
    }
    
    public Set<R> rowKeySet() {
        return (Set<R>)this.rowMap().keySet();
    }
    
    public Set<C> columnKeySet() {
        return (Set<C>)this.columnMap().keySet();
    }
    
    public boolean containsValue(@Nullable final Object value) {
        for (final Map<C, V> row : this.rowMap().values()) {
            if (row.containsValue(value)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean contains(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        final Map<C, V> row = Maps.safeGet(this.rowMap(), rowKey);
        return row != null && Maps.safeContainsKey(row, columnKey);
    }
    
    public V get(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        final Map<C, V> row = Maps.safeGet(this.rowMap(), rowKey);
        return (row == null) ? null : Maps.<V>safeGet(row, columnKey);
    }
    
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    public void clear() {
        Iterators.clear(this.cellSet().iterator());
    }
    
    @CanIgnoreReturnValue
    public V remove(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        final Map<C, V> row = Maps.safeGet(this.rowMap(), rowKey);
        return (row == null) ? null : Maps.<V>safeRemove(row, columnKey);
    }
    
    @CanIgnoreReturnValue
    public V put(final R rowKey, final C columnKey, final V value) {
        return (V)this.row(rowKey).put(columnKey, value);
    }
    
    public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
        for (final Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
            this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
        }
    }
    
    public Set<Cell<R, C, V>> cellSet() {
        final Set<Cell<R, C, V>> result = this.cellSet;
        return (result == null) ? (this.cellSet = this.createCellSet()) : result;
    }
    
    Set<Cell<R, C, V>> createCellSet() {
        return (Set<Cell<R, C, V>>)new CellSet();
    }
    
    abstract Iterator<Cell<R, C, V>> cellIterator();
    
    abstract Spliterator<Cell<R, C, V>> cellSpliterator();
    
    public Collection<V> values() {
        final Collection<V> result = this.values;
        return (result == null) ? (this.values = this.createValues()) : result;
    }
    
    Collection<V> createValues() {
        return (Collection<V>)new Values();
    }
    
    Iterator<V> valuesIterator() {
        return (Iterator<V>)new TransformedIterator<Cell<R, C, V>, V>(this.cellSet().iterator()) {
            @Override
            V transform(final Cell<R, C, V> cell) {
                return cell.getValue();
            }
        };
    }
    
    Spliterator<V> valuesSpliterator() {
        return CollectSpliterators.<Cell<R, C, V>, V>map(this.cellSpliterator(), (java.util.function.Function<? super Cell<R, C, V>, ? extends V>)Cell::getValue);
    }
    
    public boolean equals(@Nullable final Object obj) {
        return Tables.equalsImpl(this, obj);
    }
    
    public int hashCode() {
        return this.cellSet().hashCode();
    }
    
    public String toString() {
        return this.rowMap().toString();
    }
    
    class CellSet extends AbstractSet<Cell<R, C, V>> {
        public boolean contains(final Object o) {
            if (o instanceof Cell) {
                final Cell<?, ?, ?> cell = o;
                final Map<C, V> row = Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
                return row != null && Collections2.safeContains(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()));
            }
            return false;
        }
        
        public boolean remove(@Nullable final Object o) {
            if (o instanceof Cell) {
                final Cell<?, ?, ?> cell = o;
                final Map<C, V> row = Maps.safeGet(AbstractTable.this.rowMap(), cell.getRowKey());
                return row != null && Collections2.safeRemove(row.entrySet(), Maps.immutableEntry(cell.getColumnKey(), cell.getValue()));
            }
            return false;
        }
        
        public void clear() {
            AbstractTable.this.clear();
        }
        
        public Iterator<Cell<R, C, V>> iterator() {
            return AbstractTable.this.cellIterator();
        }
        
        public Spliterator<Cell<R, C, V>> spliterator() {
            return AbstractTable.this.cellSpliterator();
        }
        
        public int size() {
            return AbstractTable.this.size();
        }
    }
    
    class Values extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return AbstractTable.this.valuesIterator();
        }
        
        public boolean contains(final Object o) {
            return AbstractTable.this.containsValue(o);
        }
        
        public void clear() {
            AbstractTable.this.clear();
        }
        
        public int size() {
            return AbstractTable.this.size();
        }
    }
}
