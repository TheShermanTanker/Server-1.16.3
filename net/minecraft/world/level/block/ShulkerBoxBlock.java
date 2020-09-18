package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.StateHolder;
import java.util.function.Consumer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.ShulkerSharedHelper;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import javax.annotation.Nullable;
import net.minecraft.world.item.DyeColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class ShulkerBoxBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING;
    public static final ResourceLocation CONTENTS;
    @Nullable
    private final DyeColor color;
    
    public ShulkerBoxBlock(@Nullable final DyeColor bku, final Properties c) {
        super(c);
        this.color = bku;
        this.registerDefaultState(((StateHolder<O, BlockState>)this.stateDefinition.any()).<Direction, Direction>setValue(ShulkerBoxBlock.FACING, Direction.UP));
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new ShulkerBoxBlockEntity(this.color);
    }
    
    @Override
    public RenderShape getRenderShape(final BlockState cee) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    
    @Override
    public InteractionResult use(final BlockState cee, final Level bru, final BlockPos fx, final Player bft, final InteractionHand aoq, final BlockHitResult dcg) {
        if (bru.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        if (bft.isSpectator()) {
            return InteractionResult.CONSUME;
        }
        final BlockEntity ccg8 = bru.getBlockEntity(fx);
        if (ccg8 instanceof ShulkerBoxBlockEntity) {
            final ShulkerBoxBlockEntity cdb9 = (ShulkerBoxBlockEntity)ccg8;
            boolean boolean10;
            if (cdb9.getAnimationStatus() == ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
                final Direction gc11 = cee.<Direction>getValue(ShulkerBoxBlock.FACING);
                boolean10 = bru.noCollision(ShulkerSharedHelper.openBoundingBox(fx, gc11));
            }
            else {
                boolean10 = true;
            }
            if (boolean10) {
                bft.openMenu(cdb9);
                bft.awardStat(Stats.OPEN_SHULKER_BOX);
                PiglinAi.angerNearbyPiglins(bft, true);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
    
    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext bnv) {
        return ((StateHolder<O, BlockState>)this.defaultBlockState()).<Direction, Direction>setValue(ShulkerBoxBlock.FACING, bnv.getClickedFace());
    }
    
    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> a) {
        a.add(ShulkerBoxBlock.FACING);
    }
    
    @Override
    public void playerWillDestroy(final Level bru, final BlockPos fx, final BlockState cee, final Player bft) {
        final BlockEntity ccg6 = bru.getBlockEntity(fx);
        if (ccg6 instanceof ShulkerBoxBlockEntity) {
            final ShulkerBoxBlockEntity cdb7 = (ShulkerBoxBlockEntity)ccg6;
            if (!bru.isClientSide && bft.isCreative() && !cdb7.isEmpty()) {
                final ItemStack bly8 = getColoredItemStack(this.getColor());
                final CompoundTag md9 = cdb7.saveToTag(new CompoundTag());
                if (!md9.isEmpty()) {
                    bly8.addTagElement("BlockEntityTag", (Tag)md9);
                }
                if (cdb7.hasCustomName()) {
                    bly8.setHoverName(cdb7.getCustomName());
                }
                final ItemEntity bcs10 = new ItemEntity(bru, fx.getX() + 0.5, fx.getY() + 0.5, fx.getZ() + 0.5, bly8);
                bcs10.setDefaultPickUpDelay();
                bru.addFreshEntity(bcs10);
            }
            else {
                cdb7.unpackLootTable(bft);
            }
        }
        super.playerWillDestroy(bru, fx, cee, bft);
    }
    
    @Override
    public List<ItemStack> getDrops(final BlockState cee, LootContext.Builder a) {
        final BlockEntity ccg4 = a.<BlockEntity>getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (ccg4 instanceof ShulkerBoxBlockEntity) {
            final ShulkerBoxBlockEntity cdb5 = (ShulkerBoxBlockEntity)ccg4;
            int integer4;
            final ShulkerBoxBlockEntity shulkerBoxBlockEntity;
            a = a.withDynamicDrop(ShulkerBoxBlock.CONTENTS, (cys, consumer) -> {
                for (integer4 = 0; integer4 < shulkerBoxBlockEntity.getContainerSize(); ++integer4) {
                    consumer.accept(shulkerBoxBlockEntity.getItem(integer4));
                }
                return;
            });
        }
        return super.getDrops(cee, a);
    }
    
    @Override
    public void setPlacedBy(final Level bru, final BlockPos fx, final BlockState cee, final LivingEntity aqj, final ItemStack bly) {
        if (bly.hasCustomHoverName()) {
            final BlockEntity ccg7 = bru.getBlockEntity(fx);
            if (ccg7 instanceof ShulkerBoxBlockEntity) {
                ((ShulkerBoxBlockEntity)ccg7).setCustomName(bly.getHoverName());
            }
        }
    }
    
    @Override
    public void onRemove(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (cee1.is(cee4.getBlock())) {
            return;
        }
        final BlockEntity ccg7 = bru.getBlockEntity(fx);
        if (ccg7 instanceof ShulkerBoxBlockEntity) {
            bru.updateNeighbourForOutputSignal(fx, cee1.getBlock());
        }
        super.onRemove(cee1, bru, fx, cee4, boolean5);
    }
    
    @Override
    public PushReaction getPistonPushReaction(final BlockState cee) {
        return PushReaction.DESTROY;
    }
    
    @Override
    public VoxelShape getShape(final BlockState cee, final BlockGetter bqz, final BlockPos fx, final CollisionContext dcp) {
        final BlockEntity ccg6 = bqz.getBlockEntity(fx);
        if (ccg6 instanceof ShulkerBoxBlockEntity) {
            return Shapes.create(((ShulkerBoxBlockEntity)ccg6).getBoundingBox(cee));
        }
        return Shapes.block();
    }
    
    @Override
    public boolean hasAnalogOutputSignal(final BlockState cee) {
        return true;
    }
    
    @Override
    public int getAnalogOutputSignal(final BlockState cee, final Level bru, final BlockPos fx) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)bru.getBlockEntity(fx));
    }
    
    public static Block getBlockByColor(@Nullable final DyeColor bku) {
        if (bku == null) {
            return Blocks.SHULKER_BOX;
        }
        switch (bku) {
            case WHITE: {
                return Blocks.WHITE_SHULKER_BOX;
            }
            case ORANGE: {
                return Blocks.ORANGE_SHULKER_BOX;
            }
            case MAGENTA: {
                return Blocks.MAGENTA_SHULKER_BOX;
            }
            case LIGHT_BLUE: {
                return Blocks.LIGHT_BLUE_SHULKER_BOX;
            }
            case YELLOW: {
                return Blocks.YELLOW_SHULKER_BOX;
            }
            case LIME: {
                return Blocks.LIME_SHULKER_BOX;
            }
            case PINK: {
                return Blocks.PINK_SHULKER_BOX;
            }
            case GRAY: {
                return Blocks.GRAY_SHULKER_BOX;
            }
            case LIGHT_GRAY: {
                return Blocks.LIGHT_GRAY_SHULKER_BOX;
            }
            case CYAN: {
                return Blocks.CYAN_SHULKER_BOX;
            }
            default: {
                return Blocks.PURPLE_SHULKER_BOX;
            }
            case BLUE: {
                return Blocks.BLUE_SHULKER_BOX;
            }
            case BROWN: {
                return Blocks.BROWN_SHULKER_BOX;
            }
            case GREEN: {
                return Blocks.GREEN_SHULKER_BOX;
            }
            case RED: {
                return Blocks.RED_SHULKER_BOX;
            }
            case BLACK: {
                return Blocks.BLACK_SHULKER_BOX;
            }
        }
    }
    
    @Nullable
    public DyeColor getColor() {
        return this.color;
    }
    
    public static ItemStack getColoredItemStack(@Nullable final DyeColor bku) {
        return new ItemStack(getBlockByColor(bku));
    }
    
    @Override
    public BlockState rotate(final BlockState cee, final Rotation bzj) {
        return ((StateHolder<O, BlockState>)cee).<Direction, Direction>setValue(ShulkerBoxBlock.FACING, bzj.rotate(cee.<Direction>getValue(ShulkerBoxBlock.FACING)));
    }
    
    @Override
    public BlockState mirror(final BlockState cee, final Mirror byd) {
        return cee.rotate(byd.getRotation(cee.<Direction>getValue(ShulkerBoxBlock.FACING)));
    }
    
    static {
        FACING = DirectionalBlock.FACING;
        CONTENTS = new ResourceLocation("contents");
    }
}
