package com.google.common.util.concurrent;

import java.util.Iterator;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import javax.annotation.concurrent.GuardedBy;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible
final class ListenerCallQueue<L> implements Runnable {
    private static final Logger logger;
    private final L listener;
    private final Executor executor;
    @GuardedBy("this")
    private final Queue<Callback<L>> waitQueue;
    @GuardedBy("this")
    private boolean isThreadScheduled;
    
    ListenerCallQueue(final L listener, final Executor executor) {
        this.waitQueue = Queues.newArrayDeque();
        this.listener = Preconditions.<L>checkNotNull(listener);
        this.executor = Preconditions.<Executor>checkNotNull(executor);
    }
    
    synchronized void add(final Callback<L> callback) {
        this.waitQueue.add(callback);
    }
    
    void execute() {
        boolean scheduleTaskRunner = false;
        synchronized (this) {
            if (!this.isThreadScheduled) {
                this.isThreadScheduled = true;
                scheduleTaskRunner = true;
            }
        }
        if (scheduleTaskRunner) {
            try {
                this.executor.execute((Runnable)this);
            }
            catch (RuntimeException e) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                }
                ListenerCallQueue.logger.log(Level.SEVERE, new StringBuilder().append("Exception while running callbacks for ").append(this.listener).append(" on ").append(this.executor).toString(), (Throwable)e);
                throw e;
            }
        }
    }
    
    public void run() {
        boolean stillRunning = true;
        try {
            while (true) {
                final Callback<L> nextToRun;
                synchronized (this) {
                    Preconditions.checkState(this.isThreadScheduled);
                    nextToRun = (Callback<L>)this.waitQueue.poll();
                    if (nextToRun == null) {
                        this.isThreadScheduled = false;
                        stillRunning = false;
                        break;
                    }
                }
                try {
                    nextToRun.call(this.listener);
                }
                catch (RuntimeException e) {
                    ListenerCallQueue.logger.log(Level.SEVERE, new StringBuilder().append("Exception while executing callback: ").append(this.listener).append(".").append(((Callback<Object>)nextToRun).methodCall).toString(), (Throwable)e);
                }
            }
        }
        finally {
            if (stillRunning) {
                synchronized (this) {
                    this.isThreadScheduled = false;
                }
            }
        }
    }
    
    static {
        logger = Logger.getLogger(ListenerCallQueue.class.getName());
    }
    
    abstract static class Callback<L> {
        private final String methodCall;
        
        Callback(final String methodCall) {
            this.methodCall = methodCall;
        }
        
        abstract void call(final L object);
        
        void enqueueOn(final Iterable<ListenerCallQueue<L>> queues) {
            for (final ListenerCallQueue<L> queue : queues) {
                queue.add(this);
            }
        }
    }
}
