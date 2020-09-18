package net.minecraft.world.item.crafting;

import net.minecraft.world.Container;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import com.google.common.collect.Maps;
import net.minecraft.world.level.ItemLike;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import com.google.common.collect.Lists;
import net.minecraft.world.level.Level;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.resources.ResourceLocation;

public class RepairItemRecipe extends CustomRecipe {
    public RepairItemRecipe(final ResourceLocation vk) {
        super(vk);
    }
    
    public boolean matches(final CraftingContainer bil, final Level bru) {
        final List<ItemStack> list4 = Lists.newArrayList();
        for (int integer5 = 0; integer5 < bil.getContainerSize(); ++integer5) {
            final ItemStack bly6 = bil.getItem(integer5);
            if (!bly6.isEmpty()) {
                list4.add(bly6);
                if (list4.size() > 1) {
                    final ItemStack bly7 = (ItemStack)list4.get(0);
                    if (bly6.getItem() != bly7.getItem() || bly7.getCount() != 1 || bly6.getCount() != 1 || !bly7.getItem().canBeDepleted()) {
                        return false;
                    }
                }
            }
        }
        return list4.size() == 2;
    }
    
    public ItemStack assemble(final CraftingContainer bil) {
        final List<ItemStack> list3 = Lists.newArrayList();
        for (int integer4 = 0; integer4 < bil.getContainerSize(); ++integer4) {
            final ItemStack bly5 = bil.getItem(integer4);
            if (!bly5.isEmpty()) {
                list3.add(bly5);
                if (list3.size() > 1) {
                    final ItemStack bly6 = (ItemStack)list3.get(0);
                    if (bly5.getItem() != bly6.getItem() || bly6.getCount() != 1 || bly5.getCount() != 1 || !bly6.getItem().canBeDepleted()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        if (list3.size() == 2) {
            final ItemStack bly7 = (ItemStack)list3.get(0);
            final ItemStack bly5 = (ItemStack)list3.get(1);
            if (bly7.getItem() == bly5.getItem() && bly7.getCount() == 1 && bly5.getCount() == 1 && bly7.getItem().canBeDepleted()) {
                final Item blu6 = bly7.getItem();
                final int integer5 = blu6.getMaxDamage() - bly7.getDamageValue();
                final int integer6 = blu6.getMaxDamage() - bly5.getDamageValue();
                final int integer7 = integer5 + integer6 + blu6.getMaxDamage() * 5 / 100;
                int integer8 = blu6.getMaxDamage() - integer7;
                if (integer8 < 0) {
                    integer8 = 0;
                }
                final ItemStack bly8 = new ItemStack(bly7.getItem());
                bly8.setDamageValue(integer8);
                final Map<Enchantment, Integer> map12 = Maps.newHashMap();
                final Map<Enchantment, Integer> map13 = EnchantmentHelper.getEnchantments(bly7);
                final Map<Enchantment, Integer> map14 = EnchantmentHelper.getEnchantments(bly5);
                Registry.ENCHANTMENT.stream().filter(Enchantment::isCurse).forEach(bpp -> {
                    final int integer5 = Math.max((int)map13.getOrDefault(bpp, 0), (int)map14.getOrDefault(bpp, 0));
                    if (integer5 > 0) {
                        map12.put(bpp, integer5);
                    }
                });
                if (!map12.isEmpty()) {
                    EnchantmentHelper.setEnchantments(map12, bly8);
                }
                return bly8;
            }
        }
        return ItemStack.EMPTY;
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.REPAIR_ITEM;
    }
}
