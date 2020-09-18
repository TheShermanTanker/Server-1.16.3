package it.unimi.dsi.fastutil.shorts;

import java.io.Serializable;
import java.util.Comparator;

public final class ShortComparators {
    public static final ShortComparator NATURAL_COMPARATOR;
    public static final ShortComparator OPPOSITE_COMPARATOR;
    
    private ShortComparators() {
    }
    
    public static ShortComparator oppositeComparator(final ShortComparator c) {
        return new OppositeComparator(c);
    }
    
    public static ShortComparator asShortComparator(final Comparator<? super Short> c) {
        if (c == null || c instanceof ShortComparator) {
            return (ShortComparator)c;
        }
        return new ShortComparator() {
            public int compare(final short x, final short y) {
                return c.compare(x, y);
            }
            
            public int compare(final Short x, final Short y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements ShortComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final short a, final short b) {
            return Short.compare(a, b);
        }
        
        private Object readResolve() {
            return ShortComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements ShortComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final short a, final short b) {
            return -Short.compare(a, b);
        }
        
        private Object readResolve() {
            return ShortComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements ShortComparator, Serializable {
        private static final long serialVersionUID = 1L;
        private final ShortComparator comparator;
        
        protected OppositeComparator(final ShortComparator c) {
            this.comparator = c;
        }
        
        public final int compare(final short a, final short b) {
            return this.comparator.compare(b, a);
        }
    }
}
