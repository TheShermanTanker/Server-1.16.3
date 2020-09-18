package io.netty.handler.codec.http2;

import java.util.NoSuchElementException;
import io.netty.util.HashingStrategy;
import java.util.Map;
import java.util.Iterator;
import io.netty.handler.codec.Headers;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Set;
import io.netty.handler.codec.CharSequenceValueConverter;
import java.util.ArrayList;
import java.util.List;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.AsciiString;

public final class ReadOnlyHttp2Headers implements Http2Headers {
    private static final byte PSEUDO_HEADER_TOKEN = 58;
    private final AsciiString[] pseudoHeaders;
    private final AsciiString[] otherHeaders;
    
    public static ReadOnlyHttp2Headers trailers(final boolean validateHeaders, final AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, EmptyArrays.EMPTY_ASCII_STRINGS, otherHeaders);
    }
    
    public static ReadOnlyHttp2Headers clientHeaders(final boolean validateHeaders, final AsciiString method, final AsciiString path, final AsciiString scheme, final AsciiString authority, final AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[] { PseudoHeaderName.METHOD.value(), method, PseudoHeaderName.PATH.value(), path, PseudoHeaderName.SCHEME.value(), scheme, PseudoHeaderName.AUTHORITY.value(), authority }, otherHeaders);
    }
    
    public static ReadOnlyHttp2Headers serverHeaders(final boolean validateHeaders, final AsciiString status, final AsciiString... otherHeaders) {
        return new ReadOnlyHttp2Headers(validateHeaders, new AsciiString[] { PseudoHeaderName.STATUS.value(), status }, otherHeaders);
    }
    
    private ReadOnlyHttp2Headers(final boolean validateHeaders, final AsciiString[] pseudoHeaders, final AsciiString... otherHeaders) {
        assert (pseudoHeaders.length & 0x1) == 0x0;
        if ((otherHeaders.length & 0x1) != 0x0) {
            throw newInvalidArraySizeException();
        }
        if (validateHeaders) {
            validateHeaders(pseudoHeaders, otherHeaders);
        }
        this.pseudoHeaders = pseudoHeaders;
        this.otherHeaders = otherHeaders;
    }
    
    private static IllegalArgumentException newInvalidArraySizeException() {
        return new IllegalArgumentException("pseudoHeaders and otherHeaders must be arrays of [name, value] pairs");
    }
    
    private static void validateHeaders(final AsciiString[] pseudoHeaders, final AsciiString... otherHeaders) {
        for (int i = 1; i < pseudoHeaders.length; i += 2) {
            if (pseudoHeaders[i] == null) {
                throw new IllegalArgumentException(new StringBuilder().append("pseudoHeaders value at index ").append(i).append(" is null").toString());
            }
        }
        boolean seenNonPseudoHeader = false;
        for (int otherHeadersEnd = otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString name = otherHeaders[j];
            DefaultHttp2Headers.HTTP2_NAME_VALIDATOR.validateName((CharSequence)name);
            if (!seenNonPseudoHeader && !name.isEmpty() && name.byteAt(0) != 58) {
                seenNonPseudoHeader = true;
            }
            else if (seenNonPseudoHeader && !name.isEmpty() && name.byteAt(0) == 58) {
                throw new IllegalArgumentException(new StringBuilder().append("otherHeaders name at index ").append(j).append(" is a pseudo header that appears after non-pseudo headers.").toString());
            }
            if (otherHeaders[j + 1] == null) {
                throw new IllegalArgumentException(new StringBuilder().append("otherHeaders value at index ").append(j + 1).append(" is null").toString());
            }
        }
    }
    
    private AsciiString get0(final CharSequence name) {
        final int nameHash = AsciiString.hashCode(name);
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            final AsciiString roName = this.pseudoHeaders[i];
            if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
                return this.pseudoHeaders[i + 1];
            }
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString roName2 = this.otherHeaders[j];
            if (roName2.hashCode() == nameHash && roName2.contentEqualsIgnoreCase(name)) {
                return this.otherHeaders[j + 1];
            }
        }
        return null;
    }
    
    public CharSequence get(final CharSequence name) {
        return (CharSequence)this.get0(name);
    }
    
    public CharSequence get(final CharSequence name, final CharSequence defaultValue) {
        final CharSequence value = this.get(name);
        return (value != null) ? value : defaultValue;
    }
    
    public CharSequence getAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public CharSequence getAndRemove(final CharSequence name, final CharSequence defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public List<CharSequence> getAll(final CharSequence name) {
        final int nameHash = AsciiString.hashCode(name);
        final List<CharSequence> values = (List<CharSequence>)new ArrayList();
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            final AsciiString roName = this.pseudoHeaders[i];
            if (roName.hashCode() == nameHash && roName.contentEqualsIgnoreCase(name)) {
                values.add(this.pseudoHeaders[i + 1]);
            }
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            final AsciiString roName2 = this.otherHeaders[j];
            if (roName2.hashCode() == nameHash && roName2.contentEqualsIgnoreCase(name)) {
                values.add(this.otherHeaders[j + 1]);
            }
        }
        return values;
    }
    
    public List<CharSequence> getAllAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Boolean getBoolean(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Boolean.valueOf(CharSequenceValueConverter.INSTANCE.convertToBoolean((CharSequence)value)) : null;
    }
    
    public boolean getBoolean(final CharSequence name, final boolean defaultValue) {
        final Boolean value = this.getBoolean(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Byte getByte(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Byte.valueOf(CharSequenceValueConverter.INSTANCE.convertToByte((CharSequence)value)) : null;
    }
    
    public byte getByte(final CharSequence name, final byte defaultValue) {
        final Byte value = this.getByte(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Character getChar(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Character.valueOf(CharSequenceValueConverter.INSTANCE.convertToChar((CharSequence)value)) : null;
    }
    
    public char getChar(final CharSequence name, final char defaultValue) {
        final Character value = this.getChar(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Short getShort(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Short.valueOf(CharSequenceValueConverter.INSTANCE.convertToShort((CharSequence)value)) : null;
    }
    
    public short getShort(final CharSequence name, final short defaultValue) {
        final Short value = this.getShort(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Integer getInt(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Integer.valueOf(CharSequenceValueConverter.INSTANCE.convertToInt((CharSequence)value)) : null;
    }
    
    public int getInt(final CharSequence name, final int defaultValue) {
        final Integer value = this.getInt(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Long getLong(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToLong((CharSequence)value)) : null;
    }
    
    public long getLong(final CharSequence name, final long defaultValue) {
        final Long value = this.getLong(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Float getFloat(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Float.valueOf(CharSequenceValueConverter.INSTANCE.convertToFloat((CharSequence)value)) : null;
    }
    
    public float getFloat(final CharSequence name, final float defaultValue) {
        final Float value = this.getFloat(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Double getDouble(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Double.valueOf(CharSequenceValueConverter.INSTANCE.convertToDouble((CharSequence)value)) : null;
    }
    
    public double getDouble(final CharSequence name, final double defaultValue) {
        final Double value = this.getDouble(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Long getTimeMillis(final CharSequence name) {
        final AsciiString value = this.get0(name);
        return (value != null) ? Long.valueOf(CharSequenceValueConverter.INSTANCE.convertToTimeMillis((CharSequence)value)) : null;
    }
    
    public long getTimeMillis(final CharSequence name, final long defaultValue) {
        final Long value = this.getTimeMillis(name);
        return (value != null) ? value : defaultValue;
    }
    
    public Boolean getBooleanAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public boolean getBooleanAndRemove(final CharSequence name, final boolean defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Byte getByteAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public byte getByteAndRemove(final CharSequence name, final byte defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Character getCharAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public char getCharAndRemove(final CharSequence name, final char defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Short getShortAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public short getShortAndRemove(final CharSequence name, final short defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Integer getIntAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public int getIntAndRemove(final CharSequence name, final int defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Long getLongAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public long getLongAndRemove(final CharSequence name, final long defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Float getFloatAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public float getFloatAndRemove(final CharSequence name, final float defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Double getDoubleAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public double getDoubleAndRemove(final CharSequence name, final double defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Long getTimeMillisAndRemove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public long getTimeMillisAndRemove(final CharSequence name, final long defaultValue) {
        throw new UnsupportedOperationException("read only");
    }
    
    public boolean contains(final CharSequence name) {
        return this.get(name) != null;
    }
    
    public boolean contains(final CharSequence name, final CharSequence value) {
        return this.contains(name, value, false);
    }
    
    public boolean containsObject(final CharSequence name, final Object value) {
        if (value instanceof CharSequence) {
            return this.contains(name, (CharSequence)value);
        }
        return this.contains(name, (CharSequence)value.toString());
    }
    
    public boolean containsBoolean(final CharSequence name, final boolean value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    public boolean containsByte(final CharSequence name, final byte value) {
        return this.contains(name, (CharSequence)String.valueOf((int)value));
    }
    
    public boolean containsChar(final CharSequence name, final char value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    public boolean containsShort(final CharSequence name, final short value) {
        return this.contains(name, (CharSequence)String.valueOf((int)value));
    }
    
    public boolean containsInt(final CharSequence name, final int value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    public boolean containsLong(final CharSequence name, final long value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    public boolean containsFloat(final CharSequence name, final float value) {
        return false;
    }
    
    public boolean containsDouble(final CharSequence name, final double value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    public boolean containsTimeMillis(final CharSequence name, final long value) {
        return this.contains(name, (CharSequence)String.valueOf(value));
    }
    
    public int size() {
        return this.pseudoHeaders.length + this.otherHeaders.length >>> 1;
    }
    
    public boolean isEmpty() {
        return this.pseudoHeaders.length == 0 && this.otherHeaders.length == 0;
    }
    
    public Set<CharSequence> names() {
        if (this.isEmpty()) {
            return (Set<CharSequence>)Collections.emptySet();
        }
        final Set<CharSequence> names = (Set<CharSequence>)new LinkedHashSet(this.size());
        for (int pseudoHeadersEnd = this.pseudoHeaders.length - 1, i = 0; i < pseudoHeadersEnd; i += 2) {
            names.add(this.pseudoHeaders[i]);
        }
        for (int otherHeadersEnd = this.otherHeaders.length - 1, j = 0; j < otherHeadersEnd; j += 2) {
            names.add(this.otherHeaders[j]);
        }
        return names;
    }
    
    public Http2Headers add(final CharSequence name, final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers add(final CharSequence name, final Iterable<? extends CharSequence> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers add(final CharSequence name, final CharSequence... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addObject(final CharSequence name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addObject(final CharSequence name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addObject(final CharSequence name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addBoolean(final CharSequence name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addByte(final CharSequence name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addChar(final CharSequence name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addShort(final CharSequence name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addInt(final CharSequence name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addLong(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addFloat(final CharSequence name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addDouble(final CharSequence name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers addTimeMillis(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers add(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers set(final CharSequence name, final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers set(final CharSequence name, final Iterable<? extends CharSequence> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers set(final CharSequence name, final CharSequence... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setObject(final CharSequence name, final Object value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setObject(final CharSequence name, final Iterable<?> values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setObject(final CharSequence name, final Object... values) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setBoolean(final CharSequence name, final boolean value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setByte(final CharSequence name, final byte value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setChar(final CharSequence name, final char value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setShort(final CharSequence name, final short value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setInt(final CharSequence name, final int value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setLong(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setFloat(final CharSequence name, final float value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setDouble(final CharSequence name, final double value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setTimeMillis(final CharSequence name, final long value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers set(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers setAll(final Headers<? extends CharSequence, ? extends CharSequence, ?> headers) {
        throw new UnsupportedOperationException("read only");
    }
    
    public boolean remove(final CharSequence name) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers clear() {
        throw new UnsupportedOperationException("read only");
    }
    
    public Iterator<Map.Entry<CharSequence, CharSequence>> iterator() {
        return (Iterator<Map.Entry<CharSequence, CharSequence>>)new ReadOnlyIterator();
    }
    
    public Iterator<CharSequence> valueIterator(final CharSequence name) {
        return (Iterator<CharSequence>)new ReadOnlyValueIterator(name);
    }
    
    public Http2Headers method(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers scheme(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers authority(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers path(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public Http2Headers status(final CharSequence value) {
        throw new UnsupportedOperationException("read only");
    }
    
    public CharSequence method() {
        return this.get((CharSequence)PseudoHeaderName.METHOD.value());
    }
    
    public CharSequence scheme() {
        return this.get((CharSequence)PseudoHeaderName.SCHEME.value());
    }
    
    public CharSequence authority() {
        return this.get((CharSequence)PseudoHeaderName.AUTHORITY.value());
    }
    
    public CharSequence path() {
        return this.get((CharSequence)PseudoHeaderName.PATH.value());
    }
    
    public CharSequence status() {
        return this.get((CharSequence)PseudoHeaderName.STATUS.value());
    }
    
    public boolean contains(final CharSequence name, final CharSequence value, final boolean caseInsensitive) {
        final int nameHash = AsciiString.hashCode(name);
        final HashingStrategy<CharSequence> strategy = caseInsensitive ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER;
        final int valueHash = strategy.hashCode(value);
        return contains(name, nameHash, value, valueHash, strategy, this.otherHeaders) || contains(name, nameHash, value, valueHash, strategy, this.pseudoHeaders);
    }
    
    private static boolean contains(final CharSequence name, final int nameHash, final CharSequence value, final int valueHash, final HashingStrategy<CharSequence> hashingStrategy, final AsciiString[] headers) {
        for (int headersEnd = headers.length - 1, i = 0; i < headersEnd; i += 2) {
            final AsciiString roName = headers[i];
            final AsciiString roValue = headers[i + 1];
            if (roName.hashCode() == nameHash && roValue.hashCode() == valueHash && roName.contentEqualsIgnoreCase(name) && hashingStrategy.equals((CharSequence)roValue, value)) {
                return true;
            }
        }
        return false;
    }
    
    public String toString() {
        final StringBuilder builder = new StringBuilder(this.getClass().getSimpleName()).append('[');
        String separator = "";
        for (final Map.Entry<CharSequence, CharSequence> entry : this) {
            builder.append(separator);
            builder.append((CharSequence)entry.getKey()).append(": ").append((CharSequence)entry.getValue());
            separator = ", ";
        }
        return builder.append(']').toString();
    }
    
    private final class ReadOnlyValueIterator implements Iterator<CharSequence> {
        private int i;
        private final int nameHash;
        private final CharSequence name;
        private AsciiString[] current;
        private AsciiString next;
        
        ReadOnlyValueIterator(final CharSequence name) {
            this.current = ((ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0) ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders);
            this.nameHash = AsciiString.hashCode(name);
            this.name = name;
            this.calculateNext();
        }
        
        public boolean hasNext() {
            return this.next != null;
        }
        
        public CharSequence next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final CharSequence current = (CharSequence)this.next;
            this.calculateNext();
            return current;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
        
        private void calculateNext() {
            while (this.i < this.current.length) {
                final AsciiString roName = this.current[this.i];
                if (roName.hashCode() == this.nameHash && roName.contentEqualsIgnoreCase(this.name)) {
                    this.next = this.current[this.i + 1];
                    this.i += 2;
                    return;
                }
                this.i += 2;
            }
            if (this.i >= this.current.length && this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
                this.i = 0;
                this.current = ReadOnlyHttp2Headers.this.otherHeaders;
                this.calculateNext();
            }
            else {
                this.next = null;
            }
        }
    }
    
    private final class ReadOnlyIterator implements Map.Entry<CharSequence, CharSequence>, Iterator<Map.Entry<CharSequence, CharSequence>> {
        private int i;
        private AsciiString[] current;
        private AsciiString key;
        private AsciiString value;
        
        private ReadOnlyIterator() {
            this.current = ((ReadOnlyHttp2Headers.this.pseudoHeaders.length != 0) ? ReadOnlyHttp2Headers.this.pseudoHeaders : ReadOnlyHttp2Headers.this.otherHeaders);
        }
        
        public boolean hasNext() {
            return this.i != this.current.length;
        }
        
        public Map.Entry<CharSequence, CharSequence> next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.key = this.current[this.i];
            this.value = this.current[this.i + 1];
            this.i += 2;
            if (this.i == this.current.length && this.current == ReadOnlyHttp2Headers.this.pseudoHeaders) {
                this.current = ReadOnlyHttp2Headers.this.otherHeaders;
                this.i = 0;
            }
            return (Map.Entry<CharSequence, CharSequence>)this;
        }
        
        public CharSequence getKey() {
            return (CharSequence)this.key;
        }
        
        public CharSequence getValue() {
            return (CharSequence)this.value;
        }
        
        public CharSequence setValue(final CharSequence value) {
            throw new UnsupportedOperationException("read only");
        }
        
        public void remove() {
            throw new UnsupportedOperationException("read only");
        }
        
        public String toString() {
            return this.key.toString() + '=' + this.value.toString();
        }
    }
}
