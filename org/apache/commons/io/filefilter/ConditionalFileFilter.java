package org.apache.commons.io.filefilter;

import java.util.List;

public interface ConditionalFileFilter {
    void addFileFilter(final IOFileFilter iOFileFilter);
    
    List<IOFileFilter> getFileFilters();
    
    boolean removeFileFilter(final IOFileFilter iOFileFilter);
    
    void setFileFilters(final List<IOFileFilter> list);
}
