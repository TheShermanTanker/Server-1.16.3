package org.apache.logging.log4j.core.util;

import java.lang.ref.SoftReference;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.AbstractLifeCycle;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.lang.ref.Reference;
import java.util.Collection;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.core.LifeCycle;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LifeCycle2;

public class DefaultShutdownCallbackRegistry implements ShutdownCallbackRegistry, LifeCycle2, Runnable {
    protected static final Logger LOGGER;
    private final AtomicReference<LifeCycle.State> state;
    private final ThreadFactory threadFactory;
    private final Collection<Cancellable> hooks;
    private Reference<Thread> shutdownHookRef;
    
    public DefaultShutdownCallbackRegistry() {
        this(Executors.defaultThreadFactory());
    }
    
    protected DefaultShutdownCallbackRegistry(final ThreadFactory threadFactory) {
        this.state = (AtomicReference<LifeCycle.State>)new AtomicReference(LifeCycle.State.INITIALIZED);
        this.hooks = (Collection<Cancellable>)new CopyOnWriteArrayList();
        this.threadFactory = threadFactory;
    }
    
    public void run() {
        if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
            for (final Runnable hook : this.hooks) {
                try {
                    hook.run();
                }
                catch (Throwable t1) {
                    try {
                        DefaultShutdownCallbackRegistry.LOGGER.error(DefaultShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER, "Caught exception executing shutdown hook {}", hook, t1);
                    }
                    catch (Throwable t2) {
                        System.err.println(new StringBuilder().append("Caught exception ").append(t2.getClass()).append(" logging exception ").append(t1.getClass()).toString());
                        t1.printStackTrace();
                    }
                }
            }
            this.state.set(LifeCycle.State.STOPPED);
        }
    }
    
    public Cancellable addShutdownCallback(final Runnable callback) {
        if (this.isStarted()) {
            final Cancellable receipt = new RegisteredCancellable(callback, this.hooks);
            this.hooks.add(receipt);
            return receipt;
        }
        throw new IllegalStateException("Cannot add new shutdown hook as this is not started. Current state: " + ((LifeCycle.State)this.state.get()).name());
    }
    
    public void initialize() {
    }
    
    public void start() {
        if (this.state.compareAndSet(LifeCycle.State.INITIALIZED, LifeCycle.State.STARTING)) {
            try {
                this.addShutdownHook(this.threadFactory.newThread((Runnable)this));
                this.state.set(LifeCycle.State.STARTED);
            }
            catch (IllegalStateException ex) {
                this.state.set(LifeCycle.State.STOPPED);
                throw ex;
            }
            catch (Exception e) {
                DefaultShutdownCallbackRegistry.LOGGER.catching((Throwable)e);
                this.state.set(LifeCycle.State.STOPPED);
            }
        }
    }
    
    private void addShutdownHook(final Thread thread) {
        this.shutdownHookRef = (Reference<Thread>)new WeakReference(thread);
        Runtime.getRuntime().addShutdownHook(thread);
    }
    
    public void stop() {
        this.stop(0L, AbstractLifeCycle.DEFAULT_STOP_TIMEUNIT);
    }
    
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        if (this.state.compareAndSet(LifeCycle.State.STARTED, LifeCycle.State.STOPPING)) {
            try {
                this.removeShutdownHook();
            }
            finally {
                this.state.set(LifeCycle.State.STOPPED);
            }
        }
        return true;
    }
    
    private void removeShutdownHook() {
        final Thread shutdownThread = (Thread)this.shutdownHookRef.get();
        if (shutdownThread != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownThread);
            this.shutdownHookRef.enqueue();
        }
    }
    
    public LifeCycle.State getState() {
        return (LifeCycle.State)this.state.get();
    }
    
    public boolean isStarted() {
        return this.state.get() == LifeCycle.State.STARTED;
    }
    
    public boolean isStopped() {
        return this.state.get() == LifeCycle.State.STOPPED;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    private static class RegisteredCancellable implements Cancellable {
        private final Reference<Runnable> hook;
        private Collection<Cancellable> registered;
        
        RegisteredCancellable(final Runnable callback, final Collection<Cancellable> registered) {
            this.registered = registered;
            this.hook = (Reference<Runnable>)new SoftReference(callback);
        }
        
        public void cancel() {
            this.hook.clear();
            this.registered.remove(this);
            this.registered = null;
        }
        
        public void run() {
            final Runnable runnableHook = (Runnable)this.hook.get();
            if (runnableHook != null) {
                runnableHook.run();
                this.hook.clear();
            }
        }
        
        public String toString() {
            return String.valueOf(this.hook.get());
        }
    }
}
