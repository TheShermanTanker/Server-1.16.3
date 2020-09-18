package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class RangeDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<RangeDecoratorConfiguration> CODEC;
    public final int bottomOffset;
    public final int topOffset;
    public final int maximum;
    
    public RangeDecoratorConfiguration(final int integer1, final int integer2, final int integer3) {
        this.bottomOffset = integer1;
        this.topOffset = integer2;
        this.maximum = integer3;
    }
    
    static {
        CODEC = RecordCodecBuilder.<RangeDecoratorConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<RangeDecoratorConfiguration>, ? extends App<RecordCodecBuilder.Mu<RangeDecoratorConfiguration>, RangeDecoratorConfiguration>>)(instance -> instance.<Integer, Integer, Integer>group(Codec.INT.fieldOf("bottom_offset").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(cml -> cml.bottomOffset)), Codec.INT.fieldOf("top_offset").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(cml -> cml.topOffset)), Codec.INT.fieldOf("maximum").orElse(0).forGetter((java.util.function.Function<Object, Integer>)(cml -> cml.maximum))).<RangeDecoratorConfiguration>apply(instance, RangeDecoratorConfiguration::new)));
    }
}
