package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractDouble2BooleanSortedMap extends AbstractDouble2BooleanMap implements Double2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2BooleanSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2BooleanSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2BooleanSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2BooleanSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2BooleanMap.Entry>)AbstractDouble2BooleanSortedMap.this.double2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2BooleanSortedMaps.fastIterator(AbstractDouble2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2BooleanMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Double2BooleanSortedMaps.fastIterator(AbstractDouble2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractDouble2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Double2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Double2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
