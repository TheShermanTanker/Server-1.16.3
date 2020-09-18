package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractDouble2DoubleSortedMap extends AbstractDouble2DoubleMap implements Double2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2DoubleSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2DoubleSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2DoubleSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2DoubleSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2DoubleMap.Entry>)AbstractDouble2DoubleSortedMap.this.double2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2DoubleSortedMaps.fastIterator(AbstractDouble2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2DoubleMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractDoubleCollection {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(Double2DoubleSortedMaps.fastIterator(AbstractDouble2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Double2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
