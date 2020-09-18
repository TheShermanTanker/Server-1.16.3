package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class KickCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("kick").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(3))).then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> kickPlayers(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), new TranslatableComponent("multiplayer.disconnect.kicked"))).then(Commands.argument("reason", (ArgumentType<Object>)MessageArgument.message()).executes(commandContext -> kickPlayers(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), MessageArgument.getMessage(commandContext, "reason"))))));
    }
    
    private static int kickPlayers(final CommandSourceStack db, final Collection<ServerPlayer> collection, final Component nr) {
        for (final ServerPlayer aah5 : collection) {
            aah5.connection.disconnect(nr);
            db.sendSuccess(new TranslatableComponent("commands.kick.success", new Object[] { aah5.getDisplayName(), nr }), true);
        }
        return collection.size();
    }
}
