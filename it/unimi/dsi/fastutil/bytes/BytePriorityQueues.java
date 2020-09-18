package it.unimi.dsi.fastutil.bytes;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class BytePriorityQueues {
    private BytePriorityQueues() {
    }
    
    public static BytePriorityQueue synchronize(final BytePriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static BytePriorityQueue synchronize(final BytePriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements BytePriorityQueue {
        protected final BytePriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final BytePriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final BytePriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final byte x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public byte dequeueByte() {
            synchronized (this.sync) {
                return this.q.dequeueByte();
            }
        }
        
        public byte firstByte() {
            synchronized (this.sync) {
                return this.q.firstByte();
            }
        }
        
        public byte lastByte() {
            synchronized (this.sync) {
                return this.q.lastByte();
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
        
        public ByteComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Byte x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Byte dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Byte first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Byte last() {
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
