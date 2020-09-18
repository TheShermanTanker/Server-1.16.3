package com.google.common.base;

import java.io.Serializable;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.util.function.BiPredicate;

@GwtCompatible
public abstract class Equivalence<T> implements BiPredicate<T, T> {
    protected Equivalence() {
    }
    
    public final boolean equivalent(@Nullable final T a, @Nullable final T b) {
        return a == b || (a != null && b != null && this.doEquivalent(a, b));
    }
    
    @Deprecated
    public final boolean test(@Nullable final T t, @Nullable final T u) {
        return this.equivalent(t, u);
    }
    
    protected abstract boolean doEquivalent(final T object1, final T object2);
    
    public final int hash(@Nullable final T t) {
        if (t == null) {
            return 0;
        }
        return this.doHash(t);
    }
    
    protected abstract int doHash(final T object);
    
    public final <F> Equivalence<F> onResultOf(final Function<F, ? extends T> function) {
        return (Equivalence<F>)new FunctionalEquivalence(function, (Equivalence<Object>)this);
    }
    
    public final <S extends T> Wrapper<S> wrap(@Nullable final S reference) {
        return new Wrapper<S>(this, reference);
    }
    
    @GwtCompatible(serializable = true)
    public final <S extends T> Equivalence<Iterable<S>> pairwise() {
        return (Equivalence<Iterable<S>>)new PairwiseEquivalence(this);
    }
    
    public final Predicate<T> equivalentTo(@Nullable final T target) {
        return new EquivalentToPredicate<T>(this, target);
    }
    
    public static Equivalence<Object> equals() {
        return Equals.INSTANCE;
    }
    
    public static Equivalence<Object> identity() {
        return Identity.INSTANCE;
    }
    
    public static final class Wrapper<T> implements Serializable {
        private final Equivalence<? super T> equivalence;
        @Nullable
        private final T reference;
        private static final long serialVersionUID = 0L;
        
        private Wrapper(final Equivalence<? super T> equivalence, @Nullable final T reference) {
            this.equivalence = Preconditions.<Equivalence<? super T>>checkNotNull(equivalence);
            this.reference = reference;
        }
        
        @Nullable
        public T get() {
            return this.reference;
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof Wrapper) {
                final Wrapper<?> that = obj;
                if (this.equivalence.equals(that.equivalence)) {
                    final Equivalence<Object> equivalence = (Equivalence<Object>)this.equivalence;
                    return equivalence.equivalent(this.reference, that.reference);
                }
            }
            return false;
        }
        
        public int hashCode() {
            return this.equivalence.hash(this.reference);
        }
        
        public String toString() {
            return new StringBuilder().append(this.equivalence).append(".wrap(").append(this.reference).append(")").toString();
        }
    }
    
    private static final class EquivalentToPredicate<T> implements Predicate<T>, Serializable {
        private final Equivalence<T> equivalence;
        @Nullable
        private final T target;
        private static final long serialVersionUID = 0L;
        
        EquivalentToPredicate(final Equivalence<T> equivalence, @Nullable final T target) {
            this.equivalence = Preconditions.<Equivalence<T>>checkNotNull(equivalence);
            this.target = target;
        }
        
        public boolean apply(@Nullable final T input) {
            return this.equivalence.equivalent(input, this.target);
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof EquivalentToPredicate) {
                final EquivalentToPredicate<?> that = obj;
                return this.equivalence.equals(that.equivalence) && Objects.equal(this.target, that.target);
            }
            return false;
        }
        
        public int hashCode() {
            return Objects.hashCode(this.equivalence, this.target);
        }
        
        public String toString() {
            return new StringBuilder().append(this.equivalence).append(".equivalentTo(").append(this.target).append(")").toString();
        }
    }
    
    static final class Equals extends Equivalence<Object> implements Serializable {
        static final Equals INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected boolean doEquivalent(final Object a, final Object b) {
            return a.equals(b);
        }
        
        @Override
        protected int doHash(final Object o) {
            return o.hashCode();
        }
        
        private Object readResolve() {
            return Equals.INSTANCE;
        }
        
        static {
            INSTANCE = new Equals();
        }
    }
    
    static final class Identity extends Equivalence<Object> implements Serializable {
        static final Identity INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected boolean doEquivalent(final Object a, final Object b) {
            return false;
        }
        
        @Override
        protected int doHash(final Object o) {
            return System.identityHashCode(o);
        }
        
        private Object readResolve() {
            return Identity.INSTANCE;
        }
        
        static {
            INSTANCE = new Identity();
        }
    }
}
