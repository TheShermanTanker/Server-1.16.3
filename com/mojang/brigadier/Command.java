package com.mojang.brigadier;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;

@FunctionalInterface
public interface Command<S> {
    public static final int SINGLE_SUCCESS = 1;
    
    int run(final CommandContext<S> commandContext) throws CommandSyntaxException;
}
