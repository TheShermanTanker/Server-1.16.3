package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import java.util.Collection;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.ImmutableList;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class KillCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("kill").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).executes(commandContext -> kill(commandContext.getSource(), ImmutableList.<Entity>of(commandContext.getSource().getEntityOrException())))).then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.entities()).executes(commandContext -> kill(commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets")))));
    }
    
    private static int kill(final CommandSourceStack db, final Collection<? extends Entity> collection) {
        for (final Entity apx4 : collection) {
            apx4.kill();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.kill.success.single", new Object[] { ((Entity)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.kill.success.multiple", new Object[] { collection.size() }), true);
        }
        return collection.size();
    }
}
