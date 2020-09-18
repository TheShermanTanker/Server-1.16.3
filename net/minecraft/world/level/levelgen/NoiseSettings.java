package net.minecraft.world.level.levelgen;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Lifecycle;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class NoiseSettings {
    public static final Codec<NoiseSettings> CODEC;
    private final int height;
    private final NoiseSamplingSettings noiseSamplingSettings;
    private final NoiseSlideSettings topSlideSettings;
    private final NoiseSlideSettings bottomSlideSettings;
    private final int noiseSizeHorizontal;
    private final int noiseSizeVertical;
    private final double densityFactor;
    private final double densityOffset;
    private final boolean useSimplexSurfaceNoise;
    private final boolean randomDensityOffset;
    private final boolean islandNoiseOverride;
    private final boolean isAmplified;
    
    public NoiseSettings(final int integer1, final NoiseSamplingSettings chn, final NoiseSlideSettings chp3, final NoiseSlideSettings chp4, final int integer5, final int integer6, final double double7, final double double8, final boolean boolean9, final boolean boolean10, final boolean boolean11, final boolean boolean12) {
        this.height = integer1;
        this.noiseSamplingSettings = chn;
        this.topSlideSettings = chp3;
        this.bottomSlideSettings = chp4;
        this.noiseSizeHorizontal = integer5;
        this.noiseSizeVertical = integer6;
        this.densityFactor = double7;
        this.densityOffset = double8;
        this.useSimplexSurfaceNoise = boolean9;
        this.randomDensityOffset = boolean10;
        this.islandNoiseOverride = boolean11;
        this.isAmplified = boolean12;
    }
    
    public int height() {
        return this.height;
    }
    
    public NoiseSamplingSettings noiseSamplingSettings() {
        return this.noiseSamplingSettings;
    }
    
    public NoiseSlideSettings topSlideSettings() {
        return this.topSlideSettings;
    }
    
    public NoiseSlideSettings bottomSlideSettings() {
        return this.bottomSlideSettings;
    }
    
    public int noiseSizeHorizontal() {
        return this.noiseSizeHorizontal;
    }
    
    public int noiseSizeVertical() {
        return this.noiseSizeVertical;
    }
    
    public double densityFactor() {
        return this.densityFactor;
    }
    
    public double densityOffset() {
        return this.densityOffset;
    }
    
    @Deprecated
    public boolean useSimplexSurfaceNoise() {
        return this.useSimplexSurfaceNoise;
    }
    
    @Deprecated
    public boolean randomDensityOffset() {
        return this.randomDensityOffset;
    }
    
    @Deprecated
    public boolean islandNoiseOverride() {
        return this.islandNoiseOverride;
    }
    
    @Deprecated
    public boolean isAmplified() {
        return this.isAmplified;
    }
    
    static {
        CODEC = RecordCodecBuilder.<NoiseSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<NoiseSettings>, ? extends App<RecordCodecBuilder.Mu<NoiseSettings>, NoiseSettings>>)(instance -> instance.<Integer, NoiseSamplingSettings, NoiseSlideSettings, NoiseSlideSettings, Integer, Integer, Double, Double, Boolean, Boolean, Boolean, Boolean>group(Codec.intRange(0, 256).fieldOf("height").forGetter((java.util.function.Function<Object, Integer>)NoiseSettings::height), NoiseSamplingSettings.CODEC.fieldOf("sampling").forGetter((java.util.function.Function<Object, NoiseSamplingSettings>)NoiseSettings::noiseSamplingSettings), NoiseSlideSettings.CODEC.fieldOf("top_slide").forGetter((java.util.function.Function<Object, NoiseSlideSettings>)NoiseSettings::topSlideSettings), NoiseSlideSettings.CODEC.fieldOf("bottom_slide").forGetter((java.util.function.Function<Object, NoiseSlideSettings>)NoiseSettings::bottomSlideSettings), Codec.intRange(1, 4).fieldOf("size_horizontal").forGetter((java.util.function.Function<Object, Integer>)NoiseSettings::noiseSizeHorizontal), Codec.intRange(1, 4).fieldOf("size_vertical").forGetter((java.util.function.Function<Object, Integer>)NoiseSettings::noiseSizeVertical), Codec.DOUBLE.fieldOf("density_factor").forGetter((java.util.function.Function<Object, Object>)NoiseSettings::densityFactor), Codec.DOUBLE.fieldOf("density_offset").forGetter((java.util.function.Function<Object, Object>)NoiseSettings::densityOffset), Codec.BOOL.fieldOf("simplex_surface_noise").forGetter((java.util.function.Function<Object, Object>)NoiseSettings::useSimplexSurfaceNoise), Codec.BOOL.optionalFieldOf("random_density_offset", Boolean.valueOf(false), Lifecycle.experimental()).forGetter((java.util.function.Function<Object, Boolean>)NoiseSettings::randomDensityOffset), Codec.BOOL.optionalFieldOf("island_noise_override", Boolean.valueOf(false), Lifecycle.experimental()).forGetter((java.util.function.Function<Object, Boolean>)NoiseSettings::islandNoiseOverride), Codec.BOOL.optionalFieldOf("amplified", Boolean.valueOf(false), Lifecycle.experimental()).forGetter((java.util.function.Function<Object, Boolean>)NoiseSettings::isAmplified)).<NoiseSettings>apply(instance, NoiseSettings::new)));
    }
}
