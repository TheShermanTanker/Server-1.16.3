package com.google.common.base;

import java.util.Iterator;
import java.util.Set;
import com.google.common.annotations.Beta;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
public abstract class Optional<T> implements Serializable {
    private static final long serialVersionUID = 0L;
    
    public static <T> Optional<T> absent() {
        return Absent.<T>withType();
    }
    
    public static <T> Optional<T> of(final T reference) {
        return new Present<T>(Preconditions.<T>checkNotNull(reference));
    }
    
    public static <T> Optional<T> fromNullable(@Nullable final T nullableReference) {
        return (nullableReference == null) ? Optional.<T>absent() : new Present<T>(nullableReference);
    }
    
    @Nullable
    public static <T> Optional<T> fromJavaUtil(@Nullable final java.util.Optional<T> javaUtilOptional) {
        return (javaUtilOptional == null) ? null : Optional.<T>fromNullable(javaUtilOptional.orElse(null));
    }
    
    @Nullable
    public static <T> java.util.Optional<T> toJavaUtil(@Nullable final Optional<T> googleOptional) {
        return (googleOptional == null) ? null : googleOptional.toJavaUtil();
    }
    
    Optional() {
    }
    
    public abstract boolean isPresent();
    
    public abstract T get();
    
    public abstract T or(final T object);
    
    public abstract Optional<T> or(final Optional<? extends T> optional);
    
    @Beta
    public abstract T or(final Supplier<? extends T> supplier);
    
    @Nullable
    public abstract T orNull();
    
    public abstract Set<T> asSet();
    
    public abstract <V> Optional<V> transform(final Function<? super T, V> function);
    
    public java.util.Optional<T> toJavaUtil() {
        return (java.util.Optional<T>)java.util.Optional.ofNullable(this.orNull());
    }
    
    public abstract boolean equals(@Nullable final Object object);
    
    public abstract int hashCode();
    
    public abstract String toString();
    
    @Beta
    public static <T> Iterable<T> presentInstances(final Iterable<? extends Optional<? extends T>> optionals) {
        Preconditions.<Iterable<? extends Optional<? extends T>>>checkNotNull(optionals);
        return (Iterable<T>)new Iterable<T>() {
            public Iterator<T> iterator() {
                return (Iterator<T>)new AbstractIterator<T>() {
                    private final Iterator<? extends Optional<? extends T>> iterator = Preconditions.<Iterator>checkNotNull(optionals.iterator());
                    
                    @Override
                    protected T computeNext() {
                        while (this.iterator.hasNext()) {
                            final Optional<? extends T> optional = this.iterator.next();
                            if (optional.isPresent()) {
                                return (T)optional.get();
                            }
                        }
                        return this.endOfData();
                    }
                };
            }
        };
    }
}
