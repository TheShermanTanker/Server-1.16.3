package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractFloat2FloatSortedMap extends AbstractFloat2FloatMap implements Float2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2FloatSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2FloatSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2FloatSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2FloatSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2FloatSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2FloatMap.Entry>)AbstractFloat2FloatSortedMap.this.float2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2FloatSortedMaps.fastIterator(AbstractFloat2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2FloatMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Float2FloatSortedMaps.fastIterator(AbstractFloat2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Float2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
