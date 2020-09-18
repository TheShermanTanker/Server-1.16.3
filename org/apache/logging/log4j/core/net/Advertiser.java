package org.apache.logging.log4j.core.net;

import java.util.Map;

public interface Advertiser {
    Object advertise(final Map<String, String> map);
    
    void unadvertise(final Object object);
}
