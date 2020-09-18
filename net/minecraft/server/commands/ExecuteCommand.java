package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.world.level.storage.loot.PredicateManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ByteTag;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.OptionalInt;
import java.util.Collections;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.advancements.critereon.MinMaxBounds;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.brigadier.Command;
import net.minecraft.commands.arguments.RangeArgument;
import java.util.function.BiPredicate;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.nbt.CompoundTag;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Supplier;
import net.minecraft.nbt.Tag;
import java.util.function.IntFunction;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.bossevents.CustomBossEvent;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Objective;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.server.commands.data.DataCommands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import java.util.Iterator;
import java.util.List;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.coordinates.SwizzleArgument;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.ResultConsumer;
import java.util.function.BinaryOperator;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;

public class ExecuteCommand {
    private static final Dynamic2CommandExceptionType ERROR_AREA_TOO_LARGE;
    private static final SimpleCommandExceptionType ERROR_CONDITIONAL_FAILED;
    private static final DynamicCommandExceptionType ERROR_CONDITIONAL_FAILED_COUNT;
    private static final BinaryOperator<ResultConsumer<CommandSourceStack>> CALLBACK_CHAINER;
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_PREDICATE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        final LiteralCommandNode<CommandSourceStack> literalCommandNode2 = commandDispatcher.register(Commands.literal("execute").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))));
        final List<CommandSourceStack> list2;
        final Iterator iterator;
        Entity apx4;
        final List<CommandSourceStack> list3;
        final Iterator iterator2;
        Entity apx5;
        final List<CommandSourceStack> list4;
        final Iterator iterator3;
        Entity apx6;
        final List<CommandSourceStack> list5;
        final Iterator iterator4;
        Entity apx7;
        final List<CommandSourceStack> list6;
        final EntityAnchorArgument.Anchor a3;
        final Iterator iterator5;
        Entity apx8;
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("execute").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).then(Commands.literal("run").redirect((CommandNode<CommandSourceStack>)commandDispatcher.getRoot()))).then(addConditionals(literalCommandNode2, Commands.literal("if"), true))).then(addConditionals(literalCommandNode2, Commands.literal("unless"), false))).then(Commands.literal("as").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.entities()).fork(literalCommandNode2, commandContext -> {
            list2 = Lists.newArrayList();
            EntityArgument.getOptionalEntities(commandContext, "targets").iterator();
            while (iterator.hasNext()) {
                apx4 = (Entity)iterator.next();
                list2.add(commandContext.getSource().withEntity(apx4));
            }
            return (java.util.Collection<CommandSourceStack>)list2;
        })))).then(Commands.literal("at").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.entities()).fork(literalCommandNode2, commandContext -> {
            list3 = Lists.newArrayList();
            EntityArgument.getOptionalEntities(commandContext, "targets").iterator();
            while (iterator2.hasNext()) {
                apx5 = (Entity)iterator2.next();
                list3.add(commandContext.getSource().withLevel((ServerLevel)apx5.level).withPosition(apx5.position()).withRotation(apx5.getRotationVector()));
            }
            return (java.util.Collection<CommandSourceStack>)list3;
        })))).then(Commands.literal("store").then(wrapStores(literalCommandNode2, Commands.literal("result"), true)).then(wrapStores(literalCommandNode2, Commands.literal("success"), false)))).then(Commands.literal("positioned").then(Commands.argument("pos", (ArgumentType<Object>)Vec3Argument.vec3()).redirect(literalCommandNode2, commandContext -> commandContext.getSource().withPosition(Vec3Argument.getVec3(commandContext, "pos")).withAnchor(EntityAnchorArgument.Anchor.FEET))).then(Commands.literal("as").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.entities()).fork(literalCommandNode2, commandContext -> {
            list4 = Lists.newArrayList();
            EntityArgument.getOptionalEntities(commandContext, "targets").iterator();
            while (iterator3.hasNext()) {
                apx6 = (Entity)iterator3.next();
                list4.add(commandContext.getSource().withPosition(apx6.position()));
            }
            return (java.util.Collection<CommandSourceStack>)list4;
        }))))).then(Commands.literal("rotated").then(Commands.argument("rot", (ArgumentType<Object>)RotationArgument.rotation()).redirect(literalCommandNode2, commandContext -> commandContext.getSource().withRotation(RotationArgument.getRotation(commandContext, "rot").getRotation(commandContext.getSource())))).then(Commands.literal("as").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.entities()).fork(literalCommandNode2, commandContext -> {
            list5 = Lists.newArrayList();
            EntityArgument.getOptionalEntities(commandContext, "targets").iterator();
            while (iterator4.hasNext()) {
                apx7 = (Entity)iterator4.next();
                list5.add(commandContext.getSource().withRotation(apx7.getRotationVector()));
            }
            return (java.util.Collection<CommandSourceStack>)list5;
        }))))).then(Commands.literal("facing").then(Commands.literal("entity").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.entities()).then(Commands.argument("anchor", (ArgumentType<Object>)EntityAnchorArgument.anchor()).fork(literalCommandNode2, commandContext -> {
            list6 = Lists.newArrayList();
            a3 = EntityAnchorArgument.getAnchor(commandContext, "anchor");
            EntityArgument.getOptionalEntities(commandContext, "targets").iterator();
            while (iterator5.hasNext()) {
                apx8 = (Entity)iterator5.next();
                list6.add(commandContext.getSource().facing(apx8, a3));
            }
            return (java.util.Collection<CommandSourceStack>)list6;
        })))).then(Commands.argument("pos", (ArgumentType<Object>)Vec3Argument.vec3()).redirect(literalCommandNode2, commandContext -> commandContext.getSource().facing(Vec3Argument.getVec3(commandContext, "pos")))))).then(Commands.literal("align").then(Commands.argument("axes", (ArgumentType<Object>)SwizzleArgument.swizzle()).redirect(literalCommandNode2, commandContext -> commandContext.getSource().withPosition(commandContext.getSource().getPosition().align(SwizzleArgument.getSwizzle(commandContext, "axes"))))))).then(Commands.literal("anchored").then(Commands.argument("anchor", (ArgumentType<Object>)EntityAnchorArgument.anchor()).redirect(literalCommandNode2, commandContext -> commandContext.getSource().withAnchor(EntityAnchorArgument.getAnchor(commandContext, "anchor")))))).then(Commands.literal("in").then(Commands.argument("dimension", (ArgumentType<Object>)DimensionArgument.dimension()).redirect(literalCommandNode2, commandContext -> commandContext.getSource().withLevel(DimensionArgument.getDimension(commandContext, "dimension"))))));
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> wrapStores(final LiteralCommandNode<CommandSourceStack> literalCommandNode, final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, final boolean boolean3) {
        literalArgumentBuilder.then(Commands.literal("score").then(Commands.argument("targets", (ArgumentType<Object>)ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", (ArgumentType<Object>)ObjectiveArgument.objective()).redirect(literalCommandNode, commandContext -> storeValue(commandContext.getSource(), ScoreHolderArgument.getNamesWithDefaultWildcard(commandContext, "targets"), ObjectiveArgument.getObjective(commandContext, "objective"), boolean3)))));
        literalArgumentBuilder.then(Commands.literal("bossbar").then(Commands.argument("id", (ArgumentType<Object>)ResourceLocationArgument.id()).suggests(BossBarCommands.SUGGEST_BOSS_BAR).then(Commands.literal("value").redirect((CommandNode<CommandSourceStack>)literalCommandNode, commandContext -> storeValue(commandContext.getSource(), BossBarCommands.getBossBar(commandContext), true, boolean3))).then(Commands.literal("max").redirect((CommandNode<CommandSourceStack>)literalCommandNode, commandContext -> storeValue(commandContext.getSource(), BossBarCommands.getBossBar(commandContext), false, boolean3)))));
        for (final DataCommands.DataProvider c5 : DataCommands.TARGET_PROVIDERS) {
            c5.wrap(literalArgumentBuilder, (Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>>)(argumentBuilder -> argumentBuilder.then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("path", (ArgumentType<Object>)NbtPathArgument.nbtPath()).then(Commands.literal("int").then(Commands.argument("scale", (ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect(literalCommandNode, commandContext -> storeData(commandContext.getSource(), c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"), (IntFunction<Tag>)(integer -> IntTag.valueOf((int)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3)))).then(Commands.literal("float").then(Commands.argument("scale", (ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect(literalCommandNode, commandContext -> storeData(commandContext.getSource(), c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"), (IntFunction<Tag>)(integer -> FloatTag.valueOf((float)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("short").then(Commands.argument("scale", (ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect(literalCommandNode, commandContext -> storeData(commandContext.getSource(), c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"), (IntFunction<Tag>)(integer -> ShortTag.valueOf((short)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("long").then(Commands.argument("scale", (ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect(literalCommandNode, commandContext -> storeData(commandContext.getSource(), c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"), (IntFunction<Tag>)(integer -> LongTag.valueOf((long)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3))))).then(Commands.literal("double").then(Commands.argument("scale", (ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect(literalCommandNode, commandContext -> storeData(commandContext.getSource(), c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"), (IntFunction<Tag>)(integer -> DoubleTag.valueOf(integer * DoubleArgumentType.getDouble(commandContext, "scale"))), boolean3))))).then(Commands.literal("byte").then(Commands.argument("scale", (ArgumentType<Object>)DoubleArgumentType.doubleArg()).redirect(literalCommandNode, commandContext -> storeData(commandContext.getSource(), c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"), (IntFunction<Tag>)(integer -> ByteTag.valueOf((byte)(integer * DoubleArgumentType.getDouble(commandContext, "scale")))), boolean3)))))));
        }
        return literalArgumentBuilder;
    }
    
    private static CommandSourceStack storeValue(final CommandSourceStack db, final Collection<String> collection, final Objective ddh, final boolean boolean4) {
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        final Iterator iterator;
        String string9;
        final Scoreboard scoreboard;
        Score ddj10;
        int integer2;
        return db.withCallback((commandContext, boolean6, integer) -> {
            collection.iterator();
            while (iterator.hasNext()) {
                string9 = (String)iterator.next();
                ddj10 = scoreboard.getOrCreatePlayerScore(string9, ddh);
                integer2 = ((boolean4 ? integer : boolean6) ? 1 : 0);
                ddj10.setScore(integer2);
            }
        }, ExecuteCommand.CALLBACK_CHAINER);
    }
    
    private static CommandSourceStack storeValue(final CommandSourceStack db, final CustomBossEvent wc, final boolean boolean3, final boolean boolean4) {
        final int integer2;
        return db.withCallback((commandContext, boolean5, integer) -> {
            integer2 = (boolean4 ? integer : boolean5);
            if (boolean3) {
                wc.setValue(integer2);
            }
            else {
                wc.setMax(integer2);
            }
        }, ExecuteCommand.CALLBACK_CHAINER);
    }
    
    private static CommandSourceStack storeData(final CommandSourceStack db, final DataAccessor yz, final NbtPathArgument.NbtPath h, final IntFunction<Tag> intFunction, final boolean boolean5) {
        CompoundTag md8;
        int integer2;
        return db.withCallback((commandContext, boolean6, integer) -> {
            try {
                md8 = yz.getData();
                integer2 = (boolean5 ? integer : boolean6);
                h.set(md8, (Supplier<Tag>)(() -> (Tag)intFunction.apply(integer2)));
                yz.setData(md8);
            }
            catch (CommandSyntaxException ex) {}
        }, ExecuteCommand.CALLBACK_CHAINER);
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> addConditionals(final CommandNode<CommandSourceStack> commandNode, final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder, final boolean boolean3) {
        ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)literalArgumentBuilder.then(Commands.literal("block").then(Commands.argument("pos", (ArgumentType<Object>)BlockPosArgument.blockPos()).then(addConditional(commandNode, Commands.argument("block", (ArgumentType<Object>)BlockPredicateArgument.blockPredicate()), boolean3, commandContext -> BlockPredicateArgument.getBlockPredicate(commandContext, "block").test(new BlockInWorld(commandContext.getSource().getLevel(), BlockPosArgument.getLoadedBlockPos(commandContext, "pos"), true)))))).then(Commands.literal("score").then(Commands.argument("target", (ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("targetObjective", (ArgumentType<Object>)ObjectiveArgument.objective()).then(Commands.literal("=").then(Commands.argument("source", (ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNode, Commands.argument("sourceObjective", (ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)Integer::equals))))).then(Commands.literal("<").then(Commands.argument("source", (ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNode, Commands.argument("sourceObjective", (ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 < integer2))))))).then(Commands.literal("<=").then(Commands.argument("source", (ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNode, Commands.argument("sourceObjective", (ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 <= integer2))))))).then(Commands.literal(">").then(Commands.argument("source", (ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNode, Commands.argument("sourceObjective", (ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 > integer2))))))).then(Commands.literal(">=").then(Commands.argument("source", (ArgumentType<Object>)ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(addConditional(commandNode, Commands.argument("sourceObjective", (ArgumentType<Object>)ObjectiveArgument.objective()), boolean3, commandContext -> checkScore(commandContext, (BiPredicate<Integer, Integer>)((integer1, integer2) -> integer1 >= integer2))))))).then(Commands.literal("matches").then(addConditional(commandNode, Commands.argument("range", (ArgumentType<Object>)RangeArgument.intRange()), boolean3, commandContext -> checkScore(commandContext, RangeArgument.Ints.getRange(commandContext, "range"))))))))).then(Commands.literal("blocks").then(Commands.argument("start", (ArgumentType<Object>)BlockPosArgument.blockPos()).then(Commands.argument("end", (ArgumentType<Object>)BlockPosArgument.blockPos()).then(Commands.argument("destination", (ArgumentType<Object>)BlockPosArgument.blockPos()).then(addIfBlocksConditional(commandNode, Commands.literal("all"), boolean3, false)).then(addIfBlocksConditional(commandNode, Commands.literal("masked"), boolean3, true))))))).then(Commands.literal("entity").then(Commands.argument("entities", (ArgumentType<Object>)EntityArgument.entities()).fork(commandNode, commandContext -> expect(commandContext, boolean3, !EntityArgument.getOptionalEntities(commandContext, "entities").isEmpty())).executes(createNumericConditionalHandler(boolean3, commandContext -> EntityArgument.getOptionalEntities(commandContext, "entities").size()))))).then(Commands.literal("predicate").then(addConditional(commandNode, Commands.argument("predicate", (ArgumentType<Object>)ResourceLocationArgument.id()).suggests(ExecuteCommand.SUGGEST_PREDICATE), boolean3, commandContext -> checkCustomPredicate(commandContext.getSource(), ResourceLocationArgument.getPredicate(commandContext, "predicate")))));
        for (final DataCommands.DataProvider c5 : DataCommands.SOURCE_PROVIDERS) {
            literalArgumentBuilder.then(c5.wrap(Commands.literal("data"), (Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>>)(argumentBuilder -> argumentBuilder.then(Commands.argument("path", (ArgumentType<Object>)NbtPathArgument.nbtPath()).fork(commandNode, commandContext -> expect(commandContext, boolean3, checkMatchingData(c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path")) > 0)).executes(createNumericConditionalHandler(boolean3, commandContext -> checkMatchingData(c5.access(commandContext), NbtPathArgument.getPath(commandContext, "path"))))))));
        }
        return literalArgumentBuilder;
    }
    
    private static Command<CommandSourceStack> createNumericConditionalHandler(final boolean boolean1, final CommandNumericPredicate a) {
        if (boolean1) {
            final int integer3;
            CommandSourceStack commandSourceStack;
            final TranslatableComponent nr;
            return commandContext -> {
                integer3 = a.test(commandContext);
                if (integer3 > 0) {
                    commandSourceStack = commandContext.getSource();
                    new TranslatableComponent("commands.execute.conditional.pass_count", new Object[] { integer3 });
                    commandSourceStack.sendSuccess(nr, false);
                    return integer3;
                }
                else {
                    throw ExecuteCommand.ERROR_CONDITIONAL_FAILED.create();
                }
            };
        }
        final int integer4;
        return commandContext -> {
            integer4 = a.test(commandContext);
            if (integer4 == 0) {
                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
                return 1;
            }
            else {
                throw ExecuteCommand.ERROR_CONDITIONAL_FAILED_COUNT.create(integer4);
            }
        };
    }
    
    private static int checkMatchingData(final DataAccessor yz, final NbtPathArgument.NbtPath h) throws CommandSyntaxException {
        return h.countMatching(yz.getData());
    }
    
    private static boolean checkScore(final CommandContext<CommandSourceStack> commandContext, final BiPredicate<Integer, Integer> biPredicate) throws CommandSyntaxException {
        final String string3 = ScoreHolderArgument.getName(commandContext, "target");
        final Objective ddh4 = ObjectiveArgument.getObjective(commandContext, "targetObjective");
        final String string4 = ScoreHolderArgument.getName(commandContext, "source");
        final Objective ddh5 = ObjectiveArgument.getObjective(commandContext, "sourceObjective");
        final Scoreboard ddk7 = commandContext.getSource().getServer().getScoreboard();
        if (!ddk7.hasPlayerScore(string3, ddh4) || !ddk7.hasPlayerScore(string4, ddh5)) {
            return false;
        }
        final Score ddj8 = ddk7.getOrCreatePlayerScore(string3, ddh4);
        final Score ddj9 = ddk7.getOrCreatePlayerScore(string4, ddh5);
        return biPredicate.test(ddj8.getScore(), ddj9.getScore());
    }
    
    private static boolean checkScore(final CommandContext<CommandSourceStack> commandContext, final MinMaxBounds.Ints d) throws CommandSyntaxException {
        final String string3 = ScoreHolderArgument.getName(commandContext, "target");
        final Objective ddh4 = ObjectiveArgument.getObjective(commandContext, "targetObjective");
        final Scoreboard ddk5 = commandContext.getSource().getServer().getScoreboard();
        return ddk5.hasPlayerScore(string3, ddh4) && d.matches(ddk5.getOrCreatePlayerScore(string3, ddh4).getScore());
    }
    
    private static boolean checkCustomPredicate(final CommandSourceStack db, final LootItemCondition dbl) {
        final ServerLevel aag3 = db.getLevel();
        final LootContext.Builder a4 = new LootContext.Builder(aag3).<Vec3>withParameter(LootContextParams.ORIGIN, db.getPosition()).<Entity>withOptionalParameter(LootContextParams.THIS_ENTITY, db.getEntity());
        return dbl.test(a4.create(LootContextParamSets.COMMAND));
    }
    
    private static Collection<CommandSourceStack> expect(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2, final boolean boolean3) {
        if (boolean3 == boolean2) {
            return (Collection<CommandSourceStack>)Collections.singleton(commandContext.getSource());
        }
        return (Collection<CommandSourceStack>)Collections.emptyList();
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> addConditional(final CommandNode<CommandSourceStack> commandNode, final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final boolean boolean3, final CommandPredicate b) {
        return ((ArgumentBuilder<Object, ArgumentBuilder<CommandSourceStack, ?>>)argumentBuilder.fork(commandNode, commandContext -> expect(commandContext, boolean3, b.test(commandContext)))).executes(commandContext -> {
            if (boolean3 == b.test(commandContext)) {
                commandContext.getSource().sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
                return 1;
            }
            else {
                throw ExecuteCommand.ERROR_CONDITIONAL_FAILED.create();
            }
        });
    }
    
    private static ArgumentBuilder<CommandSourceStack, ?> addIfBlocksConditional(final CommandNode<CommandSourceStack> commandNode, final ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, final boolean boolean3, final boolean boolean4) {
        return ((ArgumentBuilder<Object, ArgumentBuilder<CommandSourceStack, ?>>)argumentBuilder.fork(commandNode, commandContext -> expect(commandContext, boolean3, checkRegions(commandContext, boolean4).isPresent()))).executes(boolean3 ? (commandContext -> checkIfRegions(commandContext, boolean4)) : (commandContext -> checkUnlessRegions(commandContext, boolean4)));
    }
    
    private static int checkIfRegions(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2) throws CommandSyntaxException {
        final OptionalInt optionalInt3 = checkRegions(commandContext, boolean2);
        if (optionalInt3.isPresent()) {
            commandContext.getSource().sendSuccess(new TranslatableComponent("commands.execute.conditional.pass_count", new Object[] { optionalInt3.getAsInt() }), false);
            return optionalInt3.getAsInt();
        }
        throw ExecuteCommand.ERROR_CONDITIONAL_FAILED.create();
    }
    
    private static int checkUnlessRegions(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2) throws CommandSyntaxException {
        final OptionalInt optionalInt3 = checkRegions(commandContext, boolean2);
        if (optionalInt3.isPresent()) {
            throw ExecuteCommand.ERROR_CONDITIONAL_FAILED_COUNT.create(optionalInt3.getAsInt());
        }
        commandContext.getSource().sendSuccess(new TranslatableComponent("commands.execute.conditional.pass"), false);
        return 1;
    }
    
    private static OptionalInt checkRegions(final CommandContext<CommandSourceStack> commandContext, final boolean boolean2) throws CommandSyntaxException {
        return checkRegions(commandContext.getSource().getLevel(), BlockPosArgument.getLoadedBlockPos(commandContext, "start"), BlockPosArgument.getLoadedBlockPos(commandContext, "end"), BlockPosArgument.getLoadedBlockPos(commandContext, "destination"), boolean2);
    }
    
    private static OptionalInt checkRegions(final ServerLevel aag, final BlockPos fx2, final BlockPos fx3, final BlockPos fx4, final boolean boolean5) throws CommandSyntaxException {
        final BoundingBox cqx6 = new BoundingBox(fx2, fx3);
        final BoundingBox cqx7 = new BoundingBox(fx4, fx4.offset(cqx6.getLength()));
        final BlockPos fx5 = new BlockPos(cqx7.x0 - cqx6.x0, cqx7.y0 - cqx6.y0, cqx7.z0 - cqx6.z0);
        final int integer9 = cqx6.getXSpan() * cqx6.getYSpan() * cqx6.getZSpan();
        if (integer9 > 32768) {
            throw ExecuteCommand.ERROR_AREA_TOO_LARGE.create(32768, integer9);
        }
        int integer10 = 0;
        for (int integer11 = cqx6.z0; integer11 <= cqx6.z1; ++integer11) {
            for (int integer12 = cqx6.y0; integer12 <= cqx6.y1; ++integer12) {
                for (int integer13 = cqx6.x0; integer13 <= cqx6.x1; ++integer13) {
                    final BlockPos fx6 = new BlockPos(integer13, integer12, integer11);
                    final BlockPos fx7 = fx6.offset(fx5);
                    final BlockState cee16 = aag.getBlockState(fx6);
                    if (!boolean5 || !cee16.is(Blocks.AIR)) {
                        if (cee16 != aag.getBlockState(fx7)) {
                            return OptionalInt.empty();
                        }
                        final BlockEntity ccg17 = aag.getBlockEntity(fx6);
                        final BlockEntity ccg18 = aag.getBlockEntity(fx7);
                        if (ccg17 != null) {
                            if (ccg18 == null) {
                                return OptionalInt.empty();
                            }
                            final CompoundTag md19 = ccg17.save(new CompoundTag());
                            md19.remove("x");
                            md19.remove("y");
                            md19.remove("z");
                            final CompoundTag md20 = ccg18.save(new CompoundTag());
                            md20.remove("x");
                            md20.remove("y");
                            md20.remove("z");
                            if (!md19.equals(md20)) {
                                return OptionalInt.empty();
                            }
                        }
                        ++integer10;
                    }
                }
            }
        }
        return OptionalInt.of(integer10);
    }
    
    static {
        final TranslatableComponent translatableComponent;
        ERROR_AREA_TOO_LARGE = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("commands.execute.blocks.toobig", new Object[] { object1, object2 });
            return translatableComponent;
        });
        ERROR_CONDITIONAL_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.execute.conditional.fail"));
        ERROR_CONDITIONAL_FAILED_COUNT = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("commands.execute.conditional.fail_count", new Object[] { object })));
        CALLBACK_CHAINER = ((resultConsumer1, resultConsumer2) -> (commandContext, boolean4, integer) -> {
            resultConsumer1.onCommandComplete(commandContext, boolean4, integer);
            resultConsumer2.onCommandComplete(commandContext, boolean4, integer);
        });
        final PredicateManager cyx3;
        SUGGEST_PREDICATE = ((commandContext, suggestionsBuilder) -> {
            cyx3 = commandContext.getSource().getServer().getPredicateManager();
            return SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)cyx3.getKeys(), suggestionsBuilder);
        });
    }
    
    @FunctionalInterface
    interface CommandNumericPredicate {
        int test(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }
    
    @FunctionalInterface
    interface CommandPredicate {
        boolean test(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException;
    }
}
