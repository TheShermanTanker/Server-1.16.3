package io.netty.util;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.lang.ref.WeakReference;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.StringUtil;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import io.netty.util.internal.logging.InternalLogger;

public class ResourceLeakDetector<T> {
    private static final String PROP_LEVEL_OLD = "io.netty.leakDetectionLevel";
    private static final String PROP_LEVEL = "io.netty.leakDetection.level";
    private static final Level DEFAULT_LEVEL;
    private static final String PROP_TARGET_RECORDS = "io.netty.leakDetection.targetRecords";
    private static final int DEFAULT_TARGET_RECORDS = 4;
    private static final int TARGET_RECORDS;
    private static Level level;
    private static final InternalLogger logger;
    static final int DEFAULT_SAMPLING_INTERVAL = 128;
    private final ConcurrentMap<DefaultResourceLeak<?>, LeakEntry> allLeaks;
    private final ReferenceQueue<Object> refQueue;
    private final ConcurrentMap<String, Boolean> reportedLeaks;
    private final String resourceType;
    private final int samplingInterval;
    private static final AtomicReference<String[]> excludedMethods;
    
    @Deprecated
    public static void setEnabled(final boolean enabled) {
        setLevel(enabled ? Level.SIMPLE : Level.DISABLED);
    }
    
    public static boolean isEnabled() {
        return getLevel().ordinal() > Level.DISABLED.ordinal();
    }
    
    public static void setLevel(final Level level) {
        if (level == null) {
            throw new NullPointerException("level");
        }
        ResourceLeakDetector.level = level;
    }
    
    public static Level getLevel() {
        return ResourceLeakDetector.level;
    }
    
    @Deprecated
    public ResourceLeakDetector(final Class<?> resourceType) {
        this(StringUtil.simpleClassName(resourceType));
    }
    
    @Deprecated
    public ResourceLeakDetector(final String resourceType) {
        this(resourceType, 128, Long.MAX_VALUE);
    }
    
    @Deprecated
    public ResourceLeakDetector(final Class<?> resourceType, final int samplingInterval, final long maxActive) {
        this(resourceType, samplingInterval);
    }
    
    public ResourceLeakDetector(final Class<?> resourceType, final int samplingInterval) {
        this(StringUtil.simpleClassName(resourceType), samplingInterval, Long.MAX_VALUE);
    }
    
    @Deprecated
    public ResourceLeakDetector(final String resourceType, final int samplingInterval, final long maxActive) {
        this.allLeaks = PlatformDependent.<DefaultResourceLeak<?>, LeakEntry>newConcurrentHashMap();
        this.refQueue = (ReferenceQueue<Object>)new ReferenceQueue();
        this.reportedLeaks = PlatformDependent.<String, Boolean>newConcurrentHashMap();
        if (resourceType == null) {
            throw new NullPointerException("resourceType");
        }
        this.resourceType = resourceType;
        this.samplingInterval = samplingInterval;
    }
    
    @Deprecated
    public final ResourceLeak open(final T obj) {
        return this.track0(obj);
    }
    
    public final ResourceLeakTracker<T> track(final T obj) {
        return (ResourceLeakTracker<T>)this.track0(obj);
    }
    
    private DefaultResourceLeak track0(final T obj) {
        final Level level = ResourceLeakDetector.level;
        if (level == Level.DISABLED) {
            return null;
        }
        if (level.ordinal() >= Level.PARANOID.ordinal()) {
            this.reportLeak();
            return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks);
        }
        if (PlatformDependent.threadLocalRandom().nextInt(this.samplingInterval) == 0) {
            this.reportLeak();
            return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks);
        }
        return null;
    }
    
    private void clearRefQueue() {
        while (true) {
            final DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
            if (ref == null) {
                break;
            }
            ref.dispose();
        }
    }
    
    private void reportLeak() {
        if (!ResourceLeakDetector.logger.isErrorEnabled()) {
            this.clearRefQueue();
            return;
        }
        while (true) {
            final DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
            if (ref == null) {
                break;
            }
            if (!ref.dispose()) {
                continue;
            }
            final String records = ref.toString();
            if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) != null) {
                continue;
            }
            if (records.isEmpty()) {
                this.reportUntracedLeak(this.resourceType);
            }
            else {
                this.reportTracedLeak(this.resourceType, records);
            }
        }
    }
    
    protected void reportTracedLeak(final String resourceType, final String records) {
        ResourceLeakDetector.logger.error("LEAK: {}.release() was not called before it's garbage-collected. See http://netty.io/wiki/reference-counted-objects.html for more information.{}", resourceType, records);
    }
    
    protected void reportUntracedLeak(final String resourceType) {
        ResourceLeakDetector.logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel() See http://netty.io/wiki/reference-counted-objects.html for more information.", resourceType, "io.netty.leakDetection.level", Level.ADVANCED.name().toLowerCase(), StringUtil.simpleClassName(this));
    }
    
    @Deprecated
    protected void reportInstancesLeak(final String resourceType) {
    }
    
    public static void addExclusions(final Class clz, final String... methodNames) {
        final Set<String> nameSet = (Set<String>)new HashSet((Collection)Arrays.asList((Object[])methodNames));
        for (final Method method : clz.getDeclaredMethods()) {
            if (nameSet.remove(method.getName()) && nameSet.isEmpty()) {
                break;
            }
        }
        if (!nameSet.isEmpty()) {
            throw new IllegalArgumentException(new StringBuilder().append("Can't find '").append(nameSet).append("' in ").append(clz.getName()).toString());
        }
        String[] oldMethods;
        String[] newMethods;
        do {
            oldMethods = (String[])ResourceLeakDetector.excludedMethods.get();
            newMethods = (String[])Arrays.copyOf((Object[])oldMethods, oldMethods.length + 2 * methodNames.length);
            for (int i = 0; i < methodNames.length; ++i) {
                newMethods[oldMethods.length + i * 2] = clz.getName();
                newMethods[oldMethods.length + i * 2 + 1] = methodNames[i];
            }
        } while (!ResourceLeakDetector.excludedMethods.compareAndSet(oldMethods, newMethods));
    }
    
    static {
        DEFAULT_LEVEL = Level.SIMPLE;
        logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
        boolean disabled;
        if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
            disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
            ResourceLeakDetector.logger.debug("-Dio.netty.noResourceLeakDetection: {}", disabled);
            ResourceLeakDetector.logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetection.level", ResourceLeakDetector.DEFAULT_LEVEL.name().toLowerCase());
        }
        else {
            disabled = false;
        }
        final Level defaultLevel = disabled ? Level.DISABLED : ResourceLeakDetector.DEFAULT_LEVEL;
        String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", defaultLevel.name());
        levelStr = SystemPropertyUtil.get("io.netty.leakDetection.level", levelStr);
        final Level level = Level.parseLevel(levelStr);
        TARGET_RECORDS = SystemPropertyUtil.getInt("io.netty.leakDetection.targetRecords", 4);
        ResourceLeakDetector.level = level;
        if (ResourceLeakDetector.logger.isDebugEnabled()) {
            ResourceLeakDetector.logger.debug("-D{}: {}", "io.netty.leakDetection.level", level.name().toLowerCase());
            ResourceLeakDetector.logger.debug("-D{}: {}", "io.netty.leakDetection.targetRecords", ResourceLeakDetector.TARGET_RECORDS);
        }
        excludedMethods = new AtomicReference(EmptyArrays.EMPTY_STRINGS);
    }
    
    public enum Level {
        DISABLED, 
        SIMPLE, 
        ADVANCED, 
        PARANOID;
        
        static Level parseLevel(final String levelStr) {
            final String trimmedLevelStr = levelStr.trim();
            for (final Level l : values()) {
                if (trimmedLevelStr.equalsIgnoreCase(l.name()) || trimmedLevelStr.equals(String.valueOf(l.ordinal()))) {
                    return l;
                }
            }
            return ResourceLeakDetector.DEFAULT_LEVEL;
        }
    }
    
    private static final class DefaultResourceLeak<T> extends WeakReference<Object> implements ResourceLeakTracker<T>, ResourceLeak {
        private static final AtomicReferenceFieldUpdater<DefaultResourceLeak<?>, Record> headUpdater;
        private static final AtomicIntegerFieldUpdater<DefaultResourceLeak<?>> droppedRecordsUpdater;
        private volatile Record head;
        private volatile int droppedRecords;
        private final ConcurrentMap<DefaultResourceLeak<?>, LeakEntry> allLeaks;
        private final int trackedHash;
        
        DefaultResourceLeak(final Object referent, final ReferenceQueue<Object> refQueue, final ConcurrentMap<DefaultResourceLeak<?>, LeakEntry> allLeaks) {
            super(referent, (ReferenceQueue)refQueue);
            assert referent != null;
            this.trackedHash = System.identityHashCode(referent);
            allLeaks.put(this, LeakEntry.INSTANCE);
            DefaultResourceLeak.headUpdater.set(this, new Record(Record.BOTTOM));
            this.allLeaks = allLeaks;
        }
        
        public void record() {
            this.record0(null);
        }
        
        public void record(final Object hint) {
            this.record0(hint);
        }
        
        private void record0(final Object hint) {
            if (ResourceLeakDetector.TARGET_RECORDS > 0) {
                Record oldHead;
                Record prevHead;
                while ((prevHead = (oldHead = (Record)DefaultResourceLeak.headUpdater.get(this))) != null) {
                    final int numElements = oldHead.pos + 1;
                    boolean dropped;
                    if (numElements >= ResourceLeakDetector.TARGET_RECORDS) {
                        final int backOffFactor = Math.min(numElements - ResourceLeakDetector.TARGET_RECORDS, 30);
                        if (dropped = (PlatformDependent.threadLocalRandom().nextInt(1 << backOffFactor) != 0)) {
                            prevHead = oldHead.next;
                        }
                    }
                    else {
                        dropped = false;
                    }
                    final Record newHead = (hint != null) ? new Record(prevHead, hint) : new Record(prevHead);
                    if (DefaultResourceLeak.headUpdater.compareAndSet(this, oldHead, newHead)) {
                        if (dropped) {
                            DefaultResourceLeak.droppedRecordsUpdater.incrementAndGet(this);
                        }
                    }
                }
            }
        }
        
        boolean dispose() {
            this.clear();
            return this.allLeaks.remove(this, LeakEntry.INSTANCE);
        }
        
        public boolean close() {
            if (this.allLeaks.remove(this, LeakEntry.INSTANCE)) {
                this.clear();
                DefaultResourceLeak.headUpdater.set(this, null);
                return true;
            }
            return false;
        }
        
        public boolean close(final T trackedObject) {
            assert this.trackedHash == System.identityHashCode(trackedObject);
            return this.close() && trackedObject != null;
        }
        
        public String toString() {
            Record oldHead = (Record)DefaultResourceLeak.headUpdater.getAndSet(this, null);
            if (oldHead == null) {
                return "";
            }
            final int dropped = DefaultResourceLeak.droppedRecordsUpdater.get(this);
            int duped = 0;
            final int present = oldHead.pos + 1;
            final StringBuilder buf = new StringBuilder(present * 2048).append(StringUtil.NEWLINE);
            buf.append("Recent access records: ").append(StringUtil.NEWLINE);
            int i = 1;
            final Set<String> seen = (Set<String>)new HashSet(present);
            while (oldHead != Record.BOTTOM) {
                final String s = oldHead.toString();
                if (seen.add(s)) {
                    if (oldHead.next == Record.BOTTOM) {
                        buf.append("Created at:").append(StringUtil.NEWLINE).append(s);
                    }
                    else {
                        buf.append('#').append(i++).append(':').append(StringUtil.NEWLINE).append(s);
                    }
                }
                else {
                    ++duped;
                }
                oldHead = oldHead.next;
            }
            if (duped > 0) {
                buf.append(": ").append(dropped).append(" leak records were discarded because they were duplicates").append(StringUtil.NEWLINE);
            }
            if (dropped > 0) {
                buf.append(": ").append(dropped).append(" leak records were discarded because the leak record count is targeted to ").append(ResourceLeakDetector.TARGET_RECORDS).append(". Use system property ").append("io.netty.leakDetection.targetRecords").append(" to increase the limit.").append(StringUtil.NEWLINE);
            }
            buf.setLength(buf.length() - StringUtil.NEWLINE.length());
            return buf.toString();
        }
        
        static {
            headUpdater = AtomicReferenceFieldUpdater.newUpdater((Class)DefaultResourceLeak.class, (Class)Record.class, "head");
            droppedRecordsUpdater = AtomicIntegerFieldUpdater.newUpdater((Class)DefaultResourceLeak.class, "droppedRecords");
        }
    }
    
    private static final class Record extends Throwable {
        private static final long serialVersionUID = 6065153674892850720L;
        private static final Record BOTTOM;
        private final String hintString;
        private final Record next;
        private final int pos;
        
        Record(final Record next, final Object hint) {
            this.hintString = ((hint instanceof ResourceLeakHint) ? ((ResourceLeakHint)hint).toHintString() : hint.toString());
            this.next = next;
            this.pos = next.pos + 1;
        }
        
        Record(final Record next) {
            this.hintString = null;
            this.next = next;
            this.pos = next.pos + 1;
        }
        
        private Record() {
            this.hintString = null;
            this.next = null;
            this.pos = -1;
        }
        
        public String toString() {
            final StringBuilder buf = new StringBuilder(2048);
            if (this.hintString != null) {
                buf.append("\tHint: ").append(this.hintString).append(StringUtil.NEWLINE);
            }
            final StackTraceElement[] array = this.getStackTrace();
            int i = 3;
        Label_0045:
            while (i < array.length) {
                final StackTraceElement element = array[i];
                final String[] exclusions = (String[])ResourceLeakDetector.excludedMethods.get();
                while (true) {
                    for (int k = 0; k < exclusions.length; k += 2) {
                        if (exclusions[k].equals(element.getClassName()) && exclusions[k + 1].equals(element.getMethodName())) {
                            ++i;
                            continue Label_0045;
                        }
                    }
                    buf.append('\t');
                    buf.append(element.toString());
                    buf.append(StringUtil.NEWLINE);
                    continue;
                }
            }
            return buf.toString();
        }
        
        static {
            BOTTOM = new Record();
        }
    }
    
    private static final class LeakEntry {
        static final LeakEntry INSTANCE;
        private static final int HASH;
        
        public int hashCode() {
            return LeakEntry.HASH;
        }
        
        public boolean equals(final Object obj) {
            return obj == this;
        }
        
        static {
            INSTANCE = new LeakEntry();
            HASH = System.identityHashCode(LeakEntry.INSTANCE);
        }
    }
}
