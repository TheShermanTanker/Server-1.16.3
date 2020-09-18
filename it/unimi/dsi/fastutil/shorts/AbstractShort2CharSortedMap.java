package it.unimi.dsi.fastutil.shorts;

import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractShort2CharSortedMap extends AbstractShort2CharMap implements Short2CharSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractShort2CharSortedMap() {
    }
    
    @Override
    public ShortSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractShortSortedSet {
        @Override
        public boolean contains(final short k) {
            return AbstractShort2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractShort2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2CharSortedMap.this.clear();
        }
        
        @Override
        public ShortComparator comparator() {
            return AbstractShort2CharSortedMap.this.comparator();
        }
        
        @Override
        public short firstShort() {
            return AbstractShort2CharSortedMap.this.firstShortKey();
        }
        
        @Override
        public short lastShort() {
            return AbstractShort2CharSortedMap.this.lastShortKey();
        }
        
        @Override
        public ShortSortedSet headSet(final short to) {
            return AbstractShort2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public ShortSortedSet tailSet(final short from) {
            return AbstractShort2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public ShortSortedSet subSet(final short from, final short to) {
            return AbstractShort2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public ShortBidirectionalIterator iterator(final short from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Short2CharMap.Entry>)AbstractShort2CharSortedMap.this.short2CharEntrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public ShortBidirectionalIterator iterator() {
            return new KeySetIterator(Short2CharSortedMaps.fastIterator(AbstractShort2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements ShortBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Short2CharMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Short2CharMap.Entry> i) {
            this.i = i;
        }
        
        public short nextShort() {
            return ((Short2CharMap.Entry)this.i.next()).getShortKey();
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
    
    protected class ValuesCollection extends AbstractCharCollection {
        @Override
        public CharIterator iterator() {
            return new ValuesIterator(Short2CharSortedMaps.fastIterator(AbstractShort2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractShort2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractShort2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractShort2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements CharIterator {
        protected final ObjectBidirectionalIterator<Short2CharMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Short2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Short2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
