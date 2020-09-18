package io.netty.channel.nio;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import java.util.ArrayList;
import io.netty.channel.EventLoop;
import java.nio.channels.CancelledKeyException;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.util.Set;
import io.netty.channel.EventLoopException;
import java.nio.channels.SelectableChannel;
import java.util.Queue;
import java.lang.reflect.Field;
import java.lang.reflect.AccessibleObject;
import io.netty.util.internal.ReflectionUtil;
import java.security.AccessController;
import io.netty.util.internal.PlatformDependent;
import java.security.PrivilegedAction;
import java.io.IOException;
import io.netty.channel.ChannelException;
import io.netty.channel.EventLoopGroup;
import io.netty.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.Executor;
import io.netty.channel.SelectStrategy;
import java.util.concurrent.atomic.AtomicBoolean;
import java.nio.channels.spi.SelectorProvider;
import java.nio.channels.Selector;
import java.util.concurrent.Callable;
import io.netty.util.IntSupplier;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.channel.SingleThreadEventLoop;

public final class NioEventLoop extends SingleThreadEventLoop {
    private static final InternalLogger logger;
    private static final int CLEANUP_INTERVAL = 256;
    private static final boolean DISABLE_KEYSET_OPTIMIZATION;
    private static final int MIN_PREMATURE_SELECTOR_RETURNS = 3;
    private static final int SELECTOR_AUTO_REBUILD_THRESHOLD;
    private final IntSupplier selectNowSupplier;
    private final Callable<Integer> pendingTasksCallable;
    static final long MAX_SCHEDULED_DAYS = 1095L;
    private Selector selector;
    private Selector unwrappedSelector;
    private SelectedSelectionKeySet selectedKeys;
    private final SelectorProvider provider;
    private final AtomicBoolean wakenUp;
    private final SelectStrategy selectStrategy;
    private volatile int ioRatio;
    private int cancelledKeys;
    private boolean needsToSelectAgain;
    
    NioEventLoop(final NioEventLoopGroup parent, final Executor executor, final SelectorProvider selectorProvider, final SelectStrategy strategy, final RejectedExecutionHandler rejectedExecutionHandler) {
        super(parent, executor, false, NioEventLoop.DEFAULT_MAX_PENDING_TASKS, rejectedExecutionHandler);
        this.selectNowSupplier = new IntSupplier() {
            public int get() throws Exception {
                return NioEventLoop.this.selectNow();
            }
        };
        this.pendingTasksCallable = (Callable<Integer>)new Callable<Integer>() {
            public Integer call() throws Exception {
                return SingleThreadEventLoop.this.pendingTasks();
            }
        };
        this.wakenUp = new AtomicBoolean();
        this.ioRatio = 50;
        if (selectorProvider == null) {
            throw new NullPointerException("selectorProvider");
        }
        if (strategy == null) {
            throw new NullPointerException("selectStrategy");
        }
        this.provider = selectorProvider;
        final SelectorTuple selectorTuple = this.openSelector();
        this.selector = selectorTuple.selector;
        this.unwrappedSelector = selectorTuple.unwrappedSelector;
        this.selectStrategy = strategy;
    }
    
    private SelectorTuple openSelector() {
        Selector unwrappedSelector;
        try {
            unwrappedSelector = (Selector)this.provider.openSelector();
        }
        catch (IOException e) {
            throw new ChannelException("failed to open a new selector", (Throwable)e);
        }
        if (NioEventLoop.DISABLE_KEYSET_OPTIMIZATION) {
            return new SelectorTuple(unwrappedSelector);
        }
        final SelectedSelectionKeySet selectedKeySet = new SelectedSelectionKeySet();
        final Object maybeSelectorImplClass = AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    return Class.forName("sun.nio.ch.SelectorImpl", false, PlatformDependent.getSystemClassLoader());
                }
                catch (Throwable cause) {
                    return cause;
                }
            }
        });
        if (!(maybeSelectorImplClass instanceof Class) || !((Class)maybeSelectorImplClass).isAssignableFrom(unwrappedSelector.getClass())) {
            if (maybeSelectorImplClass instanceof Throwable) {
                final Throwable t = (Throwable)maybeSelectorImplClass;
                NioEventLoop.logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, t);
            }
            return new SelectorTuple(unwrappedSelector);
        }
        final Class<?> selectorImplClass = maybeSelectorImplClass;
        final Object maybeException = AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Object>() {
            public Object run() {
                try {
                    final Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                    final Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
                    Throwable cause = ReflectionUtil.trySetAccessible((AccessibleObject)selectedKeysField, true);
                    if (cause != null) {
                        return cause;
                    }
                    cause = ReflectionUtil.trySetAccessible((AccessibleObject)publicSelectedKeysField, true);
                    if (cause != null) {
                        return cause;
                    }
                    selectedKeysField.set(unwrappedSelector, selectedKeySet);
                    publicSelectedKeysField.set(unwrappedSelector, selectedKeySet);
                    return null;
                }
                catch (NoSuchFieldException e) {
                    return e;
                }
                catch (IllegalAccessException e2) {
                    return e2;
                }
            }
        });
        if (maybeException instanceof Exception) {
            this.selectedKeys = null;
            final Exception e2 = (Exception)maybeException;
            NioEventLoop.logger.trace("failed to instrument a special java.util.Set into: {}", unwrappedSelector, e2);
            return new SelectorTuple(unwrappedSelector);
        }
        this.selectedKeys = selectedKeySet;
        NioEventLoop.logger.trace("instrumented a special java.util.Set into: {}", unwrappedSelector);
        return new SelectorTuple(unwrappedSelector, new SelectedSelectionKeySetSelector(unwrappedSelector, selectedKeySet));
    }
    
    public SelectorProvider selectorProvider() {
        return this.provider;
    }
    
    @Override
    protected Queue<Runnable> newTaskQueue(final int maxPendingTasks) {
        return (maxPendingTasks == Integer.MAX_VALUE) ? PlatformDependent.<Runnable>newMpscQueue() : PlatformDependent.<Runnable>newMpscQueue(maxPendingTasks);
    }
    
    @Override
    public int pendingTasks() {
        if (this.inEventLoop()) {
            return super.pendingTasks();
        }
        return this.<Integer>submit(this.pendingTasksCallable).syncUninterruptibly().getNow();
    }
    
    public void register(final SelectableChannel ch, final int interestOps, final NioTask<?> task) {
        if (ch == null) {
            throw new NullPointerException("ch");
        }
        if (interestOps == 0) {
            throw new IllegalArgumentException("interestOps must be non-zero.");
        }
        if ((interestOps & ~ch.validOps()) != 0x0) {
            throw new IllegalArgumentException(new StringBuilder().append("invalid interestOps: ").append(interestOps).append("(validOps: ").append(ch.validOps()).append(')').toString());
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (this.isShutdown()) {
            throw new IllegalStateException("event loop shut down");
        }
        try {
            ch.register(this.selector, interestOps, task);
        }
        catch (Exception e) {
            throw new EventLoopException("failed to register a channel", (Throwable)e);
        }
    }
    
    public int getIoRatio() {
        return this.ioRatio;
    }
    
    public void setIoRatio(final int ioRatio) {
        if (ioRatio <= 0 || ioRatio > 100) {
            throw new IllegalArgumentException(new StringBuilder().append("ioRatio: ").append(ioRatio).append(" (expected: 0 < ioRatio <= 100)").toString());
        }
        this.ioRatio = ioRatio;
    }
    
    public void rebuildSelector() {
        if (!this.inEventLoop()) {
            this.execute((Runnable)new Runnable() {
                public void run() {
                    NioEventLoop.this.rebuildSelector0();
                }
            });
            return;
        }
        this.rebuildSelector0();
    }
    
    private void rebuildSelector0() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/channel/nio/NioEventLoop.selector:Ljava/nio/channels/Selector;
        //     4: astore_1        /* oldSelector */
        //     5: aload_1         /* oldSelector */
        //     6: ifnonnull       10
        //     9: return         
        //    10: aload_0         /* this */
        //    11: invokespecial   io/netty/channel/nio/NioEventLoop.openSelector:()Lio/netty/channel/nio/NioEventLoop$SelectorTuple;
        //    14: astore_2        /* newSelectorTuple */
        //    15: goto            32
        //    18: astore_3        /* e */
        //    19: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //    22: ldc_w           "Failed to create a new Selector."
        //    25: aload_3         /* e */
        //    26: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    31: return         
        //    32: iconst_0       
        //    33: istore_3        /* nChannels */
        //    34: aload_1         /* oldSelector */
        //    35: invokevirtual   java/nio/channels/Selector.keys:()Ljava/util/Set;
        //    38: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    43: astore          4
        //    45: aload           4
        //    47: invokeinterface java/util/Iterator.hasNext:()Z
        //    52: ifeq            226
        //    55: aload           4
        //    57: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    62: checkcast       Ljava/nio/channels/SelectionKey;
        //    65: astore          key
        //    67: aload           key
        //    69: invokevirtual   java/nio/channels/SelectionKey.attachment:()Ljava/lang/Object;
        //    72: astore          a
        //    74: aload           key
        //    76: invokevirtual   java/nio/channels/SelectionKey.isValid:()Z
        //    79: ifeq            97
        //    82: aload           key
        //    84: invokevirtual   java/nio/channels/SelectionKey.channel:()Ljava/nio/channels/SelectableChannel;
        //    87: aload_2         /* newSelectorTuple */
        //    88: getfield        io/netty/channel/nio/NioEventLoop$SelectorTuple.unwrappedSelector:Ljava/nio/channels/Selector;
        //    91: invokevirtual   java/nio/channels/SelectableChannel.keyFor:(Ljava/nio/channels/Selector;)Ljava/nio/channels/SelectionKey;
        //    94: ifnull          100
        //    97: goto            45
        //   100: aload           key
        //   102: invokevirtual   java/nio/channels/SelectionKey.interestOps:()I
        //   105: istore          interestOps
        //   107: aload           key
        //   109: invokevirtual   java/nio/channels/SelectionKey.cancel:()V
        //   112: aload           key
        //   114: invokevirtual   java/nio/channels/SelectionKey.channel:()Ljava/nio/channels/SelectableChannel;
        //   117: aload_2         /* newSelectorTuple */
        //   118: getfield        io/netty/channel/nio/NioEventLoop$SelectorTuple.unwrappedSelector:Ljava/nio/channels/Selector;
        //   121: iload           interestOps
        //   123: aload           a
        //   125: invokevirtual   java/nio/channels/SelectableChannel.register:(Ljava/nio/channels/Selector;ILjava/lang/Object;)Ljava/nio/channels/SelectionKey;
        //   128: astore          newKey
        //   130: aload           a
        //   132: instanceof      Lio/netty/channel/nio/AbstractNioChannel;
        //   135: ifeq            148
        //   138: aload           a
        //   140: checkcast       Lio/netty/channel/nio/AbstractNioChannel;
        //   143: aload           newKey
        //   145: putfield        io/netty/channel/nio/AbstractNioChannel.selectionKey:Ljava/nio/channels/SelectionKey;
        //   148: iinc            nChannels, 1
        //   151: goto            223
        //   154: astore          e
        //   156: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   159: ldc_w           "Failed to re-register a Channel to the new Selector."
        //   162: aload           e
        //   164: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   169: aload           a
        //   171: instanceof      Lio/netty/channel/nio/AbstractNioChannel;
        //   174: ifeq            207
        //   177: aload           a
        //   179: checkcast       Lio/netty/channel/nio/AbstractNioChannel;
        //   182: astore          ch
        //   184: aload           ch
        //   186: invokevirtual   io/netty/channel/nio/AbstractNioChannel.unsafe:()Lio/netty/channel/nio/AbstractNioChannel$NioUnsafe;
        //   189: aload           ch
        //   191: invokevirtual   io/netty/channel/nio/AbstractNioChannel.unsafe:()Lio/netty/channel/nio/AbstractNioChannel$NioUnsafe;
        //   194: invokeinterface io/netty/channel/nio/AbstractNioChannel$NioUnsafe.voidPromise:()Lio/netty/channel/ChannelPromise;
        //   199: invokeinterface io/netty/channel/nio/AbstractNioChannel$NioUnsafe.close:(Lio/netty/channel/ChannelPromise;)V
        //   204: goto            223
        //   207: aload           a
        //   209: checkcast       Lio/netty/channel/nio/NioTask;
        //   212: astore          task
        //   214: aload           task
        //   216: aload           key
        //   218: aload           e
        //   220: invokestatic    io/netty/channel/nio/NioEventLoop.invokeChannelUnregistered:(Lio/netty/channel/nio/NioTask;Ljava/nio/channels/SelectionKey;Ljava/lang/Throwable;)V
        //   223: goto            45
        //   226: aload_0         /* this */
        //   227: aload_2         /* newSelectorTuple */
        //   228: getfield        io/netty/channel/nio/NioEventLoop$SelectorTuple.selector:Ljava/nio/channels/Selector;
        //   231: putfield        io/netty/channel/nio/NioEventLoop.selector:Ljava/nio/channels/Selector;
        //   234: aload_0         /* this */
        //   235: aload_2         /* newSelectorTuple */
        //   236: getfield        io/netty/channel/nio/NioEventLoop$SelectorTuple.unwrappedSelector:Ljava/nio/channels/Selector;
        //   239: putfield        io/netty/channel/nio/NioEventLoop.unwrappedSelector:Ljava/nio/channels/Selector;
        //   242: aload_1         /* oldSelector */
        //   243: invokevirtual   java/nio/channels/Selector.close:()V
        //   246: goto            275
        //   249: astore          t
        //   251: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   254: invokeinterface io/netty/util/internal/logging/InternalLogger.isWarnEnabled:()Z
        //   259: ifeq            275
        //   262: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   265: ldc_w           "Failed to close the old Selector."
        //   268: aload           t
        //   270: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   275: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   278: new             Ljava/lang/StringBuilder;
        //   281: dup            
        //   282: invokespecial   java/lang/StringBuilder.<init>:()V
        //   285: ldc_w           "Migrated "
        //   288: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   291: iload_3         /* nChannels */
        //   292: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   295: ldc_w           " channel(s) to the new Selector."
        //   298: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   301: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   304: invokeinterface io/netty/util/internal/logging/InternalLogger.info:(Ljava/lang/String;)V
        //   309: return         
        //    StackMapTable: 00 0D FC 00 0A 07 00 8C 47 07 00 B8 FC 00 0D 07 00 07 FD 00 0C 01 07 01 59 FD 00 33 07 01 61 07 00 9F 02 FD 00 2F 01 07 01 61 FF 00 05 00 07 07 00 02 07 00 8C 07 00 07 01 07 01 59 07 01 61 07 00 9F 00 01 07 00 B8 FC 00 34 07 00 B8 F8 00 0F FA 00 02 56 07 00 A9 19
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  10     15     18     32     Ljava/lang/Exception;
        //  74     97     154    223    Ljava/lang/Exception;
        //  100    151    154    223    Ljava/lang/Exception;
        //  242    246    249    275    Ljava/lang/Throwable;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
        //     at com.strobel.core.Comparer.equals(Comparer.java:31)
        //     at com.strobel.assembler.ir.FrameValue.equals(FrameValue.java:72)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:386)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:254)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    protected void run() {
    Label_0000_Outer:
        while (true) {
            while (true) {
                try {
                Label_0077:
                    while (true) {
                        switch (this.selectStrategy.calculateStrategy(this.selectNowSupplier, this.hasTasks())) {
                            case -2: {
                                continue Label_0000_Outer;
                            }
                            case -1: {
                                this.select(this.wakenUp.getAndSet(false));
                                if (this.wakenUp.get()) {
                                    this.selector.wakeup();
                                    break Label_0077;
                                }
                                break Label_0077;
                            }
                            default: {
                                break Label_0077;
                            }
                        }
                    }
                    this.cancelledKeys = 0;
                    this.needsToSelectAgain = false;
                    final int ioRatio = this.ioRatio;
                    if (ioRatio == 100) {
                        try {
                            this.processSelectedKeys();
                        }
                        finally {
                            this.runAllTasks();
                        }
                    }
                    else {
                        final long ioStartTime = System.nanoTime();
                        try {
                            this.processSelectedKeys();
                        }
                        finally {
                            final long ioTime = System.nanoTime() - ioStartTime;
                            this.runAllTasks(ioTime * (100 - ioRatio) / ioRatio);
                        }
                    }
                }
                catch (Throwable t) {
                    handleLoopException(t);
                }
                try {
                    Label_0210: {
                        if (!this.isShuttingDown()) {
                            break Label_0210;
                        }
                        this.closeAll();
                        if (this.confirmShutdown()) {
                            return;
                        }
                        break Label_0210;
                    }
                    continue;
                }
                catch (Throwable t) {
                    handleLoopException(t);
                    continue;
                }
                continue;
            }
        }
    }
    
    private static void handleLoopException(final Throwable t) {
        NioEventLoop.logger.warn("Unexpected exception in the selector loop.", t);
        try {
            Thread.sleep(1000L);
        }
        catch (InterruptedException ex) {}
    }
    
    private void processSelectedKeys() {
        if (this.selectedKeys != null) {
            this.processSelectedKeysOptimized();
        }
        else {
            this.processSelectedKeysPlain((Set<SelectionKey>)this.selector.selectedKeys());
        }
    }
    
    @Override
    protected void cleanup() {
        try {
            this.selector.close();
        }
        catch (IOException e) {
            NioEventLoop.logger.warn("Failed to close a selector.", (Throwable)e);
        }
    }
    
    void cancel(final SelectionKey key) {
        key.cancel();
        ++this.cancelledKeys;
        if (this.cancelledKeys >= 256) {
            this.cancelledKeys = 0;
            this.needsToSelectAgain = true;
        }
    }
    
    @Override
    protected Runnable pollTask() {
        final Runnable task = super.pollTask();
        if (this.needsToSelectAgain) {
            this.selectAgain();
        }
        return task;
    }
    
    private void processSelectedKeysPlain(Set<SelectionKey> selectedKeys) {
        if (selectedKeys.isEmpty()) {
            return;
        }
        Iterator<SelectionKey> i = (Iterator<SelectionKey>)selectedKeys.iterator();
        while (true) {
            final SelectionKey k = (SelectionKey)i.next();
            final Object a = k.attachment();
            i.remove();
            if (a instanceof AbstractNioChannel) {
                this.processSelectedKey(k, (AbstractNioChannel)a);
            }
            else {
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                processSelectedKey(k, task);
            }
            if (!i.hasNext()) {
                break;
            }
            if (!this.needsToSelectAgain) {
                continue;
            }
            this.selectAgain();
            selectedKeys = (Set<SelectionKey>)this.selector.selectedKeys();
            if (selectedKeys.isEmpty()) {
                break;
            }
            i = (Iterator<SelectionKey>)selectedKeys.iterator();
        }
    }
    
    private void processSelectedKeysOptimized() {
        for (int i = 0; i < this.selectedKeys.size; ++i) {
            final SelectionKey k = this.selectedKeys.keys[i];
            this.selectedKeys.keys[i] = null;
            final Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                this.processSelectedKey(k, (AbstractNioChannel)a);
            }
            else {
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                processSelectedKey(k, task);
            }
            if (this.needsToSelectAgain) {
                this.selectedKeys.reset(i + 1);
                this.selectAgain();
                i = -1;
            }
        }
    }
    
    private void processSelectedKey(final SelectionKey k, final AbstractNioChannel ch) {
        final AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
        if (k.isValid()) {
            try {
                final int readyOps = k.readyOps();
                if ((readyOps & 0x8) != 0x0) {
                    int ops = k.interestOps();
                    ops &= 0xFFFFFFF7;
                    k.interestOps(ops);
                    unsafe.finishConnect();
                }
                if ((readyOps & 0x4) != 0x0) {
                    ch.unsafe().forceFlush();
                }
                if ((readyOps & 0x11) != 0x0 || readyOps == 0) {
                    unsafe.read();
                }
            }
            catch (CancelledKeyException ignored) {
                unsafe.close(unsafe.voidPromise());
            }
            return;
        }
        EventLoop eventLoop;
        try {
            eventLoop = ch.eventLoop();
        }
        catch (Throwable ignored2) {
            return;
        }
        if (eventLoop != this || eventLoop == null) {
            return;
        }
        unsafe.close(unsafe.voidPromise());
    }
    
    private static void processSelectedKey(final SelectionKey k, final NioTask<SelectableChannel> task) {
        int state = 0;
        try {
            task.channelReady(k.channel(), k);
            state = 1;
        }
        catch (Exception e) {
            k.cancel();
            invokeChannelUnregistered(task, k, (Throwable)e);
            state = 2;
        }
        finally {
            switch (state) {
                case 0: {
                    k.cancel();
                    invokeChannelUnregistered(task, k, null);
                    break;
                }
                case 1: {
                    if (!k.isValid()) {
                        invokeChannelUnregistered(task, k, null);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void closeAll() {
        this.selectAgain();
        final Set<SelectionKey> keys = (Set<SelectionKey>)this.selector.keys();
        final Collection<AbstractNioChannel> channels = (Collection<AbstractNioChannel>)new ArrayList(keys.size());
        for (final SelectionKey k : keys) {
            final Object a = k.attachment();
            if (a instanceof AbstractNioChannel) {
                channels.add(a);
            }
            else {
                k.cancel();
                final NioTask<SelectableChannel> task = (NioTask<SelectableChannel>)a;
                invokeChannelUnregistered(task, k, null);
            }
        }
        for (final AbstractNioChannel ch : channels) {
            ch.unsafe().close(ch.unsafe().voidPromise());
        }
    }
    
    private static void invokeChannelUnregistered(final NioTask<SelectableChannel> task, final SelectionKey k, final Throwable cause) {
        try {
            task.channelUnregistered(k.channel(), cause);
        }
        catch (Exception e) {
            NioEventLoop.logger.warn("Unexpected exception while running NioTask.channelUnregistered()", (Throwable)e);
        }
    }
    
    @Override
    protected void wakeup(final boolean inEventLoop) {
        if (!inEventLoop && this.wakenUp.compareAndSet(false, true)) {
            this.selector.wakeup();
        }
    }
    
    Selector unwrappedSelector() {
        return this.unwrappedSelector;
    }
    
    int selectNow() throws IOException {
        try {
            return this.selector.selectNow();
        }
        finally {
            if (this.wakenUp.get()) {
                this.selector.wakeup();
            }
        }
    }
    
    private void select(final boolean oldWakenUp) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        io/netty/channel/nio/NioEventLoop.selector:Ljava/nio/channels/Selector;
        //     4: astore_2        /* selector */
        //     5: iconst_0       
        //     6: istore_3        /* selectCnt */
        //     7: invokestatic    java/lang/System.nanoTime:()J
        //    10: lstore          currentTimeNanos
        //    12: lload           currentTimeNanos
        //    14: aload_0         /* this */
        //    15: lload           currentTimeNanos
        //    17: invokevirtual   io/netty/channel/nio/NioEventLoop.delayNanos:(J)J
        //    20: ladd           
        //    21: lstore          selectDeadLineNanos
        //    23: lload           selectDeadLineNanos
        //    25: lload           currentTimeNanos
        //    27: lsub           
        //    28: ldc2_w          500000
        //    31: ladd           
        //    32: ldc2_w          1000000
        //    35: ldiv           
        //    36: lstore          timeoutMillis
        //    38: lload           timeoutMillis
        //    40: lconst_0       
        //    41: lcmp           
        //    42: ifgt            59
        //    45: iload_3         /* selectCnt */
        //    46: ifne            250
        //    49: aload_2         /* selector */
        //    50: invokevirtual   java/nio/channels/Selector.selectNow:()I
        //    53: pop            
        //    54: iconst_1       
        //    55: istore_3        /* selectCnt */
        //    56: goto            250
        //    59: aload_0         /* this */
        //    60: invokevirtual   io/netty/channel/nio/NioEventLoop.hasTasks:()Z
        //    63: ifeq            88
        //    66: aload_0         /* this */
        //    67: getfield        io/netty/channel/nio/NioEventLoop.wakenUp:Ljava/util/concurrent/atomic/AtomicBoolean;
        //    70: iconst_0       
        //    71: iconst_1       
        //    72: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.compareAndSet:(ZZ)Z
        //    75: ifeq            88
        //    78: aload_2         /* selector */
        //    79: invokevirtual   java/nio/channels/Selector.selectNow:()I
        //    82: pop            
        //    83: iconst_1       
        //    84: istore_3        /* selectCnt */
        //    85: goto            250
        //    88: aload_2         /* selector */
        //    89: lload           timeoutMillis
        //    91: invokevirtual   java/nio/channels/Selector.select:(J)I
        //    94: istore          selectedKeys
        //    96: iinc            selectCnt, 1
        //    99: iload           selectedKeys
        //   101: ifne            250
        //   104: iload_1         /* oldWakenUp */
        //   105: ifne            250
        //   108: aload_0         /* this */
        //   109: getfield        io/netty/channel/nio/NioEventLoop.wakenUp:Ljava/util/concurrent/atomic/AtomicBoolean;
        //   112: invokevirtual   java/util/concurrent/atomic/AtomicBoolean.get:()Z
        //   115: ifne            250
        //   118: aload_0         /* this */
        //   119: invokevirtual   io/netty/channel/nio/NioEventLoop.hasTasks:()Z
        //   122: ifne            250
        //   125: aload_0         /* this */
        //   126: invokevirtual   io/netty/channel/nio/NioEventLoop.hasScheduledTasks:()Z
        //   129: ifeq            135
        //   132: goto            250
        //   135: invokestatic    java/lang/Thread.interrupted:()Z
        //   138: ifeq            168
        //   141: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   144: invokeinterface io/netty/util/internal/logging/InternalLogger.isDebugEnabled:()Z
        //   149: ifeq            163
        //   152: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   155: ldc_w           "Selector.select() returned prematurely because Thread.currentThread().interrupt() was called. Use NioEventLoop.shutdownGracefully() to shutdown the NioEventLoop."
        //   158: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;)V
        //   163: iconst_1       
        //   164: istore_3        /* selectCnt */
        //   165: goto            250
        //   168: invokestatic    java/lang/System.nanoTime:()J
        //   171: lstore          time
        //   173: lload           time
        //   175: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   178: lload           timeoutMillis
        //   180: invokevirtual   java/util/concurrent/TimeUnit.toNanos:(J)J
        //   183: lsub           
        //   184: lload           currentTimeNanos
        //   186: lcmp           
        //   187: iflt            195
        //   190: iconst_1       
        //   191: istore_3        /* selectCnt */
        //   192: goto            243
        //   195: getstatic       io/netty/channel/nio/NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD:I
        //   198: ifle            243
        //   201: iload_3         /* selectCnt */
        //   202: getstatic       io/netty/channel/nio/NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD:I
        //   205: if_icmplt       243
        //   208: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   211: ldc_w           "Selector.select() returned prematurely {} times in a row; rebuilding Selector {}."
        //   214: iload_3         /* selectCnt */
        //   215: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   218: aload_2         /* selector */
        //   219: invokeinterface io/netty/util/internal/logging/InternalLogger.warn:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   224: aload_0         /* this */
        //   225: invokevirtual   io/netty/channel/nio/NioEventLoop.rebuildSelector:()V
        //   228: aload_0         /* this */
        //   229: getfield        io/netty/channel/nio/NioEventLoop.selector:Ljava/nio/channels/Selector;
        //   232: astore_2        /* selector */
        //   233: aload_2         /* selector */
        //   234: invokevirtual   java/nio/channels/Selector.selectNow:()I
        //   237: pop            
        //   238: iconst_1       
        //   239: istore_3        /* selectCnt */
        //   240: goto            250
        //   243: lload           time
        //   245: lstore          currentTimeNanos
        //   247: goto            23
        //   250: iload_3         /* selectCnt */
        //   251: iconst_3       
        //   252: if_icmple       284
        //   255: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   258: invokeinterface io/netty/util/internal/logging/InternalLogger.isDebugEnabled:()Z
        //   263: ifeq            284
        //   266: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   269: ldc_w           "Selector.select() returned prematurely {} times in a row for Selector {}."
        //   272: iload_3         /* selectCnt */
        //   273: iconst_1       
        //   274: isub           
        //   275: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   278: aload_2         /* selector */
        //   279: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   284: goto            334
        //   287: astore_3        /* e */
        //   288: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   291: invokeinterface io/netty/util/internal/logging/InternalLogger.isDebugEnabled:()Z
        //   296: ifeq            334
        //   299: getstatic       io/netty/channel/nio/NioEventLoop.logger:Lio/netty/util/internal/logging/InternalLogger;
        //   302: new             Ljava/lang/StringBuilder;
        //   305: dup            
        //   306: invokespecial   java/lang/StringBuilder.<init>:()V
        //   309: ldc_w           Ljava/nio/channels/CancelledKeyException;.class
        //   312: invokevirtual   java/lang/Class.getSimpleName:()Ljava/lang/String;
        //   315: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   318: ldc_w           " raised by a Selector {} - JDK bug?"
        //   321: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   324: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   327: aload_2         /* selector */
        //   328: aload_3         /* e */
        //   329: invokeinterface io/netty/util/internal/logging/InternalLogger.debug:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   334: return         
        //    Exceptions:
        //  throws java.io.IOException
        //    MethodParameters:
        //  Name        Flags  
        //  ----------  -----
        //  oldWakenUp  
        //    StackMapTable: 00 0C FF 00 17 00 06 07 00 02 01 07 00 8C 01 04 04 00 00 FC 00 23 04 1C FC 00 2E 01 1B 04 FC 00 1A 04 2F F8 00 06 F8 00 21 42 07 02 1D 2E
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  5      284    287    334    Ljava/nio/channels/CancelledKeyException;
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
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void selectAgain() {
        this.needsToSelectAgain = false;
        try {
            this.selector.selectNow();
        }
        catch (Throwable t) {
            NioEventLoop.logger.warn("Failed to update SelectionKeys.", t);
        }
    }
    
    @Override
    protected void validateScheduled(final long amount, final TimeUnit unit) {
        final long days = unit.toDays(amount);
        if (days > 1095L) {
            throw new IllegalArgumentException(new StringBuilder().append("days: ").append(days).append(" (expected: < ").append(1095L).append(')').toString());
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(NioEventLoop.class);
        DISABLE_KEYSET_OPTIMIZATION = SystemPropertyUtil.getBoolean("io.netty.noKeySetOptimization", false);
        final String key = "sun.nio.ch.bugLevel";
        final String buglevel = SystemPropertyUtil.get("sun.nio.ch.bugLevel");
        if (buglevel == null) {
            try {
                AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Void>() {
                    public Void run() {
                        System.setProperty("sun.nio.ch.bugLevel", "");
                        return null;
                    }
                });
            }
            catch (SecurityException e) {
                NioEventLoop.logger.debug("Unable to get/set System Property: sun.nio.ch.bugLevel", (Throwable)e);
            }
        }
        int selectorAutoRebuildThreshold = SystemPropertyUtil.getInt("io.netty.selectorAutoRebuildThreshold", 512);
        if (selectorAutoRebuildThreshold < 3) {
            selectorAutoRebuildThreshold = 0;
        }
        SELECTOR_AUTO_REBUILD_THRESHOLD = selectorAutoRebuildThreshold;
        if (NioEventLoop.logger.isDebugEnabled()) {
            NioEventLoop.logger.debug("-Dio.netty.noKeySetOptimization: {}", NioEventLoop.DISABLE_KEYSET_OPTIMIZATION);
            NioEventLoop.logger.debug("-Dio.netty.selectorAutoRebuildThreshold: {}", NioEventLoop.SELECTOR_AUTO_REBUILD_THRESHOLD);
        }
    }
    
    private static final class SelectorTuple {
        final Selector unwrappedSelector;
        final Selector selector;
        
        SelectorTuple(final Selector unwrappedSelector) {
            this.unwrappedSelector = unwrappedSelector;
            this.selector = unwrappedSelector;
        }
        
        SelectorTuple(final Selector unwrappedSelector, final Selector selector) {
            this.unwrappedSelector = unwrappedSelector;
            this.selector = selector;
        }
    }
}
