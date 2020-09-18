package net.minecraft.stats;

import java.util.Iterator;
import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.core.Registry;

public class StatType<T> implements Iterable<Stat<T>> {
    private final Registry<T> registry;
    private final Map<T, Stat<T>> map;
    
    public StatType(final Registry<T> gm) {
        this.map = (Map<T, Stat<T>>)new IdentityHashMap();
        this.registry = gm;
    }
    
    public Stat<T> get(final T object, final StatFormatter adw) {
        return (Stat<T>)this.map.computeIfAbsent(object, object -> new Stat((StatType<T>)this, (T)object, adw));
    }
    
    public Registry<T> getRegistry() {
        return this.registry;
    }
    
    public Iterator<Stat<T>> iterator() {
        return (Iterator<Stat<T>>)this.map.values().iterator();
    }
    
    public Stat<T> get(final T object) {
        return this.get(object, StatFormatter.DEFAULT);
    }
}
