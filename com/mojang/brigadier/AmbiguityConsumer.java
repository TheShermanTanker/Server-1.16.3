package com.mojang.brigadier;

import java.util.Collection;
import com.mojang.brigadier.tree.CommandNode;

@FunctionalInterface
public interface AmbiguityConsumer<S> {
    void ambiguous(final CommandNode<S> commandNode1, final CommandNode<S> commandNode2, final CommandNode<S> commandNode3, final Collection<String> collection);
}
