package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractFloat2ShortSortedMap extends AbstractFloat2ShortMap implements Float2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2ShortSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2ShortSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2ShortSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2ShortSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2ShortSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2ShortMap.Entry>)AbstractFloat2ShortSortedMap.this.float2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2ShortSortedMaps.fastIterator(AbstractFloat2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2ShortMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Float2ShortSortedMaps.fastIterator(AbstractFloat2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractFloat2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Float2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Float2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
