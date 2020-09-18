package net.minecraft.world.item;

import java.util.Iterator;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.core.Registry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class TippedArrowItem extends ArrowItem {
    public TippedArrowItem(final Properties a) {
        super(a);
    }
    
    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.POISON);
    }
    
    @Override
    public void fillItemCategory(final CreativeModeTab bkp, final NonNullList<ItemStack> gj) {
        if (this.allowdedIn(bkp)) {
            for (final Potion bnq5 : Registry.POTION) {
                if (!bnq5.getEffects().isEmpty()) {
                    gj.add(PotionUtils.setPotion(new ItemStack(this), bnq5));
                }
            }
        }
    }
    
    @Override
    public String getDescriptionId(final ItemStack bly) {
        return PotionUtils.getPotion(bly).getName(this.getDescriptionId() + ".effect.");
    }
}
