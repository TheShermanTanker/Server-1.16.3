package net.minecraft.world.level.biome;

import net.minecraft.sounds.SoundEvents;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import com.mojang.serialization.Codec;

public class AmbientMoodSettings {
    public static final Codec<AmbientMoodSettings> CODEC;
    public static final AmbientMoodSettings LEGACY_CAVE_SETTINGS;
    private SoundEvent soundEvent;
    private int tickDelay;
    private int blockSearchExtent;
    private double soundPositionOffset;
    
    public AmbientMoodSettings(final SoundEvent adn, final int integer2, final int integer3, final double double4) {
        this.soundEvent = adn;
        this.tickDelay = integer2;
        this.blockSearchExtent = integer3;
        this.soundPositionOffset = double4;
    }
    
    static {
        CODEC = RecordCodecBuilder.<AmbientMoodSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<AmbientMoodSettings>, ? extends App<RecordCodecBuilder.Mu<AmbientMoodSettings>, AmbientMoodSettings>>)(instance -> instance.<SoundEvent, Integer, Integer, Double>group(SoundEvent.CODEC.fieldOf("sound").forGetter((java.util.function.Function<Object, SoundEvent>)(bsq -> bsq.soundEvent)), Codec.INT.fieldOf("tick_delay").forGetter((java.util.function.Function<Object, Object>)(bsq -> bsq.tickDelay)), Codec.INT.fieldOf("block_search_extent").forGetter((java.util.function.Function<Object, Object>)(bsq -> bsq.blockSearchExtent)), Codec.DOUBLE.fieldOf("offset").forGetter((java.util.function.Function<Object, Object>)(bsq -> bsq.soundPositionOffset))).<AmbientMoodSettings>apply(instance, AmbientMoodSettings::new)));
        LEGACY_CAVE_SETTINGS = new AmbientMoodSettings(SoundEvents.AMBIENT_CAVE, 6000, 8, 2.0);
    }
}
