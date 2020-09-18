package it.unimi.dsi.fastutil.objects;

import java.util.Iterator;
import java.util.AbstractCollection;

public abstract class AbstractObjectCollection<K> extends AbstractCollection<K> implements ObjectCollection<K> {
    protected AbstractObjectCollection() {
    }
    
    public abstract ObjectIterator<K> iterator();
    
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = this.iterator();
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            }
            else {
                s.append(", ");
            }
            final Object k = i.next();
            if (this == k) {
                s.append("(this collection)");
            }
            else {
                s.append(String.valueOf(k));
            }
        }
        s.append("}");
        return s.toString();
    }
}
