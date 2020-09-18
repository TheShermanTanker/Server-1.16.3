package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractShort2LongSortedMap extends AbstractShort2LongMap implements Short2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2LongSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2LongSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2LongSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2LongSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2LongSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2LongMap.Entry>)AbstractShort2LongSortedMap.this.short2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2LongSortedMaps.fastIterator(AbstractShort2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2LongMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2LongMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Short2LongSortedMaps.fastIterator(AbstractShort2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractShort2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Short2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Short2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
