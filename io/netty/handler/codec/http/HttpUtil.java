package io.netty.handler.codec.http;

import io.netty.util.NetUtil;
import java.net.InetSocketAddress;
import java.nio.charset.UnsupportedCharsetException;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.net.URI;
import io.netty.util.AsciiString;

public final class HttpUtil {
    private static final AsciiString CHARSET_EQUALS;
    private static final AsciiString SEMICOLON;
    
    private HttpUtil() {
    }
    
    public static boolean isOriginForm(final URI uri) {
        return uri.getScheme() == null && uri.getSchemeSpecificPart() == null && uri.getHost() == null && uri.getAuthority() == null;
    }
    
    public static boolean isAsteriskForm(final URI uri) {
        return "*".equals(uri.getPath()) && uri.getScheme() == null && uri.getSchemeSpecificPart() == null && uri.getHost() == null && uri.getAuthority() == null && uri.getQuery() == null && uri.getFragment() == null;
    }
    
    public static boolean isKeepAlive(final HttpMessage message) {
        final CharSequence connection = (CharSequence)message.headers().get((CharSequence)HttpHeaderNames.CONNECTION);
        if (connection != null && HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection)) {
            return false;
        }
        if (message.protocolVersion().isKeepAliveDefault()) {
            return !HttpHeaderValues.CLOSE.contentEqualsIgnoreCase(connection);
        }
        return HttpHeaderValues.KEEP_ALIVE.contentEqualsIgnoreCase(connection);
    }
    
    public static void setKeepAlive(final HttpMessage message, final boolean keepAlive) {
        setKeepAlive(message.headers(), message.protocolVersion(), keepAlive);
    }
    
    public static void setKeepAlive(final HttpHeaders h, final HttpVersion httpVersion, final boolean keepAlive) {
        if (httpVersion.isKeepAliveDefault()) {
            if (keepAlive) {
                h.remove((CharSequence)HttpHeaderNames.CONNECTION);
            }
            else {
                h.set((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            }
        }
        else if (keepAlive) {
            h.set((CharSequence)HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        else {
            h.remove((CharSequence)HttpHeaderNames.CONNECTION);
        }
    }
    
    public static long getContentLength(final HttpMessage message) {
        final String value = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
        if (value != null) {
            return Long.parseLong(value);
        }
        final long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0L) {
            return webSocketContentLength;
        }
        throw new NumberFormatException(new StringBuilder().append("header not found: ").append(HttpHeaderNames.CONTENT_LENGTH).toString());
    }
    
    public static long getContentLength(final HttpMessage message, final long defaultValue) {
        final String value = message.headers().get((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
        if (value != null) {
            return Long.parseLong(value);
        }
        final long webSocketContentLength = getWebSocketContentLength(message);
        if (webSocketContentLength >= 0L) {
            return webSocketContentLength;
        }
        return defaultValue;
    }
    
    public static int getContentLength(final HttpMessage message, final int defaultValue) {
        return (int)Math.min(2147483647L, getContentLength(message, (long)defaultValue));
    }
    
    private static int getWebSocketContentLength(final HttpMessage message) {
        final HttpHeaders h = message.headers();
        if (message instanceof HttpRequest) {
            final HttpRequest req = (HttpRequest)message;
            if (HttpMethod.GET.equals(req.method()) && h.contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY1) && h.contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_KEY2)) {
                return 8;
            }
        }
        else if (message instanceof HttpResponse) {
            final HttpResponse res = (HttpResponse)message;
            if (res.status().code() == 101 && h.contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_ORIGIN) && h.contains((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_LOCATION)) {
                return 16;
            }
        }
        return -1;
    }
    
    public static void setContentLength(final HttpMessage message, final long length) {
        message.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, length);
    }
    
    public static boolean isContentLengthSet(final HttpMessage m) {
        return m.headers().contains((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
    }
    
    public static boolean is100ContinueExpected(final HttpMessage message) {
        if (!isExpectHeaderValid(message)) {
            return false;
        }
        final String expectValue = message.headers().get((CharSequence)HttpHeaderNames.EXPECT);
        return HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue);
    }
    
    static boolean isUnsupportedExpectation(final HttpMessage message) {
        if (!isExpectHeaderValid(message)) {
            return false;
        }
        final String expectValue = message.headers().get((CharSequence)HttpHeaderNames.EXPECT);
        return expectValue != null && !HttpHeaderValues.CONTINUE.toString().equalsIgnoreCase(expectValue);
    }
    
    private static boolean isExpectHeaderValid(final HttpMessage message) {
        return message instanceof HttpRequest && message.protocolVersion().compareTo(HttpVersion.HTTP_1_1) >= 0;
    }
    
    public static void set100ContinueExpected(final HttpMessage message, final boolean expected) {
        if (expected) {
            message.headers().set((CharSequence)HttpHeaderNames.EXPECT, HttpHeaderValues.CONTINUE);
        }
        else {
            message.headers().remove((CharSequence)HttpHeaderNames.EXPECT);
        }
    }
    
    public static boolean isTransferEncodingChunked(final HttpMessage message) {
        return message.headers().contains((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, (CharSequence)HttpHeaderValues.CHUNKED, true);
    }
    
    public static void setTransferEncodingChunked(final HttpMessage m, final boolean chunked) {
        if (chunked) {
            m.headers().set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            m.headers().remove((CharSequence)HttpHeaderNames.CONTENT_LENGTH);
        }
        else {
            final List<String> encodings = m.headers().getAll((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
            if (encodings.isEmpty()) {
                return;
            }
            final List<CharSequence> values = (List<CharSequence>)new ArrayList((Collection)encodings);
            final Iterator<CharSequence> valuesIt = (Iterator<CharSequence>)values.iterator();
            while (valuesIt.hasNext()) {
                final CharSequence value = (CharSequence)valuesIt.next();
                if (HttpHeaderValues.CHUNKED.contentEqualsIgnoreCase(value)) {
                    valuesIt.remove();
                }
            }
            if (values.isEmpty()) {
                m.headers().remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
            }
            else {
                m.headers().set((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, values);
            }
        }
    }
    
    public static Charset getCharset(final HttpMessage message) {
        return getCharset(message, CharsetUtil.ISO_8859_1);
    }
    
    public static Charset getCharset(final CharSequence contentTypeValue) {
        if (contentTypeValue != null) {
            return getCharset(contentTypeValue, CharsetUtil.ISO_8859_1);
        }
        return CharsetUtil.ISO_8859_1;
    }
    
    public static Charset getCharset(final HttpMessage message, final Charset defaultCharset) {
        final CharSequence contentTypeValue = (CharSequence)message.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            return getCharset(contentTypeValue, defaultCharset);
        }
        return defaultCharset;
    }
    
    public static Charset getCharset(final CharSequence contentTypeValue, final Charset defaultCharset) {
        if (contentTypeValue != null) {
            final CharSequence charsetCharSequence = getCharsetAsSequence(contentTypeValue);
            if (charsetCharSequence != null) {
                try {
                    return Charset.forName(charsetCharSequence.toString());
                }
                catch (UnsupportedCharsetException ignored) {
                    return defaultCharset;
                }
            }
            return defaultCharset;
        }
        return defaultCharset;
    }
    
    @Deprecated
    public static CharSequence getCharsetAsString(final HttpMessage message) {
        return getCharsetAsSequence(message);
    }
    
    public static CharSequence getCharsetAsSequence(final HttpMessage message) {
        final CharSequence contentTypeValue = (CharSequence)message.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            return getCharsetAsSequence(contentTypeValue);
        }
        return null;
    }
    
    public static CharSequence getCharsetAsSequence(final CharSequence contentTypeValue) {
        if (contentTypeValue == null) {
            throw new NullPointerException("contentTypeValue");
        }
        final int indexOfCharset = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, (CharSequence)HttpUtil.CHARSET_EQUALS, 0);
        if (indexOfCharset != -1) {
            final int indexOfEncoding = indexOfCharset + HttpUtil.CHARSET_EQUALS.length();
            if (indexOfEncoding < contentTypeValue.length()) {
                return contentTypeValue.subSequence(indexOfEncoding, contentTypeValue.length());
            }
        }
        return null;
    }
    
    public static CharSequence getMimeType(final HttpMessage message) {
        final CharSequence contentTypeValue = (CharSequence)message.headers().get((CharSequence)HttpHeaderNames.CONTENT_TYPE);
        if (contentTypeValue != null) {
            return getMimeType(contentTypeValue);
        }
        return null;
    }
    
    public static CharSequence getMimeType(final CharSequence contentTypeValue) {
        if (contentTypeValue == null) {
            throw new NullPointerException("contentTypeValue");
        }
        final int indexOfSemicolon = AsciiString.indexOfIgnoreCaseAscii(contentTypeValue, (CharSequence)HttpUtil.SEMICOLON, 0);
        if (indexOfSemicolon != -1) {
            return contentTypeValue.subSequence(0, indexOfSemicolon);
        }
        return (contentTypeValue.length() > 0) ? contentTypeValue : null;
    }
    
    public static String formatHostnameForHttp(final InetSocketAddress addr) {
        String hostString = NetUtil.getHostname(addr);
        if (NetUtil.isValidIpV6Address(hostString)) {
            if (!addr.isUnresolved()) {
                hostString = NetUtil.toAddressString(addr.getAddress());
            }
            return "[" + hostString + "]";
        }
        return hostString;
    }
    
    static {
        CHARSET_EQUALS = AsciiString.of((CharSequence)new StringBuilder().append(HttpHeaderValues.CHARSET).append("=").toString());
        SEMICOLON = AsciiString.cached(";");
    }
}
