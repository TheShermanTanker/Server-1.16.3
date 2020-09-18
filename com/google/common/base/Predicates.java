package com.google.common.base;

import java.io.Serializable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.Collection;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;
import java.util.List;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Predicates {
    private static final Joiner COMMA_JOINER;
    
    private Predicates() {
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> alwaysTrue() {
        return ObjectPredicate.ALWAYS_TRUE.<T>withNarrowedType();
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> alwaysFalse() {
        return ObjectPredicate.ALWAYS_FALSE.<T>withNarrowedType();
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> isNull() {
        return ObjectPredicate.IS_NULL.<T>withNarrowedType();
    }
    
    @GwtCompatible(serializable = true)
    public static <T> Predicate<T> notNull() {
        return ObjectPredicate.NOT_NULL.<T>withNarrowedType();
    }
    
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return new NotPredicate<T>(predicate);
    }
    
    public static <T> Predicate<T> and(final Iterable<? extends Predicate<? super T>> components) {
        return new AndPredicate<T>((List)Predicates.defensiveCopy(components));
    }
    
    public static <T> Predicate<T> and(final Predicate<? super T>... components) {
        return new AndPredicate<T>((List)Predicates.<Predicate<? super T>>defensiveCopy(components));
    }
    
    public static <T> Predicate<T> and(final Predicate<? super T> first, final Predicate<? super T> second) {
        return new AndPredicate<T>((List)Predicates.asList(Preconditions.<Predicate<? super Object>>checkNotNull(first), Preconditions.<Predicate<? super Object>>checkNotNull(second)));
    }
    
    public static <T> Predicate<T> or(final Iterable<? extends Predicate<? super T>> components) {
        return new OrPredicate<T>((List)Predicates.defensiveCopy(components));
    }
    
    public static <T> Predicate<T> or(final Predicate<? super T>... components) {
        return new OrPredicate<T>((List)Predicates.<Predicate<? super T>>defensiveCopy(components));
    }
    
    public static <T> Predicate<T> or(final Predicate<? super T> first, final Predicate<? super T> second) {
        return new OrPredicate<T>((List)Predicates.asList(Preconditions.<Predicate<? super Object>>checkNotNull(first), Preconditions.<Predicate<? super Object>>checkNotNull(second)));
    }
    
    public static <T> Predicate<T> equalTo(@Nullable final T target) {
        return (target == null) ? Predicates.<T>isNull() : new IsEqualToPredicate<T>(target);
    }
    
    @GwtIncompatible
    public static Predicate<Object> instanceOf(final Class<?> clazz) {
        return new InstanceOfPredicate((Class)clazz);
    }
    
    @Deprecated
    @GwtIncompatible
    @Beta
    public static Predicate<Class<?>> assignableFrom(final Class<?> clazz) {
        return subtypeOf(clazz);
    }
    
    @GwtIncompatible
    @Beta
    public static Predicate<Class<?>> subtypeOf(final Class<?> clazz) {
        return new SubtypeOfPredicate((Class)clazz);
    }
    
    public static <T> Predicate<T> in(final Collection<? extends T> target) {
        return new InPredicate<T>((Collection)target);
    }
    
    public static <A, B> Predicate<A> compose(final Predicate<B> predicate, final Function<A, ? extends B> function) {
        return new CompositionPredicate<A, Object>((Predicate)predicate, (Function)function);
    }
    
    @GwtIncompatible
    public static Predicate<CharSequence> containsPattern(final String pattern) {
        return new ContainsPatternFromStringPredicate(pattern);
    }
    
    @GwtIncompatible("java.util.regex.Pattern")
    public static Predicate<CharSequence> contains(final Pattern pattern) {
        return new ContainsPatternPredicate(new JdkPattern(pattern));
    }
    
    private static <T> List<Predicate<? super T>> asList(final Predicate<? super T> first, final Predicate<? super T> second) {
        return (List<Predicate<? super T>>)Arrays.asList((Object[])new Predicate[] { first, second });
    }
    
    private static <T> List<T> defensiveCopy(final T... array) {
        return Predicates.<T>defensiveCopy((java.lang.Iterable<T>)Arrays.asList((Object[])array));
    }
    
    static <T> List<T> defensiveCopy(final Iterable<T> iterable) {
        final ArrayList<T> list = (ArrayList<T>)new ArrayList();
        for (final T element : iterable) {
            list.add(Preconditions.<T>checkNotNull(element));
        }
        return (List<T>)list;
    }
    
    static {
        COMMA_JOINER = Joiner.on(',');
    }
    
    enum ObjectPredicate implements Predicate<Object> {
        ALWAYS_TRUE {
            public boolean apply(@Nullable final Object o) {
                return true;
            }
            
            public String toString() {
                return "Predicates.alwaysTrue()";
            }
        }, 
        ALWAYS_FALSE {
            public boolean apply(@Nullable final Object o) {
                return false;
            }
            
            public String toString() {
                return "Predicates.alwaysFalse()";
            }
        }, 
        IS_NULL {
            public boolean apply(@Nullable final Object o) {
                return o == null;
            }
            
            public String toString() {
                return "Predicates.isNull()";
            }
        }, 
        NOT_NULL {
            public boolean apply(@Nullable final Object o) {
                return o != null;
            }
            
            public String toString() {
                return "Predicates.notNull()";
            }
        };
        
         <T> Predicate<T> withNarrowedType() {
            return (Predicate<T>)this;
        }
    }
    
    private static class NotPredicate<T> implements Predicate<T>, Serializable {
        final Predicate<T> predicate;
        private static final long serialVersionUID = 0L;
        
        NotPredicate(final Predicate<T> predicate) {
            this.predicate = Preconditions.<Predicate<T>>checkNotNull(predicate);
        }
        
        public boolean apply(@Nullable final T t) {
            return !this.predicate.apply(t);
        }
        
        public int hashCode() {
            return ~this.predicate.hashCode();
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof NotPredicate) {
                final NotPredicate<?> that = obj;
                return this.predicate.equals(that.predicate);
            }
            return false;
        }
        
        public String toString() {
            return new StringBuilder().append("Predicates.not(").append(this.predicate).append(")").toString();
        }
    }
    
    private static class AndPredicate<T> implements Predicate<T>, Serializable {
        private final List<? extends Predicate<? super T>> components;
        private static final long serialVersionUID = 0L;
        
        private AndPredicate(final List<? extends Predicate<? super T>> components) {
            this.components = components;
        }
        
        public boolean apply(@Nullable final T t) {
            for (int i = 0; i < this.components.size(); ++i) {
                if (!((Predicate)this.components.get(i)).apply(t)) {
                    return false;
                }
            }
            return true;
        }
        
        public int hashCode() {
            return this.components.hashCode() + 306654252;
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof AndPredicate) {
                final AndPredicate<?> that = obj;
                return this.components.equals(that.components);
            }
            return false;
        }
        
        public String toString() {
            return "Predicates.and(" + Predicates.COMMA_JOINER.join(this.components) + ")";
        }
    }
    
    private static class OrPredicate<T> implements Predicate<T>, Serializable {
        private final List<? extends Predicate<? super T>> components;
        private static final long serialVersionUID = 0L;
        
        private OrPredicate(final List<? extends Predicate<? super T>> components) {
            this.components = components;
        }
        
        public boolean apply(@Nullable final T t) {
            for (int i = 0; i < this.components.size(); ++i) {
                if (((Predicate)this.components.get(i)).apply(t)) {
                    return true;
                }
            }
            return false;
        }
        
        public int hashCode() {
            return this.components.hashCode() + 87855567;
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof OrPredicate) {
                final OrPredicate<?> that = obj;
                return this.components.equals(that.components);
            }
            return false;
        }
        
        public String toString() {
            return "Predicates.or(" + Predicates.COMMA_JOINER.join(this.components) + ")";
        }
    }
    
    private static class IsEqualToPredicate<T> implements Predicate<T>, Serializable {
        private final T target;
        private static final long serialVersionUID = 0L;
        
        private IsEqualToPredicate(final T target) {
            this.target = target;
        }
        
        public boolean apply(final T t) {
            return this.target.equals(t);
        }
        
        public int hashCode() {
            return this.target.hashCode();
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof IsEqualToPredicate) {
                final IsEqualToPredicate<?> that = obj;
                return this.target.equals(that.target);
            }
            return false;
        }
        
        public String toString() {
            return new StringBuilder().append("Predicates.equalTo(").append(this.target).append(")").toString();
        }
    }
    
    @GwtIncompatible
    private static class InstanceOfPredicate implements Predicate<Object>, Serializable {
        private final Class<?> clazz;
        private static final long serialVersionUID = 0L;
        
        private InstanceOfPredicate(final Class<?> clazz) {
            this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
        }
        
        public boolean apply(@Nullable final Object o) {
            return this.clazz.isInstance(o);
        }
        
        public int hashCode() {
            return this.clazz.hashCode();
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof InstanceOfPredicate) {
                final InstanceOfPredicate that = (InstanceOfPredicate)obj;
                return this.clazz == that.clazz;
            }
            return false;
        }
        
        public String toString() {
            return "Predicates.instanceOf(" + this.clazz.getName() + ")";
        }
    }
    
    @GwtIncompatible
    private static class SubtypeOfPredicate implements Predicate<Class<?>>, Serializable {
        private final Class<?> clazz;
        private static final long serialVersionUID = 0L;
        
        private SubtypeOfPredicate(final Class<?> clazz) {
            this.clazz = Preconditions.<Class<?>>checkNotNull(clazz);
        }
        
        public boolean apply(final Class<?> input) {
            return this.clazz.isAssignableFrom((Class)input);
        }
        
        public int hashCode() {
            return this.clazz.hashCode();
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof SubtypeOfPredicate) {
                final SubtypeOfPredicate that = (SubtypeOfPredicate)obj;
                return this.clazz == that.clazz;
            }
            return false;
        }
        
        public String toString() {
            return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
        }
    }
    
    private static class InPredicate<T> implements Predicate<T>, Serializable {
        private final Collection<?> target;
        private static final long serialVersionUID = 0L;
        
        private InPredicate(final Collection<?> target) {
            this.target = Preconditions.<Collection<?>>checkNotNull(target);
        }
        
        public boolean apply(@Nullable final T t) {
            try {
                return this.target.contains(t);
            }
            catch (NullPointerException e) {
                return false;
            }
            catch (ClassCastException e2) {
                return false;
            }
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof InPredicate) {
                final InPredicate<?> that = obj;
                return this.target.equals(that.target);
            }
            return false;
        }
        
        public int hashCode() {
            return this.target.hashCode();
        }
        
        public String toString() {
            return new StringBuilder().append("Predicates.in(").append(this.target).append(")").toString();
        }
    }
    
    private static class CompositionPredicate<A, B> implements Predicate<A>, Serializable {
        final Predicate<B> p;
        final Function<A, ? extends B> f;
        private static final long serialVersionUID = 0L;
        
        private CompositionPredicate(final Predicate<B> p, final Function<A, ? extends B> f) {
            this.p = Preconditions.<Predicate<B>>checkNotNull(p);
            this.f = Preconditions.<Function<A, ? extends B>>checkNotNull(f);
        }
        
        public boolean apply(@Nullable final A a) {
            return this.p.apply((B)this.f.apply(a));
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof CompositionPredicate) {
                final CompositionPredicate<?, ?> that = obj;
                return this.f.equals(that.f) && this.p.equals(that.p);
            }
            return false;
        }
        
        public int hashCode() {
            return this.f.hashCode() ^ this.p.hashCode();
        }
        
        public String toString() {
            return new StringBuilder().append(this.p).append("(").append(this.f).append(")").toString();
        }
    }
    
    @GwtIncompatible
    private static class ContainsPatternPredicate implements Predicate<CharSequence>, Serializable {
        final CommonPattern pattern;
        private static final long serialVersionUID = 0L;
        
        ContainsPatternPredicate(final CommonPattern pattern) {
            this.pattern = Preconditions.<CommonPattern>checkNotNull(pattern);
        }
        
        public boolean apply(final CharSequence t) {
            return this.pattern.matcher(t).find();
        }
        
        public int hashCode() {
            return Objects.hashCode(this.pattern.pattern(), this.pattern.flags());
        }
        
        public boolean equals(@Nullable final Object obj) {
            if (obj instanceof ContainsPatternPredicate) {
                final ContainsPatternPredicate that = (ContainsPatternPredicate)obj;
                return Objects.equal(this.pattern.pattern(), that.pattern.pattern()) && this.pattern.flags() == that.pattern.flags();
            }
            return false;
        }
        
        public String toString() {
            final String patternString = MoreObjects.toStringHelper(this.pattern).add("pattern", this.pattern.pattern()).add("pattern.flags", this.pattern.flags()).toString();
            return "Predicates.contains(" + patternString + ")";
        }
    }
    
    @GwtIncompatible
    private static class ContainsPatternFromStringPredicate extends ContainsPatternPredicate {
        private static final long serialVersionUID = 0L;
        
        ContainsPatternFromStringPredicate(final String string) {
            super(Platform.compilePattern(string));
        }
        
        @Override
        public String toString() {
            return "Predicates.containsPattern(" + this.pattern.pattern() + ")";
        }
    }
}
