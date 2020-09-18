package net.minecraft.world.level.storage.loot;

import org.apache.commons.lang3.ArrayUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.util.Mth;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntry;
import org.apache.commons.lang3.mutable.MutableInt;
import com.google.common.collect.Lists;
import java.util.function.Consumer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.item.ItemStack;
import java.util.function.BiFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import java.util.function.Predicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

public class LootPool {
    private final LootPoolEntryContainer[] entries;
    private final LootItemCondition[] conditions;
    private final Predicate<LootContext> compositeCondition;
    private final LootItemFunction[] functions;
    private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
    private final RandomIntGenerator rolls;
    private final RandomValueBounds bonusRolls;
    
    private LootPool(final LootPoolEntryContainer[] arr, final LootItemCondition[] arr, final LootItemFunction[] arr, final RandomIntGenerator cyy, final RandomValueBounds cza) {
        this.entries = arr;
        this.conditions = arr;
        this.compositeCondition = LootItemConditions.<LootContext>andConditions((java.util.function.Predicate<LootContext>[])arr);
        this.functions = arr;
        this.compositeFunction = LootItemFunctions.compose(arr);
        this.rolls = cyy;
        this.bonusRolls = cza;
    }
    
    private void addRandomItem(final Consumer<ItemStack> consumer, final LootContext cys) {
        final Random random4 = cys.getRandom();
        final List<LootPoolEntry> list5 = Lists.newArrayList();
        final MutableInt mutableInt6 = new MutableInt();
        for (final LootPoolEntryContainer czn10 : this.entries) {
            czn10.expand(cys, (Consumer<LootPoolEntry>)(czm -> {
                final int integer5 = czm.getWeight(cys.getLuck());
                if (integer5 > 0) {
                    list5.add(czm);
                    mutableInt6.add(integer5);
                }
            }));
        }
        final int integer7 = list5.size();
        if (mutableInt6.intValue() == 0 || integer7 == 0) {
            return;
        }
        if (integer7 == 1) {
            ((LootPoolEntry)list5.get(0)).createItemStack(consumer, cys);
            return;
        }
        int integer8 = random4.nextInt(mutableInt6.intValue());
        for (final LootPoolEntry czm10 : list5) {
            integer8 -= czm10.getWeight(cys.getLuck());
            if (integer8 < 0) {
                czm10.createItemStack(consumer, cys);
            }
        }
    }
    
    public void addRandomItems(final Consumer<ItemStack> consumer, final LootContext cys) {
        if (!this.compositeCondition.test(cys)) {
            return;
        }
        final Consumer<ItemStack> consumer2 = LootItemFunction.decorate(this.compositeFunction, consumer, cys);
        final Random random5 = cys.getRandom();
        for (int integer6 = this.rolls.getInt(random5) + Mth.floor(this.bonusRolls.getFloat(random5) * cys.getLuck()), integer7 = 0; integer7 < integer6; ++integer7) {
            this.addRandomItem(consumer2, cys);
        }
    }
    
    public void validate(final ValidationContext czd) {
        for (int integer3 = 0; integer3 < this.conditions.length; ++integer3) {
            this.conditions[integer3].validate(czd.forChild(new StringBuilder().append(".condition[").append(integer3).append("]").toString()));
        }
        for (int integer3 = 0; integer3 < this.functions.length; ++integer3) {
            this.functions[integer3].validate(czd.forChild(new StringBuilder().append(".functions[").append(integer3).append("]").toString()));
        }
        for (int integer3 = 0; integer3 < this.entries.length; ++integer3) {
            this.entries[integer3].validate(czd.forChild(new StringBuilder().append(".entries[").append(integer3).append("]").toString()));
        }
    }
    
    public static Builder lootPool() {
        return new Builder();
    }
    
    public static class Builder implements FunctionUserBuilder<Builder>, ConditionUserBuilder<Builder> {
        private final List<LootPoolEntryContainer> entries;
        private final List<LootItemCondition> conditions;
        private final List<LootItemFunction> functions;
        private RandomIntGenerator rolls;
        private RandomValueBounds bonusRolls;
        
        public Builder() {
            this.entries = Lists.newArrayList();
            this.conditions = Lists.newArrayList();
            this.functions = Lists.newArrayList();
            this.rolls = new RandomValueBounds(1.0f);
            this.bonusRolls = new RandomValueBounds(0.0f, 0.0f);
        }
        
        public Builder setRolls(final RandomIntGenerator cyy) {
            this.rolls = cyy;
            return this;
        }
        
        public Builder unwrap() {
            return this;
        }
        
        public Builder add(final LootPoolEntryContainer.Builder<?> a) {
            this.entries.add(a.build());
            return this;
        }
        
        public Builder when(final LootItemCondition.Builder a) {
            this.conditions.add(a.build());
            return this;
        }
        
        public Builder apply(final LootItemFunction.Builder a) {
            this.functions.add(a.build());
            return this;
        }
        
        public LootPool build() {
            if (this.rolls == null) {
                throw new IllegalArgumentException("Rolls not set");
            }
            return new LootPool((LootPoolEntryContainer[])this.entries.toArray((Object[])new LootPoolEntryContainer[0]), (LootItemCondition[])this.conditions.toArray((Object[])new LootItemCondition[0]), (LootItemFunction[])this.functions.toArray((Object[])new LootItemFunction[0]), this.rolls, this.bonusRolls, null);
        }
    }
    
    public static class Serializer implements JsonDeserializer<LootPool>, JsonSerializer<LootPool> {
        public LootPool deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: ldc             "loot pool"
            //     3: invokestatic    net/minecraft/util/GsonHelper.convertToJsonObject:(Lcom/google/gson/JsonElement;Ljava/lang/String;)Lcom/google/gson/JsonObject;
            //     6: astore          jsonObject5
            //     8: aload           jsonObject5
            //    10: ldc             "entries"
            //    12: aload_3         /* jsonDeserializationContext */
            //    13: ldc             [Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer;.class
            //    15: invokestatic    net/minecraft/util/GsonHelper.getAsObject:(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonDeserializationContext;Ljava/lang/Class;)Ljava/lang/Object;
            //    18: checkcast       [Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer;
            //    21: astore          arr6
            //    23: aload           jsonObject5
            //    25: ldc             "conditions"
            //    27: iconst_0       
            //    28: anewarray       Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition;
            //    31: aload_3         /* jsonDeserializationContext */
            //    32: ldc             [Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition;.class
            //    34: invokestatic    net/minecraft/util/GsonHelper.getAsObject:(Lcom/google/gson/JsonObject;Ljava/lang/String;Ljava/lang/Object;Lcom/google/gson/JsonDeserializationContext;Ljava/lang/Class;)Ljava/lang/Object;
            //    37: checkcast       [Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition;
            //    40: astore          arr7
            //    42: aload           jsonObject5
            //    44: ldc             "functions"
            //    46: iconst_0       
            //    47: anewarray       Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;
            //    50: aload_3         /* jsonDeserializationContext */
            //    51: ldc             [Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;.class
            //    53: invokestatic    net/minecraft/util/GsonHelper.getAsObject:(Lcom/google/gson/JsonObject;Ljava/lang/String;Ljava/lang/Object;Lcom/google/gson/JsonDeserializationContext;Ljava/lang/Class;)Ljava/lang/Object;
            //    56: checkcast       [Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;
            //    59: astore          arr8
            //    61: aload           jsonObject5
            //    63: ldc             "rolls"
            //    65: invokevirtual   com/google/gson/JsonObject.get:(Ljava/lang/String;)Lcom/google/gson/JsonElement;
            //    68: aload_3         /* jsonDeserializationContext */
            //    69: invokestatic    net/minecraft/world/level/storage/loot/RandomIntGenerators.deserialize:(Lcom/google/gson/JsonElement;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;
            //    72: astore          cyy9
            //    74: aload           jsonObject5
            //    76: ldc             "bonus_rolls"
            //    78: new             Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
            //    81: dup            
            //    82: fconst_0       
            //    83: fconst_0       
            //    84: invokespecial   net/minecraft/world/level/storage/loot/RandomValueBounds.<init>:(FF)V
            //    87: aload_3         /* jsonDeserializationContext */
            //    88: ldc             Lnet/minecraft/world/level/storage/loot/RandomValueBounds;.class
            //    90: invokestatic    net/minecraft/util/GsonHelper.getAsObject:(Lcom/google/gson/JsonObject;Ljava/lang/String;Ljava/lang/Object;Lcom/google/gson/JsonDeserializationContext;Ljava/lang/Class;)Ljava/lang/Object;
            //    93: checkcast       Lnet/minecraft/world/level/storage/loot/RandomValueBounds;
            //    96: astore          cza10
            //    98: new             Lnet/minecraft/world/level/storage/loot/LootPool;
            //   101: dup            
            //   102: aload           arr6
            //   104: aload           arr7
            //   106: aload           arr8
            //   108: aload           cyy9
            //   110: aload           cza10
            //   112: aconst_null    
            //   113: invokespecial   net/minecraft/world/level/storage/loot/LootPool.<init>:([Lnet/minecraft/world/level/storage/loot/entries/LootPoolEntryContainer;[Lnet/minecraft/world/level/storage/loot/predicates/LootItemCondition;[Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;Lnet/minecraft/world/level/storage/loot/RandomIntGenerator;Lnet/minecraft/world/level/storage/loot/RandomValueBounds;Lnet/minecraft/world/level/storage/loot/LootPool$1;)V
            //   116: areturn        
            //    Exceptions:
            //  throws com.google.gson.JsonParseException
            //    MethodParameters:
            //  Name                        Flags  
            //  --------------------------  -----
            //  jsonElement                 
            //  type                        
            //  jsonDeserializationContext  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
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
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
            //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:710)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
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
        
        public JsonElement serialize(final LootPool cyu, final Type type, final JsonSerializationContext jsonSerializationContext) {
            final JsonObject jsonObject5 = new JsonObject();
            jsonObject5.add("rolls", RandomIntGenerators.serialize(cyu.rolls, jsonSerializationContext));
            jsonObject5.add("entries", jsonSerializationContext.serialize(cyu.entries));
            if (cyu.bonusRolls.getMin() != 0.0f && cyu.bonusRolls.getMax() != 0.0f) {
                jsonObject5.add("bonus_rolls", jsonSerializationContext.serialize(cyu.bonusRolls));
            }
            if (!ArrayUtils.isEmpty(cyu.conditions)) {
                jsonObject5.add("conditions", jsonSerializationContext.serialize(cyu.conditions));
            }
            if (!ArrayUtils.isEmpty(cyu.functions)) {
                jsonObject5.add("functions", jsonSerializationContext.serialize(cyu.functions));
            }
            return jsonObject5;
        }
    }
}
