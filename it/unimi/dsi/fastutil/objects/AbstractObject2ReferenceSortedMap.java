package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractObject2ReferenceSortedMap<K, V> extends AbstractObject2ReferenceMap<K, V> implements Object2ReferenceSortedMap<K, V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2ReferenceSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2ReferenceSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2ReferenceSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2ReferenceSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2ReferenceSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K, Object>((ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, ?>>)AbstractObject2ReferenceSortedMap.this.object2ReferenceEntrySet().iterator((Object2ReferenceMap.Entry<K, V>)new BasicEntry<Object, Object>((K)from, null)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K, Object>(Object2ReferenceSortedMaps.fastIterator((Object2ReferenceSortedMap<K, V>)AbstractObject2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K, V> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2ReferenceMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<Object, V>(Object2ReferenceSortedMaps.fastIterator((Object2ReferenceSortedMap<K, V>)AbstractObject2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractObject2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K, V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Object2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
