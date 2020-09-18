package net.minecraft.data.advancements;

import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.EntityEquipmentPredicate;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.resources.ResourceKey;
import java.util.List;
import net.minecraft.advancements.Advancement;
import java.util.function.Consumer;

public class NetherAdvancements implements Consumer<Consumer<Advancement>> {
    private static final List<ResourceKey<Biome>> EXPLORABLE_BIOMES;
    private static final EntityPredicate.Composite DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE;
    
    public void accept(final Consumer<Advancement> consumer) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: getstatic       net/minecraft/world/level/block/Blocks.RED_NETHER_BRICKS:Lnet/minecraft/world/level/block/Block;
        //     6: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //     9: dup            
        //    10: ldc             "advancements.nether.root.title"
        //    12: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    15: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    18: dup            
        //    19: ldc             "advancements.nether.root.description"
        //    21: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    24: new             Lnet/minecraft/resources/ResourceLocation;
        //    27: dup            
        //    28: ldc             "textures/gui/advancements/backgrounds/nether.png"
        //    30: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    33: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //    36: iconst_0       
        //    37: iconst_0       
        //    38: iconst_0       
        //    39: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //    42: ldc             "entered_nether"
        //    44: getstatic       net/minecraft/world/level/Level.NETHER:Lnet/minecraft/resources/ResourceKey;
        //    47: invokestatic    net/minecraft/advancements/critereon/ChangeDimensionTrigger$TriggerInstance.changedDimensionTo:(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/advancements/critereon/ChangeDimensionTrigger$TriggerInstance;
        //    50: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //    53: aload_1         /* consumer */
        //    54: ldc             "nether/root"
        //    56: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //    59: astore_2        /* y3 */
        //    60: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //    63: aload_2         /* y3 */
        //    64: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //    67: getstatic       net/minecraft/world/item/Items.FIRE_CHARGE:Lnet/minecraft/world/item/Item;
        //    70: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    73: dup            
        //    74: ldc             "advancements.nether.return_to_sender.title"
        //    76: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    79: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //    82: dup            
        //    83: ldc             "advancements.nether.return_to_sender.description"
        //    85: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //    88: aconst_null    
        //    89: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //    92: iconst_1       
        //    93: iconst_1       
        //    94: iconst_0       
        //    95: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //    98: bipush          50
        //   100: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //   103: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //   106: ldc             "killed_ghast"
        //   108: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   111: getstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        //   114: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   117: invokestatic    net/minecraft/advancements/critereon/DamageSourcePredicate$Builder.damageType:()Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;
        //   120: iconst_1       
        //   121: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //   124: invokevirtual   net/minecraft/advancements/critereon/DamageSourcePredicate$Builder.isProjectile:(Ljava/lang/Boolean;)Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;
        //   127: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   130: getstatic       net/minecraft/world/entity/EntityType.FIREBALL:Lnet/minecraft/world/entity/EntityType;
        //   133: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   136: invokevirtual   net/minecraft/advancements/critereon/DamageSourcePredicate$Builder.direct:(Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;
        //   139: invokestatic    net/minecraft/advancements/critereon/KilledTrigger$TriggerInstance.playerKilledEntity:(Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;Lnet/minecraft/advancements/critereon/DamageSourcePredicate$Builder;)Lnet/minecraft/advancements/critereon/KilledTrigger$TriggerInstance;
        //   142: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   145: aload_1         /* consumer */
        //   146: ldc_w           "nether/return_to_sender"
        //   149: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   152: astore_3        /* y4 */
        //   153: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   156: aload_2         /* y3 */
        //   157: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   160: getstatic       net/minecraft/world/level/block/Blocks.NETHER_BRICKS:Lnet/minecraft/world/level/block/Block;
        //   163: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   166: dup            
        //   167: ldc_w           "advancements.nether.find_fortress.title"
        //   170: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   173: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   176: dup            
        //   177: ldc_w           "advancements.nether.find_fortress.description"
        //   180: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   183: aconst_null    
        //   184: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //   187: iconst_1       
        //   188: iconst_1       
        //   189: iconst_0       
        //   190: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   193: ldc_w           "fortress"
        //   196: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.NETHER_BRIDGE:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
        //   199: invokestatic    net/minecraft/advancements/critereon/LocationPredicate.inFeature:(Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Lnet/minecraft/advancements/critereon/LocationPredicate;
        //   202: invokestatic    net/minecraft/advancements/critereon/LocationTrigger$TriggerInstance.located:(Lnet/minecraft/advancements/critereon/LocationPredicate;)Lnet/minecraft/advancements/critereon/LocationTrigger$TriggerInstance;
        //   205: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   208: aload_1         /* consumer */
        //   209: ldc_w           "nether/find_fortress"
        //   212: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   215: astore          y5
        //   217: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   220: aload_2         /* y3 */
        //   221: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   224: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        //   227: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   230: dup            
        //   231: ldc_w           "advancements.nether.fast_travel.title"
        //   234: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   237: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   240: dup            
        //   241: ldc_w           "advancements.nether.fast_travel.description"
        //   244: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   247: aconst_null    
        //   248: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //   251: iconst_1       
        //   252: iconst_1       
        //   253: iconst_0       
        //   254: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   257: bipush          100
        //   259: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //   262: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //   265: ldc_w           "travelled"
        //   268: ldc_w           7000.0
        //   271: invokestatic    net/minecraft/advancements/critereon/MinMaxBounds$Floats.atLeast:(F)Lnet/minecraft/advancements/critereon/MinMaxBounds$Floats;
        //   274: invokestatic    net/minecraft/advancements/critereon/DistancePredicate.horizontal:(Lnet/minecraft/advancements/critereon/MinMaxBounds$Floats;)Lnet/minecraft/advancements/critereon/DistancePredicate;
        //   277: invokestatic    net/minecraft/advancements/critereon/NetherTravelTrigger$TriggerInstance.travelledThroughNether:(Lnet/minecraft/advancements/critereon/DistancePredicate;)Lnet/minecraft/advancements/critereon/NetherTravelTrigger$TriggerInstance;
        //   280: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   283: aload_1         /* consumer */
        //   284: ldc_w           "nether/fast_travel"
        //   287: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   290: pop            
        //   291: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   294: aload_3         /* y4 */
        //   295: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   298: getstatic       net/minecraft/world/item/Items.GHAST_TEAR:Lnet/minecraft/world/item/Item;
        //   301: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   304: dup            
        //   305: ldc_w           "advancements.nether.uneasy_alliance.title"
        //   308: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   311: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   314: dup            
        //   315: ldc_w           "advancements.nether.uneasy_alliance.description"
        //   318: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   321: aconst_null    
        //   322: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //   325: iconst_1       
        //   326: iconst_1       
        //   327: iconst_0       
        //   328: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   331: bipush          100
        //   333: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //   336: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //   339: ldc             "killed_ghast"
        //   341: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   344: getstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        //   347: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   350: getstatic       net/minecraft/world/level/Level.OVERWORLD:Lnet/minecraft/resources/ResourceKey;
        //   353: invokestatic    net/minecraft/advancements/critereon/LocationPredicate.inDimension:(Lnet/minecraft/resources/ResourceKey;)Lnet/minecraft/advancements/critereon/LocationPredicate;
        //   356: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.located:(Lnet/minecraft/advancements/critereon/LocationPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   359: invokestatic    net/minecraft/advancements/critereon/KilledTrigger$TriggerInstance.playerKilledEntity:(Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/advancements/critereon/KilledTrigger$TriggerInstance;
        //   362: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   365: aload_1         /* consumer */
        //   366: ldc_w           "nether/uneasy_alliance"
        //   369: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   372: pop            
        //   373: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   376: aload           y5
        //   378: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   381: getstatic       net/minecraft/world/level/block/Blocks.WITHER_SKELETON_SKULL:Lnet/minecraft/world/level/block/Block;
        //   384: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   387: dup            
        //   388: ldc_w           "advancements.nether.get_wither_skull.title"
        //   391: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   394: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   397: dup            
        //   398: ldc_w           "advancements.nether.get_wither_skull.description"
        //   401: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   404: aconst_null    
        //   405: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //   408: iconst_1       
        //   409: iconst_1       
        //   410: iconst_0       
        //   411: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   414: ldc_w           "wither_skull"
        //   417: iconst_1       
        //   418: anewarray       Lnet/minecraft/world/level/ItemLike;
        //   421: dup            
        //   422: iconst_0       
        //   423: getstatic       net/minecraft/world/level/block/Blocks.WITHER_SKELETON_SKULL:Lnet/minecraft/world/level/block/Block;
        //   426: aastore        
        //   427: invokestatic    net/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance.hasItems:([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance;
        //   430: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   433: aload_1         /* consumer */
        //   434: ldc_w           "nether/get_wither_skull"
        //   437: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   440: astore          y6
        //   442: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   445: aload           y6
        //   447: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   450: getstatic       net/minecraft/world/item/Items.NETHER_STAR:Lnet/minecraft/world/item/Item;
        //   453: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   456: dup            
        //   457: ldc_w           "advancements.nether.summon_wither.title"
        //   460: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   463: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   466: dup            
        //   467: ldc_w           "advancements.nether.summon_wither.description"
        //   470: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   473: aconst_null    
        //   474: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //   477: iconst_1       
        //   478: iconst_1       
        //   479: iconst_0       
        //   480: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   483: ldc_w           "summoned"
        //   486: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   489: getstatic       net/minecraft/world/entity/EntityType.WITHER:Lnet/minecraft/world/entity/EntityType;
        //   492: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //   495: invokestatic    net/minecraft/advancements/critereon/SummonedEntityTrigger$TriggerInstance.summonedEntity:(Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;)Lnet/minecraft/advancements/critereon/SummonedEntityTrigger$TriggerInstance;
        //   498: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   501: aload_1         /* consumer */
        //   502: ldc_w           "nether/summon_wither"
        //   505: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   508: astore          y7
        //   510: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   513: aload           y5
        //   515: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   518: getstatic       net/minecraft/world/item/Items.BLAZE_ROD:Lnet/minecraft/world/item/Item;
        //   521: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   524: dup            
        //   525: ldc_w           "advancements.nether.obtain_blaze_rod.title"
        //   528: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   531: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   534: dup            
        //   535: ldc_w           "advancements.nether.obtain_blaze_rod.description"
        //   538: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   541: aconst_null    
        //   542: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //   545: iconst_1       
        //   546: iconst_1       
        //   547: iconst_0       
        //   548: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   551: ldc_w           "blaze_rod"
        //   554: iconst_1       
        //   555: anewarray       Lnet/minecraft/world/level/ItemLike;
        //   558: dup            
        //   559: iconst_0       
        //   560: getstatic       net/minecraft/world/item/Items.BLAZE_ROD:Lnet/minecraft/world/item/Item;
        //   563: aastore        
        //   564: invokestatic    net/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance.hasItems:([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance;
        //   567: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   570: aload_1         /* consumer */
        //   571: ldc_w           "nether/obtain_blaze_rod"
        //   574: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   577: astore          y8
        //   579: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   582: aload           y7
        //   584: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   587: getstatic       net/minecraft/world/level/block/Blocks.BEACON:Lnet/minecraft/world/level/block/Block;
        //   590: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   593: dup            
        //   594: ldc_w           "advancements.nether.create_beacon.title"
        //   597: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   600: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   603: dup            
        //   604: ldc_w           "advancements.nether.create_beacon.description"
        //   607: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   610: aconst_null    
        //   611: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //   614: iconst_1       
        //   615: iconst_1       
        //   616: iconst_0       
        //   617: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   620: ldc_w           "beacon"
        //   623: iconst_1       
        //   624: invokestatic    net/minecraft/advancements/critereon/MinMaxBounds$Ints.atLeast:(I)Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;
        //   627: invokestatic    net/minecraft/advancements/critereon/ConstructBeaconTrigger$TriggerInstance.constructedBeacon:(Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;)Lnet/minecraft/advancements/critereon/ConstructBeaconTrigger$TriggerInstance;
        //   630: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   633: aload_1         /* consumer */
        //   634: ldc_w           "nether/create_beacon"
        //   637: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   640: astore          y9
        //   642: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   645: aload           y9
        //   647: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   650: getstatic       net/minecraft/world/level/block/Blocks.BEACON:Lnet/minecraft/world/level/block/Block;
        //   653: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   656: dup            
        //   657: ldc_w           "advancements.nether.create_full_beacon.title"
        //   660: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   663: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   666: dup            
        //   667: ldc_w           "advancements.nether.create_full_beacon.description"
        //   670: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   673: aconst_null    
        //   674: getstatic       net/minecraft/advancements/FrameType.GOAL:Lnet/minecraft/advancements/FrameType;
        //   677: iconst_1       
        //   678: iconst_1       
        //   679: iconst_0       
        //   680: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   683: ldc_w           "beacon"
        //   686: iconst_4       
        //   687: invokestatic    net/minecraft/advancements/critereon/MinMaxBounds$Ints.exactly:(I)Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;
        //   690: invokestatic    net/minecraft/advancements/critereon/ConstructBeaconTrigger$TriggerInstance.constructedBeacon:(Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;)Lnet/minecraft/advancements/critereon/ConstructBeaconTrigger$TriggerInstance;
        //   693: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   696: aload_1         /* consumer */
        //   697: ldc_w           "nether/create_full_beacon"
        //   700: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   703: pop            
        //   704: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   707: aload           y8
        //   709: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   712: getstatic       net/minecraft/world/item/Items.POTION:Lnet/minecraft/world/item/Item;
        //   715: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   718: dup            
        //   719: ldc_w           "advancements.nether.brew_potion.title"
        //   722: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   725: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   728: dup            
        //   729: ldc_w           "advancements.nether.brew_potion.description"
        //   732: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   735: aconst_null    
        //   736: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //   739: iconst_1       
        //   740: iconst_1       
        //   741: iconst_0       
        //   742: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   745: ldc_w           "potion"
        //   748: invokestatic    net/minecraft/advancements/critereon/BrewedPotionTrigger$TriggerInstance.brewedPotion:()Lnet/minecraft/advancements/critereon/BrewedPotionTrigger$TriggerInstance;
        //   751: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   754: aload_1         /* consumer */
        //   755: ldc_w           "nether/brew_potion"
        //   758: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   761: astore          y10
        //   763: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   766: aload           y10
        //   768: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   771: getstatic       net/minecraft/world/item/Items.MILK_BUCKET:Lnet/minecraft/world/item/Item;
        //   774: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   777: dup            
        //   778: ldc_w           "advancements.nether.all_potions.title"
        //   781: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   784: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   787: dup            
        //   788: ldc_w           "advancements.nether.all_potions.description"
        //   791: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   794: aconst_null    
        //   795: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //   798: iconst_1       
        //   799: iconst_1       
        //   800: iconst_0       
        //   801: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   804: bipush          100
        //   806: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //   809: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //   812: ldc_w           "all_effects"
        //   815: invokestatic    net/minecraft/advancements/critereon/MobEffectsPredicate.effects:()Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   818: getstatic       net/minecraft/world/effect/MobEffects.MOVEMENT_SPEED:Lnet/minecraft/world/effect/MobEffect;
        //   821: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   824: getstatic       net/minecraft/world/effect/MobEffects.MOVEMENT_SLOWDOWN:Lnet/minecraft/world/effect/MobEffect;
        //   827: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   830: getstatic       net/minecraft/world/effect/MobEffects.DAMAGE_BOOST:Lnet/minecraft/world/effect/MobEffect;
        //   833: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   836: getstatic       net/minecraft/world/effect/MobEffects.JUMP:Lnet/minecraft/world/effect/MobEffect;
        //   839: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   842: getstatic       net/minecraft/world/effect/MobEffects.REGENERATION:Lnet/minecraft/world/effect/MobEffect;
        //   845: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   848: getstatic       net/minecraft/world/effect/MobEffects.FIRE_RESISTANCE:Lnet/minecraft/world/effect/MobEffect;
        //   851: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   854: getstatic       net/minecraft/world/effect/MobEffects.WATER_BREATHING:Lnet/minecraft/world/effect/MobEffect;
        //   857: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   860: getstatic       net/minecraft/world/effect/MobEffects.INVISIBILITY:Lnet/minecraft/world/effect/MobEffect;
        //   863: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   866: getstatic       net/minecraft/world/effect/MobEffects.NIGHT_VISION:Lnet/minecraft/world/effect/MobEffect;
        //   869: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   872: getstatic       net/minecraft/world/effect/MobEffects.WEAKNESS:Lnet/minecraft/world/effect/MobEffect;
        //   875: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   878: getstatic       net/minecraft/world/effect/MobEffects.POISON:Lnet/minecraft/world/effect/MobEffect;
        //   881: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   884: getstatic       net/minecraft/world/effect/MobEffects.SLOW_FALLING:Lnet/minecraft/world/effect/MobEffect;
        //   887: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   890: getstatic       net/minecraft/world/effect/MobEffects.DAMAGE_RESISTANCE:Lnet/minecraft/world/effect/MobEffect;
        //   893: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   896: invokestatic    net/minecraft/advancements/critereon/EffectsChangedTrigger$TriggerInstance.hasEffects:(Lnet/minecraft/advancements/critereon/MobEffectsPredicate;)Lnet/minecraft/advancements/critereon/EffectsChangedTrigger$TriggerInstance;
        //   899: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //   902: aload_1         /* consumer */
        //   903: ldc_w           "nether/all_potions"
        //   906: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //   909: astore          y11
        //   911: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //   914: aload           y11
        //   916: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //   919: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        //   922: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   925: dup            
        //   926: ldc_w           "advancements.nether.all_effects.title"
        //   929: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   932: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //   935: dup            
        //   936: ldc_w           "advancements.nether.all_effects.description"
        //   939: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //   942: aconst_null    
        //   943: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //   946: iconst_1       
        //   947: iconst_1       
        //   948: iconst_1       
        //   949: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //   952: sipush          1000
        //   955: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //   958: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //   961: ldc_w           "all_effects"
        //   964: invokestatic    net/minecraft/advancements/critereon/MobEffectsPredicate.effects:()Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   967: getstatic       net/minecraft/world/effect/MobEffects.MOVEMENT_SPEED:Lnet/minecraft/world/effect/MobEffect;
        //   970: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   973: getstatic       net/minecraft/world/effect/MobEffects.MOVEMENT_SLOWDOWN:Lnet/minecraft/world/effect/MobEffect;
        //   976: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   979: getstatic       net/minecraft/world/effect/MobEffects.DAMAGE_BOOST:Lnet/minecraft/world/effect/MobEffect;
        //   982: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   985: getstatic       net/minecraft/world/effect/MobEffects.JUMP:Lnet/minecraft/world/effect/MobEffect;
        //   988: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   991: getstatic       net/minecraft/world/effect/MobEffects.REGENERATION:Lnet/minecraft/world/effect/MobEffect;
        //   994: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //   997: getstatic       net/minecraft/world/effect/MobEffects.FIRE_RESISTANCE:Lnet/minecraft/world/effect/MobEffect;
        //  1000: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1003: getstatic       net/minecraft/world/effect/MobEffects.WATER_BREATHING:Lnet/minecraft/world/effect/MobEffect;
        //  1006: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1009: getstatic       net/minecraft/world/effect/MobEffects.INVISIBILITY:Lnet/minecraft/world/effect/MobEffect;
        //  1012: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1015: getstatic       net/minecraft/world/effect/MobEffects.NIGHT_VISION:Lnet/minecraft/world/effect/MobEffect;
        //  1018: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1021: getstatic       net/minecraft/world/effect/MobEffects.WEAKNESS:Lnet/minecraft/world/effect/MobEffect;
        //  1024: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1027: getstatic       net/minecraft/world/effect/MobEffects.POISON:Lnet/minecraft/world/effect/MobEffect;
        //  1030: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1033: getstatic       net/minecraft/world/effect/MobEffects.WITHER:Lnet/minecraft/world/effect/MobEffect;
        //  1036: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1039: getstatic       net/minecraft/world/effect/MobEffects.DIG_SPEED:Lnet/minecraft/world/effect/MobEffect;
        //  1042: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1045: getstatic       net/minecraft/world/effect/MobEffects.DIG_SLOWDOWN:Lnet/minecraft/world/effect/MobEffect;
        //  1048: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1051: getstatic       net/minecraft/world/effect/MobEffects.LEVITATION:Lnet/minecraft/world/effect/MobEffect;
        //  1054: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1057: getstatic       net/minecraft/world/effect/MobEffects.GLOWING:Lnet/minecraft/world/effect/MobEffect;
        //  1060: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1063: getstatic       net/minecraft/world/effect/MobEffects.ABSORPTION:Lnet/minecraft/world/effect/MobEffect;
        //  1066: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1069: getstatic       net/minecraft/world/effect/MobEffects.HUNGER:Lnet/minecraft/world/effect/MobEffect;
        //  1072: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1075: getstatic       net/minecraft/world/effect/MobEffects.CONFUSION:Lnet/minecraft/world/effect/MobEffect;
        //  1078: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1081: getstatic       net/minecraft/world/effect/MobEffects.DAMAGE_RESISTANCE:Lnet/minecraft/world/effect/MobEffect;
        //  1084: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1087: getstatic       net/minecraft/world/effect/MobEffects.SLOW_FALLING:Lnet/minecraft/world/effect/MobEffect;
        //  1090: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1093: getstatic       net/minecraft/world/effect/MobEffects.CONDUIT_POWER:Lnet/minecraft/world/effect/MobEffect;
        //  1096: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1099: getstatic       net/minecraft/world/effect/MobEffects.DOLPHINS_GRACE:Lnet/minecraft/world/effect/MobEffect;
        //  1102: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1105: getstatic       net/minecraft/world/effect/MobEffects.BLINDNESS:Lnet/minecraft/world/effect/MobEffect;
        //  1108: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1111: getstatic       net/minecraft/world/effect/MobEffects.BAD_OMEN:Lnet/minecraft/world/effect/MobEffect;
        //  1114: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1117: getstatic       net/minecraft/world/effect/MobEffects.HERO_OF_THE_VILLAGE:Lnet/minecraft/world/effect/MobEffect;
        //  1120: invokevirtual   net/minecraft/advancements/critereon/MobEffectsPredicate.and:(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/advancements/critereon/MobEffectsPredicate;
        //  1123: invokestatic    net/minecraft/advancements/critereon/EffectsChangedTrigger$TriggerInstance.hasEffects:(Lnet/minecraft/advancements/critereon/MobEffectsPredicate;)Lnet/minecraft/advancements/critereon/EffectsChangedTrigger$TriggerInstance;
        //  1126: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1129: aload_1         /* consumer */
        //  1130: ldc_w           "nether/all_effects"
        //  1133: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1136: pop            
        //  1137: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1140: aload_2         /* y3 */
        //  1141: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1144: getstatic       net/minecraft/world/item/Items.ANCIENT_DEBRIS:Lnet/minecraft/world/item/Item;
        //  1147: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1150: dup            
        //  1151: ldc_w           "advancements.nether.obtain_ancient_debris.title"
        //  1154: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1157: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1160: dup            
        //  1161: ldc_w           "advancements.nether.obtain_ancient_debris.description"
        //  1164: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1167: aconst_null    
        //  1168: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1171: iconst_1       
        //  1172: iconst_1       
        //  1173: iconst_0       
        //  1174: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1177: ldc_w           "ancient_debris"
        //  1180: iconst_1       
        //  1181: anewarray       Lnet/minecraft/world/level/ItemLike;
        //  1184: dup            
        //  1185: iconst_0       
        //  1186: getstatic       net/minecraft/world/item/Items.ANCIENT_DEBRIS:Lnet/minecraft/world/item/Item;
        //  1189: aastore        
        //  1190: invokestatic    net/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance.hasItems:([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance;
        //  1193: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1196: aload_1         /* consumer */
        //  1197: ldc_w           "nether/obtain_ancient_debris"
        //  1200: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1203: astore          y12
        //  1205: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1208: aload           y12
        //  1210: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1213: getstatic       net/minecraft/world/item/Items.NETHERITE_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  1216: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1219: dup            
        //  1220: ldc_w           "advancements.nether.netherite_armor.title"
        //  1223: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1226: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1229: dup            
        //  1230: ldc_w           "advancements.nether.netherite_armor.description"
        //  1233: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1236: aconst_null    
        //  1237: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //  1240: iconst_1       
        //  1241: iconst_1       
        //  1242: iconst_0       
        //  1243: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1246: bipush          100
        //  1248: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //  1251: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1254: ldc_w           "netherite_armor"
        //  1257: iconst_4       
        //  1258: anewarray       Lnet/minecraft/world/level/ItemLike;
        //  1261: dup            
        //  1262: iconst_0       
        //  1263: getstatic       net/minecraft/world/item/Items.NETHERITE_HELMET:Lnet/minecraft/world/item/Item;
        //  1266: aastore        
        //  1267: dup            
        //  1268: iconst_1       
        //  1269: getstatic       net/minecraft/world/item/Items.NETHERITE_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  1272: aastore        
        //  1273: dup            
        //  1274: iconst_2       
        //  1275: getstatic       net/minecraft/world/item/Items.NETHERITE_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  1278: aastore        
        //  1279: dup            
        //  1280: iconst_3       
        //  1281: getstatic       net/minecraft/world/item/Items.NETHERITE_BOOTS:Lnet/minecraft/world/item/Item;
        //  1284: aastore        
        //  1285: invokestatic    net/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance.hasItems:([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance;
        //  1288: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1291: aload_1         /* consumer */
        //  1292: ldc_w           "nether/netherite_armor"
        //  1295: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1298: pop            
        //  1299: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1302: aload           y12
        //  1304: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1307: getstatic       net/minecraft/world/item/Items.LODESTONE:Lnet/minecraft/world/item/Item;
        //  1310: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1313: dup            
        //  1314: ldc_w           "advancements.nether.use_lodestone.title"
        //  1317: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1320: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1323: dup            
        //  1324: ldc_w           "advancements.nether.use_lodestone.description"
        //  1327: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1330: aconst_null    
        //  1331: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1334: iconst_1       
        //  1335: iconst_1       
        //  1336: iconst_0       
        //  1337: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1340: ldc_w           "use_lodestone"
        //  1343: invokestatic    net/minecraft/advancements/critereon/LocationPredicate$Builder.location:()Lnet/minecraft/advancements/critereon/LocationPredicate$Builder;
        //  1346: invokestatic    net/minecraft/advancements/critereon/BlockPredicate$Builder.block:()Lnet/minecraft/advancements/critereon/BlockPredicate$Builder;
        //  1349: getstatic       net/minecraft/world/level/block/Blocks.LODESTONE:Lnet/minecraft/world/level/block/Block;
        //  1352: invokevirtual   net/minecraft/advancements/critereon/BlockPredicate$Builder.of:(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/advancements/critereon/BlockPredicate$Builder;
        //  1355: invokevirtual   net/minecraft/advancements/critereon/BlockPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/BlockPredicate;
        //  1358: invokevirtual   net/minecraft/advancements/critereon/LocationPredicate$Builder.setBlock:(Lnet/minecraft/advancements/critereon/BlockPredicate;)Lnet/minecraft/advancements/critereon/LocationPredicate$Builder;
        //  1361: invokestatic    net/minecraft/advancements/critereon/ItemPredicate$Builder.item:()Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1364: getstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
        //  1367: invokevirtual   net/minecraft/advancements/critereon/ItemPredicate$Builder.of:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1370: invokestatic    net/minecraft/advancements/critereon/ItemUsedOnBlockTrigger$TriggerInstance.itemUsedOnBlock:(Lnet/minecraft/advancements/critereon/LocationPredicate$Builder;Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;)Lnet/minecraft/advancements/critereon/ItemUsedOnBlockTrigger$TriggerInstance;
        //  1373: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1376: aload_1         /* consumer */
        //  1377: ldc_w           "nether/use_lodestone"
        //  1380: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1383: pop            
        //  1384: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1387: aload_2         /* y3 */
        //  1388: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1391: getstatic       net/minecraft/world/item/Items.CRYING_OBSIDIAN:Lnet/minecraft/world/item/Item;
        //  1394: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1397: dup            
        //  1398: ldc_w           "advancements.nether.obtain_crying_obsidian.title"
        //  1401: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1404: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1407: dup            
        //  1408: ldc_w           "advancements.nether.obtain_crying_obsidian.description"
        //  1411: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1414: aconst_null    
        //  1415: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1418: iconst_1       
        //  1419: iconst_1       
        //  1420: iconst_0       
        //  1421: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1424: ldc_w           "crying_obsidian"
        //  1427: iconst_1       
        //  1428: anewarray       Lnet/minecraft/world/level/ItemLike;
        //  1431: dup            
        //  1432: iconst_0       
        //  1433: getstatic       net/minecraft/world/item/Items.CRYING_OBSIDIAN:Lnet/minecraft/world/item/Item;
        //  1436: aastore        
        //  1437: invokestatic    net/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance.hasItems:([Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/InventoryChangeTrigger$TriggerInstance;
        //  1440: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1443: aload_1         /* consumer */
        //  1444: ldc_w           "nether/obtain_crying_obsidian"
        //  1447: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1450: astore          y13
        //  1452: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1455: aload           y13
        //  1457: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1460: getstatic       net/minecraft/world/item/Items.RESPAWN_ANCHOR:Lnet/minecraft/world/item/Item;
        //  1463: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1466: dup            
        //  1467: ldc_w           "advancements.nether.charge_respawn_anchor.title"
        //  1470: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1473: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1476: dup            
        //  1477: ldc_w           "advancements.nether.charge_respawn_anchor.description"
        //  1480: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1483: aconst_null    
        //  1484: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1487: iconst_1       
        //  1488: iconst_1       
        //  1489: iconst_0       
        //  1490: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1493: ldc_w           "charge_respawn_anchor"
        //  1496: invokestatic    net/minecraft/advancements/critereon/LocationPredicate$Builder.location:()Lnet/minecraft/advancements/critereon/LocationPredicate$Builder;
        //  1499: invokestatic    net/minecraft/advancements/critereon/BlockPredicate$Builder.block:()Lnet/minecraft/advancements/critereon/BlockPredicate$Builder;
        //  1502: getstatic       net/minecraft/world/level/block/Blocks.RESPAWN_ANCHOR:Lnet/minecraft/world/level/block/Block;
        //  1505: invokevirtual   net/minecraft/advancements/critereon/BlockPredicate$Builder.of:(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/advancements/critereon/BlockPredicate$Builder;
        //  1508: invokestatic    net/minecraft/advancements/critereon/StatePropertiesPredicate$Builder.properties:()Lnet/minecraft/advancements/critereon/StatePropertiesPredicate$Builder;
        //  1511: getstatic       net/minecraft/world/level/block/RespawnAnchorBlock.CHARGE:Lnet/minecraft/world/level/block/state/properties/IntegerProperty;
        //  1514: iconst_4       
        //  1515: invokevirtual   net/minecraft/advancements/critereon/StatePropertiesPredicate$Builder.hasProperty:(Lnet/minecraft/world/level/block/state/properties/Property;I)Lnet/minecraft/advancements/critereon/StatePropertiesPredicate$Builder;
        //  1518: invokevirtual   net/minecraft/advancements/critereon/StatePropertiesPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/StatePropertiesPredicate;
        //  1521: invokevirtual   net/minecraft/advancements/critereon/BlockPredicate$Builder.setProperties:(Lnet/minecraft/advancements/critereon/StatePropertiesPredicate;)Lnet/minecraft/advancements/critereon/BlockPredicate$Builder;
        //  1524: invokevirtual   net/minecraft/advancements/critereon/BlockPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/BlockPredicate;
        //  1527: invokevirtual   net/minecraft/advancements/critereon/LocationPredicate$Builder.setBlock:(Lnet/minecraft/advancements/critereon/BlockPredicate;)Lnet/minecraft/advancements/critereon/LocationPredicate$Builder;
        //  1530: invokestatic    net/minecraft/advancements/critereon/ItemPredicate$Builder.item:()Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1533: getstatic       net/minecraft/world/level/block/Blocks.GLOWSTONE:Lnet/minecraft/world/level/block/Block;
        //  1536: invokevirtual   net/minecraft/advancements/critereon/ItemPredicate$Builder.of:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1539: invokestatic    net/minecraft/advancements/critereon/ItemUsedOnBlockTrigger$TriggerInstance.itemUsedOnBlock:(Lnet/minecraft/advancements/critereon/LocationPredicate$Builder;Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;)Lnet/minecraft/advancements/critereon/ItemUsedOnBlockTrigger$TriggerInstance;
        //  1542: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1545: aload_1         /* consumer */
        //  1546: ldc_w           "nether/charge_respawn_anchor"
        //  1549: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1552: pop            
        //  1553: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1556: aload_2         /* y3 */
        //  1557: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1560: getstatic       net/minecraft/world/item/Items.WARPED_FUNGUS_ON_A_STICK:Lnet/minecraft/world/item/Item;
        //  1563: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1566: dup            
        //  1567: ldc_w           "advancements.nether.ride_strider.title"
        //  1570: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1573: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1576: dup            
        //  1577: ldc_w           "advancements.nether.ride_strider.description"
        //  1580: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1583: aconst_null    
        //  1584: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1587: iconst_1       
        //  1588: iconst_1       
        //  1589: iconst_0       
        //  1590: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1593: ldc_w           "used_warped_fungus_on_a_stick"
        //  1596: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1599: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1602: getstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        //  1605: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1608: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/EntityPredicate;
        //  1611: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.vehicle:(Lnet/minecraft/advancements/critereon/EntityPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1614: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/EntityPredicate;
        //  1617: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Composite.wrap:(Lnet/minecraft/advancements/critereon/EntityPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;
        //  1620: invokestatic    net/minecraft/advancements/critereon/ItemPredicate$Builder.item:()Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1623: getstatic       net/minecraft/world/item/Items.WARPED_FUNGUS_ON_A_STICK:Lnet/minecraft/world/item/Item;
        //  1626: invokevirtual   net/minecraft/advancements/critereon/ItemPredicate$Builder.of:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1629: invokevirtual   net/minecraft/advancements/critereon/ItemPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/ItemPredicate;
        //  1632: getstatic       net/minecraft/advancements/critereon/MinMaxBounds$Ints.ANY:Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;
        //  1635: invokestatic    net/minecraft/advancements/critereon/ItemDurabilityTrigger$TriggerInstance.changedDurability:(Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;Lnet/minecraft/advancements/critereon/ItemPredicate;Lnet/minecraft/advancements/critereon/MinMaxBounds$Ints;)Lnet/minecraft/advancements/critereon/ItemDurabilityTrigger$TriggerInstance;
        //  1638: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1641: aload_1         /* consumer */
        //  1642: ldc_w           "nether/ride_strider"
        //  1645: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1648: astore          y14
        //  1650: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1653: getstatic       net/minecraft/data/advancements/NetherAdvancements.EXPLORABLE_BIOMES:Ljava/util/List;
        //  1656: invokestatic    net/minecraft/data/advancements/AdventureAdvancements.addBiomes:(Lnet/minecraft/advancements/Advancement$Builder;Ljava/util/List;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1659: aload           y14
        //  1661: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1664: getstatic       net/minecraft/world/item/Items.NETHERITE_BOOTS:Lnet/minecraft/world/item/Item;
        //  1667: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1670: dup            
        //  1671: ldc_w           "advancements.nether.explore_nether.title"
        //  1674: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1677: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1680: dup            
        //  1681: ldc_w           "advancements.nether.explore_nether.description"
        //  1684: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1687: aconst_null    
        //  1688: getstatic       net/minecraft/advancements/FrameType.CHALLENGE:Lnet/minecraft/advancements/FrameType;
        //  1691: iconst_1       
        //  1692: iconst_1       
        //  1693: iconst_0       
        //  1694: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1697: sipush          500
        //  1700: invokestatic    net/minecraft/advancements/AdvancementRewards$Builder.experience:(I)Lnet/minecraft/advancements/AdvancementRewards$Builder;
        //  1703: invokevirtual   net/minecraft/advancements/Advancement$Builder.rewards:(Lnet/minecraft/advancements/AdvancementRewards$Builder;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1706: aload_1         /* consumer */
        //  1707: ldc_w           "nether/explore_nether"
        //  1710: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1713: pop            
        //  1714: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1717: aload_2         /* y3 */
        //  1718: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1721: getstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  1724: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1727: dup            
        //  1728: ldc_w           "advancements.nether.find_bastion.title"
        //  1731: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1734: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1737: dup            
        //  1738: ldc_w           "advancements.nether.find_bastion.description"
        //  1741: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1744: aconst_null    
        //  1745: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1748: iconst_1       
        //  1749: iconst_1       
        //  1750: iconst_0       
        //  1751: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1754: ldc_w           "bastion"
        //  1757: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.BASTION_REMNANT:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
        //  1760: invokestatic    net/minecraft/advancements/critereon/LocationPredicate.inFeature:(Lnet/minecraft/world/level/levelgen/feature/StructureFeature;)Lnet/minecraft/advancements/critereon/LocationPredicate;
        //  1763: invokestatic    net/minecraft/advancements/critereon/LocationTrigger$TriggerInstance.located:(Lnet/minecraft/advancements/critereon/LocationPredicate;)Lnet/minecraft/advancements/critereon/LocationTrigger$TriggerInstance;
        //  1766: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1769: aload_1         /* consumer */
        //  1770: ldc_w           "nether/find_bastion"
        //  1773: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1776: astore          y15
        //  1778: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1781: aload           y15
        //  1783: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1786: getstatic       net/minecraft/world/level/block/Blocks.CHEST:Lnet/minecraft/world/level/block/Block;
        //  1789: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1792: dup            
        //  1793: ldc_w           "advancements.nether.loot_bastion.title"
        //  1796: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1799: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1802: dup            
        //  1803: ldc_w           "advancements.nether.loot_bastion.description"
        //  1806: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1809: aconst_null    
        //  1810: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1813: iconst_1       
        //  1814: iconst_1       
        //  1815: iconst_0       
        //  1816: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1819: getstatic       net/minecraft/advancements/RequirementsStrategy.OR:Lnet/minecraft/advancements/RequirementsStrategy;
        //  1822: invokevirtual   net/minecraft/advancements/Advancement$Builder.requirements:(Lnet/minecraft/advancements/RequirementsStrategy;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1825: ldc_w           "loot_bastion_other"
        //  1828: new             Lnet/minecraft/resources/ResourceLocation;
        //  1831: dup            
        //  1832: ldc_w           "minecraft:chests/bastion_other"
        //  1835: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1838: invokestatic    net/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance.lootTableUsed:(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance;
        //  1841: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1844: ldc_w           "loot_bastion_treasure"
        //  1847: new             Lnet/minecraft/resources/ResourceLocation;
        //  1850: dup            
        //  1851: ldc_w           "minecraft:chests/bastion_treasure"
        //  1854: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1857: invokestatic    net/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance.lootTableUsed:(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance;
        //  1860: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1863: ldc_w           "loot_bastion_hoglin_stable"
        //  1866: new             Lnet/minecraft/resources/ResourceLocation;
        //  1869: dup            
        //  1870: ldc_w           "minecraft:chests/bastion_hoglin_stable"
        //  1873: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1876: invokestatic    net/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance.lootTableUsed:(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance;
        //  1879: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1882: ldc_w           "loot_bastion_bridge"
        //  1885: new             Lnet/minecraft/resources/ResourceLocation;
        //  1888: dup            
        //  1889: ldc_w           "minecraft:chests/bastion_bridge"
        //  1892: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1895: invokestatic    net/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance.lootTableUsed:(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/advancements/critereon/LootTableTrigger$TriggerInstance;
        //  1898: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1901: aload_1         /* consumer */
        //  1902: ldc_w           "nether/loot_bastion"
        //  1905: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  1908: pop            
        //  1909: invokestatic    net/minecraft/advancements/Advancement$Builder.advancement:()Lnet/minecraft/advancements/Advancement$Builder;
        //  1912: aload_2         /* y3 */
        //  1913: invokevirtual   net/minecraft/advancements/Advancement$Builder.parent:(Lnet/minecraft/advancements/Advancement;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1916: getstatic       net/minecraft/advancements/RequirementsStrategy.OR:Lnet/minecraft/advancements/RequirementsStrategy;
        //  1919: invokevirtual   net/minecraft/advancements/Advancement$Builder.requirements:(Lnet/minecraft/advancements/RequirementsStrategy;)Lnet/minecraft/advancements/Advancement$Builder;
        //  1922: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  1925: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1928: dup            
        //  1929: ldc_w           "advancements.nether.distract_piglin.title"
        //  1932: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1935: new             Lnet/minecraft/network/chat/TranslatableComponent;
        //  1938: dup            
        //  1939: ldc_w           "advancements.nether.distract_piglin.description"
        //  1942: invokespecial   net/minecraft/network/chat/TranslatableComponent.<init>:(Ljava/lang/String;)V
        //  1945: aconst_null    
        //  1946: getstatic       net/minecraft/advancements/FrameType.TASK:Lnet/minecraft/advancements/FrameType;
        //  1949: iconst_1       
        //  1950: iconst_1       
        //  1951: iconst_0       
        //  1952: invokevirtual   net/minecraft/advancements/Advancement$Builder.display:(Lnet/minecraft/world/level/ItemLike;Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/advancements/FrameType;ZZZ)Lnet/minecraft/advancements/Advancement$Builder;
        //  1955: ldc_w           "distract_piglin"
        //  1958: getstatic       net/minecraft/data/advancements/NetherAdvancements.DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE:Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;
        //  1961: invokestatic    net/minecraft/advancements/critereon/ItemPredicate$Builder.item:()Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1964: getstatic       net/minecraft/tags/ItemTags.PIGLIN_LOVED:Lnet/minecraft/tags/Tag$Named;
        //  1967: invokevirtual   net/minecraft/advancements/critereon/ItemPredicate$Builder.of:(Lnet/minecraft/tags/Tag;)Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  1970: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1973: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //  1976: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1979: invokestatic    net/minecraft/advancements/critereon/EntityFlagsPredicate$Builder.flags:()Lnet/minecraft/advancements/critereon/EntityFlagsPredicate$Builder;
        //  1982: iconst_0       
        //  1983: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  1986: invokevirtual   net/minecraft/advancements/critereon/EntityFlagsPredicate$Builder.setIsBaby:(Ljava/lang/Boolean;)Lnet/minecraft/advancements/critereon/EntityFlagsPredicate$Builder;
        //  1989: invokevirtual   net/minecraft/advancements/critereon/EntityFlagsPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/EntityFlagsPredicate;
        //  1992: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.flags:(Lnet/minecraft/advancements/critereon/EntityFlagsPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  1995: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/EntityPredicate;
        //  1998: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Composite.wrap:(Lnet/minecraft/advancements/critereon/EntityPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;
        //  2001: invokestatic    net/minecraft/advancements/critereon/ItemPickedUpByEntityTrigger$TriggerInstance.itemPickedUpByEntity:(Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;)Lnet/minecraft/advancements/critereon/ItemPickedUpByEntityTrigger$TriggerInstance;
        //  2004: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  2007: ldc_w           "distract_piglin_directly"
        //  2010: getstatic       net/minecraft/data/advancements/NetherAdvancements.DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE:Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;
        //  2013: invokestatic    net/minecraft/advancements/critereon/ItemPredicate$Builder.item:()Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  2016: getstatic       net/minecraft/world/entity/monster/piglin/PiglinAi.BARTERING_ITEM:Lnet/minecraft/world/item/Item;
        //  2019: invokevirtual   net/minecraft/advancements/critereon/ItemPredicate$Builder.of:(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;
        //  2022: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Builder.entity:()Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2025: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        //  2028: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.of:(Lnet/minecraft/world/entity/EntityType;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2031: invokestatic    net/minecraft/advancements/critereon/EntityFlagsPredicate$Builder.flags:()Lnet/minecraft/advancements/critereon/EntityFlagsPredicate$Builder;
        //  2034: iconst_0       
        //  2035: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  2038: invokevirtual   net/minecraft/advancements/critereon/EntityFlagsPredicate$Builder.setIsBaby:(Ljava/lang/Boolean;)Lnet/minecraft/advancements/critereon/EntityFlagsPredicate$Builder;
        //  2041: invokevirtual   net/minecraft/advancements/critereon/EntityFlagsPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/EntityFlagsPredicate;
        //  2044: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.flags:(Lnet/minecraft/advancements/critereon/EntityFlagsPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Builder;
        //  2047: invokevirtual   net/minecraft/advancements/critereon/EntityPredicate$Builder.build:()Lnet/minecraft/advancements/critereon/EntityPredicate;
        //  2050: invokestatic    net/minecraft/advancements/critereon/EntityPredicate$Composite.wrap:(Lnet/minecraft/advancements/critereon/EntityPredicate;)Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;
        //  2053: invokestatic    net/minecraft/advancements/critereon/PlayerInteractTrigger$TriggerInstance.itemUsedOnEntity:(Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;Lnet/minecraft/advancements/critereon/ItemPredicate$Builder;Lnet/minecraft/advancements/critereon/EntityPredicate$Composite;)Lnet/minecraft/advancements/critereon/PlayerInteractTrigger$TriggerInstance;
        //  2056: invokevirtual   net/minecraft/advancements/Advancement$Builder.addCriterion:(Ljava/lang/String;Lnet/minecraft/advancements/CriterionTriggerInstance;)Lnet/minecraft/advancements/Advancement$Builder;
        //  2059: aload_1         /* consumer */
        //  2060: ldc_w           "nether/distract_piglin"
        //  2063: invokevirtual   net/minecraft/advancements/Advancement$Builder.save:(Ljava/util/function/Consumer;Ljava/lang/String;)Lnet/minecraft/advancements/Advancement;
        //  2066: pop            
        //  2067: return         
        //    Signature:
        //  (Ljava/util/function/Consumer<Lnet/minecraft/advancements/Advancement;>;)V
        //    MethodParameters:
        //  Name      Flags  
        //  --------  -----
        //  consumer  
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:91)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2470)
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
    
    static {
        EXPLORABLE_BIOMES = (List)ImmutableList.<ResourceKey<Biome>>of(Biomes.NETHER_WASTES, Biomes.SOUL_SAND_VALLEY, Biomes.WARPED_FOREST, Biomes.CRIMSON_FOREST, Biomes.BASALT_DELTAS);
        DISTRACT_PIGLIN_PLAYER_ARMOR_PREDICATE = EntityPredicate.Composite.create(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().head(ItemPredicate.Builder.item().of(Items.GOLDEN_HELMET).build()).build())).invert().build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().chest(ItemPredicate.Builder.item().of(Items.GOLDEN_CHESTPLATE).build()).build())).invert().build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().legs(ItemPredicate.Builder.item().of(Items.GOLDEN_LEGGINGS).build()).build())).invert().build(), LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of(Items.GOLDEN_BOOTS).build()).build())).invert().build());
    }
}
