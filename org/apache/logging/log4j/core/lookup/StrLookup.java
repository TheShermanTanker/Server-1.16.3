package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;

public interface StrLookup {
    public static final String CATEGORY = "Lookup";
    
    String lookup(final String string);
    
    String lookup(final LogEvent logEvent, final String string);
}
