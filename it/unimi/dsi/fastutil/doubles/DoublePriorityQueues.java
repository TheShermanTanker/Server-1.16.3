package it.unimi.dsi.fastutil.doubles;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class DoublePriorityQueues {
    private DoublePriorityQueues() {
    }
    
    public static DoublePriorityQueue synchronize(final DoublePriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static DoublePriorityQueue synchronize(final DoublePriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements DoublePriorityQueue {
        protected final DoublePriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final DoublePriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final DoublePriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final double x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public double dequeueDouble() {
            synchronized (this.sync) {
                return this.q.dequeueDouble();
            }
        }
        
        public double firstDouble() {
            synchronized (this.sync) {
                return this.q.firstDouble();
            }
        }
        
        public double lastDouble() {
            synchronized (this.sync) {
                return this.q.lastDouble();
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
        
        public DoubleComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Double x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Double dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Double first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Double last() {
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
