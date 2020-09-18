package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractShort2BooleanSortedMap extends AbstractShort2BooleanMap implements Short2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2BooleanSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2BooleanSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2BooleanSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2BooleanSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2BooleanMap.Entry>)AbstractShort2BooleanSortedMap.this.short2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2BooleanSortedMaps.fastIterator(AbstractShort2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2BooleanMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Short2BooleanSortedMaps.fastIterator(AbstractShort2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractShort2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Short2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Short2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
