package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Collections;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V> {
    abstract Table.Cell<R, C, V> getCell(final int integer);
    
    @Override
    final ImmutableSet<Table.Cell<R, C, V>> createCellSet() {
        return this.isEmpty() ? ImmutableSet.<Table.Cell<R, C, V>>of() : new CellSet();
    }
    
    abstract V getValue(final int integer);
    
    @Override
    final ImmutableCollection<V> createValues() {
        return (ImmutableCollection<V>)(this.isEmpty() ? ImmutableList.of() : new Values());
    }
    
    static <R, C, V> RegularImmutableTable<R, C, V> forCells(final List<Table.Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
        Preconditions.<List<Table.Cell<R, C, V>>>checkNotNull(cells);
        if (rowComparator != null || columnComparator != null) {
            final Comparator<Table.Cell<R, C, V>> comparator = (Comparator<Table.Cell<R, C, V>>)new Comparator<Table.Cell<R, C, V>>() {
                public int compare(final Table.Cell<R, C, V> cell1, final Table.Cell<R, C, V> cell2) {
                    final int rowCompare = (rowComparator == null) ? 0 : rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
                    if (rowCompare != 0) {
                        return rowCompare;
                    }
                    return (columnComparator == null) ? 0 : columnComparator.compare(cell1.getColumnKey(), cell2.getColumnKey());
                }
            };
            Collections.sort((List)cells, (Comparator)comparator);
        }
        return RegularImmutableTable.<R, C, V>forCellsInternal((java.lang.Iterable<Table.Cell<R, C, V>>)cells, rowComparator, columnComparator);
    }
    
    static <R, C, V> RegularImmutableTable<R, C, V> forCells(final Iterable<Table.Cell<R, C, V>> cells) {
        return RegularImmutableTable.<R, C, V>forCellsInternal(cells, null, null);
    }
    
    private static final <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(final Iterable<Table.Cell<R, C, V>> cells, @Nullable final Comparator<? super R> rowComparator, @Nullable final Comparator<? super C> columnComparator) {
        final Set<R> rowSpaceBuilder = (Set<R>)new LinkedHashSet();
        final Set<C> columnSpaceBuilder = (Set<C>)new LinkedHashSet();
        final ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.<Table.Cell<R, C, V>>copyOf((java.lang.Iterable<? extends Table.Cell<R, C, V>>)cells);
        for (final Table.Cell<R, C, V> cell : cells) {
            rowSpaceBuilder.add(cell.getRowKey());
            columnSpaceBuilder.add(cell.getColumnKey());
        }
        final ImmutableSet<R> rowSpace = (rowComparator == null) ? ImmutableSet.<R>copyOf((java.util.Collection<? extends R>)rowSpaceBuilder) : ImmutableSet.<R>copyOf((java.util.Collection<? extends R>)ImmutableList.sortedCopyOf((java.util.Comparator<? super Object>)rowComparator, (java.lang.Iterable<?>)rowSpaceBuilder));
        final ImmutableSet<C> columnSpace = (columnComparator == null) ? ImmutableSet.<C>copyOf((java.util.Collection<? extends C>)columnSpaceBuilder) : ImmutableSet.<C>copyOf((java.util.Collection<? extends C>)ImmutableList.sortedCopyOf((java.util.Comparator<? super Object>)columnComparator, (java.lang.Iterable<?>)columnSpaceBuilder));
        return RegularImmutableTable.<R, C, V>forOrderedComponents(cellList, rowSpace, columnSpace);
    }
    
    static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(final ImmutableList<Table.Cell<R, C, V>> cellList, final ImmutableSet<R> rowSpace, final ImmutableSet<C> columnSpace) {
        return (RegularImmutableTable<R, C, V>)((cellList.size() > rowSpace.size() * (long)columnSpace.size() / 2L) ? new DenseImmutableTable<R, C, V>((ImmutableList<Table.Cell<Object, Object, Object>>)cellList, (ImmutableSet<Object>)rowSpace, (ImmutableSet<Object>)columnSpace) : new SparseImmutableTable<R, C, V>((ImmutableList<Table.Cell<Object, Object, Object>>)cellList, (ImmutableSet<Object>)rowSpace, (ImmutableSet<Object>)columnSpace));
    }
    
    private final class CellSet extends Indexed<Table.Cell<R, C, V>> {
        public int size() {
            return RegularImmutableTable.this.size();
        }
        
        @Override
        Table.Cell<R, C, V> get(final int index) {
            return RegularImmutableTable.this.getCell(index);
        }
        
        @Override
        public boolean contains(@Nullable final Object object) {
            if (object instanceof Table.Cell) {
                final Table.Cell<?, ?, ?> cell = object;
                final Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
                return value != null && value.equals(cell.getValue());
            }
            return false;
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
    }
    
    private final class Values extends ImmutableList<V> {
        public int size() {
            return RegularImmutableTable.this.size();
        }
        
        public V get(final int index) {
            return RegularImmutableTable.this.getValue(index);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
    }
}
