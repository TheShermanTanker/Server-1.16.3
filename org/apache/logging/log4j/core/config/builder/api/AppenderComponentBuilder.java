package org.apache.logging.log4j.core.config.builder.api;

public interface AppenderComponentBuilder extends FilterableComponentBuilder<AppenderComponentBuilder> {
    AppenderComponentBuilder add(final LayoutComponentBuilder layoutComponentBuilder);
    
    String getName();
}
