package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractInt2BooleanSortedMap extends AbstractInt2BooleanMap implements Int2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2BooleanSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2BooleanSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2BooleanSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2BooleanSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2BooleanMap.Entry>)AbstractInt2BooleanSortedMap.this.int2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2BooleanSortedMaps.fastIterator(AbstractInt2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2BooleanMap.Entry)this.i.next()).getIntKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Int2BooleanSortedMaps.fastIterator(AbstractInt2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractInt2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Int2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Int2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
