package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PropertiesRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class PropertiesRewritePolicy implements RewritePolicy {
    protected static final Logger LOGGER;
    private final Map<Property, Boolean> properties;
    private final Configuration config;
    
    private PropertiesRewritePolicy(final Configuration config, final List<Property> props) {
        this.config = config;
        this.properties = (Map<Property, Boolean>)new HashMap(props.size());
        for (final Property property : props) {
            final Boolean interpolate = property.getValue().contains("${");
            this.properties.put(property, interpolate);
        }
    }
    
    public LogEvent rewrite(final LogEvent source) {
        final Map<String, String> props = (Map<String, String>)new HashMap((Map)source.getContextData().toMap());
        for (final Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
            final Property prop = (Property)entry.getKey();
            props.put(prop.getName(), (entry.getValue() ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue()));
        }
        final LogEvent result = new Log4jLogEvent.Builder(source).setContextMap(props).build();
        return result;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" {");
        boolean first = true;
        for (final Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            final Property prop = (Property)entry.getKey();
            sb.append(prop.getName()).append('=').append(prop.getValue());
            first = false;
        }
        sb.append('}');
        return sb.toString();
    }
    
    @PluginFactory
    public static PropertiesRewritePolicy createPolicy(@PluginConfiguration final Configuration config, @PluginElement("Properties") final Property[] props) {
        if (props == null || props.length == 0) {
            PropertiesRewritePolicy.LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
            return null;
        }
        final List<Property> properties = (List<Property>)Arrays.asList((Object[])props);
        return new PropertiesRewritePolicy(config, properties);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
