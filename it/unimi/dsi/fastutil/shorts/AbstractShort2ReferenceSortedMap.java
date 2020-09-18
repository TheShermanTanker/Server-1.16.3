package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractShort2ReferenceSortedMap<V> extends AbstractShort2ReferenceMap<V> implements Short2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2ReferenceSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2ReferenceSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2ReferenceSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Short2ReferenceMap.Entry<?>>)AbstractShort2ReferenceSortedMap.this.short2ReferenceEntrySet().iterator((Short2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator<>(Short2ReferenceSortedMaps.fastIterator((Short2ReferenceSortedMap<V>)AbstractShort2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2ReferenceMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractReferenceCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Short2ReferenceSortedMaps.fastIterator((Short2ReferenceSortedMap<V>)AbstractShort2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractShort2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Short2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
