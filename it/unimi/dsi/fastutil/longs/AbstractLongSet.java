package it.unimi.dsi.fastutil.longs;

import java.util.Iterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractLongSet extends AbstractLongCollection implements Cloneable, LongSet {
    protected AbstractLongSet() {
    }
    
    @Override
    public abstract LongIterator iterator();
    
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
        final LongIterator i = this.iterator();
        while (n-- != 0) {
            final long k = i.nextLong();
            h += HashCommon.long2int(k);
        }
        return h;
    }
    
    public boolean remove(final long k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final long k) {
        return this.remove(k);
    }
}
