package it.unimi.dsi.fastutil.longs;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class LongPriorityQueues {
    private LongPriorityQueues() {
    }
    
    public static LongPriorityQueue synchronize(final LongPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static LongPriorityQueue synchronize(final LongPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements LongPriorityQueue {
        protected final LongPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final LongPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final LongPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final long x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public long dequeueLong() {
            synchronized (this.sync) {
                return this.q.dequeueLong();
            }
        }
        
        public long firstLong() {
            synchronized (this.sync) {
                return this.q.firstLong();
            }
        }
        
        public long lastLong() {
            synchronized (this.sync) {
                return this.q.lastLong();
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
        
        public LongComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Long x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Long dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Long first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Long last() {
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
