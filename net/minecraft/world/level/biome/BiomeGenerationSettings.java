package net.minecraft.world.level.biome;

import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import net.minecraft.data.worldgen.SurfaceBuilders;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import java.util.function.Consumer;
import net.minecraft.Util;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import java.util.stream.Collector;
import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import java.util.List;
import net.minecraft.world.level.levelgen.GenerationStep;
import java.util.Map;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import java.util.function.Supplier;
import com.mojang.serialization.MapCodec;
import org.apache.logging.log4j.Logger;

public class BiomeGenerationSettings {
    public static final Logger LOGGER;
    public static final BiomeGenerationSettings EMPTY;
    public static final MapCodec<BiomeGenerationSettings> CODEC;
    private final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder;
    private final Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> carvers;
    private final List<List<Supplier<ConfiguredFeature<?, ?>>>> features;
    private final List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts;
    private final List<ConfiguredFeature<?, ?>> flowerFeatures;
    
    private BiomeGenerationSettings(final Supplier<ConfiguredSurfaceBuilder<?>> supplier, final Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> map, final List<List<Supplier<ConfiguredFeature<?, ?>>>> list3, final List<Supplier<ConfiguredStructureFeature<?, ?>>> list4) {
        this.surfaceBuilder = supplier;
        this.carvers = map;
        this.features = list3;
        this.structureStarts = list4;
        this.flowerFeatures = (List<ConfiguredFeature<?, ?>>)list3.stream().flatMap(Collection::stream).map(Supplier::get).flatMap(ConfiguredFeature::getFeatures).filter(cis -> cis.feature == Feature.FLOWER).collect((Collector)ImmutableList.toImmutableList());
    }
    
    public List<Supplier<ConfiguredWorldCarver<?>>> getCarvers(final GenerationStep.Carving a) {
        return (List<Supplier<ConfiguredWorldCarver<?>>>)this.carvers.getOrDefault(a, ImmutableList.of());
    }
    
    public boolean isValidStart(final StructureFeature<?> ckx) {
        return this.structureStarts.stream().anyMatch(supplier -> ((ConfiguredStructureFeature)supplier.get()).feature == ckx);
    }
    
    public Collection<Supplier<ConfiguredStructureFeature<?, ?>>> structures() {
        return (Collection<Supplier<ConfiguredStructureFeature<?, ?>>>)this.structureStarts;
    }
    
    public ConfiguredStructureFeature<?, ?> withBiomeConfig(final ConfiguredStructureFeature<?, ?> cit) {
        return DataFixUtils.<ConfiguredStructureFeature<?, ?>>orElse((java.util.Optional<? extends ConfiguredStructureFeature<?, ?>>)this.structureStarts.stream().map(Supplier::get).filter(cit2 -> cit2.feature == cit.feature).findAny(), cit);
    }
    
    public List<ConfiguredFeature<?, ?>> getFlowerFeatures() {
        return this.flowerFeatures;
    }
    
    public List<List<Supplier<ConfiguredFeature<?, ?>>>> features() {
        return this.features;
    }
    
    public Supplier<ConfiguredSurfaceBuilder<?>> getSurfaceBuilder() {
        return this.surfaceBuilder;
    }
    
    public SurfaceBuilderConfiguration getSurfaceBuilderConfig() {
        return ((ConfiguredSurfaceBuilder)this.surfaceBuilder.get()).config();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        EMPTY = new BiomeGenerationSettings((Supplier<ConfiguredSurfaceBuilder<?>>)(() -> SurfaceBuilders.NOPE), ImmutableMap.of(), ImmutableList.of(), ImmutableList.of());
        CODEC = RecordCodecBuilder.<BiomeGenerationSettings>mapCodec((java.util.function.Function<RecordCodecBuilder.Instance<BiomeGenerationSettings>, ? extends App<RecordCodecBuilder.Mu<BiomeGenerationSettings>, BiomeGenerationSettings>>)(instance -> instance.<Supplier<ConfiguredSurfaceBuilder<?>>, Map, java.util.List<List<Supplier<ConfiguredFeature<?, ?>>>>, List<Supplier<ConfiguredStructureFeature<?, ?>>>>group(ConfiguredSurfaceBuilder.CODEC.fieldOf("surface_builder").forGetter((java.util.function.Function<Object, Supplier<ConfiguredSurfaceBuilder<?>>>)(bst -> bst.surfaceBuilder)), Codec.<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>>simpleMap(GenerationStep.Carving.CODEC, ConfiguredWorldCarver.LIST_CODEC.promotePartial(Util.prefix("Carver: ", (Consumer<String>)BiomeGenerationSettings.LOGGER::error)), StringRepresentable.keys(GenerationStep.Carving.values())).fieldOf("carvers").forGetter((java.util.function.Function<Object, Object>)(bst -> bst.carvers)), ConfiguredFeature.LIST_CODEC.promotePartial(Util.prefix("Feature: ", (Consumer<String>)BiomeGenerationSettings.LOGGER::error)).listOf().fieldOf("features").forGetter((java.util.function.Function<Object, java.util.List<List<Supplier<ConfiguredFeature<?, ?>>>>>)(bst -> bst.features)), ConfiguredStructureFeature.LIST_CODEC.promotePartial(Util.prefix("Structure start: ", (Consumer<String>)BiomeGenerationSettings.LOGGER::error)).fieldOf("starts").forGetter((java.util.function.Function<Object, List<Supplier<ConfiguredStructureFeature<?, ?>>>>)(bst -> bst.structureStarts))).<BiomeGenerationSettings>apply(instance, BiomeGenerationSettings::new)));
    }
    
    public static class Builder {
        private Optional<Supplier<ConfiguredSurfaceBuilder<?>>> surfaceBuilder;
        private final Map<GenerationStep.Carving, List<Supplier<ConfiguredWorldCarver<?>>>> carvers;
        private final List<List<Supplier<ConfiguredFeature<?, ?>>>> features;
        private final List<Supplier<ConfiguredStructureFeature<?, ?>>> structureStarts;
        
        public Builder() {
            this.surfaceBuilder = (Optional<Supplier<ConfiguredSurfaceBuilder<?>>>)Optional.empty();
            this.carvers = Maps.newLinkedHashMap();
            this.features = Lists.newArrayList();
            this.structureStarts = Lists.newArrayList();
        }
        
        public Builder surfaceBuilder(final ConfiguredSurfaceBuilder<?> ctd) {
            return this.surfaceBuilder((Supplier<ConfiguredSurfaceBuilder<?>>)(() -> ctd));
        }
        
        public Builder surfaceBuilder(final Supplier<ConfiguredSurfaceBuilder<?>> supplier) {
            this.surfaceBuilder = (Optional<Supplier<ConfiguredSurfaceBuilder<?>>>)Optional.of(supplier);
            return this;
        }
        
        public Builder addFeature(final GenerationStep.Decoration b, final ConfiguredFeature<?, ?> cis) {
            return this.addFeature(b.ordinal(), (Supplier<ConfiguredFeature<?, ?>>)(() -> cis));
        }
        
        public Builder addFeature(final int integer, final Supplier<ConfiguredFeature<?, ?>> supplier) {
            this.addFeatureStepsUpTo(integer);
            ((List)this.features.get(integer)).add(supplier);
            return this;
        }
        
        public <C extends CarverConfiguration> Builder addCarver(final GenerationStep.Carving a, final ConfiguredWorldCarver<C> chy) {
            ((List)this.carvers.computeIfAbsent(a, a -> Lists.newArrayList())).add((() -> chy));
            return this;
        }
        
        public Builder addStructureStart(final ConfiguredStructureFeature<?, ?> cit) {
            this.structureStarts.add((() -> cit));
            return this;
        }
        
        private void addFeatureStepsUpTo(final int integer) {
            while (this.features.size() <= integer) {
                this.features.add(Lists.newArrayList());
            }
        }
        
        public BiomeGenerationSettings build() {
            return new BiomeGenerationSettings((Supplier)this.surfaceBuilder.orElseThrow(() -> new IllegalStateException("Missing surface builder")), (Map)this.carvers.entrySet().stream().collect((Collector)ImmutableMap.toImmutableMap((java.util.function.Function<? super Object, ?>)Map.Entry::getKey, (java.util.function.Function<? super Object, ?>)(entry -> ImmutableList.copyOf((java.util.Collection<?>)entry.getValue())))), (List)this.features.stream().map(ImmutableList::copyOf).collect((Collector)ImmutableList.toImmutableList()), (List)ImmutableList.copyOf((java.util.Collection<?>)this.structureStarts), null);
        }
    }
}
