package net.minecraft.world.level.levelgen.feature.foliageplacers;

import java.util.function.Function;
import java.util.function.BiFunction;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import java.util.Set;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import java.util.Random;
import net.minecraft.world.level.LevelSimulatedRW;
import net.minecraft.util.UniformInt;
import com.mojang.serialization.Codec;

public class DarkOakFoliagePlacer extends FoliagePlacer {
    public static final Codec<DarkOakFoliagePlacer> CODEC;
    
    public DarkOakFoliagePlacer(final UniformInt aft1, final UniformInt aft2) {
        super(aft1, aft2);
    }
    
    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.DARK_OAK_FOLIAGE_PLACER;
    }
    
    @Override
    protected void createFoliage(final LevelSimulatedRW bry, final Random random, final TreeConfiguration cmw, final int integer4, final FoliageAttachment b, final int integer6, final int integer7, final Set<BlockPos> set, final int integer9, final BoundingBox cqx) {
        final BlockPos fx12 = b.foliagePos().above(integer9);
        final boolean boolean13 = b.doubleTrunk();
        if (boolean13) {
            this.placeLeavesRow(bry, random, cmw, fx12, integer7 + 2, set, -1, boolean13, cqx);
            this.placeLeavesRow(bry, random, cmw, fx12, integer7 + 3, set, 0, boolean13, cqx);
            this.placeLeavesRow(bry, random, cmw, fx12, integer7 + 2, set, 1, boolean13, cqx);
            if (random.nextBoolean()) {
                this.placeLeavesRow(bry, random, cmw, fx12, integer7, set, 2, boolean13, cqx);
            }
        }
        else {
            this.placeLeavesRow(bry, random, cmw, fx12, integer7 + 2, set, -1, boolean13, cqx);
            this.placeLeavesRow(bry, random, cmw, fx12, integer7 + 1, set, 0, boolean13, cqx);
        }
    }
    
    @Override
    public int foliageHeight(final Random random, final int integer, final TreeConfiguration cmw) {
        return 4;
    }
    
    @Override
    protected boolean shouldSkipLocationSigned(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        return (integer3 == 0 && boolean6 && (integer2 == -integer5 || integer2 >= integer5) && (integer4 == -integer5 || integer4 >= integer5)) || super.shouldSkipLocationSigned(random, integer2, integer3, integer4, integer5, boolean6);
    }
    
    @Override
    protected boolean shouldSkipLocation(final Random random, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        if (integer3 == -1 && !boolean6) {
            return integer2 == integer5 && integer4 == integer5;
        }
        return integer3 == 1 && integer2 + integer4 > integer5 * 2 - 2;
    }
    
    static {
        CODEC = RecordCodecBuilder.<DarkOakFoliagePlacer>create((java.util.function.Function<RecordCodecBuilder.Instance<DarkOakFoliagePlacer>, ? extends App<RecordCodecBuilder.Mu<DarkOakFoliagePlacer>, DarkOakFoliagePlacer>>)(instance -> FoliagePlacer.<FoliagePlacer>foliagePlacerParts((RecordCodecBuilder.Instance<FoliagePlacer>)instance).apply(instance, (java.util.function.BiFunction<UniformInt, UniformInt, Object>)DarkOakFoliagePlacer::new)));
    }
}
