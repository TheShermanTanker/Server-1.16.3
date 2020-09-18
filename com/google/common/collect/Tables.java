package com.google.common.collect;

import java.util.SortedSet;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.Collection;
import java.util.Set;
import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.annotations.Beta;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.function.Supplier;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Tables {
    private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER;
    
    private Tables() {
    }
    
    @Beta
    public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final java.util.function.Function<? super T, ? extends R> rowFunction, final java.util.function.Function<? super T, ? extends C> columnFunction, final java.util.function.Function<? super T, ? extends V> valueFunction, final Supplier<I> tableSupplier) {
        return Tables.<T, Object, Object, Object, I>toTable((java.util.function.Function<? super T, ?>)rowFunction, (java.util.function.Function<? super T, ?>)columnFunction, (java.util.function.Function<? super T, ?>)valueFunction, (java.util.function.BinaryOperator<Object>)((v1, v2) -> {
            throw new IllegalStateException(new StringBuilder().append("Conflicting values ").append(v1).append(" and ").append(v2).toString());
        }), tableSupplier);
    }
    
    public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(final java.util.function.Function<? super T, ? extends R> rowFunction, final java.util.function.Function<? super T, ? extends C> columnFunction, final java.util.function.Function<? super T, ? extends V> valueFunction, final BinaryOperator<V> mergeFunction, final Supplier<I> tableSupplier) {
        Preconditions.<java.util.function.Function<? super T, ? extends R>>checkNotNull(rowFunction);
        Preconditions.<java.util.function.Function<? super T, ? extends C>>checkNotNull(columnFunction);
        Preconditions.<java.util.function.Function<? super T, ? extends V>>checkNotNull(valueFunction);
        Preconditions.<BinaryOperator<V>>checkNotNull(mergeFunction);
        Preconditions.<Supplier<I>>checkNotNull(tableSupplier);
        return Collector.of((Supplier)tableSupplier, (table, input) -> Tables.merge(table, rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), (java.util.function.BinaryOperator<Object>)mergeFunction), (table1, table2) -> {
            for (final Table.Cell<R, C, V> cell2 : table2.cellSet()) {
                Tables.<R, C, V>merge(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), (java.util.function.BinaryOperator<V>)mergeFunction);
            }
            return table1;
        }, new Collector.Characteristics[0]);
    }
    
    private static <R, C, V> void merge(final Table<R, C, V> table, final R row, final C column, final V value, final BinaryOperator<V> mergeFunction) {
        Preconditions.<V>checkNotNull(value);
        final V oldValue = table.get(row, column);
        if (oldValue == null) {
            table.put(row, column, value);
        }
        else {
            final V newValue = (V)mergeFunction.apply(oldValue, value);
            if (newValue == null) {
                table.remove(row, column);
            }
            else {
                table.put(row, column, newValue);
            }
        }
    }
    
    public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable final R rowKey, @Nullable final C columnKey, @Nullable final V value) {
        return new ImmutableCell<R, C, V>(rowKey, columnKey, value);
    }
    
    public static <R, C, V> Table<C, R, V> transpose(final Table<R, C, V> table) {
        return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable<C, R, V>((Table<Object, Object, Object>)table);
    }
    
    @Beta
    public static <R, C, V> Table<R, C, V> newCustomTable(final Map<R, Map<C, V>> backingMap, final com.google.common.base.Supplier<? extends Map<C, V>> factory) {
        Preconditions.checkArgument(backingMap.isEmpty());
        Preconditions.<com.google.common.base.Supplier<? extends Map<C, V>>>checkNotNull(factory);
        return new StandardTable<R, C, V>(backingMap, factory);
    }
    
    @Beta
    public static <R, C, V1, V2> Table<R, C, V2> transformValues(final Table<R, C, V1> fromTable, final Function<? super V1, V2> function) {
        return (Table<R, C, V2>)new TransformedTable((Table<Object, Object, Object>)fromTable, function);
    }
    
    public static <R, C, V> Table<R, C, V> unmodifiableTable(final Table<? extends R, ? extends C, ? extends V> table) {
        return new UnmodifiableTable<R, C, V>(table);
    }
    
    @Beta
    public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(final RowSortedTable<R, ? extends C, ? extends V> table) {
        return new UnmodifiableRowSortedMap<R, C, V>(table);
    }
    
    private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
        return (Function<Map<K, V>, Map<K, V>>)Tables.UNMODIFIABLE_WRAPPER;
    }
    
    static boolean equalsImpl(final Table<?, ?, ?> table, @Nullable final Object obj) {
        if (obj == table) {
            return true;
        }
        if (obj instanceof Table) {
            final Table<?, ?, ?> that = obj;
            return table.cellSet().equals(that.cellSet());
        }
        return false;
    }
    
    static {
        UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>() {
            public Map<Object, Object> apply(final Map<Object, Object> input) {
                return (Map<Object, Object>)Collections.unmodifiableMap((Map)input);
            }
        };
    }
    
    static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable {
        private final R rowKey;
        private final C columnKey;
        private final V value;
        private static final long serialVersionUID = 0L;
        
        ImmutableCell(@Nullable final R rowKey, @Nullable final C columnKey, @Nullable final V value) {
            this.rowKey = rowKey;
            this.columnKey = columnKey;
            this.value = value;
        }
        
        public R getRowKey() {
            return this.rowKey;
        }
        
        public C getColumnKey() {
            return this.columnKey;
        }
        
        public V getValue() {
            return this.value;
        }
    }
    
    abstract static class AbstractCell<R, C, V> implements Table.Cell<R, C, V> {
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Table.Cell) {
                final Table.Cell<?, ?, ?> other = obj;
                return Objects.equal(this.getRowKey(), other.getRowKey()) && Objects.equal(this.getColumnKey(), other.getColumnKey()) && Objects.equal(this.getValue(), other.getValue());
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hashCode(this.getRowKey(), this.getColumnKey(), this.getValue());
        }
        
        public String toString() {
            return new StringBuilder().append("(").append(this.getRowKey()).append(",").append(this.getColumnKey()).append(")=").append(this.getValue()).toString();
        }
    }
    
    private static class TransposeTable<C, R, V> extends AbstractTable<C, R, V> {
        final Table<R, C, V> original;
        private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL;
        
        TransposeTable(final Table<R, C, V> original) {
            this.original = Preconditions.<Table<R, C, V>>checkNotNull(original);
        }
        
        @Override
        public void clear() {
            this.original.clear();
        }
        
        public Map<C, V> column(final R columnKey) {
            return this.original.row(columnKey);
        }
        
        @Override
        public Set<R> columnKeySet() {
            return this.original.rowKeySet();
        }
        
        public Map<R, Map<C, V>> columnMap() {
            return this.original.rowMap();
        }
        
        @Override
        public boolean contains(@Nullable final Object rowKey, @Nullable final Object columnKey) {
            return this.original.contains(columnKey, rowKey);
        }
        
        @Override
        public boolean containsColumn(@Nullable final Object columnKey) {
            return this.original.containsRow(columnKey);
        }
        
        @Override
        public boolean containsRow(@Nullable final Object rowKey) {
            return this.original.containsColumn(rowKey);
        }
        
        @Override
        public boolean containsValue(@Nullable final Object value) {
            return this.original.containsValue(value);
        }
        
        @Override
        public V get(@Nullable final Object rowKey, @Nullable final Object columnKey) {
            return this.original.get(columnKey, rowKey);
        }
        
        @Override
        public V put(final C rowKey, final R columnKey, final V value) {
            return this.original.put(columnKey, rowKey, value);
        }
        
        @Override
        public void putAll(final Table<? extends C, ? extends R, ? extends V> table) {
            this.original.putAll(Tables.transpose(table));
        }
        
        @Override
        public V remove(@Nullable final Object rowKey, @Nullable final Object columnKey) {
            return this.original.remove(columnKey, rowKey);
        }
        
        public Map<R, V> row(final C rowKey) {
            return this.original.column(rowKey);
        }
        
        @Override
        public Set<C> rowKeySet() {
            return this.original.columnKeySet();
        }
        
        public Map<C, Map<R, V>> rowMap() {
            return this.original.columnMap();
        }
        
        public int size() {
            return this.original.size();
        }
        
        @Override
        public Collection<V> values() {
            return this.original.values();
        }
        
        @Override
        Iterator<Table.Cell<C, R, V>> cellIterator() {
            return Iterators.<Object, Table.Cell<C, R, V>>transform((java.util.Iterator<Object>)this.original.cellSet().iterator(), TransposeTable.TRANSPOSE_CELL);
        }
        
        @Override
        Spliterator<Table.Cell<C, R, V>> cellSpliterator() {
            return CollectSpliterators.<Object, Table.Cell<C, R, V>>map((java.util.Spliterator<Object>)this.original.cellSet().spliterator(), (java.util.function.Function<? super Object, ? extends Table.Cell<C, R, V>>)TransposeTable.TRANSPOSE_CELL);
        }
        
        static {
            TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>() {
                public Table.Cell<?, ?, ?> apply(final Table.Cell<?, ?, ?> cell) {
                    return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
                }
            };
        }
    }
    
    private static class TransformedTable<R, C, V1, V2> extends AbstractTable<R, C, V2> {
        final Table<R, C, V1> fromTable;
        final Function<? super V1, V2> function;
        
        TransformedTable(final Table<R, C, V1> fromTable, final Function<? super V1, V2> function) {
            this.fromTable = Preconditions.<Table<R, C, V1>>checkNotNull(fromTable);
            this.function = Preconditions.<Function<? super V1, V2>>checkNotNull(function);
        }
        
        @Override
        public boolean contains(final Object rowKey, final Object columnKey) {
            return this.fromTable.contains(rowKey, columnKey);
        }
        
        @Override
        public V2 get(final Object rowKey, final Object columnKey) {
            return this.contains(rowKey, columnKey) ? this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
        }
        
        public int size() {
            return this.fromTable.size();
        }
        
        @Override
        public void clear() {
            this.fromTable.clear();
        }
        
        @Override
        public V2 put(final R rowKey, final C columnKey, final V2 value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Table<? extends R, ? extends C, ? extends V2> table) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V2 remove(final Object rowKey, final Object columnKey) {
            return this.contains(rowKey, columnKey) ? this.function.apply(this.fromTable.remove(rowKey, columnKey)) : null;
        }
        
        public Map<C, V2> row(final R rowKey) {
            return Maps.<C, V1, V2>transformValues(this.fromTable.row(rowKey), this.function);
        }
        
        public Map<R, V2> column(final C columnKey) {
            return Maps.<R, V1, V2>transformValues(this.fromTable.column(columnKey), this.function);
        }
        
        Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
            return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>() {
                public Table.Cell<R, C, V2> apply(final Table.Cell<R, C, V1> cell) {
                    return Tables.<R, C, V2>immutableCell(cell.getRowKey(), cell.getColumnKey(), TransformedTable.this.function.apply(cell.getValue()));
                }
            };
        }
        
        @Override
        Iterator<Table.Cell<R, C, V2>> cellIterator() {
            return Iterators.<Object, Table.Cell<R, C, V2>>transform((java.util.Iterator<Object>)this.fromTable.cellSet().iterator(), this.cellFunction());
        }
        
        @Override
        Spliterator<Table.Cell<R, C, V2>> cellSpliterator() {
            return CollectSpliterators.<Object, Table.Cell<R, C, V2>>map((java.util.Spliterator<Object>)this.fromTable.cellSet().spliterator(), (java.util.function.Function<? super Object, ? extends Table.Cell<R, C, V2>>)this.cellFunction());
        }
        
        @Override
        public Set<R> rowKeySet() {
            return this.fromTable.rowKeySet();
        }
        
        @Override
        public Set<C> columnKeySet() {
            return this.fromTable.columnKeySet();
        }
        
        @Override
        Collection<V2> createValues() {
            return Collections2.<V1, V2>transform(this.fromTable.values(), this.function);
        }
        
        public Map<R, Map<C, V2>> rowMap() {
            final Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>() {
                public Map<C, V2> apply(final Map<C, V1> row) {
                    return Maps.<C, V1, V2>transformValues(row, TransformedTable.this.function);
                }
            };
            return Maps.<R, java.util.Map<C, V1>, Map<C, V2>>transformValues(this.fromTable.rowMap(), rowFunction);
        }
        
        public Map<C, Map<R, V2>> columnMap() {
            final Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>() {
                public Map<R, V2> apply(final Map<R, V1> column) {
                    return Maps.<R, V1, V2>transformValues(column, TransformedTable.this.function);
                }
            };
            return Maps.<C, java.util.Map<R, V1>, Map<R, V2>>transformValues(this.fromTable.columnMap(), columnFunction);
        }
    }
    
    private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
        final Table<? extends R, ? extends C, ? extends V> delegate;
        private static final long serialVersionUID = 0L;
        
        UnmodifiableTable(final Table<? extends R, ? extends C, ? extends V> delegate) {
            this.delegate = Preconditions.<Table<? extends R, ? extends C, ? extends V>>checkNotNull(delegate);
        }
        
        @Override
        protected Table<R, C, V> delegate() {
            return (Table<R, C, V>)this.delegate;
        }
        
        @Override
        public Set<Table.Cell<R, C, V>> cellSet() {
            return (Set<Table.Cell<R, C, V>>)Collections.unmodifiableSet((Set)super.cellSet());
        }
        
        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<R, V> column(@Nullable final C columnKey) {
            return (Map<R, V>)Collections.unmodifiableMap((Map)super.column(columnKey));
        }
        
        @Override
        public Set<C> columnKeySet() {
            return (Set<C>)Collections.unmodifiableSet((Set)super.columnKeySet());
        }
        
        @Override
        public Map<C, Map<R, V>> columnMap() {
            final Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
            return (Map<C, Map<R, V>>)Collections.unmodifiableMap((Map)Maps.<C, java.util.Map<Object, Object>, Map<R, V>>transformValues(super.columnMap(), wrapper));
        }
        
        @Override
        public V put(@Nullable final R rowKey, @Nullable final C columnKey, @Nullable final V value) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void putAll(final Table<? extends R, ? extends C, ? extends V> table) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public V remove(@Nullable final Object rowKey, @Nullable final Object columnKey) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Map<C, V> row(@Nullable final R rowKey) {
            return (Map<C, V>)Collections.unmodifiableMap((Map)super.row(rowKey));
        }
        
        @Override
        public Set<R> rowKeySet() {
            return (Set<R>)Collections.unmodifiableSet((Set)super.rowKeySet());
        }
        
        @Override
        public Map<R, Map<C, V>> rowMap() {
            final Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
            return (Map<R, Map<C, V>>)Collections.unmodifiableMap((Map)Maps.<R, java.util.Map<Object, Object>, Map<C, V>>transformValues(super.rowMap(), wrapper));
        }
        
        @Override
        public Collection<V> values() {
            return (Collection<V>)Collections.unmodifiableCollection((Collection)super.values());
        }
    }
    
    static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
        private static final long serialVersionUID = 0L;
        
        public UnmodifiableRowSortedMap(final RowSortedTable<R, ? extends C, ? extends V> delegate) {
            super(delegate);
        }
        
        @Override
        protected RowSortedTable<R, C, V> delegate() {
            return (RowSortedTable<R, C, V>)(RowSortedTable)super.delegate();
        }
        
        @Override
        public SortedMap<R, Map<C, V>> rowMap() {
            final Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
            return (SortedMap<R, Map<C, V>>)Collections.unmodifiableSortedMap((SortedMap)Maps.<R, java.util.Map<C, V>, Map<C, V>>transformValues(this.delegate().rowMap(), wrapper));
        }
        
        @Override
        public SortedSet<R> rowKeySet() {
            return (SortedSet<R>)Collections.unmodifiableSortedSet((SortedSet)this.delegate().rowKeySet());
        }
    }
}
