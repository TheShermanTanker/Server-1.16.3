package org.apache.logging.log4j.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public final class PropertiesUtil {
    private static final PropertiesUtil LOG4J_PROPERTIES;
    private final Properties props;
    
    public PropertiesUtil(final Properties props) {
        this.props = props;
    }
    
    public PropertiesUtil(final String propertiesFileName) {
        final Properties properties = new Properties();
        for (final URL url : LoaderUtil.findResources(propertiesFileName)) {
            try (final InputStream in = url.openStream()) {
                properties.load(in);
            }
            catch (IOException ioe) {
                LowLevelLogUtil.logException("Unable to read " + url.toString(), (Throwable)ioe);
            }
        }
        this.props = properties;
    }
    
    static Properties loadClose(final InputStream in, final Object source) {
        final Properties props = new Properties();
        if (null != in) {
            try {
                props.load(in);
            }
            catch (IOException e) {
                LowLevelLogUtil.logException(new StringBuilder().append("Unable to read ").append(source).toString(), (Throwable)e);
                try {
                    in.close();
                }
                catch (IOException e) {
                    LowLevelLogUtil.logException(new StringBuilder().append("Unable to close ").append(source).toString(), (Throwable)e);
                }
            }
            finally {
                try {
                    in.close();
                }
                catch (IOException e2) {
                    LowLevelLogUtil.logException(new StringBuilder().append("Unable to close ").append(source).toString(), (Throwable)e2);
                }
            }
        }
        return props;
    }
    
    public static PropertiesUtil getProperties() {
        return PropertiesUtil.LOG4J_PROPERTIES;
    }
    
    public boolean getBooleanProperty(final String name) {
        return this.getBooleanProperty(name, false);
    }
    
    public boolean getBooleanProperty(final String name, final boolean defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
    }
    
    public Charset getCharsetProperty(final String name) {
        return this.getCharsetProperty(name, Charset.defaultCharset());
    }
    
    public Charset getCharsetProperty(final String name, final Charset defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : Charset.forName(prop);
    }
    
    public double getDoubleProperty(final String name, final double defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Double.parseDouble(prop);
            }
            catch (Exception ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public int getIntegerProperty(final String name, final int defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Integer.parseInt(prop);
            }
            catch (Exception ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public long getLongProperty(final String name, final long defaultValue) {
        final String prop = this.getStringProperty(name);
        if (prop != null) {
            try {
                return Long.parseLong(prop);
            }
            catch (Exception ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public String getStringProperty(final String name) {
        String prop = null;
        try {
            prop = System.getProperty(name);
        }
        catch (SecurityException ex) {}
        return (prop == null) ? this.props.getProperty(name) : prop;
    }
    
    public String getStringProperty(final String name, final String defaultValue) {
        final String prop = this.getStringProperty(name);
        return (prop == null) ? defaultValue : prop;
    }
    
    public static Properties getSystemProperties() {
        try {
            return new Properties(System.getProperties());
        }
        catch (SecurityException ex) {
            LowLevelLogUtil.logException("Unable to access system properties.", (Throwable)ex);
            return new Properties();
        }
    }
    
    public static Properties extractSubset(final Properties properties, final String prefix) {
        final Properties subset = new Properties();
        if (prefix == null || prefix.length() == 0) {
            return subset;
        }
        final String prefixToMatch = (prefix.charAt(prefix.length() - 1) != '.') ? (prefix + '.') : prefix;
        final List<String> keys = (List<String>)new ArrayList();
        for (final String key : properties.stringPropertyNames()) {
            if (key.startsWith(prefixToMatch)) {
                subset.setProperty(key.substring(prefixToMatch.length()), properties.getProperty(key));
                keys.add(key);
            }
        }
        for (final String key : keys) {
            properties.remove(key);
        }
        return subset;
    }
    
    public static Map<String, Properties> partitionOnCommonPrefixes(final Properties properties) {
        final Map<String, Properties> parts = (Map<String, Properties>)new ConcurrentHashMap();
        for (final String key : properties.stringPropertyNames()) {
            final String prefix = key.substring(0, key.indexOf(46));
            if (!parts.containsKey(prefix)) {
                parts.put(prefix, new Properties());
            }
            ((Properties)parts.get(prefix)).setProperty(key.substring(key.indexOf(46) + 1), properties.getProperty(key));
        }
        return parts;
    }
    
    public boolean isOsWindows() {
        return this.getStringProperty("os.name").startsWith("Windows");
    }
    
    static {
        LOG4J_PROPERTIES = new PropertiesUtil("log4j2.component.properties");
    }
}
