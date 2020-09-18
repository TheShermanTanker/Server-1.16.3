package net.minecraft.world.level.levelgen.structure;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.Util;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import java.util.Random;
import java.util.List;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.resources.ResourceLocation;

public class NetherFossilPieces {
    private static final ResourceLocation[] FOSSILS;
    
    public static void addPieces(final StructureManager cst, final List<StructurePiece> list, final Random random, final BlockPos fx) {
        final Rotation bzj5 = Rotation.getRandom(random);
        list.add(new NetherFossilPiece(cst, Util.<ResourceLocation>getRandom(NetherFossilPieces.FOSSILS, random), fx, bzj5));
    }
    
    static {
        FOSSILS = new ResourceLocation[] { new ResourceLocation("nether_fossils/fossil_1"), new ResourceLocation("nether_fossils/fossil_2"), new ResourceLocation("nether_fossils/fossil_3"), new ResourceLocation("nether_fossils/fossil_4"), new ResourceLocation("nether_fossils/fossil_5"), new ResourceLocation("nether_fossils/fossil_6"), new ResourceLocation("nether_fossils/fossil_7"), new ResourceLocation("nether_fossils/fossil_8"), new ResourceLocation("nether_fossils/fossil_9"), new ResourceLocation("nether_fossils/fossil_10"), new ResourceLocation("nether_fossils/fossil_11"), new ResourceLocation("nether_fossils/fossil_12"), new ResourceLocation("nether_fossils/fossil_13"), new ResourceLocation("nether_fossils/fossil_14") };
    }
    
    public static class NetherFossilPiece extends TemplateStructurePiece {
        private final ResourceLocation templateLocation;
        private final Rotation rotation;
        
        public NetherFossilPiece(final StructureManager cst, final ResourceLocation vk, final BlockPos fx, final Rotation bzj) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       net/minecraft/world/level/levelgen/feature/StructurePieceType.NETHER_FOSSIL:Lnet/minecraft/world/level/levelgen/feature/StructurePieceType;
            //     4: iconst_0       
            //     5: invokespecial   net/minecraft/world/level/levelgen/structure/TemplateStructurePiece.<init>:(Lnet/minecraft/world/level/levelgen/feature/StructurePieceType;I)V
            //     8: aload_0         /* this */
            //     9: aload_2         /* vk */
            //    10: putfield        net/minecraft/world/level/levelgen/structure/NetherFossilPieces$NetherFossilPiece.templateLocation:Lnet/minecraft/resources/ResourceLocation;
            //    13: aload_0         /* this */
            //    14: aload_3         /* fx */
            //    15: putfield        net/minecraft/world/level/levelgen/structure/NetherFossilPieces$NetherFossilPiece.templatePosition:Lnet/minecraft/core/BlockPos;
            //    18: aload_0         /* this */
            //    19: aload           bzj
            //    21: putfield        net/minecraft/world/level/levelgen/structure/NetherFossilPieces$NetherFossilPiece.rotation:Lnet/minecraft/world/level/block/Rotation;
            //    24: aload_0         /* this */
            //    25: aload_1         /* cst */
            //    26: invokespecial   net/minecraft/world/level/levelgen/structure/NetherFossilPieces$NetherFossilPiece.loadTemplate:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureManager;)V
            //    29: return         
            //    MethodParameters:
            //  Name  Flags  
            //  ----  -----
            //  cst   
            //  vk    
            //  fx    
            //  bzj   
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 38
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.ClassFileReader.populateNamedInnerTypes(ClassFileReader.java:698)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:442)
            //     at com.strobel.assembler.metadata.ClassFileReader.readClass(ClassFileReader.java:377)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveType(MetadataSystem.java:129)
            //     at com.strobel.assembler.metadata.MetadataSystem.resolveCore(MetadataSystem.java:81)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:104)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:111)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:621)
            //     at com.strobel.assembler.metadata.FieldReference.resolve(FieldReference.java:61)
            //     at com.strobel.decompiler.ast.TypeAnalysis.getFieldType(TypeAnalysis.java:2920)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1051)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createConstructor(AstBuilder.java:713)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:549)
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
        
        public NetherFossilPiece(final StructureManager cst, final CompoundTag md) {
            super(StructurePieceType.NETHER_FOSSIL, md);
            this.templateLocation = new ResourceLocation(md.getString("Template"));
            this.rotation = Rotation.valueOf(md.getString("Rot"));
            this.loadTemplate(cst);
        }
        
        private void loadTemplate(final StructureManager cst) {
            final StructureTemplate csy3 = cst.getOrCreate(this.templateLocation);
            final StructurePlaceSettings csu4 = new StructurePlaceSettings().setRotation(this.rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
            this.setup(csy3, this.templatePosition, csu4);
        }
        
        @Override
        protected void addAdditionalSaveData(final CompoundTag md) {
            super.addAdditionalSaveData(md);
            md.putString("Template", this.templateLocation.toString());
            md.putString("Rot", this.rotation.name());
        }
        
        @Override
        protected void handleDataMarker(final String string, final BlockPos fx, final ServerLevelAccessor bsh, final Random random, final BoundingBox cqx) {
        }
        
        @Override
        public boolean postProcess(final WorldGenLevel bso, final StructureFeatureManager bsk, final ChunkGenerator cfv, final Random random, final BoundingBox cqx, final ChunkPos bra, final BlockPos fx) {
            cqx.expand(this.template.getBoundingBox(this.placeSettings, this.templatePosition));
            return super.postProcess(bso, bsk, cfv, random, cqx, bra, fx);
        }
    }
}
