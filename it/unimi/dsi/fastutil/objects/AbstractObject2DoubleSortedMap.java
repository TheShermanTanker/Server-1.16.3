package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractObject2DoubleSortedMap<K> extends AbstractObject2DoubleMap<K> implements Object2DoubleSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2DoubleSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2DoubleSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2DoubleSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2DoubleSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2DoubleSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>>)AbstractObject2DoubleSortedMap.this.object2DoubleEntrySet().iterator((Object2DoubleMap.Entry<K>)new BasicEntry<>((K)from, 0.0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2DoubleSortedMaps.fastIterator((Object2DoubleSortedMap<K>)AbstractObject2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2DoubleMap.Entry)this.i.next()).getKey();
        }
        
        public K previous() {
            return (K)this.i.previous().getKey();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
        
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }
    
    protected class ValuesCollection extends AbstractDoubleCollection {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator<>(Object2DoubleSortedMaps.fastIterator((Object2DoubleSortedMap<K>)AbstractObject2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractObject2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2DoubleMap.Entry<K>> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Object2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
