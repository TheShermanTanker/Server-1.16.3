package com.mojang.brigadier.tree;

import java.util.Collections;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.StringReader;
import java.util.Set;
import java.util.HashSet;
import com.mojang.brigadier.AmbiguityConsumer;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.Collection;
import java.util.LinkedHashMap;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import java.util.function.Predicate;
import java.util.Map;

public abstract class CommandNode<S> implements Comparable<CommandNode<S>> {
    private Map<String, CommandNode<S>> children;
    private Map<String, LiteralCommandNode<S>> literals;
    private Map<String, ArgumentCommandNode<S, ?>> arguments;
    private final Predicate<S> requirement;
    private final CommandNode<S> redirect;
    private final RedirectModifier<S> modifier;
    private final boolean forks;
    private Command<S> command;
    
    protected CommandNode(final Command<S> command, final Predicate<S> requirement, final CommandNode<S> redirect, final RedirectModifier<S> modifier, final boolean forks) {
        this.children = (Map<String, CommandNode<S>>)new LinkedHashMap();
        this.literals = (Map<String, LiteralCommandNode<S>>)new LinkedHashMap();
        this.arguments = (Map<String, ArgumentCommandNode<S, ?>>)new LinkedHashMap();
        this.command = command;
        this.requirement = requirement;
        this.redirect = redirect;
        this.modifier = modifier;
        this.forks = forks;
    }
    
    public Command<S> getCommand() {
        return this.command;
    }
    
    public Collection<CommandNode<S>> getChildren() {
        return (Collection<CommandNode<S>>)this.children.values();
    }
    
    public CommandNode<S> getChild(final String name) {
        return (CommandNode)this.children.get(name);
    }
    
    public CommandNode<S> getRedirect() {
        return this.redirect;
    }
    
    public RedirectModifier<S> getRedirectModifier() {
        return this.modifier;
    }
    
    public boolean canUse(final S source) {
        return this.requirement.test(source);
    }
    
    public void addChild(final CommandNode<S> node) {
        if (node instanceof RootCommandNode) {
            throw new UnsupportedOperationException("Cannot add a RootCommandNode as a child to any other CommandNode");
        }
        final CommandNode<S> child = (CommandNode<S>)this.children.get(node.getName());
        if (child != null) {
            if (node.getCommand() != null) {
                child.command = node.getCommand();
            }
            for (final CommandNode<S> grandchild : node.getChildren()) {
                child.addChild(grandchild);
            }
        }
        else {
            this.children.put(node.getName(), node);
            if (node instanceof LiteralCommandNode) {
                this.literals.put(node.getName(), (LiteralCommandNode)node);
            }
            else if (node instanceof ArgumentCommandNode) {
                this.arguments.put(node.getName(), (ArgumentCommandNode)node);
            }
        }
        this.children = (Map<String, CommandNode<S>>)this.children.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
    
    public void findAmbiguities(final AmbiguityConsumer<S> consumer) {
        Set<String> matches = (Set<String>)new HashSet();
        for (final CommandNode<S> child : this.children.values()) {
            for (final CommandNode<S> sibling : this.children.values()) {
                if (child == sibling) {
                    continue;
                }
                for (final String input : child.getExamples()) {
                    if (sibling.isValidInput(input)) {
                        matches.add(input);
                    }
                }
                if (matches.size() <= 0) {
                    continue;
                }
                consumer.ambiguous(this, child, sibling, (Collection<String>)matches);
                matches = (Set<String>)new HashSet();
            }
            child.findAmbiguities(consumer);
        }
    }
    
    protected abstract boolean isValidInput(final String string);
    
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandNode)) {
            return false;
        }
        final CommandNode<S> that = (CommandNode<S>)o;
        if (!this.children.equals(that.children)) {
            return false;
        }
        if (this.command != null) {
            if (this.command.equals(that.command)) {
                return true;
            }
        }
        else if (that.command == null) {
            return true;
        }
        return false;
    }
    
    public int hashCode() {
        return 31 * this.children.hashCode() + ((this.command != null) ? this.command.hashCode() : 0);
    }
    
    public Predicate<S> getRequirement() {
        return this.requirement;
    }
    
    public abstract String getName();
    
    public abstract String getUsageText();
    
    public abstract void parse(final StringReader stringReader, final CommandContextBuilder<S> commandContextBuilder) throws CommandSyntaxException;
    
    public abstract CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException;
    
    public abstract ArgumentBuilder<S, ?> createBuilder();
    
    protected abstract String getSortedKey();
    
    public Collection<? extends CommandNode<S>> getRelevantNodes(final StringReader input) {
        if (this.literals.size() <= 0) {
            return this.arguments.values();
        }
        final int cursor = input.getCursor();
        while (input.canRead() && input.peek() != ' ') {
            input.skip();
        }
        final String text = input.getString().substring(cursor, input.getCursor());
        input.setCursor(cursor);
        final LiteralCommandNode<S> literal = (LiteralCommandNode<S>)this.literals.get(text);
        if (literal != null) {
            return Collections.singleton(literal);
        }
        return this.arguments.values();
    }
    
    public int compareTo(final CommandNode<S> o) {
        if (this instanceof LiteralCommandNode == o instanceof LiteralCommandNode) {
            return this.getSortedKey().compareTo(o.getSortedKey());
        }
        return (o instanceof LiteralCommandNode) ? 1 : -1;
    }
    
    public boolean isFork() {
        return this.forks;
    }
    
    public abstract Collection<String> getExamples();
}
