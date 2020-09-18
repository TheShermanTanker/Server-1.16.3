package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.tags.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import java.util.Iterator;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class WallBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty UP;
    public static final EnumProperty<WallSide> EAST_WALL;
    public static final EnumProperty<WallSide> NORTH_WALL;
    public static final EnumProperty<WallSide> SOUTH_WALL;
    public static final EnumProperty<WallSide> WEST_WALL;
    public static final BooleanProperty WATERLOGGED;
    private final Map<BlockState, VoxelShape> shapeByIndex;
    private final Map<BlockState, VoxelShape> collisionShapeByIndex;
    private static final VoxelShape POST_TEST;
    private static final VoxelShape NORTH_TEST;
    private static final VoxelShape SOUTH_TEST;
    private static final VoxelShape WEST_TEST;
    private static final VoxelShape EAST_TEST;
    
    public WallBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)WallBlock.UP, true)).setValue(WallBlock.NORTH_WALL, WallSide.NONE)).setValue(WallBlock.EAST_WALL, WallSide.NONE)).setValue(WallBlock.SOUTH_WALL, WallSide.NONE)).setValue(WallBlock.WEST_WALL, WallSide.NONE)).<Comparable, Boolean>setValue((Property<Comparable>)WallBlock.WATERLOGGED, false));
        this.shapeByIndex = this.makeShapes(4.0f, 3.0f, 16.0f, 0.0f, 14.0f, 16.0f);
        this.collisionShapeByIndex = this.makeShapes(4.0f, 3.0f, 24.0f, 0.0f, 24.0f, 24.0f);
    }
    
    private static VoxelShape applyWallShape(final VoxelShape dde1, final WallSide cfm, final VoxelShape dde3, final VoxelShape dde4) {
        if (cfm == WallSide.TALL) {
            return Shapes.or(dde1, dde4);
        }
        if (cfm == WallSide.LOW) {
            return Shapes.or(dde1, dde3);
        }
        return dde1;
    }
    
    private Map<BlockState, VoxelShape> makeShapes(final float float1, final float float2, final float float3, final float float4, final float float5, final float float6) {
        final float float7 = 8.0f - float1;
        final float float8 = 8.0f + float1;
        final float float9 = 8.0f - float2;
        final float float10 = 8.0f + float2;
        final VoxelShape dde12 = Block.box(float7, 0.0, float7, float8, float3, float8);
        final VoxelShape dde13 = Block.box(float9, float4, 0.0, float10, float5, float10);
        final VoxelShape dde14 = Block.box(float9, float4, float9, float10, float5, 16.0);
        final VoxelShape dde15 = Block.box(0.0, float4, float9, float10, float5, float10);
        final VoxelShape dde16 = Block.box(float9, float4, float9, 16.0, float5, float10);
        final VoxelShape dde17 = Block.box(float9, float4, 0.0, float10, float6, float10);
        final VoxelShape dde18 = Block.box(float9, float4, float9, float10, float6, 16.0);
        final VoxelShape dde19 = Block.box(0.0, float4, float9, float10, float6, float10);
        final VoxelShape dde20 = Block.box(float9, float4, float9, 16.0, float6, float10);
        final ImmutableMap.Builder<BlockState, VoxelShape> builder21 = ImmutableMap.<BlockState, VoxelShape>builder();
        for (final Boolean boolean23 : WallBlock.UP.getPossibleValues()) {
            for (final WallSide cfm25 : WallBlock.EAST_WALL.getPossibleValues()) {
                for (final WallSide cfm26 : WallBlock.NORTH_WALL.getPossibleValues()) {
                    for (final WallSide cfm27 : WallBlock.WEST_WALL.getPossibleValues()) {
                        for (final WallSide cfm28 : WallBlock.SOUTH_WALL.getPossibleValues()) {
                            VoxelShape dde21 = Shapes.empty();
                            dde21 = applyWallShape(dde21, cfm25, dde16, dde20);
                            dde21 = applyWallShape(dde21, cfm27, dde15, dde19);
                            dde21 = applyWallShape(dde21, cfm26, dde13, dde17);
                            dde21 = applyWallShape(dde21, cfm28, dde14, dde18);
                            if (boolean23) {
                                dde21 = Shapes.or(dde21, dde12);
                            }
                            final BlockState cee33 = ((((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)WallBlock.UP, boolean23)).setValue(WallBlock.EAST_WALL, cfm25)).setValue(WallBlock.WEST_WALL, cfm27)).setValue(WallBlock.NORTH_WALL, cfm26)).<WallSide, WallSide>setValue(WallBlock.SOUTH_WALL, cfm28);
                            builder21.put(((StateHolder<O, BlockState>)cee33).<Comparable, Boolean>setValue((Property<Comparable>)WallBlock.WATERLOGGED, false), dde21);
                            builder21.put(((StateHolder<O, BlockState>)cee33).<Comparable, Boolean>setValue((Property<Comparable>)WallBlock.WATERLOGGED, true), dde21);
                        }
                    }
                }
            }
        }
        return (Map<BlockState, VoxelShape>)builder21.build();
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)this.shapeByIndex.get(cee);
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return (VoxelShape)this.collisionShapeByIndex.get(cee);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    private boolean connectsTo(final BlockState cee, final boolean boolean2, final Direction gc) {
        final Block bul5 = cee.getBlock();
        final boolean boolean3 = bul5 instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(cee, gc);
        return cee.is(BlockTags.WALLS) || (!Block.isExceptionForConnection(bul5) && boolean2) || bul5 instanceof IronBarsBlock || boolean3;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final LevelReader brw3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        final FluidState cuu5 = bnv.getLevel().getFluidState(bnv.getClickedPos());
        final BlockPos fx5 = fx4.north();
        final BlockPos fx6 = fx4.east();
        final BlockPos fx7 = fx4.south();
        final BlockPos fx8 = fx4.west();
        final BlockPos fx9 = fx4.above();
        final BlockState cee11 = brw3.getBlockState(fx5);
        final BlockState cee12 = brw3.getBlockState(fx6);
        final BlockState cee13 = brw3.getBlockState(fx7);
        final BlockState cee14 = brw3.getBlockState(fx8);
        final BlockState cee15 = brw3.getBlockState(fx9);
        final boolean boolean16 = this.connectsTo(cee11, cee11.isFaceSturdy(brw3, fx5, Direction.SOUTH), Direction.SOUTH);
        final boolean boolean17 = this.connectsTo(cee12, cee12.isFaceSturdy(brw3, fx6, Direction.WEST), Direction.WEST);
        final boolean boolean18 = this.connectsTo(cee13, cee13.isFaceSturdy(brw3, fx7, Direction.NORTH), Direction.NORTH);
        final boolean boolean19 = this.connectsTo(cee14, cee14.isFaceSturdy(brw3, fx8, Direction.EAST), Direction.EAST);
        final BlockState cee16 = ((StateHolder<O, BlockState>)this.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)WallBlock.WATERLOGGED, cuu5.getType() == Fluids.WATER);
        return this.updateShape(brw3, cee16, fx9, cee15, boolean16, boolean17, boolean18, boolean19);
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee1.<Boolean>getValue((Property<Boolean>)WallBlock.WATERLOGGED)) {
            brv.getLiquidTicks().scheduleTick(fx5, Fluids.WATER, Fluids.WATER.getTickDelay(brv));
        }
        if (gc == Direction.DOWN) {
            return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        }
        if (gc == Direction.UP) {
            return this.topUpdate(brv, cee1, fx6, cee3);
        }
        return this.sideUpdate(brv, fx5, cee1, fx6, cee3, gc);
    }
    
    private static boolean isConnected(final BlockState cee, final Property<WallSide> cfg) {
        return cee.<WallSide>getValue(cfg) != WallSide.NONE;
    }
    
    private static boolean isCovered(final VoxelShape dde1, final VoxelShape dde2) {
        return !Shapes.joinIsNotEmpty(dde2, dde1, BooleanOp.ONLY_FIRST);
    }
    
    private BlockState topUpdate(final LevelReader brw, final BlockState cee2, final BlockPos fx, final BlockState cee4) {
        final boolean boolean6 = isConnected(cee2, WallBlock.NORTH_WALL);
        final boolean boolean7 = isConnected(cee2, WallBlock.EAST_WALL);
        final boolean boolean8 = isConnected(cee2, WallBlock.SOUTH_WALL);
        final boolean boolean9 = isConnected(cee2, WallBlock.WEST_WALL);
        return this.updateShape(brw, cee2, fx, cee4, boolean6, boolean7, boolean8, boolean9);
    }
    
    private BlockState sideUpdate(final LevelReader brw, final BlockPos fx2, final BlockState cee3, final BlockPos fx4, final BlockState cee5, final Direction gc) {
        final Direction gc2 = gc.getOpposite();
        final boolean boolean9 = (gc == Direction.NORTH) ? this.connectsTo(cee5, cee5.isFaceSturdy(brw, fx4, gc2), gc2) : isConnected(cee3, WallBlock.NORTH_WALL);
        final boolean boolean10 = (gc == Direction.EAST) ? this.connectsTo(cee5, cee5.isFaceSturdy(brw, fx4, gc2), gc2) : isConnected(cee3, WallBlock.EAST_WALL);
        final boolean boolean11 = (gc == Direction.SOUTH) ? this.connectsTo(cee5, cee5.isFaceSturdy(brw, fx4, gc2), gc2) : isConnected(cee3, WallBlock.SOUTH_WALL);
        final boolean boolean12 = (gc == Direction.WEST) ? this.connectsTo(cee5, cee5.isFaceSturdy(brw, fx4, gc2), gc2) : isConnected(cee3, WallBlock.WEST_WALL);
        final BlockPos fx5 = fx2.above();
        final BlockState cee6 = brw.getBlockState(fx5);
        return this.updateShape(brw, cee3, fx5, cee6, boolean9, boolean10, boolean11, boolean12);
    }
    
    private BlockState updateShape(final LevelReader brw, final BlockState cee2, final BlockPos fx, final BlockState cee4, final boolean boolean5, final boolean boolean6, final boolean boolean7, final boolean boolean8) {
        final VoxelShape dde10 = cee4.getCollisionShape(brw, fx).getFaceShape(Direction.DOWN);
        final BlockState cee5 = this.updateSides(cee2, boolean5, boolean6, boolean7, boolean8, dde10);
        return ((StateHolder<O, BlockState>)cee5).<Comparable, Boolean>setValue((Property<Comparable>)WallBlock.UP, this.shouldRaisePost(cee5, cee4, dde10));
    }
    
    private boolean shouldRaisePost(final BlockState cee1, final BlockState cee2, final VoxelShape dde) {
        final boolean boolean5 = cee2.getBlock() instanceof WallBlock && cee2.<Boolean>getValue((Property<Boolean>)WallBlock.UP);
        if (boolean5) {
            return true;
        }
        final WallSide cfm6 = cee1.<WallSide>getValue(WallBlock.NORTH_WALL);
        final WallSide cfm7 = cee1.<WallSide>getValue(WallBlock.SOUTH_WALL);
        final WallSide cfm8 = cee1.<WallSide>getValue(WallBlock.EAST_WALL);
        final WallSide cfm9 = cee1.<WallSide>getValue(WallBlock.WEST_WALL);
        final boolean boolean6 = cfm7 == WallSide.NONE;
        final boolean boolean7 = cfm9 == WallSide.NONE;
        final boolean boolean8 = cfm8 == WallSide.NONE;
        final boolean boolean9 = cfm6 == WallSide.NONE;
        final boolean boolean10 = (boolean9 && boolean6 && boolean7 && boolean8) || boolean9 != boolean6 || boolean7 != boolean8;
        if (boolean10) {
            return true;
        }
        final boolean boolean11 = (cfm6 == WallSide.TALL && cfm7 == WallSide.TALL) || (cfm8 == WallSide.TALL && cfm9 == WallSide.TALL);
        return !boolean11 && (cee2.getBlock().is(BlockTags.WALL_POST_OVERRIDE) || isCovered(dde, WallBlock.POST_TEST));
    }
    
    private BlockState updateSides(final BlockState cee, final boolean boolean2, final boolean boolean3, final boolean boolean4, final boolean boolean5, final VoxelShape dde) {
        return (((((StateHolder<O, BlockState>)cee).setValue(WallBlock.NORTH_WALL, this.makeWallState(boolean2, dde, WallBlock.NORTH_TEST))).setValue(WallBlock.EAST_WALL, this.makeWallState(boolean3, dde, WallBlock.EAST_TEST))).setValue(WallBlock.SOUTH_WALL, this.makeWallState(boolean4, dde, WallBlock.SOUTH_TEST))).<WallSide, WallSide>setValue(WallBlock.WEST_WALL, this.makeWallState(boolean5, dde, WallBlock.WEST_TEST));
    }
    
    private WallSide makeWallState(final boolean boolean1, final VoxelShape dde2, final VoxelShape dde3) {
        if (!boolean1) {
            return WallSide.NONE;
        }
        if (isCovered(dde2, dde3)) {
            return WallSide.TALL;
        }
        return WallSide.LOW;
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)WallBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return !cee.<Boolean>getValue((Property<Boolean>)WallBlock.WATERLOGGED);
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(WallBlock.UP, WallBlock.NORTH_WALL, WallBlock.EAST_WALL, WallBlock.WEST_WALL, WallBlock.SOUTH_WALL, WallBlock.WATERLOGGED);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case CLOCKWISE_180: {
                return (((((StateHolder<O, BlockState>)cee).setValue(WallBlock.NORTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.SOUTH_WALL))).setValue(WallBlock.EAST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.WEST_WALL))).setValue(WallBlock.SOUTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.NORTH_WALL))).<WallSide, Comparable>setValue(WallBlock.WEST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.EAST_WALL));
            }
            case COUNTERCLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue(WallBlock.NORTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.EAST_WALL))).setValue(WallBlock.EAST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.SOUTH_WALL))).setValue(WallBlock.SOUTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.WEST_WALL))).<WallSide, Comparable>setValue(WallBlock.WEST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.NORTH_WALL));
            }
            case CLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue(WallBlock.NORTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.WEST_WALL))).setValue(WallBlock.EAST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.NORTH_WALL))).setValue(WallBlock.SOUTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.EAST_WALL))).<WallSide, Comparable>setValue(WallBlock.WEST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.SOUTH_WALL));
            }
            default: {
                return cee;
            }
        }
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        switch (byd) {
            case LEFT_RIGHT: {
                return (((StateHolder<O, BlockState>)cee).setValue(WallBlock.NORTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.SOUTH_WALL))).<WallSide, Comparable>setValue(WallBlock.SOUTH_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.NORTH_WALL));
            }
            case FRONT_BACK: {
                return (((StateHolder<O, BlockState>)cee).setValue(WallBlock.EAST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.WEST_WALL))).<WallSide, Comparable>setValue(WallBlock.WEST_WALL, (Comparable)cee.<V>getValue((Property<V>)WallBlock.EAST_WALL));
            }
            default: {
                return super.mirror(cee, byd);
            }
        }
    }
    
    static {
        UP = BlockStateProperties.UP;
        EAST_WALL = BlockStateProperties.EAST_WALL;
        NORTH_WALL = BlockStateProperties.NORTH_WALL;
        SOUTH_WALL = BlockStateProperties.SOUTH_WALL;
        WEST_WALL = BlockStateProperties.WEST_WALL;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        POST_TEST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);
        NORTH_TEST = Block.box(7.0, 0.0, 0.0, 9.0, 16.0, 9.0);
        SOUTH_TEST = Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 16.0);
        WEST_TEST = Block.box(0.0, 0.0, 7.0, 9.0, 16.0, 9.0);
        EAST_TEST = Block.box(7.0, 0.0, 7.0, 16.0, 16.0, 9.0);
    }
}
