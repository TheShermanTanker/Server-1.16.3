package com.google.common.collect;

import java.io.Serializable;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import java.util.Iterator;
import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.Spliterator;
import java.util.Map;
import com.google.j2objc.annotations.Weak;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class ImmutableMapValues<K, V> extends ImmutableCollection<V> {
    @Weak
    private final ImmutableMap<K, V> map;
    
    ImmutableMapValues(final ImmutableMap<K, V> map) {
        this.map = map;
    }
    
    public int size() {
        return this.map.size();
    }
    
    @Override
    public UnmodifiableIterator<V> iterator() {
        return new UnmodifiableIterator<V>() {
            final UnmodifiableIterator<Map.Entry<K, V>> entryItr = ImmutableMapValues.this.map.entrySet().iterator();
            
            public boolean hasNext() {
                return this.entryItr.hasNext();
            }
            
            public V next() {
                return (V)((Map.Entry)this.entryItr.next()).getValue();
            }
        };
    }
    
    @Override
    public Spliterator<V> spliterator() {
        return CollectSpliterators.<Map.Entry<K, V>, V>map(this.map.entrySet().spliterator(), (java.util.function.Function<? super Map.Entry<K, V>, ? extends V>)Map.Entry::getValue);
    }
    
    @Override
    public boolean contains(@Nullable final Object object) {
        return object != null && Iterators.contains(this.iterator(), object);
    }
    
    @Override
    boolean isPartialView() {
        return true;
    }
    
    @Override
    public ImmutableList<V> asList() {
        final ImmutableList<Map.Entry<K, V>> entryList = this.map.entrySet().asList();
        return new ImmutableAsList<V>() {
            public V get(final int index) {
                return (V)((Map.Entry)entryList.get(index)).getValue();
            }
            
            @Override
            ImmutableCollection<V> delegateCollection() {
                return (ImmutableCollection<V>)ImmutableMapValues.this;
            }
        };
    }
    
    @GwtIncompatible
    public void forEach(final Consumer<? super V> action) {
        Preconditions.<Consumer<? super V>>checkNotNull(action);
        this.map.forEach((k, v) -> action.accept(v));
    }
    
    @GwtIncompatible
    @Override
    Object writeReplace() {
        return new SerializedForm(this.map);
    }
    
    @GwtIncompatible
    private static class SerializedForm<V> implements Serializable {
        final ImmutableMap<?, V> map;
        private static final long serialVersionUID = 0L;
        
        SerializedForm(final ImmutableMap<?, V> map) {
            this.map = map;
        }
        
        Object readResolve() {
            return this.map.values();
        }
    }
}
