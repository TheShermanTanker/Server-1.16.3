package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractDouble2IntSortedMap extends AbstractDouble2IntMap implements Double2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2IntSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2IntSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2IntSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2IntSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2IntSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2IntMap.Entry>)AbstractDouble2IntSortedMap.this.double2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2IntSortedMaps.fastIterator(AbstractDouble2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2IntMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2IntMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Double2IntSortedMaps.fastIterator(AbstractDouble2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractDouble2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Double2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Double2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
