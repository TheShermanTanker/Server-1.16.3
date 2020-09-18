package it.unimi.dsi.fastutil.floats;

import java.util.Iterator;
import it.unimi.dsi.fastutil.HashCommon;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractFloatSet extends AbstractFloatCollection implements Cloneable, FloatSet {
    protected AbstractFloatSet() {
    }
    
    @Override
    public abstract FloatIterator iterator();
    
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
        final FloatIterator i = this.iterator();
        while (n-- != 0) {
            final float k = i.nextFloat();
            h += HashCommon.float2int(k);
        }
        return h;
    }
    
    public boolean remove(final float k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final float k) {
        return this.remove(k);
    }
}
