package it.unimi.dsi.fastutil.floats;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class FloatPriorityQueues {
    private FloatPriorityQueues() {
    }
    
    public static FloatPriorityQueue synchronize(final FloatPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static FloatPriorityQueue synchronize(final FloatPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements FloatPriorityQueue {
        protected final FloatPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final FloatPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final FloatPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final float x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public float dequeueFloat() {
            synchronized (this.sync) {
                return this.q.dequeueFloat();
            }
        }
        
        public float firstFloat() {
            synchronized (this.sync) {
                return this.q.firstFloat();
            }
        }
        
        public float lastFloat() {
            synchronized (this.sync) {
                return this.q.lastFloat();
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
        
        public FloatComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Float x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Float dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Float first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Float last() {
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
