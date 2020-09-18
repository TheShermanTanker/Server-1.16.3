package io.netty.util;

import java.util.ArrayList;
import java.util.List;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.internal.StringUtil;
import io.netty.util.internal.SystemPropertyUtil;
import java.util.concurrent.ConcurrentLinkedQueue;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Queue;
import java.util.concurrent.ThreadFactory;
import io.netty.util.internal.logging.InternalLogger;

@Deprecated
public final class ThreadDeathWatcher {
    private static final InternalLogger logger;
    static final ThreadFactory threadFactory;
    private static final Queue<Entry> pendingEntries;
    private static final Watcher watcher;
    private static final AtomicBoolean started;
    private static volatile Thread watcherThread;
    
    public static void watch(final Thread thread, final Runnable task) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        if (!thread.isAlive()) {
            throw new IllegalArgumentException("thread must be alive.");
        }
        schedule(thread, task, true);
    }
    
    public static void unwatch(final Thread thread, final Runnable task) {
        if (thread == null) {
            throw new NullPointerException("thread");
        }
        if (task == null) {
            throw new NullPointerException("task");
        }
        schedule(thread, task, false);
    }
    
    private static void schedule(final Thread thread, final Runnable task, final boolean isWatch) {
        ThreadDeathWatcher.pendingEntries.add(new Entry(thread, task, isWatch));
        if (ThreadDeathWatcher.started.compareAndSet(false, true)) {
            final Thread watcherThread = ThreadDeathWatcher.threadFactory.newThread((Runnable)ThreadDeathWatcher.watcher);
            AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<Void>() {
                public Void run() {
                    watcherThread.setContextClassLoader((ClassLoader)null);
                    return null;
                }
            });
            watcherThread.start();
            ThreadDeathWatcher.watcherThread = watcherThread;
        }
    }
    
    public static boolean awaitInactivity(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (unit == null) {
            throw new NullPointerException("unit");
        }
        final Thread watcherThread = ThreadDeathWatcher.watcherThread;
        if (watcherThread != null) {
            watcherThread.join(unit.toMillis(timeout));
            return !watcherThread.isAlive();
        }
        return true;
    }
    
    private ThreadDeathWatcher() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ThreadDeathWatcher.class);
        pendingEntries = (Queue)new ConcurrentLinkedQueue();
        watcher = new Watcher();
        started = new AtomicBoolean();
        String poolName = "threadDeathWatcher";
        final String serviceThreadPrefix = SystemPropertyUtil.get("io.netty.serviceThreadPrefix");
        if (!StringUtil.isNullOrEmpty(serviceThreadPrefix)) {
            poolName = serviceThreadPrefix + poolName;
        }
        threadFactory = (ThreadFactory)new DefaultThreadFactory(poolName, true, 1, null);
    }
    
    private static final class Watcher implements Runnable {
        private final List<Entry> watchees;
        
        private Watcher() {
            this.watchees = (List<Entry>)new ArrayList();
        }
        
        public void run() {
            while (true) {
                this.fetchWatchees();
                this.notifyWatchees();
                this.fetchWatchees();
                this.notifyWatchees();
                try {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException ex) {}
                if (this.watchees.isEmpty() && ThreadDeathWatcher.pendingEntries.isEmpty()) {
                    final boolean stopped = ThreadDeathWatcher.started.compareAndSet(true, false);
                    assert stopped;
                    if (ThreadDeathWatcher.pendingEntries.isEmpty()) {
                        break;
                    }
                    if (!ThreadDeathWatcher.started.compareAndSet(false, true)) {
                        break;
                    }
                    continue;
                }
            }
        }
        
        private void fetchWatchees() {
            while (true) {
                final Entry e = (Entry)ThreadDeathWatcher.pendingEntries.poll();
                if (e == null) {
                    break;
                }
                if (e.isWatch) {
                    this.watchees.add(e);
                }
                else {
                    this.watchees.remove(e);
                }
            }
        }
        
        private void notifyWatchees() {
            final List<Entry> watchees = this.watchees;
            int i = 0;
            while (i < watchees.size()) {
                final Entry e = (Entry)watchees.get(i);
                if (!e.thread.isAlive()) {
                    watchees.remove(i);
                    try {
                        e.task.run();
                    }
                    catch (Throwable t) {
                        ThreadDeathWatcher.logger.warn("Thread death watcher task raised an exception:", t);
                    }
                }
                else {
                    ++i;
                }
            }
        }
    }
    
    private static final class Entry {
        final Thread thread;
        final Runnable task;
        final boolean isWatch;
        
        Entry(final Thread thread, final Runnable task, final boolean isWatch) {
            this.thread = thread;
            this.task = task;
            this.isWatch = isWatch;
        }
        
        public int hashCode() {
            return this.thread.hashCode() ^ this.task.hashCode();
        }
        
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            final Entry that = (Entry)obj;
            return this.thread == that.thread && this.task == that.task;
        }
    }
}
