package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class RandomBooleanFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<RandomBooleanFeatureConfiguration> CODEC;
    public final Supplier<ConfiguredFeature<?, ?>> featureTrue;
    public final Supplier<ConfiguredFeature<?, ?>> featureFalse;
    
    public RandomBooleanFeatureConfiguration(final Supplier<ConfiguredFeature<?, ?>> supplier1, final Supplier<ConfiguredFeature<?, ?>> supplier2) {
        this.featureTrue = supplier1;
        this.featureFalse = supplier2;
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)Stream.concat(((ConfiguredFeature)this.featureTrue.get()).getFeatures(), ((ConfiguredFeature)this.featureFalse.get()).getFeatures());
    }
    
    static {
        CODEC = RecordCodecBuilder.<RandomBooleanFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<RandomBooleanFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<RandomBooleanFeatureConfiguration>, RandomBooleanFeatureConfiguration>>)(instance -> instance.<Supplier<ConfiguredFeature<?, ?>>, Supplier<ConfiguredFeature<?, ?>>>group(ConfiguredFeature.CODEC.fieldOf("feature_true").forGetter((java.util.function.Function<Object, Supplier<ConfiguredFeature<?, ?>>>)(cmi -> cmi.featureTrue)), ConfiguredFeature.CODEC.fieldOf("feature_false").forGetter((java.util.function.Function<Object, Supplier<ConfiguredFeature<?, ?>>>)(cmi -> cmi.featureFalse))).apply(instance, (java.util.function.BiFunction<Supplier<ConfiguredFeature<?, ?>>, Supplier<ConfiguredFeature<?, ?>>, Object>)RandomBooleanFeatureConfiguration::new)));
    }
}
