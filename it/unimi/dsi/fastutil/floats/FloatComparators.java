package it.unimi.dsi.fastutil.floats;

import java.io.Serializable;
import java.util.Comparator;

public final class FloatComparators {
    public static final FloatComparator NATURAL_COMPARATOR;
    public static final FloatComparator OPPOSITE_COMPARATOR;
    
    private FloatComparators() {
    }
    
    public static FloatComparator oppositeComparator(final FloatComparator c) {
        return new OppositeComparator(c);
    }
    
    public static FloatComparator asFloatComparator(final Comparator<? super Float> c) {
        if (c == null || c instanceof FloatComparator) {
            return (FloatComparator)c;
        }
        return new FloatComparator() {
            public int compare(final float x, final float y) {
                return c.compare(x, y);
            }
            
            public int compare(final Float x, final Float y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements FloatComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final float a, final float b) {
            return Float.compare(a, b);
        }
        
        private Object readResolve() {
            return FloatComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements FloatComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final float a, final float b) {
            return -Float.compare(a, b);
        }
        
        private Object readResolve() {
            return FloatComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements FloatComparator, Serializable {
        private static final long serialVersionUID = 1L;
        private final FloatComparator comparator;
        
        protected OppositeComparator(final FloatComparator c) {
            this.comparator = c;
        }
        
        public final int compare(final float a, final float b) {
            return this.comparator.compare(b, a);
        }
    }
}
