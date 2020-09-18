package it.unimi.dsi.fastutil.doubles;

import java.util.Iterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractDoubleSet extends AbstractDoubleCollection implements Cloneable, DoubleSet {
    protected AbstractDoubleSet() {
    }
    
    @Override
    public abstract DoubleIterator iterator();
    
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
        final DoubleIterator i = this.iterator();
        while (n-- != 0) {
            final double k = i.nextDouble();
            h += HashCommon.double2int(k);
        }
        return h;
    }
    
    public boolean remove(final double k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final double k) {
        return this.remove(k);
    }
}
