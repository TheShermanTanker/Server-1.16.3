package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractLong2FloatSortedMap extends AbstractLong2FloatMap implements Long2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2FloatSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2FloatSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2FloatSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2FloatSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2FloatSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2FloatMap.Entry>)AbstractLong2FloatSortedMap.this.long2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2FloatSortedMaps.fastIterator(AbstractLong2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2FloatMap.Entry)this.i.next()).getLongKey();
        }
        
        public long previousLong() {
            return this.i.previous().getLongKey();
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
            return new ValuesIterator(Long2FloatSortedMaps.fastIterator(AbstractLong2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractLong2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Long2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Long2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
