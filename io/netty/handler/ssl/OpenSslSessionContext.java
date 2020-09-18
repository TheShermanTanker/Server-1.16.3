package io.netty.handler.ssl;

import java.util.NoSuchElementException;
import io.netty.util.internal.ObjectUtil;
import java.util.concurrent.locks.Lock;
import io.netty.internal.tcnative.SSLContext;
import io.netty.internal.tcnative.SSL;
import java.util.Arrays;
import io.netty.internal.tcnative.SessionTicketKey;
import javax.net.ssl.SSLSession;
import java.util.Enumeration;
import javax.net.ssl.SSLSessionContext;

public abstract class OpenSslSessionContext implements SSLSessionContext {
    private static final Enumeration<byte[]> EMPTY;
    private final OpenSslSessionStats stats;
    final ReferenceCountedOpenSslContext context;
    
    OpenSslSessionContext(final ReferenceCountedOpenSslContext context) {
        this.context = context;
        this.stats = new OpenSslSessionStats(context);
    }
    
    public SSLSession getSession(final byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes");
        }
        return null;
    }
    
    public Enumeration<byte[]> getIds() {
        return OpenSslSessionContext.EMPTY;
    }
    
    @Deprecated
    public void setTicketKeys(final byte[] keys) {
        if (keys.length % 48 != 0) {
            throw new IllegalArgumentException("keys.length % 48 != 0");
        }
        final SessionTicketKey[] tickets = new SessionTicketKey[keys.length / 48];
        byte[] name;
        byte[] hmacKey;
        byte[] aesKey;
        for (int i = 0, a = 0; i < tickets.length; i += 16, aesKey = Arrays.copyOfRange(keys, a, 16), a += 16, tickets[i] = new SessionTicketKey(name, hmacKey, aesKey), ++i) {
            name = Arrays.copyOfRange(keys, a, 16);
            a += 16;
            hmacKey = Arrays.copyOfRange(keys, a, 16);
        }
        final Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
            SSLContext.setSessionTicketKeys(this.context.ctx, tickets);
        }
        finally {
            writerLock.unlock();
        }
    }
    
    public void setTicketKeys(final OpenSslSessionTicketKey... keys) {
        ObjectUtil.<OpenSslSessionTicketKey[]>checkNotNull(keys, "keys");
        final SessionTicketKey[] ticketKeys = new SessionTicketKey[keys.length];
        for (int i = 0; i < ticketKeys.length; ++i) {
            ticketKeys[i] = keys[i].key;
        }
        final Lock writerLock = this.context.ctxLock.writeLock();
        writerLock.lock();
        try {
            SSLContext.clearOptions(this.context.ctx, SSL.SSL_OP_NO_TICKET);
            SSLContext.setSessionTicketKeys(this.context.ctx, ticketKeys);
        }
        finally {
            writerLock.unlock();
        }
    }
    
    public abstract void setSessionCacheEnabled(final boolean boolean1);
    
    public abstract boolean isSessionCacheEnabled();
    
    public OpenSslSessionStats stats() {
        return this.stats;
    }
    
    static {
        EMPTY = (Enumeration)new EmptyEnumeration();
    }
    
    private static final class EmptyEnumeration implements Enumeration<byte[]> {
        public boolean hasMoreElements() {
            return false;
        }
        
        public byte[] nextElement() {
            throw new NoSuchElementException();
        }
    }
}
