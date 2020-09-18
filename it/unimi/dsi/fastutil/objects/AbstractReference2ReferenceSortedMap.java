package it.unimi.dsi.fastutil.objects;

import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractReference2ReferenceSortedMap<K, V> extends AbstractReference2ReferenceMap<K, V> implements Reference2ReferenceSortedMap<K, V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2ReferenceSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ReferenceSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2ReferenceSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2ReferenceSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2ReferenceSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K, Object>((ObjectBidirectionalIterator<Reference2ReferenceMap.Entry<K, ?>>)AbstractReference2ReferenceSortedMap.this.reference2ReferenceEntrySet().iterator((Reference2ReferenceMap.Entry<K, V>)new BasicEntry<Object, Object>((K)from, null)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K, Object>(Reference2ReferenceSortedMaps.fastIterator((Reference2ReferenceSortedMap<K, V>)AbstractReference2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K, V> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2ReferenceMap.Entry<K, V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2ReferenceMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2ReferenceMap.Entry)this.i.next()).getKey();
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
            return new ValuesIterator<Object, V>(Reference2ReferenceSortedMaps.fastIterator((Reference2ReferenceSortedMap<K, V>)AbstractReference2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractReference2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K, V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Reference2ReferenceMap.Entry<K, V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2ReferenceMap.Entry<K, V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Reference2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
