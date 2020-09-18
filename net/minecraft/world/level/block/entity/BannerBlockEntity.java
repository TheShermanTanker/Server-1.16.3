package net.minecraft.world.level.block.entity;

import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.DyeColor;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;

public class BannerBlockEntity extends BlockEntity implements Nameable {
    @Nullable
    private Component name;
    @Nullable
    private DyeColor baseColor;
    @Nullable
    private ListTag itemPatterns;
    private boolean receivedData;
    @Nullable
    private List<Pair<BannerPattern, DyeColor>> patterns;
    
    public BannerBlockEntity() {
        super(BlockEntityType.BANNER);
        this.baseColor = DyeColor.WHITE;
    }
    
    public BannerBlockEntity(final DyeColor bku) {
        this();
        this.baseColor = bku;
    }
    
    @Override
    public Component getName() {
        if (this.name != null) {
            return this.name;
        }
        return new TranslatableComponent("block.minecraft.banner");
    }
    
    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }
    
    public void setCustomName(final Component nr) {
        this.name = nr;
    }
    
    @Override
    public CompoundTag save(final CompoundTag md) {
        super.save(md);
        if (this.itemPatterns != null) {
            md.put("Patterns", (Tag)this.itemPatterns);
        }
        if (this.name != null) {
            md.putString("CustomName", Component.Serializer.toJson(this.name));
        }
        return md;
    }
    
    @Override
    public void load(final BlockState cee, final CompoundTag md) {
        super.load(cee, md);
        if (md.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(md.getString("CustomName"));
        }
        if (this.hasLevel()) {
            this.baseColor = ((AbstractBannerBlock)this.getBlockState().getBlock()).getColor();
        }
        else {
            this.baseColor = null;
        }
        this.itemPatterns = md.getList("Patterns", 10);
        this.patterns = null;
        this.receivedData = true;
    }
    
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 6, this.getUpdateTag());
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }
    
    public static int getPatternCount(final ItemStack bly) {
        final CompoundTag md2 = bly.getTagElement("BlockEntityTag");
        if (md2 != null && md2.contains("Patterns")) {
            return md2.getList("Patterns", 10).size();
        }
        return 0;
    }
    
    public static void removeLastPattern(final ItemStack bly) {
        final CompoundTag md2 = bly.getTagElement("BlockEntityTag");
        if (md2 == null || !md2.contains("Patterns", 9)) {
            return;
        }
        final ListTag mj3 = md2.getList("Patterns", 10);
        if (mj3.isEmpty()) {
            return;
        }
        mj3.remove(mj3.size() - 1);
        if (mj3.isEmpty()) {
            bly.removeTagKey("BlockEntityTag");
        }
    }
    
    public DyeColor getBaseColor(final Supplier<BlockState> supplier) {
        if (this.baseColor == null) {
            this.baseColor = ((AbstractBannerBlock)((BlockState)supplier.get()).getBlock()).getColor();
        }
        return this.baseColor;
    }
}
