package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ObjectUtil;
import io.netty.channel.ChannelHandlerContext;

public class DefaultHttp2LocalFlowController implements Http2LocalFlowController {
    public static final float DEFAULT_WINDOW_UPDATE_RATIO = 0.5f;
    private final Http2Connection connection;
    private final Http2Connection.PropertyKey stateKey;
    private Http2FrameWriter frameWriter;
    private ChannelHandlerContext ctx;
    private float windowUpdateRatio;
    private int initialWindowSize;
    private static final FlowState REDUCED_FLOW_STATE;
    
    public DefaultHttp2LocalFlowController(final Http2Connection connection) {
        this(connection, 0.5f, false);
    }
    
    public DefaultHttp2LocalFlowController(final Http2Connection connection, final float windowUpdateRatio, final boolean autoRefillConnectionWindow) {
        this.initialWindowSize = 65535;
        this.connection = ObjectUtil.<Http2Connection>checkNotNull(connection, "connection");
        this.windowUpdateRatio(windowUpdateRatio);
        this.stateKey = connection.newKey();
        final FlowState connectionState = autoRefillConnectionWindow ? new AutoRefillState(connection.connectionStream(), this.initialWindowSize) : new DefaultState(connection.connectionStream(), this.initialWindowSize);
        connection.connectionStream().<FlowState>setProperty(this.stateKey, connectionState);
        connection.addListener(new Http2ConnectionAdapter() {
            @Override
            public void onStreamAdded(final Http2Stream stream) {
                stream.<FlowState>setProperty(DefaultHttp2LocalFlowController.this.stateKey, DefaultHttp2LocalFlowController.REDUCED_FLOW_STATE);
            }
            
            @Override
            public void onStreamActive(final Http2Stream stream) {
                stream.<DefaultState>setProperty(DefaultHttp2LocalFlowController.this.stateKey, new DefaultState(stream, DefaultHttp2LocalFlowController.this.initialWindowSize));
            }
            
            @Override
            public void onStreamClosed(final Http2Stream stream) {
                try {
                    final FlowState state = DefaultHttp2LocalFlowController.this.state(stream);
                    final int unconsumedBytes = state.unconsumedBytes();
                    if (DefaultHttp2LocalFlowController.this.ctx != null && unconsumedBytes > 0) {
                        DefaultHttp2LocalFlowController.this.connectionState().consumeBytes(unconsumedBytes);
                        state.consumeBytes(unconsumedBytes);
                    }
                }
                catch (Http2Exception e) {
                    PlatformDependent.throwException((Throwable)e);
                }
                finally {
                    stream.<FlowState>setProperty(DefaultHttp2LocalFlowController.this.stateKey, DefaultHttp2LocalFlowController.REDUCED_FLOW_STATE);
                }
            }
        });
    }
    
    public DefaultHttp2LocalFlowController frameWriter(final Http2FrameWriter frameWriter) {
        this.frameWriter = ObjectUtil.<Http2FrameWriter>checkNotNull(frameWriter, "frameWriter");
        return this;
    }
    
    public void channelHandlerContext(final ChannelHandlerContext ctx) {
        this.ctx = ObjectUtil.<ChannelHandlerContext>checkNotNull(ctx, "ctx");
    }
    
    public void initialWindowSize(final int newWindowSize) throws Http2Exception {
        assert !(!this.ctx.executor().inEventLoop());
        final int delta = newWindowSize - this.initialWindowSize;
        this.initialWindowSize = newWindowSize;
        final WindowUpdateVisitor visitor = new WindowUpdateVisitor(delta);
        this.connection.forEachActiveStream(visitor);
        visitor.throwIfError();
    }
    
    public int initialWindowSize() {
        return this.initialWindowSize;
    }
    
    public int windowSize(final Http2Stream stream) {
        return this.state(stream).windowSize();
    }
    
    public int initialWindowSize(final Http2Stream stream) {
        return this.state(stream).initialWindowSize();
    }
    
    public void incrementWindowSize(final Http2Stream stream, final int delta) throws Http2Exception {
        assert this.ctx != null && this.ctx.executor().inEventLoop();
        final FlowState state = this.state(stream);
        state.incrementInitialStreamWindow(delta);
        state.writeWindowUpdateIfNeeded();
    }
    
    public boolean consumeBytes(final Http2Stream stream, final int numBytes) throws Http2Exception {
        assert this.ctx != null && this.ctx.executor().inEventLoop();
        if (numBytes < 0) {
            throw new IllegalArgumentException("numBytes must not be negative");
        }
        if (numBytes == 0) {
            return false;
        }
        if (stream == null || isClosed(stream)) {
            return false;
        }
        if (stream.id() == 0) {
            throw new UnsupportedOperationException("Returning bytes for the connection window is not supported");
        }
        boolean windowUpdateSent = this.connectionState().consumeBytes(numBytes);
        windowUpdateSent |= this.state(stream).consumeBytes(numBytes);
        return windowUpdateSent;
    }
    
    public int unconsumedBytes(final Http2Stream stream) {
        return this.state(stream).unconsumedBytes();
    }
    
    private static void checkValidRatio(final float ratio) {
        if (Double.compare((double)ratio, 0.0) <= 0 || Double.compare((double)ratio, 1.0) >= 0) {
            throw new IllegalArgumentException(new StringBuilder().append("Invalid ratio: ").append(ratio).toString());
        }
    }
    
    public void windowUpdateRatio(final float ratio) {
        assert !(!this.ctx.executor().inEventLoop());
        checkValidRatio(ratio);
        this.windowUpdateRatio = ratio;
    }
    
    public float windowUpdateRatio() {
        return this.windowUpdateRatio;
    }
    
    public void windowUpdateRatio(final Http2Stream stream, final float ratio) throws Http2Exception {
        assert this.ctx != null && this.ctx.executor().inEventLoop();
        checkValidRatio(ratio);
        final FlowState state = this.state(stream);
        state.windowUpdateRatio(ratio);
        state.writeWindowUpdateIfNeeded();
    }
    
    public float windowUpdateRatio(final Http2Stream stream) throws Http2Exception {
        return this.state(stream).windowUpdateRatio();
    }
    
    public void receiveFlowControlledFrame(final Http2Stream stream, final ByteBuf data, final int padding, final boolean endOfStream) throws Http2Exception {
        assert this.ctx != null && this.ctx.executor().inEventLoop();
        final int dataLength = data.readableBytes() + padding;
        final FlowState connectionState = this.connectionState();
        connectionState.receiveFlowControlledFrame(dataLength);
        if (stream != null && !isClosed(stream)) {
            final FlowState state = this.state(stream);
            state.endOfStream(endOfStream);
            state.receiveFlowControlledFrame(dataLength);
        }
        else if (dataLength > 0) {
            connectionState.consumeBytes(dataLength);
        }
    }
    
    private FlowState connectionState() {
        return this.connection.connectionStream().<FlowState>getProperty(this.stateKey);
    }
    
    private FlowState state(final Http2Stream stream) {
        return stream.<FlowState>getProperty(this.stateKey);
    }
    
    private static boolean isClosed(final Http2Stream stream) {
        return stream.state() == Http2Stream.State.CLOSED;
    }
    
    static {
        REDUCED_FLOW_STATE = new FlowState() {
            public int windowSize() {
                return 0;
            }
            
            public int initialWindowSize() {
                return 0;
            }
            
            public void window(final int initialWindowSize) {
                throw new UnsupportedOperationException();
            }
            
            public void incrementInitialStreamWindow(final int delta) {
            }
            
            public boolean writeWindowUpdateIfNeeded() throws Http2Exception {
                throw new UnsupportedOperationException();
            }
            
            public boolean consumeBytes(final int numBytes) throws Http2Exception {
                return false;
            }
            
            public int unconsumedBytes() {
                return 0;
            }
            
            public float windowUpdateRatio() {
                throw new UnsupportedOperationException();
            }
            
            public void windowUpdateRatio(final float ratio) {
                throw new UnsupportedOperationException();
            }
            
            public void receiveFlowControlledFrame(final int dataLength) throws Http2Exception {
                throw new UnsupportedOperationException();
            }
            
            public void incrementFlowControlWindows(final int delta) throws Http2Exception {
            }
            
            public void endOfStream(final boolean endOfStream) {
                throw new UnsupportedOperationException();
            }
        };
    }
    
    private final class AutoRefillState extends DefaultState {
        public AutoRefillState(final Http2Stream stream, final int initialWindowSize) {
            super(stream, initialWindowSize);
        }
        
        @Override
        public void receiveFlowControlledFrame(final int dataLength) throws Http2Exception {
            super.receiveFlowControlledFrame(dataLength);
            super.consumeBytes(dataLength);
        }
        
        @Override
        public boolean consumeBytes(final int numBytes) throws Http2Exception {
            return false;
        }
    }
    
    private class DefaultState implements FlowState {
        private final Http2Stream stream;
        private int window;
        private int processedWindow;
        private int initialStreamWindowSize;
        private float streamWindowUpdateRatio;
        private int lowerBound;
        private boolean endOfStream;
        
        public DefaultState(final Http2Stream stream, final int initialWindowSize) {
            this.stream = stream;
            this.window(initialWindowSize);
            this.streamWindowUpdateRatio = DefaultHttp2LocalFlowController.this.windowUpdateRatio;
        }
        
        public void window(final int initialWindowSize) {
            assert !(!DefaultHttp2LocalFlowController.this.ctx.executor().inEventLoop());
            this.initialStreamWindowSize = initialWindowSize;
            this.processedWindow = initialWindowSize;
            this.window = initialWindowSize;
        }
        
        public int windowSize() {
            return this.window;
        }
        
        public int initialWindowSize() {
            return this.initialStreamWindowSize;
        }
        
        public void endOfStream(final boolean endOfStream) {
            this.endOfStream = endOfStream;
        }
        
        public float windowUpdateRatio() {
            return this.streamWindowUpdateRatio;
        }
        
        public void windowUpdateRatio(final float ratio) {
            assert !(!DefaultHttp2LocalFlowController.this.ctx.executor().inEventLoop());
            this.streamWindowUpdateRatio = ratio;
        }
        
        public void incrementInitialStreamWindow(int delta) {
            final int newValue = (int)Math.min(2147483647L, Math.max(0L, this.initialStreamWindowSize + (long)delta));
            delta = newValue - this.initialStreamWindowSize;
            this.initialStreamWindowSize += delta;
        }
        
        public void incrementFlowControlWindows(final int delta) throws Http2Exception {
            if (delta > 0 && this.window > Integer.MAX_VALUE - delta) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window overflowed for stream: %d", this.stream.id());
            }
            this.window += delta;
            this.processedWindow += delta;
            this.lowerBound = ((delta < 0) ? delta : 0);
        }
        
        public void receiveFlowControlledFrame(final int dataLength) throws Http2Exception {
            assert dataLength >= 0;
            this.window -= dataLength;
            if (this.window < this.lowerBound) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.FLOW_CONTROL_ERROR, "Flow control window exceeded for stream: %d", this.stream.id());
            }
        }
        
        private void returnProcessedBytes(final int delta) throws Http2Exception {
            if (this.processedWindow - delta < this.window) {
                throw Http2Exception.streamError(this.stream.id(), Http2Error.INTERNAL_ERROR, "Attempting to return too many bytes for stream %d", this.stream.id());
            }
            this.processedWindow -= delta;
        }
        
        public boolean consumeBytes(final int numBytes) throws Http2Exception {
            this.returnProcessedBytes(numBytes);
            return this.writeWindowUpdateIfNeeded();
        }
        
        public int unconsumedBytes() {
            return this.processedWindow - this.window;
        }
        
        public boolean writeWindowUpdateIfNeeded() throws Http2Exception {
            if (this.endOfStream || this.initialStreamWindowSize <= 0) {
                return false;
            }
            final int threshold = (int)(this.initialStreamWindowSize * this.streamWindowUpdateRatio);
            if (this.processedWindow <= threshold) {
                this.writeWindowUpdate();
                return true;
            }
            return false;
        }
        
        private void writeWindowUpdate() throws Http2Exception {
            final int deltaWindowSize = this.initialStreamWindowSize - this.processedWindow;
            try {
                this.incrementFlowControlWindows(deltaWindowSize);
            }
            catch (Throwable t) {
                throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, t, "Attempting to return too many bytes for stream %d", this.stream.id());
            }
            DefaultHttp2LocalFlowController.this.frameWriter.writeWindowUpdate(DefaultHttp2LocalFlowController.this.ctx, this.stream.id(), deltaWindowSize, DefaultHttp2LocalFlowController.this.ctx.newPromise());
        }
    }
    
    private final class WindowUpdateVisitor implements Http2StreamVisitor {
        private Http2Exception.CompositeStreamException compositeException;
        private final int delta;
        
        public WindowUpdateVisitor(final int delta) {
            this.delta = delta;
        }
        
        public boolean visit(final Http2Stream stream) throws Http2Exception {
            try {
                final FlowState state = DefaultHttp2LocalFlowController.this.state(stream);
                state.incrementFlowControlWindows(this.delta);
                state.incrementInitialStreamWindow(this.delta);
            }
            catch (Http2Exception.StreamException e) {
                if (this.compositeException == null) {
                    this.compositeException = new Http2Exception.CompositeStreamException(e.error(), 4);
                }
                this.compositeException.add(e);
            }
            return true;
        }
        
        public void throwIfError() throws Http2Exception.CompositeStreamException {
            if (this.compositeException != null) {
                throw this.compositeException;
            }
        }
    }
    
    private interface FlowState {
        int windowSize();
        
        int initialWindowSize();
        
        void window(final int integer);
        
        void incrementInitialStreamWindow(final int integer);
        
        boolean writeWindowUpdateIfNeeded() throws Http2Exception;
        
        boolean consumeBytes(final int integer) throws Http2Exception;
        
        int unconsumedBytes();
        
        float windowUpdateRatio();
        
        void windowUpdateRatio(final float float1);
        
        void receiveFlowControlledFrame(final int integer) throws Http2Exception;
        
        void incrementFlowControlWindows(final int integer) throws Http2Exception;
        
        void endOfStream(final boolean boolean1);
    }
}
