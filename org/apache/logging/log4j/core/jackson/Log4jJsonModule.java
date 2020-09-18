package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

class Log4jJsonModule extends SimpleModule {
    private static final long serialVersionUID = 1L;
    private final boolean encodeThreadContextAsList;
    private final boolean includeStacktrace;
    
    Log4jJsonModule(final boolean encodeThreadContextAsList, final boolean includeStacktrace) {
        super(Log4jJsonModule.class.getName(), new Version(2, 0, 0, (String)null, (String)null, (String)null));
        this.encodeThreadContextAsList = encodeThreadContextAsList;
        this.includeStacktrace = includeStacktrace;
        new Initializers.SimpleModuleInitializer().initialize(this);
    }
    
    public void setupModule(final Module.SetupContext context) {
        super.setupModule(context);
        if (this.encodeThreadContextAsList) {
            new Initializers.SetupContextInitializer().setupModule(context, this.includeStacktrace);
        }
        else {
            new Initializers.SetupContextJsonInitializer().setupModule(context, this.includeStacktrace);
        }
    }
}
