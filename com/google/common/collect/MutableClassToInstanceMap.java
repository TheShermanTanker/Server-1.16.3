package com.google.common.collect;

import com.google.common.primitives.Primitives;
import java.util.LinkedHashMap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.Spliterator;
import java.util.Set;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;

@GwtIncompatible
public final class MutableClassToInstanceMap<B> extends ForwardingMap<Class<? extends B>, B> implements ClassToInstanceMap<B>, Serializable {
    private final Map<Class<? extends B>, B> delegate;
    
    public static <B> MutableClassToInstanceMap<B> create() {
        return new MutableClassToInstanceMap<B>((java.util.Map<java.lang.Class<? extends B>, B>)new HashMap());
    }
    
    public static <B> MutableClassToInstanceMap<B> create(final Map<Class<? extends B>, B> backingMap) {
        return new MutableClassToInstanceMap<B>(backingMap);
    }
    
    private MutableClassToInstanceMap(final Map<Class<? extends B>, B> delegate) {
        this.delegate = Preconditions.<Map<Class<? extends B>, B>>checkNotNull(delegate);
    }
    
    @Override
    protected Map<Class<? extends B>, B> delegate() {
        return this.delegate;
    }
    
    private static <B> Map.Entry<Class<? extends B>, B> checkedEntry(final Map.Entry<Class<? extends B>, B> entry) {
        return (Map.Entry<Class<? extends B>, B>)new ForwardingMapEntry<Class<? extends B>, B>() {
            @Override
            protected Map.Entry<Class<? extends B>, B> delegate() {
                return entry;
            }
            
            @Override
            public B setValue(final B value) {
                return super.setValue((B)MutableClassToInstanceMap.cast((java.lang.Class<Object>)((ForwardingMapEntry<Class, V>)this).getKey(), value));
            }
        };
    }
    
    @Override
    public Set<Map.Entry<Class<? extends B>, B>> entrySet() {
        return (Set<Map.Entry<Class<? extends B>, B>>)new ForwardingSet<Map.Entry<Class<? extends B>, B>>() {
            @Override
            protected Set<Map.Entry<Class<? extends B>, B>> delegate() {
                return (Set<Map.Entry<Class<? extends B>, B>>)MutableClassToInstanceMap.this.delegate().entrySet();
            }
            
            public Spliterator<Map.Entry<Class<? extends B>, B>> spliterator() {
                return CollectSpliterators.<Object, Map.Entry<Class<? extends B>, B>>map((java.util.Spliterator<Object>)this.delegate().spliterator(), (java.util.function.Function<? super Object, ? extends Map.Entry<Class<? extends B>, B>>)(x$0 -> MutableClassToInstanceMap.checkedEntry((Map.Entry<java.lang.Class<?>, Object>)x$0)));
            }
            
            @Override
            public Iterator<Map.Entry<Class<? extends B>, B>> iterator() {
                return (Iterator<Map.Entry<Class<? extends B>, B>>)new TransformedIterator<Map.Entry<Class<? extends B>, B>, Map.Entry<Class<? extends B>, B>>(this.delegate().iterator()) {
                    @Override
                    Map.Entry<Class<? extends B>, B> transform(final Map.Entry<Class<? extends B>, B> from) {
                        return MutableClassToInstanceMap.checkedEntry((Map.Entry<java.lang.Class<?>, Object>)from);
                    }
                };
            }
            
            @Override
            public Object[] toArray() {
                return this.standardToArray();
            }
            
            @Override
            public <T> T[] toArray(final T[] array) {
                return this.<T>standardToArray(array);
            }
        };
    }
    
    @CanIgnoreReturnValue
    @Override
    public B put(final Class<? extends B> key, final B value) {
        return super.put(key, MutableClassToInstanceMap.<B, B>cast((java.lang.Class<B>)key, value));
    }
    
    @Override
    public void putAll(final Map<? extends Class<? extends B>, ? extends B> map) {
        final Map<Class<? extends B>, B> copy = (Map<Class<? extends B>, B>)new LinkedHashMap((Map)map);
        for (final Map.Entry<? extends Class<? extends B>, B> entry : copy.entrySet()) {
            MutableClassToInstanceMap.cast((java.lang.Class<Object>)entry.getKey(), entry.getValue());
        }
        super.putAll((java.util.Map<? extends Class<? extends B>, ? extends B>)copy);
    }
    
    @CanIgnoreReturnValue
    @Override
    public <T extends B> T putInstance(final Class<T> type, final T value) {
        return MutableClassToInstanceMap.<Object, T>cast(type, this.put((java.lang.Class<? extends B>)type, (B)value));
    }
    
    @Override
    public <T extends B> T getInstance(final Class<T> type) {
        return MutableClassToInstanceMap.<B, T>cast(type, this.get(type));
    }
    
    @CanIgnoreReturnValue
    private static <B, T extends B> T cast(final Class<T> type, final B value) {
        return (T)Primitives.<T>wrap(type).cast(value);
    }
    
    private Object writeReplace() {
        return new SerializedForm(this.delegate());
    }
    
    private static final class SerializedForm<B> implements Serializable {
        private final Map<Class<? extends B>, B> backingMap;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final Map<Class<? extends B>, B> backingMap) {
            this.backingMap = backingMap;
        }
        
        Object readResolve() {
            return MutableClassToInstanceMap.<B>create(this.backingMap);
        }
    }
}
