package io.netty.handler.ssl;

import java.security.Principal;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSessionBindingEvent;
import javax.net.ssl.SSLSessionBindingListener;
import java.util.HashMap;
import javax.net.ssl.SSLSessionContext;
import java.util.Map;
import javax.security.cert.X509Certificate;
import io.netty.util.ResourceLeakDetectorFactory;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.internal.tcnative.Buffer;
import java.util.Iterator;
import javax.net.ssl.SSLParameters;
import java.util.Arrays;
import java.util.ArrayList;
import io.netty.util.internal.EmptyArrays;
import java.nio.ReadOnlyBufferException;
import io.netty.buffer.ByteBuf;
import javax.net.ssl.SSLSession;
import java.util.concurrent.locks.Lock;
import io.netty.util.internal.PlatformDependent;
import io.netty.internal.tcnative.SSL;
import io.netty.util.internal.ObjectUtil;
import javax.net.ssl.SSLHandshakeException;
import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import io.netty.buffer.ByteBufAllocator;
import java.util.Collection;
import java.util.List;
import io.netty.util.AbstractReferenceCounted;
import io.netty.util.ResourceLeakTracker;
import javax.net.ssl.SSLEngineResult;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import io.netty.util.ResourceLeakDetector;
import javax.net.ssl.SSLException;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.ReferenceCounted;
import javax.net.ssl.SSLEngine;

public class ReferenceCountedOpenSslEngine extends SSLEngine implements ReferenceCounted, ApplicationProtocolAccessor {
    private static final InternalLogger logger;
    private static final SSLException BEGIN_HANDSHAKE_ENGINE_CLOSED;
    private static final SSLException HANDSHAKE_ENGINE_CLOSED;
    private static final SSLException RENEGOTIATION_UNSUPPORTED;
    private static final ResourceLeakDetector<ReferenceCountedOpenSslEngine> leakDetector;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV2 = 0;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_SSLV3 = 1;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1 = 2;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_1 = 3;
    private static final int OPENSSL_OP_NO_PROTOCOL_INDEX_TLSv1_2 = 4;
    private static final int[] OPENSSL_OP_NO_PROTOCOLS;
    private static final int DEFAULT_HOSTNAME_VALIDATION_FLAGS = 0;
    static final int MAX_PLAINTEXT_LENGTH;
    private static final int MAX_RECORD_SIZE;
    private static final AtomicIntegerFieldUpdater<ReferenceCountedOpenSslEngine> DESTROYED_UPDATER;
    private static final String INVALID_CIPHER = "SSL_NULL_WITH_NULL_NULL";
    private static final SSLEngineResult NEED_UNWRAP_OK;
    private static final SSLEngineResult NEED_UNWRAP_CLOSED;
    private static final SSLEngineResult NEED_WRAP_OK;
    private static final SSLEngineResult NEED_WRAP_CLOSED;
    private static final SSLEngineResult CLOSED_NOT_HANDSHAKING;
    private long ssl;
    private long networkBIO;
    private boolean certificateSet;
    private HandshakeState handshakeState;
    private boolean receivedShutdown;
    private volatile int destroyed;
    private volatile String applicationProtocol;
    private final ResourceLeakTracker<ReferenceCountedOpenSslEngine> leak;
    private final AbstractReferenceCounted refCnt;
    private volatile ClientAuth clientAuth;
    private volatile long lastAccessed;
    private String endPointIdentificationAlgorithm;
    private Object algorithmConstraints;
    private List<String> sniHostNames;
    private volatile Collection<?> matchers;
    private boolean isInboundDone;
    private boolean outboundClosed;
    final boolean jdkCompatibilityMode;
    private final boolean clientMode;
    private final ByteBufAllocator alloc;
    private final OpenSslEngineMap engineMap;
    private final OpenSslApplicationProtocolNegotiator apn;
    private final OpenSslSession session;
    private final Certificate[] localCerts;
    private final ByteBuffer[] singleSrcBuffer;
    private final ByteBuffer[] singleDstBuffer;
    private final OpenSslKeyMaterialManager keyMaterialManager;
    private final boolean enableOcsp;
    private int maxWrapOverhead;
    private int maxWrapBufferSize;
    SSLHandshakeException handshakeException;
    
    ReferenceCountedOpenSslEngine(final ReferenceCountedOpenSslContext context, final ByteBufAllocator alloc, final String peerHost, final int peerPort, final boolean jdkCompatibilityMode, final boolean leakDetection) {
        super(peerHost, peerPort);
        this.handshakeState = HandshakeState.NOT_STARTED;
        this.refCnt = new AbstractReferenceCounted() {
            public ReferenceCounted touch(final Object hint) {
                if (ReferenceCountedOpenSslEngine.this.leak != null) {
                    ReferenceCountedOpenSslEngine.this.leak.record(hint);
                }
                return ReferenceCountedOpenSslEngine.this;
            }
            
            @Override
            protected void deallocate() {
                ReferenceCountedOpenSslEngine.this.shutdown();
                if (ReferenceCountedOpenSslEngine.this.leak != null) {
                    final boolean closed = ReferenceCountedOpenSslEngine.this.leak.close(ReferenceCountedOpenSslEngine.this);
                    assert closed;
                }
            }
        };
        this.clientAuth = ClientAuth.NONE;
        this.lastAccessed = -1L;
        this.singleSrcBuffer = new ByteBuffer[1];
        this.singleDstBuffer = new ByteBuffer[1];
        OpenSsl.ensureAvailability();
        this.alloc = ObjectUtil.<ByteBufAllocator>checkNotNull(alloc, "alloc");
        this.apn = (OpenSslApplicationProtocolNegotiator)context.applicationProtocolNegotiator();
        this.session = new OpenSslSession(context.sessionContext());
        this.clientMode = context.isClient();
        this.engineMap = context.engineMap;
        this.localCerts = context.keyCertChain;
        this.keyMaterialManager = context.keyMaterialManager();
        this.enableOcsp = context.enableOcsp;
        this.jdkCompatibilityMode = jdkCompatibilityMode;
        final Lock readerLock = context.ctxLock.readLock();
        readerLock.lock();
        long finalSsl;
        try {
            finalSsl = SSL.newSSL(context.ctx, !context.isClient());
        }
        finally {
            readerLock.unlock();
        }
        synchronized (this) {
            this.ssl = finalSsl;
            try {
                this.networkBIO = SSL.bioNewByteBuffer(this.ssl, context.getBioNonApplicationBufferSize());
                this.setClientAuth(this.clientMode ? ClientAuth.NONE : context.clientAuth);
                if (context.protocols != null) {
                    this.setEnabledProtocols(context.protocols);
                }
                if (this.clientMode && peerHost != null) {
                    SSL.setTlsExtHostName(this.ssl, peerHost);
                }
                if (this.enableOcsp) {
                    SSL.enableOcsp(this.ssl);
                }
                if (!jdkCompatibilityMode) {
                    SSL.setMode(this.ssl, SSL.getMode(this.ssl) | SSL.SSL_MODE_ENABLE_PARTIAL_WRITE);
                }
                this.calculateMaxWrapOverhead();
            }
            catch (Throwable cause) {
                SSL.freeSSL(this.ssl);
                PlatformDependent.throwException(cause);
            }
        }
        this.leak = (leakDetection ? ReferenceCountedOpenSslEngine.leakDetector.track(this) : null);
    }
    
    public void setOcspResponse(final byte[] response) {
        if (!this.enableOcsp) {
            throw new IllegalStateException("OCSP stapling is not enabled");
        }
        if (this.clientMode) {
            throw new IllegalStateException("Not a server SSLEngine");
        }
        synchronized (this) {
            SSL.setOcspResponse(this.ssl, response);
        }
    }
    
    public byte[] getOcspResponse() {
        if (!this.enableOcsp) {
            throw new IllegalStateException("OCSP stapling is not enabled");
        }
        if (!this.clientMode) {
            throw new IllegalStateException("Not a client SSLEngine");
        }
        synchronized (this) {
            return SSL.getOcspResponse(this.ssl);
        }
    }
    
    public final int refCnt() {
        return this.refCnt.refCnt();
    }
    
    public final ReferenceCounted retain() {
        this.refCnt.retain();
        return this;
    }
    
    public final ReferenceCounted retain(final int increment) {
        this.refCnt.retain(increment);
        return this;
    }
    
    public final ReferenceCounted touch() {
        this.refCnt.touch();
        return this;
    }
    
    public final ReferenceCounted touch(final Object hint) {
        this.refCnt.touch(hint);
        return this;
    }
    
    public final boolean release() {
        return this.refCnt.release();
    }
    
    public final boolean release(final int decrement) {
        return this.refCnt.release(decrement);
    }
    
    public final synchronized SSLSession getHandshakeSession() {
        switch (this.handshakeState) {
            case NOT_STARTED:
            case FINISHED: {
                return null;
            }
            default: {
                return (SSLSession)this.session;
            }
        }
    }
    
    public final synchronized long sslPointer() {
        return this.ssl;
    }
    
    public final synchronized void shutdown() {
        if (ReferenceCountedOpenSslEngine.DESTROYED_UPDATER.compareAndSet(this, 0, 1)) {
            this.engineMap.remove(this.ssl);
            SSL.freeSSL(this.ssl);
            final long n = 0L;
            this.networkBIO = n;
            this.ssl = n;
            final boolean b = true;
            this.outboundClosed = b;
            this.isInboundDone = b;
        }
        SSL.clearError();
    }
    
    private int writePlaintextData(final ByteBuffer src, final int len) {
        final int pos = src.position();
        final int limit = src.limit();
        int sslWrote;
        if (src.isDirect()) {
            sslWrote = SSL.writeToSSL(this.ssl, bufferAddress(src) + pos, len);
            if (sslWrote > 0) {
                src.position(pos + sslWrote);
            }
        }
        else {
            final ByteBuf buf = this.alloc.directBuffer(len);
            try {
                src.limit(pos + len);
                buf.setBytes(0, src);
                src.limit(limit);
                sslWrote = SSL.writeToSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
                if (sslWrote > 0) {
                    src.position(pos + sslWrote);
                }
                else {
                    src.position(pos);
                }
            }
            finally {
                buf.release();
            }
        }
        return sslWrote;
    }
    
    private ByteBuf writeEncryptedData(final ByteBuffer src, final int len) {
        final int pos = src.position();
        if (src.isDirect()) {
            SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(src) + pos, len, false);
        }
        else {
            final ByteBuf buf = this.alloc.directBuffer(len);
            try {
                final int limit = src.limit();
                src.limit(pos + len);
                buf.writeBytes(src);
                src.position(pos);
                src.limit(limit);
                SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(buf), len, false);
                return buf;
            }
            catch (Throwable cause) {
                buf.release();
                PlatformDependent.throwException(cause);
            }
        }
        return null;
    }
    
    private int readPlaintextData(final ByteBuffer dst) {
        final int pos = dst.position();
        int sslRead;
        if (dst.isDirect()) {
            sslRead = SSL.readFromSSL(this.ssl, bufferAddress(dst) + pos, dst.limit() - pos);
            if (sslRead > 0) {
                dst.position(pos + sslRead);
            }
        }
        else {
            final int limit = dst.limit();
            final int len = Math.min(this.maxEncryptedPacketLength0(), limit - pos);
            final ByteBuf buf = this.alloc.directBuffer(len);
            try {
                sslRead = SSL.readFromSSL(this.ssl, OpenSsl.memoryAddress(buf), len);
                if (sslRead > 0) {
                    dst.limit(pos + sslRead);
                    buf.getBytes(buf.readerIndex(), dst);
                    dst.limit(limit);
                }
            }
            finally {
                buf.release();
            }
        }
        return sslRead;
    }
    
    final synchronized int maxWrapOverhead() {
        return this.maxWrapOverhead;
    }
    
    final synchronized int maxEncryptedPacketLength() {
        return this.maxEncryptedPacketLength0();
    }
    
    final int maxEncryptedPacketLength0() {
        return this.maxWrapOverhead + ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
    }
    
    final int calculateMaxLengthForWrap(final int plaintextLength, final int numComponents) {
        return (int)Math.min((long)this.maxWrapBufferSize, plaintextLength + this.maxWrapOverhead * (long)numComponents);
    }
    
    final synchronized int sslPending() {
        return this.sslPending0();
    }
    
    private void calculateMaxWrapOverhead() {
        this.maxWrapOverhead = SSL.getMaxWrapOverhead(this.ssl);
        this.maxWrapBufferSize = (this.jdkCompatibilityMode ? this.maxEncryptedPacketLength0() : (this.maxEncryptedPacketLength0() << 4));
    }
    
    private int sslPending0() {
        return (this.handshakeState != HandshakeState.FINISHED) ? 0 : SSL.sslPending(this.ssl);
    }
    
    private boolean isBytesAvailableEnoughForWrap(final int bytesAvailable, final int plaintextLength, final int numComponents) {
        return bytesAvailable - this.maxWrapOverhead * (long)numComponents >= plaintextLength;
    }
    
    public final SSLEngineResult wrap(final ByteBuffer[] srcs, int offset, final int length, final ByteBuffer dst) throws SSLException {
        if (srcs == null) {
            throw new IllegalArgumentException("srcs is null");
        }
        if (dst == null) {
            throw new IllegalArgumentException("dst is null");
        }
        if (offset >= srcs.length || offset + length > srcs.length) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("offset: ").append(offset).append(", length: ").append(length).append(" (expected: offset <= offset + length <= srcs.length (").append(srcs.length).append("))").toString());
        }
        if (dst.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        synchronized (this) {
            if (this.isOutboundDone()) {
                return (this.isInboundDone() || this.isDestroyed()) ? ReferenceCountedOpenSslEngine.CLOSED_NOT_HANDSHAKING : ReferenceCountedOpenSslEngine.NEED_UNWRAP_CLOSED;
            }
            int bytesProduced = 0;
            ByteBuf bioReadCopyBuf = null;
            try {
                if (dst.isDirect()) {
                    SSL.bioSetByteBuffer(this.networkBIO, bufferAddress(dst) + dst.position(), dst.remaining(), true);
                }
                else {
                    bioReadCopyBuf = this.alloc.directBuffer(dst.remaining());
                    SSL.bioSetByteBuffer(this.networkBIO, OpenSsl.memoryAddress(bioReadCopyBuf), bioReadCopyBuf.writableBytes(), true);
                }
                int bioLengthBefore = SSL.bioLengthByteBuffer(this.networkBIO);
                if (!this.outboundClosed) {
                    SSLEngineResult.HandshakeStatus status = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
                    if (this.handshakeState != HandshakeState.FINISHED) {
                        if (this.handshakeState != HandshakeState.STARTED_EXPLICITLY) {
                            this.handshakeState = HandshakeState.STARTED_IMPLICITLY;
                        }
                        bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
                        if (bytesProduced > 0 && this.handshakeException != null) {
                            return this.newResult(SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, bytesProduced);
                        }
                        status = this.handshake();
                        bytesProduced = bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
                        if (bytesProduced > 0) {
                            return this.newResult(this.mayFinishHandshake((status != SSLEngineResult.HandshakeStatus.FINISHED) ? ((bytesProduced == bioLengthBefore) ? SSLEngineResult.HandshakeStatus.NEED_WRAP : this.getHandshakeStatus(SSL.bioLengthNonApplication(this.networkBIO))) : SSLEngineResult.HandshakeStatus.FINISHED), 0, bytesProduced);
                        }
                        if (status == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
                            return this.isOutboundDone() ? ReferenceCountedOpenSslEngine.NEED_UNWRAP_CLOSED : ReferenceCountedOpenSslEngine.NEED_UNWRAP_OK;
                        }
                        if (this.outboundClosed) {
                            bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
                            return this.newResultMayFinishHandshake(status, 0, bytesProduced);
                        }
                    }
                    final int endOffset = offset + length;
                    if (this.jdkCompatibilityMode) {
                        int srcsLen = 0;
                        for (int i = offset; i < endOffset; ++i) {
                            final ByteBuffer src = srcs[i];
                            if (src == null) {
                                throw new IllegalArgumentException(new StringBuilder().append("srcs[").append(i).append("] is null").toString());
                            }
                            if (srcsLen != ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH) {
                                srcsLen += src.remaining();
                                if (srcsLen > ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH || srcsLen < 0) {
                                    srcsLen = ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
                                }
                            }
                        }
                        if (!this.isBytesAvailableEnoughForWrap(dst.remaining(), srcsLen, 1)) {
                            return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), 0, 0);
                        }
                    }
                    int bytesConsumed = 0;
                    bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
                    while (offset < endOffset) {
                        final ByteBuffer src2 = srcs[offset];
                        final int remaining = src2.remaining();
                        if (remaining != 0) {
                            int bytesWritten;
                            if (this.jdkCompatibilityMode) {
                                bytesWritten = this.writePlaintextData(src2, Math.min(remaining, ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH - bytesConsumed));
                            }
                            else {
                                final int availableCapacityForWrap = dst.remaining() - bytesProduced - this.maxWrapOverhead;
                                if (availableCapacityForWrap <= 0) {
                                    return new SSLEngineResult(SSLEngineResult.Status.BUFFER_OVERFLOW, this.getHandshakeStatus(), bytesConsumed, bytesProduced);
                                }
                                bytesWritten = this.writePlaintextData(src2, Math.min(remaining, availableCapacityForWrap));
                            }
                            if (bytesWritten > 0) {
                                bytesConsumed += bytesWritten;
                                final int pendingNow = SSL.bioLengthByteBuffer(this.networkBIO);
                                bytesProduced += bioLengthBefore - pendingNow;
                                bioLengthBefore = pendingNow;
                                if (this.jdkCompatibilityMode || bytesProduced == dst.remaining()) {
                                    return this.newResultMayFinishHandshake(status, bytesConsumed, bytesProduced);
                                }
                            }
                            else {
                                final int sslError = SSL.getError(this.ssl, bytesWritten);
                                if (sslError == SSL.SSL_ERROR_ZERO_RETURN) {
                                    if (!this.receivedShutdown) {
                                        this.closeAll();
                                        bytesProduced += bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
                                        final SSLEngineResult.HandshakeStatus hs = this.mayFinishHandshake((status != SSLEngineResult.HandshakeStatus.FINISHED) ? ((bytesProduced == dst.remaining()) ? SSLEngineResult.HandshakeStatus.NEED_WRAP : this.getHandshakeStatus(SSL.bioLengthNonApplication(this.networkBIO))) : SSLEngineResult.HandshakeStatus.FINISHED);
                                        return this.newResult(hs, bytesConsumed, bytesProduced);
                                    }
                                    return this.newResult(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, bytesConsumed, bytesProduced);
                                }
                                else {
                                    if (sslError == SSL.SSL_ERROR_WANT_READ) {
                                        return this.newResult(SSLEngineResult.HandshakeStatus.NEED_UNWRAP, bytesConsumed, bytesProduced);
                                    }
                                    if (sslError == SSL.SSL_ERROR_WANT_WRITE) {
                                        return this.newResult(SSLEngineResult.Status.BUFFER_OVERFLOW, status, bytesConsumed, bytesProduced);
                                    }
                                    throw this.shutdownWithError("SSL_write");
                                }
                            }
                        }
                        ++offset;
                    }
                    return this.newResultMayFinishHandshake(status, bytesConsumed, bytesProduced);
                }
                bytesProduced = SSL.bioFlushByteBuffer(this.networkBIO);
                if (bytesProduced <= 0) {
                    return this.newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
                }
                if (!this.doSSLShutdown()) {
                    return this.newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, bytesProduced);
                }
                bytesProduced = bioLengthBefore - SSL.bioLengthByteBuffer(this.networkBIO);
                return this.newResultMayFinishHandshake(SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, bytesProduced);
            }
            finally {
                SSL.bioClearByteBuffer(this.networkBIO);
                if (bioReadCopyBuf == null) {
                    dst.position(dst.position() + bytesProduced);
                }
                else {
                    assert bioReadCopyBuf.readableBytes() <= dst.remaining() : new StringBuilder().append("The destination buffer ").append(dst).append(" didn't have enough remaining space to hold the encrypted content in ").append(bioReadCopyBuf).toString();
                    dst.put(bioReadCopyBuf.internalNioBuffer(bioReadCopyBuf.readerIndex(), bytesProduced));
                    bioReadCopyBuf.release();
                }
            }
        }
    }
    
    private SSLEngineResult newResult(final SSLEngineResult.HandshakeStatus hs, final int bytesConsumed, final int bytesProduced) {
        return this.newResult(SSLEngineResult.Status.OK, hs, bytesConsumed, bytesProduced);
    }
    
    private SSLEngineResult newResult(final SSLEngineResult.Status status, SSLEngineResult.HandshakeStatus hs, final int bytesConsumed, final int bytesProduced) {
        if (this.isOutboundDone()) {
            if (this.isInboundDone()) {
                hs = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
                this.shutdown();
            }
            return new SSLEngineResult(SSLEngineResult.Status.CLOSED, hs, bytesConsumed, bytesProduced);
        }
        return new SSLEngineResult(status, hs, bytesConsumed, bytesProduced);
    }
    
    private SSLEngineResult newResultMayFinishHandshake(final SSLEngineResult.HandshakeStatus hs, final int bytesConsumed, final int bytesProduced) throws SSLException {
        return this.newResult(this.mayFinishHandshake((hs != SSLEngineResult.HandshakeStatus.FINISHED) ? this.getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED), bytesConsumed, bytesProduced);
    }
    
    private SSLEngineResult newResultMayFinishHandshake(final SSLEngineResult.Status status, final SSLEngineResult.HandshakeStatus hs, final int bytesConsumed, final int bytesProduced) throws SSLException {
        return this.newResult(status, this.mayFinishHandshake((hs != SSLEngineResult.HandshakeStatus.FINISHED) ? this.getHandshakeStatus() : SSLEngineResult.HandshakeStatus.FINISHED), bytesConsumed, bytesProduced);
    }
    
    private SSLException shutdownWithError(final String operations) {
        final String err = SSL.getLastError();
        return this.shutdownWithError(operations, err);
    }
    
    private SSLException shutdownWithError(final String operation, final String err) {
        if (ReferenceCountedOpenSslEngine.logger.isDebugEnabled()) {
            ReferenceCountedOpenSslEngine.logger.debug("{} failed: OpenSSL error: {}", operation, err);
        }
        this.shutdown();
        if (this.handshakeState == HandshakeState.FINISHED) {
            return new SSLException(err);
        }
        return (SSLException)new SSLHandshakeException(err);
    }
    
    public final SSLEngineResult unwrap(final ByteBuffer[] srcs, int srcsOffset, final int srcsLength, final ByteBuffer[] dsts, int dstsOffset, final int dstsLength) throws SSLException {
        if (srcs == null) {
            throw new NullPointerException("srcs");
        }
        if (srcsOffset >= srcs.length || srcsOffset + srcsLength > srcs.length) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("offset: ").append(srcsOffset).append(", length: ").append(srcsLength).append(" (expected: offset <= offset + length <= srcs.length (").append(srcs.length).append("))").toString());
        }
        if (dsts == null) {
            throw new IllegalArgumentException("dsts is null");
        }
        if (dstsOffset >= dsts.length || dstsOffset + dstsLength > dsts.length) {
            throw new IndexOutOfBoundsException(new StringBuilder().append("offset: ").append(dstsOffset).append(", length: ").append(dstsLength).append(" (expected: offset <= offset + length <= dsts.length (").append(dsts.length).append("))").toString());
        }
        long capacity = 0L;
        final int dstsEndOffset = dstsOffset + dstsLength;
        for (int i = dstsOffset; i < dstsEndOffset; ++i) {
            final ByteBuffer dst = dsts[i];
            if (dst == null) {
                throw new IllegalArgumentException(new StringBuilder().append("dsts[").append(i).append("] is null").toString());
            }
            if (dst.isReadOnly()) {
                throw new ReadOnlyBufferException();
            }
            capacity += dst.remaining();
        }
        final int srcsEndOffset = srcsOffset + srcsLength;
        long len = 0L;
        for (int j = srcsOffset; j < srcsEndOffset; ++j) {
            final ByteBuffer src = srcs[j];
            if (src == null) {
                throw new IllegalArgumentException(new StringBuilder().append("srcs[").append(j).append("] is null").toString());
            }
            len += src.remaining();
        }
        synchronized (this) {
            if (this.isInboundDone()) {
                return (this.isOutboundDone() || this.isDestroyed()) ? ReferenceCountedOpenSslEngine.CLOSED_NOT_HANDSHAKING : ReferenceCountedOpenSslEngine.NEED_WRAP_CLOSED;
            }
            SSLEngineResult.HandshakeStatus status = SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
            if (this.handshakeState != HandshakeState.FINISHED) {
                if (this.handshakeState != HandshakeState.STARTED_EXPLICITLY) {
                    this.handshakeState = HandshakeState.STARTED_IMPLICITLY;
                }
                status = this.handshake();
                if (status == SSLEngineResult.HandshakeStatus.NEED_WRAP) {
                    return ReferenceCountedOpenSslEngine.NEED_WRAP_OK;
                }
                if (this.isInboundDone) {
                    return ReferenceCountedOpenSslEngine.NEED_WRAP_CLOSED;
                }
            }
            int sslPending = this.sslPending0();
            int packetLength;
            if (this.jdkCompatibilityMode) {
                if (len < 5L) {
                    return this.newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0);
                }
                packetLength = SslUtils.getEncryptedPacketLength(srcs, srcsOffset);
                if (packetLength == -2) {
                    throw new NotSslRecordException("not an SSL/TLS record");
                }
                final int packetLengthDataOnly = packetLength - 5;
                if (packetLengthDataOnly > capacity) {
                    if (packetLengthDataOnly > ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE) {
                        throw new SSLException(new StringBuilder().append("Illegal packet length: ").append(packetLengthDataOnly).append(" > ").append(this.session.getApplicationBufferSize()).toString());
                    }
                    this.session.tryExpandApplicationBufferSize(packetLengthDataOnly);
                    return this.newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_OVERFLOW, status, 0, 0);
                }
                else if (len < packetLength) {
                    return this.newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0);
                }
            }
            else {
                if (len == 0L && sslPending <= 0) {
                    return this.newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_UNDERFLOW, status, 0, 0);
                }
                if (capacity == 0L) {
                    return this.newResultMayFinishHandshake(SSLEngineResult.Status.BUFFER_OVERFLOW, status, 0, 0);
                }
                packetLength = (int)Math.min(2147483647L, len);
            }
            assert srcsOffset < srcsEndOffset;
            assert capacity > 0L;
            int bytesProduced = 0;
            int bytesConsumed = 0;
            try {
            Label_1259:
                while (true) {
                    final ByteBuffer src2 = srcs[srcsOffset];
                    final int remaining = src2.remaining();
                    Label_0821: {
                        ByteBuf bioWriteCopyBuf;
                        int pendingEncryptedBytes;
                        if (remaining == 0) {
                            if (sslPending > 0) {
                                bioWriteCopyBuf = null;
                                pendingEncryptedBytes = SSL.bioLengthByteBuffer(this.networkBIO);
                                break Label_0821;
                            }
                            if (++srcsOffset >= srcsEndOffset) {
                                break;
                            }
                            continue;
                        }
                        else {
                            pendingEncryptedBytes = Math.min(packetLength, remaining);
                            bioWriteCopyBuf = this.writeEncryptedData(src2, pendingEncryptedBytes);
                        }
                        try {
                            while (true) {
                                final ByteBuffer dst2 = dsts[dstsOffset];
                                if (!dst2.hasRemaining()) {
                                    if (++dstsOffset >= dstsEndOffset) {
                                        break Label_1259;
                                    }
                                    continue;
                                }
                                else {
                                    final int bytesRead = this.readPlaintextData(dst2);
                                    final int localBytesConsumed = pendingEncryptedBytes - SSL.bioLengthByteBuffer(this.networkBIO);
                                    bytesConsumed += localBytesConsumed;
                                    packetLength -= localBytesConsumed;
                                    pendingEncryptedBytes -= localBytesConsumed;
                                    src2.position(src2.position() + localBytesConsumed);
                                    if (bytesRead > 0) {
                                        bytesProduced += bytesRead;
                                        if (!dst2.hasRemaining()) {
                                            sslPending = this.sslPending0();
                                            if (++dstsOffset >= dstsEndOffset) {
                                                return (sslPending > 0) ? this.newResult(SSLEngineResult.Status.BUFFER_OVERFLOW, status, bytesConsumed, bytesProduced) : this.newResultMayFinishHandshake(this.isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
                                            }
                                            continue;
                                        }
                                        else {
                                            if (packetLength == 0 || this.jdkCompatibilityMode) {
                                                break Label_1259;
                                            }
                                            continue;
                                        }
                                    }
                                    else {
                                        final int sslError = SSL.getError(this.ssl, bytesRead);
                                        if (sslError != SSL.SSL_ERROR_WANT_READ && sslError != SSL.SSL_ERROR_WANT_WRITE) {
                                            if (sslError == SSL.SSL_ERROR_ZERO_RETURN) {
                                                if (!this.receivedShutdown) {
                                                    this.closeAll();
                                                }
                                                return this.newResultMayFinishHandshake(this.isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
                                            }
                                            return this.sslReadErrorResult(SSL.getLastErrorNumber(), bytesConsumed, bytesProduced);
                                        }
                                        else {
                                            if (++srcsOffset >= srcsEndOffset) {
                                                break Label_1259;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        finally {
                            if (bioWriteCopyBuf != null) {
                                bioWriteCopyBuf.release();
                            }
                        }
                    }
                }
            }
            finally {
                SSL.bioClearByteBuffer(this.networkBIO);
                this.rejectRemoteInitiatedRenegotiation();
            }
            if (!this.receivedShutdown && (SSL.getShutdown(this.ssl) & SSL.SSL_RECEIVED_SHUTDOWN) == SSL.SSL_RECEIVED_SHUTDOWN) {
                this.closeAll();
            }
            return this.newResultMayFinishHandshake(this.isInboundDone() ? SSLEngineResult.Status.CLOSED : SSLEngineResult.Status.OK, status, bytesConsumed, bytesProduced);
        }
    }
    
    private SSLEngineResult sslReadErrorResult(final int err, final int bytesConsumed, final int bytesProduced) throws SSLException {
        final String errStr = SSL.getErrorString((long)err);
        if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
            if (this.handshakeException == null && this.handshakeState != HandshakeState.FINISHED) {
                this.handshakeException = new SSLHandshakeException(errStr);
            }
            return new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, bytesConsumed, bytesProduced);
        }
        throw this.shutdownWithError("SSL_read", errStr);
    }
    
    private void closeAll() throws SSLException {
        this.receivedShutdown = true;
        this.closeOutbound();
        this.closeInbound();
    }
    
    private void rejectRemoteInitiatedRenegotiation() throws SSLHandshakeException {
        if (!this.isDestroyed() && SSL.getHandshakeCount(this.ssl) > 1) {
            this.shutdown();
            throw new SSLHandshakeException("remote-initiated renegotiation not allowed");
        }
    }
    
    public final SSLEngineResult unwrap(final ByteBuffer[] srcs, final ByteBuffer[] dsts) throws SSLException {
        return this.unwrap(srcs, 0, srcs.length, dsts, 0, dsts.length);
    }
    
    private ByteBuffer[] singleSrcBuffer(final ByteBuffer src) {
        this.singleSrcBuffer[0] = src;
        return this.singleSrcBuffer;
    }
    
    private void resetSingleSrcBuffer() {
        this.singleSrcBuffer[0] = null;
    }
    
    private ByteBuffer[] singleDstBuffer(final ByteBuffer src) {
        this.singleDstBuffer[0] = src;
        return this.singleDstBuffer;
    }
    
    private void resetSingleDstBuffer() {
        this.singleDstBuffer[0] = null;
    }
    
    public final synchronized SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer[] dsts, final int offset, final int length) throws SSLException {
        try {
            return this.unwrap(this.singleSrcBuffer(src), 0, 1, dsts, offset, length);
        }
        finally {
            this.resetSingleSrcBuffer();
        }
    }
    
    public final synchronized SSLEngineResult wrap(final ByteBuffer src, final ByteBuffer dst) throws SSLException {
        try {
            return this.wrap(this.singleSrcBuffer(src), dst);
        }
        finally {
            this.resetSingleSrcBuffer();
        }
    }
    
    public final synchronized SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer dst) throws SSLException {
        try {
            return this.unwrap(this.singleSrcBuffer(src), this.singleDstBuffer(dst));
        }
        finally {
            this.resetSingleSrcBuffer();
            this.resetSingleDstBuffer();
        }
    }
    
    public final synchronized SSLEngineResult unwrap(final ByteBuffer src, final ByteBuffer[] dsts) throws SSLException {
        try {
            return this.unwrap(this.singleSrcBuffer(src), dsts);
        }
        finally {
            this.resetSingleSrcBuffer();
        }
    }
    
    public final Runnable getDelegatedTask() {
        return null;
    }
    
    public final synchronized void closeInbound() throws SSLException {
        if (this.isInboundDone) {
            return;
        }
        this.isInboundDone = true;
        if (this.isOutboundDone()) {
            this.shutdown();
        }
        if (this.handshakeState != HandshakeState.NOT_STARTED && !this.receivedShutdown) {
            throw new SSLException("Inbound closed before receiving peer's close_notify: possible truncation attack?");
        }
    }
    
    public final synchronized boolean isInboundDone() {
        return this.isInboundDone;
    }
    
    public final synchronized void closeOutbound() {
        if (this.outboundClosed) {
            return;
        }
        this.outboundClosed = true;
        if (this.handshakeState != HandshakeState.NOT_STARTED && !this.isDestroyed()) {
            final int mode = SSL.getShutdown(this.ssl);
            if ((mode & SSL.SSL_SENT_SHUTDOWN) != SSL.SSL_SENT_SHUTDOWN) {
                this.doSSLShutdown();
            }
        }
        else {
            this.shutdown();
        }
    }
    
    private boolean doSSLShutdown() {
        if (SSL.isInInit(this.ssl) != 0) {
            return false;
        }
        final int err = SSL.shutdownSSL(this.ssl);
        if (err < 0) {
            final int sslErr = SSL.getError(this.ssl, err);
            if (sslErr == SSL.SSL_ERROR_SYSCALL || sslErr == SSL.SSL_ERROR_SSL) {
                if (ReferenceCountedOpenSslEngine.logger.isDebugEnabled()) {
                    ReferenceCountedOpenSslEngine.logger.debug("SSL_shutdown failed: OpenSSL error: {}", SSL.getLastError());
                }
                this.shutdown();
                return false;
            }
            SSL.clearError();
        }
        return true;
    }
    
    public final synchronized boolean isOutboundDone() {
        return this.outboundClosed && (this.networkBIO == 0L || SSL.bioLengthNonApplication(this.networkBIO) == 0);
    }
    
    public final String[] getSupportedCipherSuites() {
        return (String[])OpenSsl.AVAILABLE_CIPHER_SUITES.toArray((Object[])new String[OpenSsl.AVAILABLE_CIPHER_SUITES.size()]);
    }
    
    public final String[] getEnabledCipherSuites() {
        final String[] enabled;
        synchronized (this) {
            if (this.isDestroyed()) {
                return EmptyArrays.EMPTY_STRINGS;
            }
            enabled = SSL.getCiphers(this.ssl);
        }
        if (enabled == null) {
            return EmptyArrays.EMPTY_STRINGS;
        }
        synchronized (this) {
            for (int i = 0; i < enabled.length; ++i) {
                final String mapped = this.toJavaCipherSuite(enabled[i]);
                if (mapped != null) {
                    enabled[i] = mapped;
                }
            }
        }
        return enabled;
    }
    
    public final void setEnabledCipherSuites(final String[] cipherSuites) {
        ObjectUtil.<String[]>checkNotNull(cipherSuites, "cipherSuites");
        final StringBuilder buf = new StringBuilder();
        for (final String c : cipherSuites) {
            if (c == null) {
                break;
            }
            String converted = CipherSuiteConverter.toOpenSsl(c);
            if (converted == null) {
                converted = c;
            }
            if (!OpenSsl.isCipherSuiteAvailable(converted)) {
                throw new IllegalArgumentException("unsupported cipher suite: " + c + '(' + converted + ')');
            }
            buf.append(converted);
            buf.append(':');
        }
        if (buf.length() == 0) {
            throw new IllegalArgumentException("empty cipher suites");
        }
        buf.setLength(buf.length() - 1);
        final String cipherSuiteSpec = buf.toString();
        synchronized (this) {
            if (!this.isDestroyed()) {
                try {
                    SSL.setCipherSuites(this.ssl, cipherSuiteSpec);
                    return;
                }
                catch (Exception e) {
                    throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec, (Throwable)e);
                }
                throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec);
            }
            throw new IllegalStateException("failed to enable cipher suites: " + cipherSuiteSpec);
        }
    }
    
    public final String[] getSupportedProtocols() {
        return (String[])OpenSsl.SUPPORTED_PROTOCOLS_SET.toArray((Object[])new String[OpenSsl.SUPPORTED_PROTOCOLS_SET.size()]);
    }
    
    public final String[] getEnabledProtocols() {
        final List<String> enabled = (List<String>)new ArrayList(6);
        enabled.add("SSLv2Hello");
        final int opts;
        synchronized (this) {
            if (this.isDestroyed()) {
                return (String[])enabled.toArray((Object[])new String[1]);
            }
            opts = SSL.getOptions(this.ssl);
        }
        if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1, "TLSv1")) {
            enabled.add("TLSv1");
        }
        if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_1, "TLSv1.1")) {
            enabled.add("TLSv1.1");
        }
        if (isProtocolEnabled(opts, SSL.SSL_OP_NO_TLSv1_2, "TLSv1.2")) {
            enabled.add("TLSv1.2");
        }
        if (isProtocolEnabled(opts, SSL.SSL_OP_NO_SSLv2, "SSLv2")) {
            enabled.add("SSLv2");
        }
        if (isProtocolEnabled(opts, SSL.SSL_OP_NO_SSLv3, "SSLv3")) {
            enabled.add("SSLv3");
        }
        return (String[])enabled.toArray((Object[])new String[enabled.size()]);
    }
    
    private static boolean isProtocolEnabled(final int opts, final int disableMask, final String protocolString) {
        return (opts & disableMask) == 0x0 && OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(protocolString);
    }
    
    public final void setEnabledProtocols(final String[] protocols) {
        if (protocols == null) {
            throw new IllegalArgumentException();
        }
        int minProtocolIndex = ReferenceCountedOpenSslEngine.OPENSSL_OP_NO_PROTOCOLS.length;
        int maxProtocolIndex = 0;
        for (final String p : protocols) {
            if (!OpenSsl.SUPPORTED_PROTOCOLS_SET.contains(p)) {
                throw new IllegalArgumentException("Protocol " + p + " is not supported.");
            }
            if (p.equals("SSLv2")) {
                if (minProtocolIndex > 0) {
                    minProtocolIndex = 0;
                }
                if (maxProtocolIndex < 0) {
                    maxProtocolIndex = 0;
                }
            }
            else if (p.equals("SSLv3")) {
                if (minProtocolIndex > 1) {
                    minProtocolIndex = 1;
                }
                if (maxProtocolIndex < 1) {
                    maxProtocolIndex = 1;
                }
            }
            else if (p.equals("TLSv1")) {
                if (minProtocolIndex > 2) {
                    minProtocolIndex = 2;
                }
                if (maxProtocolIndex < 2) {
                    maxProtocolIndex = 2;
                }
            }
            else if (p.equals("TLSv1.1")) {
                if (minProtocolIndex > 3) {
                    minProtocolIndex = 3;
                }
                if (maxProtocolIndex < 3) {
                    maxProtocolIndex = 3;
                }
            }
            else if (p.equals("TLSv1.2")) {
                if (minProtocolIndex > 4) {
                    minProtocolIndex = 4;
                }
                if (maxProtocolIndex < 4) {
                    maxProtocolIndex = 4;
                }
            }
        }
        synchronized (this) {
            if (this.isDestroyed()) {
                throw new IllegalStateException(new StringBuilder().append("failed to enable protocols: ").append(Arrays.asList((Object[])protocols)).toString());
            }
            SSL.clearOptions(this.ssl, SSL.SSL_OP_NO_SSLv2 | SSL.SSL_OP_NO_SSLv3 | SSL.SSL_OP_NO_TLSv1 | SSL.SSL_OP_NO_TLSv1_1 | SSL.SSL_OP_NO_TLSv1_2);
            int opts = 0;
            for (int i = 0; i < minProtocolIndex; ++i) {
                opts |= ReferenceCountedOpenSslEngine.OPENSSL_OP_NO_PROTOCOLS[i];
            }
            assert maxProtocolIndex != Integer.MAX_VALUE;
            for (int i = maxProtocolIndex + 1; i < ReferenceCountedOpenSslEngine.OPENSSL_OP_NO_PROTOCOLS.length; ++i) {
                opts |= ReferenceCountedOpenSslEngine.OPENSSL_OP_NO_PROTOCOLS[i];
            }
            SSL.setOptions(this.ssl, opts);
        }
    }
    
    public final SSLSession getSession() {
        return (SSLSession)this.session;
    }
    
    public final synchronized void beginHandshake() throws SSLException {
        switch (this.handshakeState) {
            case STARTED_IMPLICITLY: {
                this.checkEngineClosed(ReferenceCountedOpenSslEngine.BEGIN_HANDSHAKE_ENGINE_CLOSED);
                this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
                this.calculateMaxWrapOverhead();
                break;
            }
            case STARTED_EXPLICITLY: {
                break;
            }
            case FINISHED: {
                throw ReferenceCountedOpenSslEngine.RENEGOTIATION_UNSUPPORTED;
            }
            case NOT_STARTED: {
                this.handshakeState = HandshakeState.STARTED_EXPLICITLY;
                this.handshake();
                this.calculateMaxWrapOverhead();
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    private void checkEngineClosed(final SSLException cause) throws SSLException {
        if (this.isDestroyed()) {
            throw cause;
        }
    }
    
    private static SSLEngineResult.HandshakeStatus pendingStatus(final int pendingStatus) {
        return (pendingStatus > 0) ? SSLEngineResult.HandshakeStatus.NEED_WRAP : SSLEngineResult.HandshakeStatus.NEED_UNWRAP;
    }
    
    private static boolean isEmpty(final Object[] arr) {
        return arr == null || arr.length == 0;
    }
    
    private static boolean isEmpty(final byte[] cert) {
        return cert == null || cert.length == 0;
    }
    
    private SSLEngineResult.HandshakeStatus handshake() throws SSLException {
        if (this.handshakeState == HandshakeState.FINISHED) {
            return SSLEngineResult.HandshakeStatus.FINISHED;
        }
        this.checkEngineClosed(ReferenceCountedOpenSslEngine.HANDSHAKE_ENGINE_CLOSED);
        SSLHandshakeException exception = this.handshakeException;
        if (exception != null) {
            if (SSL.bioLengthNonApplication(this.networkBIO) > 0) {
                return SSLEngineResult.HandshakeStatus.NEED_WRAP;
            }
            this.handshakeException = null;
            this.shutdown();
            throw exception;
        }
        else {
            this.engineMap.add(this);
            if (this.lastAccessed == -1L) {
                this.lastAccessed = System.currentTimeMillis();
            }
            if (!this.certificateSet && this.keyMaterialManager != null) {
                this.certificateSet = true;
                this.keyMaterialManager.setKeyMaterial(this);
            }
            final int code = SSL.doHandshake(this.ssl);
            if (code > 0) {
                this.session.handshakeFinished();
                this.engineMap.remove(this.ssl);
                return SSLEngineResult.HandshakeStatus.FINISHED;
            }
            if (this.handshakeException != null) {
                exception = this.handshakeException;
                this.handshakeException = null;
                this.shutdown();
                throw exception;
            }
            final int sslError = SSL.getError(this.ssl, code);
            if (sslError == SSL.SSL_ERROR_WANT_READ || sslError == SSL.SSL_ERROR_WANT_WRITE) {
                return pendingStatus(SSL.bioLengthNonApplication(this.networkBIO));
            }
            throw this.shutdownWithError("SSL_do_handshake");
        }
    }
    
    private SSLEngineResult.HandshakeStatus mayFinishHandshake(final SSLEngineResult.HandshakeStatus status) throws SSLException {
        if (status == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && this.handshakeState != HandshakeState.FINISHED) {
            return this.handshake();
        }
        return status;
    }
    
    public final synchronized SSLEngineResult.HandshakeStatus getHandshakeStatus() {
        return this.needPendingStatus() ? pendingStatus(SSL.bioLengthNonApplication(this.networkBIO)) : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }
    
    private SSLEngineResult.HandshakeStatus getHandshakeStatus(final int pending) {
        return this.needPendingStatus() ? pendingStatus(pending) : SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
    }
    
    private boolean needPendingStatus() {
        return this.handshakeState != HandshakeState.NOT_STARTED && !this.isDestroyed() && (this.handshakeState != HandshakeState.FINISHED || this.isInboundDone() || this.isOutboundDone());
    }
    
    private String toJavaCipherSuite(final String openSslCipherSuite) {
        if (openSslCipherSuite == null) {
            return null;
        }
        final String prefix = toJavaCipherSuitePrefix(SSL.getVersion(this.ssl));
        return CipherSuiteConverter.toJava(openSslCipherSuite, prefix);
    }
    
    private static String toJavaCipherSuitePrefix(final String protocolVersion) {
        char c;
        if (protocolVersion == null || protocolVersion.isEmpty()) {
            c = '\0';
        }
        else {
            c = protocolVersion.charAt(0);
        }
        switch (c) {
            case 'T': {
                return "TLS";
            }
            case 'S': {
                return "SSL";
            }
            default: {
                return "UNKNOWN";
            }
        }
    }
    
    public final void setUseClientMode(final boolean clientMode) {
        if (clientMode != this.clientMode) {
            throw new UnsupportedOperationException();
        }
    }
    
    public final boolean getUseClientMode() {
        return this.clientMode;
    }
    
    public final void setNeedClientAuth(final boolean b) {
        this.setClientAuth(b ? ClientAuth.REQUIRE : ClientAuth.NONE);
    }
    
    public final boolean getNeedClientAuth() {
        return this.clientAuth == ClientAuth.REQUIRE;
    }
    
    public final void setWantClientAuth(final boolean b) {
        this.setClientAuth(b ? ClientAuth.OPTIONAL : ClientAuth.NONE);
    }
    
    public final boolean getWantClientAuth() {
        return this.clientAuth == ClientAuth.OPTIONAL;
    }
    
    public final synchronized void setVerify(final int verifyMode, final int depth) {
        SSL.setVerify(this.ssl, verifyMode, depth);
    }
    
    private void setClientAuth(final ClientAuth mode) {
        if (this.clientMode) {
            return;
        }
        synchronized (this) {
            if (this.clientAuth == mode) {
                return;
            }
            switch (mode) {
                case NONE: {
                    SSL.setVerify(this.ssl, 0, 10);
                    break;
                }
                case REQUIRE: {
                    SSL.setVerify(this.ssl, 2, 10);
                    break;
                }
                case OPTIONAL: {
                    SSL.setVerify(this.ssl, 1, 10);
                    break;
                }
                default: {
                    throw new Error(mode.toString());
                }
            }
            this.clientAuth = mode;
        }
    }
    
    public final void setEnableSessionCreation(final boolean b) {
        if (b) {
            throw new UnsupportedOperationException();
        }
    }
    
    public final boolean getEnableSessionCreation() {
        return false;
    }
    
    public final synchronized SSLParameters getSSLParameters() {
        final SSLParameters sslParameters = super.getSSLParameters();
        final int version = PlatformDependent.javaVersion();
        if (version >= 7) {
            sslParameters.setEndpointIdentificationAlgorithm(this.endPointIdentificationAlgorithm);
            Java7SslParametersUtils.setAlgorithmConstraints(sslParameters, this.algorithmConstraints);
            if (version >= 8) {
                if (this.sniHostNames != null) {
                    Java8SslUtils.setSniHostNames(sslParameters, this.sniHostNames);
                }
                if (!this.isDestroyed()) {
                    Java8SslUtils.setUseCipherSuitesOrder(sslParameters, (SSL.getOptions(this.ssl) & SSL.SSL_OP_CIPHER_SERVER_PREFERENCE) != 0x0);
                }
                Java8SslUtils.setSNIMatchers(sslParameters, this.matchers);
            }
        }
        return sslParameters;
    }
    
    public final synchronized void setSSLParameters(final SSLParameters sslParameters) {
        final int version = PlatformDependent.javaVersion();
        if (version >= 7) {
            if (sslParameters.getAlgorithmConstraints() != null) {
                throw new IllegalArgumentException("AlgorithmConstraints are not supported.");
            }
            if (version >= 8) {
                if (!this.isDestroyed()) {
                    if (this.clientMode) {
                        final List<String> sniHostNames = Java8SslUtils.getSniHostNames(sslParameters);
                        for (final String name : sniHostNames) {
                            SSL.setTlsExtHostName(this.ssl, name);
                        }
                        this.sniHostNames = sniHostNames;
                    }
                    if (Java8SslUtils.getUseCipherSuitesOrder(sslParameters)) {
                        SSL.setOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
                    }
                    else {
                        SSL.clearOptions(this.ssl, SSL.SSL_OP_CIPHER_SERVER_PREFERENCE);
                    }
                }
                this.matchers = sslParameters.getSNIMatchers();
            }
            final String endPointIdentificationAlgorithm = sslParameters.getEndpointIdentificationAlgorithm();
            final boolean endPointVerificationEnabled = endPointIdentificationAlgorithm != null && !endPointIdentificationAlgorithm.isEmpty();
            SSL.setHostNameValidation(this.ssl, 0, endPointVerificationEnabled ? this.getPeerHost() : null);
            if (this.clientMode && endPointVerificationEnabled) {
                SSL.setVerify(this.ssl, 2, -1);
            }
            this.endPointIdentificationAlgorithm = endPointIdentificationAlgorithm;
            this.algorithmConstraints = sslParameters.getAlgorithmConstraints();
        }
        super.setSSLParameters(sslParameters);
    }
    
    private boolean isDestroyed() {
        return this.destroyed != 0;
    }
    
    final boolean checkSniHostnameMatch(final String hostname) {
        return Java8SslUtils.checkSniHostnameMatch(this.matchers, hostname);
    }
    
    public String getNegotiatedApplicationProtocol() {
        return this.applicationProtocol;
    }
    
    private static long bufferAddress(final ByteBuffer b) {
        assert b.isDirect();
        if (PlatformDependent.hasUnsafe()) {
            return PlatformDependent.directBufferAddress(b);
        }
        return Buffer.address(b);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ReferenceCountedOpenSslEngine.class);
        BEGIN_HANDSHAKE_ENGINE_CLOSED = ThrowableUtil.<SSLException>unknownStackTrace(new SSLException("engine closed"), ReferenceCountedOpenSslEngine.class, "beginHandshake()");
        HANDSHAKE_ENGINE_CLOSED = ThrowableUtil.<SSLException>unknownStackTrace(new SSLException("engine closed"), ReferenceCountedOpenSslEngine.class, "handshake()");
        RENEGOTIATION_UNSUPPORTED = ThrowableUtil.<SSLException>unknownStackTrace(new SSLException("renegotiation unsupported"), ReferenceCountedOpenSslEngine.class, "beginHandshake()");
        leakDetector = ResourceLeakDetectorFactory.instance().<ReferenceCountedOpenSslEngine>newResourceLeakDetector(ReferenceCountedOpenSslEngine.class);
        OPENSSL_OP_NO_PROTOCOLS = new int[] { SSL.SSL_OP_NO_SSLv2, SSL.SSL_OP_NO_SSLv3, SSL.SSL_OP_NO_TLSv1, SSL.SSL_OP_NO_TLSv1_1, SSL.SSL_OP_NO_TLSv1_2 };
        MAX_PLAINTEXT_LENGTH = SSL.SSL_MAX_PLAINTEXT_LENGTH;
        MAX_RECORD_SIZE = SSL.SSL_MAX_RECORD_LENGTH;
        DESTROYED_UPDATER = AtomicIntegerFieldUpdater.newUpdater((Class)ReferenceCountedOpenSslEngine.class, "destroyed");
        NEED_UNWRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
        NEED_UNWRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_UNWRAP, 0, 0);
        NEED_WRAP_OK = new SSLEngineResult(SSLEngineResult.Status.OK, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
        NEED_WRAP_CLOSED = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NEED_WRAP, 0, 0);
        CLOSED_NOT_HANDSHAKING = new SSLEngineResult(SSLEngineResult.Status.CLOSED, SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING, 0, 0);
    }
    
    private enum HandshakeState {
        NOT_STARTED, 
        STARTED_IMPLICITLY, 
        STARTED_EXPLICITLY, 
        FINISHED;
    }
    
    private final class OpenSslSession implements SSLSession {
        private final OpenSslSessionContext sessionContext;
        private X509Certificate[] x509PeerCerts;
        private Certificate[] peerCerts;
        private String protocol;
        private String cipher;
        private byte[] id;
        private long creationTime;
        private volatile int applicationBufferSize;
        private Map<String, Object> values;
        
        OpenSslSession(final OpenSslSessionContext sessionContext) {
            this.applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH;
            this.sessionContext = sessionContext;
        }
        
        public byte[] getId() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.id == null) {
                    return EmptyArrays.EMPTY_BYTES;
                }
                return this.id.clone();
            }
        }
        
        public SSLSessionContext getSessionContext() {
            return (SSLSessionContext)this.sessionContext;
        }
        
        public long getCreationTime() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.creationTime == 0L && !ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                    this.creationTime = SSL.getTime(ReferenceCountedOpenSslEngine.this.ssl) * 1000L;
                }
            }
            return this.creationTime;
        }
        
        public long getLastAccessedTime() {
            final long lastAccessed = ReferenceCountedOpenSslEngine.this.lastAccessed;
            return (lastAccessed == -1L) ? this.getCreationTime() : lastAccessed;
        }
        
        public void invalidate() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                    SSL.setTimeout(ReferenceCountedOpenSslEngine.this.ssl, 0L);
                }
            }
        }
        
        public boolean isValid() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                    return System.currentTimeMillis() - SSL.getTimeout(ReferenceCountedOpenSslEngine.this.ssl) * 1000L < SSL.getTime(ReferenceCountedOpenSslEngine.this.ssl) * 1000L;
                }
            }
            return false;
        }
        
        public void putValue(final String name, final Object value) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            if (value == null) {
                throw new NullPointerException("value");
            }
            Map<String, Object> values = this.values;
            if (values == null) {
                final HashMap values2 = new HashMap(2);
                this.values = (Map<String, Object>)values2;
                values = (Map<String, Object>)values2;
            }
            final Object old = values.put(name, value);
            if (value instanceof SSLSessionBindingListener) {
                ((SSLSessionBindingListener)value).valueBound(new SSLSessionBindingEvent((SSLSession)this, name));
            }
            this.notifyUnbound(old, name);
        }
        
        public Object getValue(final String name) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            if (this.values == null) {
                return null;
            }
            return this.values.get(name);
        }
        
        public void removeValue(final String name) {
            if (name == null) {
                throw new NullPointerException("name");
            }
            final Map<String, Object> values = this.values;
            if (values == null) {
                return;
            }
            final Object old = values.remove(name);
            this.notifyUnbound(old, name);
        }
        
        public String[] getValueNames() {
            final Map<String, Object> values = this.values;
            if (values == null || values.isEmpty()) {
                return EmptyArrays.EMPTY_STRINGS;
            }
            return (String[])values.keySet().toArray((Object[])new String[values.size()]);
        }
        
        private void notifyUnbound(final Object value, final String name) {
            if (value instanceof SSLSessionBindingListener) {
                ((SSLSessionBindingListener)value).valueUnbound(new SSLSessionBindingEvent((SSLSession)this, name));
            }
        }
        
        void handshakeFinished() throws SSLException {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                    throw new SSLException("Already closed");
                }
                this.id = SSL.getSessionId(ReferenceCountedOpenSslEngine.this.ssl);
                this.cipher = ReferenceCountedOpenSslEngine.this.toJavaCipherSuite(SSL.getCipherForSSL(ReferenceCountedOpenSslEngine.this.ssl));
                this.protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
                this.initPeerCerts();
                this.selectApplicationProtocol();
                ReferenceCountedOpenSslEngine.this.calculateMaxWrapOverhead();
                ReferenceCountedOpenSslEngine.this.handshakeState = HandshakeState.FINISHED;
            }
        }
        
        private void initPeerCerts() {
            final byte[][] chain = SSL.getPeerCertChain(ReferenceCountedOpenSslEngine.this.ssl);
            if (ReferenceCountedOpenSslEngine.this.clientMode) {
                if (isEmpty(chain)) {
                    this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
                    this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
                }
                else {
                    this.peerCerts = new Certificate[chain.length];
                    this.x509PeerCerts = new X509Certificate[chain.length];
                    this.initCerts(chain, 0);
                }
            }
            else {
                final byte[] clientCert = SSL.getPeerCertificate(ReferenceCountedOpenSslEngine.this.ssl);
                if (isEmpty(clientCert)) {
                    this.peerCerts = EmptyArrays.EMPTY_CERTIFICATES;
                    this.x509PeerCerts = EmptyArrays.EMPTY_JAVAX_X509_CERTIFICATES;
                }
                else if (isEmpty(chain)) {
                    this.peerCerts = new Certificate[] { (Certificate)new OpenSslX509Certificate(clientCert) };
                    this.x509PeerCerts = new X509Certificate[] { new OpenSslJavaxX509Certificate(clientCert) };
                }
                else {
                    this.peerCerts = new Certificate[chain.length + 1];
                    this.x509PeerCerts = new X509Certificate[chain.length + 1];
                    this.peerCerts[0] = (Certificate)new OpenSslX509Certificate(clientCert);
                    this.x509PeerCerts[0] = new OpenSslJavaxX509Certificate(clientCert);
                    this.initCerts(chain, 1);
                }
            }
        }
        
        private void initCerts(final byte[][] chain, final int startPos) {
            for (int i = 0; i < chain.length; ++i) {
                final int certPos = startPos + i;
                this.peerCerts[certPos] = (Certificate)new OpenSslX509Certificate(chain[i]);
                this.x509PeerCerts[certPos] = new OpenSslJavaxX509Certificate(chain[i]);
            }
        }
        
        private void selectApplicationProtocol() throws SSLException {
            final ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior = ReferenceCountedOpenSslEngine.this.apn.selectedListenerFailureBehavior();
            final List<String> protocols = ReferenceCountedOpenSslEngine.this.apn.protocols();
            switch (ReferenceCountedOpenSslEngine.this.apn.protocol()) {
                case NONE: {
                    break;
                }
                case ALPN: {
                    final String applicationProtocol = SSL.getAlpnSelected(ReferenceCountedOpenSslEngine.this.ssl);
                    if (applicationProtocol != null) {
                        ReferenceCountedOpenSslEngine.this.applicationProtocol = this.selectApplicationProtocol(protocols, behavior, applicationProtocol);
                        break;
                    }
                    break;
                }
                case NPN: {
                    final String applicationProtocol = SSL.getNextProtoNegotiated(ReferenceCountedOpenSslEngine.this.ssl);
                    if (applicationProtocol != null) {
                        ReferenceCountedOpenSslEngine.this.applicationProtocol = this.selectApplicationProtocol(protocols, behavior, applicationProtocol);
                        break;
                    }
                    break;
                }
                case NPN_AND_ALPN: {
                    String applicationProtocol = SSL.getAlpnSelected(ReferenceCountedOpenSslEngine.this.ssl);
                    if (applicationProtocol == null) {
                        applicationProtocol = SSL.getNextProtoNegotiated(ReferenceCountedOpenSslEngine.this.ssl);
                    }
                    if (applicationProtocol != null) {
                        ReferenceCountedOpenSslEngine.this.applicationProtocol = this.selectApplicationProtocol(protocols, behavior, applicationProtocol);
                        break;
                    }
                    break;
                }
                default: {
                    throw new Error();
                }
            }
        }
        
        private String selectApplicationProtocol(final List<String> protocols, final ApplicationProtocolConfig.SelectedListenerFailureBehavior behavior, final String applicationProtocol) throws SSLException {
            if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT) {
                return applicationProtocol;
            }
            final int size = protocols.size();
            assert size > 0;
            if (protocols.contains(applicationProtocol)) {
                return applicationProtocol;
            }
            if (behavior == ApplicationProtocolConfig.SelectedListenerFailureBehavior.CHOOSE_MY_LAST_PROTOCOL) {
                return (String)protocols.get(size - 1);
            }
            throw new SSLException("unknown protocol " + applicationProtocol);
        }
        
        public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (isEmpty(this.peerCerts)) {
                    throw new SSLPeerUnverifiedException("peer not verified");
                }
                return this.peerCerts.clone();
            }
        }
        
        public Certificate[] getLocalCertificates() {
            if (ReferenceCountedOpenSslEngine.this.localCerts == null) {
                return null;
            }
            return ReferenceCountedOpenSslEngine.this.localCerts.clone();
        }
        
        public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (isEmpty(this.x509PeerCerts)) {
                    throw new SSLPeerUnverifiedException("peer not verified");
                }
                return this.x509PeerCerts.clone();
            }
        }
        
        public Principal getPeerPrincipal() throws SSLPeerUnverifiedException {
            final Certificate[] peer = this.getPeerCertificates();
            return (Principal)((java.security.cert.X509Certificate)peer[0]).getSubjectX500Principal();
        }
        
        public Principal getLocalPrincipal() {
            final Certificate[] local = ReferenceCountedOpenSslEngine.this.localCerts;
            if (local == null || local.length == 0) {
                return null;
            }
            return (Principal)((java.security.cert.X509Certificate)local[0]).getIssuerX500Principal();
        }
        
        public String getCipherSuite() {
            synchronized (ReferenceCountedOpenSslEngine.this) {
                if (this.cipher == null) {
                    return "SSL_NULL_WITH_NULL_NULL";
                }
                return this.cipher;
            }
        }
        
        public String getProtocol() {
            String protocol = this.protocol;
            if (protocol == null) {
                synchronized (ReferenceCountedOpenSslEngine.this) {
                    if (!ReferenceCountedOpenSslEngine.this.isDestroyed()) {
                        protocol = SSL.getVersion(ReferenceCountedOpenSslEngine.this.ssl);
                    }
                    else {
                        protocol = "";
                    }
                }
            }
            return protocol;
        }
        
        public String getPeerHost() {
            return ReferenceCountedOpenSslEngine.this.getPeerHost();
        }
        
        public int getPeerPort() {
            return ReferenceCountedOpenSslEngine.this.getPeerPort();
        }
        
        public int getPacketBufferSize() {
            return ReferenceCountedOpenSslEngine.this.maxEncryptedPacketLength();
        }
        
        public int getApplicationBufferSize() {
            return this.applicationBufferSize;
        }
        
        void tryExpandApplicationBufferSize(final int packetLengthDataOnly) {
            if (packetLengthDataOnly > ReferenceCountedOpenSslEngine.MAX_PLAINTEXT_LENGTH && this.applicationBufferSize != ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE) {
                this.applicationBufferSize = ReferenceCountedOpenSslEngine.MAX_RECORD_SIZE;
            }
        }
    }
}
