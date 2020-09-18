package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import java.util.Random;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class NetherPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS;
    protected static final VoxelShape X_AXIS_AABB;
    protected static final VoxelShape Z_AXIS_AABB;
    
    public NetherPortalBlock(final Properties c) {
        super(c);
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Direction.Axis, Direction.Axis>setValue(NetherPortalBlock.AXIS, Direction.Axis.X));
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        switch (cee.<Direction.Axis>getValue(NetherPortalBlock.AXIS)) {
            case Z: {
                return NetherPortalBlock.Z_AXIS_AABB;
            }
            default: {
                return NetherPortalBlock.X_AXIS_AABB;
            }
        }
    }
    
    @Override
    public void randomTick(final BlockState cee, final ServerLevel aag, BlockPos fx, final Random random) {
        if (aag.dimensionType().natural() && aag.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && random.nextInt(2000) < aag.getDifficulty().getId()) {
            while (aag.getBlockState(fx).is(this)) {
                fx = fx.below();
            }
            if (aag.getBlockState(fx).isValidSpawn(aag, fx, EntityType.ZOMBIFIED_PIGLIN)) {
                final Entity apx6 = EntityType.ZOMBIFIED_PIGLIN.spawn(aag, null, null, null, fx.above(), MobSpawnType.STRUCTURE, false, false);
                if (apx6 != null) {
                    apx6.setPortalCooldown();
                }
            }
        }
    }
    
    @Override
    public BlockState updateShape(final BlockState cee1, final Direction gc, final BlockState cee3, final LevelAccessor brv, final BlockPos fx5, final BlockPos fx6) {
        final Direction.Axis a8 = gc.getAxis();
        final Direction.Axis a9 = cee1.<Direction.Axis>getValue(NetherPortalBlock.AXIS);
        final boolean boolean10 = a9 != a8 && a8.isHorizontal();
        if (boolean10 || cee3.is(this) || new PortalShape(brv, fx5, a9).isComplete()) {
            return super.updateShape(cee1, gc, cee3, brv, fx5, fx6);
        }
        return Blocks.AIR.defaultBlockState();
    }
    
    @Override
    public void entityInside(final BlockState cee, final Level bru, final BlockPos fx, final Entity apx) {
        if (!apx.isPassenger() && !apx.isVehicle() && apx.canChangeDimensions()) {
            apx.handleInsidePortal(fx);
        }
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        switch (bzj) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90: {
                switch (cee.<Direction.Axis>getValue(NetherPortalBlock.AXIS)) {
                    case X: {
                        return ((StateHolder<O, BlockState>)cee).<Direction.Axis, Direction.Axis>setValue(NetherPortalBlock.AXIS, Direction.Axis.Z);
                    }
                    case Z: {
                        return ((StateHolder<O, BlockState>)cee).<Direction.Axis, Direction.Axis>setValue(NetherPortalBlock.AXIS, Direction.Axis.X);
                    }
                    default: {
                        return cee;
                    }
                }
                break;
            }
            default: {
                return cee;
            }
        }
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(NetherPortalBlock.AXIS);
    }
    
    static {
        AXIS = BlockStateProperties.HORIZONTAL_AXIS;
        X_AXIS_AABB = Block.box(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
        Z_AXIS_AABB = Block.box(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);
    }
}
