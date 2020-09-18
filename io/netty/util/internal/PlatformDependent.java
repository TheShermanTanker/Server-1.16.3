package io.netty.util.internal;

import java.security.AccessController;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import java.security.PrivilegedAction;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscUnboundedAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscUnboundedArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscGrowableAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscChunkedArrayQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.ByteOrder;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.regex.Matcher;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.Deque;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.SpscLinkedAtomicQueue;
import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue;
import java.util.Queue;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.io.File;
import java.util.regex.Pattern;
import io.netty.util.internal.logging.InternalLogger;

public final class PlatformDependent {
    private static final InternalLogger logger;
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN;
    private static final boolean IS_WINDOWS;
    private static final boolean IS_OSX;
    private static final boolean MAYBE_SUPER_USER;
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
    private static final boolean DIRECT_BUFFER_PREFERRED;
    private static final long MAX_DIRECT_MEMORY;
    private static final int MPSC_CHUNK_SIZE = 1024;
    private static final int MIN_MAX_MPSC_CAPACITY = 2048;
    private static final int MAX_ALLOWED_MPSC_CAPACITY = 1073741824;
    private static final long BYTE_ARRAY_BASE_OFFSET;
    private static final File TMPDIR;
    private static final int BIT_MODE;
    private static final String NORMALIZED_ARCH;
    private static final String NORMALIZED_OS;
    private static final int ADDRESS_SIZE;
    private static final boolean USE_DIRECT_BUFFER_NO_CLEANER;
    private static final AtomicLong DIRECT_MEMORY_COUNTER;
    private static final long DIRECT_MEMORY_LIMIT;
    private static final ThreadLocalRandomProvider RANDOM_PROVIDER;
    private static final Cleaner CLEANER;
    private static final int UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD;
    public static final boolean BIG_ENDIAN_NATIVE_ORDER;
    private static final Cleaner NOOP;
    
    public static boolean hasDirectBufferNoCleanerConstructor() {
        return PlatformDependent0.hasDirectBufferNoCleanerConstructor();
    }
    
    public static byte[] allocateUninitializedArray(final int size) {
        return (PlatformDependent.UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD < 0 || PlatformDependent.UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD > size) ? new byte[size] : PlatformDependent0.allocateUninitializedArray(size);
    }
    
    public static boolean isAndroid() {
        return PlatformDependent0.isAndroid();
    }
    
    public static boolean isWindows() {
        return PlatformDependent.IS_WINDOWS;
    }
    
    public static boolean isOsx() {
        return PlatformDependent.IS_OSX;
    }
    
    public static boolean maybeSuperUser() {
        return PlatformDependent.MAYBE_SUPER_USER;
    }
    
    public static int javaVersion() {
        return PlatformDependent0.javaVersion();
    }
    
    public static boolean canEnableTcpNoDelayByDefault() {
        return PlatformDependent.CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }
    
    public static boolean hasUnsafe() {
        return PlatformDependent.UNSAFE_UNAVAILABILITY_CAUSE == null;
    }
    
    public static Throwable getUnsafeUnavailabilityCause() {
        return PlatformDependent.UNSAFE_UNAVAILABILITY_CAUSE;
    }
    
    public static boolean isUnaligned() {
        return PlatformDependent0.isUnaligned();
    }
    
    public static boolean directBufferPreferred() {
        return PlatformDependent.DIRECT_BUFFER_PREFERRED;
    }
    
    public static long maxDirectMemory() {
        return PlatformDependent.MAX_DIRECT_MEMORY;
    }
    
    public static File tmpdir() {
        return PlatformDependent.TMPDIR;
    }
    
    public static int bitMode() {
        return PlatformDependent.BIT_MODE;
    }
    
    public static int addressSize() {
        return PlatformDependent.ADDRESS_SIZE;
    }
    
    public static long allocateMemory(final long size) {
        return PlatformDependent0.allocateMemory(size);
    }
    
    public static void freeMemory(final long address) {
        PlatformDependent0.freeMemory(address);
    }
    
    public static long reallocateMemory(final long address, final long newSize) {
        return PlatformDependent0.reallocateMemory(address, newSize);
    }
    
    public static void throwException(final Throwable t) {
        if (hasUnsafe()) {
            PlatformDependent0.throwException(t);
        }
        else {
            PlatformDependent.<Throwable>throwException0(t);
        }
    }
    
    private static <E extends Throwable> void throwException0(final Throwable t) throws E, Throwable {
        throw t;
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        return (ConcurrentMap<K, V>)new ConcurrentHashMap();
    }
    
    public static LongCounter newLongCounter() {
        if (javaVersion() >= 8) {
            return new LongAdderCounter();
        }
        return new AtomicLongCounter();
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity) {
        return (ConcurrentMap<K, V>)new ConcurrentHashMap(initialCapacity);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity, final float loadFactor) {
        return (ConcurrentMap<K, V>)new ConcurrentHashMap(initialCapacity, loadFactor);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final int initialCapacity, final float loadFactor, final int concurrencyLevel) {
        return (ConcurrentMap<K, V>)new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
    }
    
    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(final Map<? extends K, ? extends V> map) {
        return (ConcurrentMap<K, V>)new ConcurrentHashMap((Map)map);
    }
    
    public static void freeDirectBuffer(final ByteBuffer buffer) {
        PlatformDependent.CLEANER.freeDirectBuffer(buffer);
    }
    
    public static long directBufferAddress(final ByteBuffer buffer) {
        return PlatformDependent0.directBufferAddress(buffer);
    }
    
    public static ByteBuffer directBuffer(final long memoryAddress, final int size) {
        if (PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            return PlatformDependent0.newDirectBuffer(memoryAddress, size);
        }
        throw new UnsupportedOperationException("sun.misc.Unsafe or java.nio.DirectByteBuffer.<init>(long, int) not available");
    }
    
    public static int getInt(final Object object, final long fieldOffset) {
        return PlatformDependent0.getInt(object, fieldOffset);
    }
    
    public static byte getByte(final long address) {
        return PlatformDependent0.getByte(address);
    }
    
    public static short getShort(final long address) {
        return PlatformDependent0.getShort(address);
    }
    
    public static int getInt(final long address) {
        return PlatformDependent0.getInt(address);
    }
    
    public static long getLong(final long address) {
        return PlatformDependent0.getLong(address);
    }
    
    public static byte getByte(final byte[] data, final int index) {
        return PlatformDependent0.getByte(data, index);
    }
    
    public static short getShort(final byte[] data, final int index) {
        return PlatformDependent0.getShort(data, index);
    }
    
    public static int getInt(final byte[] data, final int index) {
        return PlatformDependent0.getInt(data, index);
    }
    
    public static long getLong(final byte[] data, final int index) {
        return PlatformDependent0.getLong(data, index);
    }
    
    private static long getLongSafe(final byte[] bytes, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (long)bytes[offset] << 56 | ((long)bytes[offset + 1] & 0xFFL) << 48 | ((long)bytes[offset + 2] & 0xFFL) << 40 | ((long)bytes[offset + 3] & 0xFFL) << 32 | ((long)bytes[offset + 4] & 0xFFL) << 24 | ((long)bytes[offset + 5] & 0xFFL) << 16 | ((long)bytes[offset + 6] & 0xFFL) << 8 | ((long)bytes[offset + 7] & 0xFFL);
        }
        return ((long)bytes[offset] & 0xFFL) | ((long)bytes[offset + 1] & 0xFFL) << 8 | ((long)bytes[offset + 2] & 0xFFL) << 16 | ((long)bytes[offset + 3] & 0xFFL) << 24 | ((long)bytes[offset + 4] & 0xFFL) << 32 | ((long)bytes[offset + 5] & 0xFFL) << 40 | ((long)bytes[offset + 6] & 0xFFL) << 48 | (long)bytes[offset + 7] << 56;
    }
    
    private static int getIntSafe(final byte[] bytes, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return bytes[offset] << 24 | (bytes[offset + 1] & 0xFF) << 16 | (bytes[offset + 2] & 0xFF) << 8 | (bytes[offset + 3] & 0xFF);
        }
        return (bytes[offset] & 0xFF) | (bytes[offset + 1] & 0xFF) << 8 | (bytes[offset + 2] & 0xFF) << 16 | bytes[offset + 3] << 24;
    }
    
    private static short getShortSafe(final byte[] bytes, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (short)(bytes[offset] << 8 | (bytes[offset + 1] & 0xFF));
        }
        return (short)((bytes[offset] & 0xFF) | bytes[offset + 1] << 8);
    }
    
    private static int hashCodeAsciiCompute(final CharSequence value, final int offset, final int hash) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return hash * -862048943 + hashCodeAsciiSanitizeInt(value, offset + 4) * 461845907 + hashCodeAsciiSanitizeInt(value, offset);
        }
        return hash * -862048943 + hashCodeAsciiSanitizeInt(value, offset) * 461845907 + hashCodeAsciiSanitizeInt(value, offset + 4);
    }
    
    private static int hashCodeAsciiSanitizeInt(final CharSequence value, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (value.charAt(offset + 3) & '\u001f') | (value.charAt(offset + 2) & '\u001f') << 8 | (value.charAt(offset + 1) & '\u001f') << 16 | (value.charAt(offset) & '\u001f') << 24;
        }
        return (value.charAt(offset + 3) & '\u001f') << 24 | (value.charAt(offset + 2) & '\u001f') << 16 | (value.charAt(offset + 1) & '\u001f') << 8 | (value.charAt(offset) & '\u001f');
    }
    
    private static int hashCodeAsciiSanitizeShort(final CharSequence value, final int offset) {
        if (PlatformDependent.BIG_ENDIAN_NATIVE_ORDER) {
            return (value.charAt(offset + 1) & '\u001f') | (value.charAt(offset) & '\u001f') << 8;
        }
        return (value.charAt(offset + 1) & '\u001f') << 8 | (value.charAt(offset) & '\u001f');
    }
    
    private static int hashCodeAsciiSanitizeByte(final char value) {
        return value & '\u001f';
    }
    
    public static void putByte(final long address, final byte value) {
        PlatformDependent0.putByte(address, value);
    }
    
    public static void putShort(final long address, final short value) {
        PlatformDependent0.putShort(address, value);
    }
    
    public static void putInt(final long address, final int value) {
        PlatformDependent0.putInt(address, value);
    }
    
    public static void putLong(final long address, final long value) {
        PlatformDependent0.putLong(address, value);
    }
    
    public static void putByte(final byte[] data, final int index, final byte value) {
        PlatformDependent0.putByte(data, index, value);
    }
    
    public static void putShort(final byte[] data, final int index, final short value) {
        PlatformDependent0.putShort(data, index, value);
    }
    
    public static void putInt(final byte[] data, final int index, final int value) {
        PlatformDependent0.putInt(data, index, value);
    }
    
    public static void putLong(final byte[] data, final int index, final long value) {
        PlatformDependent0.putLong(data, index, value);
    }
    
    public static void copyMemory(final long srcAddr, final long dstAddr, final long length) {
        PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
    }
    
    public static void copyMemory(final byte[] src, final int srcIndex, final long dstAddr, final long length) {
        PlatformDependent0.copyMemory(src, PlatformDependent.BYTE_ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
    }
    
    public static void copyMemory(final long srcAddr, final byte[] dst, final int dstIndex, final long length) {
        PlatformDependent0.copyMemory(null, srcAddr, dst, PlatformDependent.BYTE_ARRAY_BASE_OFFSET + dstIndex, length);
    }
    
    public static void setMemory(final byte[] dst, final int dstIndex, final long bytes, final byte value) {
        PlatformDependent0.setMemory(dst, PlatformDependent.BYTE_ARRAY_BASE_OFFSET + dstIndex, bytes, value);
    }
    
    public static void setMemory(final long address, final long bytes, final byte value) {
        PlatformDependent0.setMemory(address, bytes, value);
    }
    
    public static ByteBuffer allocateDirectNoCleaner(final int capacity) {
        assert PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
        incrementMemoryCounter(capacity);
        try {
            return PlatformDependent0.allocateDirectNoCleaner(capacity);
        }
        catch (Throwable e) {
            decrementMemoryCounter(capacity);
            throwException(e);
            return null;
        }
    }
    
    public static ByteBuffer reallocateDirectNoCleaner(final ByteBuffer buffer, final int capacity) {
        assert PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
        final int len = capacity - buffer.capacity();
        incrementMemoryCounter(len);
        try {
            return PlatformDependent0.reallocateDirectNoCleaner(buffer, capacity);
        }
        catch (Throwable e) {
            decrementMemoryCounter(len);
            throwException(e);
            return null;
        }
    }
    
    public static void freeDirectNoCleaner(final ByteBuffer buffer) {
        assert PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
        final int capacity = buffer.capacity();
        PlatformDependent0.freeMemory(PlatformDependent0.directBufferAddress(buffer));
        decrementMemoryCounter(capacity);
    }
    
    private static void incrementMemoryCounter(final int capacity) {
        if (PlatformDependent.DIRECT_MEMORY_COUNTER != null) {
            long usedMemory;
            long newUsedMemory;
            do {
                usedMemory = PlatformDependent.DIRECT_MEMORY_COUNTER.get();
                newUsedMemory = usedMemory + capacity;
                if (newUsedMemory > PlatformDependent.DIRECT_MEMORY_LIMIT) {
                    throw new OutOfDirectMemoryError(new StringBuilder().append("failed to allocate ").append(capacity).append(" byte(s) of direct memory (used: ").append(usedMemory).append(", max: ").append(PlatformDependent.DIRECT_MEMORY_LIMIT).append(')').toString());
                }
            } while (!PlatformDependent.DIRECT_MEMORY_COUNTER.compareAndSet(usedMemory, newUsedMemory));
        }
    }
    
    private static void decrementMemoryCounter(final int capacity) {
        if (PlatformDependent.DIRECT_MEMORY_COUNTER != null) {
            final long usedMemory = PlatformDependent.DIRECT_MEMORY_COUNTER.addAndGet((long)(-capacity));
            assert usedMemory >= 0L;
        }
    }
    
    public static boolean useDirectBufferNoCleaner() {
        return PlatformDependent.USE_DIRECT_BUFFER_NO_CLEANER;
    }
    
    public static boolean equals(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? equalsSafe(bytes1, startPos1, bytes2, startPos2, length) : PlatformDependent0.equals(bytes1, startPos1, bytes2, startPos2, length);
    }
    
    public static boolean isZero(final byte[] bytes, final int startPos, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? isZeroSafe(bytes, startPos, length) : PlatformDependent0.isZero(bytes, startPos, length);
    }
    
    public static int equalsConstantTime(final byte[] bytes1, final int startPos1, final byte[] bytes2, final int startPos2, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? ConstantTimeUtils.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length) : PlatformDependent0.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length);
    }
    
    public static int hashCodeAscii(final byte[] bytes, final int startPos, final int length) {
        return (!hasUnsafe() || !PlatformDependent0.unalignedAccess()) ? hashCodeAsciiSafe(bytes, startPos, length) : PlatformDependent0.hashCodeAscii(bytes, startPos, length);
    }
    
    public static int hashCodeAscii(final CharSequence bytes) {
        int hash = -1028477387;
        final int remainingBytes = bytes.length() & 0x7;
        switch (bytes.length()) {
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31: {
                hash = hashCodeAsciiCompute(bytes, bytes.length() - 24, hashCodeAsciiCompute(bytes, bytes.length() - 16, hashCodeAsciiCompute(bytes, bytes.length() - 8, hash)));
                break;
            }
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23: {
                hash = hashCodeAsciiCompute(bytes, bytes.length() - 16, hashCodeAsciiCompute(bytes, bytes.length() - 8, hash));
                break;
            }
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15: {
                hash = hashCodeAsciiCompute(bytes, bytes.length() - 8, hash);
                break;
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7: {
                break;
            }
            default: {
                for (int i = bytes.length() - 8; i >= remainingBytes; i -= 8) {
                    hash = hashCodeAsciiCompute(bytes, i, hash);
                }
                break;
            }
        }
        switch (remainingBytes) {
            case 7: {
                return ((hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeShort(bytes, 1)) * -862048943 + hashCodeAsciiSanitizeInt(bytes, 3);
            }
            case 6: {
                return (hash * -862048943 + hashCodeAsciiSanitizeShort(bytes, 0)) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 2);
            }
            case 5: {
                return (hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 1);
            }
            case 4: {
                return hash * -862048943 + hashCodeAsciiSanitizeInt(bytes, 0);
            }
            case 3: {
                return (hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeShort(bytes, 1);
            }
            case 2: {
                return hash * -862048943 + hashCodeAsciiSanitizeShort(bytes, 0);
            }
            case 1: {
                return hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0));
            }
            default: {
                return hash;
            }
        }
    }
    
    public static <T> Queue<T> newMpscQueue() {
        return Mpsc.<T>newMpscQueue();
    }
    
    public static <T> Queue<T> newMpscQueue(final int maxCapacity) {
        return Mpsc.<T>newMpscQueue(maxCapacity);
    }
    
    public static <T> Queue<T> newSpscQueue() {
        return (Queue<T>)(hasUnsafe() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue());
    }
    
    public static <T> Queue<T> newFixedMpscQueue(final int capacity) {
        return (Queue<T>)(hasUnsafe() ? new MpscArrayQueue<>(capacity) : new MpscAtomicArrayQueue<>(capacity));
    }
    
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        return PlatformDependent0.getClassLoader(clazz);
    }
    
    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }
    
    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }
    
    public static <C> Deque<C> newConcurrentDeque() {
        if (javaVersion() < 7) {
            return (Deque<C>)new LinkedBlockingDeque();
        }
        return (Deque<C>)new ConcurrentLinkedDeque();
    }
    
    public static Random threadLocalRandom() {
        return PlatformDependent.RANDOM_PROVIDER.current();
    }
    
    private static boolean isWindows0() {
        final boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (windows) {
            PlatformDependent.logger.debug("Platform: Windows");
        }
        return windows;
    }
    
    private static boolean isOsx0() {
        final String osname = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
        final boolean osx = osname.startsWith("macosx") || osname.startsWith("osx");
        if (osx) {
            PlatformDependent.logger.debug("Platform: MacOS");
        }
        return osx;
    }
    
    private static boolean maybeSuperUser0() {
        final String username = SystemPropertyUtil.get("user.name");
        if (isWindows()) {
            return "Administrator".equals(username);
        }
        return "root".equals(username) || "toor".equals(username);
    }
    
    private static Throwable unsafeUnavailabilityCause0() {
        if (isAndroid()) {
            PlatformDependent.logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return (Throwable)new UnsupportedOperationException("sun.misc.Unsafe: unavailable (Android)");
        }
        final Throwable cause = PlatformDependent0.getUnsafeUnavailabilityCause();
        if (cause != null) {
            return cause;
        }
        try {
            final boolean hasUnsafe = PlatformDependent0.hasUnsafe();
            PlatformDependent.logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
            return hasUnsafe ? null : PlatformDependent0.getUnsafeUnavailabilityCause();
        }
        catch (Throwable t) {
            PlatformDependent.logger.trace("Could not determine if Unsafe is available", t);
            return (Throwable)new UnsupportedOperationException("Could not determine if Unsafe is available", t);
        }
    }
    
    private static long maxDirectMemory0() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: lstore_0        /* maxDirectMemory */
        //     2: aconst_null    
        //     3: astore_2        /* systemClassLoader */
        //     4: invokestatic    io/netty/util/internal/PlatformDependent.getSystemClassLoader:()Ljava/lang/ClassLoader;
        //     7: astore_2        /* systemClassLoader */
        //     8: ldc_w           "os.name"
        //    11: ldc_w           ""
        //    14: invokestatic    io/netty/util/internal/SystemPropertyUtil.get:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //    17: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //    20: ldc_w           "z/os"
        //    23: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //    26: ifne            68
        //    29: ldc_w           "sun.misc.VM"
        //    32: iconst_1       
        //    33: aload_2         /* systemClassLoader */
        //    34: invokestatic    java/lang/Class.forName:(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
        //    37: astore_3        /* vmClass */
        //    38: aload_3         /* vmClass */
        //    39: ldc_w           "maxDirectMemory"
        //    42: iconst_0       
        //    43: anewarray       Ljava/lang/Class;
        //    46: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //    49: astore          m
        //    51: aload           m
        //    53: aconst_null    
        //    54: iconst_0       
        //    55: anewarray       Ljava/lang/Object;
        //    58: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //    61: checkcast       Ljava/lang/Number;
        //    64: invokevirtual   java/lang/Number.longValue:()J
        //    67: lstore_0        /* maxDirectMemory */
        //    68: goto            72
        //    71: astore_3       
        //    72: lload_0         /* maxDirectMemory */
        //    73: lconst_0       
        //    74: lcmp           
        //    75: ifle            80
        //    78: lload_0         /* maxDirectMemory */
        //    79: lreturn        
        //    80: ldc_w           "java.lang.management.ManagementFactory"
        //    83: iconst_1       
        //    84: aload_2         /* systemClassLoader */
        //    85: invokestatic    java/lang/Class.forName:(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
        //    88: astore_3        /* mgmtFactoryClass */
        //    89: ldc_w           "java.lang.management.RuntimeMXBean"
        //    92: iconst_1       
        //    93: aload_2         /* systemClassLoader */
        //    94: invokestatic    java/lang/Class.forName:(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
        //    97: astore          runtimeClass
        //    99: aload_3         /* mgmtFactoryClass */
        //   100: ldc_w           "getRuntimeMXBean"
        //   103: iconst_0       
        //   104: anewarray       Ljava/lang/Class;
        //   107: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   110: aconst_null    
        //   111: iconst_0       
        //   112: anewarray       Ljava/lang/Object;
        //   115: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   118: astore          runtime
        //   120: aload           runtimeClass
        //   122: ldc_w           "getInputArguments"
        //   125: iconst_0       
        //   126: anewarray       Ljava/lang/Class;
        //   129: invokevirtual   java/lang/Class.getDeclaredMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //   132: aload           runtime
        //   134: iconst_0       
        //   135: anewarray       Ljava/lang/Object;
        //   138: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //   141: checkcast       Ljava/util/List;
        //   144: astore          vmArgs
        //   146: aload           vmArgs
        //   148: invokeinterface java/util/List.size:()I
        //   153: iconst_1       
        //   154: isub           
        //   155: istore          i
        //   157: iload           i
        //   159: iflt            305
        //   162: getstatic       io/netty/util/internal/PlatformDependent.MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN:Ljava/util/regex/Pattern;
        //   165: aload           vmArgs
        //   167: iload           i
        //   169: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
        //   174: checkcast       Ljava/lang/CharSequence;
        //   177: invokevirtual   java/util/regex/Pattern.matcher:(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
        //   180: astore          m
        //   182: aload           m
        //   184: invokevirtual   java/util/regex/Matcher.matches:()Z
        //   187: ifne            193
        //   190: goto            299
        //   193: aload           m
        //   195: iconst_1       
        //   196: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   199: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   202: lstore_0        /* maxDirectMemory */
        //   203: aload           m
        //   205: iconst_2       
        //   206: invokevirtual   java/util/regex/Matcher.group:(I)Ljava/lang/String;
        //   209: iconst_0       
        //   210: invokevirtual   java/lang/String.charAt:(I)C
        //   213: lookupswitch {
        //               71: 290
        //               75: 272
        //               77: 281
        //              103: 290
        //              107: 272
        //              109: 281
        //          default: 296
        //        }
        //   272: lload_0         /* maxDirectMemory */
        //   273: ldc2_w          1024
        //   276: lmul           
        //   277: lstore_0        /* maxDirectMemory */
        //   278: goto            296
        //   281: lload_0         /* maxDirectMemory */
        //   282: ldc2_w          1048576
        //   285: lmul           
        //   286: lstore_0        /* maxDirectMemory */
        //   287: goto            296
        //   290: lload_0         /* maxDirectMemory */
        //   291: ldc2_w          1073741824
        //   294: lmul           
        //   295: lstore_0        /* maxDirectMemory */
        //   296: goto            305
        //   299: iinc            i, -1
        //   302: goto            157
        //   305: goto            309
        //   308: astore_3       
        //   309: lload_0         /* maxDirectMemory */
        //   310: lconst_0       
        //   311: lcmp           
        //   312: ifgt            340
        //   315: invokestatic    java/lang/Runtime.getRuntime:()Ljava/lang/Runtime;
        //   318: invokevirtual   java/lang/Runtime.maxMemory:()J
        //   321: lstore_0        /* maxDirectMemory */
        //   322: getstatic       io/netty/util/internal/PlatformDependent.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   325: ldc_w           "maxDirectMemory: {} bytes (maybe)"
        //   328: lload_0         /* maxDirectMemory */
        //   329: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   332: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;Ljava/lang/Object;)V
        //   337: goto            355
        //   340: getstatic       io/netty/util/internal/PlatformDependent.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   343: ldc_w           "maxDirectMemory: {} bytes"
        //   346: lload_0         /* maxDirectMemory */
        //   347: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   350: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;Ljava/lang/Object;)V
        //   355: lload_0         /* maxDirectMemory */
        //   356: lreturn        
        //    StackMapTable: 00 10 FD 00 44 04 07 02 8D 42 07 00 93 00 07 FF 00 4C 00 07 04 07 02 8D 07 02 77 07 02 77 07 00 04 07 02 97 01 00 00 FC 00 23 07 02 A6 FB 00 4E 08 08 05 FA 00 02 FF 00 05 00 02 04 07 02 8D 00 00 42 07 00 93 00 1E 0E
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  4      68     71     72     Ljava/lang/Throwable;
        //  80     305    308    309    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:248)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:273)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static File tmpdir0() {
        try {
            File f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (f != null) {
                PlatformDependent.logger.debug("-Dio.netty.tmpdir: {}", f);
                return f;
            }
            f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (f != null) {
                PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
                return f;
            }
            if (isWindows()) {
                f = toDirectory(System.getenv("TEMP"));
                if (f != null) {
                    PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
                    return f;
                }
                final String userprofile = System.getenv("USERPROFILE");
                if (userprofile != null) {
                    f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                    if (f != null) {
                        PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
                        return f;
                    }
                    f = toDirectory(userprofile + "\\Local Settings\\Temp");
                    if (f != null) {
                        PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
                        return f;
                    }
                }
            }
            else {
                f = toDirectory(System.getenv("TMPDIR"));
                if (f != null) {
                    PlatformDependent.logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
                    return f;
                }
            }
        }
        catch (Throwable t) {}
        File f;
        if (isWindows()) {
            f = new File("C:\\Windows\\Temp");
        }
        else {
            f = new File("/tmp");
        }
        PlatformDependent.logger.warn("Failed to get the temporary directory; falling back to: {}", f);
        return f;
    }
    
    private static File toDirectory(final String path) {
        if (path == null) {
            return null;
        }
        final File f = new File(path);
        f.mkdirs();
        if (!f.isDirectory()) {
            return null;
        }
        try {
            return f.getAbsoluteFile();
        }
        catch (Exception ignored) {
            return f;
        }
    }
    
    private static int bitMode0() {
        int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {}", bitMode);
            return bitMode;
        }
        bitMode = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", bitMode);
            return bitMode;
        }
        bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", bitMode);
            return bitMode;
        }
        final String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
        if ("amd64".equals(arch) || "x86_64".equals(arch)) {
            bitMode = 64;
        }
        else if ("i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
            bitMode = 32;
        }
        if (bitMode > 0) {
            PlatformDependent.logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", bitMode, arch);
        }
        final String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
        final Pattern BIT_PATTERN = Pattern.compile("([1-9][0-9]+)-?bit");
        final Matcher m = BIT_PATTERN.matcher((CharSequence)vm);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return 64;
    }
    
    private static int addressSize0() {
        if (!hasUnsafe()) {
            return -1;
        }
        return PlatformDependent0.addressSize();
    }
    
    private static long byteArrayBaseOffset0() {
        if (!hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.byteArrayBaseOffset();
    }
    
    private static boolean equalsSafe(final byte[] bytes1, int startPos1, final byte[] bytes2, int startPos2, final int length) {
        for (int end = startPos1 + length; startPos1 < end; ++startPos1, ++startPos2) {
            if (bytes1[startPos1] != bytes2[startPos2]) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean isZeroSafe(final byte[] bytes, int startPos, final int length) {
        for (int end = startPos + length; startPos < end; ++startPos) {
            if (bytes[startPos] != 0) {
                return false;
            }
        }
        return true;
    }
    
    static int hashCodeAsciiSafe(final byte[] bytes, final int startPos, final int length) {
        int hash = -1028477387;
        final int remainingBytes = length & 0x7;
        for (int end = startPos + remainingBytes, i = startPos - 8 + length; i >= end; i -= 8) {
            hash = PlatformDependent0.hashCodeAsciiCompute(getLongSafe(bytes, i), hash);
        }
        switch (remainingBytes) {
            case 7: {
                return ((hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1))) * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 3));
            }
            case 6: {
                return (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos))) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 2));
            }
            case 5: {
                return (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 1));
            }
            case 4: {
                return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos));
            }
            case 3: {
                return (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1));
            }
            case 2: {
                return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos));
            }
            case 1: {
                return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos]);
            }
            default: {
                return hash;
            }
        }
    }
    
    public static String normalizedArch() {
        return PlatformDependent.NORMALIZED_ARCH;
    }
    
    public static String normalizedOs() {
        return PlatformDependent.NORMALIZED_OS;
    }
    
    private static String normalize(final String value) {
        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }
    
    private static String normalizeArch(String value) {
        value = normalize(value);
        if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return "x86_64";
        }
        if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return "x86_32";
        }
        if (value.matches("^(ia64|itanium64)$")) {
            return "itanium_64";
        }
        if (value.matches("^(sparc|sparc32)$")) {
            return "sparc_32";
        }
        if (value.matches("^(sparcv9|sparc64)$")) {
            return "sparc_64";
        }
        if (value.matches("^(arm|arm32)$")) {
            return "arm_32";
        }
        if ("aarch64".equals(value)) {
            return "aarch_64";
        }
        if (value.matches("^(ppc|ppc32)$")) {
            return "ppc_32";
        }
        if ("ppc64".equals(value)) {
            return "ppc_64";
        }
        if ("ppc64le".equals(value)) {
            return "ppcle_64";
        }
        if ("s390".equals(value)) {
            return "s390_32";
        }
        if ("s390x".equals(value)) {
            return "s390_64";
        }
        return "unknown";
    }
    
    private static String normalizeOs(String value) {
        value = normalize(value);
        if (value.startsWith("aix")) {
            return "aix";
        }
        if (value.startsWith("hpux")) {
            return "hpux";
        }
        if (value.startsWith("os400") && (value.length() <= 5 || !Character.isDigit(value.charAt(5)))) {
            return "os400";
        }
        if (value.startsWith("linux")) {
            return "linux";
        }
        if (value.startsWith("macosx") || value.startsWith("osx")) {
            return "osx";
        }
        if (value.startsWith("freebsd")) {
            return "freebsd";
        }
        if (value.startsWith("openbsd")) {
            return "openbsd";
        }
        if (value.startsWith("netbsd")) {
            return "netbsd";
        }
        if (value.startsWith("solaris") || value.startsWith("sunos")) {
            return "sunos";
        }
        if (value.startsWith("windows")) {
            return "windows";
        }
        return "unknown";
    }
    
    private PlatformDependent() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
        MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
        IS_WINDOWS = isWindows0();
        IS_OSX = isOsx0();
        CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
        UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause0();
        DIRECT_BUFFER_PREFERRED = (PlatformDependent.UNSAFE_UNAVAILABILITY_CAUSE == null && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
        MAX_DIRECT_MEMORY = maxDirectMemory0();
        BYTE_ARRAY_BASE_OFFSET = byteArrayBaseOffset0();
        TMPDIR = tmpdir0();
        BIT_MODE = bitMode0();
        NORMALIZED_ARCH = normalizeArch(SystemPropertyUtil.get("os.arch", ""));
        NORMALIZED_OS = normalizeOs(SystemPropertyUtil.get("os.name", ""));
        ADDRESS_SIZE = addressSize0();
        BIG_ENDIAN_NATIVE_ORDER = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
        NOOP = new Cleaner() {
            public void freeDirectBuffer(final ByteBuffer buffer) {
            }
        };
        if (javaVersion() >= 7) {
            RANDOM_PROVIDER = new ThreadLocalRandomProvider() {
                public Random current() {
                    return (Random)ThreadLocalRandom.current();
                }
            };
        }
        else {
            RANDOM_PROVIDER = new ThreadLocalRandomProvider() {
                public Random current() {
                    return io.netty.util.internal.ThreadLocalRandom.current();
                }
            };
        }
        if (PlatformDependent.logger.isDebugEnabled()) {
            PlatformDependent.logger.debug("-Dio.netty.noPreferDirect: {}", !PlatformDependent.DIRECT_BUFFER_PREFERRED);
        }
        if (!hasUnsafe() && !isAndroid() && !PlatformDependent0.isExplicitNoUnsafe()) {
            PlatformDependent.logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system instability.");
        }
        long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1L);
        if (maxDirectMemory == 0L || !hasUnsafe() || !PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            USE_DIRECT_BUFFER_NO_CLEANER = false;
            DIRECT_MEMORY_COUNTER = null;
        }
        else {
            USE_DIRECT_BUFFER_NO_CLEANER = true;
            if (maxDirectMemory < 0L) {
                maxDirectMemory = maxDirectMemory0();
                if (maxDirectMemory <= 0L) {
                    DIRECT_MEMORY_COUNTER = null;
                }
                else {
                    DIRECT_MEMORY_COUNTER = new AtomicLong();
                }
            }
            else {
                DIRECT_MEMORY_COUNTER = new AtomicLong();
            }
        }
        DIRECT_MEMORY_LIMIT = maxDirectMemory;
        PlatformDependent.logger.debug("-Dio.netty.maxDirectMemory: {} bytes", maxDirectMemory);
        final int tryAllocateUninitializedArray = SystemPropertyUtil.getInt("io.netty.uninitializedArrayAllocationThreshold", 1024);
        UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD = ((javaVersion() >= 9 && PlatformDependent0.hasAllocateArrayMethod()) ? tryAllocateUninitializedArray : -1);
        PlatformDependent.logger.debug("-Dio.netty.uninitializedArrayAllocationThreshold: {}", PlatformDependent.UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD);
        MAYBE_SUPER_USER = maybeSuperUser0();
        if (!isAndroid() && hasUnsafe()) {
            if (javaVersion() >= 9) {
                CLEANER = (CleanerJava9.isSupported() ? new CleanerJava9() : PlatformDependent.NOOP);
            }
            else {
                CLEANER = (CleanerJava6.isSupported() ? new CleanerJava6() : PlatformDependent.NOOP);
            }
        }
        else {
            CLEANER = PlatformDependent.NOOP;
        }
    }
    
    private static final class Mpsc {
        private static final boolean USE_MPSC_CHUNKED_ARRAY_QUEUE;
        
        static <T> Queue<T> newMpscQueue(final int maxCapacity) {
            final int capacity = Math.max(Math.min(maxCapacity, 1073741824), 2048);
            return (Queue<T>)(Mpsc.USE_MPSC_CHUNKED_ARRAY_QUEUE ? new MpscChunkedArrayQueue<>(1024, capacity) : new MpscGrowableAtomicArrayQueue<>(1024, capacity));
        }
        
        static <T> Queue<T> newMpscQueue() {
            return (Queue<T>)(Mpsc.USE_MPSC_CHUNKED_ARRAY_QUEUE ? new MpscUnboundedArrayQueue<>(1024) : new MpscUnboundedAtomicArrayQueue<>(1024));
        }
        
        static {
            Object unsafe = null;
            if (PlatformDependent.hasUnsafe()) {
                unsafe = AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Object>() {
                    public Object run() {
                        return UnsafeAccess.UNSAFE;
                    }
                });
            }
            if (unsafe == null) {
                PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: unavailable");
                USE_MPSC_CHUNKED_ARRAY_QUEUE = false;
            }
            else {
                PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: available");
                USE_MPSC_CHUNKED_ARRAY_QUEUE = true;
            }
        }
    }
    
    private static final class AtomicLongCounter extends AtomicLong implements LongCounter {
        private static final long serialVersionUID = 4074772784610639305L;
        
        public void add(final long delta) {
            this.addAndGet(delta);
        }
        
        public void increment() {
            this.incrementAndGet();
        }
        
        public void decrement() {
            this.decrementAndGet();
        }
        
        public long value() {
            return this.get();
        }
    }
    
    private interface ThreadLocalRandomProvider {
        Random current();
    }
}
