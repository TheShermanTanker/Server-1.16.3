package org.apache.commons.lang3.exception;

import org.apache.commons.lang3.tuple.Pair;
import java.util.Set;
import java.util.List;

public interface ExceptionContext {
    ExceptionContext addContextValue(final String string, final Object object);
    
    ExceptionContext setContextValue(final String string, final Object object);
    
    List<Object> getContextValues(final String string);
    
    Object getFirstContextValue(final String string);
    
    Set<String> getContextLabels();
    
    List<Pair<String, Object>> getContextEntries();
    
    String getFormattedExceptionMessage(final String string);
}
