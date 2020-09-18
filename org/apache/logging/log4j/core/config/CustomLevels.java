package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CustomLevels", category = "Core", printObject = true)
public final class CustomLevels {
    private final List<CustomLevelConfig> customLevels;
    
    private CustomLevels(final CustomLevelConfig[] customLevels) {
        this.customLevels = (List<CustomLevelConfig>)new ArrayList((Collection)Arrays.asList((Object[])customLevels));
    }
    
    @PluginFactory
    public static CustomLevels createCustomLevels(@PluginElement("CustomLevels") final CustomLevelConfig[] customLevels) {
        return new CustomLevels((customLevels == null) ? new CustomLevelConfig[0] : customLevels);
    }
    
    public List<CustomLevelConfig> getCustomLevels() {
        return this.customLevels;
    }
}
