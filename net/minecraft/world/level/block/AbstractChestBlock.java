package net.minecraft.world.level.block;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.function.Supplier;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class AbstractChestBlock<E extends BlockEntity> extends BaseEntityBlock {
    protected final Supplier<BlockEntityType<? extends E>> blockEntityType;
    
    protected AbstractChestBlock(final Properties c, final Supplier<BlockEntityType<? extends E>> supplier) {
        super(c);
        this.blockEntityType = supplier;
    }
}
