package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.booleans.BooleanIterator;
import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.booleans.BooleanCollection;

public abstract class AbstractChar2BooleanSortedMap extends AbstractChar2BooleanMap implements Char2BooleanSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2BooleanSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public BooleanCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2BooleanSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2BooleanSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2BooleanSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2BooleanSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2BooleanSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2BooleanSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2BooleanSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2BooleanSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Char2BooleanMap.Entry>)AbstractChar2BooleanSortedMap.this.char2BooleanEntrySet().iterator(new BasicEntry(from, false)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2BooleanSortedMaps.fastIterator(AbstractChar2BooleanSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2BooleanMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2BooleanMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractBooleanCollection {
        @Override
        public BooleanIterator iterator() {
            return new ValuesIterator(Char2BooleanSortedMaps.fastIterator(AbstractChar2BooleanSortedMap.this));
        }
        
        @Override
        public boolean contains(final boolean k) {
            return AbstractChar2BooleanSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2BooleanSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2BooleanSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements BooleanIterator {
        protected final ObjectBidirectionalIterator<Char2BooleanMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2BooleanMap.Entry> i) {
            this.i = i;
        }
        
        public boolean nextBoolean() {
            return ((Char2BooleanMap.Entry)this.i.next()).getBooleanValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
