package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public interface ExtendedLogger extends Logger {
    boolean isEnabled(final Level level, final Marker marker, final Message message, final Throwable throwable);
    
    boolean isEnabled(final Level level, final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    boolean isEnabled(final Level level, final Marker marker, final Object object, final Throwable throwable);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Throwable throwable);
    
    boolean isEnabled(final Level level, final Marker marker, final String string);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object... arr);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    boolean isEnabled(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12, final Object object13);
    
    void logIfEnabled(final String string, final Level level, final Marker marker, final Message message, final Throwable throwable);
    
    void logIfEnabled(final String string, final Level level, final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void logIfEnabled(final String string, final Level level, final Marker marker, final Object object, final Throwable throwable);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Throwable throwable);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object... arr);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12, final Object object13);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12, final Object object13, final Object object14);
    
    void logMessage(final String string, final Level level, final Marker marker, final Message message, final Throwable throwable);
    
    void logIfEnabled(final String string, final Level level, final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void logIfEnabled(final String string1, final Level level, final Marker marker, final String string4, final Supplier<?>... arr);
    
    void logIfEnabled(final String string, final Level level, final Marker marker, final Supplier<?> supplier, final Throwable throwable);
}
