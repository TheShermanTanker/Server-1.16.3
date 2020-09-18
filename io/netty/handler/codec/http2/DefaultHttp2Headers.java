package io.netty.handler.codec.http2;

import io.netty.util.internal.PlatformDependent;
import java.util.Iterator;
import io.netty.util.HashingStrategy;
import io.netty.handler.codec.Headers;
import io.netty.handler.codec.ValueConverter;
import io.netty.handler.codec.CharSequenceValueConverter;
import io.netty.util.AsciiString;
import io.netty.util.ByteProcessor;
import io.netty.handler.codec.DefaultHeaders;

public class DefaultHttp2Headers extends DefaultHeaders<CharSequence, CharSequence, Http2Headers> implements Http2Headers {
    private static final ByteProcessor HTTP2_NAME_VALIDATOR_PROCESSOR;
    static final NameValidator<CharSequence> HTTP2_NAME_VALIDATOR;
    private HeaderEntry<CharSequence, CharSequence> firstNonPseudo;
    
    public DefaultHttp2Headers() {
        this(true);
    }
    
    public DefaultHttp2Headers(final boolean validate) {
        super(AsciiString.CASE_SENSITIVE_HASHER, (ValueConverter<Object>)CharSequenceValueConverter.INSTANCE, validate ? DefaultHttp2Headers.HTTP2_NAME_VALIDATOR : NameValidator.NOT_NULL);
        this.firstNonPseudo = (HeaderEntry<CharSequence, CharSequence>)this.head;
    }
    
    public DefaultHttp2Headers(final boolean validate, final int arraySizeHint) {
        super(AsciiString.CASE_SENSITIVE_HASHER, (ValueConverter<Object>)CharSequenceValueConverter.INSTANCE, validate ? DefaultHttp2Headers.HTTP2_NAME_VALIDATOR : NameValidator.NOT_NULL, arraySizeHint);
        this.firstNonPseudo = (HeaderEntry<CharSequence, CharSequence>)this.head;
    }
    
    @Override
    public Http2Headers clear() {
        this.firstNonPseudo = (HeaderEntry<CharSequence, CharSequence>)this.head;
        return super.clear();
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof Http2Headers && ((DefaultHeaders<CharSequence, CharSequence, T>)this).equals(o, AsciiString.CASE_SENSITIVE_HASHER);
    }
    
    @Override
    public int hashCode() {
        return ((DefaultHeaders<K, CharSequence, T>)this).hashCode(AsciiString.CASE_SENSITIVE_HASHER);
    }
    
    @Override
    public Http2Headers method(final CharSequence value) {
        ((DefaultHeaders<CharSequence, CharSequence, Headers>)this).set((CharSequence)PseudoHeaderName.METHOD.value(), value);
        return this;
    }
    
    @Override
    public Http2Headers scheme(final CharSequence value) {
        ((DefaultHeaders<CharSequence, CharSequence, Headers>)this).set((CharSequence)PseudoHeaderName.SCHEME.value(), value);
        return this;
    }
    
    @Override
    public Http2Headers authority(final CharSequence value) {
        ((DefaultHeaders<CharSequence, CharSequence, Headers>)this).set((CharSequence)PseudoHeaderName.AUTHORITY.value(), value);
        return this;
    }
    
    @Override
    public Http2Headers path(final CharSequence value) {
        ((DefaultHeaders<CharSequence, CharSequence, Headers>)this).set((CharSequence)PseudoHeaderName.PATH.value(), value);
        return this;
    }
    
    @Override
    public Http2Headers status(final CharSequence value) {
        ((DefaultHeaders<CharSequence, CharSequence, Headers>)this).set((CharSequence)PseudoHeaderName.STATUS.value(), value);
        return this;
    }
    
    @Override
    public CharSequence method() {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.METHOD.value());
    }
    
    @Override
    public CharSequence scheme() {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.SCHEME.value());
    }
    
    @Override
    public CharSequence authority() {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.AUTHORITY.value());
    }
    
    @Override
    public CharSequence path() {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.PATH.value());
    }
    
    @Override
    public CharSequence status() {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.STATUS.value());
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value) {
        return this.contains(name, value, false);
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value, final boolean caseInsensitive) {
        return ((DefaultHeaders<CharSequence, CharSequence, T>)this).contains(name, value, caseInsensitive ? AsciiString.CASE_INSENSITIVE_HASHER : AsciiString.CASE_SENSITIVE_HASHER);
    }
    
    @Override
    protected final HeaderEntry<CharSequence, CharSequence> newHeaderEntry(final int h, final CharSequence name, final CharSequence value, final HeaderEntry<CharSequence, CharSequence> next) {
        return new Http2HeaderEntry(h, name, value, next);
    }
    
    static {
        HTTP2_NAME_VALIDATOR_PROCESSOR = new ByteProcessor() {
            public boolean process(final byte value) {
                return !AsciiString.isUpperCase(value);
            }
        };
        HTTP2_NAME_VALIDATOR = new NameValidator<CharSequence>() {
            public void validateName(final CharSequence name) {
                if (name == null || name.length() == 0) {
                    PlatformDependent.throwException((Throwable)Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "empty headers are not allowed [%s]", name));
                }
                if (name instanceof AsciiString) {
                    int index;
                    try {
                        index = ((AsciiString)name).forEachByte(DefaultHttp2Headers.HTTP2_NAME_VALIDATOR_PROCESSOR);
                    }
                    catch (Http2Exception e) {
                        PlatformDependent.throwException((Throwable)e);
                        return;
                    }
                    catch (Throwable t) {
                        PlatformDependent.throwException((Throwable)Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "unexpected error. invalid header name [%s]", name));
                        return;
                    }
                    if (index != -1) {
                        PlatformDependent.throwException((Throwable)Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "invalid header name [%s]", name));
                    }
                }
                else {
                    for (int i = 0; i < name.length(); ++i) {
                        if (AsciiString.isUpperCase(name.charAt(i))) {
                            PlatformDependent.throwException((Throwable)Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "invalid header name [%s]", name));
                        }
                    }
                }
            }
        };
    }
    
    private final class Http2HeaderEntry extends HeaderEntry<CharSequence, CharSequence> {
        protected Http2HeaderEntry(final int hash, final CharSequence key, final CharSequence value, final HeaderEntry<CharSequence, CharSequence> next) {
            super(hash, key);
            this.value = (V)value;
            this.next = (HeaderEntry<K, V>)next;
            if (PseudoHeaderName.hasPseudoHeaderFormat(key)) {
                this.after = (HeaderEntry<K, V>)DefaultHttp2Headers.this.firstNonPseudo;
                this.before = (HeaderEntry<K, V>)DefaultHttp2Headers.this.firstNonPseudo.before();
            }
            else {
                this.after = (HeaderEntry<K, V>)DefaultHttp2Headers.this.head;
                this.before = (HeaderEntry<K, V>)DefaultHttp2Headers.this.head.before();
                if (DefaultHttp2Headers.this.firstNonPseudo == DefaultHttp2Headers.this.head) {
                    DefaultHttp2Headers.this.firstNonPseudo = this;
                }
            }
            this.pointNeighborsToThis();
        }
        
        @Override
        protected void remove() {
            if (this == DefaultHttp2Headers.this.firstNonPseudo) {
                DefaultHttp2Headers.this.firstNonPseudo = (HeaderEntry<CharSequence, CharSequence>)DefaultHttp2Headers.this.firstNonPseudo.after();
            }
            super.remove();
        }
    }
}
