package net.minecraft.world.level.levelgen.placement;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class FrequencyWithExtraChanceDecoratorConfiguration implements DecoratorConfiguration {
    public static final Codec<FrequencyWithExtraChanceDecoratorConfiguration> CODEC;
    public final int count;
    public final float extraChance;
    public final int extraCount;
    
    public FrequencyWithExtraChanceDecoratorConfiguration(final int integer1, final float float2, final int integer3) {
        this.count = integer1;
        this.extraChance = float2;
        this.extraCount = integer3;
    }
    
    static {
        CODEC = RecordCodecBuilder.<FrequencyWithExtraChanceDecoratorConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<FrequencyWithExtraChanceDecoratorConfiguration>, ? extends App<RecordCodecBuilder.Mu<FrequencyWithExtraChanceDecoratorConfiguration>, FrequencyWithExtraChanceDecoratorConfiguration>>)(instance -> instance.<Integer, Float, Integer>group(Codec.INT.fieldOf("count").forGetter((java.util.function.Function<Object, Object>)(cqa -> cqa.count)), Codec.FLOAT.fieldOf("extra_chance").forGetter((java.util.function.Function<Object, Object>)(cqa -> cqa.extraChance)), Codec.INT.fieldOf("extra_count").forGetter((java.util.function.Function<Object, Object>)(cqa -> cqa.extraCount))).<FrequencyWithExtraChanceDecoratorConfiguration>apply(instance, FrequencyWithExtraChanceDecoratorConfiguration::new)));
    }
}
