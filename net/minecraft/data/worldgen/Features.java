package net.minecraft.data.worldgen;

import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.levelgen.feature.blockplacers.ColumnPlacer;
import net.minecraft.world.level.block.Block;
import java.util.Set;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SpringConfiguration;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.feature.configurations.RangeDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
import net.minecraft.world.level.levelgen.placement.ConfiguredDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.blockplacers.DoublePlantPlacer;
import net.minecraft.world.level.levelgen.feature.blockplacers.BlockPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class Features {
    public static final ConfiguredFeature<?, ?> END_SPIKE;
    public static final ConfiguredFeature<?, ?> END_GATEWAY;
    public static final ConfiguredFeature<?, ?> END_GATEWAY_DELAYED;
    public static final ConfiguredFeature<?, ?> CHORUS_PLANT;
    public static final ConfiguredFeature<?, ?> END_ISLAND;
    public static final ConfiguredFeature<?, ?> END_ISLAND_DECORATED;
    public static final ConfiguredFeature<?, ?> DELTA;
    public static final ConfiguredFeature<?, ?> SMALL_BASALT_COLUMNS;
    public static final ConfiguredFeature<?, ?> LARGE_BASALT_COLUMNS;
    public static final ConfiguredFeature<?, ?> BASALT_BLOBS;
    public static final ConfiguredFeature<?, ?> BLACKSTONE_BLOBS;
    public static final ConfiguredFeature<?, ?> GLOWSTONE_EXTRA;
    public static final ConfiguredFeature<?, ?> GLOWSTONE;
    public static final ConfiguredFeature<?, ?> CRIMSON_FOREST_VEGETATION;
    public static final ConfiguredFeature<?, ?> WARPED_FOREST_VEGETATION;
    public static final ConfiguredFeature<?, ?> NETHER_SPROUTS;
    public static final ConfiguredFeature<?, ?> TWISTING_VINES;
    public static final ConfiguredFeature<?, ?> WEEPING_VINES;
    public static final ConfiguredFeature<?, ?> BASALT_PILLAR;
    public static final ConfiguredFeature<?, ?> SEAGRASS_COLD;
    public static final ConfiguredFeature<?, ?> SEAGRASS_DEEP_COLD;
    public static final ConfiguredFeature<?, ?> SEAGRASS_NORMAL;
    public static final ConfiguredFeature<?, ?> SEAGRASS_RIVER;
    public static final ConfiguredFeature<?, ?> SEAGRASS_DEEP;
    public static final ConfiguredFeature<?, ?> SEAGRASS_SWAMP;
    public static final ConfiguredFeature<?, ?> SEAGRASS_WARM;
    public static final ConfiguredFeature<?, ?> SEAGRASS_DEEP_WARM;
    public static final ConfiguredFeature<?, ?> SEA_PICKLE;
    public static final ConfiguredFeature<?, ?> ICE_SPIKE;
    public static final ConfiguredFeature<?, ?> ICE_PATCH;
    public static final ConfiguredFeature<?, ?> FOREST_ROCK;
    public static final ConfiguredFeature<?, ?> SEAGRASS_SIMPLE;
    public static final ConfiguredFeature<?, ?> ICEBERG_PACKED;
    public static final ConfiguredFeature<?, ?> ICEBERG_BLUE;
    public static final ConfiguredFeature<?, ?> KELP_COLD;
    public static final ConfiguredFeature<?, ?> KELP_WARM;
    public static final ConfiguredFeature<?, ?> BLUE_ICE;
    public static final ConfiguredFeature<?, ?> BAMBOO_LIGHT;
    public static final ConfiguredFeature<?, ?> BAMBOO;
    public static final ConfiguredFeature<?, ?> VINES;
    public static final ConfiguredFeature<?, ?> LAKE_WATER;
    public static final ConfiguredFeature<?, ?> LAKE_LAVA;
    public static final ConfiguredFeature<?, ?> DISK_CLAY;
    public static final ConfiguredFeature<?, ?> DISK_GRAVEL;
    public static final ConfiguredFeature<?, ?> DISK_SAND;
    public static final ConfiguredFeature<?, ?> FREEZE_TOP_LAYER;
    public static final ConfiguredFeature<?, ?> BONUS_CHEST;
    public static final ConfiguredFeature<?, ?> VOID_START_PLATFORM;
    public static final ConfiguredFeature<?, ?> MONSTER_ROOM;
    public static final ConfiguredFeature<?, ?> WELL;
    public static final ConfiguredFeature<?, ?> FOSSIL;
    public static final ConfiguredFeature<?, ?> SPRING_LAVA_DOUBLE;
    public static final ConfiguredFeature<?, ?> SPRING_LAVA;
    public static final ConfiguredFeature<?, ?> SPRING_DELTA;
    public static final ConfiguredFeature<?, ?> SPRING_CLOSED;
    public static final ConfiguredFeature<?, ?> SPRING_CLOSED_DOUBLE;
    public static final ConfiguredFeature<?, ?> SPRING_OPEN;
    public static final ConfiguredFeature<?, ?> SPRING_WATER;
    public static final ConfiguredFeature<?, ?> PILE_HAY;
    public static final ConfiguredFeature<?, ?> PILE_MELON;
    public static final ConfiguredFeature<?, ?> PILE_SNOW;
    public static final ConfiguredFeature<?, ?> PILE_ICE;
    public static final ConfiguredFeature<?, ?> PILE_PUMPKIN;
    public static final ConfiguredFeature<?, ?> PATCH_FIRE;
    public static final ConfiguredFeature<?, ?> PATCH_SOUL_FIRE;
    public static final ConfiguredFeature<?, ?> PATCH_BROWN_MUSHROOM;
    public static final ConfiguredFeature<?, ?> PATCH_RED_MUSHROOM;
    public static final ConfiguredFeature<?, ?> PATCH_CRIMSON_ROOTS;
    public static final ConfiguredFeature<?, ?> PATCH_SUNFLOWER;
    public static final ConfiguredFeature<?, ?> PATCH_PUMPKIN;
    public static final ConfiguredFeature<?, ?> PATCH_TAIGA_GRASS;
    public static final ConfiguredFeature<?, ?> PATCH_BERRY_BUSH;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_PLAIN;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_FOREST;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_BADLANDS;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_SAVANNA;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_NORMAL;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_TAIGA_2;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_TAIGA;
    public static final ConfiguredFeature<?, ?> PATCH_GRASS_JUNGLE;
    public static final ConfiguredFeature<?, ?> PATCH_DEAD_BUSH_2;
    public static final ConfiguredFeature<?, ?> PATCH_DEAD_BUSH;
    public static final ConfiguredFeature<?, ?> PATCH_DEAD_BUSH_BADLANDS;
    public static final ConfiguredFeature<?, ?> PATCH_MELON;
    public static final ConfiguredFeature<?, ?> PATCH_BERRY_SPARSE;
    public static final ConfiguredFeature<?, ?> PATCH_BERRY_DECORATED;
    public static final ConfiguredFeature<?, ?> PATCH_WATERLILLY;
    public static final ConfiguredFeature<?, ?> PATCH_TALL_GRASS_2;
    public static final ConfiguredFeature<?, ?> PATCH_TALL_GRASS;
    public static final ConfiguredFeature<?, ?> PATCH_LARGE_FERN;
    public static final ConfiguredFeature<?, ?> PATCH_CACTUS;
    public static final ConfiguredFeature<?, ?> PATCH_CACTUS_DESERT;
    public static final ConfiguredFeature<?, ?> PATCH_CACTUS_DECORATED;
    public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE_SWAMP;
    public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE_DESERT;
    public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE_BADLANDS;
    public static final ConfiguredFeature<?, ?> PATCH_SUGAR_CANE;
    public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_NETHER;
    public static final ConfiguredFeature<?, ?> RED_MUSHROOM_NETHER;
    public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_NORMAL;
    public static final ConfiguredFeature<?, ?> RED_MUSHROOM_NORMAL;
    public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_TAIGA;
    public static final ConfiguredFeature<?, ?> RED_MUSHROOM_TAIGA;
    public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_GIANT;
    public static final ConfiguredFeature<?, ?> RED_MUSHROOM_GIANT;
    public static final ConfiguredFeature<?, ?> BROWN_MUSHROOM_SWAMP;
    public static final ConfiguredFeature<?, ?> RED_MUSHROOM_SWAMP;
    public static final ConfiguredFeature<?, ?> ORE_MAGMA;
    public static final ConfiguredFeature<?, ?> ORE_SOUL_SAND;
    public static final ConfiguredFeature<?, ?> ORE_GOLD_DELTAS;
    public static final ConfiguredFeature<?, ?> ORE_QUARTZ_DELTAS;
    public static final ConfiguredFeature<?, ?> ORE_GOLD_NETHER;
    public static final ConfiguredFeature<?, ?> ORE_QUARTZ_NETHER;
    public static final ConfiguredFeature<?, ?> ORE_GRAVEL_NETHER;
    public static final ConfiguredFeature<?, ?> ORE_BLACKSTONE;
    public static final ConfiguredFeature<?, ?> ORE_DIRT;
    public static final ConfiguredFeature<?, ?> ORE_GRAVEL;
    public static final ConfiguredFeature<?, ?> ORE_GRANITE;
    public static final ConfiguredFeature<?, ?> ORE_DIORITE;
    public static final ConfiguredFeature<?, ?> ORE_ANDESITE;
    public static final ConfiguredFeature<?, ?> ORE_COAL;
    public static final ConfiguredFeature<?, ?> ORE_IRON;
    public static final ConfiguredFeature<?, ?> ORE_GOLD_EXTRA;
    public static final ConfiguredFeature<?, ?> ORE_GOLD;
    public static final ConfiguredFeature<?, ?> ORE_REDSTONE;
    public static final ConfiguredFeature<?, ?> ORE_DIAMOND;
    public static final ConfiguredFeature<?, ?> ORE_LAPIS;
    public static final ConfiguredFeature<?, ?> ORE_INFESTED;
    public static final ConfiguredFeature<?, ?> ORE_EMERALD;
    public static final ConfiguredFeature<?, ?> ORE_DEBRIS_LARGE;
    public static final ConfiguredFeature<?, ?> ORE_DEBRIS_SMALL;
    public static final ConfiguredFeature<?, ?> CRIMSON_FUNGI;
    public static final ConfiguredFeature<HugeFungusConfiguration, ?> CRIMSON_FUNGI_PLANTED;
    public static final ConfiguredFeature<?, ?> WARPED_FUNGI;
    public static final ConfiguredFeature<HugeFungusConfiguration, ?> WARPED_FUNGI_PLANTED;
    public static final ConfiguredFeature<?, ?> HUGE_BROWN_MUSHROOM;
    public static final ConfiguredFeature<?, ?> HUGE_RED_MUSHROOM;
    public static final ConfiguredFeature<TreeConfiguration, ?> OAK;
    public static final ConfiguredFeature<TreeConfiguration, ?> DARK_OAK;
    public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH;
    public static final ConfiguredFeature<TreeConfiguration, ?> ACACIA;
    public static final ConfiguredFeature<TreeConfiguration, ?> SPRUCE;
    public static final ConfiguredFeature<TreeConfiguration, ?> PINE;
    public static final ConfiguredFeature<TreeConfiguration, ?> JUNGLE_TREE;
    public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK;
    public static final ConfiguredFeature<TreeConfiguration, ?> JUNGLE_TREE_NO_VINE;
    public static final ConfiguredFeature<TreeConfiguration, ?> MEGA_JUNGLE_TREE;
    public static final ConfiguredFeature<TreeConfiguration, ?> MEGA_SPRUCE;
    public static final ConfiguredFeature<TreeConfiguration, ?> MEGA_PINE;
    public static final ConfiguredFeature<TreeConfiguration, ?> SUPER_BIRCH_BEES_0002;
    public static final ConfiguredFeature<?, ?> SWAMP_TREE;
    public static final ConfiguredFeature<?, ?> JUNGLE_BUSH;
    public static final ConfiguredFeature<TreeConfiguration, ?> OAK_BEES_0002;
    public static final ConfiguredFeature<TreeConfiguration, ?> OAK_BEES_002;
    public static final ConfiguredFeature<TreeConfiguration, ?> OAK_BEES_005;
    public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH_BEES_0002;
    public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH_BEES_002;
    public static final ConfiguredFeature<TreeConfiguration, ?> BIRCH_BEES_005;
    public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK_BEES_0002;
    public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK_BEES_002;
    public static final ConfiguredFeature<TreeConfiguration, ?> FANCY_OAK_BEES_005;
    public static final ConfiguredFeature<?, ?> OAK_BADLANDS;
    public static final ConfiguredFeature<?, ?> SPRUCE_SNOWY;
    public static final ConfiguredFeature<?, ?> FLOWER_WARM;
    public static final ConfiguredFeature<?, ?> FLOWER_DEFAULT;
    public static final ConfiguredFeature<?, ?> FLOWER_FOREST;
    public static final ConfiguredFeature<?, ?> FLOWER_SWAMP;
    public static final ConfiguredFeature<?, ?> FLOWER_PLAIN;
    public static final ConfiguredFeature<?, ?> FLOWER_PLAIN_DECORATED;
    private static final ImmutableList<Supplier<ConfiguredFeature<?, ?>>> FOREST_FLOWER_FEATURES;
    public static final ConfiguredFeature<?, ?> FOREST_FLOWER_VEGETATION_COMMON;
    public static final ConfiguredFeature<?, ?> FOREST_FLOWER_VEGETATION;
    public static final ConfiguredFeature<?, ?> DARK_FOREST_VEGETATION_BROWN;
    public static final ConfiguredFeature<?, ?> DARK_FOREST_VEGETATION_RED;
    public static final ConfiguredFeature<?, ?> WARM_OCEAN_VEGETATION;
    public static final ConfiguredFeature<?, ?> FOREST_FLOWER_TREES;
    public static final ConfiguredFeature<?, ?> TAIGA_VEGETATION;
    public static final ConfiguredFeature<?, ?> TREES_SHATTERED_SAVANNA;
    public static final ConfiguredFeature<?, ?> TREES_SAVANNA;
    public static final ConfiguredFeature<?, ?> BIRCH_TALL;
    public static final ConfiguredFeature<?, ?> TREES_BIRCH;
    public static final ConfiguredFeature<?, ?> TREES_MOUNTAIN_EDGE;
    public static final ConfiguredFeature<?, ?> TREES_MOUNTAIN;
    public static final ConfiguredFeature<?, ?> TREES_WATER;
    public static final ConfiguredFeature<?, ?> BIRCH_OTHER;
    public static final ConfiguredFeature<?, ?> PLAIN_VEGETATION;
    public static final ConfiguredFeature<?, ?> TREES_JUNGLE_EDGE;
    public static final ConfiguredFeature<?, ?> TREES_GIANT_SPRUCE;
    public static final ConfiguredFeature<?, ?> TREES_GIANT;
    public static final ConfiguredFeature<?, ?> TREES_JUNGLE;
    public static final ConfiguredFeature<?, ?> BAMBOO_VEGETATION;
    public static final ConfiguredFeature<?, ?> MUSHROOM_FIELD_VEGETATION;
    
    private static <FC extends FeatureConfiguration> ConfiguredFeature<FC, ?> register(final String string, final ConfiguredFeature<FC, ?> cis) {
        return Registry.<ConfiguredFeature<FC, ?>>register(BuiltinRegistries.CONFIGURED_FEATURE, string, cis);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: getstatic       net/minecraft/world/level/levelgen/feature/Feature.END_SPIKE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //     6: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration;
        //     9: dup            
        //    10: iconst_0       
        //    11: invokestatic    com/google/common/collect/ImmutableList.of:()Lcom/google/common/collect/ImmutableList;
        //    14: aconst_null    
        //    15: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SpikeConfiguration.<init>:(ZLjava/util/List;Lnet/minecraft/core/BlockPos;)V
        //    18: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    21: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    24: putstatic       net/minecraft/data/worldgen/Features.END_SPIKE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    27: ldc_w           "end_gateway"
        //    30: getstatic       net/minecraft/world/level/levelgen/feature/Feature.END_GATEWAY:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //    33: getstatic       net/minecraft/server/level/ServerLevel.END_SPAWN_POINT:Lnet/minecraft/core/BlockPos;
        //    36: iconst_1       
        //    37: invokestatic    net/minecraft/world/level/levelgen/feature/configurations/EndGatewayConfiguration.knownExit:(Lnet/minecraft/core/BlockPos;Z)Lnet/minecraft/world/level/levelgen/feature/configurations/EndGatewayConfiguration;
        //    40: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    43: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.END_GATEWAY:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //    46: getstatic       net/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //    49: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //    52: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    55: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    58: putstatic       net/minecraft/data/worldgen/Features.END_GATEWAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    61: ldc_w           "end_gateway_delayed"
        //    64: getstatic       net/minecraft/world/level/levelgen/feature/Feature.END_GATEWAY:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //    67: invokestatic    net/minecraft/world/level/levelgen/feature/configurations/EndGatewayConfiguration.delayedExitSearch:()Lnet/minecraft/world/level/levelgen/feature/configurations/EndGatewayConfiguration;
        //    70: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    73: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    76: putstatic       net/minecraft/data/worldgen/Features.END_GATEWAY_DELAYED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    79: ldc_w           "chorus_plant"
        //    82: getstatic       net/minecraft/world/level/levelgen/feature/Feature.CHORUS_PLANT:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //    85: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //    88: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    91: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //    94: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //    97: iconst_4       
        //    98: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.countRandom:(I)Ljava/lang/Object;
        //   101: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   104: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   107: putstatic       net/minecraft/data/worldgen/Features.CHORUS_PLANT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   110: ldc_w           "end_island"
        //   113: getstatic       net/minecraft/world/level/levelgen/feature/Feature.END_ISLAND:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   116: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //   119: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   122: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   125: putstatic       net/minecraft/data/worldgen/Features.END_ISLAND:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   128: ldc_w           "end_island_decorated"
        //   131: getstatic       net/minecraft/data/worldgen/Features.END_ISLAND:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   134: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.END_ISLAND:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   137: getstatic       net/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //   140: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   143: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   146: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   149: putstatic       net/minecraft/data/worldgen/Features.END_ISLAND_DECORATED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   152: ldc_w           "delta"
        //   155: getstatic       net/minecraft/world/level/levelgen/feature/Feature.DELTA_FEATURE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   158: new             Lnet/minecraft/world/level/levelgen/feature/configurations/DeltaFeatureConfiguration;
        //   161: dup            
        //   162: getstatic       net/minecraft/data/worldgen/Features$States.LAVA:Lnet/minecraft/world/level/block/state/BlockState;
        //   165: getstatic       net/minecraft/data/worldgen/Features$States.MAGMA_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //   168: iconst_3       
        //   169: iconst_4       
        //   170: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   173: iconst_0       
        //   174: iconst_2       
        //   175: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   178: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/DeltaFeatureConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //   181: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   184: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   187: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   190: dup            
        //   191: bipush          40
        //   193: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   196: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   199: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   202: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   205: putstatic       net/minecraft/data/worldgen/Features.DELTA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   208: ldc_w           "small_basalt_columns"
        //   211: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BASALT_COLUMNS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   214: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ColumnFeatureConfiguration;
        //   217: dup            
        //   218: iconst_1       
        //   219: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //   222: iconst_1       
        //   223: iconst_3       
        //   224: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   227: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ColumnFeatureConfiguration.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //   230: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   233: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   236: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   239: dup            
        //   240: iconst_4       
        //   241: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   244: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   247: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   250: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   253: putstatic       net/minecraft/data/worldgen/Features.SMALL_BASALT_COLUMNS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   256: ldc_w           "large_basalt_columns"
        //   259: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BASALT_COLUMNS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   262: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ColumnFeatureConfiguration;
        //   265: dup            
        //   266: iconst_2       
        //   267: iconst_1       
        //   268: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   271: iconst_5       
        //   272: iconst_5       
        //   273: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   276: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ColumnFeatureConfiguration.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //   279: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   282: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   285: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   288: dup            
        //   289: iconst_2       
        //   290: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   293: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   296: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   299: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   302: putstatic       net/minecraft/data/worldgen/Features.LARGE_BASALT_COLUMNS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   305: ldc_w           "basalt_blobs"
        //   308: getstatic       net/minecraft/world/level/levelgen/feature/Feature.REPLACE_BLOBS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   311: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ReplaceSphereConfiguration;
        //   314: dup            
        //   315: getstatic       net/minecraft/data/worldgen/Features$States.NETHERRACK:Lnet/minecraft/world/level/block/state/BlockState;
        //   318: getstatic       net/minecraft/data/worldgen/Features$States.BASALT:Lnet/minecraft/world/level/block/state/BlockState;
        //   321: iconst_3       
        //   322: iconst_4       
        //   323: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   326: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ReplaceSphereConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;)V
        //   329: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   332: sipush          128
        //   335: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //   338: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   341: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //   344: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   347: bipush          75
        //   349: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   352: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   355: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   358: putstatic       net/minecraft/data/worldgen/Features.BASALT_BLOBS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   361: ldc_w           "blackstone_blobs"
        //   364: getstatic       net/minecraft/world/level/levelgen/feature/Feature.REPLACE_BLOBS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   367: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ReplaceSphereConfiguration;
        //   370: dup            
        //   371: getstatic       net/minecraft/data/worldgen/Features$States.NETHERRACK:Lnet/minecraft/world/level/block/state/BlockState;
        //   374: getstatic       net/minecraft/data/worldgen/Features$States.BLACKSTONE:Lnet/minecraft/world/level/block/state/BlockState;
        //   377: iconst_3       
        //   378: iconst_4       
        //   379: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //   382: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ReplaceSphereConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;)V
        //   385: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   388: sipush          128
        //   391: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //   394: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   397: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //   400: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   403: bipush          25
        //   405: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   408: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   411: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   414: putstatic       net/minecraft/data/worldgen/Features.BLACKSTONE_BLOBS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   417: ldc_w           "glowstone_extra"
        //   420: getstatic       net/minecraft/world/level/levelgen/feature/Feature.GLOWSTONE_BLOB:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   423: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //   426: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   429: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.GLOWSTONE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   432: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   435: dup            
        //   436: bipush          10
        //   438: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   441: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   444: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   447: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   450: putstatic       net/minecraft/data/worldgen/Features.GLOWSTONE_EXTRA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   453: ldc_w           "glowstone"
        //   456: getstatic       net/minecraft/world/level/levelgen/feature/Feature.GLOWSTONE_BLOB:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   459: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //   462: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   465: sipush          128
        //   468: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //   471: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   474: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //   477: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   480: bipush          10
        //   482: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   485: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   488: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   491: putstatic       net/minecraft/data/worldgen/Features.GLOWSTONE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   494: ldc_w           "crimson_forest_vegetation"
        //   497: getstatic       net/minecraft/world/level/levelgen/feature/Feature.NETHER_FOREST_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   500: getstatic       net/minecraft/data/worldgen/Features$Configs.CRIMSON_FOREST_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //   503: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   506: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   509: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   512: dup            
        //   513: bipush          6
        //   515: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   518: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   521: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   524: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   527: putstatic       net/minecraft/data/worldgen/Features.CRIMSON_FOREST_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   530: ldc_w           "warped_forest_vegetation"
        //   533: getstatic       net/minecraft/world/level/levelgen/feature/Feature.NETHER_FOREST_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   536: getstatic       net/minecraft/data/worldgen/Features$Configs.WARPED_FOREST_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //   539: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   542: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   545: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   548: dup            
        //   549: iconst_5       
        //   550: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   553: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   556: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   559: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   562: putstatic       net/minecraft/data/worldgen/Features.WARPED_FOREST_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   565: ldc_w           "nether_sprouts"
        //   568: getstatic       net/minecraft/world/level/levelgen/feature/Feature.NETHER_FOREST_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   571: getstatic       net/minecraft/data/worldgen/Features$Configs.NETHER_SPROUTS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //   574: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   577: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //   580: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //   583: dup            
        //   584: iconst_4       
        //   585: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //   588: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   591: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   594: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   597: putstatic       net/minecraft/data/worldgen/Features.NETHER_SPROUTS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   600: ldc_w           "twisting_vines"
        //   603: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TWISTING_VINES:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   606: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //   609: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   612: sipush          128
        //   615: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //   618: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   621: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //   624: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   627: bipush          10
        //   629: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   632: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   635: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   638: putstatic       net/minecraft/data/worldgen/Features.TWISTING_VINES:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   641: ldc_w           "weeping_vines"
        //   644: getstatic       net/minecraft/world/level/levelgen/feature/Feature.WEEPING_VINES:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   647: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //   650: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   653: sipush          128
        //   656: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //   659: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   662: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //   665: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   668: bipush          10
        //   670: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   673: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   676: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   679: putstatic       net/minecraft/data/worldgen/Features.WEEPING_VINES:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   682: ldc_w           "basalt_pillar"
        //   685: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BASALT_PILLAR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //   688: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //   691: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   694: sipush          128
        //   697: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //   700: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   703: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //   706: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   709: bipush          10
        //   711: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   714: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   717: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   720: putstatic       net/minecraft/data/worldgen/Features.BASALT_PILLAR:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   723: ldc_w           "seagrass_cold"
        //   726: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   729: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   732: dup            
        //   733: ldc_w           0.3
        //   736: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   739: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   742: bipush          32
        //   744: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   747: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   750: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   753: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   756: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   759: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_COLD:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   762: ldc_w           "seagrass_deep_cold"
        //   765: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   768: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   771: dup            
        //   772: ldc_w           0.8
        //   775: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   778: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   781: bipush          40
        //   783: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   786: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   789: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   792: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   795: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   798: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_DEEP_COLD:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   801: ldc_w           "seagrass_normal"
        //   804: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   807: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   810: dup            
        //   811: ldc_w           0.3
        //   814: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   817: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   820: bipush          48
        //   822: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   825: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   828: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   831: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   834: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   837: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_NORMAL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   840: ldc_w           "seagrass_river"
        //   843: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   846: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   849: dup            
        //   850: ldc_w           0.4
        //   853: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   856: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   859: bipush          48
        //   861: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   864: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   867: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   870: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   873: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   876: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_RIVER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   879: ldc_w           "seagrass_deep"
        //   882: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   885: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   888: dup            
        //   889: ldc_w           0.8
        //   892: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   895: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   898: bipush          48
        //   900: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   903: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   906: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   909: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   912: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   915: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_DEEP:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   918: ldc_w           "seagrass_swamp"
        //   921: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   924: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   927: dup            
        //   928: ldc_w           0.6
        //   931: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   934: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   937: bipush          64
        //   939: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   942: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   945: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   948: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   951: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   954: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_SWAMP:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   957: ldc_w           "seagrass_warm"
        //   960: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //   963: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //   966: dup            
        //   967: ldc_w           0.3
        //   970: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //   973: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   976: bipush          80
        //   978: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //   981: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   984: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //   987: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   990: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   993: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_WARM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //   996: ldc_w           "seagrass_deep_warm"
        //   999: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEAGRASS:Lnet/minecraft/world/level/levelgen/feature/SeagrassFeature;
        //  1002: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //  1005: dup            
        //  1006: ldc_w           0.8
        //  1009: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //  1012: invokevirtual   net/minecraft/world/level/levelgen/feature/SeagrassFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1015: bipush          80
        //  1017: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1020: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1023: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1026: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1029: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1032: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_DEEP_WARM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1035: ldc_w           "sea_pickle"
        //  1038: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SEA_PICKLE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1041: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //  1044: dup            
        //  1045: bipush          20
        //  1047: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //  1050: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1053: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1056: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1059: bipush          16
        //  1061: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  1064: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1067: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1070: putstatic       net/minecraft/data/worldgen/Features.SEA_PICKLE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1073: ldc_w           "ice_spike"
        //  1076: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ICE_SPIKE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1079: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1082: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1085: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1088: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1091: iconst_3       
        //  1092: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1095: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1098: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1101: putstatic       net/minecraft/data/worldgen/Features.ICE_SPIKE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1104: ldc_w           "ice_patch"
        //  1107: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ICE_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1110: new             Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
        //  1113: dup            
        //  1114: getstatic       net/minecraft/data/worldgen/Features$States.PACKED_ICE:Lnet/minecraft/world/level/block/state/BlockState;
        //  1117: iconst_2       
        //  1118: iconst_1       
        //  1119: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  1122: iconst_1       
        //  1123: getstatic       net/minecraft/data/worldgen/Features$States.DIRT:Lnet/minecraft/world/level/block/state/BlockState;
        //  1126: getstatic       net/minecraft/data/worldgen/Features$States.GRASS_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  1129: getstatic       net/minecraft/data/worldgen/Features$States.PODZOL:Lnet/minecraft/world/level/block/state/BlockState;
        //  1132: getstatic       net/minecraft/data/worldgen/Features$States.COARSE_DIRT:Lnet/minecraft/world/level/block/state/BlockState;
        //  1135: getstatic       net/minecraft/data/worldgen/Features$States.MYCELIUM:Lnet/minecraft/world/level/block/state/BlockState;
        //  1138: getstatic       net/minecraft/data/worldgen/Features$States.SNOW_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  1141: getstatic       net/minecraft/data/worldgen/Features$States.ICE:Lnet/minecraft/world/level/block/state/BlockState;
        //  1144: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1147: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;ILjava/util/List;)V
        //  1150: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1153: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1156: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1159: iconst_2       
        //  1160: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1163: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1166: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1169: putstatic       net/minecraft/data/worldgen/Features.ICE_PATCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1172: ldc_w           "forest_rock"
        //  1175: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FOREST_ROCK:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1178: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;
        //  1181: dup            
        //  1182: getstatic       net/minecraft/data/worldgen/Features$States.MOSSY_COBBLESTONE:Lnet/minecraft/world/level/block/state/BlockState;
        //  1185: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  1188: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1191: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1194: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1197: iconst_2       
        //  1198: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.countRandom:(I)Ljava/lang/Object;
        //  1201: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1204: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1207: putstatic       net/minecraft/data/worldgen/Features.FOREST_ROCK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1210: ldc_w           "seagrass_simple"
        //  1213: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SIMPLE_BLOCK:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1216: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration;
        //  1219: dup            
        //  1220: getstatic       net/minecraft/data/worldgen/Features$States.SEAGRASS:Lnet/minecraft/world/level/block/state/BlockState;
        //  1223: getstatic       net/minecraft/data/worldgen/Features$States.STONE:Lnet/minecraft/world/level/block/state/BlockState;
        //  1226: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1229: getstatic       net/minecraft/data/worldgen/Features$States.WATER:Lnet/minecraft/world/level/block/state/BlockState;
        //  1232: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1235: getstatic       net/minecraft/data/worldgen/Features$States.WATER:Lnet/minecraft/world/level/block/state/BlockState;
        //  1238: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1241: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SimpleBlockConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V
        //  1244: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1247: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.CARVING_MASK:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1250: new             Lnet/minecraft/world/level/levelgen/placement/CarvingMaskDecoratorConfiguration;
        //  1253: dup            
        //  1254: getstatic       net/minecraft/world/level/levelgen/GenerationStep$Carving.LIQUID:Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;
        //  1257: ldc_w           0.1
        //  1260: invokespecial   net/minecraft/world/level/levelgen/placement/CarvingMaskDecoratorConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/GenerationStep$Carving;F)V
        //  1263: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1266: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1269: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1272: putstatic       net/minecraft/data/worldgen/Features.SEAGRASS_SIMPLE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1275: ldc_w           "iceberg_packed"
        //  1278: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ICEBERG:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1281: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;
        //  1284: dup            
        //  1285: getstatic       net/minecraft/data/worldgen/Features$States.PACKED_ICE:Lnet/minecraft/world/level/block/state/BlockState;
        //  1288: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  1291: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1294: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.ICEBERG:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1297: getstatic       net/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //  1300: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1303: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1306: bipush          16
        //  1308: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  1311: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1314: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1317: putstatic       net/minecraft/data/worldgen/Features.ICEBERG_PACKED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1320: ldc_w           "iceberg_blue"
        //  1323: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ICEBERG:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1326: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;
        //  1329: dup            
        //  1330: getstatic       net/minecraft/data/worldgen/Features$States.BLUE_ICE:Lnet/minecraft/world/level/block/state/BlockState;
        //  1333: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  1336: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1339: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.ICEBERG:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1342: getstatic       net/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //  1345: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1348: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1351: sipush          200
        //  1354: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  1357: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1360: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1363: putstatic       net/minecraft/data/worldgen/Features.ICEBERG_BLUE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1366: ldc_w           "kelp_cold"
        //  1369: getstatic       net/minecraft/world/level/levelgen/feature/Feature.KELP:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1372: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1375: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1378: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1381: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1384: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  1387: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1390: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1393: new             Lnet/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration;
        //  1396: dup            
        //  1397: bipush          120
        //  1399: ldc2_w          80.0
        //  1402: dconst_0       
        //  1403: invokespecial   net/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration.<init>:(IDD)V
        //  1406: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1409: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1412: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1415: putstatic       net/minecraft/data/worldgen/Features.KELP_COLD:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1418: ldc_w           "kelp_warm"
        //  1421: getstatic       net/minecraft/world/level/levelgen/feature/Feature.KELP:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1424: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1427: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1430: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1433: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1436: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  1439: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1442: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1445: new             Lnet/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration;
        //  1448: dup            
        //  1449: bipush          80
        //  1451: ldc2_w          80.0
        //  1454: dconst_0       
        //  1455: invokespecial   net/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration.<init>:(IDD)V
        //  1458: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1461: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1464: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1467: putstatic       net/minecraft/data/worldgen/Features.KELP_WARM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1470: ldc_w           "blue_ice"
        //  1473: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BLUE_ICE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1476: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1479: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1482: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1485: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  1488: dup            
        //  1489: bipush          30
        //  1491: bipush          32
        //  1493: bipush          64
        //  1495: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  1498: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1501: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1504: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  1507: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1510: bipush          19
        //  1512: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.countRandom:(I)Ljava/lang/Object;
        //  1515: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1518: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1521: putstatic       net/minecraft/data/worldgen/Features.BLUE_ICE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1524: ldc_w           "bamboo_light"
        //  1527: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BAMBOO:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1530: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //  1533: dup            
        //  1534: fconst_0       
        //  1535: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //  1538: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1541: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1544: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1547: bipush          16
        //  1549: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1552: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1555: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1558: putstatic       net/minecraft/data/worldgen/Features.BAMBOO_LIGHT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1561: ldc_w           "bamboo"
        //  1564: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BAMBOO:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1567: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration;
        //  1570: dup            
        //  1571: ldc_w           0.2
        //  1574: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ProbabilityFeatureConfiguration.<init>:(F)V
        //  1577: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1580: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_WORLD_SURFACE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1583: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1586: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  1589: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1592: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1595: new             Lnet/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration;
        //  1598: dup            
        //  1599: sipush          160
        //  1602: ldc2_w          80.0
        //  1605: ldc2_w          0.3
        //  1608: invokespecial   net/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration.<init>:(IDD)V
        //  1611: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1614: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1617: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1620: putstatic       net/minecraft/data/worldgen/Features.BAMBOO:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1623: ldc_w           "vines"
        //  1626: getstatic       net/minecraft/world/level/levelgen/feature/Feature.VINES:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1629: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1632: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1635: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  1638: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1641: bipush          50
        //  1643: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1646: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1649: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1652: putstatic       net/minecraft/data/worldgen/Features.VINES:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1655: ldc_w           "lake_water"
        //  1658: getstatic       net/minecraft/world/level/levelgen/feature/Feature.LAKE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1661: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;
        //  1664: dup            
        //  1665: getstatic       net/minecraft/data/worldgen/Features$States.WATER:Lnet/minecraft/world/level/block/state/BlockState;
        //  1668: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  1671: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1674: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.WATER_LAKE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1677: new             Lnet/minecraft/world/level/levelgen/placement/ChanceDecoratorConfiguration;
        //  1680: dup            
        //  1681: iconst_4       
        //  1682: invokespecial   net/minecraft/world/level/levelgen/placement/ChanceDecoratorConfiguration.<init>:(I)V
        //  1685: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1688: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1691: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1694: putstatic       net/minecraft/data/worldgen/Features.LAKE_WATER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1697: ldc_w           "lake_lava"
        //  1700: getstatic       net/minecraft/world/level/levelgen/feature/Feature.LAKE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1703: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration;
        //  1706: dup            
        //  1707: getstatic       net/minecraft/data/worldgen/Features$States.LAVA:Lnet/minecraft/world/level/block/state/BlockState;
        //  1710: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  1713: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1716: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.LAVA_LAKE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  1719: new             Lnet/minecraft/world/level/levelgen/placement/ChanceDecoratorConfiguration;
        //  1722: dup            
        //  1723: bipush          80
        //  1725: invokespecial   net/minecraft/world/level/levelgen/placement/ChanceDecoratorConfiguration.<init>:(I)V
        //  1728: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1731: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1734: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1737: putstatic       net/minecraft/data/worldgen/Features.LAKE_LAVA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1740: ldc_w           "disk_clay"
        //  1743: getstatic       net/minecraft/world/level/levelgen/feature/Feature.DISK:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1746: new             Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
        //  1749: dup            
        //  1750: getstatic       net/minecraft/data/worldgen/Features$States.CLAY:Lnet/minecraft/world/level/block/state/BlockState;
        //  1753: iconst_2       
        //  1754: iconst_1       
        //  1755: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  1758: iconst_1       
        //  1759: getstatic       net/minecraft/data/worldgen/Features$States.DIRT:Lnet/minecraft/world/level/block/state/BlockState;
        //  1762: getstatic       net/minecraft/data/worldgen/Features$States.CLAY:Lnet/minecraft/world/level/block/state/BlockState;
        //  1765: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1768: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;ILjava/util/List;)V
        //  1771: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1774: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1777: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1780: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1783: putstatic       net/minecraft/data/worldgen/Features.DISK_CLAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1786: ldc_w           "disk_gravel"
        //  1789: getstatic       net/minecraft/world/level/levelgen/feature/Feature.DISK:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1792: new             Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
        //  1795: dup            
        //  1796: getstatic       net/minecraft/data/worldgen/Features$States.GRAVEL:Lnet/minecraft/world/level/block/state/BlockState;
        //  1799: iconst_2       
        //  1800: iconst_3       
        //  1801: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  1804: iconst_2       
        //  1805: getstatic       net/minecraft/data/worldgen/Features$States.DIRT:Lnet/minecraft/world/level/block/state/BlockState;
        //  1808: getstatic       net/minecraft/data/worldgen/Features$States.GRASS_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  1811: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1814: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;ILjava/util/List;)V
        //  1817: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1820: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1823: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1826: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1829: putstatic       net/minecraft/data/worldgen/Features.DISK_GRAVEL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1832: ldc_w           "disk_sand"
        //  1835: getstatic       net/minecraft/world/level/levelgen/feature/Feature.DISK:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1838: new             Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
        //  1841: dup            
        //  1842: getstatic       net/minecraft/data/worldgen/Features$States.SAND:Lnet/minecraft/world/level/block/state/BlockState;
        //  1845: iconst_2       
        //  1846: iconst_4       
        //  1847: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  1850: iconst_2       
        //  1851: getstatic       net/minecraft/data/worldgen/Features$States.DIRT:Lnet/minecraft/world/level/block/state/BlockState;
        //  1854: getstatic       net/minecraft/data/worldgen/Features$States.GRASS_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  1857: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  1860: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/UniformInt;ILjava/util/List;)V
        //  1863: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1866: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1869: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1872: iconst_3       
        //  1873: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1876: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1879: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1882: putstatic       net/minecraft/data/worldgen/Features.DISK_SAND:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1885: ldc_w           "freeze_top_layer"
        //  1888: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FREEZE_TOP_LAYER:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1891: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1894: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1897: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1900: putstatic       net/minecraft/data/worldgen/Features.FREEZE_TOP_LAYER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1903: ldc_w           "bonus_chest"
        //  1906: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BONUS_CHEST:Lnet/minecraft/world/level/levelgen/feature/BonusChestFeature;
        //  1909: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1912: invokevirtual   net/minecraft/world/level/levelgen/feature/BonusChestFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1915: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1918: putstatic       net/minecraft/data/worldgen/Features.BONUS_CHEST:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1921: ldc_w           "void_start_platform"
        //  1924: getstatic       net/minecraft/world/level/levelgen/feature/Feature.VOID_START_PLATFORM:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1927: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1930: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1933: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1936: putstatic       net/minecraft/data/worldgen/Features.VOID_START_PLATFORM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1939: ldc_w           "monster_room"
        //  1942: getstatic       net/minecraft/world/level/levelgen/feature/Feature.MONSTER_ROOM:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1945: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1948: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1951: sipush          256
        //  1954: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  1957: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1960: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  1963: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1966: bipush          8
        //  1968: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  1971: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1974: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1977: putstatic       net/minecraft/data/worldgen/Features.MONSTER_ROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1980: ldc_w           "desert_well"
        //  1983: getstatic       net/minecraft/world/level/levelgen/feature/Feature.DESERT_WELL:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  1986: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  1989: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1992: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  1995: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  1998: sipush          1000
        //  2001: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  2004: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2007: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2010: putstatic       net/minecraft/data/worldgen/Features.WELL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2013: ldc_w           "fossil"
        //  2016: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FOSSIL:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2019: getstatic       net/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneFeatureConfiguration;
        //  2022: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2025: bipush          64
        //  2027: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  2030: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2033: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2036: putstatic       net/minecraft/data/worldgen/Features.FOSSIL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2039: ldc_w           "spring_lava_double"
        //  2042: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2045: getstatic       net/minecraft/data/worldgen/Features$Configs.LAVA_SPRING_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2048: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2051: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE_VERY_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  2054: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  2057: dup            
        //  2058: bipush          8
        //  2060: bipush          16
        //  2062: sipush          256
        //  2065: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  2068: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2071: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2074: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2077: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2080: bipush          40
        //  2082: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2085: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2088: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2091: putstatic       net/minecraft/data/worldgen/Features.SPRING_LAVA_DOUBLE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2094: ldc_w           "spring_lava"
        //  2097: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2100: getstatic       net/minecraft/data/worldgen/Features$Configs.LAVA_SPRING_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2103: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2106: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE_VERY_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  2109: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  2112: dup            
        //  2113: bipush          8
        //  2115: bipush          16
        //  2117: sipush          256
        //  2120: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  2123: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2126: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2129: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2132: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2135: bipush          20
        //  2137: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2140: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2143: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2146: putstatic       net/minecraft/data/worldgen/Features.SPRING_LAVA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2149: ldc_w           "spring_delta"
        //  2152: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2155: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2158: dup            
        //  2159: getstatic       net/minecraft/data/worldgen/Features$States.LAVA_STATE:Lnet/minecraft/world/level/material/FluidState;
        //  2162: iconst_1       
        //  2163: iconst_4       
        //  2164: iconst_1       
        //  2165: getstatic       net/minecraft/world/level/block/Blocks.NETHERRACK:Lnet/minecraft/world/level/block/Block;
        //  2168: getstatic       net/minecraft/world/level/block/Blocks.SOUL_SAND:Lnet/minecraft/world/level/block/Block;
        //  2171: getstatic       net/minecraft/world/level/block/Blocks.GRAVEL:Lnet/minecraft/world/level/block/Block;
        //  2174: getstatic       net/minecraft/world/level/block/Blocks.MAGMA_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2177: getstatic       net/minecraft/world/level/block/Blocks.BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        //  2180: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  2183: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration.<init>:(Lnet/minecraft/world/level/material/FluidState;ZIILjava/util/Set;)V
        //  2186: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2189: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_4_8_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2192: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2195: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2198: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2201: bipush          16
        //  2203: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2206: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2209: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2212: putstatic       net/minecraft/data/worldgen/Features.SPRING_DELTA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2215: ldc_w           "spring_closed"
        //  2218: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2221: getstatic       net/minecraft/data/worldgen/Features$Configs.CLOSED_NETHER_SPRING_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2224: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2227: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_10_20_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2230: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2233: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2236: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2239: bipush          16
        //  2241: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2244: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2247: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2250: putstatic       net/minecraft/data/worldgen/Features.SPRING_CLOSED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2253: ldc_w           "spring_closed_double"
        //  2256: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2259: getstatic       net/minecraft/data/worldgen/Features$Configs.CLOSED_NETHER_SPRING_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2262: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2265: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_10_20_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2268: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2271: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2274: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2277: bipush          32
        //  2279: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2282: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2285: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2288: putstatic       net/minecraft/data/worldgen/Features.SPRING_CLOSED_DOUBLE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2291: ldc_w           "spring_open"
        //  2294: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2297: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2300: dup            
        //  2301: getstatic       net/minecraft/data/worldgen/Features$States.LAVA_STATE:Lnet/minecraft/world/level/material/FluidState;
        //  2304: iconst_0       
        //  2305: iconst_4       
        //  2306: iconst_1       
        //  2307: getstatic       net/minecraft/world/level/block/Blocks.NETHERRACK:Lnet/minecraft/world/level/block/Block;
        //  2310: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  2313: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration.<init>:(Lnet/minecraft/world/level/material/FluidState;ZIILjava/util/Set;)V
        //  2316: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2319: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_4_8_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2322: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2325: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2328: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2331: bipush          8
        //  2333: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2336: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2339: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2342: putstatic       net/minecraft/data/worldgen/Features.SPRING_OPEN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2345: ldc_w           "spring_water"
        //  2348: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SPRING:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2351: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration;
        //  2354: dup            
        //  2355: getstatic       net/minecraft/data/worldgen/Features$States.WATER_STATE:Lnet/minecraft/world/level/material/FluidState;
        //  2358: iconst_1       
        //  2359: iconst_4       
        //  2360: iconst_1       
        //  2361: getstatic       net/minecraft/world/level/block/Blocks.STONE:Lnet/minecraft/world/level/block/Block;
        //  2364: getstatic       net/minecraft/world/level/block/Blocks.GRANITE:Lnet/minecraft/world/level/block/Block;
        //  2367: getstatic       net/minecraft/world/level/block/Blocks.DIORITE:Lnet/minecraft/world/level/block/Block;
        //  2370: getstatic       net/minecraft/world/level/block/Blocks.ANDESITE:Lnet/minecraft/world/level/block/Block;
        //  2373: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  2376: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration.<init>:(Lnet/minecraft/world/level/material/FluidState;ZIILjava/util/Set;)V
        //  2379: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2382: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  2385: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  2388: dup            
        //  2389: bipush          8
        //  2391: bipush          8
        //  2393: sipush          256
        //  2396: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  2399: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2402: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2405: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  2408: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2411: bipush          50
        //  2413: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2416: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2419: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2422: putstatic       net/minecraft/data/worldgen/Features.SPRING_WATER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2425: ldc_w           "pile_hay"
        //  2428: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BLOCK_PILE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2431: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //  2434: dup            
        //  2435: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/RotatedBlockProvider;
        //  2438: dup            
        //  2439: getstatic       net/minecraft/world/level/block/Blocks.HAY_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2442: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/RotatedBlockProvider.<init>:(Lnet/minecraft/world/level/block/Block;)V
        //  2445: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  2448: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2451: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2454: putstatic       net/minecraft/data/worldgen/Features.PILE_HAY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2457: ldc_w           "pile_melon"
        //  2460: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BLOCK_PILE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2463: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //  2466: dup            
        //  2467: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2470: dup            
        //  2471: getstatic       net/minecraft/data/worldgen/Features$States.MELON:Lnet/minecraft/world/level/block/state/BlockState;
        //  2474: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2477: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  2480: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2483: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2486: putstatic       net/minecraft/data/worldgen/Features.PILE_MELON:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2489: ldc_w           "pile_snow"
        //  2492: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BLOCK_PILE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2495: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //  2498: dup            
        //  2499: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2502: dup            
        //  2503: getstatic       net/minecraft/data/worldgen/Features$States.SNOW:Lnet/minecraft/world/level/block/state/BlockState;
        //  2506: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2509: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  2512: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2515: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2518: putstatic       net/minecraft/data/worldgen/Features.PILE_SNOW:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2521: ldc_w           "pile_ice"
        //  2524: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BLOCK_PILE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2527: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //  2530: dup            
        //  2531: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider;
        //  2534: dup            
        //  2535: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.<init>:()V
        //  2538: getstatic       net/minecraft/data/worldgen/Features$States.BLUE_ICE:Lnet/minecraft/world/level/block/state/BlockState;
        //  2541: iconst_1       
        //  2542: invokevirtual   net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.add:(Lnet/minecraft/world/level/block/state/BlockState;I)Lnet/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider;
        //  2545: getstatic       net/minecraft/data/worldgen/Features$States.PACKED_ICE:Lnet/minecraft/world/level/block/state/BlockState;
        //  2548: iconst_5       
        //  2549: invokevirtual   net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.add:(Lnet/minecraft/world/level/block/state/BlockState;I)Lnet/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider;
        //  2552: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  2555: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2558: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2561: putstatic       net/minecraft/data/worldgen/Features.PILE_ICE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2564: ldc_w           "pile_pumpkin"
        //  2567: getstatic       net/minecraft/world/level/levelgen/feature/Feature.BLOCK_PILE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2570: new             Lnet/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration;
        //  2573: dup            
        //  2574: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider;
        //  2577: dup            
        //  2578: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.<init>:()V
        //  2581: getstatic       net/minecraft/data/worldgen/Features$States.PUMPKIN:Lnet/minecraft/world/level/block/state/BlockState;
        //  2584: bipush          19
        //  2586: invokevirtual   net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.add:(Lnet/minecraft/world/level/block/state/BlockState;I)Lnet/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider;
        //  2589: getstatic       net/minecraft/data/worldgen/Features$States.JACK_O_LANTERN:Lnet/minecraft/world/level/block/state/BlockState;
        //  2592: iconst_1       
        //  2593: invokevirtual   net/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider.add:(Lnet/minecraft/world/level/block/state/BlockState;I)Lnet/minecraft/world/level/levelgen/feature/stateproviders/WeightedStateProvider;
        //  2596: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  2599: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2602: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2605: putstatic       net/minecraft/data/worldgen/Features.PILE_PUMPKIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2608: ldc_w           "patch_fire"
        //  2611: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2614: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2617: dup            
        //  2618: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2621: dup            
        //  2622: getstatic       net/minecraft/data/worldgen/Features$States.FIRE:Lnet/minecraft/world/level/block/state/BlockState;
        //  2625: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2628: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  2631: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2634: bipush          64
        //  2636: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2639: getstatic       net/minecraft/data/worldgen/Features$States.NETHERRACK:Lnet/minecraft/world/level/block/state/BlockState;
        //  2642: invokevirtual   net/minecraft/world/level/block/state/BlockState.getBlock:()Lnet/minecraft/world/level/block/Block;
        //  2645: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  2648: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.whitelist:(Ljava/util/Set;)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2651: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2654: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  2657: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2660: getstatic       net/minecraft/data/worldgen/Features$Decorators.FIRE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2663: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2666: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2669: putstatic       net/minecraft/data/worldgen/Features.PATCH_FIRE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2672: ldc_w           "patch_soul_fire"
        //  2675: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2678: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2681: dup            
        //  2682: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2685: dup            
        //  2686: getstatic       net/minecraft/data/worldgen/Features$States.SOUL_FIRE:Lnet/minecraft/world/level/block/state/BlockState;
        //  2689: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2692: new             Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  2695: dup            
        //  2696: invokespecial   net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.<init>:()V
        //  2699: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2702: bipush          64
        //  2704: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2707: getstatic       net/minecraft/data/worldgen/Features$States.SOUL_SOIL:Lnet/minecraft/world/level/block/state/BlockState;
        //  2710: invokevirtual   net/minecraft/world/level/block/state/BlockState.getBlock:()Lnet/minecraft/world/level/block/Block;
        //  2713: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  2716: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.whitelist:(Ljava/util/Set;)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2719: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2722: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  2725: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2728: getstatic       net/minecraft/data/worldgen/Features$Decorators.FIRE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2731: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2734: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2737: putstatic       net/minecraft/data/worldgen/Features.PATCH_SOUL_FIRE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2740: ldc_w           "patch_brown_mushroom"
        //  2743: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2746: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2749: dup            
        //  2750: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2753: dup            
        //  2754: getstatic       net/minecraft/data/worldgen/Features$States.BROWN_MUSHROOM:Lnet/minecraft/world/level/block/state/BlockState;
        //  2757: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2760: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  2763: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2766: bipush          64
        //  2768: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2771: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2774: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  2777: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2780: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2783: putstatic       net/minecraft/data/worldgen/Features.PATCH_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2786: ldc_w           "patch_red_mushroom"
        //  2789: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2792: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2795: dup            
        //  2796: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2799: dup            
        //  2800: getstatic       net/minecraft/data/worldgen/Features$States.RED_MUSHROOM:Lnet/minecraft/world/level/block/state/BlockState;
        //  2803: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2806: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  2809: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2812: bipush          64
        //  2814: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2817: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2820: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  2823: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2826: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2829: putstatic       net/minecraft/data/worldgen/Features.PATCH_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2832: ldc_w           "patch_crimson_roots"
        //  2835: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2838: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2841: dup            
        //  2842: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2845: dup            
        //  2846: getstatic       net/minecraft/data/worldgen/Features$States.CRIMSON_ROOTS:Lnet/minecraft/world/level/block/state/BlockState;
        //  2849: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2852: new             Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  2855: dup            
        //  2856: invokespecial   net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.<init>:()V
        //  2859: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2862: bipush          64
        //  2864: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2867: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2870: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  2873: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2876: sipush          128
        //  2879: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  2882: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2885: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2888: putstatic       net/minecraft/data/worldgen/Features.PATCH_CRIMSON_ROOTS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2891: ldc_w           "patch_sunflower"
        //  2894: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2897: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2900: dup            
        //  2901: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2904: dup            
        //  2905: getstatic       net/minecraft/data/worldgen/Features$States.SUNFLOWER:Lnet/minecraft/world/level/block/state/BlockState;
        //  2908: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2911: new             Lnet/minecraft/world/level/levelgen/feature/blockplacers/DoublePlantPlacer;
        //  2914: dup            
        //  2915: invokespecial   net/minecraft/world/level/levelgen/feature/blockplacers/DoublePlantPlacer.<init>:()V
        //  2918: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2921: bipush          64
        //  2923: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2926: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2929: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  2932: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2935: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2938: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2941: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  2944: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2947: bipush          10
        //  2949: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  2952: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2955: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2958: putstatic       net/minecraft/data/worldgen/Features.PATCH_SUNFLOWER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  2961: ldc_w           "patch_pumpkin"
        //  2964: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  2967: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2970: dup            
        //  2971: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  2974: dup            
        //  2975: getstatic       net/minecraft/data/worldgen/Features$States.PUMPKIN:Lnet/minecraft/world/level/block/state/BlockState;
        //  2978: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  2981: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  2984: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  2987: bipush          64
        //  2989: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  2992: getstatic       net/minecraft/data/worldgen/Features$States.GRASS_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  2995: invokevirtual   net/minecraft/world/level/block/state/BlockState.getBlock:()Lnet/minecraft/world/level/block/Block;
        //  2998: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  3001: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.whitelist:(Ljava/util/Set;)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3004: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3007: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3010: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3013: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3016: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3019: bipush          32
        //  3021: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  3024: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3027: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3030: putstatic       net/minecraft/data/worldgen/Features.PATCH_PUMPKIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3033: ldc_w           "patch_taiga_grass"
        //  3036: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3039: getstatic       net/minecraft/data/worldgen/Features$Configs.TAIGA_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3042: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3045: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3048: putstatic       net/minecraft/data/worldgen/Features.PATCH_TAIGA_GRASS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3051: ldc_w           "patch_berry_bush"
        //  3054: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3057: getstatic       net/minecraft/data/worldgen/Features$Configs.SWEET_BERRY_BUSH_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3060: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3063: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3066: putstatic       net/minecraft/data/worldgen/Features.PATCH_BERRY_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3069: ldc_w           "patch_grass_plain"
        //  3072: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3075: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3078: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3081: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3084: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3087: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  3090: new             Lnet/minecraft/world/level/levelgen/feature/configurations/NoiseDependantDecoratorConfiguration;
        //  3093: dup            
        //  3094: ldc2_w          -0.8
        //  3097: iconst_5       
        //  3098: bipush          10
        //  3100: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/NoiseDependantDecoratorConfiguration.<init>:(DII)V
        //  3103: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3106: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3109: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3112: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_PLAIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3115: ldc_w           "patch_grass_forest"
        //  3118: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3121: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3124: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3127: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3130: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3133: iconst_2       
        //  3134: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3137: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3140: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3143: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_FOREST:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3146: ldc_w           "patch_grass_badlands"
        //  3149: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3152: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3155: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3158: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3161: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3164: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3167: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_BADLANDS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3170: ldc_w           "patch_grass_savanna"
        //  3173: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3176: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3179: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3182: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3185: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3188: bipush          20
        //  3190: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3193: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3196: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3199: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_SAVANNA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3202: ldc_w           "patch_grass_normal"
        //  3205: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3208: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3211: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3214: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3217: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3220: iconst_5       
        //  3221: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3224: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3227: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3230: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_NORMAL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3233: ldc_w           "patch_grass_taiga_2"
        //  3236: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3239: getstatic       net/minecraft/data/worldgen/Features$Configs.TAIGA_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3242: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3245: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3248: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3251: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3254: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_TAIGA_2:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3257: ldc_w           "patch_grass_taiga"
        //  3260: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3263: getstatic       net/minecraft/data/worldgen/Features$Configs.TAIGA_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3266: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3269: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3272: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3275: bipush          7
        //  3277: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3280: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3283: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3286: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3289: ldc_w           "patch_grass_jungle"
        //  3292: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3295: getstatic       net/minecraft/data/worldgen/Features$Configs.JUNGLE_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3298: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3301: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3304: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3307: bipush          25
        //  3309: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3312: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3315: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3318: putstatic       net/minecraft/data/worldgen/Features.PATCH_GRASS_JUNGLE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3321: ldc_w           "patch_dead_bush_2"
        //  3324: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3327: getstatic       net/minecraft/data/worldgen/Features$Configs.DEAD_BUSH_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3330: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3333: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3336: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3339: iconst_2       
        //  3340: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3343: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3346: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3349: putstatic       net/minecraft/data/worldgen/Features.PATCH_DEAD_BUSH_2:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3352: ldc_w           "patch_dead_bush"
        //  3355: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3358: getstatic       net/minecraft/data/worldgen/Features$Configs.DEAD_BUSH_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3361: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3364: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3367: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3370: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3373: putstatic       net/minecraft/data/worldgen/Features.PATCH_DEAD_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3376: ldc_w           "patch_dead_bush_badlands"
        //  3379: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3382: getstatic       net/minecraft/data/worldgen/Features$Configs.DEAD_BUSH_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3385: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3388: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3391: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3394: bipush          20
        //  3396: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3399: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3402: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3405: putstatic       net/minecraft/data/worldgen/Features.PATCH_DEAD_BUSH_BADLANDS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3408: ldc_w           "patch_melon"
        //  3411: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3414: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3417: dup            
        //  3418: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  3421: dup            
        //  3422: getstatic       net/minecraft/data/worldgen/Features$States.MELON:Lnet/minecraft/world/level/block/state/BlockState;
        //  3425: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  3428: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  3431: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  3434: bipush          64
        //  3436: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3439: getstatic       net/minecraft/data/worldgen/Features$States.GRASS_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  3442: invokevirtual   net/minecraft/world/level/block/state/BlockState.getBlock:()Lnet/minecraft/world/level/block/Block;
        //  3445: invokestatic    com/google/common/collect/ImmutableSet.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;
        //  3448: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.whitelist:(Ljava/util/Set;)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3451: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.canReplace:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3454: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3457: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3460: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3463: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3466: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3469: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3472: putstatic       net/minecraft/data/worldgen/Features.PATCH_MELON:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3475: ldc_w           "patch_berry_sparse"
        //  3478: getstatic       net/minecraft/data/worldgen/Features.PATCH_BERRY_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3481: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3484: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3487: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3490: putstatic       net/minecraft/data/worldgen/Features.PATCH_BERRY_SPARSE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3493: ldc_w           "patch_berry_decorated"
        //  3496: getstatic       net/minecraft/data/worldgen/Features.PATCH_BERRY_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3499: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3502: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3505: bipush          12
        //  3507: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  3510: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3513: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3516: putstatic       net/minecraft/data/worldgen/Features.PATCH_BERRY_DECORATED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3519: ldc_w           "patch_waterlilly"
        //  3522: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3525: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3528: dup            
        //  3529: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  3532: dup            
        //  3533: getstatic       net/minecraft/data/worldgen/Features$States.LILY_PAD:Lnet/minecraft/world/level/block/state/BlockState;
        //  3536: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  3539: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  3542: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  3545: bipush          10
        //  3547: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3550: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3553: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3556: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3559: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3562: iconst_4       
        //  3563: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3566: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3569: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3572: putstatic       net/minecraft/data/worldgen/Features.PATCH_WATERLILLY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3575: ldc_w           "patch_tall_grass_2"
        //  3578: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3581: getstatic       net/minecraft/data/worldgen/Features$Configs.TALL_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3584: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3587: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3590: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3593: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3596: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3599: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  3602: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3605: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  3608: new             Lnet/minecraft/world/level/levelgen/feature/configurations/NoiseDependantDecoratorConfiguration;
        //  3611: dup            
        //  3612: ldc2_w          -0.8
        //  3615: iconst_0       
        //  3616: bipush          7
        //  3618: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/NoiseDependantDecoratorConfiguration.<init>:(DII)V
        //  3621: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3624: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3627: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3630: putstatic       net/minecraft/data/worldgen/Features.PATCH_TALL_GRASS_2:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3633: ldc_w           "patch_tall_grass"
        //  3636: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3639: getstatic       net/minecraft/data/worldgen/Features$Configs.TALL_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3642: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3645: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3648: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3651: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3654: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3657: bipush          7
        //  3659: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3662: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3665: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3668: putstatic       net/minecraft/data/worldgen/Features.PATCH_TALL_GRASS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3671: ldc_w           "patch_large_fern"
        //  3674: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3677: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3680: dup            
        //  3681: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  3684: dup            
        //  3685: getstatic       net/minecraft/data/worldgen/Features$States.LARGE_FERN:Lnet/minecraft/world/level/block/state/BlockState;
        //  3688: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  3691: new             Lnet/minecraft/world/level/levelgen/feature/blockplacers/DoublePlantPlacer;
        //  3694: dup            
        //  3695: invokespecial   net/minecraft/world/level/levelgen/feature/blockplacers/DoublePlantPlacer.<init>:()V
        //  3698: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  3701: bipush          64
        //  3703: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3706: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3709: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3712: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3715: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3718: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3721: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3724: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3727: bipush          7
        //  3729: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3732: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3735: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3738: putstatic       net/minecraft/data/worldgen/Features.PATCH_LARGE_FERN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3741: ldc_w           "patch_cactus"
        //  3744: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3747: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3750: dup            
        //  3751: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  3754: dup            
        //  3755: getstatic       net/minecraft/data/worldgen/Features$States.CACTUS:Lnet/minecraft/world/level/block/state/BlockState;
        //  3758: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  3761: new             Lnet/minecraft/world/level/levelgen/feature/blockplacers/ColumnPlacer;
        //  3764: dup            
        //  3765: iconst_1       
        //  3766: iconst_2       
        //  3767: invokespecial   net/minecraft/world/level/levelgen/feature/blockplacers/ColumnPlacer.<init>:(II)V
        //  3770: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  3773: bipush          10
        //  3775: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3778: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.noProjection:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  3781: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3784: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3787: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3790: putstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3793: ldc_w           "patch_cactus_desert"
        //  3796: getstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3799: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3802: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3805: bipush          10
        //  3807: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3810: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3813: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3816: putstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS_DESERT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3819: ldc_w           "patch_cactus_decorated"
        //  3822: getstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3825: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3828: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3831: iconst_5       
        //  3832: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3835: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3838: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3841: putstatic       net/minecraft/data/worldgen/Features.PATCH_CACTUS_DECORATED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3844: ldc_w           "patch_sugar_cane_swamp"
        //  3847: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3850: getstatic       net/minecraft/data/worldgen/Features$Configs.SUGAR_CANE_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3853: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3856: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3859: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3862: bipush          20
        //  3864: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3867: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3870: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3873: putstatic       net/minecraft/data/worldgen/Features.PATCH_SUGAR_CANE_SWAMP:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3876: ldc_w           "patch_sugar_cane_desert"
        //  3879: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3882: getstatic       net/minecraft/data/worldgen/Features$Configs.SUGAR_CANE_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3885: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3888: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3891: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3894: bipush          60
        //  3896: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3899: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3902: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3905: putstatic       net/minecraft/data/worldgen/Features.PATCH_SUGAR_CANE_DESERT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3908: ldc_w           "patch_sugar_cane_badlands"
        //  3911: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3914: getstatic       net/minecraft/data/worldgen/Features$Configs.SUGAR_CANE_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3917: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3920: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3923: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3926: bipush          13
        //  3928: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3931: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3934: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3937: putstatic       net/minecraft/data/worldgen/Features.PATCH_SUGAR_CANE_BADLANDS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3940: ldc_w           "patch_sugar_cane"
        //  3943: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  3946: getstatic       net/minecraft/data/worldgen/Features$Configs.SUGAR_CANE_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  3949: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3952: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  3955: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3958: bipush          10
        //  3960: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  3963: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3966: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3969: putstatic       net/minecraft/data/worldgen/Features.PATCH_SUGAR_CANE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3972: ldc_w           "brown_mushroom_nether"
        //  3975: getstatic       net/minecraft/data/worldgen/Features.PATCH_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3978: sipush          128
        //  3981: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  3984: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3987: iconst_2       
        //  3988: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  3991: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3994: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  3997: putstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_NETHER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4000: ldc_w           "red_mushroom_nether"
        //  4003: getstatic       net/minecraft/data/worldgen/Features.PATCH_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4006: sipush          128
        //  4009: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4012: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4015: iconst_2       
        //  4016: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  4019: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4022: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4025: putstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_NETHER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4028: ldc_w           "brown_mushroom_normal"
        //  4031: getstatic       net/minecraft/data/worldgen/Features.PATCH_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4034: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4037: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4040: iconst_4       
        //  4041: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  4044: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4047: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4050: putstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_NORMAL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4053: ldc_w           "red_mushroom_normal"
        //  4056: getstatic       net/minecraft/data/worldgen/Features.PATCH_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4059: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4062: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4065: bipush          8
        //  4067: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  4070: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4073: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4076: putstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_NORMAL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4079: ldc_w           "brown_mushroom_taiga"
        //  4082: getstatic       net/minecraft/data/worldgen/Features.PATCH_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4085: iconst_4       
        //  4086: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  4089: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4092: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4095: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4098: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4101: putstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4104: ldc_w           "red_mushroom_taiga"
        //  4107: getstatic       net/minecraft/data/worldgen/Features.PATCH_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4110: bipush          8
        //  4112: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.chance:(I)Ljava/lang/Object;
        //  4115: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4118: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_DOUBLE_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4121: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4124: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4127: putstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4130: ldc_w           "brown_mushroom_giant"
        //  4133: getstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4136: iconst_3       
        //  4137: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4140: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4143: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4146: putstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_GIANT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4149: ldc_w           "red_mushroom_giant"
        //  4152: getstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4155: iconst_3       
        //  4156: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4159: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4162: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4165: putstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_GIANT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4168: ldc_w           "brown_mushroom_swamp"
        //  4171: getstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4174: bipush          8
        //  4176: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4179: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4182: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4185: putstatic       net/minecraft/data/worldgen/Features.BROWN_MUSHROOM_SWAMP:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4188: ldc_w           "red_mushroom_swamp"
        //  4191: getstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_TAIGA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4194: bipush          8
        //  4196: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4199: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4202: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4205: putstatic       net/minecraft/data/worldgen/Features.RED_MUSHROOM_SWAMP:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4208: ldc_w           "ore_magma"
        //  4211: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4214: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4217: dup            
        //  4218: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4221: getstatic       net/minecraft/data/worldgen/Features$States.MAGMA_BLOCK:Lnet/minecraft/world/level/block/state/BlockState;
        //  4224: bipush          33
        //  4226: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4229: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4232: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.MAGMA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  4235: getstatic       net/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //  4238: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4241: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4244: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4247: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4250: iconst_4       
        //  4251: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4254: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4257: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4260: putstatic       net/minecraft/data/worldgen/Features.ORE_MAGMA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4263: ldc_w           "ore_soul_sand"
        //  4266: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4269: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4272: dup            
        //  4273: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4276: getstatic       net/minecraft/data/worldgen/Features$States.SOUL_SAND:Lnet/minecraft/world/level/block/state/BlockState;
        //  4279: bipush          12
        //  4281: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4284: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4287: bipush          32
        //  4289: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4292: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4295: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4298: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4301: bipush          12
        //  4303: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4306: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4309: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4312: putstatic       net/minecraft/data/worldgen/Features.ORE_SOUL_SAND:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4315: ldc_w           "ore_gold_deltas"
        //  4318: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4321: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4324: dup            
        //  4325: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4328: getstatic       net/minecraft/data/worldgen/Features$States.NETHER_GOLD_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4331: bipush          10
        //  4333: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4336: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4339: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_10_20_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4342: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4345: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4348: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4351: bipush          20
        //  4353: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4356: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4359: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4362: putstatic       net/minecraft/data/worldgen/Features.ORE_GOLD_DELTAS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4365: ldc_w           "ore_quartz_deltas"
        //  4368: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4371: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4374: dup            
        //  4375: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4378: getstatic       net/minecraft/data/worldgen/Features$States.NETHER_QUARTZ_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4381: bipush          14
        //  4383: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4386: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4389: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_10_20_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4392: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4395: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4398: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4401: bipush          32
        //  4403: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4406: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4409: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4412: putstatic       net/minecraft/data/worldgen/Features.ORE_QUARTZ_DELTAS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4415: ldc_w           "ore_gold_nether"
        //  4418: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4421: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4424: dup            
        //  4425: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4428: getstatic       net/minecraft/data/worldgen/Features$States.NETHER_GOLD_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4431: bipush          10
        //  4433: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4436: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4439: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_10_20_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4442: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4445: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4448: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4451: bipush          10
        //  4453: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4456: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4459: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4462: putstatic       net/minecraft/data/worldgen/Features.ORE_GOLD_NETHER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4465: ldc_w           "ore_quartz_nether"
        //  4468: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4471: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4474: dup            
        //  4475: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4478: getstatic       net/minecraft/data/worldgen/Features$States.NETHER_QUARTZ_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4481: bipush          14
        //  4483: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4486: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4489: getstatic       net/minecraft/data/worldgen/Features$Decorators.RANGE_10_20_ROOFED:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4492: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4495: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4498: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4501: bipush          16
        //  4503: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4506: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4509: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4512: putstatic       net/minecraft/data/worldgen/Features.ORE_QUARTZ_NETHER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4515: ldc_w           "ore_gravel_nether"
        //  4518: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4521: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4524: dup            
        //  4525: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4528: getstatic       net/minecraft/data/worldgen/Features$States.GRAVEL:Lnet/minecraft/world/level/block/state/BlockState;
        //  4531: bipush          33
        //  4533: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4536: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4539: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  4542: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  4545: dup            
        //  4546: iconst_5       
        //  4547: iconst_0       
        //  4548: bipush          37
        //  4550: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  4553: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4556: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4559: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4562: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4565: iconst_2       
        //  4566: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4569: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4572: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4575: putstatic       net/minecraft/data/worldgen/Features.ORE_GRAVEL_NETHER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4578: ldc_w           "ore_blackstone"
        //  4581: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4584: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4587: dup            
        //  4588: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHERRACK:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4591: getstatic       net/minecraft/data/worldgen/Features$States.BLACKSTONE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4594: bipush          33
        //  4596: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4599: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4602: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  4605: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  4608: dup            
        //  4609: iconst_5       
        //  4610: bipush          10
        //  4612: bipush          37
        //  4614: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  4617: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  4620: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4623: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4626: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4629: iconst_2       
        //  4630: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4633: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4636: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4639: putstatic       net/minecraft/data/worldgen/Features.ORE_BLACKSTONE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4642: ldc_w           "ore_dirt"
        //  4645: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4648: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4651: dup            
        //  4652: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4655: getstatic       net/minecraft/data/worldgen/Features$States.DIRT:Lnet/minecraft/world/level/block/state/BlockState;
        //  4658: bipush          33
        //  4660: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4663: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4666: sipush          256
        //  4669: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4672: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4675: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4678: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4681: bipush          10
        //  4683: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4686: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4689: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4692: putstatic       net/minecraft/data/worldgen/Features.ORE_DIRT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4695: ldc_w           "ore_gravel"
        //  4698: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4701: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4704: dup            
        //  4705: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4708: getstatic       net/minecraft/data/worldgen/Features$States.GRAVEL:Lnet/minecraft/world/level/block/state/BlockState;
        //  4711: bipush          33
        //  4713: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4716: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4719: sipush          256
        //  4722: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4725: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4728: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4731: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4734: bipush          8
        //  4736: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4739: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4742: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4745: putstatic       net/minecraft/data/worldgen/Features.ORE_GRAVEL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4748: ldc_w           "ore_granite"
        //  4751: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4754: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4757: dup            
        //  4758: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4761: getstatic       net/minecraft/data/worldgen/Features$States.GRANITE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4764: bipush          33
        //  4766: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4769: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4772: bipush          80
        //  4774: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4777: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4780: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4783: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4786: bipush          10
        //  4788: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4791: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4794: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4797: putstatic       net/minecraft/data/worldgen/Features.ORE_GRANITE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4800: ldc_w           "ore_diorite"
        //  4803: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4806: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4809: dup            
        //  4810: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4813: getstatic       net/minecraft/data/worldgen/Features$States.DIORITE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4816: bipush          33
        //  4818: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4821: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4824: bipush          80
        //  4826: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4829: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4832: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4835: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4838: bipush          10
        //  4840: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4843: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4846: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4849: putstatic       net/minecraft/data/worldgen/Features.ORE_DIORITE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4852: ldc_w           "ore_andesite"
        //  4855: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4858: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4861: dup            
        //  4862: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4865: getstatic       net/minecraft/data/worldgen/Features$States.ANDESITE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4868: bipush          33
        //  4870: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4873: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4876: bipush          80
        //  4878: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4881: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4884: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4887: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4890: bipush          10
        //  4892: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4895: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4898: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4901: putstatic       net/minecraft/data/worldgen/Features.ORE_ANDESITE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4904: ldc_w           "ore_coal"
        //  4907: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4910: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4913: dup            
        //  4914: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4917: getstatic       net/minecraft/data/worldgen/Features$States.COAL_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4920: bipush          17
        //  4922: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4925: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4928: sipush          128
        //  4931: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4934: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4937: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4940: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4943: bipush          20
        //  4945: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  4948: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4951: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4954: putstatic       net/minecraft/data/worldgen/Features.ORE_COAL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4957: ldc_w           "ore_iron"
        //  4960: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  4963: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  4966: dup            
        //  4967: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  4970: getstatic       net/minecraft/data/worldgen/Features$States.IRON_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  4973: bipush          9
        //  4975: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  4978: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4981: bipush          64
        //  4983: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  4986: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4989: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  4992: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  4995: bipush          20
        //  4997: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  5000: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5003: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5006: putstatic       net/minecraft/data/worldgen/Features.ORE_IRON:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5009: ldc_w           "ore_gold_extra"
        //  5012: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5015: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5018: dup            
        //  5019: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5022: getstatic       net/minecraft/data/worldgen/Features$States.GOLD_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5025: bipush          9
        //  5027: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5030: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5033: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5036: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  5039: dup            
        //  5040: bipush          32
        //  5042: bipush          32
        //  5044: bipush          80
        //  5046: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  5049: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5052: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5055: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5058: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5061: bipush          20
        //  5063: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  5066: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5069: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5072: putstatic       net/minecraft/data/worldgen/Features.ORE_GOLD_EXTRA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5075: ldc_w           "ore_gold"
        //  5078: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5081: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5084: dup            
        //  5085: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5088: getstatic       net/minecraft/data/worldgen/Features$States.GOLD_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5091: bipush          9
        //  5093: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5096: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5099: bipush          32
        //  5101: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  5104: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5107: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5110: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5113: iconst_2       
        //  5114: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  5117: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5120: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5123: putstatic       net/minecraft/data/worldgen/Features.ORE_GOLD:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5126: ldc_w           "ore_redstone"
        //  5129: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5132: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5135: dup            
        //  5136: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5139: getstatic       net/minecraft/data/worldgen/Features$States.REDSTONE_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5142: bipush          8
        //  5144: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5147: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5150: bipush          16
        //  5152: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  5155: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5158: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5161: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5164: bipush          8
        //  5166: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  5169: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5172: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5175: putstatic       net/minecraft/data/worldgen/Features.ORE_REDSTONE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5178: ldc_w           "ore_diamond"
        //  5181: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5184: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5187: dup            
        //  5188: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5191: getstatic       net/minecraft/data/worldgen/Features$States.DIAMOND_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5194: bipush          8
        //  5196: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5199: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5202: bipush          16
        //  5204: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  5207: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5210: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5213: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5216: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5219: putstatic       net/minecraft/data/worldgen/Features.ORE_DIAMOND:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5222: ldc_w           "ore_lapis"
        //  5225: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5228: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5231: dup            
        //  5232: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5235: getstatic       net/minecraft/data/worldgen/Features$States.LAPIS_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5238: bipush          7
        //  5240: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5243: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5246: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.DEPTH_AVERAGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5249: new             Lnet/minecraft/world/level/levelgen/placement/DepthAverageConfigation;
        //  5252: dup            
        //  5253: bipush          16
        //  5255: bipush          16
        //  5257: invokespecial   net/minecraft/world/level/levelgen/placement/DepthAverageConfigation.<init>:(II)V
        //  5260: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5263: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5266: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5269: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5272: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5275: putstatic       net/minecraft/data/worldgen/Features.ORE_LAPIS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5278: ldc_w           "ore_infested"
        //  5281: getstatic       net/minecraft/world/level/levelgen/feature/Feature.ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5284: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5287: dup            
        //  5288: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NATURAL_STONE:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5291: getstatic       net/minecraft/data/worldgen/Features$States.INFESTED_STONE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5294: bipush          9
        //  5296: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5299: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5302: bipush          64
        //  5304: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.range:(I)Ljava/lang/Object;
        //  5307: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5310: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5313: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5316: bipush          7
        //  5318: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  5321: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5324: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5327: putstatic       net/minecraft/data/worldgen/Features.ORE_INFESTED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5330: ldc_w           "ore_emerald"
        //  5333: getstatic       net/minecraft/world/level/levelgen/feature/Feature.EMERALD_ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5336: new             Lnet/minecraft/world/level/levelgen/feature/configurations/ReplaceBlockConfiguration;
        //  5339: dup            
        //  5340: getstatic       net/minecraft/data/worldgen/Features$States.STONE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5343: getstatic       net/minecraft/data/worldgen/Features$States.EMERALD_ORE:Lnet/minecraft/world/level/block/state/BlockState;
        //  5346: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/ReplaceBlockConfiguration.<init>:(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5349: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5352: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.EMERALD_ORE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5355: getstatic       net/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //  5358: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5361: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5364: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5367: putstatic       net/minecraft/data/worldgen/Features.ORE_EMERALD:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5370: ldc_w           "ore_debris_large"
        //  5373: getstatic       net/minecraft/world/level/levelgen/feature/Feature.NO_SURFACE_ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5376: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5379: dup            
        //  5380: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHER_ORE_REPLACEABLES:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5383: getstatic       net/minecraft/data/worldgen/Features$States.ANCIENT_DEBRIS:Lnet/minecraft/world/level/block/state/BlockState;
        //  5386: iconst_3       
        //  5387: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5390: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5393: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.DEPTH_AVERAGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5396: new             Lnet/minecraft/world/level/levelgen/placement/DepthAverageConfigation;
        //  5399: dup            
        //  5400: bipush          16
        //  5402: bipush          8
        //  5404: invokespecial   net/minecraft/world/level/levelgen/placement/DepthAverageConfigation.<init>:(II)V
        //  5407: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5410: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5413: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5416: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5419: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5422: putstatic       net/minecraft/data/worldgen/Features.ORE_DEBRIS_LARGE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5425: ldc_w           "ore_debris_small"
        //  5428: getstatic       net/minecraft/world/level/levelgen/feature/Feature.NO_SURFACE_ORE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5431: new             Lnet/minecraft/world/level/levelgen/feature/configurations/OreConfiguration;
        //  5434: dup            
        //  5435: getstatic       net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration$Predicates.NETHER_ORE_REPLACEABLES:Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;
        //  5438: getstatic       net/minecraft/data/worldgen/Features$States.ANCIENT_DEBRIS:Lnet/minecraft/world/level/block/state/BlockState;
        //  5441: iconst_2       
        //  5442: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/OreConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/structure/templatesystem/RuleTest;Lnet/minecraft/world/level/block/state/BlockState;I)V
        //  5445: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5448: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.RANGE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5451: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration;
        //  5454: dup            
        //  5455: bipush          8
        //  5457: bipush          16
        //  5459: sipush          128
        //  5462: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RangeDecoratorConfiguration.<init>:(III)V
        //  5465: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5468: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5471: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  5474: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5477: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5480: putstatic       net/minecraft/data/worldgen/Features.ORE_DEBRIS_SMALL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5483: ldc_w           "crimson_fungi"
        //  5486: getstatic       net/minecraft/world/level/levelgen/feature/Feature.HUGE_FUNGUS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5489: getstatic       net/minecraft/world/level/levelgen/feature/HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_NOT_PLANTED_CONFIG:Lnet/minecraft/world/level/levelgen/feature/HugeFungusConfiguration;
        //  5492: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5495: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5498: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //  5501: dup            
        //  5502: bipush          8
        //  5504: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //  5507: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5510: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5513: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5516: putstatic       net/minecraft/data/worldgen/Features.CRIMSON_FUNGI:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5519: ldc_w           "crimson_fungi_planted"
        //  5522: getstatic       net/minecraft/world/level/levelgen/feature/Feature.HUGE_FUNGUS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5525: getstatic       net/minecraft/world/level/levelgen/feature/HugeFungusConfiguration.HUGE_CRIMSON_FUNGI_PLANTED_CONFIG:Lnet/minecraft/world/level/levelgen/feature/HugeFungusConfiguration;
        //  5528: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5531: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5534: putstatic       net/minecraft/data/worldgen/Features.CRIMSON_FUNGI_PLANTED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5537: ldc_w           "warped_fungi"
        //  5540: getstatic       net/minecraft/world/level/levelgen/feature/Feature.HUGE_FUNGUS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5543: getstatic       net/minecraft/world/level/levelgen/feature/HugeFungusConfiguration.HUGE_WARPED_FUNGI_NOT_PLANTED_CONFIG:Lnet/minecraft/world/level/levelgen/feature/HugeFungusConfiguration;
        //  5546: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5549: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_MULTILAYER:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  5552: new             Lnet/minecraft/world/level/levelgen/feature/configurations/CountConfiguration;
        //  5555: dup            
        //  5556: bipush          8
        //  5558: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/CountConfiguration.<init>:(I)V
        //  5561: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  5564: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5567: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5570: putstatic       net/minecraft/data/worldgen/Features.WARPED_FUNGI:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5573: ldc_w           "warped_fungi_planted"
        //  5576: getstatic       net/minecraft/world/level/levelgen/feature/Feature.HUGE_FUNGUS:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5579: getstatic       net/minecraft/world/level/levelgen/feature/HugeFungusConfiguration.HUGE_WARPED_FUNGI_PLANTED_CONFIG:Lnet/minecraft/world/level/levelgen/feature/HugeFungusConfiguration;
        //  5582: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5585: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5588: putstatic       net/minecraft/data/worldgen/Features.WARPED_FUNGI_PLANTED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5591: ldc_w           "huge_brown_mushroom"
        //  5594: getstatic       net/minecraft/world/level/levelgen/feature/Feature.HUGE_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5597: new             Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;
        //  5600: dup            
        //  5601: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5604: dup            
        //  5605: getstatic       net/minecraft/data/worldgen/Features$States.HUGE_BROWN_MUSHROOM:Lnet/minecraft/world/level/block/state/BlockState;
        //  5608: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5611: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5614: dup            
        //  5615: getstatic       net/minecraft/data/worldgen/Features$States.HUGE_MUSHROOM_STEM:Lnet/minecraft/world/level/block/state/BlockState;
        //  5618: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5621: iconst_3       
        //  5622: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;I)V
        //  5625: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5628: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5631: putstatic       net/minecraft/data/worldgen/Features.HUGE_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5634: ldc_w           "huge_red_mushroom"
        //  5637: getstatic       net/minecraft/world/level/levelgen/feature/Feature.HUGE_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5640: new             Lnet/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration;
        //  5643: dup            
        //  5644: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5647: dup            
        //  5648: getstatic       net/minecraft/data/worldgen/Features$States.HUGE_RED_MUSHROOM:Lnet/minecraft/world/level/block/state/BlockState;
        //  5651: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5654: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5657: dup            
        //  5658: getstatic       net/minecraft/data/worldgen/Features$States.HUGE_MUSHROOM_STEM:Lnet/minecraft/world/level/block/state/BlockState;
        //  5661: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5664: iconst_2       
        //  5665: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/HugeMushroomFeatureConfiguration.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;I)V
        //  5668: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5671: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5674: putstatic       net/minecraft/data/worldgen/Features.HUGE_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5677: ldc_w           "oak"
        //  5680: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5683: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5686: dup            
        //  5687: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5690: dup            
        //  5691: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  5694: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5697: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5700: dup            
        //  5701: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  5704: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5707: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer;
        //  5710: dup            
        //  5711: iconst_2       
        //  5712: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5715: iconst_0       
        //  5716: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5719: iconst_3       
        //  5720: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  5723: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  5726: dup            
        //  5727: iconst_4       
        //  5728: iconst_2       
        //  5729: iconst_0       
        //  5730: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  5733: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  5736: dup            
        //  5737: iconst_1       
        //  5738: iconst_0       
        //  5739: iconst_1       
        //  5740: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  5743: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  5746: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5749: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  5752: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5755: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5758: putstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5761: ldc_w           "dark_oak"
        //  5764: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5767: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5770: dup            
        //  5771: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5774: dup            
        //  5775: getstatic       net/minecraft/data/worldgen/Features$States.DARK_OAK_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  5778: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5781: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5784: dup            
        //  5785: getstatic       net/minecraft/data/worldgen/Features$States.DARK_OAK_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  5788: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5791: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/DarkOakFoliagePlacer;
        //  5794: dup            
        //  5795: iconst_0       
        //  5796: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5799: iconst_0       
        //  5800: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5803: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/DarkOakFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //  5806: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/DarkOakTrunkPlacer;
        //  5809: dup            
        //  5810: bipush          6
        //  5812: iconst_2       
        //  5813: iconst_1       
        //  5814: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/DarkOakTrunkPlacer.<init>:(III)V
        //  5817: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/ThreeLayersFeatureSize;
        //  5820: dup            
        //  5821: iconst_1       
        //  5822: iconst_1       
        //  5823: iconst_0       
        //  5824: iconst_1       
        //  5825: iconst_2       
        //  5826: invokestatic    java/util/OptionalInt.empty:()Ljava/util/OptionalInt;
        //  5829: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/ThreeLayersFeatureSize.<init>:(IIIIILjava/util/OptionalInt;)V
        //  5832: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  5835: ldc_w           2147483647
        //  5838: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.maxWaterDepth:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5841: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  5844: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.heightmap:(Lnet/minecraft/world/level/levelgen/Heightmap$Types;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5847: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5850: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  5853: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5856: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5859: putstatic       net/minecraft/data/worldgen/Features.DARK_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5862: ldc_w           "birch"
        //  5865: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5868: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5871: dup            
        //  5872: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5875: dup            
        //  5876: getstatic       net/minecraft/data/worldgen/Features$States.BIRCH_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  5879: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5882: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5885: dup            
        //  5886: getstatic       net/minecraft/data/worldgen/Features$States.BIRCH_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  5889: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5892: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer;
        //  5895: dup            
        //  5896: iconst_2       
        //  5897: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5900: iconst_0       
        //  5901: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5904: iconst_3       
        //  5905: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  5908: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  5911: dup            
        //  5912: iconst_5       
        //  5913: iconst_2       
        //  5914: iconst_0       
        //  5915: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  5918: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  5921: dup            
        //  5922: iconst_1       
        //  5923: iconst_0       
        //  5924: iconst_1       
        //  5925: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  5928: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  5931: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5934: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  5937: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5940: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5943: putstatic       net/minecraft/data/worldgen/Features.BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  5946: ldc_w           "acacia"
        //  5949: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  5952: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  5955: dup            
        //  5956: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5959: dup            
        //  5960: getstatic       net/minecraft/data/worldgen/Features$States.ACACIA_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  5963: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5966: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  5969: dup            
        //  5970: getstatic       net/minecraft/data/worldgen/Features$States.ACACIA_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  5973: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  5976: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/AcaciaFoliagePlacer;
        //  5979: dup            
        //  5980: iconst_2       
        //  5981: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5984: iconst_0       
        //  5985: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  5988: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/AcaciaFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //  5991: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/ForkingTrunkPlacer;
        //  5994: dup            
        //  5995: iconst_5       
        //  5996: iconst_2       
        //  5997: iconst_2       
        //  5998: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/ForkingTrunkPlacer.<init>:(III)V
        //  6001: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6004: dup            
        //  6005: iconst_1       
        //  6006: iconst_0       
        //  6007: iconst_2       
        //  6008: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6011: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6014: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6017: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6020: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6023: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6026: putstatic       net/minecraft/data/worldgen/Features.ACACIA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6029: ldc_w           "spruce"
        //  6032: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6035: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6038: dup            
        //  6039: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6042: dup            
        //  6043: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6046: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6049: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6052: dup            
        //  6053: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6056: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6059: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/SpruceFoliagePlacer;
        //  6062: dup            
        //  6063: iconst_2       
        //  6064: iconst_1       
        //  6065: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  6068: iconst_0       
        //  6069: iconst_2       
        //  6070: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  6073: iconst_1       
        //  6074: iconst_1       
        //  6075: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  6078: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/SpruceFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //  6081: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  6084: dup            
        //  6085: iconst_5       
        //  6086: iconst_2       
        //  6087: iconst_1       
        //  6088: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  6091: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6094: dup            
        //  6095: iconst_2       
        //  6096: iconst_0       
        //  6097: iconst_2       
        //  6098: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6101: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6104: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6107: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6110: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6113: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6116: putstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6119: ldc_w           "pine"
        //  6122: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6125: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6128: dup            
        //  6129: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6132: dup            
        //  6133: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6136: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6139: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6142: dup            
        //  6143: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6146: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6149: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/PineFoliagePlacer;
        //  6152: dup            
        //  6153: iconst_1       
        //  6154: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6157: iconst_1       
        //  6158: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6161: iconst_3       
        //  6162: iconst_1       
        //  6163: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  6166: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/PineFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //  6169: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  6172: dup            
        //  6173: bipush          6
        //  6175: iconst_4       
        //  6176: iconst_0       
        //  6177: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  6180: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6183: dup            
        //  6184: iconst_2       
        //  6185: iconst_0       
        //  6186: iconst_2       
        //  6187: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6190: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6193: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6196: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6199: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6202: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6205: putstatic       net/minecraft/data/worldgen/Features.PINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6208: ldc_w           "jungle_tree"
        //  6211: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6214: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6217: dup            
        //  6218: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6221: dup            
        //  6222: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6225: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6228: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6231: dup            
        //  6232: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6235: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6238: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer;
        //  6241: dup            
        //  6242: iconst_2       
        //  6243: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6246: iconst_0       
        //  6247: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6250: iconst_3       
        //  6251: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  6254: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  6257: dup            
        //  6258: iconst_4       
        //  6259: bipush          8
        //  6261: iconst_0       
        //  6262: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  6265: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6268: dup            
        //  6269: iconst_1       
        //  6270: iconst_0       
        //  6271: iconst_1       
        //  6272: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6275: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6278: new             Lnet/minecraft/world/level/levelgen/feature/treedecorators/CocoaDecorator;
        //  6281: dup            
        //  6282: ldc_w           0.2
        //  6285: invokespecial   net/minecraft/world/level/levelgen/feature/treedecorators/CocoaDecorator.<init>:(F)V
        //  6288: getstatic       net/minecraft/world/level/levelgen/feature/treedecorators/TrunkVineDecorator.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/treedecorators/TrunkVineDecorator;
        //  6291: getstatic       net/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator;
        //  6294: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  6297: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.decorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6300: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6303: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6306: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6309: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6312: putstatic       net/minecraft/data/worldgen/Features.JUNGLE_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6315: ldc_w           "fancy_oak"
        //  6318: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6321: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6324: dup            
        //  6325: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6328: dup            
        //  6329: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6332: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6335: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6338: dup            
        //  6339: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6342: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6345: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FancyFoliagePlacer;
        //  6348: dup            
        //  6349: iconst_2       
        //  6350: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6353: iconst_4       
        //  6354: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6357: iconst_4       
        //  6358: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/FancyFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  6361: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/FancyTrunkPlacer;
        //  6364: dup            
        //  6365: iconst_3       
        //  6366: bipush          11
        //  6368: iconst_0       
        //  6369: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/FancyTrunkPlacer.<init>:(III)V
        //  6372: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6375: dup            
        //  6376: iconst_0       
        //  6377: iconst_0       
        //  6378: iconst_0       
        //  6379: iconst_4       
        //  6380: invokestatic    java/util/OptionalInt.of:(I)Ljava/util/OptionalInt;
        //  6383: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(IIILjava/util/OptionalInt;)V
        //  6386: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6389: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6392: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  6395: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.heightmap:(Lnet/minecraft/world/level/levelgen/Heightmap$Types;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6398: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6401: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6404: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6407: putstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6410: ldc_w           "jungle_tree_no_vine"
        //  6413: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6416: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6419: dup            
        //  6420: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6423: dup            
        //  6424: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6427: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6430: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6433: dup            
        //  6434: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6437: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6440: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer;
        //  6443: dup            
        //  6444: iconst_2       
        //  6445: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6448: iconst_0       
        //  6449: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6452: iconst_3       
        //  6453: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  6456: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  6459: dup            
        //  6460: iconst_4       
        //  6461: bipush          8
        //  6463: iconst_0       
        //  6464: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  6467: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6470: dup            
        //  6471: iconst_1       
        //  6472: iconst_0       
        //  6473: iconst_1       
        //  6474: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6477: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6480: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6483: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6486: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6489: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6492: putstatic       net/minecraft/data/worldgen/Features.JUNGLE_TREE_NO_VINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6495: ldc_w           "mega_jungle_tree"
        //  6498: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6501: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6504: dup            
        //  6505: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6508: dup            
        //  6509: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6512: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6515: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6518: dup            
        //  6519: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6522: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6525: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/MegaJungleFoliagePlacer;
        //  6528: dup            
        //  6529: iconst_2       
        //  6530: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6533: iconst_0       
        //  6534: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6537: iconst_2       
        //  6538: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/MegaJungleFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  6541: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/MegaJungleTrunkPlacer;
        //  6544: dup            
        //  6545: bipush          10
        //  6547: iconst_2       
        //  6548: bipush          19
        //  6550: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/MegaJungleTrunkPlacer.<init>:(III)V
        //  6553: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6556: dup            
        //  6557: iconst_1       
        //  6558: iconst_1       
        //  6559: iconst_2       
        //  6560: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6563: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6566: getstatic       net/minecraft/world/level/levelgen/feature/treedecorators/TrunkVineDecorator.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/treedecorators/TrunkVineDecorator;
        //  6569: getstatic       net/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator;
        //  6572: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  6575: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.decorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6578: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6581: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6584: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6587: putstatic       net/minecraft/data/worldgen/Features.MEGA_JUNGLE_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6590: ldc_w           "mega_spruce"
        //  6593: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6596: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6599: dup            
        //  6600: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6603: dup            
        //  6604: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6607: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6610: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6613: dup            
        //  6614: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6617: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6620: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/MegaPineFoliagePlacer;
        //  6623: dup            
        //  6624: iconst_0       
        //  6625: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6628: iconst_0       
        //  6629: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6632: bipush          13
        //  6634: iconst_4       
        //  6635: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  6638: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/MegaPineFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //  6641: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/GiantTrunkPlacer;
        //  6644: dup            
        //  6645: bipush          13
        //  6647: iconst_2       
        //  6648: bipush          14
        //  6650: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/GiantTrunkPlacer.<init>:(III)V
        //  6653: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6656: dup            
        //  6657: iconst_1       
        //  6658: iconst_1       
        //  6659: iconst_2       
        //  6660: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6663: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6666: new             Lnet/minecraft/world/level/levelgen/feature/treedecorators/AlterGroundDecorator;
        //  6669: dup            
        //  6670: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6673: dup            
        //  6674: getstatic       net/minecraft/data/worldgen/Features$States.PODZOL:Lnet/minecraft/world/level/block/state/BlockState;
        //  6677: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6680: invokespecial   net/minecraft/world/level/levelgen/feature/treedecorators/AlterGroundDecorator.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  6683: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  6686: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.decorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6689: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6692: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6695: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6698: putstatic       net/minecraft/data/worldgen/Features.MEGA_SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6701: ldc_w           "mega_pine"
        //  6704: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6707: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6710: dup            
        //  6711: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6714: dup            
        //  6715: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6718: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6721: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6724: dup            
        //  6725: getstatic       net/minecraft/data/worldgen/Features$States.SPRUCE_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6728: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6731: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/MegaPineFoliagePlacer;
        //  6734: dup            
        //  6735: iconst_0       
        //  6736: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6739: iconst_0       
        //  6740: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6743: iconst_3       
        //  6744: iconst_4       
        //  6745: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  6748: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/MegaPineFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;)V
        //  6751: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/GiantTrunkPlacer;
        //  6754: dup            
        //  6755: bipush          13
        //  6757: iconst_2       
        //  6758: bipush          14
        //  6760: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/GiantTrunkPlacer.<init>:(III)V
        //  6763: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6766: dup            
        //  6767: iconst_1       
        //  6768: iconst_1       
        //  6769: iconst_2       
        //  6770: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6773: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6776: new             Lnet/minecraft/world/level/levelgen/feature/treedecorators/AlterGroundDecorator;
        //  6779: dup            
        //  6780: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6783: dup            
        //  6784: getstatic       net/minecraft/data/worldgen/Features$States.PODZOL:Lnet/minecraft/world/level/block/state/BlockState;
        //  6787: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6790: invokespecial   net/minecraft/world/level/levelgen/feature/treedecorators/AlterGroundDecorator.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;)V
        //  6793: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  6796: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.decorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6799: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6802: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6805: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6808: putstatic       net/minecraft/data/worldgen/Features.MEGA_PINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6811: ldc_w           "super_birch_bees_0002"
        //  6814: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6817: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6820: dup            
        //  6821: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6824: dup            
        //  6825: getstatic       net/minecraft/data/worldgen/Features$States.BIRCH_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6828: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6831: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6834: dup            
        //  6835: getstatic       net/minecraft/data/worldgen/Features$States.BIRCH_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6838: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6841: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer;
        //  6844: dup            
        //  6845: iconst_2       
        //  6846: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6849: iconst_0       
        //  6850: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6853: iconst_3       
        //  6854: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  6857: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  6860: dup            
        //  6861: iconst_5       
        //  6862: iconst_2       
        //  6863: bipush          6
        //  6865: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  6868: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6871: dup            
        //  6872: iconst_1       
        //  6873: iconst_0       
        //  6874: iconst_1       
        //  6875: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6878: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6881: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.ignoreVines:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6884: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_0002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  6887: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  6890: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.decorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6893: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6896: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6899: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6902: putstatic       net/minecraft/data/worldgen/Features.SUPER_BIRCH_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6905: ldc_w           "swamp_tree"
        //  6908: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  6911: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6914: dup            
        //  6915: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6918: dup            
        //  6919: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  6922: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6925: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  6928: dup            
        //  6929: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  6932: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  6935: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer;
        //  6938: dup            
        //  6939: iconst_3       
        //  6940: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6943: iconst_0       
        //  6944: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  6947: iconst_3       
        //  6948: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BlobFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  6951: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  6954: dup            
        //  6955: iconst_5       
        //  6956: iconst_3       
        //  6957: iconst_0       
        //  6958: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  6961: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  6964: dup            
        //  6965: iconst_1       
        //  6966: iconst_0       
        //  6967: iconst_1       
        //  6968: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  6971: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  6974: iconst_1       
        //  6975: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.maxWaterDepth:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6978: getstatic       net/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/treedecorators/LeaveVineDecorator;
        //  6981: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  6984: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.decorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  6987: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  6990: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6993: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  6996: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  6999: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  7002: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  7005: dup            
        //  7006: iconst_2       
        //  7007: ldc_w           0.1
        //  7010: iconst_1       
        //  7011: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  7014: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7017: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7020: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7023: putstatic       net/minecraft/data/worldgen/Features.SWAMP_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7026: ldc_w           "jungle_bush"
        //  7029: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7032: new             Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  7035: dup            
        //  7036: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  7039: dup            
        //  7040: getstatic       net/minecraft/data/worldgen/Features$States.JUNGLE_LOG:Lnet/minecraft/world/level/block/state/BlockState;
        //  7043: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  7046: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  7049: dup            
        //  7050: getstatic       net/minecraft/data/worldgen/Features$States.OAK_LEAVES:Lnet/minecraft/world/level/block/state/BlockState;
        //  7053: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  7056: new             Lnet/minecraft/world/level/levelgen/feature/foliageplacers/BushFoliagePlacer;
        //  7059: dup            
        //  7060: iconst_2       
        //  7061: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  7064: iconst_1       
        //  7065: invokestatic    net/minecraft/util/UniformInt.fixed:(I)Lnet/minecraft/util/UniformInt;
        //  7068: iconst_2       
        //  7069: invokespecial   net/minecraft/world/level/levelgen/feature/foliageplacers/BushFoliagePlacer.<init>:(Lnet/minecraft/util/UniformInt;Lnet/minecraft/util/UniformInt;I)V
        //  7072: new             Lnet/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer;
        //  7075: dup            
        //  7076: iconst_1       
        //  7077: iconst_0       
        //  7078: iconst_0       
        //  7079: invokespecial   net/minecraft/world/level/levelgen/feature/trunkplacers/StraightTrunkPlacer.<init>:(III)V
        //  7082: new             Lnet/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize;
        //  7085: dup            
        //  7086: iconst_0       
        //  7087: iconst_0       
        //  7088: iconst_0       
        //  7089: invokespecial   net/minecraft/world/level/levelgen/feature/featuresize/TwoLayersFeatureSize.<init>:(III)V
        //  7092: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/foliageplacers/FoliagePlacer;Lnet/minecraft/world/level/levelgen/feature/trunkplacers/TrunkPlacer;Lnet/minecraft/world/level/levelgen/feature/featuresize/FeatureSize;)V
        //  7095: getstatic       net/minecraft/world/level/levelgen/Heightmap$Types.MOTION_BLOCKING_NO_LEAVES:Lnet/minecraft/world/level/levelgen/Heightmap$Types;
        //  7098: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.heightmap:(Lnet/minecraft/world/level/levelgen/Heightmap$Types;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder;
        //  7101: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration$TreeConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7104: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7107: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7110: putstatic       net/minecraft/data/worldgen/Features.JUNGLE_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7113: ldc_w           "oak_bees_0002"
        //  7116: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7119: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7122: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7125: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7128: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_0002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7131: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7134: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7137: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7140: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7143: putstatic       net/minecraft/data/worldgen/Features.OAK_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7146: ldc_w           "oak_bees_002"
        //  7149: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7152: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7155: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7158: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7161: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7164: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7167: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7170: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7173: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7176: putstatic       net/minecraft/data/worldgen/Features.OAK_BEES_002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7179: ldc_w           "oak_bees_005"
        //  7182: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7185: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7188: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7191: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7194: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_005:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7197: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7200: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7203: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7206: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7209: putstatic       net/minecraft/data/worldgen/Features.OAK_BEES_005:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7212: ldc_w           "birch_bees_0002"
        //  7215: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7218: getstatic       net/minecraft/data/worldgen/Features.BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7221: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7224: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7227: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_0002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7230: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7233: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7236: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7239: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7242: putstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7245: ldc_w           "birch_bees_002"
        //  7248: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7251: getstatic       net/minecraft/data/worldgen/Features.BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7254: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7257: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7260: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7263: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7266: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7269: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7272: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7275: putstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7278: ldc_w           "birch_bees_005"
        //  7281: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7284: getstatic       net/minecraft/data/worldgen/Features.BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7287: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7290: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7293: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_005:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7296: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7299: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7302: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7305: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7308: putstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_005:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7311: ldc_w           "fancy_oak_bees_0002"
        //  7314: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7317: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7320: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7323: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7326: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_0002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7329: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7332: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7335: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7338: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7341: putstatic       net/minecraft/data/worldgen/Features.FANCY_OAK_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7344: ldc_w           "fancy_oak_bees_002"
        //  7347: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7350: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7353: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7356: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7359: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_002:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7362: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7365: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7368: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7371: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7374: putstatic       net/minecraft/data/worldgen/Features.FANCY_OAK_BEES_002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7377: ldc_w           "fancy_oak_bees_005"
        //  7380: getstatic       net/minecraft/world/level/levelgen/feature/Feature.TREE:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7383: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7386: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.config:()Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;
        //  7389: checkcast       Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7392: getstatic       net/minecraft/data/worldgen/Features$Decorators.BEEHIVE_005:Lnet/minecraft/world/level/levelgen/feature/treedecorators/BeehiveDecorator;
        //  7395: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7398: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration.withDecorators:(Ljava/util/List;)Lnet/minecraft/world/level/levelgen/feature/configurations/TreeConfiguration;
        //  7401: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7404: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7407: putstatic       net/minecraft/data/worldgen/Features.FANCY_OAK_BEES_005:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7410: ldc_w           "oak_badlands"
        //  7413: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7416: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7419: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7422: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  7425: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  7428: dup            
        //  7429: iconst_5       
        //  7430: ldc_w           0.1
        //  7433: iconst_1       
        //  7434: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  7437: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7440: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7443: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7446: putstatic       net/minecraft/data/worldgen/Features.OAK_BADLANDS:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7449: ldc_w           "spruce_snowy"
        //  7452: getstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7455: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7458: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7461: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  7464: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  7467: dup            
        //  7468: iconst_0       
        //  7469: ldc_w           0.1
        //  7472: iconst_1       
        //  7473: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  7476: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7479: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7482: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7485: putstatic       net/minecraft/data/worldgen/Features.SPRUCE_SNOWY:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7488: ldc_w           "flower_warm"
        //  7491: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FLOWER:Lnet/minecraft/world/level/levelgen/feature/AbstractFlowerFeature;
        //  7494: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_FLOWER_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  7497: invokevirtual   net/minecraft/world/level/levelgen/feature/AbstractFlowerFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7500: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7503: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7506: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7509: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7512: iconst_4       
        //  7513: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  7516: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7519: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7522: putstatic       net/minecraft/data/worldgen/Features.FLOWER_WARM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7525: ldc_w           "flower_default"
        //  7528: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FLOWER:Lnet/minecraft/world/level/levelgen/feature/AbstractFlowerFeature;
        //  7531: getstatic       net/minecraft/data/worldgen/Features$Configs.DEFAULT_FLOWER_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  7534: invokevirtual   net/minecraft/world/level/levelgen/feature/AbstractFlowerFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7537: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7540: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7543: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7546: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7549: iconst_2       
        //  7550: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  7553: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7556: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7559: putstatic       net/minecraft/data/worldgen/Features.FLOWER_DEFAULT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7562: ldc_w           "flower_forest"
        //  7565: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FLOWER:Lnet/minecraft/world/level/levelgen/feature/AbstractFlowerFeature;
        //  7568: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  7571: dup            
        //  7572: getstatic       net/minecraft/world/level/levelgen/feature/stateproviders/ForestFlowerProvider.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/stateproviders/ForestFlowerProvider;
        //  7575: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  7578: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  7581: bipush          64
        //  7583: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  7586: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  7589: invokevirtual   net/minecraft/world/level/levelgen/feature/AbstractFlowerFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7592: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7595: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7598: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7601: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7604: bipush          100
        //  7606: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  7609: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7612: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7615: putstatic       net/minecraft/data/worldgen/Features.FLOWER_FOREST:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7618: ldc_w           "flower_swamp"
        //  7621: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FLOWER:Lnet/minecraft/world/level/levelgen/feature/AbstractFlowerFeature;
        //  7624: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  7627: dup            
        //  7628: new             Lnet/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider;
        //  7631: dup            
        //  7632: getstatic       net/minecraft/data/worldgen/Features$States.BLUE_ORCHID:Lnet/minecraft/world/level/block/state/BlockState;
        //  7635: invokespecial   net/minecraft/world/level/levelgen/feature/stateproviders/SimpleStateProvider.<init>:(Lnet/minecraft/world/level/block/state/BlockState;)V
        //  7638: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  7641: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  7644: bipush          64
        //  7646: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  7649: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  7652: invokevirtual   net/minecraft/world/level/levelgen/feature/AbstractFlowerFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7655: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7658: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7661: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7664: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7667: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7670: putstatic       net/minecraft/data/worldgen/Features.FLOWER_SWAMP:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7673: ldc_w           "flower_plain"
        //  7676: getstatic       net/minecraft/world/level/levelgen/feature/Feature.FLOWER:Lnet/minecraft/world/level/levelgen/feature/AbstractFlowerFeature;
        //  7679: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  7682: dup            
        //  7683: getstatic       net/minecraft/world/level/levelgen/feature/stateproviders/PlainFlowerProvider.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/stateproviders/PlainFlowerProvider;
        //  7686: getstatic       net/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer.INSTANCE:Lnet/minecraft/world/level/levelgen/feature/blockplacers/SimpleBlockPlacer;
        //  7689: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.<init>:(Lnet/minecraft/world/level/levelgen/feature/stateproviders/BlockStateProvider;Lnet/minecraft/world/level/levelgen/feature/blockplacers/BlockPlacer;)V
        //  7692: bipush          64
        //  7694: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.tries:(I)Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder;
        //  7697: invokevirtual   net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration$GrassConfigurationBuilder.build:()Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  7700: invokevirtual   net/minecraft/world/level/levelgen/feature/AbstractFlowerFeature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7703: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7706: putstatic       net/minecraft/data/worldgen/Features.FLOWER_PLAIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7709: ldc_w           "flower_plain_decorated"
        //  7712: getstatic       net/minecraft/data/worldgen/Features.FLOWER_PLAIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7715: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7718: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7721: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7724: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7727: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  7730: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7733: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  7736: new             Lnet/minecraft/world/level/levelgen/feature/configurations/NoiseDependantDecoratorConfiguration;
        //  7739: dup            
        //  7740: ldc2_w          -0.8
        //  7743: bipush          15
        //  7745: iconst_4       
        //  7746: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/NoiseDependantDecoratorConfiguration.<init>:(DII)V
        //  7749: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7752: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7755: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7758: putstatic       net/minecraft/data/worldgen/Features.FLOWER_PLAIN_DECORATED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7761: invokedynamic   BootstrapMethod #0, get:()Ljava/util/function/Supplier;
        //  7766: invokedynamic   BootstrapMethod #1, get:()Ljava/util/function/Supplier;
        //  7771: invokedynamic   BootstrapMethod #2, get:()Ljava/util/function/Supplier;
        //  7776: invokedynamic   BootstrapMethod #3, get:()Ljava/util/function/Supplier;
        //  7781: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7784: putstatic       net/minecraft/data/worldgen/Features.FOREST_FLOWER_FEATURES:Lcom/google/common/collect/ImmutableList;
        //  7787: ldc_w           "forest_flower_vegetation_common"
        //  7790: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SIMPLE_RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7793: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration;
        //  7796: dup            
        //  7797: getstatic       net/minecraft/data/worldgen/Features.FOREST_FLOWER_FEATURES:Lcom/google/common/collect/ImmutableList;
        //  7800: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration.<init>:(Ljava/util/List;)V
        //  7803: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7806: iconst_m1      
        //  7807: iconst_4       
        //  7808: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  7811: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(Lnet/minecraft/util/UniformInt;)Ljava/lang/Object;
        //  7814: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7817: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7820: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7823: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7826: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7829: iconst_5       
        //  7830: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  7833: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7836: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7839: putstatic       net/minecraft/data/worldgen/Features.FOREST_FLOWER_VEGETATION_COMMON:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7842: ldc_w           "forest_flower_vegetation"
        //  7845: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SIMPLE_RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7848: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration;
        //  7851: dup            
        //  7852: getstatic       net/minecraft/data/worldgen/Features.FOREST_FLOWER_FEATURES:Lcom/google/common/collect/ImmutableList;
        //  7855: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration.<init>:(Ljava/util/List;)V
        //  7858: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7861: bipush          -3
        //  7863: iconst_4       
        //  7864: invokestatic    net/minecraft/util/UniformInt.of:(II)Lnet/minecraft/util/UniformInt;
        //  7867: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(Lnet/minecraft/util/UniformInt;)Ljava/lang/Object;
        //  7870: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7873: getstatic       net/minecraft/data/worldgen/Features$Decorators.ADD_32:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7876: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7879: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7882: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7885: iconst_5       
        //  7886: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.count:(I)Ljava/lang/Object;
        //  7889: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7892: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7895: putstatic       net/minecraft/data/worldgen/Features.FOREST_FLOWER_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7898: ldc_w           "dark_forest_vegetation_brown"
        //  7901: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7904: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  7907: dup            
        //  7908: getstatic       net/minecraft/data/worldgen/Features.HUGE_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7911: ldc_w           0.025
        //  7914: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  7917: getstatic       net/minecraft/data/worldgen/Features.HUGE_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7920: ldc_w           0.05
        //  7923: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  7926: getstatic       net/minecraft/data/worldgen/Features.DARK_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7929: ldc_w           0.6666667
        //  7932: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  7935: getstatic       net/minecraft/data/worldgen/Features.BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7938: ldc_w           0.2
        //  7941: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  7944: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7947: ldc_w           0.1
        //  7950: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  7953: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  7956: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7959: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  7962: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7965: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.DARK_OAK_TREE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  7968: getstatic       net/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //  7971: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  7974: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7977: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7980: putstatic       net/minecraft/data/worldgen/Features.DARK_FOREST_VEGETATION_BROWN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7983: ldc_w           "dark_forest_vegetation_red"
        //  7986: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  7989: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  7992: dup            
        //  7993: getstatic       net/minecraft/data/worldgen/Features.HUGE_RED_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  7996: ldc_w           0.025
        //  7999: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8002: getstatic       net/minecraft/data/worldgen/Features.HUGE_BROWN_MUSHROOM:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8005: ldc_w           0.05
        //  8008: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8011: getstatic       net/minecraft/data/worldgen/Features.DARK_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8014: ldc_w           0.6666667
        //  8017: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8020: getstatic       net/minecraft/data/worldgen/Features.BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8023: ldc_w           0.2
        //  8026: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8029: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8032: ldc_w           0.1
        //  8035: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8038: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8041: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8044: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8047: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8050: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.DARK_OAK_TREE:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8053: getstatic       net/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration.NONE:Lnet/minecraft/world/level/levelgen/feature/configurations/NoneDecoratorConfiguration;
        //  8056: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8059: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8062: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8065: putstatic       net/minecraft/data/worldgen/Features.DARK_FOREST_VEGETATION_RED:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8068: ldc_w           "warm_ocean_vegetation"
        //  8071: getstatic       net/minecraft/world/level/levelgen/feature/Feature.SIMPLE_RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8074: new             Lnet/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration;
        //  8077: dup            
        //  8078: invokedynamic   BootstrapMethod #4, get:()Ljava/util/function/Supplier;
        //  8083: invokedynamic   BootstrapMethod #5, get:()Ljava/util/function/Supplier;
        //  8088: invokedynamic   BootstrapMethod #6, get:()Ljava/util/function/Supplier;
        //  8093: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8096: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/SimpleRandomFeatureConfiguration.<init>:(Ljava/util/List;)V
        //  8099: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8102: getstatic       net/minecraft/data/worldgen/Features$Decorators.TOP_SOLID_HEIGHTMAP:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8105: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8108: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.squared:()Ljava/lang/Object;
        //  8111: checkcast       Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8114: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_NOISE_BIASED:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8117: new             Lnet/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration;
        //  8120: dup            
        //  8121: bipush          20
        //  8123: ldc2_w          400.0
        //  8126: dconst_0       
        //  8127: invokespecial   net/minecraft/world/level/levelgen/placement/NoiseCountFactorDecoratorConfiguration.<init>:(IDD)V
        //  8130: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8133: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8136: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8139: putstatic       net/minecraft/data/worldgen/Features.WARM_OCEAN_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8142: ldc_w           "forest_flower_trees"
        //  8145: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8148: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8151: dup            
        //  8152: getstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8155: ldc_w           0.2
        //  8158: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8161: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK_BEES_002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8164: ldc_w           0.1
        //  8167: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8170: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8173: getstatic       net/minecraft/data/worldgen/Features.OAK_BEES_002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8176: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8179: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8182: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8185: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8188: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8191: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8194: dup            
        //  8195: bipush          6
        //  8197: ldc_w           0.1
        //  8200: iconst_1       
        //  8201: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8204: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8207: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8210: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8213: putstatic       net/minecraft/data/worldgen/Features.FOREST_FLOWER_TREES:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8216: ldc_w           "taiga_vegetation"
        //  8219: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8222: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8225: dup            
        //  8226: getstatic       net/minecraft/data/worldgen/Features.PINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8229: ldc_w           0.33333334
        //  8232: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8235: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8238: getstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8241: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8244: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8247: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8250: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8253: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8256: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8259: dup            
        //  8260: bipush          10
        //  8262: ldc_w           0.1
        //  8265: iconst_1       
        //  8266: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8269: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8272: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8275: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8278: putstatic       net/minecraft/data/worldgen/Features.TAIGA_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8281: ldc_w           "trees_shattered_savanna"
        //  8284: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8287: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8290: dup            
        //  8291: getstatic       net/minecraft/data/worldgen/Features.ACACIA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8294: ldc_w           0.8
        //  8297: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8300: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8303: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8306: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8309: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8312: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8315: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8318: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8321: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8324: dup            
        //  8325: iconst_2       
        //  8326: ldc_w           0.1
        //  8329: iconst_1       
        //  8330: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8333: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8336: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8339: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8342: putstatic       net/minecraft/data/worldgen/Features.TREES_SHATTERED_SAVANNA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8345: ldc_w           "trees_savanna"
        //  8348: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8351: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8354: dup            
        //  8355: getstatic       net/minecraft/data/worldgen/Features.ACACIA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8358: ldc_w           0.8
        //  8361: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8364: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8367: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8370: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8373: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8376: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8379: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8382: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8385: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8388: dup            
        //  8389: iconst_1       
        //  8390: ldc_w           0.1
        //  8393: iconst_1       
        //  8394: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8397: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8400: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8403: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8406: putstatic       net/minecraft/data/worldgen/Features.TREES_SAVANNA:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8409: ldc_w           "birch_tall"
        //  8412: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8415: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8418: dup            
        //  8419: getstatic       net/minecraft/data/worldgen/Features.SUPER_BIRCH_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8422: ldc_w           0.5
        //  8425: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8428: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8431: getstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8434: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8437: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8440: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8443: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8446: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8449: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8452: dup            
        //  8453: bipush          10
        //  8455: ldc_w           0.1
        //  8458: iconst_1       
        //  8459: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8462: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8465: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8468: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8471: putstatic       net/minecraft/data/worldgen/Features.BIRCH_TALL:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8474: ldc_w           "trees_birch"
        //  8477: getstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8480: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8483: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8486: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8489: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8492: dup            
        //  8493: bipush          10
        //  8495: ldc_w           0.1
        //  8498: iconst_1       
        //  8499: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8502: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8505: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8508: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8511: putstatic       net/minecraft/data/worldgen/Features.TREES_BIRCH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8514: ldc_w           "trees_mountain_edge"
        //  8517: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8520: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8523: dup            
        //  8524: getstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8527: ldc_w           0.666
        //  8530: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8533: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8536: ldc_w           0.1
        //  8539: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8542: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8545: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8548: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8551: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8554: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8557: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8560: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8563: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8566: dup            
        //  8567: iconst_3       
        //  8568: ldc_w           0.1
        //  8571: iconst_1       
        //  8572: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8575: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8578: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8581: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8584: putstatic       net/minecraft/data/worldgen/Features.TREES_MOUNTAIN_EDGE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8587: ldc_w           "trees_mountain"
        //  8590: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8593: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8596: dup            
        //  8597: getstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8600: ldc_w           0.666
        //  8603: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8606: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8609: ldc_w           0.1
        //  8612: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8615: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8618: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8621: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8624: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8627: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8630: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8633: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8636: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8639: dup            
        //  8640: iconst_0       
        //  8641: ldc_w           0.1
        //  8644: iconst_1       
        //  8645: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8648: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8651: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8654: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8657: putstatic       net/minecraft/data/worldgen/Features.TREES_MOUNTAIN:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8660: ldc_w           "trees_water"
        //  8663: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8666: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8669: dup            
        //  8670: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8673: ldc_w           0.1
        //  8676: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8679: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8682: getstatic       net/minecraft/data/worldgen/Features.OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8685: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8688: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8691: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8694: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8697: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8700: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8703: dup            
        //  8704: iconst_0       
        //  8705: ldc_w           0.1
        //  8708: iconst_1       
        //  8709: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8712: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8715: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8718: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8721: putstatic       net/minecraft/data/worldgen/Features.TREES_WATER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8724: ldc_w           "birch_other"
        //  8727: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8730: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8733: dup            
        //  8734: getstatic       net/minecraft/data/worldgen/Features.BIRCH_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8737: ldc_w           0.2
        //  8740: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8743: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8746: ldc_w           0.1
        //  8749: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8752: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8755: getstatic       net/minecraft/data/worldgen/Features.OAK_BEES_0002:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8758: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8761: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8764: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8767: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8770: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8773: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8776: dup            
        //  8777: bipush          10
        //  8779: ldc_w           0.1
        //  8782: iconst_1       
        //  8783: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8786: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8789: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8792: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8795: putstatic       net/minecraft/data/worldgen/Features.BIRCH_OTHER:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8798: ldc_w           "plain_vegetation"
        //  8801: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8804: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8807: dup            
        //  8808: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK_BEES_005:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8811: ldc_w           0.33333334
        //  8814: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8817: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8820: getstatic       net/minecraft/data/worldgen/Features.OAK_BEES_005:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8823: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8826: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8829: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8832: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8835: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8838: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8841: dup            
        //  8842: iconst_0       
        //  8843: ldc_w           0.05
        //  8846: iconst_1       
        //  8847: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8850: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8853: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8856: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8859: putstatic       net/minecraft/data/worldgen/Features.PLAIN_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8862: ldc_w           "trees_jungle_edge"
        //  8865: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8868: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8871: dup            
        //  8872: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8875: ldc_w           0.1
        //  8878: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8881: getstatic       net/minecraft/data/worldgen/Features.JUNGLE_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8884: ldc_w           0.5
        //  8887: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8890: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8893: getstatic       net/minecraft/data/worldgen/Features.JUNGLE_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8896: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8899: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8902: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8905: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8908: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8911: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8914: dup            
        //  8915: iconst_2       
        //  8916: ldc_w           0.1
        //  8919: iconst_1       
        //  8920: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8923: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8926: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8929: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8932: putstatic       net/minecraft/data/worldgen/Features.TREES_JUNGLE_EDGE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8935: ldc_w           "trees_giant_spruce"
        //  8938: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  8941: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  8944: dup            
        //  8945: getstatic       net/minecraft/data/worldgen/Features.MEGA_SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8948: ldc_w           0.33333334
        //  8951: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8954: getstatic       net/minecraft/data/worldgen/Features.PINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8957: ldc_w           0.33333334
        //  8960: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  8963: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  8966: getstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8969: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  8972: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8975: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  8978: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  8981: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  8984: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  8987: dup            
        //  8988: bipush          10
        //  8990: ldc_w           0.1
        //  8993: iconst_1       
        //  8994: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  8997: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9000: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9003: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9006: putstatic       net/minecraft/data/worldgen/Features.TREES_GIANT_SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9009: ldc_w           "trees_giant"
        //  9012: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  9015: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  9018: dup            
        //  9019: getstatic       net/minecraft/data/worldgen/Features.MEGA_SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9022: ldc_w           0.025641026
        //  9025: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9028: getstatic       net/minecraft/data/worldgen/Features.MEGA_PINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9031: ldc_w           0.30769232
        //  9034: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9037: getstatic       net/minecraft/data/worldgen/Features.PINE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9040: ldc_w           0.33333334
        //  9043: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9046: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  9049: getstatic       net/minecraft/data/worldgen/Features.SPRUCE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9052: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  9055: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9058: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9061: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9064: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  9067: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  9070: dup            
        //  9071: bipush          10
        //  9073: ldc_w           0.1
        //  9076: iconst_1       
        //  9077: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  9080: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9083: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9086: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9089: putstatic       net/minecraft/data/worldgen/Features.TREES_GIANT:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9092: ldc_w           "trees_jungle"
        //  9095: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  9098: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  9101: dup            
        //  9102: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9105: ldc_w           0.1
        //  9108: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9111: getstatic       net/minecraft/data/worldgen/Features.JUNGLE_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9114: ldc_w           0.5
        //  9117: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9120: getstatic       net/minecraft/data/worldgen/Features.MEGA_JUNGLE_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9123: ldc_w           0.33333334
        //  9126: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9129: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  9132: getstatic       net/minecraft/data/worldgen/Features.JUNGLE_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9135: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  9138: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9141: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9144: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9147: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  9150: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  9153: dup            
        //  9154: bipush          50
        //  9156: ldc_w           0.1
        //  9159: iconst_1       
        //  9160: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  9163: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9166: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9169: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9172: putstatic       net/minecraft/data/worldgen/Features.TREES_JUNGLE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9175: ldc_w           "bamboo_vegetation"
        //  9178: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  9181: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration;
        //  9184: dup            
        //  9185: getstatic       net/minecraft/data/worldgen/Features.FANCY_OAK:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9188: ldc_w           0.05
        //  9191: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9194: getstatic       net/minecraft/data/worldgen/Features.JUNGLE_BUSH:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9197: ldc_w           0.15
        //  9200: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9203: getstatic       net/minecraft/data/worldgen/Features.MEGA_JUNGLE_TREE:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9206: ldc_w           0.7
        //  9209: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.weighted:(F)Lnet/minecraft/world/level/levelgen/feature/WeightedConfiguredFeature;
        //  9212: invokestatic    com/google/common/collect/ImmutableList.of:(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList;
        //  9215: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_PATCH:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  9218: getstatic       net/minecraft/data/worldgen/Features$Configs.JUNGLE_GRASS_CONFIG:Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
        //  9221: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9224: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomFeatureConfiguration.<init>:(Ljava/util/List;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)V
        //  9227: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9230: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9233: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9236: getstatic       net/minecraft/world/level/levelgen/placement/FeatureDecorator.COUNT_EXTRA:Lnet/minecraft/world/level/levelgen/placement/FeatureDecorator;
        //  9239: new             Lnet/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration;
        //  9242: dup            
        //  9243: bipush          30
        //  9245: ldc_w           0.1
        //  9248: iconst_1       
        //  9249: invokespecial   net/minecraft/world/level/levelgen/placement/FrequencyWithExtraChanceDecoratorConfiguration.<init>:(IFI)V
        //  9252: invokevirtual   net/minecraft/world/level/levelgen/placement/FeatureDecorator.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/DecoratorConfiguration;)Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9255: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9258: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9261: putstatic       net/minecraft/data/worldgen/Features.BAMBOO_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9264: ldc_w           "mushroom_field_vegetation"
        //  9267: getstatic       net/minecraft/world/level/levelgen/feature/Feature.RANDOM_BOOLEAN_SELECTOR:Lnet/minecraft/world/level/levelgen/feature/Feature;
        //  9270: new             Lnet/minecraft/world/level/levelgen/feature/configurations/RandomBooleanFeatureConfiguration;
        //  9273: dup            
        //  9274: invokedynamic   BootstrapMethod #7, get:()Ljava/util/function/Supplier;
        //  9279: invokedynamic   BootstrapMethod #8, get:()Ljava/util/function/Supplier;
        //  9284: invokespecial   net/minecraft/world/level/levelgen/feature/configurations/RandomBooleanFeatureConfiguration.<init>:(Ljava/util/function/Supplier;Ljava/util/function/Supplier;)V
        //  9287: invokevirtual   net/minecraft/world/level/levelgen/feature/Feature.configured:(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9290: getstatic       net/minecraft/data/worldgen/Features$Decorators.HEIGHTMAP_SQUARE:Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;
        //  9293: invokevirtual   net/minecraft/world/level/levelgen/feature/ConfiguredFeature.decorated:(Lnet/minecraft/world/level/levelgen/placement/ConfiguredDecorator;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9296: invokestatic    net/minecraft/data/worldgen/Features.register:(Ljava/lang/String;Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;)Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9299: putstatic       net/minecraft/data/worldgen/Features.MUSHROOM_FIELD_VEGETATION:Lnet/minecraft/world/level/levelgen/feature/ConfiguredFeature;
        //  9302: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 1
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2780)
        //     at com.strobel.assembler.metadata.MetadataHelper$12.visitClassType(MetadataHelper.java:2760)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper.erase(MetadataHelper.java:1661)
        //     at com.strobel.assembler.metadata.MetadataHelper.eraseRecursive(MetadataHelper.java:1642)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1506)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2695)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
        //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1072)
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
    
    public static final class Decorators {
        public static final BeehiveDecorator BEEHIVE_0002;
        public static final BeehiveDecorator BEEHIVE_002;
        public static final BeehiveDecorator BEEHIVE_005;
        public static final ConfiguredDecorator<CountConfiguration> FIRE;
        public static final ConfiguredDecorator<NoneDecoratorConfiguration> HEIGHTMAP;
        public static final ConfiguredDecorator<NoneDecoratorConfiguration> TOP_SOLID_HEIGHTMAP;
        public static final ConfiguredDecorator<NoneDecoratorConfiguration> HEIGHTMAP_WORLD_SURFACE;
        public static final ConfiguredDecorator<NoneDecoratorConfiguration> HEIGHTMAP_DOUBLE;
        public static final ConfiguredDecorator<RangeDecoratorConfiguration> RANGE_10_20_ROOFED;
        public static final ConfiguredDecorator<RangeDecoratorConfiguration> RANGE_4_8_ROOFED;
        public static final ConfiguredDecorator<?> ADD_32;
        public static final ConfiguredDecorator<?> HEIGHTMAP_SQUARE;
        public static final ConfiguredDecorator<?> HEIGHTMAP_DOUBLE_SQUARE;
        public static final ConfiguredDecorator<?> TOP_SOLID_HEIGHTMAP_SQUARE;
        
        static {
            BEEHIVE_0002 = new BeehiveDecorator(0.002f);
            BEEHIVE_002 = new BeehiveDecorator(0.02f);
            BEEHIVE_005 = new BeehiveDecorator(0.05f);
            FIRE = FeatureDecorator.FIRE.configured(new CountConfiguration(10));
            HEIGHTMAP = FeatureDecorator.HEIGHTMAP.configured(DecoratorConfiguration.NONE);
            TOP_SOLID_HEIGHTMAP = FeatureDecorator.TOP_SOLID_HEIGHTMAP.configured(DecoratorConfiguration.NONE);
            HEIGHTMAP_WORLD_SURFACE = FeatureDecorator.HEIGHTMAP_WORLD_SURFACE.configured(DecoratorConfiguration.NONE);
            HEIGHTMAP_DOUBLE = FeatureDecorator.HEIGHTMAP_SPREAD_DOUBLE.configured(DecoratorConfiguration.NONE);
            RANGE_10_20_ROOFED = FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(10, 20, 128));
            RANGE_4_8_ROOFED = FeatureDecorator.RANGE.configured(new RangeDecoratorConfiguration(4, 8, 128));
            ADD_32 = FeatureDecorator.SPREAD_32_ABOVE.configured(NoneDecoratorConfiguration.INSTANCE);
            HEIGHTMAP_SQUARE = Decorators.HEIGHTMAP.squared();
            HEIGHTMAP_DOUBLE_SQUARE = Decorators.HEIGHTMAP_DOUBLE.squared();
            TOP_SOLID_HEIGHTMAP_SQUARE = Decorators.TOP_SOLID_HEIGHTMAP.squared();
        }
    }
    
    public static final class States {
        protected static final BlockState GRASS;
        protected static final BlockState FERN;
        protected static final BlockState PODZOL;
        protected static final BlockState COARSE_DIRT;
        protected static final BlockState MYCELIUM;
        protected static final BlockState SNOW_BLOCK;
        protected static final BlockState ICE;
        protected static final BlockState OAK_LOG;
        protected static final BlockState OAK_LEAVES;
        protected static final BlockState JUNGLE_LOG;
        protected static final BlockState JUNGLE_LEAVES;
        protected static final BlockState SPRUCE_LOG;
        protected static final BlockState SPRUCE_LEAVES;
        protected static final BlockState ACACIA_LOG;
        protected static final BlockState ACACIA_LEAVES;
        protected static final BlockState BIRCH_LOG;
        protected static final BlockState BIRCH_LEAVES;
        protected static final BlockState DARK_OAK_LOG;
        protected static final BlockState DARK_OAK_LEAVES;
        protected static final BlockState GRASS_BLOCK;
        protected static final BlockState LARGE_FERN;
        protected static final BlockState TALL_GRASS;
        protected static final BlockState LILAC;
        protected static final BlockState ROSE_BUSH;
        protected static final BlockState PEONY;
        protected static final BlockState BROWN_MUSHROOM;
        protected static final BlockState RED_MUSHROOM;
        protected static final BlockState PACKED_ICE;
        protected static final BlockState BLUE_ICE;
        protected static final BlockState LILY_OF_THE_VALLEY;
        protected static final BlockState BLUE_ORCHID;
        protected static final BlockState POPPY;
        protected static final BlockState DANDELION;
        protected static final BlockState DEAD_BUSH;
        protected static final BlockState MELON;
        protected static final BlockState PUMPKIN;
        protected static final BlockState SWEET_BERRY_BUSH;
        protected static final BlockState FIRE;
        protected static final BlockState SOUL_FIRE;
        protected static final BlockState NETHERRACK;
        protected static final BlockState SOUL_SOIL;
        protected static final BlockState CRIMSON_ROOTS;
        protected static final BlockState LILY_PAD;
        protected static final BlockState SNOW;
        protected static final BlockState JACK_O_LANTERN;
        protected static final BlockState SUNFLOWER;
        protected static final BlockState CACTUS;
        protected static final BlockState SUGAR_CANE;
        protected static final BlockState HUGE_RED_MUSHROOM;
        protected static final BlockState HUGE_BROWN_MUSHROOM;
        protected static final BlockState HUGE_MUSHROOM_STEM;
        protected static final FluidState WATER_STATE;
        protected static final FluidState LAVA_STATE;
        protected static final BlockState WATER;
        protected static final BlockState LAVA;
        protected static final BlockState DIRT;
        protected static final BlockState GRAVEL;
        protected static final BlockState GRANITE;
        protected static final BlockState DIORITE;
        protected static final BlockState ANDESITE;
        protected static final BlockState COAL_ORE;
        protected static final BlockState IRON_ORE;
        protected static final BlockState GOLD_ORE;
        protected static final BlockState REDSTONE_ORE;
        protected static final BlockState DIAMOND_ORE;
        protected static final BlockState LAPIS_ORE;
        protected static final BlockState STONE;
        protected static final BlockState EMERALD_ORE;
        protected static final BlockState INFESTED_STONE;
        protected static final BlockState SAND;
        protected static final BlockState CLAY;
        protected static final BlockState MOSSY_COBBLESTONE;
        protected static final BlockState SEAGRASS;
        protected static final BlockState MAGMA_BLOCK;
        protected static final BlockState SOUL_SAND;
        protected static final BlockState NETHER_GOLD_ORE;
        protected static final BlockState NETHER_QUARTZ_ORE;
        protected static final BlockState BLACKSTONE;
        protected static final BlockState ANCIENT_DEBRIS;
        protected static final BlockState BASALT;
        protected static final BlockState CRIMSON_FUNGUS;
        protected static final BlockState WARPED_FUNGUS;
        protected static final BlockState WARPED_ROOTS;
        protected static final BlockState NETHER_SPROUTS;
        
        static {
            GRASS = Blocks.GRASS.defaultBlockState();
            FERN = Blocks.FERN.defaultBlockState();
            PODZOL = Blocks.PODZOL.defaultBlockState();
            COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
            MYCELIUM = Blocks.MYCELIUM.defaultBlockState();
            SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
            ICE = Blocks.ICE.defaultBlockState();
            OAK_LOG = Blocks.OAK_LOG.defaultBlockState();
            OAK_LEAVES = Blocks.OAK_LEAVES.defaultBlockState();
            JUNGLE_LOG = Blocks.JUNGLE_LOG.defaultBlockState();
            JUNGLE_LEAVES = Blocks.JUNGLE_LEAVES.defaultBlockState();
            SPRUCE_LOG = Blocks.SPRUCE_LOG.defaultBlockState();
            SPRUCE_LEAVES = Blocks.SPRUCE_LEAVES.defaultBlockState();
            ACACIA_LOG = Blocks.ACACIA_LOG.defaultBlockState();
            ACACIA_LEAVES = Blocks.ACACIA_LEAVES.defaultBlockState();
            BIRCH_LOG = Blocks.BIRCH_LOG.defaultBlockState();
            BIRCH_LEAVES = Blocks.BIRCH_LEAVES.defaultBlockState();
            DARK_OAK_LOG = Blocks.DARK_OAK_LOG.defaultBlockState();
            DARK_OAK_LEAVES = Blocks.DARK_OAK_LEAVES.defaultBlockState();
            GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
            LARGE_FERN = Blocks.LARGE_FERN.defaultBlockState();
            TALL_GRASS = Blocks.TALL_GRASS.defaultBlockState();
            LILAC = Blocks.LILAC.defaultBlockState();
            ROSE_BUSH = Blocks.ROSE_BUSH.defaultBlockState();
            PEONY = Blocks.PEONY.defaultBlockState();
            BROWN_MUSHROOM = Blocks.BROWN_MUSHROOM.defaultBlockState();
            RED_MUSHROOM = Blocks.RED_MUSHROOM.defaultBlockState();
            PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
            BLUE_ICE = Blocks.BLUE_ICE.defaultBlockState();
            LILY_OF_THE_VALLEY = Blocks.LILY_OF_THE_VALLEY.defaultBlockState();
            BLUE_ORCHID = Blocks.BLUE_ORCHID.defaultBlockState();
            POPPY = Blocks.POPPY.defaultBlockState();
            DANDELION = Blocks.DANDELION.defaultBlockState();
            DEAD_BUSH = Blocks.DEAD_BUSH.defaultBlockState();
            MELON = Blocks.MELON.defaultBlockState();
            PUMPKIN = Blocks.PUMPKIN.defaultBlockState();
            SWEET_BERRY_BUSH = ((StateHolder<O, BlockState>)Blocks.SWEET_BERRY_BUSH.defaultBlockState()).<Comparable, Integer>setValue((Property<Comparable>)SweetBerryBushBlock.AGE, 3);
            FIRE = Blocks.FIRE.defaultBlockState();
            SOUL_FIRE = Blocks.SOUL_FIRE.defaultBlockState();
            NETHERRACK = Blocks.NETHERRACK.defaultBlockState();
            SOUL_SOIL = Blocks.SOUL_SOIL.defaultBlockState();
            CRIMSON_ROOTS = Blocks.CRIMSON_ROOTS.defaultBlockState();
            LILY_PAD = Blocks.LILY_PAD.defaultBlockState();
            SNOW = Blocks.SNOW.defaultBlockState();
            JACK_O_LANTERN = Blocks.JACK_O_LANTERN.defaultBlockState();
            SUNFLOWER = Blocks.SUNFLOWER.defaultBlockState();
            CACTUS = Blocks.CACTUS.defaultBlockState();
            SUGAR_CANE = Blocks.SUGAR_CANE.defaultBlockState();
            HUGE_RED_MUSHROOM = ((StateHolder<O, BlockState>)Blocks.RED_MUSHROOM_BLOCK.defaultBlockState()).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.DOWN, false);
            HUGE_BROWN_MUSHROOM = (((StateHolder<O, BlockState>)Blocks.BROWN_MUSHROOM_BLOCK.defaultBlockState()).setValue((Property<Comparable>)HugeMushroomBlock.UP, true)).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.DOWN, false);
            HUGE_MUSHROOM_STEM = (((StateHolder<O, BlockState>)Blocks.MUSHROOM_STEM.defaultBlockState()).setValue((Property<Comparable>)HugeMushroomBlock.UP, false)).<Comparable, Boolean>setValue((Property<Comparable>)HugeMushroomBlock.DOWN, false);
            WATER_STATE = Fluids.WATER.defaultFluidState();
            LAVA_STATE = Fluids.LAVA.defaultFluidState();
            WATER = Blocks.WATER.defaultBlockState();
            LAVA = Blocks.LAVA.defaultBlockState();
            DIRT = Blocks.DIRT.defaultBlockState();
            GRAVEL = Blocks.GRAVEL.defaultBlockState();
            GRANITE = Blocks.GRANITE.defaultBlockState();
            DIORITE = Blocks.DIORITE.defaultBlockState();
            ANDESITE = Blocks.ANDESITE.defaultBlockState();
            COAL_ORE = Blocks.COAL_ORE.defaultBlockState();
            IRON_ORE = Blocks.IRON_ORE.defaultBlockState();
            GOLD_ORE = Blocks.GOLD_ORE.defaultBlockState();
            REDSTONE_ORE = Blocks.REDSTONE_ORE.defaultBlockState();
            DIAMOND_ORE = Blocks.DIAMOND_ORE.defaultBlockState();
            LAPIS_ORE = Blocks.LAPIS_ORE.defaultBlockState();
            STONE = Blocks.STONE.defaultBlockState();
            EMERALD_ORE = Blocks.EMERALD_ORE.defaultBlockState();
            INFESTED_STONE = Blocks.INFESTED_STONE.defaultBlockState();
            SAND = Blocks.SAND.defaultBlockState();
            CLAY = Blocks.CLAY.defaultBlockState();
            MOSSY_COBBLESTONE = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            SEAGRASS = Blocks.SEAGRASS.defaultBlockState();
            MAGMA_BLOCK = Blocks.MAGMA_BLOCK.defaultBlockState();
            SOUL_SAND = Blocks.SOUL_SAND.defaultBlockState();
            NETHER_GOLD_ORE = Blocks.NETHER_GOLD_ORE.defaultBlockState();
            NETHER_QUARTZ_ORE = Blocks.NETHER_QUARTZ_ORE.defaultBlockState();
            BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();
            ANCIENT_DEBRIS = Blocks.ANCIENT_DEBRIS.defaultBlockState();
            BASALT = Blocks.BASALT.defaultBlockState();
            CRIMSON_FUNGUS = Blocks.CRIMSON_FUNGUS.defaultBlockState();
            WARPED_FUNGUS = Blocks.WARPED_FUNGUS.defaultBlockState();
            WARPED_ROOTS = Blocks.WARPED_ROOTS.defaultBlockState();
            NETHER_SPROUTS = Blocks.NETHER_SPROUTS.defaultBlockState();
        }
    }
    
    public static final class Configs {
        public static final RandomPatchConfiguration DEFAULT_GRASS_CONFIG;
        public static final RandomPatchConfiguration TAIGA_GRASS_CONFIG;
        public static final RandomPatchConfiguration JUNGLE_GRASS_CONFIG;
        public static final RandomPatchConfiguration DEFAULT_FLOWER_CONFIG;
        public static final RandomPatchConfiguration DEAD_BUSH_CONFIG;
        public static final RandomPatchConfiguration SWEET_BERRY_BUSH_CONFIG;
        public static final RandomPatchConfiguration TALL_GRASS_CONFIG;
        public static final RandomPatchConfiguration SUGAR_CANE_CONFIG;
        public static final SpringConfiguration LAVA_SPRING_CONFIG;
        public static final SpringConfiguration CLOSED_NETHER_SPRING_CONFIG;
        public static final BlockPileConfiguration CRIMSON_FOREST_CONFIG;
        public static final BlockPileConfiguration WARPED_FOREST_CONFIG;
        public static final BlockPileConfiguration NETHER_SPROUTS_CONFIG;
        
        static {
            DEFAULT_GRASS_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(States.GRASS), SimpleBlockPlacer.INSTANCE).tries(32).build();
            TAIGA_GRASS_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new WeightedStateProvider().add(States.GRASS, 1).add(States.FERN, 4), SimpleBlockPlacer.INSTANCE).tries(32).build();
            JUNGLE_GRASS_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new WeightedStateProvider().add(States.GRASS, 3).add(States.FERN, 1), SimpleBlockPlacer.INSTANCE).blacklist(ImmutableSet.of(States.PODZOL)).tries(32).build();
            DEFAULT_FLOWER_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new WeightedStateProvider().add(States.POPPY, 2).add(States.DANDELION, 1), SimpleBlockPlacer.INSTANCE).tries(64).build();
            DEAD_BUSH_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(States.DEAD_BUSH), SimpleBlockPlacer.INSTANCE).tries(4).build();
            SWEET_BERRY_BUSH_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(States.SWEET_BERRY_BUSH), SimpleBlockPlacer.INSTANCE).tries(64).whitelist(ImmutableSet.of(States.GRASS_BLOCK.getBlock())).noProjection().build();
            TALL_GRASS_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(States.TALL_GRASS), new DoublePlantPlacer()).tries(64).noProjection().build();
            SUGAR_CANE_CONFIG = new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(States.SUGAR_CANE), new ColumnPlacer(2, 2)).tries(20).xspread(4).yspread(0).zspread(4).noProjection().needWater().build();
            LAVA_SPRING_CONFIG = new SpringConfiguration(States.LAVA_STATE, true, 4, 1, ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE));
            CLOSED_NETHER_SPRING_CONFIG = new SpringConfiguration(States.LAVA_STATE, false, 5, 0, ImmutableSet.of(Blocks.NETHERRACK));
            CRIMSON_FOREST_CONFIG = new BlockPileConfiguration(new WeightedStateProvider().add(States.CRIMSON_ROOTS, 87).add(States.CRIMSON_FUNGUS, 11).add(States.WARPED_FUNGUS, 1));
            WARPED_FOREST_CONFIG = new BlockPileConfiguration(new WeightedStateProvider().add(States.WARPED_ROOTS, 85).add(States.CRIMSON_ROOTS, 1).add(States.WARPED_FUNGUS, 13).add(States.CRIMSON_FUNGUS, 1));
            NETHER_SPROUTS_CONFIG = new BlockPileConfiguration(new SimpleStateProvider(States.NETHER_SPROUTS));
        }
    }
}
