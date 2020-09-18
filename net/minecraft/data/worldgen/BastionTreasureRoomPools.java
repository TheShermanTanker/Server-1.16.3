package net.minecraft.data.worldgen;

public class BastionTreasureRoomPools {
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
        //     8: ldc             "bastion/treasure/bases"
        //    10: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    13: new             Lnet/minecraft/resources/ResourceLocation;
        //    16: dup            
        //    17: ldc             "empty"
        //    19: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    22: ldc             "bastion/treasure/bases/lava_basin"
        //    24: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    27: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    30: iconst_1       
        //    31: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    34: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    37: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //    40: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //    43: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //    46: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //    49: pop            
        //    50: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //    53: dup            
        //    54: new             Lnet/minecraft/resources/ResourceLocation;
        //    57: dup            
        //    58: ldc             "bastion/treasure/stairs"
        //    60: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    63: new             Lnet/minecraft/resources/ResourceLocation;
        //    66: dup            
        //    67: ldc             "empty"
        //    69: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //    72: ldc             "bastion/treasure/stairs/lower_stairs"
        //    74: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //    77: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //    80: iconst_1       
        //    81: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //    84: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //    87: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //    90: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //    93: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //    96: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //    99: pop            
        //   100: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   103: dup            
        //   104: new             Lnet/minecraft/resources/ResourceLocation;
        //   107: dup            
        //   108: ldc             "bastion/treasure/bases/centers"
        //   110: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   113: new             Lnet/minecraft/resources/ResourceLocation;
        //   116: dup            
        //   117: ldc             "empty"
        //   119: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   122: ldc             "bastion/treasure/bases/centers/center_0"
        //   124: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   127: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   130: iconst_1       
        //   131: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   134: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   137: ldc             "bastion/treasure/bases/centers/center_1"
        //   139: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   142: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   145: iconst_1       
        //   146: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   149: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   152: ldc             "bastion/treasure/bases/centers/center_2"
        //   154: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   157: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   160: iconst_1       
        //   161: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   164: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   167: ldc             "bastion/treasure/bases/centers/center_3"
        //   169: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   172: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   175: iconst_1       
        //   176: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   179: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   182: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   185: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   188: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   191: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   194: pop            
        //   195: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   198: dup            
        //   199: new             Lnet/minecraft/resources/ResourceLocation;
        //   202: dup            
        //   203: ldc             "bastion/treasure/brains"
        //   205: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   208: new             Lnet/minecraft/resources/ResourceLocation;
        //   211: dup            
        //   212: ldc             "empty"
        //   214: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   217: ldc             "bastion/treasure/brains/center_brain"
        //   219: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   222: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   225: iconst_1       
        //   226: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   229: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   232: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   235: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   238: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   241: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   244: pop            
        //   245: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   248: dup            
        //   249: new             Lnet/minecraft/resources/ResourceLocation;
        //   252: dup            
        //   253: ldc             "bastion/treasure/walls"
        //   255: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   258: new             Lnet/minecraft/resources/ResourceLocation;
        //   261: dup            
        //   262: ldc             "empty"
        //   264: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   267: ldc             "bastion/treasure/walls/lava_wall"
        //   269: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   272: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   275: iconst_1       
        //   276: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   279: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   282: ldc             "bastion/treasure/walls/entrance_wall"
        //   284: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   287: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   290: iconst_1       
        //   291: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   294: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   297: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   300: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   303: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   306: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   309: pop            
        //   310: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   313: dup            
        //   314: new             Lnet/minecraft/resources/ResourceLocation;
        //   317: dup            
        //   318: ldc             "bastion/treasure/walls/outer"
        //   320: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   323: new             Lnet/minecraft/resources/ResourceLocation;
        //   326: dup            
        //   327: ldc             "empty"
        //   329: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   332: ldc             "bastion/treasure/walls/outer/top_corner"
        //   334: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   337: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   340: iconst_1       
        //   341: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   344: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   347: ldc             "bastion/treasure/walls/outer/mid_corner"
        //   349: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   352: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   355: iconst_1       
        //   356: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   359: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   362: ldc             "bastion/treasure/walls/outer/bottom_corner"
        //   364: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   367: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   370: iconst_1       
        //   371: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   374: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   377: ldc             "bastion/treasure/walls/outer/outer_wall"
        //   379: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   382: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   385: iconst_1       
        //   386: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   389: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   392: ldc             "bastion/treasure/walls/outer/medium_outer_wall"
        //   394: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   397: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   400: iconst_1       
        //   401: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   404: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   407: ldc             "bastion/treasure/walls/outer/tall_outer_wall"
        //   409: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   412: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   415: iconst_1       
        //   416: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   419: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   422: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   425: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   428: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   431: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   434: pop            
        //   435: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   438: dup            
        //   439: new             Lnet/minecraft/resources/ResourceLocation;
        //   442: dup            
        //   443: ldc             "bastion/treasure/walls/bottom"
        //   445: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   448: new             Lnet/minecraft/resources/ResourceLocation;
        //   451: dup            
        //   452: ldc             "empty"
        //   454: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   457: ldc             "bastion/treasure/walls/bottom/wall_0"
        //   459: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   462: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   465: iconst_1       
        //   466: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   469: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   472: ldc             "bastion/treasure/walls/bottom/wall_1"
        //   474: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   477: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   480: iconst_1       
        //   481: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   484: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   487: ldc             "bastion/treasure/walls/bottom/wall_2"
        //   489: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   492: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   495: iconst_1       
        //   496: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   499: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   502: ldc             "bastion/treasure/walls/bottom/wall_3"
        //   504: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   507: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   510: iconst_1       
        //   511: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   514: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   517: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   520: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   523: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   526: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   529: pop            
        //   530: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   533: dup            
        //   534: new             Lnet/minecraft/resources/ResourceLocation;
        //   537: dup            
        //   538: ldc             "bastion/treasure/walls/mid"
        //   540: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   543: new             Lnet/minecraft/resources/ResourceLocation;
        //   546: dup            
        //   547: ldc             "empty"
        //   549: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   552: ldc             "bastion/treasure/walls/mid/wall_0"
        //   554: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   557: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   560: iconst_1       
        //   561: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   564: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   567: ldc             "bastion/treasure/walls/mid/wall_1"
        //   569: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   572: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   575: iconst_1       
        //   576: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   579: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   582: ldc             "bastion/treasure/walls/mid/wall_2"
        //   584: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   587: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   590: iconst_1       
        //   591: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   594: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   597: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   600: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   603: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   606: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   609: pop            
        //   610: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   613: dup            
        //   614: new             Lnet/minecraft/resources/ResourceLocation;
        //   617: dup            
        //   618: ldc             "bastion/treasure/walls/top"
        //   620: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   623: new             Lnet/minecraft/resources/ResourceLocation;
        //   626: dup            
        //   627: ldc             "empty"
        //   629: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   632: ldc             "bastion/treasure/walls/top/main_entrance"
        //   634: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   637: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   640: iconst_1       
        //   641: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   644: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   647: ldc             "bastion/treasure/walls/top/wall_0"
        //   649: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   652: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   655: iconst_1       
        //   656: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   659: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   662: ldc             "bastion/treasure/walls/top/wall_1"
        //   664: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   667: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   670: iconst_1       
        //   671: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   674: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   677: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   680: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   683: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   686: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   689: pop            
        //   690: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   693: dup            
        //   694: new             Lnet/minecraft/resources/ResourceLocation;
        //   697: dup            
        //   698: ldc             "bastion/treasure/connectors"
        //   700: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   703: new             Lnet/minecraft/resources/ResourceLocation;
        //   706: dup            
        //   707: ldc             "empty"
        //   709: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   712: ldc             "bastion/treasure/connectors/center_to_wall_middle"
        //   714: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   717: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   720: iconst_1       
        //   721: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   724: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   727: ldc             "bastion/treasure/connectors/center_to_wall_top"
        //   729: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   732: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   735: iconst_1       
        //   736: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   739: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   742: ldc             "bastion/treasure/connectors/center_to_wall_top_entrance"
        //   744: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   747: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   750: iconst_1       
        //   751: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   754: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   757: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   760: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   763: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   766: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   769: pop            
        //   770: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   773: dup            
        //   774: new             Lnet/minecraft/resources/ResourceLocation;
        //   777: dup            
        //   778: ldc             "bastion/treasure/entrances"
        //   780: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   783: new             Lnet/minecraft/resources/ResourceLocation;
        //   786: dup            
        //   787: ldc             "empty"
        //   789: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   792: ldc             "bastion/treasure/entrances/entrance_0"
        //   794: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   797: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   800: iconst_1       
        //   801: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   804: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   807: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   810: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   813: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   816: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   819: pop            
        //   820: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   823: dup            
        //   824: new             Lnet/minecraft/resources/ResourceLocation;
        //   827: dup            
        //   828: ldc             "bastion/treasure/ramparts"
        //   830: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   833: new             Lnet/minecraft/resources/ResourceLocation;
        //   836: dup            
        //   837: ldc             "empty"
        //   839: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   842: ldc             "bastion/treasure/ramparts/mid_wall_main"
        //   844: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   847: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   850: iconst_1       
        //   851: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   854: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   857: ldc             "bastion/treasure/ramparts/mid_wall_side"
        //   859: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   862: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   865: iconst_1       
        //   866: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   869: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   872: ldc             "bastion/treasure/ramparts/bottom_wall_0"
        //   874: getstatic       net/minecraft/data/worldgen/ProcessorLists.BOTTOM_RAMPART:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   877: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   880: iconst_1       
        //   881: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   884: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   887: ldc             "bastion/treasure/ramparts/top_wall"
        //   889: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_RAMPART:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   892: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   895: iconst_1       
        //   896: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   899: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   902: ldc             "bastion/treasure/ramparts/lava_basin_side"
        //   904: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   907: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   910: iconst_1       
        //   911: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   914: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   917: ldc             "bastion/treasure/ramparts/lava_basin_main"
        //   919: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   922: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   925: iconst_1       
        //   926: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   929: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   932: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //   935: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //   938: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //   941: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   944: pop            
        //   945: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //   948: dup            
        //   949: new             Lnet/minecraft/resources/ResourceLocation;
        //   952: dup            
        //   953: ldc             "bastion/treasure/corners/bottom"
        //   955: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   958: new             Lnet/minecraft/resources/ResourceLocation;
        //   961: dup            
        //   962: ldc             "empty"
        //   964: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //   967: ldc             "bastion/treasure/corners/bottom/corner_0"
        //   969: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   972: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   975: iconst_1       
        //   976: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   979: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   982: ldc             "bastion/treasure/corners/bottom/corner_1"
        //   984: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //   987: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //   990: iconst_1       
        //   991: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   994: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //   997: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1000: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1003: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1006: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1009: pop            
        //  1010: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1013: dup            
        //  1014: new             Lnet/minecraft/resources/ResourceLocation;
        //  1017: dup            
        //  1018: ldc             "bastion/treasure/corners/edges"
        //  1020: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1023: new             Lnet/minecraft/resources/ResourceLocation;
        //  1026: dup            
        //  1027: ldc             "empty"
        //  1029: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1032: ldc             "bastion/treasure/corners/edges/bottom"
        //  1034: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1037: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1040: iconst_1       
        //  1041: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1044: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1047: ldc             "bastion/treasure/corners/edges/middle"
        //  1049: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1052: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1055: iconst_1       
        //  1056: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1059: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1062: ldc             "bastion/treasure/corners/edges/top"
        //  1064: getstatic       net/minecraft/data/worldgen/ProcessorLists.HIGH_WALL:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1067: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1070: iconst_1       
        //  1071: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1074: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1077: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1080: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1083: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1086: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1089: pop            
        //  1090: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1093: dup            
        //  1094: new             Lnet/minecraft/resources/ResourceLocation;
        //  1097: dup            
        //  1098: ldc             "bastion/treasure/corners/middle"
        //  1100: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1103: new             Lnet/minecraft/resources/ResourceLocation;
        //  1106: dup            
        //  1107: ldc             "empty"
        //  1109: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1112: ldc             "bastion/treasure/corners/middle/corner_0"
        //  1114: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1117: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1120: iconst_1       
        //  1121: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1124: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1127: ldc             "bastion/treasure/corners/middle/corner_1"
        //  1129: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1132: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1135: iconst_1       
        //  1136: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1139: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1142: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1145: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1148: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1151: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1154: pop            
        //  1155: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1158: dup            
        //  1159: new             Lnet/minecraft/resources/ResourceLocation;
        //  1162: dup            
        //  1163: ldc             "bastion/treasure/corners/top"
        //  1165: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1168: new             Lnet/minecraft/resources/ResourceLocation;
        //  1171: dup            
        //  1172: ldc             "empty"
        //  1174: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1177: ldc             "bastion/treasure/corners/top/corner_0"
        //  1179: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1182: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1185: iconst_1       
        //  1186: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1189: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1192: ldc             "bastion/treasure/corners/top/corner_1"
        //  1194: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1197: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1200: iconst_1       
        //  1201: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1204: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1207: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1210: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1213: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1216: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1219: pop            
        //  1220: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1223: dup            
        //  1224: new             Lnet/minecraft/resources/ResourceLocation;
        //  1227: dup            
        //  1228: ldc             "bastion/treasure/extensions/large_pool"
        //  1230: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1233: new             Lnet/minecraft/resources/ResourceLocation;
        //  1236: dup            
        //  1237: ldc             "empty"
        //  1239: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1242: ldc             "bastion/treasure/extensions/empty"
        //  1244: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1247: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1250: iconst_1       
        //  1251: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1254: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1257: ldc             "bastion/treasure/extensions/empty"
        //  1259: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1262: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1265: iconst_1       
        //  1266: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1269: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1272: ldc             "bastion/treasure/extensions/fire_room"
        //  1274: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1277: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1280: iconst_1       
        //  1281: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1284: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1287: ldc             "bastion/treasure/extensions/large_bridge_0"
        //  1289: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1292: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1295: iconst_1       
        //  1296: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1299: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1302: ldc             "bastion/treasure/extensions/large_bridge_1"
        //  1304: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1307: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1310: iconst_1       
        //  1311: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1314: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1317: ldc             "bastion/treasure/extensions/large_bridge_2"
        //  1319: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1322: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1325: iconst_1       
        //  1326: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1329: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1332: ldc             "bastion/treasure/extensions/large_bridge_3"
        //  1334: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1337: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1340: iconst_1       
        //  1341: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1344: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1347: ldc             "bastion/treasure/extensions/roofed_bridge"
        //  1349: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1352: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1355: iconst_1       
        //  1356: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1359: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1362: ldc             "bastion/treasure/extensions/empty"
        //  1364: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1367: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1370: iconst_1       
        //  1371: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1374: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1377: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1380: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1383: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1386: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1389: pop            
        //  1390: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1393: dup            
        //  1394: new             Lnet/minecraft/resources/ResourceLocation;
        //  1397: dup            
        //  1398: ldc             "bastion/treasure/extensions/small_pool"
        //  1400: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1403: new             Lnet/minecraft/resources/ResourceLocation;
        //  1406: dup            
        //  1407: ldc             "empty"
        //  1409: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1412: ldc             "bastion/treasure/extensions/empty"
        //  1414: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1417: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1420: iconst_1       
        //  1421: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1424: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1427: ldc             "bastion/treasure/extensions/fire_room"
        //  1429: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1432: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1435: iconst_1       
        //  1436: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1439: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1442: ldc             "bastion/treasure/extensions/empty"
        //  1444: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1447: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1450: iconst_1       
        //  1451: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1454: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1457: ldc             "bastion/treasure/extensions/small_bridge_0"
        //  1459: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1462: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1465: iconst_1       
        //  1466: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1469: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1472: ldc             "bastion/treasure/extensions/small_bridge_1"
        //  1474: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1477: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1480: iconst_1       
        //  1481: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1484: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1487: ldc             "bastion/treasure/extensions/small_bridge_2"
        //  1489: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1492: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1495: iconst_1       
        //  1496: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1499: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1502: ldc             "bastion/treasure/extensions/small_bridge_3"
        //  1504: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1507: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1510: iconst_1       
        //  1511: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1514: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1517: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1520: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1523: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1526: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1529: pop            
        //  1530: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1533: dup            
        //  1534: new             Lnet/minecraft/resources/ResourceLocation;
        //  1537: dup            
        //  1538: ldc             "bastion/treasure/extensions/houses"
        //  1540: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1543: new             Lnet/minecraft/resources/ResourceLocation;
        //  1546: dup            
        //  1547: ldc             "empty"
        //  1549: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1552: ldc             "bastion/treasure/extensions/house_0"
        //  1554: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1557: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1560: iconst_1       
        //  1561: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1564: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1567: ldc             "bastion/treasure/extensions/house_1"
        //  1569: getstatic       net/minecraft/data/worldgen/ProcessorLists.TREASURE_ROOMS:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1572: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1575: iconst_1       
        //  1576: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1579: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1582: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1585: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1588: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1591: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1594: pop            
        //  1595: new             Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1598: dup            
        //  1599: new             Lnet/minecraft/resources/ResourceLocation;
        //  1602: dup            
        //  1603: ldc             "bastion/treasure/roofs"
        //  1605: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1608: new             Lnet/minecraft/resources/ResourceLocation;
        //  1611: dup            
        //  1612: ldc             "empty"
        //  1614: invokespecial   net/minecraft/resources/ResourceLocation.<init>:(Ljava/lang/String;)V
        //  1617: ldc             "bastion/treasure/roofs/wall_roof"
        //  1619: getstatic       net/minecraft/data/worldgen/ProcessorLists.ROOF:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1622: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1625: iconst_1       
        //  1626: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1629: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1632: ldc             "bastion/treasure/roofs/corner_roof"
        //  1634: getstatic       net/minecraft/data/worldgen/ProcessorLists.ROOF:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1637: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1640: iconst_1       
        //  1641: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1644: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1647: ldc             "bastion/treasure/roofs/center_roof"
        //  1649: getstatic       net/minecraft/data/worldgen/ProcessorLists.ROOF:Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;
        //  1652: invokestatic    net/minecraft/world/level/levelgen/feature/structures/StructurePoolElement.single:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessorList;)Ljava/util/function/Function;
        //  1655: iconst_1       
        //  1656: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  1659: invokestatic    com/mojang/datafixers/util/Pair.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/mojang/datafixers/util/Pair;
        //  1662: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1665: getstatic       net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection.RIGID:Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;
        //  1668: invokespecial   net/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool.<init>:(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool$Projection;)V
        //  1671: invokestatic    net/minecraft/data/worldgen/Pools.register:(Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;)Lnet/minecraft/world/level/levelgen/feature/structures/StructureTemplatePool;
        //  1674: pop            
        //  1675: return         
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
        //     at com.strobel.assembler.metadata.ParameterizedType.accept(ParameterizedType.java:103)
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
        //     at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.helpCC(ForkJoinPool.java:1116)
        //     at java.base/java.util.concurrent.ForkJoinPool.externalHelpComplete(ForkJoinPool.java:1966)
        //     at java.base/java.util.concurrent.ForkJoinTask.tryExternalHelp(ForkJoinTask.java:378)
        //     at java.base/java.util.concurrent.ForkJoinTask.externalAwaitDone(ForkJoinTask.java:323)
        //     at java.base/java.util.concurrent.ForkJoinTask.doInvoke(ForkJoinTask.java:412)
        //     at java.base/java.util.concurrent.ForkJoinTask.invoke(ForkJoinTask.java:736)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp.evaluateParallel(ForEachOps.java:159)
        //     at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateParallel(ForEachOps.java:173)
        //     at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:233)
        //     at java.base/java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:497)
        //     at cuchaz.enigma.gui.GuiController.lambda$exportSource$6(GuiController.java:216)
        //     at cuchaz.enigma.gui.dialog.ProgressDialog.lambda$runOffThread$0(ProgressDialog.java:78)
        //     at java.base/java.lang.Thread.run(Thread.java:832)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
