package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractReference2LongSortedMap<K> extends AbstractReference2LongMap<K> implements Reference2LongSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2LongSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2LongSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2LongSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2LongSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2LongSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2LongMap.Entry<K>>)AbstractReference2LongSortedMap.this.reference2LongEntrySet().iterator((Reference2LongMap.Entry<K>)new BasicEntry<>((K)from, 0L)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2LongSortedMaps.fastIterator((Reference2LongSortedMap<K>)AbstractReference2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2LongMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2LongMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2LongMap.Entry)this.i.next()).getKey();
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
            return new ValuesIterator<>(Reference2LongSortedMaps.fastIterator((Reference2LongSortedMap<K>)AbstractReference2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractReference2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements LongIterator {
        protected final ObjectBidirectionalIterator<Reference2LongMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2LongMap.Entry<K>> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Reference2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
