package io.netty.util.internal;

import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;
import java.io.Serializable;
import java.util.AbstractSet;

public final class ConcurrentSet<E> extends AbstractSet<E> implements Serializable {
    private static final long serialVersionUID = -6761513279741915432L;
    private final ConcurrentMap<E, Boolean> map;
    
    public ConcurrentSet() {
        this.map = PlatformDependent.<E, Boolean>newConcurrentHashMap();
    }
    
    public int size() {
        return this.map.size();
    }
    
    public boolean contains(final Object o) {
        return this.map.containsKey(o);
    }
    
    public boolean add(final E o) {
        return this.map.putIfAbsent(o, Boolean.TRUE) == null;
    }
    
    public boolean remove(final Object o) {
        return this.map.remove(o) != null;
    }
    
    public void clear() {
        this.map.clear();
    }
    
    public Iterator<E> iterator() {
        return (Iterator<E>)this.map.keySet().iterator();
    }
}
