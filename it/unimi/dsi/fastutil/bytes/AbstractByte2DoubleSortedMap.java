package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractByte2DoubleSortedMap extends AbstractByte2DoubleMap implements Byte2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2DoubleSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2DoubleSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2DoubleSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2DoubleSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2DoubleMap.Entry>)AbstractByte2DoubleSortedMap.this.byte2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2DoubleSortedMaps.fastIterator(AbstractByte2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2DoubleMap.Entry)this.i.next()).getByteKey();
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
    
    protected class ValuesCollection extends AbstractDoubleCollection {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(Byte2DoubleSortedMaps.fastIterator(AbstractByte2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractByte2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Byte2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Byte2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
