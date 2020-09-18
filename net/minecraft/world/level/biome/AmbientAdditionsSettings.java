package net.minecraft.world.level.biome;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.sounds.SoundEvent;
import com.mojang.serialization.Codec;

public class AmbientAdditionsSettings {
    public static final Codec<AmbientAdditionsSettings> CODEC;
    private SoundEvent soundEvent;
    private double tickChance;
    
    public AmbientAdditionsSettings(final SoundEvent adn, final double double2) {
        this.soundEvent = adn;
        this.tickChance = double2;
    }
    
    static {
        CODEC = RecordCodecBuilder.<AmbientAdditionsSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<AmbientAdditionsSettings>, ? extends App<RecordCodecBuilder.Mu<AmbientAdditionsSettings>, AmbientAdditionsSettings>>)(instance -> instance.<SoundEvent, Object>group(SoundEvent.CODEC.fieldOf("sound").forGetter((java.util.function.Function<Object, SoundEvent>)(bsp -> bsp.soundEvent)), Codec.DOUBLE.fieldOf("tick_chance").forGetter((java.util.function.Function<Object, Object>)(bsp -> bsp.tickChance))).apply(instance, (java.util.function.BiFunction<SoundEvent, Object, Object>)AmbientAdditionsSettings::new)));
    }
}
