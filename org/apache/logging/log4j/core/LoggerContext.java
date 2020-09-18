package org.apache.logging.log4j.core;

import org.apache.logging.log4j.core.config.NullConfiguration;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.core.util.ExecutorServices;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.util.Iterator;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import java.beans.PropertyChangeEvent;
import org.apache.logging.log4j.core.util.NetUtils;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.spi.ExtendedLogger;
import org.apache.logging.log4j.spi.AbstractLogger;
import java.util.Collection;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.Objects;
import org.apache.logging.log4j.core.jmx.Server;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import java.util.concurrent.locks.Lock;
import org.apache.logging.log4j.core.util.Cancellable;
import java.net.URI;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.spi.LoggerRegistry;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationListener;
import org.apache.logging.log4j.spi.Terminable;

public class LoggerContext extends AbstractLifeCycle implements org.apache.logging.log4j.spi.LoggerContext, AutoCloseable, Terminable, ConfigurationListener {
    public static final String PROPERTY_CONFIG = "config";
    private static final Configuration NULL_CONFIGURATION;
    private final LoggerRegistry<Logger> loggerRegistry;
    private final CopyOnWriteArrayList<PropertyChangeListener> propertyChangeListeners;
    private volatile Configuration configuration;
    private Object externalContext;
    private String contextName;
    private volatile URI configLocation;
    private Cancellable shutdownCallback;
    private final Lock configLock;
    
    public LoggerContext(final String name) {
        this(name, null, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext) {
        this(name, externalContext, (URI)null);
    }
    
    public LoggerContext(final String name, final Object externalContext, final URI configLocn) {
        this.loggerRegistry = new LoggerRegistry<Logger>();
        this.propertyChangeListeners = (CopyOnWriteArrayList<PropertyChangeListener>)new CopyOnWriteArrayList();
        this.configuration = new DefaultConfiguration();
        this.configLock = (Lock)new ReentrantLock();
        this.contextName = name;
        this.externalContext = externalContext;
        this.configLocation = configLocn;
    }
    
    public LoggerContext(final String name, final Object externalContext, final String configLocn) {
        this.loggerRegistry = new LoggerRegistry<Logger>();
        this.propertyChangeListeners = (CopyOnWriteArrayList<PropertyChangeListener>)new CopyOnWriteArrayList();
        this.configuration = new DefaultConfiguration();
        this.configLock = (Lock)new ReentrantLock();
        this.contextName = name;
        this.externalContext = externalContext;
        if (configLocn != null) {
            URI uri;
            try {
                uri = new File(configLocn).toURI();
            }
            catch (Exception ex) {
                uri = null;
            }
            this.configLocation = uri;
        }
        else {
            this.configLocation = null;
        }
    }
    
    public static LoggerContext getContext() {
        return (LoggerContext)LogManager.getContext();
    }
    
    public static LoggerContext getContext(final boolean currentContext) {
        return (LoggerContext)LogManager.getContext(currentContext);
    }
    
    public static LoggerContext getContext(final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        return (LoggerContext)LogManager.getContext(loader, currentContext, configLocation);
    }
    
    @Override
    public void start() {
        LoggerContext.LOGGER.debug("Starting LoggerContext[name={}, {}]...", this.getName(), this);
        if (PropertiesUtil.getProperties().getBooleanProperty("log4j.LoggerContext.stacktrace.on.start", false)) {
            LoggerContext.LOGGER.debug("Stack trace to locate invoker", (Throwable)new Exception("Not a real error, showing stack trace to locate invoker"));
        }
        if (this.configLock.tryLock()) {
            try {
                if (this.isInitialized() || this.isStopped()) {
                    this.setStarting();
                    this.reconfigure();
                    if (this.configuration.isShutdownHookEnabled()) {
                        this.setUpShutdownHook();
                    }
                    this.setStarted();
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        LoggerContext.LOGGER.debug("LoggerContext[name={}, {}] started OK.", this.getName(), this);
    }
    
    public void start(final Configuration config) {
        LoggerContext.LOGGER.debug("Starting LoggerContext[name={}, {}] with configuration {}...", this.getName(), this, config);
        if (this.configLock.tryLock()) {
            try {
                if (this.isInitialized() || this.isStopped()) {
                    if (this.configuration.isShutdownHookEnabled()) {
                        this.setUpShutdownHook();
                    }
                    this.setStarted();
                }
            }
            finally {
                this.configLock.unlock();
            }
        }
        this.setConfiguration(config);
        LoggerContext.LOGGER.debug("LoggerContext[name={}, {}] started OK with configuration {}.", this.getName(), this, config);
    }
    
    private void setUpShutdownHook() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/apache/logging/log4j/core/LoggerContext.shutdownCallback:Lorg/apache/logging/log4j/core/util/Cancellable;
        //     4: ifnonnull       93
        //     7: invokestatic    org/apache/logging/log4j/LogManager.getFactory:()Lorg/apache/logging/log4j/spi/LoggerContextFactory;
        //    10: astore_1        /* factory */
        //    11: aload_1         /* factory */
        //    12: instanceof      Lorg/apache/logging/log4j/core/util/ShutdownCallbackRegistry;
        //    15: ifeq            93
        //    18: getstatic       org/apache/logging/log4j/core/LoggerContext.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    21: getstatic       org/apache/logging/log4j/core/util/ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER:Lorg/apache/logging/log4j/Marker;
        //    24: ldc             "Shutdown hook enabled. Registering a new one."
        //    26: invokeinterface org/apache/logging/log4j/Logger.debug:(Lorg/apache/logging/log4j/Marker;Ljava/lang/String;)V
        //    31: aload_0         /* this */
        //    32: getfield        org/apache/logging/log4j/core/LoggerContext.configuration:Lorg/apache/logging/log4j/core/config/Configuration;
        //    35: invokeinterface org/apache/logging/log4j/core/config/Configuration.getShutdownTimeoutMillis:()J
        //    40: lstore_2        /* shutdownTimeoutMillis */
        //    41: aload_0         /* this */
        //    42: aload_1         /* factory */
        //    43: checkcast       Lorg/apache/logging/log4j/core/util/ShutdownCallbackRegistry;
        //    46: new             Lorg/apache/logging/log4j/core/LoggerContext$1;
        //    49: dup            
        //    50: aload_0         /* this */
        //    51: lload_2         /* shutdownTimeoutMillis */
        //    52: invokespecial   org/apache/logging/log4j/core/LoggerContext$1.<init>:(Lorg/apache/logging/log4j/core/LoggerContext;J)V
        //    55: invokeinterface org/apache/logging/log4j/core/util/ShutdownCallbackRegistry.addShutdownCallback:(Ljava/lang/Runnable;)Lorg/apache/logging/log4j/core/util/Cancellable;
        //    60: putfield        org/apache/logging/log4j/core/LoggerContext.shutdownCallback:Lorg/apache/logging/log4j/core/util/Cancellable;
        //    63: goto            93
        //    66: astore_2        /* e */
        //    67: new             Ljava/lang/IllegalStateException;
        //    70: dup            
        //    71: ldc             "Unable to register Log4j shutdown hook because JVM is shutting down."
        //    73: aload_2         /* e */
        //    74: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //    77: athrow         
        //    78: astore_2        /* e */
        //    79: getstatic       org/apache/logging/log4j/core/LoggerContext.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    82: getstatic       org/apache/logging/log4j/core/util/ShutdownCallbackRegistry.SHUTDOWN_HOOK_MARKER:Lorg/apache/logging/log4j/Marker;
        //    85: ldc             "Unable to register shutdown hook due to security restrictions"
        //    87: aload_2         /* e */
        //    88: invokeinterface org/apache/logging/log4j/Logger.error:(Lorg/apache/logging/log4j/Marker;Ljava/lang/String;Ljava/lang/Throwable;)V
        //    93: return         
        //    StackMapTable: 00 03 FF 00 42 00 02 07 00 02 07 00 EA 00 01 07 00 CA 4B 07 00 CC FA 00 0E
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  31     63     66     78     Ljava/lang/IllegalStateException;
        //  31     63     78     93     Ljava/lang/SecurityException;
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2698)
        //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2691)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public void close() {
        this.stop();
    }
    
    public void terminate() {
        this.stop();
    }
    
    @Override
    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        LoggerContext.LOGGER.debug("Stopping LoggerContext[name={}, {}]...", this.getName(), this);
        this.configLock.lock();
        try {
            if (this.isStopped()) {
                return true;
            }
            this.setStopping();
            try {
                Server.unregisterLoggerContext(this.getName());
            }
            catch (LinkageError | Exception linkageError) {
                final Throwable t;
                final Throwable e = t;
                LoggerContext.LOGGER.error("Unable to unregister MBeans", e);
            }
            if (this.shutdownCallback != null) {
                this.shutdownCallback.cancel();
                this.shutdownCallback = null;
            }
            final Configuration prev = this.configuration;
            this.configuration = LoggerContext.NULL_CONFIGURATION;
            this.updateLoggers();
            if (prev instanceof LifeCycle2) {
                ((LifeCycle2)prev).stop(timeout, timeUnit);
            }
            else {
                prev.stop();
            }
            this.externalContext = null;
            LogManager.getFactory().removeContext(this);
        }
        finally {
            this.configLock.unlock();
            this.setStopped();
        }
        LoggerContext.LOGGER.debug("Stopped LoggerContext[name={}, {}] with status {}", this.getName(), this, true);
        return true;
    }
    
    public String getName() {
        return this.contextName;
    }
    
    public Logger getRootLogger() {
        return this.getLogger("");
    }
    
    public void setName(final String name) {
        this.contextName = (String)Objects.requireNonNull(name);
    }
    
    public void setExternalContext(final Object context) {
        this.externalContext = context;
    }
    
    @Override
    public Object getExternalContext() {
        return this.externalContext;
    }
    
    @Override
    public Logger getLogger(final String name) {
        return this.getLogger(name, null);
    }
    
    public Collection<Logger> getLoggers() {
        return this.loggerRegistry.getLoggers();
    }
    
    @Override
    public Logger getLogger(final String name, final MessageFactory messageFactory) {
        Logger logger = this.loggerRegistry.getLogger(name, messageFactory);
        if (logger != null) {
            AbstractLogger.checkMessageFactory(logger, messageFactory);
            return logger;
        }
        logger = this.newInstance(this, name, messageFactory);
        this.loggerRegistry.putIfAbsent(name, messageFactory, logger);
        return this.loggerRegistry.getLogger(name, messageFactory);
    }
    
    @Override
    public boolean hasLogger(final String name) {
        return this.loggerRegistry.hasLogger(name);
    }
    
    @Override
    public boolean hasLogger(final String name, final MessageFactory messageFactory) {
        return this.loggerRegistry.hasLogger(name, messageFactory);
    }
    
    @Override
    public boolean hasLogger(final String name, final Class<? extends MessageFactory> messageFactoryClass) {
        return this.loggerRegistry.hasLogger(name, messageFactoryClass);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public void addFilter(final Filter filter) {
        this.configuration.addFilter(filter);
    }
    
    public void removeFilter(final Filter filter) {
        this.configuration.removeFilter(filter);
    }
    
    private Configuration setConfiguration(final Configuration config) {
        if (config == null) {
            LoggerContext.LOGGER.error("No configuration found for context '{}'.", this.contextName);
            return this.configuration;
        }
        this.configLock.lock();
        try {
            final Configuration prev = this.configuration;
            config.addListener(this);
            final ConcurrentMap<String, String> map = config.getComponent("ContextProperties");
            try {
                map.putIfAbsent("hostName", NetUtils.getLocalHostname());
            }
            catch (Exception ex) {
                LoggerContext.LOGGER.debug("Ignoring {}, setting hostName to 'unknown'", ex.toString());
                map.putIfAbsent("hostName", "unknown");
            }
            map.putIfAbsent("contextName", this.contextName);
            config.start();
            this.configuration = config;
            this.updateLoggers();
            if (prev != null) {
                prev.removeListener(this);
                prev.stop();
            }
            this.firePropertyChangeEvent(new PropertyChangeEvent(this, "config", prev, config));
            try {
                Server.reregisterMBeansAfterReconfigure();
            }
            catch (LinkageError | Exception linkageError) {
                final Throwable t;
                final Throwable e = t;
                LoggerContext.LOGGER.error("Could not reconfigure JMX", e);
            }
            Log4jLogEvent.setNanoClock(this.configuration.getNanoClock());
            return prev;
        }
        finally {
            this.configLock.unlock();
        }
    }
    
    private void firePropertyChangeEvent(final PropertyChangeEvent event) {
        for (final PropertyChangeListener listener : this.propertyChangeListeners) {
            listener.propertyChange(event);
        }
    }
    
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.add(Objects.requireNonNull(listener, "listener"));
    }
    
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.propertyChangeListeners.remove(listener);
    }
    
    public URI getConfigLocation() {
        return this.configLocation;
    }
    
    public void setConfigLocation(final URI configLocation) {
        this.reconfigure(this.configLocation = configLocation);
    }
    
    private void reconfigure(final URI configURI) {
        final ClassLoader cl = ClassLoader.class.isInstance(this.externalContext) ? ((ClassLoader)this.externalContext) : null;
        LoggerContext.LOGGER.debug("Reconfiguration started for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, configURI, this, cl);
        final Configuration instance = ConfigurationFactory.getInstance().getConfiguration(this, this.contextName, configURI, cl);
        if (instance == null) {
            LoggerContext.LOGGER.error("Reconfiguration failed: No configuration found for '{}' at '{}' in '{}'", this.contextName, configURI, cl);
        }
        else {
            this.setConfiguration(instance);
            final String location = (this.configuration == null) ? "?" : String.valueOf(this.configuration.getConfigurationSource());
            LoggerContext.LOGGER.debug("Reconfiguration complete for context[name={}] at URI {} ({}) with optional ClassLoader: {}", this.contextName, location, this, cl);
        }
    }
    
    public void reconfigure() {
        this.reconfigure(this.configLocation);
    }
    
    public void updateLoggers() {
        this.updateLoggers(this.configuration);
    }
    
    public void updateLoggers(final Configuration config) {
        final Configuration old = this.configuration;
        for (final Logger logger : this.loggerRegistry.getLoggers()) {
            logger.updateConfiguration(config);
        }
        this.firePropertyChangeEvent(new PropertyChangeEvent(this, "config", old, config));
    }
    
    public synchronized void onChange(final Reconfigurable reconfigurable) {
        LoggerContext.LOGGER.debug("Reconfiguration started for context {} ({})", this.contextName, this);
        final Configuration newConfig = reconfigurable.reconfigure();
        if (newConfig != null) {
            this.setConfiguration(newConfig);
            LoggerContext.LOGGER.debug("Reconfiguration completed for {} ({})", this.contextName, this);
        }
        else {
            LoggerContext.LOGGER.debug("Reconfiguration failed for {} ({})", this.contextName, this);
        }
    }
    
    protected Logger newInstance(final LoggerContext ctx, final String name, final MessageFactory messageFactory) {
        return new Logger(ctx, name, messageFactory);
    }
    
    static {
        try {
            LoaderUtil.loadClass(ExecutorServices.class.getName());
        }
        catch (Exception e) {
            LoggerContext.LOGGER.error("Failed to preload ExecutorServices class.", (Throwable)e);
        }
        NULL_CONFIGURATION = new NullConfiguration();
    }
}
