package net.minecraft.world.level.levelgen.feature.configurations;

import java.util.function.Function;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import com.mojang.serialization.Codec;

public class BlockPileConfiguration implements FeatureConfiguration {
    public static final Codec<BlockPileConfiguration> CODEC;
    public final BlockStateProvider stateProvider;
    
    public BlockPileConfiguration(final BlockStateProvider cnq) {
        this.stateProvider = cnq;
    }
    
    static {
        CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").<BlockPileConfiguration>xmap((java.util.function.Function<? super BlockStateProvider, ? extends BlockPileConfiguration>)BlockPileConfiguration::new, (java.util.function.Function<? super BlockPileConfiguration, ? extends BlockStateProvider>)(clo -> clo.stateProvider)).codec();
    }
}
