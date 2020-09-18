package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractShort2ByteSortedMap extends AbstractShort2ByteMap implements Short2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2ByteSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ByteSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2ByteSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2ByteSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2ByteSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2ByteMap.Entry>)AbstractShort2ByteSortedMap.this.short2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2ByteSortedMaps.fastIterator(AbstractShort2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2ByteMap.Entry)this.i.next()).getShortKey();
        }
        
        public short previousShort() {
            return this.i.previous().getShortKey();
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
            return new ValuesIterator(Short2ByteSortedMaps.fastIterator(AbstractShort2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractShort2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Short2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Short2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
