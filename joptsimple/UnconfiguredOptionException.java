package joptsimple;

import java.util.List;
import java.util.Collections;

class UnconfiguredOptionException extends OptionException {
    private static final long serialVersionUID = -1L;
    
    UnconfiguredOptionException(final String option) {
        this((List<String>)Collections.singletonList(option));
    }
    
    UnconfiguredOptionException(final List<String> options) {
        super(options);
    }
    
    @Override
    Object[] messageArguments() {
        return new Object[] { this.multipleOptionString() };
    }
}
