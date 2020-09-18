package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractReference2BooleanSortedMap<K> extends AbstractReference2BooleanMap<K> implements Reference2BooleanSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2BooleanSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2BooleanSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2BooleanSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2BooleanSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2BooleanSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>>)AbstractReference2BooleanSortedMap.this.reference2BooleanEntrySet().iterator((Reference2BooleanMap.Entry<K>)new BasicEntry<>((K)from, false)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2BooleanSortedMaps.fastIterator((Reference2BooleanSortedMap<K>)AbstractReference2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2BooleanMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator<>(Reference2BooleanSortedMaps.fastIterator((Reference2BooleanSortedMap<K>)AbstractReference2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractReference2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2BooleanMap.Entry<K>> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Reference2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
