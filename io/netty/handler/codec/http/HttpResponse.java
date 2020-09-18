package io.netty.handler.codec.http;

public interface HttpResponse extends HttpMessage {
    @Deprecated
    HttpResponseStatus getStatus();
    
    HttpResponseStatus status();
    
    HttpResponse setStatus(final HttpResponseStatus httpResponseStatus);
    
    HttpResponse setProtocolVersion(final HttpVersion httpVersion);
}
