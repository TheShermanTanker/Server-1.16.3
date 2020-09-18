package org.apache.logging.log4j.core.selector;

import org.apache.logging.log4j.core.LoggerContext;
import java.net.URI;

public interface NamedContextSelector extends ContextSelector {
    LoggerContext locateContext(final String string, final Object object, final URI uRI);
    
    LoggerContext removeContext(final String string);
}
