package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractLong2LongSortedMap extends AbstractLong2LongMap implements Long2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2LongSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2LongSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2LongSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2LongSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2LongSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2LongMap.Entry>)AbstractLong2LongSortedMap.this.long2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2LongSortedMaps.fastIterator(AbstractLong2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2LongMap.Entry)this.i.next()).getLongKey();
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
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Long2LongSortedMaps.fastIterator(AbstractLong2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractLong2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Long2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
