package net.minecraft.world.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class LingeringPotionItem extends ThrowablePotionItem {
    public LingeringPotionItem(final Properties a) {
        super(a);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        bru.playSound(null, bft.getX(), bft.getY(), bft.getZ(), SoundEvents.LINGERING_POTION_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (LingeringPotionItem.random.nextFloat() * 0.4f + 0.8f));
        return super.use(bru, bft, aoq);
    }
}
