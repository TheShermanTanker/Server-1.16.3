package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class SavannaVillagePools {
    public static final StructureTemplatePool START;
    
    public static void bootstrap() {
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: new             Lnet/minecraft/resources/ResourceLocation;
        //     7: dup            
        //     8: ldc             "village/savanna/town_centers"
        //    10: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    13: new             Lnet/minecraft/resources/ResourceLocation;
        //    16: dup            
        //    17: ldc             "empty"
        //    19: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    22: ldc             "village/savanna/town_centers/savanna_meeting_point_1"
        //    24: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    27: bipush          100
        //    29: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    32: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    35: ldc             "village/savanna/town_centers/savanna_meeting_point_2"
        //    37: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    40: bipush          50
        //    42: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    45: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    48: ldc             "village/savanna/town_centers/savanna_meeting_point_3"
        //    50: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    53: sipush          150
        //    56: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    59: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    62: ldc             "village/savanna/town_centers/savanna_meeting_point_4"
        //    64: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    67: sipush          150
        //    70: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    73: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    76: ldc             "village/savanna/zombie/town_centers/savanna_meeting_point_1"
        //    78: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    81: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    84: iconst_2       
        //    85: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    88: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    91: ldc             "village/savanna/zombie/town_centers/savanna_meeting_point_2"
        //    93: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    96: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    99: iconst_1       
        //   100: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   103: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   106: ldc             "village/savanna/zombie/town_centers/savanna_meeting_point_3"
        //   108: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   111: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   114: iconst_3       
        //   115: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   118: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   121: ldc             "village/savanna/zombie/town_centers/savanna_meeting_point_4"
        //   123: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   126: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   129: iconst_3       
        //   130: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   133: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   136: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   139: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   142: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   145: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   148: putstatic       net/minecraft/data/worldgen/SavannaVillagePools.START:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   151: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   154: dup            
        //   155: new             Lnet/minecraft/resources/ResourceLocation;
        //   158: dup            
        //   159: ldc             "village/savanna/streets"
        //   161: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   164: new             Lnet/minecraft/resources/ResourceLocation;
        //   167: dup            
        //   168: ldc             "village/savanna/terminators"
        //   170: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   173: ldc             "village/savanna/streets/corner_01"
        //   175: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   178: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   181: iconst_2       
        //   182: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   185: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   188: ldc             "village/savanna/streets/corner_03"
        //   190: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   193: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   196: iconst_2       
        //   197: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   200: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   203: ldc             "village/savanna/streets/straight_02"
        //   205: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   208: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   211: iconst_4       
        //   212: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   215: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   218: ldc             "village/savanna/streets/straight_04"
        //   220: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   223: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   226: bipush          7
        //   228: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   231: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   234: ldc             "village/savanna/streets/straight_05"
        //   236: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   239: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   242: iconst_3       
        //   243: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   246: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   249: ldc             "village/savanna/streets/straight_06"
        //   251: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   254: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   257: iconst_4       
        //   258: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   261: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   264: ldc             "village/savanna/streets/straight_08"
        //   266: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   269: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   272: iconst_4       
        //   273: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   276: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   279: ldc             "village/savanna/streets/straight_09"
        //   281: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   284: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   287: iconst_4       
        //   288: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   291: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   294: ldc             "village/savanna/streets/straight_10"
        //   296: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   299: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   302: iconst_4       
        //   303: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   306: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   309: ldc             "village/savanna/streets/straight_11"
        //   311: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   314: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   317: iconst_4       
        //   318: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   321: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   324: ldc             "village/savanna/streets/crossroad_02"
        //   326: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   329: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   332: iconst_1       
        //   333: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   336: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   339: ldc             "village/savanna/streets/crossroad_03"
        //   341: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   344: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   347: iconst_2       
        //   348: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   351: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   354: bipush          7
        //   356: anewarray       Lcom/mojang/datafixers/util/Pair;
        //   359: dup            
        //   360: iconst_0       
        //   361: ldc             "village/savanna/streets/crossroad_04"
        //   363: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   366: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   369: iconst_2       
        //   370: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   373: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   376: aastore        
        //   377: dup            
        //   378: iconst_1       
        //   379: ldc             "village/savanna/streets/crossroad_05"
        //   381: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   384: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   387: iconst_2       
        //   388: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   391: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   394: aastore        
        //   395: dup            
        //   396: iconst_2       
        //   397: ldc             "village/savanna/streets/crossroad_06"
        //   399: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   402: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   405: iconst_2       
        //   406: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   409: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   412: aastore        
        //   413: dup            
        //   414: iconst_3       
        //   415: ldc             "village/savanna/streets/crossroad_07"
        //   417: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   420: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   423: iconst_2       
        //   424: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   427: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   430: aastore        
        //   431: dup            
        //   432: iconst_4       
        //   433: ldc             "village/savanna/streets/split_01"
        //   435: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   438: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   441: iconst_2       
        //   442: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   445: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   448: aastore        
        //   449: dup            
        //   450: iconst_5       
        //   451: ldc             "village/savanna/streets/split_02"
        //   453: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   456: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   459: iconst_2       
        //   460: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   463: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   466: aastore        
        //   467: dup            
        //   468: bipush          6
        //   470: ldc             "village/savanna/streets/turn_01"
        //   472: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   475: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   478: iconst_3       
        //   479: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   482: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   485: aastore        
        //   486: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   489: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   492: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   495: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   498: pop            
        //   499: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   502: dup            
        //   503: new             Lnet/minecraft/resources/ResourceLocation;
        //   506: dup            
        //   507: ldc             "village/savanna/zombie/streets"
        //   509: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   512: new             Lnet/minecraft/resources/ResourceLocation;
        //   515: dup            
        //   516: ldc             "village/savanna/zombie/terminators"
        //   518: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   521: ldc             "village/savanna/zombie/streets/corner_01"
        //   523: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   526: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   529: iconst_2       
        //   530: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   533: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   536: ldc             "village/savanna/zombie/streets/corner_03"
        //   538: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   541: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   544: iconst_2       
        //   545: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   548: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   551: ldc             "village/savanna/zombie/streets/straight_02"
        //   553: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   556: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   559: iconst_4       
        //   560: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   563: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   566: ldc             "village/savanna/zombie/streets/straight_04"
        //   568: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   571: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   574: bipush          7
        //   576: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   579: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   582: ldc             "village/savanna/zombie/streets/straight_05"
        //   584: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   587: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   590: iconst_3       
        //   591: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   594: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   597: ldc             "village/savanna/zombie/streets/straight_06"
        //   599: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   602: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   605: iconst_4       
        //   606: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   609: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   612: ldc             "village/savanna/zombie/streets/straight_08"
        //   614: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   617: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   620: iconst_4       
        //   621: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   624: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   627: ldc             "village/savanna/zombie/streets/straight_09"
        //   629: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   632: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   635: iconst_4       
        //   636: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   639: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   642: ldc             "village/savanna/zombie/streets/straight_10"
        //   644: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   647: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   650: iconst_4       
        //   651: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   654: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   657: ldc             "village/savanna/zombie/streets/straight_11"
        //   659: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   662: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   665: iconst_4       
        //   666: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   669: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   672: ldc             "village/savanna/zombie/streets/crossroad_02"
        //   674: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   677: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   680: iconst_1       
        //   681: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   684: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   687: ldc             "village/savanna/zombie/streets/crossroad_03"
        //   689: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   692: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   695: iconst_2       
        //   696: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   699: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   702: bipush          7
        //   704: anewarray       Lcom/mojang/datafixers/util/Pair;
        //   707: dup            
        //   708: iconst_0       
        //   709: ldc             "village/savanna/zombie/streets/crossroad_04"
        //   711: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   714: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   717: iconst_2       
        //   718: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   721: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   724: aastore        
        //   725: dup            
        //   726: iconst_1       
        //   727: ldc             "village/savanna/zombie/streets/crossroad_05"
        //   729: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   732: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   735: iconst_2       
        //   736: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   739: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   742: aastore        
        //   743: dup            
        //   744: iconst_2       
        //   745: ldc             "village/savanna/zombie/streets/crossroad_06"
        //   747: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   750: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   753: iconst_2       
        //   754: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   757: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   760: aastore        
        //   761: dup            
        //   762: iconst_3       
        //   763: ldc             "village/savanna/zombie/streets/crossroad_07"
        //   765: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   768: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   771: iconst_2       
        //   772: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   775: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   778: aastore        
        //   779: dup            
        //   780: iconst_4       
        //   781: ldc             "village/savanna/zombie/streets/split_01"
        //   783: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   786: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   789: iconst_2       
        //   790: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   793: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   796: aastore        
        //   797: dup            
        //   798: iconst_5       
        //   799: ldc             "village/savanna/zombie/streets/split_02"
        //   801: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   804: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   807: iconst_2       
        //   808: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   811: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   814: aastore        
        //   815: dup            
        //   816: bipush          6
        //   818: ldc             "village/savanna/zombie/streets/turn_01"
        //   820: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   823: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   826: iconst_3       
        //   827: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   830: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   833: aastore        
        //   834: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   837: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   840: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   843: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   846: pop            
        //   847: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   850: dup            
        //   851: new             Lnet/minecraft/resources/ResourceLocation;
        //   854: dup            
        //   855: ldc             "village/savanna/houses"
        //   857: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   860: new             Lnet/minecraft/resources/ResourceLocation;
        //   863: dup            
        //   864: ldc             "village/savanna/terminators"
        //   866: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   869: ldc             "village/savanna/houses/savanna_small_house_1"
        //   871: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   874: iconst_2       
        //   875: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   878: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   881: ldc             "village/savanna/houses/savanna_small_house_2"
        //   883: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   886: iconst_2       
        //   887: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   890: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   893: ldc             "village/savanna/houses/savanna_small_house_3"
        //   895: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   898: iconst_2       
        //   899: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   902: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   905: ldc             "village/savanna/houses/savanna_small_house_4"
        //   907: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   910: iconst_2       
        //   911: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   914: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   917: ldc             "village/savanna/houses/savanna_small_house_5"
        //   919: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   922: iconst_2       
        //   923: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   926: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   929: ldc             "village/savanna/houses/savanna_small_house_6"
        //   931: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   934: iconst_2       
        //   935: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   938: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   941: ldc             "village/savanna/houses/savanna_small_house_7"
        //   943: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   946: iconst_2       
        //   947: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   950: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   953: ldc             "village/savanna/houses/savanna_small_house_8"
        //   955: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   958: iconst_2       
        //   959: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   962: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   965: ldc             "village/savanna/houses/savanna_medium_house_1"
        //   967: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   970: iconst_2       
        //   971: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   974: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   977: ldc             "village/savanna/houses/savanna_medium_house_2"
        //   979: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   982: iconst_2       
        //   983: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   986: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   989: ldc             "village/savanna/houses/savanna_butchers_shop_1"
        //   991: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //   994: iconst_2       
        //   995: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   998: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1001: ldc             "village/savanna/houses/savanna_butchers_shop_2"
        //  1003: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1006: iconst_2       
        //  1007: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1010: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1013: bipush          20
        //  1015: anewarray       Lcom/mojang/datafixers/util/Pair;
        //  1018: dup            
        //  1019: iconst_0       
        //  1020: ldc             "village/savanna/houses/savanna_tool_smith_1"
        //  1022: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1025: iconst_2       
        //  1026: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1029: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1032: aastore        
        //  1033: dup            
        //  1034: iconst_1       
        //  1035: ldc             "village/savanna/houses/savanna_fletcher_house_1"
        //  1037: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1040: iconst_2       
        //  1041: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1044: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1047: aastore        
        //  1048: dup            
        //  1049: iconst_2       
        //  1050: ldc             "village/savanna/houses/savanna_shepherd_1"
        //  1052: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1055: bipush          7
        //  1057: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1060: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1063: aastore        
        //  1064: dup            
        //  1065: iconst_3       
        //  1066: ldc             "village/savanna/houses/savanna_armorer_1"
        //  1068: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1071: iconst_1       
        //  1072: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1075: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1078: aastore        
        //  1079: dup            
        //  1080: iconst_4       
        //  1081: ldc             "village/savanna/houses/savanna_fisher_cottage_1"
        //  1083: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1086: iconst_3       
        //  1087: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1090: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1093: aastore        
        //  1094: dup            
        //  1095: iconst_5       
        //  1096: ldc             "village/savanna/houses/savanna_tannery_1"
        //  1098: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1101: iconst_2       
        //  1102: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1105: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1108: aastore        
        //  1109: dup            
        //  1110: bipush          6
        //  1112: ldc             "village/savanna/houses/savanna_cartographer_1"
        //  1114: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1117: iconst_2       
        //  1118: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1121: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1124: aastore        
        //  1125: dup            
        //  1126: bipush          7
        //  1128: ldc             "village/savanna/houses/savanna_library_1"
        //  1130: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1133: iconst_2       
        //  1134: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1137: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1140: aastore        
        //  1141: dup            
        //  1142: bipush          8
        //  1144: ldc             "village/savanna/houses/savanna_mason_1"
        //  1146: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1149: iconst_2       
        //  1150: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1153: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1156: aastore        
        //  1157: dup            
        //  1158: bipush          9
        //  1160: ldc             "village/savanna/houses/savanna_weaponsmith_1"
        //  1162: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1165: iconst_2       
        //  1166: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1169: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1172: aastore        
        //  1173: dup            
        //  1174: bipush          10
        //  1176: ldc             "village/savanna/houses/savanna_weaponsmith_2"
        //  1178: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1181: iconst_2       
        //  1182: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1185: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1188: aastore        
        //  1189: dup            
        //  1190: bipush          11
        //  1192: ldc             "village/savanna/houses/savanna_temple_1"
        //  1194: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1197: iconst_2       
        //  1198: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1201: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1204: aastore        
        //  1205: dup            
        //  1206: bipush          12
        //  1208: ldc             "village/savanna/houses/savanna_temple_2"
        //  1210: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1213: iconst_3       
        //  1214: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1217: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1220: aastore        
        //  1221: dup            
        //  1222: bipush          13
        //  1224: ldc             "village/savanna/houses/savanna_large_farm_1"
        //  1226: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1229: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1232: iconst_4       
        //  1233: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1236: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1239: aastore        
        //  1240: dup            
        //  1241: bipush          14
        //  1243: ldc             "village/savanna/houses/savanna_large_farm_2"
        //  1245: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1248: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1251: bipush          6
        //  1253: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1256: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1259: aastore        
        //  1260: dup            
        //  1261: bipush          15
        //  1263: ldc             "village/savanna/houses/savanna_small_farm"
        //  1265: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1268: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1271: iconst_4       
        //  1272: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1275: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1278: aastore        
        //  1279: dup            
        //  1280: bipush          16
        //  1282: ldc             "village/savanna/houses/savanna_animal_pen_1"
        //  1284: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1287: iconst_2       
        //  1288: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1291: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1294: aastore        
        //  1295: dup            
        //  1296: bipush          17
        //  1298: ldc             "village/savanna/houses/savanna_animal_pen_2"
        //  1300: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1303: iconst_2       
        //  1304: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1307: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1310: aastore        
        //  1311: dup            
        //  1312: bipush          18
        //  1314: ldc             "village/savanna/houses/savanna_animal_pen_3"
        //  1316: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1319: iconst_2       
        //  1320: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1323: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1326: aastore        
        //  1327: dup            
        //  1328: bipush          19
        //  1330: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  1333: iconst_5       
        //  1334: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1337: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1340: aastore        
        //  1341: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1344: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1347: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1350: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1353: pop            
        //  1354: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1357: dup            
        //  1358: new             Lnet/minecraft/resources/ResourceLocation;
        //  1361: dup            
        //  1362: ldc             "village/savanna/zombie/houses"
        //  1364: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1367: new             Lnet/minecraft/resources/ResourceLocation;
        //  1370: dup            
        //  1371: ldc             "village/savanna/zombie/terminators"
        //  1373: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1376: ldc             "village/savanna/zombie/houses/savanna_small_house_1"
        //  1378: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1381: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1384: iconst_2       
        //  1385: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1388: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1391: ldc_w           "village/savanna/zombie/houses/savanna_small_house_2"
        //  1394: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1397: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1400: iconst_2       
        //  1401: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1404: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1407: ldc_w           "village/savanna/zombie/houses/savanna_small_house_3"
        //  1410: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1413: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1416: iconst_2       
        //  1417: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1420: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1423: ldc_w           "village/savanna/zombie/houses/savanna_small_house_4"
        //  1426: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1429: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1432: iconst_2       
        //  1433: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1436: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1439: ldc_w           "village/savanna/zombie/houses/savanna_small_house_5"
        //  1442: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1445: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1448: iconst_2       
        //  1449: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1452: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1455: ldc_w           "village/savanna/zombie/houses/savanna_small_house_6"
        //  1458: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1461: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1464: iconst_2       
        //  1465: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1468: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1471: ldc_w           "village/savanna/zombie/houses/savanna_small_house_7"
        //  1474: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1477: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1480: iconst_2       
        //  1481: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1484: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1487: ldc_w           "village/savanna/zombie/houses/savanna_small_house_8"
        //  1490: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1493: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1496: iconst_2       
        //  1497: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1500: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1503: ldc_w           "village/savanna/zombie/houses/savanna_medium_house_1"
        //  1506: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1509: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1512: iconst_2       
        //  1513: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1516: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1519: ldc_w           "village/savanna/zombie/houses/savanna_medium_house_2"
        //  1522: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1525: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1528: iconst_2       
        //  1529: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1532: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1535: ldc             "village/savanna/houses/savanna_butchers_shop_1"
        //  1537: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1540: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1543: iconst_2       
        //  1544: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1547: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1550: ldc             "village/savanna/houses/savanna_butchers_shop_2"
        //  1552: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1555: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1558: iconst_2       
        //  1559: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1562: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1565: bipush          20
        //  1567: anewarray       Lcom/mojang/datafixers/util/Pair;
        //  1570: dup            
        //  1571: iconst_0       
        //  1572: ldc             "village/savanna/houses/savanna_tool_smith_1"
        //  1574: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1577: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1580: iconst_2       
        //  1581: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1584: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1587: aastore        
        //  1588: dup            
        //  1589: iconst_1       
        //  1590: ldc             "village/savanna/houses/savanna_fletcher_house_1"
        //  1592: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1595: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1598: iconst_2       
        //  1599: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1602: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1605: aastore        
        //  1606: dup            
        //  1607: iconst_2       
        //  1608: ldc             "village/savanna/houses/savanna_shepherd_1"
        //  1610: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1613: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1616: iconst_2       
        //  1617: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1620: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1623: aastore        
        //  1624: dup            
        //  1625: iconst_3       
        //  1626: ldc             "village/savanna/houses/savanna_armorer_1"
        //  1628: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1631: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1634: iconst_1       
        //  1635: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1638: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1641: aastore        
        //  1642: dup            
        //  1643: iconst_4       
        //  1644: ldc             "village/savanna/houses/savanna_fisher_cottage_1"
        //  1646: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1649: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1652: iconst_2       
        //  1653: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1656: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1659: aastore        
        //  1660: dup            
        //  1661: iconst_5       
        //  1662: ldc             "village/savanna/houses/savanna_tannery_1"
        //  1664: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1667: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1670: iconst_2       
        //  1671: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1674: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1677: aastore        
        //  1678: dup            
        //  1679: bipush          6
        //  1681: ldc             "village/savanna/houses/savanna_cartographer_1"
        //  1683: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1686: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1689: iconst_2       
        //  1690: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1693: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1696: aastore        
        //  1697: dup            
        //  1698: bipush          7
        //  1700: ldc             "village/savanna/houses/savanna_library_1"
        //  1702: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1705: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1708: iconst_2       
        //  1709: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1712: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1715: aastore        
        //  1716: dup            
        //  1717: bipush          8
        //  1719: ldc             "village/savanna/houses/savanna_mason_1"
        //  1721: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1724: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1727: iconst_2       
        //  1728: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1731: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1734: aastore        
        //  1735: dup            
        //  1736: bipush          9
        //  1738: ldc             "village/savanna/houses/savanna_weaponsmith_1"
        //  1740: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1743: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1746: iconst_2       
        //  1747: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1750: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1753: aastore        
        //  1754: dup            
        //  1755: bipush          10
        //  1757: ldc             "village/savanna/houses/savanna_weaponsmith_2"
        //  1759: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1762: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1765: iconst_2       
        //  1766: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1769: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1772: aastore        
        //  1773: dup            
        //  1774: bipush          11
        //  1776: ldc             "village/savanna/houses/savanna_temple_1"
        //  1778: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1781: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1784: iconst_1       
        //  1785: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1788: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1791: aastore        
        //  1792: dup            
        //  1793: bipush          12
        //  1795: ldc             "village/savanna/houses/savanna_temple_2"
        //  1797: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1800: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1803: iconst_3       
        //  1804: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1807: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1810: aastore        
        //  1811: dup            
        //  1812: bipush          13
        //  1814: ldc             "village/savanna/houses/savanna_large_farm_1"
        //  1816: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1819: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1822: iconst_4       
        //  1823: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1826: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1829: aastore        
        //  1830: dup            
        //  1831: bipush          14
        //  1833: ldc_w           "village/savanna/zombie/houses/savanna_large_farm_2"
        //  1836: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1839: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1842: iconst_4       
        //  1843: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1846: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1849: aastore        
        //  1850: dup            
        //  1851: bipush          15
        //  1853: ldc             "village/savanna/houses/savanna_small_farm"
        //  1855: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1858: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1861: iconst_4       
        //  1862: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1865: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1868: aastore        
        //  1869: dup            
        //  1870: bipush          16
        //  1872: ldc             "village/savanna/houses/savanna_animal_pen_1"
        //  1874: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1877: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1880: iconst_2       
        //  1881: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1884: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1887: aastore        
        //  1888: dup            
        //  1889: bipush          17
        //  1891: ldc_w           "village/savanna/zombie/houses/savanna_animal_pen_2"
        //  1894: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1897: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1900: iconst_2       
        //  1901: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1904: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1907: aastore        
        //  1908: dup            
        //  1909: bipush          18
        //  1911: ldc_w           "village/savanna/zombie/houses/savanna_animal_pen_3"
        //  1914: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1917: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1920: iconst_2       
        //  1921: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1924: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1927: aastore        
        //  1928: dup            
        //  1929: bipush          19
        //  1931: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  1934: iconst_5       
        //  1935: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1938: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1941: aastore        
        //  1942: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1945: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1948: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1951: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1954: pop            
        //  1955: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1958: dup            
        //  1959: new             Lnet/minecraft/resources/ResourceLocation;
        //  1962: dup            
        //  1963: ldc             "village/savanna/terminators"
        //  1965: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1968: new             Lnet/minecraft/resources/ResourceLocation;
        //  1971: dup            
        //  1972: ldc             "empty"
        //  1974: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1977: ldc_w           "village/plains/terminators/terminator_01"
        //  1980: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1983: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1986: iconst_1       
        //  1987: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1990: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1993: ldc_w           "village/plains/terminators/terminator_02"
        //  1996: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1999: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2002: iconst_1       
        //  2003: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2006: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2009: ldc_w           "village/plains/terminators/terminator_03"
        //  2012: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2015: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2018: iconst_1       
        //  2019: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2022: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2025: ldc_w           "village/plains/terminators/terminator_04"
        //  2028: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2031: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2034: iconst_1       
        //  2035: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2038: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2041: ldc_w           "village/savanna/terminators/terminator_05"
        //  2044: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2047: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2050: iconst_1       
        //  2051: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2054: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2057: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2060: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2063: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2066: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2069: pop            
        //  2070: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2073: dup            
        //  2074: new             Lnet/minecraft/resources/ResourceLocation;
        //  2077: dup            
        //  2078: ldc             "village/savanna/zombie/terminators"
        //  2080: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2083: new             Lnet/minecraft/resources/ResourceLocation;
        //  2086: dup            
        //  2087: ldc             "empty"
        //  2089: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2092: ldc_w           "village/plains/terminators/terminator_01"
        //  2095: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2098: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2101: iconst_1       
        //  2102: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2105: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2108: ldc_w           "village/plains/terminators/terminator_02"
        //  2111: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2114: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2117: iconst_1       
        //  2118: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2121: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2124: ldc_w           "village/plains/terminators/terminator_03"
        //  2127: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2130: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2133: iconst_1       
        //  2134: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2137: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2140: ldc_w           "village/plains/terminators/terminator_04"
        //  2143: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2146: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2149: iconst_1       
        //  2150: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2153: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2156: ldc_w           "village/savanna/zombie/terminators/terminator_05"
        //  2159: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2162: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2165: iconst_1       
        //  2166: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2169: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2172: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2175: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2178: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2181: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2184: pop            
        //  2185: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2188: dup            
        //  2189: new             Lnet/minecraft/resources/ResourceLocation;
        //  2192: dup            
        //  2193: ldc_w           "village/savanna/trees"
        //  2196: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2199: new             Lnet/minecraft/resources/ResourceLocation;
        //  2202: dup            
        //  2203: ldc             "empty"
        //  2205: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2208: getstatic       net/minecraft/data/worldgen/Features.ACACIA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2211: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2214: iconst_1       
        //  2215: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2218: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2221: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2224: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2227: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2230: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2233: pop            
        //  2234: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2237: dup            
        //  2238: new             Lnet/minecraft/resources/ResourceLocation;
        //  2241: dup            
        //  2242: ldc_w           "village/savanna/decor"
        //  2245: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2248: new             Lnet/minecraft/resources/ResourceLocation;
        //  2251: dup            
        //  2252: ldc             "empty"
        //  2254: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2257: ldc_w           "village/savanna/savanna_lamp_post_01"
        //  2260: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2263: iconst_4       
        //  2264: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2267: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2270: getstatic       net/minecraft/data/worldgen/Features.ACACIA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2273: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2276: iconst_4       
        //  2277: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2280: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2283: getstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2286: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2289: iconst_4       
        //  2290: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2293: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2296: getstatic       net/minecraft/data/worldgen/Features.PILE_MELON:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2299: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2302: iconst_1       
        //  2303: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2306: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2309: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2312: iconst_4       
        //  2313: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2316: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2319: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2322: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2325: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2328: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2331: pop            
        //  2332: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2335: dup            
        //  2336: new             Lnet/minecraft/resources/ResourceLocation;
        //  2339: dup            
        //  2340: ldc_w           "village/savanna/zombie/decor"
        //  2343: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2346: new             Lnet/minecraft/resources/ResourceLocation;
        //  2349: dup            
        //  2350: ldc             "empty"
        //  2352: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2355: ldc_w           "village/savanna/savanna_lamp_post_01"
        //  2358: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_SAVANNA:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2361: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2364: iconst_4       
        //  2365: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2368: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2371: getstatic       net/minecraft/data/worldgen/Features.ACACIA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2374: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2377: iconst_4       
        //  2378: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2381: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2384: getstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2387: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2390: iconst_4       
        //  2391: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2394: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2397: getstatic       net/minecraft/data/worldgen/Features.PILE_MELON:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2400: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2403: iconst_1       
        //  2404: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2407: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2410: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2413: iconst_4       
        //  2414: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2417: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2420: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2423: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2426: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2429: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2432: pop            
        //  2433: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2436: dup            
        //  2437: new             Lnet/minecraft/resources/ResourceLocation;
        //  2440: dup            
        //  2441: ldc_w           "village/savanna/villagers"
        //  2444: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2447: new             Lnet/minecraft/resources/ResourceLocation;
        //  2450: dup            
        //  2451: ldc             "empty"
        //  2453: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2456: ldc_w           "village/savanna/villagers/nitwit"
        //  2459: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2462: iconst_1       
        //  2463: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2466: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2469: ldc_w           "village/savanna/villagers/baby"
        //  2472: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2475: iconst_1       
        //  2476: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2479: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2482: ldc_w           "village/savanna/villagers/unemployed"
        //  2485: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2488: bipush          10
        //  2490: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2493: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2496: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2499: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2502: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2505: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2508: pop            
        //  2509: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2512: dup            
        //  2513: new             Lnet/minecraft/resources/ResourceLocation;
        //  2516: dup            
        //  2517: ldc_w           "village/savanna/zombie/villagers"
        //  2520: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2523: new             Lnet/minecraft/resources/ResourceLocation;
        //  2526: dup            
        //  2527: ldc             "empty"
        //  2529: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2532: ldc_w           "village/savanna/zombie/villagers/nitwit"
        //  2535: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2538: iconst_1       
        //  2539: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2542: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2545: ldc_w           "village/savanna/zombie/villagers/unemployed"
        //  2548: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2551: bipush          10
        //  2553: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2556: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2559: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2562: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2565: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2568: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2571: pop            
        //  2572: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:201)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitClassType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:173)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitParameterizedType(TypeSubstitutionVisitor.java:25)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visit(TypeSubstitutionVisitor.java:39)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitTypes(TypeSubstitutionVisitor.java:331)
        //     at com.strobel.assembler.metadata.TypeSubstitutionVisitor.visitMethod(TypeSubstitutionVisitor.java:273)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2607)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1362)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
        //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
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
