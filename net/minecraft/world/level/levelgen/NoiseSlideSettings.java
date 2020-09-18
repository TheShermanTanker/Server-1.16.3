package net.minecraft.world.level.levelgen;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class NoiseSlideSettings {
    public static final Codec<NoiseSlideSettings> CODEC;
    private final int target;
    private final int size;
    private final int offset;
    
    public NoiseSlideSettings(final int integer1, final int integer2, final int integer3) {
        this.target = integer1;
        this.size = integer2;
        this.offset = integer3;
    }
    
    public int target() {
        return this.target;
    }
    
    public int size() {
        return this.size;
    }
    
    public int offset() {
        return this.offset;
    }
    
    static {
        CODEC = RecordCodecBuilder.<NoiseSlideSettings>create((java.util.function.Function<RecordCodecBuilder.Instance<NoiseSlideSettings>, ? extends App<RecordCodecBuilder.Mu<NoiseSlideSettings>, NoiseSlideSettings>>)(instance -> instance.<Integer, Integer, Integer>group(Codec.INT.fieldOf("target").forGetter((java.util.function.Function<Object, Object>)NoiseSlideSettings::target), Codec.intRange(0, 256).fieldOf("size").forGetter((java.util.function.Function<Object, Integer>)NoiseSlideSettings::size), Codec.INT.fieldOf("offset").forGetter((java.util.function.Function<Object, Object>)NoiseSlideSettings::offset)).<NoiseSlideSettings>apply(instance, NoiseSlideSettings::new)));
    }
}
