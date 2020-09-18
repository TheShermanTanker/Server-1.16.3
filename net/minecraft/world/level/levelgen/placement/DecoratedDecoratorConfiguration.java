package net.minecraft.world.level.levelgen.placement;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class DecoratedDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<DecoratedDecoratorConfiguration> CODEC;
    private final ConfiguredDecorator<?> outer;
    private final ConfiguredDecorator<?> inner;
    
    public DecoratedDecoratorConfiguration(final ConfiguredDecorator<?> cpl1, final ConfiguredDecorator<?> cpl2) {
        this.outer = cpl1;
        this.inner = cpl2;
    }
    
    public ConfiguredDecorator<?> outer() {
        return this.outer;
    }
    
    public ConfiguredDecorator<?> inner() {
        return this.inner;
    }
    
    static {
        CODEC = RecordCodecBuilder.<DecoratedDecoratorConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<DecoratedDecoratorConfiguration>, ? extends App<RecordCodecBuilder.Mu<DecoratedDecoratorConfiguration>, DecoratedDecoratorConfiguration>>)(instance -> instance.<ConfiguredDecorator<?>, ConfiguredDecorator<?>>group(ConfiguredDecorator.CODEC.fieldOf("outer").forGetter((java.util.function.Function<Object, ConfiguredDecorator<?>>)DecoratedDecoratorConfiguration::outer), ConfiguredDecorator.CODEC.fieldOf("inner").forGetter((java.util.function.Function<Object, ConfiguredDecorator<?>>)DecoratedDecoratorConfiguration::inner)).apply(instance, (java.util.function.BiFunction<ConfiguredDecorator<?>, ConfiguredDecorator<?>, Object>)DecoratedDecoratorConfiguration::new)));
    }
}
