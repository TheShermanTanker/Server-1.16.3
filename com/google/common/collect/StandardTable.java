package com.google.common.collect;

import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import java.util.Spliterators;
import java.util.Collection;
import java.util.function.Function;
import java.util.Spliterator;
import java.util.LinkedHashMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.Set;
import com.google.common.base.Supplier;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible
class StandardTable<R, C, V> extends AbstractTable<R, C, V> implements Serializable {
    @GwtTransient
    final Map<R, Map<C, V>> backingMap;
    @GwtTransient
    final Supplier<? extends Map<C, V>> factory;
    private transient Set<C> columnKeySet;
    private transient Map<R, Map<C, V>> rowMap;
    private transient ColumnMap columnMap;
    private static final long serialVersionUID = 0L;
    
    StandardTable(final Map<R, Map<C, V>> backingMap, final Supplier<? extends Map<C, V>> factory) {
        this.backingMap = backingMap;
        this.factory = factory;
    }
    
    @Override
    public boolean contains(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        return rowKey != null && columnKey != null && super.contains(rowKey, columnKey);
    }
    
    @Override
    public boolean containsColumn(@Nullable final Object columnKey) {
        if (columnKey == null) {
            return false;
        }
        for (final Map<C, V> map : this.backingMap.values()) {
            if (Maps.safeContainsKey(map, columnKey)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean containsRow(@Nullable final Object rowKey) {
        return rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey);
    }
    
    @Override
    public boolean containsValue(@Nullable final Object value) {
        return value != null && super.containsValue(value);
    }
    
    @Override
    public V get(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
    }
    
    @Override
    public boolean isEmpty() {
        return this.backingMap.isEmpty();
    }
    
    public int size() {
        int size = 0;
        for (final Map<C, V> map : this.backingMap.values()) {
            size += map.size();
        }
        return size;
    }
    
    @Override
    public void clear() {
        this.backingMap.clear();
    }
    
    private Map<C, V> getOrCreate(final R rowKey) {
        Map<C, V> map = (Map<C, V>)this.backingMap.get(rowKey);
        if (map == null) {
            map = (Map<C, V>)this.factory.get();
            this.backingMap.put(rowKey, map);
        }
        return map;
    }
    
    @CanIgnoreReturnValue
    @Override
    public V put(final R rowKey, final C columnKey, final V value) {
        Preconditions.<R>checkNotNull(rowKey);
        Preconditions.<C>checkNotNull(columnKey);
        Preconditions.<V>checkNotNull(value);
        return (V)this.getOrCreate(rowKey).put(columnKey, value);
    }
    
    @CanIgnoreReturnValue
    @Override
    public V remove(@Nullable final Object rowKey, @Nullable final Object columnKey) {
        if (rowKey == null || columnKey == null) {
            return null;
        }
        final Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
        if (map == null) {
            return null;
        }
        final V value = (V)map.remove(columnKey);
        if (map.isEmpty()) {
            this.backingMap.remove(rowKey);
        }
        return value;
    }
    
    @CanIgnoreReturnValue
    private Map<R, V> removeColumn(final Object column) {
        final Map<R, V> output = (Map<R, V>)new LinkedHashMap();
        final Iterator<Map.Entry<R, Map<C, V>>> iterator = (Iterator<Map.Entry<R, Map<C, V>>>)this.backingMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<R, Map<C, V>> entry = (Map.Entry<R, Map<C, V>>)iterator.next();
            final V value = (V)((Map)entry.getValue()).remove(column);
            if (value != null) {
                output.put(entry.getKey(), value);
                if (!((Map)entry.getValue()).isEmpty()) {
                    continue;
                }
                iterator.remove();
            }
        }
        return output;
    }
    
    private boolean containsMapping(final Object rowKey, final Object columnKey, final Object value) {
        return value != null && value.equals(this.get(rowKey, columnKey));
    }
    
    private boolean removeMapping(final Object rowKey, final Object columnKey, final Object value) {
        if (this.containsMapping(rowKey, columnKey, value)) {
            this.remove(rowKey, columnKey);
            return true;
        }
        return false;
    }
    
    @Override
    public Set<Table.Cell<R, C, V>> cellSet() {
        return super.cellSet();
    }
    
    @Override
    Iterator<Table.Cell<R, C, V>> cellIterator() {
        return (Iterator<Table.Cell<R, C, V>>)new CellIterator();
    }
    
    @Override
    Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
        return CollectSpliterators.<Object, Table.Cell<R, C, V>>flatMap((java.util.Spliterator<Object>)this.backingMap.entrySet().spliterator(), (java.util.function.Function<? super Object, java.util.Spliterator<Table.Cell<R, C, V>>>)(rowEntry -> CollectSpliterators.map((java.util.Spliterator<Object>)((Map)rowEntry.getValue()).entrySet().spliterator(), (java.util.function.Function<? super Object, ?>)(columnEntry -> Tables.immutableCell(rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue())))), 65, this.size());
    }
    
    public Map<C, V> row(final R rowKey) {
        return (Map<C, V>)new Row(rowKey);
    }
    
    public Map<R, V> column(final C columnKey) {
        return (Map<R, V>)new Column(columnKey);
    }
    
    @Override
    public Set<R> rowKeySet() {
        return (Set<R>)this.rowMap().keySet();
    }
    
    @Override
    public Set<C> columnKeySet() {
        final Set<C> result = this.columnKeySet;
        return (result == null) ? (this.columnKeySet = (Set<C>)new ColumnKeySet()) : result;
    }
    
    Iterator<C> createColumnKeyIterator() {
        return (Iterator<C>)new ColumnKeyIterator();
    }
    
    @Override
    public Collection<V> values() {
        return super.values();
    }
    
    public Map<R, Map<C, V>> rowMap() {
        final Map<R, Map<C, V>> result = this.rowMap;
        return (result == null) ? (this.rowMap = this.createRowMap()) : result;
    }
    
    Map<R, Map<C, V>> createRowMap() {
        return (Map<R, Map<C, V>>)new RowMap();
    }
    
    public Map<C, Map<R, V>> columnMap() {
        final ColumnMap result = this.columnMap;
        return (Map<C, Map<R, V>>)((result == null) ? (this.columnMap = new ColumnMap()) : result);
    }
    
    private abstract class TableSet<T> extends Sets.ImprovedAbstractSet<T> {
        public boolean isEmpty() {
            return StandardTable.this.backingMap.isEmpty();
        }
        
        public void clear() {
            StandardTable.this.backingMap.clear();
        }
    }
    
    private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
        final Iterator<Map.Entry<R, Map<C, V>>> rowIterator;
        Map.Entry<R, Map<C, V>> rowEntry;
        Iterator<Map.Entry<C, V>> columnIterator;
        
        private CellIterator() {
            this.rowIterator = (Iterator<Map.Entry<R, Map<C, V>>>)StandardTable.this.backingMap.entrySet().iterator();
            this.columnIterator = Iterators.<Map.Entry<C, V>>emptyModifiableIterator();
        }
        
        public boolean hasNext() {
            return this.rowIterator.hasNext() || this.columnIterator.hasNext();
        }
        
        public Table.Cell<R, C, V> next() {
            if (!this.columnIterator.hasNext()) {
                this.rowEntry = (Map.Entry<R, Map<C, V>>)this.rowIterator.next();
                this.columnIterator = (Iterator<Map.Entry<C, V>>)((Map)this.rowEntry.getValue()).entrySet().iterator();
            }
            final Map.Entry<C, V> columnEntry = (Map.Entry<C, V>)this.columnIterator.next();
            return Tables.<R, C, V>immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
        }
        
        public void remove() {
            this.columnIterator.remove();
            if (((Map)this.rowEntry.getValue()).isEmpty()) {
                this.rowIterator.remove();
            }
        }
    }
    
    class Row extends Maps.IteratorBasedAbstractMap<C, V> {
        final R rowKey;
        Map<C, V> backingRowMap;
        
        Row(final R rowKey) {
            this.rowKey = Preconditions.<R>checkNotNull(rowKey);
        }
        
        Map<C, V> backingRowMap() {
            return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this.backingRowMap = this.computeBackingRowMap()) : this.backingRowMap;
        }
        
        Map<C, V> computeBackingRowMap() {
            return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
        }
        
        void maintainEmptyInvariant() {
            if (this.backingRowMap() != null && this.backingRowMap.isEmpty()) {
                StandardTable.this.backingMap.remove(this.rowKey);
                this.backingRowMap = null;
            }
        }
        
        public boolean containsKey(final Object key) {
            final Map<C, V> backingRowMap = this.backingRowMap();
            return key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key);
        }
        
        public V get(final Object key) {
            final Map<C, V> backingRowMap = this.backingRowMap();
            return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
        }
        
        public V put(final C key, final V value) {
            Preconditions.<C>checkNotNull(key);
            Preconditions.<V>checkNotNull(value);
            if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
                return (V)this.backingRowMap.put(key, value);
            }
            return StandardTable.this.put(this.rowKey, key, value);
        }
        
        public V remove(final Object key) {
            final Map<C, V> backingRowMap = this.backingRowMap();
            if (backingRowMap == null) {
                return null;
            }
            final V result = Maps.<V>safeRemove(backingRowMap, key);
            this.maintainEmptyInvariant();
            return result;
        }
        
        @Override
        public void clear() {
            final Map<C, V> backingRowMap = this.backingRowMap();
            if (backingRowMap != null) {
                backingRowMap.clear();
            }
            this.maintainEmptyInvariant();
        }
        
        @Override
        public int size() {
            final Map<C, V> map = this.backingRowMap();
            return (map == null) ? 0 : map.size();
        }
        
        @Override
        Iterator<Map.Entry<C, V>> entryIterator() {
            final Map<C, V> map = this.backingRowMap();
            if (map == null) {
                return Iterators.<Map.Entry<C, V>>emptyModifiableIterator();
            }
            final Iterator<Map.Entry<C, V>> iterator = (Iterator<Map.Entry<C, V>>)map.entrySet().iterator();
            return (Iterator<Map.Entry<C, V>>)new Iterator<Map.Entry<C, V>>() {
                public boolean hasNext() {
                    return iterator.hasNext();
                }
                
                public Map.Entry<C, V> next() {
                    return Row.this.wrapEntry((Map.Entry<C, V>)iterator.next());
                }
                
                public void remove() {
                    iterator.remove();
                    Row.this.maintainEmptyInvariant();
                }
            };
        }
        
        @Override
        Spliterator<Map.Entry<C, V>> entrySpliterator() {
            final Map<C, V> map = this.backingRowMap();
            if (map == null) {
                return (Spliterator<Map.Entry<C, V>>)Spliterators.emptySpliterator();
            }
            return CollectSpliterators.<Object, Map.Entry<C, V>>map((java.util.Spliterator<Object>)map.entrySet().spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<C, V>>)this::wrapEntry);
        }
        
        Map.Entry<C, V> wrapEntry(final Map.Entry<C, V> entry) {
            return (Map.Entry<C, V>)new ForwardingMapEntry<C, V>() {
                @Override
                protected Map.Entry<C, V> delegate() {
                    return entry;
                }
                
                @Override
                public V setValue(final V value) {
                    return super.setValue(Preconditions.<V>checkNotNull(value));
                }
                
                @Override
                public boolean equals(final Object object) {
                    return this.standardEquals(object);
                }
            };
        }
    }
    
    private class Column extends Maps.ViewCachingAbstractMap<R, V> {
        final C columnKey;
        
        Column(final C columnKey) {
            this.columnKey = Preconditions.<C>checkNotNull(columnKey);
        }
        
        public V put(final R key, final V value) {
            return StandardTable.this.put(key, this.columnKey, value);
        }
        
        public V get(final Object key) {
            return StandardTable.this.get(key, this.columnKey);
        }
        
        public boolean containsKey(final Object key) {
            return StandardTable.this.contains(key, this.columnKey);
        }
        
        public V remove(final Object key) {
            return StandardTable.this.remove(key, this.columnKey);
        }
        
        @CanIgnoreReturnValue
        boolean removeFromColumnIf(final Predicate<? super Map.Entry<R, V>> predicate) {
            boolean changed = false;
            final Iterator<Map.Entry<R, Map<C, V>>> iterator = (Iterator<Map.Entry<R, Map<C, V>>>)StandardTable.this.backingMap.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<R, Map<C, V>> entry = (Map.Entry<R, Map<C, V>>)iterator.next();
                final Map<C, V> map = (Map<C, V>)entry.getValue();
                final V value = (V)map.get(this.columnKey);
                if (value != null && predicate.apply(Maps.<Object, V>immutableEntry(entry.getKey(), value))) {
                    map.remove(this.columnKey);
                    changed = true;
                    if (!map.isEmpty()) {
                        continue;
                    }
                    iterator.remove();
                }
            }
            return changed;
        }
        
        @Override
        Set<Map.Entry<R, V>> createEntrySet() {
            return (Set<Map.Entry<R, V>>)new EntrySet();
        }
        
        @Override
        Set<R> createKeySet() {
            return (Set<R>)new KeySet();
        }
        
        @Override
        Collection<V> createValues() {
            return (Collection<V>)new Values();
        }
        
        private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
            public Iterator<Map.Entry<R, V>> iterator() {
                return (Iterator<Map.Entry<R, V>>)new EntrySetIterator();
            }
            
            public int size() {
                int size = 0;
                for (final Map<C, V> map : StandardTable.this.backingMap.values()) {
                    if (map.containsKey(Column.this.columnKey)) {
                        ++size;
                    }
                }
                return size;
            }
            
            public boolean isEmpty() {
                return !StandardTable.this.containsColumn(Column.this.columnKey);
            }
            
            public void clear() {
                Column.this.removeFromColumnIf(Predicates.alwaysTrue());
            }
            
            public boolean contains(final Object o) {
                if (o instanceof Map.Entry) {
                    final Map.Entry<?, ?> entry = o;
                    return StandardTable.this.containsMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
                }
                return false;
            }
            
            public boolean remove(final Object obj) {
                if (obj instanceof Map.Entry) {
                    final Map.Entry<?, ?> entry = obj;
                    return StandardTable.this.removeMapping(entry.getKey(), Column.this.columnKey, entry.getValue());
                }
                return false;
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                return Column.this.removeFromColumnIf(Predicates.not(Predicates.in((java.util.Collection<? extends Map.Entry<R, V>>)c)));
            }
        }
        
        private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
            final Iterator<Map.Entry<R, Map<C, V>>> iterator;
            
            private EntrySetIterator() {
                this.iterator = (Iterator<Map.Entry<R, Map<C, V>>>)StandardTable.this.backingMap.entrySet().iterator();
            }
            
            @Override
            protected Map.Entry<R, V> computeNext() {
                while (this.iterator.hasNext()) {
                    final Map.Entry<R, Map<C, V>> entry = (Map.Entry<R, Map<C, V>>)this.iterator.next();
                    if (((Map)entry.getValue()).containsKey(Column.this.columnKey)) {
                        class 1EntryImpl extends AbstractMapEntry<R, V> {
                            @Override
                            public R getKey() {
                                return (R)entry.getKey();
                            }
                            
                            @Override
                            public V getValue() {
                                return (V)((Map)entry.getValue()).get(Column.this.columnKey);
                            }
                            
                            @Override
                            public V setValue(final V value) {
                                return (V)((Map)entry.getValue()).put(Column.this.columnKey, Preconditions.<V>checkNotNull(value));
                            }
                        }
                        return (Map.Entry<R, V>)new 1EntryImpl();
                    }
                }
                return (Map.Entry<R, V>)((AbstractIterator<Map.Entry>)this).endOfData();
            }
        }
        
        private class KeySet extends Maps.KeySet<R, V> {
            KeySet() {
                super((Map)Column.this);
            }
            
            @Override
            public boolean contains(final Object obj) {
                return StandardTable.this.contains(obj, Column.this.columnKey);
            }
            
            @Override
            public boolean remove(final Object obj) {
                return StandardTable.this.remove(obj, Column.this.columnKey) != null;
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                return Column.this.removeFromColumnIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in((java.util.Collection<? extends K>)c))));
            }
        }
        
        private class Values extends Maps.Values<R, V> {
            Values() {
                super((Map)Column.this);
            }
            
            @Override
            public boolean remove(final Object obj) {
                return obj != null && Column.this.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.equalTo(obj)));
            }
            
            @Override
            public boolean removeAll(final Collection<?> c) {
                return Column.this.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                return Column.this.removeFromColumnIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in((java.util.Collection<? extends V>)c))));
            }
        }
    }
    
    private class ColumnKeySet extends TableSet<C> {
        public Iterator<C> iterator() {
            return StandardTable.this.createColumnKeyIterator();
        }
        
        public int size() {
            return Iterators.size(this.iterator());
        }
        
        public boolean remove(final Object obj) {
            if (obj == null) {
                return false;
            }
            boolean changed = false;
            final Iterator<Map<C, V>> iterator = (Iterator<Map<C, V>>)StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                final Map<C, V> map = (Map<C, V>)iterator.next();
                if (map.keySet().remove(obj)) {
                    changed = true;
                    if (!map.isEmpty()) {
                        continue;
                    }
                    iterator.remove();
                }
            }
            return changed;
        }
        
        @Override
        public boolean removeAll(final Collection<?> c) {
            Preconditions.<Collection<?>>checkNotNull(c);
            boolean changed = false;
            final Iterator<Map<C, V>> iterator = (Iterator<Map<C, V>>)StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                final Map<C, V> map = (Map<C, V>)iterator.next();
                if (Iterators.removeAll(map.keySet().iterator(), c)) {
                    changed = true;
                    if (!map.isEmpty()) {
                        continue;
                    }
                    iterator.remove();
                }
            }
            return changed;
        }
        
        @Override
        public boolean retainAll(final Collection<?> c) {
            Preconditions.<Collection<?>>checkNotNull(c);
            boolean changed = false;
            final Iterator<Map<C, V>> iterator = (Iterator<Map<C, V>>)StandardTable.this.backingMap.values().iterator();
            while (iterator.hasNext()) {
                final Map<C, V> map = (Map<C, V>)iterator.next();
                if (map.keySet().retainAll((Collection)c)) {
                    changed = true;
                    if (!map.isEmpty()) {
                        continue;
                    }
                    iterator.remove();
                }
            }
            return changed;
        }
        
        public boolean contains(final Object obj) {
            return StandardTable.this.containsColumn(obj);
        }
    }
    
    private class ColumnKeyIterator extends AbstractIterator<C> {
        final Map<C, V> seen;
        final Iterator<Map<C, V>> mapIterator;
        Iterator<Map.Entry<C, V>> entryIterator;
        
        private ColumnKeyIterator() {
            this.seen = (Map<C, V>)StandardTable.this.factory.get();
            this.mapIterator = (Iterator<Map<C, V>>)StandardTable.this.backingMap.values().iterator();
            this.entryIterator = Iterators.emptyIterator();
        }
        
        @Override
        protected C computeNext() {
            while (true) {
                if (this.entryIterator.hasNext()) {
                    final Map.Entry<C, V> entry = (Map.Entry<C, V>)this.entryIterator.next();
                    if (!this.seen.containsKey(entry.getKey())) {
                        this.seen.put(entry.getKey(), entry.getValue());
                        return (C)entry.getKey();
                    }
                    continue;
                }
                else {
                    if (!this.mapIterator.hasNext()) {
                        return this.endOfData();
                    }
                    this.entryIterator = (Iterator<Map.Entry<C, V>>)((Map)this.mapIterator.next()).entrySet().iterator();
                }
            }
        }
    }
    
    class RowMap extends Maps.ViewCachingAbstractMap<R, Map<C, V>> {
        public boolean containsKey(final Object key) {
            return StandardTable.this.containsRow(key);
        }
        
        public Map<C, V> get(final Object key) {
            return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
        }
        
        public Map<C, V> remove(final Object key) {
            return (Map<C, V>)((key == null) ? null : ((Map)StandardTable.this.backingMap.remove(key)));
        }
        
        protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
            return (Set<Map.Entry<R, Map<C, V>>>)new EntrySet();
        }
        
        class EntrySet extends TableSet<Map.Entry<R, Map<C, V>>> {
            public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
                return Maps.<R, Map<C, V>>asMapEntryIterator((java.util.Set<R>)StandardTable.this.backingMap.keySet(), new com.google.common.base.Function<R, Map<C, V>>() {
                    public Map<C, V> apply(final R rowKey) {
                        return StandardTable.this.row(rowKey);
                    }
                });
            }
            
            public int size() {
                return StandardTable.this.backingMap.size();
            }
            
            public boolean contains(final Object obj) {
                if (obj instanceof Map.Entry) {
                    final Map.Entry<?, ?> entry = obj;
                    return entry.getKey() != null && entry.getValue() instanceof Map && Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry);
                }
                return false;
            }
            
            public boolean remove(final Object obj) {
                if (obj instanceof Map.Entry) {
                    final Map.Entry<?, ?> entry = obj;
                    return entry.getKey() != null && entry.getValue() instanceof Map && StandardTable.this.backingMap.entrySet().remove(entry);
                }
                return false;
            }
        }
    }
    
    private class ColumnMap extends Maps.ViewCachingAbstractMap<C, Map<R, V>> {
        public Map<R, V> get(final Object key) {
            return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
        }
        
        public boolean containsKey(final Object key) {
            return StandardTable.this.containsColumn(key);
        }
        
        public Map<R, V> remove(final Object key) {
            return (Map<R, V>)(StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null);
        }
        
        public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
            return (Set<Map.Entry<C, Map<R, V>>>)new ColumnMapEntrySet();
        }
        
        @Override
        public Set<C> keySet() {
            return StandardTable.this.columnKeySet();
        }
        
        @Override
        Collection<Map<R, V>> createValues() {
            return (Collection<Map<R, V>>)new ColumnMapValues();
        }
        
        class ColumnMapEntrySet extends TableSet<Map.Entry<C, Map<R, V>>> {
            public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
                return Maps.<C, Map<R, V>>asMapEntryIterator(StandardTable.this.columnKeySet(), new com.google.common.base.Function<C, Map<R, V>>() {
                    public Map<R, V> apply(final C columnKey) {
                        return StandardTable.this.column(columnKey);
                    }
                });
            }
            
            public int size() {
                return StandardTable.this.columnKeySet().size();
            }
            
            public boolean contains(final Object obj) {
                if (obj instanceof Map.Entry) {
                    final Map.Entry<?, ?> entry = obj;
                    if (StandardTable.this.containsColumn(entry.getKey())) {
                        final C columnKey = (C)entry.getKey();
                        return ColumnMap.this.get(columnKey).equals(entry.getValue());
                    }
                }
                return false;
            }
            
            public boolean remove(final Object obj) {
                if (this.contains(obj)) {
                    final Map.Entry<?, ?> entry = obj;
                    StandardTable.this.removeColumn(entry.getKey());
                    return true;
                }
                return false;
            }
            
            @Override
            public boolean removeAll(final Collection<?> c) {
                Preconditions.<Collection<?>>checkNotNull(c);
                return Sets.removeAllImpl(this, c.iterator());
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                Preconditions.<Collection<?>>checkNotNull(c);
                boolean changed = false;
                for (final C columnKey : Lists.newArrayList((java.util.Iterator<?>)StandardTable.this.columnKeySet().iterator())) {
                    if (!c.contains(Maps.<C, Map<R, V>>immutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }
        }
        
        private class ColumnMapValues extends Maps.Values<C, Map<R, V>> {
            ColumnMapValues() {
                super((Map)ColumnMap.this);
            }
            
            @Override
            public boolean remove(final Object obj) {
                for (final Map.Entry<C, Map<R, V>> entry : ColumnMap.this.entrySet()) {
                    if (((Map)entry.getValue()).equals(obj)) {
                        StandardTable.this.removeColumn(entry.getKey());
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public boolean removeAll(final Collection<?> c) {
                Preconditions.<Collection<?>>checkNotNull(c);
                boolean changed = false;
                for (final C columnKey : Lists.newArrayList((java.util.Iterator<?>)StandardTable.this.columnKeySet().iterator())) {
                    if (c.contains(StandardTable.this.column(columnKey))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }
            
            @Override
            public boolean retainAll(final Collection<?> c) {
                Preconditions.<Collection<?>>checkNotNull(c);
                boolean changed = false;
                for (final C columnKey : Lists.newArrayList((java.util.Iterator<?>)StandardTable.this.columnKeySet().iterator())) {
                    if (!c.contains(StandardTable.this.column(columnKey))) {
                        StandardTable.this.removeColumn(columnKey);
                        changed = true;
                    }
                }
                return changed;
            }
        }
    }
}
