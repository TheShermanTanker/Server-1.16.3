package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import java.util.Iterator;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.commands.arguments.AngleArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import java.util.Collections;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class SetSpawnCommand {
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("spawnpoint").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).executes(commandContext -> setSpawn(commandContext.getSource(), (Collection<ServerPlayer>)Collections.singleton(commandContext.getSource().getPlayerOrException()), new BlockPos(commandContext.getSource().getPosition()), 0.0f))).then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> setSpawn(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), new BlockPos(commandContext.getSource().getPosition()), 0.0f)).then(Commands.argument("pos", (ArgumentType<Object>)BlockPosArgument.blockPos()).executes(commandContext -> setSpawn(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), BlockPosArgument.getOrLoadBlockPos(commandContext, "pos"), 0.0f)).then(Commands.argument("angle", (ArgumentType<Object>)AngleArgument.angle()).executes(commandContext -> setSpawn(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), BlockPosArgument.getOrLoadBlockPos(commandContext, "pos"), AngleArgument.getAngle(commandContext, "angle")))))));
    }
    
    private static int setSpawn(final CommandSourceStack db, final Collection<ServerPlayer> collection, final BlockPos fx, final float float4) {
        final ResourceKey<Level> vj5 = db.getLevel().dimension();
        for (final ServerPlayer aah7 : collection) {
            aah7.setRespawnPosition(vj5, fx, float4, true, false);
        }
        final String string6 = vj5.location().toString();
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.spawnpoint.success.single", new Object[] { fx.getX(), fx.getY(), fx.getZ(), float4, string6, ((ServerPlayer)collection.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.spawnpoint.success.multiple", new Object[] { fx.getX(), fx.getY(), fx.getZ(), float4, string6, collection.size() }), true);
        }
        return collection.size();
    }
}
