package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractDouble2ShortSortedMap extends AbstractDouble2ShortMap implements Double2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ShortSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ShortSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ShortSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ShortSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ShortSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2ShortMap.Entry>)AbstractDouble2ShortSortedMap.this.double2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2ShortSortedMaps.fastIterator(AbstractDouble2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2ShortMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Double2ShortSortedMaps.fastIterator(AbstractDouble2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractDouble2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Double2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Double2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
