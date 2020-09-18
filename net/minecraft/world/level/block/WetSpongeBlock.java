package net.minecraft.world.level.block;

import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WetSpongeBlock extends Block {
    protected WetSpongeBlock(final Properties c) {
        super(c);
    }
    
    @Override
    public void onPlace(final BlockState cee1, final Level bru, final BlockPos fx, final BlockState cee4, final boolean boolean5) {
        if (bru.dimensionType().ultraWarm()) {
            bru.setBlock(fx, Blocks.SPONGE.defaultBlockState(), 3);
            bru.levelEvent(2009, fx, 0);
            bru.playSound(null, fx, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0f, (1.0f + bru.getRandom().nextFloat() * 0.2f) * 0.7f);
        }
    }
}
