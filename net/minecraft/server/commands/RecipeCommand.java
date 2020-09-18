package net.minecraft.server.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Iterator;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.function.Predicate;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class RecipeCommand {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED;
    private static final SimpleCommandExceptionType ERROR_TAKE_FAILED;
    
    public static void register(final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder<CommandSourceStack>)((LiteralArgumentBuilder)Commands.literal("recipe").requires((java.util.function.Predicate<CommandSourceStack>)(db -> db.hasPermission(2))).then(Commands.literal("give").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("recipe", (ArgumentType<Object>)ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES).executes(commandContext -> giveRecipes(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), (Collection<Recipe<?>>)Collections.singleton(ResourceLocationArgument.getRecipe(commandContext, "recipe"))))).then(Commands.literal("*").executes(commandContext -> giveRecipes(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), commandContext.getSource().getServer().getRecipeManager().getRecipes())))))).then(Commands.literal("take").then(Commands.argument("targets", (ArgumentType<Object>)EntityArgument.players()).then(Commands.argument("recipe", (ArgumentType<Object>)ResourceLocationArgument.id()).suggests(SuggestionProviders.ALL_RECIPES).executes(commandContext -> takeRecipes(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), (Collection<Recipe<?>>)Collections.singleton(ResourceLocationArgument.getRecipe(commandContext, "recipe"))))).then(Commands.literal("*").executes(commandContext -> takeRecipes(commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), commandContext.getSource().getServer().getRecipeManager().getRecipes()))))));
    }
    
    private static int giveRecipes(final CommandSourceStack db, final Collection<ServerPlayer> collection2, final Collection<Recipe<?>> collection3) throws CommandSyntaxException {
        int integer4 = 0;
        for (final ServerPlayer aah6 : collection2) {
            integer4 += aah6.awardRecipes(collection3);
        }
        if (integer4 == 0) {
            throw RecipeCommand.ERROR_GIVE_FAILED.create();
        }
        if (collection2.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.recipe.give.success.single", new Object[] { collection3.size(), ((ServerPlayer)collection2.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.recipe.give.success.multiple", new Object[] { collection3.size(), collection2.size() }), true);
        }
        return integer4;
    }
    
    private static int takeRecipes(final CommandSourceStack db, final Collection<ServerPlayer> collection2, final Collection<Recipe<?>> collection3) throws CommandSyntaxException {
        int integer4 = 0;
        for (final ServerPlayer aah6 : collection2) {
            integer4 += aah6.resetRecipes(collection3);
        }
        if (integer4 == 0) {
            throw RecipeCommand.ERROR_TAKE_FAILED.create();
        }
        if (collection2.size() == 1) {
            db.sendSuccess(new TranslatableComponent("commands.recipe.take.success.single", new Object[] { collection3.size(), ((ServerPlayer)collection2.iterator().next()).getDisplayName() }), true);
        }
        else {
            db.sendSuccess(new TranslatableComponent("commands.recipe.take.success.multiple", new Object[] { collection3.size(), collection2.size() }), true);
        }
        return integer4;
    }
    
    static {
        ERROR_GIVE_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.recipe.give.failed"));
        ERROR_TAKE_FAILED = new SimpleCommandExceptionType(new TranslatableComponent("commands.recipe.take.failed"));
    }
}
