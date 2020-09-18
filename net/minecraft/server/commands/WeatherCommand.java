package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class WeatherCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("weather").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).then(Commands.literal("clear").executes(commandContext -> setClear(commandContext.getSource(), 6000)).then(Commands.argument("duration", (ArgumentType<Object>)IntegerArgumentType.integer(0, 1000000)).executes(commandContext -> setClear(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "duration") * 20))))).then(Commands.literal("rain").executes(commandContext -> setRain(commandContext.getSource(), 6000)).then(Commands.argument("duration", (ArgumentType<Object>)IntegerArgumentType.integer(0, 1000000)).executes(commandContext -> setRain(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "duration") * 20))))).then(Commands.literal("thunder").executes(commandContext -> setThunder(commandContext.getSource(), 6000)).then(Commands.argument("duration", (ArgumentType<Object>)IntegerArgumentType.integer(0, 1000000)).executes(commandContext -> setThunder(commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "duration") * 20)))));
    }
    
    private static int setClear(final CommandSourceStack db, final int integer) {
        db.getLevel().setWeatherParameters(integer, 0, false, false);
        db.sendSuccess(new TranslatableComponent("commands.weather.set.clear"), true);
        return integer;
    }
    
    private static int setRain(final CommandSourceStack db, final int integer) {
        db.getLevel().setWeatherParameters(0, integer, true, false);
        db.sendSuccess(new TranslatableComponent("commands.weather.set.rain"), true);
        return integer;
    }
    
    private static int setThunder(final CommandSourceStack db, final int integer) {
        db.getLevel().setWeatherParameters(0, integer, true, true);
        db.sendSuccess(new TranslatableComponent("commands.weather.set.thunder"), true);
        return integer;
    }
}
