package org.apache.logging.log4j.core.config.builder.api;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.util.Builder;

public interface ComponentBuilder<T extends ComponentBuilder<T>> extends Builder<Component> {
    T addAttribute(final String string1, final String string2);
    
    T addAttribute(final String string, final Level level);
    
    T addAttribute(final String string, final Enum<?> enum2);
    
    T addAttribute(final String string, final int integer);
    
    T addAttribute(final String string, final boolean boolean2);
    
    T addAttribute(final String string, final Object object);
    
    T addComponent(final ComponentBuilder<?> componentBuilder);
    
    String getName();
    
    ConfigurationBuilder<? extends Configuration> getBuilder();
}
