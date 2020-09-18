package it.unimi.dsi.fastutil.objects;

import it.unimi.dsi.fastutil.bytes.ByteIterator;
import it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import java.util.SortedSet;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.bytes.ByteCollection;

public abstract class AbstractReference2ByteSortedMap<K> extends AbstractReference2ByteMap<K> implements Reference2ByteSortedMap<K> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractReference2ByteSortedMap() {
    }
    
    @Override
    public ReferenceSortedSet<K> keySet() {
        return new KeySet();
    }
    
    @Override
    public ByteCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractReferenceSortedSet<K> {
        public boolean contains(final Object k) {
            return AbstractReference2ByteSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractReference2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ByteSortedMap.this.clear();
        }
        
        public Comparator<? super K> comparator() {
            return AbstractReference2ByteSortedMap.this.comparator();
        }
        
        public K first() {
            return (K)AbstractReference2ByteSortedMap.this.firstKey();
        }
        
        public K last() {
            return (K)AbstractReference2ByteSortedMap.this.lastKey();
        }
        
        @Override
        public ReferenceSortedSet<K> headSet(final K to) {
            return AbstractReference2ByteSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> tailSet(final K from) {
            return AbstractReference2ByteSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ReferenceSortedSet<K> subSet(final K from, final K to) {
            return AbstractReference2ByteSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator(final K from) {
            return new KeySetIterator<K>((ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>>)AbstractReference2ByteSortedMap.this.reference2ByteEntrySet().iterator((Reference2ByteMap.Entry<K>)new BasicEntry<>((K)from, (byte)0)));
        }
        
        @Override
        public ObjectBidirectionalIterator<K> iterator() {
            return new KeySetIterator<K>(Reference2ByteSortedMaps.fastIterator((Reference2ByteSortedMap<K>)AbstractReference2ByteSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<K> implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> i) {
            this.i = i;
        }
        
        public K next() {
            return (K)((Reference2ByteMap.Entry)this.i.next()).getKey();
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
            return new ValuesIterator<>(Reference2ByteSortedMaps.fastIterator((Reference2ByteSortedMap<K>)AbstractReference2ByteSortedMap.this));
        }
        
        @Override
        public boolean contains(final byte k) {
            return AbstractReference2ByteSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractReference2ByteSortedMap.this.size();
        }
        
        public void clear() {
            AbstractReference2ByteSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<K> implements ByteIterator {
        protected final ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Reference2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
    
    protected static class ValuesIterator<K> implements ByteIterator {
        protected final ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Reference2ByteMap.Entry<K>> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Reference2ByteMap.Entry)this.i.next()).getByteValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
