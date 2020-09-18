package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.floats.FloatIterator;
import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.floats.FloatCollection;

public abstract class AbstractChar2FloatSortedMap extends AbstractChar2FloatMap implements Char2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2FloatSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2FloatSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2FloatSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2FloatSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2FloatSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2FloatSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2FloatSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2FloatSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2FloatSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2FloatMap.Entry>)AbstractChar2FloatSortedMap.this.char2FloatEntrySet().iterator(new BasicEntry(from, 0.0f)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2FloatSortedMaps.fastIterator(AbstractChar2FloatSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2FloatMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2FloatMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractFloatCollection {
        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Char2FloatSortedMaps.fastIterator(AbstractChar2FloatSortedMap.this));
        }
        
        @Override
        public boolean contains(final float k) {
            return AbstractChar2FloatSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2FloatSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2FloatSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements FloatIterator {
        protected final ObjectBidirectionalIterator<Char2FloatMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2FloatMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Char2FloatMap.Entry)this.i.next()).getFloatValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
