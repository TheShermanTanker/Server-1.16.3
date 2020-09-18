package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractFloat2ReferenceSortedMap<V> extends AbstractFloat2ReferenceMap<V> implements Float2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2ReferenceSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2ReferenceSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2ReferenceSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Float2ReferenceMap.Entry<?>>)AbstractFloat2ReferenceSortedMap.this.float2ReferenceEntrySet().iterator((Float2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator<>(Float2ReferenceSortedMaps.fastIterator((Float2ReferenceSortedMap<V>)AbstractFloat2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2ReferenceMap.Entry)this.i.next()).getFloatKey();
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Float2ReferenceSortedMaps.fastIterator((Float2ReferenceSortedMap<V>)AbstractFloat2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractFloat2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Float2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Float2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
