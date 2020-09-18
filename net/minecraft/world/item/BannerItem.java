package net.minecraft.world.item;

import org.apache.commons.lang3.Validate;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.Block;

public class BannerItem extends StandingAndWallBlockItem {
    public BannerItem(final Block bul1, final Block bul2, final Properties a) {
        super(bul1, bul2, a);
        Validate.isInstanceOf(AbstractBannerBlock.class, bul1);
        Validate.isInstanceOf(AbstractBannerBlock.class, bul2);
    }
    
    public DyeColor getColor() {
        return ((AbstractBannerBlock)this.getBlock()).getColor();
    }
}
