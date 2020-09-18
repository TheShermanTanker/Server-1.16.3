package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.core.Direction;
import java.util.Map;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class HugeMushroomBlock extends Block {
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
    
    public HugeMushroomBlock(final Properties c) {
        super(c);
        this.registerDefaultState((((((((StateHolder<O, BlockState>)this.stateDefinition.any()).setValue((Property<Comparable>)HugeMushroomBlock.NORTH, true)).setValue((Property<Comparable>)HugeMushroomBlock.EAST, true)).setValue((Property<Comparable>)HugeMushroomBlock.SOUTH, true)).setValue((Property<Comparable>)HugeMushroomBlock.WEST, true)).setValue((Property<Comparable>)HugeMushroomBlock.UP, true)).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.DOWN, true));
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        final BlockGetter bqz3 = bnv.getLevel();
        final BlockPos fx4 = bnv.getClickedPos();
        return (((((((StateHolder<O, BlockState>)this.defaultBlockState()).setValue((Property<Comparable>)HugeMushroomBlock.DOWN, this != bqz3.getBlockState(fx4.below()).getBlock())).setValue((Property<Comparable>)HugeMushroomBlock.UP, this != bqz3.getBlockState(fx4.above()).getBlock())).setValue((Property<Comparable>)HugeMushroomBlock.NORTH, this != bqz3.getBlockState(fx4.north()).getBlock())).setValue((Property<Comparable>)HugeMushroomBlock.EAST, this != bqz3.getBlockState(fx4.east()).getBlock())).setValue((Property<Comparable>)HugeMushroomBlock.SOUTH, this != bqz3.getBlockState(fx4.south()).getBlock())).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.WEST, this != bqz3.getBlockState(fx4.west()).getBlock());
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        if (cee3.is(this)) {
            return ((StateHolder<O, BlockState>)cee1).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(gc), false);
        }
        return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return (((((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(bzj.rotate(Direction.NORTH)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.NORTH))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(bzj.rotate(Direction.SOUTH)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.SOUTH))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(bzj.rotate(Direction.EAST)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.EAST))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(bzj.rotate(Direction.WEST)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.WEST))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(bzj.rotate(Direction.UP)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.UP))).<Comparable, Comparable>setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(bzj.rotate(Direction.DOWN)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.DOWN));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return (((((((StateHolder<O, BlockState>)cee).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(byd.mirror(Direction.NORTH)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.NORTH))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(byd.mirror(Direction.SOUTH)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.SOUTH))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(byd.mirror(Direction.EAST)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.EAST))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(byd.mirror(Direction.WEST)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.WEST))).setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(byd.mirror(Direction.UP)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.UP))).<Comparable, Comparable>setValue((Property<Comparable>)HugeMushroomBlock.PROPERTY_BY_DIRECTION.get(byd.mirror(Direction.DOWN)), (Comparable)cee.<V>getValue((Property<V>)HugeMushroomBlock.DOWN));
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(HugeMushroomBlock.UP, HugeMushroomBlock.DOWN, HugeMushroomBlock.NORTH, HugeMushroomBlock.EAST, HugeMushroomBlock.SOUTH, HugeMushroomBlock.WEST);
    }
    
    static {
        NORTH = PipeBlock.NORTH;
        EAST = PipeBlock.EAST;
        SOUTH = PipeBlock.SOUTH;
        WEST = PipeBlock.WEST;
        UP = PipeBlock.UP;
        DOWN = PipeBlock.DOWN;
        PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
    }
}
