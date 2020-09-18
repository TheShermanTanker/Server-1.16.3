package com.google.gson;

import java.util.Arrays;
import java.util.Collection;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import com.google.gson.internal.$Gson$Preconditions;
import java.lang.reflect.Field;

public final class FieldAttributes {
    private final Field field;
    
    public FieldAttributes(final Field f) {
        $Gson$Preconditions.<Field>checkNotNull(f);
        this.field = f;
    }
    
    public Class<?> getDeclaringClass() {
        return this.field.getDeclaringClass();
    }
    
    public String getName() {
        return this.field.getName();
    }
    
    public Type getDeclaredType() {
        return this.field.getGenericType();
    }
    
    public Class<?> getDeclaredClass() {
        return this.field.getType();
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotation) {
        return (T)this.field.getAnnotation((Class)annotation);
    }
    
    public Collection<Annotation> getAnnotations() {
        return (Collection<Annotation>)Arrays.asList((Object[])this.field.getAnnotations());
    }
    
    public boolean hasModifier(final int modifier) {
        return (this.field.getModifiers() & modifier) != 0x0;
    }
    
    Object get(final Object instance) throws IllegalAccessException {
        return this.field.get(instance);
    }
    
    boolean isSynthetic() {
        return this.field.isSynthetic();
    }
}
