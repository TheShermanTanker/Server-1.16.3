package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class StopCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("stop").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(4))).executes(commandContext -> {
            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.stop.stopping"), true);
            commandContext.getSource().getServer().halt(false);
            return 1;
        }));
    }
}
