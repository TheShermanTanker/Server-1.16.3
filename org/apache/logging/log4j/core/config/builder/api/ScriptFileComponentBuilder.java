package org.apache.logging.log4j.core.config.builder.api;

public interface ScriptFileComponentBuilder extends ComponentBuilder<ScriptFileComponentBuilder> {
    ScriptFileComponentBuilder addLanguage(final String string);
    
    ScriptFileComponentBuilder addIsWatched(final boolean boolean1);
    
    ScriptFileComponentBuilder addIsWatched(final String string);
    
    ScriptFileComponentBuilder addCharset(final String string);
}
