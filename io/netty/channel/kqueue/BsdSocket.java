package io.netty.channel.kqueue;

import io.netty.util.internal.ThrowableUtil;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.unix.PeerCredentials;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.Socket;

final class BsdSocket extends Socket {
    private static final Errors.NativeIoException SENDFILE_CONNECTION_RESET_EXCEPTION;
    private static final ClosedChannelException SENDFILE_CLOSED_CHANNEL_EXCEPTION;
    private static final int APPLE_SND_LOW_AT_MAX = 131072;
    private static final int FREEBSD_SND_LOW_AT_MAX = 32768;
    static final int BSD_SND_LOW_AT_MAX;
    
    BsdSocket(final int fd) {
        super(fd);
    }
    
    void setAcceptFilter(final AcceptFilter acceptFilter) throws IOException {
        setAcceptFilter(this.intValue(), acceptFilter.filterName(), acceptFilter.filterArgs());
    }
    
    void setTcpNoPush(final boolean tcpNoPush) throws IOException {
        setTcpNoPush(this.intValue(), tcpNoPush ? 1 : 0);
    }
    
    void setSndLowAt(final int lowAt) throws IOException {
        setSndLowAt(this.intValue(), lowAt);
    }
    
    boolean isTcpNoPush() throws IOException {
        return getTcpNoPush(this.intValue()) != 0;
    }
    
    int getSndLowAt() throws IOException {
        return getSndLowAt(this.intValue());
    }
    
    AcceptFilter getAcceptFilter() throws IOException {
        final String[] result = getAcceptFilter(this.intValue());
        return (result == null) ? AcceptFilter.PLATFORM_UNSUPPORTED : new AcceptFilter(result[0], result[1]);
    }
    
    PeerCredentials getPeerCredentials() throws IOException {
        return getPeerCredentials(this.intValue());
    }
    
    long sendFile(final DefaultFileRegion src, final long baseOffset, final long offset, final long length) throws IOException {
        src.open();
        final long res = sendFile(this.intValue(), src, baseOffset, offset, length);
        if (res >= 0L) {
            return res;
        }
        return Errors.ioResult("sendfile", (int)res, BsdSocket.SENDFILE_CONNECTION_RESET_EXCEPTION, BsdSocket.SENDFILE_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public static BsdSocket newSocketStream() {
        return new BsdSocket(Socket.newSocketStream0());
    }
    
    public static BsdSocket newSocketDgram() {
        return new BsdSocket(Socket.newSocketDgram0());
    }
    
    public static BsdSocket newSocketDomain() {
        return new BsdSocket(Socket.newSocketDomain0());
    }
    
    private static native long sendFile(final int integer, final DefaultFileRegion defaultFileRegion, final long long3, final long long4, final long long5) throws IOException;
    
    private static native String[] getAcceptFilter(final int integer) throws IOException;
    
    private static native int getTcpNoPush(final int integer) throws IOException;
    
    private static native int getSndLowAt(final int integer) throws IOException;
    
    private static native PeerCredentials getPeerCredentials(final int integer) throws IOException;
    
    private static native void setAcceptFilter(final int integer, final String string2, final String string3) throws IOException;
    
    private static native void setTcpNoPush(final int integer1, final int integer2) throws IOException;
    
    private static native void setSndLowAt(final int integer1, final int integer2) throws IOException;
    
    static {
        SENDFILE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Native.class, "sendfile(..)");
        BSD_SND_LOW_AT_MAX = Math.min(131072, 32768);
        SENDFILE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendfile", Errors.ERRNO_EPIPE_NEGATIVE);
    }
}
