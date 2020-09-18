package org.apache.logging.log4j.core.jmx;

import java.util.List;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.Level;
import java.util.Objects;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.LoggerContext;

public class LoggerConfigAdmin implements LoggerConfigAdminMBean {
    private final LoggerContext loggerContext;
    private final LoggerConfig loggerConfig;
    private final ObjectName objectName;
    
    public LoggerConfigAdmin(final LoggerContext loggerContext, final LoggerConfig loggerConfig) {
        this.loggerContext = (LoggerContext)Objects.requireNonNull(loggerContext, "loggerContext");
        this.loggerConfig = (LoggerConfig)Objects.requireNonNull(loggerConfig, "loggerConfig");
        try {
            final String ctxName = Server.escape(loggerContext.getName());
            final String configName = Server.escape(loggerConfig.getName());
            final String name = String.format("org.apache.logging.log4j2:type=%s,component=Loggers,name=%s", new Object[] { ctxName, configName });
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
        return this.loggerConfig.getName();
    }
    
    public String getLevel() {
        return this.loggerConfig.getLevel().name();
    }
    
    public void setLevel(final String level) {
        this.loggerConfig.setLevel(Level.getLevel(level));
        this.loggerContext.updateLoggers();
    }
    
    public boolean isAdditive() {
        return this.loggerConfig.isAdditive();
    }
    
    public void setAdditive(final boolean additive) {
        this.loggerConfig.setAdditive(additive);
        this.loggerContext.updateLoggers();
    }
    
    public boolean isIncludeLocation() {
        return this.loggerConfig.isIncludeLocation();
    }
    
    public String getFilter() {
        return String.valueOf(this.loggerConfig.getFilter());
    }
    
    public String[] getAppenderRefs() {
        final List<AppenderRef> refs = this.loggerConfig.getAppenderRefs();
        final String[] result = new String[refs.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = ((AppenderRef)refs.get(i)).getRef();
        }
        return result;
    }
}
