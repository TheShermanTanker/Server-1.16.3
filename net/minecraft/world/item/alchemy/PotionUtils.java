package net.minecraft.world.item.alchemy;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Iterator;
import net.minecraft.nbt.ListTag;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.world.effect.MobEffectInstance;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.MutableComponent;

public class PotionUtils {
    private static final MutableComponent NO_EFFECT;
    
    public static List<MobEffectInstance> getMobEffects(final ItemStack bly) {
        return getAllEffects(bly.getTag());
    }
    
    public static List<MobEffectInstance> getAllEffects(final Potion bnq, final Collection<MobEffectInstance> collection) {
        final List<MobEffectInstance> list3 = Lists.newArrayList();
        list3.addAll((Collection)bnq.getEffects());
        list3.addAll((Collection)collection);
        return list3;
    }
    
    public static List<MobEffectInstance> getAllEffects(@Nullable final CompoundTag md) {
        final List<MobEffectInstance> list2 = Lists.newArrayList();
        list2.addAll((Collection)getPotion(md).getEffects());
        getCustomEffects(md, list2);
        return list2;
    }
    
    public static List<MobEffectInstance> getCustomEffects(final ItemStack bly) {
        return getCustomEffects(bly.getTag());
    }
    
    public static List<MobEffectInstance> getCustomEffects(@Nullable final CompoundTag md) {
        final List<MobEffectInstance> list2 = Lists.newArrayList();
        getCustomEffects(md, list2);
        return list2;
    }
    
    public static void getCustomEffects(@Nullable final CompoundTag md, final List<MobEffectInstance> list) {
        if (md != null && md.contains("CustomPotionEffects", 9)) {
            final ListTag mj3 = md.getList("CustomPotionEffects", 10);
            for (int integer4 = 0; integer4 < mj3.size(); ++integer4) {
                final CompoundTag md2 = mj3.getCompound(integer4);
                final MobEffectInstance apr6 = MobEffectInstance.load(md2);
                if (apr6 != null) {
                    list.add(apr6);
                }
            }
        }
    }
    
    public static int getColor(final ItemStack bly) {
        final CompoundTag md2 = bly.getTag();
        if (md2 != null && md2.contains("CustomPotionColor", 99)) {
            return md2.getInt("CustomPotionColor");
        }
        return (getPotion(bly) == Potions.EMPTY) ? 16253176 : getColor((Collection<MobEffectInstance>)getMobEffects(bly));
    }
    
    public static int getColor(final Potion bnq) {
        return (bnq == Potions.EMPTY) ? 16253176 : getColor((Collection<MobEffectInstance>)bnq.getEffects());
    }
    
    public static int getColor(final Collection<MobEffectInstance> collection) {
        final int integer2 = 3694022;
        if (collection.isEmpty()) {
            return 3694022;
        }
        float float3 = 0.0f;
        float float4 = 0.0f;
        float float5 = 0.0f;
        int integer3 = 0;
        for (final MobEffectInstance apr8 : collection) {
            if (!apr8.isVisible()) {
                continue;
            }
            final int integer4 = apr8.getEffect().getColor();
            final int integer5 = apr8.getAmplifier() + 1;
            float3 += integer5 * (integer4 >> 16 & 0xFF) / 255.0f;
            float4 += integer5 * (integer4 >> 8 & 0xFF) / 255.0f;
            float5 += integer5 * (integer4 >> 0 & 0xFF) / 255.0f;
            integer3 += integer5;
        }
        if (integer3 == 0) {
            return 0;
        }
        float3 = float3 / integer3 * 255.0f;
        float4 = float4 / integer3 * 255.0f;
        float5 = float5 / integer3 * 255.0f;
        return (int)float3 << 16 | (int)float4 << 8 | (int)float5;
    }
    
    public static Potion getPotion(final ItemStack bly) {
        return getPotion(bly.getTag());
    }
    
    public static Potion getPotion(@Nullable final CompoundTag md) {
        if (md == null) {
            return Potions.EMPTY;
        }
        return Potion.byName(md.getString("Potion"));
    }
    
    public static ItemStack setPotion(final ItemStack bly, final Potion bnq) {
        final ResourceLocation vk3 = Registry.POTION.getKey(bnq);
        if (bnq == Potions.EMPTY) {
            bly.removeTagKey("Potion");
        }
        else {
            bly.getOrCreateTag().putString("Potion", vk3.toString());
        }
        return bly;
    }
    
    public static ItemStack setCustomEffects(final ItemStack bly, final Collection<MobEffectInstance> collection) {
        if (collection.isEmpty()) {
            return bly;
        }
        final CompoundTag md3 = bly.getOrCreateTag();
        final ListTag mj4 = md3.getList("CustomPotionEffects", 9);
        for (final MobEffectInstance apr6 : collection) {
            mj4.add(apr6.save(new CompoundTag()));
        }
        md3.put("CustomPotionEffects", (Tag)mj4);
        return bly;
    }
    
    static {
        NO_EFFECT = new TranslatableComponent("effect.none").withStyle(ChatFormatting.GRAY);
    }
}
