package it.unimi.dsi.fastutil.chars;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;

public abstract class AbstractCharSet extends AbstractCharCollection implements Cloneable, CharSet {
    protected AbstractCharSet() {
    }
    
    @Override
    public abstract CharIterator iterator();
    
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
        final CharIterator i = this.iterator();
        while (n-- != 0) {
            final char k = i.nextChar();
            h += k;
        }
        return h;
    }
    
    public boolean remove(final char k) {
        return super.rem(k);
    }
    
    @Deprecated
    @Override
    public boolean rem(final char k) {
        return this.remove(k);
    }
}
