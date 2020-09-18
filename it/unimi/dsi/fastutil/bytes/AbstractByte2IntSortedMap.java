package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractByte2IntSortedMap extends AbstractByte2IntMap implements Byte2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2IntSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2IntSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2IntSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2IntSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2IntSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2IntMap.Entry>)AbstractByte2IntSortedMap.this.byte2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2IntSortedMaps.fastIterator(AbstractByte2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2IntMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2IntMap.Entry)this.i.next()).getByteKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Byte2IntSortedMaps.fastIterator(AbstractByte2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractByte2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Byte2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Byte2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
