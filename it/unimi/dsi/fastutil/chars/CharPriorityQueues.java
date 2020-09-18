package it.unimi.dsi.fastutil.chars;

import java.util.Comparator;
import java.io.IOException;
import java.io.ObjectOutputStream;

public final class CharPriorityQueues {
    private CharPriorityQueues() {
    }
    
    public static CharPriorityQueue synchronize(final CharPriorityQueue q) {
        return new SynchronizedPriorityQueue(q);
    }
    
    public static CharPriorityQueue synchronize(final CharPriorityQueue q, final Object sync) {
        return new SynchronizedPriorityQueue(q, sync);
    }
    
    public static class SynchronizedPriorityQueue implements CharPriorityQueue {
        protected final CharPriorityQueue q;
        protected final Object sync;
        
        protected SynchronizedPriorityQueue(final CharPriorityQueue q, final Object sync) {
            this.q = q;
            this.sync = sync;
        }
        
        protected SynchronizedPriorityQueue(final CharPriorityQueue q) {
            this.q = q;
            this.sync = this;
        }
        
        public void enqueue(final char x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        public char dequeueChar() {
            synchronized (this.sync) {
                return this.q.dequeueChar();
            }
        }
        
        public char firstChar() {
            synchronized (this.sync) {
                return this.q.firstChar();
            }
        }
        
        public char lastChar() {
            synchronized (this.sync) {
                return this.q.lastChar();
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
        
        public CharComparator comparator() {
            synchronized (this.sync) {
                return this.q.comparator();
            }
        }
        
        @Deprecated
        public void enqueue(final Character x) {
            synchronized (this.sync) {
                this.q.enqueue(x);
            }
        }
        
        @Deprecated
        public Character dequeue() {
            synchronized (this.sync) {
                return this.q.dequeue();
            }
        }
        
        @Deprecated
        public Character first() {
            synchronized (this.sync) {
                return this.q.first();
            }
        }
        
        @Deprecated
        public Character last() {
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
