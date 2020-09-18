package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractDouble2ReferenceSortedMap<V> extends AbstractDouble2ReferenceMap<V> implements Double2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ReferenceSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ReferenceSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ReferenceSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Double2ReferenceMap.Entry<?>>)AbstractDouble2ReferenceSortedMap.this.double2ReferenceEntrySet().iterator((Double2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator<>(Double2ReferenceSortedMaps.fastIterator((Double2ReferenceSortedMap<V>)AbstractDouble2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2ReferenceMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Double2ReferenceSortedMaps.fastIterator((Double2ReferenceSortedMap<V>)AbstractDouble2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractDouble2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Double2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
