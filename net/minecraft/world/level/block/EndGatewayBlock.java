package net.minecraft.world.level.block;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class EndGatewayBlock extends BaseEntityBlock {
    protected EndGatewayBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new TheEndGatewayBlockEntity();
    }
    
    @Override
    public boolean canBeReplaced(final BlockState cee, final Fluid cut) {
        return false;
    }
}
