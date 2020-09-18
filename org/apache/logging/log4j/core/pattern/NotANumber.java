package org.apache.logging.log4j.core.pattern;

public final class NotANumber {
    public static final NotANumber NAN;
    public static final String VALUE = "\u0000";
    
    private NotANumber() {
    }
    
    public String toString() {
        return "\u0000";
    }
    
    static {
        NAN = new NotANumber();
    }
}
