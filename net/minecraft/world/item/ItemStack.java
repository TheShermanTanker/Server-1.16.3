package net.minecraft.world.item;

import net.minecraft.Util;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.kinds.Applicative;
import java.util.function.Function;
import com.mojang.datafixers.kinds.App;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.sounds.SoundEvent;
import java.util.function.Predicate;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.tags.TagContainer;
import java.util.Objects;
import net.minecraft.network.chat.MutableComponent;
import java.util.function.UnaryOperator;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import com.google.common.collect.HashMultimap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.enchantment.Enchantment;
import com.google.gson.JsonParseException;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.ListTag;
import java.util.function.Consumer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.item.enchantment.DigDurabilityEnchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import javax.annotation.Nullable;
import net.minecraft.server.level.ServerPlayer;
import java.util.Random;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Optional;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Style;
import java.text.DecimalFormat;
import org.apache.logging.log4j.Logger;
import com.mojang.serialization.Codec;

public final class ItemStack {
    public static final Codec<ItemStack> CODEC;
    private static final Logger LOGGER;
    public static final ItemStack EMPTY;
    public static final DecimalFormat ATTRIBUTE_MODIFIER_FORMAT;
    private static final Style LORE_STYLE;
    private int count;
    private int popTime;
    @Deprecated
    private final Item item;
    private CompoundTag tag;
    private boolean emptyCacheFlag;
    private Entity entityRepresentation;
    private BlockInWorld cachedBreakBlock;
    private boolean cachedBreakBlockResult;
    private BlockInWorld cachedPlaceBlock;
    private boolean cachedPlaceBlockResult;
    
    public ItemStack(final ItemLike brt) {
        this(brt, 1);
    }
    
    private ItemStack(final ItemLike brt, final int integer, final Optional<CompoundTag> optional) {
        this(brt, integer);
        optional.ifPresent(this::setTag);
    }
    
    public ItemStack(final ItemLike brt, final int integer) {
        this.item = ((brt == null) ? null : brt.asItem());
        this.count = integer;
        if (this.item != null && this.item.canBeDepleted()) {
            this.setDamageValue(this.getDamageValue());
        }
        this.updateEmptyCacheFlag();
    }
    
    private void updateEmptyCacheFlag() {
        this.emptyCacheFlag = false;
        this.emptyCacheFlag = this.isEmpty();
    }
    
    private ItemStack(final CompoundTag md) {
        this.item = Registry.ITEM.get(new ResourceLocation(md.getString("id")));
        this.count = md.getByte("Count");
        if (md.contains("tag", 10)) {
            this.tag = md.getCompound("tag");
            this.getItem().verifyTagAfterLoad(md);
        }
        if (this.getItem().canBeDepleted()) {
            this.setDamageValue(this.getDamageValue());
        }
        this.updateEmptyCacheFlag();
    }
    
    public static ItemStack of(final CompoundTag md) {
        try {
            return new ItemStack(md);
        }
        catch (RuntimeException runtimeException2) {
            ItemStack.LOGGER.debug("Tried to load invalid item: {}", md, runtimeException2);
            return ItemStack.EMPTY;
        }
    }
    
    public boolean isEmpty() {
        return this == ItemStack.EMPTY || (this.getItem() == null || this.getItem() == Items.AIR) || this.count <= 0;
    }
    
    public ItemStack split(final int integer) {
        final int integer2 = Math.min(integer, this.count);
        final ItemStack bly4 = this.copy();
        bly4.setCount(integer2);
        this.shrink(integer2);
        return bly4;
    }
    
    public Item getItem() {
        return this.emptyCacheFlag ? Items.AIR : this.item;
    }
    
    public InteractionResult useOn(final UseOnContext bnx) {
        final Player bft3 = bnx.getPlayer();
        final BlockPos fx4 = bnx.getClickedPos();
        final BlockInWorld cei5 = new BlockInWorld(bnx.getLevel(), fx4, false);
        if (bft3 != null && !bft3.abilities.mayBuild && !this.hasAdventureModePlaceTagForBlock(bnx.getLevel().getTagManager(), cei5)) {
            return InteractionResult.PASS;
        }
        final Item blu6 = this.getItem();
        final InteractionResult aor7 = blu6.useOn(bnx);
        if (bft3 != null && aor7.consumesAction()) {
            bft3.awardStat(Stats.ITEM_USED.get(blu6));
        }
        return aor7;
    }
    
    public float getDestroySpeed(final BlockState cee) {
        return this.getItem().getDestroySpeed(this, cee);
    }
    
    public InteractionResultHolder<ItemStack> use(final Level bru, final Player bft, final InteractionHand aoq) {
        return this.getItem().use(bru, bft, aoq);
    }
    
    public ItemStack finishUsingItem(final Level bru, final LivingEntity aqj) {
        return this.getItem().finishUsingItem(this, bru, aqj);
    }
    
    public CompoundTag save(final CompoundTag md) {
        final ResourceLocation vk3 = Registry.ITEM.getKey(this.getItem());
        md.putString("id", (vk3 == null) ? "minecraft:air" : vk3.toString());
        md.putByte("Count", (byte)this.count);
        if (this.tag != null) {
            md.put("tag", (Tag)this.tag.copy());
        }
        return md;
    }
    
    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }
    
    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
    }
    
    public boolean isDamageableItem() {
        if (this.emptyCacheFlag || this.getItem().getMaxDamage() <= 0) {
            return false;
        }
        final CompoundTag md2 = this.getTag();
        return md2 == null || !md2.getBoolean("Unbreakable");
    }
    
    public boolean isDamaged() {
        return this.isDamageableItem() && this.getDamageValue() > 0;
    }
    
    public int getDamageValue() {
        return (this.tag == null) ? 0 : this.tag.getInt("Damage");
    }
    
    public void setDamageValue(final int integer) {
        this.getOrCreateTag().putInt("Damage", Math.max(0, integer));
    }
    
    public int getMaxDamage() {
        return this.getItem().getMaxDamage();
    }
    
    public boolean hurt(int integer, final Random random, @Nullable final ServerPlayer aah) {
        if (!this.isDamageableItem()) {
            return false;
        }
        if (integer > 0) {
            final int integer2 = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, this);
            int integer3 = 0;
            for (int integer4 = 0; integer2 > 0 && integer4 < integer; ++integer4) {
                if (DigDurabilityEnchantment.shouldIgnoreDurabilityDrop(this, integer2, random)) {
                    ++integer3;
                }
            }
            integer -= integer3;
            if (integer <= 0) {
                return false;
            }
        }
        if (aah != null && integer != 0) {
            CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(aah, this, this.getDamageValue() + integer);
        }
        final int integer2 = this.getDamageValue() + integer;
        this.setDamageValue(integer2);
        return integer2 >= this.getMaxDamage();
    }
    
    public <T extends LivingEntity> void hurtAndBreak(final int integer, final T aqj, final Consumer<T> consumer) {
        if (aqj.level.isClientSide || (aqj instanceof Player && ((Player)aqj).abilities.instabuild)) {
            return;
        }
        if (!this.isDamageableItem()) {
            return;
        }
        if (this.hurt(integer, aqj.getRandom(), (aqj instanceof ServerPlayer) ? aqj : null)) {
            consumer.accept(aqj);
            final Item blu5 = this.getItem();
            this.shrink(1);
            if (aqj instanceof Player) {
                ((Player)aqj).awardStat(Stats.ITEM_BROKEN.get(blu5));
            }
            this.setDamageValue(0);
        }
    }
    
    public void hurtEnemy(final LivingEntity aqj, final Player bft) {
        final Item blu4 = this.getItem();
        if (blu4.hurtEnemy(this, aqj, bft)) {
            bft.awardStat(Stats.ITEM_USED.get(blu4));
        }
    }
    
    public void mineBlock(final Level bru, final BlockState cee, final BlockPos fx, final Player bft) {
        final Item blu6 = this.getItem();
        if (blu6.mineBlock(this, bru, cee, fx, bft)) {
            bft.awardStat(Stats.ITEM_USED.get(blu6));
        }
    }
    
    public boolean isCorrectToolForDrops(final BlockState cee) {
        return this.getItem().isCorrectToolForDrops(cee);
    }
    
    public InteractionResult interactLivingEntity(final Player bft, final LivingEntity aqj, final InteractionHand aoq) {
        return this.getItem().interactLivingEntity(this, bft, aqj, aoq);
    }
    
    public ItemStack copy() {
        if (this.isEmpty()) {
            return ItemStack.EMPTY;
        }
        final ItemStack bly2 = new ItemStack(this.getItem(), this.count);
        bly2.setPopTime(this.getPopTime());
        if (this.tag != null) {
            bly2.tag = this.tag.copy();
        }
        return bly2;
    }
    
    public static boolean tagMatches(final ItemStack bly1, final ItemStack bly2) {
        return (bly1.isEmpty() && bly2.isEmpty()) || (!bly1.isEmpty() && !bly2.isEmpty() && (bly1.tag != null || bly2.tag == null) && (bly1.tag == null || bly1.tag.equals(bly2.tag)));
    }
    
    public static boolean matches(final ItemStack bly1, final ItemStack bly2) {
        return (bly1.isEmpty() && bly2.isEmpty()) || (!bly1.isEmpty() && !bly2.isEmpty() && bly1.matches(bly2));
    }
    
    private boolean matches(final ItemStack bly) {
        return this.count == bly.count && this.getItem() == bly.getItem() && (this.tag != null || bly.tag == null) && (this.tag == null || this.tag.equals(bly.tag));
    }
    
    public static boolean isSame(final ItemStack bly1, final ItemStack bly2) {
        return bly1 == bly2 || (!bly1.isEmpty() && !bly2.isEmpty() && bly1.sameItem(bly2));
    }
    
    public static boolean isSameIgnoreDurability(final ItemStack bly1, final ItemStack bly2) {
        return bly1 == bly2 || (!bly1.isEmpty() && !bly2.isEmpty() && bly1.sameItemStackIgnoreDurability(bly2));
    }
    
    public boolean sameItem(final ItemStack bly) {
        return !bly.isEmpty() && this.getItem() == bly.getItem();
    }
    
    public boolean sameItemStackIgnoreDurability(final ItemStack bly) {
        if (this.isDamageableItem()) {
            return !bly.isEmpty() && this.getItem() == bly.getItem();
        }
        return this.sameItem(bly);
    }
    
    public String getDescriptionId() {
        return this.getItem().getDescriptionId(this);
    }
    
    public String toString() {
        return new StringBuilder().append(this.count).append(" ").append(this.getItem()).toString();
    }
    
    public void inventoryTick(final Level bru, final Entity apx, final int integer, final boolean boolean4) {
        if (this.popTime > 0) {
            --this.popTime;
        }
        if (this.getItem() != null) {
            this.getItem().inventoryTick(this, bru, apx, integer, boolean4);
        }
    }
    
    public void onCraftedBy(final Level bru, final Player bft, final int integer) {
        bft.awardStat(Stats.ITEM_CRAFTED.get(this.getItem()), integer);
        this.getItem().onCraftedBy(this, bru, bft);
    }
    
    public int getUseDuration() {
        return this.getItem().getUseDuration(this);
    }
    
    public UseAnim getUseAnimation() {
        return this.getItem().getUseAnimation(this);
    }
    
    public void releaseUsing(final Level bru, final LivingEntity aqj, final int integer) {
        this.getItem().releaseUsing(this, bru, aqj, integer);
    }
    
    public boolean useOnRelease() {
        return this.getItem().useOnRelease(this);
    }
    
    public boolean hasTag() {
        return !this.emptyCacheFlag && this.tag != null && !this.tag.isEmpty();
    }
    
    @Nullable
    public CompoundTag getTag() {
        return this.tag;
    }
    
    public CompoundTag getOrCreateTag() {
        if (this.tag == null) {
            this.setTag(new CompoundTag());
        }
        return this.tag;
    }
    
    public CompoundTag getOrCreateTagElement(final String string) {
        if (this.tag == null || !this.tag.contains(string, 10)) {
            final CompoundTag md3 = new CompoundTag();
            this.addTagElement(string, md3);
            return md3;
        }
        return this.tag.getCompound(string);
    }
    
    @Nullable
    public CompoundTag getTagElement(final String string) {
        if (this.tag == null || !this.tag.contains(string, 10)) {
            return null;
        }
        return this.tag.getCompound(string);
    }
    
    public void removeTagKey(final String string) {
        if (this.tag != null && this.tag.contains(string)) {
            this.tag.remove(string);
            if (this.tag.isEmpty()) {
                this.tag = null;
            }
        }
    }
    
    public ListTag getEnchantmentTags() {
        if (this.tag != null) {
            return this.tag.getList("Enchantments", 10);
        }
        return new ListTag();
    }
    
    public void setTag(@Nullable final CompoundTag md) {
        this.tag = md;
        if (this.getItem().canBeDepleted()) {
            this.setDamageValue(this.getDamageValue());
        }
    }
    
    public Component getHoverName() {
        final CompoundTag md2 = this.getTagElement("display");
        if (md2 != null && md2.contains("Name", 8)) {
            try {
                final Component nr3 = Component.Serializer.fromJson(md2.getString("Name"));
                if (nr3 != null) {
                    return nr3;
                }
                md2.remove("Name");
            }
            catch (JsonParseException jsonParseException3) {
                md2.remove("Name");
            }
        }
        return this.getItem().getName(this);
    }
    
    public ItemStack setHoverName(@Nullable final Component nr) {
        final CompoundTag md3 = this.getOrCreateTagElement("display");
        if (nr != null) {
            md3.putString("Name", Component.Serializer.toJson(nr));
        }
        else {
            md3.remove("Name");
        }
        return this;
    }
    
    public void resetHoverName() {
        final CompoundTag md2 = this.getTagElement("display");
        if (md2 != null) {
            md2.remove("Name");
            if (md2.isEmpty()) {
                this.removeTagKey("display");
            }
        }
        if (this.tag != null && this.tag.isEmpty()) {
            this.tag = null;
        }
    }
    
    public boolean hasCustomHoverName() {
        final CompoundTag md2 = this.getTagElement("display");
        return md2 != null && md2.contains("Name", 8);
    }
    
    public void hideTooltipPart(final TooltipPart a) {
        final CompoundTag md3 = this.getOrCreateTag();
        md3.putInt("HideFlags", md3.getInt("HideFlags") | a.getMask());
    }
    
    public boolean hasFoil() {
        return this.getItem().isFoil(this);
    }
    
    public Rarity getRarity() {
        return this.getItem().getRarity(this);
    }
    
    public boolean isEnchantable() {
        return this.getItem().isEnchantable(this) && !this.isEnchanted();
    }
    
    public void enchant(final Enchantment bpp, final int integer) {
        this.getOrCreateTag();
        if (!this.tag.contains("Enchantments", 9)) {
            this.tag.put("Enchantments", (Tag)new ListTag());
        }
        final ListTag mj4 = this.tag.getList("Enchantments", 10);
        final CompoundTag md5 = new CompoundTag();
        md5.putString("id", String.valueOf(Registry.ENCHANTMENT.getKey(bpp)));
        md5.putShort("lvl", (short)(byte)integer);
        mj4.add(md5);
    }
    
    public boolean isEnchanted() {
        return this.tag != null && this.tag.contains("Enchantments", 9) && !this.tag.getList("Enchantments", 10).isEmpty();
    }
    
    public void addTagElement(final String string, final Tag mt) {
        this.getOrCreateTag().put(string, mt);
    }
    
    public boolean isFramed() {
        return this.entityRepresentation instanceof ItemFrame;
    }
    
    public void setEntityRepresentation(@Nullable final Entity apx) {
        this.entityRepresentation = apx;
    }
    
    @Nullable
    public ItemFrame getFrame() {
        return (this.entityRepresentation instanceof ItemFrame) ? ((ItemFrame)this.getEntityRepresentation()) : null;
    }
    
    @Nullable
    public Entity getEntityRepresentation() {
        return this.emptyCacheFlag ? null : this.entityRepresentation;
    }
    
    public int getBaseRepairCost() {
        if (this.hasTag() && this.tag.contains("RepairCost", 3)) {
            return this.tag.getInt("RepairCost");
        }
        return 0;
    }
    
    public void setRepairCost(final int integer) {
        this.getOrCreateTag().putInt("RepairCost", integer);
    }
    
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlot aqc) {
        Multimap<Attribute, AttributeModifier> multimap3;
        if (this.hasTag() && this.tag.contains("AttributeModifiers", 9)) {
            multimap3 = HashMultimap.create();
            final ListTag mj4 = this.tag.getList("AttributeModifiers", 10);
            for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
                final CompoundTag md6 = mj4.getCompound(integer5);
                if (!md6.contains("Slot", 8) || md6.getString("Slot").equals(aqc.getName())) {
                    final Optional<Attribute> optional7 = Registry.ATTRIBUTE.getOptional(ResourceLocation.tryParse(md6.getString("AttributeName")));
                    if (optional7.isPresent()) {
                        final AttributeModifier arg8 = AttributeModifier.load(md6);
                        if (arg8 != null) {
                            if (arg8.getId().getLeastSignificantBits() != 0L && arg8.getId().getMostSignificantBits() != 0L) {
                                multimap3.put((Attribute)optional7.get(), arg8);
                            }
                        }
                    }
                }
            }
        }
        else {
            multimap3 = this.getItem().getDefaultAttributeModifiers(aqc);
        }
        return multimap3;
    }
    
    public void addAttributeModifier(final Attribute ard, final AttributeModifier arg, @Nullable final EquipmentSlot aqc) {
        this.getOrCreateTag();
        if (!this.tag.contains("AttributeModifiers", 9)) {
            this.tag.put("AttributeModifiers", (Tag)new ListTag());
        }
        final ListTag mj5 = this.tag.getList("AttributeModifiers", 10);
        final CompoundTag md6 = arg.save();
        md6.putString("AttributeName", Registry.ATTRIBUTE.getKey(ard).toString());
        if (aqc != null) {
            md6.putString("Slot", aqc.getName());
        }
        mj5.add(md6);
    }
    
    public Component getDisplayName() {
        final MutableComponent nx2 = new TextComponent("").append(this.getHoverName());
        if (this.hasCustomHoverName()) {
            nx2.withStyle(ChatFormatting.ITALIC);
        }
        final MutableComponent nx3 = ComponentUtils.wrapInSquareBrackets(nx2);
        if (!this.emptyCacheFlag) {
            nx3.withStyle(this.getRarity().color).withStyle((UnaryOperator<Style>)(ob -> ob.withHoverEvent(new HoverEvent((HoverEvent.Action<T>)HoverEvent.Action.SHOW_ITEM, (T)new HoverEvent.ItemStackInfo(this)))));
        }
        return nx3;
    }
    
    private static boolean areSameBlocks(final BlockInWorld cei1, @Nullable final BlockInWorld cei2) {
        return cei2 != null && cei1.getState() == cei2.getState() && ((cei1.getEntity() == null && cei2.getEntity() == null) || (cei1.getEntity() != null && cei2.getEntity() != null && Objects.equals(cei1.getEntity().save(new CompoundTag()), cei2.getEntity().save(new CompoundTag()))));
    }
    
    public boolean hasAdventureModeBreakTagForBlock(final TagContainer ael, final BlockInWorld cei) {
        if (areSameBlocks(cei, this.cachedBreakBlock)) {
            return this.cachedBreakBlockResult;
        }
        this.cachedBreakBlock = cei;
        if (this.hasTag() && this.tag.contains("CanDestroy", 9)) {
            final ListTag mj4 = this.tag.getList("CanDestroy", 8);
            for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
                final String string6 = mj4.getString(integer5);
                try {
                    final Predicate<BlockInWorld> predicate7 = BlockPredicateArgument.blockPredicate().a(new StringReader(string6)).create(ael);
                    if (predicate7.test(cei)) {
                        return this.cachedBreakBlockResult = true;
                    }
                }
                catch (CommandSyntaxException ex) {}
            }
        }
        return this.cachedBreakBlockResult = false;
    }
    
    public boolean hasAdventureModePlaceTagForBlock(final TagContainer ael, final BlockInWorld cei) {
        if (areSameBlocks(cei, this.cachedPlaceBlock)) {
            return this.cachedPlaceBlockResult;
        }
        this.cachedPlaceBlock = cei;
        if (this.hasTag() && this.tag.contains("CanPlaceOn", 9)) {
            final ListTag mj4 = this.tag.getList("CanPlaceOn", 8);
            for (int integer5 = 0; integer5 < mj4.size(); ++integer5) {
                final String string6 = mj4.getString(integer5);
                try {
                    final Predicate<BlockInWorld> predicate7 = BlockPredicateArgument.blockPredicate().a(new StringReader(string6)).create(ael);
                    if (predicate7.test(cei)) {
                        return this.cachedPlaceBlockResult = true;
                    }
                }
                catch (CommandSyntaxException ex) {}
            }
        }
        return this.cachedPlaceBlockResult = false;
    }
    
    public int getPopTime() {
        return this.popTime;
    }
    
    public void setPopTime(final int integer) {
        this.popTime = integer;
    }
    
    public int getCount() {
        return this.emptyCacheFlag ? 0 : this.count;
    }
    
    public void setCount(final int integer) {
        this.count = integer;
        this.updateEmptyCacheFlag();
    }
    
    public void grow(final int integer) {
        this.setCount(this.count + integer);
    }
    
    public void shrink(final int integer) {
        this.grow(-integer);
    }
    
    public void onUseTick(final Level bru, final LivingEntity aqj, final int integer) {
        this.getItem().onUseTick(bru, aqj, this, integer);
    }
    
    public boolean isEdible() {
        return this.getItem().isEdible();
    }
    
    public SoundEvent getDrinkingSound() {
        return this.getItem().getDrinkingSound();
    }
    
    public SoundEvent getEatingSound() {
        return this.getItem().getEatingSound();
    }
    
    static {
        CODEC = RecordCodecBuilder.<ItemStack>create((java.util.function.Function<RecordCodecBuilder.Instance<ItemStack>, ? extends App<RecordCodecBuilder.Mu<ItemStack>, ItemStack>>)(instance -> instance.<ItemLike, Integer, java.util.Optional<CompoundTag>>group(Registry.ITEM.fieldOf("id").forGetter((java.util.function.Function<Object, Object>)(bly -> bly.item)), Codec.INT.fieldOf("Count").forGetter((java.util.function.Function<Object, Object>)(bly -> bly.count)), CompoundTag.CODEC.optionalFieldOf("tag").forGetter((java.util.function.Function<Object, java.util.Optional<CompoundTag>>)(bly -> Optional.ofNullable(bly.tag)))).<ItemStack>apply(instance, ItemStack::new)));
        LOGGER = LogManager.getLogger();
        EMPTY = new ItemStack((ItemLike)null);
        ATTRIBUTE_MODIFIER_FORMAT = Util.<DecimalFormat>make(new DecimalFormat("#.##"), (java.util.function.Consumer<DecimalFormat>)(decimalFormat -> decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT))));
        LORE_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_PURPLE).withItalic(true);
    }
    
    public enum TooltipPart {
        ENCHANTMENTS, 
        MODIFIERS, 
        UNBREAKABLE, 
        CAN_DESTROY, 
        CAN_PLACE, 
        ADDITIONAL, 
        DYE;
        
        private int mask;
        
        private TooltipPart() {
            this.mask = 1 << this.ordinal();
        }
        
        public int getMask() {
            return this.mask;
        }
    }
}
