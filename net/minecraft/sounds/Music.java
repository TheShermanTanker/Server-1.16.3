package net.minecraft.sounds;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class Music {
    public static final Codec<Music> CODEC;
    private final SoundEvent event;
    private final int minDelay;
    private final int maxDelay;
    private final boolean replaceCurrentMusic;
    
    public Music(final SoundEvent adn, final int integer2, final int integer3, final boolean boolean4) {
        this.event = adn;
        this.minDelay = integer2;
        this.maxDelay = integer3;
        this.replaceCurrentMusic = boolean4;
    }
    
    static {
        CODEC = RecordCodecBuilder.<Music>create((java.util.function.Function<RecordCodecBuilder.Instance<Music>, ? extends App<RecordCodecBuilder.Mu<Music>, Music>>)(instance -> instance.<SoundEvent, Integer, Integer, Boolean>group(SoundEvent.CODEC.fieldOf("sound").forGetter((java.util.function.Function<Object, SoundEvent>)(adl -> adl.event)), Codec.INT.fieldOf("min_delay").forGetter((java.util.function.Function<Object, Object>)(adl -> adl.minDelay)), Codec.INT.fieldOf("max_delay").forGetter((java.util.function.Function<Object, Object>)(adl -> adl.maxDelay)), Codec.BOOL.fieldOf("replace_current_music").forGetter((java.util.function.Function<Object, Object>)(adl -> adl.replaceCurrentMusic))).<Music>apply(instance, Music::new)));
    }
}
