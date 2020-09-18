package it.unimi.dsi.fastutil.shorts;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractShortSet extends AbstractShortCollection implements Cloneable, ShortSet {
    protected AbstractShortSet() {
    }
    
    @Override
    public abstract ShortIterator iterator();
    
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
        final ShortIterator i = this.iterator();
        while (n-- != 0) {
            final short k = i.nextShort();
            h += k;
        }
        return h;
    }
    
    public boolean remove(final short k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final short k) {
        return this.remove(k);
    }
}
