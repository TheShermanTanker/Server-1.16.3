package joptsimple;

import java.util.Collection;
import java.util.Iterator;
import java.util.Collections;
import java.util.Objects;
import java.util.IdentityHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class OptionSet {
    private final List<OptionSpec<?>> detectedSpecs;
    private final Map<String, AbstractOptionSpec<?>> detectedOptions;
    private final Map<AbstractOptionSpec<?>, List<String>> optionsToArguments;
    private final Map<String, AbstractOptionSpec<?>> recognizedSpecs;
    private final Map<String, List<?>> defaultValues;
    
    OptionSet(final Map<String, AbstractOptionSpec<?>> recognizedSpecs) {
        this.detectedSpecs = (List<OptionSpec<?>>)new ArrayList();
        this.detectedOptions = (Map<String, AbstractOptionSpec<?>>)new HashMap();
        this.optionsToArguments = (Map<AbstractOptionSpec<?>, List<String>>)new IdentityHashMap();
        this.defaultValues = defaultValues(recognizedSpecs);
        this.recognizedSpecs = recognizedSpecs;
    }
    
    public boolean hasOptions() {
        return this.detectedOptions.size() != 1 || !((AbstractOptionSpec)this.detectedOptions.values().iterator().next()).representsNonOptions();
    }
    
    public boolean has(final String option) {
        return this.detectedOptions.containsKey(option);
    }
    
    public boolean has(final OptionSpec<?> option) {
        return this.optionsToArguments.containsKey(option);
    }
    
    public boolean hasArgument(final String option) {
        final AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
        return spec != null && this.hasArgument(spec);
    }
    
    public boolean hasArgument(final OptionSpec<?> option) {
        Objects.requireNonNull(option);
        final List<String> values = (List<String>)this.optionsToArguments.get(option);
        return values != null && !values.isEmpty();
    }
    
    public Object valueOf(final String option) {
        Objects.requireNonNull(option);
        final AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
        if (spec == null) {
            final List<?> defaults = this.defaultValuesFor(option);
            return defaults.isEmpty() ? null : defaults.get(0);
        }
        return this.valueOf(spec);
    }
    
    public <V> V valueOf(final OptionSpec<V> option) {
        Objects.requireNonNull(option);
        final List<V> values = this.<V>valuesOf(option);
        switch (values.size()) {
            case 0: {
                return null;
            }
            case 1: {
                return (V)values.get(0);
            }
            default: {
                throw new MultipleArgumentsForOptionException(option);
            }
        }
    }
    
    public List<?> valuesOf(final String option) {
        Objects.requireNonNull(option);
        final AbstractOptionSpec<?> spec = this.detectedOptions.get(option);
        return (spec == null) ? this.defaultValuesFor(option) : this.valuesOf(spec);
    }
    
    public <V> List<V> valuesOf(final OptionSpec<V> option) {
        Objects.requireNonNull(option);
        final List<String> values = (List<String>)this.optionsToArguments.get(option);
        if (values == null || values.isEmpty()) {
            return this.defaultValueFor((OptionSpec<Object>)option);
        }
        final AbstractOptionSpec<V> spec = (AbstractOptionSpec<V>)(AbstractOptionSpec)option;
        final List<V> convertedValues = (List<V>)new ArrayList();
        for (final String each : values) {
            convertedValues.add(spec.convert(each));
        }
        return (List<V>)Collections.unmodifiableList((List)convertedValues);
    }
    
    public List<OptionSpec<?>> specs() {
        final List<OptionSpec<?>> specs = this.detectedSpecs;
        specs.removeAll((Collection)Collections.singletonList(this.detectedOptions.get("[arguments]")));
        return (List<OptionSpec<?>>)Collections.unmodifiableList((List)specs);
    }
    
    public Map<OptionSpec<?>, List<?>> asMap() {
        final Map<OptionSpec<?>, List<?>> map = (Map<OptionSpec<?>, List<?>>)new HashMap();
        for (final AbstractOptionSpec<?> spec : this.recognizedSpecs.values()) {
            if (!spec.representsNonOptions()) {
                map.put(spec, this.valuesOf(spec));
            }
        }
        return (Map<OptionSpec<?>, List<?>>)Collections.unmodifiableMap((Map)map);
    }
    
    public List<?> nonOptionArguments() {
        final AbstractOptionSpec<?> spec = this.detectedOptions.get("[arguments]");
        return this.valuesOf(spec);
    }
    
    void add(final AbstractOptionSpec<?> spec) {
        this.addWithArgument(spec, null);
    }
    
    void addWithArgument(final AbstractOptionSpec<?> spec, final String argument) {
        this.detectedSpecs.add(spec);
        for (final String each : spec.options()) {
            this.detectedOptions.put(each, spec);
        }
        List<String> optionArguments = (List<String>)this.optionsToArguments.get(spec);
        if (optionArguments == null) {
            optionArguments = (List<String>)new ArrayList();
            this.optionsToArguments.put(spec, optionArguments);
        }
        if (argument != null) {
            optionArguments.add(argument);
        }
    }
    
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || !this.getClass().equals(that.getClass())) {
            return false;
        }
        final OptionSet other = (OptionSet)that;
        final Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = (Map<AbstractOptionSpec<?>, List<String>>)new HashMap((Map)this.optionsToArguments);
        final Map<AbstractOptionSpec<?>, List<String>> otherOptionsToArguments = (Map<AbstractOptionSpec<?>, List<String>>)new HashMap((Map)other.optionsToArguments);
        return this.detectedOptions.equals(other.detectedOptions) && thisOptionsToArguments.equals(otherOptionsToArguments);
    }
    
    public int hashCode() {
        final Map<AbstractOptionSpec<?>, List<String>> thisOptionsToArguments = (Map<AbstractOptionSpec<?>, List<String>>)new HashMap((Map)this.optionsToArguments);
        return this.detectedOptions.hashCode() ^ thisOptionsToArguments.hashCode();
    }
    
    private <V> List<V> defaultValuesFor(final String option) {
        if (this.defaultValues.containsKey(option)) {
            return (List<V>)Collections.unmodifiableList((List)this.defaultValues.get(option));
        }
        return (List<V>)Collections.emptyList();
    }
    
    private <V> List<V> defaultValueFor(final OptionSpec<V> option) {
        return this.<V>defaultValuesFor((String)option.options().iterator().next());
    }
    
    private static Map<String, List<?>> defaultValues(final Map<String, AbstractOptionSpec<?>> recognizedSpecs) {
        final Map<String, List<?>> defaults = (Map<String, List<?>>)new HashMap();
        for (final Map.Entry<String, AbstractOptionSpec<?>> each : recognizedSpecs.entrySet()) {
            defaults.put(each.getKey(), ((AbstractOptionSpec)each.getValue()).defaultValues());
        }
        return defaults;
    }
}
