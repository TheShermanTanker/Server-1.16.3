package it.unimi.dsi.fastutil;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class IndirectPriorityQueues {
    public static final EmptyIndirectPriorityQueue EMPTY_QUEUE;
    
    private IndirectPriorityQueues() {
    }
    
    public static <K> IndirectPriorityQueue<K> synchronize(final IndirectPriorityQueue<K> q) {
        return new SynchronizedIndirectPriorityQueue<K>(q);
    }
    
    public static <K> IndirectPriorityQueue<K> synchronize(final IndirectPriorityQueue<K> q, final Object sync) {
        return new SynchronizedIndirectPriorityQueue<K>(q, sync);
    }
    
    static {
        EMPTY_QUEUE = new EmptyIndirectPriorityQueue();
    }
    
    public static class EmptyIndirectPriorityQueue implements IndirectPriorityQueue {
        protected EmptyIndirectPriorityQueue() {
        }
        
        public void enqueue(final int i) {
            throw new UnsupportedOperationException();
        }
        
        public int dequeue() {
            throw new NoSuchElementException();
        }
        
        public boolean isEmpty() {
            return true;
        }
        
        public int size() {
            return 0;
        }
        
        public boolean contains(final int index) {
            return false;
        }
        
        public void clear() {
        }
        
        public int first() {
            throw new NoSuchElementException();
        }
        
        public int last() {
            throw new NoSuchElementException();
        }
        
        public void changed() {
            throw new NoSuchElementException();
        }
        
        public void allChanged() {
        }
        
        public Comparator<?> comparator() {
            return null;
        }
        
        public void changed(final int i) {
            throw new IllegalArgumentException(new StringBuilder().append("Index ").append(i).append(" is not in the queue").toString());
        }
        
        public boolean remove(final int i) {
            return false;
        }
        
        public int front(final int[] a) {
            return 0;
        }
    }
    
    public static class SynchronizedIndirectPriorityQueue<K> implements IndirectPriorityQueue<K> {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final IndirectPriorityQueue<K> q;
        protected final Object sync;
        
        protected SynchronizedIndirectPriorityQueue(final IndirectPriorityQueue<K> q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedIndirectPriorityQueue(final IndirectPriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final int x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public int dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        public boolean contains(final int index) {
            synchronized (this.sync) {
                return this.q.contains(index);
            }
        }
        
        public int first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        public int last() {
            synchronized (this.sync) {
                return this.q.last();
            }
        }
        
        public boolean isEmpty() {
            synchronized (this.sync) {
                return this.q.isEmpty();
            }
        }
        
        public int size() {
            synchronized (this.sync) {
                return this.q.size();
            }
        }
        
        public void clear() {
            synchronized (this.sync) {
                this.q.clear();
            }
        }
        
        public void changed() {
            synchronized (this.sync) {
                this.q.changed();
            }
        }
        
        public void allChanged() {
            synchronized (this.sync) {
                this.q.allChanged();
            }
        }
        
        public void changed(final int i) {
            synchronized (this.sync) {
                this.q.changed(i);
            }
        }
        
        public boolean remove(final int i) {
            synchronized (this.sync) {
                return this.q.remove(i);
            }
        }
        
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        public int front(final int[] a) {
            return this.q.front(a);
        }
    }
}
