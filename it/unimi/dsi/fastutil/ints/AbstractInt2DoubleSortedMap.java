package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractInt2DoubleSortedMap extends AbstractInt2DoubleMap implements Int2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2DoubleSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2DoubleSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2DoubleSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2DoubleSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2DoubleMap.Entry>)AbstractInt2DoubleSortedMap.this.int2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2DoubleSortedMaps.fastIterator(AbstractInt2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2DoubleMap.Entry)this.i.next()).getIntKey();
        }
        
        public int previousInt() {
            return this.i.previous().getIntKey();
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
            return new ValuesIterator(Int2DoubleSortedMaps.fastIterator(AbstractInt2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractInt2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Int2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Int2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
