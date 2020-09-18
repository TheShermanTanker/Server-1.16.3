package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class SaveOnCommand {
    private static final SimpleCommandExceptionType ERROR_ALREADY_ON;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final CommandSourceStack db2;
        boolean boolean3;
        final Iterator iterator;
        ServerLevel aag5;
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("save-on").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(4))).executes(commandContext -> {
            db2 = commandContext.getSource();
            boolean3 = false;
            db2.getServer().getAllLevels().iterator();
            while (iterator.hasNext()) {
                aag5 = (ServerLevel)iterator.next();
                if (aag5 != null && aag5.noSave) {
                    aag5.noSave = false;
                    boolean3 = true;
                }
            }
            if (!boolean3) {
                throw SaveOnCommand.ERROR_ALREADY_ON.create();
            }
            else {
                db2.sendSuccess(new TranslatableComponent("commands.save.enabled"), true);
                return 1;
            }
        }));
    }
    
    static {
        ERROR_ALREADY_ON = new SimpleCommandExceptionType(new TranslatableComponent("commands.save.alreadyOn"));
    }
}
