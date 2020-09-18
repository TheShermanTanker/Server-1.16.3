package net.minecraft.world.level.levelgen.feature.blockplacers;

import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import com.mojang.serialization.Codec;

public class ColumnPlacer extends BlockPlacer {
    public static final Codec<ColumnPlacer> CODEC;
    private final int minSize;
    private final int extraSize;
    
    public ColumnPlacer(final int integer1, final int integer2) {
        this.minSize = integer1;
        this.extraSize = integer2;
    }
    
    @Override
    protected BlockPlacerType<?> type() {
        return BlockPlacerType.COLUMN_PLACER;
    }
    
    @Override
    public void place(final LevelAccessor brv, final BlockPos fx, final BlockState cee, final Random random) {
        final BlockPos.MutableBlockPos a6 = fx.mutable();
        for (int integer7 = this.minSize + random.nextInt(random.nextInt(this.extraSize + 1) + 1), integer8 = 0; integer8 < integer7; ++integer8) {
            brv.setBlock(a6, cee, 2);
            a6.move(Direction.UP);
        }
    }
    
    static {
        CODEC = RecordCodecBuilder.<ColumnPlacer>create((java.util.function.Function<RecordCodecBuilder.Instance<ColumnPlacer>, ? extends App<RecordCodecBuilder.Mu<ColumnPlacer>, ColumnPlacer>>)(instance -> instance.group(Codec.INT.fieldOf("min_size").forGetter((java.util.function.Function<Object, Object>)(clk -> clk.minSize)), Codec.INT.fieldOf("extra_size").forGetter((java.util.function.Function<Object, Object>)(clk -> clk.extraSize))).apply(instance, (java.util.function.BiFunction<Object, Object, Object>)ColumnPlacer::new)));
    }
}
