package net.minecraft.world.level.block;

import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class FurnaceBlock extends AbstractFurnaceBlock {
    protected FurnaceBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public BlockEntity newBlockEntity(final BlockGetter bqz) {
        return new FurnaceBlockEntity();
    }
    
    @Override
    protected void openContainer(final Level bru, final BlockPos fx, final Player bft) {
        final BlockEntity ccg5 = bru.getBlockEntity(fx);
        if (ccg5 instanceof FurnaceBlockEntity) {
            bft.openMenu((MenuProvider)ccg5);
            bft.awardStat(Stats.INTERACT_WITH_FURNACE);
        }
    }
}
