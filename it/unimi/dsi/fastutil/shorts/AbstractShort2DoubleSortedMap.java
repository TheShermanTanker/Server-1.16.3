package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractShort2DoubleSortedMap extends AbstractShort2DoubleMap implements Short2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2DoubleSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2DoubleSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2DoubleSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2DoubleSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2DoubleMap.Entry>)AbstractShort2DoubleSortedMap.this.short2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2DoubleSortedMaps.fastIterator(AbstractShort2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2DoubleMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractDoubleCollection {
        @Override
        public DoubleIterator iterator() {
            return new ValuesIterator(Short2DoubleSortedMaps.fastIterator(AbstractShort2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractShort2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Short2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Short2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
