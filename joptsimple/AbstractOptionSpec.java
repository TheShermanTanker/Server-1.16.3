package joptsimple;

import java.util.Iterator;
import java.util.Collection;
import joptsimple.internal.ReflectionException;
import joptsimple.internal.Reflection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractOptionSpec<V> implements OptionSpec<V>, OptionDescriptor {
    private final List<String> options;
    private final String description;
    private boolean forHelp;
    
    AbstractOptionSpec(final String option) {
        this(Collections.singletonList(option), "");
    }
    
    AbstractOptionSpec(final List<String> options, final String description) {
        this.options = (List<String>)new ArrayList();
        this.arrangeOptions(options);
        this.description = description;
    }
    
    public final List<String> options() {
        return (List<String>)Collections.unmodifiableList((List)this.options);
    }
    
    public final List<V> values(final OptionSet detectedOptions) {
        return detectedOptions.<V>valuesOf((OptionSpec<V>)this);
    }
    
    public final V value(final OptionSet detectedOptions) {
        return detectedOptions.<V>valueOf((OptionSpec<V>)this);
    }
    
    public String description() {
        return this.description;
    }
    
    public final AbstractOptionSpec<V> forHelp() {
        this.forHelp = true;
        return this;
    }
    
    public final boolean isForHelp() {
        return this.forHelp;
    }
    
    public boolean representsNonOptions() {
        return false;
    }
    
    protected abstract V convert(final String string);
    
    protected V convertWith(final ValueConverter<V> converter, final String argument) {
        try {
            return Reflection.<V>convertWith(converter, argument);
        }
        catch (ReflectionException | ValueConversionException ex3) {
            final RuntimeException ex2;
            final RuntimeException ex = ex2;
            throw new OptionArgumentConversionException(this, argument, (Throwable)ex);
        }
    }
    
    protected String argumentTypeIndicatorFrom(final ValueConverter<V> converter) {
        if (converter == null) {
            return null;
        }
        final String pattern = converter.valuePattern();
        return (pattern == null) ? converter.valueType().getName() : pattern;
    }
    
    abstract void handleOption(final OptionParser optionParser, final ArgumentList argumentList, final OptionSet optionSet, final String string);
    
    private void arrangeOptions(final List<String> unarranged) {
        if (unarranged.size() == 1) {
            this.options.addAll((Collection)unarranged);
            return;
        }
        final List<String> shortOptions = (List<String>)new ArrayList();
        final List<String> longOptions = (List<String>)new ArrayList();
        for (final String each : unarranged) {
            if (each.length() == 1) {
                shortOptions.add(each);
            }
            else {
                longOptions.add(each);
            }
        }
        Collections.sort((List)shortOptions);
        Collections.sort((List)longOptions);
        this.options.addAll((Collection)shortOptions);
        this.options.addAll((Collection)longOptions);
    }
    
    public boolean equals(final Object that) {
        if (!(that instanceof AbstractOptionSpec)) {
            return false;
        }
        final AbstractOptionSpec<?> other = that;
        return this.options.equals(other.options);
    }
    
    public int hashCode() {
        return this.options.hashCode();
    }
    
    public String toString() {
        return this.options.toString();
    }
}
