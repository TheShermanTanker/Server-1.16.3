package org.apache.logging.log4j.core.layout;

import java.io.Serializable;
import java.io.IOException;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.commons.csv.CSVFormat;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "CsvLogEventLayout", category = "Core", elementType = "layout", printObject = true)
public class CsvLogEventLayout extends AbstractCsvLayout {
    public static CsvLogEventLayout createDefaultLayout() {
        return new CsvLogEventLayout(null, Charset.forName("UTF-8"), CSVFormat.valueOf("Default"), null, null);
    }
    
    public static CsvLogEventLayout createLayout(final CSVFormat format) {
        return new CsvLogEventLayout(null, Charset.forName("UTF-8"), format, null, null);
    }
    
    @PluginFactory
    public static CsvLogEventLayout createLayout(@PluginConfiguration final Configuration config, @PluginAttribute(value = "format", defaultString = "Default") final String format, @PluginAttribute("delimiter") final Character delimiter, @PluginAttribute("escape") final Character escape, @PluginAttribute("quote") final Character quote, @PluginAttribute("quoteMode") final QuoteMode quoteMode, @PluginAttribute("nullString") final String nullString, @PluginAttribute("recordSeparator") final String recordSeparator, @PluginAttribute(value = "charset", defaultString = "UTF-8") final Charset charset, @PluginAttribute("header") final String header, @PluginAttribute("footer") final String footer) {
        final CSVFormat csvFormat = AbstractCsvLayout.createFormat(format, delimiter, escape, quote, quoteMode, nullString, recordSeparator);
        return new CsvLogEventLayout(config, charset, csvFormat, header, footer);
    }
    
    protected CsvLogEventLayout(final Configuration config, final Charset charset, final CSVFormat csvFormat, final String header, final String footer) {
        super(config, charset, csvFormat, header, footer);
    }
    
    public String toSerializable(final LogEvent event) {
        final StringBuilder buffer = AbstractStringLayout.getStringBuilder();
        final CSVFormat format = this.getFormat();
        try {
            format.print(event.getNanoTime(), (Appendable)buffer, true);
            format.print(event.getTimeMillis(), (Appendable)buffer, false);
            format.print(event.getLevel(), (Appendable)buffer, false);
            format.print(event.getThreadId(), (Appendable)buffer, false);
            format.print(event.getThreadName(), (Appendable)buffer, false);
            format.print(event.getThreadPriority(), (Appendable)buffer, false);
            format.print(event.getMessage().getFormattedMessage(), (Appendable)buffer, false);
            format.print(event.getLoggerFqcn(), (Appendable)buffer, false);
            format.print(event.getLoggerName(), (Appendable)buffer, false);
            format.print(event.getMarker(), (Appendable)buffer, false);
            format.print(event.getThrownProxy(), (Appendable)buffer, false);
            format.print(event.getSource(), (Appendable)buffer, false);
            format.print(event.getContextData(), (Appendable)buffer, false);
            format.print(event.getContextStack(), (Appendable)buffer, false);
            format.println((Appendable)buffer);
            return buffer.toString();
        }
        catch (IOException e) {
            StatusLogger.getLogger().error(event.toString(), (Throwable)e);
            return new StringBuilder().append(format.getCommentMarker()).append(" ").append(e).toString();
        }
    }
}
