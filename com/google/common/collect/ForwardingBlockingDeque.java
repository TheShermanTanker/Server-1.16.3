package com.google.common.collect;

import java.util.Queue;
import java.util.Deque;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import com.google.common.annotations.GwtIncompatible;
import java.util.concurrent.BlockingDeque;

@Deprecated
@GwtIncompatible
public abstract class ForwardingBlockingDeque<E> extends ForwardingDeque<E> implements BlockingDeque<E> {
    protected ForwardingBlockingDeque() {
    }
    
    protected abstract BlockingDeque<E> delegate();
    
    public int remainingCapacity() {
        return this.delegate().remainingCapacity();
    }
    
    public void putFirst(final E e) throws InterruptedException {
        this.delegate().putFirst(e);
    }
    
    public void putLast(final E e) throws InterruptedException {
        this.delegate().putLast(e);
    }
    
    public boolean offerFirst(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offerFirst(e, timeout, unit);
    }
    
    public boolean offerLast(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offerLast(e, timeout, unit);
    }
    
    public E takeFirst() throws InterruptedException {
        return (E)this.delegate().takeFirst();
    }
    
    public E takeLast() throws InterruptedException {
        return (E)this.delegate().takeLast();
    }
    
    public E pollFirst(final long timeout, final TimeUnit unit) throws InterruptedException {
        return (E)this.delegate().pollFirst(timeout, unit);
    }
    
    public E pollLast(final long timeout, final TimeUnit unit) throws InterruptedException {
        return (E)this.delegate().pollLast(timeout, unit);
    }
    
    public void put(final E e) throws InterruptedException {
        this.delegate().put(e);
    }
    
    public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.delegate().offer(e, timeout, unit);
    }
    
    public E take() throws InterruptedException {
        return (E)this.delegate().take();
    }
    
    public E poll(final long timeout, final TimeUnit unit) throws InterruptedException {
        return (E)this.delegate().poll(timeout, unit);
    }
    
    public int drainTo(final Collection<? super E> c) {
        return this.delegate().drainTo((Collection)c);
    }
    
    public int drainTo(final Collection<? super E> c, final int maxElements) {
        return this.delegate().drainTo((Collection)c, maxElements);
    }
}
