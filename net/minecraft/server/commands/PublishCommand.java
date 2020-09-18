package net.minecraft.server.commands;

import java.util.function.Function;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.util.HttpUtil;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class PublishCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    private static final DynamicCommandExceptionType ERROR_ALREADY_PUBLISHED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("publish").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(4))).executes(commandContext -> publish(commandContext.getSource(), HttpUtil.getAvailablePort()))).then(Commands.argument("port", (ArgumentType<Object>)IntegerArgumentType.integer(0, 65535)).executes(commandContext -> publish(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "port")))));
    }
    
    private static int publish(final CommandSourceStack db, final int integer) throws CommandSyntaxException {
        if (db.getServer().isPublished()) {
            throw PublishCommand.ERROR_ALREADY_PUBLISHED.create(db.getServer().getPort());
        }
        if (!db.getServer().publishServer(db.getServer().getDefaultGameType(), false, integer)) {
            throw PublishCommand.ERROR_FAILED.create();
        }
        db.sendSuccess(new TranslatableComponent("commands.publish.success", new Object[] { integer }), true);
        return integer;
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.publish.failed"));
        ERROR_ALREADY_PUBLISHED = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("commands.publish.alreadyPublished", new Object[] { object })));
    }
}
