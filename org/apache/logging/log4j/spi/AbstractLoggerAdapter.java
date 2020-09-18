package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.util.LoaderUtil;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;

public abstract class AbstractLoggerAdapter<L> implements LoggerAdapter<L> {
    protected final Map<LoggerContext, ConcurrentMap<String, L>> registry;
    private final ReadWriteLock lock;
    
    public AbstractLoggerAdapter() {
        this.registry = (Map<LoggerContext, ConcurrentMap<String, L>>)new WeakHashMap();
        this.lock = (ReadWriteLock)new ReentrantReadWriteLock(true);
    }
    
    public L getLogger(final String name) {
        final LoggerContext context = this.getContext();
        final ConcurrentMap<String, L> loggers = this.getLoggersInContext(context);
        final L logger = (L)loggers.get(name);
        if (logger != null) {
            return logger;
        }
        loggers.putIfAbsent(name, this.newLogger(name, context));
        return (L)loggers.get(name);
    }
    
    public ConcurrentMap<String, L> getLoggersInContext(final LoggerContext context) {
        this.lock.readLock().lock();
        ConcurrentMap<String, L> loggers;
        try {
            loggers = (ConcurrentMap<String, L>)this.registry.get(context);
        }
        finally {
            this.lock.readLock().unlock();
        }
        if (loggers != null) {
            return loggers;
        }
        this.lock.writeLock().lock();
        try {
            loggers = (ConcurrentMap<String, L>)this.registry.get(context);
            if (loggers == null) {
                loggers = (ConcurrentMap<String, L>)new ConcurrentHashMap();
                this.registry.put(context, loggers);
            }
            return loggers;
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
    
    protected abstract L newLogger(final String string, final LoggerContext loggerContext);
    
    protected abstract LoggerContext getContext();
    
    protected LoggerContext getContext(final Class<?> callerClass) {
        ClassLoader cl = null;
        if (callerClass != null) {
            cl = callerClass.getClassLoader();
        }
        if (cl == null) {
            cl = LoaderUtil.getThreadContextClassLoader();
        }
        return LogManager.getContext(cl, false);
    }
    
    public void close() {
        this.lock.writeLock().lock();
        try {
            this.registry.clear();
        }
        finally {
            this.lock.writeLock().unlock();
        }
    }
}
