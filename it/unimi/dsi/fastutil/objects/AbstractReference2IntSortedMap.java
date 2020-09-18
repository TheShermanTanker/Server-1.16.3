package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractReference2IntSortedMap<K> extends AbstractReference2IntMap<K> implements Reference2IntSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2IntSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2IntSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2IntSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2IntSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2IntSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2IntMap.Entry<K>>)AbstractReference2IntSortedMap.this.reference2IntEntrySet().iterator((Reference2IntMap.Entry<K>)new BasicEntry<>((K)from, 0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2IntSortedMaps.fastIterator((Reference2IntSortedMap<K>)AbstractReference2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2IntMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2IntMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2IntMap.Entry)this.i.next()).getKey();
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
            return new ValuesIterator<>(Reference2IntSortedMaps.fastIterator((Reference2IntSortedMap<K>)AbstractReference2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractReference2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements IntIterator {
        protected final ObjectBidirectionalIterator<Reference2IntMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2IntMap.Entry<K>> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Reference2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
