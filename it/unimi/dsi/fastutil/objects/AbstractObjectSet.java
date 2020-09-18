package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractObjectSet<K> extends AbstractObjectCollection<K> implements Cloneable, ObjectSet<K> {
    protected AbstractObjectSet() {
    }
    
    @Override
    public abstract ObjectIterator<K> iterator();
    
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        final Set<?> s = o;
        return s.size() == this.size() && this.containsAll((Collection)s);
    }
    
    public int hashCode() {
        int h = 0;
        int n = this.size();
        final ObjectIterator<K> i = this.iterator();
        while (n-- != 0) {
            final K k = (K)i.next();
            h += ((k == null) ? 0 : k.hashCode());
        }
        return h;
    }
}
