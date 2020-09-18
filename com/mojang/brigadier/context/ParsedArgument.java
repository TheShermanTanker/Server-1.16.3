package com.mojang.brigadier.context;

import java.util.Objects;

public class ParsedArgument<S, T> {
    private final StringRange range;
    private final T result;
    
    public ParsedArgument(final int start, final int end, final T result) {
        this.range = StringRange.between(start, end);
        this.result = result;
    }
    
    public StringRange getRange() {
        return this.range;
    }
    
    public T getResult() {
        return this.result;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParsedArgument)) {
            return false;
        }
        final ParsedArgument<?, ?> that = o;
        return Objects.equals(this.range, that.range) && Objects.equals(this.result, that.result);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.range, this.result });
    }
}
