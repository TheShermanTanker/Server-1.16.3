package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractLong2ReferenceSortedMap<V> extends AbstractLong2ReferenceMap<V> implements Long2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ReferenceSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ReferenceSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ReferenceSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Long2ReferenceMap.Entry<?>>)AbstractLong2ReferenceSortedMap.this.long2ReferenceEntrySet().iterator((Long2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator<>(Long2ReferenceSortedMaps.fastIterator((Long2ReferenceSortedMap<V>)AbstractLong2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2ReferenceMap.Entry)this.i.next()).getLongKey();
        }
        
        public long previousLong() {
            return this.i.previous().getLongKey();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractReferenceCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Long2ReferenceSortedMaps.fastIterator((Long2ReferenceSortedMap<V>)AbstractLong2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractLong2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Long2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
