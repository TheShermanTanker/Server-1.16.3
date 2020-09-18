package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import com.mojang.brigadier.context.CommandContext;
import java.util.function.Function;
import java.util.Map;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.commands.arguments.OperationArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.Score;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Iterator;
import net.minecraft.world.scores.Scoreboard;
import java.util.List;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.scores.Objective;
import com.google.common.collect.Lists;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class ScoreboardCommand {
    private static final SimpleCommandExceptionType ERROR_OBJECTIVE_ALREADY_EXISTS;
    private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_EMPTY;
    private static final SimpleCommandExceptionType ERROR_DISPLAY_SLOT_ALREADY_SET;
    private static final SimpleCommandExceptionType ERROR_TRIGGER_ALREADY_ENABLED;
    private static final SimpleCommandExceptionType ERROR_NOT_TRIGGER;
    private static final Dynamic2CommandExceptionType ERROR_NO_VALUE;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ldc             "scoreboard"
        //     3: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //     6: invokedynamic   BootstrapMethod #0, test:()Ljava/util/function/Predicate;
        //    11: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.requires:(Ljava/util/function/Predicate;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    14: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    17: ldc             "objectives"
        //    19: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    22: ldc             "list"
        //    24: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    27: invokedynamic   BootstrapMethod #1, run:()Lcom/mojang/brigadier/Command;
        //    32: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    35: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    38: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    41: ldc             "add"
        //    43: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //    46: ldc             "objective"
        //    48: invokestatic    com/mojang/brigadier/arguments/StringArgumentType.word:()Lcom/mojang/brigadier/arguments/StringArgumentType;
        //    51: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    54: ldc             "criteria"
        //    56: invokestatic    net/minecraft/commands/arguments/ObjectiveCriteriaArgument.criteria:()Lnet/minecraft/commands/arguments/ObjectiveCriteriaArgument;
        //    59: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    62: invokedynamic   BootstrapMethod #2, run:()Lcom/mojang/brigadier/Command;
        //    67: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    70: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    73: ldc             "displayName"
        //    75: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //    78: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //    81: invokedynamic   BootstrapMethod #3, run:()Lcom/mojang/brigadier/Command;
        //    86: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    89: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    92: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    95: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //    98: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   101: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   104: ldc             "modify"
        //   106: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   109: ldc             "objective"
        //   111: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   114: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   117: ldc             "displayname"
        //   119: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   122: ldc             "displayName"
        //   124: invokestatic    net/minecraft/commands/arguments/ComponentArgument.textComponent:()Lnet/minecraft/commands/arguments/ComponentArgument;
        //   127: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   130: invokedynamic   BootstrapMethod #4, run:()Lcom/mojang/brigadier/Command;
        //   135: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   138: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   141: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   144: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   147: invokestatic    net/minecraft/server/commands/ScoreboardCommand.createRenderTypeModify:()Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   150: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   153: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   156: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   159: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   162: ldc             "remove"
        //   164: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   167: ldc             "objective"
        //   169: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   172: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   175: invokedynamic   BootstrapMethod #5, run:()Lcom/mojang/brigadier/Command;
        //   180: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   183: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   186: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   189: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   192: ldc             "setdisplay"
        //   194: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   197: ldc             "slot"
        //   199: invokestatic    net/minecraft/commands/arguments/ScoreboardSlotArgument.displaySlot:()Lnet/minecraft/commands/arguments/ScoreboardSlotArgument;
        //   202: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   205: invokedynamic   BootstrapMethod #6, run:()Lcom/mojang/brigadier/Command;
        //   210: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   213: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   216: ldc             "objective"
        //   218: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   221: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   224: invokedynamic   BootstrapMethod #7, run:()Lcom/mojang/brigadier/Command;
        //   229: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   232: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   235: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   238: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   241: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   244: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   247: ldc             "players"
        //   249: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   252: ldc             "list"
        //   254: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   257: invokedynamic   BootstrapMethod #8, run:()Lcom/mojang/brigadier/Command;
        //   262: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   265: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   268: ldc             "target"
        //   270: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolder:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   273: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   276: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   279: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   282: invokedynamic   BootstrapMethod #9, run:()Lcom/mojang/brigadier/Command;
        //   287: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   290: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   293: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   296: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   299: ldc             "set"
        //   301: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   304: ldc             "targets"
        //   306: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   309: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   312: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   315: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   318: ldc             "objective"
        //   320: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   323: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   326: ldc             "score"
        //   328: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:()Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   331: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   334: invokedynamic   BootstrapMethod #10, run:()Lcom/mojang/brigadier/Command;
        //   339: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   342: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   345: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   348: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   351: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   354: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   357: ldc             "get"
        //   359: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   362: ldc             "target"
        //   364: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolder:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   367: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   370: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   373: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   376: ldc             "objective"
        //   378: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   381: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   384: invokedynamic   BootstrapMethod #11, run:()Lcom/mojang/brigadier/Command;
        //   389: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   392: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   395: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   398: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   401: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   404: ldc             "add"
        //   406: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   409: ldc             "targets"
        //   411: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   414: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   417: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   420: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   423: ldc             "objective"
        //   425: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   428: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   431: ldc             "score"
        //   433: iconst_0       
        //   434: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   437: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   440: invokedynamic   BootstrapMethod #12, run:()Lcom/mojang/brigadier/Command;
        //   445: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   448: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   451: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   454: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   457: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   460: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   463: ldc             "remove"
        //   465: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   468: ldc             "targets"
        //   470: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   473: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   476: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   479: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   482: ldc             "objective"
        //   484: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   487: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   490: ldc             "score"
        //   492: iconst_0       
        //   493: invokestatic    com/mojang/brigadier/arguments/IntegerArgumentType.integer:(I)Lcom/mojang/brigadier/arguments/IntegerArgumentType;
        //   496: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   499: invokedynamic   BootstrapMethod #13, run:()Lcom/mojang/brigadier/Command;
        //   504: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   507: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   510: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   513: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   516: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   519: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   522: ldc             "reset"
        //   524: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   527: ldc             "targets"
        //   529: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   532: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   535: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   538: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   541: invokedynamic   BootstrapMethod #14, run:()Lcom/mojang/brigadier/Command;
        //   546: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   549: checkcast       Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   552: ldc             "objective"
        //   554: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   557: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   560: invokedynamic   BootstrapMethod #15, run:()Lcom/mojang/brigadier/Command;
        //   565: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   568: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   571: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   574: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   577: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   580: ldc_w           "enable"
        //   583: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   586: ldc             "targets"
        //   588: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   591: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   594: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   597: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   600: ldc             "objective"
        //   602: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   605: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   608: invokedynamic   BootstrapMethod #16, getSuggestions:()Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   613: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   616: invokedynamic   BootstrapMethod #17, run:()Lcom/mojang/brigadier/Command;
        //   621: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   624: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   627: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   630: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   633: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   636: ldc_w           "operation"
        //   639: invokestatic    net/minecraft/commands/Commands.literal:(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   642: ldc             "targets"
        //   644: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   647: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   650: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   653: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   656: ldc_w           "targetObjective"
        //   659: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   662: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   665: ldc_w           "operation"
        //   668: invokestatic    net/minecraft/commands/arguments/OperationArgument.operation:()Lnet/minecraft/commands/arguments/OperationArgument;
        //   671: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   674: ldc_w           "source"
        //   677: invokestatic    net/minecraft/commands/arguments/ScoreHolderArgument.scoreHolders:()Lnet/minecraft/commands/arguments/ScoreHolderArgument;
        //   680: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   683: getstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //   686: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.suggests:(Lcom/mojang/brigadier/suggestion/SuggestionProvider;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   689: ldc_w           "sourceObjective"
        //   692: invokestatic    net/minecraft/commands/arguments/ObjectiveArgument.objective:()Lnet/minecraft/commands/arguments/ObjectiveArgument;
        //   695: invokestatic    net/minecraft/commands/Commands.argument:(Ljava/lang/String;Lcom/mojang/brigadier/arguments/ArgumentType;)Lcom/mojang/brigadier/builder/RequiredArgumentBuilder;
        //   698: invokedynamic   BootstrapMethod #18, run:()Lcom/mojang/brigadier/Command;
        //   703: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.executes:(Lcom/mojang/brigadier/Command;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   706: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   709: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   712: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   715: invokevirtual   com/mojang/brigadier/builder/RequiredArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   718: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   721: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   724: invokevirtual   com/mojang/brigadier/builder/LiteralArgumentBuilder.then:(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;
        //   727: checkcast       Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;
        //   730: invokevirtual   com/mojang/brigadier/CommandDispatcher.register:(Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;)Lcom/mojang/brigadier/tree/LiteralCommandNode;
        //   733: pop            
        //   734: return         
        //    Signature:
        //  (Lcom/mojang/brigadier/CommandDispatcher<Lnet/minecraft/commands/CommandSourceStack;>;)V
        //    MethodParameters:
        //  Name               Flags  
        //  -----------------  -----
        //  commandDispatcher  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2369)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.assembler.metadata.MetadataHelper$7.visitType(MetadataHelper.java:1982)
        //     at com.strobel.assembler.metadata.MetadataHelper$7.visitType(MetadataHelper.java:1947)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitClassType(DefaultTypeVisitor.java:45)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.containsType(MetadataHelper.java:1395)
        //     at com.strobel.assembler.metadata.MetadataHelper.containsType(MetadataHelper.java:1482)
        //     at com.strobel.assembler.metadata.MetadataHelper.containsTypeRecursive(MetadataHelper.java:1531)
        //     at com.strobel.assembler.metadata.MetadataHelper.access$100(MetadataHelper.java:33)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1863)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1882)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1829)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
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
    
    private static LiteralArgumentBuilder<CommandSourceStack> createRenderTypeModify() {
        final LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder1 = Commands.literal("rendertype");
        for (final ObjectiveCriteria.RenderType a5 : ObjectiveCriteria.RenderType.values()) {
            literalArgumentBuilder1.then(Commands.literal(a5.getId()).executes(commandContext -> setRenderType(commandContext.getSource(), ObjectiveArgument.getObjective(commandContext, "objective"), a5)));
        }
        return literalArgumentBuilder1;
    }
    
    private static CompletableFuture<Suggestions> suggestTriggers(final CommandSourceStack db, final Collection<String> collection, final SuggestionsBuilder suggestionsBuilder) {
        final List<String> list4 = Lists.newArrayList();
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        for (final Objective ddh7 : ddk5.getObjectives()) {
            if (ddh7.getCriteria() == ObjectiveCriteria.TRIGGER) {
                boolean boolean8 = false;
                for (final String string10 : collection) {
                    if (!ddk5.hasPlayerScore(string10, ddh7) || ddk5.getOrCreatePlayerScore(string10, ddh7).isLocked()) {
                        boolean8 = true;
                        break;
                    }
                }
                if (!boolean8) {
                    continue;
                }
                list4.add(ddh7.getName());
            }
        }
        return SharedSuggestionProvider.suggest((Iterable<String>)list4, suggestionsBuilder);
    }
    
    private static int getScore(final CommandSourceStack db, final String string, final Objective ddh) throws CommandSyntaxException {
        final Scoreboard ddk4 = db.getServer().getScoreboard();
        if (!ddk4.hasPlayerScore(string, ddh)) {
            throw ScoreboardCommand.ERROR_NO_VALUE.create(ddh.getName(), string);
        }
        final Score ddj5 = ddk4.getOrCreatePlayerScore(string, ddh);
        db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.get.success", new Object[] { string, ddj5.getScore(), ddh.getFormattedDisplayName() }), false);
        return ddj5.getScore();
    }
    
    private static int performOperation(final CommandSourceStack db, final Collection<String> collection2, final Objective ddh3, final OperationArgument.Operation a, final Collection<String> collection5, final Objective ddh6) throws CommandSyntaxException {
        final Scoreboard ddk7 = db.getServer().getScoreboard();
        int integer8 = 0;
        for (final String string10 : collection2) {
            final Score ddj11 = ddk7.getOrCreatePlayerScore(string10, ddh3);
            for (final String string11 : collection5) {
                final Score ddj12 = ddk7.getOrCreatePlayerScore(string11, ddh6);
                a.apply(ddj11, ddj12);
            }
            integer8 += ddj11.getScore();
        }
        if (collection2.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.operation.success.single", new Object[] { ddh3.getFormattedDisplayName(), collection2.iterator().next(), integer8 }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.operation.success.multiple", new Object[] { ddh3.getFormattedDisplayName(), collection2.size() }), true);
        }
        return integer8;
    }
    
    private static int enableTrigger(final CommandSourceStack db, final Collection<String> collection, final Objective ddh) throws CommandSyntaxException {
        if (ddh.getCriteria() != ObjectiveCriteria.TRIGGER) {
            throw ScoreboardCommand.ERROR_NOT_TRIGGER.create();
        }
        final Scoreboard ddk4 = db.getServer().getScoreboard();
        int integer5 = 0;
        for (final String string7 : collection) {
            final Score ddj8 = ddk4.getOrCreatePlayerScore(string7, ddh);
            if (ddj8.isLocked()) {
                ddj8.setLocked(false);
                ++integer5;
            }
        }
        if (integer5 == 0) {
            throw ScoreboardCommand.ERROR_TRIGGER_ALREADY_ENABLED.create();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.enable.success.single", new Object[] { ddh.getFormattedDisplayName(), collection.iterator().next() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.enable.success.multiple", new Object[] { ddh.getFormattedDisplayName(), collection.size() }), true);
        }
        return integer5;
    }
    
    private static int resetScores(final CommandSourceStack db, final Collection<String> collection) {
        final Scoreboard ddk3 = db.getServer().getScoreboard();
        for (final String string5 : collection) {
            ddk3.resetPlayerScore(string5, null);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.reset.all.single", new Object[] { collection.iterator().next() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.reset.all.multiple", new Object[] { collection.size() }), true);
        }
        return collection.size();
    }
    
    private static int resetScore(final CommandSourceStack db, final Collection<String> collection, final Objective ddh) {
        final Scoreboard ddk4 = db.getServer().getScoreboard();
        for (final String string6 : collection) {
            ddk4.resetPlayerScore(string6, ddh);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.reset.specific.single", new Object[] { ddh.getFormattedDisplayName(), collection.iterator().next() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.reset.specific.multiple", new Object[] { ddh.getFormattedDisplayName(), collection.size() }), true);
        }
        return collection.size();
    }
    
    private static int setScore(final CommandSourceStack db, final Collection<String> collection, final Objective ddh, final int integer) {
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        for (final String string7 : collection) {
            final Score ddj8 = ddk5.getOrCreatePlayerScore(string7, ddh);
            ddj8.setScore(integer);
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.set.success.single", new Object[] { ddh.getFormattedDisplayName(), collection.iterator().next(), integer }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.set.success.multiple", new Object[] { ddh.getFormattedDisplayName(), collection.size(), integer }), true);
        }
        return integer * collection.size();
    }
    
    private static int addScore(final CommandSourceStack db, final Collection<String> collection, final Objective ddh, final int integer) {
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        int integer2 = 0;
        for (final String string8 : collection) {
            final Score ddj9 = ddk5.getOrCreatePlayerScore(string8, ddh);
            ddj9.setScore(ddj9.getScore() + integer);
            integer2 += ddj9.getScore();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.add.success.single", new Object[] { integer, ddh.getFormattedDisplayName(), collection.iterator().next(), integer2 }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.add.success.multiple", new Object[] { integer, ddh.getFormattedDisplayName(), collection.size() }), true);
        }
        return integer2;
    }
    
    private static int removeScore(final CommandSourceStack db, final Collection<String> collection, final Objective ddh, final int integer) {
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        int integer2 = 0;
        for (final String string8 : collection) {
            final Score ddj9 = ddk5.getOrCreatePlayerScore(string8, ddh);
            ddj9.setScore(ddj9.getScore() - integer);
            integer2 += ddj9.getScore();
        }
        if (collection.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.remove.success.single", new Object[] { integer, ddh.getFormattedDisplayName(), collection.iterator().next(), integer2 }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.remove.success.multiple", new Object[] { integer, ddh.getFormattedDisplayName(), collection.size() }), true);
        }
        return integer2;
    }
    
    private static int listTrackedPlayers(final CommandSourceStack db) {
        final Collection<String> collection2 = db.getServer().getScoreboard().getTrackedPlayers();
        if (collection2.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.list.empty"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.list.success", new Object[] { collection2.size(), ComponentUtils.formatList(collection2) }), false);
        }
        return collection2.size();
    }
    
    private static int listTrackedPlayerScores(final CommandSourceStack db, final String string) {
        final Map<Objective, Score> map3 = db.getServer().getScoreboard().getPlayerScores(string);
        if (map3.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.list.entity.empty", new Object[] { string }), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.list.entity.success", new Object[] { string, map3.size() }), false);
            for (final Map.Entry<Objective, Score> entry5 : map3.entrySet()) {
                db.sendSuccess(new TranslatableComponent("commands.scoreboard.players.list.entity.entry", new Object[] { ((Objective)entry5.getKey()).getFormattedDisplayName(), ((Score)entry5.getValue()).getScore() }), false);
            }
        }
        return map3.size();
    }
    
    private static int clearDisplaySlot(final CommandSourceStack db, final int integer) throws CommandSyntaxException {
        final Scoreboard ddk3 = db.getServer().getScoreboard();
        if (ddk3.getDisplayObjective(integer) == null) {
            throw ScoreboardCommand.ERROR_DISPLAY_SLOT_ALREADY_EMPTY.create();
        }
        ddk3.setDisplayObjective(integer, null);
        db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.display.cleared", new Object[] { Scoreboard.getDisplaySlotNames()[integer] }), true);
        return 0;
    }
    
    private static int setDisplaySlot(final CommandSourceStack db, final int integer, final Objective ddh) throws CommandSyntaxException {
        final Scoreboard ddk4 = db.getServer().getScoreboard();
        if (ddk4.getDisplayObjective(integer) == ddh) {
            throw ScoreboardCommand.ERROR_DISPLAY_SLOT_ALREADY_SET.create();
        }
        ddk4.setDisplayObjective(integer, ddh);
        db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.display.set", new Object[] { Scoreboard.getDisplaySlotNames()[integer], ddh.getDisplayName() }), true);
        return 0;
    }
    
    private static int setDisplayName(final CommandSourceStack db, final Objective ddh, final Component nr) {
        if (!ddh.getDisplayName().equals(nr)) {
            ddh.setDisplayName(nr);
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.modify.displayname", new Object[] { ddh.getName(), ddh.getFormattedDisplayName() }), true);
        }
        return 0;
    }
    
    private static int setRenderType(final CommandSourceStack db, final Objective ddh, final ObjectiveCriteria.RenderType a) {
        if (ddh.getRenderType() != a) {
            ddh.setRenderType(a);
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.modify.rendertype", new Object[] { ddh.getFormattedDisplayName() }), true);
        }
        return 0;
    }
    
    private static int removeObjective(final CommandSourceStack db, final Objective ddh) {
        final Scoreboard ddk3 = db.getServer().getScoreboard();
        ddk3.removeObjective(ddh);
        db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.remove.success", new Object[] { ddh.getFormattedDisplayName() }), true);
        return ddk3.getObjectives().size();
    }
    
    private static int addObjective(final CommandSourceStack db, final String string, final ObjectiveCriteria ddn, final Component nr) throws CommandSyntaxException {
        final Scoreboard ddk5 = db.getServer().getScoreboard();
        if (ddk5.getObjective(string) != null) {
            throw ScoreboardCommand.ERROR_OBJECTIVE_ALREADY_EXISTS.create();
        }
        if (string.length() > 16) {
            throw ObjectiveArgument.ERROR_OBJECTIVE_NAME_TOO_LONG.create(16);
        }
        ddk5.addObjective(string, ddn, nr, ddn.getDefaultRenderType());
        final Objective ddh6 = ddk5.getObjective(string);
        db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.add.success", new Object[] { ddh6.getFormattedDisplayName() }), true);
        return ddk5.getObjectives().size();
    }
    
    private static int listObjectives(final CommandSourceStack db) {
        final Collection<Objective> collection2 = db.getServer().getScoreboard().getObjectives();
        if (collection2.isEmpty()) {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.list.empty"), false);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.scoreboard.objectives.list.success", new Object[] { collection2.size(), ComponentUtils.<Objective>formatList(collection2, (java.util.function.Function<Objective, Component>)Objective::getFormattedDisplayName) }), false);
        }
        return collection2.size();
    }
    
    static {
        ERROR_OBJECTIVE_ALREADY_EXISTS = new SimpleCommandExceptionType(new TranslatableComponent("commands.scoreboard.objectives.add.duplicate"));
        ERROR_DISPLAY_SLOT_ALREADY_EMPTY = new SimpleCommandExceptionType(new TranslatableComponent("commands.scoreboard.objectives.display.alreadyEmpty"));
        ERROR_DISPLAY_SLOT_ALREADY_SET = new SimpleCommandExceptionType(new TranslatableComponent("commands.scoreboard.objectives.display.alreadySet"));
        ERROR_TRIGGER_ALREADY_ENABLED = new SimpleCommandExceptionType(new TranslatableComponent("commands.scoreboard.players.enable.failed"));
        ERROR_NOT_TRIGGER = new SimpleCommandExceptionType(new TranslatableComponent("commands.scoreboard.players.enable.invalid"));
        final TranslatableComponent translatableComponent;
        ERROR_NO_VALUE = new Dynamic2CommandExceptionType((object1, object2) -> {
            new TranslatableComponent("commands.scoreboard.players.get.null", new Object[] { object1, object2 });
            return translatableComponent;
        });
    }
}
