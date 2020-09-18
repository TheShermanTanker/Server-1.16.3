package net.minecraft.server.commands;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.world.level.GameRules;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class GameRuleCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder2 = Commands.literal("gamerule").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2)));
        GameRules.visitGameRuleTypes(new GameRules.GameRuleTypeVisitor() {
            public <T extends GameRules.Value<T>> void visit(final GameRules.Key<T> e, final GameRules.Type<T> f) {
                literalArgumentBuilder2.then(Commands.literal(e.getId()).executes(commandContext -> GameRuleCommand.<GameRules.Value>queryRule(commandContext.getSource(), (GameRules.Key<GameRules.Value>)e)).then(f.createArgument("value").executes(commandContext -> GameRuleCommand.<GameRules.Value>setRule(commandContext, (GameRules.Key<GameRules.Value>)e))));
            }
        });
        commandDispatcher.register(literalArgumentBuilder2);
    }
    
    private static <T extends GameRules.Value<T>> int setRule(final CommandContext<CommandSourceStack> commandContext, final GameRules.Key<T> e) {
        final CommandSourceStack db3 = commandContext.getSource();
        final T g4 = db3.getServer().getGameRules().<T>getRule(e);
        g4.setFromArgument(commandContext, "value");
        db3.sendSuccess(new TranslatableComponent("commands.gamerule.set", new Object[] { e.getId(), g4.toString() }), true);
        return g4.getCommandResult();
    }
    
    private static <T extends GameRules.Value<T>> int queryRule(final CommandSourceStack db, final GameRules.Key<T> e) {
        final T g3 = db.getServer().getGameRules().<T>getRule(e);
        db.sendSuccess(new TranslatableComponent("commands.gamerule.query", new Object[] { e.getId(), g3.toString() }), false);
        return g3.getCommandResult();
    }
}
