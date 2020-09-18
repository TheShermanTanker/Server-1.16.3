package it.unimi.dsi.fastutil.doubles;

import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractDouble2CharSortedMap extends AbstractDouble2CharMap implements Double2CharSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractDouble2CharSortedMap() {
    }
    
    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractDoubleSortedSet {
        @Override
        public boolean contains(final double k) {
            return AbstractDouble2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractDouble2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2CharSortedMap.this.clear();
        }
        
        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2CharSortedMap.this.comparator();
        }
        
        @Override
        public double firstDouble() {
            return AbstractDouble2CharSortedMap.this.firstDoubleKey();
        }
        
        @Override
        public double lastDouble() {
            return AbstractDouble2CharSortedMap.this.lastDoubleKey();
        }
        
        @Override
        public DoubleSortedSet headSet(final double to) {
            return AbstractDouble2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public DoubleSortedSet tailSet(final double from) {
            return AbstractDouble2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public DoubleSortedSet subSet(final double from, final double to) {
            return AbstractDouble2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public DoubleBidirectionalIterator iterator(final double from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Double2CharMap.Entry>)AbstractDouble2CharSortedMap.this.double2CharEntrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2CharSortedMaps.fastIterator(AbstractDouble2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2CharMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Double2CharMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Double2CharMap.Entry)this.i.next()).getDoubleKey();
        }
        
        public double previousDouble() {
            return this.i.previous().getDoubleKey();
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
            return new ValuesIterator(Double2CharSortedMaps.fastIterator(AbstractDouble2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractDouble2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractDouble2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractDouble2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements CharIterator {
        protected final ObjectBidirectionalIterator<Double2CharMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Double2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Double2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
