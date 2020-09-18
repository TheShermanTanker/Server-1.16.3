package com.google.common.base;

import java.net.URLClassLoader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.annotation.Nullable;
import com.google.common.annotations.VisibleForTesting;
import java.lang.ref.Reference;
import java.util.logging.Level;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import com.google.common.annotations.GwtIncompatible;
import java.io.Closeable;

@GwtIncompatible
public class FinalizableReferenceQueue implements Closeable {
    private static final Logger logger;
    private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
    private static final Method startFinalizer;
    final ReferenceQueue<Object> queue;
    final PhantomReference<Object> frqRef;
    final boolean threadStarted;
    
    public FinalizableReferenceQueue() {
        this.queue = (ReferenceQueue<Object>)new ReferenceQueue();
        this.frqRef = (PhantomReference<Object>)new PhantomReference(this, (ReferenceQueue)this.queue);
        boolean threadStarted = false;
        try {
            FinalizableReferenceQueue.startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this.queue, this.frqRef });
            threadStarted = true;
        }
        catch (IllegalAccessException impossible) {
            throw new AssertionError(impossible);
        }
        catch (Throwable t) {
            FinalizableReferenceQueue.logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
        }
        this.threadStarted = threadStarted;
    }
    
    public void close() {
        this.frqRef.enqueue();
        this.cleanUp();
    }
    
    void cleanUp() {
        if (this.threadStarted) {
            return;
        }
        Reference<?> reference;
        while ((reference = this.queue.poll()) != null) {
            reference.clear();
            try {
                ((FinalizableReference)reference).finalizeReferent();
            }
            catch (Throwable t) {
                FinalizableReferenceQueue.logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
            }
        }
    }
    
    private static Class<?> loadFinalizer(final FinalizerLoader... loaders) {
        for (final FinalizerLoader loader : loaders) {
            final Class<?> finalizer = loader.loadFinalizer();
            if (finalizer != null) {
                return finalizer;
            }
        }
        throw new AssertionError();
    }
    
    static Method getStartFinalizer(final Class<?> finalizer) {
        try {
            return finalizer.getMethod("startFinalizer", new Class[] { Class.class, ReferenceQueue.class, PhantomReference.class });
        }
        catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }
    
    static {
        logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
        final Class<?> finalizer = loadFinalizer(new SystemLoader(), new DecoupledLoader(), new DirectLoader());
        startFinalizer = getStartFinalizer(finalizer);
    }
    
    static class SystemLoader implements FinalizerLoader {
        @VisibleForTesting
        static boolean disabled;
        
        @Nullable
        public Class<?> loadFinalizer() {
            if (SystemLoader.disabled) {
                return null;
            }
            ClassLoader systemLoader;
            try {
                systemLoader = ClassLoader.getSystemClassLoader();
            }
            catch (SecurityException e) {
                FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
                return null;
            }
            if (systemLoader != null) {
                try {
                    return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
                }
                catch (ClassNotFoundException e2) {
                    return null;
                }
            }
            return null;
        }
    }
    
    static class DecoupledLoader implements FinalizerLoader {
        private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.";
        
        @Nullable
        public Class<?> loadFinalizer() {
            try {
                final ClassLoader finalizerLoader = (ClassLoader)this.newLoader(this.getBaseUrl());
                return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
            }
            catch (Exception e) {
                FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader. Loading Finalizer in the current class loader instead. As a result, you will not be able to garbage collect this class loader. To support reclaiming this class loader, either resolve the underlying issue, or move Guava to your system class path.", (Throwable)e);
                return null;
            }
        }
        
        URL getBaseUrl() throws IOException {
            final String finalizerPath = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
            final URL finalizerUrl = this.getClass().getClassLoader().getResource(finalizerPath);
            if (finalizerUrl == null) {
                throw new FileNotFoundException(finalizerPath);
            }
            String urlString = finalizerUrl.toString();
            if (!urlString.endsWith(finalizerPath)) {
                throw new IOException("Unsupported path style: " + urlString);
            }
            urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
            return new URL(finalizerUrl, urlString);
        }
        
        URLClassLoader newLoader(final URL base) {
            return new URLClassLoader(new URL[] { base }, (ClassLoader)null);
        }
    }
    
    static class DirectLoader implements FinalizerLoader {
        public Class<?> loadFinalizer() {
            try {
                return Class.forName("com.google.common.base.internal.Finalizer");
            }
            catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
        }
    }
    
    interface FinalizerLoader {
        @Nullable
        Class<?> loadFinalizer();
    }
}
