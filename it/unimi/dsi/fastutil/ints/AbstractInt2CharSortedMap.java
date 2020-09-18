package it.unimi.dsi.fastutil.ints;

import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractInt2CharSortedMap extends AbstractInt2CharMap implements Int2CharSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractInt2CharSortedMap() {
    }
    
    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractIntSortedSet {
        @Override
        public boolean contains(final int k) {
            return AbstractInt2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractInt2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2CharSortedMap.this.clear();
        }
        
        @Override
        public IntComparator comparator() {
            return AbstractInt2CharSortedMap.this.comparator();
        }
        
        @Override
        public int firstInt() {
            return AbstractInt2CharSortedMap.this.firstIntKey();
        }
        
        @Override
        public int lastInt() {
            return AbstractInt2CharSortedMap.this.lastIntKey();
        }
        
        @Override
        public IntSortedSet headSet(final int to) {
            return AbstractInt2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public IntSortedSet tailSet(final int from) {
            return AbstractInt2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public IntSortedSet subSet(final int from, final int to) {
            return AbstractInt2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public IntBidirectionalIterator iterator(final int from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Int2CharMap.Entry>)AbstractInt2CharSortedMap.this.int2CharEntrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2CharSortedMaps.fastIterator(AbstractInt2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2CharMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Int2CharMap.Entry> i) {
            this.i = i;
        }
        
        public int nextInt() {
            return ((Int2CharMap.Entry)this.i.next()).getIntKey();
        }
        
        public int previousInt() {
            return this.i.previous().getIntKey();
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
            return new ValuesIterator(Int2CharSortedMaps.fastIterator(AbstractInt2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractInt2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractInt2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractInt2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements CharIterator {
        protected final ObjectBidirectionalIterator<Int2CharMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Int2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Int2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
