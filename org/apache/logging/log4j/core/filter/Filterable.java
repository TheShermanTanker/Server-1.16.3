package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LifeCycle;

public interface Filterable extends LifeCycle {
    void addFilter(final Filter filter);
    
    void removeFilter(final Filter filter);
    
    Filter getFilter();
    
    boolean hasFilter();
    
    boolean isFiltered(final LogEvent logEvent);
}
