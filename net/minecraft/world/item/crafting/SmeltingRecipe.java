package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class SmeltingRecipe extends AbstractCookingRecipe {
    public SmeltingRecipe(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly, final float float5, final int integer) {
        super(RecipeType.SMELTING, vk, string, bok, bly, float5, integer);
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMELTING_RECIPE;
    }
}
