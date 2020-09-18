package net.minecraft.world.item;

public class HorseArmorItem extends Item {
    private final int protection;
    private final String texture;
    
    public HorseArmorItem(final int integer, final String string, final Properties a) {
        super(a);
        this.protection = integer;
        this.texture = "textures/entity/horse/armor/horse_armor_" + string + ".png";
    }
    
    public int getProtection() {
        return this.protection;
    }
}
