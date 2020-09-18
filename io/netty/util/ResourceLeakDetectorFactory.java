package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.security.AccessController;
import io.netty.util.internal.SystemPropertyUtil;
import java.security.PrivilegedAction;
import java.lang.reflect.Constructor;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.logging.InternalLogger;

public abstract class ResourceLeakDetectorFactory {
    private static final InternalLogger logger;
    private static volatile ResourceLeakDetectorFactory factoryInstance;
    
    public static ResourceLeakDetectorFactory instance() {
        return ResourceLeakDetectorFactory.factoryInstance;
    }
    
    public static void setResourceLeakDetectorFactory(final ResourceLeakDetectorFactory factory) {
        ResourceLeakDetectorFactory.factoryInstance = ObjectUtil.<ResourceLeakDetectorFactory>checkNotNull(factory, "factory");
    }
    
    public final <T> ResourceLeakDetector<T> newResourceLeakDetector(final Class<T> resource) {
        return this.<T>newResourceLeakDetector(resource, 128);
    }
    
    @Deprecated
    public abstract <T> ResourceLeakDetector<T> newResourceLeakDetector(final Class<T> class1, final int integer, final long long3);
    
    public <T> ResourceLeakDetector<T> newResourceLeakDetector(final Class<T> resource, final int samplingInterval) {
        return this.<T>newResourceLeakDetector(resource, 128, Long.MAX_VALUE);
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ResourceLeakDetectorFactory.class);
        ResourceLeakDetectorFactory.factoryInstance = new DefaultResourceLeakDetectorFactory();
    }
    
    private static final class DefaultResourceLeakDetectorFactory extends ResourceLeakDetectorFactory {
        private final Constructor<?> obsoleteCustomClassConstructor;
        private final Constructor<?> customClassConstructor;
        
        DefaultResourceLeakDetectorFactory() {
            String customLeakDetector;
            try {
                customLeakDetector = (String)AccessController.doPrivileged((PrivilegedAction)new PrivilegedAction<String>() {
                    public String run() {
                        return SystemPropertyUtil.get("io.netty.customResourceLeakDetector");
                    }
                });
            }
            catch (Throwable cause) {
                ResourceLeakDetectorFactory.logger.error("Could not access System property: io.netty.customResourceLeakDetector", cause);
                customLeakDetector = null;
            }
            if (customLeakDetector == null) {
                final Constructor<?> constructor = null;
                this.customClassConstructor = constructor;
                this.obsoleteCustomClassConstructor = constructor;
            }
            else {
                this.obsoleteCustomClassConstructor = obsoleteCustomClassConstructor(customLeakDetector);
                this.customClassConstructor = customClassConstructor(customLeakDetector);
            }
        }
        
        private static Constructor<?> obsoleteCustomClassConstructor(final String customLeakDetector) {
            try {
                final Class<?> detectorClass = Class.forName(customLeakDetector, true, PlatformDependent.getSystemClassLoader());
                if (ResourceLeakDetector.class.isAssignableFrom((Class)detectorClass)) {
                    return detectorClass.getConstructor(new Class[] { Class.class, Integer.TYPE, Long.TYPE });
                }
                ResourceLeakDetectorFactory.logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
            }
            catch (Throwable t) {
                ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector class provided: {}", customLeakDetector, t);
            }
            return null;
        }
        
        private static Constructor<?> customClassConstructor(final String customLeakDetector) {
            try {
                final Class<?> detectorClass = Class.forName(customLeakDetector, true, PlatformDependent.getSystemClassLoader());
                if (ResourceLeakDetector.class.isAssignableFrom((Class)detectorClass)) {
                    return detectorClass.getConstructor(new Class[] { Class.class, Integer.TYPE });
                }
                ResourceLeakDetectorFactory.logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
            }
            catch (Throwable t) {
                ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector class provided: {}", customLeakDetector, t);
            }
            return null;
        }
        
        @Override
        public <T> ResourceLeakDetector<T> newResourceLeakDetector(final Class<T> resource, final int samplingInterval, final long maxActive) {
            if (this.obsoleteCustomClassConstructor != null) {
                try {
                    final ResourceLeakDetector<T> leakDetector = (ResourceLeakDetector<T>)this.obsoleteCustomClassConstructor.newInstance(new Object[] { resource, samplingInterval, maxActive });
                    ResourceLeakDetectorFactory.logger.debug("Loaded custom ResourceLeakDetector: {}", this.obsoleteCustomClassConstructor.getDeclaringClass().getName());
                    return leakDetector;
                }
                catch (Throwable t) {
                    ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector provided: {} with the given resource: {}", this.obsoleteCustomClassConstructor.getDeclaringClass().getName(), resource, t);
                }
            }
            final ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<T>(resource, samplingInterval, maxActive);
            ResourceLeakDetectorFactory.logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
            return resourceLeakDetector;
        }
        
        @Override
        public <T> ResourceLeakDetector<T> newResourceLeakDetector(final Class<T> resource, final int samplingInterval) {
            if (this.customClassConstructor != null) {
                try {
                    final ResourceLeakDetector<T> leakDetector = (ResourceLeakDetector<T>)this.customClassConstructor.newInstance(new Object[] { resource, samplingInterval });
                    ResourceLeakDetectorFactory.logger.debug("Loaded custom ResourceLeakDetector: {}", this.customClassConstructor.getDeclaringClass().getName());
                    return leakDetector;
                }
                catch (Throwable t) {
                    ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector provided: {} with the given resource: {}", this.customClassConstructor.getDeclaringClass().getName(), resource, t);
                }
            }
            final ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<T>(resource, samplingInterval);
            ResourceLeakDetectorFactory.logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
            return resourceLeakDetector;
        }
    }
}
