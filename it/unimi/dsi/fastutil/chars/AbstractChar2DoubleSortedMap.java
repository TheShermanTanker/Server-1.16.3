package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.doubles.DoubleIterator;
import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.doubles.DoubleCollection;

public abstract class AbstractChar2DoubleSortedMap extends AbstractChar2DoubleMap implements Char2DoubleSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2DoubleSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public DoubleCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2DoubleSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2DoubleSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2DoubleSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2DoubleSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2DoubleSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2DoubleSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2DoubleSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2DoubleSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2DoubleMap.Entry>)AbstractChar2DoubleSortedMap.this.char2DoubleEntrySet().iterator(new BasicEntry(from, 0.0)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2DoubleSortedMaps.fastIterator(AbstractChar2DoubleSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2DoubleMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2DoubleMap.Entry)this.i.next()).getCharKey();
        }
        
        public char previousChar() {
            return this.i.previous().getCharKey();
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
            return new ValuesIterator(Char2DoubleSortedMaps.fastIterator(AbstractChar2DoubleSortedMap.this));
        }
        
        @Override
        public boolean contains(final double k) {
            return AbstractChar2DoubleSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2DoubleSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2DoubleSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements DoubleIterator {
        protected final ObjectBidirectionalIterator<Char2DoubleMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2DoubleMap.Entry> i) {
            this.i = i;
        }
        
        public double nextDouble() {
            return ((Char2DoubleMap.Entry)this.i.next()).getDoubleValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
