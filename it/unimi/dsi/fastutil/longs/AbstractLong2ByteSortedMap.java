package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractLong2ByteSortedMap extends AbstractLong2ByteMap implements Long2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ByteSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ByteSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ByteSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ByteSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ByteSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2ByteMap.Entry>)AbstractLong2ByteSortedMap.this.long2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2ByteSortedMaps.fastIterator(AbstractLong2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2ByteMap.Entry)this.i.next()).getLongKey();
        }
        
        public long previousLong() {
            return this.i.previous().getLongKey();
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
            return new ValuesIterator(Long2ByteSortedMaps.fastIterator(AbstractLong2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractLong2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Long2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Long2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
