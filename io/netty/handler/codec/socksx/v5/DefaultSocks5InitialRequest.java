package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.internal.StringUtil;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class DefaultSocks5InitialRequest extends AbstractSocks5Message implements Socks5InitialRequest {
    private final List<Socks5AuthMethod> authMethods;
    
    public DefaultSocks5InitialRequest(final Socks5AuthMethod... authMethods) {
        if (authMethods == null) {
            throw new NullPointerException("authMethods");
        }
        final List<Socks5AuthMethod> list = (List<Socks5AuthMethod>)new ArrayList(authMethods.length);
        for (final Socks5AuthMethod m : authMethods) {
            if (m == null) {
                break;
            }
            list.add(m);
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("authMethods is empty");
        }
        this.authMethods = (List<Socks5AuthMethod>)Collections.unmodifiableList((List)list);
    }
    
    public DefaultSocks5InitialRequest(final Iterable<Socks5AuthMethod> authMethods) {
        if (authMethods == null) {
            throw new NullPointerException("authSchemes");
        }
        final List<Socks5AuthMethod> list = (List<Socks5AuthMethod>)new ArrayList();
        for (final Socks5AuthMethod m : authMethods) {
            if (m == null) {
                break;
            }
            list.add(m);
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("authMethods is empty");
        }
        this.authMethods = (List<Socks5AuthMethod>)Collections.unmodifiableList((List)list);
    }
    
    @Override
    public List<Socks5AuthMethod> authMethods() {
        return this.authMethods;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
        final DecoderResult decoderResult = this.decoderResult();
        if (!decoderResult.isSuccess()) {
            buf.append("(decoderResult: ");
            buf.append(decoderResult);
            buf.append(", authMethods: ");
        }
        else {
            buf.append("(authMethods: ");
        }
        buf.append(this.authMethods());
        buf.append(')');
        return buf.toString();
    }
}
