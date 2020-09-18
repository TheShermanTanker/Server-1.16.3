package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractLong2IntSortedMap extends AbstractLong2IntMap implements Long2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2IntSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2IntSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2IntSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2IntSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2IntSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2IntMap.Entry>)AbstractLong2IntSortedMap.this.long2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2IntSortedMaps.fastIterator(AbstractLong2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2IntMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2IntMap.Entry)this.i.next()).getLongKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Long2IntSortedMaps.fastIterator(AbstractLong2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractLong2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Long2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Long2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
