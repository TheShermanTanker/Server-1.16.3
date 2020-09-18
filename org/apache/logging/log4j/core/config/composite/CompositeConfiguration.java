package org.apache.logging.log4j.core.config.composite;

import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.util.FileWatcher;
import org.apache.logging.log4j.core.util.WatchManager;
import org.apache.logging.log4j.Level;
import java.io.File;
import org.apache.logging.log4j.core.config.ConfiguratonFileWatcher;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import org.apache.logging.log4j.core.util.Patterns;
import java.util.Map;
import org.apache.logging.log4j.core.config.status.StatusConfiguration;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.util.List;
import org.apache.logging.log4j.core.config.Reconfigurable;
import org.apache.logging.log4j.core.config.AbstractConfiguration;

public class CompositeConfiguration extends AbstractConfiguration implements Reconfigurable {
    public static final String MERGE_STRATEGY_PROPERTY = "log4j.mergeStrategy";
    private static final String[] VERBOSE_CLASSES;
    private final List<? extends AbstractConfiguration> configurations;
    private MergeStrategy mergeStrategy;
    
    public CompositeConfiguration(final List<? extends AbstractConfiguration> configurations) {
        super(((AbstractConfiguration)configurations.get(0)).getLoggerContext(), ConfigurationSource.NULL_SOURCE);
        this.rootNode = ((AbstractConfiguration)configurations.get(0)).getRootNode();
        this.configurations = configurations;
        final String mergeStrategyClassName = PropertiesUtil.getProperties().getStringProperty("log4j.mergeStrategy", DefaultMergeStrategy.class.getName());
        try {
            this.mergeStrategy = LoaderUtil.<MergeStrategy>newInstanceOf(mergeStrategyClassName);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException ex3) {
            final ReflectiveOperationException ex2;
            final ReflectiveOperationException ex = ex2;
            this.mergeStrategy = new DefaultMergeStrategy();
        }
        for (final AbstractConfiguration config : configurations) {
            this.mergeStrategy.mergeRootProperties(this.rootNode, config);
        }
        final StatusConfiguration statusConfig = new StatusConfiguration().withVerboseClasses(CompositeConfiguration.VERBOSE_CLASSES).withStatus(this.getDefaultStatus());
        for (final Map.Entry<String, String> entry : this.rootNode.getAttributes().entrySet()) {
            final String key = (String)entry.getKey();
            final String value = this.getStrSubstitutor().replace((String)entry.getValue());
            if ("status".equalsIgnoreCase(key)) {
                statusConfig.withStatus(value.toUpperCase());
            }
            else if ("dest".equalsIgnoreCase(key)) {
                statusConfig.withDestination(value);
            }
            else if ("shutdownHook".equalsIgnoreCase(key)) {
                this.isShutdownHookEnabled = !"disable".equalsIgnoreCase(value);
            }
            else if ("shutdownTimeout".equalsIgnoreCase(key)) {
                this.shutdownTimeoutMillis = Long.parseLong(value);
            }
            else if ("verbose".equalsIgnoreCase(key)) {
                statusConfig.withVerbosity(value);
            }
            else if ("packages".equalsIgnoreCase(key)) {
                this.pluginPackages.addAll((Collection)Arrays.asList((Object[])value.split(Patterns.COMMA_SEPARATOR)));
            }
            else {
                if (!"name".equalsIgnoreCase(key)) {
                    continue;
                }
                this.setName(value);
            }
        }
        statusConfig.initialize();
    }
    
    @Override
    public void setup() {
        final AbstractConfiguration targetConfiguration = (AbstractConfiguration)this.configurations.get(0);
        this.staffChildConfiguration(targetConfiguration);
        final WatchManager watchManager = this.getWatchManager();
        final WatchManager targetWatchManager = targetConfiguration.getWatchManager();
        final FileWatcher fileWatcher = new ConfiguratonFileWatcher(this, this.listeners);
        if (targetWatchManager.getIntervalSeconds() > 0) {
            watchManager.setIntervalSeconds(targetWatchManager.getIntervalSeconds());
            final Map<File, FileWatcher> watchers = targetWatchManager.getWatchers();
            for (final Map.Entry<File, FileWatcher> entry : watchers.entrySet()) {
                if (entry.getValue() instanceof ConfiguratonFileWatcher) {
                    watchManager.watchFile((File)entry.getKey(), fileWatcher);
                }
            }
        }
        for (final AbstractConfiguration sourceConfiguration : this.configurations.subList(1, this.configurations.size())) {
            this.staffChildConfiguration(sourceConfiguration);
            final Node sourceRoot = sourceConfiguration.getRootNode();
            this.mergeStrategy.mergConfigurations(this.rootNode, sourceRoot, this.getPluginManager());
            if (CompositeConfiguration.LOGGER.isEnabled(Level.ALL)) {
                final StringBuilder sb = new StringBuilder();
                this.printNodes("", this.rootNode, sb);
                System.out.println(sb.toString());
            }
            final int monitorInterval = sourceConfiguration.getWatchManager().getIntervalSeconds();
            if (monitorInterval > 0) {
                final int currentInterval = watchManager.getIntervalSeconds();
                if (currentInterval <= 0 || monitorInterval < currentInterval) {
                    watchManager.setIntervalSeconds(monitorInterval);
                }
                final WatchManager sourceWatchManager = sourceConfiguration.getWatchManager();
                final Map<File, FileWatcher> watchers2 = sourceWatchManager.getWatchers();
                for (final Map.Entry<File, FileWatcher> entry2 : watchers2.entrySet()) {
                    if (entry2.getValue() instanceof ConfiguratonFileWatcher) {
                        watchManager.watchFile((File)entry2.getKey(), fileWatcher);
                    }
                }
            }
        }
    }
    
    @Override
    public Configuration reconfigure() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ldc_w           "Reconfiguring composite configuration"
        //     6: invokeinterface org/apache/logging/log4j/Logger.debug:(Ljava/lang/String;)V
        //    11: new             Ljava/util/ArrayList;
        //    14: dup            
        //    15: invokespecial   java/util/ArrayList.<init>:()V
        //    18: astore_1        /* configs */
        //    19: invokestatic    org/apache/logging/log4j/core/config/ConfigurationFactory.getInstance:()Lorg/apache/logging/log4j/core/config/ConfigurationFactory;
        //    22: astore_2        /* factory */
        //    23: aload_0         /* this */
        //    24: getfield        org/apache/logging/log4j/core/config/composite/CompositeConfiguration.configurations:Ljava/util/List;
        //    27: invokeinterface java/util/List.iterator:()Ljava/util/Iterator;
        //    32: astore_3        /* i$ */
        //    33: aload_3         /* i$ */
        //    34: invokeinterface java/util/Iterator.hasNext:()Z
        //    39: ifeq            152
        //    42: aload_3         /* i$ */
        //    43: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    48: checkcast       Lorg/apache/logging/log4j/core/config/AbstractConfiguration;
        //    51: astore          config
        //    53: aload           config
        //    55: invokevirtual   org/apache/logging/log4j/core/config/AbstractConfiguration.getConfigurationSource:()Lorg/apache/logging/log4j/core/config/ConfigurationSource;
        //    58: astore          source
        //    60: aload           source
        //    62: invokevirtual   org/apache/logging/log4j/core/config/ConfigurationSource.getURI:()Ljava/net/URI;
        //    65: astore          sourceURI
        //    67: aload           sourceURI
        //    69: ifnull          133
        //    72: getstatic       org/apache/logging/log4j/core/config/composite/CompositeConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //    75: ldc_w           "Unable to determine URI for configuration {}, changes to it will be ignored"
        //    78: aload           config
        //    80: invokevirtual   org/apache/logging/log4j/core/config/AbstractConfiguration.getName:()Ljava/lang/String;
        //    83: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;Ljava/lang/Object;)V
        //    88: aload_2         /* factory */
        //    89: aload_0         /* this */
        //    90: invokevirtual   org/apache/logging/log4j/core/config/composite/CompositeConfiguration.getLoggerContext:()Lorg/apache/logging/log4j/core/LoggerContext;
        //    93: aload           config
        //    95: invokevirtual   org/apache/logging/log4j/core/config/AbstractConfiguration.getName:()Ljava/lang/String;
        //    98: aload           sourceURI
        //   100: invokevirtual   org/apache/logging/log4j/core/config/ConfigurationFactory.getConfiguration:(Lorg/apache/logging/log4j/core/LoggerContext;Ljava/lang/String;Ljava/net/URI;)Lorg/apache/logging/log4j/core/config/Configuration;
        //   103: astore          currentConfig
        //   105: aload           currentConfig
        //   107: ifnonnull       137
        //   110: getstatic       org/apache/logging/log4j/core/config/composite/CompositeConfiguration.LOGGER:Lorg/apache/logging/log4j/Logger;
        //   113: ldc_w           "Unable to reload configuration {}, changes to it will be ignored"
        //   116: aload           config
        //   118: invokevirtual   org/apache/logging/log4j/core/config/AbstractConfiguration.getName:()Ljava/lang/String;
        //   121: invokeinterface org/apache/logging/log4j/Logger.warn:(Ljava/lang/String;Ljava/lang/Object;)V
        //   126: aload           config
        //   128: astore          currentConfig
        //   130: goto            137
        //   133: aload           config
        //   135: astore          currentConfig
        //   137: aload_1         /* configs */
        //   138: aload           currentConfig
        //   140: checkcast       Lorg/apache/logging/log4j/core/config/AbstractConfiguration;
        //   143: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   148: pop            
        //   149: goto            33
        //   152: new             Lorg/apache/logging/log4j/core/config/composite/CompositeConfiguration;
        //   155: dup            
        //   156: aload_1         /* configs */
        //   157: invokespecial   org/apache/logging/log4j/core/config/composite/CompositeConfiguration.<init>:(Ljava/util/List;)V
        //   160: areturn        
        //    StackMapTable: 00 04 FE 00 21 07 00 26 07 01 7A 07 00 6A FE 00 63 07 00 04 07 00 30 07 01 96 FC 00 03 07 01 98 FF 00 0E 00 03 07 00 02 07 00 26 07 01 7A 00 00
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2028)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubtypeUncheckedInternal(MetadataHelper.java:540)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubTypeUnchecked(MetadataHelper.java:520)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:507)
        //     at com.strobel.assembler.metadata.MetadataHelper.isConvertible(MetadataHelper.java:488)
        //     at com.strobel.assembler.metadata.MetadataHelper.isAssignableFrom(MetadataHelper.java:557)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperTypeCore(MetadataHelper.java:237)
        //     at com.strobel.assembler.metadata.MetadataHelper.findCommonSuperType(MetadataHelper.java:200)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:369)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:254)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2206)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:211)
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
    
    private void staffChildConfiguration(final AbstractConfiguration childConfiguration) {
        childConfiguration.setPluginManager(this.pluginManager);
        childConfiguration.setScriptManager(this.scriptManager);
        childConfiguration.setup();
    }
    
    private void printNodes(final String indent, final Node node, final StringBuilder sb) {
        sb.append(indent).append(node.getName()).append(" type: ").append(node.getType()).append("\n");
        sb.append(indent).append(node.getAttributes().toString()).append("\n");
        for (final Node child : node.getChildren()) {
            this.printNodes(indent + "  ", child, sb);
        }
    }
    
    static {
        VERBOSE_CLASSES = new String[] { ResolverUtil.class.getName() };
    }
}
