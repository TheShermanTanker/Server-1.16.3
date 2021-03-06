package org.apache.commons.lang3.exception;

import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class ContextedRuntimeException extends RuntimeException implements ExceptionContext {
    private static final long serialVersionUID = 20110706L;
    private final ExceptionContext exceptionContext;
    
    public ContextedRuntimeException() {
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedRuntimeException(final String message) {
        super(message);
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedRuntimeException(final Throwable cause) {
        super(cause);
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
        this.exceptionContext = new DefaultExceptionContext();
    }
    
    public ContextedRuntimeException(final String message, final Throwable cause, ExceptionContext context) {
        super(message, cause);
        if (context == null) {
            context = new DefaultExceptionContext();
        }
        this.exceptionContext = context;
    }
    
    public ContextedRuntimeException addContextValue(final String label, final Object value) {
        this.exceptionContext.addContextValue(label, value);
        return this;
    }
    
    public ContextedRuntimeException setContextValue(final String label, final Object value) {
        this.exceptionContext.setContextValue(label, value);
        return this;
    }
    
    public List<Object> getContextValues(final String label) {
        return this.exceptionContext.getContextValues(label);
    }
    
    public Object getFirstContextValue(final String label) {
        return this.exceptionContext.getFirstContextValue(label);
    }
    
    public List<Pair<String, Object>> getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }
    
    public Set<String> getContextLabels() {
        return this.exceptionContext.getContextLabels();
    }
    
    public String getMessage() {
        return this.getFormattedExceptionMessage(super.getMessage());
    }
    
    public String getRawMessage() {
        return super.getMessage();
    }
    
    public String getFormattedExceptionMessage(final String baseMessage) {
        return this.exceptionContext.getFormattedExceptionMessage(baseMessage);
    }
}
