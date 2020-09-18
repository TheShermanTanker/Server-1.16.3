package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractFloat2DoubleSortedMap extends AbstractFloat2DoubleMap implements Float2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2DoubleSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2DoubleSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2DoubleSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2DoubleSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2DoubleMap.Entry>)AbstractFloat2DoubleSortedMap.this.float2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2DoubleSortedMaps.fastIterator(AbstractFloat2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2DoubleMap.Entry)this.i.next()).getFloatKey();
        }
        
        public float previousFloat() {
            return this.i.previous().getFloatKey();
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
            return new ValuesIterator(Float2DoubleSortedMaps.fastIterator(AbstractFloat2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractFloat2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Float2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Float2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
