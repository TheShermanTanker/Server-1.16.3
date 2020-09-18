package net.minecraft.world.level.levelgen.feature.blockplacers;

import java.util.function.Supplier;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.mojang.serialization.Codec;

public class SimpleBlockPlacer extends BlockPlacer {
    public static final Codec<SimpleBlockPlacer> CODEC;
    public static final SimpleBlockPlacer INSTANCE;
    
    @Override
    protected BlockPlacerType<?> type() {
        return BlockPlacerType.SIMPLE_BLOCK_PLACER;
    }
    
    @Override
    public void place(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final Random random) {
        brv.setBlock(fx, cee, 2);
    }
    
    static {
        CODEC = Codec.<SimpleBlockPlacer>unit((java.util.function.Supplier<SimpleBlockPlacer>)(() -> SimpleBlockPlacer.INSTANCE));
        INSTANCE = new SimpleBlockPlacer();
    }
}
