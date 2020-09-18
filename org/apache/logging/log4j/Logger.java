package org.apache.logging.log4j;

import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.message.Message;

public interface Logger {
    void catching(final Level level, final Throwable throwable);
    
    void catching(final Throwable throwable);
    
    void debug(final Marker marker, final Message message);
    
    void debug(final Marker marker, final Message message, final Throwable throwable);
    
    void debug(final Marker marker, final MessageSupplier messageSupplier);
    
    void debug(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void debug(final Marker marker, final CharSequence charSequence);
    
    void debug(final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void debug(final Marker marker, final Object object);
    
    void debug(final Marker marker, final Object object, final Throwable throwable);
    
    void debug(final Marker marker, final String string);
    
    void debug(final Marker marker, final String string, final Object... arr);
    
    void debug(final Marker marker, final String string, final Supplier<?>... arr);
    
    void debug(final Marker marker, final String string, final Throwable throwable);
    
    void debug(final Marker marker, final Supplier<?> supplier);
    
    void debug(final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void debug(final Message message);
    
    void debug(final Message message, final Throwable throwable);
    
    void debug(final MessageSupplier messageSupplier);
    
    void debug(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void debug(final CharSequence charSequence);
    
    void debug(final CharSequence charSequence, final Throwable throwable);
    
    void debug(final Object object);
    
    void debug(final Object object, final Throwable throwable);
    
    void debug(final String string);
    
    void debug(final String string, final Object... arr);
    
    void debug(final String string, final Supplier<?>... arr);
    
    void debug(final String string, final Throwable throwable);
    
    void debug(final Supplier<?> supplier);
    
    void debug(final Supplier<?> supplier, final Throwable throwable);
    
    void debug(final Marker marker, final String string, final Object object);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void debug(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void debug(final String string, final Object object);
    
    void debug(final String string, final Object object2, final Object object3);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void debug(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    @Deprecated
    void entry();
    
    void entry(final Object... arr);
    
    void error(final Marker marker, final Message message);
    
    void error(final Marker marker, final Message message, final Throwable throwable);
    
    void error(final Marker marker, final MessageSupplier messageSupplier);
    
    void error(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void error(final Marker marker, final CharSequence charSequence);
    
    void error(final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void error(final Marker marker, final Object object);
    
    void error(final Marker marker, final Object object, final Throwable throwable);
    
    void error(final Marker marker, final String string);
    
    void error(final Marker marker, final String string, final Object... arr);
    
    void error(final Marker marker, final String string, final Supplier<?>... arr);
    
    void error(final Marker marker, final String string, final Throwable throwable);
    
    void error(final Marker marker, final Supplier<?> supplier);
    
    void error(final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void error(final Message message);
    
    void error(final Message message, final Throwable throwable);
    
    void error(final MessageSupplier messageSupplier);
    
    void error(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void error(final CharSequence charSequence);
    
    void error(final CharSequence charSequence, final Throwable throwable);
    
    void error(final Object object);
    
    void error(final Object object, final Throwable throwable);
    
    void error(final String string);
    
    void error(final String string, final Object... arr);
    
    void error(final String string, final Supplier<?>... arr);
    
    void error(final String string, final Throwable throwable);
    
    void error(final Supplier<?> supplier);
    
    void error(final Supplier<?> supplier, final Throwable throwable);
    
    void error(final Marker marker, final String string, final Object object);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void error(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void error(final String string, final Object object);
    
    void error(final String string, final Object object2, final Object object3);
    
    void error(final String string, final Object object2, final Object object3, final Object object4);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void error(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    @Deprecated
    void exit();
    
    @Deprecated
     <R> R exit(final R object);
    
    void fatal(final Marker marker, final Message message);
    
    void fatal(final Marker marker, final Message message, final Throwable throwable);
    
    void fatal(final Marker marker, final MessageSupplier messageSupplier);
    
    void fatal(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void fatal(final Marker marker, final CharSequence charSequence);
    
    void fatal(final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void fatal(final Marker marker, final Object object);
    
    void fatal(final Marker marker, final Object object, final Throwable throwable);
    
    void fatal(final Marker marker, final String string);
    
    void fatal(final Marker marker, final String string, final Object... arr);
    
    void fatal(final Marker marker, final String string, final Supplier<?>... arr);
    
    void fatal(final Marker marker, final String string, final Throwable throwable);
    
    void fatal(final Marker marker, final Supplier<?> supplier);
    
    void fatal(final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void fatal(final Message message);
    
    void fatal(final Message message, final Throwable throwable);
    
    void fatal(final MessageSupplier messageSupplier);
    
    void fatal(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void fatal(final CharSequence charSequence);
    
    void fatal(final CharSequence charSequence, final Throwable throwable);
    
    void fatal(final Object object);
    
    void fatal(final Object object, final Throwable throwable);
    
    void fatal(final String string);
    
    void fatal(final String string, final Object... arr);
    
    void fatal(final String string, final Supplier<?>... arr);
    
    void fatal(final String string, final Throwable throwable);
    
    void fatal(final Supplier<?> supplier);
    
    void fatal(final Supplier<?> supplier, final Throwable throwable);
    
    void fatal(final Marker marker, final String string, final Object object);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void fatal(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void fatal(final String string, final Object object);
    
    void fatal(final String string, final Object object2, final Object object3);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void fatal(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    Level getLevel();
    
     <MF extends MessageFactory> MF getMessageFactory();
    
    String getName();
    
    void info(final Marker marker, final Message message);
    
    void info(final Marker marker, final Message message, final Throwable throwable);
    
    void info(final Marker marker, final MessageSupplier messageSupplier);
    
    void info(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void info(final Marker marker, final CharSequence charSequence);
    
    void info(final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void info(final Marker marker, final Object object);
    
    void info(final Marker marker, final Object object, final Throwable throwable);
    
    void info(final Marker marker, final String string);
    
    void info(final Marker marker, final String string, final Object... arr);
    
    void info(final Marker marker, final String string, final Supplier<?>... arr);
    
    void info(final Marker marker, final String string, final Throwable throwable);
    
    void info(final Marker marker, final Supplier<?> supplier);
    
    void info(final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void info(final Message message);
    
    void info(final Message message, final Throwable throwable);
    
    void info(final MessageSupplier messageSupplier);
    
    void info(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void info(final CharSequence charSequence);
    
    void info(final CharSequence charSequence, final Throwable throwable);
    
    void info(final Object object);
    
    void info(final Object object, final Throwable throwable);
    
    void info(final String string);
    
    void info(final String string, final Object... arr);
    
    void info(final String string, final Supplier<?>... arr);
    
    void info(final String string, final Throwable throwable);
    
    void info(final Supplier<?> supplier);
    
    void info(final Supplier<?> supplier, final Throwable throwable);
    
    void info(final Marker marker, final String string, final Object object);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void info(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void info(final String string, final Object object);
    
    void info(final String string, final Object object2, final Object object3);
    
    void info(final String string, final Object object2, final Object object3, final Object object4);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void info(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    boolean isDebugEnabled();
    
    boolean isDebugEnabled(final Marker marker);
    
    boolean isEnabled(final Level level);
    
    boolean isEnabled(final Level level, final Marker marker);
    
    boolean isErrorEnabled();
    
    boolean isErrorEnabled(final Marker marker);
    
    boolean isFatalEnabled();
    
    boolean isFatalEnabled(final Marker marker);
    
    boolean isInfoEnabled();
    
    boolean isInfoEnabled(final Marker marker);
    
    boolean isTraceEnabled();
    
    boolean isTraceEnabled(final Marker marker);
    
    boolean isWarnEnabled();
    
    boolean isWarnEnabled(final Marker marker);
    
    void log(final Level level, final Marker marker, final Message message);
    
    void log(final Level level, final Marker marker, final Message message, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final MessageSupplier messageSupplier);
    
    void log(final Level level, final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final CharSequence charSequence);
    
    void log(final Level level, final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final Object object);
    
    void log(final Level level, final Marker marker, final Object object, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final String string);
    
    void log(final Level level, final Marker marker, final String string, final Object... arr);
    
    void log(final Level level, final Marker marker, final String string, final Supplier<?>... arr);
    
    void log(final Level level, final Marker marker, final String string, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final Supplier<?> supplier);
    
    void log(final Level level, final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void log(final Level level, final Message message);
    
    void log(final Level level, final Message message, final Throwable throwable);
    
    void log(final Level level, final MessageSupplier messageSupplier);
    
    void log(final Level level, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void log(final Level level, final CharSequence charSequence);
    
    void log(final Level level, final CharSequence charSequence, final Throwable throwable);
    
    void log(final Level level, final Object object);
    
    void log(final Level level, final Object object, final Throwable throwable);
    
    void log(final Level level, final String string);
    
    void log(final Level level, final String string, final Object... arr);
    
    void log(final Level level, final String string, final Supplier<?>... arr);
    
    void log(final Level level, final String string, final Throwable throwable);
    
    void log(final Level level, final Supplier<?> supplier);
    
    void log(final Level level, final Supplier<?> supplier, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final String string, final Object object);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void log(final Level level, final Marker marker, final String string, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12, final Object object13);
    
    void log(final Level level, final String string, final Object object);
    
    void log(final Level level, final String string, final Object object3, final Object object4);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void log(final Level level, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void printf(final Level level, final Marker marker, final String string, final Object... arr);
    
    void printf(final Level level, final String string, final Object... arr);
    
     <T extends Throwable> T throwing(final Level level, final T throwable);
    
     <T extends Throwable> T throwing(final T throwable);
    
    void trace(final Marker marker, final Message message);
    
    void trace(final Marker marker, final Message message, final Throwable throwable);
    
    void trace(final Marker marker, final MessageSupplier messageSupplier);
    
    void trace(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void trace(final Marker marker, final CharSequence charSequence);
    
    void trace(final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void trace(final Marker marker, final Object object);
    
    void trace(final Marker marker, final Object object, final Throwable throwable);
    
    void trace(final Marker marker, final String string);
    
    void trace(final Marker marker, final String string, final Object... arr);
    
    void trace(final Marker marker, final String string, final Supplier<?>... arr);
    
    void trace(final Marker marker, final String string, final Throwable throwable);
    
    void trace(final Marker marker, final Supplier<?> supplier);
    
    void trace(final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void trace(final Message message);
    
    void trace(final Message message, final Throwable throwable);
    
    void trace(final MessageSupplier messageSupplier);
    
    void trace(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void trace(final CharSequence charSequence);
    
    void trace(final CharSequence charSequence, final Throwable throwable);
    
    void trace(final Object object);
    
    void trace(final Object object, final Throwable throwable);
    
    void trace(final String string);
    
    void trace(final String string, final Object... arr);
    
    void trace(final String string, final Supplier<?>... arr);
    
    void trace(final String string, final Throwable throwable);
    
    void trace(final Supplier<?> supplier);
    
    void trace(final Supplier<?> supplier, final Throwable throwable);
    
    void trace(final Marker marker, final String string, final Object object);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void trace(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void trace(final String string, final Object object);
    
    void trace(final String string, final Object object2, final Object object3);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void trace(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    EntryMessage traceEntry();
    
    EntryMessage traceEntry(final String string, final Object... arr);
    
    EntryMessage traceEntry(final Supplier<?>... arr);
    
    EntryMessage traceEntry(final String string, final Supplier<?>... arr);
    
    EntryMessage traceEntry(final Message message);
    
    void traceExit();
    
     <R> R traceExit(final R object);
    
     <R> R traceExit(final String string, final R object);
    
    void traceExit(final EntryMessage entryMessage);
    
     <R> R traceExit(final EntryMessage entryMessage, final R object);
    
     <R> R traceExit(final Message message, final R object);
    
    void warn(final Marker marker, final Message message);
    
    void warn(final Marker marker, final Message message, final Throwable throwable);
    
    void warn(final Marker marker, final MessageSupplier messageSupplier);
    
    void warn(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void warn(final Marker marker, final CharSequence charSequence);
    
    void warn(final Marker marker, final CharSequence charSequence, final Throwable throwable);
    
    void warn(final Marker marker, final Object object);
    
    void warn(final Marker marker, final Object object, final Throwable throwable);
    
    void warn(final Marker marker, final String string);
    
    void warn(final Marker marker, final String string, final Object... arr);
    
    void warn(final Marker marker, final String string, final Supplier<?>... arr);
    
    void warn(final Marker marker, final String string, final Throwable throwable);
    
    void warn(final Marker marker, final Supplier<?> supplier);
    
    void warn(final Marker marker, final Supplier<?> supplier, final Throwable throwable);
    
    void warn(final Message message);
    
    void warn(final Message message, final Throwable throwable);
    
    void warn(final MessageSupplier messageSupplier);
    
    void warn(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void warn(final CharSequence charSequence);
    
    void warn(final CharSequence charSequence, final Throwable throwable);
    
    void warn(final Object object);
    
    void warn(final Object object, final Throwable throwable);
    
    void warn(final String string);
    
    void warn(final String string, final Object... arr);
    
    void warn(final String string, final Supplier<?>... arr);
    
    void warn(final String string, final Throwable throwable);
    
    void warn(final Supplier<?> supplier);
    
    void warn(final Supplier<?> supplier, final Throwable throwable);
    
    void warn(final Marker marker, final String string, final Object object);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
    
    void warn(final Marker marker, final String string, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11, final Object object12);
    
    void warn(final String string, final Object object);
    
    void warn(final String string, final Object object2, final Object object3);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10);
    
    void warn(final String string, final Object object2, final Object object3, final Object object4, final Object object5, final Object object6, final Object object7, final Object object8, final Object object9, final Object object10, final Object object11);
}
