package io.netty.handler.codec.http2;

public interface Http2Stream {
    int id();
    
    State state();
    
    Http2Stream open(final boolean boolean1) throws Http2Exception;
    
    Http2Stream close();
    
    Http2Stream closeLocalSide();
    
    Http2Stream closeRemoteSide();
    
    boolean isResetSent();
    
    Http2Stream resetSent();
    
     <V> V setProperty(final Http2Connection.PropertyKey propertyKey, final V object);
    
     <V> V getProperty(final Http2Connection.PropertyKey propertyKey);
    
     <V> V removeProperty(final Http2Connection.PropertyKey propertyKey);
    
    Http2Stream headersSent(final boolean boolean1);
    
    boolean isHeadersSent();
    
    boolean isTrailersSent();
    
    Http2Stream headersReceived(final boolean boolean1);
    
    boolean isHeadersReceived();
    
    boolean isTrailersReceived();
    
    Http2Stream pushPromiseSent();
    
    boolean isPushPromiseSent();
    
    public enum State {
        IDLE(false, false), 
        RESERVED_LOCAL(false, false), 
        RESERVED_REMOTE(false, false), 
        OPEN(true, true), 
        HALF_CLOSED_LOCAL(false, true), 
        HALF_CLOSED_REMOTE(true, false), 
        CLOSED(false, false);
        
        private final boolean localSideOpen;
        private final boolean remoteSideOpen;
        
        private State(final boolean localSideOpen, final boolean remoteSideOpen) {
            this.localSideOpen = localSideOpen;
            this.remoteSideOpen = remoteSideOpen;
        }
        
        public boolean localSideOpen() {
            return this.localSideOpen;
        }
        
        public boolean remoteSideOpen() {
            return this.remoteSideOpen;
        }
    }
}
