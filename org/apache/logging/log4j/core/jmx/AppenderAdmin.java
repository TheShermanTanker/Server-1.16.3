package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.core.filter.AbstractFilterable;
import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.Appender;

public class AppenderAdmin implements AppenderAdminMBean {
    private final String contextName;
    private final Appender appender;
    private final ObjectName objectName;
    
    public AppenderAdmin(final String contextName, final Appender appender) {
        this.contextName = (String)Objects.requireNonNull(contextName, "contextName");
        this.appender = (Appender)Objects.requireNonNull(appender, "appender");
        try {
            final String ctxName = Server.escape(this.contextName);
            final String configName = Server.escape(appender.getName());
            final String name = String.format("org.apache.logging.log4j2:type=%s,component=Appenders,name=%s", new Object[] { ctxName, configName });
            this.objectName = new ObjectName(name);
        }
        catch (Exception e) {
            throw new IllegalStateException((Throwable)e);
        }
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    public String getName() {
        return this.appender.getName();
    }
    
    public String getLayout() {
        return String.valueOf(this.appender.getLayout());
    }
    
    public boolean isIgnoreExceptions() {
        return this.appender.ignoreExceptions();
    }
    
    public String getErrorHandler() {
        return String.valueOf(this.appender.getHandler());
    }
    
    public String getFilter() {
        if (this.appender instanceof AbstractFilterable) {
            return String.valueOf(((AbstractFilterable)this.appender).getFilter());
        }
        return null;
    }
}
