package net.minecraft.world.item;

import net.minecraft.world.level.block.entity.BannerPattern;

public class BannerPatternItem extends Item {
    private final BannerPattern bannerPattern;
    
    public BannerPatternItem(final BannerPattern cby, final Properties a) {
        super(a);
        this.bannerPattern = cby;
    }
    
    public BannerPattern getBannerPattern() {
        return this.bannerPattern;
    }
}
