package org.apache.logging.log4j.core.util.datetime;

import java.util.concurrent.ConcurrentHashMap;
import java.io.ObjectInputStream;
import java.io.IOException;
import org.apache.logging.log4j.core.util.Throwables;
import java.util.Calendar;
import java.util.Date;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.Locale;
import java.util.TimeZone;
import java.io.Serializable;

public class FastDatePrinter implements DatePrinter, Serializable {
    private static final long serialVersionUID = 1L;
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private final String mPattern;
    private final TimeZone mTimeZone;
    private final Locale mLocale;
    private transient Rule[] mRules;
    private transient int mMaxLengthEstimate;
    private static final int MAX_DIGITS = 10;
    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache;
    
    protected FastDatePrinter(final String pattern, final TimeZone timeZone, final Locale locale) {
        this.mPattern = pattern;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        this.init();
    }
    
    private void init() {
        final List<Rule> rulesList = this.parsePattern();
        this.mRules = (Rule[])rulesList.toArray((Object[])new Rule[rulesList.size()]);
        int len = 0;
        int i = this.mRules.length;
        while (--i >= 0) {
            len += this.mRules[i].estimateLength();
        }
        this.mMaxLengthEstimate = len;
    }
    
    protected List<Rule> parsePattern() {
        final DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
        final List<Rule> rules = (List<Rule>)new ArrayList();
        final String[] ERAs = symbols.getEras();
        final String[] months = symbols.getMonths();
        final String[] shortMonths = symbols.getShortMonths();
        final String[] weekdays = symbols.getWeekdays();
        final String[] shortWeekdays = symbols.getShortWeekdays();
        final String[] AmPmStrings = symbols.getAmPmStrings();
        final int length = this.mPattern.length();
        final int[] indexRef = { 0 };
        for (int i = 0; i < length; ++i) {
            indexRef[0] = i;
            final String token = this.parseToken(this.mPattern, indexRef);
            i = indexRef[0];
            final int tokenLen = token.length();
            if (tokenLen == 0) {
                break;
            }
            final char c = token.charAt(0);
            Rule rule = null;
            switch (c) {
                case 'G': {
                    rule = new TextField(0, ERAs);
                    break;
                }
                case 'Y':
                case 'y': {
                    if (tokenLen == 2) {
                        rule = TwoDigitYearField.INSTANCE;
                    }
                    else {
                        rule = this.selectNumberRule(1, (tokenLen < 4) ? 4 : tokenLen);
                    }
                    if (c == 'Y') {
                        rule = new WeekYear((NumberRule)rule);
                        break;
                    }
                    break;
                }
                case 'M': {
                    if (tokenLen >= 4) {
                        rule = new TextField(2, months);
                        break;
                    }
                    if (tokenLen == 3) {
                        rule = new TextField(2, shortMonths);
                        break;
                    }
                    if (tokenLen == 2) {
                        rule = TwoDigitMonthField.INSTANCE;
                        break;
                    }
                    rule = UnpaddedMonthField.INSTANCE;
                    break;
                }
                case 'd': {
                    rule = this.selectNumberRule(5, tokenLen);
                    break;
                }
                case 'h': {
                    rule = new TwelveHourField(this.selectNumberRule(10, tokenLen));
                    break;
                }
                case 'H': {
                    rule = this.selectNumberRule(11, tokenLen);
                    break;
                }
                case 'm': {
                    rule = this.selectNumberRule(12, tokenLen);
                    break;
                }
                case 's': {
                    rule = this.selectNumberRule(13, tokenLen);
                    break;
                }
                case 'S': {
                    rule = this.selectNumberRule(14, tokenLen);
                    break;
                }
                case 'E': {
                    rule = new TextField(7, (tokenLen < 4) ? shortWeekdays : weekdays);
                    break;
                }
                case 'u': {
                    rule = new DayInWeekField(this.selectNumberRule(7, tokenLen));
                    break;
                }
                case 'D': {
                    rule = this.selectNumberRule(6, tokenLen);
                    break;
                }
                case 'F': {
                    rule = this.selectNumberRule(8, tokenLen);
                    break;
                }
                case 'w': {
                    rule = this.selectNumberRule(3, tokenLen);
                    break;
                }
                case 'W': {
                    rule = this.selectNumberRule(4, tokenLen);
                    break;
                }
                case 'a': {
                    rule = new TextField(9, AmPmStrings);
                    break;
                }
                case 'k': {
                    rule = new TwentyFourHourField(this.selectNumberRule(11, tokenLen));
                    break;
                }
                case 'K': {
                    rule = this.selectNumberRule(10, tokenLen);
                    break;
                }
                case 'X': {
                    rule = Iso8601_Rule.getRule(tokenLen);
                    break;
                }
                case 'z': {
                    if (tokenLen >= 4) {
                        rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                        break;
                    }
                    rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                    break;
                }
                case 'Z': {
                    if (tokenLen == 1) {
                        rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                        break;
                    }
                    if (tokenLen == 2) {
                        rule = Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
                        break;
                    }
                    rule = TimeZoneNumberRule.INSTANCE_COLON;
                    break;
                }
                case '\'': {
                    final String sub = token.substring(1);
                    if (sub.length() == 1) {
                        rule = new CharacterLiteral(sub.charAt(0));
                        break;
                    }
                    rule = new StringLiteral(sub);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Illegal pattern component: " + token);
                }
            }
            rules.add(rule);
        }
        return rules;
    }
    
    protected String parseToken(final String pattern, final int[] indexRef) {
        final StringBuilder buf = new StringBuilder();
        int i = indexRef[0];
        final int length = pattern.length();
        char c = pattern.charAt(i);
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
            buf.append(c);
            while (i + 1 < length) {
                final char peek = pattern.charAt(i + 1);
                if (peek != c) {
                    break;
                }
                buf.append(c);
                ++i;
            }
        }
        else {
            buf.append('\'');
            boolean inLiteral = false;
            while (i < length) {
                c = pattern.charAt(i);
                if (c == '\'') {
                    if (i + 1 < length && pattern.charAt(i + 1) == '\'') {
                        ++i;
                        buf.append(c);
                    }
                    else {
                        inLiteral = !inLiteral;
                    }
                }
                else {
                    if (!inLiteral && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                        --i;
                        break;
                    }
                    buf.append(c);
                }
                ++i;
            }
        }
        indexRef[0] = i;
        return buf.toString();
    }
    
    protected NumberRule selectNumberRule(final int field, final int padding) {
        switch (padding) {
            case 1: {
                return new UnpaddedNumberField(field);
            }
            case 2: {
                return new TwoDigitNumberField(field);
            }
            default: {
                return new PaddedNumberField(field, padding);
            }
        }
    }
    
    @Deprecated
    public StringBuilder format(final Object obj, final StringBuilder toAppendTo, final FieldPosition pos) {
        if (obj instanceof Date) {
            return this.<StringBuilder>format((Date)obj, toAppendTo);
        }
        if (obj instanceof Calendar) {
            return this.<StringBuilder>format((Calendar)obj, toAppendTo);
        }
        if (obj instanceof Long) {
            return this.<StringBuilder>format((long)obj, toAppendTo);
        }
        throw new IllegalArgumentException(new StringBuilder().append("Unknown class: ").append((obj == null) ? "<null>" : obj.getClass().getName()).toString());
    }
    
    String format(final Object obj) {
        if (obj instanceof Date) {
            return this.format((Date)obj);
        }
        if (obj instanceof Calendar) {
            return this.format((Calendar)obj);
        }
        if (obj instanceof Long) {
            return this.format((long)obj);
        }
        throw new IllegalArgumentException(new StringBuilder().append("Unknown class: ").append((obj == null) ? "<null>" : obj.getClass().getName()).toString());
    }
    
    public String format(final long millis) {
        final Calendar c = this.newCalendar();
        c.setTimeInMillis(millis);
        return this.applyRulesToString(c);
    }
    
    private String applyRulesToString(final Calendar c) {
        return this.<StringBuilder>applyRules(c, new StringBuilder(this.mMaxLengthEstimate)).toString();
    }
    
    private Calendar newCalendar() {
        return Calendar.getInstance(this.mTimeZone, this.mLocale);
    }
    
    public String format(final Date date) {
        final Calendar c = this.newCalendar();
        c.setTime(date);
        return this.applyRulesToString(c);
    }
    
    public String format(final Calendar calendar) {
        return this.<StringBuilder>format(calendar, new StringBuilder(this.mMaxLengthEstimate)).toString();
    }
    
    public <B extends Appendable> B format(final long millis, final B buf) {
        final Calendar c = this.newCalendar();
        c.setTimeInMillis(millis);
        return this.<B>applyRules(c, buf);
    }
    
    public <B extends Appendable> B format(final Date date, final B buf) {
        final Calendar c = this.newCalendar();
        c.setTime(date);
        return this.<B>applyRules(c, buf);
    }
    
    public <B extends Appendable> B format(Calendar calendar, final B buf) {
        if (!calendar.getTimeZone().equals(this.mTimeZone)) {
            calendar = (Calendar)calendar.clone();
            calendar.setTimeZone(this.mTimeZone);
        }
        return (B)this.<Appendable>applyRules(calendar, (Appendable)buf);
    }
    
    @Deprecated
    protected StringBuffer applyRules(final Calendar calendar, final StringBuffer buf) {
        return this.<StringBuffer>applyRules(calendar, buf);
    }
    
    private <B extends Appendable> B applyRules(final Calendar calendar, final B buf) {
        try {
            for (final Rule rule : this.mRules) {
                rule.appendTo(buf, calendar);
            }
        }
        catch (IOException ioe) {
            Throwables.rethrow((Throwable)ioe);
        }
        return buf;
    }
    
    public String getPattern() {
        return this.mPattern;
    }
    
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }
    
    public Locale getLocale() {
        return this.mLocale;
    }
    
    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }
    
    public boolean equals(final Object obj) {
        if (!(obj instanceof FastDatePrinter)) {
            return false;
        }
        final FastDatePrinter other = (FastDatePrinter)obj;
        return this.mPattern.equals(other.mPattern) && this.mTimeZone.equals(other.mTimeZone) && this.mLocale.equals(other.mLocale);
    }
    
    public int hashCode() {
        return this.mPattern.hashCode() + 13 * (this.mTimeZone.hashCode() + 13 * this.mLocale.hashCode());
    }
    
    public String toString() {
        return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.init();
    }
    
    private static void appendDigits(final Appendable buffer, final int value) throws IOException {
        buffer.append((char)(value / 10 + 48));
        buffer.append((char)(value % 10 + 48));
    }
    
    private static void appendFullDigits(final Appendable buffer, int value, int minFieldWidth) throws IOException {
        if (value < 10000) {
            int nDigits = 4;
            if (value < 1000) {
                --nDigits;
                if (value < 100) {
                    --nDigits;
                    if (value < 10) {
                        --nDigits;
                    }
                }
            }
            for (int i = minFieldWidth - nDigits; i > 0; --i) {
                buffer.append('0');
            }
            Label_0190: {
                switch (nDigits) {
                    case 4: {
                        buffer.append((char)(value / 1000 + 48));
                        value %= 1000;
                    }
                    case 3: {
                        if (value >= 100) {
                            buffer.append((char)(value / 100 + 48));
                            value %= 100;
                            break Label_0190;
                        }
                        buffer.append('0');
                        break Label_0190;
                    }
                    case 2: {
                        if (value >= 10) {
                            buffer.append((char)(value / 10 + 48));
                            value %= 10;
                            break Label_0190;
                        }
                        buffer.append('0');
                        break Label_0190;
                    }
                    case 1: {
                        buffer.append((char)(value + 48));
                        break;
                    }
                }
            }
        }
        else {
            final char[] work = new char[10];
            int digit = 0;
            while (value != 0) {
                work[digit++] = (char)(value % 10 + 48);
                value /= 10;
            }
            while (digit < minFieldWidth) {
                buffer.append('0');
                --minFieldWidth;
            }
            while (--digit >= 0) {
                buffer.append(work[digit]);
            }
        }
    }
    
    static String getTimeZoneDisplay(final TimeZone tz, final boolean daylight, final int style, final Locale locale) {
        final TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = (String)FastDatePrinter.cTimeZoneDisplayCache.get(key);
        if (value == null) {
            value = tz.getDisplayName(daylight, style, locale);
            final String prior = (String)FastDatePrinter.cTimeZoneDisplayCache.putIfAbsent(key, value);
            if (prior != null) {
                value = prior;
            }
        }
        return value;
    }
    
    static {
        cTimeZoneDisplayCache = (ConcurrentMap)new ConcurrentHashMap(7);
    }
    
    private static class CharacterLiteral implements Rule {
        private final char mValue;
        
        CharacterLiteral(final char value) {
            this.mValue = value;
        }
        
        public int estimateLength() {
            return 1;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            buffer.append(this.mValue);
        }
    }
    
    private static class StringLiteral implements Rule {
        private final String mValue;
        
        StringLiteral(final String value) {
            this.mValue = value;
        }
        
        public int estimateLength() {
            return this.mValue.length();
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            buffer.append((CharSequence)this.mValue);
        }
    }
    
    private static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;
        
        TextField(final int field, final String[] values) {
            this.mField = field;
            this.mValues = values;
        }
        
        public int estimateLength() {
            int max = 0;
            int i = this.mValues.length;
            while (--i >= 0) {
                final int len = this.mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
            return max;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            buffer.append((CharSequence)this.mValues[calendar.get(this.mField)]);
        }
    }
    
    private static class UnpaddedNumberField implements NumberRule {
        private final int mField;
        
        UnpaddedNumberField(final int field) {
            this.mField = field;
        }
        
        public int estimateLength() {
            return 4;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(this.mField));
        }
        
        public final void appendTo(final Appendable buffer, final int value) throws IOException {
            if (value < 10) {
                buffer.append((char)(value + 48));
            }
            else if (value < 100) {
                appendDigits(buffer, value);
            }
            else {
                appendFullDigits(buffer, value, 1);
            }
        }
    }
    
    private static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE;
        
        UnpaddedMonthField() {
        }
        
        public int estimateLength() {
            return 2;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(2) + 1);
        }
        
        public final void appendTo(final Appendable buffer, final int value) throws IOException {
            if (value < 10) {
                buffer.append((char)(value + 48));
            }
            else {
                appendDigits(buffer, value);
            }
        }
        
        static {
            INSTANCE = new UnpaddedMonthField();
        }
    }
    
    private static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;
        
        PaddedNumberField(final int field, final int size) {
            if (size < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = field;
            this.mSize = size;
        }
        
        public int estimateLength() {
            return this.mSize;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(this.mField));
        }
        
        public final void appendTo(final Appendable buffer, final int value) throws IOException {
            appendFullDigits(buffer, value, this.mSize);
        }
    }
    
    private static class TwoDigitNumberField implements NumberRule {
        private final int mField;
        
        TwoDigitNumberField(final int field) {
            this.mField = field;
        }
        
        public int estimateLength() {
            return 2;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(this.mField));
        }
        
        public final void appendTo(final Appendable buffer, final int value) throws IOException {
            if (value < 100) {
                appendDigits(buffer, value);
            }
            else {
                appendFullDigits(buffer, value, 2);
            }
        }
    }
    
    private static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE;
        
        TwoDigitYearField() {
        }
        
        public int estimateLength() {
            return 2;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(1) % 100);
        }
        
        public final void appendTo(final Appendable buffer, final int value) throws IOException {
            appendDigits(buffer, value);
        }
        
        static {
            INSTANCE = new TwoDigitYearField();
        }
    }
    
    private static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE;
        
        TwoDigitMonthField() {
        }
        
        public int estimateLength() {
            return 2;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.appendTo(buffer, calendar.get(2) + 1);
        }
        
        public final void appendTo(final Appendable buffer, final int value) throws IOException {
            appendDigits(buffer, value);
        }
        
        static {
            INSTANCE = new TwoDigitMonthField();
        }
    }
    
    private static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;
        
        TwelveHourField(final NumberRule rule) {
            this.mRule = rule;
        }
        
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            int value = calendar.get(10);
            if (value == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }
        
        public void appendTo(final Appendable buffer, final int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }
    
    private static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;
        
        TwentyFourHourField(final NumberRule rule) {
            this.mRule = rule;
        }
        
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            int value = calendar.get(11);
            if (value == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }
        
        public void appendTo(final Appendable buffer, final int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }
    
    private static class DayInWeekField implements NumberRule {
        private final NumberRule mRule;
        
        DayInWeekField(final NumberRule rule) {
            this.mRule = rule;
        }
        
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            final int value = calendar.get(7);
            this.mRule.appendTo(buffer, (value != 1) ? (value - 1) : 7);
        }
        
        public void appendTo(final Appendable buffer, final int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }
    
    private static class WeekYear implements NumberRule {
        private final NumberRule mRule;
        
        WeekYear(final NumberRule rule) {
            this.mRule = rule;
        }
        
        public int estimateLength() {
            return this.mRule.estimateLength();
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            this.mRule.appendTo(buffer, calendar.getWeekYear());
        }
        
        public void appendTo(final Appendable buffer, final int value) throws IOException {
            this.mRule.appendTo(buffer, value);
        }
    }
    
    private static class TimeZoneNameRule implements Rule {
        private final Locale mLocale;
        private final int mStyle;
        private final String mStandard;
        private final String mDaylight;
        
        TimeZoneNameRule(final TimeZone timeZone, final Locale locale, final int style) {
            this.mLocale = locale;
            this.mStyle = style;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
        }
        
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            final TimeZone zone = calendar.getTimeZone();
            if (calendar.get(16) != 0) {
                buffer.append((CharSequence)FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
            }
            else {
                buffer.append((CharSequence)FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
            }
        }
    }
    
    private static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON;
        static final TimeZoneNumberRule INSTANCE_NO_COLON;
        final boolean mColon;
        
        TimeZoneNumberRule(final boolean colon) {
            this.mColon = colon;
        }
        
        public int estimateLength() {
            return 5;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            }
            else {
                buffer.append('+');
            }
            final int hours = offset / 3600000;
            appendDigits(buffer, hours);
            if (this.mColon) {
                buffer.append(':');
            }
            final int minutes = offset / 60000 - 60 * hours;
            appendDigits(buffer, minutes);
        }
        
        static {
            INSTANCE_COLON = new TimeZoneNumberRule(true);
            INSTANCE_NO_COLON = new TimeZoneNumberRule(false);
        }
    }
    
    private static class Iso8601_Rule implements Rule {
        static final Iso8601_Rule ISO8601_HOURS;
        static final Iso8601_Rule ISO8601_HOURS_MINUTES;
        static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES;
        final int length;
        
        static Iso8601_Rule getRule(final int tokenLen) {
            switch (tokenLen) {
                case 1: {
                    return Iso8601_Rule.ISO8601_HOURS;
                }
                case 2: {
                    return Iso8601_Rule.ISO8601_HOURS_MINUTES;
                }
                case 3: {
                    return Iso8601_Rule.ISO8601_HOURS_COLON_MINUTES;
                }
                default: {
                    throw new IllegalArgumentException("invalid number of X");
                }
            }
        }
        
        Iso8601_Rule(final int length) {
            this.length = length;
        }
        
        public int estimateLength() {
            return this.length;
        }
        
        public void appendTo(final Appendable buffer, final Calendar calendar) throws IOException {
            int offset = calendar.get(15) + calendar.get(16);
            if (offset == 0) {
                buffer.append("Z");
                return;
            }
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            }
            else {
                buffer.append('+');
            }
            final int hours = offset / 3600000;
            appendDigits(buffer, hours);
            if (this.length < 5) {
                return;
            }
            if (this.length == 6) {
                buffer.append(':');
            }
            final int minutes = offset / 60000 - 60 * hours;
            appendDigits(buffer, minutes);
        }
        
        static {
            ISO8601_HOURS = new Iso8601_Rule(3);
            ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
            ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
        }
    }
    
    private static class TimeZoneDisplayKey {
        private final TimeZone mTimeZone;
        private final int mStyle;
        private final Locale mLocale;
        
        TimeZoneDisplayKey(final TimeZone timeZone, final boolean daylight, final int style, final Locale locale) {
            this.mTimeZone = timeZone;
            if (daylight) {
                this.mStyle = (style | Integer.MIN_VALUE);
            }
            else {
                this.mStyle = style;
            }
            this.mLocale = locale;
        }
        
        public int hashCode() {
            return (this.mStyle * 31 + this.mLocale.hashCode()) * 31 + this.mTimeZone.hashCode();
        }
        
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof TimeZoneDisplayKey) {
                final TimeZoneDisplayKey other = (TimeZoneDisplayKey)obj;
                return this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale);
            }
            return false;
        }
    }
    
    private interface Rule {
        int estimateLength();
        
        void appendTo(final Appendable appendable, final Calendar calendar) throws IOException;
    }
    
    private interface NumberRule extends Rule {
        void appendTo(final Appendable appendable, final int integer) throws IOException;
    }
}
