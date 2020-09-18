package com.google.common.collect;

import java.util.Map;
import java.util.function.IntFunction;
import java.util.Spliterator;
import java.util.Iterator;
import java.util.Set;
import com.google.common.base.Objects;
import java.util.Arrays;
import com.google.common.annotations.GwtIncompatible;
import java.lang.reflect.Array;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;
import java.util.Collection;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@Beta
@GwtCompatible(emulated = true)
public final class ArrayTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
    private final ImmutableList<R> rowList;
    private final ImmutableList<C> columnList;
    private final ImmutableMap<R, Integer> rowKeyToIndex;
    private final ImmutableMap<C, Integer> columnKeyToIndex;
    private final V[][] array;
    private transient ColumnMap columnMap;
    private transient RowMap rowMap;
    private static final long serialVersionUID = 0L;
    
    public static <R, C, V> ArrayTable<R, C, V> create(final Iterable<? extends R> rowKeys, final Iterable<? extends C> columnKeys) {
        return new ArrayTable<R, C, V>(rowKeys, columnKeys);
    }
    
    public static <R, C, V> ArrayTable<R, C, V> create(final Table<R, C, V> table) {
        return (table instanceof ArrayTable) ? new ArrayTable<R, C, V>((ArrayTable)table) : new ArrayTable<R, C, V>(table);
    }
    
    private ArrayTable(final Iterable<? extends R> rowKeys, final Iterable<? extends C> columnKeys) {
        this.rowList = ImmutableList.<R>copyOf(rowKeys);
        this.columnList = ImmutableList.<C>copyOf(columnKeys);
        Preconditions.checkArgument(!this.rowList.isEmpty());
        Preconditions.checkArgument(!this.columnList.isEmpty());
        this.rowKeyToIndex = Maps.<R>indexMap((java.util.Collection<R>)this.rowList);
        this.columnKeyToIndex = Maps.<C>indexMap((java.util.Collection<C>)this.columnList);
        final V[][] tmpArray = (V[][])new Object[this.rowList.size()][this.columnList.size()];
        this.array = tmpArray;
        this.eraseAll();
    }
    
    private ArrayTable(final Table<R, C, V> table) {
        this((Iterable)table.rowKeySet(), (Iterable)table.columnKeySet());
        this.putAll(table);
    }
    
    private ArrayTable(final ArrayTable<R, C, V> table) {
        this.rowList = table.rowList;
        this.columnList = table.columnList;
        this.rowKeyToIndex = table.rowKeyToIndex;
        this.columnKeyToIndex = table.columnKeyToIndex;
        final V[][] copy = (V[][])new Object[this.rowList.size()][this.columnList.size()];
        this.array = copy;
        this.eraseAll();
        for (int i = 0; i < this.rowList.size(); ++i) {
            System.arraycopy(table.array[i], 0, copy[i], 0, table.array[i].length);
        }
    }
    
    public ImmutableList<R> rowKeyList() {
        return this.rowList;
    }
    
    public ImmutableList<C> columnKeyList() {
        return this.columnList;
    }
    
    public V at(final int rowIndex, final int columnIndex) {
        Preconditions.checkElementIndex(rowIndex, this.rowList.size());
        Preconditions.checkElementIndex(columnIndex, this.columnList.size());
        return this.array[rowIndex][columnIndex];
    }
    
    @CanIgnoreReturnValue
    public V set(final int rowIndex, final int columnIndex, @Nullable final V value) {
        Preconditions.checkElementIndex(rowIndex, this.rowList.size());
        Preconditions.checkElementIndex(columnIndex, this.columnList.size());
        final V oldValue = this.array[rowIndex][columnIndex];
        this.array[rowIndex][columnIndex] = value;
        return oldValue;
    }
    
    @GwtIncompatible
    public V[][] toArray(final Class<V> valueClass) {
        final V[][] copy = (V[][])Array.newInstance((Class)valueClass, new int[] { this.rowList.size(), this.columnList.size() });
        for (int i = 0; i < this.rowList.size(); ++i) {
            System.arraycopy(this.array[i], 0, copy[i], 0, this.array[i].length);
        }
        return copy;
    }
    
    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    public void eraseAll() {
        for (final V[] row : this.array) {
            Arrays.fill((Object[])row, null);
        }
    }
    
    @Override
    public boolean contains(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        return this.containsRow(rowKey) && this.containsColumn(columnKey);
    }
    
    @Override
    public boolean containsColumn(@Nullable final Object columnKey) {
        return this.columnKeyToIndex.containsKey(columnKey);
    }
    
    @Override
    public boolean containsRow(@Nullable final Object rowKey) {
        return this.rowKeyToIndex.containsKey(rowKey);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        for (final V[] array2 : this.array) {
            final V[] row = array2;
            for (final V element : array2) {
                if (Objects.equal(value, element)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public V get(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        final Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        final Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        return (rowIndex == null || columnIndex == null) ? null : this.at(rowIndex, columnIndex);
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @CanIgnoreReturnValue
    @Override
    public V put(final R rowKey, final C columnKey, @Nullable final V value) {
        Preconditions.<R>checkNotNull(rowKey);
        Preconditions.<C>checkNotNull(columnKey);
        final Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        Preconditions.checkArgument(rowIndex != null, "Row %s not in %s", rowKey, this.rowList);
        final Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        Preconditions.checkArgument(columnIndex != null, "Column %s not in %s", columnKey, this.columnList);
        return this.set(rowIndex, columnIndex, value);
    }
    
    @Override
    public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
        super.putAll(table);
    }
    
    @Deprecated
    @CanIgnoreReturnValue
    @Override
    public V remove(final Object rowKey, final Object columnKey) {
        throw new UnsupportedOperationException();
    }
    
    @CanIgnoreReturnValue
    public V erase(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        final Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        final Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        if (rowIndex == null || columnIndex == null) {
            return null;
        }
        return this.set(rowIndex, columnIndex, null);
    }
    
    public int size() {
        return this.rowList.size() * this.columnList.size();
    }
    
    @Override
    public Set<Table.Cell<R, C, V>> cellSet() {
        return super.cellSet();
    }
    
    @Override
    Iterator<Table.Cell<R, C, V>> cellIterator() {
        return (Iterator<Table.Cell<R, C, V>>)new AbstractIndexedListIterator<Table.Cell<R, C, V>>(this.size()) {
            @Override
            protected Table.Cell<R, C, V> get(final int index) {
                return (Table.Cell<R, C, V>)ArrayTable.this.getCell(index);
            }
        };
    }
    
    @Override
    Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
        return CollectSpliterators.<Table.Cell<R, C, V>>indexed(this.size(), 273, (java.util.function.IntFunction<Table.Cell<R, C, V>>)this::getCell);
    }
    
    private Table.Cell<R, C, V> getCell(final int index) {
        return new Tables.AbstractCell<R, C, V>() {
            final int rowIndex = index / ArrayTable.this.columnList.size();
            final int columnIndex = index % ArrayTable.this.columnList.size();
            
            public R getRowKey() {
                return (R)ArrayTable.this.rowList.get(this.rowIndex);
            }
            
            public C getColumnKey() {
                return (C)ArrayTable.this.columnList.get(this.columnIndex);
            }
            
            public V getValue() {
                return ArrayTable.this.at(this.rowIndex, this.columnIndex);
            }
        };
    }
    
    private V getValue(final int index) {
        final int rowIndex = index / this.columnList.size();
        final int columnIndex = index % this.columnList.size();
        return this.at(rowIndex, columnIndex);
    }
    
    public Map<R, V> column(final C columnKey) {
        Preconditions.<C>checkNotNull(columnKey);
        final Integer columnIndex = this.columnKeyToIndex.get(columnKey);
        return (Map<R, V>)((columnIndex == null) ? ImmutableMap.of() : new Column(columnIndex));
    }
    
    public ImmutableSet<C> columnKeySet() {
        return this.columnKeyToIndex.keySet();
    }
    
    public Map<C, Map<R, V>> columnMap() {
        final ColumnMap map = this.columnMap;
        return (Map<C, Map<R, V>>)((map == null) ? (this.columnMap = new ColumnMap()) : map);
    }
    
    public Map<C, V> row(final R rowKey) {
        Preconditions.<R>checkNotNull(rowKey);
        final Integer rowIndex = this.rowKeyToIndex.get(rowKey);
        return (Map<C, V>)((rowIndex == null) ? ImmutableMap.of() : new Row(rowIndex));
    }
    
    public ImmutableSet<R> rowKeySet() {
        return this.rowKeyToIndex.keySet();
    }
    
    public Map<R, Map<C, V>> rowMap() {
        final RowMap map = this.rowMap;
        return (Map<R, Map<C, V>>)((map == null) ? (this.rowMap = new RowMap()) : map);
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    @Override
    Iterator<V> valuesIterator() {
        return (Iterator<V>)new AbstractIndexedListIterator<V>(this.size()) {
            @Override
            protected V get(final int index) {
                return (V)ArrayTable.this.getValue(index);
            }
        };
    }
    
    @Override
    Spliterator<V> valuesSpliterator() {
        return CollectSpliterators.<V>indexed(this.size(), 16, (java.util.function.IntFunction<V>)this::getValue);
    }
    
    private abstract static class ArrayMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> {
        private final ImmutableMap<K, Integer> keyIndex;
        
        private ArrayMap(final ImmutableMap<K, Integer> keyIndex) {
            this.keyIndex = keyIndex;
        }
        
        public Set<K> keySet() {
            return (Set<K>)this.keyIndex.keySet();
        }
        
        K getKey(final int index) {
            return (K)this.keyIndex.keySet().asList().get(index);
        }
        
        abstract String getKeyRole();
        
        @Nullable
        abstract V getValue(final int integer);
        
        @Nullable
        abstract V setValue(final int integer, final V object);
        
        @Override
        public int size() {
            return this.keyIndex.size();
        }
        
        public boolean isEmpty() {
            return this.keyIndex.isEmpty();
        }
        
        Map.Entry<K, V> getEntry(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return (Map.Entry<K, V>)new AbstractMapEntry<K, V>() {
                @Override
                public K getKey() {
                    return ArrayMap.this.getKey(index);
                }
                
                @Override
                public V getValue() {
                    return ArrayMap.this.getValue(index);
                }
                
                @Override
                public V setValue(final V value) {
                    return ArrayMap.this.setValue(index, value);
                }
            };
        }
        
        @Override
        Iterator<Map.Entry<K, V>> entryIterator() {
            return (Iterator<Map.Entry<K, V>>)new AbstractIndexedListIterator<Map.Entry<K, V>>(this.size()) {
                @Override
                protected Map.Entry<K, V> get(final int index) {
                    return ArrayMap.this.getEntry(index);
                }
            };
        }
        
        @Override
        Spliterator<Map.Entry<K, V>> entrySpliterator() {
            return CollectSpliterators.<Map.Entry<K, V>>indexed(this.size(), 16, (java.util.function.IntFunction<Map.Entry<K, V>>)this::getEntry);
        }
        
        public boolean containsKey(@Nullable final Object key) {
            return this.keyIndex.containsKey(key);
        }
        
        public V get(@Nullable final Object key) {
            final Integer index = this.keyIndex.get(key);
            if (index == null) {
                return null;
            }
            return this.getValue(index);
        }
        
        public V put(final K key, final V value) {
            final Integer index = this.keyIndex.get(key);
            if (index == null) {
                throw new IllegalArgumentException(this.getKeyRole() + " " + key + " not in " + this.keyIndex.keySet());
            }
            return this.setValue(index, value);
        }
        
        public V remove(final Object key) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }
    
    private class Column extends ArrayMap<R, V> {
        final int columnIndex;
        
        Column(final int columnIndex) {
            super(ArrayTable.this.rowKeyToIndex);
            this.columnIndex = columnIndex;
        }
        
        @Override
        String getKeyRole() {
            return "Row";
        }
        
        @Override
        V getValue(final int index) {
            return ArrayTable.this.at(index, this.columnIndex);
        }
        
        @Override
        V setValue(final int index, final V newValue) {
            return ArrayTable.this.set(index, this.columnIndex, newValue);
        }
    }
    
    private class ColumnMap extends ArrayMap<C, Map<R, V>> {
        private ColumnMap() {
            super(ArrayTable.this.columnKeyToIndex);
        }
        
        @Override
        String getKeyRole() {
            return "Column";
        }
        
        @Override
        Map<R, V> getValue(final int index) {
            return (Map<R, V>)new Column(index);
        }
        
        @Override
        Map<R, V> setValue(final int index, final Map<R, V> newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<R, V> put(final C key, final Map<R, V> value) {
            throw new UnsupportedOperationException();
        }
    }
    
    private class Row extends ArrayMap<C, V> {
        final int rowIndex;
        
        Row(final int rowIndex) {
            super(ArrayTable.this.columnKeyToIndex);
            this.rowIndex = rowIndex;
        }
        
        @Override
        String getKeyRole() {
            return "Column";
        }
        
        @Override
        V getValue(final int index) {
            return ArrayTable.this.at(this.rowIndex, index);
        }
        
        @Override
        V setValue(final int index, final V newValue) {
            return ArrayTable.this.set(this.rowIndex, index, newValue);
        }
    }
    
    private class RowMap extends ArrayMap<R, Map<C, V>> {
        private RowMap() {
            super(ArrayTable.this.rowKeyToIndex);
        }
        
        @Override
        String getKeyRole() {
            return "Row";
        }
        
        @Override
        Map<C, V> getValue(final int index) {
            return (Map<C, V>)new Row(index);
        }
        
        @Override
        Map<C, V> setValue(final int index, final Map<C, V> newValue) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<C, V> put(final R key, final Map<C, V> value) {
            throw new UnsupportedOperationException();
        }
    }
}