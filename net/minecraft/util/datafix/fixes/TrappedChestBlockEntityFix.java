package net.minecraft.util.datafix.fixes;

import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import java.util.Objects;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.types.templates.TaggedChoice;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.templates.List;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import org.apache.logging.log4j.Logger;
import com.mojang.datafixers.DataFix;

public class TrappedChestBlockEntityFix extends DataFix {
    private static final Logger LOGGER;
    
    public TrappedChestBlockEntityFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getOutputSchema().getType(References.CHUNK);
        final Type<?> type3 = type2.findFieldType("Level");
        final Type<?> type4 = type3.findFieldType("TileEntities");
        if (!(type4 instanceof List.ListType)) {
            throw new IllegalStateException("Tile entity type is not a list type.");
        }
        final List.ListType<?> listType5 = (List.ListType)type4;
        final OpticFinder<? extends java.util.List<?>> opticFinder6 = DSL.fieldFinder("TileEntities", listType5);
        final Type<?> type5 = this.getInputSchema().getType(References.CHUNK);
        final OpticFinder<?> opticFinder7 = type5.findField("Level");
        final OpticFinder<?> opticFinder8 = opticFinder7.type().findField("Sections");
        final Type<?> type6 = opticFinder8.type();
        if (!(type6 instanceof List.ListType)) {
            throw new IllegalStateException("Expecting sections to be a list.");
        }
        final Type<?> type7 = ((List.ListType)type6).getElement();
        final OpticFinder<?> opticFinder9 = DSL.typeFinder(type7);
        return TypeRewriteRule.seq(new AddNewChoices(this.getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY).makeRule(), this.fixTypeEverywhereTyped("Trapped Chest fix", type5, (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder7, typed -> {
            final Optional<? extends Typed<?>> optional6 = typed.getOptionalTyped(opticFinder8);
            if (!optional6.isPresent()) {
                return typed;
            }
            final java.util.List<? extends Typed<?>> list7 = ((Typed)optional6.get()).getAllTyped(opticFinder9);
            final IntSet intSet8 = new IntOpenHashSet();
            for (final Typed<?> typed2 : list7) {
                final TrappedChestSection a11 = new TrappedChestSection(typed2, this.getInputSchema());
                if (a11.isSkippable()) {
                    continue;
                }
                for (int integer12 = 0; integer12 < 4096; ++integer12) {
                    final int integer13 = a11.getBlock(integer12);
                    if (a11.isTrappedChest(integer13)) {
                        intSet8.add(a11.getIndex() << 12 | integer12);
                    }
                }
            }
            final Dynamic<?> dynamic9 = typed.<Dynamic<?>>get(DSL.remainderFinder());
            final int integer14 = dynamic9.get("xPos").asInt(0);
            final int integer15 = dynamic9.get("zPos").asInt(0);
            final TaggedChoice.TaggedChoiceType<String> taggedChoiceType12 = (TaggedChoice.TaggedChoiceType<String>)this.getInputSchema().findChoiceType(References.BLOCK_ENTITY);
            return typed.updateTyped(opticFinder6, typed -> typed.updateTyped(taggedChoiceType12.finder(), typed -> {
                final Dynamic<?> dynamic6 = typed.<Dynamic<?>>getOrCreate(DSL.remainderFinder());
                final int integer3 = dynamic6.get("x").asInt(0) - (integer14 << 4);
                final int integer4 = dynamic6.get("y").asInt(0);
                final int integer5 = dynamic6.get("z").asInt(0) - (integer15 << 4);
                if (intSet8.contains(LeavesFix.getIndex(integer3, integer4, integer5))) {
                    return typed.update(taggedChoiceType12.finder(), pair -> pair.mapFirst(string -> {
                        if (!Objects.equals(string, "minecraft:chest")) {
                            TrappedChestBlockEntityFix.LOGGER.warn("Block Entity was expected to be a chest");
                        }
                        return "minecraft:trapped_chest";
                    }));
                }
                return typed;
            }));
        }))));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static final class TrappedChestSection extends LeavesFix.Section {
        @Nullable
        private IntSet chestIds;
        
        public TrappedChestSection(final Typed<?> typed, final Schema schema) {
            super(typed, schema);
        }
        
        @Override
        protected boolean skippable() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: new             Lit/unimi/dsi/fastutil/ints/IntOpenHashSet;
            //     4: dup            
            //     5: invokespecial   it/unimi/dsi/fastutil/ints/IntOpenHashSet.<init>:()V
            //     8: putfield        net/minecraft/util/datafix/fixes/TrappedChestBlockEntityFix$TrappedChestSection.chestIds:Lit/unimi/dsi/fastutil/ints/IntSet;
            //    11: iconst_0       
            //    12: istore_1        /* integer2 */
            //    13: iload_1         /* integer2 */
            //    14: aload_0         /* this */
            //    15: getfield        net/minecraft/util/datafix/fixes/TrappedChestBlockEntityFix$TrappedChestSection.palette:Ljava/util/List;
            //    18: invokeinterface java/util/List.size:()I
            //    23: if_icmpge       78
            //    26: aload_0         /* this */
            //    27: getfield        net/minecraft/util/datafix/fixes/TrappedChestBlockEntityFix$TrappedChestSection.palette:Ljava/util/List;
            //    30: iload_1         /* integer2 */
            //    31: invokeinterface java/util/List.get:(I)Ljava/lang/Object;
            //    36: checkcast       Lcom/mojang/serialization/Dynamic;
            //    39: astore_2        /* dynamic3 */
            //    40: aload_2         /* dynamic3 */
            //    41: ldc             "Name"
            //    43: invokevirtual   com/mojang/serialization/Dynamic.get:(Ljava/lang/String;)Lcom/mojang/serialization/OptionalDynamic;
            //    46: ldc             ""
            //    48: invokevirtual   com/mojang/serialization/OptionalDynamic.asString:(Ljava/lang/String;)Ljava/lang/String;
            //    51: astore_3        /* string4 */
            //    52: aload_3         /* string4 */
            //    53: ldc             "minecraft:trapped_chest"
            //    55: invokestatic    java/util/Objects.equals:(Ljava/lang/Object;Ljava/lang/Object;)Z
            //    58: ifeq            72
            //    61: aload_0         /* this */
            //    62: getfield        net/minecraft/util/datafix/fixes/TrappedChestBlockEntityFix$TrappedChestSection.chestIds:Lit/unimi/dsi/fastutil/ints/IntSet;
            //    65: iload_1         /* integer2 */
            //    66: invokeinterface it/unimi/dsi/fastutil/ints/IntSet.add:(I)Z
            //    71: pop            
            //    72: iinc            integer2, 1
            //    75: goto            13
            //    78: aload_0         /* this */
            //    79: getfield        net/minecraft/util/datafix/fixes/TrappedChestBlockEntityFix$TrappedChestSection.chestIds:Lit/unimi/dsi/fastutil/ints/IntSet;
            //    82: invokeinterface it/unimi/dsi/fastutil/ints/IntSet.isEmpty:()Z
            //    87: ireturn        
            //    StackMapTable: 00 03 FC 00 0D 01 3A FA 00 05
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2024)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
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
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1061)
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
        
        public boolean isTrappedChest(final int integer) {
            return this.chestIds.contains(integer);
        }
    }
}
