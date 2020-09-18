package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractObject2LongSortedMap<K> extends AbstractObject2LongMap<K> implements Object2LongSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2LongSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2LongSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2LongSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2LongSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2LongSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2LongMap.Entry<K>>)AbstractObject2LongSortedMap.this.object2LongEntrySet().iterator((Object2LongMap.Entry<K>)new BasicEntry<>((K)from, 0L)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2LongSortedMaps.fastIterator((Object2LongSortedMap<K>)AbstractObject2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2LongMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2LongMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2LongMap.Entry)this.i.next()).getKey();
        }
        
        public K previous() {
            return (K)this.i.previous().getKey();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator<>(Object2LongSortedMaps.fastIterator((Object2LongSortedMap<K>)AbstractObject2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractObject2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements LongIterator {
        protected final ObjectBidirectionalIterator<Object2LongMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2LongMap.Entry<K>> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Object2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
