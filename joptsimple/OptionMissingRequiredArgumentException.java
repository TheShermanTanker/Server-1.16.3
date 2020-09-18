package joptsimple;

import java.util.Collection;
import java.util.Arrays;

class OptionMissingRequiredArgumentException extends OptionException {
    private static final long serialVersionUID = -1L;
    
    OptionMissingRequiredArgumentException(final OptionSpec<?> option) {
        super(Arrays.asList((Object[])new OptionSpec[] { option }));
    }
    
    @Override
    Object[] messageArguments() {
        return new Object[] { this.singleOptionString() };
    }
}
