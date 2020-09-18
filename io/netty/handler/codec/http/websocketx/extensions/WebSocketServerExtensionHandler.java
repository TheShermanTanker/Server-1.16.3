package io.netty.handler.codec.http.websocketx.extensions;

import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.channel.ChannelPromise;
import java.util.Iterator;
import java.util.ArrayList;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.channel.ChannelHandlerContext;
import java.util.Arrays;
import java.util.List;
import io.netty.channel.ChannelDuplexHandler;

public class WebSocketServerExtensionHandler extends ChannelDuplexHandler {
    private final List<WebSocketServerExtensionHandshaker> extensionHandshakers;
    private List<WebSocketServerExtension> validExtensions;
    
    public WebSocketServerExtensionHandler(final WebSocketServerExtensionHandshaker... extensionHandshakers) {
        if (extensionHandshakers == null) {
            throw new NullPointerException("extensionHandshakers");
        }
        if (extensionHandshakers.length == 0) {
            throw new IllegalArgumentException("extensionHandshakers must contains at least one handshaker");
        }
        this.extensionHandshakers = (List<WebSocketServerExtensionHandshaker>)Arrays.asList((Object[])extensionHandshakers);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            final HttpRequest request = (HttpRequest)msg;
            if (WebSocketExtensionUtil.isWebsocketUpgrade(request.headers())) {
                final String extensionsHeader = request.headers().getAsString((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
                if (extensionsHeader != null) {
                    final List<WebSocketExtensionData> extensions = WebSocketExtensionUtil.extractExtensions(extensionsHeader);
                    int rsv = 0;
                    for (final WebSocketExtensionData extensionData : extensions) {
                        Iterator<WebSocketServerExtensionHandshaker> extensionHandshakersIterator;
                        WebSocketServerExtension validExtension;
                        WebSocketServerExtensionHandshaker extensionHandshaker;
                        for (extensionHandshakersIterator = (Iterator<WebSocketServerExtensionHandshaker>)this.extensionHandshakers.iterator(), validExtension = null; validExtension == null && extensionHandshakersIterator.hasNext(); validExtension = extensionHandshaker.handshakeExtension(extensionData)) {
                            extensionHandshaker = (WebSocketServerExtensionHandshaker)extensionHandshakersIterator.next();
                        }
                        if (validExtension != null && (validExtension.rsv() & rsv) == 0x0) {
                            if (this.validExtensions == null) {
                                this.validExtensions = (List<WebSocketServerExtension>)new ArrayList(1);
                            }
                            rsv |= validExtension.rsv();
                            this.validExtensions.add(validExtension);
                        }
                    }
                }
            }
        }
        super.channelRead(ctx, msg);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof HttpResponse && WebSocketExtensionUtil.isWebsocketUpgrade(((HttpResponse)msg).headers()) && this.validExtensions != null) {
            final HttpResponse response = (HttpResponse)msg;
            String headerValue = response.headers().getAsString((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS);
            for (final WebSocketServerExtension extension : this.validExtensions) {
                final WebSocketExtensionData extensionData = extension.newReponseData();
                headerValue = WebSocketExtensionUtil.appendExtension(headerValue, extensionData.name(), extensionData.parameters());
            }
            promise.addListener(new ChannelFutureListener() {
                public void operationComplete(final ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        for (final WebSocketServerExtension extension : WebSocketServerExtensionHandler.this.validExtensions) {
                            final WebSocketExtensionDecoder decoder = extension.newExtensionDecoder();
                            final WebSocketExtensionEncoder encoder = extension.newExtensionEncoder();
                            ctx.pipeline().addAfter(ctx.name(), decoder.getClass().getName(), decoder);
                            ctx.pipeline().addAfter(ctx.name(), encoder.getClass().getName(), encoder);
                        }
                    }
                    ctx.pipeline().remove(ctx.name());
                }
            });
            if (headerValue != null) {
                response.headers().set((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_EXTENSIONS, headerValue);
            }
        }
        super.write(ctx, msg, promise);
    }
}
