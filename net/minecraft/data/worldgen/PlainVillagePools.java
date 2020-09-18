package net.minecraft.data.worldgen;

import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

public class PlainVillagePools {
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
        //     8: ldc             "village/plains/town_centers"
        //    10: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    13: new             Lnet/minecraft/resources/ResourceLocation;
        //    16: dup            
        //    17: ldc             "empty"
        //    19: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    22: ldc             "village/plains/town_centers/plains_fountain_01"
        //    24: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_20_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    27: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    30: bipush          50
        //    32: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    35: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    38: ldc             "village/plains/town_centers/plains_meeting_point_1"
        //    40: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_20_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    43: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    46: bipush          50
        //    48: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    51: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    54: ldc             "village/plains/town_centers/plains_meeting_point_2"
        //    56: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //    59: bipush          50
        //    61: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    64: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    67: ldc             "village/plains/town_centers/plains_meeting_point_3"
        //    69: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_70_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    72: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    75: bipush          50
        //    77: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    80: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    83: ldc             "village/plains/zombie/town_centers/plains_fountain_01"
        //    85: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    88: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    91: iconst_1       
        //    92: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    95: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    98: ldc             "village/plains/zombie/town_centers/plains_meeting_point_1"
        //   100: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   103: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   106: iconst_1       
        //   107: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   110: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   113: ldc             "village/plains/zombie/town_centers/plains_meeting_point_2"
        //   115: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   118: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   121: iconst_1       
        //   122: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   125: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   128: ldc             "village/plains/zombie/town_centers/plains_meeting_point_3"
        //   130: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   133: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   136: iconst_1       
        //   137: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   140: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   143: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   146: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   149: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   152: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   155: putstatic       net/minecraft/data/worldgen/PlainVillagePools.START:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   158: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   161: dup            
        //   162: new             Lnet/minecraft/resources/ResourceLocation;
        //   165: dup            
        //   166: ldc             "village/plains/streets"
        //   168: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   171: new             Lnet/minecraft/resources/ResourceLocation;
        //   174: dup            
        //   175: ldc             "village/plains/terminators"
        //   177: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   180: ldc             "village/plains/streets/corner_01"
        //   182: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   185: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   188: iconst_2       
        //   189: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   192: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   195: ldc             "village/plains/streets/corner_02"
        //   197: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   200: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   203: iconst_2       
        //   204: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   207: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   210: ldc             "village/plains/streets/corner_03"
        //   212: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   215: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   218: iconst_2       
        //   219: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   222: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   225: ldc             "village/plains/streets/straight_01"
        //   227: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   230: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   233: iconst_4       
        //   234: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   237: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   240: ldc             "village/plains/streets/straight_02"
        //   242: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   245: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   248: iconst_4       
        //   249: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   252: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   255: ldc             "village/plains/streets/straight_03"
        //   257: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   260: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   263: bipush          7
        //   265: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   268: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   271: ldc             "village/plains/streets/straight_04"
        //   273: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   276: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   279: bipush          7
        //   281: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   284: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   287: ldc             "village/plains/streets/straight_05"
        //   289: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   292: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   295: iconst_3       
        //   296: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   299: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   302: ldc             "village/plains/streets/straight_06"
        //   304: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   307: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   310: iconst_4       
        //   311: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   314: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   317: ldc             "village/plains/streets/crossroad_01"
        //   319: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   322: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   325: iconst_2       
        //   326: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   329: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   332: ldc             "village/plains/streets/crossroad_02"
        //   334: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   337: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   340: iconst_1       
        //   341: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   344: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   347: ldc             "village/plains/streets/crossroad_03"
        //   349: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   352: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   355: iconst_2       
        //   356: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   359: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   362: iconst_4       
        //   363: anewarray       Lcom/mojang/datafixers/util/Pair;
        //   366: dup            
        //   367: iconst_0       
        //   368: ldc             "village/plains/streets/crossroad_04"
        //   370: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   373: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   376: iconst_2       
        //   377: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   380: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   383: aastore        
        //   384: dup            
        //   385: iconst_1       
        //   386: ldc             "village/plains/streets/crossroad_05"
        //   388: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   391: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   394: iconst_2       
        //   395: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   398: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   401: aastore        
        //   402: dup            
        //   403: iconst_2       
        //   404: ldc             "village/plains/streets/crossroad_06"
        //   406: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   409: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   412: iconst_2       
        //   413: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   416: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   419: aastore        
        //   420: dup            
        //   421: iconst_3       
        //   422: ldc             "village/plains/streets/turn_01"
        //   424: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   427: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   430: iconst_3       
        //   431: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   434: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   437: aastore        
        //   438: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   441: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   444: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   447: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   450: pop            
        //   451: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   454: dup            
        //   455: new             Lnet/minecraft/resources/ResourceLocation;
        //   458: dup            
        //   459: ldc             "village/plains/zombie/streets"
        //   461: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   464: new             Lnet/minecraft/resources/ResourceLocation;
        //   467: dup            
        //   468: ldc             "village/plains/terminators"
        //   470: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   473: ldc             "village/plains/zombie/streets/corner_01"
        //   475: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   478: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   481: iconst_2       
        //   482: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   485: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   488: ldc             "village/plains/zombie/streets/corner_02"
        //   490: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   493: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   496: iconst_2       
        //   497: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   500: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   503: ldc             "village/plains/zombie/streets/corner_03"
        //   505: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   508: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   511: iconst_2       
        //   512: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   515: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   518: ldc             "village/plains/zombie/streets/straight_01"
        //   520: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   523: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   526: iconst_4       
        //   527: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   530: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;invokestatic   !!! ERROR
        //   533: ldc             "village/plains/zombie/streets/straight_02"
        //   535: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   538: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   541: iconst_4       
        //   542: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   545: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   548: ldc             "village/plains/zombie/streets/straight_03"
        //   550: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   553: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   556: bipush          7
        //   558: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   561: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   564: ldc             "village/plains/zombie/streets/straight_04"
        //   566: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   569: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   572: bipush          7
        //   574: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   577: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   580: ldc             "village/plains/zombie/streets/straight_05"
        //   582: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   585: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   588: iconst_3       
        //   589: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   592: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   595: ldc             "village/plains/zombie/streets/straight_06"
        //   597: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   600: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   603: iconst_4       
        //   604: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   607: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   610: ldc             "village/plains/zombie/streets/crossroad_01"
        //   612: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   615: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   618: iconst_2       
        //   619: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   622: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   625: ldc             "village/plains/zombie/streets/crossroad_02"
        //   627: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   630: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   633: iconst_1       
        //   634: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   637: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   640: ldc             "village/plains/zombie/streets/crossroad_03"
        //   642: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   645: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   648: iconst_2       
        //   649: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   652: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   655: iconst_4       
        //   656: anewarray       Lcom/mojang/datafixers/util/Pair;
        //   659: dup            
        //   660: iconst_0       
        //   661: ldc             "village/plains/zombie/streets/crossroad_04"
        //   663: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   666: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   669: iconst_2       
        //   670: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   673: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   676: aastore        
        //   677: dup            
        //   678: iconst_1       
        //   679: ldc             "village/plains/zombie/streets/crossroad_05"
        //   681: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   684: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   687: iconst_2       
        //   688: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   691: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   694: aastore        
        //   695: dup            
        //   696: iconst_2       
        //   697: ldc             "village/plains/zombie/streets/crossroad_06"
        //   699: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   702: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   705: iconst_2       
        //   706: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   709: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   712: aastore        
        //   713: dup            
        //   714: iconst_3       
        //   715: ldc             "village/plains/zombie/streets/turn_01"
        //   717: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   720: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   723: iconst_3       
        //   724: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   727: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   730: aastore        
        //   731: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   734: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   737: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   740: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   743: pop            
        //   744: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   747: dup            
        //   748: new             Lnet/minecraft/resources/ResourceLocation;
        //   751: dup            
        //   752: ldc             "village/plains/houses"
        //   754: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   757: new             Lnet/minecraft/resources/ResourceLocation;
        //   760: dup            
        //   761: ldc             "village/plains/terminators"
        //   763: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   766: ldc             "village/plains/houses/plains_small_house_1"
        //   768: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   771: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   774: iconst_2       
        //   775: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   778: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   781: ldc             "village/plains/houses/plains_small_house_2"
        //   783: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   786: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   789: iconst_2       
        //   790: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   793: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   796: ldc             "village/plains/houses/plains_small_house_3"
        //   798: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   801: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   804: iconst_2       
        //   805: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   808: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   811: ldc             "village/plains/houses/plains_small_house_4"
        //   813: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   816: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   819: iconst_2       
        //   820: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   823: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   826: ldc             "village/plains/houses/plains_small_house_5"
        //   828: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   831: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   834: iconst_2       
        //   835: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   838: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   841: ldc             "village/plains/houses/plains_small_house_6"
        //   843: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   846: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   849: iconst_1       
        //   850: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   853: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   856: ldc             "village/plains/houses/plains_small_house_7"
        //   858: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   861: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   864: iconst_2       
        //   865: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   868: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   871: ldc             "village/plains/houses/plains_small_house_8"
        //   873: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   876: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   879: iconst_3       
        //   880: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   883: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   886: ldc             "village/plains/houses/plains_medium_house_1"
        //   888: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   891: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   894: iconst_2       
        //   895: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   898: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   901: ldc             "village/plains/houses/plains_medium_house_2"
        //   903: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   906: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   909: iconst_2       
        //   910: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   913: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   916: ldc             "village/plains/houses/plains_big_house_1"
        //   918: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   921: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   924: iconst_2       
        //   925: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   928: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   931: ldc             "village/plains/houses/plains_butcher_shop_1"
        //   933: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   936: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   939: iconst_2       
        //   940: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   943: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   946: bipush          25
        //   948: anewarray       Lcom/mojang/datafixers/util/Pair;
        //   951: dup            
        //   952: iconst_0       
        //   953: ldc             "village/plains/houses/plains_butcher_shop_2"
        //   955: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   958: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   961: iconst_2       
        //   962: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   965: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   968: aastore        
        //   969: dup            
        //   970: iconst_1       
        //   971: ldc             "village/plains/houses/plains_tool_smith_1"
        //   973: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   976: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   979: iconst_2       
        //   980: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   983: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   986: aastore        
        //   987: dup            
        //   988: iconst_2       
        //   989: ldc             "village/plains/houses/plains_fletcher_house_1"
        //   991: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   994: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   997: iconst_2       
        //   998: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1001: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1004: aastore        
        //  1005: dup            
        //  1006: iconst_3       
        //  1007: ldc             "village/plains/houses/plains_shepherds_house_1"
        //  1009: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1012: iconst_2       
        //  1013: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1016: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1019: aastore        
        //  1020: dup            
        //  1021: iconst_4       
        //  1022: ldc             "village/plains/houses/plains_armorer_house_1"
        //  1024: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1027: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1030: iconst_2       
        //  1031: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1034: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1037: aastore        
        //  1038: dup            
        //  1039: iconst_5       
        //  1040: ldc             "village/plains/houses/plains_fisher_cottage_1"
        //  1042: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1045: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1048: iconst_2       
        //  1049: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1052: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1055: aastore        
        //  1056: dup            
        //  1057: bipush          6
        //  1059: ldc             "village/plains/houses/plains_tannery_1"
        //  1061: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1064: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1067: iconst_2       
        //  1068: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1071: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1074: aastore        
        //  1075: dup            
        //  1076: bipush          7
        //  1078: ldc             "village/plains/houses/plains_cartographer_1"
        //  1080: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1083: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1086: iconst_1       
        //  1087: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1090: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1093: aastore        
        //  1094: dup            
        //  1095: bipush          8
        //  1097: ldc             "village/plains/houses/plains_library_1"
        //  1099: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1102: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1105: iconst_5       
        //  1106: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1109: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1112: aastore        
        //  1113: dup            
        //  1114: bipush          9
        //  1116: ldc             "village/plains/houses/plains_library_2"
        //  1118: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1121: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1124: iconst_1       
        //  1125: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1128: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1131: aastore        
        //  1132: dup            
        //  1133: bipush          10
        //  1135: ldc             "village/plains/houses/plains_masons_house_1"
        //  1137: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1140: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1143: iconst_2       
        //  1144: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1147: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1150: aastore        
        //  1151: dup            
        //  1152: bipush          11
        //  1154: ldc             "village/plains/houses/plains_weaponsmith_1"
        //  1156: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1159: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1162: iconst_2       
        //  1163: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1166: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1169: aastore        
        //  1170: dup            
        //  1171: bipush          12
        //  1173: ldc             "village/plains/houses/plains_temple_3"
        //  1175: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1178: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1181: iconst_2       
        //  1182: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1185: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1188: aastore        
        //  1189: dup            
        //  1190: bipush          13
        //  1192: ldc             "village/plains/houses/plains_temple_4"
        //  1194: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1197: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1200: iconst_2       
        //  1201: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1204: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1207: aastore        
        //  1208: dup            
        //  1209: bipush          14
        //  1211: ldc             "village/plains/houses/plains_stable_1"
        //  1213: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_10_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1216: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1219: iconst_2       
        //  1220: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1223: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1226: aastore        
        //  1227: dup            
        //  1228: bipush          15
        //  1230: ldc             "village/plains/houses/plains_stable_2"
        //  1232: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1235: iconst_2       
        //  1236: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1239: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1242: aastore        
        //  1243: dup            
        //  1244: bipush          16
        //  1246: ldc             "village/plains/houses/plains_large_farm_1"
        //  1248: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1251: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1254: iconst_4       
        //  1255: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1258: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1261: aastore        
        //  1262: dup            
        //  1263: bipush          17
        //  1265: ldc             "village/plains/houses/plains_small_farm_1"
        //  1267: getstatic       net/minecraft/data/worldgen/ProcessorLists.FARM_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1270: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1273: iconst_4       
        //  1274: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1277: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1280: aastore        
        //  1281: dup            
        //  1282: bipush          18
        //  1284: ldc             "village/plains/houses/plains_animal_pen_1"
        //  1286: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1289: iconst_1       
        //  1290: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1293: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1296: aastore        
        //  1297: dup            
        //  1298: bipush          19
        //  1300: ldc             "village/plains/houses/plains_animal_pen_2"
        //  1302: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1305: iconst_1       
        //  1306: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1309: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1312: aastore        
        //  1313: dup            
        //  1314: bipush          20
        //  1316: ldc             "village/plains/houses/plains_animal_pen_3"
        //  1318: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1321: iconst_5       
        //  1322: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1325: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1328: aastore        
        //  1329: dup            
        //  1330: bipush          21
        //  1332: ldc             "village/plains/houses/plains_accessory_1"
        //  1334: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1337: iconst_1       
        //  1338: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1341: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1344: aastore        
        //  1345: dup            
        //  1346: bipush          22
        //  1348: ldc             "village/plains/houses/plains_meeting_point_4"
        //  1350: getstatic       net/minecraft/data/worldgen/ProcessorLists.MOSSIFY_70_PERCENT:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1353: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1356: iconst_3       
        //  1357: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1360: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1363: aastore        
        //  1364: dup            
        //  1365: bipush          23
        //  1367: ldc             "village/plains/houses/plains_meeting_point_5"
        //  1369: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  1372: iconst_1       
        //  1373: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1376: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1379: aastore        
        //  1380: dup            
        //  1381: bipush          24
        //  1383: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  1386: bipush          10
        //  1388: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1391: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1394: aastore        
        //  1395: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1398: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1401: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1404: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1407: pop            
        //  1408: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1411: dup            
        //  1412: new             Lnet/minecraft/resources/ResourceLocation;
        //  1415: dup            
        //  1416: ldc_w           "village/plains/zombie/houses"
        //  1419: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1422: new             Lnet/minecraft/resources/ResourceLocation;
        //  1425: dup            
        //  1426: ldc             "village/plains/terminators"
        //  1428: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1431: ldc_w           "village/plains/zombie/houses/plains_small_house_1"
        //  1434: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1437: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1440: iconst_2       
        //  1441: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1444: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1447: ldc_w           "village/plains/zombie/houses/plains_small_house_2"
        //  1450: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1453: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1456: iconst_2       
        //  1457: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1460: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1463: ldc_w           "village/plains/zombie/houses/plains_small_house_3"
        //  1466: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1469: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1472: iconst_2       
        //  1473: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1476: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1479: ldc_w           "village/plains/zombie/houses/plains_small_house_4"
        //  1482: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1485: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1488: iconst_2       
        //  1489: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1492: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1495: ldc_w           "village/plains/zombie/houses/plains_small_house_5"
        //  1498: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1501: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1504: iconst_2       
        //  1505: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1508: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1511: ldc_w           "village/plains/zombie/houses/plains_small_house_6"
        //  1514: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1517: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1520: iconst_1       
        //  1521: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1524: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1527: ldc_w           "village/plains/zombie/houses/plains_small_house_7"
        //  1530: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1533: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1536: iconst_2       
        //  1537: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1540: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1543: ldc_w           "village/plains/zombie/houses/plains_small_house_8"
        //  1546: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1549: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1552: iconst_2       
        //  1553: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1556: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1559: ldc_w           "village/plains/zombie/houses/plains_medium_house_1"
        //  1562: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1565: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1568: iconst_2       
        //  1569: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1572: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1575: ldc_w           "village/plains/zombie/houses/plains_medium_house_2"
        //  1578: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1581: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1584: iconst_2       
        //  1585: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1588: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1591: ldc_w           "village/plains/zombie/houses/plains_big_house_1"
        //  1594: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1597: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1600: iconst_2       
        //  1601: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1604: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1607: ldc             "village/plains/houses/plains_butcher_shop_1"
        //  1609: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1612: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1615: iconst_2       
        //  1616: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1619: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1622: bipush          24
        //  1624: anewarray       Lcom/mojang/datafixers/util/Pair;
        //  1627: dup            
        //  1628: iconst_0       
        //  1629: ldc_w           "village/plains/zombie/houses/plains_butcher_shop_2"
        //  1632: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1635: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1638: iconst_2       
        //  1639: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1642: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1645: aastore        
        //  1646: dup            
        //  1647: iconst_1       
        //  1648: ldc             "village/plains/houses/plains_tool_smith_1"
        //  1650: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1653: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1656: iconst_2       
        //  1657: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1660: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1663: aastore        
        //  1664: dup            
        //  1665: iconst_2       
        //  1666: ldc_w           "village/plains/zombie/houses/plains_fletcher_house_1"
        //  1669: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1672: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1675: iconst_2       
        //  1676: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1679: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1682: aastore        
        //  1683: dup            
        //  1684: iconst_3       
        //  1685: ldc_w           "village/plains/zombie/houses/plains_shepherds_house_1"
        //  1688: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1691: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1694: iconst_2       
        //  1695: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1698: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1701: aastore        
        //  1702: dup            
        //  1703: iconst_4       
        //  1704: ldc             "village/plains/houses/plains_armorer_house_1"
        //  1706: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1709: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1712: iconst_2       
        //  1713: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1716: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1719: aastore        
        //  1720: dup            
        //  1721: iconst_5       
        //  1722: ldc             "village/plains/houses/plains_fisher_cottage_1"
        //  1724: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1727: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1730: iconst_2       
        //  1731: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1734: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1737: aastore        
        //  1738: dup            
        //  1739: bipush          6
        //  1741: ldc             "village/plains/houses/plains_tannery_1"
        //  1743: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1746: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1749: iconst_2       
        //  1750: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1753: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1756: aastore        
        //  1757: dup            
        //  1758: bipush          7
        //  1760: ldc             "village/plains/houses/plains_cartographer_1"
        //  1762: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1765: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1768: iconst_1       
        //  1769: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1772: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1775: aastore        
        //  1776: dup            
        //  1777: bipush          8
        //  1779: ldc             "village/plains/houses/plains_library_1"
        //  1781: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1784: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1787: iconst_3       
        //  1788: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1791: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1794: aastore        
        //  1795: dup            
        //  1796: bipush          9
        //  1798: ldc             "village/plains/houses/plains_library_2"
        //  1800: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1803: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1806: iconst_1       
        //  1807: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1810: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1813: aastore        
        //  1814: dup            
        //  1815: bipush          10
        //  1817: ldc             "village/plains/houses/plains_masons_house_1"
        //  1819: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1822: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1825: iconst_2       
        //  1826: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1829: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1832: aastore        
        //  1833: dup            
        //  1834: bipush          11
        //  1836: ldc             "village/plains/houses/plains_weaponsmith_1"
        //  1838: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1841: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1844: iconst_2       
        //  1845: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1848: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1851: aastore        
        //  1852: dup            
        //  1853: bipush          12
        //  1855: ldc             "village/plains/houses/plains_temple_3"
        //  1857: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1860: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1863: iconst_2       
        //  1864: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1867: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1870: aastore        
        //  1871: dup            
        //  1872: bipush          13
        //  1874: ldc             "village/plains/houses/plains_temple_4"
        //  1876: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1879: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1882: iconst_2       
        //  1883: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1886: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1889: aastore        
        //  1890: dup            
        //  1891: bipush          14
        //  1893: ldc_w           "village/plains/zombie/houses/plains_stable_1"
        //  1896: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1899: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1902: iconst_2       
        //  1903: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1906: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1909: aastore        
        //  1910: dup            
        //  1911: bipush          15
        //  1913: ldc             "village/plains/houses/plains_stable_2"
        //  1915: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1918: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1921: iconst_2       
        //  1922: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1925: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1928: aastore        
        //  1929: dup            
        //  1930: bipush          16
        //  1932: ldc             "village/plains/houses/plains_large_farm_1"
        //  1934: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1937: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1940: iconst_4       
        //  1941: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1944: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1947: aastore        
        //  1948: dup            
        //  1949: bipush          17
        //  1951: ldc             "village/plains/houses/plains_small_farm_1"
        //  1953: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1956: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1959: iconst_4       
        //  1960: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1963: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1966: aastore        
        //  1967: dup            
        //  1968: bipush          18
        //  1970: ldc             "village/plains/houses/plains_animal_pen_1"
        //  1972: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1975: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1978: iconst_1       
        //  1979: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1982: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1985: aastore        
        //  1986: dup            
        //  1987: bipush          19
        //  1989: ldc             "village/plains/houses/plains_animal_pen_2"
        //  1991: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1994: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1997: iconst_1       
        //  1998: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2001: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2004: aastore        
        //  2005: dup            
        //  2006: bipush          20
        //  2008: ldc_w           "village/plains/zombie/houses/plains_animal_pen_3"
        //  2011: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2014: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2017: iconst_5       
        //  2018: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2021: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2024: aastore        
        //  2025: dup            
        //  2026: bipush          21
        //  2028: ldc_w           "village/plains/zombie/houses/plains_meeting_point_4"
        //  2031: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2034: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2037: iconst_3       
        //  2038: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2041: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2044: aastore        
        //  2045: dup            
        //  2046: bipush          22
        //  2048: ldc_w           "village/plains/zombie/houses/plains_meeting_point_5"
        //  2051: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2054: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2057: iconst_1       
        //  2058: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2061: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2064: aastore        
        //  2065: dup            
        //  2066: bipush          23
        //  2068: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2071: bipush          10
        //  2073: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2076: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2079: aastore        
        //  2080: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2083: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2086: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2089: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2092: pop            
        //  2093: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2096: dup            
        //  2097: new             Lnet/minecraft/resources/ResourceLocation;
        //  2100: dup            
        //  2101: ldc             "village/plains/terminators"
        //  2103: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2106: new             Lnet/minecraft/resources/ResourceLocation;
        //  2109: dup            
        //  2110: ldc             "empty"
        //  2112: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2115: ldc_w           "village/plains/terminators/terminator_01"
        //  2118: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2121: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2124: iconst_1       
        //  2125: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2128: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2131: ldc_w           "village/plains/terminators/terminator_02"
        //  2134: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2137: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2140: iconst_1       
        //  2141: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2144: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2147: ldc_w           "village/plains/terminators/terminator_03"
        //  2150: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2153: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2156: iconst_1       
        //  2157: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2160: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2163: ldc_w           "village/plains/terminators/terminator_04"
        //  2166: getstatic       net/minecraft/data/worldgen/ProcessorLists.STREET_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2169: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2172: iconst_1       
        //  2173: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2176: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2179: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2182: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.TERRAIN_MATCHING:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2185: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2188: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2191: pop            
        //  2192: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2195: dup            
        //  2196: new             Lnet/minecraft/resources/ResourceLocation;
        //  2199: dup            
        //  2200: ldc_w           "village/plains/trees"
        //  2203: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2206: new             Lnet/minecraft/resources/ResourceLocation;
        //  2209: dup            
        //  2210: ldc             "empty"
        //  2212: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2215: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2218: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2221: iconst_1       
        //  2222: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2225: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2228: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2231: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2234: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2237: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2240: pop            
        //  2241: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2244: dup            
        //  2245: new             Lnet/minecraft/resources/ResourceLocation;
        //  2248: dup            
        //  2249: ldc_w           "village/plains/decor"
        //  2252: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2255: new             Lnet/minecraft/resources/ResourceLocation;
        //  2258: dup            
        //  2259: ldc             "empty"
        //  2261: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2264: ldc_w           "village/plains/plains_lamp_1"
        //  2267: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2270: iconst_2       
        //  2271: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2274: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2277: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2280: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2283: iconst_1       
        //  2284: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2287: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2290: getstatic       net/minecraft/data/worldgen/Features.FLOWER_PLAIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2293: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2296: iconst_1       
        //  2297: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2300: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2303: getstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2306: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2309: iconst_1       
        //  2310: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2313: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2316: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2319: iconst_2       
        //  2320: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2323: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2326: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2329: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2332: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2335: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2338: pop            
        //  2339: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2342: dup            
        //  2343: new             Lnet/minecraft/resources/ResourceLocation;
        //  2346: dup            
        //  2347: ldc_w           "village/plains/zombie/decor"
        //  2350: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2353: new             Lnet/minecraft/resources/ResourceLocation;
        //  2356: dup            
        //  2357: ldc             "empty"
        //  2359: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2362: ldc_w           "village/plains/plains_lamp_1"
        //  2365: getstatic       net/minecraft/data/worldgen/ProcessorLists.ZOMBIE_PLAINS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  2368: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  2371: iconst_1       
        //  2372: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2375: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2378: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2381: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2384: iconst_1       
        //  2385: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2388: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2391: getstatic       net/minecraft/data/worldgen/Features.FLOWER_PLAIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2394: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2397: iconst_1       
        //  2398: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2401: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2404: getstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2407: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.feature:(Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Ljava/util/function/Function;
        //  2410: iconst_1       
        //  2411: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2414: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2417: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2420: iconst_2       
        //  2421: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2424: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2427: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2430: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2433: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2436: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2439: pop            
        //  2440: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2443: dup            
        //  2444: new             Lnet/minecraft/resources/ResourceLocation;
        //  2447: dup            
        //  2448: ldc_w           "village/plains/villagers"
        //  2451: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2454: new             Lnet/minecraft/resources/ResourceLocation;
        //  2457: dup            
        //  2458: ldc             "empty"
        //  2460: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2463: ldc_w           "village/plains/villagers/nitwit"
        //  2466: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2469: iconst_1       
        //  2470: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2473: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2476: ldc_w           "village/plains/villagers/baby"
        //  2479: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2482: iconst_1       
        //  2483: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2486: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2489: ldc_w           "village/plains/villagers/unemployed"
        //  2492: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2495: bipush          10
        //  2497: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2500: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2503: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2506: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2509: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2512: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2515: pop            
        //  2516: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2519: dup            
        //  2520: new             Lnet/minecraft/resources/ResourceLocation;
        //  2523: dup            
        //  2524: ldc_w           "village/plains/zombie/villagers"
        //  2527: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2530: new             Lnet/minecraft/resources/ResourceLocation;
        //  2533: dup            
        //  2534: ldc             "empty"
        //  2536: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2539: ldc_w           "village/plains/zombie/villagers/nitwit"
        //  2542: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2545: iconst_1       
        //  2546: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2549: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2552: ldc_w           "village/plains/zombie/villagers/unemployed"
        //  2555: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2558: bipush          10
        //  2560: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2563: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2566: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2569: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2572: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2575: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2578: pop            
        //  2579: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2582: dup            
        //  2583: new             Lnet/minecraft/resources/ResourceLocation;
        //  2586: dup            
        //  2587: ldc_w           "village/common/animals"
        //  2590: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2593: new             Lnet/minecraft/resources/ResourceLocation;
        //  2596: dup            
        //  2597: ldc             "empty"
        //  2599: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2602: ldc_w           "village/common/animals/cows_1"
        //  2605: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2608: bipush          7
        //  2610: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2613: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2616: ldc_w           "village/common/animals/pigs_1"
        //  2619: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2622: bipush          7
        //  2624: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2627: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2630: ldc_w           "village/common/animals/horses_1"
        //  2633: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2636: iconst_1       
        //  2637: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2640: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2643: ldc_w           "village/common/animals/horses_2"
        //  2646: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2649: iconst_1       
        //  2650: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2653: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2656: ldc_w           "village/common/animals/horses_3"
        //  2659: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2662: iconst_1       
        //  2663: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2666: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2669: ldc_w           "village/common/animals/horses_4"
        //  2672: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2675: iconst_1       
        //  2676: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2679: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2682: ldc_w           "village/common/animals/horses_5"
        //  2685: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2688: iconst_1       
        //  2689: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2692: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2695: ldc_w           "village/common/animals/sheep_1"
        //  2698: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2701: iconst_1       
        //  2702: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2705: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2708: ldc_w           "village/common/animals/sheep_2"
        //  2711: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2714: iconst_1       
        //  2715: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2718: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2721: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2724: iconst_5       
        //  2725: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2728: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2731: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2734: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2737: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2740: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2743: pop            
        //  2744: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2747: dup            
        //  2748: new             Lnet/minecraft/resources/ResourceLocation;
        //  2751: dup            
        //  2752: ldc_w           "village/common/sheep"
        //  2755: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2758: new             Lnet/minecraft/resources/ResourceLocation;
        //  2761: dup            
        //  2762: ldc             "empty"
        //  2764: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2767: ldc_w           "village/common/animals/sheep_1"
        //  2770: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2773: iconst_1       
        //  2774: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2777: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2780: ldc_w           "village/common/animals/sheep_2"
        //  2783: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2786: iconst_1       
        //  2787: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2790: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2793: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2796: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2799: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2802: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2805: pop            
        //  2806: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2809: dup            
        //  2810: new             Lnet/minecraft/resources/ResourceLocation;
        //  2813: dup            
        //  2814: ldc_w           "village/common/cats"
        //  2817: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2820: new             Lnet/minecraft/resources/ResourceLocation;
        //  2823: dup            
        //  2824: ldc             "empty"
        //  2826: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2829: ldc_w           "village/common/animals/cat_black"
        //  2832: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2835: iconst_1       
        //  2836: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2839: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2842: ldc_w           "village/common/animals/cat_british"
        //  2845: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2848: iconst_1       
        //  2849: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2852: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2855: ldc_w           "village/common/animals/cat_calico"
        //  2858: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2861: iconst_1       
        //  2862: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2865: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2868: ldc_w           "village/common/animals/cat_persian"
        //  2871: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2874: iconst_1       
        //  2875: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2878: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2881: ldc_w           "village/common/animals/cat_ragdoll"
        //  2884: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2887: iconst_1       
        //  2888: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2891: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2894: ldc_w           "village/common/animals/cat_red"
        //  2897: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2900: iconst_1       
        //  2901: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2904: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2907: ldc_w           "village/common/animals/cat_siamese"
        //  2910: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2913: iconst_1       
        //  2914: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2917: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2920: ldc_w           "village/common/animals/cat_tabby"
        //  2923: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2926: iconst_1       
        //  2927: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2930: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2933: ldc_w           "village/common/animals/cat_white"
        //  2936: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2939: iconst_1       
        //  2940: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2943: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2946: ldc_w           "village/common/animals/cat_jellie"
        //  2949: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  2952: iconst_1       
        //  2953: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2956: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2959: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.empty:()Ljava/util/function/Function;
        //  2962: iconst_3       
        //  2963: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  2966: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  2969: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  2972: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  2975: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  2978: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2981: pop            
        //  2982: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  2985: dup            
        //  2986: new             Lnet/minecraft/resources/ResourceLocation;
        //  2989: dup            
        //  2990: ldc_w           "village/common/butcher_animals"
        //  2993: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  2996: new             Lnet/minecraft/resources/ResourceLocation;
        //  2999: dup            
        //  3000: ldc             "empty"
        //  3002: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  3005: ldc_w           "village/common/animals/cows_1"
        //  3008: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  3011: iconst_3       
        //  3012: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3015: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  3018: ldc_w           "village/common/animals/pigs_1"
        //  3021: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  3024: iconst_3       
        //  3025: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3028: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  3031: ldc_w           "village/common/animals/sheep_1"
        //  3034: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  3037: iconst_1       
        //  3038: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3041: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  3044: ldc_w           "village/common/animals/sheep_2"
        //  3047: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  3050: iconst_1       
        //  3051: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3054: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  3057: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  3060: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  3063: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  3066: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  3069: pop            
        //  3070: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  3073: dup            
        //  3074: new             Lnet/minecraft/resources/ResourceLocation;
        //  3077: dup            
        //  3078: ldc_w           "village/common/iron_golem"
        //  3081: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  3084: new             Lnet/minecraft/resources/ResourceLocation;
        //  3087: dup            
        //  3088: ldc             "empty"
        //  3090: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  3093: ldc_w           "village/common/iron_golem"
        //  3096: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  3099: iconst_1       
        //  3100: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3103: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  3106: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  3109: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  3112: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  3115: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  3118: pop            
        //  3119: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  3122: dup            
        //  3123: new             Lnet/minecraft/resources/ResourceLocation;
        //  3126: dup            
        //  3127: ldc_w           "village/common/well_bottoms"
        //  3130: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  3133: new             Lnet/minecraft/resources/ResourceLocation;
        //  3136: dup            
        //  3137: ldc             "empty"
        //  3139: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  3142: ldc_w           "village/common/well_bottom"
        //  3145: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.legacy:(Ljava/lang/String;)Ljava/util/function/Function;
        //  3148: iconst_1       
        //  3149: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  3152: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  3155: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  3158: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  3161: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  3164: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  3167: pop            
        //  3168: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 5
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.ParameterizedType.getGenericParameters(ParameterizedType.java:71)
        //     at com.strobel.assembler.metadata.TypeReference.hasGenericParameters(TypeReference.java:244)
        //     at com.strobel.assembler.metadata.TypeReference.isGenericType(TypeReference.java:263)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitClassType(MetadataHelper.java:2138)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitParameterizedType(MetadataHelper.java:2165)
        //     at com.strobel.assembler.metadata.MetadataHelper$9.visitParameterizedType(MetadataHelper.java:2075)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
        //     at com.strobel.assembler.metadata.MetadataHelper.getSuperType(MetadataHelper.java:1264)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitClassType(MetadataHelper.java:2011)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:2056)
        //     at com.strobel.assembler.metadata.MetadataHelper$8.visitParameterizedType(MetadataHelper.java:1994)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
        //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
        //     at com.strobel.assembler.metadata.MetadataHelper.asSuper(MetadataHelper.java:727)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitClassType(MetadataHelper.java:1853)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1882)
        //     at com.strobel.assembler.metadata.MetadataHelper$6.visitParameterizedType(MetadataHelper.java:1815)
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
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
    }
}
