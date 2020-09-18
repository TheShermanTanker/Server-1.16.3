package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractLong2ShortSortedMap extends AbstractLong2ShortMap implements Long2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2ShortSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ShortSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2ShortSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2ShortSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2ShortSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2ShortMap.Entry>)AbstractLong2ShortSortedMap.this.long2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2ShortSortedMaps.fastIterator(AbstractLong2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2ShortMap.Entry)this.i.next()).getLongKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Long2ShortSortedMaps.fastIterator(AbstractLong2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractLong2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Long2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Long2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
