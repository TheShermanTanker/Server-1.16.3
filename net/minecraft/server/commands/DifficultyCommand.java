package net.minecraft.server.commands;

import java.util.function.Function;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.function.Predicate;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.world.Difficulty;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class DifficultyCommand {
    private static final DynamicCommandExceptionType ERROR_ALREADY_DIFFICULT;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = Commands.literal("difficulty");
        for (final Difficulty aoo6 : Difficulty.values()) {
            literalArgumentBuilder2.then(Commands.literal(aoo6.getKey()).executes(commandContext -> setDifficulty(commandContext.getSource(), aoo6)));
        }
        final Difficulty aoo7;
        final CommandSourceStack commandSourceStack;
        final TranslatableComponent nr;
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)literalArgumentBuilder2.requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).executes(commandContext -> {
            aoo7 = commandContext.getSource().getLevel().getDifficulty();
            commandSourceStack = commandContext.getSource();
            new TranslatableComponent("commands.difficulty.query", new Object[] { aoo7.getDisplayName() });
            commandSourceStack.sendSuccess(nr, false);
            return aoo7.getId();
        }));
    }
    
    public static int setDifficulty(final CommandSourceStack db, final Difficulty aoo) throws CommandSyntaxException {
        final MinecraftServer minecraftServer3 = db.getServer();
        if (minecraftServer3.getWorldData().getDifficulty() == aoo) {
            throw DifficultyCommand.ERROR_ALREADY_DIFFICULT.create(aoo.getKey());
        }
        minecraftServer3.setDifficulty(aoo, true);
        db.sendSuccess(new TranslatableComponent("commands.difficulty.success", new Object[] { aoo.getDisplayName() }), true);
        return 0;
    }
    
    static {
        ERROR_ALREADY_DIFFICULT = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("commands.difficulty.failure", new Object[] { object })));
    }
}
