package io.netty.channel.kqueue;

import io.netty.channel.unix.Socket;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.ThrowableUtil;
import io.netty.util.internal.NativeLibraryLoader;
import io.netty.util.internal.PlatformDependent;
import java.util.Locale;
import io.netty.util.internal.SystemPropertyUtil;
import java.io.IOException;
import io.netty.channel.unix.Errors;
import io.netty.channel.unix.FileDescriptor;
import io.netty.util.internal.logging.InternalLogger;

final class Native {
    private static final InternalLogger logger;
    static final short EV_ADD;
    static final short EV_ENABLE;
    static final short EV_DISABLE;
    static final short EV_DELETE;
    static final short EV_CLEAR;
    static final short EV_ERROR;
    static final short EV_EOF;
    static final int NOTE_READCLOSED;
    static final int NOTE_CONNRESET;
    static final int NOTE_DISCONNECTED;
    static final int NOTE_RDHUP;
    static final short EV_ADD_CLEAR_ENABLE;
    static final short EV_DELETE_DISABLE;
    static final short EVFILT_READ;
    static final short EVFILT_WRITE;
    static final short EVFILT_USER;
    static final short EVFILT_SOCK;
    
    static FileDescriptor newKQueue() {
        return new FileDescriptor(kqueueCreate());
    }
    
    static int keventWait(final int kqueueFd, final KQueueEventArray changeList, final KQueueEventArray eventList, final int tvSec, final int tvNsec) throws IOException {
        final int ready = keventWait(kqueueFd, changeList.memoryAddress(), changeList.size(), eventList.memoryAddress(), eventList.capacity(), tvSec, tvNsec);
        if (ready < 0) {
            throw Errors.newIOException("kevent", ready);
        }
        return ready;
    }
    
    private static native int kqueueCreate();
    
    private static native int keventWait(final int integer1, final long long2, final int integer3, final long long4, final int integer5, final int integer6, final int integer7);
    
    static native int keventTriggerUserEvent(final int integer1, final int integer2);
    
    static native int keventAddUserEvent(final int integer1, final int integer2);
    
    static native int sizeofKEvent();
    
    static native int offsetofKEventIdent();
    
    static native int offsetofKEventFlags();
    
    static native int offsetofKEventFFlags();
    
    static native int offsetofKEventFilter();
    
    static native int offsetofKeventData();
    
    private static void loadNativeLibrary() {
        final String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK).trim();
        if (!name.startsWith("mac") && !name.contains("bsd") && !name.startsWith("darwin")) {
            throw new IllegalStateException("Only supported on BSD");
        }
        final String staticLibName = "netty_transport_native_kqueue";
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
            sizeofKEvent();
        }
        catch (UnsatisfiedLinkError ignore) {
            loadNativeLibrary();
        }
        Socket.initialize();
        EV_ADD = KQueueStaticallyReferencedJniMethods.evAdd();
        EV_ENABLE = KQueueStaticallyReferencedJniMethods.evEnable();
        EV_DISABLE = KQueueStaticallyReferencedJniMethods.evDisable();
        EV_DELETE = KQueueStaticallyReferencedJniMethods.evDelete();
        EV_CLEAR = KQueueStaticallyReferencedJniMethods.evClear();
        EV_ERROR = KQueueStaticallyReferencedJniMethods.evError();
        EV_EOF = KQueueStaticallyReferencedJniMethods.evEOF();
        NOTE_READCLOSED = KQueueStaticallyReferencedJniMethods.noteReadClosed();
        NOTE_CONNRESET = KQueueStaticallyReferencedJniMethods.noteConnReset();
        NOTE_DISCONNECTED = KQueueStaticallyReferencedJniMethods.noteDisconnected();
        NOTE_RDHUP = (Native.NOTE_READCLOSED | Native.NOTE_CONNRESET | Native.NOTE_DISCONNECTED);
        EV_ADD_CLEAR_ENABLE = (short)(Native.EV_ADD | Native.EV_CLEAR | Native.EV_ENABLE);
        EV_DELETE_DISABLE = (short)(Native.EV_DELETE | Native.EV_DISABLE);
        EVFILT_READ = KQueueStaticallyReferencedJniMethods.evfiltRead();
        EVFILT_WRITE = KQueueStaticallyReferencedJniMethods.evfiltWrite();
        EVFILT_USER = KQueueStaticallyReferencedJniMethods.evfiltUser();
        EVFILT_SOCK = KQueueStaticallyReferencedJniMethods.evfiltSock();
    }
}
