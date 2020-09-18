package io.netty.handler.ssl.ocsp;

import io.netty.util.internal.ThrowableUtil;
import io.netty.channel.ChannelHandler;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.handler.ssl.ReferenceCountedOpenSslEngine;
import javax.net.ssl.SSLHandshakeException;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class OcspClientHandler extends ChannelInboundHandlerAdapter {
    private static final SSLHandshakeException OCSP_VERIFICATION_EXCEPTION;
    private final ReferenceCountedOpenSslEngine engine;
    
    protected OcspClientHandler(final ReferenceCountedOpenSslEngine engine) {
        this.engine = ObjectUtil.<ReferenceCountedOpenSslEngine>checkNotNull(engine, "engine");
    }
    
    protected abstract boolean verify(final ChannelHandlerContext channelHandlerContext, final ReferenceCountedOpenSslEngine referenceCountedOpenSslEngine) throws Exception;
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt instanceof SslHandshakeCompletionEvent) {
            ctx.pipeline().remove(this);
            final SslHandshakeCompletionEvent event = (SslHandshakeCompletionEvent)evt;
            if (event.isSuccess() && !this.verify(ctx, this.engine)) {
                throw OcspClientHandler.OCSP_VERIFICATION_EXCEPTION;
            }
        }
        ctx.fireUserEventTriggered(evt);
    }
    
    static {
        OCSP_VERIFICATION_EXCEPTION = ThrowableUtil.<SSLHandshakeException>unknownStackTrace(new SSLHandshakeException("Bad OCSP response"), OcspClientHandler.class, "verify(...)");
    }
}
