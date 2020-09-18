package org.apache.logging.log4j.core.selector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.ReflectionUtil;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import java.net.URI;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.atomic.AtomicReference;

public class ClassLoaderContextSelector implements ContextSelector {
    private static final AtomicReference<LoggerContext> DEFAULT_CONTEXT;
    protected static final StatusLogger LOGGER;
    protected static final ConcurrentMap<String, AtomicReference<WeakReference<LoggerContext>>> CONTEXT_MAP;
    
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }
    
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        if (currentContext) {
            final LoggerContext ctx = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
            if (ctx != null) {
                return ctx;
            }
            return this.getDefault();
        }
        else {
            if (loader != null) {
                return this.locateContext(loader, configLocation);
            }
            final Class<?> clazz = ReflectionUtil.getCallerClass(fqcn);
            if (clazz != null) {
                return this.locateContext(clazz.getClassLoader(), configLocation);
            }
            final LoggerContext lc = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
            if (lc != null) {
                return lc;
            }
            return this.getDefault();
        }
    }
    
    public void removeContext(final LoggerContext context) {
        for (final Map.Entry<String, AtomicReference<WeakReference<LoggerContext>>> entry : ClassLoaderContextSelector.CONTEXT_MAP.entrySet()) {
            final LoggerContext ctx = (LoggerContext)((WeakReference)((AtomicReference)entry.getValue()).get()).get();
            if (ctx == context) {
                ClassLoaderContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }
    }
    
    public List<LoggerContext> getLoggerContexts() {
        final List<LoggerContext> list = (List<LoggerContext>)new ArrayList();
        final Collection<AtomicReference<WeakReference<LoggerContext>>> coll = (Collection<AtomicReference<WeakReference<LoggerContext>>>)ClassLoaderContextSelector.CONTEXT_MAP.values();
        for (final AtomicReference<WeakReference<LoggerContext>> ref : coll) {
            final LoggerContext ctx = (LoggerContext)((WeakReference)ref.get()).get();
            if (ctx != null) {
                list.add(ctx);
            }
        }
        return (List<LoggerContext>)Collections.unmodifiableList((List)list);
    }
    
    private LoggerContext locateContext(final ClassLoader loaderOrNull, final URI configLocation) {
        final ClassLoader loader = (loaderOrNull != null) ? loaderOrNull : ClassLoader.getSystemClassLoader();
        final String name = this.toContextMapKey(loader);
        AtomicReference<WeakReference<LoggerContext>> ref = (AtomicReference<WeakReference<LoggerContext>>)ClassLoaderContextSelector.CONTEXT_MAP.get(name);
        if (ref == null) {
            if (configLocation == null) {
                for (ClassLoader parent = loader.getParent(); parent != null; parent = parent.getParent()) {
                    ref = (AtomicReference<WeakReference<LoggerContext>>)ClassLoaderContextSelector.CONTEXT_MAP.get(this.toContextMapKey(parent));
                    if (ref != null) {
                        final WeakReference<LoggerContext> r = (WeakReference<LoggerContext>)ref.get();
                        final LoggerContext ctx = (LoggerContext)r.get();
                        if (ctx != null) {
                            return ctx;
                        }
                    }
                }
            }
            LoggerContext ctx2 = this.createContext(name, configLocation);
            final AtomicReference<WeakReference<LoggerContext>> r2 = (AtomicReference<WeakReference<LoggerContext>>)new AtomicReference();
            r2.set(new WeakReference((Object)ctx2));
            ClassLoaderContextSelector.CONTEXT_MAP.putIfAbsent(name, r2);
            ctx2 = (LoggerContext)((WeakReference)((AtomicReference)ClassLoaderContextSelector.CONTEXT_MAP.get(name)).get()).get();
            return ctx2;
        }
        final WeakReference<LoggerContext> weakRef = (WeakReference<LoggerContext>)ref.get();
        LoggerContext ctx3 = (LoggerContext)weakRef.get();
        if (ctx3 != null) {
            if (ctx3.getConfigLocation() == null && configLocation != null) {
                ClassLoaderContextSelector.LOGGER.debug("Setting configuration to {}", configLocation);
                ctx3.setConfigLocation(configLocation);
            }
            else if (ctx3.getConfigLocation() != null && configLocation != null && !ctx3.getConfigLocation().equals(configLocation)) {
                ClassLoaderContextSelector.LOGGER.warn("locateContext called with URI {}. Existing LoggerContext has URI {}", configLocation, ctx3.getConfigLocation());
            }
            return ctx3;
        }
        ctx3 = this.createContext(name, configLocation);
        ref.compareAndSet(weakRef, new WeakReference((Object)ctx3));
        return ctx3;
    }
    
    protected LoggerContext createContext(final String name, final URI configLocation) {
        return new LoggerContext(name, null, configLocation);
    }
    
    protected String toContextMapKey(final ClassLoader loader) {
        return Integer.toHexString(System.identityHashCode(loader));
    }
    
    protected LoggerContext getDefault() {
        final LoggerContext ctx = (LoggerContext)ClassLoaderContextSelector.DEFAULT_CONTEXT.get();
        if (ctx != null) {
            return ctx;
        }
        ClassLoaderContextSelector.DEFAULT_CONTEXT.compareAndSet(null, this.createContext(this.defaultContextName(), null));
        return (LoggerContext)ClassLoaderContextSelector.DEFAULT_CONTEXT.get();
    }
    
    protected String defaultContextName() {
        return "Default";
    }
    
    static {
        DEFAULT_CONTEXT = new AtomicReference();
        LOGGER = StatusLogger.getLogger();
        CONTEXT_MAP = (ConcurrentMap)new ConcurrentHashMap();
    }
}
