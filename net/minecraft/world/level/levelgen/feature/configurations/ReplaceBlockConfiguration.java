package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class ReplaceBlockConfiguration implements FeatureConfiguration {
    public static final Codec<ReplaceBlockConfiguration> CODEC;
    public final BlockState target;
    public final BlockState state;
    
    public ReplaceBlockConfiguration(final BlockState cee1, final BlockState cee2) {
        this.target = cee1;
        this.state = cee2;
    }
    
    static {
        CODEC = RecordCodecBuilder.<ReplaceBlockConfiguration>create((java.util.function.Function<RecordCodecBuilder.Instance<ReplaceBlockConfiguration>, ? extends App<RecordCodecBuilder.Mu<ReplaceBlockConfiguration>, ReplaceBlockConfiguration>>)(instance -> instance.<BlockState, BlockState>group(BlockState.CODEC.fieldOf("target").forGetter((java.util.function.Function<Object, BlockState>)(cmm -> cmm.target)), BlockState.CODEC.fieldOf("state").forGetter((java.util.function.Function<Object, BlockState>)(cmm -> cmm.state))).apply(instance, (java.util.function.BiFunction<BlockState, BlockState, Object>)ReplaceBlockConfiguration::new)));
    }
}
