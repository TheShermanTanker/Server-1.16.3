package it.unimi.dsi.fastutil.ints;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class IntPriorityQueues {
    private IntPriorityQueues() {
    }
    
    public static IntPriorityQueue synchronize(final IntPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static IntPriorityQueue synchronize(final IntPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements IntPriorityQueue {
        protected final IntPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final IntPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final IntPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final int x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public int dequeueInt() {
            synchronized (this.sync) {
                return this.q.dequeueInt();
            }
        }
        
        public int firstInt() {
            synchronized (this.sync) {
                return this.q.firstInt();
            }
        }
        
        public int lastInt() {
            synchronized (this.sync) {
                return this.q.lastInt();
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
        
        public IntComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Integer x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Integer dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Integer first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Integer last() {
            synchronized (this.sync) {
                return this.q.last();
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
