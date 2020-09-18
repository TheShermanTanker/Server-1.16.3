package net.minecraft.world.level.block;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DeadBushBlock extends BushBlock {
    protected static final VoxelShape SHAPE;
    
    protected DeadBushBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return DeadBushBlock.SHAPE;
    }
    
    @Override
    protected boolean mayPlaceOn(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        final Block bul5 = cee.getBlock();
        return bul5 == Blocks.SAND || bul5 == Blocks.RED_SAND || bul5 == Blocks.TERRACOTTA || bul5 == Blocks.WHITE_TERRACOTTA || bul5 == Blocks.ORANGE_TERRACOTTA || bul5 == Blocks.MAGENTA_TERRACOTTA || bul5 == Blocks.LIGHT_BLUE_TERRACOTTA || bul5 == Blocks.YELLOW_TERRACOTTA || bul5 == Blocks.LIME_TERRACOTTA || bul5 == Blocks.PINK_TERRACOTTA || bul5 == Blocks.GRAY_TERRACOTTA || bul5 == Blocks.LIGHT_GRAY_TERRACOTTA || bul5 == Blocks.CYAN_TERRACOTTA || bul5 == Blocks.PURPLE_TERRACOTTA || bul5 == Blocks.BLUE_TERRACOTTA || bul5 == Blocks.BROWN_TERRACOTTA || bul5 == Blocks.GREEN_TERRACOTTA || bul5 == Blocks.RED_TERRACOTTA || bul5 == Blocks.BLACK_TERRACOTTA || bul5 == Blocks.DIRT || bul5 == Blocks.COARSE_DIRT || bul5 == Blocks.PODZOL;
    }
    
    static {
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    }
}
