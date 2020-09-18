package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractFloat2ByteSortedMap extends AbstractFloat2ByteMap implements Float2ByteSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2ByteSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2ByteSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2ByteSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2ByteSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2ByteSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2ByteMap.Entry>)AbstractFloat2ByteSortedMap.this.float2ByteEntrySet().iterator(new BasicEntry(from, (byte)0)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2ByteSortedMaps.fastIterator(AbstractFloat2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2ByteMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2ByteMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractByteCollection {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator(Float2ByteSortedMaps.fastIterator(AbstractFloat2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractFloat2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements ByteIterator {
        protected final ObjectBidirectionalIterator<Float2ByteMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2ByteMap.Entry> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Float2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
