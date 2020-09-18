package org.apache.logging.log4j.core.jmx;

import java.util.Map;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.management.ObjectName;

public interface LoggerContextAdminMBean {
    public static final String PATTERN = "org.apache.logging.log4j2:type=%s";
    public static final String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";
    
    ObjectName getObjectName();
    
    String getStatus();
    
    String getName();
    
    String getConfigLocationUri();
    
    void setConfigLocationUri(final String string) throws URISyntaxException, IOException;
    
    String getConfigText() throws IOException;
    
    String getConfigText(final String string) throws IOException;
    
    void setConfigText(final String string1, final String string2);
    
    String getConfigName();
    
    String getConfigClassName();
    
    String getConfigFilter();
    
    Map<String, String> getConfigProperties();
}
