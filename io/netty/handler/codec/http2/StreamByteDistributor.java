package io.netty.handler.codec.http2;

public interface StreamByteDistributor {
    void updateStreamableBytes(final StreamState streamState);
    
    void updateDependencyTree(final int integer1, final int integer2, final short short3, final boolean boolean4);
    
    boolean distribute(final int integer, final Writer writer) throws Http2Exception;
    
    public interface Writer {
        void write(final Http2Stream http2Stream, final int integer);
    }
    
    public interface StreamState {
        Http2Stream stream();
        
        long pendingBytes();
        
        boolean hasFrame();
        
        int windowSize();
    }
}
