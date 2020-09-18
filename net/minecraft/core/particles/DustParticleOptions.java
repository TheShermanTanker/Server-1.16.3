package net.minecraft.core.particles;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import java.util.Locale;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import com.mojang.serialization.Codec;

public class DustParticleOptions implements ParticleOptions {
    public static final DustParticleOptions REDSTONE;
    public static final Codec<DustParticleOptions> CODEC;
    public static final Deserializer<DustParticleOptions> DESERIALIZER;
    private final float r;
    private final float g;
    private final float b;
    private final float scale;
    
    public DustParticleOptions(final float float1, final float float2, final float float3, final float float4) {
        this.r = float1;
        this.g = float2;
        this.b = float3;
        this.scale = Mth.clamp(float4, 0.01f, 4.0f);
    }
    
    public void writeToNetwork(final FriendlyByteBuf nf) {
        nf.writeFloat(this.r);
        nf.writeFloat(this.g);
        nf.writeFloat(this.b);
        nf.writeFloat(this.scale);
    }
    
    public String writeToString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f", new Object[] { Registry.PARTICLE_TYPE.getKey(this.getType()), this.r, this.g, this.b, this.scale });
    }
    
    public ParticleType<DustParticleOptions> getType() {
        return ParticleTypes.DUST;
    }
    
    static {
        REDSTONE = new DustParticleOptions(1.0f, 0.0f, 0.0f, 1.0f);
        CODEC = RecordCodecBuilder.<DustParticleOptions>create((java.util.function.Function<RecordCodecBuilder.Instance<DustParticleOptions>, ? extends App<RecordCodecBuilder.Mu<DustParticleOptions>, DustParticleOptions>>)(instance -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       com/mojang/serialization/Codec.FLOAT:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //     4: ldc             "r"
            //     6: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    11: invokedynamic   BootstrapMethod #0, apply:()Ljava/util/function/Function;
            //    16: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    19: getstatic       com/mojang/serialization/Codec.FLOAT:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    22: ldc             "g"
            //    24: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    29: invokedynamic   BootstrapMethod #1, apply:()Ljava/util/function/Function;
            //    34: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    37: getstatic       com/mojang/serialization/Codec.FLOAT:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    40: ldc             "b"
            //    42: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    47: invokedynamic   BootstrapMethod #2, apply:()Ljava/util/function/Function;
            //    52: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    55: getstatic       com/mojang/serialization/Codec.FLOAT:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    58: ldc             "scale"
            //    60: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    65: invokedynamic   BootstrapMethod #3, apply:()Ljava/util/function/Function;
            //    70: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    73: invokevirtual   com/mojang/serialization/codecs/RecordCodecBuilder$Instance.group:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P4;
            //    76: aload_0         /* instance */
            //    77: invokedynamic   BootstrapMethod #4, apply:()Lcom/mojang/datafixers/util/Function4;
            //    82: invokevirtual   com/mojang/datafixers/Products$P4.apply:(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function4;)Lcom/mojang/datafixers/kinds/App;
            //    85: areturn        
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  instance  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 2
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:294)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.convertType(AstBuilder.java:173)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:647)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformByteCode(AstMethodBodyBuilder.java:554)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformExpression(AstMethodBodyBuilder.java:540)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformNode(AstMethodBodyBuilder.java:392)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.transformBlock(AstMethodBodyBuilder.java:333)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:294)
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
        }));
        DESERIALIZER = new Deserializer<DustParticleOptions>() {
            public DustParticleOptions fromCommand(final ParticleType<DustParticleOptions> hg, final StringReader stringReader) throws CommandSyntaxException {
                stringReader.expect(' ');
                final float float4 = (float)stringReader.readDouble();
                stringReader.expect(' ');
                final float float5 = (float)stringReader.readDouble();
                stringReader.expect(' ');
                final float float6 = (float)stringReader.readDouble();
                stringReader.expect(' ');
                final float float7 = (float)stringReader.readDouble();
                return new DustParticleOptions(float4, float5, float6, float7);
            }
            
            public DustParticleOptions fromNetwork(final ParticleType<DustParticleOptions> hg, final FriendlyByteBuf nf) {
                return new DustParticleOptions(nf.readFloat(), nf.readFloat(), nf.readFloat(), nf.readFloat());
            }
        };
    }
}
