package com.google.common.collect;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
@Immutable
final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V> {
    static final ImmutableTable<Object, Object, Object> EMPTY;
    private final ImmutableMap<R, Map<C, V>> rowMap;
    private final ImmutableMap<C, Map<R, V>> columnMap;
    private final int[] cellRowIndices;
    private final int[] cellColumnInRowIndices;
    
    SparseImmutableTable(final ImmutableList<Table.Cell<R, C, V>> cellList, final ImmutableSet<R> rowSpace, final ImmutableSet<C> columnSpace) {
        final Map<R, Integer> rowIndex = Maps.indexMap((java.util.Collection<Object>)rowSpace);
        final Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
        for (final R row : rowSpace) {
            rows.put(row, new LinkedHashMap());
        }
        final Map<C, Map<R, V>> columns = Maps.newLinkedHashMap();
        for (final C col : columnSpace) {
            columns.put(col, new LinkedHashMap());
        }
        final int[] cellRowIndices = new int[cellList.size()];
        final int[] cellColumnInRowIndices = new int[cellList.size()];
        for (int i = 0; i < cellList.size(); ++i) {
            final Table.Cell<R, C, V> cell = (Table.Cell<R, C, V>)cellList.get(i);
            final R rowKey = cell.getRowKey();
            final C columnKey = cell.getColumnKey();
            final V value = cell.getValue();
            cellRowIndices[i] = (int)rowIndex.get(rowKey);
            final Map<C, V> thisRow = (Map<C, V>)rows.get(rowKey);
            cellColumnInRowIndices[i] = thisRow.size();
            final V oldValue = (V)thisRow.put(columnKey, value);
            if (oldValue != null) {
                throw new IllegalArgumentException(new StringBuilder().append("Duplicate value for row=").append(rowKey).append(", column=").append(columnKey).append(": ").append(value).append(", ").append(oldValue).toString());
            }
            ((Map)columns.get(columnKey)).put(rowKey, value);
        }
        this.cellRowIndices = cellRowIndices;
        this.cellColumnInRowIndices = cellColumnInRowIndices;
        final ImmutableMap.Builder<R, Map<C, V>> rowBuilder = new ImmutableMap.Builder<R, Map<C, V>>(rows.size());
        for (final Map.Entry<R, Map<C, V>> row2 : rows.entrySet()) {
            rowBuilder.put((R)row2.getKey(), ImmutableMap.copyOf((java.util.Map<?, ?>)row2.getValue()));
        }
        this.rowMap = rowBuilder.build();
        final ImmutableMap.Builder<C, Map<R, V>> columnBuilder = new ImmutableMap.Builder<C, Map<R, V>>(columns.size());
        for (final Map.Entry<C, Map<R, V>> col2 : columns.entrySet()) {
            columnBuilder.put((C)col2.getKey(), ImmutableMap.copyOf((java.util.Map<?, ?>)col2.getValue()));
        }
        this.columnMap = columnBuilder.build();
    }
    
    @Override
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return this.columnMap;
    }
    
    @Override
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return this.rowMap;
    }
    
    public int size() {
        return this.cellRowIndices.length;
    }
    
    @Override
    Table.Cell<R, C, V> getCell(final int index) {
        final int rowIndex = this.cellRowIndices[index];
        final Map.Entry<R, Map<C, V>> rowEntry = (Map.Entry<R, Map<C, V>>)this.rowMap.entrySet().asList().get(rowIndex);
        final ImmutableMap<C, V> row = (ImmutableMap<C, V>)rowEntry.getValue();
        final int columnIndex = this.cellColumnInRowIndices[index];
        final Map.Entry<C, V> colEntry = (Map.Entry<C, V>)row.entrySet().asList().get(columnIndex);
        return ImmutableTable.<R, C, V>cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
    }
    
    @Override
    V getValue(final int index) {
        final int rowIndex = this.cellRowIndices[index];
        final ImmutableMap<C, V> row = (ImmutableMap<C, V>)this.rowMap.values().asList().get(rowIndex);
        final int columnIndex = this.cellColumnInRowIndices[index];
        return (V)row.values().asList().get(columnIndex);
    }
    
    @Override
    SerializedForm createSerializedForm() {
        final Map<C, Integer> columnKeyToIndex = Maps.indexMap((java.util.Collection<Object>)this.columnKeySet());
        final int[] cellColumnIndices = new int[this.cellSet().size()];
        int i = 0;
        for (final Table.Cell<R, C, V> cell : this.cellSet()) {
            cellColumnIndices[i++] = (int)columnKeyToIndex.get(cell.getColumnKey());
        }
        return SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
    }
    
    static {
        EMPTY = new SparseImmutableTable<Object, Object, Object>(ImmutableList.<Table.Cell<Object, Object, Object>>of(), ImmutableSet.of(), ImmutableSet.of());
    }
}
