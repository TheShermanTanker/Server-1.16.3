package com.google.common.util.concurrent;

import com.google.common.base.Throwables;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Level;
import java.util.concurrent.Future;
import com.google.common.annotations.Beta;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import java.util.concurrent.Executor;
import java.util.concurrent.CancellationException;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public abstract class AbstractFuture<V> implements ListenableFuture<V> {
    private static final boolean GENERATE_CANCELLATION_CAUSES;
    private static final Logger log;
    private static final long SPIN_THRESHOLD_NANOS = 1000L;
    private static final AtomicHelper ATOMIC_HELPER;
    private static final Object NULL;
    private volatile Object value;
    private volatile Listener listeners;
    private volatile Waiter waiters;
    
    private void removeWaiter(final Waiter node) {
        node.thread = null;
    Label_0005:
        while (true) {
            Waiter pred = null;
            Waiter curr = this.waiters;
            if (curr == Waiter.TOMBSTONE) {
                return;
            }
            while (curr != null) {
                final Waiter succ = curr.next;
                if (curr.thread != null) {
                    pred = curr;
                }
                else if (pred != null) {
                    pred.next = succ;
                    if (pred.thread == null) {
                        continue Label_0005;
                    }
                }
                else if (!AbstractFuture.ATOMIC_HELPER.casWaiters(this, curr, succ)) {
                    continue Label_0005;
                }
                curr = succ;
            }
        }
    }
    
    protected AbstractFuture() {
    }
    
    @CanIgnoreReturnValue
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
        long remainingNanos = unit.toNanos(timeout);
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if (localValue != null & !(localValue instanceof SetFuture)) {
            return this.getDoneValue(localValue);
        }
        final long endNanos = (remainingNanos > 0L) ? (System.nanoTime() + remainingNanos) : 0L;
        Label_0248: {
            if (remainingNanos >= 1000L) {
                Waiter oldHead = this.waiters;
                if (oldHead != Waiter.TOMBSTONE) {
                    final Waiter node = new Waiter();
                    do {
                        node.setNext(oldHead);
                        if (AbstractFuture.ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                            do {
                                LockSupport.parkNanos(this, remainingNanos);
                                if (Thread.interrupted()) {
                                    this.removeWaiter(node);
                                    throw new InterruptedException();
                                }
                                localValue = this.value;
                                if (localValue != null & !(localValue instanceof SetFuture)) {
                                    return this.getDoneValue(localValue);
                                }
                                remainingNanos = endNanos - System.nanoTime();
                            } while (remainingNanos >= 1000L);
                            this.removeWaiter(node);
                            break Label_0248;
                        }
                        oldHead = this.waiters;
                    } while (oldHead != Waiter.TOMBSTONE);
                }
                return this.getDoneValue(this.value);
            }
        }
        while (remainingNanos > 0L) {
            localValue = this.value;
            if (localValue != null & !(localValue instanceof SetFuture)) {
                return this.getDoneValue(localValue);
            }
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            remainingNanos = endNanos - System.nanoTime();
        }
        throw new TimeoutException();
    }
    
    @CanIgnoreReturnValue
    public V get() throws InterruptedException, ExecutionException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        Object localValue = this.value;
        if (localValue != null & !(localValue instanceof SetFuture)) {
            return this.getDoneValue(localValue);
        }
        Waiter oldHead = this.waiters;
        if (oldHead != Waiter.TOMBSTONE) {
            final Waiter node = new Waiter();
            do {
                node.setNext(oldHead);
                if (AbstractFuture.ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                    do {
                        LockSupport.park(this);
                        if (Thread.interrupted()) {
                            this.removeWaiter(node);
                            throw new InterruptedException();
                        }
                        localValue = this.value;
                    } while (!(localValue != null & !(localValue instanceof SetFuture)));
                    return this.getDoneValue(localValue);
                }
                oldHead = this.waiters;
            } while (oldHead != Waiter.TOMBSTONE);
        }
        return this.getDoneValue(this.value);
    }
    
    private V getDoneValue(final Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause);
        }
        if (obj instanceof Failure) {
            throw new ExecutionException(((Failure)obj).exception);
        }
        if (obj == AbstractFuture.NULL) {
            return null;
        }
        final V asV = (V)obj;
        return asV;
    }
    
    public boolean isDone() {
        final Object localValue = this.value;
        return localValue != null & !(localValue instanceof SetFuture);
    }
    
    public boolean isCancelled() {
        final Object localValue = this.value;
        return localValue instanceof Cancellation;
    }
    
    @CanIgnoreReturnValue
    public boolean cancel(final boolean mayInterruptIfRunning) {
        Object localValue = this.value;
        boolean rValue = false;
        if (localValue == null | localValue instanceof SetFuture) {
            final Throwable cause = (Throwable)(AbstractFuture.GENERATE_CANCELLATION_CAUSES ? new CancellationException("Future.cancel() was called.") : null);
            final Object valueToSet = new Cancellation(mayInterruptIfRunning, cause);
            AbstractFuture<?> abstractFuture = this;
            while (true) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                    rValue = true;
                    if (mayInterruptIfRunning) {
                        abstractFuture.interruptTask();
                    }
                    complete(abstractFuture);
                    if (localValue instanceof SetFuture) {
                        final ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
                        if (futureToPropagateTo instanceof TrustedFuture) {
                            final AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
                            localValue = trusted.value;
                            if (localValue == null | localValue instanceof SetFuture) {
                                abstractFuture = trusted;
                                continue;
                            }
                        }
                        else {
                            futureToPropagateTo.cancel(mayInterruptIfRunning);
                        }
                        break;
                    }
                    break;
                }
                else {
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof SetFuture)) {
                        break;
                    }
                    continue;
                }
            }
        }
        return rValue;
    }
    
    protected void interruptTask() {
    }
    
    protected final boolean wasInterrupted() {
        final Object localValue = this.value;
        return localValue instanceof Cancellation && ((Cancellation)localValue).wasInterrupted;
    }
    
    public void addListener(final Runnable listener, final Executor executor) {
        Preconditions.<Runnable>checkNotNull(listener, "Runnable was null.");
        Preconditions.<Executor>checkNotNull(executor, "Executor was null.");
        Listener oldHead = this.listeners;
        if (oldHead != Listener.TOMBSTONE) {
            final Listener newNode = new Listener(listener, executor);
            do {
                newNode.next = oldHead;
                if (AbstractFuture.ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                    return;
                }
                oldHead = this.listeners;
            } while (oldHead != Listener.TOMBSTONE);
        }
        executeListener(listener, executor);
    }
    
    @CanIgnoreReturnValue
    protected boolean set(@Nullable final V value) {
        final Object valueToSet = (value == null) ? AbstractFuture.NULL : value;
        if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }
    
    @CanIgnoreReturnValue
    protected boolean setException(final Throwable throwable) {
        final Object valueToSet = new Failure(Preconditions.<Throwable>checkNotNull(throwable));
        if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, valueToSet)) {
            complete(this);
            return true;
        }
        return false;
    }
    
    @Beta
    @CanIgnoreReturnValue
    protected boolean setFuture(final ListenableFuture<? extends V> future) {
        Preconditions.<ListenableFuture<? extends V>>checkNotNull(future);
        Object localValue = this.value;
        if (localValue == null) {
            if (future.isDone()) {
                final Object value = getFutureValue(future);
                if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, value)) {
                    complete(this);
                    return true;
                }
                return false;
            }
            else {
                final SetFuture valueToSet = new SetFuture((AbstractFuture<V>)this, future);
                if (AbstractFuture.ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                    try {
                        future.addListener((Runnable)valueToSet, MoreExecutors.directExecutor());
                    }
                    catch (Throwable t) {
                        Failure failure;
                        try {
                            failure = new Failure(t);
                        }
                        catch (Throwable oomMostLikely) {
                            failure = Failure.FALLBACK_INSTANCE;
                        }
                        AbstractFuture.ATOMIC_HELPER.casValue(this, valueToSet, failure);
                    }
                    return true;
                }
                localValue = this.value;
            }
        }
        if (localValue instanceof Cancellation) {
            future.cancel(((Cancellation)localValue).wasInterrupted);
        }
        return false;
    }
    
    private static Object getFutureValue(final ListenableFuture<?> future) {
        if (future instanceof TrustedFuture) {
            return ((AbstractFuture)future).value;
        }
        Object valueToSet;
        try {
            final Object v = Futures.getDone((java.util.concurrent.Future<Object>)future);
            valueToSet = ((v == null) ? AbstractFuture.NULL : v);
        }
        catch (ExecutionException exception) {
            valueToSet = new Failure(exception.getCause());
        }
        catch (CancellationException cancellation) {
            valueToSet = new Cancellation(false, (Throwable)cancellation);
        }
        catch (Throwable t) {
            valueToSet = new Failure(t);
        }
        return valueToSet;
    }
    
    private static void complete(AbstractFuture<?> future) {
        Listener next = null;
    Label_0002:
        while (true) {
            future.releaseWaiters();
            future.afterDone();
            next = future.clearListeners(next);
            future = null;
            while (next != null) {
                final Listener curr = next;
                next = next.next;
                final Runnable task = curr.task;
                if (task instanceof SetFuture) {
                    final SetFuture<?> setFuture = task;
                    future = setFuture.owner;
                    if (future.value != setFuture) {
                        continue;
                    }
                    final Object valueToSet = getFutureValue(setFuture.future);
                    if (AbstractFuture.ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
                        continue Label_0002;
                    }
                    continue;
                }
                else {
                    executeListener(task, curr.executor);
                }
            }
            break;
        }
    }
    
    @Beta
    protected void afterDone() {
    }
    
    final Throwable trustedGetException() {
        return ((Failure)this.value).exception;
    }
    
    final void maybePropagateCancellation(@Nullable final Future<?> related) {
        if (related != null & this.isCancelled()) {
            related.cancel(this.wasInterrupted());
        }
    }
    
    private void releaseWaiters() {
        Waiter head;
        do {
            head = this.waiters;
        } while (!AbstractFuture.ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
        for (Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
            currentWaiter.unpark();
        }
    }
    
    private Listener clearListeners(final Listener onto) {
        Listener head;
        do {
            head = this.listeners;
        } while (!AbstractFuture.ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
        Listener reversedList;
        Listener tmp;
        for (reversedList = onto; head != null; head = head.next, tmp.next = reversedList, reversedList = tmp) {
            tmp = head;
        }
        return reversedList;
    }
    
    private static void executeListener(final Runnable runnable, final Executor executor) {
        try {
            executor.execute(runnable);
        }
        catch (RuntimeException e) {
            AbstractFuture.log.log(Level.SEVERE, new StringBuilder().append("RuntimeException while executing runnable ").append(runnable).append(" with executor ").append(executor).toString(), (Throwable)e);
        }
    }
    
    private static CancellationException cancellationExceptionWithCause(@Nullable final String message, @Nullable final Throwable cause) {
        final CancellationException exception = new CancellationException(message);
        exception.initCause(cause);
        return exception;
    }
    
    static {
        GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        log = Logger.getLogger(AbstractFuture.class.getName());
        AtomicHelper helper;
        try {
            helper = new UnsafeAtomicHelper();
        }
        catch (Throwable unsafeFailure) {
            try {
                helper = new SafeAtomicHelper((AtomicReferenceFieldUpdater<Waiter, Thread>)AtomicReferenceFieldUpdater.newUpdater((Class)Waiter.class, (Class)Thread.class, "thread"), (AtomicReferenceFieldUpdater<Waiter, Waiter>)AtomicReferenceFieldUpdater.newUpdater((Class)Waiter.class, (Class)Waiter.class, "next"), (AtomicReferenceFieldUpdater<AbstractFuture, Waiter>)AtomicReferenceFieldUpdater.newUpdater((Class)AbstractFuture.class, (Class)Waiter.class, "waiters"), (AtomicReferenceFieldUpdater<AbstractFuture, Listener>)AtomicReferenceFieldUpdater.newUpdater((Class)AbstractFuture.class, (Class)Listener.class, "listeners"), (AtomicReferenceFieldUpdater<AbstractFuture, Object>)AtomicReferenceFieldUpdater.newUpdater((Class)AbstractFuture.class, (Class)Object.class, "value"));
            }
            catch (Throwable atomicReferenceFieldUpdaterFailure) {
                AbstractFuture.log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", unsafeFailure);
                AbstractFuture.log.log(Level.SEVERE, "SafeAtomicHelper is broken!", atomicReferenceFieldUpdaterFailure);
                helper = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = helper;
        NULL = new Object();
    }
    
    abstract static class TrustedFuture<V> extends AbstractFuture<V> {
        @CanIgnoreReturnValue
        @Override
        public final V get() throws InterruptedException, ExecutionException {
            return super.get();
        }
        
        @CanIgnoreReturnValue
        @Override
        public final V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            return super.get(timeout, unit);
        }
        
        @Override
        public final boolean isDone() {
            return super.isDone();
        }
        
        @Override
        public final boolean isCancelled() {
            return super.isCancelled();
        }
        
        @Override
        public final void addListener(final Runnable listener, final Executor executor) {
            super.addListener(listener, executor);
        }
        
        @CanIgnoreReturnValue
        @Override
        public final boolean cancel(final boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
    }
    
    private static final class Waiter {
        static final Waiter TOMBSTONE;
        @Nullable
        volatile Thread thread;
        @Nullable
        volatile Waiter next;
        
        Waiter(final boolean unused) {
        }
        
        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }
        
        void setNext(final Waiter next) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, next);
        }
        
        void unpark() {
            final Thread w = this.thread;
            if (w != null) {
                this.thread = null;
                LockSupport.unpark(w);
            }
        }
        
        static {
            TOMBSTONE = new Waiter(false);
        }
    }
    
    private static final class Listener {
        static final Listener TOMBSTONE;
        final Runnable task;
        final Executor executor;
        @Nullable
        Listener next;
        
        Listener(final Runnable task, final Executor executor) {
            this.task = task;
            this.executor = executor;
        }
        
        static {
            TOMBSTONE = new Listener(null, null);
        }
    }
    
    private static final class Failure {
        static final Failure FALLBACK_INSTANCE;
        final Throwable exception;
        
        Failure(final Throwable exception) {
            this.exception = Preconditions.<Throwable>checkNotNull(exception);
        }
        
        static {
            FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") {
                public synchronized Throwable fillInStackTrace() {
                    return this;
                }
            });
        }
    }
    
    private static final class Cancellation {
        final boolean wasInterrupted;
        @Nullable
        final Throwable cause;
        
        Cancellation(final boolean wasInterrupted, @Nullable final Throwable cause) {
            this.wasInterrupted = wasInterrupted;
            this.cause = cause;
        }
    }
    
    private static final class SetFuture<V> implements Runnable {
        final AbstractFuture<V> owner;
        final ListenableFuture<? extends V> future;
        
        SetFuture(final AbstractFuture<V> owner, final ListenableFuture<? extends V> future) {
            this.owner = owner;
            this.future = future;
        }
        
        public void run() {
            if (((AbstractFuture<Object>)this.owner).value != this) {
                return;
            }
            final Object valueToSet = getFutureValue(this.future);
            if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
                complete(this.owner);
            }
        }
    }
    
    private abstract static class AtomicHelper {
        abstract void putThread(final Waiter waiter, final Thread thread);
        
        abstract void putNext(final Waiter waiter1, final Waiter waiter2);
        
        abstract boolean casWaiters(final AbstractFuture<?> abstractFuture, final Waiter waiter2, final Waiter waiter3);
        
        abstract boolean casListeners(final AbstractFuture<?> abstractFuture, final Listener listener2, final Listener listener3);
        
        abstract boolean casValue(final AbstractFuture<?> abstractFuture, final Object object2, final Object object3);
    }
    
    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final Unsafe UNSAFE;
        static final long LISTENERS_OFFSET;
        static final long WAITERS_OFFSET;
        static final long VALUE_OFFSET;
        static final long WAITER_THREAD_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        
        @Override
        void putThread(final Waiter waiter, final Thread newValue) {
            UnsafeAtomicHelper.UNSAFE.putObject(waiter, UnsafeAtomicHelper.WAITER_THREAD_OFFSET, newValue);
        }
        
        @Override
        void putNext(final Waiter waiter, final Waiter newValue) {
            UnsafeAtomicHelper.UNSAFE.putObject(waiter, UnsafeAtomicHelper.WAITER_NEXT_OFFSET, newValue);
        }
        
        @Override
        boolean casWaiters(final AbstractFuture<?> future, final Waiter expect, final Waiter update) {
            return UnsafeAtomicHelper.UNSAFE.compareAndSwapObject(future, UnsafeAtomicHelper.WAITERS_OFFSET, expect, update);
        }
        
        @Override
        boolean casListeners(final AbstractFuture<?> future, final Listener expect, final Listener update) {
            return UnsafeAtomicHelper.UNSAFE.compareAndSwapObject(future, UnsafeAtomicHelper.LISTENERS_OFFSET, expect, update);
        }
        
        @Override
        boolean casValue(final AbstractFuture<?> future, final Object expect, final Object update) {
            return UnsafeAtomicHelper.UNSAFE.compareAndSwapObject(future, UnsafeAtomicHelper.VALUE_OFFSET, expect, update);
        }
        
        static {
            Unsafe unsafe = null;
            try {
                unsafe = Unsafe.getUnsafe();
            }
            catch (SecurityException tryReflectionInstead) {
                try {
                    unsafe = (Unsafe)AccessController.doPrivileged((PrivilegedExceptionAction)new PrivilegedExceptionAction<Unsafe>() {
                        public Unsafe run() throws Exception {
                            final Class<Unsafe> k = Unsafe.class;
                            for (final Field f : k.getDeclaredFields()) {
                                f.setAccessible(true);
                                final Object x = f.get(null);
                                if (k.isInstance(x)) {
                                    return (Unsafe)k.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                }
                catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            }
            try {
                final Class<?> abstractFuture = AbstractFuture.class;
                WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            }
            catch (Exception e2) {
                Throwables.throwIfUnchecked((Throwable)e2);
                throw new RuntimeException((Throwable)e2);
            }
        }
    }
    
    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        
        SafeAtomicHelper(final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater, final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater, final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater, final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater, final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
            this.waiterThreadUpdater = waiterThreadUpdater;
            this.waiterNextUpdater = waiterNextUpdater;
            this.waitersUpdater = waitersUpdater;
            this.listenersUpdater = listenersUpdater;
            this.valueUpdater = valueUpdater;
        }
        
        @Override
        void putThread(final Waiter waiter, final Thread newValue) {
            this.waiterThreadUpdater.lazySet(waiter, newValue);
        }
        
        @Override
        void putNext(final Waiter waiter, final Waiter newValue) {
            this.waiterNextUpdater.lazySet(waiter, newValue);
        }
        
        @Override
        boolean casWaiters(final AbstractFuture<?> future, final Waiter expect, final Waiter update) {
            return this.waitersUpdater.compareAndSet(future, expect, update);
        }
        
        @Override
        boolean casListeners(final AbstractFuture<?> future, final Listener expect, final Listener update) {
            return this.listenersUpdater.compareAndSet(future, expect, update);
        }
        
        @Override
        boolean casValue(final AbstractFuture<?> future, final Object expect, final Object update) {
            return this.valueUpdater.compareAndSet(future, expect, update);
        }
    }
    
    private static final class SynchronizedHelper extends AtomicHelper {
        @Override
        void putThread(final Waiter waiter, final Thread newValue) {
            waiter.thread = newValue;
        }
        
        @Override
        void putNext(final Waiter waiter, final Waiter newValue) {
            waiter.next = newValue;
        }
        
        @Override
        boolean casWaiters(final AbstractFuture<?> future, final Waiter expect, final Waiter update) {
            synchronized (future) {
                if (((AbstractFuture<Object>)future).waiters == expect) {
                    ((AbstractFuture<Object>)future).waiters = update;
                    return true;
                }
                return false;
            }
        }
        
        @Override
        boolean casListeners(final AbstractFuture<?> future, final Listener expect, final Listener update) {
            synchronized (future) {
                if (((AbstractFuture<Object>)future).listeners == expect) {
                    ((AbstractFuture<Object>)future).listeners = update;
                    return true;
                }
                return false;
            }
        }
        
        @Override
        boolean casValue(final AbstractFuture<?> future, final Object expect, final Object update) {
            synchronized (future) {
                if (((AbstractFuture<Object>)future).value == expect) {
                    ((AbstractFuture<Object>)future).value = update;
                    return true;
                }
                return false;
            }
        }
    }
}
