package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.concurrent.Executor;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collections;
import java.util.ArrayList;
import javax.annotation.concurrent.GuardedBy;
import java.util.List;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public abstract class AbstractService implements Service {
    private static final ListenerCallQueue.Callback<Listener> STARTING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> RUNNING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> STOPPING_FROM_STARTING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> STOPPING_FROM_RUNNING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> TERMINATED_FROM_NEW_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> TERMINATED_FROM_RUNNING_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> TERMINATED_FROM_STOPPING_CALLBACK;
    private final Monitor monitor;
    private final Monitor.Guard isStartable;
    private final Monitor.Guard isStoppable;
    private final Monitor.Guard hasReachedRunning;
    private final Monitor.Guard isStopped;
    @GuardedBy("monitor")
    private final List<ListenerCallQueue<Listener>> listeners;
    @GuardedBy("monitor")
    private volatile StateSnapshot snapshot;
    
    private static ListenerCallQueue.Callback<Listener> terminatedCallback(final State from) {
        return new ListenerCallQueue.Callback<Listener>(new StringBuilder().append("terminated({from = ").append(from).append("})").toString()) {
            @Override
            void call(final Listener listener) {
                listener.terminated(from);
            }
        };
    }
    
    private static ListenerCallQueue.Callback<Listener> stoppingCallback(final State from) {
        return new ListenerCallQueue.Callback<Listener>(new StringBuilder().append("stopping({from = ").append(from).append("})").toString()) {
            @Override
            void call(final Listener listener) {
                listener.stopping(from);
            }
        };
    }
    
    protected AbstractService() {
        this.monitor = new Monitor();
        this.isStartable = new IsStartableGuard();
        this.isStoppable = new IsStoppableGuard();
        this.hasReachedRunning = new HasReachedRunningGuard();
        this.isStopped = new IsStoppedGuard();
        this.listeners = (List<ListenerCallQueue<Listener>>)Collections.synchronizedList((List)new ArrayList());
        this.snapshot = new StateSnapshot(State.NEW);
    }
    
    protected abstract void doStart();
    
    protected abstract void doStop();
    
    @CanIgnoreReturnValue
    public final Service startAsync() {
        if (this.monitor.enterIf(this.isStartable)) {
            try {
                this.snapshot = new StateSnapshot(State.STARTING);
                this.starting();
                this.doStart();
            }
            catch (Throwable startupFailure) {
                this.notifyFailed(startupFailure);
            }
            finally {
                this.monitor.leave();
                this.executeListeners();
            }
            return this;
        }
        throw new IllegalStateException(new StringBuilder().append("Service ").append(this).append(" has already been started").toString());
    }
    
    @CanIgnoreReturnValue
    public final Service stopAsync() {
        if (this.monitor.enterIf(this.isStoppable)) {
            try {
                final State previous = this.state();
                switch (previous) {
                    case NEW: {
                        this.snapshot = new StateSnapshot(State.TERMINATED);
                        this.terminated(State.NEW);
                        break;
                    }
                    case STARTING: {
                        this.snapshot = new StateSnapshot(State.STARTING, true, null);
                        this.stopping(State.STARTING);
                        break;
                    }
                    case RUNNING: {
                        this.snapshot = new StateSnapshot(State.STOPPING);
                        this.stopping(State.RUNNING);
                        this.doStop();
                        break;
                    }
                    case STOPPING:
                    case TERMINATED:
                    case FAILED: {
                        throw new AssertionError(new StringBuilder().append("isStoppable is incorrectly implemented, saw: ").append((Object)previous).toString());
                    }
                    default: {
                        throw new AssertionError(new StringBuilder().append("Unexpected state: ").append((Object)previous).toString());
                    }
                }
            }
            catch (Throwable shutdownFailure) {
                this.notifyFailed(shutdownFailure);
            }
            finally {
                this.monitor.leave();
                this.executeListeners();
            }
        }
        return this;
    }
    
    public final void awaitRunning() {
        this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
        try {
            this.checkCurrentState(State.RUNNING);
        }
        finally {
            this.monitor.leave();
        }
    }
    
    public final void awaitRunning(final long timeout, final TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
            try {
                this.checkCurrentState(State.RUNNING);
            }
            finally {
                this.monitor.leave();
            }
            return;
        }
        throw new TimeoutException(new StringBuilder().append("Timed out waiting for ").append(this).append(" to reach the RUNNING state.").toString());
    }
    
    public final void awaitTerminated() {
        this.monitor.enterWhenUninterruptibly(this.isStopped);
        try {
            this.checkCurrentState(State.TERMINATED);
        }
        finally {
            this.monitor.leave();
        }
    }
    
    public final void awaitTerminated(final long timeout, final TimeUnit unit) throws TimeoutException {
        if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
            try {
                this.checkCurrentState(State.TERMINATED);
            }
            finally {
                this.monitor.leave();
            }
            return;
        }
        throw new TimeoutException(new StringBuilder().append("Timed out waiting for ").append(this).append(" to reach a terminal state. Current state: ").append(this.state()).toString());
    }
    
    @GuardedBy("monitor")
    private void checkCurrentState(final State expected) {
        final State actual = this.state();
        if (actual == expected) {
            return;
        }
        if (actual == State.FAILED) {
            throw new IllegalStateException(new StringBuilder().append("Expected the service ").append(this).append(" to be ").append(expected).append(", but the service has FAILED").toString(), this.failureCause());
        }
        throw new IllegalStateException(new StringBuilder().append("Expected the service ").append(this).append(" to be ").append(expected).append(", but was ").append(actual).toString());
    }
    
    protected final void notifyStarted() {
        this.monitor.enter();
        try {
            if (this.snapshot.state != State.STARTING) {
                final IllegalStateException failure = new IllegalStateException(new StringBuilder().append("Cannot notifyStarted() when the service is ").append(this.snapshot.state).toString());
                this.notifyFailed((Throwable)failure);
                throw failure;
            }
            if (this.snapshot.shutdownWhenStartupFinishes) {
                this.snapshot = new StateSnapshot(State.STOPPING);
                this.doStop();
            }
            else {
                this.snapshot = new StateSnapshot(State.RUNNING);
                this.running();
            }
        }
        finally {
            this.monitor.leave();
            this.executeListeners();
        }
    }
    
    protected final void notifyStopped() {
        this.monitor.enter();
        try {
            final State previous = this.snapshot.state;
            if (previous != State.STOPPING && previous != State.RUNNING) {
                final IllegalStateException failure = new IllegalStateException(new StringBuilder().append("Cannot notifyStopped() when the service is ").append(previous).toString());
                this.notifyFailed((Throwable)failure);
                throw failure;
            }
            this.snapshot = new StateSnapshot(State.TERMINATED);
            this.terminated(previous);
        }
        finally {
            this.monitor.leave();
            this.executeListeners();
        }
    }
    
    protected final void notifyFailed(final Throwable cause) {
        Preconditions.<Throwable>checkNotNull(cause);
        this.monitor.enter();
        try {
            final State previous = this.state();
            switch (previous) {
                case NEW:
                case TERMINATED: {
                    throw new IllegalStateException(new StringBuilder().append("Failed while in state:").append(previous).toString(), cause);
                }
                case STARTING:
                case RUNNING:
                case STOPPING: {
                    this.snapshot = new StateSnapshot(State.FAILED, false, cause);
                    this.failed(previous, cause);
                    break;
                }
                case FAILED: {
                    break;
                }
                default: {
                    throw new AssertionError(new StringBuilder().append("Unexpected state: ").append((Object)previous).toString());
                }
            }
        }
        finally {
            this.monitor.leave();
            this.executeListeners();
        }
    }
    
    public final boolean isRunning() {
        return this.state() == State.RUNNING;
    }
    
    public final State state() {
        return this.snapshot.externalState();
    }
    
    public final Throwable failureCause() {
        return this.snapshot.failureCause();
    }
    
    public final void addListener(final Listener listener, final Executor executor) {
        Preconditions.<Listener>checkNotNull(listener, "listener");
        Preconditions.<Executor>checkNotNull(executor, "executor");
        this.monitor.enter();
        try {
            if (!this.state().isTerminal()) {
                this.listeners.add(new ListenerCallQueue(listener, executor));
            }
        }
        finally {
            this.monitor.leave();
        }
    }
    
    public String toString() {
        return this.getClass().getSimpleName() + " [" + this.state() + "]";
    }
    
    private void executeListeners() {
        if (!this.monitor.isOccupiedByCurrentThread()) {
            for (int i = 0; i < this.listeners.size(); ++i) {
                ((ListenerCallQueue)this.listeners.get(i)).execute();
            }
        }
    }
    
    @GuardedBy("monitor")
    private void starting() {
        AbstractService.STARTING_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
    }
    
    @GuardedBy("monitor")
    private void running() {
        AbstractService.RUNNING_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
    }
    
    @GuardedBy("monitor")
    private void stopping(final State from) {
        if (from == State.STARTING) {
            AbstractService.STOPPING_FROM_STARTING_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
        }
        else {
            if (from != State.RUNNING) {
                throw new AssertionError();
            }
            AbstractService.STOPPING_FROM_RUNNING_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
        }
    }
    
    @GuardedBy("monitor")
    private void terminated(final State from) {
        switch (from) {
            case NEW: {
                AbstractService.TERMINATED_FROM_NEW_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
                break;
            }
            case RUNNING: {
                AbstractService.TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
                break;
            }
            case STOPPING: {
                AbstractService.TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GuardedBy("monitor")
    private void failed(final State from, final Throwable cause) {
        new ListenerCallQueue.Callback<Listener>(new StringBuilder().append("failed({from = ").append(from).append(", cause = ").append(cause).append("})").toString()) {
            @Override
            void call(final Listener listener) {
                listener.failed(from, cause);
            }
        }.enqueueOn((java.lang.Iterable<ListenerCallQueue<Listener>>)this.listeners);
    }
    
    static {
        STARTING_CALLBACK = new ListenerCallQueue.Callback<Listener>("starting()") {
            @Override
            void call(final Listener listener) {
                listener.starting();
            }
        };
        RUNNING_CALLBACK = new ListenerCallQueue.Callback<Listener>("running()") {
            @Override
            void call(final Listener listener) {
                listener.running();
            }
        };
        STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(State.STARTING);
        STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(State.RUNNING);
        TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(State.NEW);
        TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(State.RUNNING);
        TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(State.STOPPING);
    }
    
    private final class IsStartableGuard extends Monitor.Guard {
        IsStartableGuard() {
            super(AbstractService.this.monitor);
        }
        
        @Override
        public boolean isSatisfied() {
            return AbstractService.this.state() == State.NEW;
        }
    }
    
    private final class IsStoppableGuard extends Monitor.Guard {
        IsStoppableGuard() {
            super(AbstractService.this.monitor);
        }
        
        @Override
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo((Enum)State.RUNNING) <= 0;
        }
    }
    
    private final class HasReachedRunningGuard extends Monitor.Guard {
        HasReachedRunningGuard() {
            super(AbstractService.this.monitor);
        }
        
        @Override
        public boolean isSatisfied() {
            return AbstractService.this.state().compareTo((Enum)State.RUNNING) >= 0;
        }
    }
    
    private final class IsStoppedGuard extends Monitor.Guard {
        IsStoppedGuard() {
            super(AbstractService.this.monitor);
        }
        
        @Override
        public boolean isSatisfied() {
            return AbstractService.this.state().isTerminal();
        }
    }
    
    @Immutable
    private static final class StateSnapshot {
        final State state;
        final boolean shutdownWhenStartupFinishes;
        @Nullable
        final Throwable failure;
        
        StateSnapshot(final State internalState) {
            this(internalState, false, null);
        }
        
        StateSnapshot(final State internalState, final boolean shutdownWhenStartupFinishes, @Nullable final Throwable failure) {
            Preconditions.checkArgument(!shutdownWhenStartupFinishes || internalState == State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
            Preconditions.checkArgument(!(failure != null ^ internalState == State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
            this.state = internalState;
            this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
            this.failure = failure;
        }
        
        State externalState() {
            if (this.shutdownWhenStartupFinishes && this.state == State.STARTING) {
                return State.STOPPING;
            }
            return this.state;
        }
        
        Throwable failureCause() {
            Preconditions.checkState(this.state == State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
            return this.failure;
        }
    }
}
