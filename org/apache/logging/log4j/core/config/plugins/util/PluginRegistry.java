package org.apache.logging.log4j.core.config.plugins.util;

import java.net.URI;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import java.util.Collections;
import org.apache.logging.log4j.util.Strings;
import java.util.Iterator;
import java.net.URL;
import java.util.Enumeration;
import java.text.DecimalFormat;
import org.apache.logging.log4j.core.config.plugins.processor.PluginEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import org.apache.logging.log4j.core.config.plugins.processor.PluginCache;
import org.apache.logging.log4j.core.util.Loader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.logging.log4j.Logger;

public class PluginRegistry {
    private static final Logger LOGGER;
    private static volatile PluginRegistry INSTANCE;
    private static final Object INSTANCE_LOCK;
    private final AtomicReference<Map<String, List<PluginType<?>>>> pluginsByCategoryRef;
    private final ConcurrentMap<Long, Map<String, List<PluginType<?>>>> pluginsByCategoryByBundleId;
    private final ConcurrentMap<String, Map<String, List<PluginType<?>>>> pluginsByCategoryByPackage;
    
    private PluginRegistry() {
        this.pluginsByCategoryRef = (AtomicReference<Map<String, List<PluginType<?>>>>)new AtomicReference();
        this.pluginsByCategoryByBundleId = (ConcurrentMap<Long, Map<String, List<PluginType<?>>>>)new ConcurrentHashMap();
        this.pluginsByCategoryByPackage = (ConcurrentMap<String, Map<String, List<PluginType<?>>>>)new ConcurrentHashMap();
    }
    
    public static PluginRegistry getInstance() {
        PluginRegistry result = PluginRegistry.INSTANCE;
        if (result == null) {
            synchronized (PluginRegistry.INSTANCE_LOCK) {
                result = PluginRegistry.INSTANCE;
                if (result == null) {
                    result = (PluginRegistry.INSTANCE = new PluginRegistry());
                }
            }
        }
        return result;
    }
    
    public void clear() {
        this.pluginsByCategoryRef.set(null);
        this.pluginsByCategoryByPackage.clear();
        this.pluginsByCategoryByBundleId.clear();
    }
    
    public Map<Long, Map<String, List<PluginType<?>>>> getPluginsByCategoryByBundleId() {
        return (Map<Long, Map<String, List<PluginType<?>>>>)this.pluginsByCategoryByBundleId;
    }
    
    public Map<String, List<PluginType<?>>> loadFromMainClassLoader() {
        final Map<String, List<PluginType<?>>> existing = (Map<String, List<PluginType<?>>>)this.pluginsByCategoryRef.get();
        if (existing != null) {
            return existing;
        }
        final Map<String, List<PluginType<?>>> newPluginsByCategory = this.decodeCacheFiles(Loader.getClassLoader());
        if (this.pluginsByCategoryRef.compareAndSet(null, newPluginsByCategory)) {
            return newPluginsByCategory;
        }
        return (Map<String, List<PluginType<?>>>)this.pluginsByCategoryRef.get();
    }
    
    public void clearBundlePlugins(final long bundleId) {
        this.pluginsByCategoryByBundleId.remove(bundleId);
    }
    
    public Map<String, List<PluginType<?>>> loadFromBundle(final long bundleId, final ClassLoader loader) {
        Map<String, List<PluginType<?>>> existing = (Map<String, List<PluginType<?>>>)this.pluginsByCategoryByBundleId.get(bundleId);
        if (existing != null) {
            return existing;
        }
        final Map<String, List<PluginType<?>>> newPluginsByCategory = this.decodeCacheFiles(loader);
        existing = (Map<String, List<PluginType<?>>>)this.pluginsByCategoryByBundleId.putIfAbsent(bundleId, newPluginsByCategory);
        if (existing != null) {
            return existing;
        }
        return newPluginsByCategory;
    }
    
    private Map<String, List<PluginType<?>>> decodeCacheFiles(final ClassLoader loader) {
        final long startTime = System.nanoTime();
        final PluginCache cache = new PluginCache();
        try {
            final Enumeration<URL> resources = (Enumeration<URL>)loader.getResources("META-INF/org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
            if (resources == null) {
                PluginRegistry.LOGGER.info("Plugin preloads not available from class loader {}", loader);
            }
            else {
                cache.loadCacheFiles(resources);
            }
        }
        catch (IOException ioe) {
            PluginRegistry.LOGGER.warn("Unable to preload plugins", (Throwable)ioe);
        }
        final Map<String, List<PluginType<?>>> newPluginsByCategory = (Map<String, List<PluginType<?>>>)new HashMap();
        int pluginCount = 0;
        for (final Map.Entry<String, Map<String, PluginEntry>> outer : cache.getAllCategories().entrySet()) {
            final String categoryLowerCase = (String)outer.getKey();
            final List<PluginType<?>> types = (List<PluginType<?>>)new ArrayList(((Map)outer.getValue()).size());
            newPluginsByCategory.put(categoryLowerCase, types);
            for (final Map.Entry<String, PluginEntry> inner : ((Map)outer.getValue()).entrySet()) {
                final PluginEntry entry = (PluginEntry)inner.getValue();
                final String className = entry.getClassName();
                try {
                    final Class<?> clazz = loader.loadClass(className);
                    final PluginType<?> type = new PluginType<>(entry, clazz, entry.getName());
                    types.add(type);
                    ++pluginCount;
                }
                catch (ClassNotFoundException e) {
                    PluginRegistry.LOGGER.info("Plugin [{}] could not be loaded due to missing classes.", className, e);
                }
                catch (VerifyError e2) {
                    PluginRegistry.LOGGER.info("Plugin [{}] could not be loaded due to verification error.", className, e2);
                }
            }
        }
        final long endTime = System.nanoTime();
        final DecimalFormat numFormat = new DecimalFormat("#0.000000");
        final double seconds = (endTime - startTime) * 1.0E-9;
        PluginRegistry.LOGGER.debug("Took {} seconds to load {} plugins from {}", numFormat.format(seconds), pluginCount, loader);
        return newPluginsByCategory;
    }
    
    public Map<String, List<PluginType<?>>> loadFromPackage(final String pkg) {
        if (Strings.isBlank(pkg)) {
            return (Map<String, List<PluginType<?>>>)Collections.emptyMap();
        }
        Map<String, List<PluginType<?>>> existing = (Map<String, List<PluginType<?>>>)this.pluginsByCategoryByPackage.get(pkg);
        if (existing != null) {
            return existing;
        }
        final long startTime = System.nanoTime();
        final ResolverUtil resolver = new ResolverUtil();
        final ClassLoader classLoader = Loader.getClassLoader();
        if (classLoader != null) {
            resolver.setClassLoader(classLoader);
        }
        resolver.findInPackage(new PluginTest(), pkg);
        final Map<String, List<PluginType<?>>> newPluginsByCategory = (Map<String, List<PluginType<?>>>)new HashMap();
        for (final Class<?> clazz : resolver.getClasses()) {
            final Plugin plugin = (Plugin)clazz.getAnnotation((Class)Plugin.class);
            final String categoryLowerCase = plugin.category().toLowerCase();
            List<PluginType<?>> list = (List<PluginType<?>>)newPluginsByCategory.get(categoryLowerCase);
            if (list == null) {
                newPluginsByCategory.put(categoryLowerCase, (list = (List<PluginType<?>>)new ArrayList()));
            }
            final PluginEntry mainEntry = new PluginEntry();
            final String mainElementName = plugin.elementType().equals("") ? plugin.name() : plugin.elementType();
            mainEntry.setKey(plugin.name().toLowerCase());
            mainEntry.setName(plugin.name());
            mainEntry.setCategory(plugin.category());
            mainEntry.setClassName(clazz.getName());
            mainEntry.setPrintable(plugin.printObject());
            mainEntry.setDefer(plugin.deferChildren());
            final PluginType<?> mainType = new PluginType<>(mainEntry, clazz, mainElementName);
            list.add(mainType);
            final PluginAliases pluginAliases = (PluginAliases)clazz.getAnnotation((Class)PluginAliases.class);
            if (pluginAliases != null) {
                for (final String alias : pluginAliases.value()) {
                    final PluginEntry aliasEntry = new PluginEntry();
                    final String aliasElementName = plugin.elementType().equals("") ? alias.trim() : plugin.elementType();
                    aliasEntry.setKey(alias.trim().toLowerCase());
                    aliasEntry.setName(plugin.name());
                    aliasEntry.setCategory(plugin.category());
                    aliasEntry.setClassName(clazz.getName());
                    aliasEntry.setPrintable(plugin.printObject());
                    aliasEntry.setDefer(plugin.deferChildren());
                    final PluginType<?> aliasType = new PluginType<>(aliasEntry, clazz, aliasElementName);
                    list.add(aliasType);
                }
            }
        }
        final long endTime = System.nanoTime();
        final DecimalFormat numFormat = new DecimalFormat("#0.000000");
        final double seconds = (endTime - startTime) * 1.0E-9;
        PluginRegistry.LOGGER.debug("Took {} seconds to load {} plugins from package {}", numFormat.format(seconds), resolver.getClasses().size(), pkg);
        existing = (Map<String, List<PluginType<?>>>)this.pluginsByCategoryByPackage.putIfAbsent(pkg, newPluginsByCategory);
        if (existing != null) {
            return existing;
        }
        return newPluginsByCategory;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        INSTANCE_LOCK = new Object();
    }
    
    public static class PluginTest implements ResolverUtil.Test {
        public boolean matches(final Class<?> type) {
            return type != null && type.isAnnotationPresent((Class)Plugin.class);
        }
        
        public String toString() {
            return "annotated with @" + Plugin.class.getSimpleName();
        }
        
        public boolean matches(final URI resource) {
            throw new UnsupportedOperationException();
        }
        
        public boolean doesMatchClass() {
            return true;
        }
        
        public boolean doesMatchResource() {
            return false;
        }
    }
}
