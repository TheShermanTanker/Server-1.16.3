package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;

public class SmokingRecipe extends AbstractCookingRecipe {
    public SmokingRecipe(final ResourceLocation vk, final String string, final Ingredient bok, final ItemStack bly, final float float5, final int integer) {
        super(RecipeType.SMOKING, vk, string, bok, bly, float5, integer);
    }
    
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SMOKING_RECIPE;
    }
}
