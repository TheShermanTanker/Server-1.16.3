package io.netty.handler.codec.http;

import java.util.Calendar;
import io.netty.handler.codec.DateFormatter;
import java.util.Date;
import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.util.internal.PlatformDependent;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.List;
import io.netty.handler.codec.HeadersUtils;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.ValueConverter;
import io.netty.util.HashingStrategy;
import io.netty.handler.codec.DefaultHeadersImpl;
import io.netty.util.AsciiString;
import io.netty.handler.codec.DefaultHeaders;
import io.netty.util.ByteProcessor;

public class DefaultHttpHeaders extends HttpHeaders {
    private static final int HIGHEST_INVALID_VALUE_CHAR_MASK = -16;
    private static final ByteProcessor HEADER_NAME_VALIDATOR;
    static final DefaultHeaders.NameValidator<CharSequence> HttpNameValidator;
    private final DefaultHeaders<CharSequence, CharSequence, ?> headers;
    
    public DefaultHttpHeaders() {
        this(true);
    }
    
    public DefaultHttpHeaders(final boolean validate) {
        this(validate, nameValidator(validate));
    }
    
    protected DefaultHttpHeaders(final boolean validate, final DefaultHeaders.NameValidator<CharSequence> nameValidator) {
        this(new DefaultHeadersImpl((HashingStrategy<Object>)AsciiString.CASE_INSENSITIVE_HASHER, (ValueConverter<Object>)valueConverter(validate), (DefaultHeaders.NameValidator<Object>)nameValidator));
    }
    
    protected DefaultHttpHeaders(final DefaultHeaders<CharSequence, CharSequence, ?> headers) {
        this.headers = headers;
    }
    
    @Override
    public HttpHeaders add(final HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.headers.add(((DefaultHttpHeaders)headers).headers);
            return this;
        }
        return super.add(headers);
    }
    
    @Override
    public HttpHeaders set(final HttpHeaders headers) {
        if (headers instanceof DefaultHttpHeaders) {
            this.headers.set(((DefaultHttpHeaders)headers).headers);
            return this;
        }
        return super.set(headers);
    }
    
    @Override
    public HttpHeaders add(final String name, final Object value) {
        this.headers.addObject((CharSequence)name, value);
        return this;
    }
    
    @Override
    public HttpHeaders add(final CharSequence name, final Object value) {
        this.headers.addObject(name, value);
        return this;
    }
    
    @Override
    public HttpHeaders add(final String name, final Iterable<?> values) {
        this.headers.addObject((CharSequence)name, values);
        return this;
    }
    
    @Override
    public HttpHeaders add(final CharSequence name, final Iterable<?> values) {
        this.headers.addObject(name, values);
        return this;
    }
    
    @Override
    public HttpHeaders addInt(final CharSequence name, final int value) {
        this.headers.addInt(name, value);
        return this;
    }
    
    @Override
    public HttpHeaders addShort(final CharSequence name, final short value) {
        this.headers.addShort(name, value);
        return this;
    }
    
    @Override
    public HttpHeaders remove(final String name) {
        this.headers.remove((CharSequence)name);
        return this;
    }
    
    @Override
    public HttpHeaders remove(final CharSequence name) {
        this.headers.remove(name);
        return this;
    }
    
    @Override
    public HttpHeaders set(final String name, final Object value) {
        this.headers.setObject((CharSequence)name, value);
        return this;
    }
    
    @Override
    public HttpHeaders set(final CharSequence name, final Object value) {
        this.headers.setObject(name, value);
        return this;
    }
    
    @Override
    public HttpHeaders set(final String name, final Iterable<?> values) {
        this.headers.setObject((CharSequence)name, values);
        return this;
    }
    
    @Override
    public HttpHeaders set(final CharSequence name, final Iterable<?> values) {
        this.headers.setObject(name, values);
        return this;
    }
    
    @Override
    public HttpHeaders setInt(final CharSequence name, final int value) {
        this.headers.setInt(name, value);
        return this;
    }
    
    @Override
    public HttpHeaders setShort(final CharSequence name, final short value) {
        this.headers.setShort(name, value);
        return this;
    }
    
    @Override
    public HttpHeaders clear() {
        this.headers.clear();
        return this;
    }
    
    @Override
    public String get(final String name) {
        return this.get((CharSequence)name);
    }
    
    @Override
    public String get(final CharSequence name) {
        return HeadersUtils.<CharSequence, CharSequence>getAsString(this.headers, name);
    }
    
    @Override
    public Integer getInt(final CharSequence name) {
        return this.headers.getInt(name);
    }
    
    @Override
    public int getInt(final CharSequence name, final int defaultValue) {
        return this.headers.getInt(name, defaultValue);
    }
    
    @Override
    public Short getShort(final CharSequence name) {
        return this.headers.getShort(name);
    }
    
    @Override
    public short getShort(final CharSequence name, final short defaultValue) {
        return this.headers.getShort(name, defaultValue);
    }
    
    @Override
    public Long getTimeMillis(final CharSequence name) {
        return this.headers.getTimeMillis(name);
    }
    
    @Override
    public long getTimeMillis(final CharSequence name, final long defaultValue) {
        return this.headers.getTimeMillis(name, defaultValue);
    }
    
    @Override
    public List<String> getAll(final String name) {
        return this.getAll((CharSequence)name);
    }
    
    @Override
    public List<String> getAll(final CharSequence name) {
        return HeadersUtils.<CharSequence, CharSequence>getAllAsString(this.headers, name);
    }
    
    @Override
    public List<Map.Entry<String, String>> entries() {
        if (this.isEmpty()) {
            return (List<Map.Entry<String, String>>)Collections.emptyList();
        }
        final List<Map.Entry<String, String>> entriesConverted = (List<Map.Entry<String, String>>)new ArrayList(this.headers.size());
        for (final Map.Entry<String, String> entry : this) {
            entriesConverted.add(entry);
        }
        return entriesConverted;
    }
    
    @Deprecated
    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        return HeadersUtils.iteratorAsString((Iterable<Map.Entry<CharSequence, CharSequence>>)this.headers);
    }
    
    @Override
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        return this.headers.iterator();
    }
    
    @Override
    public Iterator<String> valueStringIterator(final CharSequence name) {
        final Iterator<CharSequence> itr = this.valueCharSequenceIterator(name);
        return (Iterator<String>)new Iterator<String>() {
            public boolean hasNext() {
                return itr.hasNext();
            }
            
            public String next() {
                return ((CharSequence)itr.next()).toString();
            }
            
            public void remove() {
                itr.remove();
            }
        };
    }
    
    @Override
    public Iterator<CharSequence> valueCharSequenceIterator(final CharSequence name) {
        return this.headers.valueIterator(name);
    }
    
    @Override
    public boolean contains(final String name) {
        return this.contains((CharSequence)name);
    }
    
    @Override
    public boolean contains(final CharSequence name) {
        return this.headers.contains(name);
    }
    
    @Override
    public boolean isEmpty() {
        return this.headers.isEmpty();
    }
    
    @Override
    public int size() {
        return this.headers.size();
    }
    
    @Override
    public boolean contains(final String name, final String value, final boolean ignoreCase) {
        return this.contains((CharSequence)name, (CharSequence)value, ignoreCase);
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value, final boolean ignoreCase) {
        return this.headers.contains(name, value, ignoreCase ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
    }
    
    @Override
    public Set<String> names() {
        return HeadersUtils.namesAsString(this.headers);
    }
    
    public boolean equals(final Object o) {
        return o instanceof DefaultHttpHeaders && this.headers.equals(((DefaultHttpHeaders)o).headers, AsciiString.CASE_SENSITIVE_HASHER);
    }
    
    public int hashCode() {
        return this.headers.hashCode(AsciiString.CASE_SENSITIVE_HASHER);
    }
    
    @Override
    public HttpHeaders copy() {
        return new DefaultHttpHeaders(this.headers.copy());
    }
    
    private static void validateHeaderNameElement(final byte value) {
        switch (value) {
            case 0:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 32:
            case 44:
            case 58:
            case 59:
            case 61: {
                throw new IllegalArgumentException(new StringBuilder().append("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: ").append((int)value).toString());
            }
            default: {
                if (value < 0) {
                    throw new IllegalArgumentException(new StringBuilder().append("a header name cannot contain non-ASCII character: ").append((int)value).toString());
                }
            }
        }
    }
    
    private static void validateHeaderNameElement(final char value) {
        switch (value) {
            case '\0':
            case '\t':
            case '\n':
            case '\u000b':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case ';':
            case '=': {
                throw new IllegalArgumentException(new StringBuilder().append("a header name cannot contain the following prohibited characters: =,;: \\t\\r\\n\\v\\f: ").append(value).toString());
            }
            default: {
                if (value > '\u007f') {
                    throw new IllegalArgumentException(new StringBuilder().append("a header name cannot contain non-ASCII character: ").append(value).toString());
                }
            }
        }
    }
    
    static ValueConverter<CharSequence> valueConverter(final boolean validate) {
        return validate ? HeaderValueConverterAndValidator.INSTANCE : HeaderValueConverter.INSTANCE;
    }
    
    static DefaultHeaders.NameValidator<CharSequence> nameValidator(final boolean validate) {
        return validate ? DefaultHttpHeaders.HttpNameValidator : DefaultHeaders.NameValidator.NOT_NULL;
    }
    
    static {
        HEADER_NAME_VALIDATOR = new ByteProcessor() {
            public boolean process(final byte value) throws Exception {
                validateHeaderNameElement(value);
                return true;
            }
        };
        HttpNameValidator = new DefaultHeaders.NameValidator<CharSequence>() {
            public void validateName(final CharSequence name) {
                if (name == null || name.length() == 0) {
                    throw new IllegalArgumentException(new StringBuilder().append("empty headers are not allowed [").append(name).append("]").toString());
                }
                if (name instanceof AsciiString) {
                    try {
                        ((AsciiString)name).forEachByte(DefaultHttpHeaders.HEADER_NAME_VALIDATOR);
                    }
                    catch (Exception e) {
                        PlatformDependent.throwException((Throwable)e);
                    }
                }
                else {
                    for (int index = 0; index < name.length(); ++index) {
                        validateHeaderNameElement(name.charAt(index));
                    }
                }
            }
        };
    }
    
    private static class HeaderValueConverter extends CharSequenceValueConverter {
        static final HeaderValueConverter INSTANCE;
        
        @Override
        public CharSequence convertObject(final Object value) {
            if (value instanceof CharSequence) {
                return (CharSequence)value;
            }
            if (value instanceof Date) {
                return (CharSequence)DateFormatter.format((Date)value);
            }
            if (value instanceof Calendar) {
                return (CharSequence)DateFormatter.format(((Calendar)value).getTime());
            }
            return (CharSequence)value.toString();
        }
        
        static {
            INSTANCE = new HeaderValueConverter();
        }
    }
    
    private static final class HeaderValueConverterAndValidator extends HeaderValueConverter {
        static final HeaderValueConverterAndValidator INSTANCE;
        
        @Override
        public CharSequence convertObject(final Object value) {
            final CharSequence seq = super.convertObject(value);
            int state = 0;
            for (int index = 0; index < seq.length(); ++index) {
                state = validateValueChar(seq, state, seq.charAt(index));
            }
            if (state != 0) {
                throw new IllegalArgumentException(new StringBuilder().append("a header value must not end with '\\r' or '\\n':").append(seq).toString());
            }
            return seq;
        }
        
        private static int validateValueChar(final CharSequence seq, final int state, final char character) {
            if ((character & 0xFFFFFFF0) == 0x0) {
                switch (character) {
                    case '\0': {
                        throw new IllegalArgumentException(new StringBuilder().append("a header value contains a prohibited character '\u0000': ").append(seq).toString());
                    }
                    case '\u000b': {
                        throw new IllegalArgumentException(new StringBuilder().append("a header value contains a prohibited character '\\v': ").append(seq).toString());
                    }
                    case '\f': {
                        throw new IllegalArgumentException(new StringBuilder().append("a header value contains a prohibited character '\\f': ").append(seq).toString());
                    }
                }
            }
            Label_0293: {
                switch (state) {
                    case 0: {
                        switch (character) {
                            case '\r': {
                                return 1;
                            }
                            case '\n': {
                                return 2;
                            }
                            default: {
                                break Label_0293;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (character) {
                            case '\n': {
                                return 2;
                            }
                            default: {
                                throw new IllegalArgumentException(new StringBuilder().append("only '\\n' is allowed after '\\r': ").append(seq).toString());
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (character) {
                            case '\t':
                            case ' ': {
                                return 0;
                            }
                            default: {
                                throw new IllegalArgumentException(new StringBuilder().append("only ' ' and '\\t' are allowed after '\\n': ").append(seq).toString());
                            }
                        }
                        break;
                    }
                }
            }
            return state;
        }
        
        static {
            INSTANCE = new HeaderValueConverterAndValidator();
        }
    }
}
