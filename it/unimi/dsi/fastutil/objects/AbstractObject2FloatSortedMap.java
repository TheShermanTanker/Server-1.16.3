package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractObject2FloatSortedMap<K> extends AbstractObject2FloatMap<K> implements Object2FloatSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2FloatSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2FloatSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2FloatSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2FloatSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2FloatSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2FloatMap.Entry<K>>)AbstractObject2FloatSortedMap.this.object2FloatEntrySet().iterator((Object2FloatMap.Entry<K>)new BasicEntry<>((K)from, 0.0f)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2FloatSortedMaps.fastIterator((Object2FloatSortedMap<K>)AbstractObject2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2FloatMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2FloatMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2FloatMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator<>(Object2FloatSortedMaps.fastIterator((Object2FloatSortedMap<K>)AbstractObject2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractObject2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements FloatIterator {
        protected final ObjectBidirectionalIterator<Object2FloatMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2FloatMap.Entry<K>> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Object2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
