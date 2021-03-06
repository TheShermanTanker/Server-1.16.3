package it.unimi.dsi.fastutil.objects;

import java.util.function.Consumer;
import java.util.Collection;
import java.util.Set;
import java.util.Map;

public interface Object2ObjectMap<K, V> extends Object2ObjectFunction<K, V>, Map<K, V> {
    int size();
    
    default void clear() {
        throw new UnsupportedOperationException();
    }
    
    void defaultReturnValue(final V object);
    
    V defaultReturnValue();
    
    ObjectSet<Entry<K, V>> object2ObjectEntrySet();
    
    default ObjectSet<Map.Entry<K, V>> entrySet() {
        return (ObjectSet<Map.Entry<K, V>>)this.object2ObjectEntrySet();
    }
    
    default V put(final K key, final V value) {
        return super.put(key, value);
    }
    
    default V remove(final Object key) {
        return super.remove(key);
    }
    
    ObjectSet<K> keySet();
    
    ObjectCollection<V> values();
    
    boolean containsKey(final Object object);
    
    public interface FastEntrySet<K, V> extends ObjectSet<Entry<K, V>> {
        ObjectIterator<Entry<K, V>> fastIterator();
        
        default void fastForEach(final Consumer<? super Entry<K, V>> consumer) {
            this.forEach((Consumer)consumer);
        }
    }
    
    public interface Entry<K, V> extends Map.Entry<K, V> {
    }
}
