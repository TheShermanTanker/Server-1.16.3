package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonDeserializationContext;
import net.minecraft.core.Registry;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import java.util.Set;
import net.minecraft.world.item.enchantment.Enchantment;

public class BonusLevelTableCondition implements LootItemCondition {
    private final Enchantment enchantment;
    private final float[] values;
    
    private BonusLevelTableCondition(final Enchantment bpp, final float[] arr) {
        this.enchantment = bpp;
        this.values = arr;
    }
    
    public LootItemConditionType getType() {
        return LootItemConditions.TABLE_BONUS;
    }
    
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.TOOL);
    }
    
    public boolean test(final LootContext cys) {
        final ItemStack bly3 = cys.<ItemStack>getParamOrNull(LootContextParams.TOOL);
        final int integer4 = (bly3 != null) ? EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, bly3) : 0;
        final float float5 = this.values[Math.min(integer4, this.values.length - 1)];
        return cys.getRandom().nextFloat() < float5;
    }
    
    public static Builder bonusLevelFlatChance(final Enchantment bpp, final float... arr) {
        return () -> new BonusLevelTableCondition(bpp, arr);
    }
    
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<BonusLevelTableCondition> {
        public void serialize(final JsonObject jsonObject, final BonusLevelTableCondition dbc, final JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("enchantment", Registry.ENCHANTMENT.getKey(dbc.enchantment).toString());
            jsonObject.add("chances", jsonSerializationContext.serialize(dbc.values));
        }
        
        public BonusLevelTableCondition deserialize(final JsonObject jsonObject, final JsonDeserializationContext jsonDeserializationContext) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: aload_1         /* jsonObject */
            //     5: ldc             "enchantment"
            //     7: invokestatic    net/minecraft/util/GsonHelper.getAsString:(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;
            //    10: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
            //    13: astore_3        /* vk4 */
            //    14: getstatic       net/minecraft/core/Registry.ENCHANTMENT:Lnet/minecraft/core/Registry;
            //    17: aload_3         /* vk4 */
            //    18: invokevirtual   net/minecraft/core/Registry.getOptional:(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;
            //    21: aload_3         /* vk4 */
            //    22: invokedynamic   BootstrapMethod #0, get:(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/function/Supplier;
            //    27: invokevirtual   java/util/Optional.orElseThrow:(Ljava/util/function/Supplier;)Ljava/lang/Object;
            //    30: checkcast       Lnet/minecraft/world/item/enchantment/Enchantment;
            //    33: astore          bpp5
            //    35: aload_1         /* jsonObject */
            //    36: ldc             "chances"
            //    38: aload_2         /* jsonDeserializationContext */
            //    39: ldc             [F.class
            //    41: invokestatic    net/minecraft/util/GsonHelper.getAsObject:(Lcom/google/gson/JsonObject;Ljava/lang/String;Lcom/google/gson/JsonDeserializationContext;Ljava/lang/Class;)Ljava/lang/Object;
            //    44: checkcast       [F
            //    47: astore          arr6
            //    49: new             Lnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition;
            //    52: dup            
            //    53: aload           bpp5
            //    55: aload           arr6
            //    57: aconst_null    
            //    58: invokespecial   net/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition.<init>:(Lnet/minecraft/world/item/enchantment/Enchantment;[FLnet/minecraft/world/level/storage/loot/predicates/BonusLevelTableCondition$1;)V
            //    61: areturn        
            //    MethodParameters:
            //  Name                        Flags  
            //  --------------------------  -----
            //  jsonObject                  
            //  jsonDeserializationContext  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2362)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
            //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1411)
            //     at com.strobel.decompiler.ast.TypeAnalysis.isSameType(TypeAnalysis.java:3072)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:790)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:881)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
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
    }
}
