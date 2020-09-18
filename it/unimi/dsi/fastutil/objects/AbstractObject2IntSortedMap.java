package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractObject2IntSortedMap<K> extends AbstractObject2IntMap<K> implements Object2IntSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2IntSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2IntSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2IntSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2IntSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2IntSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2IntMap.Entry<K>>)AbstractObject2IntSortedMap.this.object2IntEntrySet().iterator((Object2IntMap.Entry<K>)new BasicEntry<>((K)from, 0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2IntSortedMaps.fastIterator((Object2IntSortedMap<K>)AbstractObject2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2IntMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator<>(Object2IntSortedMaps.fastIterator((Object2IntSortedMap<K>)AbstractObject2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractObject2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements IntIterator {
        protected final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2IntMap.Entry<K>> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Object2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
