package com.google.common.util.concurrent;

import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledFuture;
import java.util.Collections;
import javax.annotation.concurrent.GuardedBy;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.RejectedExecutionException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Supplier;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Throwables;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import com.google.common.base.Preconditions;
import java.util.concurrent.Callable;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class MoreExecutors {
    private MoreExecutors() {
    }
    
    @Beta
    @GwtIncompatible
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(executor, terminationTimeout, timeUnit);
    }
    
    @Beta
    @GwtIncompatible
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
    }
    
    @Beta
    @GwtIncompatible
    public static void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(service, terminationTimeout, timeUnit);
    }
    
    @Beta
    @GwtIncompatible
    public static ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor) {
        return new Application().getExitingExecutorService(executor);
    }
    
    @Beta
    @GwtIncompatible
    public static ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
        return new Application().getExitingScheduledExecutorService(executor);
    }
    
    @GwtIncompatible
    private static void useDaemonThreadFactory(final ThreadPoolExecutor executor) {
        executor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(executor.getThreadFactory()).build());
    }
    
    @GwtIncompatible
    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService();
    }
    
    public static Executor directExecutor() {
        return (Executor)DirectExecutor.INSTANCE;
    }
    
    @GwtIncompatible
    public static ListeningExecutorService listeningDecorator(final ExecutorService delegate) {
        return (delegate instanceof ListeningExecutorService) ? delegate : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
    }
    
    @GwtIncompatible
    public static ListeningScheduledExecutorService listeningDecorator(final ScheduledExecutorService delegate) {
        return (delegate instanceof ListeningScheduledExecutorService) ? delegate : new ScheduledListeningDecorator(delegate);
    }
    
    @GwtIncompatible
    static <T> T invokeAnyImpl(final ListeningExecutorService executorService, final Collection<? extends Callable<T>> tasks, final boolean timed, final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        Preconditions.<ListeningExecutorService>checkNotNull(executorService);
        Preconditions.<TimeUnit>checkNotNull(unit);
        int ntasks = tasks.size();
        Preconditions.checkArgument(ntasks > 0);
        final List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
        final BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
        long timeoutNanos = unit.toNanos(timeout);
        try {
            ExecutionException ee = null;
            long lastTime = timed ? System.nanoTime() : 0L;
            final Iterator<? extends Callable<T>> it = tasks.iterator();
            futures.add(MoreExecutors.<T>submitAndAddQueueListener(executorService, (java.util.concurrent.Callable<T>)it.next(), futureQueue));
            --ntasks;
            int active = 1;
            while (true) {
                Future<T> f = (Future<T>)futureQueue.poll();
                if (f == null) {
                    if (ntasks > 0) {
                        --ntasks;
                        futures.add(MoreExecutors.<T>submitAndAddQueueListener(executorService, (java.util.concurrent.Callable<T>)it.next(), futureQueue));
                        ++active;
                    }
                    else {
                        if (active == 0) {
                            if (ee == null) {
                                ee = new ExecutionException((Throwable)null);
                            }
                            throw ee;
                        }
                        if (timed) {
                            f = (Future<T>)futureQueue.poll(timeoutNanos, TimeUnit.NANOSECONDS);
                            if (f == null) {
                                throw new TimeoutException();
                            }
                            final long now = System.nanoTime();
                            timeoutNanos -= now - lastTime;
                            lastTime = now;
                        }
                        else {
                            f = (Future<T>)futureQueue.take();
                        }
                    }
                }
                if (f != null) {
                    --active;
                    try {
                        return (T)f.get();
                    }
                    catch (ExecutionException eex) {
                        ee = eex;
                    }
                    catch (RuntimeException rex) {
                        ee = new ExecutionException((Throwable)rex);
                    }
                }
            }
        }
        finally {
            for (final Future<T> f2 : futures) {
                f2.cancel(true);
            }
        }
    }
    
    @GwtIncompatible
    private static <T> ListenableFuture<T> submitAndAddQueueListener(final ListeningExecutorService executorService, final Callable<T> task, final BlockingQueue<Future<T>> queue) {
        final ListenableFuture<T> future = executorService.<T>submit(task);
        future.addListener((Runnable)new Runnable() {
            public void run() {
                queue.add(future);
            }
        }, directExecutor());
        return future;
    }
    
    @Beta
    @GwtIncompatible
    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngine()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", (Throwable)e);
        }
        catch (ClassNotFoundException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", (Throwable)e2);
        }
        catch (NoSuchMethodException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", (Throwable)e3);
        }
        catch (InvocationTargetException e4) {
            throw Throwables.propagate(e4.getCause());
        }
    }
    
    @GwtIncompatible
    private static boolean isAppEngine() {
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            return Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
        catch (InvocationTargetException e2) {
            return false;
        }
        catch (IllegalAccessException e3) {
            return false;
        }
        catch (NoSuchMethodException e4) {
            return false;
        }
    }
    
    @GwtIncompatible
    static Thread newThread(final String name, final Runnable runnable) {
        Preconditions.<String>checkNotNull(name);
        Preconditions.<Runnable>checkNotNull(runnable);
        final Thread result = platformThreadFactory().newThread(runnable);
        try {
            result.setName(name);
        }
        catch (SecurityException ex) {}
        return result;
    }
    
    @GwtIncompatible
    static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
        Preconditions.<Executor>checkNotNull(executor);
        Preconditions.<Supplier<String>>checkNotNull(nameSupplier);
        if (isAppEngine()) {
            return executor;
        }
        return (Executor)new Executor() {
            public void execute(final Runnable command) {
                executor.execute(Callables.threadRenaming(command, nameSupplier));
            }
        };
    }
    
    @GwtIncompatible
    static ExecutorService renamingDecorator(final ExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.<ExecutorService>checkNotNull(service);
        Preconditions.<Supplier<String>>checkNotNull(nameSupplier);
        if (isAppEngine()) {
            return service;
        }
        return (ExecutorService)new WrappingExecutorService(service) {
            @Override
            protected <T> Callable<T> wrapTask(final Callable<T> callable) {
                return Callables.<T>threadRenaming(callable, nameSupplier);
            }
            
            @Override
            protected Runnable wrapTask(final Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }
    
    @GwtIncompatible
    static ScheduledExecutorService renamingDecorator(final ScheduledExecutorService service, final Supplier<String> nameSupplier) {
        Preconditions.<ScheduledExecutorService>checkNotNull(service);
        Preconditions.<Supplier<String>>checkNotNull(nameSupplier);
        if (isAppEngine()) {
            return service;
        }
        return (ScheduledExecutorService)new WrappingScheduledExecutorService(service) {
            @Override
            protected <T> Callable<T> wrapTask(final Callable<T> callable) {
                return Callables.<T>threadRenaming(callable, nameSupplier);
            }
            
            @Override
            protected Runnable wrapTask(final Runnable command) {
                return Callables.threadRenaming(command, nameSupplier);
            }
        };
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static boolean shutdownAndAwaitTermination(final ExecutorService service, final long timeout, final TimeUnit unit) {
        final long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
        service.shutdown();
        try {
            if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
                service.shutdownNow();
                service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
            }
        }
        catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            service.shutdownNow();
        }
        return service.isTerminated();
    }
    
    static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
        Preconditions.<Executor>checkNotNull(delegate);
        Preconditions.<AbstractFuture<?>>checkNotNull(future);
        if (delegate == directExecutor()) {
            return delegate;
        }
        return (Executor)new Executor() {
            volatile boolean thrownFromDelegate = true;
            
            public void execute(final Runnable command) {
                try {
                    delegate.execute((Runnable)new Runnable() {
                        public void run() {
                            Executor.this.thrownFromDelegate = false;
                            command.run();
                        }
                    });
                }
                catch (RejectedExecutionException e) {
                    if (this.thrownFromDelegate) {
                        future.setException((Throwable)e);
                    }
                }
            }
        };
    }
    
    @GwtIncompatible
    @VisibleForTesting
    static class Application {
        final ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
            useDaemonThreadFactory(executor);
            final ExecutorService service = Executors.unconfigurableExecutorService((ExecutorService)executor);
            this.addDelayedShutdownHook(service, terminationTimeout, timeUnit);
            return service;
        }
        
        final ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor, final long terminationTimeout, final TimeUnit timeUnit) {
            useDaemonThreadFactory((ThreadPoolExecutor)executor);
            final ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService((ScheduledExecutorService)executor);
            this.addDelayedShutdownHook((ExecutorService)service, terminationTimeout, timeUnit);
            return service;
        }
        
        final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
            Preconditions.<ExecutorService>checkNotNull(service);
            Preconditions.<TimeUnit>checkNotNull(timeUnit);
            this.addShutdownHook(MoreExecutors.newThread(new StringBuilder().append("DelayedShutdownHook-for-").append(service).toString(), (Runnable)new Runnable() {
                public void run() {
                    try {
                        service.shutdown();
                        service.awaitTermination(terminationTimeout, timeUnit);
                    }
                    catch (InterruptedException ex) {}
                }
            }));
        }
        
        final ExecutorService getExitingExecutorService(final ThreadPoolExecutor executor) {
            return this.getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
        }
        
        final ScheduledExecutorService getExitingScheduledExecutorService(final ScheduledThreadPoolExecutor executor) {
            return this.getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
        }
        
        @VisibleForTesting
        void addShutdownHook(final Thread hook) {
            Runtime.getRuntime().addShutdownHook(hook);
        }
    }
    
    @GwtIncompatible
    private static final class DirectExecutorService extends AbstractListeningExecutorService {
        private final Object lock;
        @GuardedBy("lock")
        private int runningTasks;
        @GuardedBy("lock")
        private boolean shutdown;
        
        private DirectExecutorService() {
            this.lock = new Object();
            this.runningTasks = 0;
            this.shutdown = false;
        }
        
        public void execute(final Runnable command) {
            this.startTask();
            try {
                command.run();
            }
            finally {
                this.endTask();
            }
        }
        
        public boolean isShutdown() {
            synchronized (this.lock) {
                return this.shutdown;
            }
        }
        
        public void shutdown() {
            synchronized (this.lock) {
                this.shutdown = true;
                if (this.runningTasks == 0) {
                    this.lock.notifyAll();
                }
            }
        }
        
        public List<Runnable> shutdownNow() {
            this.shutdown();
            return (List<Runnable>)Collections.emptyList();
        }
        
        public boolean isTerminated() {
            synchronized (this.lock) {
                return this.shutdown && this.runningTasks == 0;
            }
        }
        
        public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            long nanos = unit.toNanos(timeout);
            synchronized (this.lock) {
                while (!this.shutdown || this.runningTasks != 0) {
                    if (nanos <= 0L) {
                        return false;
                    }
                    final long now = System.nanoTime();
                    TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                    nanos -= System.nanoTime() - now;
                }
                return true;
            }
        }
        
        private void startTask() {
            synchronized (this.lock) {
                if (this.shutdown) {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
                ++this.runningTasks;
            }
        }
        
        private void endTask() {
            synchronized (this.lock) {
                final int runningTasks = this.runningTasks - 1;
                this.runningTasks = runningTasks;
                final int numRunning = runningTasks;
                if (numRunning == 0) {
                    this.lock.notifyAll();
                }
            }
        }
    }
    
    private enum DirectExecutor implements Executor {
        INSTANCE;
        
        public void execute(final Runnable command) {
            command.run();
        }
        
        public String toString() {
            return "MoreExecutors.directExecutor()";
        }
    }
    
    @GwtIncompatible
    private static class ListeningDecorator extends AbstractListeningExecutorService {
        private final ExecutorService delegate;
        
        ListeningDecorator(final ExecutorService delegate) {
            this.delegate = Preconditions.<ExecutorService>checkNotNull(delegate);
        }
        
        public final boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
            return this.delegate.awaitTermination(timeout, unit);
        }
        
        public final boolean isShutdown() {
            return this.delegate.isShutdown();
        }
        
        public final boolean isTerminated() {
            return this.delegate.isTerminated();
        }
        
        public final void shutdown() {
            this.delegate.shutdown();
        }
        
        public final List<Runnable> shutdownNow() {
            return (List<Runnable>)this.delegate.shutdownNow();
        }
        
        public final void execute(final Runnable command) {
            this.delegate.execute(command);
        }
    }
    
    @GwtIncompatible
    private static final class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;
        
        ScheduledListeningDecorator(final ScheduledExecutorService delegate) {
            super((ExecutorService)delegate);
            this.delegate = Preconditions.<ScheduledExecutorService>checkNotNull(delegate);
        }
        
        @Override
        public ListenableScheduledFuture<?> schedule(final Runnable command, final long delay, final TimeUnit unit) {
            final TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.<Void>create(command, (Void)null);
            final ScheduledFuture<?> scheduled = this.delegate.schedule((Runnable)task, delay, unit);
            return new ListenableScheduledTask<>(task, scheduled);
        }
        
        @Override
        public <V> ListenableScheduledFuture<V> schedule(final Callable<V> callable, final long delay, final TimeUnit unit) {
            final TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.<V>create(callable);
            final ScheduledFuture<?> scheduled = this.delegate.schedule((Runnable)task, delay, unit);
            return new ListenableScheduledTask<V>(task, scheduled);
        }
        
        @Override
        public ListenableScheduledFuture<?> scheduleAtFixedRate(final Runnable command, final long initialDelay, final long period, final TimeUnit unit) {
            final NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            final ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate((Runnable)task, initialDelay, period, unit);
            return new ListenableScheduledTask<>(task, scheduled);
        }
        
        @Override
        public ListenableScheduledFuture<?> scheduleWithFixedDelay(final Runnable command, final long initialDelay, final long delay, final TimeUnit unit) {
            final NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
            final ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay((Runnable)task, initialDelay, delay, unit);
            return new ListenableScheduledTask<>(task, scheduled);
        }
        
        private static final class ListenableScheduledTask<V> extends SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
            private final ScheduledFuture<?> scheduledDelegate;
            
            public ListenableScheduledTask(final ListenableFuture<V> listenableDelegate, final ScheduledFuture<?> scheduledDelegate) {
                super(listenableDelegate);
                this.scheduledDelegate = scheduledDelegate;
            }
            
            public boolean cancel(final boolean mayInterruptIfRunning) {
                final boolean cancelled = super.cancel(mayInterruptIfRunning);
                if (cancelled) {
                    this.scheduledDelegate.cancel(mayInterruptIfRunning);
                }
                return cancelled;
            }
            
            public long getDelay(final TimeUnit unit) {
                return this.scheduledDelegate.getDelay(unit);
            }
            
            public int compareTo(final Delayed other) {
                return this.scheduledDelegate.compareTo(other);
            }
        }
        
        @GwtIncompatible
        private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable {
            private final Runnable delegate;
            
            public NeverSuccessfulListenableFutureTask(final Runnable delegate) {
                this.delegate = Preconditions.<Runnable>checkNotNull(delegate);
            }
            
            public void run() {
                try {
                    this.delegate.run();
                }
                catch (Throwable t) {
                    this.setException(t);
                    throw Throwables.propagate(t);
                }
            }
        }
    }
}
