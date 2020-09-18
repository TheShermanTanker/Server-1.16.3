package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractInt2ShortSortedMap extends AbstractInt2ShortMap implements Int2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ShortSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2ShortSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ShortSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ShortSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ShortSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2ShortMap.Entry>)AbstractInt2ShortSortedMap.this.int2ShortEntrySet().iterator(new BasicEntry(from, (short)0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2ShortSortedMaps.fastIterator(AbstractInt2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2ShortMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2ShortMap.Entry)this.i.next()).getIntKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Int2ShortSortedMaps.fastIterator(AbstractInt2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractInt2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ShortIterator {
        protected final ObjectBidirectionalIterator<Int2ShortMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2ShortMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Int2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
