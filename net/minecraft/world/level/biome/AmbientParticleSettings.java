package net.minecraft.world.level.biome;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import net.minecraft.core.particles.ParticleTypes;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import com.mojang.serialization.Codec;

public class AmbientParticleSettings {
    public static final Codec<AmbientParticleSettings> CODEC;
    private final ParticleOptions options;
    private final float probability;
    
    public AmbientParticleSettings(final ParticleOptions hf, final float float2) {
        this.options = hf;
        this.probability = float2;
    }
    
    static {
        CODEC = RecordCodecBuilder.<AmbientParticleSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<AmbientParticleSettings>, ? extends App<RecordCodecBuilder.Mu<AmbientParticleSettings>, AmbientParticleSettings>>)(instance -> instance.<ParticleOptions, Object>group(ParticleTypes.CODEC.fieldOf("options").forGetter((java.util.function.Function<Object, ParticleOptions>)(bsr -> bsr.options)), Codec.FLOAT.fieldOf("probability").forGetter((java.util.function.Function<Object, Object>)(bsr -> bsr.probability))).apply(instance, (java.util.function.BiFunction<ParticleOptions, Object, Object>)AmbientParticleSettings::new)));
    }
}
