package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractLong2DoubleSortedMap extends AbstractLong2DoubleMap implements Long2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2DoubleSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2DoubleSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2DoubleSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2DoubleSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2DoubleMap.Entry>)AbstractLong2DoubleSortedMap.this.long2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2DoubleSortedMaps.fastIterator(AbstractLong2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2DoubleMap.Entry)this.i.next()).getLongKey();
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
    
    protected class ValuesCollection extends AbstractDoubleCollection {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(Long2DoubleSortedMaps.fastIterator(AbstractLong2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractLong2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Long2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Long2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
