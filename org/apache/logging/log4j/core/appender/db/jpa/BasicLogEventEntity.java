package org.apache.logging.log4j.core.appender.db.jpa;

import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextStackAttributeConverter;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ContextMapAttributeConverter;
import java.util.Map;
import javax.persistence.Transient;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.appender.db.jpa.converter.ThrowableAttributeConverter;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MarkerAttributeConverter;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.appender.db.jpa.converter.MessageAttributeConverter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.core.appender.db.jpa.converter.StackTraceElementAttributeConverter;
import javax.persistence.Basic;
import org.apache.logging.log4j.core.appender.db.jpa.converter.LevelAttributeConverter;
import javax.persistence.Convert;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BasicLogEventEntity extends AbstractLogEventWrapperEntity {
    private static final long serialVersionUID = 1L;
    
    public BasicLogEventEntity() {
    }
    
    public BasicLogEventEntity(final LogEvent wrappedEvent) {
        super(wrappedEvent);
    }
    
    @Convert(converter = LevelAttributeConverter.class)
    public Level getLevel() {
        return this.getWrappedEvent().getLevel();
    }
    
    @Basic
    public String getLoggerName() {
        return this.getWrappedEvent().getLoggerName();
    }
    
    @Convert(converter = StackTraceElementAttributeConverter.class)
    public StackTraceElement getSource() {
        return this.getWrappedEvent().getSource();
    }
    
    @Convert(converter = MessageAttributeConverter.class)
    public Message getMessage() {
        return this.getWrappedEvent().getMessage();
    }
    
    @Convert(converter = MarkerAttributeConverter.class)
    public Marker getMarker() {
        return this.getWrappedEvent().getMarker();
    }
    
    @Basic
    public long getThreadId() {
        return this.getWrappedEvent().getThreadId();
    }
    
    @Basic
    public int getThreadPriority() {
        return this.getWrappedEvent().getThreadPriority();
    }
    
    @Basic
    public String getThreadName() {
        return this.getWrappedEvent().getThreadName();
    }
    
    @Basic
    public long getTimeMillis() {
        return this.getWrappedEvent().getTimeMillis();
    }
    
    @Basic
    public long getNanoTime() {
        return this.getWrappedEvent().getNanoTime();
    }
    
    @Convert(converter = ThrowableAttributeConverter.class)
    public Throwable getThrown() {
        return this.getWrappedEvent().getThrown();
    }
    
    @Transient
    public ThrowableProxy getThrownProxy() {
        return this.getWrappedEvent().getThrownProxy();
    }
    
    @Convert(converter = ContextMapAttributeConverter.class)
    public Map<String, String> getContextMap() {
        return this.getWrappedEvent().getContextMap();
    }
    
    @Convert(converter = ContextStackAttributeConverter.class)
    public ThreadContext.ContextStack getContextStack() {
        return this.getWrappedEvent().getContextStack();
    }
    
    @Basic
    public String getLoggerFqcn() {
        return this.getWrappedEvent().getLoggerFqcn();
    }
}
