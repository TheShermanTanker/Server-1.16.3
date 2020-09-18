package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.shorts.ShortIterator;
import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.shorts.ShortCollection;

public abstract class AbstractObject2ShortSortedMap<K> extends AbstractObject2ShortMap<K> implements Object2ShortSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2ShortSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2ShortSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2ShortSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2ShortSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2ShortSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2ShortSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2ShortSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2ShortSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2ShortSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2ShortMap.Entry<K>>)AbstractObject2ShortSortedMap.this.object2ShortEntrySet().iterator((Object2ShortMap.Entry<K>)new BasicEntry<>((K)from, (short)0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2ShortSortedMaps.fastIterator((Object2ShortSortedMap<K>)AbstractObject2ShortSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2ShortMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractShortCollection {
        @Override
        public ShortIterator iterator() {
            return new ValuesIterator<>(Object2ShortSortedMaps.fastIterator((Object2ShortSortedMap<K>)AbstractObject2ShortSortedMap.this));
        }
        
        @Override
        public boolean contains(final short k) {
            return AbstractObject2ShortSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2ShortSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2ShortSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements ShortIterator {
        protected final ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Object2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
    
    protected static class ValuesIterator<K> implements ShortIterator {
        protected final ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Object2ShortMap.Entry)this.i.next()).getShortValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
