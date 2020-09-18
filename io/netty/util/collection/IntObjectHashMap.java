package io.netty.util.collection;

import java.util.NoSuchElementException;
import java.util.AbstractSet;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Arrays;
import io.netty.util.internal.MathUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class IntObjectHashMap<V> implements IntObjectMap<V> {
    public static final int DEFAULT_CAPACITY = 8;
    public static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final Object NULL_VALUE;
    private int maxSize;
    private final float loadFactor;
    private int[] keys;
    private V[] values;
    private int size;
    private int mask;
    private final Set<Integer> keySet;
    private final Set<Map.Entry<Integer, V>> entrySet;
    private final Iterable<PrimitiveEntry<V>> entries;
    
    public IntObjectHashMap() {
        this(8, 0.5f);
    }
    
    public IntObjectHashMap(final int initialCapacity) {
        this(initialCapacity, 0.5f);
    }
    
    public IntObjectHashMap(final int initialCapacity, final float loadFactor) {
        this.keySet = (Set<Integer>)new KeySet();
        this.entrySet = (Set<Map.Entry<Integer, V>>)new EntrySet();
        this.entries = (Iterable<PrimitiveEntry<V>>)new Iterable<PrimitiveEntry<V>>() {
            public Iterator<PrimitiveEntry<V>> iterator() {
                return (Iterator<PrimitiveEntry<V>>)new PrimitiveIterator();
            }
        };
        if (loadFactor <= 0.0f || loadFactor > 1.0f) {
            throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
        }
        this.loadFactor = loadFactor;
        final int capacity = MathUtil.safeFindNextPositivePowerOfTwo(initialCapacity);
        this.mask = capacity - 1;
        this.keys = new int[capacity];
        final V[] temp = (V[])new Object[capacity];
        this.values = temp;
        this.maxSize = this.calcMaxSize(capacity);
    }
    
    private static <T> T toExternal(final T value) {
        assert value != null : "null is not a legitimate internal value. Concurrent Modification?";
        return (value == IntObjectHashMap.NULL_VALUE) ? null : value;
    }
    
    private static <T> T toInternal(final T value) {
        return (T)((value == null) ? IntObjectHashMap.NULL_VALUE : value);
    }
    
    public V get(final int key) {
        final int index = this.indexOf(key);
        return (index == -1) ? null : IntObjectHashMap.<V>toExternal(this.values[index]);
    }
    
    public V put(final int key, final V value) {
        int index;
        final int startIndex = index = this.hashIndex(key);
        while (this.values[index] != null) {
            if (this.keys[index] == key) {
                final V previousValue = this.values[index];
                this.values[index] = IntObjectHashMap.<V>toInternal(value);
                return IntObjectHashMap.<V>toExternal(previousValue);
            }
            if ((index = this.probeNext(index)) == startIndex) {
                throw new IllegalStateException("Unable to insert");
            }
        }
        this.keys[index] = key;
        this.values[index] = IntObjectHashMap.<V>toInternal(value);
        this.growSize();
        return null;
    }
    
    public void putAll(final Map<? extends Integer, ? extends V> sourceMap) {
        if (sourceMap instanceof IntObjectHashMap) {
            final IntObjectHashMap<V> source = (IntObjectHashMap)sourceMap;
            for (int i = 0; i < source.values.length; ++i) {
                final V sourceValue = source.values[i];
                if (sourceValue != null) {
                    this.put(source.keys[i], sourceValue);
                }
            }
            return;
        }
        for (final Map.Entry<? extends Integer, ? extends V> entry : sourceMap.entrySet()) {
            this.put((Integer)entry.getKey(), entry.getValue());
        }
    }
    
    public V remove(final int key) {
        final int index = this.indexOf(key);
        if (index == -1) {
            return null;
        }
        final V prev = this.values[index];
        this.removeAt(index);
        return IntObjectHashMap.<V>toExternal(prev);
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    public void clear() {
        Arrays.fill(this.keys, 0);
        Arrays.fill((Object[])this.values, null);
        this.size = 0;
    }
    
    public boolean containsKey(final int key) {
        return this.indexOf(key) >= 0;
    }
    
    public boolean containsValue(final Object value) {
        final V v1 = IntObjectHashMap.<V>toInternal(value);
        for (final V v2 : this.values) {
            if (v2 != null && v2.equals(v1)) {
                return true;
            }
        }
        return false;
    }
    
    public Iterable<PrimitiveEntry<V>> entries() {
        return this.entries;
    }
    
    public Collection<V> values() {
        return (Collection<V>)new AbstractCollection<V>() {
            public Iterator<V> iterator() {
                return (Iterator<V>)new Iterator<V>() {
                    final PrimitiveIterator iter = new PrimitiveIterator();
                    
                    public boolean hasNext() {
                        return this.iter.hasNext();
                    }
                    
                    public V next() {
                        return this.iter.next().value();
                    }
                    
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
            
            public int size() {
                return IntObjectHashMap.this.size;
            }
        };
    }
    
    public int hashCode() {
        int hash = this.size;
        for (final int key : this.keys) {
            hash ^= hashCode(key);
        }
        return hash;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IntObjectMap)) {
            return false;
        }
        final IntObjectMap other = (IntObjectMap)obj;
        if (this.size != other.size()) {
            return false;
        }
        for (int i = 0; i < this.values.length; ++i) {
            final V value = this.values[i];
            if (value != null) {
                final int key = this.keys[i];
                final Object otherValue = other.get(key);
                if (value == IntObjectHashMap.NULL_VALUE) {
                    if (otherValue != null) {
                        return false;
                    }
                }
                else if (!value.equals(otherValue)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean containsKey(final Object key) {
        return this.containsKey(this.objectToKey(key));
    }
    
    public V get(final Object key) {
        return this.get(this.objectToKey(key));
    }
    
    public V put(final Integer key, final V value) {
        return this.put(this.objectToKey(key), value);
    }
    
    public V remove(final Object key) {
        return this.remove(this.objectToKey(key));
    }
    
    public Set<Integer> keySet() {
        return this.keySet;
    }
    
    public Set<Map.Entry<Integer, V>> entrySet() {
        return this.entrySet;
    }
    
    private int objectToKey(final Object key) {
        return (int)key;
    }
    
    private int indexOf(final int key) {
        int index;
        final int startIndex = index = this.hashIndex(key);
        while (this.values[index] != null) {
            if (key == this.keys[index]) {
                return index;
            }
            if ((index = this.probeNext(index)) == startIndex) {
                return -1;
            }
        }
        return -1;
    }
    
    private int hashIndex(final int key) {
        return hashCode(key) & this.mask;
    }
    
    private static int hashCode(final int key) {
        return key;
    }
    
    private int probeNext(final int index) {
        return index + 1 & this.mask;
    }
    
    private void growSize() {
        ++this.size;
        if (this.size > this.maxSize) {
            if (this.keys.length == Integer.MAX_VALUE) {
                throw new IllegalStateException(new StringBuilder().append("Max capacity reached at size=").append(this.size).toString());
            }
            this.rehash(this.keys.length << 1);
        }
    }
    
    private boolean removeAt(final int index) {
        --this.size;
        this.keys[index] = 0;
        this.values[index] = null;
        int nextFree = index;
        int i = this.probeNext(index);
        for (V value = this.values[i]; value != null; value = this.values[i = this.probeNext(i)]) {
            final int key = this.keys[i];
            final int bucket = this.hashIndex(key);
            if ((i < bucket && (bucket <= nextFree || nextFree <= i)) || (bucket <= nextFree && nextFree <= i)) {
                this.keys[nextFree] = key;
                this.values[nextFree] = value;
                this.keys[i] = 0;
                this.values[i] = null;
                nextFree = i;
            }
        }
        return nextFree != index;
    }
    
    private int calcMaxSize(final int capacity) {
        final int upperBound = capacity - 1;
        return Math.min(upperBound, (int)(capacity * this.loadFactor));
    }
    
    private void rehash(final int newCapacity) {
        final int[] oldKeys = this.keys;
        final V[] oldVals = this.values;
        this.keys = new int[newCapacity];
        final V[] temp = (V[])new Object[newCapacity];
        this.values = temp;
        this.maxSize = this.calcMaxSize(newCapacity);
        this.mask = newCapacity - 1;
        for (int i = 0; i < oldVals.length; ++i) {
            final V oldVal = oldVals[i];
            if (oldVal != null) {
                final int oldKey = oldKeys[i];
                int index;
                for (index = this.hashIndex(oldKey); this.values[index] != null; index = this.probeNext(index)) {}
                this.keys[index] = oldKey;
                this.values[index] = oldVal;
            }
        }
    }
    
    public String toString() {
        if (this.isEmpty()) {
            return "{}";
        }
        final StringBuilder sb = new StringBuilder(4 * this.size);
        sb.append('{');
        boolean first = true;
        for (int i = 0; i < this.values.length; ++i) {
            final V value = this.values[i];
            if (value != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(this.keyToString(this.keys[i])).append('=').append((value == this) ? "(this Map)" : IntObjectHashMap.<V>toExternal(value));
                first = false;
            }
        }
        return sb.append('}').toString();
    }
    
    protected String keyToString(final int key) {
        return Integer.toString(key);
    }
    
    static {
        NULL_VALUE = new Object();
    }
    
    private final class EntrySet extends AbstractSet<Map.Entry<Integer, V>> {
        public Iterator<Map.Entry<Integer, V>> iterator() {
            return (Iterator<Map.Entry<Integer, V>>)new MapIterator();
        }
        
        public int size() {
            return IntObjectHashMap.this.size();
        }
    }
    
    private final class KeySet extends AbstractSet<Integer> {
        public int size() {
            return IntObjectHashMap.this.size();
        }
        
        public boolean contains(final Object o) {
            return IntObjectHashMap.this.containsKey(o);
        }
        
        public boolean remove(final Object o) {
            return IntObjectHashMap.this.remove(o) != null;
        }
        
        public boolean retainAll(final Collection<?> retainedKeys) {
            boolean changed = false;
            final Iterator<PrimitiveEntry<V>> iter = (Iterator<PrimitiveEntry<V>>)IntObjectHashMap.this.entries().iterator();
            while (iter.hasNext()) {
                final PrimitiveEntry<V> entry = (PrimitiveEntry<V>)iter.next();
                if (!retainedKeys.contains(entry.key())) {
                    changed = true;
                    iter.remove();
                }
            }
            return changed;
        }
        
        public void clear() {
            IntObjectHashMap.this.clear();
        }
        
        public Iterator<Integer> iterator() {
            return (Iterator<Integer>)new Iterator<Integer>() {
                private final Iterator<Map.Entry<Integer, V>> iter = IntObjectHashMap.this.entrySet.iterator();
                
                public boolean hasNext() {
                    return this.iter.hasNext();
                }
                
                public Integer next() {
                    return (Integer)((Map.Entry)this.iter.next()).getKey();
                }
                
                public void remove() {
                    this.iter.remove();
                }
            };
        }
    }
    
    private final class PrimitiveIterator implements Iterator<PrimitiveEntry<V>>, PrimitiveEntry<V> {
        private int prevIndex;
        private int nextIndex;
        private int entryIndex;
        
        private PrimitiveIterator() {
            this.prevIndex = -1;
            this.nextIndex = -1;
            this.entryIndex = -1;
        }
        
        private void scanNext() {
            while (++this.nextIndex != IntObjectHashMap.this.values.length && IntObjectHashMap.this.values[this.nextIndex] == null) {}
        }
        
        public boolean hasNext() {
            if (this.nextIndex == -1) {
                this.scanNext();
            }
            return this.nextIndex != IntObjectHashMap.this.values.length;
        }
        
        public PrimitiveEntry<V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.prevIndex = this.nextIndex;
            this.scanNext();
            this.entryIndex = this.prevIndex;
            return this;
        }
        
        public void remove() {
            if (this.prevIndex == -1) {
                throw new IllegalStateException("next must be called before each remove.");
            }
            if (IntObjectHashMap.this.removeAt(this.prevIndex)) {
                this.nextIndex = this.prevIndex;
            }
            this.prevIndex = -1;
        }
        
        public int key() {
            return IntObjectHashMap.this.keys[this.entryIndex];
        }
        
        public V value() {
            return (V)IntObjectHashMap.toExternal(IntObjectHashMap.this.values[this.entryIndex]);
        }
        
        public void setValue(final V value) {
            IntObjectHashMap.this.values[this.entryIndex] = (V)IntObjectHashMap.toInternal(value);
        }
    }
    
    private final class MapIterator implements Iterator<Map.Entry<Integer, V>> {
        private final PrimitiveIterator iter;
        
        private MapIterator() {
            this.iter = new PrimitiveIterator();
        }
        
        public boolean hasNext() {
            return this.iter.hasNext();
        }
        
        public Map.Entry<Integer, V> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.iter.next();
            return (Map.Entry<Integer, V>)new MapEntry(this.iter.entryIndex);
        }
        
        public void remove() {
            this.iter.remove();
        }
    }
    
    final class MapEntry implements Map.Entry<Integer, V> {
        private final int entryIndex;
        
        MapEntry(final int entryIndex) {
            this.entryIndex = entryIndex;
        }
        
        public Integer getKey() {
            this.verifyExists();
            return IntObjectHashMap.this.keys[this.entryIndex];
        }
        
        public V getValue() {
            this.verifyExists();
            return (V)IntObjectHashMap.toExternal(IntObjectHashMap.this.values[this.entryIndex]);
        }
        
        public V setValue(final V value) {
            this.verifyExists();
            final V prevValue = (V)IntObjectHashMap.toExternal(IntObjectHashMap.this.values[this.entryIndex]);
            IntObjectHashMap.this.values[this.entryIndex] = (V)IntObjectHashMap.toInternal(value);
            return prevValue;
        }
        
        private void verifyExists() {
            if (IntObjectHashMap.this.values[this.entryIndex] == null) {
                throw new IllegalStateException("The map entry has been removed");
            }
        }
    }
}