package com.google.common.collect;

import java.util.Set;
import java.util.Collection;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
class SingletonImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    final R singleRowKey;
    final C singleColumnKey;
    final V singleValue;
    
    SingletonImmutableTable(final R rowKey, final C columnKey, final V value) {
        this.singleRowKey = Preconditions.<R>checkNotNull(rowKey);
        this.singleColumnKey = Preconditions.<C>checkNotNull(columnKey);
        this.singleValue = Preconditions.<V>checkNotNull(value);
    }
    
    SingletonImmutableTable(final Table.Cell<R, C, V> cell) {
        this(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
    }
    
    @Override
    public ImmutableMap<R, V> column(final C columnKey) {
        Preconditions.<C>checkNotNull(columnKey);
        return this.containsColumn(columnKey) ? ImmutableMap.<R, V>of(this.singleRowKey, this.singleValue) : ImmutableMap.<R, V>of();
    }
    
    @Override
    public ImmutableMap<C, Map<R, V>> columnMap() {
        return ImmutableMap.<C, Map<R, V>>of(this.singleColumnKey, ImmutableMap.of(this.singleRowKey, this.singleValue));
    }
    
    @Override
    public ImmutableMap<R, Map<C, V>> rowMap() {
        return ImmutableMap.<R, Map<C, V>>of(this.singleRowKey, ImmutableMap.of(this.singleColumnKey, this.singleValue));
    }
    
    public int size() {
        return 1;
    }
    
    @Override
    ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return ImmutableSet.<Table.Cell<R, C, V>>of(ImmutableTable.<R, C, V>cellOf(this.singleRowKey, this.singleColumnKey, this.singleValue));
    }
    
    @Override
    ImmutableCollection<V> createValues() {
        return ImmutableSet.<V>of(this.singleValue);
    }
    
    @Override
    SerializedForm createSerializedForm() {
        return SerializedForm.create(this, new int[] { 0 }, new int[] { 0 });
    }
}
