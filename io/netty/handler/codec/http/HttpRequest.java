package io.netty.handler.codec.http;

public interface HttpRequest extends HttpMessage {
    @Deprecated
    HttpMethod getMethod();
    
    HttpMethod method();
    
    HttpRequest setMethod(final HttpMethod httpMethod);
    
    @Deprecated
    String getUri();
    
    String uri();
    
    HttpRequest setUri(final String string);
    
    HttpRequest setProtocolVersion(final HttpVersion httpVersion);
}
