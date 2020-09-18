package joptsimple;

import java.util.Map;

public interface HelpFormatter {
    String format(final Map<String, ? extends OptionDescriptor> map);
}
