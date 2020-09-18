package com.mojang.brigadier;

import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;
import com.mojang.brigadier.context.CommandContextBuilder;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collections;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import java.util.function.Predicate;
import com.mojang.brigadier.tree.RootCommandNode;

public class CommandDispatcher<S> {
    public static final String ARGUMENT_SEPARATOR = " ";
    public static final char ARGUMENT_SEPARATOR_CHAR = ' ';
    private static final String USAGE_OPTIONAL_OPEN = "[";
    private static final String USAGE_OPTIONAL_CLOSE = "]";
    private static final String USAGE_REQUIRED_OPEN = "(";
    private static final String USAGE_REQUIRED_CLOSE = ")";
    private static final String USAGE_OR = "|";
    private final RootCommandNode<S> root;
    private final Predicate<CommandNode<S>> hasCommand;
    private ResultConsumer<S> consumer;
    
    public CommandDispatcher(final RootCommandNode<S> root) {
        this.hasCommand = (Predicate<CommandNode<S>>)new Predicate<CommandNode<S>>() {
            public boolean test(final CommandNode<S> input) {
                return input != null && (input.getCommand() != null || input.getChildren().stream().anyMatch(CommandDispatcher.this.hasCommand));
            }
        };
        this.consumer = ((c, s, r) -> {});
        this.root = root;
    }
    
    public CommandDispatcher() {
        this((RootCommandNode)new RootCommandNode());
    }
    
    public LiteralCommandNode<S> register(final LiteralArgumentBuilder<S> command) {
        final LiteralCommandNode<S> build = command.build();
        this.root.addChild(build);
        return build;
    }
    
    public void setConsumer(final ResultConsumer<S> consumer) {
        this.consumer = consumer;
    }
    
    public int execute(final String input, final S source) throws CommandSyntaxException {
        return this.execute(new StringReader(input), source);
    }
    
    public int execute(final StringReader input, final S source) throws CommandSyntaxException {
        final ParseResults<S> parse = this.parse(input, source);
        return this.execute(parse);
    }
    
    public int execute(final ParseResults<S> parse) throws CommandSyntaxException {
        if (parse.getReader().canRead()) {
            if (parse.getExceptions().size() == 1) {
                throw (CommandSyntaxException)parse.getExceptions().values().iterator().next();
            }
            if (parse.getContext().getRange().isEmpty()) {
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            }
            throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().createWithContext(parse.getReader());
        }
        else {
            int result = 0;
            int successfulForks = 0;
            boolean forked = false;
            boolean foundCommand = false;
            final String command = parse.getReader().getString();
            final CommandContext<S> original = parse.getContext().build(command);
            List<CommandContext<S>> contexts = (List<CommandContext<S>>)Collections.singletonList(original);
            for (ArrayList<CommandContext<S>> next = null; contexts != null; contexts = (List<CommandContext<S>>)next, next = null) {
                for (int size = contexts.size(), i = 0; i < size; ++i) {
                    final CommandContext<S> context = (CommandContext<S>)contexts.get(i);
                    final CommandContext<S> child = context.getChild();
                    if (child != null) {
                        forked |= context.isForked();
                        if (child.hasNodes()) {
                            foundCommand = true;
                            final RedirectModifier<S> modifier = context.getRedirectModifier();
                            if (modifier == null) {
                                if (next == null) {
                                    next = (ArrayList<CommandContext<S>>)new ArrayList(1);
                                }
                                next.add(child.copyFor(context.getSource()));
                            }
                            else {
                                try {
                                    final Collection<S> results = modifier.apply(context);
                                    if (!results.isEmpty()) {
                                        if (next == null) {
                                            next = (ArrayList<CommandContext<S>>)new ArrayList(results.size());
                                        }
                                        for (final S source : results) {
                                            next.add(child.copyFor(source));
                                        }
                                    }
                                }
                                catch (CommandSyntaxException ex) {
                                    this.consumer.onCommandComplete(context, false, 0);
                                    if (!forked) {
                                        throw ex;
                                    }
                                }
                            }
                        }
                    }
                    else if (context.getCommand() != null) {
                        foundCommand = true;
                        try {
                            final int value = context.getCommand().run(context);
                            result += value;
                            this.consumer.onCommandComplete(context, true, value);
                            ++successfulForks;
                        }
                        catch (CommandSyntaxException ex2) {
                            this.consumer.onCommandComplete(context, false, 0);
                            if (!forked) {
                                throw ex2;
                            }
                        }
                    }
                }
            }
            if (!foundCommand) {
                this.consumer.onCommandComplete(original, false, 0);
                throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownCommand().createWithContext(parse.getReader());
            }
            return forked ? successfulForks : result;
        }
    }
    
    public ParseResults<S> parse(final String command, final S source) {
        return this.parse(new StringReader(command), source);
    }
    
    public ParseResults<S> parse(final StringReader command, final S source) {
        final CommandContextBuilder<S> context = new CommandContextBuilder<S>(this, source, this.root, command.getCursor());
        return this.parseNodes(this.root, command, context);
    }
    
    private ParseResults<S> parseNodes(final CommandNode<S> node, final StringReader originalReader, final CommandContextBuilder<S> contextSoFar) {
        final S source = contextSoFar.getSource();
        Map<CommandNode<S>, CommandSyntaxException> errors = null;
        List<ParseResults<S>> potentials = null;
        final int cursor = originalReader.getCursor();
        for (final CommandNode<S> child : node.getRelevantNodes(originalReader)) {
            if (!child.canUse(source)) {
                continue;
            }
            final CommandContextBuilder<S> context = contextSoFar.copy();
            final StringReader reader = new StringReader(originalReader);
            try {
                try {
                    child.parse(reader, context);
                }
                catch (RuntimeException ex) {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherParseException().createWithContext(reader, ex.getMessage());
                }
                if (reader.canRead() && reader.peek() != ' ') {
                    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherExpectedArgumentSeparator().createWithContext(reader);
                }
            }
            catch (CommandSyntaxException ex2) {
                if (errors == null) {
                    errors = (Map<CommandNode<S>, CommandSyntaxException>)new LinkedHashMap();
                }
                errors.put(child, ex2);
                reader.setCursor(cursor);
                continue;
            }
            context.withCommand(child.getCommand());
            if (reader.canRead((child.getRedirect() == null) ? 2 : 1)) {
                reader.skip();
                if (child.getRedirect() != null) {
                    final CommandContextBuilder<S> childContext = new CommandContextBuilder<S>(this, source, child.getRedirect(), reader.getCursor());
                    final ParseResults<S> parse = this.parseNodes(child.getRedirect(), reader, childContext);
                    context.withChild(parse.getContext());
                    return new ParseResults<S>(context, parse.getReader(), parse.getExceptions());
                }
                final ParseResults<S> parse2 = this.parseNodes(child, reader, context);
                if (potentials == null) {
                    potentials = (List<ParseResults<S>>)new ArrayList(1);
                }
                potentials.add(parse2);
            }
            else {
                if (potentials == null) {
                    potentials = (List<ParseResults<S>>)new ArrayList(1);
                }
                potentials.add(new ParseResults((CommandContextBuilder<Object>)context, reader, (java.util.Map<CommandNode<Object>, CommandSyntaxException>)Collections.emptyMap()));
            }
        }
        if (potentials != null) {
            if (potentials.size() > 1) {
                potentials.sort((a, b) -> {
                    if (!a.getReader().canRead() && b.getReader().canRead()) {
                        return -1;
                    }
                    if (a.getReader().canRead() && !b.getReader().canRead()) {
                        return 1;
                    }
                    if (a.getExceptions().isEmpty() && !b.getExceptions().isEmpty()) {
                        return -1;
                    }
                    if (!a.getExceptions().isEmpty() && b.getExceptions().isEmpty()) {
                        return 1;
                    }
                    return 0;
                });
            }
            return (ParseResults<S>)potentials.get(0);
        }
        return new ParseResults<S>(contextSoFar, originalReader, (java.util.Map<CommandNode<S>, CommandSyntaxException>)((errors == null) ? Collections.emptyMap() : errors));
    }
    
    public String[] getAllUsage(final CommandNode<S> node, final S source, final boolean restricted) {
        final ArrayList<String> result = (ArrayList<String>)new ArrayList();
        this.getAllUsage(node, source, result, "", restricted);
        return (String[])result.toArray((Object[])new String[result.size()]);
    }
    
    private void getAllUsage(final CommandNode<S> node, final S source, final ArrayList<String> result, final String prefix, final boolean restricted) {
        if (restricted && !node.canUse(source)) {
            return;
        }
        if (node.getCommand() != null) {
            result.add(prefix);
        }
        if (node.getRedirect() != null) {
            final String redirect = (node.getRedirect() == this.root) ? "..." : ("-> " + node.getRedirect().getUsageText());
            result.add((prefix.isEmpty() ? (node.getUsageText() + " " + redirect) : (prefix + " " + redirect)));
        }
        else if (!node.getChildren().isEmpty()) {
            for (final CommandNode<S> child : node.getChildren()) {
                this.getAllUsage(child, source, result, prefix.isEmpty() ? child.getUsageText() : (prefix + " " + child.getUsageText()), restricted);
            }
        }
    }
    
    public Map<CommandNode<S>, String> getSmartUsage(final CommandNode<S> node, final S source) {
        final Map<CommandNode<S>, String> result = (Map<CommandNode<S>, String>)new LinkedHashMap();
        final boolean optional = node.getCommand() != null;
        for (final CommandNode<S> child : node.getChildren()) {
            final String usage = this.getSmartUsage(child, source, optional, false);
            if (usage != null) {
                result.put(child, usage);
            }
        }
        return result;
    }
    
    private String getSmartUsage(final CommandNode<S> node, final S source, final boolean optional, final boolean deep) {
        if (!node.canUse(source)) {
            return null;
        }
        final String self = optional ? ("[" + node.getUsageText() + "]") : node.getUsageText();
        final boolean childOptional = node.getCommand() != null;
        final String open = childOptional ? "[" : "(";
        final String close = childOptional ? "]" : ")";
        if (!deep) {
            if (node.getRedirect() != null) {
                final String redirect = (node.getRedirect() == this.root) ? "..." : ("-> " + node.getRedirect().getUsageText());
                return self + " " + redirect;
            }
            final Collection<CommandNode<S>> children = (Collection<CommandNode<S>>)node.getChildren().stream().filter(c -> c.canUse(source)).collect(Collectors.toList());
            if (children.size() == 1) {
                final String usage = this.getSmartUsage((CommandNode<S>)children.iterator().next(), source, childOptional, childOptional);
                if (usage != null) {
                    return self + " " + usage;
                }
            }
            else if (children.size() > 1) {
                final Set<String> childUsage = (Set<String>)new LinkedHashSet();
                for (final CommandNode<S> child : children) {
                    final String usage2 = this.getSmartUsage(child, source, childOptional, true);
                    if (usage2 != null) {
                        childUsage.add(usage2);
                    }
                }
                if (childUsage.size() == 1) {
                    final String usage3 = (String)childUsage.iterator().next();
                    return self + " " + (childOptional ? ("[" + usage3 + "]") : usage3);
                }
                if (childUsage.size() > 1) {
                    final StringBuilder builder = new StringBuilder(open);
                    int count = 0;
                    for (final CommandNode<S> child2 : children) {
                        if (count > 0) {
                            builder.append("|");
                        }
                        builder.append(child2.getUsageText());
                        ++count;
                    }
                    if (count > 0) {
                        builder.append(close);
                        return self + " " + builder.toString();
                    }
                }
            }
        }
        return self;
    }
    
    public CompletableFuture<Suggestions> getCompletionSuggestions(final ParseResults<S> parse) {
        return this.getCompletionSuggestions(parse, parse.getReader().getTotalLength());
    }
    
    public CompletableFuture<Suggestions> getCompletionSuggestions(final ParseResults<S> parse, final int cursor) {
        final CommandContextBuilder<S> context = parse.getContext();
        final SuggestionContext<S> nodeBeforeCursor = context.findSuggestionContext(cursor);
        final CommandNode<S> parent = nodeBeforeCursor.parent;
        final int start = Math.min(nodeBeforeCursor.startPos, cursor);
        final String fullInput = parse.getReader().getString();
        final String truncatedInput = fullInput.substring(0, cursor);
        final CompletableFuture<Suggestions>[] futures = new CompletableFuture[parent.getChildren().size()];
        int i = 0;
        for (final CommandNode<S> node : parent.getChildren()) {
            CompletableFuture<Suggestions> future = Suggestions.empty();
            try {
                future = node.listSuggestions(context.build(truncatedInput), new SuggestionsBuilder(truncatedInput, start));
            }
            catch (CommandSyntaxException ex) {}
            futures[i++] = future;
        }
        final CompletableFuture<Suggestions> result = (CompletableFuture<Suggestions>)new CompletableFuture();
        CompletableFuture.allOf((CompletableFuture[])futures).thenRun(() -> {
            final List<Suggestions> suggestions = (List<Suggestions>)new ArrayList();
            for (final CompletableFuture<Suggestions> future : futures) {
                suggestions.add(future.join());
            }
            result.complete(Suggestions.merge(fullInput, (Collection<Suggestions>)suggestions));
        });
        return result;
    }
    
    public RootCommandNode<S> getRoot() {
        return this.root;
    }
    
    public Collection<String> getPath(final CommandNode<S> target) {
        final List<List<CommandNode<S>>> nodes = (List<List<CommandNode<S>>>)new ArrayList();
        this.addPaths(this.root, nodes, (List<CommandNode<S>>)new ArrayList());
        for (final List<CommandNode<S>> list : nodes) {
            if (list.get(list.size() - 1) == target) {
                final List<String> result = (List<String>)new ArrayList(list.size());
                for (final CommandNode<S> node : list) {
                    if (node != this.root) {
                        result.add(node.getName());
                    }
                }
                return (Collection<String>)result;
            }
        }
        return (Collection<String>)Collections.emptyList();
    }
    
    public CommandNode<S> findNode(final Collection<String> path) {
        CommandNode<S> node = this.root;
        for (final String name : path) {
            node = node.getChild(name);
            if (node == null) {
                return null;
            }
        }
        return node;
    }
    
    public void findAmbiguities(final AmbiguityConsumer<S> consumer) {
        this.root.findAmbiguities(consumer);
    }
    
    private void addPaths(final CommandNode<S> node, final List<List<CommandNode<S>>> result, final List<CommandNode<S>> parents) {
        final List<CommandNode<S>> current = (List<CommandNode<S>>)new ArrayList((Collection)parents);
        current.add(node);
        result.add(current);
        for (final CommandNode<S> child : node.getChildren()) {
            this.addPaths(child, result, current);
        }
    }
}
