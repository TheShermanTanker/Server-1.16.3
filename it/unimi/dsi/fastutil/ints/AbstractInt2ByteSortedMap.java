package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractInt2ByteSortedMap extends AbstractInt2ByteMap implements Int2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2ByteSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2ByteSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2ByteSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2ByteSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2ByteSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2ByteMap.Entry>)AbstractInt2ByteSortedMap.this.int2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2ByteSortedMaps.fastIterator(AbstractInt2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2ByteMap.Entry)this.i.next()).getIntKey();
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
    
    protected class ValuesCollection extends AbstractByteCollection {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(Int2ByteSortedMaps.fastIterator(AbstractInt2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractInt2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Int2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Int2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
