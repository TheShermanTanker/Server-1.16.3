package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class ReplaceSphereConfiguration implements FeatureConfiguration {
    public static final Codec<ReplaceSphereConfiguration> CODEC;
    public final BlockState targetState;
    public final BlockState replaceState;
    private final UniformInt radius;
    
    public ReplaceSphereConfiguration(final BlockState cee1, final BlockState cee2, final UniformInt aft) {
        this.targetState = cee1;
        this.replaceState = cee2;
        this.radius = aft;
    }
    
    public UniformInt radius() {
        return this.radius;
    }
    
    static {
        CODEC = RecordCodecBuilder.<ReplaceSphereConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<ReplaceSphereConfiguration>, ? extends App<RecordCodecBuilder.Mu<ReplaceSphereConfiguration>, ReplaceSphereConfiguration>>)(instance -> instance.<BlockState, BlockState, UniformInt>group(BlockState.CODEC.fieldOf("target").forGetter((java.util.function.Function<Object, BlockState>)(cmn -> cmn.targetState)), BlockState.CODEC.fieldOf("state").forGetter((java.util.function.Function<Object, BlockState>)(cmn -> cmn.replaceState)), UniformInt.CODEC.fieldOf("radius").forGetter((java.util.function.Function<Object, UniformInt>)(cmn -> cmn.radius))).<ReplaceSphereConfiguration>apply(instance, ReplaceSphereConfiguration::new)));
    }
}
