package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.world.entity.Entity;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.arguments.MessageArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class MsgCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralCommandNode<CommandSourceStack> literalCommandNode2 = commandDispatcher.register(Commands.literal("msg").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("message", (ArgumentType<Object>)MessageArgument.message()).executes(commandContext -> sendMessage(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), MessageArgument.getMessage(commandContext, "message"))))));
        commandDispatcher.register(Commands.literal("tell").redirect((CommandNode<CommandSourceStack>)literalCommandNode2));
        commandDispatcher.register(Commands.literal("w").redirect((CommandNode<CommandSourceStack>)literalCommandNode2));
    }
    
    private static int sendMessage(final CommandSourceStack db, final Collection<ServerPlayer> collection, final Component nr) {
        final UUID uUID4 = (db.getEntity() == null) ? Util.NIL_UUID : db.getEntity().getUUID();
        final Entity apx6 = db.getEntity();
        Consumer<Component> consumer5;
        if (apx6 instanceof ServerPlayer) {
            final ServerPlayer aah7 = (ServerPlayer)apx6;
            consumer5 = (Consumer<Component>)(nr3 -> aah7.sendMessage(new TranslatableComponent("commands.message.display.outgoing", new Object[] { nr3, nr }).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), aah7.getUUID()));
        }
        else {
            consumer5 = (Consumer<Component>)(nr3 -> db.sendSuccess(new TranslatableComponent("commands.message.display.outgoing", new Object[] { nr3, nr }).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), false));
        }
        for (final ServerPlayer aah8 : collection) {
            consumer5.accept(aah8.getDisplayName());
            aah8.sendMessage(new TranslatableComponent("commands.message.display.incoming", new Object[] { db.getDisplayName(), nr }).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC), uUID4);
        }
        return collection.size();
    }
}
