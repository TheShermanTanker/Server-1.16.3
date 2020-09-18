package org.apache.logging.log4j.core.config.builder.api;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.core.LoggerContext;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.Configuration;

public interface ConfigurationBuilder<T extends Configuration> extends Builder<T> {
    ConfigurationBuilder<T> add(final ScriptComponentBuilder scriptComponentBuilder);
    
    ConfigurationBuilder<T> add(final ScriptFileComponentBuilder scriptFileComponentBuilder);
    
    ConfigurationBuilder<T> add(final AppenderComponentBuilder appenderComponentBuilder);
    
    ConfigurationBuilder<T> add(final CustomLevelComponentBuilder customLevelComponentBuilder);
    
    ConfigurationBuilder<T> add(final FilterComponentBuilder filterComponentBuilder);
    
    ConfigurationBuilder<T> add(final LoggerComponentBuilder loggerComponentBuilder);
    
    ConfigurationBuilder<T> add(final RootLoggerComponentBuilder rootLoggerComponentBuilder);
    
    ConfigurationBuilder<T> addProperty(final String string1, final String string2);
    
    ScriptComponentBuilder newScript(final String string1, final String string2, final String string3);
    
    ScriptFileComponentBuilder newScriptFile(final String string);
    
    ScriptFileComponentBuilder newScriptFile(final String string1, final String string2);
    
    AppenderComponentBuilder newAppender(final String string1, final String string2);
    
    AppenderRefComponentBuilder newAppenderRef(final String string);
    
    LoggerComponentBuilder newAsyncLogger(final String string, final Level level);
    
    LoggerComponentBuilder newAsyncLogger(final String string, final Level level, final boolean boolean3);
    
    LoggerComponentBuilder newAsyncLogger(final String string1, final String string2);
    
    LoggerComponentBuilder newAsyncLogger(final String string1, final String string2, final boolean boolean3);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final Level level);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final Level level, final boolean boolean2);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final String string);
    
    RootLoggerComponentBuilder newAsyncRootLogger(final String string, final boolean boolean2);
    
     <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String string);
    
     <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String string1, final String string2);
    
     <B extends ComponentBuilder<B>> ComponentBuilder<B> newComponent(final String string1, final String string2, final String string3);
    
    CustomLevelComponentBuilder newCustomLevel(final String string, final int integer);
    
    FilterComponentBuilder newFilter(final String string, final Filter.Result result2, final Filter.Result result3);
    
    FilterComponentBuilder newFilter(final String string1, final String string2, final String string3);
    
    LayoutComponentBuilder newLayout(final String string);
    
    LoggerComponentBuilder newLogger(final String string, final Level level);
    
    LoggerComponentBuilder newLogger(final String string, final Level level, final boolean boolean3);
    
    LoggerComponentBuilder newLogger(final String string1, final String string2);
    
    LoggerComponentBuilder newLogger(final String string1, final String string2, final boolean boolean3);
    
    RootLoggerComponentBuilder newRootLogger(final Level level);
    
    RootLoggerComponentBuilder newRootLogger(final Level level, final boolean boolean2);
    
    RootLoggerComponentBuilder newRootLogger(final String string);
    
    RootLoggerComponentBuilder newRootLogger(final String string, final boolean boolean2);
    
    ConfigurationBuilder<T> setAdvertiser(final String string);
    
    ConfigurationBuilder<T> setConfigurationName(final String string);
    
    ConfigurationBuilder<T> setConfigurationSource(final ConfigurationSource configurationSource);
    
    ConfigurationBuilder<T> setMonitorInterval(final String string);
    
    ConfigurationBuilder<T> setPackages(final String string);
    
    ConfigurationBuilder<T> setShutdownHook(final String string);
    
    ConfigurationBuilder<T> setShutdownTimeout(final long long1, final TimeUnit timeUnit);
    
    ConfigurationBuilder<T> setStatusLevel(final Level level);
    
    ConfigurationBuilder<T> setVerbosity(final String string);
    
    ConfigurationBuilder<T> setDestination(final String string);
    
    void setLoggerContext(final LoggerContext loggerContext);
    
    ConfigurationBuilder<T> addRootProperty(final String string1, final String string2);
    
    T build(final boolean boolean1);
    
    void writeXmlConfiguration(final OutputStream outputStream) throws IOException;
    
    String toXmlConfiguration();
}
