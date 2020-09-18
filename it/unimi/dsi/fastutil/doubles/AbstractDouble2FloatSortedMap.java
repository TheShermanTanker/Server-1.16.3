package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractDouble2FloatSortedMap extends AbstractDouble2FloatMap implements Double2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2FloatSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2FloatSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2FloatSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2FloatSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2FloatSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2FloatMap.Entry>)AbstractDouble2FloatSortedMap.this.double2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2FloatSortedMaps.fastIterator(AbstractDouble2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2FloatMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Double2FloatSortedMaps.fastIterator(AbstractDouble2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractDouble2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Double2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Double2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
