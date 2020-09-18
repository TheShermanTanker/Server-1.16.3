package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.server.level.ServerPlayer;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import net.minecraft.server.players.PlayerList;
import java.util.stream.Stream;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.GameProfileArgument;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class OpCommand {
    private static final SimpleCommandExceptionType ERROR_ALREADY_OP;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final PlayerList acs3;
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("op").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(3))).then(Commands.argument("targets", (ArgumentType<Object>)GameProfileArgument.gameProfile()).suggests((commandContext, suggestionsBuilder) -> {
            acs3 = commandContext.getSource().getServer().getPlayerList();
            return SharedSuggestionProvider.suggest((Stream<String>)acs3.getPlayers().stream().filter(aah -> !acs3.isOp(aah.getGameProfile())).map(aah -> aah.getGameProfile().getName()), suggestionsBuilder);
        }).executes(commandContext -> opPlayers(commandContext.getSource(), GameProfileArgument.getGameProfiles(commandContext, "targets")))));
    }
    
    private static int opPlayers(final CommandSourceStack db, final Collection<GameProfile> collection) throws CommandSyntaxException {
        final PlayerList acs3 = db.getServer().getPlayerList();
        int integer4 = 0;
        for (final GameProfile gameProfile6 : collection) {
            if (!acs3.isOp(gameProfile6)) {
                acs3.op(gameProfile6);
                ++integer4;
                db.sendSuccess(new TranslatableComponent("commands.op.success", new Object[] { ((GameProfile)collection.iterator().next()).getName() }), true);
            }
        }
        if (integer4 == 0) {
            throw OpCommand.ERROR_ALREADY_OP.create();
        }
        return integer4;
    }
    
    static {
        ERROR_ALREADY_OP = new SimpleCommandExceptionType(new TranslatableComponent("commands.op.failed"));
    }
}
