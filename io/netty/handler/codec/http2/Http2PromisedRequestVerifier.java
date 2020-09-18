package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public interface Http2PromisedRequestVerifier {
    public static final Http2PromisedRequestVerifier ALWAYS_VERIFY = new Http2PromisedRequestVerifier() {
        public boolean isAuthoritative(final ChannelHandlerContext ctx, final Http2Headers headers) {
            return true;
        }
        
        public boolean isCacheable(final Http2Headers headers) {
            return true;
        }
        
        public boolean isSafe(final Http2Headers headers) {
            return true;
        }
    };
    
    boolean isAuthoritative(final ChannelHandlerContext channelHandlerContext, final Http2Headers http2Headers);
    
    boolean isCacheable(final Http2Headers http2Headers);
    
    boolean isSafe(final Http2Headers http2Headers);
}
