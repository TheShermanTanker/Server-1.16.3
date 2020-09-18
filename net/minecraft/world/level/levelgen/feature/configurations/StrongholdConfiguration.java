package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;

public class StrongholdConfiguration {
    public static final Codec<StrongholdConfiguration> CODEC;
    private final int distance;
    private final int spread;
    private final int count;
    
    public StrongholdConfiguration(final int integer1, final int integer2, final int integer3) {
        this.distance = integer1;
        this.spread = integer2;
        this.count = integer3;
    }
    
    public int distance() {
        return this.distance;
    }
    
    public int spread() {
        return this.spread;
    }
    
    public int count() {
        return this.count;
    }
    
    static {
        CODEC = RecordCodecBuilder.<StrongholdConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<StrongholdConfiguration>, ? extends App<RecordCodecBuilder.Mu<StrongholdConfiguration>, StrongholdConfiguration>>)(instance -> instance.<Integer, Integer, Integer>group(Codec.intRange(0, 1023).fieldOf("distance").forGetter((java.util.function.Function<Object, Integer>)StrongholdConfiguration::distance), Codec.intRange(0, 1023).fieldOf("spread").forGetter((java.util.function.Function<Object, Integer>)StrongholdConfiguration::spread), Codec.intRange(1, 4095).fieldOf("count").forGetter((java.util.function.Function<Object, Integer>)StrongholdConfiguration::count)).<StrongholdConfiguration>apply(instance, StrongholdConfiguration::new)));
    }
}
