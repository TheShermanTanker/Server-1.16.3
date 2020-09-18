package com.google.common.reflect;

import com.google.common.collect.FluentIterable;
import javax.annotation.Nullable;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.base.Preconditions;
import java.lang.annotation.Annotation;
import com.google.common.collect.ImmutableList;
import com.google.common.annotations.Beta;
import java.lang.reflect.AnnotatedElement;

@Beta
public final class Parameter implements AnnotatedElement {
    private final Invokable<?, ?> declaration;
    private final int position;
    private final TypeToken<?> type;
    private final ImmutableList<Annotation> annotations;
    
    Parameter(final Invokable<?, ?> declaration, final int position, final TypeToken<?> type, final Annotation[] annotations) {
        this.declaration = declaration;
        this.position = position;
        this.type = type;
        this.annotations = ImmutableList.<Annotation>copyOf(annotations);
    }
    
    public TypeToken<?> getType() {
        return this.type;
    }
    
    public Invokable<?, ?> getDeclaringInvokable() {
        return this.declaration;
    }
    
    public boolean isAnnotationPresent(final Class<? extends Annotation> annotationType) {
        return this.getAnnotation(annotationType) != null;
    }
    
    @Nullable
    public <A extends Annotation> A getAnnotation(final Class<A> annotationType) {
        Preconditions.<Class<A>>checkNotNull(annotationType);
        for (final Annotation annotation : this.annotations) {
            if (annotationType.isInstance(annotation)) {
                return (A)annotationType.cast(annotation);
            }
        }
        return null;
    }
    
    public Annotation[] getAnnotations() {
        return this.getDeclaredAnnotations();
    }
    
    public <A extends Annotation> A[] getAnnotationsByType(final Class<A> annotationType) {
        return (A[])this.<Annotation>getDeclaredAnnotationsByType((java.lang.Class<Annotation>)annotationType);
    }
    
    public Annotation[] getDeclaredAnnotations() {
        return this.annotations.<Annotation>toArray(new Annotation[this.annotations.size()]);
    }
    
    @Nullable
    public <A extends Annotation> A getDeclaredAnnotation(final Class<A> annotationType) {
        Preconditions.<Class<A>>checkNotNull(annotationType);
        return FluentIterable.from((java.lang.Iterable<Object>)this.annotations).<A>filter(annotationType).first().orNull();
    }
    
    public <A extends Annotation> A[] getDeclaredAnnotationsByType(final Class<A> annotationType) {
        return FluentIterable.from((java.lang.Iterable<Object>)this.annotations).<A>filter(annotationType).toArray(annotationType);
    }
    
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof Parameter) {
            final Parameter that = (Parameter)obj;
            return this.position == that.position && this.declaration.equals(that.declaration);
        }
        return false;
    }
    
    public int hashCode() {
        return this.position;
    }
    
    public String toString() {
        return new StringBuilder().append(this.type).append(" arg").append(this.position).toString();
    }
}
