package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.TriConsumer;
import org.apache.logging.log4j.util.BiConsumer;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import org.apache.logging.log4j.util.PropertiesUtil;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

public class DefaultThreadContextMap implements ThreadContextMap, ReadOnlyStringMap {
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
    private final boolean useMap;
    private final ThreadLocal<Map<String, String>> localMap;
    
    public DefaultThreadContextMap() {
        this(true);
    }
    
    public DefaultThreadContextMap(final boolean useMap) {
        this.useMap = useMap;
        this.localMap = createThreadLocalMap(useMap);
    }
    
    static ThreadLocal<Map<String, String>> createThreadLocalMap(final boolean isMapEnabled) {
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        final boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
        if (inheritable) {
            return (ThreadLocal<Map<String, String>>)new InheritableThreadLocal<Map<String, String>>() {
                protected Map<String, String> childValue(final Map<String, String> parentValue) {
                    return (Map<String, String>)((parentValue != null && isMapEnabled) ? Collections.unmodifiableMap((Map)new HashMap((Map)parentValue)) : null);
                }
            };
        }
        return (ThreadLocal<Map<String, String>>)new ThreadLocal();
    }
    
    public void put(final String key, final String value) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = (Map<String, String>)this.localMap.get();
        map = (Map<String, String>)((map == null) ? new HashMap(1) : new HashMap((Map)map));
        map.put(key, value);
        this.localMap.set(Collections.unmodifiableMap((Map)map));
    }
    
    public void putAll(final Map<String, String> m) {
        if (!this.useMap) {
            return;
        }
        Map<String, String> map = (Map<String, String>)this.localMap.get();
        map = (Map<String, String>)((map == null) ? new HashMap(m.size()) : new HashMap((Map)map));
        for (final Map.Entry<String, String> e : m.entrySet()) {
            map.put(e.getKey(), e.getValue());
        }
        this.localMap.set(Collections.unmodifiableMap((Map)map));
    }
    
    public String get(final String key) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return (map == null) ? null : ((String)map.get(key));
    }
    
    public void remove(final String key) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        if (map != null) {
            final Map<String, String> copy = (Map<String, String>)new HashMap((Map)map);
            copy.remove(key);
            this.localMap.set(Collections.unmodifiableMap((Map)copy));
        }
    }
    
    public void removeAll(final Iterable<String> keys) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        if (map != null) {
            final Map<String, String> copy = (Map<String, String>)new HashMap((Map)map);
            for (final String key : keys) {
                copy.remove(key);
            }
            this.localMap.set(Collections.unmodifiableMap((Map)copy));
        }
    }
    
    public void clear() {
        this.localMap.remove();
    }
    
    public Map<String, String> toMap() {
        return this.getCopy();
    }
    
    public boolean containsKey(final String key) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return map != null && map.containsKey(key);
    }
    
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        if (map == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            action.accept((String)entry.getKey(), entry.getValue());
        }
    }
    
    public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        if (map == null) {
            return;
        }
        for (final Map.Entry<String, String> entry : map.entrySet()) {
            action.accept((String)entry.getKey(), entry.getValue(), state);
        }
    }
    
    public <V> V getValue(final String key) {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return (V)((map == null) ? null : ((String)map.get(key)));
    }
    
    public Map<String, String> getCopy() {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return (Map<String, String>)((map == null) ? new HashMap() : new HashMap((Map)map));
    }
    
    public Map<String, String> getImmutableMapOrNull() {
        return (Map<String, String>)this.localMap.get();
    }
    
    public boolean isEmpty() {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return map == null || map.size() == 0;
    }
    
    public int size() {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return (map == null) ? 0 : map.size();
    }
    
    public String toString() {
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        return (map == null) ? "{}" : map.toString();
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
        result = 31 * result + Boolean.valueOf(this.useMap).hashCode();
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof DefaultThreadContextMap) {
            final DefaultThreadContextMap other = (DefaultThreadContextMap)obj;
            if (this.useMap != other.useMap) {
                return false;
            }
        }
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other2 = (ThreadContextMap)obj;
        final Map<String, String> map = (Map<String, String>)this.localMap.get();
        final Map<String, String> otherMap = other2.getImmutableMapOrNull();
        if (map == null) {
            if (otherMap != null) {
                return false;
            }
        }
        else if (!map.equals(otherMap)) {
            return false;
        }
        return true;
    }
}
