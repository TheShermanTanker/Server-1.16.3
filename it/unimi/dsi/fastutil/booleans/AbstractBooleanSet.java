package it.unimi.dsi.fastutil.booleans;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractBooleanSet extends AbstractBooleanCollection implements Cloneable, BooleanSet {
    protected AbstractBooleanSet() {
    }
    
    @Override
    public abstract BooleanIterator iterator();
    
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
        final BooleanIterator i = this.iterator();
        while (n-- != 0) {
            final boolean k = i.nextBoolean();
            h += (k ? 1231 : 1237);
        }
        return h;
    }
    
    public boolean remove(final boolean k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final boolean k) {
        return this.remove(k);
    }
}
