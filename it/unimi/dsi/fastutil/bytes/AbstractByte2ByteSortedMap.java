package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractByte2ByteSortedMap extends AbstractByte2ByteMap implements Byte2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2ByteSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2ByteSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2ByteSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2ByteSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2ByteSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2ByteMap.Entry>)AbstractByte2ByteSortedMap.this.byte2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2ByteSortedMaps.fastIterator(AbstractByte2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2ByteMap.Entry)this.i.next()).getByteKey();
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
    
    protected class ValuesCollection extends AbstractByteCollection {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(Byte2ByteSortedMaps.fastIterator(AbstractByte2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Byte2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
