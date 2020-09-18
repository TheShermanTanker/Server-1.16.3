package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.arguments.ComponentArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class TellRawCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        int integer2;
        final Iterator iterator;
        ServerPlayer aah4;
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("tellraw").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("message", (ArgumentType<Object>)ComponentArgument.textComponent()).executes(commandContext -> {
            integer2 = 0;
            EntityArgument.getPlayers(commandContext, "targets").iterator();
            while (iterator.hasNext()) {
                aah4 = (ServerPlayer)iterator.next();
                aah4.sendMessage(ComponentUtils.updateForEntity(commandContext.getSource(), ComponentArgument.getComponent(commandContext, "message"), aah4, 0), Util.NIL_UUID);
                ++integer2;
            }
            return integer2;
        }))));
    }
}
