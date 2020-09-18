package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.longs.LongCollection;

public abstract class AbstractInt2LongSortedMap extends AbstractInt2LongMap implements Int2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2LongSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2LongSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2LongSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2LongSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2LongSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2LongSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2LongSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2LongSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2LongSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2LongMap.Entry>)AbstractInt2LongSortedMap.this.int2LongEntrySet().iterator(new BasicEntry(from, 0L)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2LongSortedMaps.fastIterator(AbstractInt2LongSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2LongMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2LongMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2LongMap.Entry)this.i.next()).getIntKey();
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
    
    protected class ValuesCollection extends AbstractLongCollection {
        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Int2LongSortedMaps.fastIterator(AbstractInt2LongSortedMap.this));
        }
        
        @Override
        public boolean contains(final long k) {
            return AbstractInt2LongSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2LongSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2LongSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements LongIterator {
        protected final ObjectBidirectionalIterator<Int2LongMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2LongMap.Entry> i) {
            this.i = i;
        }
        
        public long nextLong() {
            return ((Int2LongMap.Entry)this.i.next()).getLongValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
