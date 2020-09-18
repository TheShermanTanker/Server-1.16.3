package net.minecraft.world.level.levelgen.feature;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.Iterator;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.core.BlockPos;
import java.util.Random;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.WorldGenLevel;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class CoralFeature extends Feature<NoneFeatureConfiguration> {
    public CoralFeature(final Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }
    
    @Override
    public boolean place(final WorldGenLevel bso, final ChunkGenerator cfv, final Random random, final BlockPos fx, final NoneFeatureConfiguration cme) {
        final BlockState cee7 = BlockTags.CORAL_BLOCKS.getRandomElement(random).defaultBlockState();
        return this.placeFeature(bso, random, fx, cee7);
    }
    
    protected abstract boolean placeFeature(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee);
    
    protected boolean placeCoralBlock(final LevelAccessor brv, final Random random, final BlockPos fx, final BlockState cee) {
        final BlockPos fx2 = fx.above();
        final BlockState cee2 = brv.getBlockState(fx);
        if ((!cee2.is(Blocks.WATER) && !cee2.is(BlockTags.CORALS)) || !brv.getBlockState(fx2).is(Blocks.WATER)) {
            return false;
        }
        brv.setBlock(fx, cee, 3);
        if (random.nextFloat() < 0.25f) {
            brv.setBlock(fx2, BlockTags.CORALS.getRandomElement(random).defaultBlockState(), 2);
        }
        else if (random.nextFloat() < 0.05f) {
            brv.setBlock(fx2, ((StateHolder<O, BlockState>)Blocks.SEA_PICKLE.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)SeaPickleBlock.PICKLES, random.nextInt(4) + 1), 2);
        }
        for (final Direction gc9 : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() < 0.2f) {
                final BlockPos fx3 = fx.relative(gc9);
                if (!brv.getBlockState(fx3).is(Blocks.WATER)) {
                    continue;
                }
                final BlockState cee3 = ((StateHolder<O, BlockState>)BlockTags.WALL_CORALS.getRandomElement(random).defaultBlockState()).<Comparable, Direction>setValue((Property<Comparable>)BaseCoralWallFanBlock.FACING, gc9);
                brv.setBlock(fx3, cee3, 2);
            }
        }
        return true;
    }
}
