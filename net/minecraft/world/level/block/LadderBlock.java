package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import javax.annotation.Nullable;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class LadderBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING;
    public static final BooleanProperty WATERLOGGED;
    protected static final VoxelShape EAST_AABB;
    protected static final VoxelShape WEST_AABB;
    protected static final VoxelShape SOUTH_AABB;
    protected static final VoxelShape NORTH_AABB;
    
    protected LadderBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)LadderBlock.FACING, Direction.NORTH)).<Comparable, Boolean>setValue((Property<Comparable>)LadderBlock.WATERLOGGED, false));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction>getValue((Property<Direction>)LadderBlock.FACING)) {
            case NORTH: {
                return LadderBlock.NORTH_AABB;
            }
            case SOUTH: {
                return LadderBlock.SOUTH_AABB;
            }
            case WEST: {
                return LadderBlock.WEST_AABB;
            }
            default: {
                return LadderBlock.EAST_AABB;
            }
        }
    }
    
    private boolean canAttachTo(final BlockGetter bqz, final BlockPos fx, final Direction gc) {
        final BlockState cee5 = bqz.getBlockState(fx);
        return cee5.isFaceSturdy(bqz, fx, gc);
    }
    
    @Override
    public boolean canSurvive(final BlockState cee, final LevelReader brw, final BlockPos fx) {
        final Direction gc5 = cee.<Direction>getValue((Property<Direction>)LadderBlock.FACING);
        return this.canAttachTo(brw, fx.relative(gc5.getOpposite()), gc5);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (gc.getOpposite() == cee1.<Comparable>getValue((Property<Comparable>)LadderBlock.FACING) && !cee1.canSurvive(brv, fx5)) {
            return Blocks.AIR.defaultBlockState();
        }
        if (cee1.<Boolean>getValue((Property<Boolean>)LadderBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        if (!bnv.replacingClickedOnBlock()) {
            final BlockState cee3 = bnv.getLevel().getBlockState(bnv.getClickedPos().relative(bnv.getClickedFace().getOpposite()));
            if (cee3.is(this) && cee3.<Comparable>getValue((Property<Comparable>)LadderBlock.FACING) == bnv.getClickedFace()) {
                return null;
            }
        }
        BlockState cee3 = this.defaultBlockState();
        final LevelReader brw4 = bnv.getLevel();
        final BlockPos fx5 = bnv.getClickedPos();
        final FluidState cuu6 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        for (final Direction gc10 : bnv.getNearestLookingDirections()) {
            if (gc10.getAxis().isHorizontal()) {
                cee3 = ((StateHolder<O, BlockState>)cee3).<Comparable, Direction>setValue((Property<Comparable>)LadderBlock.FACING, gc10.getOpposite());
                if (cee3.canSurvive(brw4, fx5)) {
                    return ((StateHolder<O, BlockState>)cee3).<Comparable, Boolean>setValue((Property<Comparable>)LadderBlock.WATERLOGGED, cuu6.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Comparable, Direction>setValue((Property<Comparable>)LadderBlock.FACING, bzj.rotate(cee.<Direction>getValue((Property<Direction>)LadderBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue((Property<Direction>)LadderBlock.FACING)));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(LadderBlock.FACING, LadderBlock.WATERLOGGED);
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)LadderBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    static {
        FACING = HorizontalDirectionalBlock.FACING;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        EAST_AABB = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
        WEST_AABB = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
        SOUTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
        NORTH_AABB = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    }
}
