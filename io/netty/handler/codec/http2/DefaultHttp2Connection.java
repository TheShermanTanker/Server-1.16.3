package io.netty.handler.codec.http2;

import java.util.LinkedHashSet;
import java.util.ArrayDeque;
import java.util.Set;
import java.util.Queue;
import java.util.Arrays;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.UnaryPromiseNotifier;
import io.netty.channel.ChannelPromise;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.Future;
import java.util.ArrayList;
import io.netty.util.collection.IntObjectHashMap;
import io.netty.util.concurrent.Promise;
import java.util.List;
import io.netty.util.collection.IntObjectMap;
import io.netty.util.internal.logging.InternalLogger;

public class DefaultHttp2Connection implements Http2Connection {
    private static final InternalLogger logger;
    final IntObjectMap<Http2Stream> streamMap;
    final PropertyKeyRegistry propertyKeyRegistry;
    final ConnectionStream connectionStream;
    final DefaultEndpoint<Http2LocalFlowController> localEndpoint;
    final DefaultEndpoint<Http2RemoteFlowController> remoteEndpoint;
    final List<Listener> listeners;
    final ActiveStreams activeStreams;
    Promise<Void> closePromise;
    
    public DefaultHttp2Connection(final boolean server) {
        this(server, 100);
    }
    
    public DefaultHttp2Connection(final boolean server, final int maxReservedStreams) {
        this.streamMap = new IntObjectHashMap<Http2Stream>();
        this.propertyKeyRegistry = new PropertyKeyRegistry();
        this.connectionStream = new ConnectionStream();
        this.listeners = (List<Listener>)new ArrayList(4);
        this.activeStreams = new ActiveStreams(this.listeners);
        this.localEndpoint = new DefaultEndpoint<Http2LocalFlowController>(server, server ? Integer.MAX_VALUE : maxReservedStreams);
        this.remoteEndpoint = new DefaultEndpoint<Http2RemoteFlowController>(!server, maxReservedStreams);
        this.streamMap.put(this.connectionStream.id(), this.connectionStream);
    }
    
    final boolean isClosed() {
        return this.closePromise != null;
    }
    
    public Future<Void> close(final Promise<Void> promise) {
        ObjectUtil.<Promise<Void>>checkNotNull(promise, "promise");
        if (this.closePromise != null) {
            if (this.closePromise != promise) {
                if (promise instanceof ChannelPromise && ((ChannelPromise)this.closePromise).isVoid()) {
                    this.closePromise = promise;
                }
                else {
                    this.closePromise.addListener(new UnaryPromiseNotifier(promise));
                }
            }
        }
        else {
            this.closePromise = promise;
        }
        if (this.isStreamMapEmpty()) {
            promise.trySuccess(null);
            return promise;
        }
        final Iterator<IntObjectMap.PrimitiveEntry<Http2Stream>> itr = (Iterator<IntObjectMap.PrimitiveEntry<Http2Stream>>)this.streamMap.entries().iterator();
        if (this.activeStreams.allowModifications()) {
            this.activeStreams.incrementPendingIterations();
            try {
                while (itr.hasNext()) {
                    final DefaultStream stream = ((IntObjectMap.PrimitiveEntry)itr.next()).value();
                    if (stream.id() != 0) {
                        stream.close(itr);
                    }
                }
            }
            finally {
                this.activeStreams.decrementPendingIterations();
            }
        }
        else {
            while (itr.hasNext()) {
                final Http2Stream stream2 = ((IntObjectMap.PrimitiveEntry)itr.next()).value();
                if (stream2.id() != 0) {
                    stream2.close();
                }
            }
        }
        return this.closePromise;
    }
    
    public void addListener(final Listener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final Listener listener) {
        this.listeners.remove(listener);
    }
    
    public boolean isServer() {
        return this.localEndpoint.isServer();
    }
    
    public Http2Stream connectionStream() {
        return this.connectionStream;
    }
    
    public Http2Stream stream(final int streamId) {
        return this.streamMap.get(streamId);
    }
    
    public boolean streamMayHaveExisted(final int streamId) {
        return this.remoteEndpoint.mayHaveCreatedStream(streamId) || this.localEndpoint.mayHaveCreatedStream(streamId);
    }
    
    public int numActiveStreams() {
        return this.activeStreams.size();
    }
    
    public Http2Stream forEachActiveStream(final Http2StreamVisitor visitor) throws Http2Exception {
        return this.activeStreams.forEachActiveStream(visitor);
    }
    
    public Endpoint<Http2LocalFlowController> local() {
        return this.localEndpoint;
    }
    
    public Endpoint<Http2RemoteFlowController> remote() {
        return this.remoteEndpoint;
    }
    
    public boolean goAwayReceived() {
        return ((DefaultEndpoint<Http2FlowController>)this.localEndpoint).lastStreamKnownByPeer >= 0;
    }
    
    public void goAwayReceived(final int lastKnownStream, final long errorCode, final ByteBuf debugData) {
        ((DefaultEndpoint<Http2FlowController>)this.localEndpoint).lastStreamKnownByPeer(lastKnownStream);
        for (int i = 0; i < this.listeners.size(); ++i) {
            try {
                ((Listener)this.listeners.get(i)).onGoAwayReceived(lastKnownStream, errorCode, debugData);
            }
            catch (Throwable cause) {
                DefaultHttp2Connection.logger.error("Caught Throwable from listener onGoAwayReceived.", cause);
            }
        }
        try {
            this.forEachActiveStream(new Http2StreamVisitor() {
                public boolean visit(final Http2Stream stream) {
                    if (stream.id() > lastKnownStream && DefaultHttp2Connection.this.localEndpoint.isValidStreamId(stream.id())) {
                        stream.close();
                    }
                    return true;
                }
            });
        }
        catch (Http2Exception e) {
            PlatformDependent.throwException((Throwable)e);
        }
    }
    
    public boolean goAwaySent() {
        return ((DefaultEndpoint<Http2FlowController>)this.remoteEndpoint).lastStreamKnownByPeer >= 0;
    }
    
    public void goAwaySent(final int lastKnownStream, final long errorCode, final ByteBuf debugData) {
        ((DefaultEndpoint<Http2FlowController>)this.remoteEndpoint).lastStreamKnownByPeer(lastKnownStream);
        for (int i = 0; i < this.listeners.size(); ++i) {
            try {
                ((Listener)this.listeners.get(i)).onGoAwaySent(lastKnownStream, errorCode, debugData);
            }
            catch (Throwable cause) {
                DefaultHttp2Connection.logger.error("Caught Throwable from listener onGoAwaySent.", cause);
            }
        }
        try {
            this.forEachActiveStream(new Http2StreamVisitor() {
                public boolean visit(final Http2Stream stream) {
                    if (stream.id() > lastKnownStream && DefaultHttp2Connection.this.remoteEndpoint.isValidStreamId(stream.id())) {
                        stream.close();
                    }
                    return true;
                }
            });
        }
        catch (Http2Exception e) {
            PlatformDependent.throwException((Throwable)e);
        }
    }
    
    private boolean isStreamMapEmpty() {
        return this.streamMap.size() == 1;
    }
    
    void removeStream(final DefaultStream stream, final Iterator<?> itr) {
        boolean removed;
        if (itr == null) {
            removed = (this.streamMap.remove(stream.id()) != null);
        }
        else {
            itr.remove();
            removed = true;
        }
        if (removed) {
            for (int i = 0; i < this.listeners.size(); ++i) {
                try {
                    ((Listener)this.listeners.get(i)).onStreamRemoved(stream);
                }
                catch (Throwable cause) {
                    DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamRemoved.", cause);
                }
            }
            if (this.closePromise != null && this.isStreamMapEmpty()) {
                this.closePromise.trySuccess(null);
            }
        }
    }
    
    static Http2Stream.State activeState(final int streamId, final Http2Stream.State initialState, final boolean isLocal, final boolean halfClosed) throws Http2Exception {
        switch (initialState) {
            case IDLE: {
                return halfClosed ? (isLocal ? Http2Stream.State.HALF_CLOSED_LOCAL : Http2Stream.State.HALF_CLOSED_REMOTE) : Http2Stream.State.OPEN;
            }
            case RESERVED_LOCAL: {
                return Http2Stream.State.HALF_CLOSED_REMOTE;
            }
            case RESERVED_REMOTE: {
                return Http2Stream.State.HALF_CLOSED_LOCAL;
            }
            default: {
                throw Http2Exception.streamError(streamId, Http2Error.PROTOCOL_ERROR, new StringBuilder().append("Attempting to open a stream in an invalid state: ").append(initialState).toString());
            }
        }
    }
    
    void notifyHalfClosed(final Http2Stream stream) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            try {
                ((Listener)this.listeners.get(i)).onStreamHalfClosed(stream);
            }
            catch (Throwable cause) {
                DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamHalfClosed.", cause);
            }
        }
    }
    
    void notifyClosed(final Http2Stream stream) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            try {
                ((Listener)this.listeners.get(i)).onStreamClosed(stream);
            }
            catch (Throwable cause) {
                DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamClosed.", cause);
            }
        }
    }
    
    public PropertyKey newKey() {
        return this.propertyKeyRegistry.newKey();
    }
    
    final DefaultPropertyKey verifyKey(final PropertyKey key) {
        return ObjectUtil.<DefaultPropertyKey>checkNotNull((DefaultPropertyKey)key, "key").verifyConnection(this);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DefaultHttp2Connection.class);
    }
    
    private class DefaultStream implements Http2Stream {
        private static final byte META_STATE_SENT_RST = 1;
        private static final byte META_STATE_SENT_HEADERS = 2;
        private static final byte META_STATE_SENT_TRAILERS = 4;
        private static final byte META_STATE_SENT_PUSHPROMISE = 8;
        private static final byte META_STATE_RECV_HEADERS = 16;
        private static final byte META_STATE_RECV_TRAILERS = 32;
        private final int id;
        private final PropertyMap properties;
        private State state;
        private byte metaState;
        
        DefaultStream(final int id, final State state) {
            this.properties = new PropertyMap();
            this.id = id;
            this.state = state;
        }
        
        public final int id() {
            return this.id;
        }
        
        public final State state() {
            return this.state;
        }
        
        public boolean isResetSent() {
            return (this.metaState & 0x1) != 0x0;
        }
        
        public Http2Stream resetSent() {
            this.metaState |= 0x1;
            return this;
        }
        
        public Http2Stream headersSent(final boolean isInformational) {
            if (!isInformational) {
                this.metaState |= (byte)(this.isHeadersSent() ? 4 : 2);
            }
            return this;
        }
        
        public boolean isHeadersSent() {
            return (this.metaState & 0x2) != 0x0;
        }
        
        public boolean isTrailersSent() {
            return (this.metaState & 0x4) != 0x0;
        }
        
        public Http2Stream headersReceived(final boolean isInformational) {
            if (!isInformational) {
                this.metaState |= (byte)(this.isHeadersReceived() ? 32 : 16);
            }
            return this;
        }
        
        public boolean isHeadersReceived() {
            return (this.metaState & 0x10) != 0x0;
        }
        
        public boolean isTrailersReceived() {
            return (this.metaState & 0x20) != 0x0;
        }
        
        public Http2Stream pushPromiseSent() {
            this.metaState |= 0x8;
            return this;
        }
        
        public boolean isPushPromiseSent() {
            return (this.metaState & 0x8) != 0x0;
        }
        
        public final <V> V setProperty(final PropertyKey key, final V value) {
            return this.properties.<V>add(DefaultHttp2Connection.this.verifyKey(key), value);
        }
        
        public final <V> V getProperty(final PropertyKey key) {
            return this.properties.<V>get(DefaultHttp2Connection.this.verifyKey(key));
        }
        
        public final <V> V removeProperty(final PropertyKey key) {
            return this.properties.<V>remove(DefaultHttp2Connection.this.verifyKey(key));
        }
        
        public Http2Stream open(final boolean halfClosed) throws Http2Exception {
            this.state = DefaultHttp2Connection.activeState(this.id, this.state, this.isLocal(), halfClosed);
            if (!this.createdBy().canOpenStream()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Maximum active streams violated for this endpoint.");
            }
            this.activate();
            return this;
        }
        
        void activate() {
            if (this.state == State.HALF_CLOSED_LOCAL) {
                this.headersSent(false);
            }
            else if (this.state == State.HALF_CLOSED_REMOTE) {
                this.headersReceived(false);
            }
            DefaultHttp2Connection.this.activeStreams.activate(this);
        }
        
        Http2Stream close(final Iterator<?> itr) {
            if (this.state == State.CLOSED) {
                return this;
            }
            this.state = State.CLOSED;
            final DefaultEndpoint<? extends Http2FlowController> createdBy = this.createdBy();
            --createdBy.numStreams;
            DefaultHttp2Connection.this.activeStreams.deactivate(this, itr);
            return this;
        }
        
        public Http2Stream close() {
            return this.close(null);
        }
        
        public Http2Stream closeLocalSide() {
            switch (this.state) {
                case OPEN: {
                    this.state = State.HALF_CLOSED_LOCAL;
                    DefaultHttp2Connection.this.notifyHalfClosed(this);
                    break;
                }
                case HALF_CLOSED_LOCAL: {
                    break;
                }
                default: {
                    this.close();
                    break;
                }
            }
            return this;
        }
        
        public Http2Stream closeRemoteSide() {
            switch (this.state) {
                case OPEN: {
                    this.state = State.HALF_CLOSED_REMOTE;
                    DefaultHttp2Connection.this.notifyHalfClosed(this);
                    break;
                }
                case HALF_CLOSED_REMOTE: {
                    break;
                }
                default: {
                    this.close();
                    break;
                }
            }
            return this;
        }
        
        DefaultEndpoint<? extends Http2FlowController> createdBy() {
            return (DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id) ? DefaultHttp2Connection.this.localEndpoint : DefaultHttp2Connection.this.remoteEndpoint);
        }
        
        final boolean isLocal() {
            return DefaultHttp2Connection.this.localEndpoint.isValidStreamId(this.id);
        }
        
        private class PropertyMap {
            Object[] values;
            
            private PropertyMap() {
                this.values = EmptyArrays.EMPTY_OBJECTS;
            }
            
             <V> V add(final DefaultPropertyKey key, final V value) {
                this.resizeIfNecessary(key.index);
                final V prevValue = (V)this.values[key.index];
                this.values[key.index] = value;
                return prevValue;
            }
            
             <V> V get(final DefaultPropertyKey key) {
                if (key.index >= this.values.length) {
                    return null;
                }
                return (V)this.values[key.index];
            }
            
             <V> V remove(final DefaultPropertyKey key) {
                V prevValue = null;
                if (key.index < this.values.length) {
                    prevValue = (V)this.values[key.index];
                    this.values[key.index] = null;
                }
                return prevValue;
            }
            
            void resizeIfNecessary(final int index) {
                if (index >= this.values.length) {
                    this.values = Arrays.copyOf(this.values, DefaultHttp2Connection.this.propertyKeyRegistry.size());
                }
            }
        }
    }
    
    private final class ConnectionStream extends DefaultStream {
        ConnectionStream() {
            super(0, Http2Stream.State.IDLE);
        }
        
        @Override
        public boolean isResetSent() {
            return false;
        }
        
        @Override
        DefaultEndpoint<? extends Http2FlowController> createdBy() {
            return null;
        }
        
        @Override
        public Http2Stream resetSent() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Http2Stream open(final boolean halfClosed) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Http2Stream close() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Http2Stream closeLocalSide() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Http2Stream closeRemoteSide() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Http2Stream headersSent(final boolean isInformational) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean isHeadersSent() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public Http2Stream pushPromiseSent() {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean isPushPromiseSent() {
            throw new UnsupportedOperationException();
        }
    }
    
    private final class DefaultEndpoint<F extends Http2FlowController> implements Endpoint<F> {
        private final boolean server;
        private int nextStreamIdToCreate;
        private int nextReservationStreamId;
        private int lastStreamKnownByPeer;
        private boolean pushToAllowed;
        private F flowController;
        private int maxStreams;
        private int maxActiveStreams;
        private final int maxReservedStreams;
        int numActiveStreams;
        int numStreams;
        
        DefaultEndpoint(final boolean server, final int maxReservedStreams) {
            this.lastStreamKnownByPeer = -1;
            this.pushToAllowed = true;
            this.server = server;
            if (server) {
                this.nextStreamIdToCreate = 2;
                this.nextReservationStreamId = 0;
            }
            else {
                this.nextStreamIdToCreate = 1;
                this.nextReservationStreamId = 1;
            }
            this.pushToAllowed = !server;
            this.maxActiveStreams = Integer.MAX_VALUE;
            this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
            this.updateMaxStreams();
        }
        
        public int incrementAndGetNextStreamId() {
            return (this.nextReservationStreamId >= 0) ? (this.nextReservationStreamId += 2) : this.nextReservationStreamId;
        }
        
        private void incrementExpectedStreamId(final int streamId) {
            if (streamId > this.nextReservationStreamId && this.nextReservationStreamId >= 0) {
                this.nextReservationStreamId = streamId;
            }
            this.nextStreamIdToCreate = streamId + 2;
            ++this.numStreams;
        }
        
        public boolean isValidStreamId(final int streamId) {
            return streamId > 0 && this.server == ((streamId & 0x1) == 0x0);
        }
        
        public boolean mayHaveCreatedStream(final int streamId) {
            return this.isValidStreamId(streamId) && streamId <= this.lastStreamCreated();
        }
        
        public boolean canOpenStream() {
            return this.numActiveStreams < this.maxActiveStreams;
        }
        
        public DefaultStream createStream(final int streamId, final boolean halfClosed) throws Http2Exception {
            final Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, this.isLocal(), halfClosed);
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            stream.activate();
            return stream;
        }
        
        public boolean created(final Http2Stream stream) {
            return stream instanceof DefaultStream && ((DefaultStream)stream).createdBy() == this;
        }
        
        public boolean isServer() {
            return this.server;
        }
        
        public DefaultStream reservePushStream(final int streamId, final Http2Stream parent) throws Http2Exception {
            if (parent == null) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing");
            }
            Label_0076: {
                if (this.isLocal()) {
                    if (parent.state().localSideOpen()) {
                        break Label_0076;
                    }
                }
                else if (parent.state().remoteSideOpen()) {
                    break Label_0076;
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", parent.id());
            }
            if (!this.opposite().allowPushTo()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint");
            }
            final Http2Stream.State state = this.isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            return stream;
        }
        
        private void addStream(final DefaultStream stream) {
            DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
            for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); ++i) {
                try {
                    ((Listener)DefaultHttp2Connection.this.listeners.get(i)).onStreamAdded(stream);
                }
                catch (Throwable cause) {
                    DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
                }
            }
        }
        
        public void allowPushTo(final boolean allow) {
            if (allow && this.server) {
                throw new IllegalArgumentException("Servers do not allow push");
            }
            this.pushToAllowed = allow;
        }
        
        public boolean allowPushTo() {
            return this.pushToAllowed;
        }
        
        public int numActiveStreams() {
            return this.numActiveStreams;
        }
        
        public int maxActiveStreams() {
            return this.maxActiveStreams;
        }
        
        public void maxActiveStreams(final int maxActiveStreams) {
            this.maxActiveStreams = maxActiveStreams;
            this.updateMaxStreams();
        }
        
        public int lastStreamCreated() {
            return (this.nextStreamIdToCreate > 1) ? (this.nextStreamIdToCreate - 2) : 0;
        }
        
        public int lastStreamKnownByPeer() {
            return this.lastStreamKnownByPeer;
        }
        
        private void lastStreamKnownByPeer(final int lastKnownStream) {
            this.lastStreamKnownByPeer = lastKnownStream;
        }
        
        public F flowController() {
            return this.flowController;
        }
        
        public void flowController(final F flowController) {
            this.flowController = ObjectUtil.<F>checkNotNull(flowController, "flowController");
        }
        
        public Endpoint<? extends Http2FlowController> opposite() {
            return this.isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
        }
        
        private void updateMaxStreams() {
            this.maxStreams = (int)Math.min(2147483647L, this.maxActiveStreams + (long)this.maxReservedStreams);
        }
        
        private void checkNewStreamAllowed(final int streamId, final Http2Stream.State state) throws Http2Exception {
            assert state != Http2Stream.State.IDLE;
            if (DefaultHttp2Connection.this.goAwayReceived() && streamId > DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Cannot create stream %d since this endpoint has received a GOAWAY frame with last stream id %d.", streamId, DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer());
            }
            if (!this.isValidStreamId(streamId)) {
                if (streamId < 0) {
                    throw new Http2NoMoreStreamIdsException();
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", streamId, this.server ? "server" : "client");
            }
            else {
                if (streamId < this.nextStreamIdToCreate) {
                    throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", streamId, this.nextStreamIdToCreate);
                }
                if (this.nextStreamIdToCreate <= 0) {
                    throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.");
                }
                final boolean isReserved = state == Http2Stream.State.RESERVED_LOCAL || state == Http2Stream.State.RESERVED_REMOTE;
                if ((!isReserved && !this.canOpenStream()) || (isReserved && this.numStreams >= this.maxStreams)) {
                    throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint.");
                }
                if (DefaultHttp2Connection.this.isClosed()) {
                    throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", streamId);
                }
            }
        }
        
        private boolean isLocal() {
            return this == DefaultHttp2Connection.this.localEndpoint;
        }
    }
    
    private final class DefaultEndpoint<F extends Http2FlowController> implements Endpoint<F> {
        private final boolean server;
        private int nextStreamIdToCreate;
        private int nextReservationStreamId;
        private int lastStreamKnownByPeer;
        private boolean pushToAllowed;
        private F flowController;
        private int maxStreams;
        private int maxActiveStreams;
        private final int maxReservedStreams;
        int numActiveStreams;
        int numStreams;
        
        DefaultEndpoint(final boolean server, final int maxReservedStreams) {
            this.lastStreamKnownByPeer = -1;
            this.pushToAllowed = true;
            this.server = server;
            if (server) {
                this.nextStreamIdToCreate = 2;
                this.nextReservationStreamId = 0;
            }
            else {
                this.nextStreamIdToCreate = 1;
                this.nextReservationStreamId = 1;
            }
            this.pushToAllowed = !server;
            this.maxActiveStreams = Integer.MAX_VALUE;
            this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
            this.updateMaxStreams();
        }
        
        public int incrementAndGetNextStreamId() {
            return (this.nextReservationStreamId >= 0) ? (this.nextReservationStreamId += 2) : this.nextReservationStreamId;
        }
        
        private void incrementExpectedStreamId(final int streamId) {
            if (streamId > this.nextReservationStreamId && this.nextReservationStreamId >= 0) {
                this.nextReservationStreamId = streamId;
            }
            this.nextStreamIdToCreate = streamId + 2;
            ++this.numStreams;
        }
        
        public boolean isValidStreamId(final int streamId) {
            return streamId > 0 && this.server == ((streamId & 0x1) == 0x0);
        }
        
        public boolean mayHaveCreatedStream(final int streamId) {
            return this.isValidStreamId(streamId) && streamId <= this.lastStreamCreated();
        }
        
        public boolean canOpenStream() {
            return this.numActiveStreams < this.maxActiveStreams;
        }
        
        public DefaultStream createStream(final int streamId, final boolean halfClosed) throws Http2Exception {
            final Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, this.isLocal(), halfClosed);
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            stream.activate();
            return stream;
        }
        
        public boolean created(final Http2Stream stream) {
            return stream instanceof DefaultStream && ((DefaultStream)stream).createdBy() == this;
        }
        
        public boolean isServer() {
            return this.server;
        }
        
        public DefaultStream reservePushStream(final int streamId, final Http2Stream parent) throws Http2Exception {
            if (parent == null) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing");
            }
            Label_0076: {
                if (this.isLocal()) {
                    if (parent.state().localSideOpen()) {
                        break Label_0076;
                    }
                }
                else if (parent.state().remoteSideOpen()) {
                    break Label_0076;
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", parent.id());
            }
            if (!this.opposite().allowPushTo()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint");
            }
            final Http2Stream.State state = this.isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            return stream;
        }
        
        private void addStream(final DefaultStream stream) {
            DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
            for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); ++i) {
                try {
                    ((Listener)DefaultHttp2Connection.this.listeners.get(i)).onStreamAdded(stream);
                }
                catch (Throwable cause) {
                    DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
                }
            }
        }
        
        public void allowPushTo(final boolean allow) {
            if (allow && this.server) {
                throw new IllegalArgumentException("Servers do not allow push");
            }
            this.pushToAllowed = allow;
        }
        
        public boolean allowPushTo() {
            return this.pushToAllowed;
        }
        
        public int numActiveStreams() {
            return this.numActiveStreams;
        }
        
        public int maxActiveStreams() {
            return this.maxActiveStreams;
        }
        
        public void maxActiveStreams(final int maxActiveStreams) {
            this.maxActiveStreams = maxActiveStreams;
            this.updateMaxStreams();
        }
        
        public int lastStreamCreated() {
            return (this.nextStreamIdToCreate > 1) ? (this.nextStreamIdToCreate - 2) : 0;
        }
        
        public int lastStreamKnownByPeer() {
            return this.lastStreamKnownByPeer;
        }
        
        private void lastStreamKnownByPeer(final int lastKnownStream) {
            this.lastStreamKnownByPeer = lastKnownStream;
        }
        
        public F flowController() {
            return this.flowController;
        }
        
        public void flowController(final F flowController) {
            this.flowController = (F)ObjectUtil.<F>checkNotNull(flowController, "flowController");
        }
        
        public Endpoint<? extends Http2FlowController> opposite() {
            return this.isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
        }
        
        private void updateMaxStreams() {
            this.maxStreams = (int)Math.min(2147483647L, this.maxActiveStreams + (long)this.maxReservedStreams);
        }
        
        private void checkNewStreamAllowed(final int streamId, final Http2Stream.State state) throws Http2Exception {
            assert state != Http2Stream.State.IDLE;
            if (DefaultHttp2Connection.this.goAwayReceived() && streamId > DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Cannot create stream %d since this endpoint has received a GOAWAY frame with last stream id %d.", streamId, DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer());
            }
            if (!this.isValidStreamId(streamId)) {
                if (streamId < 0) {
                    throw new Http2NoMoreStreamIdsException();
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", streamId, this.server ? "server" : "client");
            }
            else {
                if (streamId < this.nextStreamIdToCreate) {
                    throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", streamId, this.nextStreamIdToCreate);
                }
                if (this.nextStreamIdToCreate <= 0) {
                    throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.");
                }
                final boolean isReserved = state == Http2Stream.State.RESERVED_LOCAL || state == Http2Stream.State.RESERVED_REMOTE;
                if ((!isReserved && !this.canOpenStream()) || (isReserved && this.numStreams >= this.maxStreams)) {
                    throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint.");
                }
                if (DefaultHttp2Connection.this.isClosed()) {
                    throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", streamId);
                }
            }
        }
        
        private boolean isLocal() {
            return this == DefaultHttp2Connection.this.localEndpoint;
        }
    }
    
    private final class DefaultEndpoint<F extends Http2FlowController> implements Endpoint<F> {
        private final boolean server;
        private int nextStreamIdToCreate;
        private int nextReservationStreamId;
        private int lastStreamKnownByPeer;
        private boolean pushToAllowed;
        private F flowController;
        private int maxStreams;
        private int maxActiveStreams;
        private final int maxReservedStreams;
        int numActiveStreams;
        int numStreams;
        
        DefaultEndpoint(final boolean server, final int maxReservedStreams) {
            this.lastStreamKnownByPeer = -1;
            this.pushToAllowed = true;
            this.server = server;
            if (server) {
                this.nextStreamIdToCreate = 2;
                this.nextReservationStreamId = 0;
            }
            else {
                this.nextStreamIdToCreate = 1;
                this.nextReservationStreamId = 1;
            }
            this.pushToAllowed = !server;
            this.maxActiveStreams = Integer.MAX_VALUE;
            this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
            this.updateMaxStreams();
        }
        
        public int incrementAndGetNextStreamId() {
            return (this.nextReservationStreamId >= 0) ? (this.nextReservationStreamId += 2) : this.nextReservationStreamId;
        }
        
        private void incrementExpectedStreamId(final int streamId) {
            if (streamId > this.nextReservationStreamId && this.nextReservationStreamId >= 0) {
                this.nextReservationStreamId = streamId;
            }
            this.nextStreamIdToCreate = streamId + 2;
            ++this.numStreams;
        }
        
        public boolean isValidStreamId(final int streamId) {
            return streamId > 0 && this.server == ((streamId & 0x1) == 0x0);
        }
        
        public boolean mayHaveCreatedStream(final int streamId) {
            return this.isValidStreamId(streamId) && streamId <= this.lastStreamCreated();
        }
        
        public boolean canOpenStream() {
            return this.numActiveStreams < this.maxActiveStreams;
        }
        
        public DefaultStream createStream(final int streamId, final boolean halfClosed) throws Http2Exception {
            final Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, this.isLocal(), halfClosed);
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            stream.activate();
            return stream;
        }
        
        public boolean created(final Http2Stream stream) {
            return stream instanceof DefaultStream && ((DefaultStream)stream).createdBy() == this;
        }
        
        public boolean isServer() {
            return this.server;
        }
        
        public DefaultStream reservePushStream(final int streamId, final Http2Stream parent) throws Http2Exception {
            if (parent == null) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing");
            }
            Label_0076: {
                if (this.isLocal()) {
                    if (parent.state().localSideOpen()) {
                        break Label_0076;
                    }
                }
                else if (parent.state().remoteSideOpen()) {
                    break Label_0076;
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", parent.id());
            }
            if (!this.opposite().allowPushTo()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint");
            }
            final Http2Stream.State state = this.isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            return stream;
        }
        
        private void addStream(final DefaultStream stream) {
            DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
            for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); ++i) {
                try {
                    ((Listener)DefaultHttp2Connection.this.listeners.get(i)).onStreamAdded(stream);
                }
                catch (Throwable cause) {
                    DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
                }
            }
        }
        
        public void allowPushTo(final boolean allow) {
            if (allow && this.server) {
                throw new IllegalArgumentException("Servers do not allow push");
            }
            this.pushToAllowed = allow;
        }
        
        public boolean allowPushTo() {
            return this.pushToAllowed;
        }
        
        public int numActiveStreams() {
            return this.numActiveStreams;
        }
        
        public int maxActiveStreams() {
            return this.maxActiveStreams;
        }
        
        public void maxActiveStreams(final int maxActiveStreams) {
            this.maxActiveStreams = maxActiveStreams;
            this.updateMaxStreams();
        }
        
        public int lastStreamCreated() {
            return (this.nextStreamIdToCreate > 1) ? (this.nextStreamIdToCreate - 2) : 0;
        }
        
        public int lastStreamKnownByPeer() {
            return this.lastStreamKnownByPeer;
        }
        
        private void lastStreamKnownByPeer(final int lastKnownStream) {
            this.lastStreamKnownByPeer = lastKnownStream;
        }
        
        public F flowController() {
            return this.flowController;
        }
        
        public void flowController(final F flowController) {
            this.flowController = (F)ObjectUtil.<F>checkNotNull(flowController, "flowController");
        }
        
        public Endpoint<? extends Http2FlowController> opposite() {
            return this.isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
        }
        
        private void updateMaxStreams() {
            this.maxStreams = (int)Math.min(2147483647L, this.maxActiveStreams + (long)this.maxReservedStreams);
        }
        
        private void checkNewStreamAllowed(final int streamId, final Http2Stream.State state) throws Http2Exception {
            assert state != Http2Stream.State.IDLE;
            if (DefaultHttp2Connection.this.goAwayReceived() && streamId > DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Cannot create stream %d since this endpoint has received a GOAWAY frame with last stream id %d.", streamId, DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer());
            }
            if (!this.isValidStreamId(streamId)) {
                if (streamId < 0) {
                    throw new Http2NoMoreStreamIdsException();
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", streamId, this.server ? "server" : "client");
            }
            else {
                if (streamId < this.nextStreamIdToCreate) {
                    throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", streamId, this.nextStreamIdToCreate);
                }
                if (this.nextStreamIdToCreate <= 0) {
                    throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.");
                }
                final boolean isReserved = state == Http2Stream.State.RESERVED_LOCAL || state == Http2Stream.State.RESERVED_REMOTE;
                if ((!isReserved && !this.canOpenStream()) || (isReserved && this.numStreams >= this.maxStreams)) {
                    throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint.");
                }
                if (DefaultHttp2Connection.this.isClosed()) {
                    throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", streamId);
                }
            }
        }
        
        private boolean isLocal() {
            return this == DefaultHttp2Connection.this.localEndpoint;
        }
    }
    
    private final class DefaultEndpoint<F extends Http2FlowController> implements Endpoint<F> {
        private final boolean server;
        private int nextStreamIdToCreate;
        private int nextReservationStreamId;
        private int lastStreamKnownByPeer;
        private boolean pushToAllowed;
        private F flowController;
        private int maxStreams;
        private int maxActiveStreams;
        private final int maxReservedStreams;
        int numActiveStreams;
        int numStreams;
        
        DefaultEndpoint(final boolean server, final int maxReservedStreams) {
            this.lastStreamKnownByPeer = -1;
            this.pushToAllowed = true;
            this.server = server;
            if (server) {
                this.nextStreamIdToCreate = 2;
                this.nextReservationStreamId = 0;
            }
            else {
                this.nextStreamIdToCreate = 1;
                this.nextReservationStreamId = 1;
            }
            this.pushToAllowed = !server;
            this.maxActiveStreams = Integer.MAX_VALUE;
            this.maxReservedStreams = ObjectUtil.checkPositiveOrZero(maxReservedStreams, "maxReservedStreams");
            this.updateMaxStreams();
        }
        
        public int incrementAndGetNextStreamId() {
            return (this.nextReservationStreamId >= 0) ? (this.nextReservationStreamId += 2) : this.nextReservationStreamId;
        }
        
        private void incrementExpectedStreamId(final int streamId) {
            if (streamId > this.nextReservationStreamId && this.nextReservationStreamId >= 0) {
                this.nextReservationStreamId = streamId;
            }
            this.nextStreamIdToCreate = streamId + 2;
            ++this.numStreams;
        }
        
        public boolean isValidStreamId(final int streamId) {
            return streamId > 0 && this.server == ((streamId & 0x1) == 0x0);
        }
        
        public boolean mayHaveCreatedStream(final int streamId) {
            return this.isValidStreamId(streamId) && streamId <= this.lastStreamCreated();
        }
        
        public boolean canOpenStream() {
            return this.numActiveStreams < this.maxActiveStreams;
        }
        
        public DefaultStream createStream(final int streamId, final boolean halfClosed) throws Http2Exception {
            final Http2Stream.State state = DefaultHttp2Connection.activeState(streamId, Http2Stream.State.IDLE, this.isLocal(), halfClosed);
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            stream.activate();
            return stream;
        }
        
        public boolean created(final Http2Stream stream) {
            return stream instanceof DefaultStream && ((DefaultStream)stream).createdBy() == this;
        }
        
        public boolean isServer() {
            return this.server;
        }
        
        public DefaultStream reservePushStream(final int streamId, final Http2Stream parent) throws Http2Exception {
            if (parent == null) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing");
            }
            Label_0076: {
                if (this.isLocal()) {
                    if (parent.state().localSideOpen()) {
                        break Label_0076;
                    }
                }
                else if (parent.state().remoteSideOpen()) {
                    break Label_0076;
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", parent.id());
            }
            if (!this.opposite().allowPushTo()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint");
            }
            final Http2Stream.State state = this.isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE;
            this.checkNewStreamAllowed(streamId, state);
            final DefaultStream stream = new DefaultStream(streamId, state);
            this.incrementExpectedStreamId(streamId);
            this.addStream(stream);
            return stream;
        }
        
        private void addStream(final DefaultStream stream) {
            DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
            for (int i = 0; i < DefaultHttp2Connection.this.listeners.size(); ++i) {
                try {
                    ((Listener)DefaultHttp2Connection.this.listeners.get(i)).onStreamAdded(stream);
                }
                catch (Throwable cause) {
                    DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamAdded.", cause);
                }
            }
        }
        
        public void allowPushTo(final boolean allow) {
            if (allow && this.server) {
                throw new IllegalArgumentException("Servers do not allow push");
            }
            this.pushToAllowed = allow;
        }
        
        public boolean allowPushTo() {
            return this.pushToAllowed;
        }
        
        public int numActiveStreams() {
            return this.numActiveStreams;
        }
        
        public int maxActiveStreams() {
            return this.maxActiveStreams;
        }
        
        public void maxActiveStreams(final int maxActiveStreams) {
            this.maxActiveStreams = maxActiveStreams;
            this.updateMaxStreams();
        }
        
        public int lastStreamCreated() {
            return (this.nextStreamIdToCreate > 1) ? (this.nextStreamIdToCreate - 2) : 0;
        }
        
        public int lastStreamKnownByPeer() {
            return this.lastStreamKnownByPeer;
        }
        
        private void lastStreamKnownByPeer(final int lastKnownStream) {
            this.lastStreamKnownByPeer = lastKnownStream;
        }
        
        public F flowController() {
            return this.flowController;
        }
        
        public void flowController(final F flowController) {
            this.flowController = (F)ObjectUtil.<F>checkNotNull(flowController, "flowController");
        }
        
        public Endpoint<? extends Http2FlowController> opposite() {
            return this.isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
        }
        
        private void updateMaxStreams() {
            this.maxStreams = (int)Math.min(2147483647L, this.maxActiveStreams + (long)this.maxReservedStreams);
        }
        
        private void checkNewStreamAllowed(final int streamId, final Http2Stream.State state) throws Http2Exception {
            assert state != Http2Stream.State.IDLE;
            if (DefaultHttp2Connection.this.goAwayReceived() && streamId > DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer()) {
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Cannot create stream %d since this endpoint has received a GOAWAY frame with last stream id %d.", streamId, DefaultHttp2Connection.this.localEndpoint.lastStreamKnownByPeer());
            }
            if (!this.isValidStreamId(streamId)) {
                if (streamId < 0) {
                    throw new Http2NoMoreStreamIdsException();
                }
                throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", streamId, this.server ? "server" : "client");
            }
            else {
                if (streamId < this.nextStreamIdToCreate) {
                    throw Http2Exception.closedStreamError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", streamId, this.nextStreamIdToCreate);
                }
                if (this.nextStreamIdToCreate <= 0) {
                    throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Stream IDs are exhausted for this endpoint.");
                }
                final boolean isReserved = state == Http2Stream.State.RESERVED_LOCAL || state == Http2Stream.State.RESERVED_REMOTE;
                if ((!isReserved && !this.canOpenStream()) || (isReserved && this.numStreams >= this.maxStreams)) {
                    throw Http2Exception.streamError(streamId, Http2Error.REFUSED_STREAM, "Maximum active streams violated for this endpoint.");
                }
                if (DefaultHttp2Connection.this.isClosed()) {
                    throw Http2Exception.connectionError(Http2Error.INTERNAL_ERROR, "Attempted to create stream id %d after connection was closed", streamId);
                }
            }
        }
        
        private boolean isLocal() {
            return this == DefaultHttp2Connection.this.localEndpoint;
        }
    }
    
    private final class ActiveStreams {
        private final List<Listener> listeners;
        private final Queue<Event> pendingEvents;
        private final Set<Http2Stream> streams;
        private int pendingIterations;
        
        public ActiveStreams(final List<Listener> listeners) {
            this.pendingEvents = (Queue<Event>)new ArrayDeque(4);
            this.streams = (Set<Http2Stream>)new LinkedHashSet();
            this.listeners = listeners;
        }
        
        public int size() {
            return this.streams.size();
        }
        
        public void activate(final DefaultStream stream) {
            if (this.allowModifications()) {
                this.addToActiveStreams(stream);
            }
            else {
                this.pendingEvents.add(new Event() {
                    public void process() {
                        ActiveStreams.this.addToActiveStreams(stream);
                    }
                });
            }
        }
        
        public void deactivate(final DefaultStream stream, final Iterator<?> itr) {
            if (this.allowModifications() || itr != null) {
                this.removeFromActiveStreams(stream, itr);
            }
            else {
                this.pendingEvents.add(new Event() {
                    public void process() {
                        ActiveStreams.this.removeFromActiveStreams(stream, itr);
                    }
                });
            }
        }
        
        public Http2Stream forEachActiveStream(final Http2StreamVisitor visitor) throws Http2Exception {
            this.incrementPendingIterations();
            try {
                for (final Http2Stream stream : this.streams) {
                    if (!visitor.visit(stream)) {
                        return stream;
                    }
                }
                return null;
            }
            finally {
                this.decrementPendingIterations();
            }
        }
        
        void addToActiveStreams(final DefaultStream stream) {
            if (this.streams.add(stream)) {
                final DefaultEndpoint<? extends Http2FlowController> createdBy = stream.createdBy();
                ++createdBy.numActiveStreams;
                for (int i = 0; i < this.listeners.size(); ++i) {
                    try {
                        ((Listener)this.listeners.get(i)).onStreamActive(stream);
                    }
                    catch (Throwable cause) {
                        DefaultHttp2Connection.logger.error("Caught Throwable from listener onStreamActive.", cause);
                    }
                }
            }
        }
        
        void removeFromActiveStreams(final DefaultStream stream, final Iterator<?> itr) {
            if (this.streams.remove(stream)) {
                final DefaultEndpoint<? extends Http2FlowController> createdBy = stream.createdBy();
                --createdBy.numActiveStreams;
                DefaultHttp2Connection.this.notifyClosed(stream);
            }
            DefaultHttp2Connection.this.removeStream(stream, itr);
        }
        
        boolean allowModifications() {
            return this.pendingIterations == 0;
        }
        
        void incrementPendingIterations() {
            ++this.pendingIterations;
        }
        
        void decrementPendingIterations() {
            --this.pendingIterations;
            if (this.allowModifications()) {
                while (true) {
                    final Event event = (Event)this.pendingEvents.poll();
                    if (event == null) {
                        break;
                    }
                    try {
                        event.process();
                    }
                    catch (Throwable cause) {
                        DefaultHttp2Connection.logger.error("Caught Throwable while processing pending ActiveStreams$Event.", cause);
                    }
                }
            }
        }
    }
    
    final class DefaultPropertyKey implements PropertyKey {
        final int index;
        
        DefaultPropertyKey(final int index) {
            this.index = index;
        }
        
        DefaultPropertyKey verifyConnection(final Http2Connection connection) {
            if (connection != DefaultHttp2Connection.this) {
                throw new IllegalArgumentException("Using a key that was not created by this connection");
            }
            return this;
        }
    }
    
    private final class PropertyKeyRegistry {
        final List<DefaultPropertyKey> keys;
        
        private PropertyKeyRegistry() {
            this.keys = (List<DefaultPropertyKey>)new ArrayList(4);
        }
        
        DefaultPropertyKey newKey() {
            final DefaultPropertyKey key = new DefaultPropertyKey(this.keys.size());
            this.keys.add(key);
            return key;
        }
        
        int size() {
            return this.keys.size();
        }
    }
    
    interface Event {
        void process();
    }
}
