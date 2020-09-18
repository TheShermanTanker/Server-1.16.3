package org.apache.logging.log4j.core.pattern;

public interface PatternConverter {
    public static final String CATEGORY = "Converter";
    
    void format(final Object object, final StringBuilder stringBuilder);
    
    String getName();
    
    String getStyleClass(final Object object);
}
