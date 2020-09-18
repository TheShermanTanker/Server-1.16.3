package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.ParseResults;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.context.ParsedCommandNode;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class HelpCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final Map<CommandNode<CommandSourceStack>, String> map3;
        final Iterator iterator;
        String string5;
        CommandSourceStack commandSourceStack;
        final TextComponent nr;
        final ParseResults<CommandSourceStack> parseResults3;
        Map<CommandNode<CommandSourceStack>, String> map4;
        final Iterator iterator2;
        String string6;
        CommandSourceStack commandSourceStack2;
        final TextComponent nr2;
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("help").executes(commandContext -> {
            map3 = commandDispatcher.getSmartUsage(commandDispatcher.getRoot(), commandContext.getSource());
            map3.values().iterator();
            while (iterator.hasNext()) {
                string5 = (String)iterator.next();
                commandSourceStack = commandContext.getSource();
                new TextComponent("/" + string5);
                commandSourceStack.sendSuccess(nr, false);
            }
            return map3.size();
        }).then(Commands.argument("command", (ArgumentType<Object>)StringArgumentType.greedyString()).executes(commandContext -> {
            parseResults3 = commandDispatcher.parse(StringArgumentType.getString(commandContext, "command"), commandContext.getSource());
            if (parseResults3.getContext().getNodes().isEmpty()) {
                throw HelpCommand.ERROR_FAILED.create();
            }
            else {
                map4 = commandDispatcher.getSmartUsage(Iterables.<ParsedCommandNode<CommandSourceStack>>getLast((java.lang.Iterable<ParsedCommandNode<CommandSourceStack>>)parseResults3.getContext().getNodes()).getNode(), commandContext.getSource());
                map4.values().iterator();
                while (iterator2.hasNext()) {
                    string6 = (String)iterator2.next();
                    commandSourceStack2 = commandContext.getSource();
                    new TextComponent("/" + parseResults3.getReader().getString() + " " + string6);
                    commandSourceStack2.sendSuccess(nr2, false);
                }
                return map4.size();
            }
        })));
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.help.failed"));
    }
}
