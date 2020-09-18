package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractChar2ByteSortedMap extends AbstractChar2ByteMap implements Char2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2ByteSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ByteSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2ByteSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2ByteSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2ByteSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2ByteMap.Entry>)AbstractChar2ByteSortedMap.this.char2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2ByteSortedMaps.fastIterator(AbstractChar2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2ByteMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractByteCollection {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(Char2ByteSortedMaps.fastIterator(AbstractChar2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractChar2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Char2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Char2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
