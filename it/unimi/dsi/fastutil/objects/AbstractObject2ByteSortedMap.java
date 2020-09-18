package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractObject2ByteSortedMap<K> extends AbstractObject2ByteMap<K> implements Object2ByteSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractObject2ByteSortedMap() {
    }
    
    @Override
    public ObjectSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractObjectSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractObject2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractObject2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2ByteSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractObject2ByteSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractObject2ByteSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractObject2ByteSortedMap.this.lastKey();
        }
        
        @Override
        public ObjectSortedSet<K> headSet(final K to) {
            return AbstractObject2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> tailSet(final K from) {
            return AbstractObject2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ObjectSortedSet<K> subSet(final K from, final K to) {
            return AbstractObject2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Object2ByteMap.Entry<K>>)AbstractObject2ByteSortedMap.this.object2ByteEntrySet().iterator((Object2ByteMap.Entry<K>)new BasicEntry<>((K)from, (byte)0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Object2ByteSortedMaps.fastIterator((Object2ByteSortedMap<K>)AbstractObject2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Object2ByteMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Object2ByteMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Object2ByteMap.Entry)this.i.next()).getKey();
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
    
    protected class ValuesCollection extends AbstractByteCollection {
        @Override
        public ByteIterator iterator() {
            return new ValuesIterator<>(Object2ByteSortedMaps.fastIterator((Object2ByteSortedMap<K>)AbstractObject2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractObject2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractObject2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractObject2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements ByteIterator {
        protected final ObjectBidirectionalIterator<Object2ByteMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Object2ByteMap.Entry<K>> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Object2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
