package io.netty.handler.codec.http2;

import io.netty.handler.codec.Headers;
import io.netty.handler.codec.http.HttpScheme;
import java.util.List;
import io.netty.util.internal.StringUtil;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.ByteProcessor;
import io.netty.handler.codec.ValueConverter;
import io.netty.handler.codec.UnsupportedValueConverter;
import java.net.URI;
import io.netty.handler.codec.http.HttpMessage;
import java.util.Iterator;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpHeaderNames;
import java.util.Map;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AsciiString;

public final class HttpConversionUtil {
    private static final CharSequenceMap<AsciiString> HTTP_TO_HTTP2_HEADER_BLACKLIST;
    public static final HttpMethod OUT_OF_MESSAGE_SEQUENCE_METHOD;
    public static final String OUT_OF_MESSAGE_SEQUENCE_PATH = "";
    public static final HttpResponseStatus OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE;
    private static final AsciiString EMPTY_REQUEST_PATH;
    
    private HttpConversionUtil() {
    }
    
    public static HttpResponseStatus parseStatus(final CharSequence status) throws Http2Exception {
        HttpResponseStatus result;
        try {
            result = HttpResponseStatus.parseLine(status);
            if (result == HttpResponseStatus.SWITCHING_PROTOCOLS) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 status code '%d'", result.code());
            }
        }
        catch (Http2Exception e) {
            throw e;
        }
        catch (Throwable t) {
            throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, t, "Unrecognized HTTP status code '%s' encountered in translation to HTTP/1.x", status);
        }
        return result;
    }
    
    public static FullHttpResponse toFullHttpResponse(final int streamId, final Http2Headers http2Headers, final ByteBufAllocator alloc, final boolean validateHttpHeaders) throws Http2Exception {
        final HttpResponseStatus status = parseStatus(http2Headers.status());
        final FullHttpResponse msg = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, alloc.buffer(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
        }
        catch (Http2Exception e) {
            msg.release();
            throw e;
        }
        catch (Throwable t) {
            msg.release();
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error");
        }
        return msg;
    }
    
    public static FullHttpRequest toFullHttpRequest(final int streamId, final Http2Headers http2Headers, final ByteBufAllocator alloc, final boolean validateHttpHeaders) throws Http2Exception {
        final CharSequence method = ObjectUtil.<CharSequence>checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
        final CharSequence path = ObjectUtil.<CharSequence>checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
        final FullHttpRequest msg = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), alloc.buffer(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg, false);
        }
        catch (Http2Exception e) {
            msg.release();
            throw e;
        }
        catch (Throwable t) {
            msg.release();
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error");
        }
        return msg;
    }
    
    public static HttpRequest toHttpRequest(final int streamId, final Http2Headers http2Headers, final boolean validateHttpHeaders) throws Http2Exception {
        final CharSequence method = ObjectUtil.<CharSequence>checkNotNull(http2Headers.method(), "method header cannot be null in conversion to HTTP/1.x");
        final CharSequence path = ObjectUtil.<CharSequence>checkNotNull(http2Headers.path(), "path header cannot be null in conversion to HTTP/1.x");
        final HttpRequest msg = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.valueOf(method.toString()), path.toString(), validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, true);
        }
        catch (Http2Exception e) {
            throw e;
        }
        catch (Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error");
        }
        return msg;
    }
    
    public static HttpResponse toHttpResponse(final int streamId, final Http2Headers http2Headers, final boolean validateHttpHeaders) throws Http2Exception {
        final HttpResponseStatus status = parseStatus(http2Headers.status());
        final HttpResponse msg = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status, validateHttpHeaders);
        try {
            addHttp2ToHttpHeaders(streamId, http2Headers, msg.headers(), msg.protocolVersion(), false, true);
        }
        catch (Http2Exception e) {
            throw e;
        }
        catch (Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error");
        }
        return msg;
    }
    
    public static void addHttp2ToHttpHeaders(final int streamId, final Http2Headers sourceHeaders, final FullHttpMessage destinationMessage, final boolean addToTrailer) throws Http2Exception {
        addHttp2ToHttpHeaders(streamId, sourceHeaders, addToTrailer ? destinationMessage.trailingHeaders() : destinationMessage.headers(), destinationMessage.protocolVersion(), addToTrailer, destinationMessage instanceof HttpRequest);
    }
    
    public static void addHttp2ToHttpHeaders(final int streamId, final Http2Headers inputHeaders, final HttpHeaders outputHeaders, final HttpVersion httpVersion, final boolean isTrailer, final boolean isRequest) throws Http2Exception {
        final Http2ToHttpHeaderTranslator translator = new Http2ToHttpHeaderTranslator(streamId, outputHeaders, isRequest);
        try {
            for (final Map.Entry<CharSequence, CharSequence> entry : inputHeaders) {
                translator.translate(entry);
            }
        }
        catch (Http2Exception ex) {
            throw ex;
        }
        catch (Throwable t) {
            throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, t, "HTTP/2 to HTTP/1.x headers conversion error");
        }
        outputHeaders.remove((CharSequence)HttpHeaderNames.TRANSFER_ENCODING);
        outputHeaders.remove((CharSequence)HttpHeaderNames.TRAILER);
        if (!isTrailer) {
            outputHeaders.setInt((CharSequence)ExtensionHeaderNames.STREAM_ID.text(), streamId);
            HttpUtil.setKeepAlive(outputHeaders, httpVersion, true);
        }
    }
    
    public static Http2Headers toHttp2Headers(final HttpMessage in, final boolean validateHeaders) {
        final HttpHeaders inHeaders = in.headers();
        final Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
        if (in instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest)in;
            final URI requestTargetUri = URI.create(request.uri());
            out.path((CharSequence)toHttp2Path(requestTargetUri));
            out.method((CharSequence)request.method().asciiName());
            setHttp2Scheme(inHeaders, requestTargetUri, out);
            if (!HttpUtil.isOriginForm(requestTargetUri) && !HttpUtil.isAsteriskForm(requestTargetUri)) {
                final String host = inHeaders.getAsString((CharSequence)HttpHeaderNames.HOST);
                setHttp2Authority((host == null || host.isEmpty()) ? requestTargetUri.getAuthority() : host, out);
            }
        }
        else if (in instanceof HttpResponse) {
            final HttpResponse response = (HttpResponse)in;
            out.status((CharSequence)response.status().codeAsText());
        }
        toHttp2Headers(inHeaders, out);
        return out;
    }
    
    public static Http2Headers toHttp2Headers(final HttpHeaders inHeaders, final boolean validateHeaders) {
        if (inHeaders.isEmpty()) {
            return EmptyHttp2Headers.INSTANCE;
        }
        final Http2Headers out = new DefaultHttp2Headers(validateHeaders, inHeaders.size());
        toHttp2Headers(inHeaders, out);
        return out;
    }
    
    private static CharSequenceMap<AsciiString> toLowercaseMap(final Iterator<? extends CharSequence> valuesIter, final int arraySizeHint) {
        final UnsupportedValueConverter<AsciiString> valueConverter = UnsupportedValueConverter.<AsciiString>instance();
        final CharSequenceMap<AsciiString> result = new CharSequenceMap<AsciiString>(true, valueConverter, arraySizeHint);
        while (valuesIter.hasNext()) {
            final AsciiString lowerCased = AsciiString.of((CharSequence)valuesIter.next()).toLowerCase();
            try {
                int index = lowerCased.forEachByte(ByteProcessor.FIND_COMMA);
                if (index != -1) {
                    int start = 0;
                    do {
                        result.add((CharSequence)lowerCased.subSequence(start, index, false).trim(), AsciiString.EMPTY_STRING);
                        start = index + 1;
                    } while (start < lowerCased.length() && (index = lowerCased.forEachByte(start, lowerCased.length() - start, ByteProcessor.FIND_COMMA)) != -1);
                    result.add((CharSequence)lowerCased.subSequence(start, lowerCased.length(), false).trim(), AsciiString.EMPTY_STRING);
                }
                else {
                    result.add((CharSequence)lowerCased.trim(), AsciiString.EMPTY_STRING);
                }
            }
            catch (Exception e) {
                throw new IllegalStateException((Throwable)e);
            }
        }
        return result;
    }
    
    private static void toHttp2HeadersFilterTE(final Map.Entry<CharSequence, CharSequence> entry, final Http2Headers out) {
        if (AsciiString.indexOf((CharSequence)entry.getValue(), ',', 0) == -1) {
            if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim((CharSequence)entry.getValue()), (CharSequence)HttpHeaderValues.TRAILERS)) {
                ((Headers<CharSequence, CharSequence, Headers>)out).add((CharSequence)HttpHeaderNames.TE, (CharSequence)HttpHeaderValues.TRAILERS);
            }
        }
        else {
            final List<CharSequence> teValues = StringUtil.unescapeCsvFields((CharSequence)entry.getValue());
            for (final CharSequence teValue : teValues) {
                if (AsciiString.contentEqualsIgnoreCase(AsciiString.trim(teValue), (CharSequence)HttpHeaderValues.TRAILERS)) {
                    ((Headers<CharSequence, CharSequence, Headers>)out).add((CharSequence)HttpHeaderNames.TE, (CharSequence)HttpHeaderValues.TRAILERS);
                    break;
                }
            }
        }
    }
    
    public static void toHttp2Headers(final HttpHeaders inHeaders, final Http2Headers out) {
        final Iterator<Map.Entry<CharSequence, CharSequence>> iter = inHeaders.iteratorCharSequence();
        final CharSequenceMap<AsciiString> connectionBlacklist = toLowercaseMap(inHeaders.valueCharSequenceIterator((CharSequence)HttpHeaderNames.CONNECTION), 8);
        while (iter.hasNext()) {
            final Map.Entry<CharSequence, CharSequence> entry = (Map.Entry<CharSequence, CharSequence>)iter.next();
            final AsciiString aName = AsciiString.of((CharSequence)entry.getKey()).toLowerCase();
            if (!HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.contains((CharSequence)aName) && !connectionBlacklist.contains((CharSequence)aName)) {
                if (aName.contentEqualsIgnoreCase((CharSequence)HttpHeaderNames.TE)) {
                    toHttp2HeadersFilterTE(entry, out);
                }
                else if (aName.contentEqualsIgnoreCase((CharSequence)HttpHeaderNames.COOKIE)) {
                    final AsciiString value = AsciiString.of((CharSequence)entry.getValue());
                    try {
                        int index = value.forEachByte(ByteProcessor.FIND_SEMI_COLON);
                        if (index != -1) {
                            int start = 0;
                            do {
                                ((Headers<CharSequence, CharSequence, Headers>)out).add((CharSequence)HttpHeaderNames.COOKIE, (CharSequence)value.subSequence(start, index, false));
                                start = index + 2;
                            } while (start < value.length() && (index = value.forEachByte(start, value.length() - start, ByteProcessor.FIND_SEMI_COLON)) != -1);
                            if (start >= value.length()) {
                                throw new IllegalArgumentException(new StringBuilder().append("cookie value is of unexpected format: ").append(value).toString());
                            }
                            ((Headers<CharSequence, CharSequence, Headers>)out).add((CharSequence)HttpHeaderNames.COOKIE, (CharSequence)value.subSequence(start, value.length(), false));
                        }
                        else {
                            ((Headers<CharSequence, CharSequence, Headers>)out).add((CharSequence)HttpHeaderNames.COOKIE, (CharSequence)value);
                        }
                    }
                    catch (Exception e) {
                        throw new IllegalStateException((Throwable)e);
                    }
                }
                else {
                    ((Headers<CharSequence, CharSequence, Headers>)out).add((CharSequence)aName, (CharSequence)entry.getValue());
                }
            }
        }
    }
    
    private static AsciiString toHttp2Path(final URI uri) {
        final StringBuilder pathBuilder = new StringBuilder(StringUtil.length(uri.getRawPath()) + StringUtil.length(uri.getRawQuery()) + StringUtil.length(uri.getRawFragment()) + 2);
        if (!StringUtil.isNullOrEmpty(uri.getRawPath())) {
            pathBuilder.append(uri.getRawPath());
        }
        if (!StringUtil.isNullOrEmpty(uri.getRawQuery())) {
            pathBuilder.append('?');
            pathBuilder.append(uri.getRawQuery());
        }
        if (!StringUtil.isNullOrEmpty(uri.getRawFragment())) {
            pathBuilder.append('#');
            pathBuilder.append(uri.getRawFragment());
        }
        final String path = pathBuilder.toString();
        return path.isEmpty() ? HttpConversionUtil.EMPTY_REQUEST_PATH : new AsciiString((CharSequence)path);
    }
    
    static void setHttp2Authority(final String authority, final Http2Headers out) {
        if (authority != null) {
            if (authority.isEmpty()) {
                out.authority((CharSequence)AsciiString.EMPTY_STRING);
            }
            else {
                final int start = authority.indexOf(64) + 1;
                final int length = authority.length() - start;
                if (length == 0) {
                    throw new IllegalArgumentException("authority: " + authority);
                }
                out.authority((CharSequence)new AsciiString((CharSequence)authority, start, length));
            }
        }
    }
    
    private static void setHttp2Scheme(final HttpHeaders in, final URI uri, final Http2Headers out) {
        final String value = uri.getScheme();
        if (value != null) {
            out.scheme((CharSequence)new AsciiString((CharSequence)value));
            return;
        }
        final CharSequence cValue = (CharSequence)in.get((CharSequence)ExtensionHeaderNames.SCHEME.text());
        if (cValue != null) {
            out.scheme((CharSequence)AsciiString.of(cValue));
            return;
        }
        if (uri.getPort() == HttpScheme.HTTPS.port()) {
            out.scheme((CharSequence)HttpScheme.HTTPS.name());
        }
        else {
            if (uri.getPort() != HttpScheme.HTTP.port()) {
                throw new IllegalArgumentException(":scheme must be specified. see https://tools.ietf.org/html/rfc7540#section-8.1.2.3");
            }
            out.scheme((CharSequence)HttpScheme.HTTP.name());
        }
    }
    
    static {
        (HTTP_TO_HTTP2_HEADER_BLACKLIST = new CharSequenceMap<AsciiString>()).add(HttpHeaderNames.CONNECTION, AsciiString.EMPTY_STRING);
        final AsciiString keepAlive = HttpHeaderNames.KEEP_ALIVE;
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)keepAlive, AsciiString.EMPTY_STRING);
        final AsciiString proxyConnection = HttpHeaderNames.PROXY_CONNECTION;
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)proxyConnection, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)HttpHeaderNames.TRANSFER_ENCODING, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)HttpHeaderNames.HOST, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)HttpHeaderNames.UPGRADE, AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)ExtensionHeaderNames.STREAM_ID.text(), AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)ExtensionHeaderNames.SCHEME.text(), AsciiString.EMPTY_STRING);
        HttpConversionUtil.HTTP_TO_HTTP2_HEADER_BLACKLIST.add((CharSequence)ExtensionHeaderNames.PATH.text(), AsciiString.EMPTY_STRING);
        OUT_OF_MESSAGE_SEQUENCE_METHOD = HttpMethod.OPTIONS;
        OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE = HttpResponseStatus.OK;
        EMPTY_REQUEST_PATH = AsciiString.cached("/");
    }
    
    public enum ExtensionHeaderNames {
        STREAM_ID("x-http2-stream-id"), 
        SCHEME("x-http2-scheme"), 
        PATH("x-http2-path"), 
        STREAM_PROMISE_ID("x-http2-stream-promise-id"), 
        STREAM_DEPENDENCY_ID("x-http2-stream-dependency-id"), 
        STREAM_WEIGHT("x-http2-stream-weight");
        
        private final AsciiString text;
        
        private ExtensionHeaderNames(final String text) {
            this.text = AsciiString.cached(text);
        }
        
        public AsciiString text() {
            return this.text;
        }
    }
    
    private static final class Http2ToHttpHeaderTranslator {
        private static final CharSequenceMap<AsciiString> REQUEST_HEADER_TRANSLATIONS;
        private static final CharSequenceMap<AsciiString> RESPONSE_HEADER_TRANSLATIONS;
        private final int streamId;
        private final HttpHeaders output;
        private final CharSequenceMap<AsciiString> translations;
        
        Http2ToHttpHeaderTranslator(final int streamId, final HttpHeaders output, final boolean request) {
            this.streamId = streamId;
            this.output = output;
            this.translations = (request ? Http2ToHttpHeaderTranslator.REQUEST_HEADER_TRANSLATIONS : Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS);
        }
        
        public void translate(final Map.Entry<CharSequence, CharSequence> entry) throws Http2Exception {
            final CharSequence name = (CharSequence)entry.getKey();
            final CharSequence value = (CharSequence)entry.getValue();
            final AsciiString translatedName = this.translations.get(name);
            if (translatedName != null) {
                this.output.add((CharSequence)translatedName, AsciiString.of(value));
            }
            else if (!Http2Headers.PseudoHeaderName.isPseudoHeader(name)) {
                if (name.length() == 0 || name.charAt(0) == ':') {
                    throw Http2Exception.streamError(this.streamId, Http2Error.PROTOCOL_ERROR, "Invalid HTTP/2 header '%s' encountered in translation to HTTP/1.x", name);
                }
                if (HttpHeaderNames.COOKIE.equals(name)) {
                    final String existingCookie = this.output.get((CharSequence)HttpHeaderNames.COOKIE);
                    this.output.set((CharSequence)HttpHeaderNames.COOKIE, (existingCookie != null) ? (existingCookie + "; " + value) : value);
                }
                else {
                    this.output.add(name, value);
                }
            }
        }
        
        static {
            REQUEST_HEADER_TRANSLATIONS = new CharSequenceMap<AsciiString>();
            (RESPONSE_HEADER_TRANSLATIONS = new CharSequenceMap<AsciiString>()).add(Http2Headers.PseudoHeaderName.AUTHORITY.value(), HttpHeaderNames.HOST);
            Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS.add((CharSequence)Http2Headers.PseudoHeaderName.SCHEME.value(), ExtensionHeaderNames.SCHEME.text());
            Http2ToHttpHeaderTranslator.REQUEST_HEADER_TRANSLATIONS.add(Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS);
            Http2ToHttpHeaderTranslator.RESPONSE_HEADER_TRANSLATIONS.add((CharSequence)Http2Headers.PseudoHeaderName.PATH.value(), ExtensionHeaderNames.PATH.text());
        }
    }
}
