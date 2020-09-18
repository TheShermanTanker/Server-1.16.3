package io.netty.handler.codec.http2;

import io.netty.buffer.Unpooled;
import io.netty.util.internal.PlatformDependent;
import java.util.Iterator;
import io.netty.util.collection.CharObjectMap;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;

public class DefaultHttp2FrameWriter implements Http2FrameWriter, Http2FrameSizePolicy, Configuration {
    private static final String STREAM_ID = "Stream ID";
    private static final String STREAM_DEPENDENCY = "Stream Dependency";
    private static final ByteBuf ZERO_BUFFER;
    private final Http2HeadersEncoder headersEncoder;
    private int maxFrameSize;
    
    public DefaultHttp2FrameWriter() {
        this(new DefaultHttp2HeadersEncoder());
    }
    
    public DefaultHttp2FrameWriter(final Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector) {
        this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector));
    }
    
    public DefaultHttp2FrameWriter(final Http2HeadersEncoder.SensitivityDetector headersSensitivityDetector, final boolean ignoreMaxHeaderListSize) {
        this(new DefaultHttp2HeadersEncoder(headersSensitivityDetector, ignoreMaxHeaderListSize));
    }
    
    public DefaultHttp2FrameWriter(final Http2HeadersEncoder headersEncoder) {
        this.headersEncoder = headersEncoder;
        this.maxFrameSize = 16384;
    }
    
    public Configuration configuration() {
        return this;
    }
    
    public Http2HeadersEncoder.Configuration headersConfiguration() {
        return this.headersEncoder.configuration();
    }
    
    public Http2FrameSizePolicy frameSizePolicy() {
        return this;
    }
    
    public void maxFrameSize(final int max) throws Http2Exception {
        if (!Http2CodecUtil.isMaxFrameSizeValid(max)) {
            throw Http2Exception.connectionError(Http2Error.FRAME_SIZE_ERROR, "Invalid MAX_FRAME_SIZE specified in sent settings: %d", max);
        }
        this.maxFrameSize = max;
    }
    
    public int maxFrameSize() {
        return this.maxFrameSize;
    }
    
    public void close() {
    }
    
    public ChannelFuture writeData(final ChannelHandlerContext ctx, final int streamId, ByteBuf data, int padding, final boolean endStream, final ChannelPromise promise) {
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        ByteBuf frameHeader = null;
        try {
            verifyStreamId(streamId, "Stream ID");
            Http2CodecUtil.verifyPadding(padding);
            int remainingData = data.readableBytes();
            final Http2Flags flags = new Http2Flags();
            flags.endOfStream(false);
            flags.paddingPresent(false);
            if (remainingData > this.maxFrameSize) {
                frameHeader = ctx.alloc().buffer(9);
                Http2CodecUtil.writeFrameHeaderInternal(frameHeader, this.maxFrameSize, (byte)0, flags, streamId);
                do {
                    ctx.write(frameHeader.retainedSlice(), promiseAggregator.newPromise());
                    ctx.write(data.readRetainedSlice(this.maxFrameSize), promiseAggregator.newPromise());
                    remainingData -= this.maxFrameSize;
                } while (remainingData > this.maxFrameSize);
            }
            if (padding == 0) {
                if (frameHeader != null) {
                    frameHeader.release();
                    frameHeader = null;
                }
                final ByteBuf frameHeader2 = ctx.alloc().buffer(9);
                flags.endOfStream(endStream);
                Http2CodecUtil.writeFrameHeaderInternal(frameHeader2, remainingData, (byte)0, flags, streamId);
                ctx.write(frameHeader2, promiseAggregator.newPromise());
                final ByteBuf lastFrame = data.readSlice(remainingData);
                data = null;
                ctx.write(lastFrame, promiseAggregator.newPromise());
            }
            else {
                if (remainingData != this.maxFrameSize) {
                    if (frameHeader != null) {
                        frameHeader.release();
                        frameHeader = null;
                    }
                }
                else {
                    remainingData -= this.maxFrameSize;
                    ByteBuf lastFrame2;
                    if (frameHeader == null) {
                        lastFrame2 = ctx.alloc().buffer(9);
                        Http2CodecUtil.writeFrameHeaderInternal(lastFrame2, this.maxFrameSize, (byte)0, flags, streamId);
                    }
                    else {
                        lastFrame2 = frameHeader.slice();
                        frameHeader = null;
                    }
                    ctx.write(lastFrame2, promiseAggregator.newPromise());
                    lastFrame2 = data.readSlice(this.maxFrameSize);
                    data = null;
                    ctx.write(lastFrame2, promiseAggregator.newPromise());
                }
                do {
                    final int frameDataBytes = Math.min(remainingData, this.maxFrameSize);
                    final int framePaddingBytes = Math.min(padding, Math.max(0, this.maxFrameSize - 1 - frameDataBytes));
                    padding -= framePaddingBytes;
                    remainingData -= frameDataBytes;
                    final ByteBuf frameHeader3 = ctx.alloc().buffer(10);
                    flags.endOfStream(endStream && remainingData == 0 && padding == 0);
                    flags.paddingPresent(framePaddingBytes > 0);
                    Http2CodecUtil.writeFrameHeaderInternal(frameHeader3, framePaddingBytes + frameDataBytes, (byte)0, flags, streamId);
                    writePaddingLength(frameHeader3, framePaddingBytes);
                    ctx.write(frameHeader3, promiseAggregator.newPromise());
                    if (frameDataBytes != 0) {
                        if (remainingData == 0) {
                            final ByteBuf lastFrame3 = data.readSlice(frameDataBytes);
                            data = null;
                            ctx.write(lastFrame3, promiseAggregator.newPromise());
                        }
                        else {
                            ctx.write(data.readRetainedSlice(frameDataBytes), promiseAggregator.newPromise());
                        }
                    }
                    if (paddingBytes(framePaddingBytes) > 0) {
                        ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(framePaddingBytes)), promiseAggregator.newPromise());
                    }
                } while (remainingData != 0 || padding != 0);
            }
        }
        catch (Throwable cause) {
            if (frameHeader != null) {
                frameHeader.release();
            }
            try {
                if (data != null) {
                    data.release();
                }
            }
            finally {
                promiseAggregator.setFailure(cause);
                promiseAggregator.doneAllocatingPromises();
            }
            return promiseAggregator;
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.writeHeadersInternal(ctx, streamId, headers, padding, endStream, false, 0, (short)0, false, promise);
    }
    
    public ChannelFuture writeHeaders(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int streamDependency, final short weight, final boolean exclusive, final int padding, final boolean endStream, final ChannelPromise promise) {
        return this.writeHeadersInternal(ctx, streamId, headers, padding, endStream, true, streamDependency, weight, exclusive, promise);
    }
    
    public ChannelFuture writePriority(final ChannelHandlerContext ctx, final int streamId, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        try {
            verifyStreamId(streamId, "Stream ID");
            verifyStreamId(streamDependency, "Stream Dependency");
            verifyWeight(weight);
            final ByteBuf buf = ctx.alloc().buffer(14);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 5, (byte)2, new Http2Flags(), streamId);
            buf.writeInt(exclusive ? ((int)(0x80000000L | (long)streamDependency)) : streamDependency);
            buf.writeByte(weight - 1);
            return ctx.write(buf, promise);
        }
        catch (Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    public ChannelFuture writeRstStream(final ChannelHandlerContext ctx, final int streamId, final long errorCode, final ChannelPromise promise) {
        try {
            verifyStreamId(streamId, "Stream ID");
            verifyErrorCode(errorCode);
            final ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)3, new Http2Flags(), streamId);
            buf.writeInt((int)errorCode);
            return ctx.write(buf, promise);
        }
        catch (Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    public ChannelFuture writeSettings(final ChannelHandlerContext ctx, final Http2Settings settings, final ChannelPromise promise) {
        try {
            ObjectUtil.<Http2Settings>checkNotNull(settings, "settings");
            final int payloadLength = 6 * settings.size();
            final ByteBuf buf = ctx.alloc().buffer(9 + settings.size() * 6);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)4, new Http2Flags(), 0);
            for (final CharObjectMap.PrimitiveEntry<Long> entry : settings.entries()) {
                buf.writeChar(entry.key());
                buf.writeInt(entry.value().intValue());
            }
            return ctx.write(buf, promise);
        }
        catch (Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    public ChannelFuture writeSettingsAck(final ChannelHandlerContext ctx, final ChannelPromise promise) {
        try {
            final ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 0, (byte)4, new Http2Flags().ack(true), 0);
            return ctx.write(buf, promise);
        }
        catch (Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    public ChannelFuture writePing(final ChannelHandlerContext ctx, final boolean ack, final long data, final ChannelPromise promise) {
        final Http2Flags flags = ack ? new Http2Flags().ack(true) : new Http2Flags();
        final ByteBuf buf = ctx.alloc().buffer(17);
        Http2CodecUtil.writeFrameHeaderInternal(buf, 8, (byte)6, flags, 0);
        buf.writeLong(data);
        return ctx.write(buf, promise);
    }
    
    public ChannelFuture writePushPromise(final ChannelHandlerContext ctx, final int streamId, final int promisedStreamId, final Http2Headers headers, final int padding, final ChannelPromise promise) {
        ByteBuf headerBlock = null;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamId(streamId, "Stream ID");
            verifyStreamId(promisedStreamId, "Promised Stream ID");
            Http2CodecUtil.verifyPadding(padding);
            headerBlock = ctx.alloc().buffer();
            this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
            final Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
            final int nonFragmentLength = 4 + padding;
            final int maxFragmentLength = this.maxFrameSize - nonFragmentLength;
            final ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
            flags.endOfHeaders(!headerBlock.isReadable());
            final int payloadLength = fragment.readableBytes() + nonFragmentLength;
            final ByteBuf buf = ctx.alloc().buffer(14);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)5, flags, streamId);
            writePaddingLength(buf, padding);
            buf.writeInt(promisedStreamId);
            ctx.write(buf, promiseAggregator.newPromise());
            ctx.write(fragment, promiseAggregator.newPromise());
            if (paddingBytes(padding) > 0) {
                ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
            }
            if (!flags.endOfHeaders()) {
                this.writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
            }
        }
        catch (Http2Exception e) {
            promiseAggregator.setFailure((Throwable)e);
        }
        catch (Throwable t) {
            promiseAggregator.setFailure(t);
            promiseAggregator.doneAllocatingPromises();
            PlatformDependent.throwException(t);
        }
        finally {
            if (headerBlock != null) {
                headerBlock.release();
            }
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    public ChannelFuture writeGoAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode, final ByteBuf debugData, final ChannelPromise promise) {
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(lastStreamId, "Last Stream ID");
            verifyErrorCode(errorCode);
            final int payloadLength = 8 + debugData.readableBytes();
            final ByteBuf buf = ctx.alloc().buffer(17);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)7, new Http2Flags(), 0);
            buf.writeInt(lastStreamId);
            buf.writeInt((int)errorCode);
            ctx.write(buf, promiseAggregator.newPromise());
        }
        catch (Throwable t) {
            try {
                debugData.release();
            }
            finally {
                promiseAggregator.setFailure(t);
                promiseAggregator.doneAllocatingPromises();
            }
            return promiseAggregator;
        }
        try {
            ctx.write(debugData, promiseAggregator.newPromise());
        }
        catch (Throwable t) {
            promiseAggregator.setFailure(t);
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    public ChannelFuture writeWindowUpdate(final ChannelHandlerContext ctx, final int streamId, final int windowSizeIncrement, final ChannelPromise promise) {
        try {
            verifyStreamOrConnectionId(streamId, "Stream ID");
            verifyWindowSizeIncrement(windowSizeIncrement);
            final ByteBuf buf = ctx.alloc().buffer(13);
            Http2CodecUtil.writeFrameHeaderInternal(buf, 4, (byte)8, new Http2Flags(), streamId);
            buf.writeInt(windowSizeIncrement);
            return ctx.write(buf, promise);
        }
        catch (Throwable t) {
            return promise.setFailure(t);
        }
    }
    
    public ChannelFuture writeFrame(final ChannelHandlerContext ctx, final byte frameType, final int streamId, final Http2Flags flags, final ByteBuf payload, final ChannelPromise promise) {
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamOrConnectionId(streamId, "Stream ID");
            final ByteBuf buf = ctx.alloc().buffer(9);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payload.readableBytes(), frameType, flags, streamId);
            ctx.write(buf, promiseAggregator.newPromise());
        }
        catch (Throwable t) {
            try {
                payload.release();
            }
            finally {
                promiseAggregator.setFailure(t);
                promiseAggregator.doneAllocatingPromises();
            }
            return promiseAggregator;
        }
        try {
            ctx.write(payload, promiseAggregator.newPromise());
        }
        catch (Throwable t) {
            promiseAggregator.setFailure(t);
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    private ChannelFuture writeHeadersInternal(final ChannelHandlerContext ctx, final int streamId, final Http2Headers headers, final int padding, final boolean endStream, final boolean hasPriority, final int streamDependency, final short weight, final boolean exclusive, final ChannelPromise promise) {
        ByteBuf headerBlock = null;
        final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
        try {
            verifyStreamId(streamId, "Stream ID");
            if (hasPriority) {
                verifyStreamOrConnectionId(streamDependency, "Stream Dependency");
                Http2CodecUtil.verifyPadding(padding);
                verifyWeight(weight);
            }
            headerBlock = ctx.alloc().buffer();
            this.headersEncoder.encodeHeaders(streamId, headers, headerBlock);
            final Http2Flags flags = new Http2Flags().endOfStream(endStream).priorityPresent(hasPriority).paddingPresent(padding > 0);
            final int nonFragmentBytes = padding + flags.getNumPriorityBytes();
            final int maxFragmentLength = this.maxFrameSize - nonFragmentBytes;
            final ByteBuf fragment = headerBlock.readRetainedSlice(Math.min(headerBlock.readableBytes(), maxFragmentLength));
            flags.endOfHeaders(!headerBlock.isReadable());
            final int payloadLength = fragment.readableBytes() + nonFragmentBytes;
            final ByteBuf buf = ctx.alloc().buffer(15);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)1, flags, streamId);
            writePaddingLength(buf, padding);
            if (hasPriority) {
                buf.writeInt(exclusive ? ((int)(0x80000000L | (long)streamDependency)) : streamDependency);
                buf.writeByte(weight - 1);
            }
            ctx.write(buf, promiseAggregator.newPromise());
            ctx.write(fragment, promiseAggregator.newPromise());
            if (paddingBytes(padding) > 0) {
                ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
            }
            if (!flags.endOfHeaders()) {
                this.writeContinuationFrames(ctx, streamId, headerBlock, padding, promiseAggregator);
            }
        }
        catch (Http2Exception e) {
            promiseAggregator.setFailure((Throwable)e);
        }
        catch (Throwable t) {
            promiseAggregator.setFailure(t);
            promiseAggregator.doneAllocatingPromises();
            PlatformDependent.throwException(t);
        }
        finally {
            if (headerBlock != null) {
                headerBlock.release();
            }
        }
        return promiseAggregator.doneAllocatingPromises();
    }
    
    private ChannelFuture writeContinuationFrames(final ChannelHandlerContext ctx, final int streamId, final ByteBuf headerBlock, final int padding, final Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator) {
        Http2Flags flags = new Http2Flags().paddingPresent(padding > 0);
        final int maxFragmentLength = this.maxFrameSize - padding;
        if (maxFragmentLength <= 0) {
            return promiseAggregator.setFailure((Throwable)new IllegalArgumentException(new StringBuilder().append("Padding [").append(padding).append("] is too large for max frame size [").append(this.maxFrameSize).append("]").toString()));
        }
        if (headerBlock.isReadable()) {
            int fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
            int payloadLength = fragmentReadableBytes + padding;
            ByteBuf buf = ctx.alloc().buffer(10);
            Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
            writePaddingLength(buf, padding);
            do {
                fragmentReadableBytes = Math.min(headerBlock.readableBytes(), maxFragmentLength);
                final ByteBuf fragment = headerBlock.readRetainedSlice(fragmentReadableBytes);
                payloadLength = fragmentReadableBytes + padding;
                if (headerBlock.isReadable()) {
                    ctx.write(buf.retain(), promiseAggregator.newPromise());
                }
                else {
                    flags = flags.endOfHeaders(true);
                    buf.release();
                    buf = ctx.alloc().buffer(10);
                    Http2CodecUtil.writeFrameHeaderInternal(buf, payloadLength, (byte)9, flags, streamId);
                    writePaddingLength(buf, padding);
                    ctx.write(buf, promiseAggregator.newPromise());
                }
                ctx.write(fragment, promiseAggregator.newPromise());
                if (paddingBytes(padding) > 0) {
                    ctx.write(DefaultHttp2FrameWriter.ZERO_BUFFER.slice(0, paddingBytes(padding)), promiseAggregator.newPromise());
                }
            } while (headerBlock.isReadable());
        }
        return promiseAggregator;
    }
    
    private static int paddingBytes(final int padding) {
        return padding - 1;
    }
    
    private static void writePaddingLength(final ByteBuf buf, final int padding) {
        if (padding > 0) {
            buf.writeByte(padding - 1);
        }
    }
    
    private static void verifyStreamId(final int streamId, final String argumentName) {
        if (streamId <= 0) {
            throw new IllegalArgumentException(argumentName + " must be > 0");
        }
    }
    
    private static void verifyStreamOrConnectionId(final int streamId, final String argumentName) {
        if (streamId < 0) {
            throw new IllegalArgumentException(argumentName + " must be >= 0");
        }
    }
    
    private static void verifyWeight(final short weight) {
        if (weight < 1 || weight > 256) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid weight: ").append((int)weight).toString());
        }
    }
    
    private static void verifyErrorCode(final long errorCode) {
        if (errorCode < 0L || errorCode > 4294967295L) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid errorCode: ").append(errorCode).toString());
        }
    }
    
    private static void verifyWindowSizeIncrement(final int windowSizeIncrement) {
        if (windowSizeIncrement < 0) {
            throw new IllegalArgumentException("WindowSizeIncrement must be >= 0");
        }
    }
    
    private static void verifyPingPayload(final ByteBuf data) {
        if (data == null || data.readableBytes() != 8) {
            throw new IllegalArgumentException("Opaque data must be 8 bytes");
        }
    }
    
    static {
        ZERO_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(255).writeZero(255)).asReadOnly();
    }
}
