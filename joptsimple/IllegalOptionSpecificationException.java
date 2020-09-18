package joptsimple;

import java.util.List;
import java.util.Collections;

class IllegalOptionSpecificationException extends OptionException {
    private static final long serialVersionUID = -1L;
    
    IllegalOptionSpecificationException(final String option) {
        super((List<String>)Collections.singletonList(option));
    }
    
    @Override
    Object[] messageArguments() {
        return new Object[] { this.singleOptionString() };
    }
}
