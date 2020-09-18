package org.apache.logging.log4j.core.util.datetime;

import java.text.FieldPosition;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

public interface DatePrinter {
    String format(final long long1);
    
    String format(final Date date);
    
    String format(final Calendar calendar);
    
     <B extends Appendable> B format(final long long1, final B appendable);
    
     <B extends Appendable> B format(final Date date, final B appendable);
    
     <B extends Appendable> B format(final Calendar calendar, final B appendable);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    StringBuilder format(final Object object, final StringBuilder stringBuilder, final FieldPosition fieldPosition);
}
