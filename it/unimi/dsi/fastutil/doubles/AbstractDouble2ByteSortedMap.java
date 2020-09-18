package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractDouble2ByteSortedMap extends AbstractDouble2ByteMap implements Double2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ByteSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ByteSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ByteSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ByteSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ByteSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2ByteMap.Entry>)AbstractDouble2ByteSortedMap.this.double2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2ByteSortedMaps.fastIterator(AbstractDouble2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2ByteMap.Entry)this.i.next()).getDoubleKey();
        }
        
        public double previousDouble() {
            return this.i.previous().getDoubleKey();
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
            return new ValuesIterator(Double2ByteSortedMaps.fastIterator(AbstractDouble2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractDouble2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Double2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Double2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
