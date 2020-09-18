package com.mojang.brigadier.context;

import java.util.Objects;
import com.mojang.brigadier.tree.CommandNode;

public class ParsedCommandNode<S> {
    private final CommandNode<S> node;
    private final StringRange range;
    
    public ParsedCommandNode(final CommandNode<S> node, final StringRange range) {
        this.node = node;
        this.range = range;
    }
    
    public CommandNode<S> getNode() {
        return this.node;
    }
    
    public StringRange getRange() {
        return this.range;
    }
    
    public String toString() {
        return new StringBuilder().append(this.node).append("@").append(this.range).toString();
    }
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ParsedCommandNode<?> that = o;
        return Objects.equals(this.node, that.node) && Objects.equals(this.range, that.range);
    }
    
    public int hashCode() {
        return Objects.hash(new Object[] { this.node, this.range });
    }
}
