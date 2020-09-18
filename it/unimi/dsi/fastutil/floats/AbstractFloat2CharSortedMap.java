package it.unimi.dsi.fastutil.floats;

import it.unimi.dsi.fastutil.chars.CharIterator;
import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.chars.CharCollection;

public abstract class AbstractFloat2CharSortedMap extends AbstractFloat2CharMap implements Float2CharSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractFloat2CharSortedMap() {
    }
    
    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public CharCollection values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractFloatSortedSet {
        @Override
        public boolean contains(final float k) {
            return AbstractFloat2CharSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractFloat2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2CharSortedMap.this.clear();
        }
        
        @Override
        public FloatComparator comparator() {
            return AbstractFloat2CharSortedMap.this.comparator();
        }
        
        @Override
        public float firstFloat() {
            return AbstractFloat2CharSortedMap.this.firstFloatKey();
        }
        
        @Override
        public float lastFloat() {
            return AbstractFloat2CharSortedMap.this.lastFloatKey();
        }
        
        @Override
        public FloatSortedSet headSet(final float to) {
            return AbstractFloat2CharSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public FloatSortedSet tailSet(final float from) {
            return AbstractFloat2CharSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public FloatSortedSet subSet(final float from, final float to) {
            return AbstractFloat2CharSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public FloatBidirectionalIterator iterator(final float from) {
            return new KeySetIterator((ObjectBidirectionalIterator<Float2CharMap.Entry>)AbstractFloat2CharSortedMap.this.float2CharEntrySet().iterator(new BasicEntry(from, '\0')));
        }
        
        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2CharSortedMaps.fastIterator(AbstractFloat2CharSortedMap.this));
        }
    }
    
    protected static class KeySetIterator implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2CharMap.Entry> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Float2CharMap.Entry> i) {
            this.i = i;
        }
        
        public float nextFloat() {
            return ((Float2CharMap.Entry)this.i.next()).getFloatKey();
        }
        
        public float previousFloat() {
            return this.i.previous().getFloatKey();
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
            return new ValuesIterator(Float2CharSortedMaps.fastIterator(AbstractFloat2CharSortedMap.this));
        }
        
        @Override
        public boolean contains(final char k) {
            return AbstractFloat2CharSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractFloat2CharSortedMap.this.size();
        }
        
        public void clear() {
            AbstractFloat2CharSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator implements CharIterator {
        protected final ObjectBidirectionalIterator<Float2CharMap.Entry> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Float2CharMap.Entry> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Float2CharMap.Entry)this.i.next()).getCharValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
