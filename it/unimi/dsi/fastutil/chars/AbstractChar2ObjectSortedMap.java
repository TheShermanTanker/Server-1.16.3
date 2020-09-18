package it.unimi.dsi.fastutil.chars;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import java.util.Comparator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.util.Set;
import java.util.Collection;
import it.unimi.dsi.fastutil.objects.ObjectCollection;

public abstract class AbstractChar2ObjectSortedMap<V> extends AbstractChar2ObjectMap<V> implements Char2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;
    
    protected AbstractChar2ObjectSortedMap() {
    }
    
    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }
    
    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }
    
    protected class KeySet extends AbstractCharSortedSet {
        @Override
        public boolean contains(final char k) {
            return AbstractChar2ObjectSortedMap.this.containsKey(k);
        }
        
        public int size() {
            return AbstractChar2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ObjectSortedMap.this.clear();
        }
        
        @Override
        public CharComparator comparator() {
            return AbstractChar2ObjectSortedMap.this.comparator();
        }
        
        @Override
        public char firstChar() {
            return AbstractChar2ObjectSortedMap.this.firstCharKey();
        }
        
        @Override
        public char lastChar() {
            return AbstractChar2ObjectSortedMap.this.lastCharKey();
        }
        
        @Override
        public CharSortedSet headSet(final char to) {
            return AbstractChar2ObjectSortedMap.this.headMap(to).keySet();
        }
        
        @Override
        public CharSortedSet tailSet(final char from) {
            return AbstractChar2ObjectSortedMap.this.tailMap(from).keySet();
        }
        
        @Override
        public CharSortedSet subSet(final char from, final char to) {
            return AbstractChar2ObjectSortedMap.this.subMap(from, to).keySet();
        }
        
        @Override
        public CharBidirectionalIterator iterator(final char from) {
            return new KeySetIterator<>((ObjectBidirectionalIterator<Char2ObjectMap.Entry<?>>)AbstractChar2ObjectSortedMap.this.char2ObjectEntrySet().iterator((Char2ObjectMap.Entry<V>)new BasicEntry<>(from, null)));
        }
        
        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator<>(Char2ObjectSortedMaps.fastIterator((Char2ObjectSortedMap<V>)AbstractChar2ObjectSortedMap.this));
        }
    }
    
    protected static class KeySetIterator<V> implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> i;
        
        public KeySetIterator(final ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public char nextChar() {
            return ((Char2ObjectMap.Entry)this.i.next()).getCharKey();
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
    
    protected class ValuesCollection extends AbstractObjectCollection<V> {
        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator<V>(Char2ObjectSortedMaps.fastIterator((Char2ObjectSortedMap<V>)AbstractChar2ObjectSortedMap.this));
        }
        
        public boolean contains(final Object k) {
            return AbstractChar2ObjectSortedMap.this.containsValue(k);
        }
        
        public int size() {
            return AbstractChar2ObjectSortedMap.this.size();
        }
        
        public void clear() {
            AbstractChar2ObjectSortedMap.this.clear();
        }
    }
    
    protected static class ValuesIterator<V> implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> i;
        
        public ValuesIterator(final ObjectBidirectionalIterator<Char2ObjectMap.Entry<V>> i) {
            this.i = i;
        }
        
        public V next() {
            return (V)((Char2ObjectMap.Entry)this.i.next()).getValue();
        }
        
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }
}
