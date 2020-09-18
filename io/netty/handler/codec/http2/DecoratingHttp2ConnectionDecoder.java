package io.netty.handler.codec.http2;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;

public class DecoratingHttp2ConnectionDecoder implements Http2ConnectionDecoder {
    private final Http2ConnectionDecoder delegate;
    
    public DecoratingHttp2ConnectionDecoder(final Http2ConnectionDecoder delegate) {
        this.delegate = ObjectUtil.<Http2ConnectionDecoder>checkNotNull(delegate, "delegate");
    }
    
    public void lifecycleManager(final Http2LifecycleManager lifecycleManager) {
        this.delegate.lifecycleManager(lifecycleManager);
    }
    
    public Http2Connection connection() {
        return this.delegate.connection();
    }
    
    public Http2LocalFlowController flowController() {
        return this.delegate.flowController();
    }
    
    public void frameListener(final Http2FrameListener listener) {
        this.delegate.frameListener(listener);
    }
    
    public Http2FrameListener frameListener() {
        return this.delegate.frameListener();
    }
    
    public void decodeFrame(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Http2Exception {
        this.delegate.decodeFrame(ctx, in, out);
    }
    
    public Http2Settings localSettings() {
        return this.delegate.localSettings();
    }
    
    public boolean prefaceReceived() {
        return this.delegate.prefaceReceived();
    }
    
    public void close() {
        this.delegate.close();
    }
}
