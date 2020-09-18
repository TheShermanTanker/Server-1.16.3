package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.serialization.Codec;

public class BlockStateConfiguration implements FeatureConfiguration {
    public static final Codec<BlockStateConfiguration> CODEC;
    public final BlockState state;
    
    public BlockStateConfiguration(final BlockState cee) {
        this.state = cee;
    }
    
    static {
        CODEC = BlockState.CODEC.fieldOf("state").<BlockStateConfiguration>xmap((java.util.function.Function<? super BlockState, ? extends BlockStateConfiguration>)BlockStateConfiguration::new, (java.util.function.Function<? super BlockStateConfiguration, ? extends BlockState>)(clp -> clp.state)).codec();
    }
}
