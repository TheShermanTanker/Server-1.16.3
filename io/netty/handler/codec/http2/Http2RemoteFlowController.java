package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public interface Http2RemoteFlowController extends Http2FlowController {
    ChannelHandlerContext channelHandlerContext();
    
    void addFlowControlled(final Http2Stream http2Stream, final FlowControlled flowControlled);
    
    boolean hasFlowControlled(final Http2Stream http2Stream);
    
    void writePendingBytes() throws Http2Exception;
    
    void listener(final Listener listener);
    
    boolean isWritable(final Http2Stream http2Stream);
    
    void channelWritabilityChanged() throws Http2Exception;
    
    void updateDependencyTree(final int integer1, final int integer2, final short short3, final boolean boolean4);
    
    public interface Listener {
        void writabilityChanged(final Http2Stream http2Stream);
    }
    
    public interface FlowControlled {
        int size();
        
        void error(final ChannelHandlerContext channelHandlerContext, final Throwable throwable);
        
        void writeComplete();
        
        void write(final ChannelHandlerContext channelHandlerContext, final int integer);
        
        boolean merge(final ChannelHandlerContext channelHandlerContext, final FlowControlled flowControlled);
    }
}
