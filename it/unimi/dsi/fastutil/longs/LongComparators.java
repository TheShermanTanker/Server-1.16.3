package it.unimi.dsi.fastutil.longs;

import java.io.Serializable;
import java.util.Comparator;

public final class LongComparators {
    public static final LongComparator NATURAL_COMPARATOR;
    public static final LongComparator OPPOSITE_COMPARATOR;
    
    private LongComparators() {
    }
    
    public static LongComparator oppositeComparator(final LongComparator c) {
        return new OppositeComparator(c);
    }
    
    public static LongComparator asLongComparator(final Comparator<? super Long> c) {
        if (c == null || c instanceof LongComparator) {
            return (LongComparator)c;
        }
        return new LongComparator() {
            public int compare(final long x, final long y) {
                return c.compare(x, y);
            }
            
            public int compare(final Long x, final Long y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements LongComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final long a, final long b) {
            return Long.compare(a, b);
        }
        
        private Object readResolve() {
            return LongComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements LongComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final long a, final long b) {
            return -Long.compare(a, b);
        }
        
        private Object readResolve() {
            return LongComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements LongComparator, Serializable {
        private static final long serialVersionUID = 1L;
        private final LongComparator comparator;
        
        protected OppositeComparator(final LongComparator c) {
            this.comparator = c;
        }
        
        public final int compare(final long a, final long b) {
            return this.comparator.compare(b, a);
        }
    }
}
