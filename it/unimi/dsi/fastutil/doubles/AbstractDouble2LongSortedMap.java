package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractDouble2LongSortedMap extends AbstractDouble2LongMap implements Double2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2LongSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2LongSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2LongSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2LongSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2LongSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2LongMap.Entry>)AbstractDouble2LongSortedMap.this.double2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2LongSortedMaps.fastIterator(AbstractDouble2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2LongMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2LongMap.Entry)this.i.next()).getDoubleKey();
        }
        
        public double previousDouble() {
            return this.i.previous().getDoubleKey();
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
            return new ValuesIterator(Double2LongSortedMaps.fastIterator(AbstractDouble2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractDouble2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Double2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Double2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
