package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.util.NanoClock;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.core.async.AsyncLoggerConfigDelegate;
import org.apache.logging.log4j.core.script.ScriptManager;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import java.util.List;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import java.util.Map;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.filter.Filterable;

public interface Configuration extends Filterable {
    public static final String CONTEXT_PROPERTIES = "ContextProperties";
    
    String getName();
    
    LoggerConfig getLoggerConfig(final String string);
    
     <T extends Appender> T getAppender(final String string);
    
    Map<String, Appender> getAppenders();
    
    void addAppender(final Appender appender);
    
    Map<String, LoggerConfig> getLoggers();
    
    void addLoggerAppender(final Logger logger, final Appender appender);
    
    void addLoggerFilter(final Logger logger, final Filter filter);
    
    void setLoggerAdditive(final Logger logger, final boolean boolean2);
    
    void addLogger(final String string, final LoggerConfig loggerConfig);
    
    void removeLogger(final String string);
    
    List<String> getPluginPackages();
    
    Map<String, String> getProperties();
    
    LoggerConfig getRootLogger();
    
    void addListener(final ConfigurationListener configurationListener);
    
    void removeListener(final ConfigurationListener configurationListener);
    
    StrSubstitutor getStrSubstitutor();
    
    void createConfiguration(final Node node, final LogEvent logEvent);
    
     <T> T getComponent(final String string);
    
    void addComponent(final String string, final Object object);
    
    void setAdvertiser(final Advertiser advertiser);
    
    Advertiser getAdvertiser();
    
    boolean isShutdownHookEnabled();
    
    long getShutdownTimeoutMillis();
    
    ConfigurationScheduler getScheduler();
    
    ConfigurationSource getConfigurationSource();
    
    List<CustomLevelConfig> getCustomLevels();
    
    ScriptManager getScriptManager();
    
    AsyncLoggerConfigDelegate getAsyncLoggerConfigDelegate();
    
    WatchManager getWatchManager();
    
    ReliabilityStrategy getReliabilityStrategy(final LoggerConfig loggerConfig);
    
    NanoClock getNanoClock();
    
    void setNanoClock(final NanoClock nanoClock);
    
    LoggerContext getLoggerContext();
}
