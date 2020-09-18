package net.minecraft.util.datafix.fixes;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import java.util.Optional;
import java.util.stream.Stream;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.mojang.datafixers.DataFix;

public class ItemStackEnchantmentNamesFix extends DataFix {
    private static final Int2ObjectMap<String> MAP;
    
    public ItemStackEnchantmentNamesFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.ITEM_STACK);
        final OpticFinder<?> opticFinder3 = type2.findField("tag");
        return this.fixTypeEverywhereTyped("ItemStackEnchantmentFix", type2, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder3, typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), this::fixTag))));
    }
    
    private Dynamic<?> fixTag(Dynamic<?> dynamic) {
        final Optional<? extends Dynamic<?>> optional3 = dynamic.get("ench").asStreamOpt().map((java.util.function.Function<? super java.util.stream.Stream<Dynamic<?>>, ?>)(stream -> stream.map(dynamic -> dynamic.set("id", (Dynamic)dynamic.createString(ItemStackEnchantmentNamesFix.MAP.getOrDefault(dynamic.get("id").asInt(0), "null")))))).map((java.util.function.Function<? super Object, ?>)dynamic::createList).result();
        if (optional3.isPresent()) {
            dynamic = dynamic.remove("ench").set("Enchantments", optional3.get());
        }
        return dynamic.update("StoredEnchantments", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> DataFixUtils.<Dynamic>orElse(dynamic.asStreamOpt().map(stream -> stream.map(dynamic -> dynamic.set("id", (Dynamic)dynamic.createString(ItemStackEnchantmentNamesFix.MAP.getOrDefault(dynamic.get("id").asInt(0), "null"))))).map(dynamic::createList).result(), dynamic)));
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.<init>:()V
        //     7: invokedynamic   BootstrapMethod #9, accept:()Ljava/util/function/Consumer;
        //    12: invokestatic    com/mojang/datafixers/DataFixUtils.make:(Ljava/lang/Object;Ljava/util/function/Consumer;)Ljava/lang/Object;
        //    15: checkcast       Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
        //    18: putstatic       net/minecraft/util/datafix/fixes/ItemStackEnchantmentNamesFix.MAP:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
        //    21: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:128)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:626)
        //     at com.strobel.assembler.metadata.MethodReference.resolve(MethodReference.java:177)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2438)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
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
