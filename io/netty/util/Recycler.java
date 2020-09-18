package io.netty.util;

import java.util.Arrays;
import io.netty.util.internal.ObjectCleaner;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.MathUtil;
import java.util.Map;
import io.netty.util.concurrent.FastThreadLocal;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.util.internal.logging.InternalLogger;

public abstract class Recycler<T> {
    private static final InternalLogger logger;
    private static final Handle NOOP_HANDLE;
    private static final AtomicInteger ID_GENERATOR;
    private static final int OWN_THREAD_ID;
    private static final int DEFAULT_INITIAL_MAX_CAPACITY_PER_THREAD = 4096;
    private static final int DEFAULT_MAX_CAPACITY_PER_THREAD;
    private static final int INITIAL_CAPACITY;
    private static final int MAX_SHARED_CAPACITY_FACTOR;
    private static final int MAX_DELAYED_QUEUES_PER_THREAD;
    private static final int LINK_CAPACITY;
    private static final int RATIO;
    private final int maxCapacityPerThread;
    private final int maxSharedCapacityFactor;
    private final int ratioMask;
    private final int maxDelayedQueuesPerThread;
    private final FastThreadLocal<Stack<T>> threadLocal;
    private static final FastThreadLocal<Map<Stack<?>, WeakOrderQueue>> DELAYED_RECYCLED;
    
    protected Recycler() {
        this(Recycler.DEFAULT_MAX_CAPACITY_PER_THREAD);
    }
    
    protected Recycler(final int maxCapacityPerThread) {
        this(maxCapacityPerThread, Recycler.MAX_SHARED_CAPACITY_FACTOR);
    }
    
    protected Recycler(final int maxCapacityPerThread, final int maxSharedCapacityFactor) {
        this(maxCapacityPerThread, maxSharedCapacityFactor, Recycler.RATIO, Recycler.MAX_DELAYED_QUEUES_PER_THREAD);
    }
    
    protected Recycler(final int maxCapacityPerThread, final int maxSharedCapacityFactor, final int ratio, final int maxDelayedQueuesPerThread) {
        this.threadLocal = new FastThreadLocal<Stack<T>>() {
            @Override
            protected Stack<T> initialValue() {
                return new Stack<T>(Recycler.this, Thread.currentThread(), Recycler.this.maxCapacityPerThread, Recycler.this.maxSharedCapacityFactor, Recycler.this.ratioMask, Recycler.this.maxDelayedQueuesPerThread);
            }
            
            @Override
            protected void onRemoval(final Stack<T> value) {
                if (value.threadRef.get() == Thread.currentThread() && Recycler.DELAYED_RECYCLED.isSet()) {
                    Recycler.DELAYED_RECYCLED.get().remove(value);
                }
            }
        };
        this.ratioMask = MathUtil.safeFindNextPositivePowerOfTwo(ratio) - 1;
        if (maxCapacityPerThread <= 0) {
            this.maxCapacityPerThread = 0;
            this.maxSharedCapacityFactor = 1;
            this.maxDelayedQueuesPerThread = 0;
        }
        else {
            this.maxCapacityPerThread = maxCapacityPerThread;
            this.maxSharedCapacityFactor = Math.max(1, maxSharedCapacityFactor);
            this.maxDelayedQueuesPerThread = Math.max(0, maxDelayedQueuesPerThread);
        }
    }
    
    public final T get() {
        if (this.maxCapacityPerThread == 0) {
            return this.newObject(Recycler.NOOP_HANDLE);
        }
        final Stack<T> stack = this.threadLocal.get();
        DefaultHandle<T> handle = stack.pop();
        if (handle == null) {
            handle = stack.newHandle();
            ((DefaultHandle<Object>)handle).value = this.newObject((Handle<Object>)handle);
        }
        return (T)((DefaultHandle<Object>)handle).value;
    }
    
    @Deprecated
    public final boolean recycle(final T o, final Handle<T> handle) {
        if (handle == Recycler.NOOP_HANDLE) {
            return false;
        }
        final DefaultHandle<T> h = (DefaultHandle<T>)(DefaultHandle)handle;
        if (((DefaultHandle<Object>)h).stack.parent != this) {
            return false;
        }
        h.recycle(o);
        return true;
    }
    
    final int threadLocalCapacity() {
        return ((Stack<Object>)this.threadLocal.get()).elements.length;
    }
    
    final int threadLocalSize() {
        return ((Stack<Object>)this.threadLocal.get()).size;
    }
    
    protected abstract T newObject(final Handle<T> handle);
    
    static {
        logger = InternalLoggerFactory.getInstance(Recycler.class);
        NOOP_HANDLE = new Handle() {
            public void recycle(final Object object) {
            }
        };
        ID_GENERATOR = new AtomicInteger(Integer.MIN_VALUE);
        OWN_THREAD_ID = Recycler.ID_GENERATOR.getAndIncrement();
        int maxCapacityPerThread = SystemPropertyUtil.getInt("io.netty.recycler.maxCapacityPerThread", SystemPropertyUtil.getInt("io.netty.recycler.maxCapacity", 4096));
        if (maxCapacityPerThread < 0) {
            maxCapacityPerThread = 4096;
        }
        DEFAULT_MAX_CAPACITY_PER_THREAD = maxCapacityPerThread;
        MAX_SHARED_CAPACITY_FACTOR = Math.max(2, SystemPropertyUtil.getInt("io.netty.recycler.maxSharedCapacityFactor", 2));
        MAX_DELAYED_QUEUES_PER_THREAD = Math.max(0, SystemPropertyUtil.getInt("io.netty.recycler.maxDelayedQueuesPerThread", NettyRuntime.availableProcessors() * 2));
        LINK_CAPACITY = MathUtil.safeFindNextPositivePowerOfTwo(Math.max(SystemPropertyUtil.getInt("io.netty.recycler.linkCapacity", 16), 16));
        RATIO = MathUtil.safeFindNextPositivePowerOfTwo(SystemPropertyUtil.getInt("io.netty.recycler.ratio", 8));
        if (Recycler.logger.isDebugEnabled()) {
            if (Recycler.DEFAULT_MAX_CAPACITY_PER_THREAD == 0) {
                Recycler.logger.debug("-Dio.netty.recycler.maxCapacityPerThread: disabled");
                Recycler.logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: disabled");
                Recycler.logger.debug("-Dio.netty.recycler.linkCapacity: disabled");
                Recycler.logger.debug("-Dio.netty.recycler.ratio: disabled");
            }
            else {
                Recycler.logger.debug("-Dio.netty.recycler.maxCapacityPerThread: {}", Recycler.DEFAULT_MAX_CAPACITY_PER_THREAD);
                Recycler.logger.debug("-Dio.netty.recycler.maxSharedCapacityFactor: {}", Recycler.MAX_SHARED_CAPACITY_FACTOR);
                Recycler.logger.debug("-Dio.netty.recycler.linkCapacity: {}", Recycler.LINK_CAPACITY);
                Recycler.logger.debug("-Dio.netty.recycler.ratio: {}", Recycler.RATIO);
            }
        }
        INITIAL_CAPACITY = Math.min(Recycler.DEFAULT_MAX_CAPACITY_PER_THREAD, 256);
        DELAYED_RECYCLED = new FastThreadLocal<Map<Stack<?>, WeakOrderQueue>>() {
            @Override
            protected Map<Stack<?>, WeakOrderQueue> initialValue() {
                return (Map<Stack<?>, WeakOrderQueue>)new WeakHashMap();
            }
        };
    }
    
    static final class DefaultHandle<T> implements Handle<T> {
        private int lastRecycledId;
        private int recycleId;
        boolean hasBeenRecycled;
        private Stack<?> stack;
        private Object value;
        
        DefaultHandle(final Stack<?> stack) {
            this.stack = stack;
        }
        
        public void recycle(final Object object) {
            if (object != this.value) {
                throw new IllegalArgumentException("object does not belong to handle");
            }
            this.stack.push(this);
        }
    }
    
    private static final class WeakOrderQueue {
        static final WeakOrderQueue DUMMY;
        private final Head head;
        private Link tail;
        private WeakOrderQueue next;
        private final WeakReference<Thread> owner;
        private final int id;
        
        private WeakOrderQueue() {
            this.id = Recycler.ID_GENERATOR.getAndIncrement();
            this.owner = null;
            this.head = new Head(null);
        }
        
        private WeakOrderQueue(final Stack<?> stack, final Thread thread) {
            this.id = Recycler.ID_GENERATOR.getAndIncrement();
            this.tail = new Link();
            this.head = new Head(stack.availableSharedCapacity);
            this.head.link = this.tail;
            this.owner = (WeakReference<Thread>)new WeakReference(thread);
        }
        
        static WeakOrderQueue newQueue(final Stack<?> stack, final Thread thread) {
            final WeakOrderQueue queue = new WeakOrderQueue(stack, thread);
            stack.setHead(queue);
            final Head head = queue.head;
            ObjectCleaner.register(queue, (Runnable)head);
            return queue;
        }
        
        private void setNext(final WeakOrderQueue next) {
            assert next != this;
            this.next = next;
        }
        
        static WeakOrderQueue allocate(final Stack<?> stack, final Thread thread) {
            return Head.reserveSpace(stack.availableSharedCapacity, Recycler.LINK_CAPACITY) ? newQueue(stack, thread) : null;
        }
        
        void add(final DefaultHandle<?> handle) {
            ((DefaultHandle<Object>)handle).lastRecycledId = this.id;
            Link tail = this.tail;
            int writeIndex;
            if ((writeIndex = tail.get()) == Recycler.LINK_CAPACITY) {
                if (!this.head.reserveSpace(Recycler.LINK_CAPACITY)) {
                    return;
                }
                final Link link = tail;
                final Link link2 = new Link();
                link.next = link2;
                tail = link2;
                this.tail = link2;
                writeIndex = tail.get();
            }
            (tail.elements[writeIndex] = handle).stack = null;
            tail.lazySet(writeIndex + 1);
        }
        
        boolean hasFinalData() {
            return this.tail.readIndex != this.tail.get();
        }
        
        boolean transfer(final Stack<?> dst) {
            Link head = this.head.link;
            if (head == null) {
                return false;
            }
            if (head.readIndex == Recycler.LINK_CAPACITY) {
                if (head.next == null) {
                    return false;
                }
                head = (this.head.link = head.next);
            }
            final int srcStart = head.readIndex;
            int srcEnd = head.get();
            final int srcSize = srcEnd - srcStart;
            if (srcSize == 0) {
                return false;
            }
            final int dstSize = ((Stack<Object>)dst).size;
            final int expectedCapacity = dstSize + srcSize;
            if (expectedCapacity > ((Stack<Object>)dst).elements.length) {
                final int actualCapacity = dst.increaseCapacity(expectedCapacity);
                srcEnd = Math.min(srcStart + actualCapacity - dstSize, srcEnd);
            }
            if (srcStart == srcEnd) {
                return false;
            }
            final DefaultHandle[] srcElems = head.elements;
            final DefaultHandle[] dstElems = ((Stack<Object>)dst).elements;
            int newDstSize = dstSize;
            for (int i = srcStart; i < srcEnd; ++i) {
                final DefaultHandle element = srcElems[i];
                if (element.recycleId == 0) {
                    element.recycleId = element.lastRecycledId;
                }
                else if (element.recycleId != element.lastRecycledId) {
                    throw new IllegalStateException("recycled already");
                }
                srcElems[i] = null;
                if (!dst.dropHandle(element)) {
                    element.stack = dst;
                    dstElems[newDstSize++] = element;
                }
            }
            if (srcEnd == Recycler.LINK_CAPACITY && head.next != null) {
                this.head.reclaimSpace(Recycler.LINK_CAPACITY);
                this.head.link = head.next;
            }
            head.readIndex = srcEnd;
            if (((Stack<Object>)dst).size == newDstSize) {
                return false;
            }
            ((Stack<Object>)dst).size = newDstSize;
            return true;
        }
        
        static {
            DUMMY = new WeakOrderQueue();
        }
        
        static final class Link extends AtomicInteger {
            private final DefaultHandle<?>[] elements;
            private int readIndex;
            Link next;
            
            Link() {
                this.elements = new DefaultHandle[Recycler.LINK_CAPACITY];
            }
        }
        
        static final class Head implements Runnable {
            private final AtomicInteger availableSharedCapacity;
            Link link;
            
            Head(final AtomicInteger availableSharedCapacity) {
                this.availableSharedCapacity = availableSharedCapacity;
            }
            
            public void run() {
                for (Link head = this.link; head != null; head = head.next) {
                    this.reclaimSpace(Recycler.LINK_CAPACITY);
                }
            }
            
            void reclaimSpace(final int space) {
                assert space >= 0;
                this.availableSharedCapacity.addAndGet(space);
            }
            
            boolean reserveSpace(final int space) {
                return reserveSpace(this.availableSharedCapacity, space);
            }
            
            static boolean reserveSpace(final AtomicInteger availableSharedCapacity, final int space) {
                assert space >= 0;
                while (true) {
                    final int available = availableSharedCapacity.get();
                    if (available < space) {
                        return false;
                    }
                    if (availableSharedCapacity.compareAndSet(available, available - space)) {
                        return true;
                    }
                }
            }
        }
    }
    
    static final class Stack<T> {
        final Recycler<T> parent;
        final WeakReference<Thread> threadRef;
        final AtomicInteger availableSharedCapacity;
        final int maxDelayedQueues;
        private final int maxCapacity;
        private final int ratioMask;
        private DefaultHandle<?>[] elements;
        private int size;
        private int handleRecycleCount;
        private WeakOrderQueue cursor;
        private WeakOrderQueue prev;
        private volatile WeakOrderQueue head;
        
        Stack(final Recycler<T> parent, final Thread thread, final int maxCapacity, final int maxSharedCapacityFactor, final int ratioMask, final int maxDelayedQueues) {
            this.handleRecycleCount = -1;
            this.parent = parent;
            this.threadRef = (WeakReference<Thread>)new WeakReference(thread);
            this.maxCapacity = maxCapacity;
            this.availableSharedCapacity = new AtomicInteger(Math.max(maxCapacity / maxSharedCapacityFactor, Recycler.LINK_CAPACITY));
            this.elements = new DefaultHandle[Math.min(Recycler.INITIAL_CAPACITY, maxCapacity)];
            this.ratioMask = ratioMask;
            this.maxDelayedQueues = maxDelayedQueues;
        }
        
        synchronized void setHead(final WeakOrderQueue queue) {
            queue.setNext(this.head);
            this.head = queue;
        }
        
        int increaseCapacity(final int expectedCapacity) {
            int newCapacity = this.elements.length;
            final int maxCapacity = this.maxCapacity;
            do {
                newCapacity <<= 1;
            } while (newCapacity < expectedCapacity && newCapacity < maxCapacity);
            newCapacity = Math.min(newCapacity, maxCapacity);
            if (newCapacity != this.elements.length) {
                this.elements = Arrays.copyOf((Object[])this.elements, newCapacity);
            }
            return newCapacity;
        }
        
        DefaultHandle<T> pop() {
            int size = this.size;
            if (size == 0) {
                if (!this.scavenge()) {
                    return null;
                }
                size = this.size;
            }
            --size;
            final DefaultHandle ret = this.elements[size];
            this.elements[size] = null;
            if (ret.lastRecycledId != ret.recycleId) {
                throw new IllegalStateException("recycled multiple times");
            }
            ret.recycleId = 0;
            ret.lastRecycledId = 0;
            this.size = size;
            return (DefaultHandle<T>)ret;
        }
        
        boolean scavenge() {
            if (this.scavengeSome()) {
                return true;
            }
            this.prev = null;
            this.cursor = this.head;
            return false;
        }
        
        boolean scavengeSome() {
            WeakOrderQueue cursor = this.cursor;
            WeakOrderQueue prev;
            if (cursor == null) {
                prev = null;
                cursor = this.head;
                if (cursor == null) {
                    return false;
                }
            }
            else {
                prev = this.prev;
            }
            boolean success = false;
            while (true) {
                while (!cursor.transfer(this)) {
                    final WeakOrderQueue next = cursor.next;
                    if (cursor.owner.get() == null) {
                        if (cursor.hasFinalData()) {
                            while (cursor.transfer(this)) {
                                success = true;
                            }
                        }
                        if (prev != null) {
                            prev.setNext(next);
                        }
                    }
                    else {
                        prev = cursor;
                    }
                    cursor = next;
                    if (cursor == null || success) {
                        this.prev = prev;
                        this.cursor = cursor;
                        return success;
                    }
                }
                success = true;
                continue;
            }
        }
        
        void push(final DefaultHandle<?> item) {
            final Thread currentThread = Thread.currentThread();
            if (this.threadRef.get() == currentThread) {
                this.pushNow(item);
            }
            else {
                this.pushLater(item, currentThread);
            }
        }
        
        private void pushNow(final DefaultHandle<?> item) {
            if ((((DefaultHandle<Object>)item).recycleId | ((DefaultHandle<Object>)item).lastRecycledId) != 0x0) {
                throw new IllegalStateException("recycled already");
            }
            ((DefaultHandle<Object>)item).recycleId = (((DefaultHandle<Object>)item).lastRecycledId = Recycler.OWN_THREAD_ID);
            final int size = this.size;
            if (size >= this.maxCapacity || this.dropHandle(item)) {
                return;
            }
            if (size == this.elements.length) {
                this.elements = Arrays.copyOf((Object[])this.elements, Math.min(size << 1, this.maxCapacity));
            }
            this.elements[size] = item;
            this.size = size + 1;
        }
        
        private void pushLater(final DefaultHandle<?> item, final Thread thread) {
            final Map<Stack<?>, WeakOrderQueue> delayedRecycled = (Map<Stack<?>, WeakOrderQueue>)Recycler.DELAYED_RECYCLED.get();
            WeakOrderQueue queue = (WeakOrderQueue)delayedRecycled.get(this);
            if (queue == null) {
                if (delayedRecycled.size() >= this.maxDelayedQueues) {
                    delayedRecycled.put(this, WeakOrderQueue.DUMMY);
                    return;
                }
                if ((queue = WeakOrderQueue.allocate(this, thread)) == null) {
                    return;
                }
                delayedRecycled.put(this, queue);
            }
            else if (queue == WeakOrderQueue.DUMMY) {
                return;
            }
            queue.add(item);
        }
        
        boolean dropHandle(final DefaultHandle<?> handle) {
            if (!handle.hasBeenRecycled) {
                if ((++this.handleRecycleCount & this.ratioMask) != 0x0) {
                    return true;
                }
                handle.hasBeenRecycled = true;
            }
            return false;
        }
        
        DefaultHandle<T> newHandle() {
            return new DefaultHandle<T>(this);
        }
    }
    
    static final class Stack<T> {
        final Recycler<T> parent;
        final WeakReference<Thread> threadRef;
        final AtomicInteger availableSharedCapacity;
        final int maxDelayedQueues;
        private final int maxCapacity;
        private final int ratioMask;
        private DefaultHandle<?>[] elements;
        private int size;
        private int handleRecycleCount;
        private WeakOrderQueue cursor;
        private WeakOrderQueue prev;
        private volatile WeakOrderQueue head;
        
        Stack(final Recycler<T> parent, final Thread thread, final int maxCapacity, final int maxSharedCapacityFactor, final int ratioMask, final int maxDelayedQueues) {
            this.handleRecycleCount = -1;
            this.parent = parent;
            this.threadRef = (WeakReference<Thread>)new WeakReference(thread);
            this.maxCapacity = maxCapacity;
            this.availableSharedCapacity = new AtomicInteger(Math.max(maxCapacity / maxSharedCapacityFactor, Recycler.LINK_CAPACITY));
            this.elements = new DefaultHandle[Math.min(Recycler.INITIAL_CAPACITY, maxCapacity)];
            this.ratioMask = ratioMask;
            this.maxDelayedQueues = maxDelayedQueues;
        }
        
        synchronized void setHead(final WeakOrderQueue queue) {
            queue.setNext(this.head);
            this.head = queue;
        }
        
        int increaseCapacity(final int expectedCapacity) {
            int newCapacity = this.elements.length;
            final int maxCapacity = this.maxCapacity;
            do {
                newCapacity <<= 1;
            } while (newCapacity < expectedCapacity && newCapacity < maxCapacity);
            newCapacity = Math.min(newCapacity, maxCapacity);
            if (newCapacity != this.elements.length) {
                this.elements = Arrays.copyOf((Object[])this.elements, newCapacity);
            }
            return newCapacity;
        }
        
        DefaultHandle<T> pop() {
            int size = this.size;
            if (size == 0) {
                if (!this.scavenge()) {
                    return null;
                }
                size = this.size;
            }
            --size;
            final DefaultHandle ret = this.elements[size];
            this.elements[size] = null;
            if (ret.lastRecycledId != ret.recycleId) {
                throw new IllegalStateException("recycled multiple times");
            }
            ret.recycleId = 0;
            ret.lastRecycledId = 0;
            this.size = size;
            return (DefaultHandle<T>)ret;
        }
        
        boolean scavenge() {
            if (this.scavengeSome()) {
                return true;
            }
            this.prev = null;
            this.cursor = this.head;
            return false;
        }
        
        boolean scavengeSome() {
            WeakOrderQueue cursor = this.cursor;
            WeakOrderQueue prev;
            if (cursor == null) {
                prev = null;
                cursor = this.head;
                if (cursor == null) {
                    return false;
                }
            }
            else {
                prev = this.prev;
            }
            boolean success = false;
            while (true) {
                while (!cursor.transfer(this)) {
                    final WeakOrderQueue next = cursor.next;
                    if (cursor.owner.get() == null) {
                        if (cursor.hasFinalData()) {
                            while (cursor.transfer(this)) {
                                success = true;
                            }
                        }
                        if (prev != null) {
                            prev.setNext(next);
                        }
                    }
                    else {
                        prev = cursor;
                    }
                    cursor = next;
                    if (cursor == null || success) {
                        this.prev = prev;
                        this.cursor = cursor;
                        return success;
                    }
                }
                success = true;
                continue;
            }
        }
        
        void push(final DefaultHandle<?> item) {
            final Thread currentThread = Thread.currentThread();
            if (this.threadRef.get() == currentThread) {
                this.pushNow(item);
            }
            else {
                this.pushLater(item, currentThread);
            }
        }
        
        private void pushNow(final DefaultHandle<?> item) {
            if ((((DefaultHandle<Object>)item).recycleId | ((DefaultHandle<Object>)item).lastRecycledId) != 0x0) {
                throw new IllegalStateException("recycled already");
            }
            ((DefaultHandle<Object>)item).recycleId = (((DefaultHandle<Object>)item).lastRecycledId = Recycler.OWN_THREAD_ID);
            final int size = this.size;
            if (size >= this.maxCapacity || this.dropHandle(item)) {
                return;
            }
            if (size == this.elements.length) {
                this.elements = Arrays.copyOf((Object[])this.elements, Math.min(size << 1, this.maxCapacity));
            }
            this.elements[size] = item;
            this.size = size + 1;
        }
        
        private void pushLater(final DefaultHandle<?> item, final Thread thread) {
            final Map<Stack<?>, WeakOrderQueue> delayedRecycled = (Map<Stack<?>, WeakOrderQueue>)Recycler.DELAYED_RECYCLED.get();
            WeakOrderQueue queue = (WeakOrderQueue)delayedRecycled.get(this);
            if (queue == null) {
                if (delayedRecycled.size() >= this.maxDelayedQueues) {
                    delayedRecycled.put(this, WeakOrderQueue.DUMMY);
                    return;
                }
                if ((queue = WeakOrderQueue.allocate(this, thread)) == null) {
                    return;
                }
                delayedRecycled.put(this, queue);
            }
            else if (queue == WeakOrderQueue.DUMMY) {
                return;
            }
            queue.add(item);
        }
        
        boolean dropHandle(final DefaultHandle<?> handle) {
            if (!handle.hasBeenRecycled) {
                if ((++this.handleRecycleCount & this.ratioMask) != 0x0) {
                    return true;
                }
                handle.hasBeenRecycled = true;
            }
            return false;
        }
        
        DefaultHandle<T> newHandle() {
            return new DefaultHandle<T>(this);
        }
    }
    
    public interface Handle<T> {
        void recycle(final T object);
    }
}
