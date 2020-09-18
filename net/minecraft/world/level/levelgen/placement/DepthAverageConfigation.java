package net.minecraft.world.level.levelgen.placement;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;

public class DepthAverageConfigation implements DecoratorConfiguration {
    public static final Codec<DepthAverageConfigation> CODEC;
    public final int baseline;
    public final int spread;
    
    public DepthAverageConfigation(final int integer1, final int integer2) {
        this.baseline = integer1;
        this.spread = integer2;
    }
    
    static {
        CODEC = RecordCodecBuilder.<DepthAverageConfigation>create((java.util.function.Function<RecordCodecBuilder.Instance<DepthAverageConfigation>, ? extends App<RecordCodecBuilder.Mu<DepthAverageConfigation>, DepthAverageConfigation>>)(instance -> instance.group(Codec.INT.fieldOf("baseline").forGetter((java.util.function.Function<Object, Object>)(cpt -> cpt.baseline)), Codec.INT.fieldOf("spread").forGetter((java.util.function.Function<Object, Object>)(cpt -> cpt.spread))).apply(instance, (java.util.function.BiFunction<Object, Object, Object>)DepthAverageConfigation::new)));
    }
}
