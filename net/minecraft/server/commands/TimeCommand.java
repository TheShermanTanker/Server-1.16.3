package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.TimeArgument;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class TimeCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("time").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("set").then(Commands.literal("day").executes(commandContext -> setTime(commandContext.getSource(), 1000))).then(Commands.literal("noon").executes(commandContext -> setTime(commandContext.getSource(), 6000)))).then(Commands.literal("night").executes(commandContext -> setTime(commandContext.getSource(), 13000)))).then(Commands.literal("midnight").executes(commandContext -> setTime(commandContext.getSource(), 18000)))).then(Commands.argument("time", (ArgumentType<Object>)TimeArgument.time()).executes(commandContext -> setTime(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "time")))))).then(Commands.literal("add").then(Commands.argument("time", (ArgumentType<Object>)TimeArgument.time()).executes(commandContext -> addTime(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "time")))))).then(((LiteralArgumentBuilder)Commands.literal("query").then(Commands.literal("daytime").executes(commandContext -> queryTime(commandContext.getSource(), getDayTime(commandContext.getSource().getLevel())))).then(Commands.literal("gametime").executes(commandContext -> queryTime(commandContext.getSource(), (int)(commandContext.getSource().getLevel().getGameTime() % 2147483647L))))).then(Commands.literal("day").executes(commandContext -> queryTime(commandContext.getSource(), (int)(commandContext.getSource().getLevel().getDayTime() / 24000L % 2147483647L))))));
    }
    
    private static int getDayTime(final ServerLevel aag) {
        return (int)(aag.getDayTime() % 24000L);
    }
    
    private static int queryTime(final CommandSourceStack db, final int integer) {
        db.sendSuccess(new TranslatableComponent("commands.time.query", new Object[] { integer }), false);
        return integer;
    }
    
    public static int setTime(final CommandSourceStack db, final int integer) {
        for (final ServerLevel aag4 : db.getServer().getAllLevels()) {
            aag4.setDayTime(integer);
        }
        db.sendSuccess(new TranslatableComponent("commands.time.set", new Object[] { integer }), true);
        return getDayTime(db.getLevel());
    }
    
    public static int addTime(final CommandSourceStack db, final int integer) {
        for (final ServerLevel aag4 : db.getServer().getAllLevels()) {
            aag4.setDayTime(aag4.getDayTime() + integer);
        }
        final int integer2 = getDayTime(db.getLevel());
        db.sendSuccess(new TranslatableComponent("commands.time.set", new Object[] { integer2 }), true);
        return integer2;
    }
}
