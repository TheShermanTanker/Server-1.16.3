package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.message.SimpleMessage;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.Map;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.message.ParameterizedMessage;
import java.util.Arrays;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.apache.logging.log4j.core.util.Constants;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.core.LogEvent;

public class MutableLogEvent implements LogEvent, ReusableMessage {
    private static final Message EMPTY;
    private int threadPriority;
    private long threadId;
    private long timeMillis;
    private long nanoTime;
    private short parameterCount;
    private boolean includeLocation;
    private boolean endOfBatch;
    private Level level;
    private String threadName;
    private String loggerName;
    private Message message;
    private StringBuilder messageText;
    private Object[] parameters;
    private Throwable thrown;
    private ThrowableProxy thrownProxy;
    private StringMap contextData;
    private Marker marker;
    private String loggerFqcn;
    private StackTraceElement source;
    private ThreadContext.ContextStack contextStack;
    transient boolean reserved;
    
    public MutableLogEvent() {
        this(new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE), new Object[10]);
    }
    
    public MutableLogEvent(final StringBuilder msgText, final Object[] replacementParameters) {
        this.endOfBatch = false;
        this.contextData = ContextDataFactory.createContextData();
        this.reserved = false;
        this.messageText = msgText;
        this.parameters = replacementParameters;
    }
    
    public Log4jLogEvent toImmutable() {
        return this.createMemento();
    }
    
    public void initFrom(final LogEvent event) {
        this.loggerFqcn = event.getLoggerFqcn();
        this.marker = event.getMarker();
        this.level = event.getLevel();
        this.loggerName = event.getLoggerName();
        this.timeMillis = event.getTimeMillis();
        this.thrown = event.getThrown();
        this.thrownProxy = event.getThrownProxy();
        this.contextData.putAll(event.getContextData());
        this.contextStack = event.getContextStack();
        this.source = (event.isIncludeLocation() ? event.getSource() : null);
        this.threadId = event.getThreadId();
        this.threadName = event.getThreadName();
        this.threadPriority = event.getThreadPriority();
        this.endOfBatch = event.isEndOfBatch();
        this.includeLocation = event.isIncludeLocation();
        this.nanoTime = event.getNanoTime();
        this.setMessage(event.getMessage());
    }
    
    public void clear() {
        this.loggerFqcn = null;
        this.marker = null;
        this.level = null;
        this.loggerName = null;
        this.message = null;
        this.thrown = null;
        this.thrownProxy = null;
        this.source = null;
        if (this.contextData != null) {
            if (this.contextData.isFrozen()) {
                this.contextData = null;
            }
            else {
                this.contextData.clear();
            }
        }
        this.contextStack = null;
        this.trimMessageText();
        if (this.parameters != null) {
            for (int i = 0; i < this.parameters.length; ++i) {
                this.parameters[i] = null;
            }
        }
    }
    
    private void trimMessageText() {
        if (this.messageText != null && this.messageText.length() > Constants.MAX_REUSABLE_MESSAGE_SIZE) {
            this.messageText.setLength(Constants.MAX_REUSABLE_MESSAGE_SIZE);
            this.messageText.trimToSize();
        }
    }
    
    public String getLoggerFqcn() {
        return this.loggerFqcn;
    }
    
    public void setLoggerFqcn(final String loggerFqcn) {
        this.loggerFqcn = loggerFqcn;
    }
    
    public Marker getMarker() {
        return this.marker;
    }
    
    public void setMarker(final Marker marker) {
        this.marker = marker;
    }
    
    public Level getLevel() {
        if (this.level == null) {
            this.level = Level.OFF;
        }
        return this.level;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public String getLoggerName() {
        return this.loggerName;
    }
    
    public void setLoggerName(final String loggerName) {
        this.loggerName = loggerName;
    }
    
    public Message getMessage() {
        if (this.message == null) {
            return (this.messageText == null) ? MutableLogEvent.EMPTY : this;
        }
        return this.message;
    }
    
    public void setMessage(final Message msg) {
        if (msg instanceof ReusableMessage) {
            final ReusableMessage reusable = (ReusableMessage)msg;
            reusable.formatTo(this.getMessageTextForWriting());
            if (this.parameters != null) {
                this.parameters = reusable.swapParameters(this.parameters);
                this.parameterCount = reusable.getParameterCount();
            }
        }
        else {
            if (msg != null && !this.canFormatMessageInBackground(msg)) {
                msg.getFormattedMessage();
            }
            this.message = msg;
        }
    }
    
    private boolean canFormatMessageInBackground(final Message message) {
        return Constants.FORMAT_MESSAGES_IN_BACKGROUND || message.getClass().isAnnotationPresent((Class)AsynchronouslyFormattable.class);
    }
    
    private StringBuilder getMessageTextForWriting() {
        if (this.messageText == null) {
            this.messageText = new StringBuilder(Constants.INITIAL_REUSABLE_MESSAGE_SIZE);
        }
        this.messageText.setLength(0);
        return this.messageText;
    }
    
    public String getFormattedMessage() {
        return this.messageText.toString();
    }
    
    public String getFormat() {
        return null;
    }
    
    public Object[] getParameters() {
        return (Object[])((this.parameters == null) ? null : Arrays.copyOf(this.parameters, (int)this.parameterCount));
    }
    
    public Throwable getThrowable() {
        return this.getThrown();
    }
    
    public void formatTo(final StringBuilder buffer) {
        buffer.append((CharSequence)this.messageText);
    }
    
    public Object[] swapParameters(final Object[] emptyReplacement) {
        final Object[] result = this.parameters;
        this.parameters = emptyReplacement;
        return result;
    }
    
    public short getParameterCount() {
        return this.parameterCount;
    }
    
    public Message memento() {
        if (this.message != null) {
            return this.message;
        }
        final Object[] params = (this.parameters == null) ? new Object[0] : Arrays.copyOf(this.parameters, (int)this.parameterCount);
        return new ParameterizedMessage(this.messageText.toString(), params);
    }
    
    public Throwable getThrown() {
        return this.thrown;
    }
    
    public void setThrown(final Throwable thrown) {
        this.thrown = thrown;
    }
    
    public long getTimeMillis() {
        return this.timeMillis;
    }
    
    public void setTimeMillis(final long timeMillis) {
        this.timeMillis = timeMillis;
    }
    
    public ThrowableProxy getThrownProxy() {
        if (this.thrownProxy == null && this.thrown != null) {
            this.thrownProxy = new ThrowableProxy(this.thrown);
        }
        return this.thrownProxy;
    }
    
    public StackTraceElement getSource() {
        if (this.source != null) {
            return this.source;
        }
        if (this.loggerFqcn == null || !this.includeLocation) {
            return null;
        }
        return this.source = Log4jLogEvent.calcLocation(this.loggerFqcn);
    }
    
    public ReadOnlyStringMap getContextData() {
        return this.contextData;
    }
    
    public Map<String, String> getContextMap() {
        return this.contextData.toMap();
    }
    
    public void setContextData(final StringMap mutableContextData) {
        this.contextData = mutableContextData;
    }
    
    public ThreadContext.ContextStack getContextStack() {
        return this.contextStack;
    }
    
    public void setContextStack(final ThreadContext.ContextStack contextStack) {
        this.contextStack = contextStack;
    }
    
    public long getThreadId() {
        return this.threadId;
    }
    
    public void setThreadId(final long threadId) {
        this.threadId = threadId;
    }
    
    public String getThreadName() {
        return this.threadName;
    }
    
    public void setThreadName(final String threadName) {
        this.threadName = threadName;
    }
    
    public int getThreadPriority() {
        return this.threadPriority;
    }
    
    public void setThreadPriority(final int threadPriority) {
        this.threadPriority = threadPriority;
    }
    
    public boolean isIncludeLocation() {
        return this.includeLocation;
    }
    
    public void setIncludeLocation(final boolean includeLocation) {
        this.includeLocation = includeLocation;
    }
    
    public boolean isEndOfBatch() {
        return this.endOfBatch;
    }
    
    public void setEndOfBatch(final boolean endOfBatch) {
        this.endOfBatch = endOfBatch;
    }
    
    public long getNanoTime() {
        return this.nanoTime;
    }
    
    public void setNanoTime(final long nanoTime) {
        this.nanoTime = nanoTime;
    }
    
    protected Object writeReplace() {
        return new Log4jLogEvent.LogEventProxy(this, this.includeLocation);
    }
    
    private void readObject(final ObjectInputStream stream) throws InvalidObjectException {
        throw new InvalidObjectException("Proxy required");
    }
    
    public Log4jLogEvent createMemento() {
        return Log4jLogEvent.deserialize(Log4jLogEvent.serialize(this, this.includeLocation));
    }
    
    public void initializeBuilder(final Log4jLogEvent.Builder builder) {
        builder.setContextData(this.contextData).setContextStack(this.contextStack).setEndOfBatch(this.endOfBatch).setIncludeLocation(this.includeLocation).setLevel(this.getLevel()).setLoggerFqcn(this.loggerFqcn).setLoggerName(this.loggerName).setMarker(this.marker).setMessage(this.getNonNullImmutableMessage()).setNanoTime(this.nanoTime).setSource(this.source).setThreadId(this.threadId).setThreadName(this.threadName).setThreadPriority(this.threadPriority).setThrown(this.getThrown()).setThrownProxy(this.thrownProxy).setTimeMillis(this.timeMillis);
    }
    
    private Message getNonNullImmutableMessage() {
        return (this.message != null) ? this.message : new SimpleMessage(String.valueOf(this.messageText));
    }
    
    static {
        EMPTY = new SimpleMessage("");
    }
}
