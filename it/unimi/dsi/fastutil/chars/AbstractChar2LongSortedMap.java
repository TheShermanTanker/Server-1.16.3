package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractChar2LongSortedMap extends AbstractChar2LongMap implements Char2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2LongSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2LongSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2LongSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2LongSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2LongSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2LongMap.Entry>)AbstractChar2LongSortedMap.this.char2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2LongSortedMaps.fastIterator(AbstractChar2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2LongMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2LongMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Char2LongSortedMaps.fastIterator(AbstractChar2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractChar2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Char2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Char2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
