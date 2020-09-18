package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractChar2ShortSortedMap extends AbstractChar2ShortMap implements Char2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2ShortSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ShortSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2ShortSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2ShortSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2ShortSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2ShortMap.Entry>)AbstractChar2ShortSortedMap.this.char2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2ShortSortedMaps.fastIterator(AbstractChar2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2ShortMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Char2ShortSortedMaps.fastIterator(AbstractChar2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractChar2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Char2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Char2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
