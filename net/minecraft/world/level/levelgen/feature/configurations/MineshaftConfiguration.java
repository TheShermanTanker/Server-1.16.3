package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import com.mojang.serialization.Codec;

public class MineshaftConfiguration implements FeatureConfiguration {
    public static final Codec<MineshaftConfiguration> CODEC;
    public final float probability;
    public final MineshaftFeature.Type type;
    
    public MineshaftConfiguration(final float float1, final MineshaftFeature.Type b) {
        this.probability = float1;
        this.type = b;
    }
    
    static {
        CODEC = RecordCodecBuilder.<MineshaftConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<MineshaftConfiguration>, ? extends App<RecordCodecBuilder.Mu<MineshaftConfiguration>, MineshaftConfiguration>>)(instance -> instance.<Float, MineshaftFeature.Type>group(Codec.floatRange(0.0f, 1.0f).fieldOf("probability").forGetter((java.util.function.Function<Object, Float>)(cmb -> cmb.probability)), MineshaftFeature.Type.CODEC.fieldOf("type").forGetter((java.util.function.Function<Object, MineshaftFeature.Type>)(cmb -> cmb.type))).apply(instance, (java.util.function.BiFunction<Float, MineshaftFeature.Type, Object>)MineshaftConfiguration::new)));
    }
}
