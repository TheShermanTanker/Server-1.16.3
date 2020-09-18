package io.netty.handler.codec;

import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.List;

public class EmptyHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T> {
    public V get(final K name) {
        return null;
    }
    
    public V get(final K name, final V defaultValue) {
        return defaultValue;
    }
    
    public V getAndRemove(final K name) {
        return null;
    }
    
    public V getAndRemove(final K name, final V defaultValue) {
        return defaultValue;
    }
    
    public List<V> getAll(final K name) {
        return (List<V>)Collections.emptyList();
    }
    
    public List<V> getAllAndRemove(final K name) {
        return (List<V>)Collections.emptyList();
    }
    
    public Boolean getBoolean(final K name) {
        return null;
    }
    
    public boolean getBoolean(final K name, final boolean defaultValue) {
        return defaultValue;
    }
    
    public Byte getByte(final K name) {
        return null;
    }
    
    public byte getByte(final K name, final byte defaultValue) {
        return defaultValue;
    }
    
    public Character getChar(final K name) {
        return null;
    }
    
    public char getChar(final K name, final char defaultValue) {
        return defaultValue;
    }
    
    public Short getShort(final K name) {
        return null;
    }
    
    public short getShort(final K name, final short defaultValue) {
        return defaultValue;
    }
    
    public Integer getInt(final K name) {
        return null;
    }
    
    public int getInt(final K name, final int defaultValue) {
        return defaultValue;
    }
    
    public Long getLong(final K name) {
        return null;
    }
    
    public long getLong(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    public Float getFloat(final K name) {
        return null;
    }
    
    public float getFloat(final K name, final float defaultValue) {
        return defaultValue;
    }
    
    public Double getDouble(final K name) {
        return null;
    }
    
    public double getDouble(final K name, final double defaultValue) {
        return defaultValue;
    }
    
    public Long getTimeMillis(final K name) {
        return null;
    }
    
    public long getTimeMillis(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    public Boolean getBooleanAndRemove(final K name) {
        return null;
    }
    
    public boolean getBooleanAndRemove(final K name, final boolean defaultValue) {
        return defaultValue;
    }
    
    public Byte getByteAndRemove(final K name) {
        return null;
    }
    
    public byte getByteAndRemove(final K name, final byte defaultValue) {
        return defaultValue;
    }
    
    public Character getCharAndRemove(final K name) {
        return null;
    }
    
    public char getCharAndRemove(final K name, final char defaultValue) {
        return defaultValue;
    }
    
    public Short getShortAndRemove(final K name) {
        return null;
    }
    
    public short getShortAndRemove(final K name, final short defaultValue) {
        return defaultValue;
    }
    
    public Integer getIntAndRemove(final K name) {
        return null;
    }
    
    public int getIntAndRemove(final K name, final int defaultValue) {
        return defaultValue;
    }
    
    public Long getLongAndRemove(final K name) {
        return null;
    }
    
    public long getLongAndRemove(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    public Float getFloatAndRemove(final K name) {
        return null;
    }
    
    public float getFloatAndRemove(final K name, final float defaultValue) {
        return defaultValue;
    }
    
    public Double getDoubleAndRemove(final K name) {
        return null;
    }
    
    public double getDoubleAndRemove(final K name, final double defaultValue) {
        return defaultValue;
    }
    
    public Long getTimeMillisAndRemove(final K name) {
        return null;
    }
    
    public long getTimeMillisAndRemove(final K name, final long defaultValue) {
        return defaultValue;
    }
    
    public boolean contains(final K name) {
        return false;
    }
    
    public boolean contains(final K name, final V value) {
        return false;
    }
    
    public boolean containsObject(final K name, final Object value) {
        return false;
    }
    
    public boolean containsBoolean(final K name, final boolean value) {
        return false;
    }
    
    public boolean containsByte(final K name, final byte value) {
        return false;
    }
    
    public boolean containsChar(final K name, final char value) {
        return false;
    }
    
    public boolean containsShort(final K name, final short value) {
        return false;
    }
    
    public boolean containsInt(final K name, final int value) {
        return false;
    }
    
    public boolean containsLong(final K name, final long value) {
        return false;
    }
    
    public boolean containsFloat(final K name, final float value) {
        return false;
    }
    
    public boolean containsDouble(final K name, final double value) {
        return false;
    }
    
    public boolean containsTimeMillis(final K name, final long value) {
        return false;
    }
    
    public int size() {
        return 0;
    }
    
    public boolean isEmpty() {
        return true;
    }
    
    public Set<K> names() {
        return (Set<K>)Collections.emptySet();
    }
    
    public T add(final K name, final V value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T add(final K name, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T add(final K name, final V... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addObject(final K name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addObject(final K name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addObject(final K name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addBoolean(final K name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addByte(final K name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addChar(final K name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addShort(final K name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addInt(final K name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addLong(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addFloat(final K name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addDouble(final K name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T addTimeMillis(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T add(final Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T set(final K name, final V value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T set(final K name, final Iterable<? extends V> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T set(final K name, final V... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setObject(final K name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setObject(final K name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setObject(final K name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setBoolean(final K name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setByte(final K name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setChar(final K name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setShort(final K name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setInt(final K name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setLong(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setFloat(final K name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setDouble(final K name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setTimeMillis(final K name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T set(final Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    public T setAll(final Headers<? extends K, ? extends V, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    public boolean remove(final K name) {
        return false;
    }
    
    public T clear() {
        return this.thisT();
    }
    
    public Iterator<V> valueIterator(final K name) {
        final List<V> empty = (List<V>)Collections.emptyList();
        return (Iterator<V>)empty.iterator();
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
        final List<Map.Entry<K, V>> empty = (List<Map.Entry<K, V>>)Collections.emptyList();
        return (Iterator<Map.Entry<K, V>>)empty.iterator();
    }
    
    public boolean equals(final Object o) {
        if (!(o instanceof Headers)) {
            return false;
        }
        final Headers<?, ?, ?> rhs = o;
        return this.isEmpty() && rhs.isEmpty();
    }
    
    public int hashCode() {
        return -1028477387;
    }
    
    public String toString() {
        return this.getClass().getSimpleName() + '[' + ']';
    }
    
    private T thisT() {
        return (T)this;
    }
}
