package io.netty.handler.codec.http2;

import java.util.Iterator;
import io.netty.handler.codec.EmptyHeaders;

public final class EmptyHttp2Headers extends EmptyHeaders<CharSequence, CharSequence, Http2Headers> implements Http2Headers {
    public static final EmptyHttp2Headers INSTANCE;
    
    private EmptyHttp2Headers() {
    }
    
    @Override
    public EmptyHttp2Headers method(final CharSequence method) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers scheme(final CharSequence status) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers authority(final CharSequence authority) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers path(final CharSequence path) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public EmptyHttp2Headers status(final CharSequence status) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public CharSequence method() {
        return ((EmptyHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.METHOD.value());
    }
    
    @Override
    public CharSequence scheme() {
        return ((EmptyHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.SCHEME.value());
    }
    
    @Override
    public CharSequence authority() {
        return ((EmptyHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.AUTHORITY.value());
    }
    
    @Override
    public CharSequence path() {
        return ((EmptyHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.PATH.value());
    }
    
    @Override
    public CharSequence status() {
        return ((EmptyHeaders<CharSequence, CharSequence, T>)this).get((CharSequence)PseudoHeaderName.STATUS.value());
    }
    
    @Override
    public boolean contains(final CharSequence name, final CharSequence value, final boolean caseInsensitive) {
        return false;
    }
    
    static {
        INSTANCE = new EmptyHttp2Headers();
    }
}
