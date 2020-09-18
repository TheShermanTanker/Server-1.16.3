package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import net.minecraft.world.level.levelgen.feature.WeightedConfiguredFeature;
import java.util.List;
import com.mojang.serialization.Codec;

public class RandomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<RandomFeatureConfiguration> CODEC;
    public final List<WeightedConfiguredFeature> features;
    public final Supplier<ConfiguredFeature<?, ?>> defaultFeature;
    
    public RandomFeatureConfiguration(final List<WeightedConfiguredFeature> list, final ConfiguredFeature<?, ?> cis) {
        this(list, (Supplier<ConfiguredFeature<?, ?>>)(() -> cis));
    }
    
    private RandomFeatureConfiguration(final List<WeightedConfiguredFeature> list, final Supplier<ConfiguredFeature<?, ?>> supplier) {
        this.features = list;
        this.defaultFeature = supplier;
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)Stream.concat(this.features.stream().flatMap(clg -> ((ConfiguredFeature)clg.feature.get()).getFeatures()), ((ConfiguredFeature)this.defaultFeature.get()).getFeatures());
    }
    
    static {
        CODEC = RecordCodecBuilder.<RandomFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<RandomFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<RandomFeatureConfiguration>, RandomFeatureConfiguration>>)(instance -> instance.<java.util.List<WeightedConfiguredFeature>, Supplier<ConfiguredFeature<?, ?>>, Object>apply2((java.util.function.BiFunction<java.util.List<WeightedConfiguredFeature>, Supplier<ConfiguredFeature<?, ?>>, Object>)RandomFeatureConfiguration::new, WeightedConfiguredFeature.CODEC.listOf().fieldOf("features").forGetter((java.util.function.Function<Object, java.util.List<WeightedConfiguredFeature>>)(cmj -> cmj.features)), ConfiguredFeature.CODEC.fieldOf("default").forGetter((java.util.function.Function<Object, Supplier<ConfiguredFeature<?, ?>>>)(cmj -> cmj.defaultFeature)))));
    }
}
