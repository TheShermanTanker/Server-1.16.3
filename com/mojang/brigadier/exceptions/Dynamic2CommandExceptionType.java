package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.ImmutableStringReader;

public class Dynamic2CommandExceptionType implements CommandExceptionType {
    private final Function function;
    
    public Dynamic2CommandExceptionType(final Function function) {
        this.function = function;
    }
    
    public CommandSyntaxException create(final Object a, final Object b) {
        return new CommandSyntaxException(this, this.function.apply(a, b));
    }
    
    public CommandSyntaxException createWithContext(final ImmutableStringReader reader, final Object a, final Object b) {
        return new CommandSyntaxException(this, this.function.apply(a, b), reader.getString(), reader.getCursor());
    }
    
    public interface Function {
        Message apply(final Object object1, final Object object2);
    }
}
