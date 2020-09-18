package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.Entity;
import com.google.common.collect.Lists;
import net.minecraft.commands.SharedSuggestionProvider;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import com.mojang.brigadier.StringReader;
import java.util.function.Supplier;
import java.util.Collections;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.arguments.ArgumentType;

public class ScoreHolderArgument implements ArgumentType<Result> {
    public static final SuggestionProvider<CommandSourceStack> SUGGEST_SCORE_HOLDERS;
    private static final Collection<String> EXAMPLES;
    private static final SimpleCommandExceptionType ERROR_NO_RESULTS;
    private final boolean multiple;
    
    public ScoreHolderArgument(final boolean boolean1) {
        this.multiple = boolean1;
    }
    
    public static String getName(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return (String)getNames(commandContext, string).iterator().next();
    }
    
    public static Collection<String> getNames(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return getNames(commandContext, string, (Supplier<Collection<String>>)Collections::emptyList);
    }
    
    public static Collection<String> getNamesWithDefaultWildcard(final CommandContext<CommandSourceStack> commandContext, final String string) throws CommandSyntaxException {
        return getNames(commandContext, string, (Supplier<Collection<String>>)commandContext.getSource().getServer().getScoreboard()::getTrackedPlayers);
    }
    
    public static Collection<String> getNames(final CommandContext<CommandSourceStack> commandContext, final String string, final Supplier<Collection<String>> supplier) throws CommandSyntaxException {
        final Collection<String> collection4 = commandContext.<Result>getArgument(string, Result.class).getNames(commandContext.getSource(), supplier);
        if (collection4.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        }
        return collection4;
    }
    
    public static ScoreHolderArgument scoreHolder() {
        return new ScoreHolderArgument(false);
    }
    
    public static ScoreHolderArgument scoreHolders() {
        return new ScoreHolderArgument(true);
    }
    
    public Result parse(final StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '@') {
            final EntitySelectorParser fd3 = new EntitySelectorParser(stringReader);
            final EntitySelector fc4 = fd3.parse();
            if (!this.multiple && fc4.getMaxResults() > 1) {
                throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
            }
            return new SelectorResult(fc4);
        }
        else {
            final int integer3 = stringReader.getCursor();
            while (stringReader.canRead() && stringReader.peek() != ' ') {
                stringReader.skip();
            }
            final String string4 = stringReader.getString().substring(integer3, stringReader.getCursor());
            if (string4.equals("*")) {
                final Collection<String> collection3;
                return (db, supplier) -> {
                    collection3 = (Collection<String>)supplier.get();
                    if (collection3.isEmpty()) {
                        throw ScoreHolderArgument.ERROR_NO_RESULTS.create();
                    }
                    else {
                        return collection3;
                    }
                };
            }
            final Collection<String> collection4 = (Collection<String>)Collections.singleton(string4);
            return (db, supplier) -> collection4;
        }
    }
    
    public Collection<String> getExamples() {
        return ScoreHolderArgument.EXAMPLES;
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     5: putstatic       net/minecraft/commands/arguments/ScoreHolderArgument.SUGGEST_SCORE_HOLDERS:Lcom/mojang/brigadier/suggestion/SuggestionProvider;
        //     8: iconst_4       
        //     9: anewarray       Ljava/lang/String;
        //    12: dup            
        //    13: iconst_0       
        //    14: ldc_w           "Player"
        //    17: aastore        
        //    18: dup            
        //    19: iconst_1       
        //    20: ldc_w           "0123"
        //    23: aastore        
        //    24: dup            
        //    25: iconst_2       
        //    26: ldc             "*"
        //    28: aastore        
        //    29: dup            
        //    30: iconst_3       
        //    31: ldc_w           "@e"
        //    34: aastore        
        //    35: invokestatic    java/util/Arrays.asList:([Ljava/lang/Object;)Ljava/util/List;
        //    38: putstatic       net/minecraft/commands/arguments/ScoreHolderArgument.EXAMPLES:Ljava/util/Collection;
        //    41: new             Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    44: dup            
        //    45: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    48: dup            
        //    49: ldc_w           "argument.scoreHolder.empty"
        //    52: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    55: invokespecial   com/mojang/brigadier/exceptions/SimpleCommandExceptionType.<init>:(Lcom/mojang/brigadier/Message;)V
        //    58: putstatic       net/minecraft/commands/arguments/ScoreHolderArgument.ERROR_NO_RESULTS:Lcom/mojang/brigadier/exceptions/SimpleCommandExceptionType;
        //    61: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 13
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:715)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferDynamicCall(TypeAnalysis.java:2241)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1022)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
        //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1829)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1072)
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
    
    public static class SelectorResult implements Result {
        private final EntitySelector selector;
        
        public SelectorResult(final EntitySelector fc) {
            this.selector = fc;
        }
        
        public Collection<String> getNames(final CommandSourceStack db, final Supplier<Collection<String>> supplier) throws CommandSyntaxException {
            final List<? extends Entity> list4 = this.selector.findEntities(db);
            if (list4.isEmpty()) {
                throw EntityArgument.NO_ENTITIES_FOUND.create();
            }
            final List<String> list5 = Lists.newArrayList();
            for (final Entity apx7 : list4) {
                list5.add(apx7.getScoreboardName());
            }
            return (Collection<String>)list5;
        }
    }
    
    public static class Serializer implements ArgumentSerializer<ScoreHolderArgument> {
        public void serializeToNetwork(final ScoreHolderArgument dz, final FriendlyByteBuf nf) {
            byte byte4 = 0;
            if (dz.multiple) {
                byte4 |= 0x1;
            }
            nf.writeByte(byte4);
        }
        
        public ScoreHolderArgument deserializeFromNetwork(final FriendlyByteBuf nf) {
            final byte byte3 = nf.readByte();
            final boolean boolean4 = (byte3 & 0x1) != 0x0;
            return new ScoreHolderArgument(boolean4);
        }
        
        public void serializeToJson(final ScoreHolderArgument dz, final JsonObject jsonObject) {
            jsonObject.addProperty("amount", dz.multiple ? "multiple" : "single");
        }
    }
    
    @FunctionalInterface
    public interface Result {
        Collection<String> getNames(final CommandSourceStack db, final Supplier<Collection<String>> supplier) throws CommandSyntaxException;
    }
}
