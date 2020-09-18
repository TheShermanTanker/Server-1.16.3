package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import java.util.function.Supplier;
import com.mojang.serialization.Codec;

public class DecoratedFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<DecoratedFeatureConfiguration> CODEC;
    public final Supplier<ConfiguredFeature<?, ?>> feature;
    public final ConfiguredDecorator<?> decorator;
    
    public DecoratedFeatureConfiguration(final Supplier<ConfiguredFeature<?, ?>> supplier, final ConfiguredDecorator<?> cpl) {
        this.feature = supplier;
        this.decorator = cpl;
    }
    
    public String toString() {
        return String.format("< %s [%s | %s] >", new Object[] { this.getClass().getSimpleName(), Registry.FEATURE.getKey(((ConfiguredFeature)this.feature.get()).feature()), this.decorator });
    }
    
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return (Stream<ConfiguredFeature<?, ?>>)((ConfiguredFeature)this.feature.get()).getFeatures();
    }
    
    static {
        CODEC = RecordCodecBuilder.<DecoratedFeatureConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<DecoratedFeatureConfiguration>, ? extends App<RecordCodecBuilder.Mu<DecoratedFeatureConfiguration>, DecoratedFeatureConfiguration>>)(instance -> instance.<Supplier<ConfiguredFeature<?, ?>>, ConfiguredDecorator<?>>group(ConfiguredFeature.CODEC.fieldOf("feature").forGetter((java.util.function.Function<Object, Supplier<ConfiguredFeature<?, ?>>>)(cls -> cls.feature)), ConfiguredDecorator.CODEC.fieldOf("decorator").forGetter((java.util.function.Function<Object, ConfiguredDecorator<?>>)(cls -> cls.decorator))).apply(instance, (java.util.function.BiFunction<Supplier<ConfiguredFeature<?, ?>>, ConfiguredDecorator<?>, Object>)DecoratedFeatureConfiguration::new)));
    }
}
