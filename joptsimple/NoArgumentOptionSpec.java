package joptsimple;

import java.util.List;
import java.util.Collections;

class NoArgumentOptionSpec extends AbstractOptionSpec<Void> {
    NoArgumentOptionSpec(final String option) {
        this((List<String>)Collections.singletonList(option), "");
    }
    
    NoArgumentOptionSpec(final List<String> options, final String description) {
        super(options, description);
    }
    
    @Override
    void handleOption(final OptionParser parser, final ArgumentList arguments, final OptionSet detectedOptions, final String detectedArgument) {
        detectedOptions.add(this);
    }
    
    public boolean acceptsArguments() {
        return false;
    }
    
    public boolean requiresArgument() {
        return false;
    }
    
    public boolean isRequired() {
        return false;
    }
    
    public String argumentDescription() {
        return "";
    }
    
    public String argumentTypeIndicator() {
        return "";
    }
    
    @Override
    protected Void convert(final String argument) {
        return null;
    }
    
    public List<Void> defaultValues() {
        return (List<Void>)Collections.emptyList();
    }
}
