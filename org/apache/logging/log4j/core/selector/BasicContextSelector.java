package org.apache.logging.log4j.core.selector;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.net.URI;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import org.apache.logging.log4j.core.LoggerContext;

public class BasicContextSelector implements ContextSelector {
    private static final LoggerContext CONTEXT;
    
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        final LoggerContext ctx = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
        return (ctx != null) ? ctx : BasicContextSelector.CONTEXT;
    }
    
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        final LoggerContext ctx = (LoggerContext)ContextAnchor.THREAD_CONTEXT.get();
        return (ctx != null) ? ctx : BasicContextSelector.CONTEXT;
    }
    
    public LoggerContext locateContext(final String name, final String configLocation) {
        return BasicContextSelector.CONTEXT;
    }
    
    public void removeContext(final LoggerContext context) {
    }
    
    public List<LoggerContext> getLoggerContexts() {
        final List<LoggerContext> list = (List<LoggerContext>)new ArrayList();
        list.add(BasicContextSelector.CONTEXT);
        return (List<LoggerContext>)Collections.unmodifiableList((List)list);
    }
    
    static {
        CONTEXT = new LoggerContext("Default");
    }
}
