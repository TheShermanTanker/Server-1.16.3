package org.apache.logging.log4j.spi;

import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.Objects;
import java.util.Map;

public class LoggerRegistry<T extends ExtendedLogger> {
    private static final String DEFAULT_FACTORY_KEY;
    private final MapFactory<T> factory;
    private final Map<String, Map<String, T>> map;
    
    public LoggerRegistry() {
        this((MapFactory)new ConcurrentMapFactory());
    }
    
    public LoggerRegistry(final MapFactory<T> factory) {
        this.factory = (MapFactory<T>)Objects.requireNonNull(factory, "factory");
        this.map = factory.createOuterMap();
    }
    
    private static String factoryClassKey(final Class<? extends MessageFactory> messageFactoryClass) {
        return (messageFactoryClass == null) ? LoggerRegistry.DEFAULT_FACTORY_KEY : messageFactoryClass.getName();
    }
    
    private static String factoryKey(final MessageFactory messageFactory) {
        return (messageFactory == null) ? LoggerRegistry.DEFAULT_FACTORY_KEY : messageFactory.getClass().getName();
    }
    
    public T getLogger(final String name) {
        return (T)this.getOrCreateInnerMap(LoggerRegistry.DEFAULT_FACTORY_KEY).get(name);
    }
    
    public T getLogger(final String name, final MessageFactory messageFactory) {
        return (T)this.getOrCreateInnerMap(factoryKey(messageFactory)).get(name);
    }
    
    public Collection<T> getLoggers() {
        return this.getLoggers((Collection<T>)new ArrayList());
    }
    
    public Collection<T> getLoggers(final Collection<T> destination) {
        for (final Map<String, T> inner : this.map.values()) {
            destination.addAll(inner.values());
        }
        return destination;
    }
    
    private Map<String, T> getOrCreateInnerMap(final String factoryName) {
        Map<String, T> inner = (Map<String, T>)this.map.get(factoryName);
        if (inner == null) {
            inner = this.factory.createInnerMap();
            this.map.put(factoryName, inner);
        }
        return inner;
    }
    
    public boolean hasLogger(final String name) {
        return this.getOrCreateInnerMap(LoggerRegistry.DEFAULT_FACTORY_KEY).containsKey(name);
    }
    
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return this.getOrCreateInnerMap(factoryKey(messageFactory)).containsKey(name);
    }
    
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return this.getOrCreateInnerMap(factoryClassKey(messageFactoryClass)).containsKey(name);
    }
    
    public void putIfAbsent(final String name, final MessageFactory messageFactory, final T logger) {
        this.factory.putIfAbsent(this.getOrCreateInnerMap(factoryKey(messageFactory)), name, logger);
    }
    
    static {
        DEFAULT_FACTORY_KEY = AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName();
    }
    
    public static class ConcurrentMapFactory<T extends ExtendedLogger> implements MapFactory<T> {
        public Map<String, T> createInnerMap() {
            return (Map<String, T>)new ConcurrentHashMap();
        }
        
        public Map<String, Map<String, T>> createOuterMap() {
            return (Map<String, Map<String, T>>)new ConcurrentHashMap();
        }
        
        public void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger) {
            ((ConcurrentMap)innerMap).putIfAbsent(name, logger);
        }
    }
    
    public static class WeakMapFactory<T extends ExtendedLogger> implements MapFactory<T> {
        public Map<String, T> createInnerMap() {
            return (Map<String, T>)new WeakHashMap();
        }
        
        public Map<String, Map<String, T>> createOuterMap() {
            return (Map<String, Map<String, T>>)new WeakHashMap();
        }
        
        public void putIfAbsent(final Map<String, T> innerMap, final String name, final T logger) {
            innerMap.put(name, logger);
        }
    }
    
    public interface MapFactory<T extends ExtendedLogger> {
        Map<String, T> createInnerMap();
        
        Map<String, Map<String, T>> createOuterMap();
        
        void putIfAbsent(final Map<String, T> map, final String string, final T extendedLogger);
    }
}
