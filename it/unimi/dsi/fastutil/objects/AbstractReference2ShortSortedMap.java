package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractReference2ShortSortedMap<K> extends AbstractReference2ShortMap<K> implements Reference2ShortSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2ShortSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ShortSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2ShortSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2ShortSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2ShortSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>>)AbstractReference2ShortSortedMap.this.reference2ShortEntrySet().iterator((Reference2ShortMap.Entry<K>)new BasicEntry<>((K)from, (short)0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2ShortSortedMaps.fastIterator((Reference2ShortSortedMap<K>)AbstractReference2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2ShortMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator<>(Reference2ShortSortedMaps.fastIterator((Reference2ShortSortedMap<K>)AbstractReference2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractReference2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements ShortIterator {
        protected final ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2ShortMap.Entry<K>> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Reference2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
