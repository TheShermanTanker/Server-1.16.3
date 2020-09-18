package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractByte2LongSortedMap extends AbstractByte2LongMap implements Byte2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2LongSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2LongSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2LongSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2LongSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2LongSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2LongMap.Entry>)AbstractByte2LongSortedMap.this.byte2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2LongSortedMaps.fastIterator(AbstractByte2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2LongMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2LongMap.Entry)this.i.next()).getByteKey();
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
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Byte2LongSortedMaps.fastIterator(AbstractByte2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractByte2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Byte2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Byte2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
