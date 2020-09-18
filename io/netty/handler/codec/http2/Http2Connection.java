package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface Http2Connection {
    Future<Void> close(final Promise<Void> promise);
    
    PropertyKey newKey();
    
    void addListener(final Listener listener);
    
    void removeListener(final Listener listener);
    
    Http2Stream stream(final int integer);
    
    boolean streamMayHaveExisted(final int integer);
    
    Http2Stream connectionStream();
    
    int numActiveStreams();
    
    Http2Stream forEachActiveStream(final Http2StreamVisitor http2StreamVisitor) throws Http2Exception;
    
    boolean isServer();
    
    Endpoint<Http2LocalFlowController> local();
    
    Endpoint<Http2RemoteFlowController> remote();
    
    boolean goAwayReceived();
    
    void goAwayReceived(final int integer, final long long2, final ByteBuf byteBuf);
    
    boolean goAwaySent();
    
    void goAwaySent(final int integer, final long long2, final ByteBuf byteBuf);
    
    public interface PropertyKey {
    }
    
    public interface Endpoint<F extends Http2FlowController> {
        int incrementAndGetNextStreamId();
        
        boolean isValidStreamId(final int integer);
        
        boolean mayHaveCreatedStream(final int integer);
        
        boolean created(final Http2Stream http2Stream);
        
        boolean canOpenStream();
        
        Http2Stream createStream(final int integer, final boolean boolean2) throws Http2Exception;
        
        Http2Stream reservePushStream(final int integer, final Http2Stream http2Stream) throws Http2Exception;
        
        boolean isServer();
        
        void allowPushTo(final boolean boolean1);
        
        boolean allowPushTo();
        
        int numActiveStreams();
        
        int maxActiveStreams();
        
        void maxActiveStreams(final int integer);
        
        int lastStreamCreated();
        
        int lastStreamKnownByPeer();
        
        F flowController();
        
        void flowController(final F http2FlowController);
        
        Endpoint<? extends Http2FlowController> opposite();
    }
    
    public interface Listener {
        void onStreamAdded(final Http2Stream http2Stream);
        
        void onStreamActive(final Http2Stream http2Stream);
        
        void onStreamHalfClosed(final Http2Stream http2Stream);
        
        void onStreamClosed(final Http2Stream http2Stream);
        
        void onStreamRemoved(final Http2Stream http2Stream);
        
        void onGoAwaySent(final int integer, final long long2, final ByteBuf byteBuf);
        
        void onGoAwayReceived(final int integer, final long long2, final ByteBuf byteBuf);
    }
}
