package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2HeadersDecoder {
    Http2Headers decodeHeaders(final int integer, final ByteBuf byteBuf) throws Http2Exception;
    
    Configuration configuration();
    
    public interface Configuration {
        void maxHeaderTableSize(final long long1) throws Http2Exception;
        
        long maxHeaderTableSize();
        
        void maxHeaderListSize(final long long1, final long long2) throws Http2Exception;
        
        long maxHeaderListSize();
        
        long maxHeaderListSizeGoAway();
    }
}
