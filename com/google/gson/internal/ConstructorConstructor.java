package com.google.gson.internal;

import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.LinkedHashSet;
import java.util.Set;
import com.google.gson.JsonIOException;
import java.lang.reflect.ParameterizedType;
import java.util.EnumSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.util.Map;

public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    
    public ConstructorConstructor(final Map<Type, InstanceCreator<?>> instanceCreators) {
        this.instanceCreators = instanceCreators;
    }
    
    public <T> ObjectConstructor<T> get(final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class<? super T> rawType = typeToken.getRawType();
        final InstanceCreator<T> typeCreator = (InstanceCreator<T>)this.instanceCreators.get(type);
        if (typeCreator != null) {
            return new ObjectConstructor<T>() {
                public T construct() {
                    return typeCreator.createInstance(type);
                }
            };
        }
        final InstanceCreator<T> rawTypeCreator = (InstanceCreator<T>)this.instanceCreators.get(rawType);
        if (rawTypeCreator != null) {
            return new ObjectConstructor<T>() {
                public T construct() {
                    return rawTypeCreator.createInstance(type);
                }
            };
        }
        final ObjectConstructor<T> defaultConstructor = this.<T>newDefaultConstructor(rawType);
        if (defaultConstructor != null) {
            return defaultConstructor;
        }
        final ObjectConstructor<T> defaultImplementation = this.<T>newDefaultImplementationConstructor(type, rawType);
        if (defaultImplementation != null) {
            return defaultImplementation;
        }
        return this.<T>newUnsafeAllocator(type, rawType);
    }
    
    private <T> ObjectConstructor<T> newDefaultConstructor(final Class<? super T> rawType) {
        try {
            final Constructor<? super T> constructor = rawType.getDeclaredConstructor(new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return new ObjectConstructor<T>() {
                public T construct() {
                    try {
                        final Object[] args = null;
                        return (T)constructor.newInstance(args);
                    }
                    catch (InstantiationException e) {
                        throw new RuntimeException(new StringBuilder().append("Failed to invoke ").append(constructor).append(" with no args").toString(), (Throwable)e);
                    }
                    catch (InvocationTargetException e2) {
                        throw new RuntimeException(new StringBuilder().append("Failed to invoke ").append(constructor).append(" with no args").toString(), e2.getTargetException());
                    }
                    catch (IllegalAccessException e3) {
                        throw new AssertionError(e3);
                    }
                }
            };
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type type, final Class<? super T> rawType) {
        if (Collection.class.isAssignableFrom((Class)rawType)) {
            if (SortedSet.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        if (!(type instanceof ParameterizedType)) {
                            throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                        }
                        final Type elementType = ((ParameterizedType)type).getActualTypeArguments()[0];
                        if (elementType instanceof Class) {
                            return (T)EnumSet.noneOf((Class)elementType);
                        }
                        throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                    }
                };
            }
            if (Set.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new ArrayDeque();
                    }
                };
            }
            return new ObjectConstructor<T>() {
                public T construct() {
                    return (T)new ArrayList();
                }
            };
        }
        else {
            if (!Map.class.isAssignableFrom((Class)rawType)) {
                return null;
            }
            if (ConcurrentNavigableMap.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new ConcurrentSkipListMap();
                    }
                };
            }
            if (ConcurrentMap.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new ConcurrentHashMap();
                    }
                };
            }
            if (SortedMap.class.isAssignableFrom((Class)rawType)) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom((Class)TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>() {
                    public T construct() {
                        return (T)new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>() {
                public T construct() {
                    return (T)new LinkedTreeMap();
                }
            };
        }
    }
    
    private <T> ObjectConstructor<T> newUnsafeAllocator(final Type type, final Class<? super T> rawType) {
        return new ObjectConstructor<T>() {
            private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            
            public T construct() {
                try {
                    final Object newInstance = this.unsafeAllocator.newInstance(rawType);
                    return (T)newInstance;
                }
                catch (Exception e) {
                    throw new RuntimeException(new StringBuilder().append("Unable to invoke no-args constructor for ").append(type).append(". Register an InstanceCreator with Gson for this type may fix this problem.").toString(), (Throwable)e);
                }
            }
        };
    }
    
    public String toString() {
        return this.instanceCreators.toString();
    }
}
