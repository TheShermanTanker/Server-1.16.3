package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public abstract class AbstractLong2ObjectSortedMap<V> extends AbstractLong2ObjectMap<V> implements Long2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ObjectSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ObjectSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ObjectSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ObjectSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ObjectSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Long2ObjectMap.Entry<?>>)AbstractLong2ObjectSortedMap.this.long2ObjectEntrySet().iterator((Long2ObjectMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator<>(Long2ObjectSortedMaps.fastIterator((Long2ObjectSortedMap<V>)AbstractLong2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2ObjectMap.Entry)this.i.next()).getLongKey();
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
    
    protected class ValuesCollection extends AbstractObjectCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Long2ObjectSortedMaps.fastIterator((Long2ObjectSortedMap<V>)AbstractLong2ObjectSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractLong2ObjectSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Long2ObjectMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
