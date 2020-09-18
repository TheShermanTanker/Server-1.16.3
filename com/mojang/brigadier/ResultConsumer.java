package com.mojang.brigadier;

import com.mojang.brigadier.context.CommandContext;

@FunctionalInterface
public interface ResultConsumer<S> {
    void onCommandComplete(final CommandContext<S> commandContext, final boolean boolean2, final int integer);
}
