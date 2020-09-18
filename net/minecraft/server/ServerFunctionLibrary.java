package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import java.util.function.Predicate;
import java.util.Iterator;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.commands.CommandSource;
import com.google.common.collect.Maps;
import java.util.Collection;
import net.minecraft.server.packs.resources.Resource;
import java.io.IOException;
import java.util.concurrent.CompletionException;
import org.apache.commons.io.IOUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletionStage;
import com.mojang.datafixers.util.Pair;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import java.util.function.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.tags.Tag;
import java.util.Optional;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.tags.TagCollection;
import net.minecraft.tags.TagLoader;
import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class ServerFunctionLibrary implements PreparableReloadListener {
    private static final Logger LOGGER;
    private static final int PATH_PREFIX_LENGTH;
    private static final int PATH_SUFFIX_LENGTH;
    private volatile Map<ResourceLocation, CommandFunction> functions;
    private final TagLoader<CommandFunction> tagsLoader;
    private volatile TagCollection<CommandFunction> tags;
    private final int functionCompilationLevel;
    private final CommandDispatcher<CommandSourceStack> dispatcher;
    
    public Optional<CommandFunction> getFunction(final ResourceLocation vk) {
        return (Optional<CommandFunction>)Optional.ofNullable(this.functions.get(vk));
    }
    
    public Map<ResourceLocation, CommandFunction> getFunctions() {
        return this.functions;
    }
    
    public TagCollection<CommandFunction> getTags() {
        return this.tags;
    }
    
    public Tag<CommandFunction> getTag(final ResourceLocation vk) {
        return this.tags.getTagOrEmpty(vk);
    }
    
    public ServerFunctionLibrary(final int integer, final CommandDispatcher<CommandSourceStack> commandDispatcher) {
        this.functions = ImmutableMap.of();
        this.tagsLoader = new TagLoader<CommandFunction>((java.util.function.Function<ResourceLocation, java.util.Optional<CommandFunction>>)this::getFunction, "tags/functions", "function");
        this.tags = TagCollection.<CommandFunction>empty();
        this.functionCompilationLevel = integer;
        this.dispatcher = commandDispatcher;
    }
    
    public CompletableFuture<Void> reload(final PreparationBarrier a, final ResourceManager acf, final ProfilerFiller ant3, final ProfilerFiller ant4, final Executor executor5, final Executor executor6) {
        final CompletableFuture<Map<ResourceLocation, Tag.Builder>> completableFuture8 = this.tagsLoader.prepare(acf, executor5);
        final CompletableFuture<Map<ResourceLocation, CompletableFuture<CommandFunction>>> completableFuture9 = (CompletableFuture<Map<ResourceLocation, CompletableFuture<CommandFunction>>>)CompletableFuture.supplyAsync(() -> acf.listResources("functions", (Predicate<String>)(string -> string.endsWith(".mcfunction"))), executor5).thenCompose(collection -> {
            final Map<ResourceLocation, CompletableFuture<CommandFunction>> map5 = Maps.newHashMap();
            final CommandSourceStack db6 = new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, (ServerLevel)null, this.functionCompilationLevel, "", TextComponent.EMPTY, (MinecraftServer)null, (Entity)null);
            for (final ResourceLocation vk8 : collection) {
                final String string9 = vk8.getPath();
                final ResourceLocation vk9 = new ResourceLocation(vk8.getNamespace(), string9.substring(ServerFunctionLibrary.PATH_PREFIX_LENGTH, string9.length() - ServerFunctionLibrary.PATH_SUFFIX_LENGTH));
                map5.put(vk9, CompletableFuture.supplyAsync(() -> {
                    final List<String> list6 = readLines(acf, vk8);
                    return CommandFunction.fromLines(vk9, this.dispatcher, db6, list6);
                }, executor5));
            }
            final CompletableFuture<?>[] arr7 = map5.values().toArray((Object[])new CompletableFuture[0]);
            return (CompletionStage)CompletableFuture.allOf((CompletableFuture[])arr7).handle((void2, throwable) -> map5);
        });
        return (CompletableFuture<Void>)completableFuture8.thenCombine((CompletionStage)completableFuture9, Pair::of).thenCompose(a::wait).thenAcceptAsync(pair -> {
            final Map<ResourceLocation, CompletableFuture<CommandFunction>> map3 = (Map<ResourceLocation, CompletableFuture<CommandFunction>>)pair.getSecond();
            final ImmutableMap.Builder<ResourceLocation, CommandFunction> builder4 = ImmutableMap.<ResourceLocation, CommandFunction>builder();
            map3.forEach((vk, completableFuture) -> completableFuture.handle((cy, throwable) -> {
                if (throwable != null) {
                    ServerFunctionLibrary.LOGGER.error("Failed to load function {}", vk, throwable);
                }
                else {
                    builder4.put(vk, cy);
                }
                return null;
            }).join());
            this.functions = (Map<ResourceLocation, CommandFunction>)builder4.build();
            this.tags = this.tagsLoader.load((Map<ResourceLocation, Tag.Builder>)pair.getFirst());
        }, executor6);
    }
    
    private static List<String> readLines(final ResourceManager acf, final ResourceLocation vk) {
        try (final Resource ace3 = acf.getResource(vk)) {
            return IOUtils.readLines(ace3.getInputStream(), StandardCharsets.UTF_8);
        }
        catch (IOException iOException3) {
            throw new CompletionException((Throwable)iOException3);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PATH_PREFIX_LENGTH = "functions/".length();
        PATH_SUFFIX_LENGTH = ".mcfunction".length();
    }
}
