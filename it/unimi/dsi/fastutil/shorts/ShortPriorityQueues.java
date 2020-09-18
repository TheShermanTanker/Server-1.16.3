package it.unimi.dsi.fastutil.shorts;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class ShortPriorityQueues {
    private ShortPriorityQueues() {
    }
    
    public static ShortPriorityQueue synchronize(final ShortPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static ShortPriorityQueue synchronize(final ShortPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements ShortPriorityQueue {
        protected final ShortPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final ShortPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final ShortPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final short x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public short dequeueShort() {
            synchronized (this.sync) {
                return this.q.dequeueShort();
            }
        }
        
        public short firstShort() {
            synchronized (this.sync) {
                return this.q.firstShort();
            }
        }
        
        public short lastShort() {
            synchronized (this.sync) {
                return this.q.lastShort();
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
        
        public ShortComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Short x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Short dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Short first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Short last() {
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
