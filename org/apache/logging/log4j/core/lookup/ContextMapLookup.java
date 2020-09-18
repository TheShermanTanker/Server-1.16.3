package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.core.impl.ContextDataInjectorFactory;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ctx", category = "Lookup")
public class ContextMapLookup implements StrLookup {
    private final ContextDataInjector injector;
    
    public ContextMapLookup() {
        this.injector = ContextDataInjectorFactory.createInjector();
    }
    
    public String lookup(final String key) {
        return this.currentContextData().<String>getValue(key);
    }
    
    private ReadOnlyStringMap currentContextData() {
        return this.injector.rawContextData();
    }
    
    public String lookup(final LogEvent event, final String key) {
        return event.getContextData().<String>getValue(key);
    }
}
