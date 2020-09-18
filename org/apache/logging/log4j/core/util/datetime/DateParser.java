package org.apache.logging.log4j.core.util.datetime;

import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.ParsePosition;
import java.text.ParseException;
import java.util.Date;

public interface DateParser {
    Date parse(final String string) throws ParseException;
    
    Date parse(final String string, final ParsePosition parsePosition);
    
    boolean parse(final String string, final ParsePosition parsePosition, final Calendar calendar);
    
    String getPattern();
    
    TimeZone getTimeZone();
    
    Locale getLocale();
    
    Object parseObject(final String string) throws ParseException;
    
    Object parseObject(final String string, final ParsePosition parsePosition);
}
