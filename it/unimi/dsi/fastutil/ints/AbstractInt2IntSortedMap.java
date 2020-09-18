package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;

public abstract class AbstractInt2IntSortedMap extends AbstractInt2IntMap implements Int2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2IntSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2IntSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2IntSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2IntSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2IntSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2IntSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2IntSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2IntSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2IntSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2IntMap.Entry>)AbstractInt2IntSortedMap.this.int2IntEntrySet().iterator(new BasicEntry(from, 0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2IntSortedMaps.fastIterator(AbstractInt2IntSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2IntMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2IntMap.Entry)this.i.next()).getIntKey();
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
    
    protected class ValuesCollection extends AbstractIntCollection {
        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Int2IntSortedMaps.fastIterator(AbstractInt2IntSortedMap.this));
        }
        
        @Override
        public boolean contains(final int k) {
            return AbstractInt2IntSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2IntSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2IntSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements IntIterator {
        protected final ObjectBidirectionalIterator<Int2IntMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2IntMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2IntMap.Entry)this.i.next()).getIntValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
