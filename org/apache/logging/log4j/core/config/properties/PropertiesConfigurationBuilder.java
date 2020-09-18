package org.apache.logging.log4j.core.config.properties;

import org.apache.logging.log4j.core.config.builder.api.LoggableComponentBuilder;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.core.config.builder.api.FilterableComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.core.config.ConfigurationException;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptFileComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ScriptComponentBuilder;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import java.util.Properties;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;

public class PropertiesConfigurationBuilder extends ConfigurationBuilderFactory implements Builder<PropertiesConfiguration> {
    private static final String ADVERTISER_KEY = "advertiser";
    private static final String STATUS_KEY = "status";
    private static final String SHUTDOWN_HOOK = "shutdownHook";
    private static final String SHUTDOWN_TIMEOUT = "shutdownTimeout";
    private static final String VERBOSE = "verbose";
    private static final String DEST = "dest";
    private static final String PACKAGES = "packages";
    private static final String CONFIG_NAME = "name";
    private static final String MONITOR_INTERVAL = "monitorInterval";
    private static final String CONFIG_TYPE = "type";
    private final ConfigurationBuilder<PropertiesConfiguration> builder;
    private LoggerContext loggerContext;
    private Properties rootProperties;
    
    public PropertiesConfigurationBuilder() {
        this.builder = ConfigurationBuilderFactory.<PropertiesConfiguration>newConfigurationBuilder(PropertiesConfiguration.class);
    }
    
    public PropertiesConfigurationBuilder setRootProperties(final Properties rootProperties) {
        this.rootProperties = rootProperties;
        return this;
    }
    
    public PropertiesConfigurationBuilder setConfigurationSource(final ConfigurationSource source) {
        this.builder.setConfigurationSource(source);
        return this;
    }
    
    @Override
    public PropertiesConfiguration build() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //     4: invokevirtual   java/util/Properties.stringPropertyNames:()Ljava/util/Set;
        //     7: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //    12: astore_1        /* i$ */
        //    13: aload_1         /* i$ */
        //    14: invokeinterface java/util/Iterator.hasNext:()Z
        //    19: ifeq            63
        //    22: aload_1         /* i$ */
        //    23: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //    28: checkcast       Ljava/lang/String;
        //    31: astore_2        /* key */
        //    32: aload_2         /* key */
        //    33: ldc             "."
        //    35: invokevirtual   java/lang/String.contains:(Ljava/lang/CharSequence;)Z
        //    38: ifne            60
        //    41: aload_0         /* this */
        //    42: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //    45: aload_2         /* key */
        //    46: aload_0         /* this */
        //    47: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //    50: aload_2         /* key */
        //    51: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //    54: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.addRootProperty:(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //    59: pop            
        //    60: goto            13
        //    63: aload_0         /* this */
        //    64: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //    67: aload_0         /* this */
        //    68: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //    71: ldc             "status"
        //    73: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //    76: getstatic       org/apache/logging/log4j/Level.ERROR:Lorg/apache/logging/log4j/Level;
        //    79: invokestatic    org/apache/logging/log4j/Level.toLevel:(Ljava/lang/String;Lorg/apache/logging/log4j/Level;)Lorg/apache/logging/log4j/Level;
        //    82: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setStatusLevel:(Lorg/apache/logging/log4j/Level;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //    87: aload_0         /* this */
        //    88: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //    91: ldc             "shutdownHook"
        //    93: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //    96: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setShutdownHook:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   101: aload_0         /* this */
        //   102: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   105: ldc             "shutdownTimeout"
        //   107: ldc             "0"
        //   109: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   112: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   115: getstatic       java/util/concurrent/TimeUnit.MILLISECONDS:Ljava/util/concurrent/TimeUnit;
        //   118: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setShutdownTimeout:(JLjava/util/concurrent/TimeUnit;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   123: aload_0         /* this */
        //   124: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   127: ldc             "verbose"
        //   129: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   132: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setVerbosity:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   137: aload_0         /* this */
        //   138: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   141: ldc             "dest"
        //   143: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   146: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setDestination:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   151: aload_0         /* this */
        //   152: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   155: ldc             "packages"
        //   157: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   160: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setPackages:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   165: aload_0         /* this */
        //   166: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   169: ldc             "name"
        //   171: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   174: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setConfigurationName:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   179: aload_0         /* this */
        //   180: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   183: ldc             "monitorInterval"
        //   185: ldc             "0"
        //   187: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   190: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setMonitorInterval:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   195: aload_0         /* this */
        //   196: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   199: ldc             "advertiser"
        //   201: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   204: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setAdvertiser:(Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   209: pop            
        //   210: aload_0         /* this */
        //   211: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   214: ldc             "property"
        //   216: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   219: astore_1        /* propertyPlaceholders */
        //   220: aload_1         /* propertyPlaceholders */
        //   221: invokevirtual   java/util/Properties.stringPropertyNames:()Ljava/util/Set;
        //   224: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   229: astore_2        /* i$ */
        //   230: aload_2         /* i$ */
        //   231: invokeinterface java/util/Iterator.hasNext:()Z
        //   236: ifeq            268
        //   239: aload_2         /* i$ */
        //   240: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   245: checkcast       Ljava/lang/String;
        //   248: astore_3        /* key */
        //   249: aload_0         /* this */
        //   250: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   253: aload_3         /* key */
        //   254: aload_1         /* propertyPlaceholders */
        //   255: aload_3         /* key */
        //   256: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   259: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.addProperty:(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   264: pop            
        //   265: goto            230
        //   268: aload_0         /* this */
        //   269: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   272: ldc             "script"
        //   274: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   277: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.partitionOnCommonPrefixes:(Ljava/util/Properties;)Ljava/util/Map;
        //   280: astore_2        /* scripts */
        //   281: aload_2         /* scripts */
        //   282: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   287: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   292: astore_3        /* i$ */
        //   293: aload_3         /* i$ */
        //   294: invokeinterface java/util/Iterator.hasNext:()Z
        //   299: ifeq            400
        //   302: aload_3         /* i$ */
        //   303: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   308: checkcast       Ljava/util/Map$Entry;
        //   311: astore          entry
        //   313: aload           entry
        //   315: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   320: checkcast       Ljava/util/Properties;
        //   323: astore          scriptProps
        //   325: aload           scriptProps
        //   327: ldc             "type"
        //   329: invokevirtual   java/util/Properties.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //   332: checkcast       Ljava/lang/String;
        //   335: astore          type
        //   337: aload           type
        //   339: ifnonnull       352
        //   342: new             Lorg/apache/logging/log4j/core/config/ConfigurationException;
        //   345: dup            
        //   346: ldc             "No type provided for script - must be Script or ScriptFile"
        //   348: invokespecial   org/apache/logging/log4j/core/config/ConfigurationException.<init>:(Ljava/lang/String;)V
        //   351: athrow         
        //   352: aload           type
        //   354: ldc             "script"
        //   356: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //   359: ifeq            381
        //   362: aload_0         /* this */
        //   363: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   366: aload_0         /* this */
        //   367: aload           scriptProps
        //   369: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createScript:(Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/ScriptComponentBuilder;
        //   372: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/ScriptComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   377: pop            
        //   378: goto            397
        //   381: aload_0         /* this */
        //   382: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   385: aload_0         /* this */
        //   386: aload           scriptProps
        //   388: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createScriptFile:(Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/ScriptFileComponentBuilder;
        //   391: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/ScriptFileComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   396: pop            
        //   397: goto            293
        //   400: aload_0         /* this */
        //   401: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   404: ldc             "customLevel"
        //   406: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   409: astore_3        /* levelProps */
        //   410: aload_3         /* levelProps */
        //   411: invokevirtual   java/util/Properties.size:()I
        //   414: ifle            483
        //   417: aload_3         /* levelProps */
        //   418: invokevirtual   java/util/Properties.stringPropertyNames:()Ljava/util/Set;
        //   421: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   426: astore          i$
        //   428: aload           i$
        //   430: invokeinterface java/util/Iterator.hasNext:()Z
        //   435: ifeq            483
        //   438: aload           i$
        //   440: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   445: checkcast       Ljava/lang/String;
        //   448: astore          key
        //   450: aload_0         /* this */
        //   451: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   454: aload_0         /* this */
        //   455: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   458: aload           key
        //   460: aload_3         /* levelProps */
        //   461: aload           key
        //   463: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   466: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   469: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.newCustomLevel:(Ljava/lang/String;I)Lorg/apache/logging/log4j/core/config/builder/api/CustomLevelComponentBuilder;
        //   474: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/CustomLevelComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   479: pop            
        //   480: goto            428
        //   483: aload_0         /* this */
        //   484: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   487: ldc             "filters"
        //   489: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   492: astore          filterProp
        //   494: aload           filterProp
        //   496: ifnull          594
        //   499: aload           filterProp
        //   501: ldc             ","
        //   503: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   506: astore          filterNames
        //   508: aload           filterNames
        //   510: astore          arr$
        //   512: aload           arr$
        //   514: arraylength    
        //   515: istore          len$
        //   517: iconst_0       
        //   518: istore          i$
        //   520: iload           i$
        //   522: iload           len$
        //   524: if_icmpge       591
        //   527: aload           arr$
        //   529: iload           i$
        //   531: aaload         
        //   532: astore          filterName
        //   534: aload           filterName
        //   536: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   539: astore          name
        //   541: aload_0         /* this */
        //   542: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   545: aload_0         /* this */
        //   546: aload           name
        //   548: aload_0         /* this */
        //   549: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   552: new             Ljava/lang/StringBuilder;
        //   555: dup            
        //   556: invokespecial   java/lang/StringBuilder.<init>:()V
        //   559: ldc_w           "filter."
        //   562: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   565: aload           name
        //   567: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   570: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   573: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   576: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createFilter:(Ljava/lang/String;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/FilterComponentBuilder;
        //   579: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/FilterComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   584: pop            
        //   585: iinc            i$, 1
        //   588: goto            520
        //   591: goto            685
        //   594: aload_0         /* this */
        //   595: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   598: ldc_w           "filter"
        //   601: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   604: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.partitionOnCommonPrefixes:(Ljava/util/Properties;)Ljava/util/Map;
        //   607: astore          filters
        //   609: aload           filters
        //   611: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   616: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   621: astore          i$
        //   623: aload           i$
        //   625: invokeinterface java/util/Iterator.hasNext:()Z
        //   630: ifeq            685
        //   633: aload           i$
        //   635: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   640: checkcast       Ljava/util/Map$Entry;
        //   643: astore          entry
        //   645: aload_0         /* this */
        //   646: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   649: aload_0         /* this */
        //   650: aload           entry
        //   652: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   657: checkcast       Ljava/lang/String;
        //   660: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   663: aload           entry
        //   665: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   670: checkcast       Ljava/util/Properties;
        //   673: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createFilter:(Ljava/lang/String;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/FilterComponentBuilder;
        //   676: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/FilterComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   681: pop            
        //   682: goto            623
        //   685: aload_0         /* this */
        //   686: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   689: ldc_w           "appenders"
        //   692: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   695: astore          appenderProp
        //   697: aload           appenderProp
        //   699: ifnull          800
        //   702: aload           appenderProp
        //   704: ldc             ","
        //   706: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   709: astore          appenderNames
        //   711: aload           appenderNames
        //   713: astore          arr$
        //   715: aload           arr$
        //   717: arraylength    
        //   718: istore          len$
        //   720: iconst_0       
        //   721: istore          i$
        //   723: iload           i$
        //   725: iload           len$
        //   727: if_icmpge       797
        //   730: aload           arr$
        //   732: iload           i$
        //   734: aaload         
        //   735: astore          appenderName
        //   737: aload           appenderName
        //   739: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   742: astore          name
        //   744: aload_0         /* this */
        //   745: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   748: aload_0         /* this */
        //   749: aload           appenderName
        //   751: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   754: aload_0         /* this */
        //   755: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   758: new             Ljava/lang/StringBuilder;
        //   761: dup            
        //   762: invokespecial   java/lang/StringBuilder.<init>:()V
        //   765: ldc_w           "appender."
        //   768: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   771: aload           name
        //   773: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   776: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   779: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   782: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createAppender:(Ljava/lang/String;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/AppenderComponentBuilder;
        //   785: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/AppenderComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   790: pop            
        //   791: iinc            i$, 1
        //   794: goto            723
        //   797: goto            891
        //   800: aload_0         /* this */
        //   801: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   804: ldc_w           "appender"
        //   807: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   810: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.partitionOnCommonPrefixes:(Ljava/util/Properties;)Ljava/util/Map;
        //   813: astore          appenders
        //   815: aload           appenders
        //   817: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //   822: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //   827: astore          i$
        //   829: aload           i$
        //   831: invokeinterface java/util/Iterator.hasNext:()Z
        //   836: ifeq            891
        //   839: aload           i$
        //   841: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //   846: checkcast       Ljava/util/Map$Entry;
        //   849: astore          entry
        //   851: aload_0         /* this */
        //   852: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   855: aload_0         /* this */
        //   856: aload           entry
        //   858: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //   863: checkcast       Ljava/lang/String;
        //   866: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   869: aload           entry
        //   871: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //   876: checkcast       Ljava/util/Properties;
        //   879: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createAppender:(Ljava/lang/String;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/AppenderComponentBuilder;
        //   882: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/AppenderComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   887: pop            
        //   888: goto            829
        //   891: aload_0         /* this */
        //   892: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   895: ldc_w           "loggers"
        //   898: invokevirtual   java/util/Properties.getProperty:(Ljava/lang/String;)Ljava/lang/String;
        //   901: astore          loggerProp
        //   903: aload           loggerProp
        //   905: ifnull          1014
        //   908: aload           loggerProp
        //   910: ldc             ","
        //   912: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //   915: astore          loggerNames
        //   917: aload           loggerNames
        //   919: astore          arr$
        //   921: aload           arr$
        //   923: arraylength    
        //   924: istore          len$
        //   926: iconst_0       
        //   927: istore          i$
        //   929: iload           i$
        //   931: iload           len$
        //   933: if_icmpge       1011
        //   936: aload           arr$
        //   938: iload           i$
        //   940: aaload         
        //   941: astore          loggerName
        //   943: aload           loggerName
        //   945: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //   948: astore          name
        //   950: aload           name
        //   952: ldc_w           "root"
        //   955: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   958: ifne            1005
        //   961: aload_0         /* this */
        //   962: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   965: aload_0         /* this */
        //   966: aload           name
        //   968: aload_0         /* this */
        //   969: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //   972: new             Ljava/lang/StringBuilder;
        //   975: dup            
        //   976: invokespecial   java/lang/StringBuilder.<init>:()V
        //   979: ldc_w           "logger."
        //   982: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   985: aload           name
        //   987: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   990: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   993: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //   996: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createLogger:(Ljava/lang/String;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;
        //   999: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1004: pop            
        //  1005: iinc            i$, 1
        //  1008: goto            929
        //  1011: goto            1120
        //  1014: aload_0         /* this */
        //  1015: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //  1018: ldc_w           "logger"
        //  1021: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //  1024: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.partitionOnCommonPrefixes:(Ljava/util/Properties;)Ljava/util/Map;
        //  1027: astore          loggers
        //  1029: aload           loggers
        //  1031: invokeinterface java/util/Map.entrySet:()Ljava/util/Set;
        //  1036: invokeinterface java/util/Set.iterator:()Ljava/util/Iterator;
        //  1041: astore          i$
        //  1043: aload           i$
        //  1045: invokeinterface java/util/Iterator.hasNext:()Z
        //  1050: ifeq            1120
        //  1053: aload           i$
        //  1055: invokeinterface java/util/Iterator.next:()Ljava/lang/Object;
        //  1060: checkcast       Ljava/util/Map$Entry;
        //  1063: astore          entry
        //  1065: aload           entry
        //  1067: invokeinterface java/util/Map$Entry.getKey:()Ljava/lang/Object;
        //  1072: checkcast       Ljava/lang/String;
        //  1075: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //  1078: astore          name
        //  1080: aload           name
        //  1082: ldc_w           "root"
        //  1085: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //  1088: ifne            1117
        //  1091: aload_0         /* this */
        //  1092: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1095: aload_0         /* this */
        //  1096: aload           name
        //  1098: aload           entry
        //  1100: invokeinterface java/util/Map$Entry.getValue:()Ljava/lang/Object;
        //  1105: checkcast       Ljava/util/Properties;
        //  1108: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createLogger:(Ljava/lang/String;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;
        //  1111: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1116: pop            
        //  1117: goto            1043
        //  1120: aload_0         /* this */
        //  1121: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.rootProperties:Ljava/util/Properties;
        //  1124: ldc_w           "rootLogger"
        //  1127: invokestatic    org/apache/logging/log4j/util/PropertiesUtil.extractSubset:(Ljava/util/Properties;Ljava/lang/String;)Ljava/util/Properties;
        //  1130: astore          props
        //  1132: aload           props
        //  1134: invokevirtual   java/util/Properties.size:()I
        //  1137: ifle            1156
        //  1140: aload_0         /* this */
        //  1141: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1144: aload_0         /* this */
        //  1145: aload           props
        //  1147: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.createRootLogger:(Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/RootLoggerComponentBuilder;
        //  1150: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.add:(Lorg/apache/logging/log4j/core/config/builder/api/RootLoggerComponentBuilder;)Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1155: pop            
        //  1156: aload_0         /* this */
        //  1157: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1160: aload_0         /* this */
        //  1161: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.loggerContext:Lorg/apache/logging/log4j/core/LoggerContext;
        //  1164: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.setLoggerContext:(Lorg/apache/logging/log4j/core/LoggerContext;)V
        //  1169: aload_0         /* this */
        //  1170: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //  1173: iconst_0       
        //  1174: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.build:(Z)Lorg/apache/logging/log4j/core/config/Configuration;
        //  1179: checkcast       Lorg/apache/logging/log4j/core/config/properties/PropertiesConfiguration;
        //  1182: areturn        
        //    StackMapTable: 00 1E FC 00 0D 07 00 5E 2E FA 00 02 FD 00 A6 07 00 52 07 00 5E FA 00 25 FD 00 18 07 00 0C 07 00 5E FE 00 3A 07 00 0A 07 00 52 07 00 68 1C F8 00 0F FA 00 02 FD 00 1B 07 00 52 07 00 5E FA 00 36 FF 00 24 00 09 07 00 02 07 00 52 07 00 0C 07 00 52 07 00 68 07 01 01 07 01 01 01 01 00 00 FF 00 46 00 05 07 00 02 07 00 52 07 00 0C 07 00 52 07 00 68 00 00 02 FD 00 1C 07 00 0C 07 00 5E F9 00 3D FF 00 25 00 0A 07 00 02 07 00 52 07 00 0C 07 00 52 07 00 68 07 00 68 07 01 01 07 01 01 01 01 00 00 FF 00 49 00 06 07 00 02 07 00 52 07 00 0C 07 00 52 07 00 68 07 00 68 00 00 02 FD 00 1C 07 00 0C 07 00 5E F9 00 3D FF 00 25 00 0B 07 00 02 07 00 52 07 00 0C 07 00 52 07 00 68 07 00 68 07 00 68 07 01 01 07 01 01 01 01 00 00 FB 00 4B FF 00 05 00 07 07 00 02 07 00 52 07 00 0C 07 00 52 07 00 68 07 00 68 07 00 68 00 00 02 FD 00 1C 07 00 0C 07 00 5E FB 00 49 F9 00 02 FC 00 23 07 00 52
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.TypeDefinition.accept(TypeDefinition.java:183)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
        //     at com.strobel.assembler.metadata.TypeReference.equals(TypeReference.java:118)
        //     at com.strobel.core.Comparer.equals(Comparer.java:31)
        //     at com.strobel.assembler.ir.FrameValue.equals(FrameValue.java:72)
        //     at com.strobel.assembler.ir.Frame.merge(Frame.java:386)
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
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:408)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private ScriptComponentBuilder createScript(final Properties properties) {
        final String name = (String)properties.remove("name");
        final String language = (String)properties.remove("language");
        final String text = (String)properties.remove("text");
        final ScriptComponentBuilder scriptBuilder = this.builder.newScript(name, language, text);
        return PropertiesConfigurationBuilder.<ScriptComponentBuilder>processRemainingProperties(scriptBuilder, properties);
    }
    
    private ScriptFileComponentBuilder createScriptFile(final Properties properties) {
        final String name = (String)properties.remove("name");
        final String path = (String)properties.remove("path");
        final ScriptFileComponentBuilder scriptFileBuilder = this.builder.newScriptFile(name, path);
        return PropertiesConfigurationBuilder.<ScriptFileComponentBuilder>processRemainingProperties(scriptFileBuilder, properties);
    }
    
    private AppenderComponentBuilder createAppender(final String key, final Properties properties) {
        final String name = (String)properties.remove("name");
        if (Strings.isEmpty((CharSequence)name)) {
            throw new ConfigurationException("No name attribute provided for Appender " + key);
        }
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty((CharSequence)type)) {
            throw new ConfigurationException("No type attribute provided for Appender " + key);
        }
        final AppenderComponentBuilder appenderBuilder = this.builder.newAppender(name, type);
        this.<AppenderComponentBuilder>addFiltersToComponent(appenderBuilder, properties);
        final Properties layoutProps = PropertiesUtil.extractSubset(properties, "layout");
        if (layoutProps.size() > 0) {
            appenderBuilder.add(this.createLayout(name, layoutProps));
        }
        return PropertiesConfigurationBuilder.<AppenderComponentBuilder>processRemainingProperties(appenderBuilder, properties);
    }
    
    private FilterComponentBuilder createFilter(final String key, final Properties properties) {
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty((CharSequence)type)) {
            throw new ConfigurationException("No type attribute provided for Appender " + key);
        }
        final String onMatch = (String)properties.remove("onMatch");
        final String onMisMatch = (String)properties.remove("onMisMatch");
        final FilterComponentBuilder filterBuilder = this.builder.newFilter(type, onMatch, onMisMatch);
        return PropertiesConfigurationBuilder.<FilterComponentBuilder>processRemainingProperties(filterBuilder, properties);
    }
    
    private AppenderRefComponentBuilder createAppenderRef(final String key, final Properties properties) {
        final String ref = (String)properties.remove("ref");
        if (Strings.isEmpty((CharSequence)ref)) {
            throw new ConfigurationException("No ref attribute provided for AppenderRef " + key);
        }
        final AppenderRefComponentBuilder appenderRefBuilder = this.builder.newAppenderRef(ref);
        final String level = (String)properties.remove("level");
        if (!Strings.isEmpty((CharSequence)level)) {
            appenderRefBuilder.addAttribute("level", level);
        }
        return this.<AppenderRefComponentBuilder>addFiltersToComponent(appenderRefBuilder, properties);
    }
    
    private LoggerComponentBuilder createLogger(final String key, final Properties properties) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "name"
        //     3: invokevirtual   java/util/Properties.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //     6: checkcast       Ljava/lang/String;
        //     9: astore_3        /* name */
        //    10: aload_2         /* properties */
        //    11: ldc_w           "includeLocation"
        //    14: invokevirtual   java/util/Properties.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    17: checkcast       Ljava/lang/String;
        //    20: astore          location
        //    22: aload_3         /* name */
        //    23: invokestatic    org/apache/logging/log4j/util/Strings.isEmpty:(Ljava/lang/CharSequence;)Z
        //    26: ifeq            57
        //    29: new             Lorg/apache/logging/log4j/core/config/ConfigurationException;
        //    32: dup            
        //    33: new             Ljava/lang/StringBuilder;
        //    36: dup            
        //    37: invokespecial   java/lang/StringBuilder.<init>:()V
        //    40: ldc_w           "No name attribute provided for Logger "
        //    43: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    46: aload_1         /* key */
        //    47: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    50: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    53: invokespecial   org/apache/logging/log4j/core/config/ConfigurationException.<init>:(Ljava/lang/String;)V
        //    56: athrow         
        //    57: aload_2         /* properties */
        //    58: ldc_w           "level"
        //    61: invokevirtual   java/util/Properties.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    64: checkcast       Ljava/lang/String;
        //    67: astore          level
        //    69: aload_2         /* properties */
        //    70: ldc             "type"
        //    72: invokevirtual   java/util/Properties.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //    75: checkcast       Ljava/lang/String;
        //    78: astore          type
        //    80: aload           type
        //    82: ifnull          183
        //    85: aload           type
        //    87: ldc_w           "asyncLogger"
        //    90: invokevirtual   java/lang/String.equalsIgnoreCase:(Ljava/lang/String;)Z
        //    93: ifeq            144
        //    96: aload           location
        //    98: ifnull          127
        //   101: aload           location
        //   103: invokestatic    java/lang/Boolean.parseBoolean:(Ljava/lang/String;)Z
        //   106: istore          includeLocation
        //   108: aload_0         /* this */
        //   109: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   112: aload_3         /* name */
        //   113: aload           level
        //   115: iload           includeLocation
        //   117: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.newAsyncLogger:(Ljava/lang/String;Ljava/lang/String;Z)Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;
        //   122: astore          loggerBuilder
        //   124: goto            228
        //   127: aload_0         /* this */
        //   128: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   131: aload_3         /* name */
        //   132: aload           level
        //   134: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.newAsyncLogger:(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;
        //   139: astore          loggerBuilder
        //   141: goto            228
        //   144: new             Lorg/apache/logging/log4j/core/config/ConfigurationException;
        //   147: dup            
        //   148: new             Ljava/lang/StringBuilder;
        //   151: dup            
        //   152: invokespecial   java/lang/StringBuilder.<init>:()V
        //   155: ldc_w           "Unknown Logger type "
        //   158: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   161: aload           type
        //   163: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   166: ldc_w           " for Logger "
        //   169: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   172: aload_3         /* name */
        //   173: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   176: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   179: invokespecial   org/apache/logging/log4j/core/config/ConfigurationException.<init>:(Ljava/lang/String;)V
        //   182: athrow         
        //   183: aload           location
        //   185: ifnull          214
        //   188: aload           location
        //   190: invokestatic    java/lang/Boolean.parseBoolean:(Ljava/lang/String;)Z
        //   193: istore          includeLocation
        //   195: aload_0         /* this */
        //   196: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   199: aload_3         /* name */
        //   200: aload           level
        //   202: iload           includeLocation
        //   204: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.newLogger:(Ljava/lang/String;Ljava/lang/String;Z)Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;
        //   209: astore          loggerBuilder
        //   211: goto            228
        //   214: aload_0         /* this */
        //   215: getfield        org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.builder:Lorg/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder;
        //   218: aload_3         /* name */
        //   219: aload           level
        //   221: invokeinterface org/apache/logging/log4j/core/config/builder/api/ConfigurationBuilder.newLogger:(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder;
        //   226: astore          loggerBuilder
        //   228: aload_0         /* this */
        //   229: aload           loggerBuilder
        //   231: aload_2         /* properties */
        //   232: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.addLoggersToComponent:(Lorg/apache/logging/log4j/core/config/builder/api/LoggableComponentBuilder;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/LoggableComponentBuilder;
        //   235: pop            
        //   236: aload_0         /* this */
        //   237: aload           loggerBuilder
        //   239: aload_2         /* properties */
        //   240: invokespecial   org/apache/logging/log4j/core/config/properties/PropertiesConfigurationBuilder.addFiltersToComponent:(Lorg/apache/logging/log4j/core/config/builder/api/FilterableComponentBuilder;Ljava/util/Properties;)Lorg/apache/logging/log4j/core/config/builder/api/FilterableComponentBuilder;
        //   243: pop            
        //   244: aload_2         /* properties */
        //   245: ldc_w           "additivity"
        //   248: invokevirtual   java/util/Properties.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //   251: checkcast       Ljava/lang/String;
        //   254: astore          additivity
        //   256: aload           additivity
        //   258: invokestatic    org/apache/logging/log4j/util/Strings.isEmpty:(Ljava/lang/CharSequence;)Z
        //   261: ifne            277
        //   264: aload           loggerBuilder
        //   266: ldc_w           "additivity"
        //   269: aload           additivity
        //   271: invokeinterface org/apache/logging/log4j/core/config/builder/api/LoggerComponentBuilder.addAttribute:(Ljava/lang/String;Ljava/lang/String;)Lorg/apache/logging/log4j/core/config/builder/api/ComponentBuilder;
        //   276: pop            
        //   277: aload           loggerBuilder
        //   279: areturn        
        //    MethodParameters:
        //  Name        Flags  
        //  ----------  -----
        //  key         
        //  properties  
        //    StackMapTable: 00 07 FD 00 39 07 00 68 07 00 68 FD 00 45 07 00 68 07 00 68 10 26 1E FC 00 0D 07 01 DF FD 00 30 00 07 00 68
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 4
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:258)
        //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:851)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2476)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:408)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private RootLoggerComponentBuilder createRootLogger(final Properties properties) {
        final String level = (String)properties.remove("level");
        final String type = (String)properties.remove("type");
        final String location = (String)properties.remove("includeLocation");
        RootLoggerComponentBuilder loggerBuilder;
        if (type != null) {
            if (!type.equalsIgnoreCase("asyncRoot")) {
                throw new ConfigurationException("Unknown Logger type for root logger" + type);
            }
            if (location != null) {
                final boolean includeLocation = Boolean.parseBoolean(location);
                loggerBuilder = this.builder.newAsyncRootLogger(level, includeLocation);
            }
            else {
                loggerBuilder = this.builder.newAsyncRootLogger(level);
            }
        }
        else if (location != null) {
            final boolean includeLocation = Boolean.parseBoolean(location);
            loggerBuilder = this.builder.newRootLogger(level, includeLocation);
        }
        else {
            loggerBuilder = this.builder.newRootLogger(level);
        }
        this.<RootLoggerComponentBuilder>addLoggersToComponent(loggerBuilder, properties);
        return this.<RootLoggerComponentBuilder>addFiltersToComponent(loggerBuilder, properties);
    }
    
    private LayoutComponentBuilder createLayout(final String appenderName, final Properties properties) {
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty((CharSequence)type)) {
            throw new ConfigurationException("No type attribute provided for Layout on Appender " + appenderName);
        }
        final LayoutComponentBuilder layoutBuilder = this.builder.newLayout(type);
        return PropertiesConfigurationBuilder.<LayoutComponentBuilder>processRemainingProperties(layoutBuilder, properties);
    }
    
    private static <B extends ComponentBuilder<B>> ComponentBuilder<B> createComponent(final ComponentBuilder<?> parent, final String key, final Properties properties) {
        final String name = (String)properties.remove("name");
        final String type = (String)properties.remove("type");
        if (Strings.isEmpty((CharSequence)type)) {
            throw new ConfigurationException("No type attribute provided for component " + key);
        }
        final ComponentBuilder<B> componentBuilder = parent.getBuilder().<B>newComponent(name, type);
        return PropertiesConfigurationBuilder.<ComponentBuilder<B>>processRemainingProperties(componentBuilder, properties);
    }
    
    private static <B extends ComponentBuilder<?>> B processRemainingProperties(final B builder, final Properties properties) {
        while (properties.size() > 0) {
            final String propertyName = (String)properties.stringPropertyNames().iterator().next();
            final int index = propertyName.indexOf(46);
            if (index > 0) {
                final String prefix = propertyName.substring(0, index);
                final Properties componentProperties = PropertiesUtil.extractSubset(properties, prefix);
                builder.addComponent(PropertiesConfigurationBuilder.createComponent(builder, prefix, componentProperties));
            }
            else {
                builder.addAttribute(propertyName, properties.getProperty(propertyName));
                properties.remove(propertyName);
            }
        }
        return builder;
    }
    
    private <B extends FilterableComponentBuilder<? extends ComponentBuilder<?>>> B addFiltersToComponent(final B componentBuilder, final Properties properties) {
        final Map<String, Properties> filters = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(properties, "filter"));
        for (final Map.Entry<String, Properties> entry : filters.entrySet()) {
            componentBuilder.add(this.createFilter(((String)entry.getKey()).trim(), (Properties)entry.getValue()));
        }
        return componentBuilder;
    }
    
    private <B extends LoggableComponentBuilder<? extends ComponentBuilder<?>>> B addLoggersToComponent(final B loggerBuilder, final Properties properties) {
        final Map<String, Properties> appenderRefs = PropertiesUtil.partitionOnCommonPrefixes(PropertiesUtil.extractSubset(properties, "appenderRef"));
        for (final Map.Entry<String, Properties> entry : appenderRefs.entrySet()) {
            loggerBuilder.add(this.createAppenderRef(((String)entry.getKey()).trim(), (Properties)entry.getValue()));
        }
        return loggerBuilder;
    }
    
    public PropertiesConfigurationBuilder setLoggerContext(final LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
        return this;
    }
    
    public LoggerContext getLoggerContext() {
        return this.loggerContext;
    }
}
