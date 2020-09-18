package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "loggers", category = "Core")
public final class LoggersPlugin {
    private LoggersPlugin() {
    }
    
    @PluginFactory
    public static Loggers createLoggers(@PluginElement("Loggers") final LoggerConfig[] loggers) {
        final ConcurrentMap<String, LoggerConfig> loggerMap = (ConcurrentMap<String, LoggerConfig>)new ConcurrentHashMap();
        LoggerConfig root = null;
        for (final LoggerConfig logger : loggers) {
            if (logger != null) {
                if (logger.getName().isEmpty()) {
                    root = logger;
                }
                loggerMap.put(logger.getName(), logger);
            }
        }
        return new Loggers(loggerMap, root);
    }
}
