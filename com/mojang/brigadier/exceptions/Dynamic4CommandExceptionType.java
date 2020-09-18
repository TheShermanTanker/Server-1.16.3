package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.ImmutableStringReader;

public class Dynamic4CommandExceptionType implements CommandExceptionType {
    private final Function function;
    
    public Dynamic4CommandExceptionType(final Function function) {
        this.function = function;
    }
    
    public CommandSyntaxException create(final Object a, final Object b, final Object c, final Object d) {
        return new CommandSyntaxException(this, this.function.apply(a, b, c, d));
    }
    
    public CommandSyntaxException createWithContext(final ImmutableStringReader reader, final Object a, final Object b, final Object c, final Object d) {
        return new CommandSyntaxException(this, this.function.apply(a, b, c, d), reader.getString(), reader.getCursor());
    }
    
    public interface Function {
        Message apply(final Object object1, final Object object2, final Object object3, final Object object4);
    }
}
