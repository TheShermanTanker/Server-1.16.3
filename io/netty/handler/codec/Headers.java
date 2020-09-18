package io.netty.handler.codec;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.Map;

public interface Headers<K, V, T extends Headers<K, V, T>> extends Iterable<Map.Entry<K, V>> {
    V get(final K object);
    
    V get(final K object1, final V object2);
    
    V getAndRemove(final K object);
    
    V getAndRemove(final K object1, final V object2);
    
    List<V> getAll(final K object);
    
    List<V> getAllAndRemove(final K object);
    
    Boolean getBoolean(final K object);
    
    boolean getBoolean(final K object, final boolean boolean2);
    
    Byte getByte(final K object);
    
    byte getByte(final K object, final byte byte2);
    
    Character getChar(final K object);
    
    char getChar(final K object, final char character);
    
    Short getShort(final K object);
    
    short getShort(final K object, final short short2);
    
    Integer getInt(final K object);
    
    int getInt(final K object, final int integer);
    
    Long getLong(final K object);
    
    long getLong(final K object, final long long2);
    
    Float getFloat(final K object);
    
    float getFloat(final K object, final float float2);
    
    Double getDouble(final K object);
    
    double getDouble(final K object, final double double2);
    
    Long getTimeMillis(final K object);
    
    long getTimeMillis(final K object, final long long2);
    
    Boolean getBooleanAndRemove(final K object);
    
    boolean getBooleanAndRemove(final K object, final boolean boolean2);
    
    Byte getByteAndRemove(final K object);
    
    byte getByteAndRemove(final K object, final byte byte2);
    
    Character getCharAndRemove(final K object);
    
    char getCharAndRemove(final K object, final char character);
    
    Short getShortAndRemove(final K object);
    
    short getShortAndRemove(final K object, final short short2);
    
    Integer getIntAndRemove(final K object);
    
    int getIntAndRemove(final K object, final int integer);
    
    Long getLongAndRemove(final K object);
    
    long getLongAndRemove(final K object, final long long2);
    
    Float getFloatAndRemove(final K object);
    
    float getFloatAndRemove(final K object, final float float2);
    
    Double getDoubleAndRemove(final K object);
    
    double getDoubleAndRemove(final K object, final double double2);
    
    Long getTimeMillisAndRemove(final K object);
    
    long getTimeMillisAndRemove(final K object, final long long2);
    
    boolean contains(final K object);
    
    boolean contains(final K object1, final V object2);
    
    boolean containsObject(final K object1, final Object object2);
    
    boolean containsBoolean(final K object, final boolean boolean2);
    
    boolean containsByte(final K object, final byte byte2);
    
    boolean containsChar(final K object, final char character);
    
    boolean containsShort(final K object, final short short2);
    
    boolean containsInt(final K object, final int integer);
    
    boolean containsLong(final K object, final long long2);
    
    boolean containsFloat(final K object, final float float2);
    
    boolean containsDouble(final K object, final double double2);
    
    boolean containsTimeMillis(final K object, final long long2);
    
    int size();
    
    boolean isEmpty();
    
    Set<K> names();
    
    T add(final K object1, final V object2);
    
    T add(final K object, final Iterable<? extends V> iterable);
    
    T add(final K object, final V... arr);
    
    T addObject(final K object1, final Object object2);
    
    T addObject(final K object, final Iterable<?> iterable);
    
    T addObject(final K object, final Object... arr);
    
    T addBoolean(final K object, final boolean boolean2);
    
    T addByte(final K object, final byte byte2);
    
    T addChar(final K object, final char character);
    
    T addShort(final K object, final short short2);
    
    T addInt(final K object, final int integer);
    
    T addLong(final K object, final long long2);
    
    T addFloat(final K object, final float float2);
    
    T addDouble(final K object, final double double2);
    
    T addTimeMillis(final K object, final long long2);
    
    T add(final Headers<? extends K, ? extends V, ?> headers);
    
    T set(final K object1, final V object2);
    
    T set(final K object, final Iterable<? extends V> iterable);
    
    T set(final K object, final V... arr);
    
    T setObject(final K object1, final Object object2);
    
    T setObject(final K object, final Iterable<?> iterable);
    
    T setObject(final K object, final Object... arr);
    
    T setBoolean(final K object, final boolean boolean2);
    
    T setByte(final K object, final byte byte2);
    
    T setChar(final K object, final char character);
    
    T setShort(final K object, final short short2);
    
    T setInt(final K object, final int integer);
    
    T setLong(final K object, final long long2);
    
    T setFloat(final K object, final float float2);
    
    T setDouble(final K object, final double double2);
    
    T setTimeMillis(final K object, final long long2);
    
    T set(final Headers<? extends K, ? extends V, ?> headers);
    
    T setAll(final Headers<? extends K, ? extends V, ?> headers);
    
    boolean remove(final K object);
    
    T clear();
    
    Iterator<Map.Entry<K, V>> iterator();
}
