package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractReference2FloatSortedMap<K> extends AbstractReference2FloatMap<K> implements Reference2FloatSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2FloatSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2FloatSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2FloatSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2FloatSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2FloatSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>>)AbstractReference2FloatSortedMap.this.reference2FloatEntrySet().iterator((Reference2FloatMap.Entry<K>)new BasicEntry<>((K)from, 0.0f)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2FloatSortedMaps.fastIterator((Reference2FloatSortedMap<K>)AbstractReference2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2FloatMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator<>(Reference2FloatSortedMaps.fastIterator((Reference2FloatSortedMap<K>)AbstractReference2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractReference2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements FloatIterator {
        protected final ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Reference2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
