package io.netty.util.concurrent;

import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.ObjectCleaner;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import io.netty.util.internal.InternalThreadLocalMap;

public class FastThreadLocal<V> {
    private static final int variablesToRemoveIndex;
    private final int index;
    
    public static void removeAll() {
        final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return;
        }
        try {
            final Object v = threadLocalMap.indexedVariable(FastThreadLocal.variablesToRemoveIndex);
            if (v != null && v != InternalThreadLocalMap.UNSET) {
                final Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
                final FastThreadLocal[] array;
                final FastThreadLocal<?>[] variablesToRemoveArray = (array = (FastThreadLocal[])variablesToRemove.toArray((Object[])new FastThreadLocal[variablesToRemove.size()]));
                for (final FastThreadLocal<?> tlv : array) {
                    tlv.remove(threadLocalMap);
                }
            }
        }
        finally {
            InternalThreadLocalMap.remove();
        }
    }
    
    public static int size() {
        final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return 0;
        }
        return threadLocalMap.size();
    }
    
    public static void destroy() {
        InternalThreadLocalMap.destroy();
    }
    
    private static void addToVariablesToRemove(final InternalThreadLocalMap threadLocalMap, final FastThreadLocal<?> variable) {
        final Object v = threadLocalMap.indexedVariable(FastThreadLocal.variablesToRemoveIndex);
        Set<FastThreadLocal<?>> variablesToRemove;
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            variablesToRemove = (Set<FastThreadLocal<?>>)Collections.newSetFromMap((Map)new IdentityHashMap());
            threadLocalMap.setIndexedVariable(FastThreadLocal.variablesToRemoveIndex, variablesToRemove);
        }
        else {
            variablesToRemove = (Set<FastThreadLocal<?>>)v;
        }
        variablesToRemove.add(variable);
    }
    
    private static void removeFromVariablesToRemove(final InternalThreadLocalMap threadLocalMap, final FastThreadLocal<?> variable) {
        final Object v = threadLocalMap.indexedVariable(FastThreadLocal.variablesToRemoveIndex);
        if (v == InternalThreadLocalMap.UNSET || v == null) {
            return;
        }
        final Set<FastThreadLocal<?>> variablesToRemove = (Set<FastThreadLocal<?>>)v;
        variablesToRemove.remove(variable);
    }
    
    public FastThreadLocal() {
        this.index = InternalThreadLocalMap.nextVariableIndex();
    }
    
    public final V get() {
        final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
        final Object v = threadLocalMap.indexedVariable(this.index);
        if (v != InternalThreadLocalMap.UNSET) {
            return (V)v;
        }
        final V value = this.initialize(threadLocalMap);
        this.registerCleaner(threadLocalMap);
        return value;
    }
    
    private void registerCleaner(final InternalThreadLocalMap threadLocalMap) {
        final Thread current = Thread.currentThread();
        if (FastThreadLocalThread.willCleanupFastThreadLocals(current) || threadLocalMap.isCleanerFlagSet(this.index)) {
            return;
        }
        threadLocalMap.setCleanerFlag(this.index);
        ObjectCleaner.register(current, (Runnable)new Runnable() {
            public void run() {
                FastThreadLocal.this.remove(threadLocalMap);
            }
        });
    }
    
    public final V get(final InternalThreadLocalMap threadLocalMap) {
        final Object v = threadLocalMap.indexedVariable(this.index);
        if (v != InternalThreadLocalMap.UNSET) {
            return (V)v;
        }
        return this.initialize(threadLocalMap);
    }
    
    private V initialize(final InternalThreadLocalMap threadLocalMap) {
        V v = null;
        try {
            v = this.initialValue();
        }
        catch (Exception e) {
            PlatformDependent.throwException((Throwable)e);
        }
        threadLocalMap.setIndexedVariable(this.index, v);
        addToVariablesToRemove(threadLocalMap, this);
        return v;
    }
    
    public final void set(final V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            final InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.get();
            if (this.setKnownNotUnset(threadLocalMap, value)) {
                this.registerCleaner(threadLocalMap);
            }
        }
        else {
            this.remove();
        }
    }
    
    public final void set(final InternalThreadLocalMap threadLocalMap, final V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            this.setKnownNotUnset(threadLocalMap, value);
        }
        else {
            this.remove(threadLocalMap);
        }
    }
    
    private boolean setKnownNotUnset(final InternalThreadLocalMap threadLocalMap, final V value) {
        if (threadLocalMap.setIndexedVariable(this.index, value)) {
            addToVariablesToRemove(threadLocalMap, this);
            return true;
        }
        return false;
    }
    
    public final boolean isSet() {
        return this.isSet(InternalThreadLocalMap.getIfSet());
    }
    
    public final boolean isSet(final InternalThreadLocalMap threadLocalMap) {
        return threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index);
    }
    
    public final void remove() {
        this.remove(InternalThreadLocalMap.getIfSet());
    }
    
    public final void remove(final InternalThreadLocalMap threadLocalMap) {
        if (threadLocalMap == null) {
            return;
        }
        final Object v = threadLocalMap.removeIndexedVariable(this.index);
        removeFromVariablesToRemove(threadLocalMap, this);
        if (v != InternalThreadLocalMap.UNSET) {
            try {
                this.onRemoval(v);
            }
            catch (Exception e) {
                PlatformDependent.throwException((Throwable)e);
            }
        }
    }
    
    protected V initialValue() throws Exception {
        return null;
    }
    
    protected void onRemoval(final V value) throws Exception {
    }
    
    static {
        variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
    }
}
