package org.apache.logging.log4j.core.jmx;

public interface LoggerConfigAdminMBean {
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s,component=Loggers,name=%s";
    
    String getName();
    
    String getLevel();
    
    void setLevel(final String string);
    
    boolean isAdditive();
    
    void setAdditive(final boolean boolean1);
    
    boolean isIncludeLocation();
    
    String getFilter();
    
    String[] getAppenderRefs();
}
