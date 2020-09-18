package com.mojang.brigadier.arguments;

import java.util.Arrays;
import com.mojang.brigadier.ImmutableStringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;

public class LongArgumentType implements ArgumentType<Long> {
    private static final Collection<String> EXAMPLES;
    private final long minimum;
    private final long maximum;
    
    private LongArgumentType(final long minimum, final long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }
    
    public static LongArgumentType longArg() {
        return longArg(Long.MIN_VALUE);
    }
    
    public static LongArgumentType longArg(final long min) {
        return longArg(min, Long.MAX_VALUE);
    }
    
    public static LongArgumentType longArg(final long min, final long max) {
        return new LongArgumentType(min, max);
    }
    
    public static long getLong(final CommandContext<?> context, final String name) {
        return context.<Long>getArgument(name, (java.lang.Class<Long>)Long.TYPE);
    }
    
    public long getMinimum() {
        return this.minimum;
    }
    
    public long getMaximum() {
        return this.maximum;
    }
    
    public Long parse(final StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();
        final long result = reader.readLong();
        if (result < this.minimum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooLow().createWithContext(reader, result, this.minimum);
        }
        if (result > this.maximum) {
            reader.setCursor(start);
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.longTooHigh().createWithContext(reader, result, this.maximum);
        }
        return result;
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LongArgumentType)) {
            return false;
        }
        final LongArgumentType that = (LongArgumentType)o;
        return this.maximum == that.maximum && this.minimum == that.minimum;
    }
    
    public int hashCode() {
        return 31 * Long.hashCode(this.minimum) + Long.hashCode(this.maximum);
    }
    
    public String toString() {
        if (this.minimum == Long.MIN_VALUE && this.maximum == Long.MAX_VALUE) {
            return "longArg()";
        }
        if (this.maximum == Long.MAX_VALUE) {
            return new StringBuilder().append("longArg(").append(this.minimum).append(")").toString();
        }
        return new StringBuilder().append("longArg(").append(this.minimum).append(", ").append(this.maximum).append(")").toString();
    }
    
    public Collection<String> getExamples() {
        return LongArgumentType.EXAMPLES;
    }
    
    static {
        EXAMPLES = (Collection)Arrays.asList((Object[])new String[] { "0", "123", "-123" });
    }
}
