package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractInt2FloatSortedMap extends AbstractInt2FloatMap implements Int2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2FloatSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2FloatSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2FloatSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2FloatSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2FloatSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2FloatMap.Entry>)AbstractInt2FloatSortedMap.this.int2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2FloatSortedMaps.fastIterator(AbstractInt2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2FloatMap.Entry)this.i.next()).getIntKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Int2FloatSortedMaps.fastIterator(AbstractInt2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractInt2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Int2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Int2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
