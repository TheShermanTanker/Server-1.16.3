package it.unimi.dsi.fastutil.objects;

import java.io.Serializable;
import java.util.Comparator;

public final class ObjectComparators {
    public static final Comparator NATURAL_COMPARATOR;
    public static final Comparator OPPOSITE_COMPARATOR;
    
    private ObjectComparators() {
    }
    
    public static <K> Comparator<K> oppositeComparator(final Comparator<K> c) {
        return (Comparator<K>)new OppositeComparator((java.util.Comparator<Object>)c);
    }
    
    static {
        NATURAL_COMPARATOR = (Comparator)new NaturalImplicitComparator();
        OPPOSITE_COMPARATOR = (Comparator)new OppositeImplicitComparator();
    }
    
    protected static class NaturalImplicitComparator implements Comparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final Object a, final Object b) {
            return ((Comparable)a).compareTo(b);
        }
        
        private Object readResolve() {
            return ObjectComparators.NATURAL_COMPARATOR;
        }
    }
    
    protected static class OppositeImplicitComparator implements Comparator, Serializable {
        private static final long serialVersionUID = 1L;
        
        public final int compare(final Object a, final Object b) {
            return ((Comparable)b).compareTo(a);
        }
        
        private Object readResolve() {
            return ObjectComparators.OPPOSITE_COMPARATOR;
        }
    }
    
    protected static class OppositeComparator<K> implements Comparator<K>, Serializable {
        private static final long serialVersionUID = 1L;
        private final Comparator<K> comparator;
        
        protected OppositeComparator(final Comparator<K> c) {
            this.comparator = c;
        }
        
        public final int compare(final K a, final K b) {
            return this.comparator.compare(b, a);
        }
    }
}
