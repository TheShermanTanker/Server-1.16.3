package net.minecraft.server.commands;

import java.util.function.Function;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.GameType;
import net.minecraft.server.level.ServerPlayer;
import javax.annotation.Nullable;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.world.entity.Entity;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class SpectateCommand {
    private static final SimpleCommandExceptionType ERROR_SELF;
    private static final DynamicCommandExceptionType ERROR_NOT_SPECTATOR;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("spectate").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).executes(commandContext -> spectate(commandContext.getSource(), null, commandContext.getSource().getPlayerOrException()))).then(Commands.argument("target", (ArgumentType<Object>)EntityArgument.entity()).executes(commandContext -> spectate(commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), commandContext.getSource().getPlayerOrException())).then(Commands.argument("player", (ArgumentType<Object>)EntityArgument.player()).executes(commandContext -> spectate(commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), EntityArgument.getPlayer(commandContext, "player"))))));
    }
    
    private static int spectate(final CommandSourceStack db, @Nullable final Entity apx, final ServerPlayer aah) throws CommandSyntaxException {
        if (aah == apx) {
            throw SpectateCommand.ERROR_SELF.create();
        }
        if (aah.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
            throw SpectateCommand.ERROR_NOT_SPECTATOR.create(aah.getDisplayName());
        }
        aah.setCamera(apx);
        if (apx != null) {
            db.sendSuccess(new TranslatableComponent("commands.spectate.success.started", new Object[] { apx.getDisplayName() }), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.spectate.success.stopped"), false);
        }
        return 1;
    }
    
    static {
        ERROR_SELF = new SimpleCommandExceptionType(new TranslatableComponent("commands.spectate.self"));
        ERROR_NOT_SPECTATOR = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("commands.spectate.not_spectator", new Object[] { object })));
    }
}
