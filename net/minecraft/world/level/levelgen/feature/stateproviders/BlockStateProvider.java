package net.minecraft.world.level.levelgen.feature.stateproviders;

import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Random;
import com.mojang.serialization.Codec;

public abstract class BlockStateProvider {
    public static final Codec<BlockStateProvider> CODEC;
    
    protected abstract BlockStateProviderType<?> type();
    
    public abstract BlockState getState(final Random random, final BlockPos fx);
    
    static {
        CODEC = Registry.BLOCKSTATE_PROVIDER_TYPES.<BlockStateProvider>dispatch((java.util.function.Function<? super BlockStateProvider, ?>)BlockStateProvider::type, (java.util.function.Function<? super Object, ? extends Codec<? extends BlockStateProvider>>)BlockStateProviderType::codec);
    }
}
