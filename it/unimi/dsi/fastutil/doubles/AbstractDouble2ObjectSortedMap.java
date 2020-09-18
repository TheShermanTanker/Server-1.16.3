package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public abstract class AbstractDouble2ObjectSortedMap<V> extends AbstractDouble2ObjectMap<V> implements Double2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2ObjectSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2ObjectSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ObjectSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2ObjectSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2ObjectSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Double2ObjectMap.Entry<?>>)AbstractDouble2ObjectSortedMap.this.double2ObjectEntrySet().iterator((Double2ObjectMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator<>(Double2ObjectSortedMaps.fastIterator((Double2ObjectSortedMap<V>)AbstractDouble2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2ObjectMap.Entry)this.i.next()).getDoubleKey();
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
    
    protected class ValuesCollection extends AbstractObjectCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Double2ObjectSortedMaps.fastIterator((Double2ObjectSortedMap<V>)AbstractDouble2ObjectSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractDouble2ObjectSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Double2ObjectMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
