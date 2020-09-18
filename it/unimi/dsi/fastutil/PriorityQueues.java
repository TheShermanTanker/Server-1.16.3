package it.unimi.dsi.fastutil;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class PriorityQueues {
    public static final EmptyPriorityQueue EMPTY_QUEUE;
    
    private PriorityQueues() {
    }
    
    public static <K> PriorityQueue<K> emptyQueue() {
        return (PriorityQueue<K>)PriorityQueues.EMPTY_QUEUE;
    }
    
    public static <K> PriorityQueue<K> synchronize(final PriorityQueue<K> q) {
        return new SynchronizedPriorityQueue<K>(q);
    }
    
    public static <K> PriorityQueue<K> synchronize(final PriorityQueue<K> q, final Object sync) {
        return new SynchronizedPriorityQueue<K>(q, sync);
    }
    
    static {
        EMPTY_QUEUE = new EmptyPriorityQueue();
    }
    
    public static class EmptyPriorityQueue implements PriorityQueue, Serializable {
        private static final long serialVersionUID = 0L;
        
        protected EmptyPriorityQueue() {
        }
        
        public void enqueue(final Object o) {
            throw new UnsupportedOperationException();
        }
        
        public Object dequeue() {
            throw new NoSuchElementException();
        }
        
        public boolean isEmpty() {
            return true;
        }
        
        public int size() {
            return 0;
        }
        
        public void clear() {
        }
        
        public Object first() {
            throw new NoSuchElementException();
        }
        
        public Object last() {
            throw new NoSuchElementException();
        }
        
        public void changed() {
            throw new NoSuchElementException();
        }
        
        public Comparator<?> comparator() {
            return null;
        }
        
        public Object clone() {
            return PriorityQueues.EMPTY_QUEUE;
        }
        
        public int hashCode() {
            return 0;
        }
        
        public boolean equals(final Object o) {
            return o instanceof PriorityQueue && ((PriorityQueue)o).isEmpty();
        }
        
        private Object readResolve() {
            return PriorityQueues.EMPTY_QUEUE;
        }
    }
    
    public static class SynchronizedPriorityQueue<K> implements PriorityQueue<K>, Serializable {
        public static final long serialVersionUID = -7046029254386353129L;
        protected final PriorityQueue<K> q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final PriorityQueue<K> q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final PriorityQueue<K> q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final K x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public K dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        public K first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        public K last() {
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
        
        public Comparator<? super K> comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        public String toString() {
            synchronized (this.sync) {
                return this.q.toString();
            }
        }
        
        public int hashCode() {
            synchronized (this.sync) {
                return this.q.hashCode();
            }
        }
        
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            }
            synchronized (this.sync) {
                return this.q.equals(o);
            }
        }
        
        private void writeObject(final ObjectOutputStream s) throws IOException {
            synchronized (this.sync) {
                s.defaultWriteObject();
            }
        }
    }
}
