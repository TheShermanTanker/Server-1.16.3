package org.apache.logging.log4j.core.net;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.Hashtable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;
import org.apache.logging.log4j.core.util.Integers;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "multicastdns", category = "Core", elementType = "advertiser", printObject = false)
public class MulticastDnsAdvertiser implements Advertiser {
    protected static final Logger LOGGER;
    private static final int MAX_LENGTH = 255;
    private static final int DEFAULT_PORT = 4555;
    private static Object jmDNS;
    private static Class<?> jmDNSClass;
    private static Class<?> serviceInfoClass;
    
    public Object advertise(final Map<String, String> properties) {
        final Map<String, String> truncatedProperties = (Map<String, String>)new HashMap();
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            if (((String)entry.getKey()).length() <= 255 && ((String)entry.getValue()).length() <= 255) {
                truncatedProperties.put(entry.getKey(), entry.getValue());
            }
        }
        final String protocol = (String)truncatedProperties.get("protocol");
        final String zone = new StringBuilder().append("._log4j._").append((protocol != null) ? protocol : "tcp").append(".local.").toString();
        final String portString = (String)truncatedProperties.get("port");
        final int port = Integers.parseInt(portString, 4555);
        final String name = (String)truncatedProperties.get("name");
        if (MulticastDnsAdvertiser.jmDNS != null) {
            boolean isVersion3 = false;
            try {
                MulticastDnsAdvertiser.jmDNSClass.getMethod("create", new Class[0]);
                isVersion3 = true;
            }
            catch (NoSuchMethodException ex2) {}
            Object serviceInfo;
            if (isVersion3) {
                serviceInfo = buildServiceInfoVersion3(zone, port, name, truncatedProperties);
            }
            else {
                serviceInfo = buildServiceInfoVersion1(zone, port, name, truncatedProperties);
            }
            try {
                final Method method = MulticastDnsAdvertiser.jmDNSClass.getMethod("registerService", new Class[] { MulticastDnsAdvertiser.serviceInfoClass });
                method.invoke(MulticastDnsAdvertiser.jmDNS, new Object[] { serviceInfo });
            }
            catch (IllegalAccessException | InvocationTargetException ex3) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke registerService method", (Throwable)e);
            }
            catch (NoSuchMethodException e2) {
                MulticastDnsAdvertiser.LOGGER.warn("No registerService method", (Throwable)e2);
            }
            return serviceInfo;
        }
        MulticastDnsAdvertiser.LOGGER.warn("JMDNS not available - will not advertise ZeroConf support");
        return null;
    }
    
    public void unadvertise(final Object serviceInfo) {
        if (MulticastDnsAdvertiser.jmDNS != null) {
            try {
                final Method method = MulticastDnsAdvertiser.jmDNSClass.getMethod("unregisterService", new Class[] { MulticastDnsAdvertiser.serviceInfoClass });
                method.invoke(MulticastDnsAdvertiser.jmDNS, new Object[] { serviceInfo });
            }
            catch (IllegalAccessException | InvocationTargetException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke unregisterService method", (Throwable)e);
            }
            catch (NoSuchMethodException e2) {
                MulticastDnsAdvertiser.LOGGER.warn("No unregisterService method", (Throwable)e2);
            }
        }
    }
    
    private static Object createJmDnsVersion1() {
        try {
            return MulticastDnsAdvertiser.jmDNSClass.getConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to instantiate JMDNS", (Throwable)e);
            return null;
        }
    }
    
    private static Object createJmDnsVersion3() {
        try {
            final Method jmDNSCreateMethod = MulticastDnsAdvertiser.jmDNSClass.getMethod("create", new Class[0]);
            return jmDNSCreateMethod.invoke(null, (Object[])null);
        }
        catch (IllegalAccessException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke create method", (Throwable)e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDnsAdvertiser.LOGGER.warn("Unable to get create method", (Throwable)e2);
        }
        return null;
    }
    
    private static Object buildServiceInfoVersion1(final String zone, final int port, final String name, final Map<String, String> properties) {
        final Hashtable<String, String> hashtableProperties = (Hashtable<String, String>)new Hashtable((Map)properties);
        try {
            return MulticastDnsAdvertiser.serviceInfoClass.getConstructor(new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Hashtable.class }).newInstance(new Object[] { zone, name, port, 0, 0, hashtableProperties });
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to construct ServiceInfo instance", (Throwable)e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDnsAdvertiser.LOGGER.warn("Unable to get ServiceInfo constructor", (Throwable)e2);
        }
        return null;
    }
    
    private static Object buildServiceInfoVersion3(final String zone, final int port, final String name, final Map<String, String> properties) {
        try {
            return MulticastDnsAdvertiser.serviceInfoClass.getMethod("create", new Class[] { String.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Map.class }).invoke(null, new Object[] { zone, name, port, 0, 0, properties });
        }
        catch (IllegalAccessException | InvocationTargetException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            MulticastDnsAdvertiser.LOGGER.warn("Unable to invoke create method", (Throwable)e);
        }
        catch (NoSuchMethodException e2) {
            MulticastDnsAdvertiser.LOGGER.warn("Unable to find create method", (Throwable)e2);
        }
        return null;
    }
    
    private static Object initializeJmDns() {
        try {
            MulticastDnsAdvertiser.jmDNSClass = LoaderUtil.loadClass("javax.jmdns.JmDNS");
            MulticastDnsAdvertiser.serviceInfoClass = LoaderUtil.loadClass("javax.jmdns.ServiceInfo");
            boolean isVersion3 = false;
            try {
                MulticastDnsAdvertiser.jmDNSClass.getMethod("create", new Class[0]);
                isVersion3 = true;
            }
            catch (NoSuchMethodException ex) {}
            if (isVersion3) {
                return createJmDnsVersion3();
            }
            return createJmDnsVersion1();
        }
        catch (ClassNotFoundException | ExceptionInInitializerError ex2) {
            final Throwable t;
            final Throwable e = t;
            MulticastDnsAdvertiser.LOGGER.warn("JmDNS or serviceInfo class not found", e);
            return null;
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        MulticastDnsAdvertiser.jmDNS = initializeJmDns();
    }
}
