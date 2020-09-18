package net.minecraft.world.level.levelgen.flat;

import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.function.Function;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.data.worldgen.StructureFeatures;
import java.util.HashMap;
import net.minecraft.world.level.levelgen.feature.configurations.StrongholdConfiguration;
import com.google.common.collect.Maps;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.Blocks;
import java.util.Arrays;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Optional;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Supplier;
import java.util.List;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.Map;
import com.mojang.serialization.Codec;
import org.apache.logging.log4j.Logger;

public class FlatLevelGeneratorSettings {
    private static final Logger LOGGER;
    public static final Codec<FlatLevelGeneratorSettings> CODEC;
    private static final Map<StructureFeature<?>, ConfiguredStructureFeature<?, ?>> STRUCTURE_FEATURES;
    private final Registry<Biome> biomes;
    private final StructureSettings structureSettings;
    private final List<FlatLayerInfo> layersInfo;
    private Supplier<Biome> biome;
    private final BlockState[] layers;
    private boolean voidGen;
    private boolean decoration;
    private boolean addLakes;
    
    public FlatLevelGeneratorSettings(final Registry<Biome> gm, final StructureSettings chs, final List<FlatLayerInfo> list, final boolean boolean4, final boolean boolean5, final Optional<Supplier<Biome>> optional) {
        this(chs, gm);
        if (boolean4) {
            this.setAddLakes();
        }
        if (boolean5) {
            this.setDecoration();
        }
        this.layersInfo.addAll((Collection)list);
        this.updateLayers();
        if (!optional.isPresent()) {
            FlatLevelGeneratorSettings.LOGGER.error("Unknown biome, defaulting to plains");
            this.biome = (Supplier<Biome>)(() -> gm.getOrThrow(Biomes.PLAINS));
        }
        else {
            this.biome = (Supplier<Biome>)optional.get();
        }
    }
    
    public FlatLevelGeneratorSettings(final StructureSettings chs, final Registry<Biome> gm) {
        this.layersInfo = Lists.newArrayList();
        this.layers = new BlockState[256];
        this.decoration = false;
        this.addLakes = false;
        this.biomes = gm;
        this.structureSettings = chs;
        this.biome = (Supplier<Biome>)(() -> gm.getOrThrow(Biomes.PLAINS));
    }
    
    public void setDecoration() {
        this.decoration = true;
    }
    
    public void setAddLakes() {
        this.addLakes = true;
    }
    
    public Biome getBiomeFromSettings() {
        final Biome bss2 = this.getBiome();
        final BiomeGenerationSettings bst3 = bss2.getGenerationSettings();
        final BiomeGenerationSettings.Builder a4 = new BiomeGenerationSettings.Builder().surfaceBuilder(bst3.getSurfaceBuilder());
        if (this.addLakes) {
            a4.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_WATER);
            a4.addFeature(GenerationStep.Decoration.LAKES, Features.LAKE_LAVA);
        }
        for (final Map.Entry<StructureFeature<?>, StructureFeatureConfiguration> entry6 : this.structureSettings.structureConfig().entrySet()) {
            a4.addStructureStart(bst3.withBiomeConfig(FlatLevelGeneratorSettings.STRUCTURE_FEATURES.get(entry6.getKey())));
        }
        final boolean boolean5 = (!this.voidGen || this.biomes.getResourceKey(bss2).equals(Optional.of((Object)Biomes.THE_VOID))) && this.decoration;
        if (boolean5) {
            final List<List<Supplier<ConfiguredFeature<?, ?>>>> list6 = bst3.features();
            for (int integer7 = 0; integer7 < list6.size(); ++integer7) {
                if (integer7 != GenerationStep.Decoration.UNDERGROUND_STRUCTURES.ordinal()) {
                    if (integer7 != GenerationStep.Decoration.SURFACE_STRUCTURES.ordinal()) {
                        final List<Supplier<ConfiguredFeature<?, ?>>> list7 = (List<Supplier<ConfiguredFeature<?, ?>>>)list6.get(integer7);
                        for (final Supplier<ConfiguredFeature<?, ?>> supplier10 : list7) {
                            a4.addFeature(integer7, supplier10);
                        }
                    }
                }
            }
        }
        final BlockState[] arr6 = this.getLayers();
        for (int integer7 = 0; integer7 < arr6.length; ++integer7) {
            final BlockState cee8 = arr6[integer7];
            if (cee8 != null && !Heightmap.Types.MOTION_BLOCKING.isOpaque().test(cee8)) {
                this.layers[integer7] = null;
                a4.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, Feature.FILL_LAYER.configured(new LayerConfiguration(integer7, cee8)));
            }
        }
        return new Biome.BiomeBuilder().precipitation(bss2.getPrecipitation()).biomeCategory(bss2.getBiomeCategory()).depth(bss2.getDepth()).scale(bss2.getScale()).temperature(bss2.getBaseTemperature()).downfall(bss2.getDownfall()).specialEffects(bss2.getSpecialEffects()).generationSettings(a4.build()).mobSpawnSettings(bss2.getMobSettings()).build();
    }
    
    public StructureSettings structureSettings() {
        return this.structureSettings;
    }
    
    public Biome getBiome() {
        return (Biome)this.biome.get();
    }
    
    public List<FlatLayerInfo> getLayersInfo() {
        return this.layersInfo;
    }
    
    public BlockState[] getLayers() {
        return this.layers;
    }
    
    public void updateLayers() {
        Arrays.fill((Object[])this.layers, 0, this.layers.length, null);
        int integer2 = 0;
        for (final FlatLayerInfo cpb4 : this.layersInfo) {
            cpb4.setStart(integer2);
            integer2 += cpb4.getHeight();
        }
        this.voidGen = true;
        for (final FlatLayerInfo cpb5 : this.layersInfo) {
            for (int integer3 = cpb5.getStart(); integer3 < cpb5.getStart() + cpb5.getHeight(); ++integer3) {
                final BlockState cee5 = cpb5.getBlockState();
                if (!cee5.is(Blocks.AIR)) {
                    this.voidGen = false;
                    this.layers[integer3] = cee5;
                }
            }
        }
    }
    
    public static FlatLevelGeneratorSettings getDefault(final Registry<Biome> gm) {
        final StructureSettings chs2 = new StructureSettings((Optional<StrongholdConfiguration>)Optional.of(StructureSettings.DEFAULT_STRONGHOLD), Maps.newHashMap((java.util.Map<?, ?>)ImmutableMap.<StructureFeature<JigsawConfiguration>, StructureFeatureConfiguration>of(StructureFeature.VILLAGE, StructureSettings.DEFAULTS.get(StructureFeature.VILLAGE))));
        final FlatLevelGeneratorSettings cpc3 = new FlatLevelGeneratorSettings(chs2, gm);
        cpc3.biome = (Supplier<Biome>)(() -> gm.getOrThrow(Biomes.PLAINS));
        cpc3.getLayersInfo().add(new FlatLayerInfo(1, Blocks.BEDROCK));
        cpc3.getLayersInfo().add(new FlatLayerInfo(2, Blocks.DIRT));
        cpc3.getLayersInfo().add(new FlatLayerInfo(1, Blocks.GRASS_BLOCK));
        cpc3.updateLayers();
        return cpc3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        CODEC = RecordCodecBuilder.<FlatLevelGeneratorSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<FlatLevelGeneratorSettings>, ? extends App<RecordCodecBuilder.Mu<FlatLevelGeneratorSettings>, FlatLevelGeneratorSettings>>)(instance -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       net/minecraft/core/Registry.BIOME_REGISTRY:Lnet/minecraft/resources/ResourceKey;
            //     4: invokestatic    net/minecraft/resources/RegistryLookupCodec.create:(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/resources/RegistryLookupCodec;
            //     7: invokedynamic   BootstrapMethod #3, apply:()Ljava/util/function/Function;
            //    12: invokevirtual   net/minecraft/resources/RegistryLookupCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    15: getstatic       net/minecraft/world/level/levelgen/StructureSettings.CODEC:Lcom/mojang/serialization/Codec;
            //    18: ldc_w           "structures"
            //    21: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    26: invokedynamic   BootstrapMethod #4, apply:()Ljava/util/function/Function;
            //    31: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    34: getstatic       net/minecraft/world/level/levelgen/flat/FlatLayerInfo.CODEC:Lcom/mojang/serialization/Codec;
            //    37: invokeinterface com/mojang/serialization/Codec.listOf:()Lcom/mojang/serialization/Codec;
            //    42: ldc_w           "layers"
            //    45: invokeinterface com/mojang/serialization/Codec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    50: invokedynamic   BootstrapMethod #5, apply:()Ljava/util/function/Function;
            //    55: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    58: getstatic       com/mojang/serialization/Codec.BOOL:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    61: ldc_w           "lakes"
            //    64: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    69: iconst_0       
            //    70: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    73: invokevirtual   com/mojang/serialization/MapCodec.orElse:(Ljava/lang/Object;)Lcom/mojang/serialization/MapCodec;
            //    76: invokedynamic   BootstrapMethod #6, apply:()Ljava/util/function/Function;
            //    81: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //    84: getstatic       com/mojang/serialization/Codec.BOOL:Lcom/mojang/serialization/codecs/PrimitiveCodec;
            //    87: ldc_w           "features"
            //    90: invokeinterface com/mojang/serialization/codecs/PrimitiveCodec.fieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //    95: iconst_0       
            //    96: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    99: invokevirtual   com/mojang/serialization/MapCodec.orElse:(Ljava/lang/Object;)Lcom/mojang/serialization/MapCodec;
            //   102: invokedynamic   BootstrapMethod #7, apply:()Ljava/util/function/Function;
            //   107: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //   110: getstatic       net/minecraft/world/level/biome/Biome.CODEC:Lcom/mojang/serialization/Codec;
            //   113: ldc_w           "biome"
            //   116: invokeinterface com/mojang/serialization/Codec.optionalFieldOf:(Ljava/lang/String;)Lcom/mojang/serialization/MapCodec;
            //   121: invokedynamic   BootstrapMethod #8, get:()Ljava/util/function/Supplier;
            //   126: invokevirtual   com/mojang/serialization/MapCodec.orElseGet:(Ljava/util/function/Supplier;)Lcom/mojang/serialization/MapCodec;
            //   129: invokedynamic   BootstrapMethod #9, apply:()Ljava/util/function/Function;
            //   134: invokevirtual   com/mojang/serialization/MapCodec.forGetter:(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;
            //   137: invokevirtual   com/mojang/serialization/codecs/RecordCodecBuilder$Instance.group:(Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;Lcom/mojang/datafixers/kinds/App;)Lcom/mojang/datafixers/Products$P6;
            //   140: aload_0         /* instance */
            //   141: invokedynamic   BootstrapMethod #10, apply:()Lcom/mojang/datafixers/util/Function6;
            //   146: invokevirtual   com/mojang/datafixers/Products$P6.apply:(Lcom/mojang/datafixers/kinds/Applicative;Lcom/mojang/datafixers/util/Function6;)Lcom/mojang/datafixers/kinds/App;
            //   149: areturn        
            //    MethodParameters:
            //  Name      Flags  
            //  --------  -----
            //  instance  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 3
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2611)
            //     at com.strobel.assembler.metadata.MetadataHelper$10.visitClassType(MetadataHelper.java:2608)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getInterfaces(MetadataHelper.java:702)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2027)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
            //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
            //     at com.strobel.assembler.metadata.MetadataHelper.asMemberOf(MetadataHelper.java:840)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2684)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2669)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.invalidateDependentExpressions(TypeAnalysis.java:759)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1011)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
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
            //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
            //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
            //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
            //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
            //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
            //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
            //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
            //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
            //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
            //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
            //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
            //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
            //     at java.base/java.lang.Thread.run(Thread.java:832)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        })).stable();
        STRUCTURE_FEATURES = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put(StructureFeature.MINESHAFT, StructureFeatures.MINESHAFT);
            hashMap.put(StructureFeature.VILLAGE, StructureFeatures.VILLAGE_PLAINS);
            hashMap.put(StructureFeature.STRONGHOLD, StructureFeatures.STRONGHOLD);
            hashMap.put(StructureFeature.SWAMP_HUT, StructureFeatures.SWAMP_HUT);
            hashMap.put(StructureFeature.DESERT_PYRAMID, StructureFeatures.DESERT_PYRAMID);
            hashMap.put(StructureFeature.JUNGLE_TEMPLE, StructureFeatures.JUNGLE_TEMPLE);
            hashMap.put(StructureFeature.IGLOO, StructureFeatures.IGLOO);
            hashMap.put(StructureFeature.OCEAN_RUIN, StructureFeatures.OCEAN_RUIN_COLD);
            hashMap.put(StructureFeature.SHIPWRECK, StructureFeatures.SHIPWRECK);
            hashMap.put(StructureFeature.OCEAN_MONUMENT, StructureFeatures.OCEAN_MONUMENT);
            hashMap.put(StructureFeature.END_CITY, StructureFeatures.END_CITY);
            hashMap.put(StructureFeature.WOODLAND_MANSION, StructureFeatures.WOODLAND_MANSION);
            hashMap.put(StructureFeature.NETHER_BRIDGE, StructureFeatures.NETHER_BRIDGE);
            hashMap.put(StructureFeature.PILLAGER_OUTPOST, StructureFeatures.PILLAGER_OUTPOST);
            hashMap.put(StructureFeature.RUINED_PORTAL, StructureFeatures.RUINED_PORTAL_STANDARD);
            hashMap.put(StructureFeature.BASTION_REMNANT, StructureFeatures.BASTION_REMNANT);
        }));
    }
}
