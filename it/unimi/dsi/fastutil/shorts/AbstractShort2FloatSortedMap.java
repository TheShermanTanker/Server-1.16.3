package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractShort2FloatSortedMap extends AbstractShort2FloatMap implements Short2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2FloatSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2FloatSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2FloatSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2FloatSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2FloatSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2FloatMap.Entry>)AbstractShort2FloatSortedMap.this.short2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2FloatSortedMaps.fastIterator(AbstractShort2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2FloatMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Short2FloatSortedMaps.fastIterator(AbstractShort2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractShort2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Short2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Short2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
