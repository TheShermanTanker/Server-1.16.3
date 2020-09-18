package com.google.common.reflect;

import com.google.common.base.Objects;
import javax.annotation.Nullable;
import com.google.common.base.Joiner;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.GenericDeclaration;
import java.util.Arrays;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.WildcardType;
import java.lang.reflect.TypeVariable;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.common.annotations.Beta;

@Beta
public final class TypeResolver {
    private final TypeTable typeTable;
    
    public TypeResolver() {
        this.typeTable = new TypeTable();
    }
    
    private TypeResolver(final TypeTable typeTable) {
        this.typeTable = typeTable;
    }
    
    static TypeResolver accordingTo(final Type type) {
        return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
    }
    
    public TypeResolver where(final Type formal, final Type actual) {
        final Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
        populateTypeMappings(mappings, Preconditions.<Type>checkNotNull(formal), Preconditions.<Type>checkNotNull(actual));
        return this.where(mappings);
    }
    
    TypeResolver where(final Map<TypeVariableKey, ? extends Type> mappings) {
        return new TypeResolver(this.typeTable.where(mappings));
    }
    
    private static void populateTypeMappings(final Map<TypeVariableKey, Type> mappings, final Type from, final Type to) {
        if (from.equals(to)) {
            return;
        }
        new TypeVisitor() {
            @Override
            void visitTypeVariable(final TypeVariable<?> typeVariable) {
                mappings.put(new TypeVariableKey(typeVariable), to);
            }
            
            @Override
            void visitWildcardType(final WildcardType fromWildcardType) {
                if (!(to instanceof WildcardType)) {
                    return;
                }
                final WildcardType toWildcardType = (WildcardType)to;
                final Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
                final Type[] toUpperBounds = toWildcardType.getUpperBounds();
                final Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
                final Type[] toLowerBounds = toWildcardType.getLowerBounds();
                Preconditions.checkArgument(fromUpperBounds.length == toUpperBounds.length && fromLowerBounds.length == toLowerBounds.length, "Incompatible type: %s vs. %s", fromWildcardType, to);
                for (int i = 0; i < fromUpperBounds.length; ++i) {
                    populateTypeMappings(mappings, fromUpperBounds[i], toUpperBounds[i]);
                }
                for (int i = 0; i < fromLowerBounds.length; ++i) {
                    populateTypeMappings(mappings, fromLowerBounds[i], toLowerBounds[i]);
                }
            }
            
            @Override
            void visitParameterizedType(final ParameterizedType fromParameterizedType) {
                if (to instanceof WildcardType) {
                    return;
                }
                final ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument((java.lang.Class<Object>)ParameterizedType.class, to);
                if (fromParameterizedType.getOwnerType() != null && toParameterizedType.getOwnerType() != null) {
                    populateTypeMappings(mappings, fromParameterizedType.getOwnerType(), toParameterizedType.getOwnerType());
                }
                Preconditions.checkArgument(fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
                final Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
                final Type[] toArgs = toParameterizedType.getActualTypeArguments();
                Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);
                for (int i = 0; i < fromArgs.length; ++i) {
                    populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
                }
            }
            
            @Override
            void visitGenericArrayType(final GenericArrayType fromArrayType) {
                if (to instanceof WildcardType) {
                    return;
                }
                final Type componentType = Types.getComponentType(to);
                Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
                populateTypeMappings(mappings, fromArrayType.getGenericComponentType(), componentType);
            }
            
            @Override
            void visitClass(final Class<?> fromClass) {
                if (to instanceof WildcardType) {
                    return;
                }
                throw new IllegalArgumentException(new StringBuilder().append("No type mapping from ").append(fromClass).append(" to ").append(to).toString());
            }
        }.visit(from);
    }
    
    public Type resolveType(final Type type) {
        Preconditions.<Type>checkNotNull(type);
        if (type instanceof TypeVariable) {
            return this.typeTable.resolve(type);
        }
        if (type instanceof ParameterizedType) {
            return (Type)this.resolveParameterizedType((ParameterizedType)type);
        }
        if (type instanceof GenericArrayType) {
            return this.resolveGenericArrayType((GenericArrayType)type);
        }
        if (type instanceof WildcardType) {
            return (Type)this.resolveWildcardType((WildcardType)type);
        }
        return type;
    }
    
    private Type[] resolveTypes(final Type[] types) {
        final Type[] result = new Type[types.length];
        for (int i = 0; i < types.length; ++i) {
            result[i] = this.resolveType(types[i]);
        }
        return result;
    }
    
    private WildcardType resolveWildcardType(final WildcardType type) {
        final Type[] lowerBounds = type.getLowerBounds();
        final Type[] upperBounds = type.getUpperBounds();
        return (WildcardType)new Types.WildcardTypeImpl(this.resolveTypes(lowerBounds), this.resolveTypes(upperBounds));
    }
    
    private Type resolveGenericArrayType(final GenericArrayType type) {
        final Type componentType = type.getGenericComponentType();
        final Type resolvedComponentType = this.resolveType(componentType);
        return Types.newArrayType(resolvedComponentType);
    }
    
    private ParameterizedType resolveParameterizedType(final ParameterizedType type) {
        final Type owner = type.getOwnerType();
        final Type resolvedOwner = (owner == null) ? null : this.resolveType(owner);
        final Type resolvedRawType = this.resolveType(type.getRawType());
        final Type[] args = type.getActualTypeArguments();
        final Type[] resolvedArgs = this.resolveTypes(args);
        return Types.newParameterizedTypeWithOwner(resolvedOwner, resolvedRawType, resolvedArgs);
    }
    
    private static <T> T expectArgument(final Class<T> type, final Object arg) {
        try {
            return (T)type.cast(arg);
        }
        catch (ClassCastException e) {
            throw new IllegalArgumentException(new StringBuilder().append(arg).append(" is not a ").append(type.getSimpleName()).toString());
        }
    }
    
    private static class TypeTable {
        private final ImmutableMap<TypeVariableKey, Type> map;
        
        TypeTable() {
            this.map = ImmutableMap.<TypeVariableKey, Type>of();
        }
        
        private TypeTable(final ImmutableMap<TypeVariableKey, Type> map) {
            this.map = map;
        }
        
        final TypeTable where(final Map<TypeVariableKey, ? extends Type> mappings) {
            final ImmutableMap.Builder<TypeVariableKey, Type> builder = ImmutableMap.<TypeVariableKey, Type>builder();
            builder.putAll((java.util.Map<? extends TypeVariableKey, ? extends Type>)this.map);
            for (final Map.Entry<TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
                final TypeVariableKey variable = (TypeVariableKey)mapping.getKey();
                final Type type = (Type)mapping.getValue();
                Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
                builder.put(variable, type);
            }
            return new TypeTable(builder.build());
        }
        
        final Type resolve(final TypeVariable<?> var) {
            final TypeTable unguarded = this;
            final TypeTable guarded = new TypeTable() {
                public Type resolveInternal(final TypeVariable<?> intermediateVar, final TypeTable forDependent) {
                    if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
                        return (Type)intermediateVar;
                    }
                    return unguarded.resolveInternal(intermediateVar, forDependent);
                }
            };
            return this.resolveInternal(var, guarded);
        }
        
        Type resolveInternal(final TypeVariable<?> var, final TypeTable forDependants) {
            final Type type = this.map.get(new TypeVariableKey(var));
            if (type != null) {
                return new TypeResolver(forDependants, null).resolveType(type);
            }
            final Type[] bounds = var.getBounds();
            if (bounds.length == 0) {
                return (Type)var;
            }
            final Type[] resolvedBounds = new TypeResolver(forDependants, null).resolveTypes(bounds);
            if (Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY && Arrays.equals((Object[])bounds, (Object[])resolvedBounds)) {
                return (Type)var;
            }
            return (Type)Types.<GenericDeclaration>newArtificialTypeVariable(var.getGenericDeclaration(), var.getName(), resolvedBounds);
        }
    }
    
    private static final class TypeMappingIntrospector extends TypeVisitor {
        private static final WildcardCapturer wildcardCapturer;
        private final Map<TypeVariableKey, Type> mappings;
        
        private TypeMappingIntrospector() {
            this.mappings = Maps.newHashMap();
        }
        
        static ImmutableMap<TypeVariableKey, Type> getTypeMappings(final Type contextType) {
            final TypeMappingIntrospector introspector = new TypeMappingIntrospector();
            introspector.visit(TypeMappingIntrospector.wildcardCapturer.capture(contextType));
            return ImmutableMap.<TypeVariableKey, Type>copyOf((java.util.Map<? extends TypeVariableKey, ? extends Type>)introspector.mappings);
        }
        
        @Override
        void visitClass(final Class<?> clazz) {
            this.visit(clazz.getGenericSuperclass());
            this.visit(clazz.getGenericInterfaces());
        }
        
        @Override
        void visitParameterizedType(final ParameterizedType parameterizedType) {
            final Class<?> rawClass = parameterizedType.getRawType();
            final TypeVariable<?>[] vars = rawClass.getTypeParameters();
            final Type[] typeArgs = parameterizedType.getActualTypeArguments();
            Preconditions.checkState(vars.length == typeArgs.length);
            for (int i = 0; i < vars.length; ++i) {
                this.map(new TypeVariableKey(vars[i]), typeArgs[i]);
            }
            this.visit((Type)rawClass);
            this.visit(parameterizedType.getOwnerType());
        }
        
        @Override
        void visitTypeVariable(final TypeVariable<?> t) {
            this.visit(t.getBounds());
        }
        
        @Override
        void visitWildcardType(final WildcardType t) {
            this.visit(t.getUpperBounds());
        }
        
        private void map(final TypeVariableKey var, final Type arg) {
            if (this.mappings.containsKey(var)) {
                return;
            }
            for (Type t = arg; t != null; t = (Type)this.mappings.get(TypeVariableKey.forLookup(t))) {
                if (var.equalsType(t)) {
                    for (Type x = arg; x != null; x = (Type)this.mappings.remove(TypeVariableKey.forLookup(x))) {}
                    return;
                }
            }
            this.mappings.put(var, arg);
        }
        
        static {
            wildcardCapturer = new WildcardCapturer();
        }
    }
    
    private static final class WildcardCapturer {
        private final AtomicInteger id;
        
        private WildcardCapturer() {
            this.id = new AtomicInteger();
        }
        
        Type capture(final Type type) {
            Preconditions.<Type>checkNotNull(type);
            if (type instanceof Class) {
                return type;
            }
            if (type instanceof TypeVariable) {
                return type;
            }
            if (type instanceof GenericArrayType) {
                final GenericArrayType arrayType = (GenericArrayType)type;
                return Types.newArrayType(this.capture(arrayType.getGenericComponentType()));
            }
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType)type;
                return (Type)Types.newParameterizedTypeWithOwner(this.captureNullable(parameterizedType.getOwnerType()), parameterizedType.getRawType(), this.capture(parameterizedType.getActualTypeArguments()));
            }
            if (!(type instanceof WildcardType)) {
                throw new AssertionError("must have been one of the known types");
            }
            final WildcardType wildcardType = (WildcardType)type;
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            if (lowerBounds.length == 0) {
                final Type[] upperBounds = wildcardType.getUpperBounds();
                final String name = new StringBuilder().append("capture#").append(this.id.incrementAndGet()).append("-of ? extends ").append(Joiner.on('&').join(upperBounds)).toString();
                return (Type)Types.<Class<WildcardCapturer>>newArtificialTypeVariable(WildcardCapturer.class, name, wildcardType.getUpperBounds());
            }
            return type;
        }
        
        private Type captureNullable(@Nullable final Type type) {
            if (type == null) {
                return null;
            }
            return this.capture(type);
        }
        
        private Type[] capture(final Type[] types) {
            final Type[] result = new Type[types.length];
            for (int i = 0; i < types.length; ++i) {
                result[i] = this.capture(types[i]);
            }
            return result;
        }
    }
    
    static final class TypeVariableKey {
        private final TypeVariable<?> var;
        
        TypeVariableKey(final TypeVariable<?> var) {
            this.var = Preconditions.<TypeVariable<?>>checkNotNull(var);
        }
        
        public int hashCode() {
            return Objects.hashCode(this.var.getGenericDeclaration(), this.var.getName());
        }
        
        public boolean equals(final Object obj) {
            if (obj instanceof TypeVariableKey) {
                final TypeVariableKey that = (TypeVariableKey)obj;
                return this.equalsTypeVariable(that.var);
            }
            return false;
        }
        
        public String toString() {
            return this.var.toString();
        }
        
        static TypeVariableKey forLookup(final Type t) {
            if (t instanceof TypeVariable) {
                return new TypeVariableKey(t);
            }
            return null;
        }
        
        boolean equalsType(final Type type) {
            return type instanceof TypeVariable && this.equalsTypeVariable(type);
        }
        
        private boolean equalsTypeVariable(final TypeVariable<?> that) {
            return this.var.getGenericDeclaration().equals(that.getGenericDeclaration()) && this.var.getName().equals(that.getName());
        }
    }
}
