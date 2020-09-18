package net.minecraft.world.level.block;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.Nameable;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnchantmentTableBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE;
    
    protected EnchantmentTableBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public boolean useShapeForLightOcclusion(final BlockState cee) {
        return true;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        return EnchantmentTableBlock.SHAPE;
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.MODEL;
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new EnchantmentTableBlockEntity();
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        bft.openMenu(cee.getMenuProvider(bru, fx));
        return InteractionResult.CONSUME;
    }
    
    @Nullable
    @Override
    public MenuProvider getMenuProvider(final BlockState cee, final Level bru, final BlockPos fx) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof EnchantmentTableBlockEntity) {
            final Component nr6 = ((Nameable)ccg5).getDisplayName();
            return new SimpleMenuProvider((integer, bfs, bft) -> new EnchantmentMenu(integer, bfs, ContainerLevelAccess.create(bru, fx)), nr6);
        }
        return null;
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof EnchantmentTableBlockEntity) {
                ((EnchantmentTableBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public boolean isPathfindable(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final PathComputationType cxb) {
        return false;
    }
    
    static {
        SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    }
}
