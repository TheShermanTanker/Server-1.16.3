package com.google.common.eventbus;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.lang.reflect.Method;
import com.google.common.annotations.VisibleForTesting;
import com.google.j2objc.annotations.Weak;

class Subscriber {
    @Weak
    private EventBus bus;
    @VisibleForTesting
    final Object target;
    private final Method method;
    private final Executor executor;
    
    static Subscriber create(final EventBus bus, final Object listener, final Method method) {
        return isDeclaredThreadSafe(method) ? new Subscriber(bus, listener, method) : new SynchronizedSubscriber(bus, listener, method);
    }
    
    private Subscriber(final EventBus bus, final Object target, final Method method) {
        this.bus = bus;
        this.target = Preconditions.checkNotNull(target);
        (this.method = method).setAccessible(true);
        this.executor = bus.executor();
    }
    
    final void dispatchEvent(final Object event) {
        this.executor.execute((Runnable)new Runnable() {
            public void run() {
                try {
                    Subscriber.this.invokeSubscriberMethod(event);
                }
                catch (InvocationTargetException e) {
                    Subscriber.this.bus.handleSubscriberException(e.getCause(), Subscriber.this.context(event));
                }
            }
        });
    }
    
    @VisibleForTesting
    void invokeSubscriberMethod(final Object event) throws InvocationTargetException {
        try {
            this.method.invoke(this.target, new Object[] { Preconditions.checkNotNull(event) });
        }
        catch (IllegalArgumentException e) {
            throw new Error(new StringBuilder().append("Method rejected target/argument: ").append(event).toString(), (Throwable)e);
        }
        catch (IllegalAccessException e2) {
            throw new Error(new StringBuilder().append("Method became inaccessible: ").append(event).toString(), (Throwable)e2);
        }
        catch (InvocationTargetException e3) {
            if (e3.getCause() instanceof Error) {
                throw (Error)e3.getCause();
            }
            throw e3;
        }
    }
    
    private SubscriberExceptionContext context(final Object event) {
        return new SubscriberExceptionContext(this.bus, event, this.target, this.method);
    }
    
    public final int hashCode() {
        return (31 + this.method.hashCode()) * 31 + System.identityHashCode(this.target);
    }
    
    public final boolean equals(@Nullable final Object obj) {
        if (obj instanceof Subscriber) {
            final Subscriber that = (Subscriber)obj;
            return this.target == that.target && this.method.equals(that.method);
        }
        return false;
    }
    
    private static boolean isDeclaredThreadSafe(final Method method) {
        return method.getAnnotation((Class)AllowConcurrentEvents.class) != null;
    }
    
    @VisibleForTesting
    static final class SynchronizedSubscriber extends Subscriber {
        private SynchronizedSubscriber(final EventBus bus, final Object target, final Method method) {
            super(bus, target, method, null);
        }
        
        @Override
        void invokeSubscriberMethod(final Object event) throws InvocationTargetException {
            synchronized (this) {
                super.invokeSubscriberMethod(event);
            }
        }
    }
}
