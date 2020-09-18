package it.unimi.dsi.fastutil.bytes;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractByte2ReferenceSortedMap<V> extends AbstractByte2ReferenceMap<V> implements Byte2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractByte2ReferenceSortedMap() {
    }
    
    @Override
    public ByteSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractByteSortedSet {
        @Override
        public boolean contains(final byte k) {
            return AbstractByte2ReferenceSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractByte2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2ReferenceSortedMap.this.clear();
        }
        
        @Override
        public ByteComparator comparator() {
            return AbstractByte2ReferenceSortedMap.this.comparator();
        }
        
        @Override
        public byte firstByte() {
            return AbstractByte2ReferenceSortedMap.this.firstByteKey();
        }
        
        @Override
        public byte lastByte() {
            return AbstractByte2ReferenceSortedMap.this.lastByteKey();
        }
        
        @Override
        public ByteSortedSet headSet(final byte to) {
            return AbstractByte2ReferenceSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ByteSortedSet tailSet(final byte from) {
            return AbstractByte2ReferenceSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ByteSortedSet subSet(final byte from, final byte to) {
            return AbstractByte2ReferenceSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ByteBidirectionalIterator iterator(final byte from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Byte2ReferenceMap.Entry<?>>)AbstractByte2ReferenceSortedMap.this.byte2ReferenceEntrySet().iterator((Byte2ReferenceMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public ByteBidirectionalIterator iterator() {
            return new KeySetIterator<>(Byte2ReferenceSortedMaps.fastIterator((Byte2ReferenceSortedMap<V>)AbstractByte2ReferenceSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements ByteBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Byte2ReferenceMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Byte2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public byte nextByte() {
            return ((Byte2ReferenceMap.Entry)this.i.next()).getByteKey();
        }
        
        public byte previousByte() {
            return this.i.previous().getByteKey();
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
            return new ValuesIterator<V>(Byte2ReferenceSortedMaps.fastIterator((Byte2ReferenceSortedMap<V>)AbstractByte2ReferenceSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractByte2ReferenceSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractByte2ReferenceSortedMap.this.size();
        }
        
        public void clear() {
            AbstractByte2ReferenceSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Byte2ReferenceMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Byte2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Byte2ReferenceMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
