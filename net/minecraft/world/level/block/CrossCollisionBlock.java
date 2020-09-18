package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.stream.Collector;
import net.minecraft.Util;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.FluidState;
import java.util.function.ToIntFunction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.Shapes;
import com.google.common.collect.UnmodifiableIterator;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class CrossCollisionBlock extends Block implements SimpleWaterloggedBlock {
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty WATERLOGGED;
    protected static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    protected final VoxelShape[] collisionShapeByIndex;
    protected final VoxelShape[] shapeByIndex;
    private final Object2IntMap<BlockState> stateToIndex;
    
    protected CrossCollisionBlock(final float float1, final float float2, final float float3, final float float4, final float float5, final Properties c) {
        super(c);
        this.stateToIndex = new Object2IntOpenHashMap<BlockState>();
        this.collisionShapeByIndex = this.makeShapes(float1, float2, float5, 0.0f, float5);
        this.shapeByIndex = this.makeShapes(float1, float2, float3, 0.0f, float4);
        for (final BlockState cee9 : this.stateDefinition.getPossibleStates()) {
            this.getAABBIndex(cee9);
        }
    }
    
    protected VoxelShape[] makeShapes(final float float1, final float float2, final float float3, final float float4, final float float5) {
        final float float6 = 8.0f - float1;
        final float float7 = 8.0f + float1;
        final float float8 = 8.0f - float2;
        final float float9 = 8.0f + float2;
        final VoxelShape dde11 = Block.box(float6, 0.0, float6, float7, float3, float7);
        final VoxelShape dde12 = Block.box(float8, float4, 0.0, float9, float5, float9);
        final VoxelShape dde13 = Block.box(float8, float4, float8, float9, float5, 16.0);
        final VoxelShape dde14 = Block.box(0.0, float4, float8, float9, float5, float9);
        final VoxelShape dde15 = Block.box(float8, float4, float8, 16.0, float5, float9);
        final VoxelShape dde16 = Shapes.or(dde12, dde15);
        final VoxelShape dde17 = Shapes.or(dde13, dde14);
        final VoxelShape[] arr18 = { Shapes.empty(), dde13, dde14, dde17, dde12, Shapes.or(dde13, dde12), Shapes.or(dde14, dde12), Shapes.or(dde17, dde12), dde15, Shapes.or(dde13, dde15), Shapes.or(dde14, dde15), Shapes.or(dde17, dde15), dde16, Shapes.or(dde13, dde16), Shapes.or(dde14, dde16), Shapes.or(dde17, dde16) };
        for (int integer19 = 0; integer19 < 16; ++integer19) {
            arr18[integer19] = Shapes.or(dde11, arr18[integer19]);
        }
        return arr18;
    }
    
    @Override
    public boolean propagatesSkylightDown(final BlockState cee, final BlockGetter bqz, final BlockPos fx) {
        return !cee.<Boolean>getValue((Property<Boolean>)CrossCollisionBlock.WATERLOGGED);
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.shapeByIndex[this.getAABBIndex(cee)];
    }
    
    @Override
    public VoxelShape getCollisionShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return this.collisionShapeByIndex[this.getAABBIndex(cee)];
    }
    
    private static int indexFor(final Direction gc) {
        return 1 << gc.get2DDataValue();
    }
    
    protected int getAABBIndex(final BlockState cee) {
        return this.stateToIndex.computeIntIfAbsent(cee, (java.util.function.ToIntFunction<? super BlockState>)(cee -> {
            int integer2 = 0;
            if (cee.<Boolean>getValue((Property<Boolean>)CrossCollisionBlock.NORTH)) {
                integer2 |= indexFor(Direction.NORTH);
            }
            if (cee.<Boolean>getValue((Property<Boolean>)CrossCollisionBlock.EAST)) {
                integer2 |= indexFor(Direction.EAST);
            }
            if (cee.<Boolean>getValue((Property<Boolean>)CrossCollisionBlock.SOUTH)) {
                integer2 |= indexFor(Direction.SOUTH);
            }
            if (cee.<Boolean>getValue((Property<Boolean>)CrossCollisionBlock.WEST)) {
                integer2 |= indexFor(Direction.WEST);
            }
            return integer2;
        }));
    }
    
    @Override
    public FluidState getFluidState(final BlockState cee) {
        if (cee.<Boolean>getValue((Property<Boolean>)CrossCollisionBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(cee);
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case CLOCKWISE_180: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)CrossCollisionBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.SOUTH))).setValue((Property<Comparable>)CrossCollisionBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.WEST))).setValue((Property<Comparable>)CrossCollisionBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.NORTH))).<Comparable, Comparable>setValue((Property<Comparable>)CrossCollisionBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.EAST));
            }
            case COUNTERCLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)CrossCollisionBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.EAST))).setValue((Property<Comparable>)CrossCollisionBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.SOUTH))).setValue((Property<Comparable>)CrossCollisionBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.WEST))).<Comparable, Comparable>setValue((Property<Comparable>)CrossCollisionBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.NORTH));
            }
            case CLOCKWISE_90: {
                return (((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)CrossCollisionBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.WEST))).setValue((Property<Comparable>)CrossCollisionBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.NORTH))).setValue((Property<Comparable>)CrossCollisionBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.EAST))).<Comparable, Comparable>setValue((Property<Comparable>)CrossCollisionBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.SOUTH));
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
                return (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)CrossCollisionBlock.NORTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.SOUTH))).<Comparable, Comparable>setValue((Property<Comparable>)CrossCollisionBlock.SOUTH, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.NORTH));
            }
            case FRONT_BACK: {
                return (((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)CrossCollisionBlock.EAST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.WEST))).<Comparable, Comparable>setValue((Property<Comparable>)CrossCollisionBlock.WEST, (Comparable)cee.<V>getValue((Property<V>)CrossCollisionBlock.EAST));
            }
            default: {
                return super.mirror(cee, byd);
            }
        }
    }
    
    static {
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        PROPERTY_BY_DIRECTION = (Map)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(entry -> ((Direction)entry.getKey()).getAxis().isHorizontal()).collect((Collector)Util.toMap());
    }
}
