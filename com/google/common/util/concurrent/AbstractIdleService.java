package com.google.common.util.concurrent;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Executor;
import com.google.common.base.Supplier;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtIncompatible
public abstract class AbstractIdleService implements Service {
    private final Supplier<String> threadNameSupplier;
    private final Service delegate;
    
    protected AbstractIdleService() {
        this.threadNameSupplier = new ThreadNameSupplier();
        this.delegate = new DelegateService();
    }
    
    protected abstract void startUp() throws Exception;
    
    protected abstract void shutDown() throws Exception;
    
    protected Executor executor() {
        return (Executor)new Executor() {
            public void execute(final Runnable command) {
                MoreExecutors.newThread(AbstractIdleService.this.threadNameSupplier.get(), command).start();
            }
        };
    }
    
    public String toString() {
        return this.serviceName() + " [" + this.state() + "]";
    }
    
    public final boolean isRunning() {
        return this.delegate.isRunning();
    }
    
    public final State state() {
        return this.delegate.state();
    }
    
    public final void addListener(final Listener listener, final Executor executor) {
        this.delegate.addListener(listener, executor);
    }
    
    public final Throwable failureCause() {
        return this.delegate.failureCause();
    }
    
    @CanIgnoreReturnValue
    public final Service startAsync() {
        this.delegate.startAsync();
        return this;
    }
    
    @CanIgnoreReturnValue
    public final Service stopAsync() {
        this.delegate.stopAsync();
        return this;
    }
    
    public final void awaitRunning() {
        this.delegate.awaitRunning();
    }
    
    public final void awaitRunning(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitRunning(timeout, unit);
    }
    
    public final void awaitTerminated() {
        this.delegate.awaitTerminated();
    }
    
    public final void awaitTerminated(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.delegate.awaitTerminated(timeout, unit);
    }
    
    protected String serviceName() {
        return this.getClass().getSimpleName();
    }
    
    private final class ThreadNameSupplier implements Supplier<String> {
        public String get() {
            return AbstractIdleService.this.serviceName() + " " + AbstractIdleService.this.state();
        }
    }
    
    private final class DelegateService extends AbstractService {
        @Override
        protected final void doStart() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute((Runnable)new Runnable() {
                public void run() {
                    try {
                        AbstractIdleService.this.startUp();
                        DelegateService.this.notifyStarted();
                    }
                    catch (Throwable t) {
                        DelegateService.this.notifyFailed(t);
                    }
                }
            });
        }
        
        @Override
        protected final void doStop() {
            MoreExecutors.renamingDecorator(AbstractIdleService.this.executor(), AbstractIdleService.this.threadNameSupplier).execute((Runnable)new Runnable() {
                public void run() {
                    try {
                        AbstractIdleService.this.shutDown();
                        DelegateService.this.notifyStopped();
                    }
                    catch (Throwable t) {
                        DelegateService.this.notifyFailed(t);
                    }
                }
            });
        }
        
        @Override
        public String toString() {
            return AbstractIdleService.this.toString();
        }
    }
}
