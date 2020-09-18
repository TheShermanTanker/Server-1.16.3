package com.google.common.util.concurrent;

import java.util.logging.Level;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import com.google.common.collect.Sets;
import java.util.logging.Logger;
import java.util.Set;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
abstract class AggregateFutureState {
    private volatile Set<Throwable> seenExceptions;
    private volatile int remaining;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Logger log;
    
    AggregateFutureState(final int remainingFutures) {
        this.seenExceptions = null;
        this.remaining = remainingFutures;
    }
    
    final Set<Throwable> getOrInitSeenExceptions() {
        Set<Throwable> seenExceptionsLocal = this.seenExceptions;
        if (seenExceptionsLocal == null) {
            seenExceptionsLocal = Sets.<Throwable>newConcurrentHashSet();
            this.addInitialException(seenExceptionsLocal);
            AggregateFutureState.ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
            seenExceptionsLocal = this.seenExceptions;
        }
        return seenExceptionsLocal;
    }
    
    abstract void addInitialException(final Set<Throwable> set);
    
    final int decrementRemainingAndGet() {
        return AggregateFutureState.ATOMIC_HELPER.decrementAndGetRemainingCount(this);
    }
    
    static {
        log = Logger.getLogger(AggregateFutureState.class.getName());
        AtomicHelper helper;
        try {
            helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater((Class)AggregateFutureState.class, (Class)Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater((Class)AggregateFutureState.class, "remaining"));
        }
        catch (Throwable reflectionFailure) {
            AggregateFutureState.log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
            helper = new SynchronizedAtomicHelper();
        }
        ATOMIC_HELPER = helper;
    }
    
    private abstract static class AtomicHelper {
        abstract void compareAndSetSeenExceptions(final AggregateFutureState aggregateFutureState, final Set<Throwable> set2, final Set<Throwable> set3);
        
        abstract int decrementAndGetRemainingCount(final AggregateFutureState aggregateFutureState);
    }
    
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;
        final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
        
        SafeAtomicHelper(final AtomicReferenceFieldUpdater seenExceptionsUpdater, final AtomicIntegerFieldUpdater remainingCountUpdater) {
            this.seenExceptionsUpdater = (AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>>)seenExceptionsUpdater;
            this.remainingCountUpdater = (AtomicIntegerFieldUpdater<AggregateFutureState>)remainingCountUpdater;
        }
        
        @Override
        void compareAndSetSeenExceptions(final AggregateFutureState state, final Set<Throwable> expect, final Set<Throwable> update) {
            this.seenExceptionsUpdater.compareAndSet(state, expect, update);
        }
        
        @Override
        int decrementAndGetRemainingCount(final AggregateFutureState state) {
            return this.remainingCountUpdater.decrementAndGet(state);
        }
    }
    
    private static final class SynchronizedAtomicHelper extends AtomicHelper {
        @Override
        void compareAndSetSeenExceptions(final AggregateFutureState state, final Set<Throwable> expect, final Set<Throwable> update) {
            synchronized (state) {
                if (state.seenExceptions == expect) {
                    state.seenExceptions = update;
                }
            }
        }
        
        @Override
        int decrementAndGetRemainingCount(final AggregateFutureState state) {
            synchronized (state) {
                state.remaining--;
                return state.remaining;
            }
        }
    }
}
