package org.apache.logging.log4j.status;

import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.logging.log4j.message.ParameterizedNoReferenceMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import java.io.Closeable;
import org.apache.logging.log4j.Level;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.message.MessageFactory;
import java.util.concurrent.locks.Lock;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.Collection;
import org.apache.logging.log4j.simple.SimpleLogger;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.spi.AbstractLogger;

public final class StatusLogger extends AbstractLogger {
    public static final String MAX_STATUS_ENTRIES = "log4j2.status.entries";
    public static final String DEFAULT_STATUS_LISTENER_LEVEL = "log4j2.StatusLogger.level";
    private static final long serialVersionUID = 2L;
    private static final String NOT_AVAIL = "?";
    private static final PropertiesUtil PROPS;
    private static final int MAX_ENTRIES;
    private static final String DEFAULT_STATUS_LEVEL;
    private static final StatusLogger STATUS_LOGGER;
    private final SimpleLogger logger;
    private final Collection<StatusListener> listeners;
    private final ReadWriteLock listenersLock;
    private final Queue<StatusData> messages;
    private final Lock msgLock;
    private int listenersLevel;
    
    private StatusLogger(final String name, final MessageFactory messageFactory) {
        super(name, messageFactory);
        this.listeners = (Collection<StatusListener>)new CopyOnWriteArrayList();
        this.listenersLock = (ReadWriteLock)new ReentrantReadWriteLock();
        this.messages = (Queue<StatusData>)new BoundedQueue(StatusLogger.MAX_ENTRIES);
        this.msgLock = (Lock)new ReentrantLock();
        this.logger = new SimpleLogger("StatusLogger", Level.ERROR, false, true, false, false, "", messageFactory, StatusLogger.PROPS, System.err);
        this.listenersLevel = Level.toLevel(StatusLogger.DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
    }
    
    public static StatusLogger getLogger() {
        return StatusLogger.STATUS_LOGGER;
    }
    
    public void setLevel(final Level level) {
        this.logger.setLevel(level);
    }
    
    public void registerListener(final StatusListener listener) {
        this.listenersLock.writeLock().lock();
        try {
            this.listeners.add(listener);
            final Level lvl = listener.getStatusLevel();
            if (this.listenersLevel < lvl.intLevel()) {
                this.listenersLevel = lvl.intLevel();
            }
        }
        finally {
            this.listenersLock.writeLock().unlock();
        }
    }
    
    public void removeListener(final StatusListener listener) {
        closeSilently((Closeable)listener);
        this.listenersLock.writeLock().lock();
        try {
            this.listeners.remove(listener);
            int lowest = Level.toLevel(StatusLogger.DEFAULT_STATUS_LEVEL, Level.WARN).intLevel();
            for (final StatusListener statusListener : this.listeners) {
                final int level = statusListener.getStatusLevel().intLevel();
                if (lowest < level) {
                    lowest = level;
                }
            }
            this.listenersLevel = lowest;
        }
        finally {
            this.listenersLock.writeLock().unlock();
        }
    }
    
    public void updateListenerLevel(final Level status) {
        if (status.intLevel() > this.listenersLevel) {
            this.listenersLevel = status.intLevel();
        }
    }
    
    public Iterable<StatusListener> getListeners() {
        return (Iterable<StatusListener>)this.listeners;
    }
    
    public void reset() {
        this.listenersLock.writeLock().lock();
        try {
            for (final StatusListener listener : this.listeners) {
                closeSilently((Closeable)listener);
            }
        }
        finally {
            this.listeners.clear();
            this.listenersLock.writeLock().unlock();
            this.clear();
        }
    }
    
    private static void closeSilently(final Closeable resource) {
        try {
            resource.close();
        }
        catch (IOException ex) {}
    }
    
    public List<StatusData> getStatusData() {
        this.msgLock.lock();
        try {
            return (List<StatusData>)new ArrayList((Collection)this.messages);
        }
        finally {
            this.msgLock.unlock();
        }
    }
    
    public void clear() {
        this.msgLock.lock();
        try {
            this.messages.clear();
        }
        finally {
            this.msgLock.unlock();
        }
    }
    
    public Level getLevel() {
        return this.logger.getLevel();
    }
    
    public void logMessage(final String fqcn, final Level level, final Marker marker, final Message msg, final Throwable t) {
        StackTraceElement element = null;
        if (fqcn != null) {
            element = this.getStackTraceElement(fqcn, Thread.currentThread().getStackTrace());
        }
        final StatusData data = new StatusData(element, level, msg, t, null);
        this.msgLock.lock();
        try {
            this.messages.add(data);
        }
        finally {
            this.msgLock.unlock();
        }
        if (this.listeners.size() > 0) {
            for (final StatusListener listener : this.listeners) {
                if (data.getLevel().isMoreSpecificThan(listener.getStatusLevel())) {
                    listener.log(data);
                }
            }
        }
        else {
            this.logger.logMessage(fqcn, level, marker, msg, t);
        }
    }
    
    private StackTraceElement getStackTraceElement(final String fqcn, final StackTraceElement[] stackTrace) {
        if (fqcn == null) {
            return null;
        }
        boolean next = false;
        for (final StackTraceElement element : stackTrace) {
            final String className = element.getClassName();
            if (next && !fqcn.equals(className)) {
                return element;
            }
            if (fqcn.equals(className)) {
                next = true;
            }
            else if ("?".equals(className)) {
                break;
            }
        }
        return null;
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object... params) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final CharSequence message, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final Object message, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    public boolean isEnabled(final Level level, final Marker marker, final Message message, final Throwable t) {
        return this.isEnabled(level, marker);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker) {
        if (this.listeners.size() > 0) {
            return this.listenersLevel >= level.intLevel();
        }
        return this.logger.isEnabled(level, marker);
    }
    
    static {
        PROPS = new PropertiesUtil("log4j2.StatusLogger.properties");
        MAX_ENTRIES = StatusLogger.PROPS.getIntegerProperty("log4j2.status.entries", 200);
        DEFAULT_STATUS_LEVEL = StatusLogger.PROPS.getStringProperty("log4j2.StatusLogger.level");
        STATUS_LOGGER = new StatusLogger(StatusLogger.class.getName(), ParameterizedNoReferenceMessageFactory.INSTANCE);
    }
    
    private class BoundedQueue<E> extends ConcurrentLinkedQueue<E> {
        private static final long serialVersionUID = -3945953719763255337L;
        private final int size;
        
        BoundedQueue(final int size) {
            this.size = size;
        }
        
        public boolean add(final E object) {
            super.add(object);
            while (StatusLogger.this.messages.size() > this.size) {
                StatusLogger.this.messages.poll();
            }
            return this.size > 0;
        }
    }
    
    private class BoundedQueue<E> extends ConcurrentLinkedQueue<E> {
        private static final long serialVersionUID = -3945953719763255337L;
        private final int size;
        
        BoundedQueue(final int size) {
            this.size = size;
        }
        
        public boolean add(final E object) {
            super.add(object);
            while (StatusLogger.this.messages.size() > this.size) {
                StatusLogger.this.messages.poll();
            }
            return this.size > 0;
        }
    }
}
