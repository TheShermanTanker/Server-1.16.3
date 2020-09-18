package com.google.common.cache;

import java.util.concurrent.Callable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.function.Predicate;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.NoSuchElementException;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.util.concurrent.SettableFuture;
import java.lang.ref.Reference;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.Uninterruptibles;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.Futures;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.GuardedBy;
import com.google.j2objc.annotations.Weak;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import com.google.common.collect.ImmutableSet;
import java.util.AbstractQueue;
import java.lang.ref.ReferenceQueue;
import java.util.function.BiPredicate;
import com.google.common.collect.Iterators;
import java.util.ArrayList;
import com.google.common.annotations.GwtIncompatible;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.ExecutionError;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import java.util.Iterator;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import java.util.concurrent.ExecutionException;
import com.google.common.primitives.Ints;
import java.util.logging.Level;
import com.google.common.base.Preconditions;
import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.base.Ticker;
import java.util.Queue;
import com.google.common.base.Equivalence;
import java.util.logging.Logger;
import com.google.common.annotations.GwtCompatible;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;

@GwtCompatible(emulated = true)
class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MAX_SEGMENTS = 65536;
    static final int CONTAINS_VALUE_RETRIES = 3;
    static final int DRAIN_THRESHOLD = 63;
    static final int DRAIN_MAX = 16;
    static final Logger logger;
    final int segmentMask;
    final int segmentShift;
    final Segment<K, V>[] segments;
    final int concurrencyLevel;
    final Equivalence<Object> keyEquivalence;
    final Equivalence<Object> valueEquivalence;
    final Strength keyStrength;
    final Strength valueStrength;
    final long maxWeight;
    final Weigher<K, V> weigher;
    final long expireAfterAccessNanos;
    final long expireAfterWriteNanos;
    final long refreshNanos;
    final Queue<RemovalNotification<K, V>> removalNotificationQueue;
    final RemovalListener<K, V> removalListener;
    final Ticker ticker;
    final EntryFactory entryFactory;
    final AbstractCache.StatsCounter globalStatsCounter;
    @Nullable
    final CacheLoader<? super K, V> defaultLoader;
    static final ValueReference<Object, Object> UNSET;
    static final Queue<?> DISCARDING_QUEUE;
    Set<K> keySet;
    Collection<V> values;
    Set<Map.Entry<K, V>> entrySet;
    
    LocalCache(final CacheBuilder<? super K, ? super V> builder, @Nullable final CacheLoader<? super K, V> loader) {
        this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
        this.keyStrength = builder.getKeyStrength();
        this.valueStrength = builder.getValueStrength();
        this.keyEquivalence = builder.getKeyEquivalence();
        this.valueEquivalence = builder.getValueEquivalence();
        this.maxWeight = builder.getMaximumWeight();
        this.weigher = builder.<K, V>getWeigher();
        this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
        this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
        this.refreshNanos = builder.getRefreshNanos();
        this.removalListener = builder.<K, V>getRemovalListener();
        this.removalNotificationQueue = (Queue<RemovalNotification<K, V>>)((this.removalListener == CacheBuilder.NullListener.INSTANCE) ? LocalCache.discardingQueue() : new ConcurrentLinkedQueue());
        this.ticker = builder.getTicker(this.recordsTime());
        this.entryFactory = EntryFactory.getFactory(this.keyStrength, this.usesAccessEntries(), this.usesWriteEntries());
        this.globalStatsCounter = (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get();
        this.defaultLoader = loader;
        int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
        if (this.evictsBySize() && !this.customWeigher()) {
            initialCapacity = Math.min(initialCapacity, (int)this.maxWeight);
        }
        int segmentShift = 0;
        int segmentCount;
        for (segmentCount = 1; segmentCount < this.concurrencyLevel && (!this.evictsBySize() || segmentCount * 20 <= this.maxWeight); segmentCount <<= 1) {
            ++segmentShift;
        }
        this.segmentShift = 32 - segmentShift;
        this.segmentMask = segmentCount - 1;
        this.segments = this.newSegmentArray(segmentCount);
        int segmentCapacity = initialCapacity / segmentCount;
        if (segmentCapacity * segmentCount < initialCapacity) {
            ++segmentCapacity;
        }
        int segmentSize;
        for (segmentSize = 1; segmentSize < segmentCapacity; segmentSize <<= 1) {}
        if (this.evictsBySize()) {
            long maxSegmentWeight = this.maxWeight / segmentCount + 1L;
            final long remainder = this.maxWeight % segmentCount;
            for (int i = 0; i < this.segments.length; ++i) {
                if (i == remainder) {
                    --maxSegmentWeight;
                }
                this.segments[i] = this.createSegment(segmentSize, maxSegmentWeight, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
            }
        }
        else {
            for (int j = 0; j < this.segments.length; ++j) {
                this.segments[j] = this.createSegment(segmentSize, -1L, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
            }
        }
    }
    
    boolean evictsBySize() {
        return this.maxWeight >= 0L;
    }
    
    boolean customWeigher() {
        return this.weigher != CacheBuilder.OneWeigher.INSTANCE;
    }
    
    boolean expires() {
        return this.expiresAfterWrite() || this.expiresAfterAccess();
    }
    
    boolean expiresAfterWrite() {
        return this.expireAfterWriteNanos > 0L;
    }
    
    boolean expiresAfterAccess() {
        return this.expireAfterAccessNanos > 0L;
    }
    
    boolean refreshes() {
        return this.refreshNanos > 0L;
    }
    
    boolean usesAccessQueue() {
        return this.expiresAfterAccess() || this.evictsBySize();
    }
    
    boolean usesWriteQueue() {
        return this.expiresAfterWrite();
    }
    
    boolean recordsWrite() {
        return this.expiresAfterWrite() || this.refreshes();
    }
    
    boolean recordsAccess() {
        return this.expiresAfterAccess();
    }
    
    boolean recordsTime() {
        return this.recordsWrite() || this.recordsAccess();
    }
    
    boolean usesWriteEntries() {
        return this.usesWriteQueue() || this.recordsWrite();
    }
    
    boolean usesAccessEntries() {
        return this.usesAccessQueue() || this.recordsAccess();
    }
    
    boolean usesKeyReferences() {
        return this.keyStrength != Strength.STRONG;
    }
    
    boolean usesValueReferences() {
        return this.valueStrength != Strength.STRONG;
    }
    
    static <K, V> ValueReference<K, V> unset() {
        return (ValueReference<K, V>)LocalCache.UNSET;
    }
    
    static <K, V> ReferenceEntry<K, V> nullEntry() {
        return (ReferenceEntry<K, V>)NullEntry.INSTANCE;
    }
    
    static <E> Queue<E> discardingQueue() {
        return (Queue<E>)LocalCache.DISCARDING_QUEUE;
    }
    
    static int rehash(int h) {
        h += (h << 15 ^ 0xFFFFCD7D);
        h ^= h >>> 10;
        h += h << 3;
        h ^= h >>> 6;
        h += (h << 2) + (h << 14);
        return h ^ h >>> 16;
    }
    
    @VisibleForTesting
    ReferenceEntry<K, V> newEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
        final Segment<K, V> segment = this.segmentFor(hash);
        segment.lock();
        try {
            return segment.newEntry(key, hash, next);
        }
        finally {
            segment.unlock();
        }
    }
    
    @VisibleForTesting
    ReferenceEntry<K, V> copyEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
        final int hash = original.getHash();
        return this.segmentFor(hash).copyEntry(original, newNext);
    }
    
    @VisibleForTesting
    ValueReference<K, V> newValueReference(final ReferenceEntry<K, V> entry, final V value, final int weight) {
        final int hash = entry.getHash();
        return this.valueStrength.<K, V>referenceValue(this.segmentFor(hash), entry, (V)Preconditions.<V>checkNotNull((V)value), weight);
    }
    
    int hash(@Nullable final Object key) {
        final int h = this.keyEquivalence.hash(key);
        return rehash(h);
    }
    
    void reclaimValue(final ValueReference<K, V> valueReference) {
        final ReferenceEntry<K, V> entry = valueReference.getEntry();
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
    }
    
    void reclaimKey(final ReferenceEntry<K, V> entry) {
        final int hash = entry.getHash();
        this.segmentFor(hash).reclaimKey(entry, hash);
    }
    
    @VisibleForTesting
    boolean isLive(final ReferenceEntry<K, V> entry, final long now) {
        return this.segmentFor(entry.getHash()).getLiveValue(entry, now) != null;
    }
    
    Segment<K, V> segmentFor(final int hash) {
        return this.segments[hash >>> this.segmentShift & this.segmentMask];
    }
    
    Segment<K, V> createSegment(final int initialCapacity, final long maxSegmentWeight, final AbstractCache.StatsCounter statsCounter) {
        return new Segment<K, V>(this, initialCapacity, maxSegmentWeight, statsCounter);
    }
    
    @Nullable
    V getLiveValue(final ReferenceEntry<K, V> entry, final long now) {
        if (entry.getKey() == null) {
            return null;
        }
        final V value = entry.getValueReference().get();
        if (value == null) {
            return null;
        }
        if (this.isExpired(entry, now)) {
            return null;
        }
        return value;
    }
    
    boolean isExpired(final ReferenceEntry<K, V> entry, final long now) {
        Preconditions.<ReferenceEntry<K, V>>checkNotNull(entry);
        return (this.expiresAfterAccess() && now - entry.getAccessTime() >= this.expireAfterAccessNanos) || (this.expiresAfterWrite() && now - entry.getWriteTime() >= this.expireAfterWriteNanos);
    }
    
    static <K, V> void connectAccessOrder(final ReferenceEntry<K, V> previous, final ReferenceEntry<K, V> next) {
        previous.setNextInAccessQueue(next);
        next.setPreviousInAccessQueue(previous);
    }
    
    static <K, V> void nullifyAccessOrder(final ReferenceEntry<K, V> nulled) {
        final ReferenceEntry<K, V> nullEntry = LocalCache.<K, V>nullEntry();
        nulled.setNextInAccessQueue(nullEntry);
        nulled.setPreviousInAccessQueue(nullEntry);
    }
    
    static <K, V> void connectWriteOrder(final ReferenceEntry<K, V> previous, final ReferenceEntry<K, V> next) {
        previous.setNextInWriteQueue(next);
        next.setPreviousInWriteQueue(previous);
    }
    
    static <K, V> void nullifyWriteOrder(final ReferenceEntry<K, V> nulled) {
        final ReferenceEntry<K, V> nullEntry = LocalCache.<K, V>nullEntry();
        nulled.setNextInWriteQueue(nullEntry);
        nulled.setPreviousInWriteQueue(nullEntry);
    }
    
    void processPendingNotifications() {
        RemovalNotification<K, V> notification;
        while ((notification = (RemovalNotification<K, V>)this.removalNotificationQueue.poll()) != null) {
            try {
                this.removalListener.onRemoval(notification);
            }
            catch (Throwable e) {
                LocalCache.logger.log(Level.WARNING, "Exception thrown by removal listener", e);
            }
        }
    }
    
    final Segment<K, V>[] newSegmentArray(final int ssize) {
        return new Segment[ssize];
    }
    
    public void cleanUp() {
        for (final Segment<?, ?> segment : this.segments) {
            segment.cleanUp();
        }
    }
    
    public boolean isEmpty() {
        long sum = 0L;
        final Segment<K, V>[] segments = this.segments;
        for (int i = 0; i < segments.length; ++i) {
            if (segments[i].count != 0) {
                return false;
            }
            sum += segments[i].modCount;
        }
        if (sum != 0L) {
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0) {
                    return false;
                }
                sum -= segments[i].modCount;
            }
            if (sum != 0L) {
                return false;
            }
        }
        return true;
    }
    
    long longSize() {
        final Segment<K, V>[] segments = this.segments;
        long sum = 0L;
        for (int i = 0; i < segments.length; ++i) {
            sum += Math.max(0, segments[i].count);
        }
        return sum;
    }
    
    public int size() {
        return Ints.saturatedCast(this.longSize());
    }
    
    @Nullable
    public V get(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).get(key, hash);
    }
    
    @Nullable
    public V getIfPresent(final Object key) {
        final int hash = this.hash(Preconditions.checkNotNull(key));
        final V value = this.segmentFor(hash).get(key, hash);
        if (value == null) {
            this.globalStatsCounter.recordMisses(1);
        }
        else {
            this.globalStatsCounter.recordHits(1);
        }
        return value;
    }
    
    @Nullable
    public V getOrDefault(@Nullable final Object key, @Nullable final V defaultValue) {
        final V result = this.get(key);
        return (result != null) ? result : defaultValue;
    }
    
    V get(final K key, final CacheLoader<? super K, V> loader) throws ExecutionException {
        final int hash = this.hash(Preconditions.<K>checkNotNull(key));
        return this.segmentFor(hash).get(key, hash, loader);
    }
    
    V getOrLoad(final K key) throws ExecutionException {
        return this.get(key, this.defaultLoader);
    }
    
    ImmutableMap<K, V> getAllPresent(final Iterable<?> keys) {
        int hits = 0;
        int misses = 0;
        final Map<K, V> result = Maps.newLinkedHashMap();
        for (final Object key : keys) {
            final V value = this.get(key);
            if (value == null) {
                ++misses;
            }
            else {
                final K castKey = (K)key;
                result.put(castKey, value);
                ++hits;
            }
        }
        this.globalStatsCounter.recordHits(hits);
        this.globalStatsCounter.recordMisses(misses);
        return ImmutableMap.<K, V>copyOf((java.util.Map<? extends K, ? extends V>)result);
    }
    
    ImmutableMap<K, V> getAll(final Iterable<? extends K> keys) throws ExecutionException {
        int hits = 0;
        int misses = 0;
        final Map<K, V> result = Maps.newLinkedHashMap();
        final Set<K> keysToLoad = Sets.newLinkedHashSet();
        for (final K key : keys) {
            final V value = this.get(key);
            if (!result.containsKey(key)) {
                result.put(key, value);
                if (value == null) {
                    ++misses;
                    keysToLoad.add(key);
                }
                else {
                    ++hits;
                }
            }
        }
        try {
            if (!keysToLoad.isEmpty()) {
                try {
                    final Map<K, V> newEntries = this.loadAll(keysToLoad, this.defaultLoader);
                    for (final K key2 : keysToLoad) {
                        final V value2 = (V)newEntries.get(key2);
                        if (value2 == null) {
                            throw new CacheLoader.InvalidCacheLoadException(new StringBuilder().append("loadAll failed to return a value for ").append(key2).toString());
                        }
                        result.put(key2, value2);
                    }
                }
                catch (CacheLoader.UnsupportedLoadingOperationException e) {
                    for (final K key2 : keysToLoad) {
                        --misses;
                        result.put(key2, this.get(key2, this.defaultLoader));
                    }
                }
            }
            return ImmutableMap.<K, V>copyOf((java.util.Map<? extends K, ? extends V>)result);
        }
        finally {
            this.globalStatsCounter.recordHits(hits);
            this.globalStatsCounter.recordMisses(misses);
        }
    }
    
    @Nullable
    Map<K, V> loadAll(final Set<? extends K> keys, final CacheLoader<? super K, V> loader) throws ExecutionException {
        Preconditions.<CacheLoader<? super K, V>>checkNotNull(loader);
        Preconditions.<Set<? extends K>>checkNotNull(keys);
        final Stopwatch stopwatch = Stopwatch.createStarted();
        boolean success = false;
        Map<K, V> result;
        try {
            final Map<K, V> map = result = (Map<K, V>)loader.loadAll((java.lang.Iterable<? extends K>)keys);
            success = true;
        }
        catch (CacheLoader.UnsupportedLoadingOperationException e) {
            success = true;
            throw e;
        }
        catch (InterruptedException e2) {
            Thread.currentThread().interrupt();
            throw new ExecutionException((Throwable)e2);
        }
        catch (RuntimeException e3) {
            throw new UncheckedExecutionException((Throwable)e3);
        }
        catch (Exception e4) {
            throw new ExecutionException((Throwable)e4);
        }
        catch (Error e5) {
            throw new ExecutionError(e5);
        }
        finally {
            if (!success) {
                this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            }
        }
        if (result == null) {
            this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            throw new CacheLoader.InvalidCacheLoadException(new StringBuilder().append(loader).append(" returned null map from loadAll").toString());
        }
        stopwatch.stop();
        boolean nullsPresent = false;
        for (final Map.Entry<K, V> entry : result.entrySet()) {
            final K key = (K)entry.getKey();
            final V value = (V)entry.getValue();
            if (key == null || value == null) {
                nullsPresent = true;
            }
            else {
                this.put(key, value);
            }
        }
        if (nullsPresent) {
            this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            throw new CacheLoader.InvalidCacheLoadException(new StringBuilder().append(loader).append(" returned null keys or values from loadAll").toString());
        }
        this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
        return result;
    }
    
    ReferenceEntry<K, V> getEntry(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).getEntry(key, hash);
    }
    
    void refresh(final K key) {
        final int hash = this.hash(Preconditions.<K>checkNotNull(key));
        this.segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
    }
    
    public boolean containsKey(@Nullable final Object key) {
        if (key == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).containsKey(key, hash);
    }
    
    public boolean containsValue(@Nullable final Object value) {
        if (value == null) {
            return false;
        }
        final long now = this.ticker.read();
        final Segment<K, V>[] segments = this.segments;
        long last = -1L;
        for (int i = 0; i < 3; ++i) {
            long sum = 0L;
            for (final Segment<K, V> segment : segments) {
                final int unused = segment.count;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
                for (int j = 0; j < table.length(); ++j) {
                    for (ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)table.get(j); e != null; e = e.getNext()) {
                        final V v = segment.getLiveValue(e, now);
                        if (v != null && this.valueEquivalence.equivalent(value, v)) {
                            return true;
                        }
                    }
                }
                sum += segment.modCount;
            }
            if (sum == last) {
                break;
            }
            last = sum;
        }
        return false;
    }
    
    public V put(final K key, final V value) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<V>checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, false);
    }
    
    public V putIfAbsent(final K key, final V value) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<V>checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).put(key, hash, value, true);
    }
    
    public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> function) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<BiFunction<? super K, ? super V, ? extends V>>checkNotNull(function);
        final int hash = this.hash(key);
        return this.segmentFor(hash).compute(key, hash, function);
    }
    
    public V computeIfAbsent(final K key, final Function<? super K, ? extends V> function) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<Function<? super K, ? extends V>>checkNotNull(function);
        return this.compute(key, ((k, oldValue) -> (oldValue == null) ? function.apply(key) : oldValue));
    }
    
    public V computeIfPresent(final K key, final BiFunction<? super K, ? super V, ? extends V> function) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<BiFunction<? super K, ? super V, ? extends V>>checkNotNull(function);
        return this.compute(key, ((k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue)));
    }
    
    public V merge(final K key, final V newValue, final BiFunction<? super V, ? super V, ? extends V> function) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<V>checkNotNull(newValue);
        Preconditions.<BiFunction<? super V, ? super V, ? extends V>>checkNotNull(function);
        return this.compute(key, ((k, oldValue) -> (oldValue == null) ? newValue : function.apply(oldValue, newValue)));
    }
    
    public void putAll(final Map<? extends K, ? extends V> m) {
        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            this.put(e.getKey(), e.getValue());
        }
    }
    
    public V remove(@Nullable final Object key) {
        if (key == null) {
            return null;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash);
    }
    
    public boolean remove(@Nullable final Object key, @Nullable final Object value) {
        if (key == null || value == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).remove(key, hash, value);
    }
    
    public boolean replace(final K key, @Nullable final V oldValue, final V newValue) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<V>checkNotNull(newValue);
        if (oldValue == null) {
            return false;
        }
        final int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, oldValue, newValue);
    }
    
    public V replace(final K key, final V value) {
        Preconditions.<K>checkNotNull(key);
        Preconditions.<V>checkNotNull(value);
        final int hash = this.hash(key);
        return this.segmentFor(hash).replace(key, hash, value);
    }
    
    public void clear() {
        for (final Segment<K, V> segment : this.segments) {
            segment.clear();
        }
    }
    
    void invalidateAll(final Iterable<?> keys) {
        for (final Object key : keys) {
            this.remove(key);
        }
    }
    
    public Set<K> keySet() {
        final Set<K> ks = this.keySet;
        return (ks != null) ? ks : (this.keySet = (Set<K>)new KeySet(this));
    }
    
    public Collection<V> values() {
        final Collection<V> vs = this.values;
        return (vs != null) ? vs : (this.values = (Collection<V>)new Values(this));
    }
    
    @GwtIncompatible
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> es = this.entrySet;
        return (es != null) ? es : (this.entrySet = (Set<Map.Entry<K, V>>)new EntrySet(this));
    }
    
    private static <E> ArrayList<E> toArrayList(final Collection<E> c) {
        final ArrayList<E> result = (ArrayList<E>)new ArrayList(c.size());
        Iterators.addAll((java.util.Collection<Object>)result, (java.util.Iterator<?>)c.iterator());
        return result;
    }
    
    boolean removeIf(final BiPredicate<? super K, ? super V> filter) {
        Preconditions.<BiPredicate<? super K, ? super V>>checkNotNull(filter);
        boolean changed = false;
        for (final K key : this.keySet()) {
            while (true) {
                final V value = this.get(key);
                if (value == null) {
                    break;
                }
                if (!filter.test(key, value)) {
                    break;
                }
                if (this.remove(key, value)) {
                    changed = true;
                    break;
                }
            }
        }
        return changed;
    }
    
    static {
        logger = Logger.getLogger(LocalCache.class.getName());
        UNSET = new ValueReference<Object, Object>() {
            public Object get() {
                return null;
            }
            
            public int getWeight() {
                return 0;
            }
            
            public ReferenceEntry<Object, Object> getEntry() {
                return null;
            }
            
            public ValueReference<Object, Object> copyFor(final ReferenceQueue<Object> queue, @Nullable final Object value, final ReferenceEntry<Object, Object> entry) {
                return this;
            }
            
            public boolean isLoading() {
                return false;
            }
            
            public boolean isActive() {
                return false;
            }
            
            public Object waitForValue() {
                return null;
            }
            
            public void notifyNewValue(final Object newValue) {
            }
        };
        DISCARDING_QUEUE = (Queue)new AbstractQueue<Object>() {
            public boolean offer(final Object o) {
                return true;
            }
            
            public Object peek() {
                return null;
            }
            
            public Object poll() {
                return null;
            }
            
            public int size() {
                return 0;
            }
            
            public Iterator<Object> iterator() {
                return (Iterator<Object>)ImmutableSet.of().iterator();
            }
        };
    }
    
    enum Strength {
        STRONG {
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> entry, final V value, final int weight) {
                return (weight == 1) ? new StrongValueReference<K, V>(value) : new WeightedStrongValueReference<K, V>(value, weight);
            }
            
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.equals();
            }
        }, 
        SOFT {
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> entry, final V value, final int weight) {
                return (weight == 1) ? new SoftValueReference<K, V>(segment.valueReferenceQueue, value, entry) : new WeightedSoftValueReference<K, V>(segment.valueReferenceQueue, value, entry, weight);
            }
            
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        }, 
        WEAK {
            @Override
             <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> entry, final V value, final int weight) {
                return (weight == 1) ? new WeakValueReference<K, V>(segment.valueReferenceQueue, value, entry) : new WeightedWeakValueReference<K, V>(segment.valueReferenceQueue, value, entry, weight);
            }
            
            @Override
            Equivalence<Object> defaultEquivalence() {
                return Equivalence.identity();
            }
        };
        
        abstract <K, V> ValueReference<K, V> referenceValue(final Segment<K, V> segment, final ReferenceEntry<K, V> referenceEntry, final V object, final int integer);
        
        abstract Equivalence<Object> defaultEquivalence();
    }
    
    enum EntryFactory {
        STRONG {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongEntry<K, V>(key, hash, next);
            }
        }, 
        STRONG_ACCESS {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongAccessEntry<K, V>(key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.<K, V>copyEntry(segment, original, newNext);
                this.<K, V>copyAccessEntry(original, newEntry);
                return newEntry;
            }
        }, 
        STRONG_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongWriteEntry<K, V>(key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.<K, V>copyEntry(segment, original, newNext);
                this.<K, V>copyWriteEntry(original, newEntry);
                return newEntry;
            }
        }, 
        STRONG_ACCESS_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new StrongAccessWriteEntry<K, V>(key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.<K, V>copyEntry(segment, original, newNext);
                this.<K, V>copyAccessEntry(original, newEntry);
                this.<K, V>copyWriteEntry(original, newEntry);
                return newEntry;
            }
        }, 
        WEAK {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
        }, 
        WEAK_ACCESS {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakAccessEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.<K, V>copyEntry(segment, original, newNext);
                this.<K, V>copyAccessEntry(original, newEntry);
                return newEntry;
            }
        }, 
        WEAK_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakWriteEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.<K, V>copyEntry(segment, original, newNext);
                this.<K, V>copyWriteEntry(original, newEntry);
                return newEntry;
            }
        }, 
        WEAK_ACCESS_WRITE {
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
                return new WeakAccessWriteEntry<K, V>(segment.keyReferenceQueue, key, hash, next);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
                final ReferenceEntry<K, V> newEntry = super.<K, V>copyEntry(segment, original, newNext);
                this.<K, V>copyAccessEntry(original, newEntry);
                this.<K, V>copyWriteEntry(original, newEntry);
                return newEntry;
            }
        };
        
        static final int ACCESS_MASK = 1;
        static final int WRITE_MASK = 2;
        static final int WEAK_MASK = 4;
        static final EntryFactory[] factories;
        
        static EntryFactory getFactory(final Strength keyStrength, final boolean usesAccessQueue, final boolean usesWriteQueue) {
            final int flags = ((keyStrength == Strength.WEAK) ? 4 : 0) | (usesAccessQueue ? 1 : 0) | (usesWriteQueue ? 2 : 0);
            return EntryFactory.factories[flags];
        }
        
        abstract <K, V> ReferenceEntry<K, V> newEntry(final Segment<K, V> segment, final K object, final int integer, @Nullable final ReferenceEntry<K, V> referenceEntry);
        
         <K, V> ReferenceEntry<K, V> copyEntry(final Segment<K, V> segment, final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
            return this.<K, V>newEntry(segment, original.getKey(), original.getHash(), newNext);
        }
        
         <K, V> void copyAccessEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newEntry) {
            newEntry.setAccessTime(original.getAccessTime());
            LocalCache.<K, V>connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
            LocalCache.<K, V>connectAccessOrder(newEntry, original.getNextInAccessQueue());
            LocalCache.<K, V>nullifyAccessOrder(original);
        }
        
         <K, V> void copyWriteEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newEntry) {
            newEntry.setWriteTime(original.getWriteTime());
            LocalCache.<K, V>connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
            LocalCache.<K, V>connectWriteOrder(newEntry, original.getNextInWriteQueue());
            LocalCache.<K, V>nullifyWriteOrder(original);
        }
        
        static {
            factories = new EntryFactory[] { EntryFactory.STRONG, EntryFactory.STRONG_ACCESS, EntryFactory.STRONG_WRITE, EntryFactory.STRONG_ACCESS_WRITE, EntryFactory.WEAK, EntryFactory.WEAK_ACCESS, EntryFactory.WEAK_WRITE, EntryFactory.WEAK_ACCESS_WRITE };
        }
    }
    
    private enum NullEntry implements ReferenceEntry<Object, Object> {
        INSTANCE;
        
        public ValueReference<Object, Object> getValueReference() {
            return null;
        }
        
        public void setValueReference(final ValueReference<Object, Object> valueReference) {
        }
        
        public ReferenceEntry<Object, Object> getNext() {
            return null;
        }
        
        public int getHash() {
            return 0;
        }
        
        public Object getKey() {
            return null;
        }
        
        public long getAccessTime() {
            return 0L;
        }
        
        public void setAccessTime(final long time) {
        }
        
        public ReferenceEntry<Object, Object> getNextInAccessQueue() {
            return this;
        }
        
        public void setNextInAccessQueue(final ReferenceEntry<Object, Object> next) {
        }
        
        public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
            return this;
        }
        
        public void setPreviousInAccessQueue(final ReferenceEntry<Object, Object> previous) {
        }
        
        public long getWriteTime() {
            return 0L;
        }
        
        public void setWriteTime(final long time) {
        }
        
        public ReferenceEntry<Object, Object> getNextInWriteQueue() {
            return this;
        }
        
        public void setNextInWriteQueue(final ReferenceEntry<Object, Object> next) {
        }
        
        public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
            return this;
        }
        
        public void setPreviousInWriteQueue(final ReferenceEntry<Object, Object> previous) {
        }
    }
    
    abstract static class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
        public ValueReference<K, V> getValueReference() {
            throw new UnsupportedOperationException();
        }
        
        public void setValueReference(final ValueReference<K, V> valueReference) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getNext() {
            throw new UnsupportedOperationException();
        }
        
        public int getHash() {
            throw new UnsupportedOperationException();
        }
        
        public K getKey() {
            throw new UnsupportedOperationException();
        }
        
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }
        
        public void setAccessTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }
        
        public void setWriteTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
    }
    
    static class StrongEntry<K, V> extends AbstractReferenceEntry<K, V> {
        final K key;
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        StrongEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            this.valueReference = LocalCache.<K, V>unset();
            this.key = key;
            this.hash = hash;
            this.next = next;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;
        
        StrongAccessEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, hash, next);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.<K, V>nullEntry();
            this.previousAccess = LocalCache.<K, V>nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public void setAccessTime(final long time) {
            this.accessTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
    }
    
    static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long writeTime;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        
        StrongWriteEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, hash, next);
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.<K, V>nullEntry();
            this.previousWrite = LocalCache.<K, V>nullEntry();
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setWriteTime(final long time) {
            this.writeTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }
    
    static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;
        volatile long writeTime;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        
        StrongAccessWriteEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, hash, next);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.<K, V>nullEntry();
            this.previousAccess = LocalCache.<K, V>nullEntry();
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.<K, V>nullEntry();
            this.previousWrite = LocalCache.<K, V>nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public void setAccessTime(final long time) {
            this.accessTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setWriteTime(final long time) {
            this.writeTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }
    
    static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
        final int hash;
        final ReferenceEntry<K, V> next;
        volatile ValueReference<K, V> valueReference;
        
        WeakEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(key, (ReferenceQueue)queue);
            this.valueReference = LocalCache.<K, V>unset();
            this.hash = hash;
            this.next = next;
        }
        
        public K getKey() {
            return (K)this.get();
        }
        
        public long getAccessTime() {
            throw new UnsupportedOperationException();
        }
        
        public void setAccessTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        public long getWriteTime() {
            throw new UnsupportedOperationException();
        }
        
        public void setWriteTime(final long time) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
            throw new UnsupportedOperationException();
        }
        
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            throw new UnsupportedOperationException();
        }
        
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
            throw new UnsupportedOperationException();
        }
        
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        public int getHash() {
            return this.hash;
        }
        
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;
        
        WeakAccessEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.<K, V>nullEntry();
            this.previousAccess = LocalCache.<K, V>nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public void setAccessTime(final long time) {
            this.accessTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
    }
    
    static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long writeTime;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        
        WeakWriteEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.<K, V>nullEntry();
            this.previousWrite = LocalCache.<K, V>nullEntry();
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setWriteTime(final long time) {
            this.writeTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }
    
    static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> {
        volatile long accessTime;
        ReferenceEntry<K, V> nextAccess;
        ReferenceEntry<K, V> previousAccess;
        volatile long writeTime;
        ReferenceEntry<K, V> nextWrite;
        ReferenceEntry<K, V> previousWrite;
        
        WeakAccessWriteEntry(final ReferenceQueue<K> queue, final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            super(queue, key, hash, next);
            this.accessTime = Long.MAX_VALUE;
            this.nextAccess = LocalCache.<K, V>nullEntry();
            this.previousAccess = LocalCache.<K, V>nullEntry();
            this.writeTime = Long.MAX_VALUE;
            this.nextWrite = LocalCache.<K, V>nullEntry();
            this.previousWrite = LocalCache.<K, V>nullEntry();
        }
        
        @Override
        public long getAccessTime() {
            return this.accessTime;
        }
        
        @Override
        public void setAccessTime(final long time) {
            this.accessTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInAccessQueue() {
            return this.nextAccess;
        }
        
        @Override
        public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
            this.nextAccess = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInAccessQueue() {
            return this.previousAccess;
        }
        
        @Override
        public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
            this.previousAccess = previous;
        }
        
        @Override
        public long getWriteTime() {
            return this.writeTime;
        }
        
        @Override
        public void setWriteTime(final long time) {
            this.writeTime = time;
        }
        
        @Override
        public ReferenceEntry<K, V> getNextInWriteQueue() {
            return this.nextWrite;
        }
        
        @Override
        public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
            this.nextWrite = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getPreviousInWriteQueue() {
            return this.previousWrite;
        }
        
        @Override
        public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
            this.previousWrite = previous;
        }
    }
    
    static class WeakValueReference<K, V> extends WeakReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;
        
        WeakValueReference(final ReferenceQueue<V> queue, final V referent, final ReferenceEntry<K, V> entry) {
            super(referent, (ReferenceQueue)queue);
            this.entry = entry;
        }
        
        public int getWeight() {
            return 1;
        }
        
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }
        
        public void notifyNewValue(final V newValue) {
        }
        
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return new WeakValueReference((java.lang.ref.ReferenceQueue<Object>)queue, value, (ReferenceEntry<Object, Object>)entry);
        }
        
        public boolean isLoading() {
            return false;
        }
        
        public boolean isActive() {
            return true;
        }
        
        public V waitForValue() {
            return this.get();
        }
    }
    
    static class SoftValueReference<K, V> extends SoftReference<V> implements ValueReference<K, V> {
        final ReferenceEntry<K, V> entry;
        
        SoftValueReference(final ReferenceQueue<V> queue, final V referent, final ReferenceEntry<K, V> entry) {
            super(referent, (ReferenceQueue)queue);
            this.entry = entry;
        }
        
        public int getWeight() {
            return 1;
        }
        
        public ReferenceEntry<K, V> getEntry() {
            return this.entry;
        }
        
        public void notifyNewValue(final V newValue) {
        }
        
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return new SoftValueReference((java.lang.ref.ReferenceQueue<Object>)queue, value, (ReferenceEntry<Object, Object>)entry);
        }
        
        public boolean isLoading() {
            return false;
        }
        
        public boolean isActive() {
            return true;
        }
        
        public V waitForValue() {
            return this.get();
        }
    }
    
    static class StrongValueReference<K, V> implements ValueReference<K, V> {
        final V referent;
        
        StrongValueReference(final V referent) {
            this.referent = referent;
        }
        
        public V get() {
            return this.referent;
        }
        
        public int getWeight() {
            return 1;
        }
        
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return this;
        }
        
        public boolean isLoading() {
            return false;
        }
        
        public boolean isActive() {
            return true;
        }
        
        public V waitForValue() {
            return this.get();
        }
        
        public void notifyNewValue(final V newValue) {
        }
    }
    
    static final class WeightedWeakValueReference<K, V> extends WeakValueReference<K, V> {
        final int weight;
        
        WeightedWeakValueReference(final ReferenceQueue<V> queue, final V referent, final ReferenceEntry<K, V> entry, final int weight) {
            super(queue, referent, entry);
            this.weight = weight;
        }
        
        @Override
        public int getWeight() {
            return this.weight;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return new WeightedWeakValueReference((java.lang.ref.ReferenceQueue<Object>)queue, value, (ReferenceEntry<Object, Object>)entry, this.weight);
        }
    }
    
    static final class WeightedSoftValueReference<K, V> extends SoftValueReference<K, V> {
        final int weight;
        
        WeightedSoftValueReference(final ReferenceQueue<V> queue, final V referent, final ReferenceEntry<K, V> entry, final int weight) {
            super(queue, referent, entry);
            this.weight = weight;
        }
        
        @Override
        public int getWeight() {
            return this.weight;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, final V value, final ReferenceEntry<K, V> entry) {
            return new WeightedSoftValueReference((java.lang.ref.ReferenceQueue<Object>)queue, value, (ReferenceEntry<Object, Object>)entry, this.weight);
        }
    }
    
    static final class WeightedStrongValueReference<K, V> extends StrongValueReference<K, V> {
        final int weight;
        
        WeightedStrongValueReference(final V referent, final int weight) {
            super(referent);
            this.weight = weight;
        }
        
        @Override
        public int getWeight() {
            return this.weight;
        }
    }
    
    static class Segment<K, V> extends ReentrantLock {
        @Weak
        final LocalCache<K, V> map;
        volatile int count;
        @GuardedBy("this")
        long totalWeight;
        int modCount;
        int threshold;
        volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
        final long maxSegmentWeight;
        final ReferenceQueue<K> keyReferenceQueue;
        final ReferenceQueue<V> valueReferenceQueue;
        final Queue<ReferenceEntry<K, V>> recencyQueue;
        final AtomicInteger readCount;
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> writeQueue;
        @GuardedBy("this")
        final Queue<ReferenceEntry<K, V>> accessQueue;
        final AbstractCache.StatsCounter statsCounter;
        
        Segment(final LocalCache<K, V> map, final int initialCapacity, final long maxSegmentWeight, final AbstractCache.StatsCounter statsCounter) {
            this.readCount = new AtomicInteger();
            this.map = map;
            this.maxSegmentWeight = maxSegmentWeight;
            this.statsCounter = Preconditions.<AbstractCache.StatsCounter>checkNotNull(statsCounter);
            this.initTable(this.newEntryArray(initialCapacity));
            this.keyReferenceQueue = (ReferenceQueue<K>)(map.usesKeyReferences() ? new ReferenceQueue() : null);
            this.valueReferenceQueue = (ReferenceQueue<V>)(map.usesValueReferences() ? new ReferenceQueue() : null);
            this.recencyQueue = (Queue<ReferenceEntry<K, V>>)(map.usesAccessQueue() ? new ConcurrentLinkedQueue() : LocalCache.discardingQueue());
            this.writeQueue = (Queue<ReferenceEntry<K, V>>)(map.usesWriteQueue() ? new WriteQueue() : LocalCache.discardingQueue());
            this.accessQueue = (Queue<ReferenceEntry<K, V>>)(map.usesAccessQueue() ? new AccessQueue() : LocalCache.discardingQueue());
        }
        
        AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(final int size) {
            return (AtomicReferenceArray<ReferenceEntry<K, V>>)new AtomicReferenceArray(size);
        }
        
        void initTable(final AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
            this.threshold = newTable.length() * 3 / 4;
            if (!this.map.customWeigher() && this.threshold == this.maxSegmentWeight) {
                ++this.threshold;
            }
            this.table = newTable;
        }
        
        @GuardedBy("this")
        ReferenceEntry<K, V> newEntry(final K key, final int hash, @Nullable final ReferenceEntry<K, V> next) {
            return this.map.entryFactory.<K, V>newEntry(this, (K)Preconditions.<K>checkNotNull((K)key), hash, next);
        }
        
        @GuardedBy("this")
        ReferenceEntry<K, V> copyEntry(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newNext) {
            if (original.getKey() == null) {
                return null;
            }
            final ValueReference<K, V> valueReference = original.getValueReference();
            final V value = valueReference.get();
            if (value == null && valueReference.isActive()) {
                return null;
            }
            final ReferenceEntry<K, V> newEntry = this.map.entryFactory.<K, V>copyEntry(this, original, newNext);
            newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
            return newEntry;
        }
        
        @GuardedBy("this")
        void setValue(final ReferenceEntry<K, V> entry, final K key, final V value, final long now) {
            final ValueReference<K, V> previous = entry.getValueReference();
            final int weight = this.map.weigher.weigh(key, value);
            Preconditions.checkState(weight >= 0, "Weights must be non-negative");
            final ValueReference<K, V> valueReference = this.map.valueStrength.<K, V>referenceValue(this, entry, value, weight);
            entry.setValueReference(valueReference);
            this.recordWrite(entry, weight, now);
            previous.notifyNewValue(value);
        }
        
        V get(final K key, final int hash, final CacheLoader<? super K, V> loader) throws ExecutionException {
            Preconditions.<K>checkNotNull(key);
            Preconditions.<CacheLoader<? super K, V>>checkNotNull(loader);
            try {
                if (this.count != 0) {
                    final ReferenceEntry<K, V> e = this.getEntry(key, hash);
                    if (e != null) {
                        final long now = this.map.ticker.read();
                        final V value = this.getLiveValue(e, now);
                        if (value != null) {
                            this.recordRead(e, now);
                            this.statsCounter.recordHits(1);
                            return (V)this.scheduleRefresh((ReferenceEntry<K, Object>)e, key, hash, value, now, loader);
                        }
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        if (valueReference.isLoading()) {
                            return (V)this.waitForLoadingValue((ReferenceEntry<K, Object>)e, key, (ValueReference<K, Object>)valueReference);
                        }
                    }
                }
                return (V)this.lockedGetOrLoad(key, hash, loader);
            }
            catch (ExecutionException ee) {
                final Throwable cause = ee.getCause();
                if (cause instanceof Error) {
                    throw new ExecutionError((Error)cause);
                }
                if (cause instanceof RuntimeException) {
                    throw new UncheckedExecutionException(cause);
                }
                throw ee;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        V lockedGetOrLoad(final K key, final int hash, final CacheLoader<? super K, V> loader) throws ExecutionException {
            ValueReference<K, V> valueReference = null;
            LoadingValueReference<K, V> loadingValueReference = null;
            boolean createNewEntry = true;
            this.lock();
            ReferenceEntry<K, V> e;
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                final int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        valueReference = e.getValueReference();
                        if (valueReference.isLoading()) {
                            createNewEntry = false;
                            break;
                        }
                        final V value = valueReference.get();
                        if (value == null) {
                            this.enqueueNotification(entryKey, hash, value, valueReference.getWeight(), RemovalCause.COLLECTED);
                        }
                        else {
                            if (!this.map.isExpired(e, now)) {
                                this.recordLockedRead(e, now);
                                this.statsCounter.recordHits(1);
                                return value;
                            }
                            this.enqueueNotification(entryKey, hash, value, valueReference.getWeight(), RemovalCause.EXPIRED);
                        }
                        this.writeQueue.remove(e);
                        this.accessQueue.remove(e);
                        this.count = newCount;
                        break;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                if (createNewEntry) {
                    loadingValueReference = new LoadingValueReference<K, V>();
                    if (e == null) {
                        e = this.newEntry(key, hash, first);
                        e.setValueReference(loadingValueReference);
                        table.set(index, e);
                    }
                    else {
                        e.setValueReference(loadingValueReference);
                    }
                }
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
            if (createNewEntry) {
                try {
                    synchronized (e) {
                        return (V)this.loadSync(key, hash, (LoadingValueReference<K, Object>)loadingValueReference, loader);
                    }
                }
                finally {
                    this.statsCounter.recordMisses(1);
                }
            }
            return this.waitForLoadingValue(e, key, valueReference);
        }
        
        V waitForLoadingValue(final ReferenceEntry<K, V> e, final K key, final ValueReference<K, V> valueReference) throws ExecutionException {
            if (!valueReference.isLoading()) {
                throw new AssertionError();
            }
            Preconditions.checkState(!Thread.holdsLock(e), "Recursive load of: %s", key);
            try {
                final V value = valueReference.waitForValue();
                if (value == null) {
                    throw new CacheLoader.InvalidCacheLoadException(new StringBuilder().append("CacheLoader returned null for key ").append(key).append(".").toString());
                }
                final long now = this.map.ticker.read();
                this.recordRead(e, now);
                return value;
            }
            finally {
                this.statsCounter.recordMisses(1);
            }
        }
        
        V compute(final K key, final int hash, final BiFunction<? super K, ? super V, ? extends V> function) {
            ValueReference<K, V> valueReference = null;
            LoadingValueReference<K, V> loadingValueReference = null;
            boolean createNewEntry = true;
            this.lock();
            ReferenceEntry<K, V> e;
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> first;
                for (first = (e = (ReferenceEntry<K, V>)table.get(index)); e != null; e = e.getNext()) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        valueReference = e.getValueReference();
                        if (this.map.isExpired(e, now)) {
                            this.enqueueNotification(entryKey, hash, valueReference.get(), valueReference.getWeight(), RemovalCause.EXPIRED);
                        }
                        this.writeQueue.remove(e);
                        this.accessQueue.remove(e);
                        createNewEntry = false;
                        break;
                    }
                }
                loadingValueReference = new LoadingValueReference<K, V>(valueReference);
                if (e == null) {
                    createNewEntry = true;
                    e = this.newEntry(key, hash, first);
                    e.setValueReference(loadingValueReference);
                    table.set(index, e);
                }
                else {
                    e.setValueReference(loadingValueReference);
                }
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
            synchronized (e) {
                final V newValue = loadingValueReference.compute(key, function);
                if (newValue != null) {
                    try {
                        return this.getAndRecordStats(key, hash, loadingValueReference, Futures.<V>immediateFuture(newValue));
                    }
                    catch (ExecutionException exception) {
                        throw new AssertionError("impossible; Futures.immediateFuture can't throw");
                    }
                }
                if (createNewEntry) {
                    this.removeLoadingValue(key, hash, loadingValueReference);
                    return null;
                }
                this.lock();
                try {
                    this.removeEntry(e, hash, RemovalCause.EXPLICIT);
                }
                finally {
                    this.unlock();
                }
                return null;
            }
        }
        
        V loadSync(final K key, final int hash, final LoadingValueReference<K, V> loadingValueReference, final CacheLoader<? super K, V> loader) throws ExecutionException {
            final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
            return this.getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
        }
        
        ListenableFuture<V> loadAsync(final K key, final int hash, final LoadingValueReference<K, V> loadingValueReference, final CacheLoader<? super K, V> loader) {
            final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
            loadingFuture.addListener((Runnable)new Runnable() {
                public void run() {
                    try {
                        Segment.this.getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
                    }
                    catch (Throwable t) {
                        LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
                        loadingValueReference.setException(t);
                    }
                }
            }, MoreExecutors.directExecutor());
            return loadingFuture;
        }
        
        V getAndRecordStats(final K key, final int hash, final LoadingValueReference<K, V> loadingValueReference, final ListenableFuture<V> newValue) throws ExecutionException {
            V value = null;
            try {
                value = Uninterruptibles.<V>getUninterruptibly((java.util.concurrent.Future<V>)newValue);
                if (value == null) {
                    throw new CacheLoader.InvalidCacheLoadException(new StringBuilder().append("CacheLoader returned null for key ").append(key).append(".").toString());
                }
                this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                this.storeLoadedValue(key, hash, loadingValueReference, value);
                return value;
            }
            finally {
                if (value == null) {
                    this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                    this.removeLoadingValue(key, hash, loadingValueReference);
                }
            }
        }
        
        V scheduleRefresh(final ReferenceEntry<K, V> entry, final K key, final int hash, final V oldValue, final long now, final CacheLoader<? super K, V> loader) {
            if (this.map.refreshes() && now - entry.getWriteTime() > this.map.refreshNanos && !entry.getValueReference().isLoading()) {
                final V newValue = this.refresh(key, hash, loader, true);
                if (newValue != null) {
                    return newValue;
                }
            }
            return oldValue;
        }
        
        @Nullable
        V refresh(final K key, final int hash, final CacheLoader<? super K, V> loader, final boolean checkTime) {
            final LoadingValueReference<K, V> loadingValueReference = this.insertLoadingValueReference(key, hash, checkTime);
            if (loadingValueReference == null) {
                return null;
            }
            final ListenableFuture<V> result = this.loadAsync(key, hash, loadingValueReference, loader);
            if (result.isDone()) {
                try {
                    return Uninterruptibles.<V>getUninterruptibly((java.util.concurrent.Future<V>)result);
                }
                catch (Throwable t) {}
            }
            return null;
        }
        
        @Nullable
        LoadingValueReference<K, V> insertLoadingValueReference(final K key, final int hash, final boolean checkTime) {
            ReferenceEntry<K, V> e = null;
            this.lock();
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        if (valueReference.isLoading() || (checkTime && now - e.getWriteTime() < this.map.refreshNanos)) {
                            return null;
                        }
                        ++this.modCount;
                        final LoadingValueReference<K, V> loadingValueReference = new LoadingValueReference<K, V>(valueReference);
                        e.setValueReference(loadingValueReference);
                        return loadingValueReference;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                ++this.modCount;
                final LoadingValueReference<K, V> loadingValueReference2 = new LoadingValueReference<K, V>();
                e = this.newEntry(key, hash, first);
                e.setValueReference(loadingValueReference2);
                table.set(index, e);
                return loadingValueReference2;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        void tryDrainReferenceQueues() {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        @GuardedBy("this")
        void drainReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.drainKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.drainValueReferenceQueue();
            }
        }
        
        @GuardedBy("this")
        void drainKeyReferenceQueue() {
            int i = 0;
            Reference<? extends K> ref;
            while ((ref = this.keyReferenceQueue.poll()) != null) {
                final ReferenceEntry<K, V> entry = (ReferenceEntry<K, V>)(ReferenceEntry)ref;
                this.map.reclaimKey(entry);
                if (++i == 16) {
                    break;
                }
            }
        }
        
        @GuardedBy("this")
        void drainValueReferenceQueue() {
            int i = 0;
            Reference<? extends V> ref;
            while ((ref = this.valueReferenceQueue.poll()) != null) {
                final ValueReference<K, V> valueReference = (ValueReference<K, V>)(ValueReference)ref;
                this.map.reclaimValue(valueReference);
                if (++i == 16) {
                    break;
                }
            }
        }
        
        void clearReferenceQueues() {
            if (this.map.usesKeyReferences()) {
                this.clearKeyReferenceQueue();
            }
            if (this.map.usesValueReferences()) {
                this.clearValueReferenceQueue();
            }
        }
        
        void clearKeyReferenceQueue() {
            while (this.keyReferenceQueue.poll() != null) {}
        }
        
        void clearValueReferenceQueue() {
            while (this.valueReferenceQueue.poll() != null) {}
        }
        
        void recordRead(final ReferenceEntry<K, V> entry, final long now) {
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            this.recencyQueue.add(entry);
        }
        
        @GuardedBy("this")
        void recordLockedRead(final ReferenceEntry<K, V> entry, final long now) {
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            this.accessQueue.add(entry);
        }
        
        @GuardedBy("this")
        void recordWrite(final ReferenceEntry<K, V> entry, final int weight, final long now) {
            this.drainRecencyQueue();
            this.totalWeight += weight;
            if (this.map.recordsAccess()) {
                entry.setAccessTime(now);
            }
            if (this.map.recordsWrite()) {
                entry.setWriteTime(now);
            }
            this.accessQueue.add(entry);
            this.writeQueue.add(entry);
        }
        
        @GuardedBy("this")
        void drainRecencyQueue() {
            ReferenceEntry<K, V> e;
            while ((e = (ReferenceEntry<K, V>)this.recencyQueue.poll()) != null) {
                if (this.accessQueue.contains(e)) {
                    this.accessQueue.add(e);
                }
            }
        }
        
        void tryExpireEntries(final long now) {
            if (this.tryLock()) {
                try {
                    this.expireEntries(now);
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        @GuardedBy("this")
        void expireEntries(final long now) {
            this.drainRecencyQueue();
            ReferenceEntry<K, V> e;
            while ((e = (ReferenceEntry<K, V>)this.writeQueue.peek()) != null && this.map.isExpired(e, now)) {
                if (!this.removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
            while ((e = (ReferenceEntry<K, V>)this.accessQueue.peek()) != null && this.map.isExpired(e, now)) {
                if (!this.removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
                    throw new AssertionError();
                }
            }
        }
        
        @GuardedBy("this")
        void enqueueNotification(@Nullable final K key, final int hash, @Nullable final V value, final int weight, final RemovalCause cause) {
            this.totalWeight -= weight;
            if (cause.wasEvicted()) {
                this.statsCounter.recordEviction();
            }
            if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
                final RemovalNotification<K, V> notification = RemovalNotification.<K, V>create(key, value, cause);
                this.map.removalNotificationQueue.offer(notification);
            }
        }
        
        @GuardedBy("this")
        void evictEntries(final ReferenceEntry<K, V> newest) {
            if (!this.map.evictsBySize()) {
                return;
            }
            this.drainRecencyQueue();
            if (newest.getValueReference().getWeight() > this.maxSegmentWeight && !this.removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
                throw new AssertionError();
            }
            while (this.totalWeight > this.maxSegmentWeight) {
                final ReferenceEntry<K, V> e = this.getNextEvictable();
                if (!this.removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
                    throw new AssertionError();
                }
            }
        }
        
        @GuardedBy("this")
        ReferenceEntry<K, V> getNextEvictable() {
            for (final ReferenceEntry<K, V> e : this.accessQueue) {
                final int weight = e.getValueReference().getWeight();
                if (weight > 0) {
                    return e;
                }
            }
            throw new AssertionError();
        }
        
        ReferenceEntry<K, V> getFirst(final int hash) {
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            return (ReferenceEntry<K, V>)table.get(hash & table.length() - 1);
        }
        
        @Nullable
        ReferenceEntry<K, V> getEntry(final Object key, final int hash) {
            for (ReferenceEntry<K, V> e = this.getFirst(hash); e != null; e = e.getNext()) {
                if (e.getHash() == hash) {
                    final K entryKey = e.getKey();
                    if (entryKey == null) {
                        this.tryDrainReferenceQueues();
                    }
                    else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
                        return e;
                    }
                }
            }
            return null;
        }
        
        @Nullable
        ReferenceEntry<K, V> getLiveEntry(final Object key, final int hash, final long now) {
            final ReferenceEntry<K, V> e = this.getEntry(key, hash);
            if (e == null) {
                return null;
            }
            if (this.map.isExpired(e, now)) {
                this.tryExpireEntries(now);
                return null;
            }
            return e;
        }
        
        V getLiveValue(final ReferenceEntry<K, V> entry, final long now) {
            if (entry.getKey() == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            final V value = entry.getValueReference().get();
            if (value == null) {
                this.tryDrainReferenceQueues();
                return null;
            }
            if (this.map.isExpired(entry, now)) {
                this.tryExpireEntries(now);
                return null;
            }
            return value;
        }
        
        @Nullable
        V get(final Object key, final int hash) {
            try {
                if (this.count != 0) {
                    final long now = this.map.ticker.read();
                    final ReferenceEntry<K, V> e = this.getLiveEntry(key, hash, now);
                    if (e == null) {
                        return null;
                    }
                    final V value = e.getValueReference().get();
                    if (value != null) {
                        this.recordRead(e, now);
                        return (V)this.scheduleRefresh((ReferenceEntry<K, Object>)e, e.getKey(), hash, value, now, this.map.defaultLoader);
                    }
                    this.tryDrainReferenceQueues();
                }
                return null;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        boolean containsKey(final Object key, final int hash) {
            try {
                if (this.count == 0) {
                    return false;
                }
                final long now = this.map.ticker.read();
                final ReferenceEntry<K, V> e = this.getLiveEntry(key, hash, now);
                if (e == null) {
                    return false;
                }
                return e.getValueReference().get() != null;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        @VisibleForTesting
        boolean containsValue(final Object value) {
            try {
                if (this.count != 0) {
                    final long now = this.map.ticker.read();
                    final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    for (int length = table.length(), i = 0; i < length; ++i) {
                        for (ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)table.get(i); e != null; e = e.getNext()) {
                            final V entryValue = this.getLiveValue(e, now);
                            if (entryValue != null) {
                                if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }
            finally {
                this.postReadCleanup();
            }
        }
        
        @Nullable
        V put(final K key, final int hash, final V value, final boolean onlyIfAbsent) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     4: aload_0         /* this */
            //     5: getfield        com/google/common/cache/LocalCache$Segment.map:Lcom/google/common/cache/LocalCache;
            //     8: getfield        com/google/common/cache/LocalCache.ticker:Lcom/google/common/base/Ticker;
            //    11: invokevirtual   com/google/common/base/Ticker.read:()J
            //    14: lstore          now
            //    16: aload_0         /* this */
            //    17: lload           now
            //    19: invokevirtual   com/google/common/cache/LocalCache$Segment.preWriteCleanup:(J)V
            //    22: aload_0         /* this */
            //    23: getfield        com/google/common/cache/LocalCache$Segment.count:I
            //    26: iconst_1       
            //    27: iadd           
            //    28: istore          newCount
            //    30: iload           newCount
            //    32: aload_0         /* this */
            //    33: getfield        com/google/common/cache/LocalCache$Segment.threshold:I
            //    36: if_icmple       51
            //    39: aload_0         /* this */
            //    40: invokevirtual   com/google/common/cache/LocalCache$Segment.expand:()V
            //    43: aload_0         /* this */
            //    44: getfield        com/google/common/cache/LocalCache$Segment.count:I
            //    47: iconst_1       
            //    48: iadd           
            //    49: istore          newCount
            //    51: aload_0         /* this */
            //    52: getfield        com/google/common/cache/LocalCache$Segment.table:Ljava/util/concurrent/atomic/AtomicReferenceArray;
            //    55: astore          table
            //    57: iload_2         /* hash */
            //    58: aload           table
            //    60: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.length:()I
            //    63: iconst_1       
            //    64: isub           
            //    65: iand           
            //    66: istore          index
            //    68: aload           table
            //    70: iload           index
            //    72: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.get:(I)Ljava/lang/Object;
            //    75: checkcast       Lcom/google/common/cache/LocalCache$ReferenceEntry;
            //    78: astore          first
            //    80: aload           first
            //    82: astore          e
            //    84: aload           e
            //    86: ifnull          353
            //    89: aload           e
            //    91: invokeinterface com/google/common/cache/LocalCache$ReferenceEntry.getKey:()Ljava/lang/Object;
            //    96: astore          entryKey
            //    98: aload           e
            //   100: invokeinterface com/google/common/cache/LocalCache$ReferenceEntry.getHash:()I
            //   105: iload_2         /* hash */
            //   106: if_icmpne       341
            //   109: aload           entryKey
            //   111: ifnull          341
            //   114: aload_0         /* this */
            //   115: getfield        com/google/common/cache/LocalCache$Segment.map:Lcom/google/common/cache/LocalCache;
            //   118: getfield        com/google/common/cache/LocalCache.keyEquivalence:Lcom/google/common/base/Equivalence;
            //   121: aload_1         /* key */
            //   122: aload           entryKey
            //   124: invokevirtual   com/google/common/base/Equivalence.equivalent:(Ljava/lang/Object;Ljava/lang/Object;)Z
            //   127: ifeq            341
            //   130: aload           e
            //   132: invokeinterface com/google/common/cache/LocalCache$ReferenceEntry.getValueReference:()Lcom/google/common/cache/LocalCache$ValueReference;
            //   137: astore          valueReference
            //   139: aload           valueReference
            //   141: invokeinterface com/google/common/cache/LocalCache$ValueReference.get:()Ljava/lang/Object;
            //   146: astore          entryValue
            //   148: aload           entryValue
            //   150: ifnonnull       254
            //   153: aload_0         /* this */
            //   154: dup            
            //   155: getfield        com/google/common/cache/LocalCache$Segment.modCount:I
            //   158: iconst_1       
            //   159: iadd           
            //   160: putfield        com/google/common/cache/LocalCache$Segment.modCount:I
            //   163: aload           valueReference
            //   165: invokeinterface com/google/common/cache/LocalCache$ValueReference.isActive:()Z
            //   170: ifeq            210
            //   173: aload_0         /* this */
            //   174: aload_1         /* key */
            //   175: iload_2         /* hash */
            //   176: aload           entryValue
            //   178: aload           valueReference
            //   180: invokeinterface com/google/common/cache/LocalCache$ValueReference.getWeight:()I
            //   185: getstatic       com/google/common/cache/RemovalCause.COLLECTED:Lcom/google/common/cache/RemovalCause;
            //   188: invokevirtual   com/google/common/cache/LocalCache$Segment.enqueueNotification:(Ljava/lang/Object;ILjava/lang/Object;ILcom/google/common/cache/RemovalCause;)V
            //   191: aload_0         /* this */
            //   192: aload           e
            //   194: aload_1         /* key */
            //   195: aload_3         /* value */
            //   196: lload           now
            //   198: invokevirtual   com/google/common/cache/LocalCache$Segment.setValue:(Lcom/google/common/cache/LocalCache$ReferenceEntry;Ljava/lang/Object;Ljava/lang/Object;J)V
            //   201: aload_0         /* this */
            //   202: getfield        com/google/common/cache/LocalCache$Segment.count:I
            //   205: istore          newCount
            //   207: goto            228
            //   210: aload_0         /* this */
            //   211: aload           e
            //   213: aload_1         /* key */
            //   214: aload_3         /* value */
            //   215: lload           now
            //   217: invokevirtual   com/google/common/cache/LocalCache$Segment.setValue:(Lcom/google/common/cache/LocalCache$ReferenceEntry;Ljava/lang/Object;Ljava/lang/Object;J)V
            //   220: aload_0         /* this */
            //   221: getfield        com/google/common/cache/LocalCache$Segment.count:I
            //   224: iconst_1       
            //   225: iadd           
            //   226: istore          newCount
            //   228: aload_0         /* this */
            //   229: iload           newCount
            //   231: putfield        com/google/common/cache/LocalCache$Segment.count:I
            //   234: aload_0         /* this */
            //   235: aload           e
            //   237: invokevirtual   com/google/common/cache/LocalCache$Segment.evictEntries:(Lcom/google/common/cache/LocalCache$ReferenceEntry;)V
            //   240: aconst_null    
            //   241: astore          15
            //   243: aload_0         /* this */
            //   244: invokevirtual   com/google/common/cache/LocalCache$Segment.unlock:()V
            //   247: aload_0         /* this */
            //   248: invokevirtual   com/google/common/cache/LocalCache$Segment.postWriteCleanup:()V
            //   251: aload           15
            //   253: areturn        
            //   254: iload           onlyIfAbsent
            //   256: ifeq            282
            //   259: aload_0         /* this */
            //   260: aload           e
            //   262: lload           now
            //   264: invokevirtual   com/google/common/cache/LocalCache$Segment.recordLockedRead:(Lcom/google/common/cache/LocalCache$ReferenceEntry;J)V
            //   267: aload           entryValue
            //   269: astore          15
            //   271: aload_0         /* this */
            //   272: invokevirtual   com/google/common/cache/LocalCache$Segment.unlock:()V
            //   275: aload_0         /* this */
            //   276: invokevirtual   com/google/common/cache/LocalCache$Segment.postWriteCleanup:()V
            //   279: aload           15
            //   281: areturn        
            //   282: aload_0         /* this */
            //   283: dup            
            //   284: getfield        com/google/common/cache/LocalCache$Segment.modCount:I
            //   287: iconst_1       
            //   288: iadd           
            //   289: putfield        com/google/common/cache/LocalCache$Segment.modCount:I
            //   292: aload_0         /* this */
            //   293: aload_1         /* key */
            //   294: iload_2         /* hash */
            //   295: aload           entryValue
            //   297: aload           valueReference
            //   299: invokeinterface com/google/common/cache/LocalCache$ValueReference.getWeight:()I
            //   304: getstatic       com/google/common/cache/RemovalCause.REPLACED:Lcom/google/common/cache/RemovalCause;
            //   307: invokevirtual   com/google/common/cache/LocalCache$Segment.enqueueNotification:(Ljava/lang/Object;ILjava/lang/Object;ILcom/google/common/cache/RemovalCause;)V
            //   310: aload_0         /* this */
            //   311: aload           e
            //   313: aload_1         /* key */
            //   314: aload_3         /* value */
            //   315: lload           now
            //   317: invokevirtual   com/google/common/cache/LocalCache$Segment.setValue:(Lcom/google/common/cache/LocalCache$ReferenceEntry;Ljava/lang/Object;Ljava/lang/Object;J)V
            //   320: aload_0         /* this */
            //   321: aload           e
            //   323: invokevirtual   com/google/common/cache/LocalCache$Segment.evictEntries:(Lcom/google/common/cache/LocalCache$ReferenceEntry;)V
            //   326: aload           entryValue
            //   328: astore          15
            //   330: aload_0         /* this */
            //   331: invokevirtual   com/google/common/cache/LocalCache$Segment.unlock:()V
            //   334: aload_0         /* this */
            //   335: invokevirtual   com/google/common/cache/LocalCache$Segment.postWriteCleanup:()V
            //   338: aload           15
            //   340: areturn        
            //   341: aload           e
            //   343: invokeinterface com/google/common/cache/LocalCache$ReferenceEntry.getNext:()Lcom/google/common/cache/LocalCache$ReferenceEntry;
            //   348: astore          e
            //   350: goto            84
            //   353: aload_0         /* this */
            //   354: dup            
            //   355: getfield        com/google/common/cache/LocalCache$Segment.modCount:I
            //   358: iconst_1       
            //   359: iadd           
            //   360: putfield        com/google/common/cache/LocalCache$Segment.modCount:I
            //   363: aload_0         /* this */
            //   364: aload_1         /* key */
            //   365: iload_2         /* hash */
            //   366: aload           first
            //   368: invokevirtual   com/google/common/cache/LocalCache$Segment.newEntry:(Ljava/lang/Object;ILcom/google/common/cache/LocalCache$ReferenceEntry;)Lcom/google/common/cache/LocalCache$ReferenceEntry;
            //   371: astore          newEntry
            //   373: aload_0         /* this */
            //   374: aload           newEntry
            //   376: aload_1         /* key */
            //   377: aload_3         /* value */
            //   378: lload           now
            //   380: invokevirtual   com/google/common/cache/LocalCache$Segment.setValue:(Lcom/google/common/cache/LocalCache$ReferenceEntry;Ljava/lang/Object;Ljava/lang/Object;J)V
            //   383: aload           table
            //   385: iload           index
            //   387: aload           newEntry
            //   389: invokevirtual   java/util/concurrent/atomic/AtomicReferenceArray.set:(ILjava/lang/Object;)V
            //   392: aload_0         /* this */
            //   393: getfield        com/google/common/cache/LocalCache$Segment.count:I
            //   396: iconst_1       
            //   397: iadd           
            //   398: istore          newCount
            //   400: aload_0         /* this */
            //   401: iload           newCount
            //   403: putfield        com/google/common/cache/LocalCache$Segment.count:I
            //   406: aload_0         /* this */
            //   407: aload           newEntry
            //   409: invokevirtual   com/google/common/cache/LocalCache$Segment.evictEntries:(Lcom/google/common/cache/LocalCache$ReferenceEntry;)V
            //   412: aconst_null    
            //   413: astore          12
            //   415: aload_0         /* this */
            //   416: invokevirtual   com/google/common/cache/LocalCache$Segment.unlock:()V
            //   419: aload_0         /* this */
            //   420: invokevirtual   com/google/common/cache/LocalCache$Segment.postWriteCleanup:()V
            //   423: aload           12
            //   425: areturn        
            //   426: astore          16
            //   428: aload_0         /* this */
            //   429: invokevirtual   com/google/common/cache/LocalCache$Segment.unlock:()V
            //   432: aload_0         /* this */
            //   433: invokevirtual   com/google/common/cache/LocalCache$Segment.postWriteCleanup:()V
            //   436: aload           16
            //   438: athrow         
            //    Signature:
            //  (TK;ITV;Z)TV;
            //    MethodParameters:
            //  Name          Flags  
            //  ------------  -----
            //  key           
            //  hash          
            //  value         
            //  onlyIfAbsent  
            //    StackMapTable: 00 09 FD 00 33 04 01 FF 00 20 00 0B 07 00 02 07 00 CB 01 07 00 CB 01 04 01 07 00 94 01 07 00 0D 07 00 0D 00 00 FE 00 7D 07 00 CB 07 00 15 07 00 CB 11 19 1B F8 00 3A FA 00 0B FF 00 48 00 05 07 00 02 07 00 CB 01 07 00 CB 01 00 01 07 01 3F
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  4      243    426    439    Any
            //  254    271    426    439    Any
            //  282    330    426    439    Any
            //  341    415    426    439    Any
            //  426    428    426    439    Any
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2780)
            //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2760)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.MetadataHelper.erase(MetadataHelper.java:1661)
            //     at com.strobel.assembler.metadata.MetadataHelper.eraseRecursive(MetadataHelper.java:1642)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
            //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
            //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
            //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
            //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
            //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
            //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
            //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
            //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
            //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
            //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
            //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @GuardedBy("this")
        void expand() {
            final AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = this.table;
            final int oldCapacity = oldTable.length();
            if (oldCapacity >= 1073741824) {
                return;
            }
            int newCount = this.count;
            final AtomicReferenceArray<ReferenceEntry<K, V>> newTable = this.newEntryArray(oldCapacity << 1);
            this.threshold = newTable.length() * 3 / 4;
            final int newMask = newTable.length() - 1;
            for (int oldIndex = 0; oldIndex < oldCapacity; ++oldIndex) {
                final ReferenceEntry<K, V> head = (ReferenceEntry<K, V>)oldTable.get(oldIndex);
                if (head != null) {
                    final ReferenceEntry<K, V> next = head.getNext();
                    final int headIndex = head.getHash() & newMask;
                    if (next == null) {
                        newTable.set(headIndex, head);
                    }
                    else {
                        ReferenceEntry<K, V> tail = head;
                        int tailIndex = headIndex;
                        for (ReferenceEntry<K, V> e = next; e != null; e = e.getNext()) {
                            final int newIndex = e.getHash() & newMask;
                            if (newIndex != tailIndex) {
                                tailIndex = newIndex;
                                tail = e;
                            }
                        }
                        newTable.set(tailIndex, tail);
                        for (ReferenceEntry<K, V> e = head; e != tail; e = e.getNext()) {
                            final int newIndex = e.getHash() & newMask;
                            final ReferenceEntry<K, V> newNext = (ReferenceEntry<K, V>)newTable.get(newIndex);
                            final ReferenceEntry<K, V> newFirst = this.copyEntry(e, newNext);
                            if (newFirst != null) {
                                newTable.set(newIndex, newFirst);
                            }
                            else {
                                this.removeCollectedEntry(e);
                                --newCount;
                            }
                        }
                    }
                }
            }
            this.table = newTable;
            this.count = newCount;
        }
        
        boolean replace(final K key, final int hash, final V oldValue, final V newValue) {
            this.lock();
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (valueReference.isActive()) {
                                int newCount = this.count - 1;
                                ++this.modCount;
                                final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount;
                            }
                            return false;
                        }
                        if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
                            ++this.modCount;
                            this.enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                            this.setValue(e, key, newValue, now);
                            this.evictEntries(e);
                            return true;
                        }
                        this.recordLockedRead(e, now);
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        @Nullable
        V replace(final K key, final int hash, final V newValue) {
            this.lock();
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        if (entryValue == null) {
                            if (valueReference.isActive()) {
                                int newCount = this.count - 1;
                                ++this.modCount;
                                final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount;
                            }
                            return null;
                        }
                        ++this.modCount;
                        this.enqueueNotification(key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                        this.setValue(e, key, newValue, now);
                        this.evictEntries(e);
                        return entryValue;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        @Nullable
        V remove(final Object key, final int hash) {
            this.lock();
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                for (ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index); e != null; e = e.getNext()) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        RemovalCause cause;
                        if (entryValue != null) {
                            cause = RemovalCause.EXPLICIT;
                        }
                        else {
                            if (!valueReference.isActive()) {
                                return null;
                            }
                            cause = RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return entryValue;
                    }
                }
                return null;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean storeLoadedValue(final K key, final int hash, final LoadingValueReference<K, V> oldValueReference, final V newValue) {
            this.lock();
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                int newCount = this.count + 1;
                if (newCount > this.threshold) {
                    this.expand();
                    newCount = this.count + 1;
                }
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        if (oldValueReference == valueReference || (entryValue == null && valueReference != LocalCache.UNSET)) {
                            ++this.modCount;
                            if (oldValueReference.isActive()) {
                                final RemovalCause cause = (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                                this.enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), cause);
                                --newCount;
                            }
                            this.setValue(e, key, newValue, now);
                            this.count = newCount;
                            this.evictEntries(e);
                            return true;
                        }
                        this.enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                ++this.modCount;
                final ReferenceEntry<K, V> newEntry = this.newEntry(key, hash, first);
                this.setValue(newEntry, key, newValue, now);
                table.set(index, newEntry);
                this.count = newCount;
                this.evictEntries(newEntry);
                return true;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean remove(final Object key, final int hash, final Object value) {
            this.lock();
            try {
                final long now = this.map.ticker.read();
                this.preWriteCleanup(now);
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                for (ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index); e != null; e = e.getNext()) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> valueReference = e.getValueReference();
                        final V entryValue = valueReference.get();
                        RemovalCause cause;
                        if (this.map.valueEquivalence.equivalent(value, entryValue)) {
                            cause = RemovalCause.EXPLICIT;
                        }
                        else {
                            if (entryValue != null || !valueReference.isActive()) {
                                return false;
                            }
                            cause = RemovalCause.COLLECTED;
                        }
                        ++this.modCount;
                        final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return cause == RemovalCause.EXPLICIT;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        void clear() {
            if (this.count != 0) {
                this.lock();
                try {
                    final long now = this.map.ticker.read();
                    this.preWriteCleanup(now);
                    final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    for (int i = 0; i < table.length(); ++i) {
                        for (ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)table.get(i); e != null; e = e.getNext()) {
                            if (e.getValueReference().isActive()) {
                                final K key = e.getKey();
                                final V value = e.getValueReference().get();
                                final RemovalCause cause = (key == null || value == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT;
                                this.enqueueNotification(key, e.getHash(), value, e.getValueReference().getWeight(), cause);
                            }
                        }
                    }
                    for (int i = 0; i < table.length(); ++i) {
                        table.set(i, null);
                    }
                    this.clearReferenceQueues();
                    this.writeQueue.clear();
                    this.accessQueue.clear();
                    this.readCount.set(0);
                    ++this.modCount;
                    this.count = 0;
                }
                finally {
                    this.unlock();
                    this.postWriteCleanup();
                }
            }
        }
        
        @Nullable
        @GuardedBy("this")
        ReferenceEntry<K, V> removeValueFromChain(final ReferenceEntry<K, V> first, final ReferenceEntry<K, V> entry, @Nullable final K key, final int hash, final V value, final ValueReference<K, V> valueReference, final RemovalCause cause) {
            this.enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
            this.writeQueue.remove(entry);
            this.accessQueue.remove(entry);
            if (valueReference.isLoading()) {
                valueReference.notifyNewValue(null);
                return first;
            }
            return this.removeEntryFromChain(first, entry);
        }
        
        @Nullable
        @GuardedBy("this")
        ReferenceEntry<K, V> removeEntryFromChain(final ReferenceEntry<K, V> first, final ReferenceEntry<K, V> entry) {
            int newCount = this.count;
            ReferenceEntry<K, V> newFirst = entry.getNext();
            for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
                final ReferenceEntry<K, V> next = this.copyEntry(e, newFirst);
                if (next != null) {
                    newFirst = next;
                }
                else {
                    this.removeCollectedEntry(e);
                    --newCount;
                }
            }
            this.count = newCount;
            return newFirst;
        }
        
        @GuardedBy("this")
        void removeCollectedEntry(final ReferenceEntry<K, V> entry) {
            this.enqueueNotification(entry.getKey(), entry.getHash(), entry.getValueReference().get(), entry.getValueReference().getWeight(), RemovalCause.COLLECTED);
            this.writeQueue.remove(entry);
            this.accessQueue.remove(entry);
        }
        
        boolean reclaimKey(final ReferenceEntry<K, V> entry, final int hash) {
            this.lock();
            try {
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                for (ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index); e != null; e = e.getNext()) {
                    if (e == entry) {
                        ++this.modCount;
                        final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference().get(), e.getValueReference(), RemovalCause.COLLECTED);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount;
                        return true;
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        boolean reclaimValue(final K key, final int hash, final ValueReference<K, V> valueReference) {
            this.lock();
            try {
                int newCount = this.count - 1;
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> v = e.getValueReference();
                        if (v == valueReference) {
                            ++this.modCount;
                            final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, entryKey, hash, valueReference.get(), valueReference, RemovalCause.COLLECTED);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount;
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                if (!this.isHeldByCurrentThread()) {
                    this.postWriteCleanup();
                }
            }
        }
        
        boolean removeLoadingValue(final K key, final int hash, final LoadingValueReference<K, V> valueReference) {
            this.lock();
            try {
                final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                final int index = hash & table.length() - 1;
                ReferenceEntry<K, V> e;
                final ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index);
                while (e != null) {
                    final K entryKey = e.getKey();
                    if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence.equivalent(key, entryKey)) {
                        final ValueReference<K, V> v = e.getValueReference();
                        if (v == valueReference) {
                            if (valueReference.isActive()) {
                                e.setValueReference(valueReference.getOldValue());
                            }
                            else {
                                final ReferenceEntry<K, V> newFirst = this.removeEntryFromChain(first, e);
                                table.set(index, newFirst);
                            }
                            return true;
                        }
                        return false;
                    }
                    else {
                        e = e.getNext();
                    }
                }
                return false;
            }
            finally {
                this.unlock();
                this.postWriteCleanup();
            }
        }
        
        @VisibleForTesting
        @GuardedBy("this")
        boolean removeEntry(final ReferenceEntry<K, V> entry, final int hash, final RemovalCause cause) {
            int newCount = this.count - 1;
            final AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
            final int index = hash & table.length() - 1;
            ReferenceEntry<K, V> e;
            for (ReferenceEntry<K, V> first = e = (ReferenceEntry<K, V>)table.get(index); e != null; e = e.getNext()) {
                if (e == entry) {
                    ++this.modCount;
                    final ReferenceEntry<K, V> newFirst = this.removeValueFromChain(first, e, e.getKey(), hash, e.getValueReference().get(), e.getValueReference(), cause);
                    newCount = this.count - 1;
                    table.set(index, newFirst);
                    this.count = newCount;
                    return true;
                }
            }
            return false;
        }
        
        void postReadCleanup() {
            if ((this.readCount.incrementAndGet() & 0x3F) == 0x0) {
                this.cleanUp();
            }
        }
        
        @GuardedBy("this")
        void preWriteCleanup(final long now) {
            this.runLockedCleanup(now);
        }
        
        void postWriteCleanup() {
            this.runUnlockedCleanup();
        }
        
        void cleanUp() {
            final long now = this.map.ticker.read();
            this.runLockedCleanup(now);
            this.runUnlockedCleanup();
        }
        
        void runLockedCleanup(final long now) {
            if (this.tryLock()) {
                try {
                    this.drainReferenceQueues();
                    this.expireEntries(now);
                    this.readCount.set(0);
                }
                finally {
                    this.unlock();
                }
            }
        }
        
        void runUnlockedCleanup() {
            if (!this.isHeldByCurrentThread()) {
                this.map.processPendingNotifications();
            }
        }
    }
    
    static class LoadingValueReference<K, V> implements ValueReference<K, V> {
        volatile ValueReference<K, V> oldValue;
        final SettableFuture<V> futureValue;
        final Stopwatch stopwatch;
        
        public LoadingValueReference() {
            this(null);
        }
        
        public LoadingValueReference(final ValueReference<K, V> oldValue) {
            this.futureValue = SettableFuture.<V>create();
            this.stopwatch = Stopwatch.createUnstarted();
            this.oldValue = ((oldValue == null) ? LocalCache.<K, V>unset() : oldValue);
        }
        
        public boolean isLoading() {
            return true;
        }
        
        public boolean isActive() {
            return this.oldValue.isActive();
        }
        
        public int getWeight() {
            return this.oldValue.getWeight();
        }
        
        public boolean set(@Nullable final V newValue) {
            return this.futureValue.set(newValue);
        }
        
        public boolean setException(final Throwable t) {
            return this.futureValue.setException(t);
        }
        
        private ListenableFuture<V> fullyFailedFuture(final Throwable t) {
            return Futures.<V>immediateFailedFuture(t);
        }
        
        public void notifyNewValue(@Nullable final V newValue) {
            if (newValue != null) {
                this.set(newValue);
            }
            else {
                this.oldValue = LocalCache.<K, V>unset();
            }
        }
        
        public ListenableFuture<V> loadFuture(final K key, final CacheLoader<? super K, V> loader) {
            try {
                this.stopwatch.start();
                final V previousValue = this.oldValue.get();
                if (previousValue == null) {
                    final V newValue = loader.load(key);
                    return this.set(newValue) ? this.futureValue : Futures.<V>immediateFuture(newValue);
                }
                final ListenableFuture<V> newValue2 = loader.reload(key, previousValue);
                if (newValue2 == null) {
                    return Futures.<V>immediateFuture((V)null);
                }
                return Futures.<V, V>transform(newValue2, new com.google.common.base.Function<V, V>() {
                    public V apply(final V newValue) {
                        LoadingValueReference.this.set(newValue);
                        return newValue;
                    }
                });
            }
            catch (Throwable t) {
                final ListenableFuture<V> result = this.setException(t) ? this.futureValue : this.fullyFailedFuture(t);
                if (t instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }
                return result;
            }
        }
        
        public V compute(final K key, final BiFunction<? super K, ? super V, ? extends V> function) {
            this.stopwatch.start();
            V previousValue;
            try {
                previousValue = this.oldValue.waitForValue();
            }
            catch (ExecutionException e) {
                previousValue = null;
            }
            final V newValue = (V)function.apply(key, previousValue);
            this.set(newValue);
            return newValue;
        }
        
        public long elapsedNanos() {
            return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
        }
        
        public V waitForValue() throws ExecutionException {
            return Uninterruptibles.<V>getUninterruptibly((java.util.concurrent.Future<V>)this.futureValue);
        }
        
        public V get() {
            return this.oldValue.get();
        }
        
        public ValueReference<K, V> getOldValue() {
            return this.oldValue;
        }
        
        public ReferenceEntry<K, V> getEntry() {
            return null;
        }
        
        public ValueReference<K, V> copyFor(final ReferenceQueue<V> queue, @Nullable final V value, final ReferenceEntry<K, V> entry) {
            return this;
        }
    }
    
    static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;
        
        WriteQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextWrite = this;
                ReferenceEntry<K, V> previousWrite = this;
                
                @Override
                public long getWriteTime() {
                    return Long.MAX_VALUE;
                }
                
                @Override
                public void setWriteTime(final long time) {
                }
                
                @Override
                public ReferenceEntry<K, V> getNextInWriteQueue() {
                    return this.nextWrite;
                }
                
                @Override
                public void setNextInWriteQueue(final ReferenceEntry<K, V> next) {
                    this.nextWrite = next;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                    return this.previousWrite;
                }
                
                @Override
                public void setPreviousInWriteQueue(final ReferenceEntry<K, V> previous) {
                    this.previousWrite = previous;
                }
            };
        }
        
        public boolean offer(final ReferenceEntry<K, V> entry) {
            LocalCache.<K, V>connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
            LocalCache.<K, V>connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
            LocalCache.<K, V>connectWriteOrder(entry, this.head);
            return true;
        }
        
        public ReferenceEntry<K, V> peek() {
            final ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
            return (next == this.head) ? null : next;
        }
        
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
            if (next == this.head) {
                return null;
            }
            this.remove(next);
            return next;
        }
        
        public boolean remove(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            final ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
            final ReferenceEntry<K, V> next = e.getNextInWriteQueue();
            LocalCache.<K, V>connectWriteOrder(previous, next);
            LocalCache.<K, V>nullifyWriteOrder(e);
            return next != NullEntry.INSTANCE;
        }
        
        public boolean contains(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            return e.getNextInWriteQueue() != NullEntry.INSTANCE;
        }
        
        public boolean isEmpty() {
            return this.head.getNextInWriteQueue() == this.head;
        }
        
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextInWriteQueue(); e != this.head; e = e.getNextInWriteQueue()) {
                ++size;
            }
            return size;
        }
        
        public void clear() {
            ReferenceEntry<K, V> next;
            for (ReferenceEntry<K, V> e = this.head.getNextInWriteQueue(); e != this.head; e = next) {
                next = e.getNextInWriteQueue();
                LocalCache.<K, V>nullifyWriteOrder(e);
            }
            this.head.setNextInWriteQueue(this.head);
            this.head.setPreviousInWriteQueue(this.head);
        }
        
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return (Iterator<ReferenceEntry<K, V>>)new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> previous) {
                    final ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
                    return (next == WriteQueue.this.head) ? null : next;
                }
            };
        }
    }
    
    static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
        final ReferenceEntry<K, V> head;
        
        AccessQueue() {
            this.head = new AbstractReferenceEntry<K, V>() {
                ReferenceEntry<K, V> nextAccess = this;
                ReferenceEntry<K, V> previousAccess = this;
                
                @Override
                public long getAccessTime() {
                    return Long.MAX_VALUE;
                }
                
                @Override
                public void setAccessTime(final long time) {
                }
                
                @Override
                public ReferenceEntry<K, V> getNextInAccessQueue() {
                    return this.nextAccess;
                }
                
                @Override
                public void setNextInAccessQueue(final ReferenceEntry<K, V> next) {
                    this.nextAccess = next;
                }
                
                @Override
                public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                    return this.previousAccess;
                }
                
                @Override
                public void setPreviousInAccessQueue(final ReferenceEntry<K, V> previous) {
                    this.previousAccess = previous;
                }
            };
        }
        
        public boolean offer(final ReferenceEntry<K, V> entry) {
            LocalCache.<K, V>connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
            LocalCache.<K, V>connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
            LocalCache.<K, V>connectAccessOrder(entry, this.head);
            return true;
        }
        
        public ReferenceEntry<K, V> peek() {
            final ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
            return (next == this.head) ? null : next;
        }
        
        public ReferenceEntry<K, V> poll() {
            final ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
            if (next == this.head) {
                return null;
            }
            this.remove(next);
            return next;
        }
        
        public boolean remove(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            final ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
            final ReferenceEntry<K, V> next = e.getNextInAccessQueue();
            LocalCache.<K, V>connectAccessOrder(previous, next);
            LocalCache.<K, V>nullifyAccessOrder(e);
            return next != NullEntry.INSTANCE;
        }
        
        public boolean contains(final Object o) {
            final ReferenceEntry<K, V> e = (ReferenceEntry<K, V>)o;
            return e.getNextInAccessQueue() != NullEntry.INSTANCE;
        }
        
        public boolean isEmpty() {
            return this.head.getNextInAccessQueue() == this.head;
        }
        
        public int size() {
            int size = 0;
            for (ReferenceEntry<K, V> e = this.head.getNextInAccessQueue(); e != this.head; e = e.getNextInAccessQueue()) {
                ++size;
            }
            return size;
        }
        
        public void clear() {
            ReferenceEntry<K, V> next;
            for (ReferenceEntry<K, V> e = this.head.getNextInAccessQueue(); e != this.head; e = next) {
                next = e.getNextInAccessQueue();
                LocalCache.<K, V>nullifyAccessOrder(e);
            }
            this.head.setNextInAccessQueue(this.head);
            this.head.setPreviousInAccessQueue(this.head);
        }
        
        public Iterator<ReferenceEntry<K, V>> iterator() {
            return (Iterator<ReferenceEntry<K, V>>)new AbstractSequentialIterator<ReferenceEntry<K, V>>(this.peek()) {
                @Override
                protected ReferenceEntry<K, V> computeNext(final ReferenceEntry<K, V> previous) {
                    final ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
                    return (next == AccessQueue.this.head) ? null : next;
                }
            };
        }
    }
    
    abstract class HashIterator<T> implements Iterator<T> {
        int nextSegmentIndex;
        int nextTableIndex;
        Segment<K, V> currentSegment;
        AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
        ReferenceEntry<K, V> nextEntry;
        WriteThroughEntry nextExternal;
        WriteThroughEntry lastReturned;
        
        HashIterator() {
            this.nextSegmentIndex = LocalCache.this.segments.length - 1;
            this.nextTableIndex = -1;
            this.advance();
        }
        
        public abstract T next();
        
        final void advance() {
            this.nextExternal = null;
            if (this.nextInChain()) {
                return;
            }
            if (this.nextInTable()) {
                return;
            }
            while (this.nextSegmentIndex >= 0) {
                this.currentSegment = LocalCache.this.segments[this.nextSegmentIndex--];
                if (this.currentSegment.count != 0) {
                    this.currentTable = this.currentSegment.table;
                    this.nextTableIndex = this.currentTable.length() - 1;
                    if (this.nextInTable()) {
                        return;
                    }
                    continue;
                }
            }
        }
        
        boolean nextInChain() {
            if (this.nextEntry != null) {
                this.nextEntry = this.nextEntry.getNext();
                while (this.nextEntry != null) {
                    if (this.advanceTo(this.nextEntry)) {
                        return true;
                    }
                    this.nextEntry = this.nextEntry.getNext();
                }
            }
            return false;
        }
        
        boolean nextInTable() {
            while (this.nextTableIndex >= 0) {
                final ReferenceEntry nextEntry = (ReferenceEntry)this.currentTable.get(this.nextTableIndex--);
                this.nextEntry = (ReferenceEntry<K, V>)nextEntry;
                if (nextEntry != null && (this.advanceTo(this.nextEntry) || this.nextInChain())) {
                    return true;
                }
            }
            return false;
        }
        
        boolean advanceTo(final ReferenceEntry<K, V> entry) {
            try {
                final long now = LocalCache.this.ticker.read();
                final K key = entry.getKey();
                final V value = LocalCache.this.getLiveValue(entry, now);
                if (value != null) {
                    this.nextExternal = new WriteThroughEntry(key, value);
                    return true;
                }
                return false;
            }
            finally {
                this.currentSegment.postReadCleanup();
            }
        }
        
        public boolean hasNext() {
            return this.nextExternal != null;
        }
        
        WriteThroughEntry nextEntry() {
            if (this.nextExternal == null) {
                throw new NoSuchElementException();
            }
            this.lastReturned = this.nextExternal;
            this.advance();
            return this.lastReturned;
        }
        
        public void remove() {
            Preconditions.checkState(this.lastReturned != null);
            LocalCache.this.remove(this.lastReturned.getKey());
            this.lastReturned = null;
        }
    }
    
    final class KeyIterator extends HashIterator<K> {
        @Override
        public K next() {
            return this.nextEntry().getKey();
        }
    }
    
    final class ValueIterator extends HashIterator<V> {
        @Override
        public V next() {
            return this.nextEntry().getValue();
        }
    }
    
    final class WriteThroughEntry implements Map.Entry<K, V> {
        final K key;
        V value;
        
        WriteThroughEntry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return this.key;
        }
        
        public V getValue() {
            return this.value;
        }
        
        public boolean equals(@Nullable final Object object) {
            if (object instanceof Map.Entry) {
                final Map.Entry<?, ?> that = object;
                return this.key.equals(that.getKey()) && this.value.equals(that.getValue());
            }
            return false;
        }
        
        public int hashCode() {
            return this.key.hashCode() ^ this.value.hashCode();
        }
        
        public V setValue(final V newValue) {
            final V oldValue = LocalCache.this.put(this.key, newValue);
            this.value = newValue;
            return oldValue;
        }
        
        public String toString() {
            return new StringBuilder().append(this.getKey()).append("=").append(this.getValue()).toString();
        }
    }
    
    final class EntryIterator extends HashIterator<Map.Entry<K, V>> {
        @Override
        public Map.Entry<K, V> next() {
            return (Map.Entry<K, V>)this.nextEntry();
        }
    }
    
    abstract class AbstractCacheSet<T> extends AbstractSet<T> {
        @Weak
        final ConcurrentMap<?, ?> map;
        
        AbstractCacheSet(final ConcurrentMap<?, ?> map) {
            this.map = map;
        }
        
        public int size() {
            return this.map.size();
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        public void clear() {
            this.map.clear();
        }
        
        public Object[] toArray() {
            return LocalCache.toArrayList((java.util.Collection<Object>)this).toArray();
        }
        
        public <E> E[] toArray(final E[] a) {
            return (E[])LocalCache.toArrayList((java.util.Collection<Object>)this).toArray((Object[])a);
        }
    }
    
    final class KeySet extends AbstractCacheSet<K> {
        KeySet(final ConcurrentMap<?, ?> map) {
            super(map);
        }
        
        public Iterator<K> iterator() {
            return (Iterator<K>)new KeyIterator();
        }
        
        public boolean contains(final Object o) {
            return this.map.containsKey(o);
        }
        
        public boolean remove(final Object o) {
            return this.map.remove(o) != null;
        }
    }
    
    final class Values extends AbstractCollection<V> {
        private final ConcurrentMap<?, ?> map;
        
        Values(final ConcurrentMap<?, ?> map) {
            this.map = map;
        }
        
        public int size() {
            return this.map.size();
        }
        
        public boolean isEmpty() {
            return this.map.isEmpty();
        }
        
        public void clear() {
            this.map.clear();
        }
        
        public Iterator<V> iterator() {
            return (Iterator<V>)new ValueIterator();
        }
        
        public boolean removeIf(final Predicate<? super V> filter) {
            Preconditions.<Predicate<? super V>>checkNotNull(filter);
            return LocalCache.this.removeIf((k, v) -> filter.test(v));
        }
        
        public boolean contains(final Object o) {
            return this.map.containsValue(o);
        }
        
        public Object[] toArray() {
            return LocalCache.toArrayList((java.util.Collection<Object>)this).toArray();
        }
        
        public <E> E[] toArray(final E[] a) {
            return (E[])LocalCache.toArrayList((java.util.Collection<Object>)this).toArray((Object[])a);
        }
    }
    
    final class EntrySet extends AbstractCacheSet<Map.Entry<K, V>> {
        EntrySet(final ConcurrentMap<?, ?> map) {
            super(map);
        }
        
        public Iterator<Map.Entry<K, V>> iterator() {
            return (Iterator<Map.Entry<K, V>>)new EntryIterator();
        }
        
        public boolean removeIf(final Predicate<? super Map.Entry<K, V>> filter) {
            Preconditions.<Predicate<? super Map.Entry<K, V>>>checkNotNull(filter);
            return LocalCache.this.removeIf((k, v) -> filter.test(Maps.immutableEntry(k, v)));
        }
        
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final Object key = e.getKey();
            if (key == null) {
                return false;
            }
            final V v = LocalCache.this.get(key);
            return v != null && LocalCache.this.valueEquivalence.equivalent(e.getValue(), v);
        }
        
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = o;
            final Object key = e.getKey();
            return key != null && LocalCache.this.remove(key, e.getValue());
        }
    }
    
    static class ManualSerializationProxy<K, V> extends ForwardingCache<K, V> implements Serializable {
        private static final long serialVersionUID = 1L;
        final Strength keyStrength;
        final Strength valueStrength;
        final Equivalence<Object> keyEquivalence;
        final Equivalence<Object> valueEquivalence;
        final long expireAfterWriteNanos;
        final long expireAfterAccessNanos;
        final long maxWeight;
        final Weigher<K, V> weigher;
        final int concurrencyLevel;
        final RemovalListener<? super K, ? super V> removalListener;
        final Ticker ticker;
        final CacheLoader<? super K, V> loader;
        transient Cache<K, V> delegate;
        
        ManualSerializationProxy(final LocalCache<K, V> cache) {
            this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
        }
        
        private ManualSerializationProxy(final Strength keyStrength, final Strength valueStrength, final Equivalence<Object> keyEquivalence, final Equivalence<Object> valueEquivalence, final long expireAfterWriteNanos, final long expireAfterAccessNanos, final long maxWeight, final Weigher<K, V> weigher, final int concurrencyLevel, final RemovalListener<? super K, ? super V> removalListener, final Ticker ticker, final CacheLoader<? super K, V> loader) {
            this.keyStrength = keyStrength;
            this.valueStrength = valueStrength;
            this.keyEquivalence = keyEquivalence;
            this.valueEquivalence = valueEquivalence;
            this.expireAfterWriteNanos = expireAfterWriteNanos;
            this.expireAfterAccessNanos = expireAfterAccessNanos;
            this.maxWeight = maxWeight;
            this.weigher = weigher;
            this.concurrencyLevel = concurrencyLevel;
            this.removalListener = removalListener;
            this.ticker = ((ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) ? null : ticker);
            this.loader = loader;
        }
        
        CacheBuilder<K, V> recreateCacheBuilder() {
            final CacheBuilder<K, V> builder = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).<K, V>removalListener(this.removalListener);
            builder.strictParsing = false;
            if (this.expireAfterWriteNanos > 0L) {
                builder.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
            }
            if (this.expireAfterAccessNanos > 0L) {
                builder.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
            }
            if (this.weigher != CacheBuilder.OneWeigher.INSTANCE) {
                builder.weigher(this.weigher);
                if (this.maxWeight != -1L) {
                    builder.maximumWeight(this.maxWeight);
                }
            }
            else if (this.maxWeight != -1L) {
                builder.maximumSize(this.maxWeight);
            }
            if (this.ticker != null) {
                builder.ticker(this.ticker);
            }
            return builder;
        }
        
        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final CacheBuilder<K, V> builder = this.recreateCacheBuilder();
            this.delegate = builder.<K, V>build();
        }
        
        private Object readResolve() {
            return this.delegate;
        }
        
        @Override
        protected Cache<K, V> delegate() {
            return this.delegate;
        }
    }
    
    static final class LoadingSerializationProxy<K, V> extends ManualSerializationProxy<K, V> implements LoadingCache<K, V>, Serializable {
        private static final long serialVersionUID = 1L;
        transient LoadingCache<K, V> autoDelegate;
        
        LoadingSerializationProxy(final LocalCache<K, V> cache) {
            super(cache);
        }
        
        private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            final CacheBuilder<K, V> builder = this.recreateCacheBuilder();
            this.autoDelegate = builder.<K, V>build(this.loader);
        }
        
        @Override
        public V get(final K key) throws ExecutionException {
            return this.autoDelegate.get(key);
        }
        
        @Override
        public V getUnchecked(final K key) {
            return this.autoDelegate.getUnchecked(key);
        }
        
        @Override
        public ImmutableMap<K, V> getAll(final Iterable<? extends K> keys) throws ExecutionException {
            return this.autoDelegate.getAll(keys);
        }
        
        @Override
        public final V apply(final K key) {
            return this.autoDelegate.apply(key);
        }
        
        @Override
        public void refresh(final K key) {
            this.autoDelegate.refresh(key);
        }
        
        private Object readResolve() {
            return this.autoDelegate;
        }
    }
    
    static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
        final LocalCache<K, V> localCache;
        private static final long serialVersionUID = 1L;
        
        LocalManualCache(final CacheBuilder<? super K, ? super V> builder) {
            this(new LocalCache<Object, Object>(builder, null));
        }
        
        private LocalManualCache(final LocalCache<K, V> localCache) {
            this.localCache = localCache;
        }
        
        @Nullable
        public V getIfPresent(final Object key) {
            return this.localCache.getIfPresent(key);
        }
        
        public V get(final K key, final Callable<? extends V> valueLoader) throws ExecutionException {
            Preconditions.<Callable<? extends V>>checkNotNull(valueLoader);
            return this.localCache.get(key, new CacheLoader<Object, V>() {
                @Override
                public V load(final Object key) throws Exception {
                    return (V)valueLoader.call();
                }
            });
        }
        
        public ImmutableMap<K, V> getAllPresent(final Iterable<?> keys) {
            return this.localCache.getAllPresent(keys);
        }
        
        public void put(final K key, final V value) {
            this.localCache.put(key, value);
        }
        
        public void putAll(final Map<? extends K, ? extends V> m) {
            this.localCache.putAll(m);
        }
        
        public void invalidate(final Object key) {
            Preconditions.checkNotNull(key);
            this.localCache.remove(key);
        }
        
        public void invalidateAll(final Iterable<?> keys) {
            this.localCache.invalidateAll(keys);
        }
        
        public void invalidateAll() {
            this.localCache.clear();
        }
        
        public long size() {
            return this.localCache.longSize();
        }
        
        public ConcurrentMap<K, V> asMap() {
            return (ConcurrentMap<K, V>)this.localCache;
        }
        
        public CacheStats stats() {
            final AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
            aggregator.incrementBy(this.localCache.globalStatsCounter);
            for (final Segment<K, V> segment : this.localCache.segments) {
                aggregator.incrementBy(segment.statsCounter);
            }
            return aggregator.snapshot();
        }
        
        public void cleanUp() {
            this.localCache.cleanUp();
        }
        
        Object writeReplace() {
            return new ManualSerializationProxy((LocalCache<Object, Object>)this.localCache);
        }
    }
    
    static class LocalLoadingCache<K, V> extends LocalManualCache<K, V> implements LoadingCache<K, V> {
        private static final long serialVersionUID = 1L;
        
        LocalLoadingCache(final CacheBuilder<? super K, ? super V> builder, final CacheLoader<? super K, V> loader) {
            super((LocalCache)new LocalCache(builder, Preconditions.<CacheLoader<? super K, V>>checkNotNull(loader)));
        }
        
        @Override
        public V get(final K key) throws ExecutionException {
            return this.localCache.getOrLoad(key);
        }
        
        @Override
        public V getUnchecked(final K key) {
            try {
                return this.get(key);
            }
            catch (ExecutionException e) {
                throw new UncheckedExecutionException(e.getCause());
            }
        }
        
        @Override
        public ImmutableMap<K, V> getAll(final Iterable<? extends K> keys) throws ExecutionException {
            return this.localCache.getAll(keys);
        }
        
        @Override
        public void refresh(final K key) {
            this.localCache.refresh(key);
        }
        
        @Override
        public final V apply(final K key) {
            return this.getUnchecked(key);
        }
        
        @Override
        Object writeReplace() {
            return new LoadingSerializationProxy((LocalCache<Object, Object>)this.localCache);
        }
    }
    
    interface ReferenceEntry<K, V> {
        ValueReference<K, V> getValueReference();
        
        void setValueReference(final ValueReference<K, V> valueReference);
        
        @Nullable
        ReferenceEntry<K, V> getNext();
        
        int getHash();
        
        @Nullable
        K getKey();
        
        long getAccessTime();
        
        void setAccessTime(final long long1);
        
        ReferenceEntry<K, V> getNextInAccessQueue();
        
        void setNextInAccessQueue(final ReferenceEntry<K, V> referenceEntry);
        
        ReferenceEntry<K, V> getPreviousInAccessQueue();
        
        void setPreviousInAccessQueue(final ReferenceEntry<K, V> referenceEntry);
        
        long getWriteTime();
        
        void setWriteTime(final long long1);
        
        ReferenceEntry<K, V> getNextInWriteQueue();
        
        void setNextInWriteQueue(final ReferenceEntry<K, V> referenceEntry);
        
        ReferenceEntry<K, V> getPreviousInWriteQueue();
        
        void setPreviousInWriteQueue(final ReferenceEntry<K, V> referenceEntry);
    }
    
    interface ValueReference<K, V> {
        @Nullable
        V get();
        
        V waitForValue() throws ExecutionException;
        
        int getWeight();
        
        @Nullable
        ReferenceEntry<K, V> getEntry();
        
        ValueReference<K, V> copyFor(final ReferenceQueue<V> referenceQueue, @Nullable final V object, final ReferenceEntry<K, V> referenceEntry);
        
        void notifyNewValue(@Nullable final V object);
        
        boolean isLoading();
        
        boolean isActive();
    }
}
