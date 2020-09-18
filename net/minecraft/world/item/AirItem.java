package net.minecraft.world.item;

import net.minecraft.world.level.block.Block;

public class AirItem extends Item {
    private final Block block;
    
    public AirItem(final Block bul, final Properties a) {
        super(a);
        this.block = bul;
    }
    
    @Override
    public String getDescriptionId() {
        return this.block.getDescriptionId();
    }
}
