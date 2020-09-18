package net.minecraft.world.level.chunk.storage;

import java.io.IOException;
import javax.annotation.Nullable;
import java.io.OutputStream;
import java.io.InputStream;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class RegionFileVersion {
    private static final Int2ObjectMap<RegionFileVersion> VERSIONS;
    public static final RegionFileVersion VERSION_GZIP;
    public static final RegionFileVersion VERSION_DEFLATE;
    public static final RegionFileVersion VERSION_NONE;
    private final int id;
    private final StreamWrapper<InputStream> inputWrapper;
    private final StreamWrapper<OutputStream> outputWrapper;
    
    private RegionFileVersion(final int integer, final StreamWrapper<InputStream> a2, final StreamWrapper<OutputStream> a3) {
        this.id = integer;
        this.inputWrapper = a2;
        this.outputWrapper = a3;
    }
    
    private static RegionFileVersion register(final RegionFileVersion cgx) {
        RegionFileVersion.VERSIONS.put(cgx.id, cgx);
        return cgx;
    }
    
    @Nullable
    public static RegionFileVersion fromId(final int integer) {
        return RegionFileVersion.VERSIONS.get(integer);
    }
    
    public static boolean isValidVersion(final int integer) {
        return RegionFileVersion.VERSIONS.containsKey(integer);
    }
    
    public int getId() {
        return this.id;
    }
    
    public OutputStream wrap(final OutputStream outputStream) throws IOException {
        return this.outputWrapper.wrap(outputStream);
    }
    
    public InputStream wrap(final InputStream inputStream) throws IOException {
        return this.inputWrapper.wrap(inputStream);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   it/unimi/dsi/fastutil/ints/Int2ObjectOpenHashMap.<init>:()V
        //     7: putstatic       net/minecraft/world/level/chunk/storage/RegionFileVersion.VERSIONS:Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
        //    10: new             Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    13: dup            
        //    14: iconst_1       
        //    15: invokedynamic   BootstrapMethod #0, wrap:()Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;
        //    20: invokedynamic   BootstrapMethod #1, wrap:()Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;
        //    25: invokespecial   net/minecraft/world/level/chunk/storage/RegionFileVersion.<init>:(ILnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;)V
        //    28: invokestatic    net/minecraft/world/level/chunk/storage/RegionFileVersion.register:(Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;)Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    31: putstatic       net/minecraft/world/level/chunk/storage/RegionFileVersion.VERSION_GZIP:Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    34: new             Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    37: dup            
        //    38: iconst_2       
        //    39: invokedynamic   BootstrapMethod #2, wrap:()Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;
        //    44: invokedynamic   BootstrapMethod #3, wrap:()Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;
        //    49: invokespecial   net/minecraft/world/level/chunk/storage/RegionFileVersion.<init>:(ILnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;)V
        //    52: invokestatic    net/minecraft/world/level/chunk/storage/RegionFileVersion.register:(Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;)Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    55: putstatic       net/minecraft/world/level/chunk/storage/RegionFileVersion.VERSION_DEFLATE:Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    58: new             Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    61: dup            
        //    62: iconst_3       
        //    63: invokedynamic   BootstrapMethod #4, wrap:()Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;
        //    68: invokedynamic   BootstrapMethod #5, wrap:()Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;
        //    73: invokespecial   net/minecraft/world/level/chunk/storage/RegionFileVersion.<init>:(ILnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;Lnet/minecraft/world/level/chunk/storage/RegionFileVersion$StreamWrapper;)V
        //    76: invokestatic    net/minecraft/world/level/chunk/storage/RegionFileVersion.register:(Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;)Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    79: putstatic       net/minecraft/world/level/chunk/storage/RegionFileVersion.VERSION_NONE:Lnet/minecraft/world/level/chunk/storage/RegionFileVersion;
        //    82: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferDynamicCall(TypeAnalysis.java:2262)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1022)
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
    
    @FunctionalInterface
    interface StreamWrapper<O> {
        O wrap(final O object) throws IOException;
    }
}
