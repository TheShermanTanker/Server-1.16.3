package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractObject2BooleanSortedMap<K> extends AbstractObject2BooleanMap<K> implements Object2BooleanSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2BooleanSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2BooleanSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2BooleanSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2BooleanSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2BooleanSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>>)AbstractObject2BooleanSortedMap.this.object2BooleanEntrySet().iterator((Object2BooleanMap.Entry<K>)new BasicEntry<>((K)from, false)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2BooleanSortedMaps.fastIterator((Object2BooleanSortedMap<K>)AbstractObject2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2BooleanMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator<>(Object2BooleanSortedMaps.fastIterator((Object2BooleanSortedMap<K>)AbstractObject2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractObject2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Object2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
