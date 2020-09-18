package io.netty.handler.ssl;

import io.netty.internal.tcnative.SSL;
import java.util.concurrent.locks.Lock;
import io.netty.internal.tcnative.SSLContext;

public final class OpenSslServerSessionContext extends OpenSslSessionContext {
    OpenSslServerSessionContext(final ReferenceCountedOpenSslContext context) {
        super(context);
    }
    
    public void setSessionTimeout(final int seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException();
        }
        final Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheTimeout(this.context.ctx, (long)seconds);
        }
        finally {
            writerLock.unlock();
        }
    }
    
    public int getSessionTimeout() {
        final Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return (int)SSLContext.getSessionCacheTimeout(this.context.ctx);
        }
        finally {
            readerLock.unlock();
        }
    }
    
    public void setSessionCacheSize(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException();
        }
        final Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheSize(this.context.ctx, (long)size);
        }
        finally {
            writerLock.unlock();
        }
    }
    
    public int getSessionCacheSize() {
        final Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return (int)SSLContext.getSessionCacheSize(this.context.ctx);
        }
        finally {
            readerLock.unlock();
        }
    }
    
    @Override
    public void setSessionCacheEnabled(final boolean enabled) {
        final long mode = enabled ? SSL.SSL_SESS_CACHE_SERVER : SSL.SSL_SESS_CACHE_OFF;
        final Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.setSessionCacheMode(this.context.ctx, mode);
        }
        finally {
            writerLock.unlock();
        }
    }
    
    @Override
    public boolean isSessionCacheEnabled() {
        final Lock readerLock = this.context.ctxLock.readLock();
        readerLock.lock();
        try {
            return SSLContext.getSessionCacheMode(this.context.ctx) == SSL.SSL_SESS_CACHE_SERVER;
        }
        finally {
            readerLock.unlock();
        }
    }
    
    public boolean setSessionIdContext(final byte[] sidCtx) {
        final Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            return SSLContext.setSessionIdContext(this.context.ctx, sidCtx);
        }
        finally {
            writerLock.unlock();
        }
    }
}
