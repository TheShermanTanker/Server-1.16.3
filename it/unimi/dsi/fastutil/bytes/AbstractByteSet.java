package it.unimi.dsi.fastutil.bytes;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractByteSet extends AbstractByteCollection implements Cloneable, ByteSet {
    protected AbstractByteSet() {
    }
    
    @Override
    public abstract ByteIterator iterator();
    
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
        final ByteIterator i = this.iterator();
        while (n-- != 0) {
            final byte k = i.nextByte();
            h += k;
        }
        return h;
    }
    
    public boolean remove(final byte k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final byte k) {
        return this.remove(k);
    }
}
