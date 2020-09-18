package org.apache.commons.lang3.text;

import java.text.Format;
import java.util.Locale;

public interface FormatFactory {
    Format getFormat(final String string1, final String string2, final Locale locale);
}
