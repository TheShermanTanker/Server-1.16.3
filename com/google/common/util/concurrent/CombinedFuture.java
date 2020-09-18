package com.google.common.util.concurrent;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import com.google.common.base.Preconditions;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import com.google.common.collect.ImmutableCollection;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CombinedFuture<V> extends AggregateFuture<Object, V> {
    CombinedFuture(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final AsyncCallable<V> callable) {
        this.init(new CombinedFutureRunningState(futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
    }
    
    CombinedFuture(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final Callable<V> callable) {
        this.init(new CombinedFutureRunningState(futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
    }
    
    private final class CombinedFutureRunningState extends RunningState {
        private CombinedFutureInterruptibleTask task;
        
        CombinedFutureRunningState(final ImmutableCollection<? extends ListenableFuture<?>> futures, final boolean allMustSucceed, final CombinedFutureInterruptibleTask task) {
            super(futures, allMustSucceed, false);
            this.task = task;
        }
        
        @Override
        void collectOneValue(final boolean allMustSucceed, final int index, @Nullable final Object returnValue) {
        }
        
        @Override
        void handleAllCompleted() {
            final CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.execute();
            }
            else {
                Preconditions.checkState(CombinedFuture.this.isDone());
            }
        }
        
        @Override
        void releaseResourcesAfterFailure() {
            super.releaseResourcesAfterFailure();
            this.task = null;
        }
        
        @Override
        void interruptTask() {
            final CombinedFutureInterruptibleTask localTask = this.task;
            if (localTask != null) {
                localTask.interruptTask();
            }
        }
    }
    
    private abstract class CombinedFutureInterruptibleTask extends InterruptibleTask {
        private final Executor listenerExecutor;
        volatile boolean thrownByExecute;
        
        public CombinedFutureInterruptibleTask(final Executor listenerExecutor) {
            this.thrownByExecute = true;
            this.listenerExecutor = Preconditions.<Executor>checkNotNull(listenerExecutor);
        }
        
        @Override
        final void runInterruptibly() {
            this.thrownByExecute = false;
            if (!CombinedFuture.this.isDone()) {
                try {
                    this.setValue();
                }
                catch (ExecutionException e) {
                    CombinedFuture.this.setException(e.getCause());
                }
                catch (CancellationException e3) {
                    CombinedFuture.this.cancel(false);
                }
                catch (Throwable e2) {
                    CombinedFuture.this.setException(e2);
                }
            }
        }
        
        @Override
        final boolean wasInterrupted() {
            return CombinedFuture.this.wasInterrupted();
        }
        
        final void execute() {
            try {
                this.listenerExecutor.execute((Runnable)this);
            }
            catch (RejectedExecutionException e) {
                if (this.thrownByExecute) {
                    CombinedFuture.this.setException((Throwable)e);
                }
            }
        }
        
        abstract void setValue() throws Exception;
    }
    
    private final class AsyncCallableInterruptibleTask extends CombinedFutureInterruptibleTask {
        private final AsyncCallable<V> callable;
        
        public AsyncCallableInterruptibleTask(final AsyncCallable<V> callable, final Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = Preconditions.<AsyncCallable<V>>checkNotNull(callable);
        }
        
        @Override
        void setValue() throws Exception {
            CombinedFuture.this.setFuture(this.callable.call());
        }
    }
    
    private final class CallableInterruptibleTask extends CombinedFutureInterruptibleTask {
        private final Callable<V> callable;
        
        public CallableInterruptibleTask(final Callable<V> callable, final Executor listenerExecutor) {
            super(listenerExecutor);
            this.callable = Preconditions.<Callable<V>>checkNotNull(callable);
        }
        
        @Override
        void setValue() throws Exception {
            CombinedFuture.this.set(this.callable.call());
        }
    }
}
