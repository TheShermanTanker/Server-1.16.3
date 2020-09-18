package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractByte2FloatSortedMap extends AbstractByte2FloatMap implements Byte2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2FloatSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2FloatSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2FloatSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2FloatSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2FloatSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Byte2FloatMap.Entry>)AbstractByte2FloatSortedMap.this.byte2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator(Byte2FloatSortedMaps.fastIterator(AbstractByte2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2FloatMap.Entry)this.i.next()).getByteKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Byte2FloatSortedMaps.fastIterator(AbstractByte2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractByte2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Byte2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Byte2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
