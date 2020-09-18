package net.minecraft.advancements;

import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.PlayerInteractTrigger;
import net.minecraft.advancements.critereon.ItemPickedUpByEntityTrigger;
import net.minecraft.advancements.critereon.LootTableTrigger;
import net.minecraft.advancements.critereon.ItemUsedOnBlockTrigger;
import net.minecraft.advancements.critereon.TargetBlockTrigger;
import net.minecraft.advancements.critereon.BeeNestDestroyedTrigger;
import net.minecraft.advancements.critereon.SlideDownBlockTrigger;
import net.minecraft.advancements.critereon.KilledByCrossbowTrigger;
import net.minecraft.advancements.critereon.ShotCrossbowTrigger;
import net.minecraft.advancements.critereon.ChanneledLightningTrigger;
import net.minecraft.advancements.critereon.FishingRodHookedTrigger;
import net.minecraft.advancements.critereon.NetherTravelTrigger;
import net.minecraft.advancements.critereon.UsedTotemTrigger;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.ConsumeItemTrigger;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.advancements.critereon.ChangeDimensionTrigger;
import net.minecraft.advancements.critereon.LevitationTrigger;
import net.minecraft.advancements.critereon.ItemDurabilityTrigger;
import net.minecraft.advancements.critereon.TradeTrigger;
import net.minecraft.advancements.critereon.CuredZombieVillagerTrigger;
import net.minecraft.advancements.critereon.LocationTrigger;
import net.minecraft.advancements.critereon.BredAnimalsTrigger;
import net.minecraft.advancements.critereon.SummonedEntityTrigger;
import net.minecraft.advancements.critereon.UsedEnderEyeTrigger;
import net.minecraft.advancements.critereon.ConstructBeaconTrigger;
import net.minecraft.advancements.critereon.BrewedPotionTrigger;
import net.minecraft.advancements.critereon.FilledBucketTrigger;
import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.advancements.critereon.PlayerHurtEntityTrigger;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.resources.ResourceLocation;
import java.util.Map;

public class CriteriaTriggers {
    private static final Map<ResourceLocation, CriterionTrigger<?>> CRITERIA;
    public static final ImpossibleTrigger IMPOSSIBLE;
    public static final KilledTrigger PLAYER_KILLED_ENTITY;
    public static final KilledTrigger ENTITY_KILLED_PLAYER;
    public static final EnterBlockTrigger ENTER_BLOCK;
    public static final InventoryChangeTrigger INVENTORY_CHANGED;
    public static final RecipeUnlockedTrigger RECIPE_UNLOCKED;
    public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY;
    public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER;
    public static final EnchantedItemTrigger ENCHANTED_ITEM;
    public static final FilledBucketTrigger FILLED_BUCKET;
    public static final BrewedPotionTrigger BREWED_POTION;
    public static final ConstructBeaconTrigger CONSTRUCT_BEACON;
    public static final UsedEnderEyeTrigger USED_ENDER_EYE;
    public static final SummonedEntityTrigger SUMMONED_ENTITY;
    public static final BredAnimalsTrigger BRED_ANIMALS;
    public static final LocationTrigger LOCATION;
    public static final LocationTrigger SLEPT_IN_BED;
    public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER;
    public static final TradeTrigger TRADE;
    public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED;
    public static final LevitationTrigger LEVITATION;
    public static final ChangeDimensionTrigger CHANGED_DIMENSION;
    public static final TickTrigger TICK;
    public static final TameAnimalTrigger TAME_ANIMAL;
    public static final PlacedBlockTrigger PLACED_BLOCK;
    public static final ConsumeItemTrigger CONSUME_ITEM;
    public static final EffectsChangedTrigger EFFECTS_CHANGED;
    public static final UsedTotemTrigger USED_TOTEM;
    public static final NetherTravelTrigger NETHER_TRAVEL;
    public static final FishingRodHookedTrigger FISHING_ROD_HOOKED;
    public static final ChanneledLightningTrigger CHANNELED_LIGHTNING;
    public static final ShotCrossbowTrigger SHOT_CROSSBOW;
    public static final KilledByCrossbowTrigger KILLED_BY_CROSSBOW;
    public static final LocationTrigger RAID_WIN;
    public static final LocationTrigger BAD_OMEN;
    public static final SlideDownBlockTrigger HONEY_BLOCK_SLIDE;
    public static final BeeNestDestroyedTrigger BEE_NEST_DESTROYED;
    public static final TargetBlockTrigger TARGET_BLOCK_HIT;
    public static final ItemUsedOnBlockTrigger ITEM_USED_ON_BLOCK;
    public static final LootTableTrigger GENERATE_LOOT;
    public static final ItemPickedUpByEntityTrigger ITEM_PICKED_UP_BY_ENTITY;
    public static final PlayerInteractTrigger PLAYER_INTERACTED_WITH_ENTITY;
    
    private static <T extends CriterionTrigger<?>> T register(final T af) {
        if (CriteriaTriggers.CRITERIA.containsKey(af.getId())) {
            throw new IllegalArgumentException(new StringBuilder().append("Duplicate criterion id ").append(af.getId()).toString());
        }
        CriteriaTriggers.CRITERIA.put(af.getId(), af);
        return af;
    }
    
    @Nullable
    public static <T extends CriterionTriggerInstance> CriterionTrigger<T> getCriterion(final ResourceLocation vk) {
        return (CriterionTrigger<T>)CriteriaTriggers.CRITERIA.get(vk);
    }
    
    public static Iterable<? extends CriterionTrigger<?>> all() {
        return CriteriaTriggers.CRITERIA.values();
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: putstatic       net/minecraft/advancements/CriteriaTriggers.CRITERIA:Ljava/util/Map;
        //     6: new             Lnet/minecraft/advancements/critereon/ImpossibleTrigger;
        //     9: dup            
        //    10: invokespecial   net/minecraft/advancements/critereon/ImpossibleTrigger.<init>:()V
        //    13: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //    16: checkcast       Lnet/minecraft/advancements/critereon/ImpossibleTrigger;
        //    19: putstatic       net/minecraft/advancements/CriteriaTriggers.IMPOSSIBLE:Lnet/minecraft/advancements/critereon/ImpossibleTrigger;
        //    22: new             Lnet/minecraft/advancements/critereon/KilledTrigger;
        //    25: dup            
        //    26: new             Lnet/minecraft/resources/ResourceLocation;
        //    29: dup            
        //    30: ldc             "player_killed_entity"
        //    32: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    35: invokespecial   net/minecraft/advancements/critereon/KilledTrigger.<init>:(Lnet/minecraft/resources/ResourceLocation;)V
        //    38: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //    41: checkcast       Lnet/minecraft/advancements/critereon/KilledTrigger;
        //    44: putstatic       net/minecraft/advancements/CriteriaTriggers.PLAYER_KILLED_ENTITY:Lnet/minecraft/advancements/critereon/KilledTrigger;
        //    47: new             Lnet/minecraft/advancements/critereon/KilledTrigger;
        //    50: dup            
        //    51: new             Lnet/minecraft/resources/ResourceLocation;
        //    54: dup            
        //    55: ldc             "entity_killed_player"
        //    57: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    60: invokespecial   net/minecraft/advancements/critereon/KilledTrigger.<init>:(Lnet/minecraft/resources/ResourceLocation;)V
        //    63: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //    66: checkcast       Lnet/minecraft/advancements/critereon/KilledTrigger;
        //    69: putstatic       net/minecraft/advancements/CriteriaTriggers.ENTITY_KILLED_PLAYER:Lnet/minecraft/advancements/critereon/KilledTrigger;
        //    72: new             Lnet/minecraft/advancements/critereon/EnterBlockTrigger;
        //    75: dup            
        //    76: invokespecial   net/minecraft/advancements/critereon/EnterBlockTrigger.<init>:()V
        //    79: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //    82: checkcast       Lnet/minecraft/advancements/critereon/EnterBlockTrigger;
        //    85: putstatic       net/minecraft/advancements/CriteriaTriggers.ENTER_BLOCK:Lnet/minecraft/advancements/critereon/EnterBlockTrigger;
        //    88: new             Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;
        //    91: dup            
        //    92: invokespecial   net/minecraft/advancements/critereon/InventoryChangeTrigger.<init>:()V
        //    95: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //    98: checkcast       Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;
        //   101: putstatic       net/minecraft/advancements/CriteriaTriggers.INVENTORY_CHANGED:Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;
        //   104: new             Lnet/minecraft/advancements/critereon/RecipeUnlockedTrigger;
        //   107: dup            
        //   108: invokespecial   net/minecraft/advancements/critereon/RecipeUnlockedTrigger.<init>:()V
        //   111: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   114: checkcast       Lnet/minecraft/advancements/critereon/RecipeUnlockedTrigger;
        //   117: putstatic       net/minecraft/advancements/CriteriaTriggers.RECIPE_UNLOCKED:Lnet/minecraft/advancements/critereon/RecipeUnlockedTrigger;
        //   120: new             Lnet/minecraft/advancements/critereon/PlayerHurtEntityTrigger;
        //   123: dup            
        //   124: invokespecial   net/minecraft/advancements/critereon/PlayerHurtEntityTrigger.<init>:()V
        //   127: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   130: checkcast       Lnet/minecraft/advancements/critereon/PlayerHurtEntityTrigger;
        //   133: putstatic       net/minecraft/advancements/CriteriaTriggers.PLAYER_HURT_ENTITY:Lnet/minecraft/advancements/critereon/PlayerHurtEntityTrigger;
        //   136: new             Lnet/minecraft/advancements/critereon/EntityHurtPlayerTrigger;
        //   139: dup            
        //   140: invokespecial   net/minecraft/advancements/critereon/EntityHurtPlayerTrigger.<init>:()V
        //   143: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   146: checkcast       Lnet/minecraft/advancements/critereon/EntityHurtPlayerTrigger;
        //   149: putstatic       net/minecraft/advancements/CriteriaTriggers.ENTITY_HURT_PLAYER:Lnet/minecraft/advancements/critereon/EntityHurtPlayerTrigger;
        //   152: new             Lnet/minecraft/advancements/critereon/EnchantedItemTrigger;
        //   155: dup            
        //   156: invokespecial   net/minecraft/advancements/critereon/EnchantedItemTrigger.<init>:()V
        //   159: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   162: checkcast       Lnet/minecraft/advancements/critereon/EnchantedItemTrigger;
        //   165: putstatic       net/minecraft/advancements/CriteriaTriggers.ENCHANTED_ITEM:Lnet/minecraft/advancements/critereon/EnchantedItemTrigger;
        //   168: new             Lnet/minecraft/advancements/critereon/FilledBucketTrigger;
        //   171: dup            
        //   172: invokespecial   net/minecraft/advancements/critereon/FilledBucketTrigger.<init>:()V
        //   175: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   178: checkcast       Lnet/minecraft/advancements/critereon/FilledBucketTrigger;
        //   181: putstatic       net/minecraft/advancements/CriteriaTriggers.FILLED_BUCKET:Lnet/minecraft/advancements/critereon/FilledBucketTrigger;
        //   184: new             Lnet/minecraft/advancements/critereon/BrewedPotionTrigger;
        //   187: dup            
        //   188: invokespecial   net/minecraft/advancements/critereon/BrewedPotionTrigger.<init>:()V
        //   191: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   194: checkcast       Lnet/minecraft/advancements/critereon/BrewedPotionTrigger;
        //   197: putstatic       net/minecraft/advancements/CriteriaTriggers.BREWED_POTION:Lnet/minecraft/advancements/critereon/BrewedPotionTrigger;
        //   200: new             Lnet/minecraft/advancements/critereon/ConstructBeaconTrigger;
        //   203: dup            
        //   204: invokespecial   net/minecraft/advancements/critereon/ConstructBeaconTrigger.<init>:()V
        //   207: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   210: checkcast       Lnet/minecraft/advancements/critereon/ConstructBeaconTrigger;
        //   213: putstatic       net/minecraft/advancements/CriteriaTriggers.CONSTRUCT_BEACON:Lnet/minecraft/advancements/critereon/ConstructBeaconTrigger;
        //   216: new             Lnet/minecraft/advancements/critereon/UsedEnderEyeTrigger;
        //   219: dup            
        //   220: invokespecial   net/minecraft/advancements/critereon/UsedEnderEyeTrigger.<init>:()V
        //   223: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   226: checkcast       Lnet/minecraft/advancements/critereon/UsedEnderEyeTrigger;
        //   229: putstatic       net/minecraft/advancements/CriteriaTriggers.USED_ENDER_EYE:Lnet/minecraft/advancements/critereon/UsedEnderEyeTrigger;
        //   232: new             Lnet/minecraft/advancements/critereon/SummonedEntityTrigger;
        //   235: dup            
        //   236: invokespecial   net/minecraft/advancements/critereon/SummonedEntityTrigger.<init>:()V
        //   239: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   242: checkcast       Lnet/minecraft/advancements/critereon/SummonedEntityTrigger;
        //   245: putstatic       net/minecraft/advancements/CriteriaTriggers.SUMMONED_ENTITY:Lnet/minecraft/advancements/critereon/SummonedEntityTrigger;
        //   248: new             Lnet/minecraft/advancements/critereon/BredAnimalsTrigger;
        //   251: dup            
        //   252: invokespecial   net/minecraft/advancements/critereon/BredAnimalsTrigger.<init>:()V
        //   255: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   258: checkcast       Lnet/minecraft/advancements/critereon/BredAnimalsTrigger;
        //   261: putstatic       net/minecraft/advancements/CriteriaTriggers.BRED_ANIMALS:Lnet/minecraft/advancements/critereon/BredAnimalsTrigger;
        //   264: new             Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   267: dup            
        //   268: new             Lnet/minecraft/resources/ResourceLocation;
        //   271: dup            
        //   272: ldc             "location"
        //   274: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   277: invokespecial   net/minecraft/advancements/critereon/LocationTrigger.<init>:(Lnet/minecraft/resources/ResourceLocation;)V
        //   280: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   283: checkcast       Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   286: putstatic       net/minecraft/advancements/CriteriaTriggers.LOCATION:Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   289: new             Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   292: dup            
        //   293: new             Lnet/minecraft/resources/ResourceLocation;
        //   296: dup            
        //   297: ldc             "slept_in_bed"
        //   299: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   302: invokespecial   net/minecraft/advancements/critereon/LocationTrigger.<init>:(Lnet/minecraft/resources/ResourceLocation;)V
        //   305: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   308: checkcast       Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   311: putstatic       net/minecraft/advancements/CriteriaTriggers.SLEPT_IN_BED:Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   314: new             Lnet/minecraft/advancements/critereon/CuredZombieVillagerTrigger;
        //   317: dup            
        //   318: invokespecial   net/minecraft/advancements/critereon/CuredZombieVillagerTrigger.<init>:()V
        //   321: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   324: checkcast       Lnet/minecraft/advancements/critereon/CuredZombieVillagerTrigger;
        //   327: putstatic       net/minecraft/advancements/CriteriaTriggers.CURED_ZOMBIE_VILLAGER:Lnet/minecraft/advancements/critereon/CuredZombieVillagerTrigger;
        //   330: new             Lnet/minecraft/advancements/critereon/TradeTrigger;
        //   333: dup            
        //   334: invokespecial   net/minecraft/advancements/critereon/TradeTrigger.<init>:()V
        //   337: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   340: checkcast       Lnet/minecraft/advancements/critereon/TradeTrigger;
        //   343: putstatic       net/minecraft/advancements/CriteriaTriggers.TRADE:Lnet/minecraft/advancements/critereon/TradeTrigger;
        //   346: new             Lnet/minecraft/advancements/critereon/ItemDurabilityTrigger;
        //   349: dup            
        //   350: invokespecial   net/minecraft/advancements/critereon/ItemDurabilityTrigger.<init>:()V
        //   353: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   356: checkcast       Lnet/minecraft/advancements/critereon/ItemDurabilityTrigger;
        //   359: putstatic       net/minecraft/advancements/CriteriaTriggers.ITEM_DURABILITY_CHANGED:Lnet/minecraft/advancements/critereon/ItemDurabilityTrigger;
        //   362: new             Lnet/minecraft/advancements/critereon/LevitationTrigger;
        //   365: dup            
        //   366: invokespecial   net/minecraft/advancements/critereon/LevitationTrigger.<init>:()V
        //   369: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   372: checkcast       Lnet/minecraft/advancements/critereon/LevitationTrigger;
        //   375: putstatic       net/minecraft/advancements/CriteriaTriggers.LEVITATION:Lnet/minecraft/advancements/critereon/LevitationTrigger;
        //   378: new             Lnet/minecraft/advancements/critereon/ChangeDimensionTrigger;
        //   381: dup            
        //   382: invokespecial   net/minecraft/advancements/critereon/ChangeDimensionTrigger.<init>:()V
        //   385: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   388: checkcast       Lnet/minecraft/advancements/critereon/ChangeDimensionTrigger;
        //   391: putstatic       net/minecraft/advancements/CriteriaTriggers.CHANGED_DIMENSION:Lnet/minecraft/advancements/critereon/ChangeDimensionTrigger;
        //   394: new             Lnet/minecraft/advancements/critereon/TickTrigger;
        //   397: dup            
        //   398: invokespecial   net/minecraft/advancements/critereon/TickTrigger.<init>:()V
        //   401: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   404: checkcast       Lnet/minecraft/advancements/critereon/TickTrigger;
        //   407: putstatic       net/minecraft/advancements/CriteriaTriggers.TICK:Lnet/minecraft/advancements/critereon/TickTrigger;
        //   410: new             Lnet/minecraft/advancements/critereon/TameAnimalTrigger;
        //   413: dup            
        //   414: invokespecial   net/minecraft/advancements/critereon/TameAnimalTrigger.<init>:()V
        //   417: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   420: checkcast       Lnet/minecraft/advancements/critereon/TameAnimalTrigger;
        //   423: putstatic       net/minecraft/advancements/CriteriaTriggers.TAME_ANIMAL:Lnet/minecraft/advancements/critereon/TameAnimalTrigger;
        //   426: new             Lnet/minecraft/advancements/critereon/PlacedBlockTrigger;
        //   429: dup            
        //   430: invokespecial   net/minecraft/advancements/critereon/PlacedBlockTrigger.<init>:()V
        //   433: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   436: checkcast       Lnet/minecraft/advancements/critereon/PlacedBlockTrigger;
        //   439: putstatic       net/minecraft/advancements/CriteriaTriggers.PLACED_BLOCK:Lnet/minecraft/advancements/critereon/PlacedBlockTrigger;
        //   442: new             Lnet/minecraft/advancements/critereon/ConsumeItemTrigger;
        //   445: dup            
        //   446: invokespecial   net/minecraft/advancements/critereon/ConsumeItemTrigger.<init>:()V
        //   449: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   452: checkcast       Lnet/minecraft/advancements/critereon/ConsumeItemTrigger;
        //   455: putstatic       net/minecraft/advancements/CriteriaTriggers.CONSUME_ITEM:Lnet/minecraft/advancements/critereon/ConsumeItemTrigger;
        //   458: new             Lnet/minecraft/advancements/critereon/EffectsChangedTrigger;
        //   461: dup            
        //   462: invokespecial   net/minecraft/advancements/critereon/EffectsChangedTrigger.<init>:()V
        //   465: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   468: checkcast       Lnet/minecraft/advancements/critereon/EffectsChangedTrigger;
        //   471: putstatic       net/minecraft/advancements/CriteriaTriggers.EFFECTS_CHANGED:Lnet/minecraft/advancements/critereon/EffectsChangedTrigger;
        //   474: new             Lnet/minecraft/advancements/critereon/UsedTotemTrigger;
        //   477: dup            
        //   478: invokespecial   net/minecraft/advancements/critereon/UsedTotemTrigger.<init>:()V
        //   481: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   484: checkcast       Lnet/minecraft/advancements/critereon/UsedTotemTrigger;
        //   487: putstatic       net/minecraft/advancements/CriteriaTriggers.USED_TOTEM:Lnet/minecraft/advancements/critereon/UsedTotemTrigger;
        //   490: new             Lnet/minecraft/advancements/critereon/NetherTravelTrigger;
        //   493: dup            
        //   494: invokespecial   net/minecraft/advancements/critereon/NetherTravelTrigger.<init>:()V
        //   497: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   500: checkcast       Lnet/minecraft/advancements/critereon/NetherTravelTrigger;
        //   503: putstatic       net/minecraft/advancements/CriteriaTriggers.NETHER_TRAVEL:Lnet/minecraft/advancements/critereon/NetherTravelTrigger;
        //   506: new             Lnet/minecraft/advancements/critereon/FishingRodHookedTrigger;
        //   509: dup            
        //   510: invokespecial   net/minecraft/advancements/critereon/FishingRodHookedTrigger.<init>:()V
        //   513: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   516: checkcast       Lnet/minecraft/advancements/critereon/FishingRodHookedTrigger;
        //   519: putstatic       net/minecraft/advancements/CriteriaTriggers.FISHING_ROD_HOOKED:Lnet/minecraft/advancements/critereon/FishingRodHookedTrigger;
        //   522: new             Lnet/minecraft/advancements/critereon/ChanneledLightningTrigger;
        //   525: dup            
        //   526: invokespecial   net/minecraft/advancements/critereon/ChanneledLightningTrigger.<init>:()V
        //   529: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   532: checkcast       Lnet/minecraft/advancements/critereon/ChanneledLightningTrigger;
        //   535: putstatic       net/minecraft/advancements/CriteriaTriggers.CHANNELED_LIGHTNING:Lnet/minecraft/advancements/critereon/ChanneledLightningTrigger;
        //   538: new             Lnet/minecraft/advancements/critereon/ShotCrossbowTrigger;
        //   541: dup            
        //   542: invokespecial   net/minecraft/advancements/critereon/ShotCrossbowTrigger.<init>:()V
        //   545: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   548: checkcast       Lnet/minecraft/advancements/critereon/ShotCrossbowTrigger;
        //   551: putstatic       net/minecraft/advancements/CriteriaTriggers.SHOT_CROSSBOW:Lnet/minecraft/advancements/critereon/ShotCrossbowTrigger;
        //   554: new             Lnet/minecraft/advancements/critereon/KilledByCrossbowTrigger;
        //   557: dup            
        //   558: invokespecial   net/minecraft/advancements/critereon/KilledByCrossbowTrigger.<init>:()V
        //   561: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   564: checkcast       Lnet/minecraft/advancements/critereon/KilledByCrossbowTrigger;
        //   567: putstatic       net/minecraft/advancements/CriteriaTriggers.KILLED_BY_CROSSBOW:Lnet/minecraft/advancements/critereon/KilledByCrossbowTrigger;
        //   570: new             Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   573: dup            
        //   574: new             Lnet/minecraft/resources/ResourceLocation;
        //   577: dup            
        //   578: ldc_w           "hero_of_the_village"
        //   581: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   584: invokespecial   net/minecraft/advancements/critereon/LocationTrigger.<init>:(Lnet/minecraft/resources/ResourceLocation;)V
        //   587: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   590: checkcast       Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   593: putstatic       net/minecraft/advancements/CriteriaTriggers.RAID_WIN:Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   596: new             Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   599: dup            
        //   600: new             Lnet/minecraft/resources/ResourceLocation;
        //   603: dup            
        //   604: ldc_w           "voluntary_exile"
        //   607: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   610: invokespecial   net/minecraft/advancements/critereon/LocationTrigger.<init>:(Lnet/minecraft/resources/ResourceLocation;)V
        //   613: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   616: checkcast       Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   619: putstatic       net/minecraft/advancements/CriteriaTriggers.BAD_OMEN:Lnet/minecraft/advancements/critereon/LocationTrigger;
        //   622: new             Lnet/minecraft/advancements/critereon/SlideDownBlockTrigger;
        //   625: dup            
        //   626: invokespecial   net/minecraft/advancements/critereon/SlideDownBlockTrigger.<init>:()V
        //   629: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   632: checkcast       Lnet/minecraft/advancements/critereon/SlideDownBlockTrigger;
        //   635: putstatic       net/minecraft/advancements/CriteriaTriggers.HONEY_BLOCK_SLIDE:Lnet/minecraft/advancements/critereon/SlideDownBlockTrigger;
        //   638: new             Lnet/minecraft/advancements/critereon/BeeNestDestroyedTrigger;
        //   641: dup            
        //   642: invokespecial   net/minecraft/advancements/critereon/BeeNestDestroyedTrigger.<init>:()V
        //   645: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   648: checkcast       Lnet/minecraft/advancements/critereon/BeeNestDestroyedTrigger;
        //   651: putstatic       net/minecraft/advancements/CriteriaTriggers.BEE_NEST_DESTROYED:Lnet/minecraft/advancements/critereon/BeeNestDestroyedTrigger;
        //   654: new             Lnet/minecraft/advancements/critereon/TargetBlockTrigger;
        //   657: dup            
        //   658: invokespecial   net/minecraft/advancements/critereon/TargetBlockTrigger.<init>:()V
        //   661: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   664: checkcast       Lnet/minecraft/advancements/critereon/TargetBlockTrigger;
        //   667: putstatic       net/minecraft/advancements/CriteriaTriggers.TARGET_BLOCK_HIT:Lnet/minecraft/advancements/critereon/TargetBlockTrigger;
        //   670: new             Lnet/minecraft/advancements/critereon/ItemUsedOnBlockTrigger;
        //   673: dup            
        //   674: invokespecial   net/minecraft/advancements/critereon/ItemUsedOnBlockTrigger.<init>:()V
        //   677: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   680: checkcast       Lnet/minecraft/advancements/critereon/ItemUsedOnBlockTrigger;
        //   683: putstatic       net/minecraft/advancements/CriteriaTriggers.ITEM_USED_ON_BLOCK:Lnet/minecraft/advancements/critereon/ItemUsedOnBlockTrigger;
        //   686: new             Lnet/minecraft/advancements/critereon/LootTableTrigger;
        //   689: dup            
        //   690: invokespecial   net/minecraft/advancements/critereon/LootTableTrigger.<init>:()V
        //   693: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   696: checkcast       Lnet/minecraft/advancements/critereon/LootTableTrigger;
        //   699: putstatic       net/minecraft/advancements/CriteriaTriggers.GENERATE_LOOT:Lnet/minecraft/advancements/critereon/LootTableTrigger;
        //   702: new             Lnet/minecraft/advancements/critereon/ItemPickedUpByEntityTrigger;
        //   705: dup            
        //   706: invokespecial   net/minecraft/advancements/critereon/ItemPickedUpByEntityTrigger.<init>:()V
        //   709: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   712: checkcast       Lnet/minecraft/advancements/critereon/ItemPickedUpByEntityTrigger;
        //   715: putstatic       net/minecraft/advancements/CriteriaTriggers.ITEM_PICKED_UP_BY_ENTITY:Lnet/minecraft/advancements/critereon/ItemPickedUpByEntityTrigger;
        //   718: new             Lnet/minecraft/advancements/critereon/PlayerInteractTrigger;
        //   721: dup            
        //   722: invokespecial   net/minecraft/advancements/critereon/PlayerInteractTrigger.<init>:()V
        //   725: invokestatic    net/minecraft/advancements/CriteriaTriggers.register:(Lnet/minecraft/advancements/CriterionTrigger;)Lnet/minecraft/advancements/CriterionTrigger;
        //   728: checkcast       Lnet/minecraft/advancements/critereon/PlayerInteractTrigger;
        //   731: putstatic       net/minecraft/advancements/CriteriaTriggers.PLAYER_INTERACTED_WITH_ENTITY:Lnet/minecraft/advancements/critereon/PlayerInteractTrigger;
        //   734: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.resolve(CoreMetadataFactory.java:744)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2114)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitParameterizedType(MetadataHelper.java:2165)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitParameterizedType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2017)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:1302)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSubType(MetadataHelper.java:568)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:922)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:586)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:397)
        //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at cuchaz.enigma.source.procyon.ProcyonDecompiler.getSource(ProcyonDecompiler.java:75)
        //     at cuchaz.enigma.EnigmaProject$JarExport.decompileClass(EnigmaProject.java:270)
        //     at cuchaz.enigma.EnigmaProject$JarExport.lambda$decompileStream$1(EnigmaProject.java:246)
        //     at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:195)
        //     at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1624)
        //     at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:484)
        //     at java.base/java.util.stream.ForEachOps$ForEachTask.compute(ForEachOps.java:290)
        //     at java.base/java.util.concurrent.CountedCompleter.exec(CountedCompleter.java:746)
        //     at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:290)
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1016)
        //     at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1665)
        //     at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1598)
        //     at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:177)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
