package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractShort2ShortSortedMap extends AbstractShort2ShortMap implements Short2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2ShortSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ShortSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2ShortSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2ShortSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2ShortSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2ShortMap.Entry>)AbstractShort2ShortSortedMap.this.short2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2ShortSortedMaps.fastIterator(AbstractShort2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2ShortMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Short2ShortSortedMaps.fastIterator(AbstractShort2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Short2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
