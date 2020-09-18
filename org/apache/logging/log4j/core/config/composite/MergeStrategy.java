package org.apache.logging.log4j.core.config.composite;

import org.apache.logging.log4j.core.config.plugins.util.PluginManager;
import org.apache.logging.log4j.core.config.AbstractConfiguration;
import org.apache.logging.log4j.core.config.Node;

public interface MergeStrategy {
    void mergeRootProperties(final Node node, final AbstractConfiguration abstractConfiguration);
    
    void mergConfigurations(final Node node1, final Node node2, final PluginManager pluginManager);
}
