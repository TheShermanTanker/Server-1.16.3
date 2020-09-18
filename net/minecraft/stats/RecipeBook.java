package net.minecraft.stats;

import net.minecraft.world.inventory.RecipeBookType;
import javax.annotation.Nullable;
import net.minecraft.world.item.crafting.Recipe;
import java.util.Collection;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;

public class RecipeBook {
    protected final Set<ResourceLocation> known;
    protected final Set<ResourceLocation> highlight;
    private final RecipeBookSettings bookSettings;
    
    public RecipeBook() {
        this.known = Sets.newHashSet();
        this.highlight = Sets.newHashSet();
        this.bookSettings = new RecipeBookSettings();
    }
    
    public void copyOverData(final RecipeBook adr) {
        this.known.clear();
        this.highlight.clear();
        this.bookSettings.replaceFrom(adr.bookSettings);
        this.known.addAll((Collection)adr.known);
        this.highlight.addAll((Collection)adr.highlight);
    }
    
    public void add(final Recipe<?> bon) {
        if (!bon.isSpecial()) {
            this.add(bon.getId());
        }
    }
    
    protected void add(final ResourceLocation vk) {
        this.known.add(vk);
    }
    
    public boolean contains(@Nullable final Recipe<?> bon) {
        return bon != null && this.known.contains(bon.getId());
    }
    
    public boolean contains(final ResourceLocation vk) {
        return this.known.contains(vk);
    }
    
    protected void remove(final ResourceLocation vk) {
        this.known.remove(vk);
        this.highlight.remove(vk);
    }
    
    public void removeHighlight(final Recipe<?> bon) {
        this.highlight.remove(bon.getId());
    }
    
    public void addHighlight(final Recipe<?> bon) {
        this.addHighlight(bon.getId());
    }
    
    protected void addHighlight(final ResourceLocation vk) {
        this.highlight.add(vk);
    }
    
    public void setBookSettings(final RecipeBookSettings ads) {
        this.bookSettings.replaceFrom(ads);
    }
    
    public RecipeBookSettings getBookSettings() {
        return this.bookSettings.copy();
    }
    
    public void setBookSetting(final RecipeBookType bjh, final boolean boolean2, final boolean boolean3) {
        this.bookSettings.setOpen(bjh, boolean2);
        this.bookSettings.setFiltering(bjh, boolean3);
    }
}
