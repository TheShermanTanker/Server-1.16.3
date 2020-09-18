package org.apache.logging.log4j.core.appender.rolling.action;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.nio.file.FileSystems;
import java.nio.file.PathMatcher;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "IfFileName", category = "Core", printObject = true)
public final class IfFileName implements PathCondition {
    private static final Logger LOGGER;
    private final PathMatcher pathMatcher;
    private final String syntaxAndPattern;
    private final PathCondition[] nestedConditions;
    
    private IfFileName(final String glob, final String regex, final PathCondition[] nestedConditions) {
        if (regex == null && glob == null) {
            throw new IllegalArgumentException("Specify either a path glob or a regular expression. Both cannot be null.");
        }
        this.syntaxAndPattern = createSyntaxAndPatternString(glob, regex);
        this.pathMatcher = FileSystems.getDefault().getPathMatcher(this.syntaxAndPattern);
        this.nestedConditions = (PathCondition[])((nestedConditions == null) ? new PathCondition[0] : Arrays.copyOf((Object[])nestedConditions, nestedConditions.length));
    }
    
    static String createSyntaxAndPatternString(final String glob, final String regex) {
        if (glob != null) {
            return glob.startsWith("glob:") ? glob : ("glob:" + glob);
        }
        return regex.startsWith("regex:") ? regex : ("regex:" + regex);
    }
    
    public String getSyntaxAndPattern() {
        return this.syntaxAndPattern;
    }
    
    public List<PathCondition> getNestedConditions() {
        return (List<PathCondition>)Collections.unmodifiableList(Arrays.asList((Object[])this.nestedConditions));
    }
    
    public boolean accept(final Path basePath, final Path relativePath, final BasicFileAttributes attrs) {
        final boolean result = this.pathMatcher.matches(relativePath);
        final String match = result ? "matches" : "does not match";
        final String accept = result ? "ACCEPTED" : "REJECTED";
        IfFileName.LOGGER.trace("IfFileName {}: '{}' {} relative path '{}'", accept, this.syntaxAndPattern, match, relativePath);
        if (result) {
            return IfAll.accept(this.nestedConditions, basePath, relativePath, attrs);
        }
        return result;
    }
    
    public void beforeFileTreeWalk() {
        IfAll.beforeFileTreeWalk(this.nestedConditions);
    }
    
    @PluginFactory
    public static IfFileName createNameCondition(@PluginAttribute("glob") final String glob, @PluginAttribute("regex") final String regex, @PluginElement("PathConditions") final PathCondition... nestedConditions) {
        return new IfFileName(glob, regex, nestedConditions);
    }
    
    public String toString() {
        final String nested = (this.nestedConditions.length == 0) ? "" : (" AND " + Arrays.toString((Object[])this.nestedConditions));
        return "IfFileName(" + this.syntaxAndPattern + nested + ")";
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
