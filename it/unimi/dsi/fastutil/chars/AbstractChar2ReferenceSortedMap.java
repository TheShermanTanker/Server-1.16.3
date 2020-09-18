package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractChar2ReferenceSortedMap<V> extends AbstractChar2ReferenceMap<V> implements Char2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2ReferenceSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2ReferenceSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2ReferenceSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Char2ReferenceMap.Entry<?>>)AbstractChar2ReferenceSortedMap.this.char2ReferenceEntrySet().iterator((Char2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator<>(Char2ReferenceSortedMaps.fastIterator((Char2ReferenceSortedMap<V>)AbstractChar2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2ReferenceMap.Entry)this.i.next()).getCharKey();
        }
        
        public char previousChar() {
            return this.i.previous().getCharKey();
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
            return new ValuesIterator<V>(Char2ReferenceSortedMaps.fastIterator((Char2ReferenceSortedMap<V>)AbstractChar2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractChar2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Char2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
