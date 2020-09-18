package com.google.common.eventbus;

import javax.annotation.Nullable;
import com.google.common.base.Objects;
import java.util.Arrays;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.common.base.Throwables;
import com.google.common.reflect.TypeToken;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.UnmodifiableIterator;
import java.util.List;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.annotations.VisibleForTesting;
import java.util.Set;
import java.util.Iterator;
import com.google.common.collect.Multimap;
import com.google.common.base.MoreObjects;
import java.util.Collection;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Method;
import com.google.common.collect.ImmutableList;
import com.google.common.cache.LoadingCache;
import com.google.j2objc.annotations.Weak;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ConcurrentMap;

final class SubscriberRegistry {
    private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers;
    @Weak
    private final EventBus bus;
    private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache;
    private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache;
    
    SubscriberRegistry(final EventBus bus) {
        this.subscribers = Maps.<Class<?>, CopyOnWriteArraySet<Subscriber>>newConcurrentMap();
        this.bus = Preconditions.<EventBus>checkNotNull(bus);
    }
    
    void register(final Object listener) {
        final Multimap<Class<?>, Subscriber> listenerMethods = this.findAllSubscribers(listener);
        for (final Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
            final Class<?> eventType = entry.getKey();
            final Collection<Subscriber> eventMethodsInListener = (Collection<Subscriber>)entry.getValue();
            CopyOnWriteArraySet<Subscriber> eventSubscribers = (CopyOnWriteArraySet<Subscriber>)this.subscribers.get(eventType);
            if (eventSubscribers == null) {
                final CopyOnWriteArraySet<Subscriber> newSet = (CopyOnWriteArraySet<Subscriber>)new CopyOnWriteArraySet();
                eventSubscribers = MoreObjects.firstNonNull(this.subscribers.putIfAbsent(eventType, newSet), newSet);
            }
            eventSubscribers.addAll((Collection)eventMethodsInListener);
        }
    }
    
    void unregister(final Object listener) {
        final Multimap<Class<?>, Subscriber> listenerMethods = this.findAllSubscribers(listener);
        for (final Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
            final Class<?> eventType = entry.getKey();
            final Collection<Subscriber> listenerMethodsForType = (Collection<Subscriber>)entry.getValue();
            final CopyOnWriteArraySet<Subscriber> currentSubscribers = (CopyOnWriteArraySet<Subscriber>)this.subscribers.get(eventType);
            if (currentSubscribers == null || !currentSubscribers.removeAll((Collection)listenerMethodsForType)) {
                throw new IllegalArgumentException(new StringBuilder().append("missing event subscriber for an annotated method. Is ").append(listener).append(" registered?").toString());
            }
        }
    }
    
    @VisibleForTesting
    Set<Subscriber> getSubscribersForTesting(final Class<?> eventType) {
        return MoreObjects.firstNonNull(this.subscribers.get(eventType), ImmutableSet.of());
    }
    
    Iterator<Subscriber> getSubscribers(final Object event) {
        final ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
        final List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());
        for (final Class<?> eventType : eventTypes) {
            final CopyOnWriteArraySet<Subscriber> eventSubscribers = (CopyOnWriteArraySet<Subscriber>)this.subscribers.get(eventType);
            if (eventSubscribers != null) {
                subscriberIterators.add(eventSubscribers.iterator());
            }
        }
        return Iterators.<Subscriber>concat((java.util.Iterator<? extends java.util.Iterator<? extends Subscriber>>)subscriberIterators.iterator());
    }
    
    private Multimap<Class<?>, Subscriber> findAllSubscribers(final Object listener) {
        final Multimap<Class<?>, Subscriber> methodsInListener = HashMultimap.create();
        final Class<?> clazz = listener.getClass();
        for (final Method method : getAnnotatedMethods(clazz)) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Class<?> eventType = parameterTypes[0];
            methodsInListener.put(eventType, Subscriber.create(this.bus, listener, method));
        }
        return methodsInListener;
    }
    
    private static ImmutableList<Method> getAnnotatedMethods(final Class<?> clazz) {
        return SubscriberRegistry.subscriberMethodsCache.getUnchecked(clazz);
    }
    
    private static ImmutableList<Method> getAnnotatedMethodsNotCached(final Class<?> clazz) {
        final Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
        final Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
        for (final Class<?> supertype : supertypes) {
            for (final Method method : supertype.getDeclaredMethods()) {
                if (method.isAnnotationPresent((Class)Subscribe.class) && !method.isSynthetic()) {
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    Preconditions.checkArgument(parameterTypes.length == 1, "Method %s has @Subscribe annotation but has %s parameters.Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
                    final MethodIdentifier ident = new MethodIdentifier(method);
                    if (!identifiers.containsKey(ident)) {
                        identifiers.put(ident, method);
                    }
                }
            }
        }
        return ImmutableList.<Method>copyOf((java.util.Collection<? extends Method>)identifiers.values());
    }
    
    @VisibleForTesting
    static ImmutableSet<Class<?>> flattenHierarchy(final Class<?> concreteClass) {
        try {
            return SubscriberRegistry.flattenHierarchyCache.getUnchecked(concreteClass);
        }
        catch (UncheckedExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
    
    static {
        subscriberMethodsCache = CacheBuilder.newBuilder().weakKeys().<Class<?>, ImmutableList<Method>>build(new CacheLoader<Class<?>, ImmutableList<Method>>() {
            @Override
            public ImmutableList<Method> load(final Class<?> concreteClass) throws Exception {
                return getAnnotatedMethodsNotCached(concreteClass);
            }
        });
        flattenHierarchyCache = CacheBuilder.newBuilder().weakKeys().<Class<?>, ImmutableSet<Class<?>>>build(new CacheLoader<Class<?>, ImmutableSet<Class<?>>>() {
            @Override
            public ImmutableSet<Class<?>> load(final Class<?> concreteClass) {
                return ImmutableSet.<Class<?>>copyOf((java.util.Collection<? extends Class<?>>)TypeToken.of(concreteClass).getTypes().rawTypes());
            }
        });
    }
    
    private static final class MethodIdentifier {
        private final String name;
        private final List<Class<?>> parameterTypes;
        
        MethodIdentifier(final Method method) {
            this.name = method.getName();
            this.parameterTypes = (List<Class<?>>)Arrays.asList((Object[])method.getParameterTypes());
        }
        
        public int hashCode() {
            return Objects.hashCode(this.name, this.parameterTypes);
        }
        
        public boolean equals(@Nullable final Object o) {
            if (o instanceof MethodIdentifier) {
                final MethodIdentifier ident = (MethodIdentifier)o;
                return this.name.equals(ident.name) && this.parameterTypes.equals(ident.parameterTypes);
            }
            return false;
        }
    }
}
