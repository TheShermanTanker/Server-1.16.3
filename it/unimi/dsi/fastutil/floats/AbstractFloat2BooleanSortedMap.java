package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractFloat2BooleanSortedMap extends AbstractFloat2BooleanMap implements Float2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2BooleanSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2BooleanSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2BooleanSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2BooleanSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2BooleanMap.Entry>)AbstractFloat2BooleanSortedMap.this.float2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2BooleanSortedMaps.fastIterator(AbstractFloat2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2BooleanMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Float2BooleanSortedMaps.fastIterator(AbstractFloat2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractFloat2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Float2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Float2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
