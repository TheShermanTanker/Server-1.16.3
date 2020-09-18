package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public abstract class AbstractShort2ObjectSortedMap<V> extends AbstractShort2ObjectMap<V> implements Short2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2ObjectSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ObjectSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ObjectSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2ObjectSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2ObjectSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Short2ObjectMap.Entry<?>>)AbstractShort2ObjectSortedMap.this.short2ObjectEntrySet().iterator((Short2ObjectMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator<>(Short2ObjectSortedMaps.fastIterator((Short2ObjectSortedMap<V>)AbstractShort2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2ObjectMap.Entry)this.i.next()).getShortKey();
        }
        
        public short previousShort() {
            return this.i.previous().getShortKey();
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
            return new ValuesIterator<V>(Short2ObjectSortedMaps.fastIterator((Short2ObjectSortedMap<V>)AbstractShort2ObjectSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractShort2ObjectSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Short2ObjectMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
