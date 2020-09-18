package net.minecraft.world.level.levelgen.placement;

import java.util.function.Function;
import net.minecraft.core.Registry;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.Decoratable;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class ConfiguredDecorator<DC extends DecoratorConfiguration> implements Decoratable<ConfiguredDecorator<?>> {
    public static final Codec<ConfiguredDecorator<?>> CODEC;
    private final FeatureDecorator<DC> decorator;
    private final DC config;
    
    public ConfiguredDecorator(final FeatureDecorator<DC> cpz, final DC clt) {
        this.decorator = cpz;
        this.config = clt;
    }
    
    public Stream<BlockPos> getPositions(final DecorationContext cps, final Random random, final BlockPos fx) {
        return this.decorator.getPositions(cps, random, this.config, fx);
    }
    
    public String toString() {
        return String.format("[%s %s]", new Object[] { Registry.DECORATOR.getKey(this.decorator), this.config });
    }
    
    public ConfiguredDecorator<?> decorated(final ConfiguredDecorator<?> cpl) {
        return new ConfiguredDecorator<>(FeatureDecorator.DECORATED, new DecoratedDecoratorConfiguration(cpl, this));
    }
    
    public DC config() {
        return this.config;
    }
    
    static {
        CODEC = Registry.DECORATOR.<ConfiguredDecorator<?>>dispatch("type", (java.util.function.Function<? super ConfiguredDecorator<?>, ?>)(cpl -> cpl.decorator), (java.util.function.Function<? super Object, ? extends Codec<? extends ConfiguredDecorator<?>>>)FeatureDecorator::configuredCodec);
    }
}
