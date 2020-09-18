package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.util.Strings;
import java.util.Locale;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "java", category = "Lookup")
public class JavaLookup extends AbstractLookup {
    private final SystemPropertiesLookup spLookup;
    
    public JavaLookup() {
        this.spLookup = new SystemPropertiesLookup();
    }
    
    public String getHardware() {
        return new StringBuilder().append("processors: ").append(Runtime.getRuntime().availableProcessors()).append(", architecture: ").append(this.getSystemProperty("os.arch")).append(this.getSystemProperty("-", "sun.arch.data.model")).append(this.getSystemProperty(", instruction sets: ", "sun.cpu.isalist")).toString();
    }
    
    public String getLocale() {
        return new StringBuilder().append("default locale: ").append(Locale.getDefault()).append(", platform encoding: ").append(this.getSystemProperty("file.encoding")).toString();
    }
    
    public String getOperatingSystem() {
        return this.getSystemProperty("os.name") + " " + this.getSystemProperty("os.version") + this.getSystemProperty(" ", "sun.os.patch.level") + ", architecture: " + this.getSystemProperty("os.arch") + this.getSystemProperty("-", "sun.arch.data.model");
    }
    
    public String getRuntime() {
        return this.getSystemProperty("java.runtime.name") + " (build " + this.getSystemProperty("java.runtime.version") + ") from " + this.getSystemProperty("java.vendor");
    }
    
    private String getSystemProperty(final String name) {
        return this.spLookup.lookup(name);
    }
    
    private String getSystemProperty(final String prefix, final String name) {
        final String value = this.getSystemProperty(name);
        if (Strings.isEmpty((CharSequence)value)) {
            return "";
        }
        return prefix + value;
    }
    
    public String getVirtualMachine() {
        return this.getSystemProperty("java.vm.name") + " (build " + this.getSystemProperty("java.vm.version") + ", " + this.getSystemProperty("java.vm.info") + ")";
    }
    
    public String lookup(final LogEvent event, final String key) {
        switch (key) {
            case "version": {
                return "Java version " + this.getSystemProperty("java.version");
            }
            case "runtime": {
                return this.getRuntime();
            }
            case "vm": {
                return this.getVirtualMachine();
            }
            case "os": {
                return this.getOperatingSystem();
            }
            case "hw": {
                return this.getHardware();
            }
            case "locale": {
                return this.getLocale();
            }
            default: {
                throw new IllegalArgumentException(key);
            }
        }
    }
}
