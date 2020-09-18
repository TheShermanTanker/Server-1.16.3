package org.apache.logging.log4j.core;

import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;

public interface Filter extends LifeCycle {
    public static final String ELEMENT_TYPE = "filter";
    
    Result getOnMismatch();
    
    Result getOnMatch();
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object... arr);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12, final Object object13);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final String string, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12, final Object object13, final Object object14);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final Object object, final Throwable throwable);
    
    Result filter(final Logger logger, final Level level, final Marker marker, final Message message, final Throwable throwable);
    
    Result filter(final LogEvent logEvent);
    
    public enum Result {
        ACCEPT, 
        NEUTRAL, 
        DENY;
        
        public static Result toResult(final String name) {
            return toResult(name, null);
        }
        
        public static Result toResult(final String name, final Result defaultResult) {
            return EnglishEnums.<Result>valueOf(Result.class, name, defaultResult);
        }
    }
}
