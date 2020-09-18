package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.Util;
import net.minecraft.world.level.GameRules;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.world.level.GameType;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class GameModeCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = Commands.literal("gamemode").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2)));
        for (final GameType brr6 : GameType.values()) {
            if (brr6 != GameType.NOT_SET) {
                literalArgumentBuilder2.then(Commands.literal(brr6.getName()).executes(commandContext -> setMode(commandContext, (Collection<ServerPlayer>)Collections.singleton(commandContext.getSource().getPlayerOrException()), brr6)).then(Commands.argument("target", (ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> setMode(commandContext, EntityArgument.getPlayers(commandContext, "target"), brr6))));
            }
        }
        commandDispatcher.register(literalArgumentBuilder2);
    }
    
    private static void logGamemodeChange(final CommandSourceStack db, final ServerPlayer aah, final GameType brr) {
        final Component nr4 = new TranslatableComponent("gameMode." + brr.getName());
        if (db.getEntity() == aah) {
            db.sendSuccess(new TranslatableComponent("commands.gamemode.success.self", new Object[] { nr4 }), true);
        }
        else {
            if (db.getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
                aah.sendMessage(new TranslatableComponent("gameMode.changed", new Object[] { nr4 }), Util.NIL_UUID);
            }
            db.sendSuccess(new TranslatableComponent("commands.gamemode.success.other", new Object[] { aah.getDisplayName(), nr4 }), true);
        }
    }
    
    private static int setMode(final CommandContext<CommandSourceStack> commandContext, final Collection<ServerPlayer> collection, final GameType brr) {
        int integer4 = 0;
        for (final ServerPlayer aah6 : collection) {
            if (aah6.gameMode.getGameModeForPlayer() != brr) {
                aah6.setGameMode(brr);
                logGamemodeChange(commandContext.getSource(), aah6, brr);
                ++integer4;
            }
        }
        return integer4;
    }
}
