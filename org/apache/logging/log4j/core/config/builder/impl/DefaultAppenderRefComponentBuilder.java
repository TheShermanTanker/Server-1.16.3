package org.apache.logging.log4j.core.config.builder.impl;

import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.FilterComponentBuilder;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.builder.api.AppenderRefComponentBuilder;

class DefaultAppenderRefComponentBuilder extends DefaultComponentAndConfigurationBuilder<AppenderRefComponentBuilder> implements AppenderRefComponentBuilder {
    public DefaultAppenderRefComponentBuilder(final DefaultConfigurationBuilder<? extends Configuration> builder, final String ref) {
        super((DefaultConfigurationBuilder)builder, "AppenderRef");
        this.addAttribute("ref", ref);
    }
    
    public AppenderRefComponentBuilder add(final FilterComponentBuilder builder) {
        return ((DefaultComponentBuilder<AppenderRefComponentBuilder, CB>)this).addComponent(builder);
    }
}
