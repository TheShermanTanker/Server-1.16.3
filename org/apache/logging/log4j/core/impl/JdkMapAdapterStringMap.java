package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Arrays;
import org.apache.logging.log4j.util.BiConsumer;
import java.util.Objects;
import java.util.HashMap;
import org.apache.logging.log4j.util.TriConsumer;
import java.util.Map;
import java.util.Comparator;
import org.apache.logging.log4j.util.StringMap;

class JdkMapAdapterStringMap implements StringMap {
    private static final long serialVersionUID = -7348247784983193612L;
    private static final String FROZEN = "Frozen collection cannot be modified";
    private static final Comparator<? super String> NULL_FIRST_COMPARATOR;
    private final Map<String, String> map;
    private boolean immutable;
    private transient String[] sortedKeys;
    private static TriConsumer<String, String, Map<String, String>> PUT_ALL;
    
    public JdkMapAdapterStringMap() {
        this((Map<String, String>)new HashMap());
    }
    
    public JdkMapAdapterStringMap(final Map<String, String> map) {
        this.immutable = false;
        this.map = (Map<String, String>)Objects.requireNonNull(map, "map");
    }
    
    public Map<String, String> toMap() {
        return this.map;
    }
    
    private void assertNotFrozen() {
        if (this.immutable) {
            throw new UnsupportedOperationException("Frozen collection cannot be modified");
        }
    }
    
    public boolean containsKey(final String key) {
        return this.map.containsKey(key);
    }
    
    public <V> void forEach(final BiConsumer<String, ? super V> action) {
        final String[] keys = this.getSortedKeys();
        for (int i = 0; i < keys.length; ++i) {
            action.accept(keys[i], this.map.get((Object)keys[i]));
        }
    }
    
    public <V, S> void forEach(final TriConsumer<String, ? super V, S> action, final S state) {
        final String[] keys = this.getSortedKeys();
        for (int i = 0; i < keys.length; ++i) {
            action.accept(keys[i], this.map.get((Object)keys[i]), state);
        }
    }
    
    private String[] getSortedKeys() {
        if (this.sortedKeys == null) {
            Arrays.sort((Object[])(this.sortedKeys = (String[])this.map.keySet().toArray((Object[])new String[this.map.size()])), (Comparator)JdkMapAdapterStringMap.NULL_FIRST_COMPARATOR);
        }
        return this.sortedKeys;
    }
    
    public <V> V getValue(final String key) {
        return (V)this.map.get(key);
    }
    
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    public int size() {
        return this.map.size();
    }
    
    public void clear() {
        if (this.map.isEmpty()) {
            return;
        }
        this.assertNotFrozen();
        this.map.clear();
        this.sortedKeys = null;
    }
    
    public void freeze() {
        this.immutable = true;
    }
    
    public boolean isFrozen() {
        return this.immutable;
    }
    
    public void putAll(final ReadOnlyStringMap source) {
        this.assertNotFrozen();
        source.<Object, Map<String, String>>forEach(JdkMapAdapterStringMap.PUT_ALL, this.map);
        this.sortedKeys = null;
    }
    
    public void putValue(final String key, final Object value) {
        this.assertNotFrozen();
        this.map.put(key, ((value == null) ? null : String.valueOf(value)));
        this.sortedKeys = null;
    }
    
    public void remove(final String key) {
        if (!this.map.containsKey(key)) {
            return;
        }
        this.assertNotFrozen();
        this.map.remove(key);
        this.sortedKeys = null;
    }
    
    public String toString() {
        final StringBuilder result = new StringBuilder(this.map.size() * 13);
        result.append('{');
        final String[] keys = this.getSortedKeys();
        for (int i = 0; i < keys.length; ++i) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(keys[i]).append('=').append((String)this.map.get(keys[i]));
        }
        result.append('}');
        return result.toString();
    }
    
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof JdkMapAdapterStringMap)) {
            return false;
        }
        final JdkMapAdapterStringMap other = (JdkMapAdapterStringMap)object;
        return this.map.equals(other.map) && this.immutable == other.immutable;
    }
    
    public int hashCode() {
        return this.map.hashCode() + (this.immutable ? 31 : 0);
    }
    
    static {
        NULL_FIRST_COMPARATOR = (Comparator)new Comparator<String>() {
            public int compare(final String left, final String right) {
                if (left == null) {
                    return -1;
                }
                if (right == null) {
                    return 1;
                }
                return left.compareTo(right);
            }
        };
        JdkMapAdapterStringMap.PUT_ALL = new TriConsumer<String, String, Map<String, String>>() {
            public void accept(final String key, final String value, final Map<String, String> stringStringMap) {
                stringStringMap.put(key, value);
            }
        };
    }
}
