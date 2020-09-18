package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractByte2CharSortedMap extends AbstractByte2CharMap implements Byte2CharSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2CharSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2CharSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2CharSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2CharSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2CharSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2CharMap.Entry>)AbstractByte2CharSortedMap.this.byte2CharEntrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2CharSortedMaps.fastIterator(AbstractByte2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2CharMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2CharMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2CharMap.Entry)this.i.next()).getByteKey();
        }
        
        public byte previousByte() {
            return this.i.previous().getByteKey();
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
            return new ValuesIterator(Byte2CharSortedMaps.fastIterator(AbstractByte2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractByte2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements CharIterator {
        protected final ObjectBidirectionalIterator<Byte2CharMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Byte2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
