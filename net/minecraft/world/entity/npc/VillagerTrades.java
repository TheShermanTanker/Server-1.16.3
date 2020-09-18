package net.minecraft.world.entity.npc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import java.util.Locale;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.MapItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.DyeableLeatherItem;
import com.google.common.collect.Lists;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potion;
import java.util.stream.Collectors;
import java.util.List;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.function.Consumer;
import net.minecraft.Util;
import com.google.common.collect.Maps;
import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Map;

public class VillagerTrades {
    public static final Map<VillagerProfession, Int2ObjectMap<ItemListing[]>> TRADES;
    public static final Int2ObjectMap<ItemListing[]> WANDERING_TRADER_TRADES;
    
    private static Int2ObjectMap<ItemListing[]> toIntMap(final ImmutableMap<Integer, ItemListing[]> immutableMap) {
        return new Int2ObjectOpenHashMap<ItemListing[]>((java.util.Map<? extends Integer, ? extends ItemListing[]>)immutableMap);
    }
    
    static {
        TRADES = Util.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getstatic       net/minecraft/world/entity/npc/VillagerProfession.FARMER:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //     4: iconst_1       
            //     5: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //     8: iconst_5       
            //     9: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //    12: dup            
            //    13: iconst_0       
            //    14: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //    17: dup            
            //    18: getstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
            //    21: bipush          20
            //    23: bipush          16
            //    25: iconst_2       
            //    26: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //    29: aastore        
            //    30: dup            
            //    31: iconst_1       
            //    32: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //    35: dup            
            //    36: getstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
            //    39: bipush          26
            //    41: bipush          16
            //    43: iconst_2       
            //    44: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //    47: aastore        
            //    48: dup            
            //    49: iconst_2       
            //    50: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //    53: dup            
            //    54: getstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
            //    57: bipush          22
            //    59: bipush          16
            //    61: iconst_2       
            //    62: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //    65: aastore        
            //    66: dup            
            //    67: iconst_3       
            //    68: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //    71: dup            
            //    72: getstatic       net/minecraft/world/item/Items.BEETROOT:Lnet/minecraft/world/item/Item;
            //    75: bipush          15
            //    77: bipush          16
            //    79: iconst_2       
            //    80: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //    83: aastore        
            //    84: dup            
            //    85: iconst_4       
            //    86: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //    89: dup            
            //    90: getstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
            //    93: iconst_1       
            //    94: bipush          6
            //    96: bipush          16
            //    98: iconst_1       
            //    99: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //   102: aastore        
            //   103: iconst_2       
            //   104: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   107: iconst_3       
            //   108: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   111: dup            
            //   112: iconst_0       
            //   113: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   116: dup            
            //   117: getstatic       net/minecraft/world/level/block/Blocks.PUMPKIN:Lnet/minecraft/world/level/block/Block;
            //   120: bipush          6
            //   122: bipush          12
            //   124: bipush          10
            //   126: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   129: aastore        
            //   130: dup            
            //   131: iconst_1       
            //   132: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   135: dup            
            //   136: getstatic       net/minecraft/world/item/Items.PUMPKIN_PIE:Lnet/minecraft/world/item/Item;
            //   139: iconst_1       
            //   140: iconst_4       
            //   141: iconst_5       
            //   142: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //   145: aastore        
            //   146: dup            
            //   147: iconst_2       
            //   148: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   151: dup            
            //   152: getstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
            //   155: iconst_1       
            //   156: iconst_4       
            //   157: bipush          16
            //   159: iconst_5       
            //   160: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //   163: aastore        
            //   164: iconst_3       
            //   165: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   168: iconst_2       
            //   169: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   172: dup            
            //   173: iconst_0       
            //   174: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   177: dup            
            //   178: getstatic       net/minecraft/world/item/Items.COOKIE:Lnet/minecraft/world/item/Item;
            //   181: iconst_3       
            //   182: bipush          18
            //   184: bipush          10
            //   186: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //   189: aastore        
            //   190: dup            
            //   191: iconst_1       
            //   192: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   195: dup            
            //   196: getstatic       net/minecraft/world/level/block/Blocks.MELON:Lnet/minecraft/world/level/block/Block;
            //   199: iconst_4       
            //   200: bipush          12
            //   202: bipush          20
            //   204: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   207: aastore        
            //   208: iconst_4       
            //   209: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   212: bipush          7
            //   214: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   217: dup            
            //   218: iconst_0       
            //   219: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   222: dup            
            //   223: getstatic       net/minecraft/world/level/block/Blocks.CAKE:Lnet/minecraft/world/level/block/Block;
            //   226: iconst_1       
            //   227: iconst_1       
            //   228: bipush          12
            //   230: bipush          15
            //   232: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //   235: aastore        
            //   236: dup            
            //   237: iconst_1       
            //   238: new             Lnet/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald;
            //   241: dup            
            //   242: getstatic       net/minecraft/world/effect/MobEffects.NIGHT_VISION:Lnet/minecraft/world/effect/MobEffect;
            //   245: bipush          100
            //   247: bipush          15
            //   249: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald.<init>:(Lnet/minecraft/world/effect/MobEffect;II)V
            //   252: aastore        
            //   253: dup            
            //   254: iconst_2       
            //   255: new             Lnet/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald;
            //   258: dup            
            //   259: getstatic       net/minecraft/world/effect/MobEffects.JUMP:Lnet/minecraft/world/effect/MobEffect;
            //   262: sipush          160
            //   265: bipush          15
            //   267: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald.<init>:(Lnet/minecraft/world/effect/MobEffect;II)V
            //   270: aastore        
            //   271: dup            
            //   272: iconst_3       
            //   273: new             Lnet/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald;
            //   276: dup            
            //   277: getstatic       net/minecraft/world/effect/MobEffects.WEAKNESS:Lnet/minecraft/world/effect/MobEffect;
            //   280: sipush          140
            //   283: bipush          15
            //   285: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald.<init>:(Lnet/minecraft/world/effect/MobEffect;II)V
            //   288: aastore        
            //   289: dup            
            //   290: iconst_4       
            //   291: new             Lnet/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald;
            //   294: dup            
            //   295: getstatic       net/minecraft/world/effect/MobEffects.BLINDNESS:Lnet/minecraft/world/effect/MobEffect;
            //   298: bipush          120
            //   300: bipush          15
            //   302: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald.<init>:(Lnet/minecraft/world/effect/MobEffect;II)V
            //   305: aastore        
            //   306: dup            
            //   307: iconst_5       
            //   308: new             Lnet/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald;
            //   311: dup            
            //   312: getstatic       net/minecraft/world/effect/MobEffects.POISON:Lnet/minecraft/world/effect/MobEffect;
            //   315: sipush          280
            //   318: bipush          15
            //   320: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald.<init>:(Lnet/minecraft/world/effect/MobEffect;II)V
            //   323: aastore        
            //   324: dup            
            //   325: bipush          6
            //   327: new             Lnet/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald;
            //   330: dup            
            //   331: getstatic       net/minecraft/world/effect/MobEffects.SATURATION:Lnet/minecraft/world/effect/MobEffect;
            //   334: bipush          7
            //   336: bipush          15
            //   338: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$SuspisciousStewForEmerald.<init>:(Lnet/minecraft/world/effect/MobEffect;II)V
            //   341: aastore        
            //   342: iconst_5       
            //   343: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   346: iconst_2       
            //   347: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   350: dup            
            //   351: iconst_0       
            //   352: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   355: dup            
            //   356: getstatic       net/minecraft/world/item/Items.GOLDEN_CARROT:Lnet/minecraft/world/item/Item;
            //   359: iconst_3       
            //   360: iconst_3       
            //   361: bipush          30
            //   363: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //   366: aastore        
            //   367: dup            
            //   368: iconst_1       
            //   369: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   372: dup            
            //   373: getstatic       net/minecraft/world/item/Items.GLISTERING_MELON_SLICE:Lnet/minecraft/world/item/Item;
            //   376: iconst_4       
            //   377: iconst_3       
            //   378: bipush          30
            //   380: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //   383: aastore        
            //   384: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //   387: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //   390: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   393: pop            
            //   394: aload_0         /* hashMap */
            //   395: getstatic       net/minecraft/world/entity/npc/VillagerProfession.FISHERMAN:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //   398: iconst_1       
            //   399: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   402: iconst_4       
            //   403: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   406: dup            
            //   407: iconst_0       
            //   408: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   411: dup            
            //   412: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
            //   415: bipush          20
            //   417: bipush          16
            //   419: iconst_2       
            //   420: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   423: aastore        
            //   424: dup            
            //   425: iconst_1       
            //   426: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   429: dup            
            //   430: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
            //   433: bipush          10
            //   435: bipush          16
            //   437: iconst_2       
            //   438: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   441: aastore        
            //   442: dup            
            //   443: iconst_2       
            //   444: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsAndEmeraldsToItems;
            //   447: dup            
            //   448: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
            //   451: bipush          6
            //   453: getstatic       net/minecraft/world/item/Items.COOKED_COD:Lnet/minecraft/world/item/Item;
            //   456: bipush          6
            //   458: bipush          16
            //   460: iconst_1       
            //   461: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsAndEmeraldsToItems.<init>:(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/world/item/Item;III)V
            //   464: aastore        
            //   465: dup            
            //   466: iconst_3       
            //   467: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   470: dup            
            //   471: getstatic       net/minecraft/world/item/Items.COD_BUCKET:Lnet/minecraft/world/item/Item;
            //   474: iconst_3       
            //   475: iconst_1       
            //   476: bipush          16
            //   478: iconst_1       
            //   479: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //   482: aastore        
            //   483: iconst_2       
            //   484: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   487: iconst_3       
            //   488: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   491: dup            
            //   492: iconst_0       
            //   493: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   496: dup            
            //   497: getstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
            //   500: bipush          15
            //   502: bipush          16
            //   504: bipush          10
            //   506: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   509: aastore        
            //   510: dup            
            //   511: iconst_1       
            //   512: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsAndEmeraldsToItems;
            //   515: dup            
            //   516: getstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
            //   519: bipush          6
            //   521: getstatic       net/minecraft/world/item/Items.COOKED_SALMON:Lnet/minecraft/world/item/Item;
            //   524: bipush          6
            //   526: bipush          16
            //   528: iconst_5       
            //   529: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsAndEmeraldsToItems.<init>:(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/world/item/Item;III)V
            //   532: aastore        
            //   533: dup            
            //   534: iconst_2       
            //   535: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   538: dup            
            //   539: getstatic       net/minecraft/world/item/Items.CAMPFIRE:Lnet/minecraft/world/item/Item;
            //   542: iconst_2       
            //   543: iconst_1       
            //   544: iconst_5       
            //   545: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //   548: aastore        
            //   549: iconst_3       
            //   550: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   553: iconst_2       
            //   554: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   557: dup            
            //   558: iconst_0       
            //   559: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   562: dup            
            //   563: getstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
            //   566: bipush          13
            //   568: bipush          16
            //   570: bipush          20
            //   572: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   575: aastore        
            //   576: dup            
            //   577: iconst_1       
            //   578: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //   581: dup            
            //   582: getstatic       net/minecraft/world/item/Items.FISHING_ROD:Lnet/minecraft/world/item/Item;
            //   585: iconst_3       
            //   586: iconst_3       
            //   587: bipush          10
            //   589: ldc             0.2
            //   591: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //   594: aastore        
            //   595: iconst_4       
            //   596: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   599: iconst_1       
            //   600: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   603: dup            
            //   604: iconst_0       
            //   605: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   608: dup            
            //   609: getstatic       net/minecraft/world/item/Items.TROPICAL_FISH:Lnet/minecraft/world/item/Item;
            //   612: bipush          6
            //   614: bipush          12
            //   616: bipush          30
            //   618: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   621: aastore        
            //   622: iconst_5       
            //   623: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   626: iconst_2       
            //   627: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   630: dup            
            //   631: iconst_0       
            //   632: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   635: dup            
            //   636: getstatic       net/minecraft/world/item/Items.PUFFERFISH:Lnet/minecraft/world/item/Item;
            //   639: iconst_4       
            //   640: bipush          12
            //   642: bipush          30
            //   644: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   647: aastore        
            //   648: dup            
            //   649: iconst_1       
            //   650: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldsForVillagerTypeItem;
            //   653: dup            
            //   654: iconst_1       
            //   655: bipush          12
            //   657: bipush          30
            //   659: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
            //   662: getstatic       net/minecraft/world/entity/npc/VillagerType.PLAINS:Lnet/minecraft/world/entity/npc/VillagerType;
            //   665: getstatic       net/minecraft/world/item/Items.OAK_BOAT:Lnet/minecraft/world/item/Item;
            //   668: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //   671: getstatic       net/minecraft/world/entity/npc/VillagerType.TAIGA:Lnet/minecraft/world/entity/npc/VillagerType;
            //   674: getstatic       net/minecraft/world/item/Items.SPRUCE_BOAT:Lnet/minecraft/world/item/Item;
            //   677: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //   680: getstatic       net/minecraft/world/entity/npc/VillagerType.SNOW:Lnet/minecraft/world/entity/npc/VillagerType;
            //   683: getstatic       net/minecraft/world/item/Items.SPRUCE_BOAT:Lnet/minecraft/world/item/Item;
            //   686: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //   689: getstatic       net/minecraft/world/entity/npc/VillagerType.DESERT:Lnet/minecraft/world/entity/npc/VillagerType;
            //   692: getstatic       net/minecraft/world/item/Items.JUNGLE_BOAT:Lnet/minecraft/world/item/Item;
            //   695: invokevirtual   invokevirtual  !!! ERROR
            //   698: getstatic       net/minecraft/world/entity/npc/VillagerType.JUNGLE:Lnet/minecraft/world/entity/npc/VillagerType;
            //   701: getstatic       net/minecraft/world/item/Items.JUNGLE_BOAT:Lnet/minecraft/world/item/Item;
            //   704: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //   707: getstatic       net/minecraft/world/entity/npc/VillagerType.SAVANNA:Lnet/minecraft/world/entity/npc/VillagerType;
            //   710: getstatic       net/minecraft/world/item/Items.ACACIA_BOAT:Lnet/minecraft/world/item/Item;
            //   713: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //   716: getstatic       net/minecraft/world/entity/npc/VillagerType.SWAMP:Lnet/minecraft/world/entity/npc/VillagerType;
            //   719: getstatic       net/minecraft/world/item/Items.DARK_OAK_BOAT:Lnet/minecraft/world/item/Item;
            //   722: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //   725: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
            //   728: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldsForVillagerTypeItem.<init>:(IIILjava/util/Map;)V
            //   731: aastore        
            //   732: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //   735: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //   738: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //   741: pop            
            //   742: aload_0         /* hashMap */
            //   743: getstatic       net/minecraft/world/entity/npc/VillagerProfession.SHEPHERD:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //   746: iconst_1       
            //   747: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   750: iconst_5       
            //   751: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   754: dup            
            //   755: iconst_0       
            //   756: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   759: dup            
            //   760: getstatic       net/minecraft/world/level/block/Blocks.WHITE_WOOL:Lnet/minecraft/world/level/block/Block;
            //   763: bipush          18
            //   765: bipush          16
            //   767: iconst_2       
            //   768: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   771: aastore        
            //   772: dup            
            //   773: iconst_1       
            //   774: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   777: dup            
            //   778: getstatic       net/minecraft/world/level/block/Blocks.BROWN_WOOL:Lnet/minecraft/world/level/block/Block;
            //   781: bipush          18
            //   783: bipush          16
            //   785: iconst_2       
            //   786: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   789: aastore        
            //   790: dup            
            //   791: iconst_2       
            //   792: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   795: dup            
            //   796: getstatic       net/minecraft/world/level/block/Blocks.BLACK_WOOL:Lnet/minecraft/world/level/block/Block;
            //   799: bipush          18
            //   801: bipush          16
            //   803: iconst_2       
            //   804: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   807: aastore        
            //   808: dup            
            //   809: iconst_3       
            //   810: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   813: dup            
            //   814: getstatic       net/minecraft/world/level/block/Blocks.GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
            //   817: bipush          18
            //   819: bipush          16
            //   821: iconst_2       
            //   822: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   825: aastore        
            //   826: dup            
            //   827: iconst_4       
            //   828: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   831: dup            
            //   832: getstatic       net/minecraft/world/item/Items.SHEARS:Lnet/minecraft/world/item/Item;
            //   835: iconst_2       
            //   836: iconst_1       
            //   837: iconst_1       
            //   838: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //   841: aastore        
            //   842: iconst_2       
            //   843: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //   846: bipush          37
            //   848: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //   851: dup            
            //   852: iconst_0       
            //   853: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   856: dup            
            //   857: getstatic       net/minecraft/world/item/Items.WHITE_DYE:Lnet/minecraft/world/item/Item;
            //   860: bipush          12
            //   862: bipush          16
            //   864: bipush          10
            //   866: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   869: aastore        
            //   870: dup            
            //   871: iconst_1       
            //   872: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   875: dup            
            //   876: getstatic       net/minecraft/world/item/Items.GRAY_DYE:Lnet/minecraft/world/item/Item;
            //   879: bipush          12
            //   881: bipush          16
            //   883: bipush          10
            //   885: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   888: aastore        
            //   889: dup            
            //   890: iconst_2       
            //   891: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   894: dup            
            //   895: getstatic       net/minecraft/world/item/Items.BLACK_DYE:Lnet/minecraft/world/item/Item;
            //   898: bipush          12
            //   900: bipush          16
            //   902: bipush          10
            //   904: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   907: aastore        
            //   908: dup            
            //   909: iconst_3       
            //   910: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   913: dup            
            //   914: getstatic       net/minecraft/world/item/Items.LIGHT_BLUE_DYE:Lnet/minecraft/world/item/Item;
            //   917: bipush          12
            //   919: bipush          16
            //   921: bipush          10
            //   923: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   926: aastore        
            //   927: dup            
            //   928: iconst_4       
            //   929: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //   932: dup            
            //   933: getstatic       net/minecraft/world/item/Items.LIME_DYE:Lnet/minecraft/world/item/Item;
            //   936: bipush          12
            //   938: bipush          16
            //   940: bipush          10
            //   942: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //   945: aastore        
            //   946: dup            
            //   947: iconst_5       
            //   948: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   951: dup            
            //   952: getstatic       net/minecraft/world/level/block/Blocks.WHITE_WOOL:Lnet/minecraft/world/level/block/Block;
            //   955: iconst_1       
            //   956: iconst_1       
            //   957: bipush          16
            //   959: iconst_5       
            //   960: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //   963: aastore        
            //   964: dup            
            //   965: bipush          6
            //   967: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   970: dup            
            //   971: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_WOOL:Lnet/minecraft/world/level/block/Block;
            //   974: iconst_1       
            //   975: iconst_1       
            //   976: bipush          16
            //   978: iconst_5       
            //   979: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //   982: aastore        
            //   983: dup            
            //   984: bipush          7
            //   986: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //   989: dup            
            //   990: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_WOOL:Lnet/minecraft/world/level/block/Block;
            //   993: iconst_1       
            //   994: iconst_1       
            //   995: bipush          16
            //   997: iconst_5       
            //   998: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1001: aastore        
            //  1002: dup            
            //  1003: bipush          8
            //  1005: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1008: dup            
            //  1009: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1012: iconst_1       
            //  1013: iconst_1       
            //  1014: bipush          16
            //  1016: iconst_5       
            //  1017: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1020: aastore        
            //  1021: dup            
            //  1022: bipush          9
            //  1024: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1027: dup            
            //  1028: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1031: iconst_1       
            //  1032: iconst_1       
            //  1033: bipush          16
            //  1035: iconst_5       
            //  1036: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1039: aastore        
            //  1040: dup            
            //  1041: bipush          10
            //  1043: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1046: dup            
            //  1047: getstatic       net/minecraft/world/level/block/Blocks.LIME_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1050: iconst_1       
            //  1051: iconst_1       
            //  1052: bipush          16
            //  1054: iconst_5       
            //  1055: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1058: aastore        
            //  1059: dup            
            //  1060: bipush          11
            //  1062: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1065: dup            
            //  1066: getstatic       net/minecraft/world/level/block/Blocks.PINK_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1069: iconst_1       
            //  1070: iconst_1       
            //  1071: bipush          16
            //  1073: iconst_5       
            //  1074: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1077: aastore        
            //  1078: dup            
            //  1079: bipush          12
            //  1081: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1084: dup            
            //  1085: getstatic       net/minecraft/world/level/block/Blocks.GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1088: iconst_1       
            //  1089: iconst_1       
            //  1090: bipush          16
            //  1092: iconst_5       
            //  1093: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1096: aastore        
            //  1097: dup            
            //  1098: bipush          13
            //  1100: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1103: dup            
            //  1104: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1107: iconst_1       
            //  1108: iconst_1       
            //  1109: bipush          16
            //  1111: iconst_5       
            //  1112: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1115: aastore        
            //  1116: dup            
            //  1117: bipush          14
            //  1119: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1122: dup            
            //  1123: getstatic       net/minecraft/world/level/block/Blocks.CYAN_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1126: iconst_1       
            //  1127: iconst_1       
            //  1128: bipush          16
            //  1130: iconst_5       
            //  1131: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1134: aastore        
            //  1135: dup            
            //  1136: bipush          15
            //  1138: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1141: dup            
            //  1142: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1145: iconst_1       
            //  1146: iconst_1       
            //  1147: bipush          16
            //  1149: iconst_5       
            //  1150: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1153: aastore        
            //  1154: dup            
            //  1155: bipush          16
            //  1157: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1160: dup            
            //  1161: getstatic       net/minecraft/world/level/block/Blocks.BLUE_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1164: iconst_1       
            //  1165: iconst_1       
            //  1166: bipush          16
            //  1168: iconst_5       
            //  1169: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1172: aastore        
            //  1173: dup            
            //  1174: bipush          17
            //  1176: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1179: dup            
            //  1180: getstatic       net/minecraft/world/level/block/Blocks.BROWN_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1183: iconst_1       
            //  1184: iconst_1       
            //  1185: bipush          16
            //  1187: iconst_5       
            //  1188: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1191: aastore        
            //  1192: dup            
            //  1193: bipush          18
            //  1195: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1198: dup            
            //  1199: getstatic       net/minecraft/world/level/block/Blocks.GREEN_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1202: iconst_1       
            //  1203: iconst_1       
            //  1204: bipush          16
            //  1206: iconst_5       
            //  1207: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1210: aastore        
            //  1211: dup            
            //  1212: bipush          19
            //  1214: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1217: dup            
            //  1218: getstatic       net/minecraft/world/level/block/Blocks.RED_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1221: iconst_1       
            //  1222: iconst_1       
            //  1223: bipush          16
            //  1225: iconst_5       
            //  1226: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1229: aastore        
            //  1230: dup            
            //  1231: bipush          20
            //  1233: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1236: dup            
            //  1237: getstatic       net/minecraft/world/level/block/Blocks.BLACK_WOOL:Lnet/minecraft/world/level/block/Block;
            //  1240: iconst_1       
            //  1241: iconst_1       
            //  1242: bipush          16
            //  1244: iconst_5       
            //  1245: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1248: aastore        
            //  1249: dup            
            //  1250: bipush          21
            //  1252: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1255: dup            
            //  1256: getstatic       net/minecraft/world/level/block/Blocks.WHITE_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1259: iconst_1       
            //  1260: iconst_4       
            //  1261: bipush          16
            //  1263: iconst_5       
            //  1264: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1267: aastore        
            //  1268: dup            
            //  1269: bipush          22
            //  1271: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1274: dup            
            //  1275: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1278: iconst_1       
            //  1279: iconst_4       
            //  1280: bipush          16
            //  1282: iconst_5       
            //  1283: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1286: aastore        
            //  1287: dup            
            //  1288: bipush          23
            //  1290: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1293: dup            
            //  1294: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1297: iconst_1       
            //  1298: iconst_4       
            //  1299: bipush          16
            //  1301: iconst_5       
            //  1302: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1305: aastore        
            //  1306: dup            
            //  1307: bipush          24
            //  1309: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1312: dup            
            //  1313: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1316: iconst_1       
            //  1317: iconst_4       
            //  1318: bipush          16
            //  1320: iconst_5       
            //  1321: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1324: aastore        
            //  1325: dup            
            //  1326: bipush          25
            //  1328: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1331: dup            
            //  1332: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1335: iconst_1       
            //  1336: iconst_4       
            //  1337: bipush          16
            //  1339: iconst_5       
            //  1340: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1343: aastore        
            //  1344: dup            
            //  1345: bipush          26
            //  1347: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1350: dup            
            //  1351: getstatic       net/minecraft/world/level/block/Blocks.LIME_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1354: iconst_1       
            //  1355: iconst_4       
            //  1356: bipush          16
            //  1358: iconst_5       
            //  1359: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1362: aastore        
            //  1363: dup            
            //  1364: bipush          27
            //  1366: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1369: dup            
            //  1370: getstatic       net/minecraft/world/level/block/Blocks.PINK_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1373: iconst_1       
            //  1374: iconst_4       
            //  1375: bipush          16
            //  1377: iconst_5       
            //  1378: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1381: aastore        
            //  1382: dup            
            //  1383: bipush          28
            //  1385: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1388: dup            
            //  1389: getstatic       net/minecraft/world/level/block/Blocks.GRAY_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1392: iconst_1       
            //  1393: iconst_4       
            //  1394: bipush          16
            //  1396: iconst_5       
            //  1397: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1400: aastore        
            //  1401: dup            
            //  1402: bipush          29
            //  1404: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1407: dup            
            //  1408: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1411: iconst_1       
            //  1412: iconst_4       
            //  1413: bipush          16
            //  1415: iconst_5       
            //  1416: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1419: aastore        
            //  1420: dup            
            //  1421: bipush          30
            //  1423: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1426: dup            
            //  1427: getstatic       net/minecraft/world/level/block/Blocks.CYAN_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1430: iconst_1       
            //  1431: iconst_4       
            //  1432: bipush          16
            //  1434: iconst_5       
            //  1435: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1438: aastore        
            //  1439: dup            
            //  1440: bipush          31
            //  1442: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1445: dup            
            //  1446: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1449: iconst_1       
            //  1450: iconst_4       
            //  1451: bipush          16
            //  1453: iconst_5       
            //  1454: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1457: aastore        
            //  1458: dup            
            //  1459: bipush          32
            //  1461: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1464: dup            
            //  1465: getstatic       net/minecraft/world/level/block/Blocks.BLUE_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1468: iconst_1       
            //  1469: iconst_4       
            //  1470: bipush          16
            //  1472: iconst_5       
            //  1473: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1476: aastore        
            //  1477: dup            
            //  1478: bipush          33
            //  1480: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1483: dup            
            //  1484: getstatic       net/minecraft/world/level/block/Blocks.BROWN_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1487: iconst_1       
            //  1488: iconst_4       
            //  1489: bipush          16
            //  1491: iconst_5       
            //  1492: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1495: aastore        
            //  1496: dup            
            //  1497: bipush          34
            //  1499: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1502: dup            
            //  1503: getstatic       net/minecraft/world/level/block/Blocks.GREEN_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1506: iconst_1       
            //  1507: iconst_4       
            //  1508: bipush          16
            //  1510: iconst_5       
            //  1511: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1514: aastore        
            //  1515: dup            
            //  1516: bipush          35
            //  1518: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1521: dup            
            //  1522: getstatic       net/minecraft/world/level/block/Blocks.RED_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1525: iconst_1       
            //  1526: iconst_4       
            //  1527: bipush          16
            //  1529: iconst_5       
            //  1530: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1533: aastore        
            //  1534: dup            
            //  1535: bipush          36
            //  1537: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1540: dup            
            //  1541: getstatic       net/minecraft/world/level/block/Blocks.BLACK_CARPET:Lnet/minecraft/world/level/block/Block;
            //  1544: iconst_1       
            //  1545: iconst_4       
            //  1546: bipush          16
            //  1548: iconst_5       
            //  1549: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1552: aastore        
            //  1553: iconst_3       
            //  1554: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1557: bipush          21
            //  1559: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  1562: dup            
            //  1563: iconst_0       
            //  1564: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  1567: dup            
            //  1568: getstatic       net/minecraft/world/item/Items.YELLOW_DYE:Lnet/minecraft/world/item/Item;
            //  1571: bipush          12
            //  1573: bipush          16
            //  1575: bipush          20
            //  1577: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  1580: aastore        
            //  1581: dup            
            //  1582: iconst_1       
            //  1583: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  1586: dup            
            //  1587: getstatic       net/minecraft/world/item/Items.LIGHT_GRAY_DYE:Lnet/minecraft/world/item/Item;
            //  1590: bipush          12
            //  1592: bipush          16
            //  1594: bipush          20
            //  1596: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  1599: aastore        
            //  1600: dup            
            //  1601: iconst_2       
            //  1602: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  1605: dup            
            //  1606: getstatic       net/minecraft/world/item/Items.ORANGE_DYE:Lnet/minecraft/world/item/Item;
            //  1609: bipush          12
            //  1611: bipush          16
            //  1613: bipush          20
            //  1615: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  1618: aastore        
            //  1619: dup            
            //  1620: iconst_3       
            //  1621: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  1624: dup            
            //  1625: getstatic       net/minecraft/world/item/Items.RED_DYE:Lnet/minecraft/world/item/Item;
            //  1628: bipush          12
            //  1630: bipush          16
            //  1632: bipush          20
            //  1634: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  1637: aastore        
            //  1638: dup            
            //  1639: iconst_4       
            //  1640: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  1643: dup            
            //  1644: getstatic       net/minecraft/world/item/Items.PINK_DYE:Lnet/minecraft/world/item/Item;
            //  1647: bipush          12
            //  1649: bipush          16
            //  1651: bipush          20
            //  1653: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  1656: aastore        
            //  1657: dup            
            //  1658: iconst_5       
            //  1659: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1662: dup            
            //  1663: getstatic       net/minecraft/world/level/block/Blocks.WHITE_BED:Lnet/minecraft/world/level/block/Block;
            //  1666: iconst_3       
            //  1667: iconst_1       
            //  1668: bipush          12
            //  1670: bipush          10
            //  1672: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1675: aastore        
            //  1676: dup            
            //  1677: bipush          6
            //  1679: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1682: dup            
            //  1683: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_BED:Lnet/minecraft/world/level/block/Block;
            //  1686: iconst_3       
            //  1687: iconst_1       
            //  1688: bipush          12
            //  1690: bipush          10
            //  1692: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1695: aastore        
            //  1696: dup            
            //  1697: bipush          7
            //  1699: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1702: dup            
            //  1703: getstatic       net/minecraft/world/level/block/Blocks.RED_BED:Lnet/minecraft/world/level/block/Block;
            //  1706: iconst_3       
            //  1707: iconst_1       
            //  1708: bipush          12
            //  1710: bipush          10
            //  1712: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1715: aastore        
            //  1716: dup            
            //  1717: bipush          8
            //  1719: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1722: dup            
            //  1723: getstatic       net/minecraft/world/level/block/Blocks.BLACK_BED:Lnet/minecraft/world/level/block/Block;
            //  1726: iconst_3       
            //  1727: iconst_1       
            //  1728: bipush          12
            //  1730: bipush          10
            //  1732: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1735: aastore        
            //  1736: dup            
            //  1737: bipush          9
            //  1739: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1742: dup            
            //  1743: getstatic       net/minecraft/world/level/block/Blocks.BLUE_BED:Lnet/minecraft/world/level/block/Block;
            //  1746: iconst_3       
            //  1747: iconst_1       
            //  1748: bipush          12
            //  1750: bipush          10
            //  1752: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1755: aastore        
            //  1756: dup            
            //  1757: bipush          10
            //  1759: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1762: dup            
            //  1763: getstatic       net/minecraft/world/level/block/Blocks.BROWN_BED:Lnet/minecraft/world/level/block/Block;
            //  1766: iconst_3       
            //  1767: iconst_1       
            //  1768: bipush          12
            //  1770: bipush          10
            //  1772: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1775: aastore        
            //  1776: dup            
            //  1777: bipush          11
            //  1779: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1782: dup            
            //  1783: getstatic       net/minecraft/world/level/block/Blocks.CYAN_BED:Lnet/minecraft/world/level/block/Block;
            //  1786: iconst_3       
            //  1787: iconst_1       
            //  1788: bipush          12
            //  1790: bipush          10
            //  1792: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1795: aastore        
            //  1796: dup            
            //  1797: bipush          12
            //  1799: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1802: dup            
            //  1803: getstatic       net/minecraft/world/level/block/Blocks.GRAY_BED:Lnet/minecraft/world/level/block/Block;
            //  1806: iconst_3       
            //  1807: iconst_1       
            //  1808: bipush          12
            //  1810: bipush          10
            //  1812: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1815: aastore        
            //  1816: dup            
            //  1817: bipush          13
            //  1819: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1822: dup            
            //  1823: getstatic       net/minecraft/world/level/block/Blocks.GREEN_BED:Lnet/minecraft/world/level/block/Block;
            //  1826: iconst_3       
            //  1827: iconst_1       
            //  1828: bipush          12
            //  1830: bipush          10
            //  1832: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1835: aastore        
            //  1836: dup            
            //  1837: bipush          14
            //  1839: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1842: dup            
            //  1843: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_BED:Lnet/minecraft/world/level/block/Block;
            //  1846: iconst_3       
            //  1847: iconst_1       
            //  1848: bipush          12
            //  1850: bipush          10
            //  1852: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1855: aastore        
            //  1856: dup            
            //  1857: bipush          15
            //  1859: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1862: dup            
            //  1863: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_BED:Lnet/minecraft/world/level/block/Block;
            //  1866: iconst_3       
            //  1867: iconst_1       
            //  1868: bipush          12
            //  1870: bipush          10
            //  1872: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1875: aastore        
            //  1876: dup            
            //  1877: bipush          16
            //  1879: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1882: dup            
            //  1883: getstatic       net/minecraft/world/level/block/Blocks.LIME_BED:Lnet/minecraft/world/level/block/Block;
            //  1886: iconst_3       
            //  1887: iconst_1       
            //  1888: bipush          12
            //  1890: bipush          10
            //  1892: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1895: aastore        
            //  1896: dup            
            //  1897: bipush          17
            //  1899: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1902: dup            
            //  1903: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_BED:Lnet/minecraft/world/level/block/Block;
            //  1906: iconst_3       
            //  1907: iconst_1       
            //  1908: bipush          12
            //  1910: bipush          10
            //  1912: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1915: aastore        
            //  1916: dup            
            //  1917: bipush          18
            //  1919: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1922: dup            
            //  1923: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_BED:Lnet/minecraft/world/level/block/Block;
            //  1926: iconst_3       
            //  1927: iconst_1       
            //  1928: bipush          12
            //  1930: bipush          10
            //  1932: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1935: aastore        
            //  1936: dup            
            //  1937: bipush          19
            //  1939: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1942: dup            
            //  1943: getstatic       net/minecraft/world/level/block/Blocks.PINK_BED:Lnet/minecraft/world/level/block/Block;
            //  1946: iconst_3       
            //  1947: iconst_1       
            //  1948: bipush          12
            //  1950: bipush          10
            //  1952: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1955: aastore        
            //  1956: dup            
            //  1957: bipush          20
            //  1959: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  1962: dup            
            //  1963: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_BED:Lnet/minecraft/world/level/block/Block;
            //  1966: iconst_3       
            //  1967: iconst_1       
            //  1968: bipush          12
            //  1970: bipush          10
            //  1972: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  1975: aastore        
            //  1976: iconst_4       
            //  1977: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  1980: bipush          22
            //  1982: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  1985: dup            
            //  1986: iconst_0       
            //  1987: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  1990: dup            
            //  1991: getstatic       net/minecraft/world/item/Items.BROWN_DYE:Lnet/minecraft/world/item/Item;
            //  1994: bipush          12
            //  1996: bipush          16
            //  1998: bipush          30
            //  2000: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2003: aastore        
            //  2004: dup            
            //  2005: iconst_1       
            //  2006: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2009: dup            
            //  2010: getstatic       net/minecraft/world/item/Items.PURPLE_DYE:Lnet/minecraft/world/item/Item;
            //  2013: bipush          12
            //  2015: bipush          16
            //  2017: bipush          30
            //  2019: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2022: aastore        
            //  2023: dup            
            //  2024: iconst_2       
            //  2025: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2028: dup            
            //  2029: getstatic       net/minecraft/world/item/Items.BLUE_DYE:Lnet/minecraft/world/item/Item;
            //  2032: bipush          12
            //  2034: bipush          16
            //  2036: bipush          30
            //  2038: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2041: aastore        
            //  2042: dup            
            //  2043: iconst_3       
            //  2044: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2047: dup            
            //  2048: getstatic       net/minecraft/world/item/Items.GREEN_DYE:Lnet/minecraft/world/item/Item;
            //  2051: bipush          12
            //  2053: bipush          16
            //  2055: bipush          30
            //  2057: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2060: aastore        
            //  2061: dup            
            //  2062: iconst_4       
            //  2063: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2066: dup            
            //  2067: getstatic       net/minecraft/world/item/Items.MAGENTA_DYE:Lnet/minecraft/world/item/Item;
            //  2070: bipush          12
            //  2072: bipush          16
            //  2074: bipush          30
            //  2076: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2079: aastore        
            //  2080: dup            
            //  2081: iconst_5       
            //  2082: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2085: dup            
            //  2086: getstatic       net/minecraft/world/item/Items.CYAN_DYE:Lnet/minecraft/world/item/Item;
            //  2089: bipush          12
            //  2091: bipush          16
            //  2093: bipush          30
            //  2095: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2098: aastore        
            //  2099: dup            
            //  2100: bipush          6
            //  2102: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2105: dup            
            //  2106: getstatic       net/minecraft/world/item/Items.WHITE_BANNER:Lnet/minecraft/world/item/Item;
            //  2109: iconst_3       
            //  2110: iconst_1       
            //  2111: bipush          12
            //  2113: bipush          15
            //  2115: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2118: aastore        
            //  2119: dup            
            //  2120: bipush          7
            //  2122: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2125: dup            
            //  2126: getstatic       net/minecraft/world/item/Items.BLUE_BANNER:Lnet/minecraft/world/item/Item;
            //  2129: iconst_3       
            //  2130: iconst_1       
            //  2131: bipush          12
            //  2133: bipush          15
            //  2135: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2138: aastore        
            //  2139: dup            
            //  2140: bipush          8
            //  2142: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2145: dup            
            //  2146: getstatic       net/minecraft/world/item/Items.LIGHT_BLUE_BANNER:Lnet/minecraft/world/item/Item;
            //  2149: iconst_3       
            //  2150: iconst_1       
            //  2151: bipush          12
            //  2153: bipush          15
            //  2155: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2158: aastore        
            //  2159: dup            
            //  2160: bipush          9
            //  2162: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2165: dup            
            //  2166: getstatic       net/minecraft/world/item/Items.RED_BANNER:Lnet/minecraft/world/item/Item;
            //  2169: iconst_3       
            //  2170: iconst_1       
            //  2171: bipush          12
            //  2173: bipush          15
            //  2175: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2178: aastore        
            //  2179: dup            
            //  2180: bipush          10
            //  2182: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2185: dup            
            //  2186: getstatic       net/minecraft/world/item/Items.PINK_BANNER:Lnet/minecraft/world/item/Item;
            //  2189: iconst_3       
            //  2190: iconst_1       
            //  2191: bipush          12
            //  2193: bipush          15
            //  2195: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2198: aastore        
            //  2199: dup            
            //  2200: bipush          11
            //  2202: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2205: dup            
            //  2206: getstatic       net/minecraft/world/item/Items.GREEN_BANNER:Lnet/minecraft/world/item/Item;
            //  2209: iconst_3       
            //  2210: iconst_1       
            //  2211: bipush          12
            //  2213: bipush          15
            //  2215: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2218: aastore        
            //  2219: dup            
            //  2220: bipush          12
            //  2222: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2225: dup            
            //  2226: getstatic       net/minecraft/world/item/Items.LIME_BANNER:Lnet/minecraft/world/item/Item;
            //  2229: iconst_3       
            //  2230: iconst_1       
            //  2231: bipush          12
            //  2233: bipush          15
            //  2235: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2238: aastore        
            //  2239: dup            
            //  2240: bipush          13
            //  2242: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2245: dup            
            //  2246: getstatic       net/minecraft/world/item/Items.GRAY_BANNER:Lnet/minecraft/world/item/Item;
            //  2249: iconst_3       
            //  2250: iconst_1       
            //  2251: bipush          12
            //  2253: bipush          15
            //  2255: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2258: aastore        
            //  2259: dup            
            //  2260: bipush          14
            //  2262: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2265: dup            
            //  2266: getstatic       net/minecraft/world/item/Items.BLACK_BANNER:Lnet/minecraft/world/item/Item;
            //  2269: iconst_3       
            //  2270: iconst_1       
            //  2271: bipush          12
            //  2273: bipush          15
            //  2275: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2278: aastore        
            //  2279: dup            
            //  2280: bipush          15
            //  2282: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2285: dup            
            //  2286: getstatic       net/minecraft/world/item/Items.PURPLE_BANNER:Lnet/minecraft/world/item/Item;
            //  2289: iconst_3       
            //  2290: iconst_1       
            //  2291: bipush          12
            //  2293: bipush          15
            //  2295: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2298: aastore        
            //  2299: dup            
            //  2300: bipush          16
            //  2302: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2305: dup            
            //  2306: getstatic       net/minecraft/world/item/Items.MAGENTA_BANNER:Lnet/minecraft/world/item/Item;
            //  2309: iconst_3       
            //  2310: iconst_1       
            //  2311: bipush          12
            //  2313: bipush          15
            //  2315: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2318: aastore        
            //  2319: dup            
            //  2320: bipush          17
            //  2322: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2325: dup            
            //  2326: getstatic       net/minecraft/world/item/Items.CYAN_BANNER:Lnet/minecraft/world/item/Item;
            //  2329: iconst_3       
            //  2330: iconst_1       
            //  2331: bipush          12
            //  2333: bipush          15
            //  2335: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2338: aastore        
            //  2339: dup            
            //  2340: bipush          18
            //  2342: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2345: dup            
            //  2346: getstatic       net/minecraft/world/item/Items.BROWN_BANNER:Lnet/minecraft/world/item/Item;
            //  2349: iconst_3       
            //  2350: iconst_1       
            //  2351: bipush          12
            //  2353: bipush          15
            //  2355: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2358: aastore        
            //  2359: dup            
            //  2360: bipush          19
            //  2362: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2365: dup            
            //  2366: getstatic       net/minecraft/world/item/Items.YELLOW_BANNER:Lnet/minecraft/world/item/Item;
            //  2369: iconst_3       
            //  2370: iconst_1       
            //  2371: bipush          12
            //  2373: bipush          15
            //  2375: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2378: aastore        
            //  2379: dup            
            //  2380: bipush          20
            //  2382: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2385: dup            
            //  2386: getstatic       net/minecraft/world/item/Items.ORANGE_BANNER:Lnet/minecraft/world/item/Item;
            //  2389: iconst_3       
            //  2390: iconst_1       
            //  2391: bipush          12
            //  2393: bipush          15
            //  2395: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2398: aastore        
            //  2399: dup            
            //  2400: bipush          21
            //  2402: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2405: dup            
            //  2406: getstatic       net/minecraft/world/item/Items.LIGHT_GRAY_BANNER:Lnet/minecraft/world/item/Item;
            //  2409: iconst_3       
            //  2410: iconst_1       
            //  2411: bipush          12
            //  2413: bipush          15
            //  2415: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  2418: aastore        
            //  2419: iconst_5       
            //  2420: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2423: iconst_1       
            //  2424: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2427: dup            
            //  2428: iconst_0       
            //  2429: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2432: dup            
            //  2433: getstatic       net/minecraft/world/item/Items.PAINTING:Lnet/minecraft/world/item/Item;
            //  2436: iconst_2       
            //  2437: iconst_3       
            //  2438: bipush          30
            //  2440: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2443: aastore        
            //  2444: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  2447: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  2450: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  2453: pop            
            //  2454: aload_0         /* hashMap */
            //  2455: getstatic       net/minecraft/world/entity/npc/VillagerProfession.FLETCHER:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  2458: iconst_1       
            //  2459: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2462: iconst_3       
            //  2463: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2466: dup            
            //  2467: iconst_0       
            //  2468: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2471: dup            
            //  2472: getstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
            //  2475: bipush          32
            //  2477: bipush          16
            //  2479: iconst_2       
            //  2480: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2483: aastore        
            //  2484: dup            
            //  2485: iconst_1       
            //  2486: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2489: dup            
            //  2490: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
            //  2493: iconst_1       
            //  2494: bipush          16
            //  2496: iconst_1       
            //  2497: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2500: aastore        
            //  2501: dup            
            //  2502: iconst_2       
            //  2503: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsAndEmeraldsToItems;
            //  2506: dup            
            //  2507: getstatic       net/minecraft/world/level/block/Blocks.GRAVEL:Lnet/minecraft/world/level/block/Block;
            //  2510: bipush          10
            //  2512: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
            //  2515: bipush          10
            //  2517: bipush          12
            //  2519: iconst_1       
            //  2520: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsAndEmeraldsToItems.<init>:(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/world/item/Item;III)V
            //  2523: aastore        
            //  2524: iconst_2       
            //  2525: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2528: iconst_2       
            //  2529: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2532: dup            
            //  2533: iconst_0       
            //  2534: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2537: dup            
            //  2538: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
            //  2541: bipush          26
            //  2543: bipush          12
            //  2545: bipush          10
            //  2547: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2550: aastore        
            //  2551: dup            
            //  2552: iconst_1       
            //  2553: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2556: dup            
            //  2557: getstatic       net/minecraft/world/item/Items.BOW:Lnet/minecraft/world/item/Item;
            //  2560: iconst_2       
            //  2561: iconst_1       
            //  2562: iconst_5       
            //  2563: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2566: aastore        
            //  2567: iconst_3       
            //  2568: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2571: iconst_2       
            //  2572: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2575: dup            
            //  2576: iconst_0       
            //  2577: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2580: dup            
            //  2581: getstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
            //  2584: bipush          14
            //  2586: bipush          16
            //  2588: bipush          20
            //  2590: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2593: aastore        
            //  2594: dup            
            //  2595: iconst_1       
            //  2596: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2599: dup            
            //  2600: getstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
            //  2603: iconst_3       
            //  2604: iconst_1       
            //  2605: bipush          10
            //  2607: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2610: aastore        
            //  2611: iconst_4       
            //  2612: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2615: iconst_2       
            //  2616: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2619: dup            
            //  2620: iconst_0       
            //  2621: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2624: dup            
            //  2625: getstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
            //  2628: bipush          24
            //  2630: bipush          16
            //  2632: bipush          30
            //  2634: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2637: aastore        
            //  2638: dup            
            //  2639: iconst_1       
            //  2640: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  2643: dup            
            //  2644: getstatic       net/minecraft/world/item/Items.BOW:Lnet/minecraft/world/item/Item;
            //  2647: iconst_2       
            //  2648: iconst_3       
            //  2649: bipush          15
            //  2651: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2654: aastore        
            //  2655: iconst_5       
            //  2656: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2659: iconst_3       
            //  2660: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2663: dup            
            //  2664: iconst_0       
            //  2665: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2668: dup            
            //  2669: getstatic       net/minecraft/world/item/Items.TRIPWIRE_HOOK:Lnet/minecraft/world/item/Item;
            //  2672: bipush          8
            //  2674: bipush          12
            //  2676: bipush          30
            //  2678: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2681: aastore        
            //  2682: dup            
            //  2683: iconst_1       
            //  2684: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  2687: dup            
            //  2688: getstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
            //  2691: iconst_3       
            //  2692: iconst_3       
            //  2693: bipush          15
            //  2695: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2698: aastore        
            //  2699: dup            
            //  2700: iconst_2       
            //  2701: new             Lnet/minecraft/world/entity/npc/VillagerTrades$TippedArrowForItemsAndEmeralds;
            //  2704: dup            
            //  2705: getstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
            //  2708: iconst_5       
            //  2709: getstatic       net/minecraft/world/item/Items.TIPPED_ARROW:Lnet/minecraft/world/item/Item;
            //  2712: iconst_5       
            //  2713: iconst_2       
            //  2714: bipush          12
            //  2716: bipush          30
            //  2718: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$TippedArrowForItemsAndEmeralds.<init>:(Lnet/minecraft/world/item/Item;ILnet/minecraft/world/item/Item;IIII)V
            //  2721: aastore        
            //  2722: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  2725: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  2728: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  2731: pop            
            //  2732: aload_0         /* hashMap */
            //  2733: getstatic       net/minecraft/world/entity/npc/VillagerProfession.LIBRARIAN:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  2736: invokestatic    com/google/common/collect/ImmutableMap.builder:()Lcom/google/common/collect/ImmutableMap$Builder;
            //  2739: iconst_1       
            //  2740: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2743: iconst_3       
            //  2744: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2747: dup            
            //  2748: iconst_0       
            //  2749: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2752: dup            
            //  2753: getstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
            //  2756: bipush          24
            //  2758: bipush          16
            //  2760: iconst_2       
            //  2761: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2764: aastore        
            //  2765: dup            
            //  2766: iconst_1       
            //  2767: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds;
            //  2770: dup            
            //  2771: iconst_1       
            //  2772: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds.<init>:(I)V
            //  2775: aastore        
            //  2776: dup            
            //  2777: iconst_2       
            //  2778: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2781: dup            
            //  2782: getstatic       net/minecraft/world/level/block/Blocks.BOOKSHELF:Lnet/minecraft/world/level/block/Block;
            //  2785: bipush          9
            //  2787: iconst_1       
            //  2788: bipush          12
            //  2790: iconst_1       
            //  2791: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  2794: aastore        
            //  2795: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //  2798: iconst_2       
            //  2799: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2802: iconst_3       
            //  2803: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2806: dup            
            //  2807: iconst_0       
            //  2808: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2811: dup            
            //  2812: getstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
            //  2815: iconst_4       
            //  2816: bipush          12
            //  2818: bipush          10
            //  2820: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2823: aastore        
            //  2824: dup            
            //  2825: iconst_1       
            //  2826: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds;
            //  2829: dup            
            //  2830: iconst_5       
            //  2831: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds.<init>:(I)V
            //  2834: aastore        
            //  2835: dup            
            //  2836: iconst_2       
            //  2837: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2840: dup            
            //  2841: getstatic       net/minecraft/world/item/Items.LANTERN:Lnet/minecraft/world/item/Item;
            //  2844: iconst_1       
            //  2845: iconst_1       
            //  2846: iconst_5       
            //  2847: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2850: aastore        
            //  2851: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //  2854: iconst_3       
            //  2855: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2858: iconst_3       
            //  2859: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2862: dup            
            //  2863: iconst_0       
            //  2864: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2867: dup            
            //  2868: getstatic       net/minecraft/world/item/Items.INK_SAC:Lnet/minecraft/world/item/Item;
            //  2871: iconst_5       
            //  2872: bipush          12
            //  2874: bipush          20
            //  2876: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2879: aastore        
            //  2880: dup            
            //  2881: iconst_1       
            //  2882: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds;
            //  2885: dup            
            //  2886: bipush          10
            //  2888: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds.<init>:(I)V
            //  2891: aastore        
            //  2892: dup            
            //  2893: iconst_2       
            //  2894: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2897: dup            
            //  2898: getstatic       net/minecraft/world/item/Items.GLASS:Lnet/minecraft/world/item/Item;
            //  2901: iconst_1       
            //  2902: iconst_4       
            //  2903: bipush          10
            //  2905: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2908: aastore        
            //  2909: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //  2912: iconst_4       
            //  2913: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2916: iconst_4       
            //  2917: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2920: dup            
            //  2921: iconst_0       
            //  2922: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  2925: dup            
            //  2926: getstatic       net/minecraft/world/item/Items.WRITABLE_BOOK:Lnet/minecraft/world/item/Item;
            //  2929: iconst_2       
            //  2930: bipush          12
            //  2932: bipush          30
            //  2934: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  2937: aastore        
            //  2938: dup            
            //  2939: iconst_1       
            //  2940: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds;
            //  2943: dup            
            //  2944: bipush          15
            //  2946: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantBookForEmeralds.<init>:(I)V
            //  2949: aastore        
            //  2950: dup            
            //  2951: iconst_2       
            //  2952: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2955: dup            
            //  2956: getstatic       net/minecraft/world/item/Items.CLOCK:Lnet/minecraft/world/item/Item;
            //  2959: iconst_5       
            //  2960: iconst_1       
            //  2961: bipush          15
            //  2963: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2966: aastore        
            //  2967: dup            
            //  2968: iconst_3       
            //  2969: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  2972: dup            
            //  2973: getstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
            //  2976: iconst_4       
            //  2977: iconst_1       
            //  2978: bipush          15
            //  2980: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  2983: aastore        
            //  2984: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //  2987: iconst_5       
            //  2988: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  2991: iconst_1       
            //  2992: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  2995: dup            
            //  2996: iconst_0       
            //  2997: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3000: dup            
            //  3001: getstatic       net/minecraft/world/item/Items.NAME_TAG:Lnet/minecraft/world/item/Item;
            //  3004: bipush          20
            //  3006: iconst_1       
            //  3007: bipush          30
            //  3009: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3012: aastore        
            //  3013: invokevirtual   com/google/common/collect/ImmutableMap$Builder.put:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap$Builder;
            //  3016: invokevirtual   com/google/common/collect/ImmutableMap$Builder.build:()Lcom/google/common/collect/ImmutableMap;
            //  3019: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  3022: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  3025: pop            
            //  3026: aload_0         /* hashMap */
            //  3027: getstatic       net/minecraft/world/entity/npc/VillagerProfession.CARTOGRAPHER:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  3030: iconst_1       
            //  3031: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3034: iconst_2       
            //  3035: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3038: dup            
            //  3039: iconst_0       
            //  3040: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3043: dup            
            //  3044: getstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
            //  3047: bipush          24
            //  3049: bipush          16
            //  3051: iconst_2       
            //  3052: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3055: aastore        
            //  3056: dup            
            //  3057: iconst_1       
            //  3058: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3061: dup            
            //  3062: getstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
            //  3065: bipush          7
            //  3067: iconst_1       
            //  3068: iconst_1       
            //  3069: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3072: aastore        
            //  3073: iconst_2       
            //  3074: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3077: iconst_2       
            //  3078: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3081: dup            
            //  3082: iconst_0       
            //  3083: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3086: dup            
            //  3087: getstatic       net/minecraft/world/item/Items.GLASS_PANE:Lnet/minecraft/world/item/Item;
            //  3090: bipush          11
            //  3092: bipush          16
            //  3094: bipush          10
            //  3096: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3099: aastore        
            //  3100: dup            
            //  3101: iconst_1       
            //  3102: new             Lnet/minecraft/world/entity/npc/VillagerTrades$TreasureMapForEmeralds;
            //  3105: dup            
            //  3106: bipush          13
            //  3108: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.OCEAN_MONUMENT:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
            //  3111: getstatic       net/minecraft/world/level/saveddata/maps/MapDecoration$Type.MONUMENT:Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;
            //  3114: bipush          12
            //  3116: iconst_5       
            //  3117: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$TreasureMapForEmeralds.<init>:(ILnet/minecraft/world/level/levelgen/feature/StructureFeature;Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;II)V
            //  3120: aastore        
            //  3121: iconst_3       
            //  3122: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3125: iconst_2       
            //  3126: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3129: dup            
            //  3130: iconst_0       
            //  3131: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3134: dup            
            //  3135: getstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
            //  3138: iconst_1       
            //  3139: bipush          12
            //  3141: bipush          20
            //  3143: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3146: aastore        
            //  3147: dup            
            //  3148: iconst_1       
            //  3149: new             Lnet/minecraft/world/entity/npc/VillagerTrades$TreasureMapForEmeralds;
            //  3152: dup            
            //  3153: bipush          14
            //  3155: getstatic       net/minecraft/world/level/levelgen/feature/StructureFeature.WOODLAND_MANSION:Lnet/minecraft/world/level/levelgen/feature/StructureFeature;
            //  3158: getstatic       net/minecraft/world/level/saveddata/maps/MapDecoration$Type.MANSION:Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;
            //  3161: bipush          12
            //  3163: bipush          10
            //  3165: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$TreasureMapForEmeralds.<init>:(ILnet/minecraft/world/level/levelgen/feature/StructureFeature;Lnet/minecraft/world/level/saveddata/maps/MapDecoration$Type;II)V
            //  3168: aastore        
            //  3169: iconst_4       
            //  3170: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3173: bipush          17
            //  3175: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3178: dup            
            //  3179: iconst_0       
            //  3180: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3183: dup            
            //  3184: getstatic       net/minecraft/world/item/Items.ITEM_FRAME:Lnet/minecraft/world/item/Item;
            //  3187: bipush          7
            //  3189: iconst_1       
            //  3190: bipush          15
            //  3192: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3195: aastore        
            //  3196: dup            
            //  3197: iconst_1       
            //  3198: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3201: dup            
            //  3202: getstatic       net/minecraft/world/item/Items.WHITE_BANNER:Lnet/minecraft/world/item/Item;
            //  3205: iconst_3       
            //  3206: iconst_1       
            //  3207: bipush          15
            //  3209: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3212: aastore        
            //  3213: dup            
            //  3214: iconst_2       
            //  3215: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3218: dup            
            //  3219: getstatic       net/minecraft/world/item/Items.BLUE_BANNER:Lnet/minecraft/world/item/Item;
            //  3222: iconst_3       
            //  3223: iconst_1       
            //  3224: bipush          15
            //  3226: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3229: aastore        
            //  3230: dup            
            //  3231: iconst_3       
            //  3232: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3235: dup            
            //  3236: getstatic       net/minecraft/world/item/Items.LIGHT_BLUE_BANNER:Lnet/minecraft/world/item/Item;
            //  3239: iconst_3       
            //  3240: iconst_1       
            //  3241: bipush          15
            //  3243: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3246: aastore        
            //  3247: dup            
            //  3248: iconst_4       
            //  3249: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3252: dup            
            //  3253: getstatic       net/minecraft/world/item/Items.RED_BANNER:Lnet/minecraft/world/item/Item;
            //  3256: iconst_3       
            //  3257: iconst_1       
            //  3258: bipush          15
            //  3260: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3263: aastore        
            //  3264: dup            
            //  3265: iconst_5       
            //  3266: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3269: dup            
            //  3270: getstatic       net/minecraft/world/item/Items.PINK_BANNER:Lnet/minecraft/world/item/Item;
            //  3273: iconst_3       
            //  3274: iconst_1       
            //  3275: bipush          15
            //  3277: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3280: aastore        
            //  3281: dup            
            //  3282: bipush          6
            //  3284: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3287: dup            
            //  3288: getstatic       net/minecraft/world/item/Items.GREEN_BANNER:Lnet/minecraft/world/item/Item;
            //  3291: iconst_3       
            //  3292: iconst_1       
            //  3293: bipush          15
            //  3295: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3298: aastore        
            //  3299: dup            
            //  3300: bipush          7
            //  3302: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3305: dup            
            //  3306: getstatic       net/minecraft/world/item/Items.LIME_BANNER:Lnet/minecraft/world/item/Item;
            //  3309: iconst_3       
            //  3310: iconst_1       
            //  3311: bipush          15
            //  3313: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3316: aastore        
            //  3317: dup            
            //  3318: bipush          8
            //  3320: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3323: dup            
            //  3324: getstatic       net/minecraft/world/item/Items.GRAY_BANNER:Lnet/minecraft/world/item/Item;
            //  3327: iconst_3       
            //  3328: iconst_1       
            //  3329: bipush          15
            //  3331: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3334: aastore        
            //  3335: dup            
            //  3336: bipush          9
            //  3338: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3341: dup            
            //  3342: getstatic       net/minecraft/world/item/Items.BLACK_BANNER:Lnet/minecraft/world/item/Item;
            //  3345: iconst_3       
            //  3346: iconst_1       
            //  3347: bipush          15
            //  3349: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3352: aastore        
            //  3353: dup            
            //  3354: bipush          10
            //  3356: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3359: dup            
            //  3360: getstatic       net/minecraft/world/item/Items.PURPLE_BANNER:Lnet/minecraft/world/item/Item;
            //  3363: iconst_3       
            //  3364: iconst_1       
            //  3365: bipush          15
            //  3367: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3370: aastore        
            //  3371: dup            
            //  3372: bipush          11
            //  3374: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3377: dup            
            //  3378: getstatic       net/minecraft/world/item/Items.MAGENTA_BANNER:Lnet/minecraft/world/item/Item;
            //  3381: iconst_3       
            //  3382: iconst_1       
            //  3383: bipush          15
            //  3385: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3388: aastore        
            //  3389: dup            
            //  3390: bipush          12
            //  3392: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3395: dup            
            //  3396: getstatic       net/minecraft/world/item/Items.CYAN_BANNER:Lnet/minecraft/world/item/Item;
            //  3399: iconst_3       
            //  3400: iconst_1       
            //  3401: bipush          15
            //  3403: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3406: aastore        
            //  3407: dup            
            //  3408: bipush          13
            //  3410: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3413: dup            
            //  3414: getstatic       net/minecraft/world/item/Items.BROWN_BANNER:Lnet/minecraft/world/item/Item;
            //  3417: iconst_3       
            //  3418: iconst_1       
            //  3419: bipush          15
            //  3421: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3424: aastore        
            //  3425: dup            
            //  3426: bipush          14
            //  3428: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3431: dup            
            //  3432: getstatic       net/minecraft/world/item/Items.YELLOW_BANNER:Lnet/minecraft/world/item/Item;
            //  3435: iconst_3       
            //  3436: iconst_1       
            //  3437: bipush          15
            //  3439: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3442: aastore        
            //  3443: dup            
            //  3444: bipush          15
            //  3446: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3449: dup            
            //  3450: getstatic       net/minecraft/world/item/Items.ORANGE_BANNER:Lnet/minecraft/world/item/Item;
            //  3453: iconst_3       
            //  3454: iconst_1       
            //  3455: bipush          15
            //  3457: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3460: aastore        
            //  3461: dup            
            //  3462: bipush          16
            //  3464: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3467: dup            
            //  3468: getstatic       net/minecraft/world/item/Items.LIGHT_GRAY_BANNER:Lnet/minecraft/world/item/Item;
            //  3471: iconst_3       
            //  3472: iconst_1       
            //  3473: bipush          15
            //  3475: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3478: aastore        
            //  3479: iconst_5       
            //  3480: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3483: iconst_1       
            //  3484: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3487: dup            
            //  3488: iconst_0       
            //  3489: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3492: dup            
            //  3493: getstatic       net/minecraft/world/item/Items.GLOBE_BANNER_PATTER:Lnet/minecraft/world/item/Item;
            //  3496: bipush          8
            //  3498: iconst_1       
            //  3499: bipush          30
            //  3501: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3504: aastore        
            //  3505: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  3508: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  3511: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  3514: pop            
            //  3515: aload_0         /* hashMap */
            //  3516: getstatic       net/minecraft/world/entity/npc/VillagerProfession.CLERIC:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  3519: iconst_1       
            //  3520: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3523: iconst_2       
            //  3524: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3527: dup            
            //  3528: iconst_0       
            //  3529: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3532: dup            
            //  3533: getstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
            //  3536: bipush          32
            //  3538: bipush          16
            //  3540: iconst_2       
            //  3541: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3544: aastore        
            //  3545: dup            
            //  3546: iconst_1       
            //  3547: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3550: dup            
            //  3551: getstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
            //  3554: iconst_1       
            //  3555: iconst_2       
            //  3556: iconst_1       
            //  3557: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3560: aastore        
            //  3561: iconst_2       
            //  3562: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3565: iconst_2       
            //  3566: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3569: dup            
            //  3570: iconst_0       
            //  3571: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3574: dup            
            //  3575: getstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
            //  3578: iconst_3       
            //  3579: bipush          12
            //  3581: bipush          10
            //  3583: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3586: aastore        
            //  3587: dup            
            //  3588: iconst_1       
            //  3589: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3592: dup            
            //  3593: getstatic       net/minecraft/world/item/Items.LAPIS_LAZULI:Lnet/minecraft/world/item/Item;
            //  3596: iconst_1       
            //  3597: iconst_1       
            //  3598: iconst_5       
            //  3599: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3602: aastore        
            //  3603: iconst_3       
            //  3604: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3607: iconst_2       
            //  3608: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3611: dup            
            //  3612: iconst_0       
            //  3613: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3616: dup            
            //  3617: getstatic       net/minecraft/world/item/Items.RABBIT_FOOT:Lnet/minecraft/world/item/Item;
            //  3620: iconst_2       
            //  3621: bipush          12
            //  3623: bipush          20
            //  3625: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3628: aastore        
            //  3629: dup            
            //  3630: iconst_1       
            //  3631: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3634: dup            
            //  3635: getstatic       net/minecraft/world/level/block/Blocks.GLOWSTONE:Lnet/minecraft/world/level/block/Block;
            //  3638: iconst_4       
            //  3639: iconst_1       
            //  3640: bipush          12
            //  3642: bipush          10
            //  3644: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  3647: aastore        
            //  3648: iconst_4       
            //  3649: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3652: iconst_3       
            //  3653: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3656: dup            
            //  3657: iconst_0       
            //  3658: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3661: dup            
            //  3662: getstatic       net/minecraft/world/item/Items.SCUTE:Lnet/minecraft/world/item/Item;
            //  3665: iconst_4       
            //  3666: bipush          12
            //  3668: bipush          30
            //  3670: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3673: aastore        
            //  3674: dup            
            //  3675: iconst_1       
            //  3676: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3679: dup            
            //  3680: getstatic       net/minecraft/world/item/Items.GLASS_BOTTLE:Lnet/minecraft/world/item/Item;
            //  3683: bipush          9
            //  3685: bipush          12
            //  3687: bipush          30
            //  3689: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3692: aastore        
            //  3693: dup            
            //  3694: iconst_2       
            //  3695: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3698: dup            
            //  3699: getstatic       net/minecraft/world/item/Items.ENDER_PEARL:Lnet/minecraft/world/item/Item;
            //  3702: iconst_5       
            //  3703: iconst_1       
            //  3704: bipush          15
            //  3706: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3709: aastore        
            //  3710: iconst_5       
            //  3711: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3714: iconst_2       
            //  3715: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3718: dup            
            //  3719: iconst_0       
            //  3720: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3723: dup            
            //  3724: getstatic       net/minecraft/world/item/Items.NETHER_WART:Lnet/minecraft/world/item/Item;
            //  3727: bipush          22
            //  3729: bipush          12
            //  3731: bipush          30
            //  3733: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3736: aastore        
            //  3737: dup            
            //  3738: iconst_1       
            //  3739: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3742: dup            
            //  3743: getstatic       net/minecraft/world/item/Items.EXPERIENCE_BOTTLE:Lnet/minecraft/world/item/Item;
            //  3746: iconst_3       
            //  3747: iconst_1       
            //  3748: bipush          30
            //  3750: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  3753: aastore        
            //  3754: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  3757: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  3760: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  3763: pop            
            //  3764: aload_0         /* hashMap */
            //  3765: getstatic       net/minecraft/world/entity/npc/VillagerProfession.ARMORER:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  3768: iconst_1       
            //  3769: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3772: iconst_5       
            //  3773: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3776: dup            
            //  3777: iconst_0       
            //  3778: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3781: dup            
            //  3782: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
            //  3785: bipush          15
            //  3787: bipush          16
            //  3789: iconst_2       
            //  3790: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3793: aastore        
            //  3794: dup            
            //  3795: iconst_1       
            //  3796: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3799: dup            
            //  3800: new             Lnet/minecraft/world/item/ItemStack;
            //  3803: dup            
            //  3804: getstatic       net/minecraft/world/item/Items.IRON_LEGGINGS:Lnet/minecraft/world/item/Item;
            //  3807: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  3810: bipush          7
            //  3812: iconst_1       
            //  3813: bipush          12
            //  3815: iconst_1       
            //  3816: ldc             0.2
            //  3818: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  3821: aastore        
            //  3822: dup            
            //  3823: iconst_2       
            //  3824: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3827: dup            
            //  3828: new             Lnet/minecraft/world/item/ItemStack;
            //  3831: dup            
            //  3832: getstatic       net/minecraft/world/item/Items.IRON_BOOTS:Lnet/minecraft/world/item/Item;
            //  3835: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  3838: iconst_4       
            //  3839: iconst_1       
            //  3840: bipush          12
            //  3842: iconst_1       
            //  3843: ldc             0.2
            //  3845: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  3848: aastore        
            //  3849: dup            
            //  3850: iconst_3       
            //  3851: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3854: dup            
            //  3855: new             Lnet/minecraft/world/item/ItemStack;
            //  3858: dup            
            //  3859: getstatic       net/minecraft/world/item/Items.IRON_HELMET:Lnet/minecraft/world/item/Item;
            //  3862: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  3865: iconst_5       
            //  3866: iconst_1       
            //  3867: bipush          12
            //  3869: iconst_1       
            //  3870: ldc             0.2
            //  3872: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  3875: aastore        
            //  3876: dup            
            //  3877: iconst_4       
            //  3878: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3881: dup            
            //  3882: new             Lnet/minecraft/world/item/ItemStack;
            //  3885: dup            
            //  3886: getstatic       net/minecraft/world/item/Items.IRON_CHESTPLATE:Lnet/minecraft/world/item/Item;
            //  3889: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  3892: bipush          9
            //  3894: iconst_1       
            //  3895: bipush          12
            //  3897: iconst_1       
            //  3898: ldc             0.2
            //  3900: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  3903: aastore        
            //  3904: iconst_2       
            //  3905: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  3908: iconst_4       
            //  3909: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  3912: dup            
            //  3913: iconst_0       
            //  3914: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  3917: dup            
            //  3918: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
            //  3921: iconst_4       
            //  3922: bipush          12
            //  3924: bipush          10
            //  3926: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  3929: aastore        
            //  3930: dup            
            //  3931: iconst_1       
            //  3932: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3935: dup            
            //  3936: new             Lnet/minecraft/world/item/ItemStack;
            //  3939: dup            
            //  3940: getstatic       net/minecraft/world/item/Items.BELL:Lnet/minecraft/world/item/Item;
            //  3943: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  3946: bipush          36
            //  3948: iconst_1       
            //  3949: bipush          12
            //  3951: iconst_5       
            //  3952: ldc             0.2
            //  3954: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  3957: aastore        
            //  3958: dup            
            //  3959: iconst_2       
            //  3960: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3963: dup            
            //  3964: new             Lnet/minecraft/world/item/ItemStack;
            //  3967: dup            
            //  3968: getstatic       net/minecraft/world/item/Items.CHAINMAIL_BOOTS:Lnet/minecraft/world/item/Item;
            //  3971: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  3974: iconst_1       
            //  3975: iconst_1       
            //  3976: bipush          12
            //  3978: iconst_5       
            //  3979: ldc             0.2
            //  3981: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  3984: aastore        
            //  3985: dup            
            //  3986: iconst_3       
            //  3987: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  3990: dup            
            //  3991: new             Lnet/minecraft/world/item/ItemStack;
            //  3994: dup            
            //  3995: getstatic       net/minecraft/world/item/Items.CHAINMAIL_LEGGINGS:Lnet/minecraft/world/item/Item;
            //  3998: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4001: iconst_3       
            //  4002: iconst_1       
            //  4003: bipush          12
            //  4005: iconst_5       
            //  4006: ldc             0.2
            //  4008: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4011: aastore        
            //  4012: iconst_3       
            //  4013: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4016: iconst_5       
            //  4017: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4020: dup            
            //  4021: iconst_0       
            //  4022: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4025: dup            
            //  4026: getstatic       net/minecraft/world/item/Items.LAVA_BUCKET:Lnet/minecraft/world/item/Item;
            //  4029: iconst_1       
            //  4030: bipush          12
            //  4032: bipush          20
            //  4034: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4037: aastore        
            //  4038: dup            
            //  4039: iconst_1       
            //  4040: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4043: dup            
            //  4044: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
            //  4047: iconst_1       
            //  4048: bipush          12
            //  4050: bipush          20
            //  4052: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4055: aastore        
            //  4056: dup            
            //  4057: iconst_2       
            //  4058: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4061: dup            
            //  4062: new             Lnet/minecraft/world/item/ItemStack;
            //  4065: dup            
            //  4066: getstatic       net/minecraft/world/item/Items.CHAINMAIL_HELMET:Lnet/minecraft/world/item/Item;
            //  4069: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4072: iconst_1       
            //  4073: iconst_1       
            //  4074: bipush          12
            //  4076: bipush          10
            //  4078: ldc             0.2
            //  4080: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4083: aastore        
            //  4084: dup            
            //  4085: iconst_3       
            //  4086: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4089: dup            
            //  4090: new             Lnet/minecraft/world/item/ItemStack;
            //  4093: dup            
            //  4094: getstatic       net/minecraft/world/item/Items.CHAINMAIL_CHESTPLATE:Lnet/minecraft/world/item/Item;
            //  4097: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4100: iconst_4       
            //  4101: iconst_1       
            //  4102: bipush          12
            //  4104: bipush          10
            //  4106: ldc             0.2
            //  4108: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4111: aastore        
            //  4112: dup            
            //  4113: iconst_4       
            //  4114: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4117: dup            
            //  4118: new             Lnet/minecraft/world/item/ItemStack;
            //  4121: dup            
            //  4122: getstatic       net/minecraft/world/item/Items.SHIELD:Lnet/minecraft/world/item/Item;
            //  4125: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4128: iconst_5       
            //  4129: iconst_1       
            //  4130: bipush          12
            //  4132: bipush          10
            //  4134: ldc             0.2
            //  4136: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4139: aastore        
            //  4140: iconst_4       
            //  4141: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4144: iconst_2       
            //  4145: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4148: dup            
            //  4149: iconst_0       
            //  4150: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4153: dup            
            //  4154: getstatic       net/minecraft/world/item/Items.DIAMOND_LEGGINGS:Lnet/minecraft/world/item/Item;
            //  4157: bipush          14
            //  4159: iconst_3       
            //  4160: bipush          15
            //  4162: ldc             0.2
            //  4164: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4167: aastore        
            //  4168: dup            
            //  4169: iconst_1       
            //  4170: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4173: dup            
            //  4174: getstatic       net/minecraft/world/item/Items.DIAMOND_BOOTS:Lnet/minecraft/world/item/Item;
            //  4177: bipush          8
            //  4179: iconst_3       
            //  4180: bipush          15
            //  4182: ldc             0.2
            //  4184: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4187: aastore        
            //  4188: iconst_5       
            //  4189: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4192: iconst_2       
            //  4193: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4196: dup            
            //  4197: iconst_0       
            //  4198: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4201: dup            
            //  4202: getstatic       net/minecraft/world/item/Items.DIAMOND_HELMET:Lnet/minecraft/world/item/Item;
            //  4205: bipush          8
            //  4207: iconst_3       
            //  4208: bipush          30
            //  4210: ldc             0.2
            //  4212: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4215: aastore        
            //  4216: dup            
            //  4217: iconst_1       
            //  4218: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4221: dup            
            //  4222: getstatic       net/minecraft/world/item/Items.DIAMOND_CHESTPLATE:Lnet/minecraft/world/item/Item;
            //  4225: bipush          16
            //  4227: iconst_3       
            //  4228: bipush          30
            //  4230: ldc             0.2
            //  4232: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4235: aastore        
            //  4236: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  4239: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  4242: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  4245: pop            
            //  4246: aload_0         /* hashMap */
            //  4247: getstatic       net/minecraft/world/entity/npc/VillagerProfession.WEAPONSMITH:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  4250: iconst_1       
            //  4251: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4254: iconst_3       
            //  4255: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4258: dup            
            //  4259: iconst_0       
            //  4260: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4263: dup            
            //  4264: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
            //  4267: bipush          15
            //  4269: bipush          16
            //  4271: iconst_2       
            //  4272: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4275: aastore        
            //  4276: dup            
            //  4277: iconst_1       
            //  4278: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4281: dup            
            //  4282: new             Lnet/minecraft/world/item/ItemStack;
            //  4285: dup            
            //  4286: getstatic       net/minecraft/world/item/Items.IRON_AXE:Lnet/minecraft/world/item/Item;
            //  4289: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4292: iconst_3       
            //  4293: iconst_1       
            //  4294: bipush          12
            //  4296: iconst_1       
            //  4297: ldc             0.2
            //  4299: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4302: aastore        
            //  4303: dup            
            //  4304: iconst_2       
            //  4305: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4308: dup            
            //  4309: getstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
            //  4312: iconst_2       
            //  4313: iconst_3       
            //  4314: iconst_1       
            //  4315: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  4318: aastore        
            //  4319: iconst_2       
            //  4320: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4323: iconst_2       
            //  4324: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4327: dup            
            //  4328: iconst_0       
            //  4329: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4332: dup            
            //  4333: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
            //  4336: iconst_4       
            //  4337: bipush          12
            //  4339: bipush          10
            //  4341: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4344: aastore        
            //  4345: dup            
            //  4346: iconst_1       
            //  4347: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4350: dup            
            //  4351: new             Lnet/minecraft/world/item/ItemStack;
            //  4354: dup            
            //  4355: getstatic       net/minecraft/world/item/Items.BELL:Lnet/minecraft/world/item/Item;
            //  4358: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4361: bipush          36
            //  4363: iconst_1       
            //  4364: bipush          12
            //  4366: iconst_5       
            //  4367: ldc             0.2
            //  4369: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4372: aastore        
            //  4373: iconst_3       
            //  4374: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4377: iconst_1       
            //  4378: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4381: dup            
            //  4382: iconst_0       
            //  4383: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4386: dup            
            //  4387: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
            //  4390: bipush          24
            //  4392: bipush          12
            //  4394: bipush          20
            //  4396: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4399: aastore        
            //  4400: iconst_4       
            //  4401: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4404: iconst_2       
            //  4405: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4408: dup            
            //  4409: iconst_0       
            //  4410: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4413: dup            
            //  4414: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
            //  4417: iconst_1       
            //  4418: bipush          12
            //  4420: bipush          30
            //  4422: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4425: aastore        
            //  4426: dup            
            //  4427: iconst_1       
            //  4428: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4431: dup            
            //  4432: getstatic       net/minecraft/world/item/Items.DIAMOND_AXE:Lnet/minecraft/world/item/Item;
            //  4435: bipush          12
            //  4437: iconst_3       
            //  4438: bipush          15
            //  4440: ldc             0.2
            //  4442: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4445: aastore        
            //  4446: iconst_5       
            //  4447: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4450: iconst_1       
            //  4451: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4454: dup            
            //  4455: iconst_0       
            //  4456: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4459: dup            
            //  4460: getstatic       net/minecraft/world/item/Items.DIAMOND_SWORD:Lnet/minecraft/world/item/Item;
            //  4463: bipush          8
            //  4465: iconst_3       
            //  4466: bipush          30
            //  4468: ldc             0.2
            //  4470: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4473: aastore        
            //  4474: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  4477: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  4480: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  4483: pop            
            //  4484: aload_0         /* hashMap */
            //  4485: getstatic       net/minecraft/world/entity/npc/VillagerProfession.TOOLSMITH:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  4488: iconst_1       
            //  4489: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4492: iconst_5       
            //  4493: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4496: dup            
            //  4497: iconst_0       
            //  4498: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4501: dup            
            //  4502: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
            //  4505: bipush          15
            //  4507: bipush          16
            //  4509: iconst_2       
            //  4510: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4513: aastore        
            //  4514: dup            
            //  4515: iconst_1       
            //  4516: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4519: dup            
            //  4520: new             Lnet/minecraft/world/item/ItemStack;
            //  4523: dup            
            //  4524: getstatic       net/minecraft/world/item/Items.STONE_AXE:Lnet/minecraft/world/item/Item;
            //  4527: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4530: iconst_1       
            //  4531: iconst_1       
            //  4532: bipush          12
            //  4534: iconst_1       
            //  4535: ldc             0.2
            //  4537: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4540: aastore        
            //  4541: dup            
            //  4542: iconst_2       
            //  4543: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4546: dup            
            //  4547: new             Lnet/minecraft/world/item/ItemStack;
            //  4550: dup            
            //  4551: getstatic       net/minecraft/world/item/Items.STONE_SHOVEL:Lnet/minecraft/world/item/Item;
            //  4554: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4557: iconst_1       
            //  4558: iconst_1       
            //  4559: bipush          12
            //  4561: iconst_1       
            //  4562: ldc             0.2
            //  4564: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4567: aastore        
            //  4568: dup            
            //  4569: iconst_3       
            //  4570: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4573: dup            
            //  4574: new             Lnet/minecraft/world/item/ItemStack;
            //  4577: dup            
            //  4578: getstatic       net/minecraft/world/item/Items.STONE_PICKAXE:Lnet/minecraft/world/item/Item;
            //  4581: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4584: iconst_1       
            //  4585: iconst_1       
            //  4586: bipush          12
            //  4588: iconst_1       
            //  4589: ldc             0.2
            //  4591: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4594: aastore        
            //  4595: dup            
            //  4596: iconst_4       
            //  4597: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4600: dup            
            //  4601: new             Lnet/minecraft/world/item/ItemStack;
            //  4604: dup            
            //  4605: getstatic       net/minecraft/world/item/Items.STONE_HOE:Lnet/minecraft/world/item/Item;
            //  4608: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4611: iconst_1       
            //  4612: iconst_1       
            //  4613: bipush          12
            //  4615: iconst_1       
            //  4616: ldc             0.2
            //  4618: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4621: aastore        
            //  4622: iconst_2       
            //  4623: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4626: iconst_2       
            //  4627: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4630: dup            
            //  4631: iconst_0       
            //  4632: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4635: dup            
            //  4636: getstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
            //  4639: iconst_4       
            //  4640: bipush          12
            //  4642: bipush          10
            //  4644: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4647: aastore        
            //  4648: dup            
            //  4649: iconst_1       
            //  4650: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4653: dup            
            //  4654: new             Lnet/minecraft/world/item/ItemStack;
            //  4657: dup            
            //  4658: getstatic       net/minecraft/world/item/Items.BELL:Lnet/minecraft/world/item/Item;
            //  4661: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4664: bipush          36
            //  4666: iconst_1       
            //  4667: bipush          12
            //  4669: iconst_5       
            //  4670: ldc             0.2
            //  4672: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4675: aastore        
            //  4676: iconst_3       
            //  4677: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4680: iconst_5       
            //  4681: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4684: dup            
            //  4685: iconst_0       
            //  4686: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4689: dup            
            //  4690: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
            //  4693: bipush          30
            //  4695: bipush          12
            //  4697: bipush          20
            //  4699: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4702: aastore        
            //  4703: dup            
            //  4704: iconst_1       
            //  4705: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4708: dup            
            //  4709: getstatic       net/minecraft/world/item/Items.IRON_AXE:Lnet/minecraft/world/item/Item;
            //  4712: iconst_1       
            //  4713: iconst_3       
            //  4714: bipush          10
            //  4716: ldc             0.2
            //  4718: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4721: aastore        
            //  4722: dup            
            //  4723: iconst_2       
            //  4724: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4727: dup            
            //  4728: getstatic       net/minecraft/world/item/Items.IRON_SHOVEL:Lnet/minecraft/world/item/Item;
            //  4731: iconst_2       
            //  4732: iconst_3       
            //  4733: bipush          10
            //  4735: ldc             0.2
            //  4737: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4740: aastore        
            //  4741: dup            
            //  4742: iconst_3       
            //  4743: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4746: dup            
            //  4747: getstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
            //  4750: iconst_3       
            //  4751: iconst_3       
            //  4752: bipush          10
            //  4754: ldc             0.2
            //  4756: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4759: aastore        
            //  4760: dup            
            //  4761: iconst_4       
            //  4762: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4765: dup            
            //  4766: new             Lnet/minecraft/world/item/ItemStack;
            //  4769: dup            
            //  4770: getstatic       net/minecraft/world/item/Items.DIAMOND_HOE:Lnet/minecraft/world/item/Item;
            //  4773: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  4776: iconst_4       
            //  4777: iconst_1       
            //  4778: iconst_3       
            //  4779: bipush          10
            //  4781: ldc             0.2
            //  4783: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  4786: aastore        
            //  4787: iconst_4       
            //  4788: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4791: iconst_3       
            //  4792: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4795: dup            
            //  4796: iconst_0       
            //  4797: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4800: dup            
            //  4801: getstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
            //  4804: iconst_1       
            //  4805: bipush          12
            //  4807: bipush          30
            //  4809: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4812: aastore        
            //  4813: dup            
            //  4814: iconst_1       
            //  4815: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4818: dup            
            //  4819: getstatic       net/minecraft/world/item/Items.DIAMOND_AXE:Lnet/minecraft/world/item/Item;
            //  4822: bipush          12
            //  4824: iconst_3       
            //  4825: bipush          15
            //  4827: ldc             0.2
            //  4829: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4832: aastore        
            //  4833: dup            
            //  4834: iconst_2       
            //  4835: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4838: dup            
            //  4839: getstatic       net/minecraft/world/item/Items.DIAMOND_SHOVEL:Lnet/minecraft/world/item/Item;
            //  4842: iconst_5       
            //  4843: iconst_3       
            //  4844: bipush          15
            //  4846: ldc             0.2
            //  4848: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4851: aastore        
            //  4852: iconst_5       
            //  4853: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4856: iconst_1       
            //  4857: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4860: dup            
            //  4861: iconst_0       
            //  4862: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds;
            //  4865: dup            
            //  4866: getstatic       net/minecraft/world/item/Items.DIAMOND_PICKAXE:Lnet/minecraft/world/item/Item;
            //  4869: bipush          13
            //  4871: iconst_3       
            //  4872: bipush          30
            //  4874: ldc             0.2
            //  4876: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EnchantedItemForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIIF)V
            //  4879: aastore        
            //  4880: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  4883: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  4886: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  4889: pop            
            //  4890: aload_0         /* hashMap */
            //  4891: getstatic       net/minecraft/world/entity/npc/VillagerProfession.BUTCHER:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  4894: iconst_1       
            //  4895: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4898: iconst_4       
            //  4899: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4902: dup            
            //  4903: iconst_0       
            //  4904: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4907: dup            
            //  4908: getstatic       net/minecraft/world/item/Items.CHICKEN:Lnet/minecraft/world/item/Item;
            //  4911: bipush          14
            //  4913: bipush          16
            //  4915: iconst_2       
            //  4916: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4919: aastore        
            //  4920: dup            
            //  4921: iconst_1       
            //  4922: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4925: dup            
            //  4926: getstatic       net/minecraft/world/item/Items.PORKCHOP:Lnet/minecraft/world/item/Item;
            //  4929: bipush          7
            //  4931: bipush          16
            //  4933: iconst_2       
            //  4934: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4937: aastore        
            //  4938: dup            
            //  4939: iconst_2       
            //  4940: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4943: dup            
            //  4944: getstatic       net/minecraft/world/item/Items.RABBIT:Lnet/minecraft/world/item/Item;
            //  4947: iconst_4       
            //  4948: bipush          16
            //  4950: iconst_2       
            //  4951: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4954: aastore        
            //  4955: dup            
            //  4956: iconst_3       
            //  4957: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  4960: dup            
            //  4961: getstatic       net/minecraft/world/item/Items.RABBIT_STEW:Lnet/minecraft/world/item/Item;
            //  4964: iconst_1       
            //  4965: iconst_1       
            //  4966: iconst_1       
            //  4967: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  4970: aastore        
            //  4971: iconst_2       
            //  4972: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  4975: iconst_3       
            //  4976: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  4979: dup            
            //  4980: iconst_0       
            //  4981: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  4984: dup            
            //  4985: getstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
            //  4988: bipush          15
            //  4990: bipush          16
            //  4992: iconst_2       
            //  4993: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  4996: aastore        
            //  4997: dup            
            //  4998: iconst_1       
            //  4999: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5002: dup            
            //  5003: getstatic       net/minecraft/world/item/Items.COOKED_PORKCHOP:Lnet/minecraft/world/item/Item;
            //  5006: iconst_1       
            //  5007: iconst_5       
            //  5008: bipush          16
            //  5010: iconst_5       
            //  5011: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  5014: aastore        
            //  5015: dup            
            //  5016: iconst_2       
            //  5017: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5020: dup            
            //  5021: getstatic       net/minecraft/world/item/Items.COOKED_CHICKEN:Lnet/minecraft/world/item/Item;
            //  5024: iconst_1       
            //  5025: bipush          8
            //  5027: bipush          16
            //  5029: iconst_5       
            //  5030: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  5033: aastore        
            //  5034: iconst_3       
            //  5035: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5038: iconst_2       
            //  5039: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5042: dup            
            //  5043: iconst_0       
            //  5044: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5047: dup            
            //  5048: getstatic       net/minecraft/world/item/Items.MUTTON:Lnet/minecraft/world/item/Item;
            //  5051: bipush          7
            //  5053: bipush          16
            //  5055: bipush          20
            //  5057: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5060: aastore        
            //  5061: dup            
            //  5062: iconst_1       
            //  5063: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5066: dup            
            //  5067: getstatic       net/minecraft/world/item/Items.BEEF:Lnet/minecraft/world/item/Item;
            //  5070: bipush          10
            //  5072: bipush          16
            //  5074: bipush          20
            //  5076: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5079: aastore        
            //  5080: iconst_4       
            //  5081: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5084: iconst_1       
            //  5085: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5088: dup            
            //  5089: iconst_0       
            //  5090: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5093: dup            
            //  5094: getstatic       net/minecraft/world/item/Items.DRIED_KELP_BLOCK:Lnet/minecraft/world/item/Item;
            //  5097: bipush          10
            //  5099: bipush          12
            //  5101: bipush          30
            //  5103: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5106: aastore        
            //  5107: iconst_5       
            //  5108: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5111: iconst_1       
            //  5112: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5115: dup            
            //  5116: iconst_0       
            //  5117: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5120: dup            
            //  5121: getstatic       net/minecraft/world/item/Items.SWEET_BERRIES:Lnet/minecraft/world/item/Item;
            //  5124: bipush          10
            //  5126: bipush          12
            //  5128: bipush          30
            //  5130: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5133: aastore        
            //  5134: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  5137: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  5140: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  5143: pop            
            //  5144: aload_0         /* hashMap */
            //  5145: getstatic       net/minecraft/world/entity/npc/VillagerProfession.LEATHERWORKER:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  5148: iconst_1       
            //  5149: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5152: iconst_3       
            //  5153: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5156: dup            
            //  5157: iconst_0       
            //  5158: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5161: dup            
            //  5162: getstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
            //  5165: bipush          6
            //  5167: bipush          16
            //  5169: iconst_2       
            //  5170: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5173: aastore        
            //  5174: dup            
            //  5175: iconst_1       
            //  5176: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5179: dup            
            //  5180: getstatic       net/minecraft/world/item/Items.LEATHER_LEGGINGS:Lnet/minecraft/world/item/Item;
            //  5183: iconst_3       
            //  5184: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;I)V
            //  5187: aastore        
            //  5188: dup            
            //  5189: iconst_2       
            //  5190: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5193: dup            
            //  5194: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
            //  5197: bipush          7
            //  5199: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;I)V
            //  5202: aastore        
            //  5203: iconst_2       
            //  5204: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5207: iconst_3       
            //  5208: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5211: dup            
            //  5212: iconst_0       
            //  5213: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5216: dup            
            //  5217: getstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
            //  5220: bipush          26
            //  5222: bipush          12
            //  5224: bipush          10
            //  5226: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5229: aastore        
            //  5230: dup            
            //  5231: iconst_1       
            //  5232: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5235: dup            
            //  5236: getstatic       net/minecraft/world/item/Items.LEATHER_HELMET:Lnet/minecraft/world/item/Item;
            //  5239: iconst_5       
            //  5240: bipush          12
            //  5242: iconst_5       
            //  5243: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  5246: aastore        
            //  5247: dup            
            //  5248: iconst_2       
            //  5249: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5252: dup            
            //  5253: getstatic       net/minecraft/world/item/Items.LEATHER_BOOTS:Lnet/minecraft/world/item/Item;
            //  5256: iconst_4       
            //  5257: bipush          12
            //  5259: iconst_5       
            //  5260: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  5263: aastore        
            //  5264: iconst_3       
            //  5265: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5268: iconst_2       
            //  5269: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5272: dup            
            //  5273: iconst_0       
            //  5274: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5277: dup            
            //  5278: getstatic       net/minecraft/world/item/Items.RABBIT_HIDE:Lnet/minecraft/world/item/Item;
            //  5281: bipush          9
            //  5283: bipush          12
            //  5285: bipush          20
            //  5287: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5290: aastore        
            //  5291: dup            
            //  5292: iconst_1       
            //  5293: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5296: dup            
            //  5297: getstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
            //  5300: bipush          7
            //  5302: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;I)V
            //  5305: aastore        
            //  5306: iconst_4       
            //  5307: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5310: iconst_2       
            //  5311: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5314: dup            
            //  5315: iconst_0       
            //  5316: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5319: dup            
            //  5320: getstatic       net/minecraft/world/item/Items.SCUTE:Lnet/minecraft/world/item/Item;
            //  5323: iconst_4       
            //  5324: bipush          12
            //  5326: bipush          30
            //  5328: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5331: aastore        
            //  5332: dup            
            //  5333: iconst_1       
            //  5334: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5337: dup            
            //  5338: getstatic       net/minecraft/world/item/Items.LEATHER_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
            //  5341: bipush          6
            //  5343: bipush          12
            //  5345: bipush          15
            //  5347: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  5350: aastore        
            //  5351: iconst_5       
            //  5352: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5355: iconst_2       
            //  5356: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5359: dup            
            //  5360: iconst_0       
            //  5361: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5364: dup            
            //  5365: new             Lnet/minecraft/world/item/ItemStack;
            //  5368: dup            
            //  5369: getstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
            //  5372: invokespecial   net/minecraft/world/item/ItemStack.<init>:(Lnet/minecraft/world/level/ItemLike;)V
            //  5375: bipush          6
            //  5377: iconst_1       
            //  5378: bipush          12
            //  5380: bipush          30
            //  5382: ldc             0.2
            //  5384: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/ItemStack;IIIIF)V
            //  5387: aastore        
            //  5388: dup            
            //  5389: iconst_1       
            //  5390: new             Lnet/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds;
            //  5393: dup            
            //  5394: getstatic       net/minecraft/world/item/Items.LEATHER_HELMET:Lnet/minecraft/world/item/Item;
            //  5397: iconst_5       
            //  5398: bipush          12
            //  5400: bipush          30
            //  5402: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$DyedArmorForEmeralds.<init>:(Lnet/minecraft/world/item/Item;III)V
            //  5405: aastore        
            //  5406: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  5409: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  5412: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  5415: pop            
            //  5416: aload_0         /* hashMap */
            //  5417: getstatic       net/minecraft/world/entity/npc/VillagerProfession.MASON:Lnet/minecraft/world/entity/npc/VillagerProfession;
            //  5420: iconst_1       
            //  5421: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5424: iconst_2       
            //  5425: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5428: dup            
            //  5429: iconst_0       
            //  5430: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5433: dup            
            //  5434: getstatic       net/minecraft/world/item/Items.CLAY_BALL:Lnet/minecraft/world/item/Item;
            //  5437: bipush          10
            //  5439: bipush          16
            //  5441: iconst_2       
            //  5442: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5445: aastore        
            //  5446: dup            
            //  5447: iconst_1       
            //  5448: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5451: dup            
            //  5452: getstatic       net/minecraft/world/item/Items.BRICK:Lnet/minecraft/world/item/Item;
            //  5455: iconst_1       
            //  5456: bipush          10
            //  5458: bipush          16
            //  5460: iconst_1       
            //  5461: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/item/Item;IIII)V
            //  5464: aastore        
            //  5465: iconst_2       
            //  5466: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5469: iconst_2       
            //  5470: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5473: dup            
            //  5474: iconst_0       
            //  5475: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5478: dup            
            //  5479: getstatic       net/minecraft/world/level/block/Blocks.STONE:Lnet/minecraft/world/level/block/Block;
            //  5482: bipush          20
            //  5484: bipush          16
            //  5486: bipush          10
            //  5488: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5491: aastore        
            //  5492: dup            
            //  5493: iconst_1       
            //  5494: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5497: dup            
            //  5498: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
            //  5501: iconst_1       
            //  5502: iconst_4       
            //  5503: bipush          16
            //  5505: iconst_5       
            //  5506: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5509: aastore        
            //  5510: iconst_3       
            //  5511: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5514: bipush          6
            //  5516: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5519: dup            
            //  5520: iconst_0       
            //  5521: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5524: dup            
            //  5525: getstatic       net/minecraft/world/level/block/Blocks.GRANITE:Lnet/minecraft/world/level/block/Block;
            //  5528: bipush          16
            //  5530: bipush          16
            //  5532: bipush          20
            //  5534: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5537: aastore        
            //  5538: dup            
            //  5539: iconst_1       
            //  5540: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5543: dup            
            //  5544: getstatic       net/minecraft/world/level/block/Blocks.ANDESITE:Lnet/minecraft/world/level/block/Block;
            //  5547: bipush          16
            //  5549: bipush          16
            //  5551: bipush          20
            //  5553: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5556: aastore        
            //  5557: dup            
            //  5558: iconst_2       
            //  5559: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5562: dup            
            //  5563: getstatic       net/minecraft/world/level/block/Blocks.DIORITE:Lnet/minecraft/world/level/block/Block;
            //  5566: bipush          16
            //  5568: bipush          16
            //  5570: bipush          20
            //  5572: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5575: aastore        
            //  5576: dup            
            //  5577: iconst_3       
            //  5578: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5581: dup            
            //  5582: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_ANDESITE:Lnet/minecraft/world/level/block/Block;
            //  5585: iconst_1       
            //  5586: iconst_4       
            //  5587: bipush          16
            //  5589: bipush          10
            //  5591: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5594: aastore        
            //  5595: dup            
            //  5596: iconst_4       
            //  5597: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5600: dup            
            //  5601: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_DIORITE:Lnet/minecraft/world/level/block/Block;
            //  5604: iconst_1       
            //  5605: iconst_4       
            //  5606: bipush          16
            //  5608: bipush          10
            //  5610: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5613: aastore        
            //  5614: dup            
            //  5615: iconst_5       
            //  5616: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5619: dup            
            //  5620: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_GRANITE:Lnet/minecraft/world/level/block/Block;
            //  5623: iconst_1       
            //  5624: iconst_4       
            //  5625: bipush          16
            //  5627: bipush          10
            //  5629: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5632: aastore        
            //  5633: iconst_4       
            //  5634: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  5637: bipush          33
            //  5639: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  5642: dup            
            //  5643: iconst_0       
            //  5644: new             Lnet/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems;
            //  5647: dup            
            //  5648: getstatic       net/minecraft/world/item/Items.QUARTZ:Lnet/minecraft/world/item/Item;
            //  5651: bipush          12
            //  5653: bipush          12
            //  5655: bipush          30
            //  5657: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$EmeraldForItems.<init>:(Lnet/minecraft/world/level/ItemLike;III)V
            //  5660: aastore        
            //  5661: dup            
            //  5662: iconst_1       
            //  5663: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5666: dup            
            //  5667: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5670: iconst_1       
            //  5671: iconst_1       
            //  5672: bipush          12
            //  5674: bipush          15
            //  5676: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5679: aastore        
            //  5680: dup            
            //  5681: iconst_2       
            //  5682: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5685: dup            
            //  5686: getstatic       net/minecraft/world/level/block/Blocks.WHITE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5689: iconst_1       
            //  5690: iconst_1       
            //  5691: bipush          12
            //  5693: bipush          15
            //  5695: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5698: aastore        
            //  5699: dup            
            //  5700: iconst_3       
            //  5701: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5704: dup            
            //  5705: getstatic       net/minecraft/world/level/block/Blocks.BLUE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5708: iconst_1       
            //  5709: iconst_1       
            //  5710: bipush          12
            //  5712: bipush          15
            //  5714: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5717: aastore        
            //  5718: dup            
            //  5719: iconst_4       
            //  5720: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5723: dup            
            //  5724: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5727: iconst_1       
            //  5728: iconst_1       
            //  5729: bipush          12
            //  5731: bipush          15
            //  5733: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5736: aastore        
            //  5737: dup            
            //  5738: iconst_5       
            //  5739: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5742: dup            
            //  5743: getstatic       net/minecraft/world/level/block/Blocks.GRAY_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5746: iconst_1       
            //  5747: iconst_1       
            //  5748: bipush          12
            //  5750: bipush          15
            //  5752: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5755: aastore        
            //  5756: dup            
            //  5757: bipush          6
            //  5759: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5762: dup            
            //  5763: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5766: iconst_1       
            //  5767: iconst_1       
            //  5768: bipush          12
            //  5770: bipush          15
            //  5772: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5775: aastore        
            //  5776: dup            
            //  5777: bipush          7
            //  5779: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5782: dup            
            //  5783: getstatic       net/minecraft/world/level/block/Blocks.BLACK_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5786: iconst_1       
            //  5787: iconst_1       
            //  5788: bipush          12
            //  5790: bipush          15
            //  5792: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5795: aastore        
            //  5796: dup            
            //  5797: bipush          8
            //  5799: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5802: dup            
            //  5803: getstatic       net/minecraft/world/level/block/Blocks.RED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5806: iconst_1       
            //  5807: iconst_1       
            //  5808: bipush          12
            //  5810: bipush          15
            //  5812: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5815: aastore        
            //  5816: dup            
            //  5817: bipush          9
            //  5819: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5822: dup            
            //  5823: getstatic       net/minecraft/world/level/block/Blocks.PINK_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5826: iconst_1       
            //  5827: iconst_1       
            //  5828: bipush          12
            //  5830: bipush          15
            //  5832: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5835: aastore        
            //  5836: dup            
            //  5837: bipush          10
            //  5839: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5842: dup            
            //  5843: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5846: iconst_1       
            //  5847: iconst_1       
            //  5848: bipush          12
            //  5850: bipush          15
            //  5852: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5855: aastore        
            //  5856: dup            
            //  5857: bipush          11
            //  5859: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5862: dup            
            //  5863: getstatic       net/minecraft/world/level/block/Blocks.LIME_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5866: iconst_1       
            //  5867: iconst_1       
            //  5868: bipush          12
            //  5870: bipush          15
            //  5872: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5875: aastore        
            //  5876: dup            
            //  5877: bipush          12
            //  5879: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5882: dup            
            //  5883: getstatic       net/minecraft/world/level/block/Blocks.GREEN_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5886: iconst_1       
            //  5887: iconst_1       
            //  5888: bipush          12
            //  5890: bipush          15
            //  5892: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5895: aastore        
            //  5896: dup            
            //  5897: bipush          13
            //  5899: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5902: dup            
            //  5903: getstatic       net/minecraft/world/level/block/Blocks.CYAN_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5906: iconst_1       
            //  5907: iconst_1       
            //  5908: bipush          12
            //  5910: bipush          15
            //  5912: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5915: aastore        
            //  5916: dup            
            //  5917: bipush          14
            //  5919: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5922: dup            
            //  5923: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5926: iconst_1       
            //  5927: iconst_1       
            //  5928: bipush          12
            //  5930: bipush          15
            //  5932: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5935: aastore        
            //  5936: dup            
            //  5937: bipush          15
            //  5939: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5942: dup            
            //  5943: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5946: iconst_1       
            //  5947: iconst_1       
            //  5948: bipush          12
            //  5950: bipush          15
            //  5952: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5955: aastore        
            //  5956: dup            
            //  5957: bipush          16
            //  5959: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5962: dup            
            //  5963: getstatic       net/minecraft/world/level/block/Blocks.BROWN_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5966: iconst_1       
            //  5967: iconst_1       
            //  5968: bipush          12
            //  5970: bipush          15
            //  5972: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5975: aastore        
            //  5976: dup            
            //  5977: bipush          17
            //  5979: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  5982: dup            
            //  5983: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  5986: iconst_1       
            //  5987: iconst_1       
            //  5988: bipush          12
            //  5990: bipush          15
            //  5992: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  5995: aastore        
            //  5996: dup            
            //  5997: bipush          18
            //  5999: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6002: dup            
            //  6003: getstatic       net/minecraft/world/level/block/Blocks.WHITE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6006: iconst_1       
            //  6007: iconst_1       
            //  6008: bipush          12
            //  6010: bipush          15
            //  6012: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6015: aastore        
            //  6016: dup            
            //  6017: bipush          19
            //  6019: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6022: dup            
            //  6023: getstatic       net/minecraft/world/level/block/Blocks.BLUE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6026: iconst_1       
            //  6027: iconst_1       
            //  6028: bipush          12
            //  6030: bipush          15
            //  6032: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6035: aastore        
            //  6036: dup            
            //  6037: bipush          20
            //  6039: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6042: dup            
            //  6043: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6046: iconst_1       
            //  6047: iconst_1       
            //  6048: bipush          12
            //  6050: bipush          15
            //  6052: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6055: aastore        
            //  6056: dup            
            //  6057: bipush          21
            //  6059: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6062: dup            
            //  6063: getstatic       net/minecraft/world/level/block/Blocks.GRAY_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6066: iconst_1       
            //  6067: iconst_1       
            //  6068: bipush          12
            //  6070: bipush          15
            //  6072: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6075: aastore        
            //  6076: dup            
            //  6077: bipush          22
            //  6079: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6082: dup            
            //  6083: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6086: iconst_1       
            //  6087: iconst_1       
            //  6088: bipush          12
            //  6090: bipush          15
            //  6092: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6095: aastore        
            //  6096: dup            
            //  6097: bipush          23
            //  6099: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6102: dup            
            //  6103: getstatic       net/minecraft/world/level/block/Blocks.BLACK_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6106: iconst_1       
            //  6107: iconst_1       
            //  6108: bipush          12
            //  6110: bipush          15
            //  6112: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6115: aastore        
            //  6116: dup            
            //  6117: bipush          24
            //  6119: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6122: dup            
            //  6123: getstatic       net/minecraft/world/level/block/Blocks.RED_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6126: iconst_1       
            //  6127: iconst_1       
            //  6128: bipush          12
            //  6130: bipush          15
            //  6132: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6135: aastore        
            //  6136: dup            
            //  6137: bipush          25
            //  6139: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6142: dup            
            //  6143: getstatic       net/minecraft/world/level/block/Blocks.PINK_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6146: iconst_1       
            //  6147: iconst_1       
            //  6148: bipush          12
            //  6150: bipush          15
            //  6152: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6155: aastore        
            //  6156: dup            
            //  6157: bipush          26
            //  6159: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6162: dup            
            //  6163: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6166: iconst_1       
            //  6167: iconst_1       
            //  6168: bipush          12
            //  6170: bipush          15
            //  6172: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6175: aastore        
            //  6176: dup            
            //  6177: bipush          27
            //  6179: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6182: dup            
            //  6183: getstatic       net/minecraft/world/level/block/Blocks.LIME_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6186: iconst_1       
            //  6187: iconst_1       
            //  6188: bipush          12
            //  6190: bipush          15
            //  6192: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6195: aastore        
            //  6196: dup            
            //  6197: bipush          28
            //  6199: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6202: dup            
            //  6203: getstatic       net/minecraft/world/level/block/Blocks.GREEN_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6206: iconst_1       
            //  6207: iconst_1       
            //  6208: bipush          12
            //  6210: bipush          15
            //  6212: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6215: aastore        
            //  6216: dup            
            //  6217: bipush          29
            //  6219: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6222: dup            
            //  6223: getstatic       net/minecraft/world/level/block/Blocks.CYAN_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6226: iconst_1       
            //  6227: iconst_1       
            //  6228: bipush          12
            //  6230: bipush          15
            //  6232: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6235: aastore        
            //  6236: dup            
            //  6237: bipush          30
            //  6239: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6242: dup            
            //  6243: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6246: iconst_1       
            //  6247: iconst_1       
            //  6248: bipush          12
            //  6250: bipush          15
            //  6252: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6255: aastore        
            //  6256: dup            
            //  6257: bipush          31
            //  6259: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6262: dup            
            //  6263: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6266: iconst_1       
            //  6267: iconst_1       
            //  6268: bipush          12
            //  6270: bipush          15
            //  6272: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6275: aastore        
            //  6276: dup            
            //  6277: bipush          32
            //  6279: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6282: dup            
            //  6283: getstatic       net/minecraft/world/level/block/Blocks.BROWN_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
            //  6286: iconst_1       
            //  6287: iconst_1       
            //  6288: bipush          12
            //  6290: bipush          15
            //  6292: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6295: aastore        
            //  6296: iconst_5       
            //  6297: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //  6300: iconst_2       
            //  6301: anewarray       Lnet/minecraft/world/entity/npc/VillagerTrades$ItemListing;
            //  6304: dup            
            //  6305: iconst_0       
            //  6306: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6309: dup            
            //  6310: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_PILLAR:Lnet/minecraft/world/level/block/Block;
            //  6313: iconst_1       
            //  6314: iconst_1       
            //  6315: bipush          12
            //  6317: bipush          30
            //  6319: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6322: aastore        
            //  6323: dup            
            //  6324: iconst_1       
            //  6325: new             Lnet/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds;
            //  6328: dup            
            //  6329: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_BLOCK:Lnet/minecraft/world/level/block/Block;
            //  6332: iconst_1       
            //  6333: iconst_1       
            //  6334: bipush          12
            //  6336: bipush          30
            //  6338: invokespecial   net/minecraft/world/entity/npc/VillagerTrades$ItemsForEmeralds.<init>:(Lnet/minecraft/world/level/block/Block;IIII)V
            //  6341: aastore        
            //  6342: invokestatic    com/google/common/collect/ImmutableMap.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableMap;
            //  6345: invokestatic    net/minecraft/world/entity/npc/VillagerTrades.toIntMap:(Lcom/google/common/collect/ImmutableMap;)Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;
            //  6348: invokevirtual   java/util/HashMap.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
            //  6351: pop            
            //  6352: return         
            //    MethodParameters:
            //  Name     Flags  
            //  -------  -----
            //  hashMap  
            // 
            // The error that occurred was:
            // 
            // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
            //     at java.base/java.util.Vector.get(Vector.java:749)
            //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:201)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:25)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitArrayType(TypeSubstitutionVisitor.java:44)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitArrayType(TypeSubstitutionVisitor.java:25)
            //     at com.strobel.assembler.metadata.ArrayType.accept(ArrayType.java:80)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
            //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2607)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
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
        }));
        WANDERING_TRADER_TRADES = toIntMap(ImmutableMap.<Integer, ItemListing[]>of(1, new ItemListing[] { new ItemsForEmeralds(Items.SEA_PICKLE, 2, 1, 5, 1), new ItemsForEmeralds(Items.SLIME_BALL, 4, 1, 5, 1), new ItemsForEmeralds(Items.GLOWSTONE, 2, 1, 5, 1), new ItemsForEmeralds(Items.NAUTILUS_SHELL, 5, 1, 5, 1), new ItemsForEmeralds(Items.FERN, 1, 1, 12, 1), new ItemsForEmeralds(Items.SUGAR_CANE, 1, 1, 8, 1), new ItemsForEmeralds(Items.PUMPKIN, 1, 1, 4, 1), new ItemsForEmeralds(Items.KELP, 3, 1, 12, 1), new ItemsForEmeralds(Items.CACTUS, 3, 1, 8, 1), new ItemsForEmeralds(Items.DANDELION, 1, 1, 12, 1), new ItemsForEmeralds(Items.POPPY, 1, 1, 12, 1), new ItemsForEmeralds(Items.BLUE_ORCHID, 1, 1, 8, 1), new ItemsForEmeralds(Items.ALLIUM, 1, 1, 12, 1), new ItemsForEmeralds(Items.AZURE_BLUET, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.ORANGE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.WHITE_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.PINK_TULIP, 1, 1, 12, 1), new ItemsForEmeralds(Items.OXEYE_DAISY, 1, 1, 12, 1), new ItemsForEmeralds(Items.CORNFLOWER, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_OF_THE_VALLEY, 1, 1, 7, 1), new ItemsForEmeralds(Items.WHEAT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.BEETROOT_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.PUMPKIN_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.MELON_SEEDS, 1, 1, 12, 1), new ItemsForEmeralds(Items.ACACIA_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.BIRCH_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.DARK_OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.JUNGLE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.OAK_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.SPRUCE_SAPLING, 5, 1, 8, 1), new ItemsForEmeralds(Items.RED_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.WHITE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PINK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BLACK_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GREEN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.MAGENTA_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.YELLOW_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.GRAY_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.PURPLE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIGHT_BLUE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.LIME_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.ORANGE_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BROWN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.CYAN_DYE, 1, 3, 12, 1), new ItemsForEmeralds(Items.BRAIN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.BUBBLE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.FIRE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.HORN_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.TUBE_CORAL_BLOCK, 3, 1, 8, 1), new ItemsForEmeralds(Items.VINE, 1, 1, 12, 1), new ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 1, 12, 1), new ItemsForEmeralds(Items.RED_MUSHROOM, 1, 1, 12, 1), new ItemsForEmeralds(Items.LILY_PAD, 1, 2, 5, 1), new ItemsForEmeralds(Items.SAND, 1, 8, 8, 1), new ItemsForEmeralds(Items.RED_SAND, 1, 4, 6, 1) }, 2, new ItemListing[] { new ItemsForEmeralds(Items.TROPICAL_FISH_BUCKET, 5, 1, 4, 1), new ItemsForEmeralds(Items.PUFFERFISH_BUCKET, 5, 1, 4, 1), new ItemsForEmeralds(Items.PACKED_ICE, 3, 1, 6, 1), new ItemsForEmeralds(Items.BLUE_ICE, 6, 1, 6, 1), new ItemsForEmeralds(Items.GUNPOWDER, 1, 1, 8, 1), new ItemsForEmeralds(Items.PODZOL, 3, 3, 6, 1) }));
    }
    
    static class EmeraldForItems implements ItemListing {
        private final Item item;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public EmeraldForItems(final ItemLike brt, final int integer2, final int integer3, final int integer4) {
            this.item = brt.asItem();
            this.cost = integer2;
            this.maxUses = integer3;
            this.villagerXp = integer4;
            this.priceMultiplier = 0.05f;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(this.item, this.cost);
            return new MerchantOffer(bly4, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class EmeraldsForVillagerTypeItem implements ItemListing {
        private final Map<VillagerType, Item> trades;
        private final int cost;
        private final int maxUses;
        private final int villagerXp;
        
        public EmeraldsForVillagerTypeItem(final int integer1, final int integer2, final int integer3, final Map<VillagerType, Item> map) {
            Registry.VILLAGER_TYPE.stream().filter(bfl -> !map.containsKey(bfl)).findAny().ifPresent(bfl -> {
                throw new IllegalStateException(new StringBuilder().append("Missing trade for villager type: ").append(Registry.VILLAGER_TYPE.getKey(bfl)).toString());
            });
            this.trades = map;
            this.cost = integer1;
            this.maxUses = integer2;
            this.villagerXp = integer3;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            if (apx instanceof VillagerDataHolder) {
                final ItemStack bly4 = new ItemStack((ItemLike)this.trades.get(((VillagerDataHolder)apx).getVillagerData().getType()), this.cost);
                return new MerchantOffer(bly4, new ItemStack(Items.EMERALD), this.maxUses, this.villagerXp, 0.05f);
            }
            return null;
        }
    }
    
    static class ItemsForEmeralds implements ItemListing {
        private final ItemStack itemStack;
        private final int emeraldCost;
        private final int numberOfItems;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public ItemsForEmeralds(final Block bul, final int integer2, final int integer3, final int integer4, final int integer5) {
            this(new ItemStack(bul), integer2, integer3, integer4, integer5);
        }
        
        public ItemsForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4) {
            this(new ItemStack(blu), integer2, integer3, 12, integer4);
        }
        
        public ItemsForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4, final int integer5) {
            this(new ItemStack(blu), integer2, integer3, integer4, integer5);
        }
        
        public ItemsForEmeralds(final ItemStack bly, final int integer2, final int integer3, final int integer4, final int integer5) {
            this(bly, integer2, integer3, integer4, integer5, 0.05f);
        }
        
        public ItemsForEmeralds(final ItemStack bly, final int integer2, final int integer3, final int integer4, final int integer5, final float float6) {
            this.itemStack = bly;
            this.emeraldCost = integer2;
            this.numberOfItems = integer3;
            this.maxUses = integer4;
            this.villagerXp = integer5;
            this.priceMultiplier = float6;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.itemStack.getItem(), this.numberOfItems), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class SuspisciousStewForEmerald implements ItemListing {
        final MobEffect effect;
        final int duration;
        final int xp;
        private final float priceMultiplier;
        
        public SuspisciousStewForEmerald(final MobEffect app, final int integer2, final int integer3) {
            this.effect = app;
            this.duration = integer2;
            this.xp = integer3;
            this.priceMultiplier = 0.05f;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.saveMobEffect(bly4, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(Items.EMERALD, 1), bly4, 12, this.xp, this.priceMultiplier);
        }
    }
    
    static class EnchantedItemForEmeralds implements ItemListing {
        private final ItemStack itemStack;
        private final int baseEmeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public EnchantedItemForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4) {
            this(blu, integer2, integer3, integer4, 0.05f);
        }
        
        public EnchantedItemForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4, final float float5) {
            this.itemStack = new ItemStack(blu);
            this.baseEmeraldCost = integer2;
            this.maxUses = integer3;
            this.villagerXp = integer4;
            this.priceMultiplier = float5;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final int integer4 = 5 + random.nextInt(15);
            final ItemStack bly5 = EnchantmentHelper.enchantItem(random, new ItemStack(this.itemStack.getItem()), integer4, false);
            final int integer5 = Math.min(this.baseEmeraldCost + integer4, 64);
            final ItemStack bly6 = new ItemStack(Items.EMERALD, integer5);
            return new MerchantOffer(bly6, bly5, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class TippedArrowForItemsAndEmeralds implements ItemListing {
        private final ItemStack toItem;
        private final int toCount;
        private final int emeraldCost;
        private final int maxUses;
        private final int villagerXp;
        private final Item fromItem;
        private final int fromCount;
        private final float priceMultiplier;
        
        public TippedArrowForItemsAndEmeralds(final Item blu1, final int integer2, final Item blu3, final int integer4, final int integer5, final int integer6, final int integer7) {
            this.toItem = new ItemStack(blu3);
            this.emeraldCost = integer5;
            this.maxUses = integer6;
            this.villagerXp = integer7;
            this.fromItem = blu1;
            this.fromCount = integer2;
            this.toCount = integer4;
            this.priceMultiplier = 0.05f;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(Items.EMERALD, this.emeraldCost);
            final List<Potion> list5 = (List<Potion>)Registry.POTION.stream().filter(bnq -> !bnq.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(bnq)).collect(Collectors.toList());
            final Potion bnq6 = (Potion)list5.get(random.nextInt(list5.size()));
            final ItemStack bly5 = PotionUtils.setPotion(new ItemStack(this.toItem.getItem(), this.toCount), bnq6);
            return new MerchantOffer(bly4, new ItemStack(this.fromItem, this.fromCount), bly5, this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    static class DyedArmorForEmeralds implements ItemListing {
        private final Item item;
        private final int value;
        private final int maxUses;
        private final int villagerXp;
        
        public DyedArmorForEmeralds(final Item blu, final int integer) {
            this(blu, integer, 12, 1);
        }
        
        public DyedArmorForEmeralds(final Item blu, final int integer2, final int integer3, final int integer4) {
            this.item = blu;
            this.value = integer2;
            this.maxUses = integer3;
            this.villagerXp = integer4;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final ItemStack bly4 = new ItemStack(Items.EMERALD, this.value);
            ItemStack bly5 = new ItemStack(this.item);
            if (this.item instanceof DyeableArmorItem) {
                final List<DyeItem> list6 = Lists.newArrayList();
                list6.add(getRandomDye(random));
                if (random.nextFloat() > 0.7f) {
                    list6.add(getRandomDye(random));
                }
                if (random.nextFloat() > 0.8f) {
                    list6.add(getRandomDye(random));
                }
                bly5 = DyeableLeatherItem.dyeArmor(bly5, list6);
            }
            return new MerchantOffer(bly4, bly5, this.maxUses, this.villagerXp, 0.2f);
        }
        
        private static DyeItem getRandomDye(final Random random) {
            return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
        }
    }
    
    static class EnchantBookForEmeralds implements ItemListing {
        private final int villagerXp;
        
        public EnchantBookForEmeralds(final int integer) {
            this.villagerXp = integer;
        }
        
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            final List<Enchantment> list4 = (List<Enchantment>)Registry.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
            final Enchantment bpp5 = (Enchantment)list4.get(random.nextInt(list4.size()));
            final int integer6 = Mth.nextInt(random, bpp5.getMinLevel(), bpp5.getMaxLevel());
            final ItemStack bly7 = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(bpp5, integer6));
            int integer7 = 2 + random.nextInt(5 + integer6 * 10) + 3 * integer6;
            if (bpp5.isTreasureOnly()) {
                integer7 *= 2;
            }
            if (integer7 > 64) {
                integer7 = 64;
            }
            return new MerchantOffer(new ItemStack(Items.EMERALD, integer7), new ItemStack(Items.BOOK), bly7, 12, this.villagerXp, 0.2f);
        }
    }
    
    static class TreasureMapForEmeralds implements ItemListing {
        private final int emeraldCost;
        private final StructureFeature<?> destination;
        private final MapDecoration.Type destinationType;
        private final int maxUses;
        private final int villagerXp;
        
        public TreasureMapForEmeralds(final int integer1, final StructureFeature<?> ckx, final MapDecoration.Type a, final int integer4, final int integer5) {
            this.emeraldCost = integer1;
            this.destination = ckx;
            this.destinationType = a;
            this.maxUses = integer4;
            this.villagerXp = integer5;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            if (!(apx.level instanceof ServerLevel)) {
                return null;
            }
            final ServerLevel aag4 = (ServerLevel)apx.level;
            final BlockPos fx5 = aag4.findNearestMapFeature(this.destination, apx.blockPosition(), 100, true);
            if (fx5 != null) {
                final ItemStack bly6 = MapItem.create(aag4, fx5.getX(), fx5.getZ(), (byte)2, true, true);
                MapItem.renderBiomePreviewMap(aag4, bly6);
                MapItemSavedData.addTargetDecoration(bly6, fx5, "+", this.destinationType);
                bly6.setHoverName(new TranslatableComponent("filled_map." + this.destination.getFeatureName().toLowerCase(Locale.ROOT)));
                return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(Items.COMPASS), bly6, this.maxUses, this.villagerXp, 0.2f);
            }
            return null;
        }
    }
    
    static class ItemsAndEmeraldsToItems implements ItemListing {
        private final ItemStack fromItem;
        private final int fromCount;
        private final int emeraldCost;
        private final ItemStack toItem;
        private final int toCount;
        private final int maxUses;
        private final int villagerXp;
        private final float priceMultiplier;
        
        public ItemsAndEmeraldsToItems(final ItemLike brt, final int integer2, final Item blu, final int integer4, final int integer5, final int integer6) {
            this(brt, integer2, 1, blu, integer4, integer5, integer6);
        }
        
        public ItemsAndEmeraldsToItems(final ItemLike brt, final int integer2, final int integer3, final Item blu, final int integer5, final int integer6, final int integer7) {
            this.fromItem = new ItemStack(brt);
            this.fromCount = integer2;
            this.emeraldCost = integer3;
            this.toItem = new ItemStack(blu);
            this.toCount = integer5;
            this.maxUses = integer6;
            this.villagerXp = integer7;
            this.priceMultiplier = 0.05f;
        }
        
        @Nullable
        public MerchantOffer getOffer(final Entity apx, final Random random) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCost), new ItemStack(this.fromItem.getItem(), this.fromCount), new ItemStack(this.toItem.getItem(), this.toCount), this.maxUses, this.villagerXp, this.priceMultiplier);
        }
    }
    
    public interface ItemListing {
        @Nullable
        MerchantOffer getOffer(final Entity apx, final Random random);
    }
}
