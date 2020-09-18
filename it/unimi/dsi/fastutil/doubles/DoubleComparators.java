package it.unimi.dsi.fastutil.doubles;

import java.io.Serializable;
import java.util.Comparator;

public final class DoubleComparators {
    public static final DoubleComparator NATURAL_COMPARATOR;
    public static final DoubleComparator OPPOSITE_COMPARATOR;
    
    private DoubleComparators() {
    }
    
    public static DoubleComparator oppositeComparator(final DoubleComparator c) {
        return new OppositeComparator(c);
    }
    
    public static DoubleComparator asDoubleComparator(final Comparator<? super Double> c) {
        if (c == null || c instanceof DoubleComparator) {
            return (DoubleComparator)c;
        }
        return new DoubleComparator() {
            public int compare(final double x, final double y) {
                return c.compare(x, y);
            }
            
            public int compare(final Double x, final Double y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements DoubleComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final double a, final double b) {
            return Double.compare(a, b);
        }
        
        private Object readResolve() {
            return DoubleComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements DoubleComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final double a, final double b) {
            return -Double.compare(a, b);
        }
        
        private Object readResolve() {
            return DoubleComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements DoubleComparator, Serializable {
        private static final long serialVersionUID = 1L;
        private final DoubleComparator comparator;
        
        protected OppositeComparator(final DoubleComparator c) {
            this.comparator = c;
        }
        
        public final int compare(final double a, final double b) {
            return this.comparator.compare(b, a);
        }
    }
}
