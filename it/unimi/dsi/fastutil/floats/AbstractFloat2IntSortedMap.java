package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntCollection;

public abstract class AbstractFloat2IntSortedMap extends AbstractFloat2IntMap implements Float2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2IntSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2IntSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2IntSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2IntSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2IntSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2IntMap.Entry>)AbstractFloat2IntSortedMap.this.float2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2IntSortedMaps.fastIterator(AbstractFloat2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2IntMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2IntMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Float2IntSortedMaps.fastIterator(AbstractFloat2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractFloat2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Float2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Float2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
