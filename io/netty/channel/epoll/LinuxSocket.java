package io.netty.channel.epoll;

import io.netty.util.internal.ThrowableUtil;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.unix.PeerCredentials;
import io.netty.channel.unix.NativeInetAddress;
import java.net.InetAddress;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.Socket;

final class LinuxSocket extends Socket {
    private static final long MAX_UINT32_T = 4294967295L;
    private static final Errors.NativeIoException SENDFILE_CONNECTION_RESET_EXCEPTION;
    private static final ClosedChannelException SENDFILE_CLOSED_CHANNEL_EXCEPTION;
    
    public LinuxSocket(final int fd) {
        super(fd);
    }
    
    void setTcpDeferAccept(final int deferAccept) throws IOException {
        setTcpDeferAccept(this.intValue(), deferAccept);
    }
    
    void setTcpQuickAck(final boolean quickAck) throws IOException {
        setTcpQuickAck(this.intValue(), quickAck ? 1 : 0);
    }
    
    void setTcpCork(final boolean tcpCork) throws IOException {
        setTcpCork(this.intValue(), tcpCork ? 1 : 0);
    }
    
    void setTcpNotSentLowAt(final long tcpNotSentLowAt) throws IOException {
        if (tcpNotSentLowAt < 0L || tcpNotSentLowAt > 4294967295L) {
            throw new IllegalArgumentException("tcpNotSentLowAt must be a uint32_t");
        }
        setTcpNotSentLowAt(this.intValue(), (int)tcpNotSentLowAt);
    }
    
    void setTcpFastOpen(final int tcpFastopenBacklog) throws IOException {
        setTcpFastOpen(this.intValue(), tcpFastopenBacklog);
    }
    
    void setTcpFastOpenConnect(final boolean tcpFastOpenConnect) throws IOException {
        setTcpFastOpenConnect(this.intValue(), tcpFastOpenConnect ? 1 : 0);
    }
    
    boolean isTcpFastOpenConnect() throws IOException {
        return isTcpFastOpenConnect(this.intValue()) != 0;
    }
    
    void setTcpKeepIdle(final int seconds) throws IOException {
        setTcpKeepIdle(this.intValue(), seconds);
    }
    
    void setTcpKeepIntvl(final int seconds) throws IOException {
        setTcpKeepIntvl(this.intValue(), seconds);
    }
    
    void setTcpKeepCnt(final int probes) throws IOException {
        setTcpKeepCnt(this.intValue(), probes);
    }
    
    void setTcpUserTimeout(final int milliseconds) throws IOException {
        setTcpUserTimeout(this.intValue(), milliseconds);
    }
    
    void setIpFreeBind(final boolean enabled) throws IOException {
        setIpFreeBind(this.intValue(), enabled ? 1 : 0);
    }
    
    void setIpTransparent(final boolean enabled) throws IOException {
        setIpTransparent(this.intValue(), enabled ? 1 : 0);
    }
    
    void setIpRecvOrigDestAddr(final boolean enabled) throws IOException {
        setIpRecvOrigDestAddr(this.intValue(), enabled ? 1 : 0);
    }
    
    void getTcpInfo(final EpollTcpInfo info) throws IOException {
        getTcpInfo(this.intValue(), info.info);
    }
    
    void setTcpMd5Sig(final InetAddress address, final byte[] key) throws IOException {
        final NativeInetAddress a = NativeInetAddress.newInstance(address);
        setTcpMd5Sig(this.intValue(), a.address(), a.scopeId(), key);
    }
    
    boolean isTcpCork() throws IOException {
        return isTcpCork(this.intValue()) != 0;
    }
    
    int getTcpDeferAccept() throws IOException {
        return getTcpDeferAccept(this.intValue());
    }
    
    boolean isTcpQuickAck() throws IOException {
        return isTcpQuickAck(this.intValue()) != 0;
    }
    
    long getTcpNotSentLowAt() throws IOException {
        return (long)getTcpNotSentLowAt(this.intValue()) & 0xFFFFFFFFL;
    }
    
    int getTcpKeepIdle() throws IOException {
        return getTcpKeepIdle(this.intValue());
    }
    
    int getTcpKeepIntvl() throws IOException {
        return getTcpKeepIntvl(this.intValue());
    }
    
    int getTcpKeepCnt() throws IOException {
        return getTcpKeepCnt(this.intValue());
    }
    
    int getTcpUserTimeout() throws IOException {
        return getTcpUserTimeout(this.intValue());
    }
    
    boolean isIpFreeBind() throws IOException {
        return isIpFreeBind(this.intValue()) != 0;
    }
    
    boolean isIpTransparent() throws IOException {
        return isIpTransparent(this.intValue()) != 0;
    }
    
    boolean isIpRecvOrigDestAddr() throws IOException {
        return isIpRecvOrigDestAddr(this.intValue()) != 0;
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
        return Errors.ioResult("sendfile", (int)res, LinuxSocket.SENDFILE_CONNECTION_RESET_EXCEPTION, LinuxSocket.SENDFILE_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public static LinuxSocket newSocketStream() {
        return new LinuxSocket(Socket.newSocketStream0());
    }
    
    public static LinuxSocket newSocketDgram() {
        return new LinuxSocket(Socket.newSocketDgram0());
    }
    
    public static LinuxSocket newSocketDomain() {
        return new LinuxSocket(Socket.newSocketDomain0());
    }
    
    private static native long sendFile(final int integer, final DefaultFileRegion defaultFileRegion, final long long3, final long long4, final long long5) throws IOException;
    
    private static native int getTcpDeferAccept(final int integer) throws IOException;
    
    private static native int isTcpQuickAck(final int integer) throws IOException;
    
    private static native int isTcpCork(final int integer) throws IOException;
    
    private static native int getTcpNotSentLowAt(final int integer) throws IOException;
    
    private static native int getTcpKeepIdle(final int integer) throws IOException;
    
    private static native int getTcpKeepIntvl(final int integer) throws IOException;
    
    private static native int getTcpKeepCnt(final int integer) throws IOException;
    
    private static native int getTcpUserTimeout(final int integer) throws IOException;
    
    private static native int isIpFreeBind(final int integer) throws IOException;
    
    private static native int isIpTransparent(final int integer) throws IOException;
    
    private static native int isIpRecvOrigDestAddr(final int integer) throws IOException;
    
    private static native void getTcpInfo(final int integer, final long[] arr) throws IOException;
    
    private static native PeerCredentials getPeerCredentials(final int integer) throws IOException;
    
    private static native int isTcpFastOpenConnect(final int integer) throws IOException;
    
    private static native void setTcpDeferAccept(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpQuickAck(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpCork(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpNotSentLowAt(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpFastOpen(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpFastOpenConnect(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpKeepIdle(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpKeepIntvl(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpKeepCnt(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpUserTimeout(final int integer1, final int integer2) throws IOException;
    
    private static native void setIpFreeBind(final int integer1, final int integer2) throws IOException;
    
    private static native void setIpTransparent(final int integer1, final int integer2) throws IOException;
    
    private static native void setIpRecvOrigDestAddr(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpMd5Sig(final int integer1, final byte[] arr2, final int integer3, final byte[] arr4) throws IOException;
    
    static {
        SENDFILE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendfile(...)", Errors.ERRNO_EPIPE_NEGATIVE);
        SENDFILE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Native.class, "sendfile(...)");
    }
}
