package it.unimi.dsi.fastutil.chars;

import java.io.Serializable;
import java.util.Comparator;

public final class CharComparators {
    public static final CharComparator NATURAL_COMPARATOR;
    public static final CharComparator OPPOSITE_COMPARATOR;
    
    private CharComparators() {
    }
    
    public static CharComparator oppositeComparator(final CharComparator c) {
        return new OppositeComparator(c);
    }
    
    public static CharComparator asCharComparator(final Comparator<? super Character> c) {
        if (c == null || c instanceof CharComparator) {
            return (CharComparator)c;
        }
        return new CharComparator() {
            public int compare(final char x, final char y) {
                return c.compare(x, y);
            }
            
            public int compare(final Character x, final Character y) {
                return c.compare(x, y);
            }
        };
    }
    
    static {
        NATURAL_COMPARATOR = new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements CharComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final char a, final char b) {
            return Character.compare(a, b);
        }
        
        private Object readResolve() {
            return CharComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements CharComparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final char a, final char b) {
            return -Character.compare(a, b);
        }
        
        private Object readResolve() {
            return CharComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator implements CharComparator, Serializable {
        private static final long serialVersionUID = 1L;
        private final CharComparator comparator;
        
        protected OppositeComparator(final CharComparator c) {
            this.comparator = c;
        }
        
        public final int compare(final char a, final char b) {
            return this.comparator.compare(b, a);
        }
    }
}
