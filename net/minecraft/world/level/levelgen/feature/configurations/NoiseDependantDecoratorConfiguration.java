package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class NoiseDependantDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<NoiseDependantDecoratorConfiguration> CODEC;
    public final double noiseLevel;
    public final int belowNoise;
    public final int aboveNoise;
    
    public NoiseDependantDecoratorConfiguration(final double double1, final int integer2, final int integer3) {
        this.noiseLevel = double1;
        this.belowNoise = integer2;
        this.aboveNoise = integer3;
    }
    
    static {
        CODEC = RecordCodecBuilder.<NoiseDependantDecoratorConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<NoiseDependantDecoratorConfiguration>, ? extends App<RecordCodecBuilder.Mu<NoiseDependantDecoratorConfiguration>, NoiseDependantDecoratorConfiguration>>)(instance -> instance.<Double, Integer, Integer>group(Codec.DOUBLE.fieldOf("noise_level").forGetter((java.util.function.Function<Object, Object>)(cmc -> cmc.noiseLevel)), Codec.INT.fieldOf("below_noise").forGetter((java.util.function.Function<Object, Object>)(cmc -> cmc.belowNoise)), Codec.INT.fieldOf("above_noise").forGetter((java.util.function.Function<Object, Object>)(cmc -> cmc.aboveNoise))).<NoiseDependantDecoratorConfiguration>apply(instance, NoiseDependantDecoratorConfiguration::new)));
    }
}
