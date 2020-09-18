package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.commands.arguments.AngleArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.core.BlockPos;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class SetWorldSpawnCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("setworldspawn").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).executes(commandContext -> setSpawn(commandContext.getSource(), new BlockPos(commandContext.getSource().getPosition()), 0.0f))).then(Commands.argument("pos", (ArgumentType<Object>)BlockPosArgument.blockPos()).executes(commandContext -> setSpawn(commandContext.getSource(), BlockPosArgument.getOrLoadBlockPos(commandContext, "pos"), 0.0f)).then(Commands.argument("angle", (ArgumentType<Object>)AngleArgument.angle()).executes(commandContext -> setSpawn(commandContext.getSource(), BlockPosArgument.getOrLoadBlockPos(commandContext, "pos"), AngleArgument.getAngle(commandContext, "angle"))))));
    }
    
    private static int setSpawn(final CommandSourceStack db, final BlockPos fx, final float float3) {
        db.getLevel().setDefaultSpawnPos(fx, float3);
        db.sendSuccess(new TranslatableComponent("commands.setworldspawn.success", new Object[] { fx.getX(), fx.getY(), fx.getZ(), float3 }), true);
        return 1;
    }
}
