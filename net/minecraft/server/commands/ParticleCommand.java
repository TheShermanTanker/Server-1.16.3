package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import net.minecraft.world.phys.Vec3;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.ParticleArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ParticleCommand {
    private static final SimpleCommandExceptionType ERROR_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)Commands.literal("particle").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).then(Commands.argument("name", (ArgumentType<Object>)ParticleArgument.particle()).executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), commandContext.getSource().getPosition(), Vec3.ZERO, 0.0f, 0, false, (Collection<ServerPlayer>)commandContext.getSource().getServer().getPlayerList().getPlayers())).then(Commands.argument("pos", (ArgumentType<Object>)Vec3Argument.vec3()).executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), Vec3Argument.getVec3(commandContext, "pos"), Vec3.ZERO, 0.0f, 0, false, (Collection<ServerPlayer>)commandContext.getSource().getServer().getPlayerList().getPlayers())).then(Commands.argument("delta", (ArgumentType<Object>)Vec3Argument.vec3(false)).then(Commands.argument("speed", (ArgumentType<Object>)FloatArgumentType.floatArg(0.0f)).then(((RequiredArgumentBuilder)Commands.argument("count", (ArgumentType<Object>)IntegerArgumentType.integer(0)).executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), Vec3Argument.getVec3(commandContext, "pos"), Vec3Argument.getVec3(commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), false, (Collection<ServerPlayer>)commandContext.getSource().getServer().getPlayerList().getPlayers())).then(Commands.literal("force").executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), Vec3Argument.getVec3(commandContext, "pos"), Vec3Argument.getVec3(commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), true, (Collection<ServerPlayer>)commandContext.getSource().getServer().getPlayerList().getPlayers())).then(Commands.argument("viewers", (ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), Vec3Argument.getVec3(commandContext, "pos"), Vec3Argument.getVec3(commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), true, EntityArgument.getPlayers(commandContext, "viewers")))))).then(Commands.literal("normal").executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), Vec3Argument.getVec3(commandContext, "pos"), Vec3Argument.getVec3(commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), false, (Collection<ServerPlayer>)commandContext.getSource().getServer().getPlayerList().getPlayers())).then(Commands.argument("viewers", (ArgumentType<Object>)EntityArgument.players()).executes(commandContext -> sendParticles(commandContext.getSource(), ParticleArgument.getParticle(commandContext, "name"), Vec3Argument.getVec3(commandContext, "pos"), Vec3Argument.getVec3(commandContext, "delta"), FloatArgumentType.getFloat(commandContext, "speed"), IntegerArgumentType.getInteger(commandContext, "count"), false, EntityArgument.getPlayers(commandContext, "viewers")))))))))));
    }
    
    private static int sendParticles(final CommandSourceStack db, final ParticleOptions hf, final Vec3 dck3, final Vec3 dck4, final float float5, final int integer, final boolean boolean7, final Collection<ServerPlayer> collection) throws CommandSyntaxException {
        int integer2 = 0;
        for (final ServerPlayer aah11 : collection) {
            if (db.getLevel().<ParticleOptions>sendParticles(aah11, hf, boolean7, dck3.x, dck3.y, dck3.z, integer, dck4.x, dck4.y, dck4.z, float5)) {
                ++integer2;
            }
        }
        if (integer2 == 0) {
            throw ParticleCommand.ERROR_FAILED.create();
        }
        db.sendSuccess(new TranslatableComponent("commands.particle.success", new Object[] { Registry.PARTICLE_TYPE.getKey(hf.getType()).toString() }), true);
        return integer2;
    }
    
    static {
        ERROR_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.particle.failed"));
    }
}
