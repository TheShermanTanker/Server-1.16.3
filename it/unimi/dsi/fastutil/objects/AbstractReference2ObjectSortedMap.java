package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractReference2ObjectSortedMap<K, V> extends AbstractReference2ObjectMap<K, V> implements Reference2ObjectSortedMap<K, V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2ObjectSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2ObjectSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ObjectSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2ObjectSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2ObjectSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2ObjectSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K, Object>((ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, ?>>)AbstractReference2ObjectSortedMap.this.reference2ObjectEntrySet().iterator((Reference2ObjectMap.Entry<K, V>)new BasicEntry<Object, Object>((K)from, null)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K, Object>(Reference2ObjectSortedMaps.fastIterator((Reference2ObjectSortedMap<K, V>)AbstractReference2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K, V> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2ObjectMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractObjectCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<Object, V>(Reference2ObjectSortedMaps.fastIterator((Reference2ObjectSortedMap<K, V>)AbstractReference2ObjectSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractReference2ObjectSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K, V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2ObjectMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Reference2ObjectMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
