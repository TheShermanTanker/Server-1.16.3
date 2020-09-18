package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.util.UniformInt;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class DiskConfiguration implements FeatureConfiguration {
    public static final Codec<DiskConfiguration> CODEC;
    public final BlockState state;
    public final UniformInt radius;
    public final int halfHeight;
    public final List<BlockState> targets;
    
    public DiskConfiguration(final BlockState cee, final UniformInt aft, final int integer, final List<BlockState> list) {
        this.state = cee;
        this.radius = aft;
        this.halfHeight = integer;
        this.targets = list;
    }
    
    static {
        CODEC = RecordCodecBuilder.<DiskConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<DiskConfiguration>, ? extends App<RecordCodecBuilder.Mu<DiskConfiguration>, DiskConfiguration>>)(instance -> instance.<BlockState, UniformInt, Integer, java.util.List<BlockState>>group(BlockState.CODEC.fieldOf("state").forGetter((java.util.function.Function<Object, BlockState>)(clv -> clv.state)), UniformInt.codec(0, 4, 4).fieldOf("radius").forGetter((java.util.function.Function<Object, UniformInt>)(clv -> clv.radius)), Codec.intRange(0, 4).fieldOf("half_height").forGetter((java.util.function.Function<Object, Integer>)(clv -> clv.halfHeight)), BlockState.CODEC.listOf().fieldOf("targets").forGetter((java.util.function.Function<Object, java.util.List<BlockState>>)(clv -> clv.targets))).<DiskConfiguration>apply(instance, DiskConfiguration::new)));
    }
}
