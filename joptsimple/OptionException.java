package joptsimple;

import joptsimple.internal.Messages;
import java.util.Locale;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collections;
import joptsimple.internal.Strings;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public abstract class OptionException extends RuntimeException {
    private static final long serialVersionUID = -1L;
    private final List<String> options;
    
    protected OptionException(final List<String> options) {
        (this.options = (List<String>)new ArrayList()).addAll((Collection)options);
    }
    
    protected OptionException(final Collection<? extends OptionSpec<?>> options) {
        (this.options = (List<String>)new ArrayList()).addAll((Collection)this.specsToStrings(options));
    }
    
    protected OptionException(final Collection<? extends OptionSpec<?>> options, final Throwable cause) {
        super(cause);
        (this.options = (List<String>)new ArrayList()).addAll((Collection)this.specsToStrings(options));
    }
    
    private List<String> specsToStrings(final Collection<? extends OptionSpec<?>> options) {
        final List<String> strings = (List<String>)new ArrayList();
        for (final OptionSpec<?> each : options) {
            strings.add(this.specToString(each));
        }
        return strings;
    }
    
    private String specToString(final OptionSpec<?> option) {
        return Strings.join((Iterable<String>)new ArrayList((Collection)option.options()), "/");
    }
    
    public List<String> options() {
        return (List<String>)Collections.unmodifiableList((List)this.options);
    }
    
    protected final String singleOptionString() {
        return this.singleOptionString((String)this.options.get(0));
    }
    
    protected final String singleOptionString(final String option) {
        return option;
    }
    
    protected final String multipleOptionString() {
        final StringBuilder buffer = new StringBuilder("[");
        final Set<String> asSet = (Set<String>)new LinkedHashSet((Collection)this.options);
        final Iterator<String> iter = (Iterator<String>)asSet.iterator();
        while (iter.hasNext()) {
            buffer.append(this.singleOptionString((String)iter.next()));
            if (iter.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }
    
    static OptionException unrecognizedOption(final String option) {
        return new UnrecognizedOptionException(option);
    }
    
    public final String getMessage() {
        return this.localizedMessage(Locale.getDefault());
    }
    
    final String localizedMessage(final Locale locale) {
        return this.formattedMessage(locale);
    }
    
    private String formattedMessage(final Locale locale) {
        return Messages.message(locale, "joptsimple.ExceptionMessages", this.getClass(), "message", this.messageArguments());
    }
    
    abstract Object[] messageArguments();
}
