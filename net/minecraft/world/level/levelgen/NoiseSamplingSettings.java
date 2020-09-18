package net.minecraft.world.level.levelgen;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class NoiseSamplingSettings {
    private static final Codec<Double> SCALE_RANGE;
    public static final Codec<NoiseSamplingSettings> CODEC;
    private final double xzScale;
    private final double yScale;
    private final double xzFactor;
    private final double yFactor;
    
    public NoiseSamplingSettings(final double double1, final double double2, final double double3, final double double4) {
        this.xzScale = double1;
        this.yScale = double2;
        this.xzFactor = double3;
        this.yFactor = double4;
    }
    
    public double xzScale() {
        return this.xzScale;
    }
    
    public double yScale() {
        return this.yScale;
    }
    
    public double xzFactor() {
        return this.xzFactor;
    }
    
    public double yFactor() {
        return this.yFactor;
    }
    
    static {
        SCALE_RANGE = Codec.doubleRange(0.001, 1000.0);
        CODEC = RecordCodecBuilder.<NoiseSamplingSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<NoiseSamplingSettings>, ? extends App<RecordCodecBuilder.Mu<NoiseSamplingSettings>, NoiseSamplingSettings>>)(instance -> instance.<Double, Double, Double, Double>group(NoiseSamplingSettings.SCALE_RANGE.fieldOf("xz_scale").forGetter((java.util.function.Function<Object, Double>)NoiseSamplingSettings::xzScale), NoiseSamplingSettings.SCALE_RANGE.fieldOf("y_scale").forGetter((java.util.function.Function<Object, Double>)NoiseSamplingSettings::yScale), NoiseSamplingSettings.SCALE_RANGE.fieldOf("xz_factor").forGetter((java.util.function.Function<Object, Double>)NoiseSamplingSettings::xzFactor), NoiseSamplingSettings.SCALE_RANGE.fieldOf("y_factor").forGetter((java.util.function.Function<Object, Double>)NoiseSamplingSettings::yFactor)).<NoiseSamplingSettings>apply(instance, NoiseSamplingSettings::new)));
    }
}
