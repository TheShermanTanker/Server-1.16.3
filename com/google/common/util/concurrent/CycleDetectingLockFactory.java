package com.google.common.util.concurrent;

import com.google.j2objc.annotations.Weak;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import com.google.common.collect.MapMaker;
import com.google.common.annotations.VisibleForTesting;
import java.util.EnumMap;
import java.util.Collections;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.concurrent.ThreadSafe;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;

@Beta
@CanIgnoreReturnValue
@ThreadSafe
@GwtIncompatible
public class CycleDetectingLockFactory {
    private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType;
    private static final Logger logger;
    final Policy policy;
    private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks;
    
    public static CycleDetectingLockFactory newInstance(final Policy policy) {
        return new CycleDetectingLockFactory(policy);
    }
    
    public ReentrantLock newReentrantLock(final String lockName) {
        return this.newReentrantLock(lockName, false);
    }
    
    public ReentrantLock newReentrantLock(final String lockName, final boolean fair) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/google/common/util/concurrent/CycleDetectingLockFactory.policy:Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policy;
        //     4: getstatic       com/google/common/util/concurrent/CycleDetectingLockFactory$Policies.DISABLED:Lcom/google/common/util/concurrent/CycleDetectingLockFactory$Policies;
        //     7: if_acmpne       21
        //    10: new             Ljava/util/concurrent/locks/ReentrantLock;
        //    13: dup            
        //    14: iload_2         /* fair */
        //    15: invokespecial   java/util/concurrent/locks/ReentrantLock.<init>:(Z)V
        //    18: goto            39
        //    21: new             Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock;
        //    24: dup            
        //    25: aload_0         /* this */
        //    26: new             Lcom/google/common/util/concurrent/CycleDetectingLockFactory$LockGraphNode;
        //    29: dup            
        //    30: aload_1         /* lockName */
        //    31: invokespecial   com/google/common/util/concurrent/CycleDetectingLockFactory$LockGraphNode.<init>:(Ljava/lang/String;)V
        //    34: iload_2         /* fair */
        //    35: aconst_null    
        //    36: invokespecial   com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock.<init>:(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$LockGraphNode;ZLcom/google/common/util/concurrent/CycleDetectingLockFactory$1;)V
        //    39: areturn        
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  lockName  
        //  fair      
        //    StackMapTable: 00 02 15 51 07 00 4E
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformCall(AstMethodBodyBuilder.java:1131)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:1009)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public ReentrantReadWriteLock newReentrantReadWriteLock(final String lockName) {
        return this.newReentrantReadWriteLock(lockName, false);
    }
    
    public ReentrantReadWriteLock newReentrantReadWriteLock(final String lockName, final boolean fair) {
        return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
    }
    
    public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(final Class<E> enumClass, final Policy policy) {
        Preconditions.<Class<E>>checkNotNull(enumClass);
        Preconditions.<Policy>checkNotNull(policy);
        final Map<E, LockGraphNode> lockGraphNodes = (Map<E, LockGraphNode>)getOrCreateNodes(enumClass);
        return new WithExplicitOrdering<E>(policy, lockGraphNodes);
    }
    
    private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(final Class<? extends Enum> clazz) {
        Map<? extends Enum, LockGraphNode> existing = CycleDetectingLockFactory.lockGraphNodesPerType.get(clazz);
        if (existing != null) {
            return existing;
        }
        final Map<? extends Enum, LockGraphNode> created = CycleDetectingLockFactory.createNodes(clazz);
        existing = CycleDetectingLockFactory.lockGraphNodesPerType.putIfAbsent(clazz, created);
        return MoreObjects.<Map<? extends Enum, LockGraphNode>>firstNonNull(existing, created);
    }
    
    @VisibleForTesting
    static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(final Class<E> clazz) {
        final EnumMap<E, LockGraphNode> map = Maps.<E, LockGraphNode>newEnumMap(clazz);
        final E[] keys = (E[])clazz.getEnumConstants();
        final int numKeys = keys.length;
        final ArrayList<LockGraphNode> nodes = Lists.<LockGraphNode>newArrayListWithCapacity(numKeys);
        for (final E key : keys) {
            final LockGraphNode node = new LockGraphNode(getLockName(key));
            nodes.add(node);
            map.put((Enum)key, node);
        }
        for (int i = 1; i < numKeys; ++i) {
            ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, (List<LockGraphNode>)nodes.subList(0, i));
        }
        for (int i = 0; i < numKeys - 1; ++i) {
            ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, (List<LockGraphNode>)nodes.subList(i + 1, numKeys));
        }
        return (Map<E, LockGraphNode>)Collections.unmodifiableMap((Map)map);
    }
    
    private static String getLockName(final Enum<?> rank) {
        return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
    }
    
    private CycleDetectingLockFactory(final Policy policy) {
        this.policy = Preconditions.<Policy>checkNotNull(policy);
    }
    
    private void aboutToAcquire(final CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            final ArrayList<LockGraphNode> acquiredLockList = (ArrayList<LockGraphNode>)CycleDetectingLockFactory.acquiredLocks.get();
            final LockGraphNode node = lock.getLockGraphNode();
            node.checkAcquiredLocks(this.policy, (List<LockGraphNode>)acquiredLockList);
            acquiredLockList.add(node);
        }
    }
    
    private static void lockStateChanged(final CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            final ArrayList<LockGraphNode> acquiredLockList = (ArrayList<LockGraphNode>)CycleDetectingLockFactory.acquiredLocks.get();
            final LockGraphNode node = lock.getLockGraphNode();
            for (int i = acquiredLockList.size() - 1; i >= 0; --i) {
                if (acquiredLockList.get(i) == node) {
                    acquiredLockList.remove(i);
                    break;
                }
            }
        }
    }
    
    static {
        lockGraphNodesPerType = new MapMaker().weakKeys().<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>>makeMap();
        logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
        acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>() {
            protected ArrayList<LockGraphNode> initialValue() {
                return Lists.<LockGraphNode>newArrayListWithCapacity(3);
            }
        };
    }
    
    @Beta
    public enum Policies implements Policy {
        THROW {
            public void handlePotentialDeadlock(final PotentialDeadlockException e) {
                throw e;
            }
        }, 
        WARN {
            public void handlePotentialDeadlock(final PotentialDeadlockException e) {
                CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", (Throwable)e);
            }
        }, 
        DISABLED {
            public void handlePotentialDeadlock(final PotentialDeadlockException e) {
            }
        };
    }
    
    @Beta
    public static final class WithExplicitOrdering<E extends Enum<E>> extends CycleDetectingLockFactory {
        private final Map<E, LockGraphNode> lockGraphNodes;
        
        @VisibleForTesting
        WithExplicitOrdering(final Policy policy, final Map<E, LockGraphNode> lockGraphNodes) {
            super(policy, null);
            this.lockGraphNodes = lockGraphNodes;
        }
        
        public ReentrantLock newReentrantLock(final E rank) {
            return this.newReentrantLock(rank, false);
        }
        
        public ReentrantLock newReentrantLock(final E rank, final boolean fair) {
            return (this.policy == Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingReentrantLock((LockGraphNode)this.lockGraphNodes.get(rank), fair);
        }
        
        public ReentrantReadWriteLock newReentrantReadWriteLock(final E rank) {
            return this.newReentrantReadWriteLock(rank, false);
        }
        
        public ReentrantReadWriteLock newReentrantReadWriteLock(final E rank, final boolean fair) {
            return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock((LockGraphNode)this.lockGraphNodes.get(rank), fair);
        }
    }
    
    private static class ExampleStackTrace extends IllegalStateException {
        static final StackTraceElement[] EMPTY_STACK_TRACE;
        static final Set<String> EXCLUDED_CLASS_NAMES;
        
        ExampleStackTrace(final LockGraphNode node1, final LockGraphNode node2) {
            super(node1.getLockName() + " -> " + node2.getLockName());
            final StackTraceElement[] origStackTrace = this.getStackTrace();
            for (int i = 0, n = origStackTrace.length; i < n; ++i) {
                if (WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
                    this.setStackTrace(ExampleStackTrace.EMPTY_STACK_TRACE);
                    break;
                }
                if (!ExampleStackTrace.EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
                    this.setStackTrace((StackTraceElement[])Arrays.copyOfRange((Object[])origStackTrace, i, n));
                    break;
                }
            }
        }
        
        static {
            EMPTY_STACK_TRACE = new StackTraceElement[0];
            EXCLUDED_CLASS_NAMES = (Set)ImmutableSet.<String>of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), LockGraphNode.class.getName());
        }
    }
    
    @Beta
    public static final class PotentialDeadlockException extends ExampleStackTrace {
        private final ExampleStackTrace conflictingStackTrace;
        
        private PotentialDeadlockException(final LockGraphNode node1, final LockGraphNode node2, final ExampleStackTrace conflictingStackTrace) {
            super(node1, node2);
            this.initCause((Throwable)(this.conflictingStackTrace = conflictingStackTrace));
        }
        
        public ExampleStackTrace getConflictingStackTrace() {
            return this.conflictingStackTrace;
        }
        
        public String getMessage() {
            final StringBuilder message = new StringBuilder(super.getMessage());
            for (Throwable t = (Throwable)this.conflictingStackTrace; t != null; t = t.getCause()) {
                message.append(", ").append(t.getMessage());
            }
            return message.toString();
        }
    }
    
    private static class LockGraphNode {
        final Map<LockGraphNode, ExampleStackTrace> allowedPriorLocks;
        final Map<LockGraphNode, PotentialDeadlockException> disallowedPriorLocks;
        final String lockName;
        
        LockGraphNode(final String lockName) {
            this.allowedPriorLocks = new MapMaker().weakKeys().makeMap();
            this.disallowedPriorLocks = new MapMaker().weakKeys().makeMap();
            this.lockName = Preconditions.<String>checkNotNull(lockName);
        }
        
        String getLockName() {
            return this.lockName;
        }
        
        void checkAcquiredLocks(final Policy policy, final List<LockGraphNode> acquiredLocks) {
            for (int i = 0, size = acquiredLocks.size(); i < size; ++i) {
                this.checkAcquiredLock(policy, (LockGraphNode)acquiredLocks.get(i));
            }
        }
        
        void checkAcquiredLock(final Policy policy, final LockGraphNode acquiredLock) {
            Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank %s", acquiredLock.getLockName());
            if (this.allowedPriorLocks.containsKey(acquiredLock)) {
                return;
            }
            final PotentialDeadlockException previousDeadlockException = (PotentialDeadlockException)this.disallowedPriorLocks.get(acquiredLock);
            if (previousDeadlockException != null) {
                final PotentialDeadlockException exception = new PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
                policy.handlePotentialDeadlock(exception);
                return;
            }
            final Set<LockGraphNode> seen = Sets.<LockGraphNode>newIdentityHashSet();
            final ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
            if (path == null) {
                this.allowedPriorLocks.put(acquiredLock, new ExampleStackTrace(acquiredLock, this));
            }
            else {
                final PotentialDeadlockException exception2 = new PotentialDeadlockException(acquiredLock, this, path);
                this.disallowedPriorLocks.put(acquiredLock, exception2);
                policy.handlePotentialDeadlock(exception2);
            }
        }
        
        @Nullable
        private ExampleStackTrace findPathTo(final LockGraphNode node, final Set<LockGraphNode> seen) {
            if (!seen.add(this)) {
                return null;
            }
            ExampleStackTrace found = (ExampleStackTrace)this.allowedPriorLocks.get(node);
            if (found != null) {
                return found;
            }
            for (final Map.Entry<LockGraphNode, ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
                final LockGraphNode preAcquiredLock = (LockGraphNode)entry.getKey();
                found = preAcquiredLock.findPathTo(node, seen);
                if (found != null) {
                    final ExampleStackTrace path = new ExampleStackTrace(preAcquiredLock, this);
                    path.setStackTrace(((ExampleStackTrace)entry.getValue()).getStackTrace());
                    path.initCause((Throwable)found);
                    return path;
                }
            }
            return null;
        }
    }
    
    final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLock {
        private final LockGraphNode lockGraphNode;
        
        private CycleDetectingReentrantLock(final LockGraphNode lockGraphNode, final boolean fair) {
            super(fair);
            this.lockGraphNode = Preconditions.<LockGraphNode>checkNotNull(lockGraphNode);
        }
        
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }
        
        public boolean isAcquiredByCurrentThread() {
            return this.isHeldByCurrentThread();
        }
        
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lock();
            }
            finally {
                lockStateChanged(this);
            }
        }
        
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lockInterruptibly();
            }
            finally {
                lockStateChanged(this);
            }
        }
        
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                return super.tryLock();
            }
            finally {
                lockStateChanged(this);
            }
        }
        
        public boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                return super.tryLock(timeout, unit);
            }
            finally {
                lockStateChanged(this);
            }
        }
        
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                lockStateChanged(this);
            }
        }
    }
    
    final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLock {
        private final CycleDetectingReentrantReadLock readLock;
        private final CycleDetectingReentrantWriteLock writeLock;
        private final LockGraphNode lockGraphNode;
        
        private CycleDetectingReentrantReadWriteLock(final LockGraphNode lockGraphNode, final boolean fair) {
            super(fair);
            this.readLock = new CycleDetectingReentrantReadLock(this);
            this.writeLock = new CycleDetectingReentrantWriteLock(this);
            this.lockGraphNode = Preconditions.<LockGraphNode>checkNotNull(lockGraphNode);
        }
        
        public ReentrantReadWriteLock.ReadLock readLock() {
            return this.readLock;
        }
        
        public ReentrantReadWriteLock.WriteLock writeLock() {
            return this.writeLock;
        }
        
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }
        
        public boolean isAcquiredByCurrentThread() {
            return this.isWriteLockedByCurrentThread() || this.getReadHoldCount() > 0;
        }
    }
    
    private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock {
        @Weak
        final CycleDetectingReentrantReadWriteLock readWriteLock;
        
        CycleDetectingReentrantReadLock(final CycleDetectingReentrantReadWriteLock readWriteLock) {
            super((ReentrantReadWriteLock)readWriteLock);
            this.readWriteLock = readWriteLock;
        }
        
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock(timeout, unit);
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
    }
    
    private class CycleDetectingReentrantWriteLock extends ReentrantReadWriteLock.WriteLock {
        @Weak
        final CycleDetectingReentrantReadWriteLock readWriteLock;
        
        CycleDetectingReentrantWriteLock(final CycleDetectingReentrantReadWriteLock readWriteLock) {
            super((ReentrantReadWriteLock)readWriteLock);
            this.readWriteLock = readWriteLock;
        }
        
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock(timeout, unit);
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
        
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                lockStateChanged(this.readWriteLock);
            }
        }
    }
    
    private interface CycleDetectingLock {
        LockGraphNode getLockGraphNode();
        
        boolean isAcquiredByCurrentThread();
    }
    
    @Beta
    @ThreadSafe
    public interface Policy {
        void handlePotentialDeadlock(final PotentialDeadlockException potentialDeadlockException);
    }
}
