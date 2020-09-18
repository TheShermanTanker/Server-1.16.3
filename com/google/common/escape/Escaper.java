package com.google.common.escape;

import com.google.common.base.Function;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public abstract class Escaper {
    private final Function<String, String> asFunction;
    
    protected Escaper() {
        this.asFunction = new Function<String, String>() {
            public String apply(final String from) {
                return Escaper.this.escape(from);
            }
        };
    }
    
    public abstract String escape(final String string);
    
    public final Function<String, String> asFunction() {
        return this.asFunction;
    }
}
