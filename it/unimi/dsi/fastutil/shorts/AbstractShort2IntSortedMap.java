package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractShort2IntSortedMap extends AbstractShort2IntMap implements Short2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2IntSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2IntSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2IntSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2IntSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2IntSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2IntMap.Entry>)AbstractShort2IntSortedMap.this.short2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2IntSortedMaps.fastIterator(AbstractShort2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2IntMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2IntMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Short2IntSortedMaps.fastIterator(AbstractShort2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractShort2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Short2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Short2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
