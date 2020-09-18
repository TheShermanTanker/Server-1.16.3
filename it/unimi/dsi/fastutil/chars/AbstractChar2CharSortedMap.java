package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractChar2CharSortedMap extends AbstractChar2CharMap implements Char2CharSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2CharSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2CharSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2CharSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2CharSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2CharSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2CharMap.Entry>)AbstractChar2CharSortedMap.this.char2CharEntrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2CharSortedMaps.fastIterator(AbstractChar2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2CharMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2CharMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractCharCollection {
        @Override
        public CharIterator iterator() {
            return new ValuesIterator(Char2CharSortedMaps.fastIterator(AbstractChar2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractChar2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements CharIterator {
        protected final ObjectBidirectionalIterator<Char2CharMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
