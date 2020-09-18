package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractReference2CharSortedMap<K> extends AbstractReference2CharMap<K> implements Reference2CharSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2CharSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2CharSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2CharSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2CharSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2CharSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2CharMap.Entry<K>>)AbstractReference2CharSortedMap.this.reference2CharEntrySet().iterator((Reference2CharMap.Entry<K>)new BasicEntry<>((K)from, '\0')));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2CharSortedMaps.fastIterator((Reference2CharSortedMap<K>)AbstractReference2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2CharMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractCharCollection {
        @Override
        public CharIterator iterator() {
            return new ValuesIterator<>(Reference2CharSortedMaps.fastIterator((Reference2CharSortedMap<K>)AbstractReference2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractReference2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements CharIterator {
        protected final ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Reference2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
