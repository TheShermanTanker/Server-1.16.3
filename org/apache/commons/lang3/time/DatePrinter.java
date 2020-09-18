package org.apache.commons.lang3.time;

import java.text.FieldPosition;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;

public interface DatePrinter {
    String format(final long long1);
    
    String format(final Date date);
    
    String format(final Calendar calendar);
    
    @Deprecated
    StringBuffer format(final long long1, final StringBuffer stringBuffer);
    
    @Deprecated
    StringBuffer format(final Date date, final StringBuffer stringBuffer);
    
    @Deprecated
    StringBuffer format(final Calendar calendar, final StringBuffer stringBuffer);
    
     <B extends Appendable> B format(final long long1, final B appendable);
    
     <B extends Appendable> B format(final Date date, final B appendable);
    
     <B extends Appendable> B format(final Calendar calendar, final B appendable);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    StringBuffer format(final Object object, final StringBuffer stringBuffer, final FieldPosition fieldPosition);
}
