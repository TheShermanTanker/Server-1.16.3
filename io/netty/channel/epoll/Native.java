package io.netty.channel.epoll;

import io.netty.channel.unix.Socket;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import java.util.Locale;
import io.netty.util.internal.SystemPropertyUtil;
import java.io.IOException;
import io.netty.channel.unix.FileDescriptor;
import java.nio.channels.ClosedChannelException;
import io.netty.channel.unix.Errors;
import io.netty.util.internal.logging.InternalLogger;

public final class Native {
    private static final InternalLogger logger;
    public static final int EPOLLIN;
    public static final int EPOLLOUT;
    public static final int EPOLLRDHUP;
    public static final int EPOLLET;
    public static final int EPOLLERR;
    public static final boolean IS_SUPPORTING_SENDMMSG;
    public static final boolean IS_SUPPORTING_TCP_FASTOPEN;
    public static final int TCP_MD5SIG_MAXKEYLEN;
    public static final String KERNEL_VERSION;
    private static final Errors.NativeIoException SENDMMSG_CONNECTION_RESET_EXCEPTION;
    private static final Errors.NativeIoException SPLICE_CONNECTION_RESET_EXCEPTION;
    private static final ClosedChannelException SENDMMSG_CLOSED_CHANNEL_EXCEPTION;
    private static final ClosedChannelException SPLICE_CLOSED_CHANNEL_EXCEPTION;
    
    public static FileDescriptor newEventFd() {
        return new FileDescriptor(eventFd());
    }
    
    public static FileDescriptor newTimerFd() {
        return new FileDescriptor(timerFd());
    }
    
    private static native int eventFd();
    
    private static native int timerFd();
    
    public static native void eventFdWrite(final int integer, final long long2);
    
    public static native void eventFdRead(final int integer);
    
    static native void timerFdRead(final int integer);
    
    public static FileDescriptor newEpollCreate() {
        return new FileDescriptor(epollCreate());
    }
    
    private static native int epollCreate();
    
    public static int epollWait(final FileDescriptor epollFd, final EpollEventArray events, final FileDescriptor timerFd, final int timeoutSec, final int timeoutNs) throws IOException {
        final int ready = epollWait0(epollFd.intValue(), events.memoryAddress(), events.length(), timerFd.intValue(), timeoutSec, timeoutNs);
        if (ready < 0) {
            throw Errors.newIOException("epoll_wait", ready);
        }
        return ready;
    }
    
    private static native int epollWait0(final int integer1, final long long2, final int integer3, final int integer4, final int integer5, final int integer6);
    
    public static void epollCtlAdd(final int efd, final int fd, final int flags) throws IOException {
        final int res = epollCtlAdd0(efd, fd, flags);
        if (res < 0) {
            throw Errors.newIOException("epoll_ctl", res);
        }
    }
    
    private static native int epollCtlAdd0(final int integer1, final int integer2, final int integer3);
    
    public static void epollCtlMod(final int efd, final int fd, final int flags) throws IOException {
        final int res = epollCtlMod0(efd, fd, flags);
        if (res < 0) {
            throw Errors.newIOException("epoll_ctl", res);
        }
    }
    
    private static native int epollCtlMod0(final int integer1, final int integer2, final int integer3);
    
    public static void epollCtlDel(final int efd, final int fd) throws IOException {
        final int res = epollCtlDel0(efd, fd);
        if (res < 0) {
            throw Errors.newIOException("epoll_ctl", res);
        }
    }
    
    private static native int epollCtlDel0(final int integer1, final int integer2);
    
    public static int splice(final int fd, final long offIn, final int fdOut, final long offOut, final long len) throws IOException {
        final int res = splice0(fd, offIn, fdOut, offOut, len);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("splice", res, Native.SPLICE_CONNECTION_RESET_EXCEPTION, Native.SPLICE_CLOSED_CHANNEL_EXCEPTION);
    }
    
    private static native int splice0(final int integer1, final long long2, final int integer3, final long long4, final long long5);
    
    public static int sendmmsg(final int fd, final NativeDatagramPacketArray.NativeDatagramPacket[] msgs, final int offset, final int len) throws IOException {
        final int res = sendmmsg0(fd, msgs, offset, len);
        if (res >= 0) {
            return res;
        }
        return Errors.ioResult("sendmmsg", res, Native.SENDMMSG_CONNECTION_RESET_EXCEPTION, Native.SENDMMSG_CLOSED_CHANNEL_EXCEPTION);
    }
    
    private static native int sendmmsg0(final int integer1, final NativeDatagramPacketArray.NativeDatagramPacket[] arr, final int integer3, final int integer4);
    
    public static native int sizeofEpollEvent();
    
    public static native int offsetofEpollData();
    
    private static void loadNativeLibrary() {
        final String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        if (!name.startsWith("linux")) {
            throw new IllegalStateException("Only supported on Linux");
        }
        final String staticLibName = "netty_transport_native_epoll";
        final String sharedLibName = staticLibName + '_' + PlatformDependent.normalizedArch();
        final ClassLoader cl = PlatformDependent.getClassLoader(Native.class);
        try {
            NativeLibraryLoader.load(sharedLibName, cl);
        }
        catch (UnsatisfiedLinkError e1) {
            try {
                NativeLibraryLoader.load(staticLibName, cl);
                Native.logger.debug("Failed to load {}", sharedLibName, e1);
            }
            catch (UnsatisfiedLinkError e2) {
                ThrowableUtil.addSuppressed((Throwable)e1, (Throwable)e2);
                throw e1;
            }
        }
    }
    
    private Native() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(Native.class);
        try {
            offsetofEpollData();
        }
        catch (UnsatisfiedLinkError ignore) {
            loadNativeLibrary();
        }
        Socket.initialize();
        EPOLLIN = NativeStaticallyReferencedJniMethods.epollin();
        EPOLLOUT = NativeStaticallyReferencedJniMethods.epollout();
        EPOLLRDHUP = NativeStaticallyReferencedJniMethods.epollrdhup();
        EPOLLET = NativeStaticallyReferencedJniMethods.epollet();
        EPOLLERR = NativeStaticallyReferencedJniMethods.epollerr();
        IS_SUPPORTING_SENDMMSG = NativeStaticallyReferencedJniMethods.isSupportingSendmmsg();
        IS_SUPPORTING_TCP_FASTOPEN = NativeStaticallyReferencedJniMethods.isSupportingTcpFastopen();
        TCP_MD5SIG_MAXKEYLEN = NativeStaticallyReferencedJniMethods.tcpMd5SigMaxKeyLen();
        KERNEL_VERSION = NativeStaticallyReferencedJniMethods.kernelVersion();
        SENDMMSG_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Native.class, "sendmmsg(...)");
        SPLICE_CLOSED_CHANNEL_EXCEPTION = ThrowableUtil.<ClosedChannelException>unknownStackTrace(new ClosedChannelException(), Native.class, "splice(...)");
        SENDMMSG_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendmmsg(...)", Errors.ERRNO_EPIPE_NEGATIVE);
        SPLICE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:splice(...)", Errors.ERRNO_EPIPE_NEGATIVE);
    }
}
