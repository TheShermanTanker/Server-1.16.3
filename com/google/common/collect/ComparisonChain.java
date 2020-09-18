package com.google.common.collect;

import com.google.common.primitives.Booleans;
import com.google.common.primitives.Longs;
import com.google.common.primitives.Ints;
import java.util.Comparator;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class ComparisonChain {
    private static final ComparisonChain ACTIVE;
    private static final ComparisonChain LESS;
    private static final ComparisonChain GREATER;
    
    private ComparisonChain() {
    }
    
    public static ComparisonChain start() {
        return ComparisonChain.ACTIVE;
    }
    
    public abstract ComparisonChain compare(final Comparable<?> comparable1, final Comparable<?> comparable2);
    
    public abstract <T> ComparisonChain compare(@Nullable final T object1, @Nullable final T object2, final Comparator<T> comparator);
    
    public abstract ComparisonChain compare(final int integer1, final int integer2);
    
    public abstract ComparisonChain compare(final long long1, final long long2);
    
    public abstract ComparisonChain compare(final float float1, final float float2);
    
    public abstract ComparisonChain compare(final double double1, final double double2);
    
    @Deprecated
    public final ComparisonChain compare(final Boolean left, final Boolean right) {
        return this.compareFalseFirst(left, right);
    }
    
    public abstract ComparisonChain compareTrueFirst(final boolean boolean1, final boolean boolean2);
    
    public abstract ComparisonChain compareFalseFirst(final boolean boolean1, final boolean boolean2);
    
    public abstract int result();
    
    static {
        ACTIVE = new ComparisonChain() {
            @Override
            public ComparisonChain compare(final Comparable left, final Comparable right) {
                return this.classify(left.compareTo(right));
            }
            
            @Override
            public <T> ComparisonChain compare(@Nullable final T left, @Nullable final T right, final Comparator<T> comparator) {
                return this.classify(comparator.compare(left, right));
            }
            
            @Override
            public ComparisonChain compare(final int left, final int right) {
                return this.classify(Ints.compare(left, right));
            }
            
            @Override
            public ComparisonChain compare(final long left, final long right) {
                return this.classify(Longs.compare(left, right));
            }
            
            @Override
            public ComparisonChain compare(final float left, final float right) {
                return this.classify(Float.compare(left, right));
            }
            
            @Override
            public ComparisonChain compare(final double left, final double right) {
                return this.classify(Double.compare(left, right));
            }
            
            @Override
            public ComparisonChain compareTrueFirst(final boolean left, final boolean right) {
                return this.classify(Booleans.compare(right, left));
            }
            
            @Override
            public ComparisonChain compareFalseFirst(final boolean left, final boolean right) {
                return this.classify(Booleans.compare(left, right));
            }
            
            ComparisonChain classify(final int result) {
                return (result < 0) ? ComparisonChain.LESS : ((result > 0) ? ComparisonChain.GREATER : ComparisonChain.ACTIVE);
            }
            
            @Override
            public int result() {
                return 0;
            }
        };
        LESS = new InactiveComparisonChain(-1);
        GREATER = new InactiveComparisonChain(1);
    }
    
    private static final class InactiveComparisonChain extends ComparisonChain {
        final int result;
        
        InactiveComparisonChain(final int result) {
            super(null);
            this.result = result;
        }
        
        @Override
        public ComparisonChain compare(@Nullable final Comparable left, @Nullable final Comparable right) {
            return this;
        }
        
        @Override
        public <T> ComparisonChain compare(@Nullable final T left, @Nullable final T right, @Nullable final Comparator<T> comparator) {
            return this;
        }
        
        @Override
        public ComparisonChain compare(final int left, final int right) {
            return this;
        }
        
        @Override
        public ComparisonChain compare(final long left, final long right) {
            return this;
        }
        
        @Override
        public ComparisonChain compare(final float left, final float right) {
            return this;
        }
        
        @Override
        public ComparisonChain compare(final double left, final double right) {
            return this;
        }
        
        @Override
        public ComparisonChain compareTrueFirst(final boolean left, final boolean right) {
            return this;
        }
        
        @Override
        public ComparisonChain compareFalseFirst(final boolean left, final boolean right) {
            return this;
        }
        
        @Override
        public int result() {
            return this.result;
        }
    }
}
