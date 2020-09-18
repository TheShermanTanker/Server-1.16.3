package it.unimi.dsi.fastutil.bytes;

import java.io.Serializable;
import java.util.Comparator;

public final class ByteComparators {
    public static final ByteComparator NATURAL_COMPARATOR;
    public static final ByteComparator OPPOSITE_COMPARATOR;
    
    private ByteComparators() {
    }
    
    public static ByteComparator oppositeComparator(final ByteComparator c) {
        return new OppositeComparator(c);
    }
    
    public static ByteComparator asByteComparator(final Comparator<? super Byte> c) {
        if (c == null || c instanceof ByteComparator) {
            return (ByteComparator)c;
        }
        return new ByteComparator() {
            public int compare(final byte x, final byte y) {
                return c.compare(x, y);
            }
            
            public int compare(final Byte x, final Byte y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final byte a, final byte b) {
            return Byte.compare(a, b);
        }
        
        private Object readResolve() {
            return ByteComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final byte a, final byte b) {
            return -Byte.compare(a, b);
        }
        
        private Object readResolve() {
            return ByteComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements ByteComparator, Serializable {
        private static final long serialVersionUID = 1L;
        private final ByteComparator comparator;
        
        protected OppositeComparator(final ByteComparator c) {
            this.comparator = c;
        }
        
        public final int compare(final byte a, final byte b) {
            return this.comparator.compare(b, a);
        }
    }
}
