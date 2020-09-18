package net.minecraft.world.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.entity.EntityType;

public class FishBucketItem extends BucketItem {
    private final EntityType<?> type;
    
    public FishBucketItem(final EntityType<?> aqb, final Fluid cut, final Properties a) {
        super(cut, a);
        this.type = aqb;
    }
    
    @Override
    public void checkExtraContent(final Level bru, final ItemStack bly, final BlockPos fx) {
        if (bru instanceof ServerLevel) {
            this.spawn((ServerLevel)bru, bly, fx);
        }
    }
    
    @Override
    protected void playEmptySound(@Nullable final Player bft, final LevelAccessor brv, final BlockPos fx) {
        brv.playSound(bft, fx, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0f, 1.0f);
    }
    
    private void spawn(final ServerLevel aag, final ItemStack bly, final BlockPos fx) {
        final Entity apx5 = this.type.spawn(aag, bly, null, fx, MobSpawnType.BUCKET, true, false);
        if (apx5 != null) {
            ((AbstractFish)apx5).setFromBucket(true);
        }
    }
}
