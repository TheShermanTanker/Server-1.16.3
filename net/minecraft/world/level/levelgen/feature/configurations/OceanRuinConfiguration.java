package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.OceanRuinFeature;
import com.mojang.serialization.Codec;

public class OceanRuinConfiguration implements FeatureConfiguration {
    public static final Codec<OceanRuinConfiguration> CODEC;
    public final OceanRuinFeature.Type biomeTemp;
    public final float largeProbability;
    public final float clusterProbability;
    
    public OceanRuinConfiguration(final OceanRuinFeature.Type b, final float float2, final float float3) {
        this.biomeTemp = b;
        this.largeProbability = float2;
        this.clusterProbability = float3;
    }
    
    static {
        CODEC = RecordCodecBuilder.<OceanRuinConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<OceanRuinConfiguration>, ? extends App<RecordCodecBuilder.Mu<OceanRuinConfiguration>, OceanRuinConfiguration>>)(instance -> instance.<OceanRuinFeature.Type, Float, Float>group(OceanRuinFeature.Type.CODEC.fieldOf("biome_temp").forGetter((java.util.function.Function<Object, OceanRuinFeature.Type>)(cmf -> cmf.biomeTemp)), Codec.floatRange(0.0f, 1.0f).fieldOf("large_probability").forGetter((java.util.function.Function<Object, Float>)(cmf -> cmf.largeProbability)), Codec.floatRange(0.0f, 1.0f).fieldOf("cluster_probability").forGetter((java.util.function.Function<Object, Float>)(cmf -> cmf.clusterProbability))).<OceanRuinConfiguration>apply(instance, OceanRuinConfiguration::new)));
    }
}
