package io.netty.handler.codec;

import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Map;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import io.netty.util.internal.MathUtil;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.HashingStrategy;

public class DefaultHeaders<K, V, T extends Headers<K, V, T>> implements Headers<K, V, T> {
    static final int HASH_CODE_SEED = -1028477387;
    private final HeaderEntry<K, V>[] entries;
    protected final HeaderEntry<K, V> head;
    private final byte hashMask;
    private final ValueConverter<V> valueConverter;
    private final NameValidator<K> nameValidator;
    private final HashingStrategy<K> hashingStrategy;
    int size;
    
    public DefaultHeaders(final ValueConverter<V> valueConverter) {
        this(HashingStrategy.JAVA_HASHER, valueConverter);
    }
    
    public DefaultHeaders(final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator) {
        this(HashingStrategy.JAVA_HASHER, valueConverter, nameValidator);
    }
    
    public DefaultHeaders(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter) {
        this(nameHashingStrategy, valueConverter, NameValidator.NOT_NULL);
    }
    
    public DefaultHeaders(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator) {
        this(nameHashingStrategy, valueConverter, nameValidator, 16);
    }
    
    public DefaultHeaders(final HashingStrategy<K> nameHashingStrategy, final ValueConverter<V> valueConverter, final NameValidator<K> nameValidator, final int arraySizeHint) {
        this.valueConverter = ObjectUtil.<ValueConverter<V>>checkNotNull(valueConverter, "valueConverter");
        this.nameValidator = ObjectUtil.<NameValidator<K>>checkNotNull(nameValidator, "nameValidator");
        this.hashingStrategy = ObjectUtil.<HashingStrategy<K>>checkNotNull(nameHashingStrategy, "nameHashingStrategy");
        this.entries = new HeaderEntry[MathUtil.findNextPositivePowerOfTwo(Math.max(2, Math.min(arraySizeHint, 128)))];
        this.hashMask = (byte)(this.entries.length - 1);
        this.head = new HeaderEntry<K, V>();
    }
    
    public V get(final K name) {
        ObjectUtil.<K>checkNotNull(name, "name");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        HeaderEntry<K, V> e = this.entries[i];
        V value = null;
        while (e != null) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                value = e.value;
            }
            e = e.next;
        }
        return value;
    }
    
    public V get(final K name, final V defaultValue) {
        final V value = this.get(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public V getAndRemove(final K name) {
        final int h = this.hashingStrategy.hashCode(name);
        return this.remove0(h, this.index(h), ObjectUtil.<K>checkNotNull(name, "name"));
    }
    
    public V getAndRemove(final K name, final V defaultValue) {
        final V value = this.getAndRemove(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public List<V> getAll(final K name) {
        ObjectUtil.<K>checkNotNull(name, "name");
        final LinkedList<V> values = (LinkedList<V>)new LinkedList();
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
                values.addFirst(e.getValue());
            }
        }
        return (List<V>)values;
    }
    
    public Iterator<V> valueIterator(final K name) {
        return (Iterator<V>)new ValueIterator(name);
    }
    
    public List<V> getAllAndRemove(final K name) {
        final List<V> all = this.getAll(name);
        this.remove(name);
        return all;
    }
    
    public boolean contains(final K name) {
        return this.get(name) != null;
    }
    
    public boolean containsObject(final K name, final Object value) {
        return this.contains(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
    }
    
    public boolean containsBoolean(final K name, final boolean value) {
        return this.contains(name, this.valueConverter.convertBoolean(value));
    }
    
    public boolean containsByte(final K name, final byte value) {
        return this.contains(name, this.valueConverter.convertByte(value));
    }
    
    public boolean containsChar(final K name, final char value) {
        return this.contains(name, this.valueConverter.convertChar(value));
    }
    
    public boolean containsShort(final K name, final short value) {
        return this.contains(name, this.valueConverter.convertShort(value));
    }
    
    public boolean containsInt(final K name, final int value) {
        return this.contains(name, this.valueConverter.convertInt(value));
    }
    
    public boolean containsLong(final K name, final long value) {
        return this.contains(name, this.valueConverter.convertLong(value));
    }
    
    public boolean containsFloat(final K name, final float value) {
        return this.contains(name, this.valueConverter.convertFloat(value));
    }
    
    public boolean containsDouble(final K name, final double value) {
        return this.contains(name, this.valueConverter.convertDouble(value));
    }
    
    public boolean containsTimeMillis(final K name, final long value) {
        return this.contains(name, this.valueConverter.convertTimeMillis(value));
    }
    
    public boolean contains(final K name, final V value) {
        return this.contains(name, value, HashingStrategy.JAVA_HASHER);
    }
    
    public final boolean contains(final K name, final V value, final HashingStrategy<? super V> valueHashingStrategy) {
        ObjectUtil.<K>checkNotNull(name, "name");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (HeaderEntry<K, V> e = this.entries[i]; e != null; e = e.next) {
            if (e.hash == h && this.hashingStrategy.equals(name, e.key) && valueHashingStrategy.equals(value, e.value)) {
                return true;
            }
        }
        return false;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isEmpty() {
        return this.head == this.head.after;
    }
    
    public Set<K> names() {
        if (this.isEmpty()) {
            return (Set<K>)Collections.emptySet();
        }
        final Set<K> names = (Set<K>)new LinkedHashSet(this.size());
        for (HeaderEntry<K, V> e = this.head.after; e != this.head; e = e.after) {
            names.add(e.getKey());
        }
        return names;
    }
    
    public T add(final K name, final V value) {
        this.nameValidator.validateName(name);
        ObjectUtil.<V>checkNotNull(value, "value");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.add0(h, i, name, value);
        return this.thisT();
    }
    
    public T add(final K name, final Iterable<? extends V> values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (final V v : values) {
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    public T add(final K name, final V... values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        for (final V v : values) {
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    public T addObject(final K name, final Object value) {
        return this.add(name, this.valueConverter.convertObject(ObjectUtil.checkNotNull(value, "value")));
    }
    
    public T addObject(final K name, final Iterable<?> values) {
        for (final Object value : values) {
            this.addObject(name, value);
        }
        return this.thisT();
    }
    
    public T addObject(final K name, final Object... values) {
        for (final Object value : values) {
            this.addObject(name, value);
        }
        return this.thisT();
    }
    
    public T addInt(final K name, final int value) {
        return this.add(name, this.valueConverter.convertInt(value));
    }
    
    public T addLong(final K name, final long value) {
        return this.add(name, this.valueConverter.convertLong(value));
    }
    
    public T addDouble(final K name, final double value) {
        return this.add(name, this.valueConverter.convertDouble(value));
    }
    
    public T addTimeMillis(final K name, final long value) {
        return this.add(name, this.valueConverter.convertTimeMillis(value));
    }
    
    public T addChar(final K name, final char value) {
        return this.add(name, this.valueConverter.convertChar(value));
    }
    
    public T addBoolean(final K name, final boolean value) {
        return this.add(name, this.valueConverter.convertBoolean(value));
    }
    
    public T addFloat(final K name, final float value) {
        return this.add(name, this.valueConverter.convertFloat(value));
    }
    
    public T addByte(final K name, final byte value) {
        return this.add(name, this.valueConverter.convertByte(value));
    }
    
    public T addShort(final K name, final short value) {
        return this.add(name, this.valueConverter.convertShort(value));
    }
    
    public T add(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers == this) {
            throw new IllegalArgumentException("can't add to itself.");
        }
        this.addImpl(headers);
        return this.thisT();
    }
    
    protected void addImpl(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers instanceof DefaultHeaders) {
            final DefaultHeaders<? extends K, ? extends V, T> defaultHeaders = (DefaultHeaders)headers;
            HeaderEntry<? extends K, ? extends V> e = defaultHeaders.head.after;
            if (defaultHeaders.hashingStrategy == this.hashingStrategy && defaultHeaders.nameValidator == this.nameValidator) {
                while (e != defaultHeaders.head) {
                    this.add0(e.hash, this.index(e.hash), e.key, e.value);
                    e = e.after;
                }
            }
            else {
                while (e != defaultHeaders.head) {
                    this.add(e.key, e.value);
                    e = e.after;
                }
            }
        }
        else {
            for (final Map.Entry<? extends K, ? extends V> header : headers) {
                this.add(header.getKey(), header.getValue());
            }
        }
    }
    
    public T set(final K name, final V value) {
        this.nameValidator.validateName(name);
        ObjectUtil.<V>checkNotNull(value, "value");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        this.add0(h, i, name, value);
        return this.thisT();
    }
    
    public T set(final K name, final Iterable<? extends V> values) {
        this.nameValidator.validateName(name);
        ObjectUtil.<Iterable<? extends V>>checkNotNull(values, "values");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final V v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    public T set(final K name, final V... values) {
        this.nameValidator.validateName(name);
        ObjectUtil.<V[]>checkNotNull(values, "values");
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final V v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, v);
        }
        return this.thisT();
    }
    
    public T setObject(final K name, final Object value) {
        ObjectUtil.checkNotNull(value, "value");
        final V convertedValue = ObjectUtil.<V>checkNotNull(this.valueConverter.convertObject(value), "convertedValue");
        return this.set(name, convertedValue);
    }
    
    public T setObject(final K name, final Iterable<?> values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final Object v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, this.valueConverter.convertObject(v));
        }
        return this.thisT();
    }
    
    public T setObject(final K name, final Object... values) {
        this.nameValidator.validateName(name);
        final int h = this.hashingStrategy.hashCode(name);
        final int i = this.index(h);
        this.remove0(h, i, name);
        for (final Object v : values) {
            if (v == null) {
                break;
            }
            this.add0(h, i, name, this.valueConverter.convertObject(v));
        }
        return this.thisT();
    }
    
    public T setInt(final K name, final int value) {
        return this.set(name, this.valueConverter.convertInt(value));
    }
    
    public T setLong(final K name, final long value) {
        return this.set(name, this.valueConverter.convertLong(value));
    }
    
    public T setDouble(final K name, final double value) {
        return this.set(name, this.valueConverter.convertDouble(value));
    }
    
    public T setTimeMillis(final K name, final long value) {
        return this.set(name, this.valueConverter.convertTimeMillis(value));
    }
    
    public T setFloat(final K name, final float value) {
        return this.set(name, this.valueConverter.convertFloat(value));
    }
    
    public T setChar(final K name, final char value) {
        return this.set(name, this.valueConverter.convertChar(value));
    }
    
    public T setBoolean(final K name, final boolean value) {
        return this.set(name, this.valueConverter.convertBoolean(value));
    }
    
    public T setByte(final K name, final byte value) {
        return this.set(name, this.valueConverter.convertByte(value));
    }
    
    public T setShort(final K name, final short value) {
        return this.set(name, this.valueConverter.convertShort(value));
    }
    
    public T set(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            this.clear();
            this.addImpl(headers);
        }
        return this.thisT();
    }
    
    public T setAll(final Headers<? extends K, ? extends V, ?> headers) {
        if (headers != this) {
            for (final K key : headers.names()) {
                this.remove(key);
            }
            this.addImpl(headers);
        }
        return this.thisT();
    }
    
    public boolean remove(final K name) {
        return this.getAndRemove(name) != null;
    }
    
    public T clear() {
        Arrays.fill((Object[])this.entries, null);
        final HeaderEntry<K, V> head = this.head;
        final HeaderEntry<K, V> head2 = this.head;
        final HeaderEntry<K, V> head3 = this.head;
        head2.after = head3;
        head.before = head3;
        this.size = 0;
        return this.thisT();
    }
    
    public Iterator<Map.Entry<K, V>> iterator() {
        return (Iterator<Map.Entry<K, V>>)new HeaderIterator();
    }
    
    public Boolean getBoolean(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Boolean.valueOf(this.valueConverter.convertToBoolean(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public boolean getBoolean(final K name, final boolean defaultValue) {
        final Boolean v = this.getBoolean(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Byte getByte(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Byte.valueOf(this.valueConverter.convertToByte(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public byte getByte(final K name, final byte defaultValue) {
        final Byte v = this.getByte(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Character getChar(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Character.valueOf(this.valueConverter.convertToChar(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public char getChar(final K name, final char defaultValue) {
        final Character v = this.getChar(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Short getShort(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Short.valueOf(this.valueConverter.convertToShort(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public short getShort(final K name, final short defaultValue) {
        final Short v = this.getShort(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Integer getInt(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Integer.valueOf(this.valueConverter.convertToInt(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public int getInt(final K name, final int defaultValue) {
        final Integer v = this.getInt(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Long getLong(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Long.valueOf(this.valueConverter.convertToLong(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public long getLong(final K name, final long defaultValue) {
        final Long v = this.getLong(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Float getFloat(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Float.valueOf(this.valueConverter.convertToFloat(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public float getFloat(final K name, final float defaultValue) {
        final Float v = this.getFloat(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Double getDouble(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Double.valueOf(this.valueConverter.convertToDouble(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public double getDouble(final K name, final double defaultValue) {
        final Double v = this.getDouble(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Long getTimeMillis(final K name) {
        final V v = this.get(name);
        try {
            return (v != null) ? Long.valueOf(this.valueConverter.convertToTimeMillis(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public long getTimeMillis(final K name, final long defaultValue) {
        final Long v = this.getTimeMillis(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Boolean getBooleanAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Boolean.valueOf(this.valueConverter.convertToBoolean(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public boolean getBooleanAndRemove(final K name, final boolean defaultValue) {
        final Boolean v = this.getBooleanAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Byte getByteAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Byte.valueOf(this.valueConverter.convertToByte(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public byte getByteAndRemove(final K name, final byte defaultValue) {
        final Byte v = this.getByteAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Character getCharAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Character.valueOf(this.valueConverter.convertToChar(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public char getCharAndRemove(final K name, final char defaultValue) {
        final Character v = this.getCharAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Short getShortAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Short.valueOf(this.valueConverter.convertToShort(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public short getShortAndRemove(final K name, final short defaultValue) {
        final Short v = this.getShortAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Integer getIntAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Integer.valueOf(this.valueConverter.convertToInt(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public int getIntAndRemove(final K name, final int defaultValue) {
        final Integer v = this.getIntAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Long getLongAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Long.valueOf(this.valueConverter.convertToLong(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public long getLongAndRemove(final K name, final long defaultValue) {
        final Long v = this.getLongAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Float getFloatAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Float.valueOf(this.valueConverter.convertToFloat(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public float getFloatAndRemove(final K name, final float defaultValue) {
        final Float v = this.getFloatAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Double getDoubleAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Double.valueOf(this.valueConverter.convertToDouble(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public double getDoubleAndRemove(final K name, final double defaultValue) {
        final Double v = this.getDoubleAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public Long getTimeMillisAndRemove(final K name) {
        final V v = this.getAndRemove(name);
        try {
            return (v != null) ? Long.valueOf(this.valueConverter.convertToTimeMillis(v)) : null;
        }
        catch (RuntimeException ignore) {
            return null;
        }
    }
    
    public long getTimeMillisAndRemove(final K name, final long defaultValue) {
        final Long v = this.getTimeMillisAndRemove(name);
        return (v != null) ? v : defaultValue;
    }
    
    public boolean equals(final Object o) {
        return o instanceof Headers && this.equals(o, HashingStrategy.JAVA_HASHER);
    }
    
    public int hashCode() {
        return this.hashCode(HashingStrategy.JAVA_HASHER);
    }
    
    public final boolean equals(final Headers<K, V, ?> h2, final HashingStrategy<V> valueHashingStrategy) {
        if (h2.size() != this.size()) {
            return false;
        }
        if (this == h2) {
            return true;
        }
        for (final K name : this.names()) {
            final List<V> otherValues = h2.getAll(name);
            final List<V> values = this.getAll(name);
            if (otherValues.size() != values.size()) {
                return false;
            }
            for (int i = 0; i < otherValues.size(); ++i) {
                if (!valueHashingStrategy.equals((V)otherValues.get(i), (V)values.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public final int hashCode(final HashingStrategy<V> valueHashingStrategy) {
        int result = -1028477387;
        for (final K name : this.names()) {
            result = 31 * result + this.hashingStrategy.hashCode(name);
            final List<V> values = this.getAll(name);
            for (int i = 0; i < values.size(); ++i) {
                result = 31 * result + valueHashingStrategy.hashCode((V)values.get(i));
            }
        }
        return result;
    }
    
    public String toString() {
        return HeadersUtils.toString(this.getClass(), this.iterator(), this.size());
    }
    
    protected HeaderEntry<K, V> newHeaderEntry(final int h, final K name, final V value, final HeaderEntry<K, V> next) {
        return new HeaderEntry<K, V>(h, name, value, next, this.head);
    }
    
    protected ValueConverter<V> valueConverter() {
        return this.valueConverter;
    }
    
    private int index(final int hash) {
        return hash & this.hashMask;
    }
    
    private void add0(final int h, final int i, final K name, final V value) {
        this.entries[i] = this.newHeaderEntry(h, name, value, this.entries[i]);
        ++this.size;
    }
    
    private V remove0(final int h, final int i, final K name) {
        HeaderEntry<K, V> e = this.entries[i];
        if (e == null) {
            return null;
        }
        V value = null;
        for (HeaderEntry<K, V> next = e.next; next != null; next = e.next) {
            if (next.hash == h && this.hashingStrategy.equals(name, next.key)) {
                value = next.value;
                e.next = next.next;
                next.remove();
                --this.size;
            }
            else {
                e = next;
            }
        }
        e = this.entries[i];
        if (e.hash == h && this.hashingStrategy.equals(name, e.key)) {
            if (value == null) {
                value = e.value;
            }
            this.entries[i] = e.next;
            e.remove();
            --this.size;
        }
        return value;
    }
    
    private T thisT() {
        return (T)this;
    }
    
    public DefaultHeaders<K, V, T> copy() {
        final DefaultHeaders<K, V, T> copy = new DefaultHeaders<K, V, T>(this.hashingStrategy, this.valueConverter, this.nameValidator, this.entries.length);
        copy.addImpl(this);
        return copy;
    }
    
    public interface NameValidator<K> {
        public static final NameValidator NOT_NULL;
        
        void validateName(final K object);
        
        default static {
            NOT_NULL = new NameValidator() {
                public void validateName(final Object name) {
                    ObjectUtil.checkNotNull(name, "name");
                }
            };
        }
    }
    
    public interface NameValidator<K> {
        public static final NameValidator NOT_NULL = new NameValidator() {
            public void validateName(final Object name) {
                ObjectUtil.checkNotNull(name, "name");
            }
        };
        
        void validateName(final K object);
    }
    
    private final class HeaderIterator implements Iterator<Map.Entry<K, V>> {
        private HeaderEntry<K, V> current;
        
        private HeaderIterator() {
            this.current = DefaultHeaders.this.head;
        }
        
        public boolean hasNext() {
            return this.current.after != DefaultHeaders.this.head;
        }
        
        public Map.Entry<K, V> next() {
            this.current = this.current.after;
            if (this.current == DefaultHeaders.this.head) {
                throw new NoSuchElementException();
            }
            return (Map.Entry<K, V>)this.current;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
    }
    
    private final class ValueIterator implements Iterator<V> {
        private final K name;
        private final int hash;
        private HeaderEntry<K, V> next;
        
        ValueIterator(final K name) {
            this.name = ObjectUtil.<K>checkNotNull(name, "name");
            this.hash = DefaultHeaders.this.hashingStrategy.hashCode(name);
            this.calculateNext(DefaultHeaders.this.entries[DefaultHeaders.this.index(this.hash)]);
        }
        
        public boolean hasNext() {
            return this.next != null;
        }
        
        public V next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final HeaderEntry<K, V> current = this.next;
            this.calculateNext(this.next.next);
            return current.value;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
        
        private void calculateNext(HeaderEntry<K, V> entry) {
            while (entry != null) {
                if (entry.hash == this.hash && DefaultHeaders.this.hashingStrategy.equals(this.name, entry.key)) {
                    this.next = entry;
                    return;
                }
                entry = entry.next;
            }
            this.next = null;
        }
    }
    
    protected static class HeaderEntry<K, V> implements Map.Entry<K, V> {
        protected final int hash;
        protected final K key;
        protected V value;
        protected HeaderEntry<K, V> next;
        protected HeaderEntry<K, V> before;
        protected HeaderEntry<K, V> after;
        
        protected HeaderEntry(final int hash, final K key) {
            this.hash = hash;
            this.key = key;
        }
        
        HeaderEntry(final int hash, final K key, final V value, final HeaderEntry<K, V> next, final HeaderEntry<K, V> head) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
            this.after = head;
            this.before = head.before;
            this.pointNeighborsToThis();
        }
        
        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.after = this;
            this.before = this;
        }
        
        protected final void pointNeighborsToThis() {
            this.before.after = this;
            this.after.before = this;
        }
        
        public final HeaderEntry<K, V> before() {
            return this.before;
        }
        
        public final HeaderEntry<K, V> after() {
            return this.after;
        }
        
        protected void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }
        
        public final K getKey() {
            return this.key;
        }
        
        public final V getValue() {
            return this.value;
        }
        
        public final V setValue(final V value) {
            ObjectUtil.<V>checkNotNull(value, "value");
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public final String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> other = o;
            if (this.getKey() == null) {
                if (other.getKey() != null) {
                    return false;
                }
            }
            else if (!this.getKey().equals(other.getKey())) {
                return false;
            }
            if ((this.getValue() != null) ? this.getValue().equals(other.getValue()) : (other.getValue() == null)) {
                return true;
            }
            return false;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
    }
    
    protected static class HeaderEntry<K, V> implements Map.Entry<K, V> {
        protected final int hash;
        protected final K key;
        protected V value;
        protected HeaderEntry<K, V> next;
        protected HeaderEntry<K, V> before;
        protected HeaderEntry<K, V> after;
        
        protected HeaderEntry(final int hash, final K key) {
            this.hash = hash;
            this.key = key;
        }
        
        HeaderEntry(final int hash, final K key, final V value, final HeaderEntry<K, V> next, final HeaderEntry<K, V> head) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
            this.after = head;
            this.before = head.before;
            this.pointNeighborsToThis();
        }
        
        HeaderEntry() {
            this.hash = -1;
            this.key = null;
            this.after = this;
            this.before = this;
        }
        
        protected final void pointNeighborsToThis() {
            this.before.after = this;
            this.after.before = this;
        }
        
        public final HeaderEntry<K, V> before() {
            return this.before;
        }
        
        public final HeaderEntry<K, V> after() {
            return this.after;
        }
        
        protected void remove() {
            this.before.after = this.after;
            this.after.before = this.before;
        }
        
        public final K getKey() {
            return this.key;
        }
        
        public final V getValue() {
            return this.value;
        }
        
        public final V setValue(final V value) {
            ObjectUtil.<V>checkNotNull(value, "value");
            final V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
        
        public final String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
        
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> other = o;
            if (this.getKey() == null) {
                if (other.getKey() != null) {
                    return false;
                }
            }
            else if (!this.getKey().equals(other.getKey())) {
                return false;
            }
            if ((this.getValue() != null) ? this.getValue().equals(other.getValue()) : (other.getValue() == null)) {
                return true;
            }
            return false;
        }
        
        public int hashCode() {
            return ((this.key == null) ? 0 : this.key.hashCode()) ^ ((this.value == null) ? 0 : this.value.hashCode());
        }
    }
}
