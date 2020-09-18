package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import io.netty.util.ReferenceCountUtil;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpServerUpgradeHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.logging.InternalLogger;

public class Http2FrameCodec extends Http2ConnectionHandler {
    private static final InternalLogger LOG;
    private final Http2Connection.PropertyKey streamKey;
    private final Http2Connection.PropertyKey upgradeKey;
    private final Integer initialFlowControlWindowSize;
    private ChannelHandlerContext ctx;
    private int numBufferedStreams;
    private DefaultHttp2FrameStream frameStreamToInitialize;
    
    Http2FrameCodec(final Http2ConnectionEncoder encoder, final Http2ConnectionDecoder decoder, final Http2Settings initialSettings) {
        super(decoder, encoder, initialSettings);
        decoder.frameListener(new FrameListener());
        this.connection().addListener(new ConnectionListener());
        this.connection().remote().flowController().listener(new Http2RemoteFlowControllerListener());
        this.streamKey = this.connection().newKey();
        this.upgradeKey = this.connection().newKey();
        this.initialFlowControlWindowSize = initialSettings.initialWindowSize();
    }
    
    DefaultHttp2FrameStream newStream() {
        return new DefaultHttp2FrameStream();
    }
    
    final void forEachActiveStream(final Http2FrameStreamVisitor streamVisitor) throws Http2Exception {
        assert this.ctx.executor().inEventLoop();
        this.connection().forEachActiveStream(new Http2StreamVisitor() {
            public boolean visit(final Http2Stream stream) {
                try {
                    return streamVisitor.visit(stream.<Http2FrameStream>getProperty(Http2FrameCodec.this.streamKey));
                }
                catch (Throwable cause) {
                    Http2FrameCodec.this.onError(Http2FrameCodec.this.ctx, false, cause);
                    return false;
                }
            }
        });
    }
    
    @Override
    public final void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(this.ctx = ctx);
        this.handlerAdded0(ctx);
        final Http2Connection connection = this.connection();
        if (connection.isServer()) {
            this.tryExpandConnectionFlowControlWindow(connection);
        }
    }
    
    private void tryExpandConnectionFlowControlWindow(final Http2Connection connection) throws Http2Exception {
        if (this.initialFlowControlWindowSize != null) {
            final Http2Stream connectionStream = connection.connectionStream();
            final Http2LocalFlowController localFlowController = connection.local().flowController();
            final int delta = this.initialFlowControlWindowSize - localFlowController.initialWindowSize(connectionStream);
            if (delta > 0) {
                localFlowController.incrementWindowSize(connectionStream, Math.max(delta << 1, delta));
                this.flush(this.ctx);
            }
        }
    }
    
    void handlerAdded0(final ChannelHandlerContext ctx) throws Exception {
    }
    
    @Override
    public final void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt == Http2ConnectionPrefaceAndSettingsFrameWrittenEvent.INSTANCE) {
            this.tryExpandConnectionFlowControlWindow(this.connection());
        }
        else if (evt instanceof HttpServerUpgradeHandler.UpgradeEvent) {
            final HttpServerUpgradeHandler.UpgradeEvent upgrade = (HttpServerUpgradeHandler.UpgradeEvent)evt;
            try {
                this.onUpgradeEvent(ctx, upgrade.retain());
                final Http2Stream stream = this.connection().stream(1);
                if (stream.getProperty(this.streamKey) == null) {
                    this.onStreamActive0(stream);
                }
                upgrade.upgradeRequest().headers().setInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), 1);
                stream.<Boolean>setProperty(this.upgradeKey, true);
                InboundHttpToHttp2Adapter.handle(ctx, this.connection(), this.decoder().frameListener(), upgrade.upgradeRequest().retain());
            }
            finally {
                upgrade.release();
            }
            return;
        }
        super.userEventTriggered(ctx, evt);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) {
        if (msg instanceof Http2DataFrame) {
            final Http2DataFrame dataFrame = (Http2DataFrame)msg;
            this.encoder().writeData(ctx, dataFrame.stream().id(), dataFrame.content(), dataFrame.padding(), dataFrame.isEndStream(), promise);
        }
        else if (msg instanceof Http2HeadersFrame) {
            this.writeHeadersFrame(ctx, (Http2HeadersFrame)msg, promise);
        }
        else if (msg instanceof Http2WindowUpdateFrame) {
            final Http2WindowUpdateFrame frame = (Http2WindowUpdateFrame)msg;
            final Http2FrameStream frameStream = frame.stream();
            try {
                if (frameStream == null) {
                    this.increaseInitialConnectionWindow(frame.windowSizeIncrement());
                }
                else {
                    this.consumeBytes(frameStream.id(), frame.windowSizeIncrement());
                }
                promise.setSuccess();
            }
            catch (Throwable t) {
                promise.setFailure(t);
            }
        }
        else if (msg instanceof Http2ResetFrame) {
            final Http2ResetFrame rstFrame = (Http2ResetFrame)msg;
            this.encoder().writeRstStream(ctx, rstFrame.stream().id(), rstFrame.errorCode(), promise);
        }
        else if (msg instanceof Http2PingFrame) {
            final Http2PingFrame frame2 = (Http2PingFrame)msg;
            this.encoder().writePing(ctx, frame2.ack(), frame2.content(), promise);
        }
        else if (msg instanceof Http2SettingsFrame) {
            this.encoder().writeSettings(ctx, ((Http2SettingsFrame)msg).settings(), promise);
        }
        else if (msg instanceof Http2GoAwayFrame) {
            this.writeGoAwayFrame(ctx, (Http2GoAwayFrame)msg, promise);
        }
        else if (msg instanceof Http2UnknownFrame) {
            final Http2UnknownFrame unknownFrame = (Http2UnknownFrame)msg;
            this.encoder().writeFrame(ctx, unknownFrame.frameType(), unknownFrame.stream().id(), unknownFrame.flags(), unknownFrame.content(), promise);
        }
        else {
            if (msg instanceof Http2Frame) {
                ReferenceCountUtil.release(msg);
                throw new UnsupportedMessageTypeException(msg, new Class[0]);
            }
            ctx.write(msg, promise);
        }
    }
    
    private void increaseInitialConnectionWindow(final int deltaBytes) throws Http2Exception {
        this.connection().local().flowController().incrementWindowSize(this.connection().connectionStream(), deltaBytes);
    }
    
    final boolean consumeBytes(final int streamId, final int bytes) throws Http2Exception {
        final Http2Stream stream = this.connection().stream(streamId);
        if (stream != null && streamId == 1) {
            final Boolean upgraded = stream.<Boolean>getProperty(this.upgradeKey);
            if (Boolean.TRUE.equals(upgraded)) {
                return false;
            }
        }
        return this.connection().local().flowController().consumeBytes(stream, bytes);
    }
    
    private void writeGoAwayFrame(final ChannelHandlerContext ctx, final Http2GoAwayFrame frame, final ChannelPromise promise) {
        if (frame.lastStreamId() > -1) {
            frame.release();
            throw new IllegalArgumentException("Last stream id must not be set on GOAWAY frame");
        }
        final int lastStreamCreated = this.connection().remote().lastStreamCreated();
        long lastStreamId = lastStreamCreated + frame.extraStreamIds() * 2L;
        if (lastStreamId > 2147483647L) {
            lastStreamId = 2147483647L;
        }
        this.goAway(ctx, (int)lastStreamId, frame.errorCode(), frame.content(), promise);
    }
    
    private void writeHeadersFrame(final ChannelHandlerContext ctx, final Http2HeadersFrame headersFrame, final ChannelPromise promise) {
        if (Http2CodecUtil.isStreamIdValid(headersFrame.stream().id())) {
            this.encoder().writeHeaders(ctx, headersFrame.stream().id(), headersFrame.headers(), headersFrame.padding(), headersFrame.isEndStream(), promise);
        }
        else {
            final DefaultHttp2FrameStream stream = (DefaultHttp2FrameStream)headersFrame.stream();
            final Http2Connection connection = this.connection();
            final int streamId = connection.local().incrementAndGetNextStreamId();
            if (streamId < 0) {
                promise.setFailure((Throwable)new Http2NoMoreStreamIdsException());
                return;
            }
            stream.id = streamId;
            assert this.frameStreamToInitialize == null;
            this.frameStreamToInitialize = stream;
            final ChannelPromise writePromise = ctx.newPromise();
            this.encoder().writeHeaders(ctx, streamId, headersFrame.headers(), headersFrame.padding(), headersFrame.isEndStream(), writePromise);
            if (writePromise.isDone()) {
                notifyHeaderWritePromise(writePromise, promise);
            }
            else {
                ++this.numBufferedStreams;
                writePromise.addListener(new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        Http2FrameCodec.this.numBufferedStreams--;
                        notifyHeaderWritePromise(future, promise);
                    }
                });
            }
        }
    }
    
    private static void notifyHeaderWritePromise(final ChannelFuture future, final ChannelPromise promise) {
        final Throwable cause = future.cause();
        if (cause == null) {
            promise.setSuccess();
        }
        else {
            promise.setFailure(cause);
        }
    }
    
    private void onStreamActive0(final Http2Stream stream) {
        if (this.connection().local().isValidStreamId(stream.id())) {
            return;
        }
        final DefaultHttp2FrameStream stream2 = this.newStream().setStreamAndProperty(this.streamKey, stream);
        this.onHttp2StreamStateChanged(this.ctx, stream2);
    }
    
    @Override
    protected void onConnectionError(final ChannelHandlerContext ctx, final boolean outbound, final Throwable cause, final Http2Exception http2Ex) {
        if (!outbound) {
            ctx.fireExceptionCaught(cause);
        }
        super.onConnectionError(ctx, outbound, cause, http2Ex);
    }
    
    @Override
    protected final void onStreamError(final ChannelHandlerContext ctx, final boolean outbound, final Throwable cause, final Http2Exception.StreamException streamException) {
        final int streamId = streamException.streamId();
        final Http2Stream connectionStream = this.connection().stream(streamId);
        if (connectionStream == null) {
            this.onHttp2UnknownStreamError(ctx, cause, streamException);
            super.onStreamError(ctx, outbound, cause, streamException);
            return;
        }
        final Http2FrameStream stream = connectionStream.<Http2FrameStream>getProperty(this.streamKey);
        if (stream == null) {
            Http2FrameCodec.LOG.warn("Stream exception thrown without stream object attached.", cause);
            super.onStreamError(ctx, outbound, cause, streamException);
            return;
        }
        if (!outbound) {
            this.onHttp2FrameStreamException(ctx, new Http2FrameStreamException(stream, streamException.error(), cause));
        }
    }
    
    void onHttp2UnknownStreamError(final ChannelHandlerContext ctx, final Throwable cause, final Http2Exception.StreamException streamException) {
        Http2FrameCodec.LOG.warn("Stream exception thrown for unkown stream {}.", streamException.streamId(), cause);
    }
    
    @Override
    protected final boolean isGracefulShutdownComplete() {
        return super.isGracefulShutdownComplete() && this.numBufferedStreams == 0;
    }
    
    void onUpgradeEvent(final ChannelHandlerContext ctx, final HttpServerUpgradeHandler.UpgradeEvent evt) {
        ctx.fireUserEventTriggered(evt);
    }
    
    void onHttp2StreamWritabilityChanged(final ChannelHandlerContext ctx, final Http2FrameStream stream, final boolean writable) {
        ctx.fireUserEventTriggered(Http2FrameStreamEvent.writabilityChanged(stream));
    }
    
    void onHttp2StreamStateChanged(final ChannelHandlerContext ctx, final Http2FrameStream stream) {
        ctx.fireUserEventTriggered(Http2FrameStreamEvent.stateChanged(stream));
    }
    
    void onHttp2Frame(final ChannelHandlerContext ctx, final Http2Frame frame) {
        ctx.fireChannelRead(frame);
    }
    
    void onHttp2FrameStreamException(final ChannelHandlerContext ctx, final Http2FrameStreamException cause) {
        ctx.fireExceptionCaught((Throwable)cause);
    }
    
    final boolean isWritable(final DefaultHttp2FrameStream stream) {
        final Http2Stream s = stream.stream;
        return s != null && this.connection().remote().flowController().isWritable(s);
    }
    
    static {
        LOG = InternalLoggerFactory.getInstance(Http2FrameCodec.class);
    }
    
    private final class ConnectionListener extends Http2ConnectionAdapter {
        @Override
        public void onStreamAdded(final Http2Stream stream) {
            if (Http2FrameCodec.this.frameStreamToInitialize != null && stream.id() == Http2FrameCodec.this.frameStreamToInitialize.id()) {
                Http2FrameCodec.this.frameStreamToInitialize.setStreamAndProperty(Http2FrameCodec.this.streamKey, stream);
                Http2FrameCodec.this.frameStreamToInitialize = null;
            }
        }
        
        @Override
        public void onStreamActive(final Http2Stream stream) {
            Http2FrameCodec.this.onStreamActive0(stream);
        }
        
        @Override
        public void onStreamClosed(final Http2Stream stream) {
            final DefaultHttp2FrameStream stream2 = stream.<DefaultHttp2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
            if (stream2 != null) {
                Http2FrameCodec.this.onHttp2StreamStateChanged(Http2FrameCodec.this.ctx, stream2);
            }
        }
        
        @Override
        public void onStreamHalfClosed(final Http2Stream stream) {
            final DefaultHttp2FrameStream stream2 = stream.<DefaultHttp2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
            if (stream2 != null) {
                Http2FrameCodec.this.onHttp2StreamStateChanged(Http2FrameCodec.this.ctx, stream2);
            }
        }
    }
    
    private final class FrameListener implements Http2FrameListener {
        public void onUnknownFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2UnknownFrame(frameType, flags, payload).stream(this.requireStream(streamId)).retain());
        }
        
        public void onSettingsRead(final ChannelHandlerContext ctx, final Http2Settings settings) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2SettingsFrame(settings));
        }
        
        public void onPingRead(final ChannelHandlerContext ctx, final long data) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2PingFrame(data, false));
        }
        
        public void onPingAckRead(final ChannelHandlerContext ctx, final long data) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2PingFrame(data, true));
        }
        
        public void onRstStreamRead(final ChannelHandlerContext ctx, final int streamId, final long errorCode) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2ResetFrame(errorCode).stream(this.requireStream(streamId)));
        }
        
        public void onWindowUpdateRead(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement) {
            if (streamId == 0) {
                return;
            }
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2WindowUpdateFrame(windowSizeIncrement).stream(this.requireStream(streamId)));
        }
        
        public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream) {
            this.onHeadersRead(ctx, streamId, headers, padding, endStream);
        }
        
        public void onHeadersRead(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endOfStream) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2HeadersFrame(headers, endOfStream, padding).stream(this.requireStream(streamId)));
        }
        
        public int onDataRead(final ChannelHandlerContext ctx, final int streamId, final ByteBuf data, final int padding, final boolean endOfStream) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2DataFrame(data, endOfStream, padding).stream(this.requireStream(streamId)).retain());
            return 0;
        }
        
        public void onGoAwayRead(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData) {
            Http2FrameCodec.this.onHttp2Frame(ctx, new DefaultHttp2GoAwayFrame(lastStreamId, errorCode, debugData).retain());
        }
        
        public void onPriorityRead(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive) {
        }
        
        public void onSettingsAckRead(final ChannelHandlerContext ctx) {
        }
        
        public void onPushPromiseRead(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding) {
        }
        
        private Http2FrameStream requireStream(final int streamId) {
            final Http2FrameStream stream = Http2FrameCodec.this.connection().stream(streamId).<Http2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
            if (stream == null) {
                throw new IllegalStateException(new StringBuilder().append("Stream object required for identifier: ").append(streamId).toString());
            }
            return stream;
        }
    }
    
    private final class Http2RemoteFlowControllerListener implements Http2RemoteFlowController.Listener {
        public void writabilityChanged(final Http2Stream stream) {
            final Http2FrameStream frameStream = stream.<Http2FrameStream>getProperty(Http2FrameCodec.this.streamKey);
            if (frameStream == null) {
                return;
            }
            Http2FrameCodec.this.onHttp2StreamWritabilityChanged(Http2FrameCodec.this.ctx, frameStream, Http2FrameCodec.this.connection().remote().flowController().isWritable(stream));
        }
    }
    
    static class DefaultHttp2FrameStream implements Http2FrameStream {
        private volatile int id;
        volatile Http2Stream stream;
        
        DefaultHttp2FrameStream() {
            this.id = -1;
        }
        
        DefaultHttp2FrameStream setStreamAndProperty(final Http2Connection.PropertyKey streamKey, final Http2Stream stream) {
            assert stream.id() == this.id;
            (this.stream = stream).<DefaultHttp2FrameStream>setProperty(streamKey, this);
            return this;
        }
        
        public int id() {
            final Http2Stream stream = this.stream;
            return (stream == null) ? this.id : stream.id();
        }
        
        public Http2Stream.State state() {
            final Http2Stream stream = this.stream;
            return (stream == null) ? Http2Stream.State.IDLE : stream.state();
        }
        
        public String toString() {
            return String.valueOf(this.id());
        }
    }
}
