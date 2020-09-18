package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import com.google.common.collect.Iterables;
import java.util.function.Consumer;
import net.minecraft.commands.SharedSuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.ImmutableStringReader;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import com.mojang.brigadier.StringReader;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.arguments.selector.EntitySelector;
import com.mojang.brigadier.arguments.ArgumentType;

public class EntityArgument implements ArgumentType<EntitySelector> {
    private static final Collection<String> EXAMPLES;
    public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_ENTITY;
    public static final SimpleCommandExceptionType ERROR_NOT_SINGLE_PLAYER;
    public static final SimpleCommandExceptionType ERROR_ONLY_PLAYERS_ALLOWED;
    public static final SimpleCommandExceptionType NO_ENTITIES_FOUND;
    public static final SimpleCommandExceptionType NO_PLAYERS_FOUND;
    public static final SimpleCommandExceptionType ERROR_SELECTORS_NOT_ALLOWED;
    private final boolean single;
    private final boolean playersOnly;
    
    protected EntityArgument(final boolean boolean1, final boolean boolean2) {
        this.single = boolean1;
        this.playersOnly = boolean2;
    }
    
    public static EntityArgument entity() {
        return new EntityArgument(true, false);
    }
    
    public static Entity getEntity(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return commandContext.<EntitySelector>getArgument(string, EntitySelector.class).findSingleEntity(commandContext.getSource());
    }
    
    public static EntityArgument entities() {
        return new EntityArgument(false, false);
    }
    
    public static Collection<? extends Entity> getEntities(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final Collection<? extends Entity> collection3 = getOptionalEntities(commandContext, string);
        if (collection3.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        }
        return collection3;
    }
    
    public static Collection<? extends Entity> getOptionalEntities(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return commandContext.<EntitySelector>getArgument(string, EntitySelector.class).findEntities(commandContext.getSource());
    }
    
    public static Collection<ServerPlayer> getOptionalPlayers(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return (Collection<ServerPlayer>)commandContext.<EntitySelector>getArgument(string, EntitySelector.class).findPlayers(commandContext.getSource());
    }
    
    public static EntityArgument player() {
        return new EntityArgument(true, true);
    }
    
    public static ServerPlayer getPlayer(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return commandContext.<EntitySelector>getArgument(string, EntitySelector.class).findSinglePlayer(commandContext.getSource());
    }
    
    public static EntityArgument players() {
        return new EntityArgument(false, true);
    }
    
    public static Collection<ServerPlayer> getPlayers(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        final List<ServerPlayer> list3 = commandContext.<EntitySelector>getArgument(string, EntitySelector.class).findPlayers(commandContext.getSource());
        if (list3.isEmpty()) {
            throw EntityArgument.NO_PLAYERS_FOUND.create();
        }
        return (Collection<ServerPlayer>)list3;
    }
    
    public EntitySelector parse(final StringReader stringReader) throws CommandSyntaxException {
        final int integer3 = 0;
        final EntitySelectorParser fd4 = new EntitySelectorParser(stringReader);
        final EntitySelector fc5 = fd4.parse();
        if (fc5.getMaxResults() > 1 && this.single) {
            if (this.playersOnly) {
                stringReader.setCursor(0);
                throw EntityArgument.ERROR_NOT_SINGLE_PLAYER.createWithContext(stringReader);
            }
            stringReader.setCursor(0);
            throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.createWithContext(stringReader);
        }
        else {
            if (fc5.includesEntities() && this.playersOnly && !fc5.isSelfSelector()) {
                stringReader.setCursor(0);
                throw EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(stringReader);
            }
            return fc5;
        }
    }
    
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> commandContext, final SuggestionsBuilder suggestionsBuilder) {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            final StringReader stringReader4 = new StringReader(suggestionsBuilder.getInput());
            stringReader4.setCursor(suggestionsBuilder.getStart());
            final SharedSuggestionProvider dd5 = (SharedSuggestionProvider)commandContext.getSource();
            final EntitySelectorParser fd6 = new EntitySelectorParser(stringReader4, dd5.hasPermission(2));
            try {
                fd6.parse();
            }
            catch (CommandSyntaxException ex) {}
            return fd6.fillSuggestions(suggestionsBuilder, (Consumer<SuggestionsBuilder>)(suggestionsBuilder -> {
                final Collection<String> collection4 = dd5.getOnlinePlayerNames();
                final Iterable<String> iterable5 = (Iterable<String>)(this.playersOnly ? collection4 : Iterables.concat((java.lang.Iterable<?>)collection4, (java.lang.Iterable<?>)dd5.getSelectedEntities()));
                SharedSuggestionProvider.suggest(iterable5, suggestionsBuilder);
            }));
        }
        return Suggestions.empty();
    }
    
    public Collection<String> getExamples() {
        return EntityArgument.EXAMPLES;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: anewarray       Ljava/lang/String;
        //     4: dup            
        //     5: iconst_0       
        //     6: ldc_w           "Player"
        //     9: aastore        
        //    10: dup            
        //    11: iconst_1       
        //    12: ldc_w           "0123"
        //    15: aastore        
        //    16: dup            
        //    17: iconst_2       
        //    18: ldc_w           "@e"
        //    21: aastore        
        //    22: dup            
        //    23: iconst_3       
        //    24: ldc_w           "@e[type=foo]"
        //    27: aastore        
        //    28: dup            
        //    29: iconst_4       
        //    30: ldc_w           "dd12be42-52a9-4a91-a8a1-11c01849e498"
        //    33: aastore        
        //    34: invokestatic    java/util/Arrays.asList:([Ljava/lang/Object;)Ljava/util/List;
        //    37: putstatic       net/minecraft/commands/arguments/EntityArgument.EXAMPLES:Ljava/util/Collection;
        //    40: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    43: dup            
        //    44: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    47: dup            
        //    48: ldc_w           "argument.entity.toomany"
        //    51: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    54: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //    57: putstatic       net/minecraft/commands/arguments/EntityArgument.ERROR_NOT_SINGLE_ENTITY:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    60: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    63: dup            
        //    64: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    67: dup            
        //    68: ldc_w           "argument.player.toomany"
        //    71: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    74: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //    77: putstatic       net/minecraft/commands/arguments/EntityArgument.ERROR_NOT_SINGLE_PLAYER:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    80: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    83: dup            
        //    84: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    87: dup            
        //    88: ldc_w           "argument.player.entities"
        //    91: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    94: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //    97: putstatic       net/minecraft/commands/arguments/EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   100: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   103: dup            
        //   104: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   107: dup            
        //   108: ldc_w           "argument.entity.notfound.entity"
        //   111: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   114: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //   117: putstatic       net/minecraft/commands/arguments/EntityArgument.NO_ENTITIES_FOUND:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   120: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   123: dup            
        //   124: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   127: dup            
        //   128: ldc_w           "argument.entity.notfound.player"
        //   131: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   134: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //   137: putstatic       net/minecraft/commands/arguments/EntityArgument.NO_PLAYERS_FOUND:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   140: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   143: dup            
        //   144: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   147: dup            
        //   148: ldc_w           "argument.entity.selector.not_allowed"
        //   151: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   154: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //   157: putstatic       net/minecraft/commands/arguments/EntityArgument.ERROR_SELECTORS_NOT_ALLOWED:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //   160: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 13
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
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
    
    public static class Serializer implements ArgumentSerializer<EntityArgument> {
        public void serializeToNetwork(final EntityArgument dk, final FriendlyByteBuf nf) {
            byte byte4 = 0;
            if (dk.single) {
                byte4 |= 0x1;
            }
            if (dk.playersOnly) {
                byte4 |= 0x2;
            }
            nf.writeByte(byte4);
        }
        
        public EntityArgument deserializeFromNetwork(final FriendlyByteBuf nf) {
            final byte byte3 = nf.readByte();
            return new EntityArgument((byte3 & 0x1) != 0x0, (byte3 & 0x2) != 0x0);
        }
        
        public void serializeToJson(final EntityArgument dk, final JsonObject jsonObject) {
            jsonObject.addProperty("amount", dk.single ? "single" : "multiple");
            jsonObject.addProperty("type", dk.playersOnly ? "players" : "entities");
        }
    }
}
