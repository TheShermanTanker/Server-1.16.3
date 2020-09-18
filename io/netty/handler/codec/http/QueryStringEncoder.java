package io.netty.handler.codec.http;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;
import java.net.URLEncoder;
import java.net.URISyntaxException;
import java.net.URI;
import io.netty.util.internal.ObjectUtil;
import java.nio.charset.Charset;

public class QueryStringEncoder {
    private final String charsetName;
    private final StringBuilder uriBuilder;
    private boolean hasParams;
    
    public QueryStringEncoder(final String uri) {
        this(uri, HttpConstants.DEFAULT_CHARSET);
    }
    
    public QueryStringEncoder(final String uri, final Charset charset) {
        this.uriBuilder = new StringBuilder(uri);
        this.charsetName = charset.name();
    }
    
    public void addParam(final String name, final String value) {
        ObjectUtil.<String>checkNotNull(name, "name");
        if (this.hasParams) {
            this.uriBuilder.append('&');
        }
        else {
            this.uriBuilder.append('?');
            this.hasParams = true;
        }
        appendComponent(name, this.charsetName, this.uriBuilder);
        if (value != null) {
            this.uriBuilder.append('=');
            appendComponent(value, this.charsetName, this.uriBuilder);
        }
    }
    
    public URI toUri() throws URISyntaxException {
        return new URI(this.toString());
    }
    
    public String toString() {
        return this.uriBuilder.toString();
    }
    
    private static void appendComponent(String s, final String charset, final StringBuilder sb) {
        try {
            s = URLEncoder.encode(s, charset);
        }
        catch (UnsupportedEncodingException ignored) {
            throw new UnsupportedCharsetException(charset);
        }
        int idx = s.indexOf(43);
        if (idx == -1) {
            sb.append(s);
            return;
        }
        sb.append((CharSequence)s, 0, idx).append("%20");
        final int size = s.length();
        ++idx;
        while (idx < size) {
            final char c = s.charAt(idx);
            if (c != '+') {
                sb.append(c);
            }
            else {
                sb.append("%20");
            }
            ++idx;
        }
    }
}
