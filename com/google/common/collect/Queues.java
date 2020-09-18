package com.google.common.collect;

import java.util.Deque;
import java.util.Queue;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Collection;
import java.util.ArrayDeque;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.ArrayBlockingQueue;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Queues {
    private Queues() {
    }
    
    @GwtIncompatible
    public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(final int capacity) {
        return (ArrayBlockingQueue<E>)new ArrayBlockingQueue(capacity);
    }
    
    public static <E> ArrayDeque<E> newArrayDeque() {
        return (ArrayDeque<E>)new ArrayDeque();
    }
    
    public static <E> ArrayDeque<E> newArrayDeque(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (ArrayDeque<E>)new ArrayDeque((Collection)Collections2.cast(elements));
        }
        final ArrayDeque<E> deque = (ArrayDeque<E>)new ArrayDeque();
        Iterables.addAll((java.util.Collection<Object>)deque, elements);
        return deque;
    }
    
    @GwtIncompatible
    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
        return (ConcurrentLinkedQueue<E>)new ConcurrentLinkedQueue();
    }
    
    @GwtIncompatible
    public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (ConcurrentLinkedQueue<E>)new ConcurrentLinkedQueue((Collection)Collections2.cast(elements));
        }
        final ConcurrentLinkedQueue<E> queue = (ConcurrentLinkedQueue<E>)new ConcurrentLinkedQueue();
        Iterables.addAll((java.util.Collection<Object>)queue, elements);
        return queue;
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
        return (LinkedBlockingDeque<E>)new LinkedBlockingDeque();
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(final int capacity) {
        return (LinkedBlockingDeque<E>)new LinkedBlockingDeque(capacity);
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (LinkedBlockingDeque<E>)new LinkedBlockingDeque((Collection)Collections2.cast(elements));
        }
        final LinkedBlockingDeque<E> deque = (LinkedBlockingDeque<E>)new LinkedBlockingDeque();
        Iterables.addAll((java.util.Collection<Object>)deque, elements);
        return deque;
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
        return (LinkedBlockingQueue<E>)new LinkedBlockingQueue();
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(final int capacity) {
        return (LinkedBlockingQueue<E>)new LinkedBlockingQueue(capacity);
    }
    
    @GwtIncompatible
    public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (LinkedBlockingQueue<E>)new LinkedBlockingQueue((Collection)Collections2.cast(elements));
        }
        final LinkedBlockingQueue<E> queue = (LinkedBlockingQueue<E>)new LinkedBlockingQueue();
        Iterables.addAll((java.util.Collection<Object>)queue, elements);
        return queue;
    }
    
    @GwtIncompatible
    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
        return (PriorityBlockingQueue<E>)new PriorityBlockingQueue();
    }
    
    @GwtIncompatible
    public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (PriorityBlockingQueue<E>)new PriorityBlockingQueue((Collection)Collections2.cast(elements));
        }
        final PriorityBlockingQueue<E> queue = (PriorityBlockingQueue<E>)new PriorityBlockingQueue();
        Iterables.addAll((java.util.Collection<Object>)queue, elements);
        return queue;
    }
    
    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
        return (PriorityQueue<E>)new PriorityQueue();
    }
    
    public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(final Iterable<? extends E> elements) {
        if (elements instanceof Collection) {
            return (PriorityQueue<E>)new PriorityQueue((Collection)Collections2.cast(elements));
        }
        final PriorityQueue<E> queue = (PriorityQueue<E>)new PriorityQueue();
        Iterables.addAll((java.util.Collection<Object>)queue, elements);
        return queue;
    }
    
    @GwtIncompatible
    public static <E> SynchronousQueue<E> newSynchronousQueue() {
        return (SynchronousQueue<E>)new SynchronousQueue();
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <E> int drain(final BlockingQueue<E> q, final Collection<? super E> buffer, final int numElements, final long timeout, final TimeUnit unit) throws InterruptedException {
        Preconditions.<Collection<? super E>>checkNotNull(buffer);
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        int added;
        for (added = 0; added < numElements; ++added) {
            added += q.drainTo((Collection)buffer, numElements - added);
            if (added < numElements) {
                final E e = (E)q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                if (e == null) {
                    break;
                }
                buffer.add(e);
            }
        }
        return added;
    }
    
    @Beta
    @CanIgnoreReturnValue
    @GwtIncompatible
    public static <E> int drainUninterruptibly(final BlockingQueue<E> q, final Collection<? super E> buffer, final int numElements, final long timeout, final TimeUnit unit) {
        Preconditions.<Collection<? super E>>checkNotNull(buffer);
        final long deadline = System.nanoTime() + unit.toNanos(timeout);
        int added = 0;
        boolean interrupted = false;
        try {
            while (added < numElements) {
                added += q.drainTo((Collection)buffer, numElements - added);
                if (added < numElements) {
                    E e;
                    while (true) {
                        try {
                            e = (E)q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
                        }
                        catch (InterruptedException ex) {
                            interrupted = true;
                            continue;
                        }
                        break;
                    }
                    if (e == null) {
                        break;
                    }
                    buffer.add(e);
                    ++added;
                }
            }
        }
        finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
        return added;
    }
    
    public static <E> Queue<E> synchronizedQueue(final Queue<E> queue) {
        return Synchronized.<E>queue(queue, null);
    }
    
    public static <E> Deque<E> synchronizedDeque(final Deque<E> deque) {
        return Synchronized.<E>deque(deque, null);
    }
}
