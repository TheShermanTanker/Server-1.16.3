package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.ComponentArgument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import java.util.Collections;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.resources.ResourceLocation;
import java.util.Collection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.BossEvent;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.bossevents.CustomBossEvent;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;

public class BossBarCommands {
    private static final DynamicCommandExceptionType ERROR_ALREADY_EXISTS;
    private static final DynamicCommandExceptionType ERROR_DOESNT_EXIST;
    private static final SimpleCommandExceptionType ERROR_NO_PLAYER_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_NAME_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_COLOR_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_STYLE_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_VALUE_CHANGE;
    private static final SimpleCommandExceptionType ERROR_NO_MAX_CHANGE;
    private static final SimpleCommandExceptionType ERROR_ALREADY_HIDDEN;
    private static final SimpleCommandExceptionType ERROR_ALREADY_VISIBLE;
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_BOSS_BAR;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "bossbar"
        //     3: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //     6: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //    11: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.requires:(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    14: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    17: ldc             "add"
        //    19: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    22: ldc             "id"
        //    24: invokestatic    net/minecraft/commands/arguments/ResourceLocationArgument.id:()Lnet/minecraft/commands/arguments/ResourceLocationArgument;
        //    27: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    30: ldc             "name"
        //    32: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //    35: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    38: invokedynamic   BootstrapMethod #1, run:()Lcom/mojang/brigadier/Command;
        //    43: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    46: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    49: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    52: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    55: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    58: ldc             "remove"
        //    60: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    63: ldc             "id"
        //    65: invokestatic    net/minecraft/commands/arguments/ResourceLocationArgument.id:()Lnet/minecraft/commands/arguments/ResourceLocationArgument;
        //    68: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    71: getstatic       net/minecraft/server/commands/BossBarCommands.SUGGEST_BOSS_BAR:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //    74: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    77: invokedynamic   BootstrapMethod #2, run:()Lcom/mojang/brigadier/Command;
        //    82: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    85: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    88: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    91: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    94: ldc             "list"
        //    96: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    99: invokedynamic   BootstrapMethod #3, run:()Lcom/mojang/brigadier/Command;
        //   104: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   107: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   110: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   113: ldc             "set"
        //   115: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   118: ldc             "id"
        //   120: invokestatic    net/minecraft/commands/arguments/ResourceLocationArgument.id:()Lnet/minecraft/commands/arguments/ResourceLocationArgument;
        //   123: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   126: getstatic       net/minecraft/server/commands/BossBarCommands.SUGGEST_BOSS_BAR:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   129: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   132: ldc             "name"
        //   134: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   137: ldc             "name"
        //   139: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //   142: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   145: invokedynamic   BootstrapMethod #4, run:()Lcom/mojang/brigadier/Command;
        //   150: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   153: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   156: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   159: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   162: ldc             "color"
        //   164: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   167: ldc             "pink"
        //   169: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   172: invokedynamic   BootstrapMethod #5, run:()Lcom/mojang/brigadier/Command;
        //   177: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   180: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   183: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   186: ldc             "blue"
        //   188: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   191: invokedynamic   BootstrapMethod #6, run:()Lcom/mojang/brigadier/Command;
        //   196: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   199: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   202: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   205: ldc             "red"
        //   207: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   210: invokedynamic   BootstrapMethod #7, run:()Lcom/mojang/brigadier/Command;
        //   215: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   218: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   221: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   224: ldc             "green"
        //   226: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   229: invokedynamic   BootstrapMethod #8, run:()Lcom/mojang/brigadier/Command;
        //   234: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   237: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   240: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   243: ldc             "yellow"
        //   245: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   248: invokedynamic   BootstrapMethod #9, run:()Lcom/mojang/brigadier/Command;
        //   253: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   256: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   259: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   262: ldc             "purple"
        //   264: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   267: invokedynamic   BootstrapMethod #10, run:()Lcom/mojang/brigadier/Command;
        //   272: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   275: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   278: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   281: ldc             "white"
        //   283: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   286: invokedynamic   BootstrapMethod #11, run:()Lcom/mojang/brigadier/Command;
        //   291: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   294: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   297: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   300: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   303: ldc             "style"
        //   305: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   308: ldc             "progress"
        //   310: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   313: invokedynamic   BootstrapMethod #12, run:()Lcom/mojang/brigadier/Command;
        //   318: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   321: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   324: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   327: ldc             "notched_6"
        //   329: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   332: invokedynamic   BootstrapMethod #13, run:()Lcom/mojang/brigadier/Command;
        //   337: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   340: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   343: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   346: ldc             "notched_10"
        //   348: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   351: invokedynamic   BootstrapMethod #14, run:()Lcom/mojang/brigadier/Command;
        //   356: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   359: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   362: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   365: ldc             "notched_12"
        //   367: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   370: invokedynamic   BootstrapMethod #15, run:()Lcom/mojang/brigadier/Command;
        //   375: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   378: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   381: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   384: ldc             "notched_20"
        //   386: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   389: invokedynamic   BootstrapMethod #16, run:()Lcom/mojang/brigadier/Command;
        //   394: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   397: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   400: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   403: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   406: ldc             "value"
        //   408: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   411: ldc             "value"
        //   413: iconst_0       
        //   414: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   417: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   420: invokedynamic   BootstrapMethod #17, run:()Lcom/mojang/brigadier/Command;
        //   425: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   428: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   431: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   434: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   437: ldc             "max"
        //   439: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   442: ldc             "max"
        //   444: iconst_1       
        //   445: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   448: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   451: invokedynamic   BootstrapMethod #18, run:()Lcom/mojang/brigadier/Command;
        //   456: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   459: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   462: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   465: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   468: ldc             "visible"
        //   470: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   473: ldc             "visible"
        //   475: invokestatic    com/mojang/brigadier/arguments/BoolArgumentType.bool:()Lcom/mojang/brigadier/arguments/BoolArgumentType;
        //   478: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   481: invokedynamic   BootstrapMethod #19, run:()Lcom/mojang/brigadier/Command;
        //   486: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   489: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   492: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   495: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   498: ldc_w           "players"
        //   501: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   504: invokedynamic   BootstrapMethod #20, run:()Lcom/mojang/brigadier/Command;
        //   509: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   512: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   515: ldc_w           "targets"
        //   518: invokestatic    net/minecraft/commands/arguments/EntityArgument.players:()Lnet/minecraft/commands/arguments/EntityArgument;
        //   521: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   524: invokedynamic   BootstrapMethod #21, run:()Lcom/mojang/brigadier/Command;
        //   529: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   532: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   535: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   538: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   541: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   544: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   547: ldc_w           "get"
        //   550: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   553: ldc             "id"
        //   555: invokestatic    net/minecraft/commands/arguments/ResourceLocationArgument.id:()Lnet/minecraft/commands/arguments/ResourceLocationArgument;
        //   558: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   561: getstatic       net/minecraft/server/commands/BossBarCommands.SUGGEST_BOSS_BAR:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   564: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   567: ldc             "value"
        //   569: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   572: invokedynamic   BootstrapMethod #22, run:()Lcom/mojang/brigadier/Command;
        //   577: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   580: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   583: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   586: ldc             "max"
        //   588: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   591: invokedynamic   BootstrapMethod #23, run:()Lcom/mojang/brigadier/Command;
        //   596: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   599: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   602: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   605: ldc             "visible"
        //   607: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   610: invokedynamic   BootstrapMethod #24, run:()Lcom/mojang/brigadier/Command;
        //   615: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   618: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   621: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   624: ldc_w           "players"
        //   627: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   630: invokedynamic   BootstrapMethod #25, run:()Lcom/mojang/brigadier/Command;
        //   635: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   638: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   641: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   644: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   647: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   650: invokevirtual   com/mojang/brigadier/CommandDispatcher.register:(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;
        //   653: pop            
        //   654: return         
        //    Signature:
        //  (Lcom/mojang/brigadier/CommandDispatcher<Lnet/minecraft/commands/CommandSourceStack;>;)V
        //    MethodParameters:
        //  Name               Flags  
        //  -----------------  -----
        //  commandDispatcher  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2780)
        //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2760)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper.erase(MetadataHelper.java:1661)
        //     at com.strobel.assembler.metadata.MetadataHelper.eraseRecursive(MetadataHelper.java:1642)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2463)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1499)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:344)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static int getValue(final CommandSourceStack db, final CustomBossEvent wc) {
        db.sendSuccess(new TranslatableComponent("commands.bossbar.get.value", new Object[] { wc.getDisplayName(), wc.getValue() }), true);
        return wc.getValue();
    }
    
    private static int getMax(final CommandSourceStack db, final CustomBossEvent wc) {
        db.sendSuccess(new TranslatableComponent("commands.bossbar.get.max", new Object[] { wc.getDisplayName(), wc.getMax() }), true);
        return wc.getMax();
    }
    
    private static int getVisible(final CommandSourceStack db, final CustomBossEvent wc) {
        if (wc.isVisible()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.get.visible.visible", new Object[] { wc.getDisplayName() }), true);
            return 1;
        }
        db.sendSuccess(new TranslatableComponent("commands.bossbar.get.visible.hidden", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int getPlayers(final CommandSourceStack db, final CustomBossEvent wc) {
        if (wc.getPlayers().isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.get.players.none", new Object[] { wc.getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.get.players.some", new Object[] { wc.getDisplayName(), wc.getPlayers().size(), ComponentUtils.<ServerPlayer>formatList(wc.getPlayers(), (java.util.function.Function<ServerPlayer, Component>)Player::getDisplayName) }), true);
        }
        return wc.getPlayers().size();
    }
    
    private static int setVisible(final CommandSourceStack db, final CustomBossEvent wc, final boolean boolean3) throws CommandSyntaxException {
        if (wc.isVisible() != boolean3) {
            wc.setVisible(boolean3);
            if (boolean3) {
                db.sendSuccess(new TranslatableComponent("commands.bossbar.set.visible.success.visible", new Object[] { wc.getDisplayName() }), true);
            }
            else {
                db.sendSuccess(new TranslatableComponent("commands.bossbar.set.visible.success.hidden", new Object[] { wc.getDisplayName() }), true);
            }
            return 0;
        }
        if (boolean3) {
            throw BossBarCommands.ERROR_ALREADY_VISIBLE.create();
        }
        throw BossBarCommands.ERROR_ALREADY_HIDDEN.create();
    }
    
    private static int setValue(final CommandSourceStack db, final CustomBossEvent wc, final int integer) throws CommandSyntaxException {
        if (wc.getValue() == integer) {
            throw BossBarCommands.ERROR_NO_VALUE_CHANGE.create();
        }
        wc.setValue(integer);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.value.success", new Object[] { wc.getDisplayName(), integer }), true);
        return integer;
    }
    
    private static int setMax(final CommandSourceStack db, final CustomBossEvent wc, final int integer) throws CommandSyntaxException {
        if (wc.getMax() == integer) {
            throw BossBarCommands.ERROR_NO_MAX_CHANGE.create();
        }
        wc.setMax(integer);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.max.success", new Object[] { wc.getDisplayName(), integer }), true);
        return integer;
    }
    
    private static int setColor(final CommandSourceStack db, final CustomBossEvent wc, final BossEvent.BossBarColor a) throws CommandSyntaxException {
        if (wc.getColor().equals(a)) {
            throw BossBarCommands.ERROR_NO_COLOR_CHANGE.create();
        }
        wc.setColor(a);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.color.success", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int setStyle(final CommandSourceStack db, final CustomBossEvent wc, final BossEvent.BossBarOverlay b) throws CommandSyntaxException {
        if (wc.getOverlay().equals(b)) {
            throw BossBarCommands.ERROR_NO_STYLE_CHANGE.create();
        }
        wc.setOverlay(b);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.style.success", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int setName(final CommandSourceStack db, final CustomBossEvent wc, final Component nr) throws CommandSyntaxException {
        final Component nr2 = ComponentUtils.updateForEntity(db, nr, null, 0);
        if (wc.getName().equals(nr2)) {
            throw BossBarCommands.ERROR_NO_NAME_CHANGE.create();
        }
        wc.setName(nr2);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.set.name.success", new Object[] { wc.getDisplayName() }), true);
        return 0;
    }
    
    private static int setPlayers(final CommandSourceStack db, final CustomBossEvent wc, final Collection<ServerPlayer> collection) throws CommandSyntaxException {
        final boolean boolean4 = wc.setPlayers(collection);
        if (!boolean4) {
            throw BossBarCommands.ERROR_NO_PLAYER_CHANGE.create();
        }
        if (wc.getPlayers().isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.set.players.success.none", new Object[] { wc.getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.set.players.success.some", new Object[] { wc.getDisplayName(), collection.size(), ComponentUtils.<ServerPlayer>formatList(collection, (java.util.function.Function<ServerPlayer, Component>)Player::getDisplayName) }), true);
        }
        return wc.getPlayers().size();
    }
    
    private static int listBars(final CommandSourceStack db) {
        final Collection<CustomBossEvent> collection2 = db.getServer().getCustomBossEvents().getEvents();
        if (collection2.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.list.bars.none"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.bossbar.list.bars.some", new Object[] { collection2.size(), ComponentUtils.<CustomBossEvent>formatList(collection2, (java.util.function.Function<CustomBossEvent, Component>)CustomBossEvent::getDisplayName) }), false);
        }
        return collection2.size();
    }
    
    private static int createBar(final CommandSourceStack db, final ResourceLocation vk, final Component nr) throws CommandSyntaxException {
        final CustomBossEvents wd4 = db.getServer().getCustomBossEvents();
        if (wd4.get(vk) != null) {
            throw BossBarCommands.ERROR_ALREADY_EXISTS.create(vk.toString());
        }
        final CustomBossEvent wc5 = wd4.create(vk, ComponentUtils.updateForEntity(db, nr, null, 0));
        db.sendSuccess(new TranslatableComponent("commands.bossbar.create.success", new Object[] { wc5.getDisplayName() }), true);
        return wd4.getEvents().size();
    }
    
    private static int removeBar(final CommandSourceStack db, final CustomBossEvent wc) {
        final CustomBossEvents wd3 = db.getServer().getCustomBossEvents();
        wc.removeAllPlayers();
        wd3.remove(wc);
        db.sendSuccess(new TranslatableComponent("commands.bossbar.remove.success", new Object[] { wc.getDisplayName() }), true);
        return wd3.getEvents().size();
    }
    
    public static CustomBossEvent getBossBar(final CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        final ResourceLocation vk2 = ResourceLocationArgument.getId(commandContext, "id");
        final CustomBossEvent wc3 = commandContext.getSource().getServer().getCustomBossEvents().get(vk2);
        if (wc3 == null) {
            throw BossBarCommands.ERROR_DOESNT_EXIST.create(vk2.toString());
        }
        return wc3;
    }
    
    static {
        ERROR_ALREADY_EXISTS = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("commands.bossbar.create.failed", new Object[] { object })));
        ERROR_DOESNT_EXIST = new DynamicCommandExceptionType((Function<Object, Message>)(object -> new TranslatableComponent("commands.bossbar.unknown", new Object[] { object })));
        ERROR_NO_PLAYER_CHANGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.players.unchanged"));
        ERROR_NO_NAME_CHANGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.name.unchanged"));
        ERROR_NO_COLOR_CHANGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.color.unchanged"));
        ERROR_NO_STYLE_CHANGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.style.unchanged"));
        ERROR_NO_VALUE_CHANGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.value.unchanged"));
        ERROR_NO_MAX_CHANGE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.max.unchanged"));
        ERROR_ALREADY_HIDDEN = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.visibility.unchanged.hidden"));
        ERROR_ALREADY_VISIBLE = new SimpleCommandExceptionType(new TranslatableComponent("commands.bossbar.set.visibility.unchanged.visible"));
        SUGGEST_BOSS_BAR = ((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>)commandContext.getSource().getServer().getCustomBossEvents().getIds(), suggestionsBuilder));
    }
}
