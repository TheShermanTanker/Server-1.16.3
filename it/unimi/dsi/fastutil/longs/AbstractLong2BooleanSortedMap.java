package it.unimi.dsi.fastutil.longs;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractLong2BooleanSortedMap extends AbstractLong2BooleanMap implements Long2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractLong2BooleanSortedMap() {
    }
    
    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractLongSortedSet {
        @Override
        public boolean contains(final long k) {
            return AbstractLong2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractLong2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2BooleanSortedMap.this.clear();
        }
        
        @Override
        public LongComparator comparator() {
            return AbstractLong2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public long firstLong() {
            return AbstractLong2BooleanSortedMap.this.firstLongKey();
        }
        
        @Override
        public long lastLong() {
            return AbstractLong2BooleanSortedMap.this.lastLongKey();
        }
        
        @Override
        public LongSortedSet headSet(final long to) {
            return AbstractLong2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public LongSortedSet tailSet(final long from) {
            return AbstractLong2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public LongSortedSet subSet(final long from, final long to) {
            return AbstractLong2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public LongBidirectionalIterator iterator(final long from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Long2BooleanMap.Entry>)AbstractLong2BooleanSortedMap.this.long2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2BooleanSortedMaps.fastIterator(AbstractLong2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Long2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Long2BooleanMap.Entry)this.i.next()).getLongKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Long2BooleanSortedMaps.fastIterator(AbstractLong2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractLong2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractLong2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractLong2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Long2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Long2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Long2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
