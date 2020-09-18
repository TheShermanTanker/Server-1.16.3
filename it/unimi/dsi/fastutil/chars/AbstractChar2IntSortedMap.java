package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractChar2IntSortedMap extends AbstractChar2IntMap implements Char2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2IntSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2IntSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2IntSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2IntSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2IntSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2IntMap.Entry>)AbstractChar2IntSortedMap.this.char2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2IntSortedMaps.fastIterator(AbstractChar2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2IntMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2IntMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Char2IntSortedMaps.fastIterator(AbstractChar2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractChar2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Char2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Char2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
