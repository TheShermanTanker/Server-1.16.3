package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StringMap;

class GarbageFreeSortedArrayThreadContextMap implements ReadOnlyThreadContextMap, ObjectThreadContextMap {
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
    protected final ThreadLocal<StringMap> localMap;
    
    public GarbageFreeSortedArrayThreadContextMap() {
        this.localMap = this.createThreadLocalMap();
    }
    
    private ThreadLocal<StringMap> createThreadLocalMap() {
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        final boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
        if (inheritable) {
            return (ThreadLocal<StringMap>)new InheritableThreadLocal<StringMap>() {
                protected StringMap childValue(final StringMap parentValue) {
                    return (parentValue != null) ? GarbageFreeSortedArrayThreadContextMap.this.createStringMap(parentValue) : null;
                }
            };
        }
        return (ThreadLocal<StringMap>)new ThreadLocal();
    }
    
    protected StringMap createStringMap() {
        return new SortedArrayStringMap(PropertiesUtil.getProperties().getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16));
    }
    
    protected StringMap createStringMap(final ReadOnlyStringMap original) {
        return new SortedArrayStringMap(original);
    }
    
    private StringMap getThreadLocalMap() {
        StringMap map = (StringMap)this.localMap.get();
        if (map == null) {
            map = this.createStringMap();
            this.localMap.set(map);
        }
        return map;
    }
    
    public void put(final String key, final String value) {
        this.getThreadLocalMap().putValue(key, value);
    }
    
    public void putValue(final String key, final Object value) {
        this.getThreadLocalMap().putValue(key, value);
    }
    
    public void putAll(final Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        final StringMap map = this.getThreadLocalMap();
        for (final Map.Entry<String, String> entry : values.entrySet()) {
            map.putValue((String)entry.getKey(), entry.getValue());
        }
    }
    
    public <V> void putAllValues(final Map<String, V> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        final StringMap map = this.getThreadLocalMap();
        for (final Map.Entry<String, V> entry : values.entrySet()) {
            map.putValue((String)entry.getKey(), entry.getValue());
        }
    }
    
    public String get(final String key) {
        return (String)this.getValue(key);
    }
    
    public Object getValue(final String key) {
        final StringMap map = (StringMap)this.localMap.get();
        return (map == null) ? null : map.getValue(key);
    }
    
    public void remove(final String key) {
        final StringMap map = (StringMap)this.localMap.get();
        if (map != null) {
            map.remove(key);
        }
    }
    
    public void removeAll(final Iterable<String> keys) {
        final StringMap map = (StringMap)this.localMap.get();
        if (map != null) {
            for (final String key : keys) {
                map.remove(key);
            }
        }
    }
    
    public void clear() {
        final StringMap map = (StringMap)this.localMap.get();
        if (map != null) {
            map.clear();
        }
    }
    
    public boolean containsKey(final String key) {
        final StringMap map = (StringMap)this.localMap.get();
        return map != null && map.containsKey(key);
    }
    
    public Map<String, String> getCopy() {
        final StringMap map = (StringMap)this.localMap.get();
        return (Map<String, String>)((map == null) ? new HashMap() : map.toMap());
    }
    
    public StringMap getReadOnlyContextData() {
        StringMap map = (StringMap)this.localMap.get();
        if (map == null) {
            map = this.createStringMap();
            this.localMap.set(map);
        }
        return map;
    }
    
    public Map<String, String> getImmutableMapOrNull() {
        final StringMap map = (StringMap)this.localMap.get();
        return (Map<String, String>)((map == null) ? null : Collections.unmodifiableMap((Map)map.toMap()));
    }
    
    public boolean isEmpty() {
        final StringMap map = (StringMap)this.localMap.get();
        return map == null || map.size() == 0;
    }
    
    public String toString() {
        final StringMap map = (StringMap)this.localMap.get();
        return (map == null) ? "{}" : map.toString();
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final StringMap map = (StringMap)this.localMap.get();
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other = (ThreadContextMap)obj;
        final Map<String, String> map = this.getImmutableMapOrNull();
        final Map<String, String> otherMap = other.getImmutableMapOrNull();
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
