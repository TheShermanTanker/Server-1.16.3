package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractByte2ShortSortedMap extends AbstractByte2ShortMap implements Byte2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2ShortSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2ShortSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2ShortSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2ShortSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2ShortSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2ShortMap.Entry>)AbstractByte2ShortSortedMap.this.byte2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2ShortSortedMaps.fastIterator(AbstractByte2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2ShortMap.Entry)this.i.next()).getByteKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Byte2ShortSortedMaps.fastIterator(AbstractByte2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractByte2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Byte2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Byte2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
