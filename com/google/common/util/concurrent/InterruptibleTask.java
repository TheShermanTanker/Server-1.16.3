package com.google.common.util.concurrent;

import java.util.logging.Level;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
abstract class InterruptibleTask implements Runnable {
    private volatile Thread runner;
    private volatile boolean doneInterrupting;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log;
    
    public final void run() {
        if (!InterruptibleTask.ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
            return;
        }
        try {
            this.runInterruptibly();
            if (this.wasInterrupted()) {
                while (!this.doneInterrupting) {
                    Thread.yield();
                }
            }
        }
        finally {
            if (this.wasInterrupted()) {
                while (!this.doneInterrupting) {
                    Thread.yield();
                }
            }
        }
    }
    
    abstract void runInterruptibly();
    
    abstract boolean wasInterrupted();
    
    final void interruptTask() {
        final Thread currentRunner = this.runner;
        if (currentRunner != null) {
            currentRunner.interrupt();
        }
        this.doneInterrupting = true;
    }
    
    static {
        log = Logger.getLogger(InterruptibleTask.class.getName());
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater((Class)InterruptibleTask.class, (Class)Thread.class, "runner"));
        }
        catch (Throwable reflectionFailure) {
            InterruptibleTask.log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }
    
    private abstract static class AtomicHelper {
        abstract boolean compareAndSetRunner(final InterruptibleTask interruptibleTask, final Thread thread2, final Thread thread3);
    }
    
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;
        
        SafeAtomicHelper(final AtomicReferenceFieldUpdater runnerUpdater) {
            this.runnerUpdater = (AtomicReferenceFieldUpdater<InterruptibleTask, Thread>)runnerUpdater;
        }
        
        @Override
        boolean compareAndSetRunner(final InterruptibleTask task, final Thread expect, final Thread update) {
            return this.runnerUpdater.compareAndSet(task, expect, update);
        }
    }
    
    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        @Override
        boolean compareAndSetRunner(final InterruptibleTask task, final Thread expect, final Thread update) {
            synchronized (task) {
                if (task.runner == expect) {
                    task.runner = update;
                }
            }
            return true;
        }
    }
}
