package io.netty.channel.unix;

import io.netty.util.internal.ThrowableUtil;
import io.netty.channel.ChannelException;
import io.netty.util.NetUtil;
import io.netty.util.CharsetUtil;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.PortUnreachableException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.channels.ClosedChannelException;

public class Socket extends FileDescriptor {
    private static final ClosedChannelException SHUTDOWN_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SEND_TO_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION;
    private static final Errors.NativeIoException SEND_TO_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException CONNECTION_RESET_EXCEPTION_SENDMSG;
    private static final Errors.NativeIoException CONNECTION_RESET_SHUTDOWN_EXCEPTION;
    private static final Errors.NativeConnectException FINISH_CONNECT_REFUSED_EXCEPTION;
    private static final Errors.NativeConnectException CONNECT_REFUSED_EXCEPTION;
    public static final int UDS_SUN_PATH_SIZE;
    private static final AtomicBoolean INITIALIZED;
    
    public Socket(final int fd) {
        super(fd);
    }
    
    public final void shutdown() throws IOException {
        this.shutdown(true, true);
    }
    
    public final void shutdown(final boolean read, final boolean write) throws IOException {
        while (true) {
            final int oldState = this.state;
            if (FileDescriptor.isClosed(oldState)) {
                throw new ClosedChannelException();
            }
            int newState = oldState;
            if (read && !FileDescriptor.isInputShutdown(newState)) {
                newState = FileDescriptor.inputShutdown(newState);
            }
            if (write && !FileDescriptor.isOutputShutdown(newState)) {
                newState = FileDescriptor.outputShutdown(newState);
            }
            if (newState == oldState) {
                return;
            }
            if (this.casState(oldState, newState)) {
                final int res = shutdown(this.fd, read, write);
                if (res < 0) {
                    Errors.ioResult("shutdown", res, Socket.CONNECTION_RESET_SHUTDOWN_EXCEPTION, Socket.SHUTDOWN_CLOSED_CHANNEL_EXCEPTION);
                }
            }
        }
    }
    
    public final boolean isShutdown() {
        final int state = this.state;
        return FileDescriptor.isInputShutdown(state) && FileDescriptor.isOutputShutdown(state);
    }
    
    public final boolean isInputShutdown() {
        return FileDescriptor.isInputShutdown(this.state);
    }
    
    public final boolean isOutputShutdown() {
        return FileDescriptor.isOutputShutdown(this.state);
    }
    
    public final int sendTo(final ByteBuffer buf, final int pos, final int limit, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        final int res = sendTo(this.fd, buf, pos, limit, address, scopeId, port);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            throw new PortUnreachableException("sendTo failed");
        }
        return Errors.ioResult("sendTo", res, Socket.SEND_TO_CONNECTION_RESET_EXCEPTION, Socket.SEND_TO_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public final int sendToAddress(final long memoryAddress, final int pos, final int limit, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        final int res = sendToAddress(this.fd, memoryAddress, pos, limit, address, scopeId, port);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            throw new PortUnreachableException("sendToAddress failed");
        }
        return Errors.ioResult("sendToAddress", res, Socket.SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION, Socket.SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public final int sendToAddresses(final long memoryAddress, final int length, final InetAddress addr, final int port) throws IOException {
        byte[] address;
        int scopeId;
        if (addr instanceof Inet6Address) {
            address = addr.getAddress();
            scopeId = ((Inet6Address)addr).getScopeId();
        }
        else {
            scopeId = 0;
            address = NativeInetAddress.ipv4MappedIpv6Address(addr.getAddress());
        }
        final int res = sendToAddresses(this.fd, memoryAddress, length, address, scopeId, port);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERROR_ECONNREFUSED_NEGATIVE) {
            throw new PortUnreachableException("sendToAddresses failed");
        }
        return Errors.ioResult("sendToAddresses", res, Socket.CONNECTION_RESET_EXCEPTION_SENDMSG, Socket.SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION);
    }
    
    public final DatagramSocketAddress recvFrom(final ByteBuffer buf, final int pos, final int limit) throws IOException {
        return recvFrom(this.fd, buf, pos, limit);
    }
    
    public final DatagramSocketAddress recvFromAddress(final long memoryAddress, final int pos, final int limit) throws IOException {
        return recvFromAddress(this.fd, memoryAddress, pos, limit);
    }
    
    public final int recvFd() throws IOException {
        final int res = recvFd(this.fd);
        if (res > 0) {
            return res;
        }
        if (res == 0) {
            return -1;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return 0;
        }
        throw Errors.newIOException("recvFd", res);
    }
    
    public final int sendFd(final int fdToSend) throws IOException {
        final int res = sendFd(this.fd, fdToSend);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return -1;
        }
        throw Errors.newIOException("sendFd", res);
    }
    
    public final boolean connect(final SocketAddress socketAddress) throws IOException {
        int res;
        if (socketAddress instanceof InetSocketAddress) {
            final InetSocketAddress inetSocketAddress = (InetSocketAddress)socketAddress;
            final NativeInetAddress address = NativeInetAddress.newInstance(inetSocketAddress.getAddress());
            res = connect(this.fd, address.address, address.scopeId, inetSocketAddress.getPort());
        }
        else {
            if (!(socketAddress instanceof DomainSocketAddress)) {
                throw new Error(new StringBuilder().append("Unexpected SocketAddress implementation ").append(socketAddress).toString());
            }
            final DomainSocketAddress unixDomainSocketAddress = (DomainSocketAddress)socketAddress;
            res = connectDomainSocket(this.fd, unixDomainSocketAddress.path().getBytes(CharsetUtil.UTF_8));
        }
        if (res < 0) {
            if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE) {
                return false;
            }
            Errors.throwConnectException("connect", Socket.CONNECT_REFUSED_EXCEPTION, res);
        }
        return true;
    }
    
    public final boolean finishConnect() throws IOException {
        final int res = finishConnect(this.fd);
        if (res < 0) {
            if (res == Errors.ERRNO_EINPROGRESS_NEGATIVE) {
                return false;
            }
            Errors.throwConnectException("finishConnect", Socket.FINISH_CONNECT_REFUSED_EXCEPTION, res);
        }
        return true;
    }
    
    public final void disconnect() throws IOException {
        final int res = disconnect(this.fd);
        if (res < 0) {
            Errors.throwConnectException("disconnect", Socket.FINISH_CONNECT_REFUSED_EXCEPTION, res);
        }
    }
    
    public final void bind(final SocketAddress socketAddress) throws IOException {
        if (socketAddress instanceof InetSocketAddress) {
            final InetSocketAddress addr = (InetSocketAddress)socketAddress;
            final NativeInetAddress address = NativeInetAddress.newInstance(addr.getAddress());
            final int res = bind(this.fd, address.address, address.scopeId, addr.getPort());
            if (res < 0) {
                throw Errors.newIOException("bind", res);
            }
        }
        else {
            if (!(socketAddress instanceof DomainSocketAddress)) {
                throw new Error(new StringBuilder().append("Unexpected SocketAddress implementation ").append(socketAddress).toString());
            }
            final DomainSocketAddress addr2 = (DomainSocketAddress)socketAddress;
            final int res2 = bindDomainSocket(this.fd, addr2.path().getBytes(CharsetUtil.UTF_8));
            if (res2 < 0) {
                throw Errors.newIOException("bind", res2);
            }
        }
    }
    
    public final void listen(final int backlog) throws IOException {
        final int res = listen(this.fd, backlog);
        if (res < 0) {
            throw Errors.newIOException("listen", res);
        }
    }
    
    public final int accept(final byte[] addr) throws IOException {
        final int res = accept(this.fd, addr);
        if (res >= 0) {
            return res;
        }
        if (res == Errors.ERRNO_EAGAIN_NEGATIVE || res == Errors.ERRNO_EWOULDBLOCK_NEGATIVE) {
            return -1;
        }
        throw Errors.newIOException("accept", res);
    }
    
    public final InetSocketAddress remoteAddress() {
        final byte[] addr = remoteAddress(this.fd);
        return (addr == null) ? null : NativeInetAddress.address(addr, 0, addr.length);
    }
    
    public final InetSocketAddress localAddress() {
        final byte[] addr = localAddress(this.fd);
        return (addr == null) ? null : NativeInetAddress.address(addr, 0, addr.length);
    }
    
    public final int getReceiveBufferSize() throws IOException {
        return getReceiveBufferSize(this.fd);
    }
    
    public final int getSendBufferSize() throws IOException {
        return getSendBufferSize(this.fd);
    }
    
    public final boolean isKeepAlive() throws IOException {
        return isKeepAlive(this.fd) != 0;
    }
    
    public final boolean isTcpNoDelay() throws IOException {
        return isTcpNoDelay(this.fd) != 0;
    }
    
    public final boolean isReuseAddress() throws IOException {
        return isReuseAddress(this.fd) != 0;
    }
    
    public final boolean isReusePort() throws IOException {
        return isReusePort(this.fd) != 0;
    }
    
    public final boolean isBroadcast() throws IOException {
        return isBroadcast(this.fd) != 0;
    }
    
    public final int getSoLinger() throws IOException {
        return getSoLinger(this.fd);
    }
    
    public final int getSoError() throws IOException {
        return getSoError(this.fd);
    }
    
    public final int getTrafficClass() throws IOException {
        return getTrafficClass(this.fd);
    }
    
    public final void setKeepAlive(final boolean keepAlive) throws IOException {
        setKeepAlive(this.fd, keepAlive ? 1 : 0);
    }
    
    public final void setReceiveBufferSize(final int receiveBufferSize) throws IOException {
        setReceiveBufferSize(this.fd, receiveBufferSize);
    }
    
    public final void setSendBufferSize(final int sendBufferSize) throws IOException {
        setSendBufferSize(this.fd, sendBufferSize);
    }
    
    public final void setTcpNoDelay(final boolean tcpNoDelay) throws IOException {
        setTcpNoDelay(this.fd, tcpNoDelay ? 1 : 0);
    }
    
    public final void setSoLinger(final int soLinger) throws IOException {
        setSoLinger(this.fd, soLinger);
    }
    
    public final void setReuseAddress(final boolean reuseAddress) throws IOException {
        setReuseAddress(this.fd, reuseAddress ? 1 : 0);
    }
    
    public final void setReusePort(final boolean reusePort) throws IOException {
        setReusePort(this.fd, reusePort ? 1 : 0);
    }
    
    public final void setBroadcast(final boolean broadcast) throws IOException {
        setBroadcast(this.fd, broadcast ? 1 : 0);
    }
    
    public final void setTrafficClass(final int trafficClass) throws IOException {
        setTrafficClass(this.fd, trafficClass);
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append("Socket{fd=").append(this.fd).append('}').toString();
    }
    
    public static Socket newSocketStream() {
        return new Socket(newSocketStream0());
    }
    
    public static Socket newSocketDgram() {
        return new Socket(newSocketDgram0());
    }
    
    public static Socket newSocketDomain() {
        return new Socket(newSocketDomain0());
    }
    
    public static void initialize() {
        if (Socket.INITIALIZED.compareAndSet(false, true)) {
            initialize(NetUtil.isIpV4StackPreferred());
        }
    }
    
    protected static int newSocketStream0() {
        final int res = newSocketStreamFd();
        if (res < 0) {
            throw new ChannelException((Throwable)Errors.newIOException("newSocketStream", res));
        }
        return res;
    }
    
    protected static int newSocketDgram0() {
        final int res = newSocketDgramFd();
        if (res < 0) {
            throw new ChannelException((Throwable)Errors.newIOException("newSocketDgram", res));
        }
        return res;
    }
    
    protected static int newSocketDomain0() {
        final int res = newSocketDomainFd();
        if (res < 0) {
            throw new ChannelException((Throwable)Errors.newIOException("newSocketDomain", res));
        }
        return res;
    }
    
    private static native int shutdown(final int integer, final boolean boolean2, final boolean boolean3);
    
    private static native int connect(final int integer1, final byte[] arr, final int integer3, final int integer4);
    
    private static native int connectDomainSocket(final int integer, final byte[] arr);
    
    private static native int finishConnect(final int integer);
    
    private static native int disconnect(final int integer);
    
    private static native int bind(final int integer1, final byte[] arr, final int integer3, final int integer4);
    
    private static native int bindDomainSocket(final int integer, final byte[] arr);
    
    private static native int listen(final int integer1, final int integer2);
    
    private static native int accept(final int integer, final byte[] arr);
    
    private static native byte[] remoteAddress(final int integer);
    
    private static native byte[] localAddress(final int integer);
    
    private static native int sendTo(final int integer1, final ByteBuffer byteBuffer, final int integer3, final int integer4, final byte[] arr, final int integer6, final int integer7);
    
    private static native int sendToAddress(final int integer1, final long long2, final int integer3, final int integer4, final byte[] arr, final int integer6, final int integer7);
    
    private static native int sendToAddresses(final int integer1, final long long2, final int integer3, final byte[] arr, final int integer5, final int integer6);
    
    private static native DatagramSocketAddress recvFrom(final int integer1, final ByteBuffer byteBuffer, final int integer3, final int integer4) throws IOException;
    
    private static native DatagramSocketAddress recvFromAddress(final int integer1, final long long2, final int integer3, final int integer4) throws IOException;
    
    private static native int recvFd(final int integer);
    
    private static native int sendFd(final int integer1, final int integer2);
    
    private static native int newSocketStreamFd();
    
    private static native int newSocketDgramFd();
    
    private static native int newSocketDomainFd();
    
    private static native int isReuseAddress(final int integer) throws IOException;
    
    private static native int isReusePort(final int integer) throws IOException;
    
    private static native int getReceiveBufferSize(final int integer) throws IOException;
    
    private static native int getSendBufferSize(final int integer) throws IOException;
    
    private static native int isKeepAlive(final int integer) throws IOException;
    
    private static native int isTcpNoDelay(final int integer) throws IOException;
    
    private static native int isBroadcast(final int integer) throws IOException;
    
    private static native int getSoLinger(final int integer) throws IOException;
    
    private static native int getSoError(final int integer) throws IOException;
    
    private static native int getTrafficClass(final int integer) throws IOException;
    
    private static native void setReuseAddress(final int integer1, final int integer2) throws IOException;
    
    private static native void setReusePort(final int integer1, final int integer2) throws IOException;
    
    private static native void setKeepAlive(final int integer1, final int integer2) throws IOException;
    
    private static native void setReceiveBufferSize(final int integer1, final int integer2) throws IOException;
    
    private static native void setSendBufferSize(final int integer1, final int integer2) throws IOException;
    
    private static native void setTcpNoDelay(final int integer1, final int integer2) throws IOException;
    
    private static native void setSoLinger(final int integer1, final int integer2) throws IOException;
    
    private static native void setBroadcast(final int integer1, final int integer2) throws IOException;
    
    private static native void setTrafficClass(final int integer1, final int integer2) throws IOException;
    
    private static native void initialize(final boolean boolean1);
    
    static {
        SHUTDOWN_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Socket.class, "shutdown(..)");
        SEND_TO_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Socket.class, "sendTo(..)");
        SEND_TO_ADDRESS_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Socket.class, "sendToAddress(..)");
        SEND_TO_ADDRESSES_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Socket.class, "sendToAddresses(..)");
        SEND_TO_CONNECTION_RESET_EXCEPTION = ThrowableUtil.<Errors.NativeIoException>unknownStackTrace(Errors.newConnectionResetException("syscall:sendto", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendTo(..)");
        SEND_TO_ADDRESS_CONNECTION_RESET_EXCEPTION = ThrowableUtil.<Errors.NativeIoException>unknownStackTrace(Errors.newConnectionResetException("syscall:sendto", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendToAddress");
        CONNECTION_RESET_EXCEPTION_SENDMSG = ThrowableUtil.<Errors.NativeIoException>unknownStackTrace(Errors.newConnectionResetException("syscall:sendmsg", Errors.ERRNO_EPIPE_NEGATIVE), Socket.class, "sendToAddresses(..)");
        CONNECTION_RESET_SHUTDOWN_EXCEPTION = ThrowableUtil.<Errors.NativeIoException>unknownStackTrace(Errors.newConnectionResetException("syscall:shutdown", Errors.ERRNO_ECONNRESET_NEGATIVE), Socket.class, "shutdown");
        FINISH_CONNECT_REFUSED_EXCEPTION = ThrowableUtil.<Errors.NativeConnectException>unknownStackTrace(new Errors.NativeConnectException("syscall:getsockopt", Errors.ERROR_ECONNREFUSED_NEGATIVE), Socket.class, "finishConnect(..)");
        CONNECT_REFUSED_EXCEPTION = ThrowableUtil.<Errors.NativeConnectException>unknownStackTrace(new Errors.NativeConnectException("syscall:connect", Errors.ERROR_ECONNREFUSED_NEGATIVE), Socket.class, "connect(..)");
        UDS_SUN_PATH_SIZE = LimitsStaticallyReferencedJniMethods.udsSunPathSize();
        INITIALIZED = new AtomicBoolean();
    }
}
