package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractFloat2LongSortedMap extends AbstractFloat2LongMap implements Float2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2LongSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2LongSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2LongSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2LongSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2LongSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2LongMap.Entry>)AbstractFloat2LongSortedMap.this.float2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2LongSortedMaps.fastIterator(AbstractFloat2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2LongMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2LongMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Float2LongSortedMaps.fastIterator(AbstractFloat2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractFloat2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Float2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Float2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
