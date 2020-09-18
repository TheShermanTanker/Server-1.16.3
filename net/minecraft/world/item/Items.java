package net.minecraft.world.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;

public class Items {
    public static final Item AIR;
    public static final Item STONE;
    public static final Item GRANITE;
    public static final Item POLISHED_GRANITE;
    public static final Item DIORITE;
    public static final Item POLISHED_DIORITE;
    public static final Item ANDESITE;
    public static final Item POLISHED_ANDESITE;
    public static final Item GRASS_BLOCK;
    public static final Item DIRT;
    public static final Item COARSE_DIRT;
    public static final Item PODZOL;
    public static final Item CRIMSON_NYLIUM;
    public static final Item WARPED_NYLIUM;
    public static final Item COBBLESTONE;
    public static final Item OAK_PLANKS;
    public static final Item SPRUCE_PLANKS;
    public static final Item BIRCH_PLANKS;
    public static final Item JUNGLE_PLANKS;
    public static final Item ACACIA_PLANKS;
    public static final Item DARK_OAK_PLANKS;
    public static final Item CRIMSON_PLANKS;
    public static final Item WARPED_PLANKS;
    public static final Item OAK_SAPLING;
    public static final Item SPRUCE_SAPLING;
    public static final Item BIRCH_SAPLING;
    public static final Item JUNGLE_SAPLING;
    public static final Item ACACIA_SAPLING;
    public static final Item DARK_OAK_SAPLING;
    public static final Item BEDROCK;
    public static final Item SAND;
    public static final Item RED_SAND;
    public static final Item GRAVEL;
    public static final Item GOLD_ORE;
    public static final Item IRON_ORE;
    public static final Item COAL_ORE;
    public static final Item NETHER_GOLD_ORE;
    public static final Item OAK_LOG;
    public static final Item SPRUCE_LOG;
    public static final Item BIRCH_LOG;
    public static final Item JUNGLE_LOG;
    public static final Item ACACIA_LOG;
    public static final Item DARK_OAK_LOG;
    public static final Item CRIMSON_STEM;
    public static final Item WARPED_STEM;
    public static final Item STRIPPED_OAK_LOG;
    public static final Item STRIPPED_SPRUCE_LOG;
    public static final Item STRIPPED_BIRCH_LOG;
    public static final Item STRIPPED_JUNGLE_LOG;
    public static final Item STRIPPED_ACACIA_LOG;
    public static final Item STRIPPED_DARK_OAK_LOG;
    public static final Item STRIPPED_CRIMSON_STEM;
    public static final Item STRIPPED_WARPED_STEM;
    public static final Item STRIPPED_OAK_WOOD;
    public static final Item STRIPPED_SPRUCE_WOOD;
    public static final Item STRIPPED_BIRCH_WOOD;
    public static final Item STRIPPED_JUNGLE_WOOD;
    public static final Item STRIPPED_ACACIA_WOOD;
    public static final Item STRIPPED_DARK_OAK_WOOD;
    public static final Item STRIPPED_CRIMSON_HYPHAE;
    public static final Item STRIPPED_WARPED_HYPHAE;
    public static final Item OAK_WOOD;
    public static final Item SPRUCE_WOOD;
    public static final Item BIRCH_WOOD;
    public static final Item JUNGLE_WOOD;
    public static final Item ACACIA_WOOD;
    public static final Item DARK_OAK_WOOD;
    public static final Item CRIMSON_HYPHAE;
    public static final Item WARPED_HYPHAE;
    public static final Item OAK_LEAVES;
    public static final Item SPRUCE_LEAVES;
    public static final Item BIRCH_LEAVES;
    public static final Item JUNGLE_LEAVES;
    public static final Item ACACIA_LEAVES;
    public static final Item DARK_OAK_LEAVES;
    public static final Item SPONGE;
    public static final Item WET_SPONGE;
    public static final Item GLASS;
    public static final Item LAPIS_ORE;
    public static final Item LAPIS_BLOCK;
    public static final Item DISPENSER;
    public static final Item SANDSTONE;
    public static final Item CHISELED_SANDSTONE;
    public static final Item CUT_SANDSTONE;
    public static final Item NOTE_BLOCK;
    public static final Item POWERED_RAIL;
    public static final Item DETECTOR_RAIL;
    public static final Item STICKY_PISTON;
    public static final Item COBWEB;
    public static final Item GRASS;
    public static final Item FERN;
    public static final Item DEAD_BUSH;
    public static final Item SEAGRASS;
    public static final Item SEA_PICKLE;
    public static final Item PISTON;
    public static final Item WHITE_WOOL;
    public static final Item ORANGE_WOOL;
    public static final Item MAGENTA_WOOL;
    public static final Item LIGHT_BLUE_WOOL;
    public static final Item YELLOW_WOOL;
    public static final Item LIME_WOOL;
    public static final Item PINK_WOOL;
    public static final Item GRAY_WOOL;
    public static final Item LIGHT_GRAY_WOOL;
    public static final Item CYAN_WOOL;
    public static final Item PURPLE_WOOL;
    public static final Item BLUE_WOOL;
    public static final Item BROWN_WOOL;
    public static final Item GREEN_WOOL;
    public static final Item RED_WOOL;
    public static final Item BLACK_WOOL;
    public static final Item DANDELION;
    public static final Item POPPY;
    public static final Item BLUE_ORCHID;
    public static final Item ALLIUM;
    public static final Item AZURE_BLUET;
    public static final Item RED_TULIP;
    public static final Item ORANGE_TULIP;
    public static final Item WHITE_TULIP;
    public static final Item PINK_TULIP;
    public static final Item OXEYE_DAISY;
    public static final Item CORNFLOWER;
    public static final Item LILY_OF_THE_VALLEY;
    public static final Item WITHER_ROSE;
    public static final Item BROWN_MUSHROOM;
    public static final Item RED_MUSHROOM;
    public static final Item CRIMSON_FUNGUS;
    public static final Item WARPED_FUNGUS;
    public static final Item CRIMSON_ROOTS;
    public static final Item WARPED_ROOTS;
    public static final Item NETHER_SPROUTS;
    public static final Item WEEPING_VINES;
    public static final Item TWISTING_VINES;
    public static final Item SUGAR_CANE;
    public static final Item KELP;
    public static final Item BAMBOO;
    public static final Item GOLD_BLOCK;
    public static final Item IRON_BLOCK;
    public static final Item OAK_SLAB;
    public static final Item SPRUCE_SLAB;
    public static final Item BIRCH_SLAB;
    public static final Item JUNGLE_SLAB;
    public static final Item ACACIA_SLAB;
    public static final Item DARK_OAK_SLAB;
    public static final Item CRIMSON_SLAB;
    public static final Item WARPED_SLAB;
    public static final Item STONE_SLAB;
    public static final Item SMOOTH_STONE_SLAB;
    public static final Item SANDSTONE_SLAB;
    public static final Item CUT_STANDSTONE_SLAB;
    public static final Item PETRIFIED_OAK_SLAB;
    public static final Item COBBLESTONE_SLAB;
    public static final Item BRICK_SLAB;
    public static final Item STONE_BRICK_SLAB;
    public static final Item NETHER_BRICK_SLAB;
    public static final Item QUARTZ_SLAB;
    public static final Item RED_SANDSTONE_SLAB;
    public static final Item CUT_RED_SANDSTONE_SLAB;
    public static final Item PURPUR_SLAB;
    public static final Item PRISMARINE_SLAB;
    public static final Item PRISMARINE_BRICK_SLAB;
    public static final Item DARK_PRISMARINE_SLAB;
    public static final Item SMOOTH_QUARTZ;
    public static final Item SMOOTH_RED_SANDSTONE;
    public static final Item SMOOTH_SANDSTONE;
    public static final Item SMOOTH_STONE;
    public static final Item BRICKS;
    public static final Item TNT;
    public static final Item BOOKSHELF;
    public static final Item MOSSY_COBBLESTONE;
    public static final Item OBSIDIAN;
    public static final Item TORCH;
    public static final Item END_ROD;
    public static final Item CHORUS_PLANT;
    public static final Item CHORUS_FLOWER;
    public static final Item PURPUR_BLOCK;
    public static final Item PURPUR_PILLAR;
    public static final Item PURPUR_STAIRS;
    public static final Item SPAWNER;
    public static final Item OAK_STAIRS;
    public static final Item CHEST;
    public static final Item DIAMOND_ORE;
    public static final Item DIAMOND_BLOCK;
    public static final Item CRAFTING_TABLE;
    public static final Item FARMLAND;
    public static final Item FURNACE;
    public static final Item LADDER;
    public static final Item RAIL;
    public static final Item COBBLESTONE_STAIRS;
    public static final Item LEVER;
    public static final Item STONE_PRESSURE_PLATE;
    public static final Item OAK_PRESSURE_PLATE;
    public static final Item SPRUCE_PRESSURE_PLATE;
    public static final Item BIRCH_PRESSURE_PLATE;
    public static final Item JUNGLE_PRESSURE_PLATE;
    public static final Item ACACIA_PRESSURE_PLATE;
    public static final Item DARK_OAK_PRESSURE_PLATE;
    public static final Item CRIMSON_PRESSURE_PLATE;
    public static final Item WARPED_PRESSURE_PLATE;
    public static final Item POLISHED_BLACKSTONE_PRESSURE_PLATE;
    public static final Item REDSTONE_ORE;
    public static final Item REDSTONE_TORCH;
    public static final Item SNOW;
    public static final Item ICE;
    public static final Item SNOW_BLOCK;
    public static final Item CACTUS;
    public static final Item CLAY;
    public static final Item JUKEBOX;
    public static final Item OAK_FENCE;
    public static final Item SPRUCE_FENCE;
    public static final Item BIRCH_FENCE;
    public static final Item JUNGLE_FENCE;
    public static final Item ACACIA_FENCE;
    public static final Item DARK_OAK_FENCE;
    public static final Item CRIMSON_FENCE;
    public static final Item WARPED_FENCE;
    public static final Item PUMPKIN;
    public static final Item CARVED_PUMPKIN;
    public static final Item NETHERRACK;
    public static final Item SOUL_SAND;
    public static final Item SOUL_SOIL;
    public static final Item BASALT;
    public static final Item POLISHED_BASALT;
    public static final Item SOUL_TORCH;
    public static final Item GLOWSTONE;
    public static final Item JACK_O_LANTERN;
    public static final Item OAK_TRAPDOOR;
    public static final Item SPRUCE_TRAPDOOR;
    public static final Item BIRCH_TRAPDOOR;
    public static final Item JUNGLE_TRAPDOOR;
    public static final Item ACACIA_TRAPDOOR;
    public static final Item DARK_OAK_TRAPDOOR;
    public static final Item CRIMSON_TRAPDOOR;
    public static final Item WARPED_TRAPDOOR;
    public static final Item INFESTED_STONE;
    public static final Item INFESTED_COBBLESTONE;
    public static final Item INFESTED_STONE_BRICKS;
    public static final Item INFESTED_MOSSY_STONE_BRICKS;
    public static final Item INFESTED_CRACKED_STONE_BRICKS;
    public static final Item INFESTED_CHISELED_STONE_BRICKS;
    public static final Item STONE_BRICKS;
    public static final Item MOSSY_STONE_BRICKS;
    public static final Item CRACKED_STONE_BRICKS;
    public static final Item CHISELED_STONE_BRICKS;
    public static final Item BROWN_MUSHROOM_BLOCK;
    public static final Item RED_MUSHROOM_BLOCK;
    public static final Item MUSHROOM_STEM;
    public static final Item IRON_BARS;
    public static final Item CHAIN;
    public static final Item GLASS_PANE;
    public static final Item MELON;
    public static final Item VINE;
    public static final Item OAK_FENCE_GATE;
    public static final Item SPRUCE_FENCE_GATE;
    public static final Item BIRCH_FENCE_GATE;
    public static final Item JUNGLE_FENCE_GATE;
    public static final Item ACACIA_FENCE_GATE;
    public static final Item DARK_OAK_FENCE_GATE;
    public static final Item CRIMSON_FENCE_GATE;
    public static final Item WARPED_FENCE_GATE;
    public static final Item BRICK_STAIRS;
    public static final Item STONE_BRICK_STAIRS;
    public static final Item MYCELIUM;
    public static final Item LILY_PAD;
    public static final Item NETHER_BRICKS;
    public static final Item CRACKED_NETHER_BRICKS;
    public static final Item CHISELED_NETHER_BRICKS;
    public static final Item NETHER_BRICK_FENCE;
    public static final Item NETHER_BRICK_STAIRS;
    public static final Item ENCHANTING_TABLE;
    public static final Item END_PORTAL_FRAME;
    public static final Item END_STONE;
    public static final Item END_STONE_BRICKS;
    public static final Item DRAGON_EGG;
    public static final Item REDSTONE_LAMP;
    public static final Item SANDSTONE_STAIRS;
    public static final Item EMERALD_ORE;
    public static final Item ENDER_CHEST;
    public static final Item TRIPWIRE_HOOK;
    public static final Item EMERALD_BLOCK;
    public static final Item SPRUCE_STAIRS;
    public static final Item BIRCH_STAIRS;
    public static final Item JUNGLE_STAIRS;
    public static final Item CRIMSON_STAIRS;
    public static final Item WARPED_STAIRS;
    public static final Item COMMAND_BLOCK;
    public static final Item BEACON;
    public static final Item COBBLESTONE_WALL;
    public static final Item MOSSY_COBBLESTONE_WALL;
    public static final Item BRICK_WALL;
    public static final Item PRISMARINE_WALL;
    public static final Item RED_SANDSTONE_WALL;
    public static final Item MOSSY_STONE_BRICK_WALL;
    public static final Item GRANITE_WALL;
    public static final Item STONE_BRICK_WALL;
    public static final Item NETHER_BRICK_WALL;
    public static final Item ANDESITE_WALL;
    public static final Item RED_NETHER_BRICK_WALL;
    public static final Item SANDSTONE_WALL;
    public static final Item END_STONE_BRICK_WALL;
    public static final Item DIORITE_WALL;
    public static final Item BLACKSTONE_WALL;
    public static final Item POLISHED_BLACKSTONE_WALL;
    public static final Item POLISHED_BLACKSTONE_BRICK_WALL;
    public static final Item STONE_BUTTON;
    public static final Item OAK_BUTTON;
    public static final Item SPRUCE_BUTTON;
    public static final Item BIRCH_BUTTON;
    public static final Item JUNGLE_BUTTON;
    public static final Item ACACIA_BUTTON;
    public static final Item DARK_OAK_BUTTON;
    public static final Item CRIMSON_BUTTON;
    public static final Item WARPED_BUTTON;
    public static final Item POLISHED_BLACKSTONE_BUTTON;
    public static final Item ANVIL;
    public static final Item CHIPPED_ANVIL;
    public static final Item DAMAGED_ANVIL;
    public static final Item TRAPPED_CHEST;
    public static final Item LIGHT_WEIGHTED_PRESSURE_PLATE;
    public static final Item HEAVY_WEIGHTED_PRESSURE_PLATE;
    public static final Item DAYLIGHT_DETECTOR;
    public static final Item REDSTONE_BLOCK;
    public static final Item NETHER_QUARTZ_ORE;
    public static final Item HOPPER;
    public static final Item CHISELED_QUARTZ_BLOCK;
    public static final Item QUARTZ_BLOCK;
    public static final Item QUARTZ_BRICKS;
    public static final Item QUARTZ_PILLAR;
    public static final Item QUARTZ_STAIRS;
    public static final Item ACTIVATOR_RAIL;
    public static final Item DROPPER;
    public static final Item WHITE_TERRACOTTA;
    public static final Item ORANGE_TERRACOTTA;
    public static final Item MAGENTA_TERRACOTTA;
    public static final Item LIGHT_BLUE_TERRACOTTA;
    public static final Item YELLOW_TERRACOTTA;
    public static final Item LIME_TERRACOTTA;
    public static final Item PINK_TERRACOTTA;
    public static final Item GRAY_TERRACOTTA;
    public static final Item LIGHT_GRAY_TERRACOTTA;
    public static final Item CYAN_TERRACOTTA;
    public static final Item PURPLE_TERRACOTTA;
    public static final Item BLUE_TERRACOTTA;
    public static final Item BROWN_TERRACOTTA;
    public static final Item GREEN_TERRACOTTA;
    public static final Item RED_TERRACOTTA;
    public static final Item BLACK_TERRACOTTA;
    public static final Item BARRIER;
    public static final Item IRON_TRAPDOOR;
    public static final Item HAY_BLOCK;
    public static final Item WHITE_CARPET;
    public static final Item ORANGE_CARPET;
    public static final Item MAGENTA_CARPET;
    public static final Item LIGHT_BLUE_CARPET;
    public static final Item YELLOW_CARPET;
    public static final Item LIME_CARPET;
    public static final Item PINK_CARPET;
    public static final Item GRAY_CARPET;
    public static final Item LIGHT_GRAY_CARPET;
    public static final Item CYAN_CARPET;
    public static final Item PURPLE_CARPET;
    public static final Item BLUE_CARPET;
    public static final Item BROWN_CARPET;
    public static final Item GREEN_CARPET;
    public static final Item RED_CARPET;
    public static final Item BLACK_CARPET;
    public static final Item TERRACOTTA;
    public static final Item COAL_BLOCK;
    public static final Item PACKED_ICE;
    public static final Item ACACIA_STAIRS;
    public static final Item DARK_OAK_STAIRS;
    public static final Item SLIME_BLOCK;
    public static final Item GRASS_PATH;
    public static final Item SUNFLOWER;
    public static final Item LILAC;
    public static final Item ROSE_BUSH;
    public static final Item PEONY;
    public static final Item TALL_GRASS;
    public static final Item LARGE_FERN;
    public static final Item WHITE_STAINED_GLASS;
    public static final Item ORANGE_STAINED_GLASS;
    public static final Item MAGENTA_STAINED_GLASS;
    public static final Item LIGHT_BLUE_STAINED_GLASS;
    public static final Item YELLOW_STAINED_GLASS;
    public static final Item LIME_STAINED_GLASS;
    public static final Item PINK_STAINED_GLASS;
    public static final Item GRAY_STAINED_GLASS;
    public static final Item LIGHT_GRAY_STAINED_GLASS;
    public static final Item CYAN_STAINED_GLASS;
    public static final Item PURPLE_STAINED_GLASS;
    public static final Item BLUE_STAINED_GLASS;
    public static final Item BROWN_STAINED_GLASS;
    public static final Item GREEN_STAINED_GLASS;
    public static final Item RED_STAINED_GLASS;
    public static final Item BLACK_STAINED_GLASS;
    public static final Item WHITE_STAINED_GLASS_PANE;
    public static final Item ORANGE_STAINED_GLASS_PANE;
    public static final Item MAGENTA_STAINED_GLASS_PANE;
    public static final Item LIGHT_BLUE_STAINED_GLASS_PANE;
    public static final Item YELLOW_STAINED_GLASS_PANE;
    public static final Item LIME_STAINED_GLASS_PANE;
    public static final Item PINK_STAINED_GLASS_PANE;
    public static final Item GRAY_STAINED_GLASS_PANE;
    public static final Item LIGHT_GRAY_STAINED_GLASS_PANE;
    public static final Item CYAN_STAINED_GLASS_PANE;
    public static final Item PURPLE_STAINED_GLASS_PANE;
    public static final Item BLUE_STAINED_GLASS_PANE;
    public static final Item BROWN_STAINED_GLASS_PANE;
    public static final Item GREEN_STAINED_GLASS_PANE;
    public static final Item RED_STAINED_GLASS_PANE;
    public static final Item BLACK_STAINED_GLASS_PANE;
    public static final Item PRISMARINE;
    public static final Item PRISMARINE_BRICKS;
    public static final Item DARK_PRISMARINE;
    public static final Item PRISMARINE_STAIRS;
    public static final Item PRISMARINE_BRICK_STAIRS;
    public static final Item DARK_PRISMARINE_STAIRS;
    public static final Item SEA_LANTERN;
    public static final Item RED_SANDSTONE;
    public static final Item CHISELED_RED_SANDSTONE;
    public static final Item CUT_RED_SANDSTONE;
    public static final Item RED_SANDSTONE_STAIRS;
    public static final Item REPEATING_COMMAND_BLOCK;
    public static final Item CHAIN_COMMAND_BLOCK;
    public static final Item MAGMA_BLOCK;
    public static final Item NETHER_WART_BLOCK;
    public static final Item WARPED_WART_BLOCK;
    public static final Item RED_NETHER_BRICKS;
    public static final Item BONE_BLOCK;
    public static final Item STRUCTURE_VOID;
    public static final Item OBSERVER;
    public static final Item SHULKER_BOX;
    public static final Item WHITE_SHULKER_BOX;
    public static final Item ORANGE_SHULKER_BOX;
    public static final Item MAGENTA_SHULKER_BOX;
    public static final Item LIGHT_BLUE_SHULKER_BOX;
    public static final Item YELLOW_SHULKER_BOX;
    public static final Item LIME_SHULKER_BOX;
    public static final Item PINK_SHULKER_BOX;
    public static final Item GRAY_SHULKER_BOX;
    public static final Item LIGHT_GRAY_SHULKER_BOX;
    public static final Item CYAN_SHULKER_BOX;
    public static final Item PURPLE_SHULKER_BOX;
    public static final Item BLUE_SHULKER_BOX;
    public static final Item BROWN_SHULKER_BOX;
    public static final Item GREEN_SHULKER_BOX;
    public static final Item RED_SHULKER_BOX;
    public static final Item BLACK_SHULKER_BOX;
    public static final Item WHITE_GLAZED_TERRACOTTA;
    public static final Item ORANGE_GLAZED_TERRACOTTA;
    public static final Item MAGENTA_GLAZED_TERRACOTTA;
    public static final Item LIGHT_BLUE_GLAZED_TERRACOTTA;
    public static final Item YELLOW_GLAZED_TERRACOTTA;
    public static final Item LIME_GLAZED_TERRACOTTA;
    public static final Item PINK_GLAZED_TERRACOTTA;
    public static final Item GRAY_GLAZED_TERRACOTTA;
    public static final Item LIGHT_GRAY_GLAZED_TERRACOTTA;
    public static final Item CYAN_GLAZED_TERRACOTTA;
    public static final Item PURPLE_GLAZED_TERRACOTTA;
    public static final Item BLUE_GLAZED_TERRACOTTA;
    public static final Item BROWN_GLAZED_TERRACOTTA;
    public static final Item GREEN_GLAZED_TERRACOTTA;
    public static final Item RED_GLAZED_TERRACOTTA;
    public static final Item BLACK_GLAZED_TERRACOTTA;
    public static final Item WHITE_CONCRETE;
    public static final Item ORANGE_CONCRETE;
    public static final Item MAGENTA_CONCRETE;
    public static final Item LIGHT_BLUE_CONCRETE;
    public static final Item YELLOW_CONCRETE;
    public static final Item LIME_CONCRETE;
    public static final Item PINK_CONCRETE;
    public static final Item GRAY_CONCRETE;
    public static final Item LIGHT_GRAY_CONCRETE;
    public static final Item CYAN_CONCRETE;
    public static final Item PURPLE_CONCRETE;
    public static final Item BLUE_CONCRETE;
    public static final Item BROWN_CONCRETE;
    public static final Item GREEN_CONCRETE;
    public static final Item RED_CONCRETE;
    public static final Item BLACK_CONCRETE;
    public static final Item WHITE_CONCRETE_POWDER;
    public static final Item ORANGE_CONCRETE_POWDER;
    public static final Item MAGENTA_CONCRETE_POWDER;
    public static final Item LIGHT_BLUE_CONCRETE_POWDER;
    public static final Item YELLOW_CONCRETE_POWDER;
    public static final Item LIME_CONCRETE_POWDER;
    public static final Item PINK_CONCRETE_POWDER;
    public static final Item GRAY_CONCRETE_POWDER;
    public static final Item LIGHT_GRAY_CONCRETE_POWDER;
    public static final Item CYAN_CONCRETE_POWDER;
    public static final Item PURPLE_CONCRETE_POWDER;
    public static final Item BLUE_CONCRETE_POWDER;
    public static final Item BROWN_CONCRETE_POWDER;
    public static final Item GREEN_CONCRETE_POWDER;
    public static final Item RED_CONCRETE_POWDER;
    public static final Item BLACK_CONCRETE_POWDER;
    public static final Item TURTLE_EGG;
    public static final Item DEAD_TUBE_CORAL_BLOCK;
    public static final Item DEAD_BRAIN_CORAL_BLOCK;
    public static final Item DEAD_BUBBLE_CORAL_BLOCK;
    public static final Item DEAD_FIRE_CORAL_BLOCK;
    public static final Item DEAD_HORN_CORAL_BLOCK;
    public static final Item TUBE_CORAL_BLOCK;
    public static final Item BRAIN_CORAL_BLOCK;
    public static final Item BUBBLE_CORAL_BLOCK;
    public static final Item FIRE_CORAL_BLOCK;
    public static final Item HORN_CORAL_BLOCK;
    public static final Item TUBE_CORAL;
    public static final Item BRAIN_CORAL;
    public static final Item BUBBLE_CORAL;
    public static final Item FIRE_CORAL;
    public static final Item HORN_CORAL;
    public static final Item DEAD_BRAIN_CORAL;
    public static final Item DEAD_BUBBLE_CORAL;
    public static final Item DEAD_FIRE_CORAL;
    public static final Item DEAD_HORN_CORAL;
    public static final Item DEAD_TUBE_CORAL;
    public static final Item TUBE_CORAL_FAN;
    public static final Item BRAIN_CORAL_FAN;
    public static final Item BUBBLE_CORAL_FAN;
    public static final Item FIRE_CORAL_FAN;
    public static final Item HORN_CORAL_FAN;
    public static final Item DEAD_TUBE_CORAL_FAN;
    public static final Item DEAD_BRAIN_CORAL_FAN;
    public static final Item DEAD_BUBBLE_CORAL_FAN;
    public static final Item DEAD_FIRE_CORAL_FAN;
    public static final Item DEAD_HORN_CORAL_FAN;
    public static final Item BLUE_ICE;
    public static final Item CONDUIT;
    public static final Item POLISHED_GRANITE_STAIRS;
    public static final Item SMOOTH_RED_SANDSTONE_STAIRS;
    public static final Item MOSSY_STONE_BRICK_STAIRS;
    public static final Item POLISHED_DIORITE_STAIRS;
    public static final Item MOSSY_COBBLESTONE_STAIRS;
    public static final Item END_STONE_BRICK_STAIRS;
    public static final Item STONE_STAIRS;
    public static final Item SMOOTH_SANDSTONE_STAIRS;
    public static final Item SMOOTH_QUARTZ_STAIRS;
    public static final Item GRANITE_STAIRS;
    public static final Item ANDESITE_STAIRS;
    public static final Item RED_NETHER_BRICK_STAIRS;
    public static final Item POLISHED_ANDESITE_STAIRS;
    public static final Item DIORITE_STAIRS;
    public static final Item POLISHED_GRANITE_SLAB;
    public static final Item SMOOTH_RED_SANDSTONE_SLAB;
    public static final Item MOSSY_STONE_BRICK_SLAB;
    public static final Item POLISHED_DIORITE_SLAB;
    public static final Item MOSSY_COBBLESTONE_SLAB;
    public static final Item END_STONE_BRICK_SLAB;
    public static final Item SMOOTH_SANDSTONE_SLAB;
    public static final Item SMOOTH_QUARTZ_SLAB;
    public static final Item GRANITE_SLAB;
    public static final Item ANDESITE_SLAB;
    public static final Item RED_NETHER_BRICK_SLAB;
    public static final Item POLISHED_ANDESITE_SLAB;
    public static final Item DIORITE_SLAB;
    public static final Item SCAFFOLDING;
    public static final Item IRON_DOOR;
    public static final Item OAK_DOOR;
    public static final Item SPRUCE_DOOR;
    public static final Item BIRCH_DOOR;
    public static final Item JUNGLE_DOOR;
    public static final Item ACACIA_DOOR;
    public static final Item DARK_OAK_DOOR;
    public static final Item CRIMSON_DOOR;
    public static final Item WARPED_DOOR;
    public static final Item REPEATER;
    public static final Item COMPARATOR;
    public static final Item STRUCTURE_BLOCK;
    public static final Item JIGSAW;
    public static final Item TURTLE_HELMET;
    public static final Item SCUTE;
    public static final Item FLINT_AND_STEEL;
    public static final Item APPLE;
    public static final Item BOW;
    public static final Item ARROW;
    public static final Item COAL;
    public static final Item CHARCOAL;
    public static final Item DIAMOND;
    public static final Item IRON_INGOT;
    public static final Item GOLD_INGOT;
    public static final Item NETHERITE_INGOT;
    public static final Item NETHERITE_SCRAP;
    public static final Item WOODEN_SWORD;
    public static final Item WOODEN_SHOVEL;
    public static final Item WOODEN_PICKAXE;
    public static final Item WOODEN_AXE;
    public static final Item WOODEN_HOE;
    public static final Item STONE_SWORD;
    public static final Item STONE_SHOVEL;
    public static final Item STONE_PICKAXE;
    public static final Item STONE_AXE;
    public static final Item STONE_HOE;
    public static final Item GOLDEN_SWORD;
    public static final Item GOLDEN_SHOVEL;
    public static final Item GOLDEN_PICKAXE;
    public static final Item GOLDEN_AXE;
    public static final Item GOLDEN_HOE;
    public static final Item IRON_SWORD;
    public static final Item IRON_SHOVEL;
    public static final Item IRON_PICKAXE;
    public static final Item IRON_AXE;
    public static final Item IRON_HOE;
    public static final Item DIAMOND_SWORD;
    public static final Item DIAMOND_SHOVEL;
    public static final Item DIAMOND_PICKAXE;
    public static final Item DIAMOND_AXE;
    public static final Item DIAMOND_HOE;
    public static final Item NETHERITE_SWORD;
    public static final Item NETHERITE_SHOVEL;
    public static final Item NETHERITE_PICKAXE;
    public static final Item NETHERITE_AXE;
    public static final Item NETHERITE_HOE;
    public static final Item STICK;
    public static final Item BOWL;
    public static final Item MUSHROOM_STEW;
    public static final Item STRING;
    public static final Item FEATHER;
    public static final Item GUNPOWDER;
    public static final Item WHEAT_SEEDS;
    public static final Item WHEAT;
    public static final Item BREAD;
    public static final Item LEATHER_HELMET;
    public static final Item LEATHER_CHESTPLATE;
    public static final Item LEATHER_LEGGINGS;
    public static final Item LEATHER_BOOTS;
    public static final Item CHAINMAIL_HELMET;
    public static final Item CHAINMAIL_CHESTPLATE;
    public static final Item CHAINMAIL_LEGGINGS;
    public static final Item CHAINMAIL_BOOTS;
    public static final Item IRON_HELMET;
    public static final Item IRON_CHESTPLATE;
    public static final Item IRON_LEGGINGS;
    public static final Item IRON_BOOTS;
    public static final Item DIAMOND_HELMET;
    public static final Item DIAMOND_CHESTPLATE;
    public static final Item DIAMOND_LEGGINGS;
    public static final Item DIAMOND_BOOTS;
    public static final Item GOLDEN_HELMET;
    public static final Item GOLDEN_CHESTPLATE;
    public static final Item GOLDEN_LEGGINGS;
    public static final Item GOLDEN_BOOTS;
    public static final Item NETHERITE_HELMET;
    public static final Item NETHERITE_CHESTPLATE;
    public static final Item NETHERITE_LEGGINGS;
    public static final Item NETHERITE_BOOTS;
    public static final Item FLINT;
    public static final Item PORKCHOP;
    public static final Item COOKED_PORKCHOP;
    public static final Item PAINTING;
    public static final Item GOLDEN_APPLE;
    public static final Item ENCHANTED_GOLDEN_APPLE;
    public static final Item OAK_SIGN;
    public static final Item SPRUCE_SIGN;
    public static final Item BIRCH_SIGN;
    public static final Item JUNGLE_SIGN;
    public static final Item ACACIA_SIGN;
    public static final Item DARK_OAK_SIGN;
    public static final Item CRIMSON_SIGN;
    public static final Item WARPED_SIGN;
    public static final Item BUCKET;
    public static final Item WATER_BUCKET;
    public static final Item LAVA_BUCKET;
    public static final Item MINECART;
    public static final Item SADDLE;
    public static final Item REDSTONE;
    public static final Item SNOWBALL;
    public static final Item OAK_BOAT;
    public static final Item LEATHER;
    public static final Item MILK_BUCKET;
    public static final Item PUFFERFISH_BUCKET;
    public static final Item SALMON_BUCKET;
    public static final Item COD_BUCKET;
    public static final Item TROPICAL_FISH_BUCKET;
    public static final Item BRICK;
    public static final Item CLAY_BALL;
    public static final Item DRIED_KELP_BLOCK;
    public static final Item PAPER;
    public static final Item BOOK;
    public static final Item SLIME_BALL;
    public static final Item CHEST_MINECART;
    public static final Item FURNACE_MINECART;
    public static final Item EGG;
    public static final Item COMPASS;
    public static final Item FISHING_ROD;
    public static final Item CLOCK;
    public static final Item GLOWSTONE_DUST;
    public static final Item COD;
    public static final Item SALMON;
    public static final Item TROPICAL_FISH;
    public static final Item PUFFERFISH;
    public static final Item COOKED_COD;
    public static final Item COOKED_SALMON;
    public static final Item INK_SAC;
    public static final Item COCOA_BEANS;
    public static final Item LAPIS_LAZULI;
    public static final Item WHITE_DYE;
    public static final Item ORANGE_DYE;
    public static final Item MAGENTA_DYE;
    public static final Item LIGHT_BLUE_DYE;
    public static final Item YELLOW_DYE;
    public static final Item LIME_DYE;
    public static final Item PINK_DYE;
    public static final Item GRAY_DYE;
    public static final Item LIGHT_GRAY_DYE;
    public static final Item CYAN_DYE;
    public static final Item PURPLE_DYE;
    public static final Item BLUE_DYE;
    public static final Item BROWN_DYE;
    public static final Item GREEN_DYE;
    public static final Item RED_DYE;
    public static final Item BLACK_DYE;
    public static final Item BONE_MEAL;
    public static final Item BONE;
    public static final Item SUGAR;
    public static final Item CAKE;
    public static final Item WHITE_BED;
    public static final Item ORANGE_BED;
    public static final Item MAGENTA_BED;
    public static final Item LIGHT_BLUE_BED;
    public static final Item YELLOW_BED;
    public static final Item LIME_BED;
    public static final Item PINK_BED;
    public static final Item GRAY_BED;
    public static final Item LIGHT_GRAY_BED;
    public static final Item CYAN_BED;
    public static final Item PURPLE_BED;
    public static final Item BLUE_BED;
    public static final Item BROWN_BED;
    public static final Item GREEN_BED;
    public static final Item RED_BED;
    public static final Item BLACK_BED;
    public static final Item COOKIE;
    public static final Item FILLED_MAP;
    public static final Item SHEARS;
    public static final Item MELON_SLICE;
    public static final Item DRIED_KELP;
    public static final Item PUMPKIN_SEEDS;
    public static final Item MELON_SEEDS;
    public static final Item BEEF;
    public static final Item COOKED_BEEF;
    public static final Item CHICKEN;
    public static final Item COOKED_CHICKEN;
    public static final Item ROTTEN_FLESH;
    public static final Item ENDER_PEARL;
    public static final Item BLAZE_ROD;
    public static final Item GHAST_TEAR;
    public static final Item GOLD_NUGGET;
    public static final Item NETHER_WART;
    public static final Item POTION;
    public static final Item GLASS_BOTTLE;
    public static final Item SPIDER_EYE;
    public static final Item FERMENTED_SPIDER_EYE;
    public static final Item BLAZE_POWDER;
    public static final Item MAGMA_CREAM;
    public static final Item BREWING_STAND;
    public static final Item CAULDRON;
    public static final Item ENDER_EYE;
    public static final Item GLISTERING_MELON_SLICE;
    public static final Item BAT_SPAWN_EGG;
    public static final Item BEE_SPAWN_EGG;
    public static final Item BLAZE_SPAWN_EGG;
    public static final Item CAT_SPAWN_EGG;
    public static final Item CAVE_SPIDER_SPAWN_EGG;
    public static final Item CHICKEN_SPAWN_EGG;
    public static final Item COD_SPAWN_EGG;
    public static final Item COW_SPAWN_EGG;
    public static final Item CREEPER_SPAWN_EGG;
    public static final Item DOLPHIN_SPAWN_EGG;
    public static final Item DONKEY_SPAWN_EGG;
    public static final Item DROWNED_SPAWN_EGG;
    public static final Item ELDER_GUARDIAN_SPAWN_EGG;
    public static final Item ENDERMAN_SPAWN_EGG;
    public static final Item ENDERMITE_SPAWN_EGG;
    public static final Item EVOKER_SPAWN_EGG;
    public static final Item FOX_SPAWN_EGG;
    public static final Item GHAST_SPAWN_EGG;
    public static final Item GUARDIAN_SPAWN_EGG;
    public static final Item HOGLIN_SPAWN_EGG;
    public static final Item HORSE_SPAWN_EGG;
    public static final Item HUSK_SPAWN_EGG;
    public static final Item LLAMA_SPAWN_EGG;
    public static final Item MAGMA_CUBE_SPAWN_EGG;
    public static final Item MOOSHROOM_SPAWN_EGG;
    public static final Item MULE_SPAWN_EGG;
    public static final Item OCELOT_SPAWN_EGG;
    public static final Item PANDA_SPAWN_EGG;
    public static final Item PARROT_SPAWN_EGG;
    public static final Item PHANTOM_SPAWN_EGG;
    public static final Item PIG_SPAWN_EGG;
    public static final Item PIGLIN_SPAWN_EGG;
    public static final Item PIGLIN_BRUTE_SPAWN_EGG;
    public static final Item PILLAGER_SPAWN_EGG;
    public static final Item POLAR_BEAR_SPAWN_EGG;
    public static final Item PUFFERFISH_SPAWN_EGG;
    public static final Item RABBIT_SPAWN_EGG;
    public static final Item RAVAGER_SPAWN_EGG;
    public static final Item SALMON_SPAWN_EGG;
    public static final Item SHEEP_SPAWN_EGG;
    public static final Item SHULKER_SPAWN_EGG;
    public static final Item SILVERFISH_SPAWN_EGG;
    public static final Item SKELETON_SPAWN_EGG;
    public static final Item SKELETON_HORSE_SPAWN_EGG;
    public static final Item SLIME_SPAWN_EGG;
    public static final Item SPIDER_SPAWN_EGG;
    public static final Item SQUID_SPAWN_EGG;
    public static final Item STRAY_SPAWN_EGG;
    public static final Item STRIDER_SPAWN_EGG;
    public static final Item TRADER_LLAMA_SPAWN_EGG;
    public static final Item TROPICAL_FISH_SPAWN_EGG;
    public static final Item TURTLE_SPAWN_EGG;
    public static final Item VEX_SPAWN_EGG;
    public static final Item VILLAGER_SPAWN_EGG;
    public static final Item VINDICATOR_SPAWN_EGG;
    public static final Item WANDERING_TRADER_SPAWN_EGG;
    public static final Item WITCH_SPAWN_EGG;
    public static final Item WITHER_SKELETON_SPAWN_EGG;
    public static final Item WOLF_SPAWN_EGG;
    public static final Item ZOGLIN_SPAWN_EGG;
    public static final Item ZOMBIE_SPAWN_EGG;
    public static final Item ZOMBIE_HORSE_SPAWN_EGG;
    public static final Item ZOMBIE_VILLAGER_SPAWN_EGG;
    public static final Item ZOMBIFIED_PIGLIN_SPAWN_EGG;
    public static final Item EXPERIENCE_BOTTLE;
    public static final Item FIRE_CHARGE;
    public static final Item WRITABLE_BOOK;
    public static final Item WRITTEN_BOOK;
    public static final Item EMERALD;
    public static final Item ITEM_FRAME;
    public static final Item FLOWER_POT;
    public static final Item CARROT;
    public static final Item POTATO;
    public static final Item BAKED_POTATO;
    public static final Item POISONOUS_POTATO;
    public static final Item MAP;
    public static final Item GOLDEN_CARROT;
    public static final Item SKELETON_SKULL;
    public static final Item WITHER_SKELETON_SKULL;
    public static final Item PLAYER_HEAD;
    public static final Item ZOMBIE_HEAD;
    public static final Item CREEPER_HEAD;
    public static final Item DRAGON_HEAD;
    public static final Item CARROT_ON_A_STICK;
    public static final Item WARPED_FUNGUS_ON_A_STICK;
    public static final Item NETHER_STAR;
    public static final Item PUMPKIN_PIE;
    public static final Item FIREWORK_ROCKET;
    public static final Item FIREWORK_STAR;
    public static final Item ENCHANTED_BOOK;
    public static final Item NETHER_BRICK;
    public static final Item QUARTZ;
    public static final Item TNT_MINECART;
    public static final Item HOPPER_MINECART;
    public static final Item PRISMARINE_SHARD;
    public static final Item PRISMARINE_CRYSTALS;
    public static final Item RABBIT;
    public static final Item COOKED_RABBIT;
    public static final Item RABBIT_STEW;
    public static final Item RABBIT_FOOT;
    public static final Item RABBIT_HIDE;
    public static final Item ARMOR_STAND;
    public static final Item IRON_HORSE_ARMOR;
    public static final Item GOLDEN_HORSE_ARMOR;
    public static final Item DIAMOND_HORSE_ARMOR;
    public static final Item LEATHER_HORSE_ARMOR;
    public static final Item LEAD;
    public static final Item NAME_TAG;
    public static final Item COMMAND_BLOCK_MINECART;
    public static final Item MUTTON;
    public static final Item COOKED_MUTTON;
    public static final Item WHITE_BANNER;
    public static final Item ORANGE_BANNER;
    public static final Item MAGENTA_BANNER;
    public static final Item LIGHT_BLUE_BANNER;
    public static final Item YELLOW_BANNER;
    public static final Item LIME_BANNER;
    public static final Item PINK_BANNER;
    public static final Item GRAY_BANNER;
    public static final Item LIGHT_GRAY_BANNER;
    public static final Item CYAN_BANNER;
    public static final Item PURPLE_BANNER;
    public static final Item BLUE_BANNER;
    public static final Item BROWN_BANNER;
    public static final Item GREEN_BANNER;
    public static final Item RED_BANNER;
    public static final Item BLACK_BANNER;
    public static final Item END_CRYSTAL;
    public static final Item CHORUS_FRUIT;
    public static final Item POPPED_CHORUS_FRUIT;
    public static final Item BEETROOT;
    public static final Item BEETROOT_SEEDS;
    public static final Item BEETROOT_SOUP;
    public static final Item DRAGON_BREATH;
    public static final Item SPLASH_POTION;
    public static final Item SPECTRAL_ARROW;
    public static final Item TIPPED_ARROW;
    public static final Item LINGERING_POTION;
    public static final Item SHIELD;
    public static final Item ELYTRA;
    public static final Item SPRUCE_BOAT;
    public static final Item BIRCH_BOAT;
    public static final Item JUNGLE_BOAT;
    public static final Item ACACIA_BOAT;
    public static final Item DARK_OAK_BOAT;
    public static final Item TOTEM_OF_UNDYING;
    public static final Item SHULKER_SHELL;
    public static final Item IRON_NUGGET;
    public static final Item KNOWLEDGE_BOOK;
    public static final Item DEBUG_STICK;
    public static final Item MUSIC_DISC_13;
    public static final Item MUSIC_DISC_CAT;
    public static final Item MUSIC_DISC_BLOCKS;
    public static final Item MUSIC_DISC_CHIRP;
    public static final Item MUSIC_DISC_FAR;
    public static final Item MUSIC_DISC_MALL;
    public static final Item MUSIC_DISC_MELLOHI;
    public static final Item MUSIC_DISC_STAL;
    public static final Item MUSIC_DISC_STRAD;
    public static final Item MUSIC_DISC_WARD;
    public static final Item MUSIC_DISC_11;
    public static final Item MUSIC_DISC_WAIT;
    public static final Item MUSIC_DISC_PIGSTEP;
    public static final Item TRIDENT;
    public static final Item PHANTOM_MEMBRANE;
    public static final Item NAUTILUS_SHELL;
    public static final Item HEART_OF_THE_SEA;
    public static final Item CROSSBOW;
    public static final Item SUSPICIOUS_STEW;
    public static final Item LOOM;
    public static final Item FLOWER_BANNER_PATTERN;
    public static final Item CREEPER_BANNER_PATTERN;
    public static final Item SKULL_BANNER_PATTERN;
    public static final Item MOJANG_BANNER_PATTERN;
    public static final Item GLOBE_BANNER_PATTER;
    public static final Item PIGLIN_BANNER_PATTERN;
    public static final Item COMPOSTER;
    public static final Item BARREL;
    public static final Item SMOKER;
    public static final Item BLAST_FURNACE;
    public static final Item CARTOGRAPHY_TABLE;
    public static final Item FLETCHING_TABLE;
    public static final Item GRINDSTONE;
    public static final Item LECTERN;
    public static final Item SMITHING_TABLE;
    public static final Item STONECUTTER;
    public static final Item BELL;
    public static final Item LANTERN;
    public static final Item SOUL_LANTERN;
    public static final Item SWEET_BERRIES;
    public static final Item CAMPFIRE;
    public static final Item SOUL_CAMPFIRE;
    public static final Item SHROOMLIGHT;
    public static final Item HONEYCOMB;
    public static final Item BEE_NEST;
    public static final Item BEEHIVE;
    public static final Item HONEY_BOTTLE;
    public static final Item HONEY_BLOCK;
    public static final Item HONEYCOMB_BLOCK;
    public static final Item LODESTONE;
    public static final Item NETHERITE_BLOCK;
    public static final Item ANCIENT_DEBRIS;
    public static final Item TARGET;
    public static final Item CRYING_OBSIDIAN;
    public static final Item BLACKSTONE;
    public static final Item BLACKSTONE_SLAB;
    public static final Item BLACKSTONE_STAIRS;
    public static final Item GILDED_BLACKSTONE;
    public static final Item POLISHED_BLACKSTONE;
    public static final Item POLISHED_BLACKSTONE_SLAB;
    public static final Item POLISHED_BLACKSTONE_STAIRS;
    public static final Item CHISELED_POLISHED_BLACKSTONE;
    public static final Item POLISHED_BLACKSTONE_BRICKS;
    public static final Item POLISHED_BLACKSTONE_BRICK_SLAB;
    public static final Item POLISHED_BLACKSTONE_BRICK_STAIRS;
    public static final Item CRACKED_POLISHED_BLACKSTONE_BRICKS;
    public static final Item RESPAWN_ANCHOR;
    
    private static Item registerBlock(final Block bul) {
        return registerBlock(new BlockItem(bul, new Item.Properties()));
    }
    
    private static Item registerBlock(final Block bul, final CreativeModeTab bkp) {
        return registerBlock(new BlockItem(bul, new Item.Properties().tab(bkp)));
    }
    
    private static Item registerBlock(final BlockItem bke) {
        return registerBlock(bke.getBlock(), bke);
    }
    
    protected static Item registerBlock(final Block bul, final Item blu) {
        return registerItem(Registry.BLOCK.getKey(bul), blu);
    }
    
    private static Item registerItem(final String string, final Item blu) {
        return registerItem(new ResourceLocation(string), blu);
    }
    
    private static Item registerItem(final ResourceLocation vk, final Item blu) {
        if (blu instanceof BlockItem) {
            ((BlockItem)blu).registerBlocks(Item.BY_BLOCK, blu);
        }
        return Registry.<Item, Item>register(Registry.ITEM, vk, blu);
    }
    
    static {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: new             Lnet/minecraft/world/item/AirItem;
        //     6: dup            
        //     7: getstatic       net/minecraft/world/level/block/Blocks.AIR:Lnet/minecraft/world/level/block/Block;
        //    10: new             Lnet/minecraft/world/item/Item$Properties;
        //    13: dup            
        //    14: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //    17: invokespecial   net/minecraft/world/item/AirItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //    20: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //    23: putstatic       net/minecraft/world/item/Items.AIR:Lnet/minecraft/world/item/Item;
        //    26: getstatic       net/minecraft/world/level/block/Blocks.STONE:Lnet/minecraft/world/level/block/Block;
        //    29: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //    32: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //    35: putstatic       net/minecraft/world/item/Items.STONE:Lnet/minecraft/world/item/Item;
        //    38: getstatic       net/minecraft/world/level/block/Blocks.GRANITE:Lnet/minecraft/world/level/block/Block;
        //    41: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //    44: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //    47: putstatic       net/minecraft/world/item/Items.GRANITE:Lnet/minecraft/world/item/Item;
        //    50: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_GRANITE:Lnet/minecraft/world/level/block/Block;
        //    53: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //    56: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //    59: putstatic       net/minecraft/world/item/Items.POLISHED_GRANITE:Lnet/minecraft/world/item/Item;
        //    62: getstatic       net/minecraft/world/level/block/Blocks.DIORITE:Lnet/minecraft/world/level/block/Block;
        //    65: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //    68: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //    71: putstatic       net/minecraft/world/item/Items.DIORITE:Lnet/minecraft/world/item/Item;
        //    74: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_DIORITE:Lnet/minecraft/world/level/block/Block;
        //    77: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //    80: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //    83: putstatic       net/minecraft/world/item/Items.POLISHED_DIORITE:Lnet/minecraft/world/item/Item;
        //    86: getstatic       net/minecraft/world/level/block/Blocks.ANDESITE:Lnet/minecraft/world/level/block/Block;
        //    89: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //    92: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //    95: putstatic       net/minecraft/world/item/Items.ANDESITE:Lnet/minecraft/world/item/Item;
        //    98: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_ANDESITE:Lnet/minecraft/world/level/block/Block;
        //   101: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   104: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   107: putstatic       net/minecraft/world/item/Items.POLISHED_ANDESITE:Lnet/minecraft/world/item/Item;
        //   110: getstatic       net/minecraft/world/level/block/Blocks.GRASS_BLOCK:Lnet/minecraft/world/level/block/Block;
        //   113: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   116: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   119: putstatic       net/minecraft/world/item/Items.GRASS_BLOCK:Lnet/minecraft/world/item/Item;
        //   122: getstatic       net/minecraft/world/level/block/Blocks.DIRT:Lnet/minecraft/world/level/block/Block;
        //   125: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   128: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   131: putstatic       net/minecraft/world/item/Items.DIRT:Lnet/minecraft/world/item/Item;
        //   134: getstatic       net/minecraft/world/level/block/Blocks.COARSE_DIRT:Lnet/minecraft/world/level/block/Block;
        //   137: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   140: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   143: putstatic       net/minecraft/world/item/Items.COARSE_DIRT:Lnet/minecraft/world/item/Item;
        //   146: getstatic       net/minecraft/world/level/block/Blocks.PODZOL:Lnet/minecraft/world/level/block/Block;
        //   149: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   152: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   155: putstatic       net/minecraft/world/item/Items.PODZOL:Lnet/minecraft/world/item/Item;
        //   158: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_NYLIUM:Lnet/minecraft/world/level/block/Block;
        //   161: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   164: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   167: putstatic       net/minecraft/world/item/Items.CRIMSON_NYLIUM:Lnet/minecraft/world/item/Item;
        //   170: getstatic       net/minecraft/world/level/block/Blocks.WARPED_NYLIUM:Lnet/minecraft/world/level/block/Block;
        //   173: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   176: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   179: putstatic       net/minecraft/world/item/Items.WARPED_NYLIUM:Lnet/minecraft/world/item/Item;
        //   182: getstatic       net/minecraft/world/level/block/Blocks.COBBLESTONE:Lnet/minecraft/world/level/block/Block;
        //   185: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   188: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   191: putstatic       net/minecraft/world/item/Items.COBBLESTONE:Lnet/minecraft/world/item/Item;
        //   194: getstatic       net/minecraft/world/level/block/Blocks.OAK_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   197: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   200: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   203: putstatic       net/minecraft/world/item/Items.OAK_PLANKS:Lnet/minecraft/world/item/Item;
        //   206: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   209: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   212: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   215: putstatic       net/minecraft/world/item/Items.SPRUCE_PLANKS:Lnet/minecraft/world/item/Item;
        //   218: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   221: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   224: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   227: putstatic       net/minecraft/world/item/Items.BIRCH_PLANKS:Lnet/minecraft/world/item/Item;
        //   230: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   233: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   236: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   239: putstatic       net/minecraft/world/item/Items.JUNGLE_PLANKS:Lnet/minecraft/world/item/Item;
        //   242: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   245: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   248: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   251: putstatic       net/minecraft/world/item/Items.ACACIA_PLANKS:Lnet/minecraft/world/item/Item;
        //   254: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   257: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   260: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   263: putstatic       net/minecraft/world/item/Items.DARK_OAK_PLANKS:Lnet/minecraft/world/item/Item;
        //   266: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   269: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   272: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   275: putstatic       net/minecraft/world/item/Items.CRIMSON_PLANKS:Lnet/minecraft/world/item/Item;
        //   278: getstatic       net/minecraft/world/level/block/Blocks.WARPED_PLANKS:Lnet/minecraft/world/level/block/Block;
        //   281: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   284: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   287: putstatic       net/minecraft/world/item/Items.WARPED_PLANKS:Lnet/minecraft/world/item/Item;
        //   290: getstatic       net/minecraft/world/level/block/Blocks.OAK_SAPLING:Lnet/minecraft/world/level/block/Block;
        //   293: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   296: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   299: putstatic       net/minecraft/world/item/Items.OAK_SAPLING:Lnet/minecraft/world/item/Item;
        //   302: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_SAPLING:Lnet/minecraft/world/level/block/Block;
        //   305: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   308: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   311: putstatic       net/minecraft/world/item/Items.SPRUCE_SAPLING:Lnet/minecraft/world/item/Item;
        //   314: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_SAPLING:Lnet/minecraft/world/level/block/Block;
        //   317: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   320: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   323: putstatic       net/minecraft/world/item/Items.BIRCH_SAPLING:Lnet/minecraft/world/item/Item;
        //   326: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_SAPLING:Lnet/minecraft/world/level/block/Block;
        //   329: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   332: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   335: putstatic       net/minecraft/world/item/Items.JUNGLE_SAPLING:Lnet/minecraft/world/item/Item;
        //   338: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_SAPLING:Lnet/minecraft/world/level/block/Block;
        //   341: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   344: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   347: putstatic       net/minecraft/world/item/Items.ACACIA_SAPLING:Lnet/minecraft/world/item/Item;
        //   350: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_SAPLING:Lnet/minecraft/world/level/block/Block;
        //   353: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   356: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   359: putstatic       net/minecraft/world/item/Items.DARK_OAK_SAPLING:Lnet/minecraft/world/item/Item;
        //   362: getstatic       net/minecraft/world/level/block/Blocks.BEDROCK:Lnet/minecraft/world/level/block/Block;
        //   365: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   368: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   371: putstatic       net/minecraft/world/item/Items.BEDROCK:Lnet/minecraft/world/item/Item;
        //   374: getstatic       net/minecraft/world/level/block/Blocks.SAND:Lnet/minecraft/world/level/block/Block;
        //   377: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   380: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   383: putstatic       net/minecraft/world/item/Items.SAND:Lnet/minecraft/world/item/Item;
        //   386: getstatic       net/minecraft/world/level/block/Blocks.RED_SAND:Lnet/minecraft/world/level/block/Block;
        //   389: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   392: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   395: putstatic       net/minecraft/world/item/Items.RED_SAND:Lnet/minecraft/world/item/Item;
        //   398: getstatic       net/minecraft/world/level/block/Blocks.GRAVEL:Lnet/minecraft/world/level/block/Block;
        //   401: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   404: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   407: putstatic       net/minecraft/world/item/Items.GRAVEL:Lnet/minecraft/world/item/Item;
        //   410: getstatic       net/minecraft/world/level/block/Blocks.GOLD_ORE:Lnet/minecraft/world/level/block/Block;
        //   413: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   416: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   419: putstatic       net/minecraft/world/item/Items.GOLD_ORE:Lnet/minecraft/world/item/Item;
        //   422: getstatic       net/minecraft/world/level/block/Blocks.IRON_ORE:Lnet/minecraft/world/level/block/Block;
        //   425: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   428: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   431: putstatic       net/minecraft/world/item/Items.IRON_ORE:Lnet/minecraft/world/item/Item;
        //   434: getstatic       net/minecraft/world/level/block/Blocks.COAL_ORE:Lnet/minecraft/world/level/block/Block;
        //   437: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   440: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   443: putstatic       net/minecraft/world/item/Items.COAL_ORE:Lnet/minecraft/world/item/Item;
        //   446: getstatic       net/minecraft/world/level/block/Blocks.NETHER_GOLD_ORE:Lnet/minecraft/world/level/block/Block;
        //   449: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   452: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   455: putstatic       net/minecraft/world/item/Items.NETHER_GOLD_ORE:Lnet/minecraft/world/item/Item;
        //   458: getstatic       net/minecraft/world/level/block/Blocks.OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //   461: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   464: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   467: putstatic       net/minecraft/world/item/Items.OAK_LOG:Lnet/minecraft/world/item/Item;
        //   470: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_LOG:Lnet/minecraft/world/level/block/Block;
        //   473: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   476: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   479: putstatic       net/minecraft/world/item/Items.SPRUCE_LOG:Lnet/minecraft/world/item/Item;
        //   482: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_LOG:Lnet/minecraft/world/level/block/Block;
        //   485: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   488: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   491: putstatic       net/minecraft/world/item/Items.BIRCH_LOG:Lnet/minecraft/world/item/Item;
        //   494: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_LOG:Lnet/minecraft/world/level/block/Block;
        //   497: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   500: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   503: putstatic       net/minecraft/world/item/Items.JUNGLE_LOG:Lnet/minecraft/world/item/Item;
        //   506: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_LOG:Lnet/minecraft/world/level/block/Block;
        //   509: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   512: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   515: putstatic       net/minecraft/world/item/Items.ACACIA_LOG:Lnet/minecraft/world/item/Item;
        //   518: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //   521: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   524: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   527: putstatic       net/minecraft/world/item/Items.DARK_OAK_LOG:Lnet/minecraft/world/item/Item;
        //   530: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_STEM:Lnet/minecraft/world/level/block/Block;
        //   533: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   536: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   539: putstatic       net/minecraft/world/item/Items.CRIMSON_STEM:Lnet/minecraft/world/item/Item;
        //   542: getstatic       net/minecraft/world/level/block/Blocks.WARPED_STEM:Lnet/minecraft/world/level/block/Block;
        //   545: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   548: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   551: putstatic       net/minecraft/world/item/Items.WARPED_STEM:Lnet/minecraft/world/item/Item;
        //   554: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //   557: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   560: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   563: putstatic       net/minecraft/world/item/Items.STRIPPED_OAK_LOG:Lnet/minecraft/world/item/Item;
        //   566: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_SPRUCE_LOG:Lnet/minecraft/world/level/block/Block;
        //   569: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   572: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   575: putstatic       net/minecraft/world/item/Items.STRIPPED_SPRUCE_LOG:Lnet/minecraft/world/item/Item;
        //   578: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_BIRCH_LOG:Lnet/minecraft/world/level/block/Block;
        //   581: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   584: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   587: putstatic       net/minecraft/world/item/Items.STRIPPED_BIRCH_LOG:Lnet/minecraft/world/item/Item;
        //   590: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_JUNGLE_LOG:Lnet/minecraft/world/level/block/Block;
        //   593: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   596: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   599: putstatic       net/minecraft/world/item/Items.STRIPPED_JUNGLE_LOG:Lnet/minecraft/world/item/Item;
        //   602: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_ACACIA_LOG:Lnet/minecraft/world/level/block/Block;
        //   605: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   608: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   611: putstatic       net/minecraft/world/item/Items.STRIPPED_ACACIA_LOG:Lnet/minecraft/world/item/Item;
        //   614: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_DARK_OAK_LOG:Lnet/minecraft/world/level/block/Block;
        //   617: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   620: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   623: putstatic       net/minecraft/world/item/Items.STRIPPED_DARK_OAK_LOG:Lnet/minecraft/world/item/Item;
        //   626: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_CRIMSON_STEM:Lnet/minecraft/world/level/block/Block;
        //   629: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   632: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   635: putstatic       net/minecraft/world/item/Items.STRIPPED_CRIMSON_STEM:Lnet/minecraft/world/item/Item;
        //   638: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_WARPED_STEM:Lnet/minecraft/world/level/block/Block;
        //   641: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   644: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   647: putstatic       net/minecraft/world/item/Items.STRIPPED_WARPED_STEM:Lnet/minecraft/world/item/Item;
        //   650: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_OAK_WOOD:Lnet/minecraft/world/level/block/Block;
        //   653: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   656: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   659: putstatic       net/minecraft/world/item/Items.STRIPPED_OAK_WOOD:Lnet/minecraft/world/item/Item;
        //   662: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_SPRUCE_WOOD:Lnet/minecraft/world/level/block/Block;
        //   665: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   668: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   671: putstatic       net/minecraft/world/item/Items.STRIPPED_SPRUCE_WOOD:Lnet/minecraft/world/item/Item;
        //   674: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_BIRCH_WOOD:Lnet/minecraft/world/level/block/Block;
        //   677: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   680: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   683: putstatic       net/minecraft/world/item/Items.STRIPPED_BIRCH_WOOD:Lnet/minecraft/world/item/Item;
        //   686: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_JUNGLE_WOOD:Lnet/minecraft/world/level/block/Block;
        //   689: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   692: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   695: putstatic       net/minecraft/world/item/Items.STRIPPED_JUNGLE_WOOD:Lnet/minecraft/world/item/Item;
        //   698: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_ACACIA_WOOD:Lnet/minecraft/world/level/block/Block;
        //   701: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   704: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   707: putstatic       net/minecraft/world/item/Items.STRIPPED_ACACIA_WOOD:Lnet/minecraft/world/item/Item;
        //   710: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_DARK_OAK_WOOD:Lnet/minecraft/world/level/block/Block;
        //   713: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   716: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   719: putstatic       net/minecraft/world/item/Items.STRIPPED_DARK_OAK_WOOD:Lnet/minecraft/world/item/Item;
        //   722: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_CRIMSON_HYPHAE:Lnet/minecraft/world/level/block/Block;
        //   725: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   728: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   731: putstatic       net/minecraft/world/item/Items.STRIPPED_CRIMSON_HYPHAE:Lnet/minecraft/world/item/Item;
        //   734: getstatic       net/minecraft/world/level/block/Blocks.STRIPPED_WARPED_HYPHAE:Lnet/minecraft/world/level/block/Block;
        //   737: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   740: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   743: putstatic       net/minecraft/world/item/Items.STRIPPED_WARPED_HYPHAE:Lnet/minecraft/world/item/Item;
        //   746: getstatic       net/minecraft/world/level/block/Blocks.OAK_WOOD:Lnet/minecraft/world/level/block/Block;
        //   749: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   752: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   755: putstatic       net/minecraft/world/item/Items.OAK_WOOD:Lnet/minecraft/world/item/Item;
        //   758: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_WOOD:Lnet/minecraft/world/level/block/Block;
        //   761: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   764: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   767: putstatic       net/minecraft/world/item/Items.SPRUCE_WOOD:Lnet/minecraft/world/item/Item;
        //   770: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_WOOD:Lnet/minecraft/world/level/block/Block;
        //   773: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   776: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   779: putstatic       net/minecraft/world/item/Items.BIRCH_WOOD:Lnet/minecraft/world/item/Item;
        //   782: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_WOOD:Lnet/minecraft/world/level/block/Block;
        //   785: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   788: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   791: putstatic       net/minecraft/world/item/Items.JUNGLE_WOOD:Lnet/minecraft/world/item/Item;
        //   794: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_WOOD:Lnet/minecraft/world/level/block/Block;
        //   797: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   800: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   803: putstatic       net/minecraft/world/item/Items.ACACIA_WOOD:Lnet/minecraft/world/item/Item;
        //   806: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_WOOD:Lnet/minecraft/world/level/block/Block;
        //   809: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   812: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   815: putstatic       net/minecraft/world/item/Items.DARK_OAK_WOOD:Lnet/minecraft/world/item/Item;
        //   818: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_HYPHAE:Lnet/minecraft/world/level/block/Block;
        //   821: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   824: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   827: putstatic       net/minecraft/world/item/Items.CRIMSON_HYPHAE:Lnet/minecraft/world/item/Item;
        //   830: getstatic       net/minecraft/world/level/block/Blocks.WARPED_HYPHAE:Lnet/minecraft/world/level/block/Block;
        //   833: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   836: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   839: putstatic       net/minecraft/world/item/Items.WARPED_HYPHAE:Lnet/minecraft/world/item/Item;
        //   842: getstatic       net/minecraft/world/level/block/Blocks.OAK_LEAVES:Lnet/minecraft/world/level/block/Block;
        //   845: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   848: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   851: putstatic       net/minecraft/world/item/Items.OAK_LEAVES:Lnet/minecraft/world/item/Item;
        //   854: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_LEAVES:Lnet/minecraft/world/level/block/Block;
        //   857: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   860: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   863: putstatic       net/minecraft/world/item/Items.SPRUCE_LEAVES:Lnet/minecraft/world/item/Item;
        //   866: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_LEAVES:Lnet/minecraft/world/level/block/Block;
        //   869: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   872: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   875: putstatic       net/minecraft/world/item/Items.BIRCH_LEAVES:Lnet/minecraft/world/item/Item;
        //   878: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_LEAVES:Lnet/minecraft/world/level/block/Block;
        //   881: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   884: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   887: putstatic       net/minecraft/world/item/Items.JUNGLE_LEAVES:Lnet/minecraft/world/item/Item;
        //   890: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_LEAVES:Lnet/minecraft/world/level/block/Block;
        //   893: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   896: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   899: putstatic       net/minecraft/world/item/Items.ACACIA_LEAVES:Lnet/minecraft/world/item/Item;
        //   902: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_LEAVES:Lnet/minecraft/world/level/block/Block;
        //   905: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //   908: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   911: putstatic       net/minecraft/world/item/Items.DARK_OAK_LEAVES:Lnet/minecraft/world/item/Item;
        //   914: getstatic       net/minecraft/world/level/block/Blocks.SPONGE:Lnet/minecraft/world/level/block/Block;
        //   917: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   920: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   923: putstatic       net/minecraft/world/item/Items.SPONGE:Lnet/minecraft/world/item/Item;
        //   926: getstatic       net/minecraft/world/level/block/Blocks.WET_SPONGE:Lnet/minecraft/world/level/block/Block;
        //   929: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   932: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   935: putstatic       net/minecraft/world/item/Items.WET_SPONGE:Lnet/minecraft/world/item/Item;
        //   938: getstatic       net/minecraft/world/level/block/Blocks.GLASS:Lnet/minecraft/world/level/block/Block;
        //   941: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   944: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   947: putstatic       net/minecraft/world/item/Items.GLASS:Lnet/minecraft/world/item/Item;
        //   950: getstatic       net/minecraft/world/level/block/Blocks.LAPIS_ORE:Lnet/minecraft/world/level/block/Block;
        //   953: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   956: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   959: putstatic       net/minecraft/world/item/Items.LAPIS_ORE:Lnet/minecraft/world/item/Item;
        //   962: getstatic       net/minecraft/world/level/block/Blocks.LAPIS_BLOCK:Lnet/minecraft/world/level/block/Block;
        //   965: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   968: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   971: putstatic       net/minecraft/world/item/Items.LAPIS_BLOCK:Lnet/minecraft/world/item/Item;
        //   974: getstatic       net/minecraft/world/level/block/Blocks.DISPENSER:Lnet/minecraft/world/level/block/Block;
        //   977: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //   980: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   983: putstatic       net/minecraft/world/item/Items.DISPENSER:Lnet/minecraft/world/item/Item;
        //   986: getstatic       net/minecraft/world/level/block/Blocks.SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //   989: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //   992: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //   995: putstatic       net/minecraft/world/item/Items.SANDSTONE:Lnet/minecraft/world/item/Item;
        //   998: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  1001: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1004: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1007: putstatic       net/minecraft/world/item/Items.CHISELED_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  1010: getstatic       net/minecraft/world/level/block/Blocks.CUT_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  1013: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1016: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1019: putstatic       net/minecraft/world/item/Items.CUT_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  1022: getstatic       net/minecraft/world/level/block/Blocks.NOTE_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  1025: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  1028: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1031: putstatic       net/minecraft/world/item/Items.NOTE_BLOCK:Lnet/minecraft/world/item/Item;
        //  1034: getstatic       net/minecraft/world/level/block/Blocks.POWERED_RAIL:Lnet/minecraft/world/level/block/Block;
        //  1037: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        //  1040: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1043: putstatic       net/minecraft/world/item/Items.POWERED_RAIL:Lnet/minecraft/world/item/Item;
        //  1046: getstatic       net/minecraft/world/level/block/Blocks.DETECTOR_RAIL:Lnet/minecraft/world/level/block/Block;
        //  1049: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        //  1052: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1055: putstatic       net/minecraft/world/item/Items.DETECTOR_RAIL:Lnet/minecraft/world/item/Item;
        //  1058: getstatic       net/minecraft/world/level/block/Blocks.STICKY_PISTON:Lnet/minecraft/world/level/block/Block;
        //  1061: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  1064: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1067: putstatic       net/minecraft/world/item/Items.STICKY_PISTON:Lnet/minecraft/world/item/Item;
        //  1070: getstatic       net/minecraft/world/level/block/Blocks.COBWEB:Lnet/minecraft/world/level/block/Block;
        //  1073: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1076: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1079: putstatic       net/minecraft/world/item/Items.COBWEB:Lnet/minecraft/world/item/Item;
        //  1082: getstatic       net/minecraft/world/level/block/Blocks.GRASS:Lnet/minecraft/world/level/block/Block;
        //  1085: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1088: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1091: putstatic       net/minecraft/world/item/Items.GRASS:Lnet/minecraft/world/item/Item;
        //  1094: getstatic       net/minecraft/world/level/block/Blocks.FERN:Lnet/minecraft/world/level/block/Block;
        //  1097: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1100: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1103: putstatic       net/minecraft/world/item/Items.FERN:Lnet/minecraft/world/item/Item;
        //  1106: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BUSH:Lnet/minecraft/world/level/block/Block;
        //  1109: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1112: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1115: putstatic       net/minecraft/world/item/Items.DEAD_BUSH:Lnet/minecraft/world/item/Item;
        //  1118: getstatic       net/minecraft/world/level/block/Blocks.SEAGRASS:Lnet/minecraft/world/level/block/Block;
        //  1121: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1124: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1127: putstatic       net/minecraft/world/item/Items.SEAGRASS:Lnet/minecraft/world/item/Item;
        //  1130: getstatic       net/minecraft/world/level/block/Blocks.SEA_PICKLE:Lnet/minecraft/world/level/block/Block;
        //  1133: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1136: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1139: putstatic       net/minecraft/world/item/Items.SEA_PICKLE:Lnet/minecraft/world/item/Item;
        //  1142: getstatic       net/minecraft/world/level/block/Blocks.PISTON:Lnet/minecraft/world/level/block/Block;
        //  1145: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  1148: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1151: putstatic       net/minecraft/world/item/Items.PISTON:Lnet/minecraft/world/item/Item;
        //  1154: getstatic       net/minecraft/world/level/block/Blocks.WHITE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1157: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1160: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1163: putstatic       net/minecraft/world/item/Items.WHITE_WOOL:Lnet/minecraft/world/item/Item;
        //  1166: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1169: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1172: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1175: putstatic       net/minecraft/world/item/Items.ORANGE_WOOL:Lnet/minecraft/world/item/Item;
        //  1178: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1181: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1184: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1187: putstatic       net/minecraft/world/item/Items.MAGENTA_WOOL:Lnet/minecraft/world/item/Item;
        //  1190: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1193: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1196: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1199: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_WOOL:Lnet/minecraft/world/item/Item;
        //  1202: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1205: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1208: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1211: putstatic       net/minecraft/world/item/Items.YELLOW_WOOL:Lnet/minecraft/world/item/Item;
        //  1214: getstatic       net/minecraft/world/level/block/Blocks.LIME_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1217: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1220: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1223: putstatic       net/minecraft/world/item/Items.LIME_WOOL:Lnet/minecraft/world/item/Item;
        //  1226: getstatic       net/minecraft/world/level/block/Blocks.PINK_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1229: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1232: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1235: putstatic       net/minecraft/world/item/Items.PINK_WOOL:Lnet/minecraft/world/item/Item;
        //  1238: getstatic       net/minecraft/world/level/block/Blocks.GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1241: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1244: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1247: putstatic       net/minecraft/world/item/Items.GRAY_WOOL:Lnet/minecraft/world/item/Item;
        //  1250: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1253: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1256: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1259: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_WOOL:Lnet/minecraft/world/item/Item;
        //  1262: getstatic       net/minecraft/world/level/block/Blocks.CYAN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1265: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1268: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1271: putstatic       net/minecraft/world/item/Items.CYAN_WOOL:Lnet/minecraft/world/item/Item;
        //  1274: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1277: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1280: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1283: putstatic       net/minecraft/world/item/Items.PURPLE_WOOL:Lnet/minecraft/world/item/Item;
        //  1286: getstatic       net/minecraft/world/level/block/Blocks.BLUE_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1289: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1292: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1295: putstatic       net/minecraft/world/item/Items.BLUE_WOOL:Lnet/minecraft/world/item/Item;
        //  1298: getstatic       net/minecraft/world/level/block/Blocks.BROWN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1301: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1304: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1307: putstatic       net/minecraft/world/item/Items.BROWN_WOOL:Lnet/minecraft/world/item/Item;
        //  1310: getstatic       net/minecraft/world/level/block/Blocks.GREEN_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1313: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1316: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1319: putstatic       net/minecraft/world/item/Items.GREEN_WOOL:Lnet/minecraft/world/item/Item;
        //  1322: getstatic       net/minecraft/world/level/block/Blocks.RED_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1325: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1328: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1331: putstatic       net/minecraft/world/item/Items.RED_WOOL:Lnet/minecraft/world/item/Item;
        //  1334: getstatic       net/minecraft/world/level/block/Blocks.BLACK_WOOL:Lnet/minecraft/world/level/block/Block;
        //  1337: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1340: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1343: putstatic       net/minecraft/world/item/Items.BLACK_WOOL:Lnet/minecraft/world/item/Item;
        //  1346: getstatic       net/minecraft/world/level/block/Blocks.DANDELION:Lnet/minecraft/world/level/block/Block;
        //  1349: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1352: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1355: putstatic       net/minecraft/world/item/Items.DANDELION:Lnet/minecraft/world/item/Item;
        //  1358: getstatic       net/minecraft/world/level/block/Blocks.POPPY:Lnet/minecraft/world/level/block/Block;
        //  1361: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1364: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1367: putstatic       net/minecraft/world/item/Items.POPPY:Lnet/minecraft/world/item/Item;
        //  1370: getstatic       net/minecraft/world/level/block/Blocks.BLUE_ORCHID:Lnet/minecraft/world/level/block/Block;
        //  1373: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1376: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1379: putstatic       net/minecraft/world/item/Items.BLUE_ORCHID:Lnet/minecraft/world/item/Item;
        //  1382: getstatic       net/minecraft/world/level/block/Blocks.ALLIUM:Lnet/minecraft/world/level/block/Block;
        //  1385: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1388: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1391: putstatic       net/minecraft/world/item/Items.ALLIUM:Lnet/minecraft/world/item/Item;
        //  1394: getstatic       net/minecraft/world/level/block/Blocks.AZURE_BLUET:Lnet/minecraft/world/level/block/Block;
        //  1397: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1400: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1403: putstatic       net/minecraft/world/item/Items.AZURE_BLUET:Lnet/minecraft/world/item/Item;
        //  1406: getstatic       net/minecraft/world/level/block/Blocks.RED_TULIP:Lnet/minecraft/world/level/block/Block;
        //  1409: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1412: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1415: putstatic       net/minecraft/world/item/Items.RED_TULIP:Lnet/minecraft/world/item/Item;
        //  1418: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_TULIP:Lnet/minecraft/world/level/block/Block;
        //  1421: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1424: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1427: putstatic       net/minecraft/world/item/Items.ORANGE_TULIP:Lnet/minecraft/world/item/Item;
        //  1430: getstatic       net/minecraft/world/level/block/Blocks.WHITE_TULIP:Lnet/minecraft/world/level/block/Block;
        //  1433: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1436: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1439: putstatic       net/minecraft/world/item/Items.WHITE_TULIP:Lnet/minecraft/world/item/Item;
        //  1442: getstatic       net/minecraft/world/level/block/Blocks.PINK_TULIP:Lnet/minecraft/world/level/block/Block;
        //  1445: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1448: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1451: putstatic       net/minecraft/world/item/Items.PINK_TULIP:Lnet/minecraft/world/item/Item;
        //  1454: getstatic       net/minecraft/world/level/block/Blocks.OXEYE_DAISY:Lnet/minecraft/world/level/block/Block;
        //  1457: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1460: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1463: putstatic       net/minecraft/world/item/Items.OXEYE_DAISY:Lnet/minecraft/world/item/Item;
        //  1466: getstatic       net/minecraft/world/level/block/Blocks.CORNFLOWER:Lnet/minecraft/world/level/block/Block;
        //  1469: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1472: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1475: putstatic       net/minecraft/world/item/Items.CORNFLOWER:Lnet/minecraft/world/item/Item;
        //  1478: getstatic       net/minecraft/world/level/block/Blocks.LILY_OF_THE_VALLEY:Lnet/minecraft/world/level/block/Block;
        //  1481: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1484: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1487: putstatic       net/minecraft/world/item/Items.LILY_OF_THE_VALLEY:Lnet/minecraft/world/item/Item;
        //  1490: getstatic       net/minecraft/world/level/block/Blocks.WITHER_ROSE:Lnet/minecraft/world/level/block/Block;
        //  1493: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1496: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1499: putstatic       net/minecraft/world/item/Items.WITHER_ROSE:Lnet/minecraft/world/item/Item;
        //  1502: getstatic       net/minecraft/world/level/block/Blocks.BROWN_MUSHROOM:Lnet/minecraft/world/level/block/Block;
        //  1505: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1508: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1511: putstatic       net/minecraft/world/item/Items.BROWN_MUSHROOM:Lnet/minecraft/world/item/Item;
        //  1514: getstatic       net/minecraft/world/level/block/Blocks.RED_MUSHROOM:Lnet/minecraft/world/level/block/Block;
        //  1517: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1520: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1523: putstatic       net/minecraft/world/item/Items.RED_MUSHROOM:Lnet/minecraft/world/item/Item;
        //  1526: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_FUNGUS:Lnet/minecraft/world/level/block/Block;
        //  1529: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1532: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1535: putstatic       net/minecraft/world/item/Items.CRIMSON_FUNGUS:Lnet/minecraft/world/item/Item;
        //  1538: getstatic       net/minecraft/world/level/block/Blocks.WARPED_FUNGUS:Lnet/minecraft/world/level/block/Block;
        //  1541: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1544: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1547: putstatic       net/minecraft/world/item/Items.WARPED_FUNGUS:Lnet/minecraft/world/item/Item;
        //  1550: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_ROOTS:Lnet/minecraft/world/level/block/Block;
        //  1553: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1556: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1559: putstatic       net/minecraft/world/item/Items.CRIMSON_ROOTS:Lnet/minecraft/world/item/Item;
        //  1562: getstatic       net/minecraft/world/level/block/Blocks.WARPED_ROOTS:Lnet/minecraft/world/level/block/Block;
        //  1565: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1568: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1571: putstatic       net/minecraft/world/item/Items.WARPED_ROOTS:Lnet/minecraft/world/item/Item;
        //  1574: getstatic       net/minecraft/world/level/block/Blocks.NETHER_SPROUTS:Lnet/minecraft/world/level/block/Block;
        //  1577: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1580: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1583: putstatic       net/minecraft/world/item/Items.NETHER_SPROUTS:Lnet/minecraft/world/item/Item;
        //  1586: getstatic       net/minecraft/world/level/block/Blocks.WEEPING_VINES:Lnet/minecraft/world/level/block/Block;
        //  1589: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1592: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1595: putstatic       net/minecraft/world/item/Items.WEEPING_VINES:Lnet/minecraft/world/item/Item;
        //  1598: getstatic       net/minecraft/world/level/block/Blocks.TWISTING_VINES:Lnet/minecraft/world/level/block/Block;
        //  1601: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1604: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1607: putstatic       net/minecraft/world/item/Items.TWISTING_VINES:Lnet/minecraft/world/item/Item;
        //  1610: getstatic       net/minecraft/world/level/block/Blocks.SUGAR_CANE:Lnet/minecraft/world/level/block/Block;
        //  1613: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1616: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1619: putstatic       net/minecraft/world/item/Items.SUGAR_CANE:Lnet/minecraft/world/item/Item;
        //  1622: getstatic       net/minecraft/world/level/block/Blocks.KELP:Lnet/minecraft/world/level/block/Block;
        //  1625: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1628: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1631: putstatic       net/minecraft/world/item/Items.KELP:Lnet/minecraft/world/item/Item;
        //  1634: getstatic       net/minecraft/world/level/block/Blocks.BAMBOO:Lnet/minecraft/world/level/block/Block;
        //  1637: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1640: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1643: putstatic       net/minecraft/world/item/Items.BAMBOO:Lnet/minecraft/world/item/Item;
        //  1646: getstatic       net/minecraft/world/level/block/Blocks.GOLD_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  1649: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1652: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1655: putstatic       net/minecraft/world/item/Items.GOLD_BLOCK:Lnet/minecraft/world/item/Item;
        //  1658: getstatic       net/minecraft/world/level/block/Blocks.IRON_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  1661: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1664: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1667: putstatic       net/minecraft/world/item/Items.IRON_BLOCK:Lnet/minecraft/world/item/Item;
        //  1670: getstatic       net/minecraft/world/level/block/Blocks.OAK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1673: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1676: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1679: putstatic       net/minecraft/world/item/Items.OAK_SLAB:Lnet/minecraft/world/item/Item;
        //  1682: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1685: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1688: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1691: putstatic       net/minecraft/world/item/Items.SPRUCE_SLAB:Lnet/minecraft/world/item/Item;
        //  1694: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1697: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1700: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1703: putstatic       net/minecraft/world/item/Items.BIRCH_SLAB:Lnet/minecraft/world/item/Item;
        //  1706: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1709: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1712: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1715: putstatic       net/minecraft/world/item/Items.JUNGLE_SLAB:Lnet/minecraft/world/item/Item;
        //  1718: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1721: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1724: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1727: putstatic       net/minecraft/world/item/Items.ACACIA_SLAB:Lnet/minecraft/world/item/Item;
        //  1730: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1733: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1736: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1739: putstatic       net/minecraft/world/item/Items.DARK_OAK_SLAB:Lnet/minecraft/world/item/Item;
        //  1742: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1745: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1748: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1751: putstatic       net/minecraft/world/item/Items.CRIMSON_SLAB:Lnet/minecraft/world/item/Item;
        //  1754: getstatic       net/minecraft/world/level/block/Blocks.WARPED_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1757: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1760: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1763: putstatic       net/minecraft/world/item/Items.WARPED_SLAB:Lnet/minecraft/world/item/Item;
        //  1766: getstatic       net/minecraft/world/level/block/Blocks.STONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1769: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1772: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1775: putstatic       net/minecraft/world/item/Items.STONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1778: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_STONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1781: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1784: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1787: putstatic       net/minecraft/world/item/Items.SMOOTH_STONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1790: getstatic       net/minecraft/world/level/block/Blocks.SANDSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1793: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1796: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1799: putstatic       net/minecraft/world/item/Items.SANDSTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1802: getstatic       net/minecraft/world/level/block/Blocks.CUT_SANDSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1805: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1808: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1811: putstatic       net/minecraft/world/item/Items.CUT_STANDSTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1814: getstatic       net/minecraft/world/level/block/Blocks.PETRIFIED_OAK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1817: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1820: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1823: putstatic       net/minecraft/world/item/Items.PETRIFIED_OAK_SLAB:Lnet/minecraft/world/item/Item;
        //  1826: getstatic       net/minecraft/world/level/block/Blocks.COBBLESTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1829: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1832: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1835: putstatic       net/minecraft/world/item/Items.COBBLESTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1838: getstatic       net/minecraft/world/level/block/Blocks.BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1841: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1844: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1847: putstatic       net/minecraft/world/item/Items.BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  1850: getstatic       net/minecraft/world/level/block/Blocks.STONE_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1853: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1856: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1859: putstatic       net/minecraft/world/item/Items.STONE_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  1862: getstatic       net/minecraft/world/level/block/Blocks.NETHER_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1865: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1868: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1871: putstatic       net/minecraft/world/item/Items.NETHER_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  1874: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1877: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1880: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1883: putstatic       net/minecraft/world/item/Items.QUARTZ_SLAB:Lnet/minecraft/world/item/Item;
        //  1886: getstatic       net/minecraft/world/level/block/Blocks.RED_SANDSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1889: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1892: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1895: putstatic       net/minecraft/world/item/Items.RED_SANDSTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1898: getstatic       net/minecraft/world/level/block/Blocks.CUT_RED_SANDSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1901: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1904: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1907: putstatic       net/minecraft/world/item/Items.CUT_RED_SANDSTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  1910: getstatic       net/minecraft/world/level/block/Blocks.PURPUR_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1913: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1916: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1919: putstatic       net/minecraft/world/item/Items.PURPUR_SLAB:Lnet/minecraft/world/item/Item;
        //  1922: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1925: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1928: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1931: putstatic       net/minecraft/world/item/Items.PRISMARINE_SLAB:Lnet/minecraft/world/item/Item;
        //  1934: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1937: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1940: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1943: putstatic       net/minecraft/world/item/Items.PRISMARINE_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  1946: getstatic       net/minecraft/world/level/block/Blocks.DARK_PRISMARINE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  1949: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1952: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1955: putstatic       net/minecraft/world/item/Items.DARK_PRISMARINE_SLAB:Lnet/minecraft/world/item/Item;
        //  1958: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_QUARTZ:Lnet/minecraft/world/level/block/Block;
        //  1961: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1964: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1967: putstatic       net/minecraft/world/item/Items.SMOOTH_QUARTZ:Lnet/minecraft/world/item/Item;
        //  1970: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_RED_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  1973: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1976: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1979: putstatic       net/minecraft/world/item/Items.SMOOTH_RED_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  1982: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  1985: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  1988: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  1991: putstatic       net/minecraft/world/item/Items.SMOOTH_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  1994: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_STONE:Lnet/minecraft/world/level/block/Block;
        //  1997: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2000: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2003: putstatic       net/minecraft/world/item/Items.SMOOTH_STONE:Lnet/minecraft/world/item/Item;
        //  2006: getstatic       net/minecraft/world/level/block/Blocks.BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2009: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2012: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2015: putstatic       net/minecraft/world/item/Items.BRICKS:Lnet/minecraft/world/item/Item;
        //  2018: getstatic       net/minecraft/world/level/block/Blocks.TNT:Lnet/minecraft/world/level/block/Block;
        //  2021: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2024: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2027: putstatic       net/minecraft/world/item/Items.TNT:Lnet/minecraft/world/item/Item;
        //  2030: getstatic       net/minecraft/world/level/block/Blocks.BOOKSHELF:Lnet/minecraft/world/level/block/Block;
        //  2033: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2036: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2039: putstatic       net/minecraft/world/item/Items.BOOKSHELF:Lnet/minecraft/world/item/Item;
        //  2042: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_COBBLESTONE:Lnet/minecraft/world/level/block/Block;
        //  2045: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2048: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2051: putstatic       net/minecraft/world/item/Items.MOSSY_COBBLESTONE:Lnet/minecraft/world/item/Item;
        //  2054: getstatic       net/minecraft/world/level/block/Blocks.OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        //  2057: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2060: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2063: putstatic       net/minecraft/world/item/Items.OBSIDIAN:Lnet/minecraft/world/item/Item;
        //  2066: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  2069: dup            
        //  2070: getstatic       net/minecraft/world/level/block/Blocks.TORCH:Lnet/minecraft/world/level/block/Block;
        //  2073: getstatic       net/minecraft/world/level/block/Blocks.WALL_TORCH:Lnet/minecraft/world/level/block/Block;
        //  2076: new             Lnet/minecraft/world/item/Item$Properties;
        //  2079: dup            
        //  2080: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  2083: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2086: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  2089: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  2092: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  2095: putstatic       net/minecraft/world/item/Items.TORCH:Lnet/minecraft/world/item/Item;
        //  2098: getstatic       net/minecraft/world/level/block/Blocks.END_ROD:Lnet/minecraft/world/level/block/Block;
        //  2101: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2104: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2107: putstatic       net/minecraft/world/item/Items.END_ROD:Lnet/minecraft/world/item/Item;
        //  2110: getstatic       net/minecraft/world/level/block/Blocks.CHORUS_PLANT:Lnet/minecraft/world/level/block/Block;
        //  2113: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2116: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2119: putstatic       net/minecraft/world/item/Items.CHORUS_PLANT:Lnet/minecraft/world/item/Item;
        //  2122: getstatic       net/minecraft/world/level/block/Blocks.CHORUS_FLOWER:Lnet/minecraft/world/level/block/Block;
        //  2125: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2128: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2131: putstatic       net/minecraft/world/item/Items.CHORUS_FLOWER:Lnet/minecraft/world/item/Item;
        //  2134: getstatic       net/minecraft/world/level/block/Blocks.PURPUR_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2137: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2140: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2143: putstatic       net/minecraft/world/item/Items.PURPUR_BLOCK:Lnet/minecraft/world/item/Item;
        //  2146: getstatic       net/minecraft/world/level/block/Blocks.PURPUR_PILLAR:Lnet/minecraft/world/level/block/Block;
        //  2149: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2152: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2155: putstatic       net/minecraft/world/item/Items.PURPUR_PILLAR:Lnet/minecraft/world/item/Item;
        //  2158: getstatic       net/minecraft/world/level/block/Blocks.PURPUR_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  2161: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2164: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2167: putstatic       net/minecraft/world/item/Items.PURPUR_STAIRS:Lnet/minecraft/world/item/Item;
        //  2170: getstatic       net/minecraft/world/level/block/Blocks.SPAWNER:Lnet/minecraft/world/level/block/Block;
        //  2173: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/item/Item;
        //  2176: putstatic       net/minecraft/world/item/Items.SPAWNER:Lnet/minecraft/world/item/Item;
        //  2179: getstatic       net/minecraft/world/level/block/Blocks.OAK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  2182: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2185: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2188: putstatic       net/minecraft/world/item/Items.OAK_STAIRS:Lnet/minecraft/world/item/Item;
        //  2191: getstatic       net/minecraft/world/level/block/Blocks.CHEST:Lnet/minecraft/world/level/block/Block;
        //  2194: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2197: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2200: putstatic       net/minecraft/world/item/Items.CHEST:Lnet/minecraft/world/item/Item;
        //  2203: getstatic       net/minecraft/world/level/block/Blocks.DIAMOND_ORE:Lnet/minecraft/world/level/block/Block;
        //  2206: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2209: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2212: putstatic       net/minecraft/world/item/Items.DIAMOND_ORE:Lnet/minecraft/world/item/Item;
        //  2215: getstatic       net/minecraft/world/level/block/Blocks.DIAMOND_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2218: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2221: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2224: putstatic       net/minecraft/world/item/Items.DIAMOND_BLOCK:Lnet/minecraft/world/item/Item;
        //  2227: getstatic       net/minecraft/world/level/block/Blocks.CRAFTING_TABLE:Lnet/minecraft/world/level/block/Block;
        //  2230: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2233: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2236: putstatic       net/minecraft/world/item/Items.CRAFTING_TABLE:Lnet/minecraft/world/item/Item;
        //  2239: getstatic       net/minecraft/world/level/block/Blocks.FARMLAND:Lnet/minecraft/world/level/block/Block;
        //  2242: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2245: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2248: putstatic       net/minecraft/world/item/Items.FARMLAND:Lnet/minecraft/world/item/Item;
        //  2251: getstatic       net/minecraft/world/level/block/Blocks.FURNACE:Lnet/minecraft/world/level/block/Block;
        //  2254: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2257: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2260: putstatic       net/minecraft/world/item/Items.FURNACE:Lnet/minecraft/world/item/Item;
        //  2263: getstatic       net/minecraft/world/level/block/Blocks.LADDER:Lnet/minecraft/world/level/block/Block;
        //  2266: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2269: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2272: putstatic       net/minecraft/world/item/Items.LADDER:Lnet/minecraft/world/item/Item;
        //  2275: getstatic       net/minecraft/world/level/block/Blocks.RAIL:Lnet/minecraft/world/level/block/Block;
        //  2278: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        //  2281: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2284: putstatic       net/minecraft/world/item/Items.RAIL:Lnet/minecraft/world/item/Item;
        //  2287: getstatic       net/minecraft/world/level/block/Blocks.COBBLESTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  2290: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2293: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2296: putstatic       net/minecraft/world/item/Items.COBBLESTONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  2299: getstatic       net/minecraft/world/level/block/Blocks.LEVER:Lnet/minecraft/world/level/block/Block;
        //  2302: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2305: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2308: putstatic       net/minecraft/world/item/Items.LEVER:Lnet/minecraft/world/item/Item;
        //  2311: getstatic       net/minecraft/world/level/block/Blocks.STONE_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2314: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2317: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2320: putstatic       net/minecraft/world/item/Items.STONE_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2323: getstatic       net/minecraft/world/level/block/Blocks.OAK_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2326: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2329: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2332: putstatic       net/minecraft/world/item/Items.OAK_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2335: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2338: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2341: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2344: putstatic       net/minecraft/world/item/Items.SPRUCE_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2347: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2350: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2353: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2356: putstatic       net/minecraft/world/item/Items.BIRCH_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2359: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2362: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2365: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2368: putstatic       net/minecraft/world/item/Items.JUNGLE_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2371: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2374: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2377: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2380: putstatic       net/minecraft/world/item/Items.ACACIA_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2383: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2386: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2389: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2392: putstatic       net/minecraft/world/item/Items.DARK_OAK_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2395: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2398: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2401: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2404: putstatic       net/minecraft/world/item/Items.CRIMSON_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2407: getstatic       net/minecraft/world/level/block/Blocks.WARPED_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2410: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2413: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2416: putstatic       net/minecraft/world/item/Items.WARPED_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2419: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  2422: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2425: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2428: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  2431: getstatic       net/minecraft/world/level/block/Blocks.REDSTONE_ORE:Lnet/minecraft/world/level/block/Block;
        //  2434: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2437: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2440: putstatic       net/minecraft/world/item/Items.REDSTONE_ORE:Lnet/minecraft/world/item/Item;
        //  2443: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  2446: dup            
        //  2447: getstatic       net/minecraft/world/level/block/Blocks.REDSTONE_TORCH:Lnet/minecraft/world/level/block/Block;
        //  2450: getstatic       net/minecraft/world/level/block/Blocks.REDSTONE_WALL_TORCH:Lnet/minecraft/world/level/block/Block;
        //  2453: new             Lnet/minecraft/world/item/Item$Properties;
        //  2456: dup            
        //  2457: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  2460: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2463: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  2466: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  2469: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  2472: putstatic       net/minecraft/world/item/Items.REDSTONE_TORCH:Lnet/minecraft/world/item/Item;
        //  2475: getstatic       net/minecraft/world/level/block/Blocks.SNOW:Lnet/minecraft/world/level/block/Block;
        //  2478: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2481: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2484: putstatic       net/minecraft/world/item/Items.SNOW:Lnet/minecraft/world/item/Item;
        //  2487: getstatic       net/minecraft/world/level/block/Blocks.ICE:Lnet/minecraft/world/level/block/Block;
        //  2490: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2493: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2496: putstatic       net/minecraft/world/item/Items.ICE:Lnet/minecraft/world/item/Item;
        //  2499: getstatic       net/minecraft/world/level/block/Blocks.SNOW_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  2502: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2505: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2508: putstatic       net/minecraft/world/item/Items.SNOW_BLOCK:Lnet/minecraft/world/item/Item;
        //  2511: getstatic       net/minecraft/world/level/block/Blocks.CACTUS:Lnet/minecraft/world/level/block/Block;
        //  2514: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2517: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2520: putstatic       net/minecraft/world/item/Items.CACTUS:Lnet/minecraft/world/item/Item;
        //  2523: getstatic       net/minecraft/world/level/block/Blocks.CLAY:Lnet/minecraft/world/level/block/Block;
        //  2526: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2529: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2532: putstatic       net/minecraft/world/item/Items.CLAY:Lnet/minecraft/world/item/Item;
        //  2535: getstatic       net/minecraft/world/level/block/Blocks.JUKEBOX:Lnet/minecraft/world/level/block/Block;
        //  2538: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2541: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2544: putstatic       net/minecraft/world/item/Items.JUKEBOX:Lnet/minecraft/world/item/Item;
        //  2547: getstatic       net/minecraft/world/level/block/Blocks.OAK_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2550: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2553: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2556: putstatic       net/minecraft/world/item/Items.OAK_FENCE:Lnet/minecraft/world/item/Item;
        //  2559: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2562: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2565: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2568: putstatic       net/minecraft/world/item/Items.SPRUCE_FENCE:Lnet/minecraft/world/item/Item;
        //  2571: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2574: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2577: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2580: putstatic       net/minecraft/world/item/Items.BIRCH_FENCE:Lnet/minecraft/world/item/Item;
        //  2583: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2586: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2589: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2592: putstatic       net/minecraft/world/item/Items.JUNGLE_FENCE:Lnet/minecraft/world/item/Item;
        //  2595: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2598: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2601: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2604: putstatic       net/minecraft/world/item/Items.ACACIA_FENCE:Lnet/minecraft/world/item/Item;
        //  2607: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2610: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2613: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2616: putstatic       net/minecraft/world/item/Items.DARK_OAK_FENCE:Lnet/minecraft/world/item/Item;
        //  2619: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2622: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2625: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2628: putstatic       net/minecraft/world/item/Items.CRIMSON_FENCE:Lnet/minecraft/world/item/Item;
        //  2631: getstatic       net/minecraft/world/level/block/Blocks.WARPED_FENCE:Lnet/minecraft/world/level/block/Block;
        //  2634: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2637: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2640: putstatic       net/minecraft/world/item/Items.WARPED_FENCE:Lnet/minecraft/world/item/Item;
        //  2643: getstatic       net/minecraft/world/level/block/Blocks.PUMPKIN:Lnet/minecraft/world/level/block/Block;
        //  2646: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2649: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2652: putstatic       net/minecraft/world/item/Items.PUMPKIN:Lnet/minecraft/world/item/Item;
        //  2655: getstatic       net/minecraft/world/level/block/Blocks.CARVED_PUMPKIN:Lnet/minecraft/world/level/block/Block;
        //  2658: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2661: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2664: putstatic       net/minecraft/world/item/Items.CARVED_PUMPKIN:Lnet/minecraft/world/item/Item;
        //  2667: getstatic       net/minecraft/world/level/block/Blocks.NETHERRACK:Lnet/minecraft/world/level/block/Block;
        //  2670: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2673: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2676: putstatic       net/minecraft/world/item/Items.NETHERRACK:Lnet/minecraft/world/item/Item;
        //  2679: getstatic       net/minecraft/world/level/block/Blocks.SOUL_SAND:Lnet/minecraft/world/level/block/Block;
        //  2682: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2685: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2688: putstatic       net/minecraft/world/item/Items.SOUL_SAND:Lnet/minecraft/world/item/Item;
        //  2691: getstatic       net/minecraft/world/level/block/Blocks.SOUL_SOIL:Lnet/minecraft/world/level/block/Block;
        //  2694: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2697: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2700: putstatic       net/minecraft/world/item/Items.SOUL_SOIL:Lnet/minecraft/world/item/Item;
        //  2703: getstatic       net/minecraft/world/level/block/Blocks.BASALT:Lnet/minecraft/world/level/block/Block;
        //  2706: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2709: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2712: putstatic       net/minecraft/world/item/Items.BASALT:Lnet/minecraft/world/item/Item;
        //  2715: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BASALT:Lnet/minecraft/world/level/block/Block;
        //  2718: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2721: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2724: putstatic       net/minecraft/world/item/Items.POLISHED_BASALT:Lnet/minecraft/world/item/Item;
        //  2727: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  2730: dup            
        //  2731: getstatic       net/minecraft/world/level/block/Blocks.SOUL_TORCH:Lnet/minecraft/world/level/block/Block;
        //  2734: getstatic       net/minecraft/world/level/block/Blocks.SOUL_WALL_TORCH:Lnet/minecraft/world/level/block/Block;
        //  2737: new             Lnet/minecraft/world/item/Item$Properties;
        //  2740: dup            
        //  2741: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  2744: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2747: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  2750: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  2753: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  2756: putstatic       net/minecraft/world/item/Items.SOUL_TORCH:Lnet/minecraft/world/item/Item;
        //  2759: getstatic       net/minecraft/world/level/block/Blocks.GLOWSTONE:Lnet/minecraft/world/level/block/Block;
        //  2762: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2765: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2768: putstatic       net/minecraft/world/item/Items.GLOWSTONE:Lnet/minecraft/world/item/Item;
        //  2771: getstatic       net/minecraft/world/level/block/Blocks.JACK_O_LANTERN:Lnet/minecraft/world/level/block/Block;
        //  2774: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2777: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2780: putstatic       net/minecraft/world/item/Items.JACK_O_LANTERN:Lnet/minecraft/world/item/Item;
        //  2783: getstatic       net/minecraft/world/level/block/Blocks.OAK_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2786: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2789: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2792: putstatic       net/minecraft/world/item/Items.OAK_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2795: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2798: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2801: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2804: putstatic       net/minecraft/world/item/Items.SPRUCE_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2807: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2810: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2813: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2816: putstatic       net/minecraft/world/item/Items.BIRCH_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2819: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2822: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2825: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2828: putstatic       net/minecraft/world/item/Items.JUNGLE_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2831: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2834: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2837: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2840: putstatic       net/minecraft/world/item/Items.ACACIA_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2843: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2846: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2849: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2852: putstatic       net/minecraft/world/item/Items.DARK_OAK_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2855: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2858: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2861: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2864: putstatic       net/minecraft/world/item/Items.CRIMSON_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2867: getstatic       net/minecraft/world/level/block/Blocks.WARPED_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  2870: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  2873: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2876: putstatic       net/minecraft/world/item/Items.WARPED_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  2879: getstatic       net/minecraft/world/level/block/Blocks.INFESTED_STONE:Lnet/minecraft/world/level/block/Block;
        //  2882: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2885: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2888: putstatic       net/minecraft/world/item/Items.INFESTED_STONE:Lnet/minecraft/world/item/Item;
        //  2891: getstatic       net/minecraft/world/level/block/Blocks.INFESTED_COBBLESTONE:Lnet/minecraft/world/level/block/Block;
        //  2894: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2897: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2900: putstatic       net/minecraft/world/item/Items.INFESTED_COBBLESTONE:Lnet/minecraft/world/item/Item;
        //  2903: getstatic       net/minecraft/world/level/block/Blocks.INFESTED_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2906: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2909: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2912: putstatic       net/minecraft/world/item/Items.INFESTED_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2915: getstatic       net/minecraft/world/level/block/Blocks.INFESTED_MOSSY_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2918: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2921: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2924: putstatic       net/minecraft/world/item/Items.INFESTED_MOSSY_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2927: getstatic       net/minecraft/world/level/block/Blocks.INFESTED_CRACKED_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2930: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2933: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2936: putstatic       net/minecraft/world/item/Items.INFESTED_CRACKED_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2939: getstatic       net/minecraft/world/level/block/Blocks.INFESTED_CHISELED_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2942: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2945: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2948: putstatic       net/minecraft/world/item/Items.INFESTED_CHISELED_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2951: getstatic       net/minecraft/world/level/block/Blocks.STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2954: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2957: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2960: putstatic       net/minecraft/world/item/Items.STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2963: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2966: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2969: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2972: putstatic       net/minecraft/world/item/Items.MOSSY_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2975: getstatic       net/minecraft/world/level/block/Blocks.CRACKED_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2978: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2981: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2984: putstatic       net/minecraft/world/item/Items.CRACKED_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2987: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  2990: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  2993: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  2996: putstatic       net/minecraft/world/item/Items.CHISELED_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  2999: getstatic       net/minecraft/world/level/block/Blocks.BROWN_MUSHROOM_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  3002: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3005: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3008: putstatic       net/minecraft/world/item/Items.BROWN_MUSHROOM_BLOCK:Lnet/minecraft/world/item/Item;
        //  3011: getstatic       net/minecraft/world/level/block/Blocks.RED_MUSHROOM_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  3014: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3017: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3020: putstatic       net/minecraft/world/item/Items.RED_MUSHROOM_BLOCK:Lnet/minecraft/world/item/Item;
        //  3023: getstatic       net/minecraft/world/level/block/Blocks.MUSHROOM_STEM:Lnet/minecraft/world/level/block/Block;
        //  3026: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3029: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3032: putstatic       net/minecraft/world/item/Items.MUSHROOM_STEM:Lnet/minecraft/world/item/Item;
        //  3035: getstatic       net/minecraft/world/level/block/Blocks.IRON_BARS:Lnet/minecraft/world/level/block/Block;
        //  3038: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3041: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3044: putstatic       net/minecraft/world/item/Items.IRON_BARS:Lnet/minecraft/world/item/Item;
        //  3047: getstatic       net/minecraft/world/level/block/Blocks.CHAIN:Lnet/minecraft/world/level/block/Block;
        //  3050: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3053: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3056: putstatic       net/minecraft/world/item/Items.CHAIN:Lnet/minecraft/world/item/Item;
        //  3059: getstatic       net/minecraft/world/level/block/Blocks.GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  3062: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3065: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3068: putstatic       net/minecraft/world/item/Items.GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  3071: getstatic       net/minecraft/world/level/block/Blocks.MELON:Lnet/minecraft/world/level/block/Block;
        //  3074: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3077: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3080: putstatic       net/minecraft/world/item/Items.MELON:Lnet/minecraft/world/item/Item;
        //  3083: getstatic       net/minecraft/world/level/block/Blocks.VINE:Lnet/minecraft/world/level/block/Block;
        //  3086: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3089: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3092: putstatic       net/minecraft/world/item/Items.VINE:Lnet/minecraft/world/item/Item;
        //  3095: getstatic       net/minecraft/world/level/block/Blocks.OAK_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3098: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3101: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3104: putstatic       net/minecraft/world/item/Items.OAK_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3107: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3110: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3113: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3116: putstatic       net/minecraft/world/item/Items.SPRUCE_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3119: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3122: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3125: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3128: putstatic       net/minecraft/world/item/Items.BIRCH_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3131: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3134: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3137: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3140: putstatic       net/minecraft/world/item/Items.JUNGLE_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3143: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3146: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3149: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3152: putstatic       net/minecraft/world/item/Items.ACACIA_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3155: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3158: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3161: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3164: putstatic       net/minecraft/world/item/Items.DARK_OAK_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3167: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3170: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3173: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3176: putstatic       net/minecraft/world/item/Items.CRIMSON_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3179: getstatic       net/minecraft/world/level/block/Blocks.WARPED_FENCE_GATE:Lnet/minecraft/world/level/block/Block;
        //  3182: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3185: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3188: putstatic       net/minecraft/world/item/Items.WARPED_FENCE_GATE:Lnet/minecraft/world/item/Item;
        //  3191: getstatic       net/minecraft/world/level/block/Blocks.BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3194: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3197: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3200: putstatic       net/minecraft/world/item/Items.BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  3203: getstatic       net/minecraft/world/level/block/Blocks.STONE_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3206: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3209: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3212: putstatic       net/minecraft/world/item/Items.STONE_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  3215: getstatic       net/minecraft/world/level/block/Blocks.MYCELIUM:Lnet/minecraft/world/level/block/Block;
        //  3218: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3221: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3224: putstatic       net/minecraft/world/item/Items.MYCELIUM:Lnet/minecraft/world/item/Item;
        //  3227: new             Lnet/minecraft/world/item/WaterLilyBlockItem;
        //  3230: dup            
        //  3231: getstatic       net/minecraft/world/level/block/Blocks.LILY_PAD:Lnet/minecraft/world/level/block/Block;
        //  3234: new             Lnet/minecraft/world/item/Item$Properties;
        //  3237: dup            
        //  3238: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  3241: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3244: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  3247: invokespecial   net/minecraft/world/item/WaterLilyBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  3250: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  3253: putstatic       net/minecraft/world/item/Items.LILY_PAD:Lnet/minecraft/world/item/Item;
        //  3256: getstatic       net/minecraft/world/level/block/Blocks.NETHER_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  3259: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3262: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3265: putstatic       net/minecraft/world/item/Items.NETHER_BRICKS:Lnet/minecraft/world/item/Item;
        //  3268: getstatic       net/minecraft/world/level/block/Blocks.CRACKED_NETHER_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  3271: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3274: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3277: putstatic       net/minecraft/world/item/Items.CRACKED_NETHER_BRICKS:Lnet/minecraft/world/item/Item;
        //  3280: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_NETHER_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  3283: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3286: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3289: putstatic       net/minecraft/world/item/Items.CHISELED_NETHER_BRICKS:Lnet/minecraft/world/item/Item;
        //  3292: getstatic       net/minecraft/world/level/block/Blocks.NETHER_BRICK_FENCE:Lnet/minecraft/world/level/block/Block;
        //  3295: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3298: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3301: putstatic       net/minecraft/world/item/Items.NETHER_BRICK_FENCE:Lnet/minecraft/world/item/Item;
        //  3304: getstatic       net/minecraft/world/level/block/Blocks.NETHER_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3307: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3310: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3313: putstatic       net/minecraft/world/item/Items.NETHER_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  3316: getstatic       net/minecraft/world/level/block/Blocks.ENCHANTING_TABLE:Lnet/minecraft/world/level/block/Block;
        //  3319: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3322: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3325: putstatic       net/minecraft/world/item/Items.ENCHANTING_TABLE:Lnet/minecraft/world/item/Item;
        //  3328: getstatic       net/minecraft/world/level/block/Blocks.END_PORTAL_FRAME:Lnet/minecraft/world/level/block/Block;
        //  3331: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3334: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3337: putstatic       net/minecraft/world/item/Items.END_PORTAL_FRAME:Lnet/minecraft/world/item/Item;
        //  3340: getstatic       net/minecraft/world/level/block/Blocks.END_STONE:Lnet/minecraft/world/level/block/Block;
        //  3343: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3346: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3349: putstatic       net/minecraft/world/item/Items.END_STONE:Lnet/minecraft/world/item/Item;
        //  3352: getstatic       net/minecraft/world/level/block/Blocks.END_STONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  3355: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3358: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3361: putstatic       net/minecraft/world/item/Items.END_STONE_BRICKS:Lnet/minecraft/world/item/Item;
        //  3364: new             Lnet/minecraft/world/item/BlockItem;
        //  3367: dup            
        //  3368: getstatic       net/minecraft/world/level/block/Blocks.DRAGON_EGG:Lnet/minecraft/world/level/block/Block;
        //  3371: new             Lnet/minecraft/world/item/Item$Properties;
        //  3374: dup            
        //  3375: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  3378: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        //  3381: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  3384: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  3387: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  3390: putstatic       net/minecraft/world/item/Items.DRAGON_EGG:Lnet/minecraft/world/item/Item;
        //  3393: getstatic       net/minecraft/world/level/block/Blocks.REDSTONE_LAMP:Lnet/minecraft/world/level/block/Block;
        //  3396: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3399: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3402: putstatic       net/minecraft/world/item/Items.REDSTONE_LAMP:Lnet/minecraft/world/item/Item;
        //  3405: getstatic       net/minecraft/world/level/block/Blocks.SANDSTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3408: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3411: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3414: putstatic       net/minecraft/world/item/Items.SANDSTONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  3417: getstatic       net/minecraft/world/level/block/Blocks.EMERALD_ORE:Lnet/minecraft/world/level/block/Block;
        //  3420: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3423: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3426: putstatic       net/minecraft/world/item/Items.EMERALD_ORE:Lnet/minecraft/world/item/Item;
        //  3429: getstatic       net/minecraft/world/level/block/Blocks.ENDER_CHEST:Lnet/minecraft/world/level/block/Block;
        //  3432: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3435: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3438: putstatic       net/minecraft/world/item/Items.ENDER_CHEST:Lnet/minecraft/world/item/Item;
        //  3441: getstatic       net/minecraft/world/level/block/Blocks.TRIPWIRE_HOOK:Lnet/minecraft/world/level/block/Block;
        //  3444: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3447: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3450: putstatic       net/minecraft/world/item/Items.TRIPWIRE_HOOK:Lnet/minecraft/world/item/Item;
        //  3453: getstatic       net/minecraft/world/level/block/Blocks.EMERALD_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  3456: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3459: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3462: putstatic       net/minecraft/world/item/Items.EMERALD_BLOCK:Lnet/minecraft/world/item/Item;
        //  3465: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3468: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3471: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3474: putstatic       net/minecraft/world/item/Items.SPRUCE_STAIRS:Lnet/minecraft/world/item/Item;
        //  3477: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3480: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3483: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3486: putstatic       net/minecraft/world/item/Items.BIRCH_STAIRS:Lnet/minecraft/world/item/Item;
        //  3489: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3492: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3495: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3498: putstatic       net/minecraft/world/item/Items.JUNGLE_STAIRS:Lnet/minecraft/world/item/Item;
        //  3501: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3504: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3507: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3510: putstatic       net/minecraft/world/item/Items.CRIMSON_STAIRS:Lnet/minecraft/world/item/Item;
        //  3513: getstatic       net/minecraft/world/level/block/Blocks.WARPED_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  3516: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3519: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3522: putstatic       net/minecraft/world/item/Items.WARPED_STAIRS:Lnet/minecraft/world/item/Item;
        //  3525: new             Lnet/minecraft/world/item/GameMasterBlockItem;
        //  3528: dup            
        //  3529: getstatic       net/minecraft/world/level/block/Blocks.COMMAND_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  3532: new             Lnet/minecraft/world/item/Item$Properties;
        //  3535: dup            
        //  3536: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  3539: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        //  3542: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  3545: invokespecial   net/minecraft/world/item/GameMasterBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  3548: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  3551: putstatic       net/minecraft/world/item/Items.COMMAND_BLOCK:Lnet/minecraft/world/item/Item;
        //  3554: new             Lnet/minecraft/world/item/BlockItem;
        //  3557: dup            
        //  3558: getstatic       net/minecraft/world/level/block/Blocks.BEACON:Lnet/minecraft/world/level/block/Block;
        //  3561: new             Lnet/minecraft/world/item/Item$Properties;
        //  3564: dup            
        //  3565: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  3568: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        //  3571: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  3574: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        //  3577: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  3580: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  3583: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  3586: putstatic       net/minecraft/world/item/Items.BEACON:Lnet/minecraft/world/item/Item;
        //  3589: getstatic       net/minecraft/world/level/block/Blocks.COBBLESTONE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3592: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3595: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3598: putstatic       net/minecraft/world/item/Items.COBBLESTONE_WALL:Lnet/minecraft/world/item/Item;
        //  3601: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_COBBLESTONE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3604: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3607: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3610: putstatic       net/minecraft/world/item/Items.MOSSY_COBBLESTONE_WALL:Lnet/minecraft/world/item/Item;
        //  3613: getstatic       net/minecraft/world/level/block/Blocks.BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3616: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3619: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3622: putstatic       net/minecraft/world/item/Items.BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3625: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3628: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3631: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3634: putstatic       net/minecraft/world/item/Items.PRISMARINE_WALL:Lnet/minecraft/world/item/Item;
        //  3637: getstatic       net/minecraft/world/level/block/Blocks.RED_SANDSTONE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3640: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3643: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3646: putstatic       net/minecraft/world/item/Items.RED_SANDSTONE_WALL:Lnet/minecraft/world/item/Item;
        //  3649: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_STONE_BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3652: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3655: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3658: putstatic       net/minecraft/world/item/Items.MOSSY_STONE_BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3661: getstatic       net/minecraft/world/level/block/Blocks.GRANITE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3664: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3667: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3670: putstatic       net/minecraft/world/item/Items.GRANITE_WALL:Lnet/minecraft/world/item/Item;
        //  3673: getstatic       net/minecraft/world/level/block/Blocks.STONE_BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3676: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3679: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3682: putstatic       net/minecraft/world/item/Items.STONE_BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3685: getstatic       net/minecraft/world/level/block/Blocks.NETHER_BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3688: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3691: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3694: putstatic       net/minecraft/world/item/Items.NETHER_BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3697: getstatic       net/minecraft/world/level/block/Blocks.ANDESITE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3700: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3703: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3706: putstatic       net/minecraft/world/item/Items.ANDESITE_WALL:Lnet/minecraft/world/item/Item;
        //  3709: getstatic       net/minecraft/world/level/block/Blocks.RED_NETHER_BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3712: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3715: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3718: putstatic       net/minecraft/world/item/Items.RED_NETHER_BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3721: getstatic       net/minecraft/world/level/block/Blocks.SANDSTONE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3724: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3727: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3730: putstatic       net/minecraft/world/item/Items.SANDSTONE_WALL:Lnet/minecraft/world/item/Item;
        //  3733: getstatic       net/minecraft/world/level/block/Blocks.END_STONE_BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3736: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3739: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3742: putstatic       net/minecraft/world/item/Items.END_STONE_BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3745: getstatic       net/minecraft/world/level/block/Blocks.DIORITE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3748: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3751: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3754: putstatic       net/minecraft/world/item/Items.DIORITE_WALL:Lnet/minecraft/world/item/Item;
        //  3757: getstatic       net/minecraft/world/level/block/Blocks.BLACKSTONE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3760: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3763: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3766: putstatic       net/minecraft/world/item/Items.BLACKSTONE_WALL:Lnet/minecraft/world/item/Item;
        //  3769: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_WALL:Lnet/minecraft/world/level/block/Block;
        //  3772: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3775: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3778: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_WALL:Lnet/minecraft/world/item/Item;
        //  3781: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_BRICK_WALL:Lnet/minecraft/world/level/block/Block;
        //  3784: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3787: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3790: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_BRICK_WALL:Lnet/minecraft/world/item/Item;
        //  3793: getstatic       net/minecraft/world/level/block/Blocks.STONE_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3796: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3799: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3802: putstatic       net/minecraft/world/item/Items.STONE_BUTTON:Lnet/minecraft/world/item/Item;
        //  3805: getstatic       net/minecraft/world/level/block/Blocks.OAK_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3808: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3811: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3814: putstatic       net/minecraft/world/item/Items.OAK_BUTTON:Lnet/minecraft/world/item/Item;
        //  3817: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3820: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3823: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3826: putstatic       net/minecraft/world/item/Items.SPRUCE_BUTTON:Lnet/minecraft/world/item/Item;
        //  3829: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3832: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3835: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3838: putstatic       net/minecraft/world/item/Items.BIRCH_BUTTON:Lnet/minecraft/world/item/Item;
        //  3841: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3844: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3847: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3850: putstatic       net/minecraft/world/item/Items.JUNGLE_BUTTON:Lnet/minecraft/world/item/Item;
        //  3853: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3856: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3859: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3862: putstatic       net/minecraft/world/item/Items.ACACIA_BUTTON:Lnet/minecraft/world/item/Item;
        //  3865: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3868: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3871: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3874: putstatic       net/minecraft/world/item/Items.DARK_OAK_BUTTON:Lnet/minecraft/world/item/Item;
        //  3877: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3880: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3883: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3886: putstatic       net/minecraft/world/item/Items.CRIMSON_BUTTON:Lnet/minecraft/world/item/Item;
        //  3889: getstatic       net/minecraft/world/level/block/Blocks.WARPED_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3892: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3895: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3898: putstatic       net/minecraft/world/item/Items.WARPED_BUTTON:Lnet/minecraft/world/item/Item;
        //  3901: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_BUTTON:Lnet/minecraft/world/level/block/Block;
        //  3904: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3907: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3910: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_BUTTON:Lnet/minecraft/world/item/Item;
        //  3913: getstatic       net/minecraft/world/level/block/Blocks.ANVIL:Lnet/minecraft/world/level/block/Block;
        //  3916: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3919: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3922: putstatic       net/minecraft/world/item/Items.ANVIL:Lnet/minecraft/world/item/Item;
        //  3925: getstatic       net/minecraft/world/level/block/Blocks.CHIPPED_ANVIL:Lnet/minecraft/world/level/block/Block;
        //  3928: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3931: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3934: putstatic       net/minecraft/world/item/Items.CHIPPED_ANVIL:Lnet/minecraft/world/item/Item;
        //  3937: getstatic       net/minecraft/world/level/block/Blocks.DAMAGED_ANVIL:Lnet/minecraft/world/level/block/Block;
        //  3940: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  3943: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3946: putstatic       net/minecraft/world/item/Items.DAMAGED_ANVIL:Lnet/minecraft/world/item/Item;
        //  3949: getstatic       net/minecraft/world/level/block/Blocks.TRAPPED_CHEST:Lnet/minecraft/world/level/block/Block;
        //  3952: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3955: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3958: putstatic       net/minecraft/world/item/Items.TRAPPED_CHEST:Lnet/minecraft/world/item/Item;
        //  3961: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  3964: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3967: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3970: putstatic       net/minecraft/world/item/Items.LIGHT_WEIGHTED_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  3973: getstatic       net/minecraft/world/level/block/Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE:Lnet/minecraft/world/level/block/Block;
        //  3976: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3979: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3982: putstatic       net/minecraft/world/item/Items.HEAVY_WEIGHTED_PRESSURE_PLATE:Lnet/minecraft/world/item/Item;
        //  3985: getstatic       net/minecraft/world/level/block/Blocks.DAYLIGHT_DETECTOR:Lnet/minecraft/world/level/block/Block;
        //  3988: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  3991: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  3994: putstatic       net/minecraft/world/item/Items.DAYLIGHT_DETECTOR:Lnet/minecraft/world/item/Item;
        //  3997: getstatic       net/minecraft/world/level/block/Blocks.REDSTONE_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  4000: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  4003: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4006: putstatic       net/minecraft/world/item/Items.REDSTONE_BLOCK:Lnet/minecraft/world/item/Item;
        //  4009: getstatic       net/minecraft/world/level/block/Blocks.NETHER_QUARTZ_ORE:Lnet/minecraft/world/level/block/Block;
        //  4012: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4015: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4018: putstatic       net/minecraft/world/item/Items.NETHER_QUARTZ_ORE:Lnet/minecraft/world/item/Item;
        //  4021: getstatic       net/minecraft/world/level/block/Blocks.HOPPER:Lnet/minecraft/world/level/block/Block;
        //  4024: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  4027: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4030: putstatic       net/minecraft/world/item/Items.HOPPER:Lnet/minecraft/world/item/Item;
        //  4033: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_QUARTZ_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  4036: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4039: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4042: putstatic       net/minecraft/world/item/Items.CHISELED_QUARTZ_BLOCK:Lnet/minecraft/world/item/Item;
        //  4045: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  4048: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4051: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4054: putstatic       net/minecraft/world/item/Items.QUARTZ_BLOCK:Lnet/minecraft/world/item/Item;
        //  4057: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  4060: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4063: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4066: putstatic       net/minecraft/world/item/Items.QUARTZ_BRICKS:Lnet/minecraft/world/item/Item;
        //  4069: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_PILLAR:Lnet/minecraft/world/level/block/Block;
        //  4072: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4075: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4078: putstatic       net/minecraft/world/item/Items.QUARTZ_PILLAR:Lnet/minecraft/world/item/Item;
        //  4081: getstatic       net/minecraft/world/level/block/Blocks.QUARTZ_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  4084: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4087: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4090: putstatic       net/minecraft/world/item/Items.QUARTZ_STAIRS:Lnet/minecraft/world/item/Item;
        //  4093: getstatic       net/minecraft/world/level/block/Blocks.ACTIVATOR_RAIL:Lnet/minecraft/world/level/block/Block;
        //  4096: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        //  4099: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4102: putstatic       net/minecraft/world/item/Items.ACTIVATOR_RAIL:Lnet/minecraft/world/item/Item;
        //  4105: getstatic       net/minecraft/world/level/block/Blocks.DROPPER:Lnet/minecraft/world/level/block/Block;
        //  4108: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  4111: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4114: putstatic       net/minecraft/world/item/Items.DROPPER:Lnet/minecraft/world/item/Item;
        //  4117: getstatic       net/minecraft/world/level/block/Blocks.WHITE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4120: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4123: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4126: putstatic       net/minecraft/world/item/Items.WHITE_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4129: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4132: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4135: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4138: putstatic       net/minecraft/world/item/Items.ORANGE_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4141: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4144: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4147: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4150: putstatic       net/minecraft/world/item/Items.MAGENTA_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4153: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4156: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4159: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4162: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4165: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4168: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4171: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4174: putstatic       net/minecraft/world/item/Items.YELLOW_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4177: getstatic       net/minecraft/world/level/block/Blocks.LIME_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4180: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4183: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4186: putstatic       net/minecraft/world/item/Items.LIME_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4189: getstatic       net/minecraft/world/level/block/Blocks.PINK_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4192: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4195: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4198: putstatic       net/minecraft/world/item/Items.PINK_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4201: getstatic       net/minecraft/world/level/block/Blocks.GRAY_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4204: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4207: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4210: putstatic       net/minecraft/world/item/Items.GRAY_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4213: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4216: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4219: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4222: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4225: getstatic       net/minecraft/world/level/block/Blocks.CYAN_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4228: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4231: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4234: putstatic       net/minecraft/world/item/Items.CYAN_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4237: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4240: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4243: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4246: putstatic       net/minecraft/world/item/Items.PURPLE_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4249: getstatic       net/minecraft/world/level/block/Blocks.BLUE_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4252: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4255: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4258: putstatic       net/minecraft/world/item/Items.BLUE_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4261: getstatic       net/minecraft/world/level/block/Blocks.BROWN_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4264: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4267: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4270: putstatic       net/minecraft/world/item/Items.BROWN_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4273: getstatic       net/minecraft/world/level/block/Blocks.GREEN_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4276: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4279: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4282: putstatic       net/minecraft/world/item/Items.GREEN_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4285: getstatic       net/minecraft/world/level/block/Blocks.RED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4288: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4291: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4294: putstatic       net/minecraft/world/item/Items.RED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4297: getstatic       net/minecraft/world/level/block/Blocks.BLACK_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4300: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4303: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4306: putstatic       net/minecraft/world/item/Items.BLACK_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4309: getstatic       net/minecraft/world/level/block/Blocks.BARRIER:Lnet/minecraft/world/level/block/Block;
        //  4312: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/item/Item;
        //  4315: putstatic       net/minecraft/world/item/Items.BARRIER:Lnet/minecraft/world/item/Item;
        //  4318: getstatic       net/minecraft/world/level/block/Blocks.IRON_TRAPDOOR:Lnet/minecraft/world/level/block/Block;
        //  4321: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  4324: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4327: putstatic       net/minecraft/world/item/Items.IRON_TRAPDOOR:Lnet/minecraft/world/item/Item;
        //  4330: getstatic       net/minecraft/world/level/block/Blocks.HAY_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  4333: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4336: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4339: putstatic       net/minecraft/world/item/Items.HAY_BLOCK:Lnet/minecraft/world/item/Item;
        //  4342: getstatic       net/minecraft/world/level/block/Blocks.WHITE_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4345: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4348: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4351: putstatic       net/minecraft/world/item/Items.WHITE_CARPET:Lnet/minecraft/world/item/Item;
        //  4354: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4357: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4360: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4363: putstatic       net/minecraft/world/item/Items.ORANGE_CARPET:Lnet/minecraft/world/item/Item;
        //  4366: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4369: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4372: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4375: putstatic       net/minecraft/world/item/Items.MAGENTA_CARPET:Lnet/minecraft/world/item/Item;
        //  4378: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4381: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4384: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4387: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_CARPET:Lnet/minecraft/world/item/Item;
        //  4390: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4393: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4396: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4399: putstatic       net/minecraft/world/item/Items.YELLOW_CARPET:Lnet/minecraft/world/item/Item;
        //  4402: getstatic       net/minecraft/world/level/block/Blocks.LIME_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4405: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4408: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4411: putstatic       net/minecraft/world/item/Items.LIME_CARPET:Lnet/minecraft/world/item/Item;
        //  4414: getstatic       net/minecraft/world/level/block/Blocks.PINK_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4417: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4420: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4423: putstatic       net/minecraft/world/item/Items.PINK_CARPET:Lnet/minecraft/world/item/Item;
        //  4426: getstatic       net/minecraft/world/level/block/Blocks.GRAY_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4429: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4432: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4435: putstatic       net/minecraft/world/item/Items.GRAY_CARPET:Lnet/minecraft/world/item/Item;
        //  4438: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4441: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4444: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4447: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_CARPET:Lnet/minecraft/world/item/Item;
        //  4450: getstatic       net/minecraft/world/level/block/Blocks.CYAN_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4453: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4456: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4459: putstatic       net/minecraft/world/item/Items.CYAN_CARPET:Lnet/minecraft/world/item/Item;
        //  4462: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4465: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4468: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4471: putstatic       net/minecraft/world/item/Items.PURPLE_CARPET:Lnet/minecraft/world/item/Item;
        //  4474: getstatic       net/minecraft/world/level/block/Blocks.BLUE_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4477: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4480: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4483: putstatic       net/minecraft/world/item/Items.BLUE_CARPET:Lnet/minecraft/world/item/Item;
        //  4486: getstatic       net/minecraft/world/level/block/Blocks.BROWN_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4489: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4492: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4495: putstatic       net/minecraft/world/item/Items.BROWN_CARPET:Lnet/minecraft/world/item/Item;
        //  4498: getstatic       net/minecraft/world/level/block/Blocks.GREEN_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4501: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4504: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4507: putstatic       net/minecraft/world/item/Items.GREEN_CARPET:Lnet/minecraft/world/item/Item;
        //  4510: getstatic       net/minecraft/world/level/block/Blocks.RED_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4513: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4516: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4519: putstatic       net/minecraft/world/item/Items.RED_CARPET:Lnet/minecraft/world/item/Item;
        //  4522: getstatic       net/minecraft/world/level/block/Blocks.BLACK_CARPET:Lnet/minecraft/world/level/block/Block;
        //  4525: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4528: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4531: putstatic       net/minecraft/world/item/Items.BLACK_CARPET:Lnet/minecraft/world/item/Item;
        //  4534: getstatic       net/minecraft/world/level/block/Blocks.TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  4537: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4540: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4543: putstatic       net/minecraft/world/item/Items.TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  4546: getstatic       net/minecraft/world/level/block/Blocks.COAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  4549: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4552: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4555: putstatic       net/minecraft/world/item/Items.COAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  4558: getstatic       net/minecraft/world/level/block/Blocks.PACKED_ICE:Lnet/minecraft/world/level/block/Block;
        //  4561: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4564: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4567: putstatic       net/minecraft/world/item/Items.PACKED_ICE:Lnet/minecraft/world/item/Item;
        //  4570: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  4573: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4576: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4579: putstatic       net/minecraft/world/item/Items.ACACIA_STAIRS:Lnet/minecraft/world/item/Item;
        //  4582: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  4585: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4588: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4591: putstatic       net/minecraft/world/item/Items.DARK_OAK_STAIRS:Lnet/minecraft/world/item/Item;
        //  4594: getstatic       net/minecraft/world/level/block/Blocks.SLIME_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  4597: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4600: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4603: putstatic       net/minecraft/world/item/Items.SLIME_BLOCK:Lnet/minecraft/world/item/Item;
        //  4606: getstatic       net/minecraft/world/level/block/Blocks.GRASS_PATH:Lnet/minecraft/world/level/block/Block;
        //  4609: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4612: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4615: putstatic       net/minecraft/world/item/Items.GRASS_PATH:Lnet/minecraft/world/item/Item;
        //  4618: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  4621: dup            
        //  4622: getstatic       net/minecraft/world/level/block/Blocks.SUNFLOWER:Lnet/minecraft/world/level/block/Block;
        //  4625: new             Lnet/minecraft/world/item/Item$Properties;
        //  4628: dup            
        //  4629: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  4632: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4635: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  4638: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  4641: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  4644: putstatic       net/minecraft/world/item/Items.SUNFLOWER:Lnet/minecraft/world/item/Item;
        //  4647: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  4650: dup            
        //  4651: getstatic       net/minecraft/world/level/block/Blocks.LILAC:Lnet/minecraft/world/level/block/Block;
        //  4654: new             Lnet/minecraft/world/item/Item$Properties;
        //  4657: dup            
        //  4658: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  4661: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4664: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  4667: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  4670: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  4673: putstatic       net/minecraft/world/item/Items.LILAC:Lnet/minecraft/world/item/Item;
        //  4676: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  4679: dup            
        //  4680: getstatic       net/minecraft/world/level/block/Blocks.ROSE_BUSH:Lnet/minecraft/world/level/block/Block;
        //  4683: new             Lnet/minecraft/world/item/Item$Properties;
        //  4686: dup            
        //  4687: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  4690: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4693: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  4696: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  4699: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  4702: putstatic       net/minecraft/world/item/Items.ROSE_BUSH:Lnet/minecraft/world/item/Item;
        //  4705: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  4708: dup            
        //  4709: getstatic       net/minecraft/world/level/block/Blocks.PEONY:Lnet/minecraft/world/level/block/Block;
        //  4712: new             Lnet/minecraft/world/item/Item$Properties;
        //  4715: dup            
        //  4716: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  4719: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4722: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  4725: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  4728: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  4731: putstatic       net/minecraft/world/item/Items.PEONY:Lnet/minecraft/world/item/Item;
        //  4734: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  4737: dup            
        //  4738: getstatic       net/minecraft/world/level/block/Blocks.TALL_GRASS:Lnet/minecraft/world/level/block/Block;
        //  4741: new             Lnet/minecraft/world/item/Item$Properties;
        //  4744: dup            
        //  4745: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  4748: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4751: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  4754: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  4757: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  4760: putstatic       net/minecraft/world/item/Items.TALL_GRASS:Lnet/minecraft/world/item/Item;
        //  4763: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  4766: dup            
        //  4767: getstatic       net/minecraft/world/level/block/Blocks.LARGE_FERN:Lnet/minecraft/world/level/block/Block;
        //  4770: new             Lnet/minecraft/world/item/Item$Properties;
        //  4773: dup            
        //  4774: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  4777: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4780: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  4783: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  4786: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  4789: putstatic       net/minecraft/world/item/Items.LARGE_FERN:Lnet/minecraft/world/item/Item;
        //  4792: getstatic       net/minecraft/world/level/block/Blocks.WHITE_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4795: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4798: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4801: putstatic       net/minecraft/world/item/Items.WHITE_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4804: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4807: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4810: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4813: putstatic       net/minecraft/world/item/Items.ORANGE_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4816: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4819: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4822: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4825: putstatic       net/minecraft/world/item/Items.MAGENTA_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4828: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4831: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4834: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4837: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4840: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4843: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4846: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4849: putstatic       net/minecraft/world/item/Items.YELLOW_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4852: getstatic       net/minecraft/world/level/block/Blocks.LIME_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4855: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4858: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4861: putstatic       net/minecraft/world/item/Items.LIME_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4864: getstatic       net/minecraft/world/level/block/Blocks.PINK_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4867: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4870: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4873: putstatic       net/minecraft/world/item/Items.PINK_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4876: getstatic       net/minecraft/world/level/block/Blocks.GRAY_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4879: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4882: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4885: putstatic       net/minecraft/world/item/Items.GRAY_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4888: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4891: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4894: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4897: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4900: getstatic       net/minecraft/world/level/block/Blocks.CYAN_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4903: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4906: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4909: putstatic       net/minecraft/world/item/Items.CYAN_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4912: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4915: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4918: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4921: putstatic       net/minecraft/world/item/Items.PURPLE_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4924: getstatic       net/minecraft/world/level/block/Blocks.BLUE_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4927: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4930: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4933: putstatic       net/minecraft/world/item/Items.BLUE_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4936: getstatic       net/minecraft/world/level/block/Blocks.BROWN_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4939: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4942: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4945: putstatic       net/minecraft/world/item/Items.BROWN_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4948: getstatic       net/minecraft/world/level/block/Blocks.GREEN_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4951: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4954: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4957: putstatic       net/minecraft/world/item/Items.GREEN_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4960: getstatic       net/minecraft/world/level/block/Blocks.RED_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4963: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4966: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4969: putstatic       net/minecraft/world/item/Items.RED_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4972: getstatic       net/minecraft/world/level/block/Blocks.BLACK_STAINED_GLASS:Lnet/minecraft/world/level/block/Block;
        //  4975: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4978: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4981: putstatic       net/minecraft/world/item/Items.BLACK_STAINED_GLASS:Lnet/minecraft/world/item/Item;
        //  4984: getstatic       net/minecraft/world/level/block/Blocks.WHITE_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  4987: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  4990: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  4993: putstatic       net/minecraft/world/item/Items.WHITE_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  4996: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  4999: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5002: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5005: putstatic       net/minecraft/world/item/Items.ORANGE_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5008: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5011: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5014: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5017: putstatic       net/minecraft/world/item/Items.MAGENTA_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5020: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5023: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5026: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5029: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5032: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5035: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5038: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5041: putstatic       net/minecraft/world/item/Items.YELLOW_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5044: getstatic       net/minecraft/world/level/block/Blocks.LIME_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5047: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5050: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5053: putstatic       net/minecraft/world/item/Items.LIME_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5056: getstatic       net/minecraft/world/level/block/Blocks.PINK_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5059: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5062: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5065: putstatic       net/minecraft/world/item/Items.PINK_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5068: getstatic       net/minecraft/world/level/block/Blocks.GRAY_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5071: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5074: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5077: putstatic       net/minecraft/world/item/Items.GRAY_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5080: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5083: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5086: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5089: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5092: getstatic       net/minecraft/world/level/block/Blocks.CYAN_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5095: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5098: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5101: putstatic       net/minecraft/world/item/Items.CYAN_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5104: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5107: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5110: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5113: putstatic       net/minecraft/world/item/Items.PURPLE_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5116: getstatic       net/minecraft/world/level/block/Blocks.BLUE_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5119: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5122: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5125: putstatic       net/minecraft/world/item/Items.BLUE_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5128: getstatic       net/minecraft/world/level/block/Blocks.BROWN_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5131: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5134: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5137: putstatic       net/minecraft/world/item/Items.BROWN_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5140: getstatic       net/minecraft/world/level/block/Blocks.GREEN_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5143: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5146: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5149: putstatic       net/minecraft/world/item/Items.GREEN_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5152: getstatic       net/minecraft/world/level/block/Blocks.RED_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5155: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5158: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5161: putstatic       net/minecraft/world/item/Items.RED_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5164: getstatic       net/minecraft/world/level/block/Blocks.BLACK_STAINED_GLASS_PANE:Lnet/minecraft/world/level/block/Block;
        //  5167: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5170: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5173: putstatic       net/minecraft/world/item/Items.BLACK_STAINED_GLASS_PANE:Lnet/minecraft/world/item/Item;
        //  5176: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE:Lnet/minecraft/world/level/block/Block;
        //  5179: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5182: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5185: putstatic       net/minecraft/world/item/Items.PRISMARINE:Lnet/minecraft/world/item/Item;
        //  5188: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  5191: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5194: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5197: putstatic       net/minecraft/world/item/Items.PRISMARINE_BRICKS:Lnet/minecraft/world/item/Item;
        //  5200: getstatic       net/minecraft/world/level/block/Blocks.DARK_PRISMARINE:Lnet/minecraft/world/level/block/Block;
        //  5203: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5206: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5209: putstatic       net/minecraft/world/item/Items.DARK_PRISMARINE:Lnet/minecraft/world/item/Item;
        //  5212: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  5215: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5218: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5221: putstatic       net/minecraft/world/item/Items.PRISMARINE_STAIRS:Lnet/minecraft/world/item/Item;
        //  5224: getstatic       net/minecraft/world/level/block/Blocks.PRISMARINE_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  5227: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5230: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5233: putstatic       net/minecraft/world/item/Items.PRISMARINE_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  5236: getstatic       net/minecraft/world/level/block/Blocks.DARK_PRISMARINE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  5239: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5242: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5245: putstatic       net/minecraft/world/item/Items.DARK_PRISMARINE_STAIRS:Lnet/minecraft/world/item/Item;
        //  5248: getstatic       net/minecraft/world/level/block/Blocks.SEA_LANTERN:Lnet/minecraft/world/level/block/Block;
        //  5251: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5254: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5257: putstatic       net/minecraft/world/item/Items.SEA_LANTERN:Lnet/minecraft/world/item/Item;
        //  5260: getstatic       net/minecraft/world/level/block/Blocks.RED_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  5263: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5266: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5269: putstatic       net/minecraft/world/item/Items.RED_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  5272: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_RED_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  5275: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5278: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5281: putstatic       net/minecraft/world/item/Items.CHISELED_RED_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  5284: getstatic       net/minecraft/world/level/block/Blocks.CUT_RED_SANDSTONE:Lnet/minecraft/world/level/block/Block;
        //  5287: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5290: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5293: putstatic       net/minecraft/world/item/Items.CUT_RED_SANDSTONE:Lnet/minecraft/world/item/Item;
        //  5296: getstatic       net/minecraft/world/level/block/Blocks.RED_SANDSTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  5299: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5302: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5305: putstatic       net/minecraft/world/item/Items.RED_SANDSTONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  5308: new             Lnet/minecraft/world/item/GameMasterBlockItem;
        //  5311: dup            
        //  5312: getstatic       net/minecraft/world/level/block/Blocks.REPEATING_COMMAND_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  5315: new             Lnet/minecraft/world/item/Item$Properties;
        //  5318: dup            
        //  5319: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5322: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        //  5325: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  5328: invokespecial   net/minecraft/world/item/GameMasterBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5331: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5334: putstatic       net/minecraft/world/item/Items.REPEATING_COMMAND_BLOCK:Lnet/minecraft/world/item/Item;
        //  5337: new             Lnet/minecraft/world/item/GameMasterBlockItem;
        //  5340: dup            
        //  5341: getstatic       net/minecraft/world/level/block/Blocks.CHAIN_COMMAND_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  5344: new             Lnet/minecraft/world/item/Item$Properties;
        //  5347: dup            
        //  5348: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5351: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        //  5354: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  5357: invokespecial   net/minecraft/world/item/GameMasterBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5360: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5363: putstatic       net/minecraft/world/item/Items.CHAIN_COMMAND_BLOCK:Lnet/minecraft/world/item/Item;
        //  5366: getstatic       net/minecraft/world/level/block/Blocks.MAGMA_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  5369: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5372: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5375: putstatic       net/minecraft/world/item/Items.MAGMA_BLOCK:Lnet/minecraft/world/item/Item;
        //  5378: getstatic       net/minecraft/world/level/block/Blocks.NETHER_WART_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  5381: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5384: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5387: putstatic       net/minecraft/world/item/Items.NETHER_WART_BLOCK:Lnet/minecraft/world/item/Item;
        //  5390: getstatic       net/minecraft/world/level/block/Blocks.WARPED_WART_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  5393: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5396: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5399: putstatic       net/minecraft/world/item/Items.WARPED_WART_BLOCK:Lnet/minecraft/world/item/Item;
        //  5402: getstatic       net/minecraft/world/level/block/Blocks.RED_NETHER_BRICKS:Lnet/minecraft/world/level/block/Block;
        //  5405: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5408: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5411: putstatic       net/minecraft/world/item/Items.RED_NETHER_BRICKS:Lnet/minecraft/world/item/Item;
        //  5414: getstatic       net/minecraft/world/level/block/Blocks.BONE_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  5417: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5420: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5423: putstatic       net/minecraft/world/item/Items.BONE_BLOCK:Lnet/minecraft/world/item/Item;
        //  5426: getstatic       net/minecraft/world/level/block/Blocks.STRUCTURE_VOID:Lnet/minecraft/world/level/block/Block;
        //  5429: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/item/Item;
        //  5432: putstatic       net/minecraft/world/item/Items.STRUCTURE_VOID:Lnet/minecraft/world/item/Item;
        //  5435: getstatic       net/minecraft/world/level/block/Blocks.OBSERVER:Lnet/minecraft/world/level/block/Block;
        //  5438: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  5441: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  5444: putstatic       net/minecraft/world/item/Items.OBSERVER:Lnet/minecraft/world/item/Item;
        //  5447: new             Lnet/minecraft/world/item/BlockItem;
        //  5450: dup            
        //  5451: getstatic       net/minecraft/world/level/block/Blocks.SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5454: new             Lnet/minecraft/world/item/Item$Properties;
        //  5457: dup            
        //  5458: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5461: iconst_1       
        //  5462: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5465: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5468: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5471: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5474: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5477: putstatic       net/minecraft/world/item/Items.SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5480: new             Lnet/minecraft/world/item/BlockItem;
        //  5483: dup            
        //  5484: getstatic       net/minecraft/world/level/block/Blocks.WHITE_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5487: new             Lnet/minecraft/world/item/Item$Properties;
        //  5490: dup            
        //  5491: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5494: iconst_1       
        //  5495: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5498: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5501: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5504: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5507: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5510: putstatic       net/minecraft/world/item/Items.WHITE_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5513: new             Lnet/minecraft/world/item/BlockItem;
        //  5516: dup            
        //  5517: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5520: new             Lnet/minecraft/world/item/Item$Properties;
        //  5523: dup            
        //  5524: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5527: iconst_1       
        //  5528: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5531: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5534: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5537: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5540: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5543: putstatic       net/minecraft/world/item/Items.ORANGE_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5546: new             Lnet/minecraft/world/item/BlockItem;
        //  5549: dup            
        //  5550: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5553: new             Lnet/minecraft/world/item/Item$Properties;
        //  5556: dup            
        //  5557: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5560: iconst_1       
        //  5561: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5564: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5567: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5570: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5573: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5576: putstatic       net/minecraft/world/item/Items.MAGENTA_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5579: new             Lnet/minecraft/world/item/BlockItem;
        //  5582: dup            
        //  5583: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5586: new             Lnet/minecraft/world/item/Item$Properties;
        //  5589: dup            
        //  5590: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5593: iconst_1       
        //  5594: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5597: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5600: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5603: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5606: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5609: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5612: new             Lnet/minecraft/world/item/BlockItem;
        //  5615: dup            
        //  5616: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5619: new             Lnet/minecraft/world/item/Item$Properties;
        //  5622: dup            
        //  5623: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5626: iconst_1       
        //  5627: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5630: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5633: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5636: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5639: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5642: putstatic       net/minecraft/world/item/Items.YELLOW_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5645: new             Lnet/minecraft/world/item/BlockItem;
        //  5648: dup            
        //  5649: getstatic       net/minecraft/world/level/block/Blocks.LIME_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5652: new             Lnet/minecraft/world/item/Item$Properties;
        //  5655: dup            
        //  5656: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5659: iconst_1       
        //  5660: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5663: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5666: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5669: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5672: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5675: putstatic       net/minecraft/world/item/Items.LIME_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5678: new             Lnet/minecraft/world/item/BlockItem;
        //  5681: dup            
        //  5682: getstatic       net/minecraft/world/level/block/Blocks.PINK_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5685: new             Lnet/minecraft/world/item/Item$Properties;
        //  5688: dup            
        //  5689: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5692: iconst_1       
        //  5693: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5696: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5699: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5702: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5705: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5708: putstatic       net/minecraft/world/item/Items.PINK_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5711: new             Lnet/minecraft/world/item/BlockItem;
        //  5714: dup            
        //  5715: getstatic       net/minecraft/world/level/block/Blocks.GRAY_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5718: new             Lnet/minecraft/world/item/Item$Properties;
        //  5721: dup            
        //  5722: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5725: iconst_1       
        //  5726: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5729: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5732: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5735: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5738: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5741: putstatic       net/minecraft/world/item/Items.GRAY_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5744: new             Lnet/minecraft/world/item/BlockItem;
        //  5747: dup            
        //  5748: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5751: new             Lnet/minecraft/world/item/Item$Properties;
        //  5754: dup            
        //  5755: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5758: iconst_1       
        //  5759: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5762: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5765: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5768: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5771: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5774: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5777: new             Lnet/minecraft/world/item/BlockItem;
        //  5780: dup            
        //  5781: getstatic       net/minecraft/world/level/block/Blocks.CYAN_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5784: new             Lnet/minecraft/world/item/Item$Properties;
        //  5787: dup            
        //  5788: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5791: iconst_1       
        //  5792: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5795: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5798: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5801: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5804: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5807: putstatic       net/minecraft/world/item/Items.CYAN_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5810: new             Lnet/minecraft/world/item/BlockItem;
        //  5813: dup            
        //  5814: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5817: new             Lnet/minecraft/world/item/Item$Properties;
        //  5820: dup            
        //  5821: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5824: iconst_1       
        //  5825: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5828: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5831: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5834: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5837: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5840: putstatic       net/minecraft/world/item/Items.PURPLE_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5843: new             Lnet/minecraft/world/item/BlockItem;
        //  5846: dup            
        //  5847: getstatic       net/minecraft/world/level/block/Blocks.BLUE_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5850: new             Lnet/minecraft/world/item/Item$Properties;
        //  5853: dup            
        //  5854: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5857: iconst_1       
        //  5858: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5861: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5864: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5867: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5870: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5873: putstatic       net/minecraft/world/item/Items.BLUE_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5876: new             Lnet/minecraft/world/item/BlockItem;
        //  5879: dup            
        //  5880: getstatic       net/minecraft/world/level/block/Blocks.BROWN_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5883: new             Lnet/minecraft/world/item/Item$Properties;
        //  5886: dup            
        //  5887: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5890: iconst_1       
        //  5891: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5894: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5897: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5900: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5903: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5906: putstatic       net/minecraft/world/item/Items.BROWN_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5909: new             Lnet/minecraft/world/item/BlockItem;
        //  5912: dup            
        //  5913: getstatic       net/minecraft/world/level/block/Blocks.GREEN_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5916: new             Lnet/minecraft/world/item/Item$Properties;
        //  5919: dup            
        //  5920: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5923: iconst_1       
        //  5924: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5927: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5930: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5933: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5936: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5939: putstatic       net/minecraft/world/item/Items.GREEN_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5942: new             Lnet/minecraft/world/item/BlockItem;
        //  5945: dup            
        //  5946: getstatic       net/minecraft/world/level/block/Blocks.RED_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5949: new             Lnet/minecraft/world/item/Item$Properties;
        //  5952: dup            
        //  5953: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5956: iconst_1       
        //  5957: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5960: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5963: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5966: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  5969: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  5972: putstatic       net/minecraft/world/item/Items.RED_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  5975: new             Lnet/minecraft/world/item/BlockItem;
        //  5978: dup            
        //  5979: getstatic       net/minecraft/world/level/block/Blocks.BLACK_SHULKER_BOX:Lnet/minecraft/world/level/block/Block;
        //  5982: new             Lnet/minecraft/world/item/Item$Properties;
        //  5985: dup            
        //  5986: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  5989: iconst_1       
        //  5990: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  5993: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  5996: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  5999: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  6002: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  6005: putstatic       net/minecraft/world/item/Items.BLACK_SHULKER_BOX:Lnet/minecraft/world/item/Item;
        //  6008: getstatic       net/minecraft/world/level/block/Blocks.WHITE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6011: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6014: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6017: putstatic       net/minecraft/world/item/Items.WHITE_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6020: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6023: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6026: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6029: putstatic       net/minecraft/world/item/Items.ORANGE_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6032: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6035: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6038: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6041: putstatic       net/minecraft/world/item/Items.MAGENTA_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6044: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6047: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6050: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6053: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6056: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6059: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6062: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6065: putstatic       net/minecraft/world/item/Items.YELLOW_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6068: getstatic       net/minecraft/world/level/block/Blocks.LIME_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6071: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6074: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6077: putstatic       net/minecraft/world/item/Items.LIME_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6080: getstatic       net/minecraft/world/level/block/Blocks.PINK_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6083: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6086: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6089: putstatic       net/minecraft/world/item/Items.PINK_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6092: getstatic       net/minecraft/world/level/block/Blocks.GRAY_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6095: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6098: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6101: putstatic       net/minecraft/world/item/Items.GRAY_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6104: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6107: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6110: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6113: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6116: getstatic       net/minecraft/world/level/block/Blocks.CYAN_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6119: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6122: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6125: putstatic       net/minecraft/world/item/Items.CYAN_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6128: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6131: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6134: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6137: putstatic       net/minecraft/world/item/Items.PURPLE_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6140: getstatic       net/minecraft/world/level/block/Blocks.BLUE_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6143: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6146: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6149: putstatic       net/minecraft/world/item/Items.BLUE_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6152: getstatic       net/minecraft/world/level/block/Blocks.BROWN_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6155: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6158: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6161: putstatic       net/minecraft/world/item/Items.BROWN_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6164: getstatic       net/minecraft/world/level/block/Blocks.GREEN_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6167: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6170: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6173: putstatic       net/minecraft/world/item/Items.GREEN_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6176: getstatic       net/minecraft/world/level/block/Blocks.RED_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6179: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6182: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6185: putstatic       net/minecraft/world/item/Items.RED_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6188: getstatic       net/minecraft/world/level/block/Blocks.BLACK_GLAZED_TERRACOTTA:Lnet/minecraft/world/level/block/Block;
        //  6191: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6194: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6197: putstatic       net/minecraft/world/item/Items.BLACK_GLAZED_TERRACOTTA:Lnet/minecraft/world/item/Item;
        //  6200: getstatic       net/minecraft/world/level/block/Blocks.WHITE_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6203: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6206: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6209: putstatic       net/minecraft/world/item/Items.WHITE_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6212: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6215: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6218: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6221: putstatic       net/minecraft/world/item/Items.ORANGE_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6224: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6227: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6230: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6233: putstatic       net/minecraft/world/item/Items.MAGENTA_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6236: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6239: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6242: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6245: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6248: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6251: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6254: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6257: putstatic       net/minecraft/world/item/Items.YELLOW_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6260: getstatic       net/minecraft/world/level/block/Blocks.LIME_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6263: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6266: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6269: putstatic       net/minecraft/world/item/Items.LIME_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6272: getstatic       net/minecraft/world/level/block/Blocks.PINK_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6275: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6278: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6281: putstatic       net/minecraft/world/item/Items.PINK_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6284: getstatic       net/minecraft/world/level/block/Blocks.GRAY_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6287: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6290: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6293: putstatic       net/minecraft/world/item/Items.GRAY_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6296: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6299: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6302: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6305: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6308: getstatic       net/minecraft/world/level/block/Blocks.CYAN_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6311: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6314: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6317: putstatic       net/minecraft/world/item/Items.CYAN_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6320: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6323: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6326: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6329: putstatic       net/minecraft/world/item/Items.PURPLE_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6332: getstatic       net/minecraft/world/level/block/Blocks.BLUE_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6335: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6338: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6341: putstatic       net/minecraft/world/item/Items.BLUE_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6344: getstatic       net/minecraft/world/level/block/Blocks.BROWN_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6347: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6350: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6353: putstatic       net/minecraft/world/item/Items.BROWN_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6356: getstatic       net/minecraft/world/level/block/Blocks.GREEN_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6359: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6362: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6365: putstatic       net/minecraft/world/item/Items.GREEN_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6368: getstatic       net/minecraft/world/level/block/Blocks.RED_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6371: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6374: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6377: putstatic       net/minecraft/world/item/Items.RED_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6380: getstatic       net/minecraft/world/level/block/Blocks.BLACK_CONCRETE:Lnet/minecraft/world/level/block/Block;
        //  6383: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6386: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6389: putstatic       net/minecraft/world/item/Items.BLACK_CONCRETE:Lnet/minecraft/world/item/Item;
        //  6392: getstatic       net/minecraft/world/level/block/Blocks.WHITE_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6395: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6398: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6401: putstatic       net/minecraft/world/item/Items.WHITE_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6404: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6407: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6410: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6413: putstatic       net/minecraft/world/item/Items.ORANGE_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6416: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6419: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6422: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6425: putstatic       net/minecraft/world/item/Items.MAGENTA_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6428: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6431: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6434: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6437: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6440: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6443: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6446: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6449: putstatic       net/minecraft/world/item/Items.YELLOW_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6452: getstatic       net/minecraft/world/level/block/Blocks.LIME_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6455: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6458: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6461: putstatic       net/minecraft/world/item/Items.LIME_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6464: getstatic       net/minecraft/world/level/block/Blocks.PINK_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6467: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6470: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6473: putstatic       net/minecraft/world/item/Items.PINK_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6476: getstatic       net/minecraft/world/level/block/Blocks.GRAY_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6479: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6482: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6485: putstatic       net/minecraft/world/item/Items.GRAY_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6488: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6491: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6494: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6497: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6500: getstatic       net/minecraft/world/level/block/Blocks.CYAN_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6503: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6506: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6509: putstatic       net/minecraft/world/item/Items.CYAN_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6512: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6515: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6518: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6521: putstatic       net/minecraft/world/item/Items.PURPLE_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6524: getstatic       net/minecraft/world/level/block/Blocks.BLUE_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6527: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6530: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6533: putstatic       net/minecraft/world/item/Items.BLUE_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6536: getstatic       net/minecraft/world/level/block/Blocks.BROWN_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6539: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6542: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6545: putstatic       net/minecraft/world/item/Items.BROWN_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6548: getstatic       net/minecraft/world/level/block/Blocks.GREEN_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6551: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6554: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6557: putstatic       net/minecraft/world/item/Items.GREEN_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6560: getstatic       net/minecraft/world/level/block/Blocks.RED_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6563: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6566: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6569: putstatic       net/minecraft/world/item/Items.RED_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6572: getstatic       net/minecraft/world/level/block/Blocks.BLACK_CONCRETE_POWDER:Lnet/minecraft/world/level/block/Block;
        //  6575: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6578: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6581: putstatic       net/minecraft/world/item/Items.BLACK_CONCRETE_POWDER:Lnet/minecraft/world/item/Item;
        //  6584: getstatic       net/minecraft/world/level/block/Blocks.TURTLE_EGG:Lnet/minecraft/world/level/block/Block;
        //  6587: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        //  6590: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6593: putstatic       net/minecraft/world/item/Items.TURTLE_EGG:Lnet/minecraft/world/item/Item;
        //  6596: getstatic       net/minecraft/world/level/block/Blocks.DEAD_TUBE_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6599: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6602: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6605: putstatic       net/minecraft/world/item/Items.DEAD_TUBE_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6608: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BRAIN_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6611: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6614: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6617: putstatic       net/minecraft/world/item/Items.DEAD_BRAIN_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6620: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BUBBLE_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6623: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6626: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6629: putstatic       net/minecraft/world/item/Items.DEAD_BUBBLE_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6632: getstatic       net/minecraft/world/level/block/Blocks.DEAD_FIRE_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6635: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6638: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6641: putstatic       net/minecraft/world/item/Items.DEAD_FIRE_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6644: getstatic       net/minecraft/world/level/block/Blocks.DEAD_HORN_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6647: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6650: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6653: putstatic       net/minecraft/world/item/Items.DEAD_HORN_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6656: getstatic       net/minecraft/world/level/block/Blocks.TUBE_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6659: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6662: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6665: putstatic       net/minecraft/world/item/Items.TUBE_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6668: getstatic       net/minecraft/world/level/block/Blocks.BRAIN_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6671: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6674: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6677: putstatic       net/minecraft/world/item/Items.BRAIN_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6680: getstatic       net/minecraft/world/level/block/Blocks.BUBBLE_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6683: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6686: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6689: putstatic       net/minecraft/world/item/Items.BUBBLE_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6692: getstatic       net/minecraft/world/level/block/Blocks.FIRE_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6695: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6698: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6701: putstatic       net/minecraft/world/item/Items.FIRE_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6704: getstatic       net/minecraft/world/level/block/Blocks.HORN_CORAL_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  6707: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6710: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6713: putstatic       net/minecraft/world/item/Items.HORN_CORAL_BLOCK:Lnet/minecraft/world/item/Item;
        //  6716: getstatic       net/minecraft/world/level/block/Blocks.TUBE_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6719: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6722: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6725: putstatic       net/minecraft/world/item/Items.TUBE_CORAL:Lnet/minecraft/world/item/Item;
        //  6728: getstatic       net/minecraft/world/level/block/Blocks.BRAIN_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6731: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6734: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6737: putstatic       net/minecraft/world/item/Items.BRAIN_CORAL:Lnet/minecraft/world/item/Item;
        //  6740: getstatic       net/minecraft/world/level/block/Blocks.BUBBLE_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6743: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6746: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6749: putstatic       net/minecraft/world/item/Items.BUBBLE_CORAL:Lnet/minecraft/world/item/Item;
        //  6752: getstatic       net/minecraft/world/level/block/Blocks.FIRE_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6755: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6758: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6761: putstatic       net/minecraft/world/item/Items.FIRE_CORAL:Lnet/minecraft/world/item/Item;
        //  6764: getstatic       net/minecraft/world/level/block/Blocks.HORN_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6767: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6770: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6773: putstatic       net/minecraft/world/item/Items.HORN_CORAL:Lnet/minecraft/world/item/Item;
        //  6776: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BRAIN_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6779: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6782: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6785: putstatic       net/minecraft/world/item/Items.DEAD_BRAIN_CORAL:Lnet/minecraft/world/item/Item;
        //  6788: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BUBBLE_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6791: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6794: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6797: putstatic       net/minecraft/world/item/Items.DEAD_BUBBLE_CORAL:Lnet/minecraft/world/item/Item;
        //  6800: getstatic       net/minecraft/world/level/block/Blocks.DEAD_FIRE_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6803: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6806: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6809: putstatic       net/minecraft/world/item/Items.DEAD_FIRE_CORAL:Lnet/minecraft/world/item/Item;
        //  6812: getstatic       net/minecraft/world/level/block/Blocks.DEAD_HORN_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6815: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6818: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6821: putstatic       net/minecraft/world/item/Items.DEAD_HORN_CORAL:Lnet/minecraft/world/item/Item;
        //  6824: getstatic       net/minecraft/world/level/block/Blocks.DEAD_TUBE_CORAL:Lnet/minecraft/world/level/block/Block;
        //  6827: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6830: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  6833: putstatic       net/minecraft/world/item/Items.DEAD_TUBE_CORAL:Lnet/minecraft/world/item/Item;
        //  6836: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  6839: dup            
        //  6840: getstatic       net/minecraft/world/level/block/Blocks.TUBE_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6843: getstatic       net/minecraft/world/level/block/Blocks.TUBE_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6846: new             Lnet/minecraft/world/item/Item$Properties;
        //  6849: dup            
        //  6850: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  6853: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6856: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  6859: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  6862: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  6865: putstatic       net/minecraft/world/item/Items.TUBE_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  6868: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  6871: dup            
        //  6872: getstatic       net/minecraft/world/level/block/Blocks.BRAIN_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6875: getstatic       net/minecraft/world/level/block/Blocks.BRAIN_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6878: new             Lnet/minecraft/world/item/Item$Properties;
        //  6881: dup            
        //  6882: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  6885: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6888: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  6891: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  6894: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  6897: putstatic       net/minecraft/world/item/Items.BRAIN_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  6900: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  6903: dup            
        //  6904: getstatic       net/minecraft/world/level/block/Blocks.BUBBLE_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6907: getstatic       net/minecraft/world/level/block/Blocks.BUBBLE_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6910: new             Lnet/minecraft/world/item/Item$Properties;
        //  6913: dup            
        //  6914: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  6917: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6920: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  6923: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  6926: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  6929: putstatic       net/minecraft/world/item/Items.BUBBLE_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  6932: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  6935: dup            
        //  6936: getstatic       net/minecraft/world/level/block/Blocks.FIRE_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6939: getstatic       net/minecraft/world/level/block/Blocks.FIRE_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6942: new             Lnet/minecraft/world/item/Item$Properties;
        //  6945: dup            
        //  6946: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  6949: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6952: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  6955: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  6958: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  6961: putstatic       net/minecraft/world/item/Items.FIRE_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  6964: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  6967: dup            
        //  6968: getstatic       net/minecraft/world/level/block/Blocks.HORN_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6971: getstatic       net/minecraft/world/level/block/Blocks.HORN_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  6974: new             Lnet/minecraft/world/item/Item$Properties;
        //  6977: dup            
        //  6978: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  6981: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  6984: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  6987: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  6990: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  6993: putstatic       net/minecraft/world/item/Items.HORN_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  6996: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  6999: dup            
        //  7000: getstatic       net/minecraft/world/level/block/Blocks.DEAD_TUBE_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7003: getstatic       net/minecraft/world/level/block/Blocks.DEAD_TUBE_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7006: new             Lnet/minecraft/world/item/Item$Properties;
        //  7009: dup            
        //  7010: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7013: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7016: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7019: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7022: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7025: putstatic       net/minecraft/world/item/Items.DEAD_TUBE_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  7028: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  7031: dup            
        //  7032: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BRAIN_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7035: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BRAIN_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7038: new             Lnet/minecraft/world/item/Item$Properties;
        //  7041: dup            
        //  7042: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7045: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7048: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7051: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7054: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7057: putstatic       net/minecraft/world/item/Items.DEAD_BRAIN_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  7060: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  7063: dup            
        //  7064: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BUBBLE_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7067: getstatic       net/minecraft/world/level/block/Blocks.DEAD_BUBBLE_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7070: new             Lnet/minecraft/world/item/Item$Properties;
        //  7073: dup            
        //  7074: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7077: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7080: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7083: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7086: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7089: putstatic       net/minecraft/world/item/Items.DEAD_BUBBLE_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  7092: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  7095: dup            
        //  7096: getstatic       net/minecraft/world/level/block/Blocks.DEAD_FIRE_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7099: getstatic       net/minecraft/world/level/block/Blocks.DEAD_FIRE_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7102: new             Lnet/minecraft/world/item/Item$Properties;
        //  7105: dup            
        //  7106: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7109: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7112: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7115: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7118: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7121: putstatic       net/minecraft/world/item/Items.DEAD_FIRE_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  7124: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        //  7127: dup            
        //  7128: getstatic       net/minecraft/world/level/block/Blocks.DEAD_HORN_CORAL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7131: getstatic       net/minecraft/world/level/block/Blocks.DEAD_HORN_CORAL_WALL_FAN:Lnet/minecraft/world/level/block/Block;
        //  7134: new             Lnet/minecraft/world/item/Item$Properties;
        //  7137: dup            
        //  7138: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7141: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7144: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7147: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7150: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7153: putstatic       net/minecraft/world/item/Items.DEAD_HORN_CORAL_FAN:Lnet/minecraft/world/item/Item;
        //  7156: getstatic       net/minecraft/world/level/block/Blocks.BLUE_ICE:Lnet/minecraft/world/level/block/Block;
        //  7159: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7162: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7165: putstatic       net/minecraft/world/item/Items.BLUE_ICE:Lnet/minecraft/world/item/Item;
        //  7168: new             Lnet/minecraft/world/item/BlockItem;
        //  7171: dup            
        //  7172: getstatic       net/minecraft/world/level/block/Blocks.CONDUIT:Lnet/minecraft/world/level/block/Block;
        //  7175: new             Lnet/minecraft/world/item/Item$Properties;
        //  7178: dup            
        //  7179: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7182: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        //  7185: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7188: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        //  7191: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  7194: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7197: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7200: putstatic       net/minecraft/world/item/Items.CONDUIT:Lnet/minecraft/world/item/Item;
        //  7203: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_GRANITE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7206: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7209: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7212: putstatic       net/minecraft/world/item/Items.POLISHED_GRANITE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7215: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_RED_SANDSTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7218: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7221: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7224: putstatic       net/minecraft/world/item/Items.SMOOTH_RED_SANDSTONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7227: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_STONE_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7230: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7233: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7236: putstatic       net/minecraft/world/item/Items.MOSSY_STONE_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  7239: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_DIORITE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7242: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7245: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7248: putstatic       net/minecraft/world/item/Items.POLISHED_DIORITE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7251: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_COBBLESTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7254: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7257: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7260: putstatic       net/minecraft/world/item/Items.MOSSY_COBBLESTONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7263: getstatic       net/minecraft/world/level/block/Blocks.END_STONE_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7266: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7269: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7272: putstatic       net/minecraft/world/item/Items.END_STONE_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  7275: getstatic       net/minecraft/world/level/block/Blocks.STONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7278: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7281: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7284: putstatic       net/minecraft/world/item/Items.STONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7287: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_SANDSTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7290: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7293: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7296: putstatic       net/minecraft/world/item/Items.SMOOTH_SANDSTONE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7299: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_QUARTZ_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7302: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7305: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7308: putstatic       net/minecraft/world/item/Items.SMOOTH_QUARTZ_STAIRS:Lnet/minecraft/world/item/Item;
        //  7311: getstatic       net/minecraft/world/level/block/Blocks.GRANITE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7314: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7317: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7320: putstatic       net/minecraft/world/item/Items.GRANITE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7323: getstatic       net/minecraft/world/level/block/Blocks.ANDESITE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7326: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7329: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7332: putstatic       net/minecraft/world/item/Items.ANDESITE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7335: getstatic       net/minecraft/world/level/block/Blocks.RED_NETHER_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7338: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7341: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7344: putstatic       net/minecraft/world/item/Items.RED_NETHER_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        //  7347: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_ANDESITE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7350: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7353: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7356: putstatic       net/minecraft/world/item/Items.POLISHED_ANDESITE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7359: getstatic       net/minecraft/world/level/block/Blocks.DIORITE_STAIRS:Lnet/minecraft/world/level/block/Block;
        //  7362: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7365: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7368: putstatic       net/minecraft/world/item/Items.DIORITE_STAIRS:Lnet/minecraft/world/item/Item;
        //  7371: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_GRANITE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7374: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7377: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7380: putstatic       net/minecraft/world/item/Items.POLISHED_GRANITE_SLAB:Lnet/minecraft/world/item/Item;
        //  7383: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_RED_SANDSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7386: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7389: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7392: putstatic       net/minecraft/world/item/Items.SMOOTH_RED_SANDSTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  7395: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_STONE_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7398: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7401: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7404: putstatic       net/minecraft/world/item/Items.MOSSY_STONE_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  7407: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_DIORITE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7410: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7413: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7416: putstatic       net/minecraft/world/item/Items.POLISHED_DIORITE_SLAB:Lnet/minecraft/world/item/Item;
        //  7419: getstatic       net/minecraft/world/level/block/Blocks.MOSSY_COBBLESTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7422: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7425: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7428: putstatic       net/minecraft/world/item/Items.MOSSY_COBBLESTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  7431: getstatic       net/minecraft/world/level/block/Blocks.END_STONE_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7434: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7437: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7440: putstatic       net/minecraft/world/item/Items.END_STONE_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  7443: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_SANDSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7446: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7449: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7452: putstatic       net/minecraft/world/item/Items.SMOOTH_SANDSTONE_SLAB:Lnet/minecraft/world/item/Item;
        //  7455: getstatic       net/minecraft/world/level/block/Blocks.SMOOTH_QUARTZ_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7458: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7461: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7464: putstatic       net/minecraft/world/item/Items.SMOOTH_QUARTZ_SLAB:Lnet/minecraft/world/item/Item;
        //  7467: getstatic       net/minecraft/world/level/block/Blocks.GRANITE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7470: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7473: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7476: putstatic       net/minecraft/world/item/Items.GRANITE_SLAB:Lnet/minecraft/world/item/Item;
        //  7479: getstatic       net/minecraft/world/level/block/Blocks.ANDESITE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7482: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7485: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7488: putstatic       net/minecraft/world/item/Items.ANDESITE_SLAB:Lnet/minecraft/world/item/Item;
        //  7491: getstatic       net/minecraft/world/level/block/Blocks.RED_NETHER_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7494: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7497: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7500: putstatic       net/minecraft/world/item/Items.RED_NETHER_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        //  7503: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_ANDESITE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7506: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7509: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7512: putstatic       net/minecraft/world/item/Items.POLISHED_ANDESITE_SLAB:Lnet/minecraft/world/item/Item;
        //  7515: getstatic       net/minecraft/world/level/block/Blocks.DIORITE_SLAB:Lnet/minecraft/world/level/block/Block;
        //  7518: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7521: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7524: putstatic       net/minecraft/world/item/Items.DIORITE_SLAB:Lnet/minecraft/world/item/Item;
        //  7527: new             Lnet/minecraft/world/item/ScaffoldingBlockItem;
        //  7530: dup            
        //  7531: getstatic       net/minecraft/world/level/block/Blocks.SCAFFOLDING:Lnet/minecraft/world/level/block/Block;
        //  7534: new             Lnet/minecraft/world/item/Item$Properties;
        //  7537: dup            
        //  7538: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7541: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7544: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7547: invokespecial   net/minecraft/world/item/ScaffoldingBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7550: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7553: putstatic       net/minecraft/world/item/Items.SCAFFOLDING:Lnet/minecraft/world/item/Item;
        //  7556: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7559: dup            
        //  7560: getstatic       net/minecraft/world/level/block/Blocks.IRON_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7563: new             Lnet/minecraft/world/item/Item$Properties;
        //  7566: dup            
        //  7567: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7570: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7573: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7576: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7579: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7582: putstatic       net/minecraft/world/item/Items.IRON_DOOR:Lnet/minecraft/world/item/Item;
        //  7585: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7588: dup            
        //  7589: getstatic       net/minecraft/world/level/block/Blocks.OAK_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7592: new             Lnet/minecraft/world/item/Item$Properties;
        //  7595: dup            
        //  7596: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7599: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7602: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7605: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7608: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7611: putstatic       net/minecraft/world/item/Items.OAK_DOOR:Lnet/minecraft/world/item/Item;
        //  7614: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7617: dup            
        //  7618: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7621: new             Lnet/minecraft/world/item/Item$Properties;
        //  7624: dup            
        //  7625: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7628: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7631: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7634: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7637: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7640: putstatic       net/minecraft/world/item/Items.SPRUCE_DOOR:Lnet/minecraft/world/item/Item;
        //  7643: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7646: dup            
        //  7647: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7650: new             Lnet/minecraft/world/item/Item$Properties;
        //  7653: dup            
        //  7654: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7657: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7660: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7663: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7666: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7669: putstatic       net/minecraft/world/item/Items.BIRCH_DOOR:Lnet/minecraft/world/item/Item;
        //  7672: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7675: dup            
        //  7676: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7679: new             Lnet/minecraft/world/item/Item$Properties;
        //  7682: dup            
        //  7683: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7686: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7689: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7692: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7695: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7698: putstatic       net/minecraft/world/item/Items.JUNGLE_DOOR:Lnet/minecraft/world/item/Item;
        //  7701: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7704: dup            
        //  7705: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7708: new             Lnet/minecraft/world/item/Item$Properties;
        //  7711: dup            
        //  7712: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7715: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7718: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7721: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7724: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7727: putstatic       net/minecraft/world/item/Items.ACACIA_DOOR:Lnet/minecraft/world/item/Item;
        //  7730: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7733: dup            
        //  7734: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7737: new             Lnet/minecraft/world/item/Item$Properties;
        //  7740: dup            
        //  7741: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7744: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7747: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7750: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7753: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7756: putstatic       net/minecraft/world/item/Items.DARK_OAK_DOOR:Lnet/minecraft/world/item/Item;
        //  7759: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7762: dup            
        //  7763: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7766: new             Lnet/minecraft/world/item/Item$Properties;
        //  7769: dup            
        //  7770: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7773: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7776: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7779: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7782: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7785: putstatic       net/minecraft/world/item/Items.CRIMSON_DOOR:Lnet/minecraft/world/item/Item;
        //  7788: new             Lnet/minecraft/world/item/DoubleHighBlockItem;
        //  7791: dup            
        //  7792: getstatic       net/minecraft/world/level/block/Blocks.WARPED_DOOR:Lnet/minecraft/world/level/block/Block;
        //  7795: new             Lnet/minecraft/world/item/Item$Properties;
        //  7798: dup            
        //  7799: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7802: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7805: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7808: invokespecial   net/minecraft/world/item/DoubleHighBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7811: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7814: putstatic       net/minecraft/world/item/Items.WARPED_DOOR:Lnet/minecraft/world/item/Item;
        //  7817: getstatic       net/minecraft/world/level/block/Blocks.REPEATER:Lnet/minecraft/world/level/block/Block;
        //  7820: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7823: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7826: putstatic       net/minecraft/world/item/Items.REPEATER:Lnet/minecraft/world/item/Item;
        //  7829: getstatic       net/minecraft/world/level/block/Blocks.COMPARATOR:Lnet/minecraft/world/level/block/Block;
        //  7832: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        //  7835: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        //  7838: putstatic       net/minecraft/world/item/Items.COMPARATOR:Lnet/minecraft/world/item/Item;
        //  7841: new             Lnet/minecraft/world/item/GameMasterBlockItem;
        //  7844: dup            
        //  7845: getstatic       net/minecraft/world/level/block/Blocks.STRUCTURE_BLOCK:Lnet/minecraft/world/level/block/Block;
        //  7848: new             Lnet/minecraft/world/item/Item$Properties;
        //  7851: dup            
        //  7852: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7855: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        //  7858: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  7861: invokespecial   net/minecraft/world/item/GameMasterBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7864: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7867: putstatic       net/minecraft/world/item/Items.STRUCTURE_BLOCK:Lnet/minecraft/world/item/Item;
        //  7870: new             Lnet/minecraft/world/item/GameMasterBlockItem;
        //  7873: dup            
        //  7874: getstatic       net/minecraft/world/level/block/Blocks.JIGSAW:Lnet/minecraft/world/level/block/Block;
        //  7877: new             Lnet/minecraft/world/item/Item$Properties;
        //  7880: dup            
        //  7881: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7884: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        //  7887: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        //  7890: invokespecial   net/minecraft/world/item/GameMasterBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  7893: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        //  7896: putstatic       net/minecraft/world/item/Items.JIGSAW:Lnet/minecraft/world/item/Item;
        //  7899: ldc_w           "turtle_helmet"
        //  7902: new             Lnet/minecraft/world/item/ArmorItem;
        //  7905: dup            
        //  7906: getstatic       net/minecraft/world/item/ArmorMaterials.TURTLE:Lnet/minecraft/world/item/ArmorMaterials;
        //  7909: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        //  7912: new             Lnet/minecraft/world/item/Item$Properties;
        //  7915: dup            
        //  7916: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7919: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  7922: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7925: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  7928: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  7931: putstatic       net/minecraft/world/item/Items.TURTLE_HELMET:Lnet/minecraft/world/item/Item;
        //  7934: ldc_w           "scute"
        //  7937: new             Lnet/minecraft/world/item/Item;
        //  7940: dup            
        //  7941: new             Lnet/minecraft/world/item/Item$Properties;
        //  7944: dup            
        //  7945: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7948: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7951: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7954: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  7957: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  7960: putstatic       net/minecraft/world/item/Items.SCUTE:Lnet/minecraft/world/item/Item;
        //  7963: ldc_w           "flint_and_steel"
        //  7966: new             Lnet/minecraft/world/item/FlintAndSteelItem;
        //  7969: dup            
        //  7970: new             Lnet/minecraft/world/item/Item$Properties;
        //  7973: dup            
        //  7974: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  7977: bipush          64
        //  7979: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        //  7982: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  7985: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  7988: invokespecial   net/minecraft/world/item/FlintAndSteelItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  7991: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  7994: putstatic       net/minecraft/world/item/Items.FLINT_AND_STEEL:Lnet/minecraft/world/item/Item;
        //  7997: ldc_w           "apple"
        //  8000: new             Lnet/minecraft/world/item/Item;
        //  8003: dup            
        //  8004: new             Lnet/minecraft/world/item/Item$Properties;
        //  8007: dup            
        //  8008: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8011: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        //  8014: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8017: getstatic       net/minecraft/world/food/Foods.APPLE:Lnet/minecraft/world/food/FoodProperties;
        //  8020: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        //  8023: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8026: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8029: putstatic       net/minecraft/world/item/Items.APPLE:Lnet/minecraft/world/item/Item;
        //  8032: ldc_w           "bow"
        //  8035: new             Lnet/minecraft/world/item/BowItem;
        //  8038: dup            
        //  8039: new             Lnet/minecraft/world/item/Item$Properties;
        //  8042: dup            
        //  8043: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8046: sipush          384
        //  8049: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        //  8052: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  8055: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8058: invokespecial   net/minecraft/world/item/BowItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8061: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8064: putstatic       net/minecraft/world/item/Items.BOW:Lnet/minecraft/world/item/Item;
        //  8067: ldc_w           "arrow"
        //  8070: new             Lnet/minecraft/world/item/ArrowItem;
        //  8073: dup            
        //  8074: new             Lnet/minecraft/world/item/Item$Properties;
        //  8077: dup            
        //  8078: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8081: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  8084: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8087: invokespecial   net/minecraft/world/item/ArrowItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8090: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8093: putstatic       net/minecraft/world/item/Items.ARROW:Lnet/minecraft/world/item/Item;
        //  8096: ldc_w           "coal"
        //  8099: new             Lnet/minecraft/world/item/Item;
        //  8102: dup            
        //  8103: new             Lnet/minecraft/world/item/Item$Properties;
        //  8106: dup            
        //  8107: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8110: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8113: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8116: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8119: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8122: putstatic       net/minecraft/world/item/Items.COAL:Lnet/minecraft/world/item/Item;
        //  8125: ldc_w           "charcoal"
        //  8128: new             Lnet/minecraft/world/item/Item;
        //  8131: dup            
        //  8132: new             Lnet/minecraft/world/item/Item$Properties;
        //  8135: dup            
        //  8136: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8139: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8142: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8145: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8148: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8151: putstatic       net/minecraft/world/item/Items.CHARCOAL:Lnet/minecraft/world/item/Item;
        //  8154: ldc_w           "diamond"
        //  8157: new             Lnet/minecraft/world/item/Item;
        //  8160: dup            
        //  8161: new             Lnet/minecraft/world/item/Item$Properties;
        //  8164: dup            
        //  8165: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8168: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8171: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8174: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8177: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8180: putstatic       net/minecraft/world/item/Items.DIAMOND:Lnet/minecraft/world/item/Item;
        //  8183: ldc_w           "iron_ingot"
        //  8186: new             Lnet/minecraft/world/item/Item;
        //  8189: dup            
        //  8190: new             Lnet/minecraft/world/item/Item$Properties;
        //  8193: dup            
        //  8194: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8197: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8200: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8203: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8206: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8209: putstatic       net/minecraft/world/item/Items.IRON_INGOT:Lnet/minecraft/world/item/Item;
        //  8212: ldc_w           "gold_ingot"
        //  8215: new             Lnet/minecraft/world/item/Item;
        //  8218: dup            
        //  8219: new             Lnet/minecraft/world/item/Item$Properties;
        //  8222: dup            
        //  8223: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8226: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8229: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8232: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8235: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8238: putstatic       net/minecraft/world/item/Items.GOLD_INGOT:Lnet/minecraft/world/item/Item;
        //  8241: ldc_w           "netherite_ingot"
        //  8244: new             Lnet/minecraft/world/item/Item;
        //  8247: dup            
        //  8248: new             Lnet/minecraft/world/item/Item$Properties;
        //  8251: dup            
        //  8252: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8255: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8258: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8261: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  8264: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8267: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8270: putstatic       net/minecraft/world/item/Items.NETHERITE_INGOT:Lnet/minecraft/world/item/Item;
        //  8273: ldc_w           "netherite_scrap"
        //  8276: new             Lnet/minecraft/world/item/Item;
        //  8279: dup            
        //  8280: new             Lnet/minecraft/world/item/Item$Properties;
        //  8283: dup            
        //  8284: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8287: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8290: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8293: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  8296: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  8299: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8302: putstatic       net/minecraft/world/item/Items.NETHERITE_SCRAP:Lnet/minecraft/world/item/Item;
        //  8305: ldc_w           "wooden_sword"
        //  8308: new             Lnet/minecraft/world/item/SwordItem;
        //  8311: dup            
        //  8312: getstatic       net/minecraft/world/item/Tiers.WOOD:Lnet/minecraft/world/item/Tiers;
        //  8315: iconst_3       
        //  8316: ldc_w           -2.4
        //  8319: new             Lnet/minecraft/world/item/Item$Properties;
        //  8322: dup            
        //  8323: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8326: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  8329: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8332: invokespecial   net/minecraft/world/item/SwordItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8335: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8338: putstatic       net/minecraft/world/item/Items.WOODEN_SWORD:Lnet/minecraft/world/item/Item;
        //  8341: ldc_w           "wooden_shovel"
        //  8344: new             Lnet/minecraft/world/item/ShovelItem;
        //  8347: dup            
        //  8348: getstatic       net/minecraft/world/item/Tiers.WOOD:Lnet/minecraft/world/item/Tiers;
        //  8351: ldc_w           1.5
        //  8354: ldc_w           -3.0
        //  8357: new             Lnet/minecraft/world/item/Item$Properties;
        //  8360: dup            
        //  8361: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8364: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8367: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8370: invokespecial   net/minecraft/world/item/ShovelItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8373: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8376: putstatic       net/minecraft/world/item/Items.WOODEN_SHOVEL:Lnet/minecraft/world/item/Item;
        //  8379: ldc_w           "wooden_pickaxe"
        //  8382: new             Lnet/minecraft/world/item/PickaxeItem;
        //  8385: dup            
        //  8386: getstatic       net/minecraft/world/item/Tiers.WOOD:Lnet/minecraft/world/item/Tiers;
        //  8389: iconst_1       
        //  8390: ldc_w           -2.8
        //  8393: new             Lnet/minecraft/world/item/Item$Properties;
        //  8396: dup            
        //  8397: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8400: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8403: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8406: invokespecial   net/minecraft/world/item/PickaxeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8409: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8412: putstatic       net/minecraft/world/item/Items.WOODEN_PICKAXE:Lnet/minecraft/world/item/Item;
        //  8415: ldc_w           "wooden_axe"
        //  8418: new             Lnet/minecraft/world/item/AxeItem;
        //  8421: dup            
        //  8422: getstatic       net/minecraft/world/item/Tiers.WOOD:Lnet/minecraft/world/item/Tiers;
        //  8425: ldc_w           6.0
        //  8428: ldc_w           -3.2
        //  8431: new             Lnet/minecraft/world/item/Item$Properties;
        //  8434: dup            
        //  8435: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8438: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8441: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8444: invokespecial   net/minecraft/world/item/AxeItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8447: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8450: putstatic       net/minecraft/world/item/Items.WOODEN_AXE:Lnet/minecraft/world/item/Item;
        //  8453: ldc_w           "wooden_hoe"
        //  8456: new             Lnet/minecraft/world/item/HoeItem;
        //  8459: dup            
        //  8460: getstatic       net/minecraft/world/item/Tiers.WOOD:Lnet/minecraft/world/item/Tiers;
        //  8463: iconst_0       
        //  8464: ldc_w           -3.0
        //  8467: new             Lnet/minecraft/world/item/Item$Properties;
        //  8470: dup            
        //  8471: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8474: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8477: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8480: invokespecial   net/minecraft/world/item/HoeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8483: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8486: putstatic       net/minecraft/world/item/Items.WOODEN_HOE:Lnet/minecraft/world/item/Item;
        //  8489: ldc_w           "stone_sword"
        //  8492: new             Lnet/minecraft/world/item/SwordItem;
        //  8495: dup            
        //  8496: getstatic       net/minecraft/world/item/Tiers.STONE:Lnet/minecraft/world/item/Tiers;
        //  8499: iconst_3       
        //  8500: ldc_w           -2.4
        //  8503: new             Lnet/minecraft/world/item/Item$Properties;
        //  8506: dup            
        //  8507: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8510: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  8513: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8516: invokespecial   net/minecraft/world/item/SwordItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8519: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8522: putstatic       net/minecraft/world/item/Items.STONE_SWORD:Lnet/minecraft/world/item/Item;
        //  8525: ldc_w           "stone_shovel"
        //  8528: new             Lnet/minecraft/world/item/ShovelItem;
        //  8531: dup            
        //  8532: getstatic       net/minecraft/world/item/Tiers.STONE:Lnet/minecraft/world/item/Tiers;
        //  8535: ldc_w           1.5
        //  8538: ldc_w           -3.0
        //  8541: new             Lnet/minecraft/world/item/Item$Properties;
        //  8544: dup            
        //  8545: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8548: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8551: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8554: invokespecial   net/minecraft/world/item/ShovelItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8557: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8560: putstatic       net/minecraft/world/item/Items.STONE_SHOVEL:Lnet/minecraft/world/item/Item;
        //  8563: ldc_w           "stone_pickaxe"
        //  8566: new             Lnet/minecraft/world/item/PickaxeItem;
        //  8569: dup            
        //  8570: getstatic       net/minecraft/world/item/Tiers.STONE:Lnet/minecraft/world/item/Tiers;
        //  8573: iconst_1       
        //  8574: ldc_w           -2.8
        //  8577: new             Lnet/minecraft/world/item/Item$Properties;
        //  8580: dup            
        //  8581: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8584: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8587: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8590: invokespecial   net/minecraft/world/item/PickaxeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8593: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8596: putstatic       net/minecraft/world/item/Items.STONE_PICKAXE:Lnet/minecraft/world/item/Item;
        //  8599: ldc_w           "stone_axe"
        //  8602: new             Lnet/minecraft/world/item/AxeItem;
        //  8605: dup            
        //  8606: getstatic       net/minecraft/world/item/Tiers.STONE:Lnet/minecraft/world/item/Tiers;
        //  8609: ldc_w           7.0
        //  8612: ldc_w           -3.2
        //  8615: new             Lnet/minecraft/world/item/Item$Properties;
        //  8618: dup            
        //  8619: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8622: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8625: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8628: invokespecial   net/minecraft/world/item/AxeItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8631: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8634: putstatic       net/minecraft/world/item/Items.STONE_AXE:Lnet/minecraft/world/item/Item;
        //  8637: ldc_w           "stone_hoe"
        //  8640: new             Lnet/minecraft/world/item/HoeItem;
        //  8643: dup            
        //  8644: getstatic       net/minecraft/world/item/Tiers.STONE:Lnet/minecraft/world/item/Tiers;
        //  8647: iconst_m1      
        //  8648: ldc_w           -2.0
        //  8651: new             Lnet/minecraft/world/item/Item$Properties;
        //  8654: dup            
        //  8655: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8658: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8661: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8664: invokespecial   net/minecraft/world/item/HoeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8667: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8670: putstatic       net/minecraft/world/item/Items.STONE_HOE:Lnet/minecraft/world/item/Item;
        //  8673: ldc_w           "golden_sword"
        //  8676: new             Lnet/minecraft/world/item/SwordItem;
        //  8679: dup            
        //  8680: getstatic       net/minecraft/world/item/Tiers.GOLD:Lnet/minecraft/world/item/Tiers;
        //  8683: iconst_3       
        //  8684: ldc_w           -2.4
        //  8687: new             Lnet/minecraft/world/item/Item$Properties;
        //  8690: dup            
        //  8691: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8694: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  8697: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8700: invokespecial   net/minecraft/world/item/SwordItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8703: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8706: putstatic       net/minecraft/world/item/Items.GOLDEN_SWORD:Lnet/minecraft/world/item/Item;
        //  8709: ldc_w           "golden_shovel"
        //  8712: new             Lnet/minecraft/world/item/ShovelItem;
        //  8715: dup            
        //  8716: getstatic       net/minecraft/world/item/Tiers.GOLD:Lnet/minecraft/world/item/Tiers;
        //  8719: ldc_w           1.5
        //  8722: ldc_w           -3.0
        //  8725: new             Lnet/minecraft/world/item/Item$Properties;
        //  8728: dup            
        //  8729: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8732: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8735: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8738: invokespecial   net/minecraft/world/item/ShovelItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8741: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8744: putstatic       net/minecraft/world/item/Items.GOLDEN_SHOVEL:Lnet/minecraft/world/item/Item;
        //  8747: ldc_w           "golden_pickaxe"
        //  8750: new             Lnet/minecraft/world/item/PickaxeItem;
        //  8753: dup            
        //  8754: getstatic       net/minecraft/world/item/Tiers.GOLD:Lnet/minecraft/world/item/Tiers;
        //  8757: iconst_1       
        //  8758: ldc_w           -2.8
        //  8761: new             Lnet/minecraft/world/item/Item$Properties;
        //  8764: dup            
        //  8765: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8768: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8771: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8774: invokespecial   net/minecraft/world/item/PickaxeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8777: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8780: putstatic       net/minecraft/world/item/Items.GOLDEN_PICKAXE:Lnet/minecraft/world/item/Item;
        //  8783: ldc_w           "golden_axe"
        //  8786: new             Lnet/minecraft/world/item/AxeItem;
        //  8789: dup            
        //  8790: getstatic       net/minecraft/world/item/Tiers.GOLD:Lnet/minecraft/world/item/Tiers;
        //  8793: ldc_w           6.0
        //  8796: ldc_w           -3.0
        //  8799: new             Lnet/minecraft/world/item/Item$Properties;
        //  8802: dup            
        //  8803: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8806: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8809: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8812: invokespecial   net/minecraft/world/item/AxeItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8815: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8818: putstatic       net/minecraft/world/item/Items.GOLDEN_AXE:Lnet/minecraft/world/item/Item;
        //  8821: ldc_w           "golden_hoe"
        //  8824: new             Lnet/minecraft/world/item/HoeItem;
        //  8827: dup            
        //  8828: getstatic       net/minecraft/world/item/Tiers.GOLD:Lnet/minecraft/world/item/Tiers;
        //  8831: iconst_0       
        //  8832: ldc_w           -3.0
        //  8835: new             Lnet/minecraft/world/item/Item$Properties;
        //  8838: dup            
        //  8839: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8842: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8845: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8848: invokespecial   net/minecraft/world/item/HoeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8851: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8854: putstatic       net/minecraft/world/item/Items.GOLDEN_HOE:Lnet/minecraft/world/item/Item;
        //  8857: ldc_w           "iron_sword"
        //  8860: new             Lnet/minecraft/world/item/SwordItem;
        //  8863: dup            
        //  8864: getstatic       net/minecraft/world/item/Tiers.IRON:Lnet/minecraft/world/item/Tiers;
        //  8867: iconst_3       
        //  8868: ldc_w           -2.4
        //  8871: new             Lnet/minecraft/world/item/Item$Properties;
        //  8874: dup            
        //  8875: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8878: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  8881: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8884: invokespecial   net/minecraft/world/item/SwordItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8887: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8890: putstatic       net/minecraft/world/item/Items.IRON_SWORD:Lnet/minecraft/world/item/Item;
        //  8893: ldc_w           "iron_shovel"
        //  8896: new             Lnet/minecraft/world/item/ShovelItem;
        //  8899: dup            
        //  8900: getstatic       net/minecraft/world/item/Tiers.IRON:Lnet/minecraft/world/item/Tiers;
        //  8903: ldc_w           1.5
        //  8906: ldc_w           -3.0
        //  8909: new             Lnet/minecraft/world/item/Item$Properties;
        //  8912: dup            
        //  8913: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8916: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8919: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8922: invokespecial   net/minecraft/world/item/ShovelItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8925: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8928: putstatic       net/minecraft/world/item/Items.IRON_SHOVEL:Lnet/minecraft/world/item/Item;
        //  8931: ldc_w           "iron_pickaxe"
        //  8934: new             Lnet/minecraft/world/item/PickaxeItem;
        //  8937: dup            
        //  8938: getstatic       net/minecraft/world/item/Tiers.IRON:Lnet/minecraft/world/item/Tiers;
        //  8941: iconst_1       
        //  8942: ldc_w           -2.8
        //  8945: new             Lnet/minecraft/world/item/Item$Properties;
        //  8948: dup            
        //  8949: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8952: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8955: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8958: invokespecial   net/minecraft/world/item/PickaxeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  8961: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  8964: putstatic       net/minecraft/world/item/Items.IRON_PICKAXE:Lnet/minecraft/world/item/Item;
        //  8967: ldc_w           "iron_axe"
        //  8970: new             Lnet/minecraft/world/item/AxeItem;
        //  8973: dup            
        //  8974: getstatic       net/minecraft/world/item/Tiers.IRON:Lnet/minecraft/world/item/Tiers;
        //  8977: ldc_w           6.0
        //  8980: ldc_w           -3.1
        //  8983: new             Lnet/minecraft/world/item/Item$Properties;
        //  8986: dup            
        //  8987: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  8990: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  8993: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  8996: invokespecial   net/minecraft/world/item/AxeItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  8999: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9002: putstatic       net/minecraft/world/item/Items.IRON_AXE:Lnet/minecraft/world/item/Item;
        //  9005: ldc_w           "iron_hoe"
        //  9008: new             Lnet/minecraft/world/item/HoeItem;
        //  9011: dup            
        //  9012: getstatic       net/minecraft/world/item/Tiers.IRON:Lnet/minecraft/world/item/Tiers;
        //  9015: bipush          -2
        //  9017: ldc_w           -1.0
        //  9020: new             Lnet/minecraft/world/item/Item$Properties;
        //  9023: dup            
        //  9024: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9027: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9030: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9033: invokespecial   net/minecraft/world/item/HoeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9036: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9039: putstatic       net/minecraft/world/item/Items.IRON_HOE:Lnet/minecraft/world/item/Item;
        //  9042: ldc_w           "diamond_sword"
        //  9045: new             Lnet/minecraft/world/item/SwordItem;
        //  9048: dup            
        //  9049: getstatic       net/minecraft/world/item/Tiers.DIAMOND:Lnet/minecraft/world/item/Tiers;
        //  9052: iconst_3       
        //  9053: ldc_w           -2.4
        //  9056: new             Lnet/minecraft/world/item/Item$Properties;
        //  9059: dup            
        //  9060: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9063: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9066: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9069: invokespecial   net/minecraft/world/item/SwordItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9072: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9075: putstatic       net/minecraft/world/item/Items.DIAMOND_SWORD:Lnet/minecraft/world/item/Item;
        //  9078: ldc_w           "diamond_shovel"
        //  9081: new             Lnet/minecraft/world/item/ShovelItem;
        //  9084: dup            
        //  9085: getstatic       net/minecraft/world/item/Tiers.DIAMOND:Lnet/minecraft/world/item/Tiers;
        //  9088: ldc_w           1.5
        //  9091: ldc_w           -3.0
        //  9094: new             Lnet/minecraft/world/item/Item$Properties;
        //  9097: dup            
        //  9098: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9101: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9104: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9107: invokespecial   net/minecraft/world/item/ShovelItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  9110: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9113: putstatic       net/minecraft/world/item/Items.DIAMOND_SHOVEL:Lnet/minecraft/world/item/Item;
        //  9116: ldc_w           "diamond_pickaxe"
        //  9119: new             Lnet/minecraft/world/item/PickaxeItem;
        //  9122: dup            
        //  9123: getstatic       net/minecraft/world/item/Tiers.DIAMOND:Lnet/minecraft/world/item/Tiers;
        //  9126: iconst_1       
        //  9127: ldc_w           -2.8
        //  9130: new             Lnet/minecraft/world/item/Item$Properties;
        //  9133: dup            
        //  9134: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9137: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9140: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9143: invokespecial   net/minecraft/world/item/PickaxeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9146: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9149: putstatic       net/minecraft/world/item/Items.DIAMOND_PICKAXE:Lnet/minecraft/world/item/Item;
        //  9152: ldc_w           "diamond_axe"
        //  9155: new             Lnet/minecraft/world/item/AxeItem;
        //  9158: dup            
        //  9159: getstatic       net/minecraft/world/item/Tiers.DIAMOND:Lnet/minecraft/world/item/Tiers;
        //  9162: ldc_w           5.0
        //  9165: ldc_w           -3.0
        //  9168: new             Lnet/minecraft/world/item/Item$Properties;
        //  9171: dup            
        //  9172: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9175: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9178: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9181: invokespecial   net/minecraft/world/item/AxeItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  9184: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9187: putstatic       net/minecraft/world/item/Items.DIAMOND_AXE:Lnet/minecraft/world/item/Item;
        //  9190: ldc_w           "diamond_hoe"
        //  9193: new             Lnet/minecraft/world/item/HoeItem;
        //  9196: dup            
        //  9197: getstatic       net/minecraft/world/item/Tiers.DIAMOND:Lnet/minecraft/world/item/Tiers;
        //  9200: bipush          -3
        //  9202: fconst_0       
        //  9203: new             Lnet/minecraft/world/item/Item$Properties;
        //  9206: dup            
        //  9207: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9210: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9213: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9216: invokespecial   net/minecraft/world/item/HoeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9219: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9222: putstatic       net/minecraft/world/item/Items.DIAMOND_HOE:Lnet/minecraft/world/item/Item;
        //  9225: ldc_w           "netherite_sword"
        //  9228: new             Lnet/minecraft/world/item/SwordItem;
        //  9231: dup            
        //  9232: getstatic       net/minecraft/world/item/Tiers.NETHERITE:Lnet/minecraft/world/item/Tiers;
        //  9235: iconst_3       
        //  9236: ldc_w           -2.4
        //  9239: new             Lnet/minecraft/world/item/Item$Properties;
        //  9242: dup            
        //  9243: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9246: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9249: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9252: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  9255: invokespecial   net/minecraft/world/item/SwordItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9258: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9261: putstatic       net/minecraft/world/item/Items.NETHERITE_SWORD:Lnet/minecraft/world/item/Item;
        //  9264: ldc_w           "netherite_shovel"
        //  9267: new             Lnet/minecraft/world/item/ShovelItem;
        //  9270: dup            
        //  9271: getstatic       net/minecraft/world/item/Tiers.NETHERITE:Lnet/minecraft/world/item/Tiers;
        //  9274: ldc_w           1.5
        //  9277: ldc_w           -3.0
        //  9280: new             Lnet/minecraft/world/item/Item$Properties;
        //  9283: dup            
        //  9284: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9287: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9290: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9293: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  9296: invokespecial   net/minecraft/world/item/ShovelItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  9299: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9302: putstatic       net/minecraft/world/item/Items.NETHERITE_SHOVEL:Lnet/minecraft/world/item/Item;
        //  9305: ldc_w           "netherite_pickaxe"
        //  9308: new             Lnet/minecraft/world/item/PickaxeItem;
        //  9311: dup            
        //  9312: getstatic       net/minecraft/world/item/Tiers.NETHERITE:Lnet/minecraft/world/item/Tiers;
        //  9315: iconst_1       
        //  9316: ldc_w           -2.8
        //  9319: new             Lnet/minecraft/world/item/Item$Properties;
        //  9322: dup            
        //  9323: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9326: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9329: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9332: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  9335: invokespecial   net/minecraft/world/item/PickaxeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9338: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9341: putstatic       net/minecraft/world/item/Items.NETHERITE_PICKAXE:Lnet/minecraft/world/item/Item;
        //  9344: ldc_w           "netherite_axe"
        //  9347: new             Lnet/minecraft/world/item/AxeItem;
        //  9350: dup            
        //  9351: getstatic       net/minecraft/world/item/Tiers.NETHERITE:Lnet/minecraft/world/item/Tiers;
        //  9354: ldc_w           5.0
        //  9357: ldc_w           -3.0
        //  9360: new             Lnet/minecraft/world/item/Item$Properties;
        //  9363: dup            
        //  9364: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9367: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9370: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9373: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  9376: invokespecial   net/minecraft/world/item/AxeItem.<init>:(Lnet/minecraft/world/item/Tier;FFLnet/minecraft/world/item/Item$Properties;)V
        //  9379: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9382: putstatic       net/minecraft/world/item/Items.NETHERITE_AXE:Lnet/minecraft/world/item/Item;
        //  9385: ldc_w           "netherite_hoe"
        //  9388: new             Lnet/minecraft/world/item/HoeItem;
        //  9391: dup            
        //  9392: getstatic       net/minecraft/world/item/Tiers.NETHERITE:Lnet/minecraft/world/item/Tiers;
        //  9395: bipush          -4
        //  9397: fconst_0       
        //  9398: new             Lnet/minecraft/world/item/Item$Properties;
        //  9401: dup            
        //  9402: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9405: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9408: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9411: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        //  9414: invokespecial   net/minecraft/world/item/HoeItem.<init>:(Lnet/minecraft/world/item/Tier;IFLnet/minecraft/world/item/Item$Properties;)V
        //  9417: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9420: putstatic       net/minecraft/world/item/Items.NETHERITE_HOE:Lnet/minecraft/world/item/Item;
        //  9423: ldc_w           "stick"
        //  9426: new             Lnet/minecraft/world/item/Item;
        //  9429: dup            
        //  9430: new             Lnet/minecraft/world/item/Item$Properties;
        //  9433: dup            
        //  9434: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9437: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9440: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9443: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9446: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9449: putstatic       net/minecraft/world/item/Items.STICK:Lnet/minecraft/world/item/Item;
        //  9452: ldc_w           "bowl"
        //  9455: new             Lnet/minecraft/world/item/Item;
        //  9458: dup            
        //  9459: new             Lnet/minecraft/world/item/Item$Properties;
        //  9462: dup            
        //  9463: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9466: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9469: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9472: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9475: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9478: putstatic       net/minecraft/world/item/Items.BOWL:Lnet/minecraft/world/item/Item;
        //  9481: ldc_w           "mushroom_stew"
        //  9484: new             Lnet/minecraft/world/item/BowlFoodItem;
        //  9487: dup            
        //  9488: new             Lnet/minecraft/world/item/Item$Properties;
        //  9491: dup            
        //  9492: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9495: iconst_1       
        //  9496: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        //  9499: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        //  9502: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9505: getstatic       net/minecraft/world/food/Foods.MUSHROOM_STEW:Lnet/minecraft/world/food/FoodProperties;
        //  9508: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        //  9511: invokespecial   net/minecraft/world/item/BowlFoodItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9514: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9517: putstatic       net/minecraft/world/item/Items.MUSHROOM_STEW:Lnet/minecraft/world/item/Item;
        //  9520: ldc_w           "string"
        //  9523: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        //  9526: dup            
        //  9527: getstatic       net/minecraft/world/level/block/Blocks.TRIPWIRE:Lnet/minecraft/world/level/block/Block;
        //  9530: new             Lnet/minecraft/world/item/Item$Properties;
        //  9533: dup            
        //  9534: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9537: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        //  9540: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9543: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  9546: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9549: putstatic       net/minecraft/world/item/Items.STRING:Lnet/minecraft/world/item/Item;
        //  9552: ldc_w           "feather"
        //  9555: new             Lnet/minecraft/world/item/Item;
        //  9558: dup            
        //  9559: new             Lnet/minecraft/world/item/Item$Properties;
        //  9562: dup            
        //  9563: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9566: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9569: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9572: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9575: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9578: putstatic       net/minecraft/world/item/Items.FEATHER:Lnet/minecraft/world/item/Item;
        //  9581: ldc_w           "gunpowder"
        //  9584: new             Lnet/minecraft/world/item/Item;
        //  9587: dup            
        //  9588: new             Lnet/minecraft/world/item/Item$Properties;
        //  9591: dup            
        //  9592: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9595: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9598: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9601: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9604: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9607: putstatic       net/minecraft/world/item/Items.GUNPOWDER:Lnet/minecraft/world/item/Item;
        //  9610: ldc_w           "wheat_seeds"
        //  9613: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        //  9616: dup            
        //  9617: getstatic       net/minecraft/world/level/block/Blocks.WHEAT:Lnet/minecraft/world/level/block/Block;
        //  9620: new             Lnet/minecraft/world/item/Item$Properties;
        //  9623: dup            
        //  9624: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9627: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9630: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9633: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        //  9636: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9639: putstatic       net/minecraft/world/item/Items.WHEAT_SEEDS:Lnet/minecraft/world/item/Item;
        //  9642: ldc_w           "wheat"
        //  9645: new             Lnet/minecraft/world/item/Item;
        //  9648: dup            
        //  9649: new             Lnet/minecraft/world/item/Item$Properties;
        //  9652: dup            
        //  9653: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9656: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        //  9659: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9662: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9665: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9668: putstatic       net/minecraft/world/item/Items.WHEAT:Lnet/minecraft/world/item/Item;
        //  9671: ldc_w           "bread"
        //  9674: new             Lnet/minecraft/world/item/Item;
        //  9677: dup            
        //  9678: new             Lnet/minecraft/world/item/Item$Properties;
        //  9681: dup            
        //  9682: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9685: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        //  9688: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9691: getstatic       net/minecraft/world/food/Foods.BREAD:Lnet/minecraft/world/food/FoodProperties;
        //  9694: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        //  9697: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        //  9700: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9703: putstatic       net/minecraft/world/item/Items.BREAD:Lnet/minecraft/world/item/Item;
        //  9706: ldc_w           "leather_helmet"
        //  9709: new             Lnet/minecraft/world/item/DyeableArmorItem;
        //  9712: dup            
        //  9713: getstatic       net/minecraft/world/item/ArmorMaterials.LEATHER:Lnet/minecraft/world/item/ArmorMaterials;
        //  9716: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9719: new             Lnet/minecraft/world/item/Item$Properties;
        //  9722: dup            
        //  9723: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9726: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9729: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9732: invokespecial   net/minecraft/world/item/DyeableArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9735: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9738: putstatic       net/minecraft/world/item/Items.LEATHER_HELMET:Lnet/minecraft/world/item/Item;
        //  9741: ldc_w           "leather_chestplate"
        //  9744: new             Lnet/minecraft/world/item/DyeableArmorItem;
        //  9747: dup            
        //  9748: getstatic       net/minecraft/world/item/ArmorMaterials.LEATHER:Lnet/minecraft/world/item/ArmorMaterials;
        //  9751: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9754: new             Lnet/minecraft/world/item/Item$Properties;
        //  9757: dup            
        //  9758: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9761: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9764: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9767: invokespecial   net/minecraft/world/item/DyeableArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9770: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9773: putstatic       net/minecraft/world/item/Items.LEATHER_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  9776: ldc_w           "leather_leggings"
        //  9779: new             Lnet/minecraft/world/item/DyeableArmorItem;
        //  9782: dup            
        //  9783: getstatic       net/minecraft/world/item/ArmorMaterials.LEATHER:Lnet/minecraft/world/item/ArmorMaterials;
        //  9786: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9789: new             Lnet/minecraft/world/item/Item$Properties;
        //  9792: dup            
        //  9793: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9796: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9799: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9802: invokespecial   net/minecraft/world/item/DyeableArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9805: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9808: putstatic       net/minecraft/world/item/Items.LEATHER_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  9811: ldc_w           "leather_boots"
        //  9814: new             Lnet/minecraft/world/item/DyeableArmorItem;
        //  9817: dup            
        //  9818: getstatic       net/minecraft/world/item/ArmorMaterials.LEATHER:Lnet/minecraft/world/item/ArmorMaterials;
        //  9821: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9824: new             Lnet/minecraft/world/item/Item$Properties;
        //  9827: dup            
        //  9828: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9831: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9834: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9837: invokespecial   net/minecraft/world/item/DyeableArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9840: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9843: putstatic       net/minecraft/world/item/Items.LEATHER_BOOTS:Lnet/minecraft/world/item/Item;
        //  9846: ldc_w           "chainmail_helmet"
        //  9849: new             Lnet/minecraft/world/item/ArmorItem;
        //  9852: dup            
        //  9853: getstatic       net/minecraft/world/item/ArmorMaterials.CHAIN:Lnet/minecraft/world/item/ArmorMaterials;
        //  9856: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9859: new             Lnet/minecraft/world/item/Item$Properties;
        //  9862: dup            
        //  9863: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9866: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9869: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9872: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9875: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9878: putstatic       net/minecraft/world/item/Items.CHAINMAIL_HELMET:Lnet/minecraft/world/item/Item;
        //  9881: ldc_w           "chainmail_chestplate"
        //  9884: new             Lnet/minecraft/world/item/ArmorItem;
        //  9887: dup            
        //  9888: getstatic       net/minecraft/world/item/ArmorMaterials.CHAIN:Lnet/minecraft/world/item/ArmorMaterials;
        //  9891: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9894: new             Lnet/minecraft/world/item/Item$Properties;
        //  9897: dup            
        //  9898: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9901: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9904: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9907: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9910: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9913: putstatic       net/minecraft/world/item/Items.CHAINMAIL_CHESTPLATE:Lnet/minecraft/world/item/Item;
        //  9916: ldc_w           "chainmail_leggings"
        //  9919: new             Lnet/minecraft/world/item/ArmorItem;
        //  9922: dup            
        //  9923: getstatic       net/minecraft/world/item/ArmorMaterials.CHAIN:Lnet/minecraft/world/item/ArmorMaterials;
        //  9926: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9929: new             Lnet/minecraft/world/item/Item$Properties;
        //  9932: dup            
        //  9933: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9936: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9939: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9942: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9945: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9948: putstatic       net/minecraft/world/item/Items.CHAINMAIL_LEGGINGS:Lnet/minecraft/world/item/Item;
        //  9951: ldc_w           "chainmail_boots"
        //  9954: new             Lnet/minecraft/world/item/ArmorItem;
        //  9957: dup            
        //  9958: getstatic       net/minecraft/world/item/ArmorMaterials.CHAIN:Lnet/minecraft/world/item/ArmorMaterials;
        //  9961: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9964: new             Lnet/minecraft/world/item/Item$Properties;
        //  9967: dup            
        //  9968: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        //  9971: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        //  9974: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        //  9977: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        //  9980: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        //  9983: putstatic       net/minecraft/world/item/Items.CHAINMAIL_BOOTS:Lnet/minecraft/world/item/Item;
        //  9986: ldc_w           "iron_helmet"
        //  9989: new             Lnet/minecraft/world/item/ArmorItem;
        //  9992: dup            
        //  9993: getstatic       net/minecraft/world/item/ArmorMaterials.IRON:Lnet/minecraft/world/item/ArmorMaterials;
        //  9996: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        //  9999: new             Lnet/minecraft/world/item/Item$Properties;
        // 10002: dup            
        // 10003: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10006: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10009: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10012: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10015: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10018: putstatic       net/minecraft/world/item/Items.IRON_HELMET:Lnet/minecraft/world/item/Item;
        // 10021: ldc_w           "iron_chestplate"
        // 10024: new             Lnet/minecraft/world/item/ArmorItem;
        // 10027: dup            
        // 10028: getstatic       net/minecraft/world/item/ArmorMaterials.IRON:Lnet/minecraft/world/item/ArmorMaterials;
        // 10031: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10034: new             Lnet/minecraft/world/item/Item$Properties;
        // 10037: dup            
        // 10038: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10041: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10044: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10047: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10050: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10053: putstatic       net/minecraft/world/item/Items.IRON_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 10056: ldc_w           "iron_leggings"
        // 10059: new             Lnet/minecraft/world/item/ArmorItem;
        // 10062: dup            
        // 10063: getstatic       net/minecraft/world/item/ArmorMaterials.IRON:Lnet/minecraft/world/item/ArmorMaterials;
        // 10066: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10069: new             Lnet/minecraft/world/item/Item$Properties;
        // 10072: dup            
        // 10073: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10076: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10079: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10082: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10085: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10088: putstatic       net/minecraft/world/item/Items.IRON_LEGGINGS:Lnet/minecraft/world/item/Item;
        // 10091: ldc_w           "iron_boots"
        // 10094: new             Lnet/minecraft/world/item/ArmorItem;
        // 10097: dup            
        // 10098: getstatic       net/minecraft/world/item/ArmorMaterials.IRON:Lnet/minecraft/world/item/ArmorMaterials;
        // 10101: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10104: new             Lnet/minecraft/world/item/Item$Properties;
        // 10107: dup            
        // 10108: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10111: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10114: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10117: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10120: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10123: putstatic       net/minecraft/world/item/Items.IRON_BOOTS:Lnet/minecraft/world/item/Item;
        // 10126: ldc_w           "diamond_helmet"
        // 10129: new             Lnet/minecraft/world/item/ArmorItem;
        // 10132: dup            
        // 10133: getstatic       net/minecraft/world/item/ArmorMaterials.DIAMOND:Lnet/minecraft/world/item/ArmorMaterials;
        // 10136: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10139: new             Lnet/minecraft/world/item/Item$Properties;
        // 10142: dup            
        // 10143: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10146: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10149: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10152: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10155: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10158: putstatic       net/minecraft/world/item/Items.DIAMOND_HELMET:Lnet/minecraft/world/item/Item;
        // 10161: ldc_w           "diamond_chestplate"
        // 10164: new             Lnet/minecraft/world/item/ArmorItem;
        // 10167: dup            
        // 10168: getstatic       net/minecraft/world/item/ArmorMaterials.DIAMOND:Lnet/minecraft/world/item/ArmorMaterials;
        // 10171: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10174: new             Lnet/minecraft/world/item/Item$Properties;
        // 10177: dup            
        // 10178: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10181: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10184: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10187: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10190: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10193: putstatic       net/minecraft/world/item/Items.DIAMOND_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 10196: ldc_w           "diamond_leggings"
        // 10199: new             Lnet/minecraft/world/item/ArmorItem;
        // 10202: dup            
        // 10203: getstatic       net/minecraft/world/item/ArmorMaterials.DIAMOND:Lnet/minecraft/world/item/ArmorMaterials;
        // 10206: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10209: new             Lnet/minecraft/world/item/Item$Properties;
        // 10212: dup            
        // 10213: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10216: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10219: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10222: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10225: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10228: putstatic       net/minecraft/world/item/Items.DIAMOND_LEGGINGS:Lnet/minecraft/world/item/Item;
        // 10231: ldc_w           "diamond_boots"
        // 10234: new             Lnet/minecraft/world/item/ArmorItem;
        // 10237: dup            
        // 10238: getstatic       net/minecraft/world/item/ArmorMaterials.DIAMOND:Lnet/minecraft/world/item/ArmorMaterials;
        // 10241: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10244: new             Lnet/minecraft/world/item/Item$Properties;
        // 10247: dup            
        // 10248: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10251: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10254: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10257: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10260: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10263: putstatic       net/minecraft/world/item/Items.DIAMOND_BOOTS:Lnet/minecraft/world/item/Item;
        // 10266: ldc_w           "golden_helmet"
        // 10269: new             Lnet/minecraft/world/item/ArmorItem;
        // 10272: dup            
        // 10273: getstatic       net/minecraft/world/item/ArmorMaterials.GOLD:Lnet/minecraft/world/item/ArmorMaterials;
        // 10276: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10279: new             Lnet/minecraft/world/item/Item$Properties;
        // 10282: dup            
        // 10283: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10286: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10289: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10292: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10295: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10298: putstatic       net/minecraft/world/item/Items.GOLDEN_HELMET:Lnet/minecraft/world/item/Item;
        // 10301: ldc_w           "golden_chestplate"
        // 10304: new             Lnet/minecraft/world/item/ArmorItem;
        // 10307: dup            
        // 10308: getstatic       net/minecraft/world/item/ArmorMaterials.GOLD:Lnet/minecraft/world/item/ArmorMaterials;
        // 10311: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10314: new             Lnet/minecraft/world/item/Item$Properties;
        // 10317: dup            
        // 10318: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10321: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10324: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10327: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10330: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10333: putstatic       net/minecraft/world/item/Items.GOLDEN_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 10336: ldc_w           "golden_leggings"
        // 10339: new             Lnet/minecraft/world/item/ArmorItem;
        // 10342: dup            
        // 10343: getstatic       net/minecraft/world/item/ArmorMaterials.GOLD:Lnet/minecraft/world/item/ArmorMaterials;
        // 10346: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10349: new             Lnet/minecraft/world/item/Item$Properties;
        // 10352: dup            
        // 10353: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10356: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10359: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10362: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10365: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10368: putstatic       net/minecraft/world/item/Items.GOLDEN_LEGGINGS:Lnet/minecraft/world/item/Item;
        // 10371: ldc_w           "golden_boots"
        // 10374: new             Lnet/minecraft/world/item/ArmorItem;
        // 10377: dup            
        // 10378: getstatic       net/minecraft/world/item/ArmorMaterials.GOLD:Lnet/minecraft/world/item/ArmorMaterials;
        // 10381: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10384: new             Lnet/minecraft/world/item/Item$Properties;
        // 10387: dup            
        // 10388: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10391: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10394: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10397: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10400: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10403: putstatic       net/minecraft/world/item/Items.GOLDEN_BOOTS:Lnet/minecraft/world/item/Item;
        // 10406: ldc_w           "netherite_helmet"
        // 10409: new             Lnet/minecraft/world/item/ArmorItem;
        // 10412: dup            
        // 10413: getstatic       net/minecraft/world/item/ArmorMaterials.NETHERITE:Lnet/minecraft/world/item/ArmorMaterials;
        // 10416: getstatic       net/minecraft/world/entity/EquipmentSlot.HEAD:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10419: new             Lnet/minecraft/world/item/Item$Properties;
        // 10422: dup            
        // 10423: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10426: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10429: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10432: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        // 10435: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10438: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10441: putstatic       net/minecraft/world/item/Items.NETHERITE_HELMET:Lnet/minecraft/world/item/Item;
        // 10444: ldc_w           "netherite_chestplate"
        // 10447: new             Lnet/minecraft/world/item/ArmorItem;
        // 10450: dup            
        // 10451: getstatic       net/minecraft/world/item/ArmorMaterials.NETHERITE:Lnet/minecraft/world/item/ArmorMaterials;
        // 10454: getstatic       net/minecraft/world/entity/EquipmentSlot.CHEST:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10457: new             Lnet/minecraft/world/item/Item$Properties;
        // 10460: dup            
        // 10461: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10464: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10467: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10470: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        // 10473: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10476: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10479: putstatic       net/minecraft/world/item/Items.NETHERITE_CHESTPLATE:Lnet/minecraft/world/item/Item;
        // 10482: ldc_w           "netherite_leggings"
        // 10485: new             Lnet/minecraft/world/item/ArmorItem;
        // 10488: dup            
        // 10489: getstatic       net/minecraft/world/item/ArmorMaterials.NETHERITE:Lnet/minecraft/world/item/ArmorMaterials;
        // 10492: getstatic       net/minecraft/world/entity/EquipmentSlot.LEGS:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10495: new             Lnet/minecraft/world/item/Item$Properties;
        // 10498: dup            
        // 10499: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10502: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10505: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10508: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        // 10511: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10514: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10517: putstatic       net/minecraft/world/item/Items.NETHERITE_LEGGINGS:Lnet/minecraft/world/item/Item;
        // 10520: ldc_w           "netherite_boots"
        // 10523: new             Lnet/minecraft/world/item/ArmorItem;
        // 10526: dup            
        // 10527: getstatic       net/minecraft/world/item/ArmorMaterials.NETHERITE:Lnet/minecraft/world/item/ArmorMaterials;
        // 10530: getstatic       net/minecraft/world/entity/EquipmentSlot.FEET:Lnet/minecraft/world/entity/EquipmentSlot;
        // 10533: new             Lnet/minecraft/world/item/Item$Properties;
        // 10536: dup            
        // 10537: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10540: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 10543: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10546: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        // 10549: invokespecial   net/minecraft/world/item/ArmorItem.<init>:(Lnet/minecraft/world/item/ArmorMaterial;Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/Item$Properties;)V
        // 10552: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10555: putstatic       net/minecraft/world/item/Items.NETHERITE_BOOTS:Lnet/minecraft/world/item/Item;
        // 10558: ldc_w           "flint"
        // 10561: new             Lnet/minecraft/world/item/Item;
        // 10564: dup            
        // 10565: new             Lnet/minecraft/world/item/Item$Properties;
        // 10568: dup            
        // 10569: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10572: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10575: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10578: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 10581: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10584: putstatic       net/minecraft/world/item/Items.FLINT:Lnet/minecraft/world/item/Item;
        // 10587: ldc_w           "porkchop"
        // 10590: new             Lnet/minecraft/world/item/Item;
        // 10593: dup            
        // 10594: new             Lnet/minecraft/world/item/Item$Properties;
        // 10597: dup            
        // 10598: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10601: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 10604: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10607: getstatic       net/minecraft/world/food/Foods.PORKCHOP:Lnet/minecraft/world/food/FoodProperties;
        // 10610: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 10613: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 10616: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10619: putstatic       net/minecraft/world/item/Items.PORKCHOP:Lnet/minecraft/world/item/Item;
        // 10622: ldc_w           "cooked_porkchop"
        // 10625: new             Lnet/minecraft/world/item/Item;
        // 10628: dup            
        // 10629: new             Lnet/minecraft/world/item/Item$Properties;
        // 10632: dup            
        // 10633: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10636: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 10639: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10642: getstatic       net/minecraft/world/food/Foods.COOKED_PORKCHOP:Lnet/minecraft/world/food/FoodProperties;
        // 10645: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 10648: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 10651: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10654: putstatic       net/minecraft/world/item/Items.COOKED_PORKCHOP:Lnet/minecraft/world/item/Item;
        // 10657: ldc_w           "painting"
        // 10660: new             Lnet/minecraft/world/item/HangingEntityItem;
        // 10663: dup            
        // 10664: getstatic       net/minecraft/world/entity/EntityType.PAINTING:Lnet/minecraft/world/entity/EntityType;
        // 10667: new             Lnet/minecraft/world/item/Item$Properties;
        // 10670: dup            
        // 10671: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10674: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10677: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10680: invokespecial   net/minecraft/world/item/HangingEntityItem.<init>:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/item/Item$Properties;)V
        // 10683: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10686: putstatic       net/minecraft/world/item/Items.PAINTING:Lnet/minecraft/world/item/Item;
        // 10689: ldc_w           "golden_apple"
        // 10692: new             Lnet/minecraft/world/item/Item;
        // 10695: dup            
        // 10696: new             Lnet/minecraft/world/item/Item$Properties;
        // 10699: dup            
        // 10700: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10703: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 10706: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10709: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 10712: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 10715: getstatic       net/minecraft/world/food/Foods.GOLDEN_APPLE:Lnet/minecraft/world/food/FoodProperties;
        // 10718: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 10721: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 10724: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10727: putstatic       net/minecraft/world/item/Items.GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        // 10730: ldc_w           "enchanted_golden_apple"
        // 10733: new             Lnet/minecraft/world/item/EnchantedGoldenAppleItem;
        // 10736: dup            
        // 10737: new             Lnet/minecraft/world/item/Item$Properties;
        // 10740: dup            
        // 10741: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10744: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 10747: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10750: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        // 10753: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 10756: getstatic       net/minecraft/world/food/Foods.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/food/FoodProperties;
        // 10759: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 10762: invokespecial   net/minecraft/world/item/EnchantedGoldenAppleItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 10765: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10768: putstatic       net/minecraft/world/item/Items.ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;
        // 10771: ldc_w           "oak_sign"
        // 10774: new             Lnet/minecraft/world/item/SignItem;
        // 10777: dup            
        // 10778: new             Lnet/minecraft/world/item/Item$Properties;
        // 10781: dup            
        // 10782: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10785: bipush          16
        // 10787: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 10790: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10793: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10796: getstatic       net/minecraft/world/level/block/Blocks.OAK_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10799: getstatic       net/minecraft/world/level/block/Blocks.OAK_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10802: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 10805: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10808: putstatic       net/minecraft/world/item/Items.OAK_SIGN:Lnet/minecraft/world/item/Item;
        // 10811: ldc_w           "spruce_sign"
        // 10814: new             Lnet/minecraft/world/item/SignItem;
        // 10817: dup            
        // 10818: new             Lnet/minecraft/world/item/Item$Properties;
        // 10821: dup            
        // 10822: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10825: bipush          16
        // 10827: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 10830: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10833: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10836: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10839: getstatic       net/minecraft/world/level/block/Blocks.SPRUCE_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10842: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 10845: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10848: putstatic       net/minecraft/world/item/Items.SPRUCE_SIGN:Lnet/minecraft/world/item/Item;
        // 10851: ldc_w           "birch_sign"
        // 10854: new             Lnet/minecraft/world/item/SignItem;
        // 10857: dup            
        // 10858: new             Lnet/minecraft/world/item/Item$Properties;
        // 10861: dup            
        // 10862: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10865: bipush          16
        // 10867: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 10870: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10873: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10876: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10879: getstatic       net/minecraft/world/level/block/Blocks.BIRCH_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10882: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 10885: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10888: putstatic       net/minecraft/world/item/Items.BIRCH_SIGN:Lnet/minecraft/world/item/Item;
        // 10891: ldc_w           "jungle_sign"
        // 10894: new             Lnet/minecraft/world/item/SignItem;
        // 10897: dup            
        // 10898: new             Lnet/minecraft/world/item/Item$Properties;
        // 10901: dup            
        // 10902: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10905: bipush          16
        // 10907: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 10910: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10913: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10916: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10919: getstatic       net/minecraft/world/level/block/Blocks.JUNGLE_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10922: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 10925: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10928: putstatic       net/minecraft/world/item/Items.JUNGLE_SIGN:Lnet/minecraft/world/item/Item;
        // 10931: ldc_w           "acacia_sign"
        // 10934: new             Lnet/minecraft/world/item/SignItem;
        // 10937: dup            
        // 10938: new             Lnet/minecraft/world/item/Item$Properties;
        // 10941: dup            
        // 10942: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10945: bipush          16
        // 10947: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 10950: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10953: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10956: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10959: getstatic       net/minecraft/world/level/block/Blocks.ACACIA_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10962: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 10965: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 10968: putstatic       net/minecraft/world/item/Items.ACACIA_SIGN:Lnet/minecraft/world/item/Item;
        // 10971: ldc_w           "dark_oak_sign"
        // 10974: new             Lnet/minecraft/world/item/SignItem;
        // 10977: dup            
        // 10978: new             Lnet/minecraft/world/item/Item$Properties;
        // 10981: dup            
        // 10982: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 10985: bipush          16
        // 10987: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 10990: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 10993: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 10996: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_SIGN:Lnet/minecraft/world/level/block/Block;
        // 10999: getstatic       net/minecraft/world/level/block/Blocks.DARK_OAK_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 11002: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 11005: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11008: putstatic       net/minecraft/world/item/Items.DARK_OAK_SIGN:Lnet/minecraft/world/item/Item;
        // 11011: ldc_w           "crimson_sign"
        // 11014: new             Lnet/minecraft/world/item/SignItem;
        // 11017: dup            
        // 11018: new             Lnet/minecraft/world/item/Item$Properties;
        // 11021: dup            
        // 11022: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11025: bipush          16
        // 11027: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11030: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11033: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11036: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_SIGN:Lnet/minecraft/world/level/block/Block;
        // 11039: getstatic       net/minecraft/world/level/block/Blocks.CRIMSON_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 11042: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 11045: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11048: putstatic       net/minecraft/world/item/Items.CRIMSON_SIGN:Lnet/minecraft/world/item/Item;
        // 11051: ldc_w           "warped_sign"
        // 11054: new             Lnet/minecraft/world/item/SignItem;
        // 11057: dup            
        // 11058: new             Lnet/minecraft/world/item/Item$Properties;
        // 11061: dup            
        // 11062: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11065: bipush          16
        // 11067: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11070: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11073: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11076: getstatic       net/minecraft/world/level/block/Blocks.WARPED_SIGN:Lnet/minecraft/world/level/block/Block;
        // 11079: getstatic       net/minecraft/world/level/block/Blocks.WARPED_WALL_SIGN:Lnet/minecraft/world/level/block/Block;
        // 11082: invokespecial   net/minecraft/world/item/SignItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;)V
        // 11085: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11088: putstatic       net/minecraft/world/item/Items.WARPED_SIGN:Lnet/minecraft/world/item/Item;
        // 11091: ldc_w           "bucket"
        // 11094: new             Lnet/minecraft/world/item/BucketItem;
        // 11097: dup            
        // 11098: getstatic       net/minecraft/world/level/material/Fluids.EMPTY:Lnet/minecraft/world/level/material/Fluid;
        // 11101: new             Lnet/minecraft/world/item/Item$Properties;
        // 11104: dup            
        // 11105: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11108: bipush          16
        // 11110: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11113: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11116: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11119: invokespecial   net/minecraft/world/item/BucketItem.<init>:(Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11122: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11125: putstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        // 11128: ldc_w           "water_bucket"
        // 11131: new             Lnet/minecraft/world/item/BucketItem;
        // 11134: dup            
        // 11135: getstatic       net/minecraft/world/level/material/Fluids.WATER:Lnet/minecraft/world/level/material/FlowingFluid;
        // 11138: new             Lnet/minecraft/world/item/Item$Properties;
        // 11141: dup            
        // 11142: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11145: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        // 11148: invokevirtual   net/minecraft/world/item/Item$Properties.craftRemainder:(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item$Properties;
        // 11151: iconst_1       
        // 11152: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11155: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11158: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11161: invokespecial   net/minecraft/world/item/BucketItem.<init>:(Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11164: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11167: putstatic       net/minecraft/world/item/Items.WATER_BUCKET:Lnet/minecraft/world/item/Item;
        // 11170: ldc_w           "lava_bucket"
        // 11173: new             Lnet/minecraft/world/item/BucketItem;
        // 11176: dup            
        // 11177: getstatic       net/minecraft/world/level/material/Fluids.LAVA:Lnet/minecraft/world/level/material/FlowingFluid;
        // 11180: new             Lnet/minecraft/world/item/Item$Properties;
        // 11183: dup            
        // 11184: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11187: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        // 11190: invokevirtual   net/minecraft/world/item/Item$Properties.craftRemainder:(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item$Properties;
        // 11193: iconst_1       
        // 11194: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11197: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11200: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11203: invokespecial   net/minecraft/world/item/BucketItem.<init>:(Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11206: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11209: putstatic       net/minecraft/world/item/Items.LAVA_BUCKET:Lnet/minecraft/world/item/Item;
        // 11212: ldc_w           "minecart"
        // 11215: new             Lnet/minecraft/world/item/MinecartItem;
        // 11218: dup            
        // 11219: getstatic       net/minecraft/world/entity/vehicle/AbstractMinecart$Type.RIDEABLE:Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;
        // 11222: new             Lnet/minecraft/world/item/Item$Properties;
        // 11225: dup            
        // 11226: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11229: iconst_1       
        // 11230: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11233: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 11236: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11239: invokespecial   net/minecraft/world/item/MinecartItem.<init>:(Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 11242: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11245: putstatic       net/minecraft/world/item/Items.MINECART:Lnet/minecraft/world/item/Item;
        // 11248: ldc_w           "saddle"
        // 11251: new             Lnet/minecraft/world/item/SaddleItem;
        // 11254: dup            
        // 11255: new             Lnet/minecraft/world/item/Item$Properties;
        // 11258: dup            
        // 11259: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11262: iconst_1       
        // 11263: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11266: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 11269: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11272: invokespecial   net/minecraft/world/item/SaddleItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11275: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11278: putstatic       net/minecraft/world/item/Items.SADDLE:Lnet/minecraft/world/item/Item;
        // 11281: ldc_w           "redstone"
        // 11284: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 11287: dup            
        // 11288: getstatic       net/minecraft/world/level/block/Blocks.REDSTONE_WIRE:Lnet/minecraft/world/level/block/Block;
        // 11291: new             Lnet/minecraft/world/item/Item$Properties;
        // 11294: dup            
        // 11295: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11298: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        // 11301: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11304: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 11307: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11310: putstatic       net/minecraft/world/item/Items.REDSTONE:Lnet/minecraft/world/item/Item;
        // 11313: ldc_w           "snowball"
        // 11316: new             Lnet/minecraft/world/item/SnowballItem;
        // 11319: dup            
        // 11320: new             Lnet/minecraft/world/item/Item$Properties;
        // 11323: dup            
        // 11324: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11327: bipush          16
        // 11329: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11332: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11335: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11338: invokespecial   net/minecraft/world/item/SnowballItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11341: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11344: putstatic       net/minecraft/world/item/Items.SNOWBALL:Lnet/minecraft/world/item/Item;
        // 11347: ldc_w           "oak_boat"
        // 11350: new             Lnet/minecraft/world/item/BoatItem;
        // 11353: dup            
        // 11354: getstatic       net/minecraft/world/entity/vehicle/Boat$Type.OAK:Lnet/minecraft/world/entity/vehicle/Boat$Type;
        // 11357: new             Lnet/minecraft/world/item/Item$Properties;
        // 11360: dup            
        // 11361: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11364: iconst_1       
        // 11365: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11368: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 11371: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11374: invokespecial   net/minecraft/world/item/BoatItem.<init>:(Lnet/minecraft/world/entity/vehicle/Boat$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 11377: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11380: putstatic       net/minecraft/world/item/Items.OAK_BOAT:Lnet/minecraft/world/item/Item;
        // 11383: ldc_w           "leather"
        // 11386: new             Lnet/minecraft/world/item/Item;
        // 11389: dup            
        // 11390: new             Lnet/minecraft/world/item/Item$Properties;
        // 11393: dup            
        // 11394: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11397: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11400: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11403: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11406: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11409: putstatic       net/minecraft/world/item/Items.LEATHER:Lnet/minecraft/world/item/Item;
        // 11412: ldc_w           "milk_bucket"
        // 11415: new             Lnet/minecraft/world/item/MilkBucketItem;
        // 11418: dup            
        // 11419: new             Lnet/minecraft/world/item/Item$Properties;
        // 11422: dup            
        // 11423: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11426: getstatic       net/minecraft/world/item/Items.BUCKET:Lnet/minecraft/world/item/Item;
        // 11429: invokevirtual   net/minecraft/world/item/Item$Properties.craftRemainder:(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item$Properties;
        // 11432: iconst_1       
        // 11433: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11436: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11439: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11442: invokespecial   net/minecraft/world/item/MilkBucketItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11445: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11448: putstatic       net/minecraft/world/item/Items.MILK_BUCKET:Lnet/minecraft/world/item/Item;
        // 11451: ldc_w           "pufferfish_bucket"
        // 11454: new             Lnet/minecraft/world/item/FishBucketItem;
        // 11457: dup            
        // 11458: getstatic       net/minecraft/world/entity/EntityType.PUFFERFISH:Lnet/minecraft/world/entity/EntityType;
        // 11461: getstatic       net/minecraft/world/level/material/Fluids.WATER:Lnet/minecraft/world/level/material/FlowingFluid;
        // 11464: new             Lnet/minecraft/world/item/Item$Properties;
        // 11467: dup            
        // 11468: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11471: iconst_1       
        // 11472: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11475: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11478: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11481: invokespecial   net/minecraft/world/item/FishBucketItem.<init>:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11484: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11487: putstatic       net/minecraft/world/item/Items.PUFFERFISH_BUCKET:Lnet/minecraft/world/item/Item;
        // 11490: ldc_w           "salmon_bucket"
        // 11493: new             Lnet/minecraft/world/item/FishBucketItem;
        // 11496: dup            
        // 11497: getstatic       net/minecraft/world/entity/EntityType.SALMON:Lnet/minecraft/world/entity/EntityType;
        // 11500: getstatic       net/minecraft/world/level/material/Fluids.WATER:Lnet/minecraft/world/level/material/FlowingFluid;
        // 11503: new             Lnet/minecraft/world/item/Item$Properties;
        // 11506: dup            
        // 11507: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11510: iconst_1       
        // 11511: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11514: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11517: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11520: invokespecial   net/minecraft/world/item/FishBucketItem.<init>:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11523: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11526: putstatic       net/minecraft/world/item/Items.SALMON_BUCKET:Lnet/minecraft/world/item/Item;
        // 11529: ldc_w           "cod_bucket"
        // 11532: new             Lnet/minecraft/world/item/FishBucketItem;
        // 11535: dup            
        // 11536: getstatic       net/minecraft/world/entity/EntityType.COD:Lnet/minecraft/world/entity/EntityType;
        // 11539: getstatic       net/minecraft/world/level/material/Fluids.WATER:Lnet/minecraft/world/level/material/FlowingFluid;
        // 11542: new             Lnet/minecraft/world/item/Item$Properties;
        // 11545: dup            
        // 11546: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11549: iconst_1       
        // 11550: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11553: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11556: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11559: invokespecial   net/minecraft/world/item/FishBucketItem.<init>:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11562: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11565: putstatic       net/minecraft/world/item/Items.COD_BUCKET:Lnet/minecraft/world/item/Item;
        // 11568: ldc_w           "tropical_fish_bucket"
        // 11571: new             Lnet/minecraft/world/item/FishBucketItem;
        // 11574: dup            
        // 11575: getstatic       net/minecraft/world/entity/EntityType.TROPICAL_FISH:Lnet/minecraft/world/entity/EntityType;
        // 11578: getstatic       net/minecraft/world/level/material/Fluids.WATER:Lnet/minecraft/world/level/material/FlowingFluid;
        // 11581: new             Lnet/minecraft/world/item/Item$Properties;
        // 11584: dup            
        // 11585: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11588: iconst_1       
        // 11589: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11592: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11595: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11598: invokespecial   net/minecraft/world/item/FishBucketItem.<init>:(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/material/Fluid;Lnet/minecraft/world/item/Item$Properties;)V
        // 11601: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11604: putstatic       net/minecraft/world/item/Items.TROPICAL_FISH_BUCKET:Lnet/minecraft/world/item/Item;
        // 11607: ldc_w           "brick"
        // 11610: new             Lnet/minecraft/world/item/Item;
        // 11613: dup            
        // 11614: new             Lnet/minecraft/world/item/Item$Properties;
        // 11617: dup            
        // 11618: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11621: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11624: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11627: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11630: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11633: putstatic       net/minecraft/world/item/Items.BRICK:Lnet/minecraft/world/item/Item;
        // 11636: ldc_w           "clay_ball"
        // 11639: new             Lnet/minecraft/world/item/Item;
        // 11642: dup            
        // 11643: new             Lnet/minecraft/world/item/Item$Properties;
        // 11646: dup            
        // 11647: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11650: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11653: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11656: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11659: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11662: putstatic       net/minecraft/world/item/Items.CLAY_BALL:Lnet/minecraft/world/item/Item;
        // 11665: getstatic       net/minecraft/world/level/block/Blocks.DRIED_KELP_BLOCK:Lnet/minecraft/world/level/block/Block;
        // 11668: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11671: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 11674: putstatic       net/minecraft/world/item/Items.DRIED_KELP_BLOCK:Lnet/minecraft/world/item/Item;
        // 11677: ldc_w           "paper"
        // 11680: new             Lnet/minecraft/world/item/Item;
        // 11683: dup            
        // 11684: new             Lnet/minecraft/world/item/Item$Properties;
        // 11687: dup            
        // 11688: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11691: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11694: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11697: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11700: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11703: putstatic       net/minecraft/world/item/Items.PAPER:Lnet/minecraft/world/item/Item;
        // 11706: ldc_w           "book"
        // 11709: new             Lnet/minecraft/world/item/BookItem;
        // 11712: dup            
        // 11713: new             Lnet/minecraft/world/item/Item$Properties;
        // 11716: dup            
        // 11717: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11720: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11723: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11726: invokespecial   net/minecraft/world/item/BookItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11729: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11732: putstatic       net/minecraft/world/item/Items.BOOK:Lnet/minecraft/world/item/Item;
        // 11735: ldc_w           "slime_ball"
        // 11738: new             Lnet/minecraft/world/item/Item;
        // 11741: dup            
        // 11742: new             Lnet/minecraft/world/item/Item$Properties;
        // 11745: dup            
        // 11746: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11749: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 11752: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11755: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11758: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11761: putstatic       net/minecraft/world/item/Items.SLIME_BALL:Lnet/minecraft/world/item/Item;
        // 11764: ldc_w           "chest_minecart"
        // 11767: new             Lnet/minecraft/world/item/MinecartItem;
        // 11770: dup            
        // 11771: getstatic       net/minecraft/world/entity/vehicle/AbstractMinecart$Type.CHEST:Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;
        // 11774: new             Lnet/minecraft/world/item/Item$Properties;
        // 11777: dup            
        // 11778: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11781: iconst_1       
        // 11782: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11785: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 11788: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11791: invokespecial   net/minecraft/world/item/MinecartItem.<init>:(Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 11794: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11797: putstatic       net/minecraft/world/item/Items.CHEST_MINECART:Lnet/minecraft/world/item/Item;
        // 11800: ldc_w           "furnace_minecart"
        // 11803: new             Lnet/minecraft/world/item/MinecartItem;
        // 11806: dup            
        // 11807: getstatic       net/minecraft/world/entity/vehicle/AbstractMinecart$Type.FURNACE:Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;
        // 11810: new             Lnet/minecraft/world/item/Item$Properties;
        // 11813: dup            
        // 11814: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11817: iconst_1       
        // 11818: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11821: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 11824: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11827: invokespecial   net/minecraft/world/item/MinecartItem.<init>:(Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 11830: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11833: putstatic       net/minecraft/world/item/Items.FURNACE_MINECART:Lnet/minecraft/world/item/Item;
        // 11836: ldc_w           "egg"
        // 11839: new             Lnet/minecraft/world/item/EggItem;
        // 11842: dup            
        // 11843: new             Lnet/minecraft/world/item/Item$Properties;
        // 11846: dup            
        // 11847: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11850: bipush          16
        // 11852: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11855: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11858: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11861: invokespecial   net/minecraft/world/item/EggItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11864: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11867: putstatic       net/minecraft/world/item/Items.EGG:Lnet/minecraft/world/item/Item;
        // 11870: ldc_w           "compass"
        // 11873: new             Lnet/minecraft/world/item/CompassItem;
        // 11876: dup            
        // 11877: new             Lnet/minecraft/world/item/Item$Properties;
        // 11880: dup            
        // 11881: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11884: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11887: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11890: invokespecial   net/minecraft/world/item/CompassItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11893: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11896: putstatic       net/minecraft/world/item/Items.COMPASS:Lnet/minecraft/world/item/Item;
        // 11899: ldc_w           "fishing_rod"
        // 11902: new             Lnet/minecraft/world/item/FishingRodItem;
        // 11905: dup            
        // 11906: new             Lnet/minecraft/world/item/Item$Properties;
        // 11909: dup            
        // 11910: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11913: bipush          64
        // 11915: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 11918: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11921: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11924: invokespecial   net/minecraft/world/item/FishingRodItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11927: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11930: putstatic       net/minecraft/world/item/Items.FISHING_ROD:Lnet/minecraft/world/item/Item;
        // 11933: ldc_w           "clock"
        // 11936: new             Lnet/minecraft/world/item/Item;
        // 11939: dup            
        // 11940: new             Lnet/minecraft/world/item/Item$Properties;
        // 11943: dup            
        // 11944: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11947: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11950: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11953: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11956: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11959: putstatic       net/minecraft/world/item/Items.CLOCK:Lnet/minecraft/world/item/Item;
        // 11962: ldc_w           "glowstone_dust"
        // 11965: new             Lnet/minecraft/world/item/Item;
        // 11968: dup            
        // 11969: new             Lnet/minecraft/world/item/Item$Properties;
        // 11972: dup            
        // 11973: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 11976: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 11979: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 11982: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 11985: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 11988: putstatic       net/minecraft/world/item/Items.GLOWSTONE_DUST:Lnet/minecraft/world/item/Item;
        // 11991: ldc_w           "cod"
        // 11994: new             Lnet/minecraft/world/item/Item;
        // 11997: dup            
        // 11998: new             Lnet/minecraft/world/item/Item$Properties;
        // 12001: dup            
        // 12002: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12005: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12008: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12011: getstatic       net/minecraft/world/food/Foods.COD:Lnet/minecraft/world/food/FoodProperties;
        // 12014: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 12017: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12020: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12023: putstatic       net/minecraft/world/item/Items.COD:Lnet/minecraft/world/item/Item;
        // 12026: ldc_w           "salmon"
        // 12029: new             Lnet/minecraft/world/item/Item;
        // 12032: dup            
        // 12033: new             Lnet/minecraft/world/item/Item$Properties;
        // 12036: dup            
        // 12037: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12040: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12043: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12046: getstatic       net/minecraft/world/food/Foods.SALMON:Lnet/minecraft/world/food/FoodProperties;
        // 12049: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 12052: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12055: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12058: putstatic       net/minecraft/world/item/Items.SALMON:Lnet/minecraft/world/item/Item;
        // 12061: ldc_w           "tropical_fish"
        // 12064: new             Lnet/minecraft/world/item/Item;
        // 12067: dup            
        // 12068: new             Lnet/minecraft/world/item/Item$Properties;
        // 12071: dup            
        // 12072: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12075: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12078: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12081: getstatic       net/minecraft/world/food/Foods.TROPICAL_FISH:Lnet/minecraft/world/food/FoodProperties;
        // 12084: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 12087: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12090: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12093: putstatic       net/minecraft/world/item/Items.TROPICAL_FISH:Lnet/minecraft/world/item/Item;
        // 12096: ldc_w           "pufferfish"
        // 12099: new             Lnet/minecraft/world/item/Item;
        // 12102: dup            
        // 12103: new             Lnet/minecraft/world/item/Item$Properties;
        // 12106: dup            
        // 12107: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12110: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12113: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12116: getstatic       net/minecraft/world/food/Foods.PUFFERFISH:Lnet/minecraft/world/food/FoodProperties;
        // 12119: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 12122: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12125: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12128: putstatic       net/minecraft/world/item/Items.PUFFERFISH:Lnet/minecraft/world/item/Item;
        // 12131: ldc_w           "cooked_cod"
        // 12134: new             Lnet/minecraft/world/item/Item;
        // 12137: dup            
        // 12138: new             Lnet/minecraft/world/item/Item$Properties;
        // 12141: dup            
        // 12142: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12145: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12148: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12151: getstatic       net/minecraft/world/food/Foods.COOKED_COD:Lnet/minecraft/world/food/FoodProperties;
        // 12154: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 12157: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12160: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12163: putstatic       net/minecraft/world/item/Items.COOKED_COD:Lnet/minecraft/world/item/Item;
        // 12166: ldc_w           "cooked_salmon"
        // 12169: new             Lnet/minecraft/world/item/Item;
        // 12172: dup            
        // 12173: new             Lnet/minecraft/world/item/Item$Properties;
        // 12176: dup            
        // 12177: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12180: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12183: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12186: getstatic       net/minecraft/world/food/Foods.COOKED_SALMON:Lnet/minecraft/world/food/FoodProperties;
        // 12189: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 12192: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12195: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12198: putstatic       net/minecraft/world/item/Items.COOKED_SALMON:Lnet/minecraft/world/item/Item;
        // 12201: ldc_w           "ink_sac"
        // 12204: new             Lnet/minecraft/world/item/Item;
        // 12207: dup            
        // 12208: new             Lnet/minecraft/world/item/Item$Properties;
        // 12211: dup            
        // 12212: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12215: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12218: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12221: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12224: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12227: putstatic       net/minecraft/world/item/Items.INK_SAC:Lnet/minecraft/world/item/Item;
        // 12230: ldc_w           "cocoa_beans"
        // 12233: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 12236: dup            
        // 12237: getstatic       net/minecraft/world/level/block/Blocks.COCOA:Lnet/minecraft/world/level/block/Block;
        // 12240: new             Lnet/minecraft/world/item/Item$Properties;
        // 12243: dup            
        // 12244: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12247: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12250: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12253: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 12256: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12259: putstatic       net/minecraft/world/item/Items.COCOA_BEANS:Lnet/minecraft/world/item/Item;
        // 12262: ldc_w           "lapis_lazuli"
        // 12265: new             Lnet/minecraft/world/item/Item;
        // 12268: dup            
        // 12269: new             Lnet/minecraft/world/item/Item$Properties;
        // 12272: dup            
        // 12273: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12276: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12279: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12282: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12285: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12288: putstatic       net/minecraft/world/item/Items.LAPIS_LAZULI:Lnet/minecraft/world/item/Item;
        // 12291: ldc_w           "white_dye"
        // 12294: new             Lnet/minecraft/world/item/DyeItem;
        // 12297: dup            
        // 12298: getstatic       net/minecraft/world/item/DyeColor.WHITE:Lnet/minecraft/world/item/DyeColor;
        // 12301: new             Lnet/minecraft/world/item/Item$Properties;
        // 12304: dup            
        // 12305: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12308: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12311: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12314: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12317: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12320: putstatic       net/minecraft/world/item/Items.WHITE_DYE:Lnet/minecraft/world/item/Item;
        // 12323: ldc_w           "orange_dye"
        // 12326: new             Lnet/minecraft/world/item/DyeItem;
        // 12329: dup            
        // 12330: getstatic       net/minecraft/world/item/DyeColor.ORANGE:Lnet/minecraft/world/item/DyeColor;
        // 12333: new             Lnet/minecraft/world/item/Item$Properties;
        // 12336: dup            
        // 12337: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12340: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12343: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12346: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12349: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12352: putstatic       net/minecraft/world/item/Items.ORANGE_DYE:Lnet/minecraft/world/item/Item;
        // 12355: ldc_w           "magenta_dye"
        // 12358: new             Lnet/minecraft/world/item/DyeItem;
        // 12361: dup            
        // 12362: getstatic       net/minecraft/world/item/DyeColor.MAGENTA:Lnet/minecraft/world/item/DyeColor;
        // 12365: new             Lnet/minecraft/world/item/Item$Properties;
        // 12368: dup            
        // 12369: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12372: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12375: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12378: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12381: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12384: putstatic       net/minecraft/world/item/Items.MAGENTA_DYE:Lnet/minecraft/world/item/Item;
        // 12387: ldc_w           "light_blue_dye"
        // 12390: new             Lnet/minecraft/world/item/DyeItem;
        // 12393: dup            
        // 12394: getstatic       net/minecraft/world/item/DyeColor.LIGHT_BLUE:Lnet/minecraft/world/item/DyeColor;
        // 12397: new             Lnet/minecraft/world/item/Item$Properties;
        // 12400: dup            
        // 12401: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12404: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12407: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12410: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12413: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12416: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_DYE:Lnet/minecraft/world/item/Item;
        // 12419: ldc_w           "yellow_dye"
        // 12422: new             Lnet/minecraft/world/item/DyeItem;
        // 12425: dup            
        // 12426: getstatic       net/minecraft/world/item/DyeColor.YELLOW:Lnet/minecraft/world/item/DyeColor;
        // 12429: new             Lnet/minecraft/world/item/Item$Properties;
        // 12432: dup            
        // 12433: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12436: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12439: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12442: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12445: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12448: putstatic       net/minecraft/world/item/Items.YELLOW_DYE:Lnet/minecraft/world/item/Item;
        // 12451: ldc_w           "lime_dye"
        // 12454: new             Lnet/minecraft/world/item/DyeItem;
        // 12457: dup            
        // 12458: getstatic       net/minecraft/world/item/DyeColor.LIME:Lnet/minecraft/world/item/DyeColor;
        // 12461: new             Lnet/minecraft/world/item/Item$Properties;
        // 12464: dup            
        // 12465: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12468: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12471: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12474: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12477: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12480: putstatic       net/minecraft/world/item/Items.LIME_DYE:Lnet/minecraft/world/item/Item;
        // 12483: ldc_w           "pink_dye"
        // 12486: new             Lnet/minecraft/world/item/DyeItem;
        // 12489: dup            
        // 12490: getstatic       net/minecraft/world/item/DyeColor.PINK:Lnet/minecraft/world/item/DyeColor;
        // 12493: new             Lnet/minecraft/world/item/Item$Properties;
        // 12496: dup            
        // 12497: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12500: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12503: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12506: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12509: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12512: putstatic       net/minecraft/world/item/Items.PINK_DYE:Lnet/minecraft/world/item/Item;
        // 12515: ldc_w           "gray_dye"
        // 12518: new             Lnet/minecraft/world/item/DyeItem;
        // 12521: dup            
        // 12522: getstatic       net/minecraft/world/item/DyeColor.GRAY:Lnet/minecraft/world/item/DyeColor;
        // 12525: new             Lnet/minecraft/world/item/Item$Properties;
        // 12528: dup            
        // 12529: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12532: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12535: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12538: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12541: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12544: putstatic       net/minecraft/world/item/Items.GRAY_DYE:Lnet/minecraft/world/item/Item;
        // 12547: ldc_w           "light_gray_dye"
        // 12550: new             Lnet/minecraft/world/item/DyeItem;
        // 12553: dup            
        // 12554: getstatic       net/minecraft/world/item/DyeColor.LIGHT_GRAY:Lnet/minecraft/world/item/DyeColor;
        // 12557: new             Lnet/minecraft/world/item/Item$Properties;
        // 12560: dup            
        // 12561: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12564: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12567: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12570: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12573: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12576: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_DYE:Lnet/minecraft/world/item/Item;
        // 12579: ldc_w           "cyan_dye"
        // 12582: new             Lnet/minecraft/world/item/DyeItem;
        // 12585: dup            
        // 12586: getstatic       net/minecraft/world/item/DyeColor.CYAN:Lnet/minecraft/world/item/DyeColor;
        // 12589: new             Lnet/minecraft/world/item/Item$Properties;
        // 12592: dup            
        // 12593: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12596: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12599: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12602: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12605: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12608: putstatic       net/minecraft/world/item/Items.CYAN_DYE:Lnet/minecraft/world/item/Item;
        // 12611: ldc_w           "purple_dye"
        // 12614: new             Lnet/minecraft/world/item/DyeItem;
        // 12617: dup            
        // 12618: getstatic       net/minecraft/world/item/DyeColor.PURPLE:Lnet/minecraft/world/item/DyeColor;
        // 12621: new             Lnet/minecraft/world/item/Item$Properties;
        // 12624: dup            
        // 12625: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12628: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12631: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12634: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12637: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12640: putstatic       net/minecraft/world/item/Items.PURPLE_DYE:Lnet/minecraft/world/item/Item;
        // 12643: ldc_w           "blue_dye"
        // 12646: new             Lnet/minecraft/world/item/DyeItem;
        // 12649: dup            
        // 12650: getstatic       net/minecraft/world/item/DyeColor.BLUE:Lnet/minecraft/world/item/DyeColor;
        // 12653: new             Lnet/minecraft/world/item/Item$Properties;
        // 12656: dup            
        // 12657: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12660: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12663: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12666: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12669: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12672: putstatic       net/minecraft/world/item/Items.BLUE_DYE:Lnet/minecraft/world/item/Item;
        // 12675: ldc_w           "brown_dye"
        // 12678: new             Lnet/minecraft/world/item/DyeItem;
        // 12681: dup            
        // 12682: getstatic       net/minecraft/world/item/DyeColor.BROWN:Lnet/minecraft/world/item/DyeColor;
        // 12685: new             Lnet/minecraft/world/item/Item$Properties;
        // 12688: dup            
        // 12689: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12692: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12695: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12698: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12701: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12704: putstatic       net/minecraft/world/item/Items.BROWN_DYE:Lnet/minecraft/world/item/Item;
        // 12707: ldc_w           "green_dye"
        // 12710: new             Lnet/minecraft/world/item/DyeItem;
        // 12713: dup            
        // 12714: getstatic       net/minecraft/world/item/DyeColor.GREEN:Lnet/minecraft/world/item/DyeColor;
        // 12717: new             Lnet/minecraft/world/item/Item$Properties;
        // 12720: dup            
        // 12721: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12724: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12727: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12730: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12733: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12736: putstatic       net/minecraft/world/item/Items.GREEN_DYE:Lnet/minecraft/world/item/Item;
        // 12739: ldc_w           "red_dye"
        // 12742: new             Lnet/minecraft/world/item/DyeItem;
        // 12745: dup            
        // 12746: getstatic       net/minecraft/world/item/DyeColor.RED:Lnet/minecraft/world/item/DyeColor;
        // 12749: new             Lnet/minecraft/world/item/Item$Properties;
        // 12752: dup            
        // 12753: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12756: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12759: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12762: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12765: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12768: putstatic       net/minecraft/world/item/Items.RED_DYE:Lnet/minecraft/world/item/Item;
        // 12771: ldc_w           "black_dye"
        // 12774: new             Lnet/minecraft/world/item/DyeItem;
        // 12777: dup            
        // 12778: getstatic       net/minecraft/world/item/DyeColor.BLACK:Lnet/minecraft/world/item/DyeColor;
        // 12781: new             Lnet/minecraft/world/item/Item$Properties;
        // 12784: dup            
        // 12785: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12788: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12791: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12794: invokespecial   net/minecraft/world/item/DyeItem.<init>:(Lnet/minecraft/world/item/DyeColor;Lnet/minecraft/world/item/Item$Properties;)V
        // 12797: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12800: putstatic       net/minecraft/world/item/Items.BLACK_DYE:Lnet/minecraft/world/item/Item;
        // 12803: ldc_w           "bone_meal"
        // 12806: new             Lnet/minecraft/world/item/BoneMealItem;
        // 12809: dup            
        // 12810: new             Lnet/minecraft/world/item/Item$Properties;
        // 12813: dup            
        // 12814: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12817: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12820: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12823: invokespecial   net/minecraft/world/item/BoneMealItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12826: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12829: putstatic       net/minecraft/world/item/Items.BONE_MEAL:Lnet/minecraft/world/item/Item;
        // 12832: ldc_w           "bone"
        // 12835: new             Lnet/minecraft/world/item/Item;
        // 12838: dup            
        // 12839: new             Lnet/minecraft/world/item/Item$Properties;
        // 12842: dup            
        // 12843: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12846: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 12849: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12852: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12855: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12858: putstatic       net/minecraft/world/item/Items.BONE:Lnet/minecraft/world/item/Item;
        // 12861: ldc_w           "sugar"
        // 12864: new             Lnet/minecraft/world/item/Item;
        // 12867: dup            
        // 12868: new             Lnet/minecraft/world/item/Item$Properties;
        // 12871: dup            
        // 12872: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12875: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12878: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12881: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 12884: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 12887: putstatic       net/minecraft/world/item/Items.SUGAR:Lnet/minecraft/world/item/Item;
        // 12890: new             Lnet/minecraft/world/item/BlockItem;
        // 12893: dup            
        // 12894: getstatic       net/minecraft/world/level/block/Blocks.CAKE:Lnet/minecraft/world/level/block/Block;
        // 12897: new             Lnet/minecraft/world/item/Item$Properties;
        // 12900: dup            
        // 12901: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12904: iconst_1       
        // 12905: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 12908: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 12911: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12914: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 12917: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 12920: putstatic       net/minecraft/world/item/Items.CAKE:Lnet/minecraft/world/item/Item;
        // 12923: new             Lnet/minecraft/world/item/BedItem;
        // 12926: dup            
        // 12927: getstatic       net/minecraft/world/level/block/Blocks.WHITE_BED:Lnet/minecraft/world/level/block/Block;
        // 12930: new             Lnet/minecraft/world/item/Item$Properties;
        // 12933: dup            
        // 12934: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12937: iconst_1       
        // 12938: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 12941: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12944: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12947: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 12950: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 12953: putstatic       net/minecraft/world/item/Items.WHITE_BED:Lnet/minecraft/world/item/Item;
        // 12956: new             Lnet/minecraft/world/item/BedItem;
        // 12959: dup            
        // 12960: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_BED:Lnet/minecraft/world/level/block/Block;
        // 12963: new             Lnet/minecraft/world/item/Item$Properties;
        // 12966: dup            
        // 12967: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 12970: iconst_1       
        // 12971: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 12974: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 12977: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 12980: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 12983: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 12986: putstatic       net/minecraft/world/item/Items.ORANGE_BED:Lnet/minecraft/world/item/Item;
        // 12989: new             Lnet/minecraft/world/item/BedItem;
        // 12992: dup            
        // 12993: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_BED:Lnet/minecraft/world/level/block/Block;
        // 12996: new             Lnet/minecraft/world/item/Item$Properties;
        // 12999: dup            
        // 13000: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13003: iconst_1       
        // 13004: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13007: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13010: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13013: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13016: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13019: putstatic       net/minecraft/world/item/Items.MAGENTA_BED:Lnet/minecraft/world/item/Item;
        // 13022: new             Lnet/minecraft/world/item/BedItem;
        // 13025: dup            
        // 13026: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_BED:Lnet/minecraft/world/level/block/Block;
        // 13029: new             Lnet/minecraft/world/item/Item$Properties;
        // 13032: dup            
        // 13033: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13036: iconst_1       
        // 13037: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13040: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13043: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13046: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13049: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13052: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_BED:Lnet/minecraft/world/item/Item;
        // 13055: new             Lnet/minecraft/world/item/BedItem;
        // 13058: dup            
        // 13059: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_BED:Lnet/minecraft/world/level/block/Block;
        // 13062: new             Lnet/minecraft/world/item/Item$Properties;
        // 13065: dup            
        // 13066: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13069: iconst_1       
        // 13070: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13073: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13076: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13079: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13082: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13085: putstatic       net/minecraft/world/item/Items.YELLOW_BED:Lnet/minecraft/world/item/Item;
        // 13088: new             Lnet/minecraft/world/item/BedItem;
        // 13091: dup            
        // 13092: getstatic       net/minecraft/world/level/block/Blocks.LIME_BED:Lnet/minecraft/world/level/block/Block;
        // 13095: new             Lnet/minecraft/world/item/Item$Properties;
        // 13098: dup            
        // 13099: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13102: iconst_1       
        // 13103: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13106: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13109: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13112: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13115: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13118: putstatic       net/minecraft/world/item/Items.LIME_BED:Lnet/minecraft/world/item/Item;
        // 13121: new             Lnet/minecraft/world/item/BedItem;
        // 13124: dup            
        // 13125: getstatic       net/minecraft/world/level/block/Blocks.PINK_BED:Lnet/minecraft/world/level/block/Block;
        // 13128: new             Lnet/minecraft/world/item/Item$Properties;
        // 13131: dup            
        // 13132: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13135: iconst_1       
        // 13136: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13139: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13142: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13145: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13148: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13151: putstatic       net/minecraft/world/item/Items.PINK_BED:Lnet/minecraft/world/item/Item;
        // 13154: new             Lnet/minecraft/world/item/BedItem;
        // 13157: dup            
        // 13158: getstatic       net/minecraft/world/level/block/Blocks.GRAY_BED:Lnet/minecraft/world/level/block/Block;
        // 13161: new             Lnet/minecraft/world/item/Item$Properties;
        // 13164: dup            
        // 13165: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13168: iconst_1       
        // 13169: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13172: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13175: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13178: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13181: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13184: putstatic       net/minecraft/world/item/Items.GRAY_BED:Lnet/minecraft/world/item/Item;
        // 13187: new             Lnet/minecraft/world/item/BedItem;
        // 13190: dup            
        // 13191: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_BED:Lnet/minecraft/world/level/block/Block;
        // 13194: new             Lnet/minecraft/world/item/Item$Properties;
        // 13197: dup            
        // 13198: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13201: iconst_1       
        // 13202: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13205: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13208: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13211: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13214: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13217: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_BED:Lnet/minecraft/world/item/Item;
        // 13220: new             Lnet/minecraft/world/item/BedItem;
        // 13223: dup            
        // 13224: getstatic       net/minecraft/world/level/block/Blocks.CYAN_BED:Lnet/minecraft/world/level/block/Block;
        // 13227: new             Lnet/minecraft/world/item/Item$Properties;
        // 13230: dup            
        // 13231: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13234: iconst_1       
        // 13235: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13238: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13241: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13244: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13247: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13250: putstatic       net/minecraft/world/item/Items.CYAN_BED:Lnet/minecraft/world/item/Item;
        // 13253: new             Lnet/minecraft/world/item/BedItem;
        // 13256: dup            
        // 13257: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_BED:Lnet/minecraft/world/level/block/Block;
        // 13260: new             Lnet/minecraft/world/item/Item$Properties;
        // 13263: dup            
        // 13264: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13267: iconst_1       
        // 13268: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13271: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13274: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13277: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13280: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13283: putstatic       net/minecraft/world/item/Items.PURPLE_BED:Lnet/minecraft/world/item/Item;
        // 13286: new             Lnet/minecraft/world/item/BedItem;
        // 13289: dup            
        // 13290: getstatic       net/minecraft/world/level/block/Blocks.BLUE_BED:Lnet/minecraft/world/level/block/Block;
        // 13293: new             Lnet/minecraft/world/item/Item$Properties;
        // 13296: dup            
        // 13297: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13300: iconst_1       
        // 13301: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13304: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13307: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13310: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13313: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13316: putstatic       net/minecraft/world/item/Items.BLUE_BED:Lnet/minecraft/world/item/Item;
        // 13319: new             Lnet/minecraft/world/item/BedItem;
        // 13322: dup            
        // 13323: getstatic       net/minecraft/world/level/block/Blocks.BROWN_BED:Lnet/minecraft/world/level/block/Block;
        // 13326: new             Lnet/minecraft/world/item/Item$Properties;
        // 13329: dup            
        // 13330: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13333: iconst_1       
        // 13334: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13337: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13340: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13343: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13346: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13349: putstatic       net/minecraft/world/item/Items.BROWN_BED:Lnet/minecraft/world/item/Item;
        // 13352: new             Lnet/minecraft/world/item/BedItem;
        // 13355: dup            
        // 13356: getstatic       net/minecraft/world/level/block/Blocks.GREEN_BED:Lnet/minecraft/world/level/block/Block;
        // 13359: new             Lnet/minecraft/world/item/Item$Properties;
        // 13362: dup            
        // 13363: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13366: iconst_1       
        // 13367: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13370: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13373: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13376: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13379: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13382: putstatic       net/minecraft/world/item/Items.GREEN_BED:Lnet/minecraft/world/item/Item;
        // 13385: new             Lnet/minecraft/world/item/BedItem;
        // 13388: dup            
        // 13389: getstatic       net/minecraft/world/level/block/Blocks.RED_BED:Lnet/minecraft/world/level/block/Block;
        // 13392: new             Lnet/minecraft/world/item/Item$Properties;
        // 13395: dup            
        // 13396: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13399: iconst_1       
        // 13400: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13403: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13406: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13409: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13412: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13415: putstatic       net/minecraft/world/item/Items.RED_BED:Lnet/minecraft/world/item/Item;
        // 13418: new             Lnet/minecraft/world/item/BedItem;
        // 13421: dup            
        // 13422: getstatic       net/minecraft/world/level/block/Blocks.BLACK_BED:Lnet/minecraft/world/level/block/Block;
        // 13425: new             Lnet/minecraft/world/item/Item$Properties;
        // 13428: dup            
        // 13429: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13432: iconst_1       
        // 13433: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13436: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13439: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13442: invokespecial   net/minecraft/world/item/BedItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13445: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 13448: putstatic       net/minecraft/world/item/Items.BLACK_BED:Lnet/minecraft/world/item/Item;
        // 13451: ldc_w           "cookie"
        // 13454: new             Lnet/minecraft/world/item/Item;
        // 13457: dup            
        // 13458: new             Lnet/minecraft/world/item/Item$Properties;
        // 13461: dup            
        // 13462: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13465: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13468: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13471: getstatic       net/minecraft/world/food/Foods.COOKIE:Lnet/minecraft/world/food/FoodProperties;
        // 13474: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13477: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13480: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13483: putstatic       net/minecraft/world/item/Items.COOKIE:Lnet/minecraft/world/item/Item;
        // 13486: ldc_w           "filled_map"
        // 13489: new             Lnet/minecraft/world/item/MapItem;
        // 13492: dup            
        // 13493: new             Lnet/minecraft/world/item/Item$Properties;
        // 13496: dup            
        // 13497: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13500: invokespecial   net/minecraft/world/item/MapItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13503: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13506: putstatic       net/minecraft/world/item/Items.FILLED_MAP:Lnet/minecraft/world/item/Item;
        // 13509: ldc_w           "shears"
        // 13512: new             Lnet/minecraft/world/item/ShearsItem;
        // 13515: dup            
        // 13516: new             Lnet/minecraft/world/item/Item$Properties;
        // 13519: dup            
        // 13520: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13523: sipush          238
        // 13526: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13529: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13532: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13535: invokespecial   net/minecraft/world/item/ShearsItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13538: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13541: putstatic       net/minecraft/world/item/Items.SHEARS:Lnet/minecraft/world/item/Item;
        // 13544: ldc_w           "melon_slice"
        // 13547: new             Lnet/minecraft/world/item/Item;
        // 13550: dup            
        // 13551: new             Lnet/minecraft/world/item/Item$Properties;
        // 13554: dup            
        // 13555: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13558: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13561: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13564: getstatic       net/minecraft/world/food/Foods.MELON_SLICE:Lnet/minecraft/world/food/FoodProperties;
        // 13567: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13570: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13573: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13576: putstatic       net/minecraft/world/item/Items.MELON_SLICE:Lnet/minecraft/world/item/Item;
        // 13579: ldc_w           "dried_kelp"
        // 13582: new             Lnet/minecraft/world/item/Item;
        // 13585: dup            
        // 13586: new             Lnet/minecraft/world/item/Item$Properties;
        // 13589: dup            
        // 13590: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13593: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13596: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13599: getstatic       net/minecraft/world/food/Foods.DRIED_KELP:Lnet/minecraft/world/food/FoodProperties;
        // 13602: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13605: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13608: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13611: putstatic       net/minecraft/world/item/Items.DRIED_KELP:Lnet/minecraft/world/item/Item;
        // 13614: ldc_w           "pumpkin_seeds"
        // 13617: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 13620: dup            
        // 13621: getstatic       net/minecraft/world/level/block/Blocks.PUMPKIN_STEM:Lnet/minecraft/world/level/block/Block;
        // 13624: new             Lnet/minecraft/world/item/Item$Properties;
        // 13627: dup            
        // 13628: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13631: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13634: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13637: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13640: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13643: putstatic       net/minecraft/world/item/Items.PUMPKIN_SEEDS:Lnet/minecraft/world/item/Item;
        // 13646: ldc_w           "melon_seeds"
        // 13649: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 13652: dup            
        // 13653: getstatic       net/minecraft/world/level/block/Blocks.MELON_STEM:Lnet/minecraft/world/level/block/Block;
        // 13656: new             Lnet/minecraft/world/item/Item$Properties;
        // 13659: dup            
        // 13660: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13663: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13666: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13669: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 13672: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13675: putstatic       net/minecraft/world/item/Items.MELON_SEEDS:Lnet/minecraft/world/item/Item;
        // 13678: ldc_w           "beef"
        // 13681: new             Lnet/minecraft/world/item/Item;
        // 13684: dup            
        // 13685: new             Lnet/minecraft/world/item/Item$Properties;
        // 13688: dup            
        // 13689: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13692: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13695: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13698: getstatic       net/minecraft/world/food/Foods.BEEF:Lnet/minecraft/world/food/FoodProperties;
        // 13701: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13704: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13707: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13710: putstatic       net/minecraft/world/item/Items.BEEF:Lnet/minecraft/world/item/Item;
        // 13713: ldc_w           "cooked_beef"
        // 13716: new             Lnet/minecraft/world/item/Item;
        // 13719: dup            
        // 13720: new             Lnet/minecraft/world/item/Item$Properties;
        // 13723: dup            
        // 13724: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13727: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13730: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13733: getstatic       net/minecraft/world/food/Foods.COOKED_BEEF:Lnet/minecraft/world/food/FoodProperties;
        // 13736: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13739: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13742: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13745: putstatic       net/minecraft/world/item/Items.COOKED_BEEF:Lnet/minecraft/world/item/Item;
        // 13748: ldc_w           "chicken"
        // 13751: new             Lnet/minecraft/world/item/Item;
        // 13754: dup            
        // 13755: new             Lnet/minecraft/world/item/Item$Properties;
        // 13758: dup            
        // 13759: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13762: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13765: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13768: getstatic       net/minecraft/world/food/Foods.CHICKEN:Lnet/minecraft/world/food/FoodProperties;
        // 13771: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13774: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13777: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13780: putstatic       net/minecraft/world/item/Items.CHICKEN:Lnet/minecraft/world/item/Item;
        // 13783: ldc_w           "cooked_chicken"
        // 13786: new             Lnet/minecraft/world/item/Item;
        // 13789: dup            
        // 13790: new             Lnet/minecraft/world/item/Item$Properties;
        // 13793: dup            
        // 13794: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13797: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13800: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13803: getstatic       net/minecraft/world/food/Foods.COOKED_CHICKEN:Lnet/minecraft/world/food/FoodProperties;
        // 13806: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13809: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13812: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13815: putstatic       net/minecraft/world/item/Items.COOKED_CHICKEN:Lnet/minecraft/world/item/Item;
        // 13818: ldc_w           "rotten_flesh"
        // 13821: new             Lnet/minecraft/world/item/Item;
        // 13824: dup            
        // 13825: new             Lnet/minecraft/world/item/Item$Properties;
        // 13828: dup            
        // 13829: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13832: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 13835: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13838: getstatic       net/minecraft/world/food/Foods.ROTTEN_FLESH:Lnet/minecraft/world/food/FoodProperties;
        // 13841: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 13844: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13847: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13850: putstatic       net/minecraft/world/item/Items.ROTTEN_FLESH:Lnet/minecraft/world/item/Item;
        // 13853: ldc_w           "ender_pearl"
        // 13856: new             Lnet/minecraft/world/item/EnderpearlItem;
        // 13859: dup            
        // 13860: new             Lnet/minecraft/world/item/Item$Properties;
        // 13863: dup            
        // 13864: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13867: bipush          16
        // 13869: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 13872: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 13875: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13878: invokespecial   net/minecraft/world/item/EnderpearlItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13881: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13884: putstatic       net/minecraft/world/item/Items.ENDER_PEARL:Lnet/minecraft/world/item/Item;
        // 13887: ldc_w           "blaze_rod"
        // 13890: new             Lnet/minecraft/world/item/Item;
        // 13893: dup            
        // 13894: new             Lnet/minecraft/world/item/Item$Properties;
        // 13897: dup            
        // 13898: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13901: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13904: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13907: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13910: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13913: putstatic       net/minecraft/world/item/Items.BLAZE_ROD:Lnet/minecraft/world/item/Item;
        // 13916: ldc_w           "ghast_tear"
        // 13919: new             Lnet/minecraft/world/item/Item;
        // 13922: dup            
        // 13923: new             Lnet/minecraft/world/item/Item$Properties;
        // 13926: dup            
        // 13927: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13930: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 13933: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13936: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13939: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13942: putstatic       net/minecraft/world/item/Items.GHAST_TEAR:Lnet/minecraft/world/item/Item;
        // 13945: ldc_w           "gold_nugget"
        // 13948: new             Lnet/minecraft/world/item/Item;
        // 13951: dup            
        // 13952: new             Lnet/minecraft/world/item/Item$Properties;
        // 13955: dup            
        // 13956: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13959: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13962: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13965: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 13968: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 13971: putstatic       net/minecraft/world/item/Items.GOLD_NUGGET:Lnet/minecraft/world/item/Item;
        // 13974: ldc_w           "nether_wart"
        // 13977: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 13980: dup            
        // 13981: getstatic       net/minecraft/world/level/block/Blocks.NETHER_WART:Lnet/minecraft/world/level/block/Block;
        // 13984: new             Lnet/minecraft/world/item/Item$Properties;
        // 13987: dup            
        // 13988: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 13991: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 13994: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 13997: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 14000: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14003: putstatic       net/minecraft/world/item/Items.NETHER_WART:Lnet/minecraft/world/item/Item;
        // 14006: ldc_w           "potion"
        // 14009: new             Lnet/minecraft/world/item/PotionItem;
        // 14012: dup            
        // 14013: new             Lnet/minecraft/world/item/Item$Properties;
        // 14016: dup            
        // 14017: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14020: iconst_1       
        // 14021: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 14024: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14027: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14030: invokespecial   net/minecraft/world/item/PotionItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14033: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14036: putstatic       net/minecraft/world/item/Items.POTION:Lnet/minecraft/world/item/Item;
        // 14039: ldc_w           "glass_bottle"
        // 14042: new             Lnet/minecraft/world/item/BottleItem;
        // 14045: dup            
        // 14046: new             Lnet/minecraft/world/item/Item$Properties;
        // 14049: dup            
        // 14050: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14053: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14056: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14059: invokespecial   net/minecraft/world/item/BottleItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14062: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14065: putstatic       net/minecraft/world/item/Items.GLASS_BOTTLE:Lnet/minecraft/world/item/Item;
        // 14068: ldc_w           "spider_eye"
        // 14071: new             Lnet/minecraft/world/item/Item;
        // 14074: dup            
        // 14075: new             Lnet/minecraft/world/item/Item$Properties;
        // 14078: dup            
        // 14079: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14082: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 14085: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14088: getstatic       net/minecraft/world/food/Foods.SPIDER_EYE:Lnet/minecraft/world/food/FoodProperties;
        // 14091: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 14094: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14097: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14100: putstatic       net/minecraft/world/item/Items.SPIDER_EYE:Lnet/minecraft/world/item/Item;
        // 14103: ldc_w           "fermented_spider_eye"
        // 14106: new             Lnet/minecraft/world/item/Item;
        // 14109: dup            
        // 14110: new             Lnet/minecraft/world/item/Item$Properties;
        // 14113: dup            
        // 14114: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14117: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14120: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14123: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14126: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14129: putstatic       net/minecraft/world/item/Items.FERMENTED_SPIDER_EYE:Lnet/minecraft/world/item/Item;
        // 14132: ldc_w           "blaze_powder"
        // 14135: new             Lnet/minecraft/world/item/Item;
        // 14138: dup            
        // 14139: new             Lnet/minecraft/world/item/Item$Properties;
        // 14142: dup            
        // 14143: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14146: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14149: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14152: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14155: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14158: putstatic       net/minecraft/world/item/Items.BLAZE_POWDER:Lnet/minecraft/world/item/Item;
        // 14161: ldc_w           "magma_cream"
        // 14164: new             Lnet/minecraft/world/item/Item;
        // 14167: dup            
        // 14168: new             Lnet/minecraft/world/item/Item$Properties;
        // 14171: dup            
        // 14172: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14175: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14178: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14181: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14184: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14187: putstatic       net/minecraft/world/item/Items.MAGMA_CREAM:Lnet/minecraft/world/item/Item;
        // 14190: getstatic       net/minecraft/world/level/block/Blocks.BREWING_STAND:Lnet/minecraft/world/level/block/Block;
        // 14193: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14196: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 14199: putstatic       net/minecraft/world/item/Items.BREWING_STAND:Lnet/minecraft/world/item/Item;
        // 14202: getstatic       net/minecraft/world/level/block/Blocks.CAULDRON:Lnet/minecraft/world/level/block/Block;
        // 14205: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14208: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 14211: putstatic       net/minecraft/world/item/Items.CAULDRON:Lnet/minecraft/world/item/Item;
        // 14214: ldc_w           "ender_eye"
        // 14217: new             Lnet/minecraft/world/item/EnderEyeItem;
        // 14220: dup            
        // 14221: new             Lnet/minecraft/world/item/Item$Properties;
        // 14224: dup            
        // 14225: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14228: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14231: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14234: invokespecial   net/minecraft/world/item/EnderEyeItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14237: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14240: putstatic       net/minecraft/world/item/Items.ENDER_EYE:Lnet/minecraft/world/item/Item;
        // 14243: ldc_w           "glistering_melon_slice"
        // 14246: new             Lnet/minecraft/world/item/Item;
        // 14249: dup            
        // 14250: new             Lnet/minecraft/world/item/Item$Properties;
        // 14253: dup            
        // 14254: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14257: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 14260: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14263: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 14266: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14269: putstatic       net/minecraft/world/item/Items.GLISTERING_MELON_SLICE:Lnet/minecraft/world/item/Item;
        // 14272: ldc_w           "bat_spawn_egg"
        // 14275: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14278: dup            
        // 14279: getstatic       net/minecraft/world/entity/EntityType.BAT:Lnet/minecraft/world/entity/EntityType;
        // 14282: ldc_w           4996656
        // 14285: ldc_w           986895
        // 14288: new             Lnet/minecraft/world/item/Item$Properties;
        // 14291: dup            
        // 14292: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14295: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14298: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14301: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14304: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14307: putstatic       net/minecraft/world/item/Items.BAT_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14310: ldc_w           "bee_spawn_egg"
        // 14313: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14316: dup            
        // 14317: getstatic       net/minecraft/world/entity/EntityType.BEE:Lnet/minecraft/world/entity/EntityType;
        // 14320: ldc_w           15582019
        // 14323: ldc_w           4400155
        // 14326: new             Lnet/minecraft/world/item/Item$Properties;
        // 14329: dup            
        // 14330: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14333: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14336: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14339: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14342: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14345: putstatic       net/minecraft/world/item/Items.BEE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14348: ldc_w           "blaze_spawn_egg"
        // 14351: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14354: dup            
        // 14355: getstatic       net/minecraft/world/entity/EntityType.BLAZE:Lnet/minecraft/world/entity/EntityType;
        // 14358: ldc_w           16167425
        // 14361: ldc_w           16775294
        // 14364: new             Lnet/minecraft/world/item/Item$Properties;
        // 14367: dup            
        // 14368: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14371: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14374: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14377: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14380: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14383: putstatic       net/minecraft/world/item/Items.BLAZE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14386: ldc_w           "cat_spawn_egg"
        // 14389: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14392: dup            
        // 14393: getstatic       net/minecraft/world/entity/EntityType.CAT:Lnet/minecraft/world/entity/EntityType;
        // 14396: ldc_w           15714446
        // 14399: ldc_w           9794134
        // 14402: new             Lnet/minecraft/world/item/Item$Properties;
        // 14405: dup            
        // 14406: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14409: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14412: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14415: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14418: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14421: putstatic       net/minecraft/world/item/Items.CAT_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14424: ldc_w           "cave_spider_spawn_egg"
        // 14427: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14430: dup            
        // 14431: getstatic       net/minecraft/world/entity/EntityType.CAVE_SPIDER:Lnet/minecraft/world/entity/EntityType;
        // 14434: ldc_w           803406
        // 14437: ldc_w           11013646
        // 14440: new             Lnet/minecraft/world/item/Item$Properties;
        // 14443: dup            
        // 14444: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14447: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14450: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14453: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14456: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14459: putstatic       net/minecraft/world/item/Items.CAVE_SPIDER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14462: ldc_w           "chicken_spawn_egg"
        // 14465: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14468: dup            
        // 14469: getstatic       net/minecraft/world/entity/EntityType.CHICKEN:Lnet/minecraft/world/entity/EntityType;
        // 14472: ldc_w           10592673
        // 14475: ldc_w           16711680
        // 14478: new             Lnet/minecraft/world/item/Item$Properties;
        // 14481: dup            
        // 14482: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14485: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14488: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14491: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14494: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14497: putstatic       net/minecraft/world/item/Items.CHICKEN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14500: ldc_w           "cod_spawn_egg"
        // 14503: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14506: dup            
        // 14507: getstatic       net/minecraft/world/entity/EntityType.COD:Lnet/minecraft/world/entity/EntityType;
        // 14510: ldc_w           12691306
        // 14513: ldc_w           15058059
        // 14516: new             Lnet/minecraft/world/item/Item$Properties;
        // 14519: dup            
        // 14520: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14523: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14526: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14529: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14532: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14535: putstatic       net/minecraft/world/item/Items.COD_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14538: ldc_w           "cow_spawn_egg"
        // 14541: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14544: dup            
        // 14545: getstatic       net/minecraft/world/entity/EntityType.COW:Lnet/minecraft/world/entity/EntityType;
        // 14548: ldc_w           4470310
        // 14551: ldc_w           10592673
        // 14554: new             Lnet/minecraft/world/item/Item$Properties;
        // 14557: dup            
        // 14558: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14561: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14564: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14567: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14570: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14573: putstatic       net/minecraft/world/item/Items.COW_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14576: ldc_w           "creeper_spawn_egg"
        // 14579: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14582: dup            
        // 14583: getstatic       net/minecraft/world/entity/EntityType.CREEPER:Lnet/minecraft/world/entity/EntityType;
        // 14586: ldc_w           894731
        // 14589: iconst_0       
        // 14590: new             Lnet/minecraft/world/item/Item$Properties;
        // 14593: dup            
        // 14594: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14597: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14600: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14603: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14606: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14609: putstatic       net/minecraft/world/item/Items.CREEPER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14612: ldc_w           "dolphin_spawn_egg"
        // 14615: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14618: dup            
        // 14619: getstatic       net/minecraft/world/entity/EntityType.DOLPHIN:Lnet/minecraft/world/entity/EntityType;
        // 14622: ldc_w           2243405
        // 14625: ldc_w           16382457
        // 14628: new             Lnet/minecraft/world/item/Item$Properties;
        // 14631: dup            
        // 14632: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14635: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14638: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14641: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14644: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14647: putstatic       net/minecraft/world/item/Items.DOLPHIN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14650: ldc_w           "donkey_spawn_egg"
        // 14653: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14656: dup            
        // 14657: getstatic       net/minecraft/world/entity/EntityType.DONKEY:Lnet/minecraft/world/entity/EntityType;
        // 14660: ldc_w           5457209
        // 14663: ldc_w           8811878
        // 14666: new             Lnet/minecraft/world/item/Item$Properties;
        // 14669: dup            
        // 14670: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14673: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14676: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14679: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14682: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14685: putstatic       net/minecraft/world/item/Items.DONKEY_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14688: ldc_w           "drowned_spawn_egg"
        // 14691: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14694: dup            
        // 14695: getstatic       net/minecraft/world/entity/EntityType.DROWNED:Lnet/minecraft/world/entity/EntityType;
        // 14698: ldc_w           9433559
        // 14701: ldc_w           7969893
        // 14704: new             Lnet/minecraft/world/item/Item$Properties;
        // 14707: dup            
        // 14708: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14711: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14714: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14717: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14720: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14723: putstatic       net/minecraft/world/item/Items.DROWNED_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14726: ldc_w           "elder_guardian_spawn_egg"
        // 14729: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14732: dup            
        // 14733: getstatic       net/minecraft/world/entity/EntityType.ELDER_GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        // 14736: ldc_w           13552826
        // 14739: ldc_w           7632531
        // 14742: new             Lnet/minecraft/world/item/Item$Properties;
        // 14745: dup            
        // 14746: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14749: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14752: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14755: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14758: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14761: putstatic       net/minecraft/world/item/Items.ELDER_GUARDIAN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14764: ldc_w           "enderman_spawn_egg"
        // 14767: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14770: dup            
        // 14771: getstatic       net/minecraft/world/entity/EntityType.ENDERMAN:Lnet/minecraft/world/entity/EntityType;
        // 14774: ldc_w           1447446
        // 14777: iconst_0       
        // 14778: new             Lnet/minecraft/world/item/Item$Properties;
        // 14781: dup            
        // 14782: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14785: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14788: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14791: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14794: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14797: putstatic       net/minecraft/world/item/Items.ENDERMAN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14800: ldc_w           "endermite_spawn_egg"
        // 14803: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14806: dup            
        // 14807: getstatic       net/minecraft/world/entity/EntityType.ENDERMITE:Lnet/minecraft/world/entity/EntityType;
        // 14810: ldc_w           1447446
        // 14813: ldc_w           7237230
        // 14816: new             Lnet/minecraft/world/item/Item$Properties;
        // 14819: dup            
        // 14820: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14823: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14826: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14829: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14832: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14835: putstatic       net/minecraft/world/item/Items.ENDERMITE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14838: ldc_w           "evoker_spawn_egg"
        // 14841: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14844: dup            
        // 14845: getstatic       net/minecraft/world/entity/EntityType.EVOKER:Lnet/minecraft/world/entity/EntityType;
        // 14848: ldc_w           9804699
        // 14851: ldc_w           1973274
        // 14854: new             Lnet/minecraft/world/item/Item$Properties;
        // 14857: dup            
        // 14858: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14861: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14864: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14867: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14870: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14873: putstatic       net/minecraft/world/item/Items.EVOKER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14876: ldc_w           "fox_spawn_egg"
        // 14879: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14882: dup            
        // 14883: getstatic       net/minecraft/world/entity/EntityType.FOX:Lnet/minecraft/world/entity/EntityType;
        // 14886: ldc_w           14005919
        // 14889: ldc_w           13396256
        // 14892: new             Lnet/minecraft/world/item/Item$Properties;
        // 14895: dup            
        // 14896: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14899: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14902: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14905: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14908: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14911: putstatic       net/minecraft/world/item/Items.FOX_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14914: ldc_w           "ghast_spawn_egg"
        // 14917: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14920: dup            
        // 14921: getstatic       net/minecraft/world/entity/EntityType.GHAST:Lnet/minecraft/world/entity/EntityType;
        // 14924: ldc_w           16382457
        // 14927: ldc_w           12369084
        // 14930: new             Lnet/minecraft/world/item/Item$Properties;
        // 14933: dup            
        // 14934: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14937: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14940: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14943: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14946: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14949: putstatic       net/minecraft/world/item/Items.GHAST_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14952: ldc_w           "guardian_spawn_egg"
        // 14955: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14958: dup            
        // 14959: getstatic       net/minecraft/world/entity/EntityType.GUARDIAN:Lnet/minecraft/world/entity/EntityType;
        // 14962: ldc_w           5931634
        // 14965: ldc_w           15826224
        // 14968: new             Lnet/minecraft/world/item/Item$Properties;
        // 14971: dup            
        // 14972: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 14975: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 14978: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 14981: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 14984: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 14987: putstatic       net/minecraft/world/item/Items.GUARDIAN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 14990: ldc_w           "hoglin_spawn_egg"
        // 14993: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 14996: dup            
        // 14997: getstatic       net/minecraft/world/entity/EntityType.HOGLIN:Lnet/minecraft/world/entity/EntityType;
        // 15000: ldc_w           13004373
        // 15003: ldc_w           6251620
        // 15006: new             Lnet/minecraft/world/item/Item$Properties;
        // 15009: dup            
        // 15010: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15013: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15016: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15019: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15022: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15025: putstatic       net/minecraft/world/item/Items.HOGLIN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15028: ldc_w           "horse_spawn_egg"
        // 15031: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15034: dup            
        // 15035: getstatic       net/minecraft/world/entity/EntityType.HORSE:Lnet/minecraft/world/entity/EntityType;
        // 15038: ldc_w           12623485
        // 15041: ldc_w           15656192
        // 15044: new             Lnet/minecraft/world/item/Item$Properties;
        // 15047: dup            
        // 15048: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15051: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15054: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15057: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15060: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15063: putstatic       net/minecraft/world/item/Items.HORSE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15066: ldc_w           "husk_spawn_egg"
        // 15069: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15072: dup            
        // 15073: getstatic       net/minecraft/world/entity/EntityType.HUSK:Lnet/minecraft/world/entity/EntityType;
        // 15076: ldc_w           7958625
        // 15079: ldc_w           15125652
        // 15082: new             Lnet/minecraft/world/item/Item$Properties;
        // 15085: dup            
        // 15086: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15089: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15092: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15095: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15098: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15101: putstatic       net/minecraft/world/item/Items.HUSK_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15104: ldc_w           "llama_spawn_egg"
        // 15107: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15110: dup            
        // 15111: getstatic       net/minecraft/world/entity/EntityType.LLAMA:Lnet/minecraft/world/entity/EntityType;
        // 15114: ldc_w           12623485
        // 15117: ldc_w           10051392
        // 15120: new             Lnet/minecraft/world/item/Item$Properties;
        // 15123: dup            
        // 15124: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15127: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15130: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15133: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15136: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15139: putstatic       net/minecraft/world/item/Items.LLAMA_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15142: ldc_w           "magma_cube_spawn_egg"
        // 15145: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15148: dup            
        // 15149: getstatic       net/minecraft/world/entity/EntityType.MAGMA_CUBE:Lnet/minecraft/world/entity/EntityType;
        // 15152: ldc_w           3407872
        // 15155: ldc_w           16579584
        // 15158: new             Lnet/minecraft/world/item/Item$Properties;
        // 15161: dup            
        // 15162: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15165: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15168: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15171: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15174: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15177: putstatic       net/minecraft/world/item/Items.MAGMA_CUBE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15180: ldc_w           "mooshroom_spawn_egg"
        // 15183: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15186: dup            
        // 15187: getstatic       net/minecraft/world/entity/EntityType.MOOSHROOM:Lnet/minecraft/world/entity/EntityType;
        // 15190: ldc_w           10489616
        // 15193: ldc_w           12040119
        // 15196: new             Lnet/minecraft/world/item/Item$Properties;
        // 15199: dup            
        // 15200: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15203: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15206: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15209: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15212: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15215: putstatic       net/minecraft/world/item/Items.MOOSHROOM_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15218: ldc_w           "mule_spawn_egg"
        // 15221: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15224: dup            
        // 15225: getstatic       net/minecraft/world/entity/EntityType.MULE:Lnet/minecraft/world/entity/EntityType;
        // 15228: ldc_w           1769984
        // 15231: ldc_w           5321501
        // 15234: new             Lnet/minecraft/world/item/Item$Properties;
        // 15237: dup            
        // 15238: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15241: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15244: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15247: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15250: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15253: putstatic       net/minecraft/world/item/Items.MULE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15256: ldc_w           "ocelot_spawn_egg"
        // 15259: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15262: dup            
        // 15263: getstatic       net/minecraft/world/entity/EntityType.OCELOT:Lnet/minecraft/world/entity/EntityType;
        // 15266: ldc_w           15720061
        // 15269: ldc_w           5653556
        // 15272: new             Lnet/minecraft/world/item/Item$Properties;
        // 15275: dup            
        // 15276: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15279: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15282: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15285: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15288: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15291: putstatic       net/minecraft/world/item/Items.OCELOT_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15294: ldc_w           "panda_spawn_egg"
        // 15297: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15300: dup            
        // 15301: getstatic       net/minecraft/world/entity/EntityType.PANDA:Lnet/minecraft/world/entity/EntityType;
        // 15304: ldc_w           15198183
        // 15307: ldc_w           1776418
        // 15310: new             Lnet/minecraft/world/item/Item$Properties;
        // 15313: dup            
        // 15314: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15317: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15320: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15323: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15326: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15329: putstatic       net/minecraft/world/item/Items.PANDA_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15332: ldc_w           "parrot_spawn_egg"
        // 15335: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15338: dup            
        // 15339: getstatic       net/minecraft/world/entity/EntityType.PARROT:Lnet/minecraft/world/entity/EntityType;
        // 15342: ldc_w           894731
        // 15345: ldc_w           16711680
        // 15348: new             Lnet/minecraft/world/item/Item$Properties;
        // 15351: dup            
        // 15352: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15355: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15358: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15361: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15364: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15367: putstatic       net/minecraft/world/item/Items.PARROT_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15370: ldc_w           "phantom_spawn_egg"
        // 15373: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15376: dup            
        // 15377: getstatic       net/minecraft/world/entity/EntityType.PHANTOM:Lnet/minecraft/world/entity/EntityType;
        // 15380: ldc_w           4411786
        // 15383: ldc_w           8978176
        // 15386: new             Lnet/minecraft/world/item/Item$Properties;
        // 15389: dup            
        // 15390: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15393: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15396: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15399: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15402: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15405: putstatic       net/minecraft/world/item/Items.PHANTOM_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15408: ldc_w           "pig_spawn_egg"
        // 15411: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15414: dup            
        // 15415: getstatic       net/minecraft/world/entity/EntityType.PIG:Lnet/minecraft/world/entity/EntityType;
        // 15418: ldc_w           15771042
        // 15421: ldc_w           14377823
        // 15424: new             Lnet/minecraft/world/item/Item$Properties;
        // 15427: dup            
        // 15428: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15431: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15434: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15437: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15440: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15443: putstatic       net/minecraft/world/item/Items.PIG_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15446: ldc_w           "piglin_spawn_egg"
        // 15449: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15452: dup            
        // 15453: getstatic       net/minecraft/world/entity/EntityType.PIGLIN:Lnet/minecraft/world/entity/EntityType;
        // 15456: ldc_w           10051392
        // 15459: ldc_w           16380836
        // 15462: new             Lnet/minecraft/world/item/Item$Properties;
        // 15465: dup            
        // 15466: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15469: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15472: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15475: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15478: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15481: putstatic       net/minecraft/world/item/Items.PIGLIN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15484: ldc_w           "piglin_brute_spawn_egg"
        // 15487: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15490: dup            
        // 15491: getstatic       net/minecraft/world/entity/EntityType.PIGLIN_BRUTE:Lnet/minecraft/world/entity/EntityType;
        // 15494: ldc_w           5843472
        // 15497: ldc_w           16380836
        // 15500: new             Lnet/minecraft/world/item/Item$Properties;
        // 15503: dup            
        // 15504: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15507: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15510: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15513: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15516: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15519: putstatic       net/minecraft/world/item/Items.PIGLIN_BRUTE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15522: ldc_w           "pillager_spawn_egg"
        // 15525: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15528: dup            
        // 15529: getstatic       net/minecraft/world/entity/EntityType.PILLAGER:Lnet/minecraft/world/entity/EntityType;
        // 15532: ldc_w           5451574
        // 15535: ldc_w           9804699
        // 15538: new             Lnet/minecraft/world/item/Item$Properties;
        // 15541: dup            
        // 15542: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15545: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15548: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15551: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15554: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15557: putstatic       net/minecraft/world/item/Items.PILLAGER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15560: ldc_w           "polar_bear_spawn_egg"
        // 15563: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15566: dup            
        // 15567: getstatic       net/minecraft/world/entity/EntityType.POLAR_BEAR:Lnet/minecraft/world/entity/EntityType;
        // 15570: ldc_w           15921906
        // 15573: ldc_w           9803152
        // 15576: new             Lnet/minecraft/world/item/Item$Properties;
        // 15579: dup            
        // 15580: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15583: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15586: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15589: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15592: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15595: putstatic       net/minecraft/world/item/Items.POLAR_BEAR_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15598: ldc_w           "pufferfish_spawn_egg"
        // 15601: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15604: dup            
        // 15605: getstatic       net/minecraft/world/entity/EntityType.PUFFERFISH:Lnet/minecraft/world/entity/EntityType;
        // 15608: ldc_w           16167425
        // 15611: ldc_w           3654642
        // 15614: new             Lnet/minecraft/world/item/Item$Properties;
        // 15617: dup            
        // 15618: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15621: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15624: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15627: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15630: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15633: putstatic       net/minecraft/world/item/Items.PUFFERFISH_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15636: ldc_w           "rabbit_spawn_egg"
        // 15639: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15642: dup            
        // 15643: getstatic       net/minecraft/world/entity/EntityType.RABBIT:Lnet/minecraft/world/entity/EntityType;
        // 15646: ldc_w           10051392
        // 15649: ldc_w           7555121
        // 15652: new             Lnet/minecraft/world/item/Item$Properties;
        // 15655: dup            
        // 15656: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15659: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15662: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15665: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15668: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15671: putstatic       net/minecraft/world/item/Items.RABBIT_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15674: ldc_w           "ravager_spawn_egg"
        // 15677: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15680: dup            
        // 15681: getstatic       net/minecraft/world/entity/EntityType.RAVAGER:Lnet/minecraft/world/entity/EntityType;
        // 15684: ldc_w           7697520
        // 15687: ldc_w           5984329
        // 15690: new             Lnet/minecraft/world/item/Item$Properties;
        // 15693: dup            
        // 15694: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15697: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15700: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15703: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15706: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15709: putstatic       net/minecraft/world/item/Items.RAVAGER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15712: ldc_w           "salmon_spawn_egg"
        // 15715: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15718: dup            
        // 15719: getstatic       net/minecraft/world/entity/EntityType.SALMON:Lnet/minecraft/world/entity/EntityType;
        // 15722: ldc_w           10489616
        // 15725: ldc_w           951412
        // 15728: new             Lnet/minecraft/world/item/Item$Properties;
        // 15731: dup            
        // 15732: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15735: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15738: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15741: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15744: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15747: putstatic       net/minecraft/world/item/Items.SALMON_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15750: ldc_w           "sheep_spawn_egg"
        // 15753: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15756: dup            
        // 15757: getstatic       net/minecraft/world/entity/EntityType.SHEEP:Lnet/minecraft/world/entity/EntityType;
        // 15760: ldc_w           15198183
        // 15763: ldc_w           16758197
        // 15766: new             Lnet/minecraft/world/item/Item$Properties;
        // 15769: dup            
        // 15770: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15773: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15776: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15779: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15782: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15785: putstatic       net/minecraft/world/item/Items.SHEEP_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15788: ldc_w           "shulker_spawn_egg"
        // 15791: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15794: dup            
        // 15795: getstatic       net/minecraft/world/entity/EntityType.SHULKER:Lnet/minecraft/world/entity/EntityType;
        // 15798: ldc_w           9725844
        // 15801: ldc_w           5060690
        // 15804: new             Lnet/minecraft/world/item/Item$Properties;
        // 15807: dup            
        // 15808: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15811: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15814: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15817: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15820: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15823: putstatic       net/minecraft/world/item/Items.SHULKER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15826: ldc_w           "silverfish_spawn_egg"
        // 15829: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15832: dup            
        // 15833: getstatic       net/minecraft/world/entity/EntityType.SILVERFISH:Lnet/minecraft/world/entity/EntityType;
        // 15836: ldc_w           7237230
        // 15839: ldc_w           3158064
        // 15842: new             Lnet/minecraft/world/item/Item$Properties;
        // 15845: dup            
        // 15846: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15849: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15852: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15855: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15858: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15861: putstatic       net/minecraft/world/item/Items.SILVERFISH_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15864: ldc_w           "skeleton_spawn_egg"
        // 15867: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15870: dup            
        // 15871: getstatic       net/minecraft/world/entity/EntityType.SKELETON:Lnet/minecraft/world/entity/EntityType;
        // 15874: ldc_w           12698049
        // 15877: ldc_w           4802889
        // 15880: new             Lnet/minecraft/world/item/Item$Properties;
        // 15883: dup            
        // 15884: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15887: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15890: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15893: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15896: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15899: putstatic       net/minecraft/world/item/Items.SKELETON_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15902: ldc_w           "skeleton_horse_spawn_egg"
        // 15905: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15908: dup            
        // 15909: getstatic       net/minecraft/world/entity/EntityType.SKELETON_HORSE:Lnet/minecraft/world/entity/EntityType;
        // 15912: ldc_w           6842447
        // 15915: ldc_w           15066584
        // 15918: new             Lnet/minecraft/world/item/Item$Properties;
        // 15921: dup            
        // 15922: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15925: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15928: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15931: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15934: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15937: putstatic       net/minecraft/world/item/Items.SKELETON_HORSE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15940: ldc_w           "slime_spawn_egg"
        // 15943: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15946: dup            
        // 15947: getstatic       net/minecraft/world/entity/EntityType.SLIME:Lnet/minecraft/world/entity/EntityType;
        // 15950: ldc_w           5349438
        // 15953: ldc_w           8306542
        // 15956: new             Lnet/minecraft/world/item/Item$Properties;
        // 15959: dup            
        // 15960: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 15963: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 15966: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 15969: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 15972: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 15975: putstatic       net/minecraft/world/item/Items.SLIME_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 15978: ldc_w           "spider_spawn_egg"
        // 15981: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 15984: dup            
        // 15985: getstatic       net/minecraft/world/entity/EntityType.SPIDER:Lnet/minecraft/world/entity/EntityType;
        // 15988: ldc_w           3419431
        // 15991: ldc_w           11013646
        // 15994: new             Lnet/minecraft/world/item/Item$Properties;
        // 15997: dup            
        // 15998: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16001: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16004: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16007: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16010: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16013: putstatic       net/minecraft/world/item/Items.SPIDER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16016: ldc_w           "squid_spawn_egg"
        // 16019: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16022: dup            
        // 16023: getstatic       net/minecraft/world/entity/EntityType.SQUID:Lnet/minecraft/world/entity/EntityType;
        // 16026: ldc_w           2243405
        // 16029: ldc_w           7375001
        // 16032: new             Lnet/minecraft/world/item/Item$Properties;
        // 16035: dup            
        // 16036: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16039: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16042: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16045: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16048: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16051: putstatic       net/minecraft/world/item/Items.SQUID_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16054: ldc_w           "stray_spawn_egg"
        // 16057: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16060: dup            
        // 16061: getstatic       net/minecraft/world/entity/EntityType.STRAY:Lnet/minecraft/world/entity/EntityType;
        // 16064: ldc_w           6387319
        // 16067: ldc_w           14543594
        // 16070: new             Lnet/minecraft/world/item/Item$Properties;
        // 16073: dup            
        // 16074: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16077: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16080: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16083: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16086: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16089: putstatic       net/minecraft/world/item/Items.STRAY_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16092: ldc_w           "strider_spawn_egg"
        // 16095: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16098: dup            
        // 16099: getstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        // 16102: ldc_w           10236982
        // 16105: ldc_w           5065037
        // 16108: new             Lnet/minecraft/world/item/Item$Properties;
        // 16111: dup            
        // 16112: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16115: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16118: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16121: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16124: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16127: putstatic       net/minecraft/world/item/Items.STRIDER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16130: ldc_w           "trader_llama_spawn_egg"
        // 16133: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16136: dup            
        // 16137: getstatic       net/minecraft/world/entity/EntityType.TRADER_LLAMA:Lnet/minecraft/world/entity/EntityType;
        // 16140: ldc_w           15377456
        // 16143: ldc_w           4547222
        // 16146: new             Lnet/minecraft/world/item/Item$Properties;
        // 16149: dup            
        // 16150: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16153: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16156: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16159: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16162: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16165: putstatic       net/minecraft/world/item/Items.TRADER_LLAMA_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16168: ldc_w           "tropical_fish_spawn_egg"
        // 16171: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16174: dup            
        // 16175: getstatic       net/minecraft/world/entity/EntityType.TROPICAL_FISH:Lnet/minecraft/world/entity/EntityType;
        // 16178: ldc_w           15690005
        // 16181: ldc_w           16775663
        // 16184: new             Lnet/minecraft/world/item/Item$Properties;
        // 16187: dup            
        // 16188: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16191: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16194: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16197: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16200: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16203: putstatic       net/minecraft/world/item/Items.TROPICAL_FISH_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16206: ldc_w           "turtle_spawn_egg"
        // 16209: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16212: dup            
        // 16213: getstatic       net/minecraft/world/entity/EntityType.TURTLE:Lnet/minecraft/world/entity/EntityType;
        // 16216: ldc_w           15198183
        // 16219: ldc_w           44975
        // 16222: new             Lnet/minecraft/world/item/Item$Properties;
        // 16225: dup            
        // 16226: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16229: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16232: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16235: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16238: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16241: putstatic       net/minecraft/world/item/Items.TURTLE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16244: ldc_w           "vex_spawn_egg"
        // 16247: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16250: dup            
        // 16251: getstatic       net/minecraft/world/entity/EntityType.VEX:Lnet/minecraft/world/entity/EntityType;
        // 16254: ldc_w           8032420
        // 16257: ldc_w           15265265
        // 16260: new             Lnet/minecraft/world/item/Item$Properties;
        // 16263: dup            
        // 16264: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16267: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16270: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16273: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16276: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16279: putstatic       net/minecraft/world/item/Items.VEX_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16282: ldc_w           "villager_spawn_egg"
        // 16285: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16288: dup            
        // 16289: getstatic       net/minecraft/world/entity/EntityType.VILLAGER:Lnet/minecraft/world/entity/EntityType;
        // 16292: ldc_w           5651507
        // 16295: ldc_w           12422002
        // 16298: new             Lnet/minecraft/world/item/Item$Properties;
        // 16301: dup            
        // 16302: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16305: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16308: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16311: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16314: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16317: putstatic       net/minecraft/world/item/Items.VILLAGER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16320: ldc_w           "vindicator_spawn_egg"
        // 16323: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16326: dup            
        // 16327: getstatic       net/minecraft/world/entity/EntityType.VINDICATOR:Lnet/minecraft/world/entity/EntityType;
        // 16330: ldc_w           9804699
        // 16333: ldc_w           2580065
        // 16336: new             Lnet/minecraft/world/item/Item$Properties;
        // 16339: dup            
        // 16340: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16343: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16346: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16349: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16352: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16355: putstatic       net/minecraft/world/item/Items.VINDICATOR_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16358: ldc_w           "wandering_trader_spawn_egg"
        // 16361: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16364: dup            
        // 16365: getstatic       net/minecraft/world/entity/EntityType.WANDERING_TRADER:Lnet/minecraft/world/entity/EntityType;
        // 16368: ldc_w           4547222
        // 16371: ldc_w           15377456
        // 16374: new             Lnet/minecraft/world/item/Item$Properties;
        // 16377: dup            
        // 16378: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16381: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16384: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16387: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16390: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16393: putstatic       net/minecraft/world/item/Items.WANDERING_TRADER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16396: ldc_w           "witch_spawn_egg"
        // 16399: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16402: dup            
        // 16403: getstatic       net/minecraft/world/entity/EntityType.WITCH:Lnet/minecraft/world/entity/EntityType;
        // 16406: ldc_w           3407872
        // 16409: ldc_w           5349438
        // 16412: new             Lnet/minecraft/world/item/Item$Properties;
        // 16415: dup            
        // 16416: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16419: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16422: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16425: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16428: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16431: putstatic       net/minecraft/world/item/Items.WITCH_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16434: ldc_w           "wither_skeleton_spawn_egg"
        // 16437: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16440: dup            
        // 16441: getstatic       net/minecraft/world/entity/EntityType.WITHER_SKELETON:Lnet/minecraft/world/entity/EntityType;
        // 16444: ldc_w           1315860
        // 16447: ldc_w           4672845
        // 16450: new             Lnet/minecraft/world/item/Item$Properties;
        // 16453: dup            
        // 16454: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16457: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16460: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16463: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16466: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16469: putstatic       net/minecraft/world/item/Items.WITHER_SKELETON_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16472: ldc_w           "wolf_spawn_egg"
        // 16475: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16478: dup            
        // 16479: getstatic       net/minecraft/world/entity/EntityType.WOLF:Lnet/minecraft/world/entity/EntityType;
        // 16482: ldc_w           14144467
        // 16485: ldc_w           13545366
        // 16488: new             Lnet/minecraft/world/item/Item$Properties;
        // 16491: dup            
        // 16492: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16495: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16498: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16501: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16504: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16507: putstatic       net/minecraft/world/item/Items.WOLF_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16510: ldc_w           "zoglin_spawn_egg"
        // 16513: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16516: dup            
        // 16517: getstatic       net/minecraft/world/entity/EntityType.ZOGLIN:Lnet/minecraft/world/entity/EntityType;
        // 16520: ldc_w           13004373
        // 16523: ldc_w           15132390
        // 16526: new             Lnet/minecraft/world/item/Item$Properties;
        // 16529: dup            
        // 16530: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16533: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16536: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16539: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16542: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16545: putstatic       net/minecraft/world/item/Items.ZOGLIN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16548: ldc_w           "zombie_spawn_egg"
        // 16551: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16554: dup            
        // 16555: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE:Lnet/minecraft/world/entity/EntityType;
        // 16558: ldc_w           44975
        // 16561: ldc_w           7969893
        // 16564: new             Lnet/minecraft/world/item/Item$Properties;
        // 16567: dup            
        // 16568: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16571: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16574: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16577: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16580: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16583: putstatic       net/minecraft/world/item/Items.ZOMBIE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16586: ldc_w           "zombie_horse_spawn_egg"
        // 16589: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16592: dup            
        // 16593: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_HORSE:Lnet/minecraft/world/entity/EntityType;
        // 16596: ldc_w           3232308
        // 16599: ldc_w           9945732
        // 16602: new             Lnet/minecraft/world/item/Item$Properties;
        // 16605: dup            
        // 16606: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16609: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16612: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16615: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16618: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16621: putstatic       net/minecraft/world/item/Items.ZOMBIE_HORSE_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16624: ldc_w           "zombie_villager_spawn_egg"
        // 16627: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16630: dup            
        // 16631: getstatic       net/minecraft/world/entity/EntityType.ZOMBIE_VILLAGER:Lnet/minecraft/world/entity/EntityType;
        // 16634: ldc_w           5651507
        // 16637: ldc_w           7969893
        // 16640: new             Lnet/minecraft/world/item/Item$Properties;
        // 16643: dup            
        // 16644: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16647: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16650: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16653: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16656: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16659: putstatic       net/minecraft/world/item/Items.ZOMBIE_VILLAGER_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16662: ldc_w           "zombified_piglin_spawn_egg"
        // 16665: new             Lnet/minecraft/world/item/SpawnEggItem;
        // 16668: dup            
        // 16669: getstatic       net/minecraft/world/entity/EntityType.ZOMBIFIED_PIGLIN:Lnet/minecraft/world/entity/EntityType;
        // 16672: ldc_w           15373203
        // 16675: ldc_w           5009705
        // 16678: new             Lnet/minecraft/world/item/Item$Properties;
        // 16681: dup            
        // 16682: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16685: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16688: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16691: invokespecial   net/minecraft/world/item/SpawnEggItem.<init>:(Lnet/minecraft/world/entity/EntityType;IILnet/minecraft/world/item/Item$Properties;)V
        // 16694: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16697: putstatic       net/minecraft/world/item/Items.ZOMBIFIED_PIGLIN_SPAWN_EGG:Lnet/minecraft/world/item/Item;
        // 16700: ldc_w           "experience_bottle"
        // 16703: new             Lnet/minecraft/world/item/ExperienceBottleItem;
        // 16706: dup            
        // 16707: new             Lnet/minecraft/world/item/Item$Properties;
        // 16710: dup            
        // 16711: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16714: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16717: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16720: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 16723: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 16726: invokespecial   net/minecraft/world/item/ExperienceBottleItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 16729: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16732: putstatic       net/minecraft/world/item/Items.EXPERIENCE_BOTTLE:Lnet/minecraft/world/item/Item;
        // 16735: ldc_w           "fire_charge"
        // 16738: new             Lnet/minecraft/world/item/FireChargeItem;
        // 16741: dup            
        // 16742: new             Lnet/minecraft/world/item/Item$Properties;
        // 16745: dup            
        // 16746: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16749: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16752: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16755: invokespecial   net/minecraft/world/item/FireChargeItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 16758: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16761: putstatic       net/minecraft/world/item/Items.FIRE_CHARGE:Lnet/minecraft/world/item/Item;
        // 16764: ldc_w           "writable_book"
        // 16767: new             Lnet/minecraft/world/item/WritableBookItem;
        // 16770: dup            
        // 16771: new             Lnet/minecraft/world/item/Item$Properties;
        // 16774: dup            
        // 16775: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16778: iconst_1       
        // 16779: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 16782: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 16785: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16788: invokespecial   net/minecraft/world/item/WritableBookItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 16791: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16794: putstatic       net/minecraft/world/item/Items.WRITABLE_BOOK:Lnet/minecraft/world/item/Item;
        // 16797: ldc_w           "written_book"
        // 16800: new             Lnet/minecraft/world/item/WrittenBookItem;
        // 16803: dup            
        // 16804: new             Lnet/minecraft/world/item/Item$Properties;
        // 16807: dup            
        // 16808: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16811: bipush          16
        // 16813: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 16816: invokespecial   net/minecraft/world/item/WrittenBookItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 16819: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16822: putstatic       net/minecraft/world/item/Items.WRITTEN_BOOK:Lnet/minecraft/world/item/Item;
        // 16825: ldc_w           "emerald"
        // 16828: new             Lnet/minecraft/world/item/Item;
        // 16831: dup            
        // 16832: new             Lnet/minecraft/world/item/Item$Properties;
        // 16835: dup            
        // 16836: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16839: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 16842: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16845: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 16848: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16851: putstatic       net/minecraft/world/item/Items.EMERALD:Lnet/minecraft/world/item/Item;
        // 16854: ldc_w           "item_frame"
        // 16857: new             Lnet/minecraft/world/item/ItemFrameItem;
        // 16860: dup            
        // 16861: new             Lnet/minecraft/world/item/Item$Properties;
        // 16864: dup            
        // 16865: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16868: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 16871: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16874: invokespecial   net/minecraft/world/item/ItemFrameItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 16877: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16880: putstatic       net/minecraft/world/item/Items.ITEM_FRAME:Lnet/minecraft/world/item/Item;
        // 16883: getstatic       net/minecraft/world/level/block/Blocks.FLOWER_POT:Lnet/minecraft/world/level/block/Block;
        // 16886: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 16889: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 16892: putstatic       net/minecraft/world/item/Items.FLOWER_POT:Lnet/minecraft/world/item/Item;
        // 16895: ldc_w           "carrot"
        // 16898: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 16901: dup            
        // 16902: getstatic       net/minecraft/world/level/block/Blocks.CARROTS:Lnet/minecraft/world/level/block/Block;
        // 16905: new             Lnet/minecraft/world/item/Item$Properties;
        // 16908: dup            
        // 16909: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16912: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 16915: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16918: getstatic       net/minecraft/world/food/Foods.CARROT:Lnet/minecraft/world/food/FoodProperties;
        // 16921: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 16924: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 16927: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16930: putstatic       net/minecraft/world/item/Items.CARROT:Lnet/minecraft/world/item/Item;
        // 16933: ldc_w           "potato"
        // 16936: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 16939: dup            
        // 16940: getstatic       net/minecraft/world/level/block/Blocks.POTATOES:Lnet/minecraft/world/level/block/Block;
        // 16943: new             Lnet/minecraft/world/item/Item$Properties;
        // 16946: dup            
        // 16947: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16950: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 16953: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16956: getstatic       net/minecraft/world/food/Foods.POTATO:Lnet/minecraft/world/food/FoodProperties;
        // 16959: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 16962: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 16965: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 16968: putstatic       net/minecraft/world/item/Items.POTATO:Lnet/minecraft/world/item/Item;
        // 16971: ldc_w           "baked_potato"
        // 16974: new             Lnet/minecraft/world/item/Item;
        // 16977: dup            
        // 16978: new             Lnet/minecraft/world/item/Item$Properties;
        // 16981: dup            
        // 16982: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 16985: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 16988: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 16991: getstatic       net/minecraft/world/food/Foods.BAKED_POTATO:Lnet/minecraft/world/food/FoodProperties;
        // 16994: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 16997: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17000: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17003: putstatic       net/minecraft/world/item/Items.BAKED_POTATO:Lnet/minecraft/world/item/Item;
        // 17006: ldc_w           "poisonous_potato"
        // 17009: new             Lnet/minecraft/world/item/Item;
        // 17012: dup            
        // 17013: new             Lnet/minecraft/world/item/Item$Properties;
        // 17016: dup            
        // 17017: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17020: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 17023: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17026: getstatic       net/minecraft/world/food/Foods.POISONOUS_POTATO:Lnet/minecraft/world/food/FoodProperties;
        // 17029: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 17032: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17035: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17038: putstatic       net/minecraft/world/item/Items.POISONOUS_POTATO:Lnet/minecraft/world/item/Item;
        // 17041: ldc_w           "map"
        // 17044: new             Lnet/minecraft/world/item/EmptyMapItem;
        // 17047: dup            
        // 17048: new             Lnet/minecraft/world/item/Item$Properties;
        // 17051: dup            
        // 17052: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17055: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 17058: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17061: invokespecial   net/minecraft/world/item/EmptyMapItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17064: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17067: putstatic       net/minecraft/world/item/Items.MAP:Lnet/minecraft/world/item/Item;
        // 17070: ldc_w           "golden_carrot"
        // 17073: new             Lnet/minecraft/world/item/Item;
        // 17076: dup            
        // 17077: new             Lnet/minecraft/world/item/Item$Properties;
        // 17080: dup            
        // 17081: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17084: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 17087: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17090: getstatic       net/minecraft/world/food/Foods.GOLDEN_CARROT:Lnet/minecraft/world/food/FoodProperties;
        // 17093: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 17096: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17099: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17102: putstatic       net/minecraft/world/item/Items.GOLDEN_CARROT:Lnet/minecraft/world/item/Item;
        // 17105: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        // 17108: dup            
        // 17109: getstatic       net/minecraft/world/level/block/Blocks.SKELETON_SKULL:Lnet/minecraft/world/level/block/Block;
        // 17112: getstatic       net/minecraft/world/level/block/Blocks.SKELETON_WALL_SKULL:Lnet/minecraft/world/level/block/Block;
        // 17115: new             Lnet/minecraft/world/item/Item$Properties;
        // 17118: dup            
        // 17119: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17122: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17125: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17128: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17131: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17134: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 17137: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 17140: putstatic       net/minecraft/world/item/Items.SKELETON_SKULL:Lnet/minecraft/world/item/Item;
        // 17143: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        // 17146: dup            
        // 17147: getstatic       net/minecraft/world/level/block/Blocks.WITHER_SKELETON_SKULL:Lnet/minecraft/world/level/block/Block;
        // 17150: getstatic       net/minecraft/world/level/block/Blocks.WITHER_SKELETON_WALL_SKULL:Lnet/minecraft/world/level/block/Block;
        // 17153: new             Lnet/minecraft/world/item/Item$Properties;
        // 17156: dup            
        // 17157: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17160: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17163: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17166: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17169: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17172: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 17175: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 17178: putstatic       net/minecraft/world/item/Items.WITHER_SKELETON_SKULL:Lnet/minecraft/world/item/Item;
        // 17181: new             Lnet/minecraft/world/item/PlayerHeadItem;
        // 17184: dup            
        // 17185: getstatic       net/minecraft/world/level/block/Blocks.PLAYER_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17188: getstatic       net/minecraft/world/level/block/Blocks.PLAYER_WALL_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17191: new             Lnet/minecraft/world/item/Item$Properties;
        // 17194: dup            
        // 17195: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17198: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17201: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17204: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17207: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17210: invokespecial   net/minecraft/world/item/PlayerHeadItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 17213: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 17216: putstatic       net/minecraft/world/item/Items.PLAYER_HEAD:Lnet/minecraft/world/item/Item;
        // 17219: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        // 17222: dup            
        // 17223: getstatic       net/minecraft/world/level/block/Blocks.ZOMBIE_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17226: getstatic       net/minecraft/world/level/block/Blocks.ZOMBIE_WALL_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17229: new             Lnet/minecraft/world/item/Item$Properties;
        // 17232: dup            
        // 17233: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17236: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17239: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17242: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17245: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17248: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 17251: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 17254: putstatic       net/minecraft/world/item/Items.ZOMBIE_HEAD:Lnet/minecraft/world/item/Item;
        // 17257: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        // 17260: dup            
        // 17261: getstatic       net/minecraft/world/level/block/Blocks.CREEPER_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17264: getstatic       net/minecraft/world/level/block/Blocks.CREEPER_WALL_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17267: new             Lnet/minecraft/world/item/Item$Properties;
        // 17270: dup            
        // 17271: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17274: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17277: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17280: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17283: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17286: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 17289: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 17292: putstatic       net/minecraft/world/item/Items.CREEPER_HEAD:Lnet/minecraft/world/item/Item;
        // 17295: new             Lnet/minecraft/world/item/StandingAndWallBlockItem;
        // 17298: dup            
        // 17299: getstatic       net/minecraft/world/level/block/Blocks.DRAGON_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17302: getstatic       net/minecraft/world/level/block/Blocks.DRAGON_WALL_HEAD:Lnet/minecraft/world/level/block/Block;
        // 17305: new             Lnet/minecraft/world/item/Item$Properties;
        // 17308: dup            
        // 17309: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17312: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17315: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17318: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17321: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17324: invokespecial   net/minecraft/world/item/StandingAndWallBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 17327: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 17330: putstatic       net/minecraft/world/item/Items.DRAGON_HEAD:Lnet/minecraft/world/item/Item;
        // 17333: ldc_w           "carrot_on_a_stick"
        // 17336: new             Lnet/minecraft/world/item/FoodOnAStickItem;
        // 17339: dup            
        // 17340: new             Lnet/minecraft/world/item/Item$Properties;
        // 17343: dup            
        // 17344: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17347: bipush          25
        // 17349: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17352: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 17355: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17358: getstatic       net/minecraft/world/entity/EntityType.PIG:Lnet/minecraft/world/entity/EntityType;
        // 17361: bipush          7
        // 17363: invokespecial   net/minecraft/world/item/FoodOnAStickItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/entity/EntityType;I)V
        // 17366: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17369: putstatic       net/minecraft/world/item/Items.CARROT_ON_A_STICK:Lnet/minecraft/world/item/Item;
        // 17372: ldc_w           "warped_fungus_on_a_stick"
        // 17375: new             Lnet/minecraft/world/item/FoodOnAStickItem;
        // 17378: dup            
        // 17379: new             Lnet/minecraft/world/item/Item$Properties;
        // 17382: dup            
        // 17383: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17386: bipush          100
        // 17388: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17391: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 17394: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17397: getstatic       net/minecraft/world/entity/EntityType.STRIDER:Lnet/minecraft/world/entity/EntityType;
        // 17400: iconst_1       
        // 17401: invokespecial   net/minecraft/world/item/FoodOnAStickItem.<init>:(Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/world/entity/EntityType;I)V
        // 17404: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17407: putstatic       net/minecraft/world/item/Items.WARPED_FUNGUS_ON_A_STICK:Lnet/minecraft/world/item/Item;
        // 17410: ldc_w           "nether_star"
        // 17413: new             Lnet/minecraft/world/item/SimpleFoiledItem;
        // 17416: dup            
        // 17417: new             Lnet/minecraft/world/item/Item$Properties;
        // 17420: dup            
        // 17421: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17424: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17427: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17430: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17433: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17436: invokespecial   net/minecraft/world/item/SimpleFoiledItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17439: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17442: putstatic       net/minecraft/world/item/Items.NETHER_STAR:Lnet/minecraft/world/item/Item;
        // 17445: ldc_w           "pumpkin_pie"
        // 17448: new             Lnet/minecraft/world/item/Item;
        // 17451: dup            
        // 17452: new             Lnet/minecraft/world/item/Item$Properties;
        // 17455: dup            
        // 17456: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17459: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 17462: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17465: getstatic       net/minecraft/world/food/Foods.PUMPKIN_PIE:Lnet/minecraft/world/food/FoodProperties;
        // 17468: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 17471: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17474: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17477: putstatic       net/minecraft/world/item/Items.PUMPKIN_PIE:Lnet/minecraft/world/item/Item;
        // 17480: ldc_w           "firework_rocket"
        // 17483: new             Lnet/minecraft/world/item/FireworkRocketItem;
        // 17486: dup            
        // 17487: new             Lnet/minecraft/world/item/Item$Properties;
        // 17490: dup            
        // 17491: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17494: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 17497: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17500: invokespecial   net/minecraft/world/item/FireworkRocketItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17503: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17506: putstatic       net/minecraft/world/item/Items.FIREWORK_ROCKET:Lnet/minecraft/world/item/Item;
        // 17509: ldc_w           "firework_star"
        // 17512: new             Lnet/minecraft/world/item/FireworkStarItem;
        // 17515: dup            
        // 17516: new             Lnet/minecraft/world/item/Item$Properties;
        // 17519: dup            
        // 17520: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17523: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 17526: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17529: invokespecial   net/minecraft/world/item/FireworkStarItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17532: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17535: putstatic       net/minecraft/world/item/Items.FIREWORK_STAR:Lnet/minecraft/world/item/Item;
        // 17538: ldc_w           "enchanted_book"
        // 17541: new             Lnet/minecraft/world/item/EnchantedBookItem;
        // 17544: dup            
        // 17545: new             Lnet/minecraft/world/item/Item$Properties;
        // 17548: dup            
        // 17549: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17552: iconst_1       
        // 17553: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17556: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 17559: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 17562: invokespecial   net/minecraft/world/item/EnchantedBookItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17565: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17568: putstatic       net/minecraft/world/item/Items.ENCHANTED_BOOK:Lnet/minecraft/world/item/Item;
        // 17571: ldc_w           "nether_brick"
        // 17574: new             Lnet/minecraft/world/item/Item;
        // 17577: dup            
        // 17578: new             Lnet/minecraft/world/item/Item$Properties;
        // 17581: dup            
        // 17582: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17585: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17588: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17591: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17594: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17597: putstatic       net/minecraft/world/item/Items.NETHER_BRICK:Lnet/minecraft/world/item/Item;
        // 17600: ldc_w           "quartz"
        // 17603: new             Lnet/minecraft/world/item/Item;
        // 17606: dup            
        // 17607: new             Lnet/minecraft/world/item/Item$Properties;
        // 17610: dup            
        // 17611: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17614: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17617: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17620: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17623: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17626: putstatic       net/minecraft/world/item/Items.QUARTZ:Lnet/minecraft/world/item/Item;
        // 17629: ldc_w           "tnt_minecart"
        // 17632: new             Lnet/minecraft/world/item/MinecartItem;
        // 17635: dup            
        // 17636: getstatic       net/minecraft/world/entity/vehicle/AbstractMinecart$Type.TNT:Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;
        // 17639: new             Lnet/minecraft/world/item/Item$Properties;
        // 17642: dup            
        // 17643: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17646: iconst_1       
        // 17647: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17650: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 17653: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17656: invokespecial   net/minecraft/world/item/MinecartItem.<init>:(Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 17659: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17662: putstatic       net/minecraft/world/item/Items.TNT_MINECART:Lnet/minecraft/world/item/Item;
        // 17665: ldc_w           "hopper_minecart"
        // 17668: new             Lnet/minecraft/world/item/MinecartItem;
        // 17671: dup            
        // 17672: getstatic       net/minecraft/world/entity/vehicle/AbstractMinecart$Type.HOPPER:Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;
        // 17675: new             Lnet/minecraft/world/item/Item$Properties;
        // 17678: dup            
        // 17679: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17682: iconst_1       
        // 17683: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17686: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 17689: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17692: invokespecial   net/minecraft/world/item/MinecartItem.<init>:(Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 17695: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17698: putstatic       net/minecraft/world/item/Items.HOPPER_MINECART:Lnet/minecraft/world/item/Item;
        // 17701: ldc_w           "prismarine_shard"
        // 17704: new             Lnet/minecraft/world/item/Item;
        // 17707: dup            
        // 17708: new             Lnet/minecraft/world/item/Item$Properties;
        // 17711: dup            
        // 17712: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17715: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17718: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17721: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17724: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17727: putstatic       net/minecraft/world/item/Items.PRISMARINE_SHARD:Lnet/minecraft/world/item/Item;
        // 17730: ldc_w           "prismarine_crystals"
        // 17733: new             Lnet/minecraft/world/item/Item;
        // 17736: dup            
        // 17737: new             Lnet/minecraft/world/item/Item$Properties;
        // 17740: dup            
        // 17741: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17744: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17747: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17750: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17753: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17756: putstatic       net/minecraft/world/item/Items.PRISMARINE_CRYSTALS:Lnet/minecraft/world/item/Item;
        // 17759: ldc_w           "rabbit"
        // 17762: new             Lnet/minecraft/world/item/Item;
        // 17765: dup            
        // 17766: new             Lnet/minecraft/world/item/Item$Properties;
        // 17769: dup            
        // 17770: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17773: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 17776: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17779: getstatic       net/minecraft/world/food/Foods.RABBIT:Lnet/minecraft/world/food/FoodProperties;
        // 17782: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 17785: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17788: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17791: putstatic       net/minecraft/world/item/Items.RABBIT:Lnet/minecraft/world/item/Item;
        // 17794: ldc_w           "cooked_rabbit"
        // 17797: new             Lnet/minecraft/world/item/Item;
        // 17800: dup            
        // 17801: new             Lnet/minecraft/world/item/Item$Properties;
        // 17804: dup            
        // 17805: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17808: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 17811: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17814: getstatic       net/minecraft/world/food/Foods.COOKED_RABBIT:Lnet/minecraft/world/food/FoodProperties;
        // 17817: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 17820: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17823: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17826: putstatic       net/minecraft/world/item/Items.COOKED_RABBIT:Lnet/minecraft/world/item/Item;
        // 17829: ldc_w           "rabbit_stew"
        // 17832: new             Lnet/minecraft/world/item/BowlFoodItem;
        // 17835: dup            
        // 17836: new             Lnet/minecraft/world/item/Item$Properties;
        // 17839: dup            
        // 17840: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17843: iconst_1       
        // 17844: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17847: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 17850: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17853: getstatic       net/minecraft/world/food/Foods.RABBIT_STEW:Lnet/minecraft/world/food/FoodProperties;
        // 17856: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 17859: invokespecial   net/minecraft/world/item/BowlFoodItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17862: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17865: putstatic       net/minecraft/world/item/Items.RABBIT_STEW:Lnet/minecraft/world/item/Item;
        // 17868: ldc_w           "rabbit_foot"
        // 17871: new             Lnet/minecraft/world/item/Item;
        // 17874: dup            
        // 17875: new             Lnet/minecraft/world/item/Item$Properties;
        // 17878: dup            
        // 17879: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17882: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 17885: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17888: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17891: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17894: putstatic       net/minecraft/world/item/Items.RABBIT_FOOT:Lnet/minecraft/world/item/Item;
        // 17897: ldc_w           "rabbit_hide"
        // 17900: new             Lnet/minecraft/world/item/Item;
        // 17903: dup            
        // 17904: new             Lnet/minecraft/world/item/Item$Properties;
        // 17907: dup            
        // 17908: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17911: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17914: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17917: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17920: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17923: putstatic       net/minecraft/world/item/Items.RABBIT_HIDE:Lnet/minecraft/world/item/Item;
        // 17926: ldc_w           "armor_stand"
        // 17929: new             Lnet/minecraft/world/item/ArmorStandItem;
        // 17932: dup            
        // 17933: new             Lnet/minecraft/world/item/Item$Properties;
        // 17936: dup            
        // 17937: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17940: bipush          16
        // 17942: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17945: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 17948: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17951: invokespecial   net/minecraft/world/item/ArmorStandItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 17954: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17957: putstatic       net/minecraft/world/item/Items.ARMOR_STAND:Lnet/minecraft/world/item/Item;
        // 17960: ldc_w           "iron_horse_armor"
        // 17963: new             Lnet/minecraft/world/item/HorseArmorItem;
        // 17966: dup            
        // 17967: iconst_5       
        // 17968: ldc_w           "iron"
        // 17971: new             Lnet/minecraft/world/item/Item$Properties;
        // 17974: dup            
        // 17975: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 17978: iconst_1       
        // 17979: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 17982: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 17985: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 17988: invokespecial   net/minecraft/world/item/HorseArmorItem.<init>:(ILjava/lang/String;Lnet/minecraft/world/item/Item$Properties;)V
        // 17991: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 17994: putstatic       net/minecraft/world/item/Items.IRON_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        // 17997: ldc_w           "golden_horse_armor"
        // 18000: new             Lnet/minecraft/world/item/HorseArmorItem;
        // 18003: dup            
        // 18004: bipush          7
        // 18006: ldc_w           "gold"
        // 18009: new             Lnet/minecraft/world/item/Item$Properties;
        // 18012: dup            
        // 18013: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18016: iconst_1       
        // 18017: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18020: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 18023: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18026: invokespecial   net/minecraft/world/item/HorseArmorItem.<init>:(ILjava/lang/String;Lnet/minecraft/world/item/Item$Properties;)V
        // 18029: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18032: putstatic       net/minecraft/world/item/Items.GOLDEN_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        // 18035: ldc_w           "diamond_horse_armor"
        // 18038: new             Lnet/minecraft/world/item/HorseArmorItem;
        // 18041: dup            
        // 18042: bipush          11
        // 18044: ldc_w           "diamond"
        // 18047: new             Lnet/minecraft/world/item/Item$Properties;
        // 18050: dup            
        // 18051: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18054: iconst_1       
        // 18055: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18058: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 18061: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18064: invokespecial   net/minecraft/world/item/HorseArmorItem.<init>:(ILjava/lang/String;Lnet/minecraft/world/item/Item$Properties;)V
        // 18067: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18070: putstatic       net/minecraft/world/item/Items.DIAMOND_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        // 18073: ldc_w           "leather_horse_armor"
        // 18076: new             Lnet/minecraft/world/item/DyeableHorseArmorItem;
        // 18079: dup            
        // 18080: iconst_3       
        // 18081: ldc_w           "leather"
        // 18084: new             Lnet/minecraft/world/item/Item$Properties;
        // 18087: dup            
        // 18088: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18091: iconst_1       
        // 18092: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18095: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 18098: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18101: invokespecial   net/minecraft/world/item/DyeableHorseArmorItem.<init>:(ILjava/lang/String;Lnet/minecraft/world/item/Item$Properties;)V
        // 18104: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18107: putstatic       net/minecraft/world/item/Items.LEATHER_HORSE_ARMOR:Lnet/minecraft/world/item/Item;
        // 18110: ldc_w           "lead"
        // 18113: new             Lnet/minecraft/world/item/LeadItem;
        // 18116: dup            
        // 18117: new             Lnet/minecraft/world/item/Item$Properties;
        // 18120: dup            
        // 18121: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18124: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18127: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18130: invokespecial   net/minecraft/world/item/LeadItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 18133: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18136: putstatic       net/minecraft/world/item/Items.LEAD:Lnet/minecraft/world/item/Item;
        // 18139: ldc_w           "name_tag"
        // 18142: new             Lnet/minecraft/world/item/NameTagItem;
        // 18145: dup            
        // 18146: new             Lnet/minecraft/world/item/Item$Properties;
        // 18149: dup            
        // 18150: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18153: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TOOLS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18156: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18159: invokespecial   net/minecraft/world/item/NameTagItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 18162: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18165: putstatic       net/minecraft/world/item/Items.NAME_TAG:Lnet/minecraft/world/item/Item;
        // 18168: ldc_w           "command_block_minecart"
        // 18171: new             Lnet/minecraft/world/item/MinecartItem;
        // 18174: dup            
        // 18175: getstatic       net/minecraft/world/entity/vehicle/AbstractMinecart$Type.COMMAND_BLOCK:Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;
        // 18178: new             Lnet/minecraft/world/item/Item$Properties;
        // 18181: dup            
        // 18182: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18185: iconst_1       
        // 18186: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18189: invokespecial   net/minecraft/world/item/MinecartItem.<init>:(Lnet/minecraft/world/entity/vehicle/AbstractMinecart$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 18192: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18195: putstatic       net/minecraft/world/item/Items.COMMAND_BLOCK_MINECART:Lnet/minecraft/world/item/Item;
        // 18198: ldc_w           "mutton"
        // 18201: new             Lnet/minecraft/world/item/Item;
        // 18204: dup            
        // 18205: new             Lnet/minecraft/world/item/Item$Properties;
        // 18208: dup            
        // 18209: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18212: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 18215: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18218: getstatic       net/minecraft/world/food/Foods.MUTTON:Lnet/minecraft/world/food/FoodProperties;
        // 18221: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 18224: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 18227: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18230: putstatic       net/minecraft/world/item/Items.MUTTON:Lnet/minecraft/world/item/Item;
        // 18233: ldc_w           "cooked_mutton"
        // 18236: new             Lnet/minecraft/world/item/Item;
        // 18239: dup            
        // 18240: new             Lnet/minecraft/world/item/Item$Properties;
        // 18243: dup            
        // 18244: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18247: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 18250: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18253: getstatic       net/minecraft/world/food/Foods.COOKED_MUTTON:Lnet/minecraft/world/food/FoodProperties;
        // 18256: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 18259: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 18262: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18265: putstatic       net/minecraft/world/item/Items.COOKED_MUTTON:Lnet/minecraft/world/item/Item;
        // 18268: ldc_w           "white_banner"
        // 18271: new             Lnet/minecraft/world/item/BannerItem;
        // 18274: dup            
        // 18275: getstatic       net/minecraft/world/level/block/Blocks.WHITE_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18278: getstatic       net/minecraft/world/level/block/Blocks.WHITE_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18281: new             Lnet/minecraft/world/item/Item$Properties;
        // 18284: dup            
        // 18285: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18288: bipush          16
        // 18290: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18293: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18296: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18299: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18302: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18305: putstatic       net/minecraft/world/item/Items.WHITE_BANNER:Lnet/minecraft/world/item/Item;
        // 18308: ldc_w           "orange_banner"
        // 18311: new             Lnet/minecraft/world/item/BannerItem;
        // 18314: dup            
        // 18315: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18318: getstatic       net/minecraft/world/level/block/Blocks.ORANGE_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18321: new             Lnet/minecraft/world/item/Item$Properties;
        // 18324: dup            
        // 18325: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18328: bipush          16
        // 18330: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18333: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18336: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18339: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18342: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18345: putstatic       net/minecraft/world/item/Items.ORANGE_BANNER:Lnet/minecraft/world/item/Item;
        // 18348: ldc_w           "magenta_banner"
        // 18351: new             Lnet/minecraft/world/item/BannerItem;
        // 18354: dup            
        // 18355: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18358: getstatic       net/minecraft/world/level/block/Blocks.MAGENTA_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18361: new             Lnet/minecraft/world/item/Item$Properties;
        // 18364: dup            
        // 18365: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18368: bipush          16
        // 18370: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18373: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18376: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18379: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18382: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18385: putstatic       net/minecraft/world/item/Items.MAGENTA_BANNER:Lnet/minecraft/world/item/Item;
        // 18388: ldc_w           "light_blue_banner"
        // 18391: new             Lnet/minecraft/world/item/BannerItem;
        // 18394: dup            
        // 18395: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18398: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_BLUE_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18401: new             Lnet/minecraft/world/item/Item$Properties;
        // 18404: dup            
        // 18405: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18408: bipush          16
        // 18410: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18413: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18416: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18419: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18422: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18425: putstatic       net/minecraft/world/item/Items.LIGHT_BLUE_BANNER:Lnet/minecraft/world/item/Item;
        // 18428: ldc_w           "yellow_banner"
        // 18431: new             Lnet/minecraft/world/item/BannerItem;
        // 18434: dup            
        // 18435: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18438: getstatic       net/minecraft/world/level/block/Blocks.YELLOW_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18441: new             Lnet/minecraft/world/item/Item$Properties;
        // 18444: dup            
        // 18445: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18448: bipush          16
        // 18450: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18453: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18456: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18459: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18462: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18465: putstatic       net/minecraft/world/item/Items.YELLOW_BANNER:Lnet/minecraft/world/item/Item;
        // 18468: ldc_w           "lime_banner"
        // 18471: new             Lnet/minecraft/world/item/BannerItem;
        // 18474: dup            
        // 18475: getstatic       net/minecraft/world/level/block/Blocks.LIME_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18478: getstatic       net/minecraft/world/level/block/Blocks.LIME_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18481: new             Lnet/minecraft/world/item/Item$Properties;
        // 18484: dup            
        // 18485: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18488: bipush          16
        // 18490: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18493: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18496: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18499: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18502: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18505: putstatic       net/minecraft/world/item/Items.LIME_BANNER:Lnet/minecraft/world/item/Item;
        // 18508: ldc_w           "pink_banner"
        // 18511: new             Lnet/minecraft/world/item/BannerItem;
        // 18514: dup            
        // 18515: getstatic       net/minecraft/world/level/block/Blocks.PINK_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18518: getstatic       net/minecraft/world/level/block/Blocks.PINK_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18521: new             Lnet/minecraft/world/item/Item$Properties;
        // 18524: dup            
        // 18525: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18528: bipush          16
        // 18530: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18533: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18536: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18539: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18542: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18545: putstatic       net/minecraft/world/item/Items.PINK_BANNER:Lnet/minecraft/world/item/Item;
        // 18548: ldc_w           "gray_banner"
        // 18551: new             Lnet/minecraft/world/item/BannerItem;
        // 18554: dup            
        // 18555: getstatic       net/minecraft/world/level/block/Blocks.GRAY_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18558: getstatic       net/minecraft/world/level/block/Blocks.GRAY_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18561: new             Lnet/minecraft/world/item/Item$Properties;
        // 18564: dup            
        // 18565: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18568: bipush          16
        // 18570: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18573: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18576: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18579: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18582: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18585: putstatic       net/minecraft/world/item/Items.GRAY_BANNER:Lnet/minecraft/world/item/Item;
        // 18588: ldc_w           "light_gray_banner"
        // 18591: new             Lnet/minecraft/world/item/BannerItem;
        // 18594: dup            
        // 18595: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18598: getstatic       net/minecraft/world/level/block/Blocks.LIGHT_GRAY_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18601: new             Lnet/minecraft/world/item/Item$Properties;
        // 18604: dup            
        // 18605: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18608: bipush          16
        // 18610: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18613: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18616: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18619: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18622: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18625: putstatic       net/minecraft/world/item/Items.LIGHT_GRAY_BANNER:Lnet/minecraft/world/item/Item;
        // 18628: ldc_w           "cyan_banner"
        // 18631: new             Lnet/minecraft/world/item/BannerItem;
        // 18634: dup            
        // 18635: getstatic       net/minecraft/world/level/block/Blocks.CYAN_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18638: getstatic       net/minecraft/world/level/block/Blocks.CYAN_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18641: new             Lnet/minecraft/world/item/Item$Properties;
        // 18644: dup            
        // 18645: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18648: bipush          16
        // 18650: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18653: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18656: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18659: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18662: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18665: putstatic       net/minecraft/world/item/Items.CYAN_BANNER:Lnet/minecraft/world/item/Item;
        // 18668: ldc_w           "purple_banner"
        // 18671: new             Lnet/minecraft/world/item/BannerItem;
        // 18674: dup            
        // 18675: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18678: getstatic       net/minecraft/world/level/block/Blocks.PURPLE_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18681: new             Lnet/minecraft/world/item/Item$Properties;
        // 18684: dup            
        // 18685: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18688: bipush          16
        // 18690: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18693: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18696: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18699: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18702: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18705: putstatic       net/minecraft/world/item/Items.PURPLE_BANNER:Lnet/minecraft/world/item/Item;
        // 18708: ldc_w           "blue_banner"
        // 18711: new             Lnet/minecraft/world/item/BannerItem;
        // 18714: dup            
        // 18715: getstatic       net/minecraft/world/level/block/Blocks.BLUE_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18718: getstatic       net/minecraft/world/level/block/Blocks.BLUE_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18721: new             Lnet/minecraft/world/item/Item$Properties;
        // 18724: dup            
        // 18725: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18728: bipush          16
        // 18730: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18733: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18736: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18739: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18742: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18745: putstatic       net/minecraft/world/item/Items.BLUE_BANNER:Lnet/minecraft/world/item/Item;
        // 18748: ldc_w           "brown_banner"
        // 18751: new             Lnet/minecraft/world/item/BannerItem;
        // 18754: dup            
        // 18755: getstatic       net/minecraft/world/level/block/Blocks.BROWN_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18758: getstatic       net/minecraft/world/level/block/Blocks.BROWN_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18761: new             Lnet/minecraft/world/item/Item$Properties;
        // 18764: dup            
        // 18765: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18768: bipush          16
        // 18770: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18773: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18776: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18779: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18782: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18785: putstatic       net/minecraft/world/item/Items.BROWN_BANNER:Lnet/minecraft/world/item/Item;
        // 18788: ldc_w           "green_banner"
        // 18791: new             Lnet/minecraft/world/item/BannerItem;
        // 18794: dup            
        // 18795: getstatic       net/minecraft/world/level/block/Blocks.GREEN_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18798: getstatic       net/minecraft/world/level/block/Blocks.GREEN_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18801: new             Lnet/minecraft/world/item/Item$Properties;
        // 18804: dup            
        // 18805: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18808: bipush          16
        // 18810: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18813: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18816: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18819: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18822: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18825: putstatic       net/minecraft/world/item/Items.GREEN_BANNER:Lnet/minecraft/world/item/Item;
        // 18828: ldc_w           "red_banner"
        // 18831: new             Lnet/minecraft/world/item/BannerItem;
        // 18834: dup            
        // 18835: getstatic       net/minecraft/world/level/block/Blocks.RED_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18838: getstatic       net/minecraft/world/level/block/Blocks.RED_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18841: new             Lnet/minecraft/world/item/Item$Properties;
        // 18844: dup            
        // 18845: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18848: bipush          16
        // 18850: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18853: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18856: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18859: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18862: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18865: putstatic       net/minecraft/world/item/Items.RED_BANNER:Lnet/minecraft/world/item/Item;
        // 18868: ldc_w           "black_banner"
        // 18871: new             Lnet/minecraft/world/item/BannerItem;
        // 18874: dup            
        // 18875: getstatic       net/minecraft/world/level/block/Blocks.BLACK_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18878: getstatic       net/minecraft/world/level/block/Blocks.BLACK_WALL_BANNER:Lnet/minecraft/world/level/block/Block;
        // 18881: new             Lnet/minecraft/world/item/Item$Properties;
        // 18884: dup            
        // 18885: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18888: bipush          16
        // 18890: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 18893: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18896: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18899: invokespecial   net/minecraft/world/item/BannerItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 18902: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18905: putstatic       net/minecraft/world/item/Items.BLACK_BANNER:Lnet/minecraft/world/item/Item;
        // 18908: ldc_w           "end_crystal"
        // 18911: new             Lnet/minecraft/world/item/EndCrystalItem;
        // 18914: dup            
        // 18915: new             Lnet/minecraft/world/item/Item$Properties;
        // 18918: dup            
        // 18919: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18922: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18925: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18928: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 18931: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 18934: invokespecial   net/minecraft/world/item/EndCrystalItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 18937: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18940: putstatic       net/minecraft/world/item/Items.END_CRYSTAL:Lnet/minecraft/world/item/Item;
        // 18943: ldc_w           "chorus_fruit"
        // 18946: new             Lnet/minecraft/world/item/ChorusFruitItem;
        // 18949: dup            
        // 18950: new             Lnet/minecraft/world/item/Item$Properties;
        // 18953: dup            
        // 18954: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18957: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18960: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18963: getstatic       net/minecraft/world/food/Foods.CHORUS_FRUIT:Lnet/minecraft/world/food/FoodProperties;
        // 18966: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 18969: invokespecial   net/minecraft/world/item/ChorusFruitItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 18972: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 18975: putstatic       net/minecraft/world/item/Items.CHORUS_FRUIT:Lnet/minecraft/world/item/Item;
        // 18978: ldc_w           "popped_chorus_fruit"
        // 18981: new             Lnet/minecraft/world/item/Item;
        // 18984: dup            
        // 18985: new             Lnet/minecraft/world/item/Item$Properties;
        // 18988: dup            
        // 18989: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 18992: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 18995: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 18998: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19001: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19004: putstatic       net/minecraft/world/item/Items.POPPED_CHORUS_FRUIT:Lnet/minecraft/world/item/Item;
        // 19007: ldc_w           "beetroot"
        // 19010: new             Lnet/minecraft/world/item/Item;
        // 19013: dup            
        // 19014: new             Lnet/minecraft/world/item/Item$Properties;
        // 19017: dup            
        // 19018: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19021: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 19024: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19027: getstatic       net/minecraft/world/food/Foods.BEETROOT:Lnet/minecraft/world/food/FoodProperties;
        // 19030: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 19033: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19036: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19039: putstatic       net/minecraft/world/item/Items.BEETROOT:Lnet/minecraft/world/item/Item;
        // 19042: ldc_w           "beetroot_seeds"
        // 19045: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 19048: dup            
        // 19049: getstatic       net/minecraft/world/level/block/Blocks.BEETROOTS:Lnet/minecraft/world/level/block/Block;
        // 19052: new             Lnet/minecraft/world/item/Item$Properties;
        // 19055: dup            
        // 19056: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19059: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 19062: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19065: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 19068: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19071: putstatic       net/minecraft/world/item/Items.BEETROOT_SEEDS:Lnet/minecraft/world/item/Item;
        // 19074: ldc_w           "beetroot_soup"
        // 19077: new             Lnet/minecraft/world/item/BowlFoodItem;
        // 19080: dup            
        // 19081: new             Lnet/minecraft/world/item/Item$Properties;
        // 19084: dup            
        // 19085: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19088: iconst_1       
        // 19089: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19092: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 19095: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19098: getstatic       net/minecraft/world/food/Foods.BEETROOT_SOUP:Lnet/minecraft/world/food/FoodProperties;
        // 19101: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 19104: invokespecial   net/minecraft/world/item/BowlFoodItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19107: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19110: putstatic       net/minecraft/world/item/Items.BEETROOT_SOUP:Lnet/minecraft/world/item/Item;
        // 19113: ldc_w           "dragon_breath"
        // 19116: new             Lnet/minecraft/world/item/Item;
        // 19119: dup            
        // 19120: new             Lnet/minecraft/world/item/Item$Properties;
        // 19123: dup            
        // 19124: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19127: getstatic       net/minecraft/world/item/Items.GLASS_BOTTLE:Lnet/minecraft/world/item/Item;
        // 19130: invokevirtual   net/minecraft/world/item/Item$Properties.craftRemainder:(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item$Properties;
        // 19133: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 19136: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19139: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 19142: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19145: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19148: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19151: putstatic       net/minecraft/world/item/Items.DRAGON_BREATH:Lnet/minecraft/world/item/Item;
        // 19154: ldc_w           "splash_potion"
        // 19157: new             Lnet/minecraft/world/item/SplashPotionItem;
        // 19160: dup            
        // 19161: new             Lnet/minecraft/world/item/Item$Properties;
        // 19164: dup            
        // 19165: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19168: iconst_1       
        // 19169: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19172: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 19175: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19178: invokespecial   net/minecraft/world/item/SplashPotionItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19181: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19184: putstatic       net/minecraft/world/item/Items.SPLASH_POTION:Lnet/minecraft/world/item/Item;
        // 19187: ldc_w           "spectral_arrow"
        // 19190: new             Lnet/minecraft/world/item/SpectralArrowItem;
        // 19193: dup            
        // 19194: new             Lnet/minecraft/world/item/Item$Properties;
        // 19197: dup            
        // 19198: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19201: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 19204: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19207: invokespecial   net/minecraft/world/item/SpectralArrowItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19210: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19213: putstatic       net/minecraft/world/item/Items.SPECTRAL_ARROW:Lnet/minecraft/world/item/Item;
        // 19216: ldc_w           "tipped_arrow"
        // 19219: new             Lnet/minecraft/world/item/TippedArrowItem;
        // 19222: dup            
        // 19223: new             Lnet/minecraft/world/item/Item$Properties;
        // 19226: dup            
        // 19227: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19230: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 19233: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19236: invokespecial   net/minecraft/world/item/TippedArrowItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19239: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19242: putstatic       net/minecraft/world/item/Items.TIPPED_ARROW:Lnet/minecraft/world/item/Item;
        // 19245: ldc_w           "lingering_potion"
        // 19248: new             Lnet/minecraft/world/item/LingeringPotionItem;
        // 19251: dup            
        // 19252: new             Lnet/minecraft/world/item/Item$Properties;
        // 19255: dup            
        // 19256: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19259: iconst_1       
        // 19260: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19263: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 19266: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19269: invokespecial   net/minecraft/world/item/LingeringPotionItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19272: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19275: putstatic       net/minecraft/world/item/Items.LINGERING_POTION:Lnet/minecraft/world/item/Item;
        // 19278: ldc_w           "shield"
        // 19281: new             Lnet/minecraft/world/item/ShieldItem;
        // 19284: dup            
        // 19285: new             Lnet/minecraft/world/item/Item$Properties;
        // 19288: dup            
        // 19289: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19292: sipush          336
        // 19295: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19298: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 19301: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19304: invokespecial   net/minecraft/world/item/ShieldItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19307: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19310: putstatic       net/minecraft/world/item/Items.SHIELD:Lnet/minecraft/world/item/Item;
        // 19313: ldc_w           "elytra"
        // 19316: new             Lnet/minecraft/world/item/ElytraItem;
        // 19319: dup            
        // 19320: new             Lnet/minecraft/world/item/Item$Properties;
        // 19323: dup            
        // 19324: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19327: sipush          432
        // 19330: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19333: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 19336: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19339: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 19342: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19345: invokespecial   net/minecraft/world/item/ElytraItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19348: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19351: putstatic       net/minecraft/world/item/Items.ELYTRA:Lnet/minecraft/world/item/Item;
        // 19354: ldc_w           "spruce_boat"
        // 19357: new             Lnet/minecraft/world/item/BoatItem;
        // 19360: dup            
        // 19361: getstatic       net/minecraft/world/entity/vehicle/Boat$Type.SPRUCE:Lnet/minecraft/world/entity/vehicle/Boat$Type;
        // 19364: new             Lnet/minecraft/world/item/Item$Properties;
        // 19367: dup            
        // 19368: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19371: iconst_1       
        // 19372: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19375: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 19378: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19381: invokespecial   net/minecraft/world/item/BoatItem.<init>:(Lnet/minecraft/world/entity/vehicle/Boat$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 19384: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19387: putstatic       net/minecraft/world/item/Items.SPRUCE_BOAT:Lnet/minecraft/world/item/Item;
        // 19390: ldc_w           "birch_boat"
        // 19393: new             Lnet/minecraft/world/item/BoatItem;
        // 19396: dup            
        // 19397: getstatic       net/minecraft/world/entity/vehicle/Boat$Type.BIRCH:Lnet/minecraft/world/entity/vehicle/Boat$Type;
        // 19400: new             Lnet/minecraft/world/item/Item$Properties;
        // 19403: dup            
        // 19404: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19407: iconst_1       
        // 19408: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19411: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 19414: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19417: invokespecial   net/minecraft/world/item/BoatItem.<init>:(Lnet/minecraft/world/entity/vehicle/Boat$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 19420: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19423: putstatic       net/minecraft/world/item/Items.BIRCH_BOAT:Lnet/minecraft/world/item/Item;
        // 19426: ldc_w           "jungle_boat"
        // 19429: new             Lnet/minecraft/world/item/BoatItem;
        // 19432: dup            
        // 19433: getstatic       net/minecraft/world/entity/vehicle/Boat$Type.JUNGLE:Lnet/minecraft/world/entity/vehicle/Boat$Type;
        // 19436: new             Lnet/minecraft/world/item/Item$Properties;
        // 19439: dup            
        // 19440: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19443: iconst_1       
        // 19444: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19447: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 19450: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19453: invokespecial   net/minecraft/world/item/BoatItem.<init>:(Lnet/minecraft/world/entity/vehicle/Boat$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 19456: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19459: putstatic       net/minecraft/world/item/Items.JUNGLE_BOAT:Lnet/minecraft/world/item/Item;
        // 19462: ldc_w           "acacia_boat"
        // 19465: new             Lnet/minecraft/world/item/BoatItem;
        // 19468: dup            
        // 19469: getstatic       net/minecraft/world/entity/vehicle/Boat$Type.ACACIA:Lnet/minecraft/world/entity/vehicle/Boat$Type;
        // 19472: new             Lnet/minecraft/world/item/Item$Properties;
        // 19475: dup            
        // 19476: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19479: iconst_1       
        // 19480: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19483: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 19486: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19489: invokespecial   net/minecraft/world/item/BoatItem.<init>:(Lnet/minecraft/world/entity/vehicle/Boat$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 19492: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19495: putstatic       net/minecraft/world/item/Items.ACACIA_BOAT:Lnet/minecraft/world/item/Item;
        // 19498: ldc_w           "dark_oak_boat"
        // 19501: new             Lnet/minecraft/world/item/BoatItem;
        // 19504: dup            
        // 19505: getstatic       net/minecraft/world/entity/vehicle/Boat$Type.DARK_OAK:Lnet/minecraft/world/entity/vehicle/Boat$Type;
        // 19508: new             Lnet/minecraft/world/item/Item$Properties;
        // 19511: dup            
        // 19512: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19515: iconst_1       
        // 19516: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19519: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_TRANSPORTATION:Lnet/minecraft/world/item/CreativeModeTab;
        // 19522: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19525: invokespecial   net/minecraft/world/item/BoatItem.<init>:(Lnet/minecraft/world/entity/vehicle/Boat$Type;Lnet/minecraft/world/item/Item$Properties;)V
        // 19528: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19531: putstatic       net/minecraft/world/item/Items.DARK_OAK_BOAT:Lnet/minecraft/world/item/Item;
        // 19534: ldc_w           "totem_of_undying"
        // 19537: new             Lnet/minecraft/world/item/Item;
        // 19540: dup            
        // 19541: new             Lnet/minecraft/world/item/Item$Properties;
        // 19544: dup            
        // 19545: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19548: iconst_1       
        // 19549: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19552: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 19555: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19558: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 19561: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19564: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19567: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19570: putstatic       net/minecraft/world/item/Items.TOTEM_OF_UNDYING:Lnet/minecraft/world/item/Item;
        // 19573: ldc_w           "shulker_shell"
        // 19576: new             Lnet/minecraft/world/item/Item;
        // 19579: dup            
        // 19580: new             Lnet/minecraft/world/item/Item$Properties;
        // 19583: dup            
        // 19584: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19587: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 19590: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19593: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19596: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19599: putstatic       net/minecraft/world/item/Items.SHULKER_SHELL:Lnet/minecraft/world/item/Item;
        // 19602: ldc_w           "iron_nugget"
        // 19605: new             Lnet/minecraft/world/item/Item;
        // 19608: dup            
        // 19609: new             Lnet/minecraft/world/item/Item$Properties;
        // 19612: dup            
        // 19613: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19616: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 19619: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19622: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19625: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19628: putstatic       net/minecraft/world/item/Items.IRON_NUGGET:Lnet/minecraft/world/item/Item;
        // 19631: ldc_w           "knowledge_book"
        // 19634: new             Lnet/minecraft/world/item/KnowledgeBookItem;
        // 19637: dup            
        // 19638: new             Lnet/minecraft/world/item/Item$Properties;
        // 19641: dup            
        // 19642: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19645: iconst_1       
        // 19646: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19649: invokespecial   net/minecraft/world/item/KnowledgeBookItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19652: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19655: putstatic       net/minecraft/world/item/Items.KNOWLEDGE_BOOK:Lnet/minecraft/world/item/Item;
        // 19658: ldc_w           "debug_stick"
        // 19661: new             Lnet/minecraft/world/item/DebugStickItem;
        // 19664: dup            
        // 19665: new             Lnet/minecraft/world/item/Item$Properties;
        // 19668: dup            
        // 19669: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19672: iconst_1       
        // 19673: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19676: invokespecial   net/minecraft/world/item/DebugStickItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 19679: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19682: putstatic       net/minecraft/world/item/Items.DEBUG_STICK:Lnet/minecraft/world/item/Item;
        // 19685: ldc_w           "music_disc_13"
        // 19688: new             Lnet/minecraft/world/item/RecordItem;
        // 19691: dup            
        // 19692: iconst_1       
        // 19693: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_13:Lnet/minecraft/sounds/SoundEvent;
        // 19696: new             Lnet/minecraft/world/item/Item$Properties;
        // 19699: dup            
        // 19700: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19703: iconst_1       
        // 19704: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19707: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19710: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19713: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19716: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19719: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19722: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19725: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_13:Lnet/minecraft/world/item/Item;
        // 19728: ldc_w           "music_disc_cat"
        // 19731: new             Lnet/minecraft/world/item/RecordItem;
        // 19734: dup            
        // 19735: iconst_2       
        // 19736: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_CAT:Lnet/minecraft/sounds/SoundEvent;
        // 19739: new             Lnet/minecraft/world/item/Item$Properties;
        // 19742: dup            
        // 19743: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19746: iconst_1       
        // 19747: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19750: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19753: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19756: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19759: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19762: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19765: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19768: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_CAT:Lnet/minecraft/world/item/Item;
        // 19771: ldc_w           "music_disc_blocks"
        // 19774: new             Lnet/minecraft/world/item/RecordItem;
        // 19777: dup            
        // 19778: iconst_3       
        // 19779: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_BLOCKS:Lnet/minecraft/sounds/SoundEvent;
        // 19782: new             Lnet/minecraft/world/item/Item$Properties;
        // 19785: dup            
        // 19786: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19789: iconst_1       
        // 19790: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19793: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19796: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19799: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19802: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19805: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19808: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19811: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_BLOCKS:Lnet/minecraft/world/item/Item;
        // 19814: ldc_w           "music_disc_chirp"
        // 19817: new             Lnet/minecraft/world/item/RecordItem;
        // 19820: dup            
        // 19821: iconst_4       
        // 19822: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_CHIRP:Lnet/minecraft/sounds/SoundEvent;
        // 19825: new             Lnet/minecraft/world/item/Item$Properties;
        // 19828: dup            
        // 19829: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19832: iconst_1       
        // 19833: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19836: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19839: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19842: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19845: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19848: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19851: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19854: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_CHIRP:Lnet/minecraft/world/item/Item;
        // 19857: ldc_w           "music_disc_far"
        // 19860: new             Lnet/minecraft/world/item/RecordItem;
        // 19863: dup            
        // 19864: iconst_5       
        // 19865: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_FAR:Lnet/minecraft/sounds/SoundEvent;
        // 19868: new             Lnet/minecraft/world/item/Item$Properties;
        // 19871: dup            
        // 19872: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19875: iconst_1       
        // 19876: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19879: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19882: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19885: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19888: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19891: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19894: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19897: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_FAR:Lnet/minecraft/world/item/Item;
        // 19900: ldc_w           "music_disc_mall"
        // 19903: new             Lnet/minecraft/world/item/RecordItem;
        // 19906: dup            
        // 19907: bipush          6
        // 19909: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_MALL:Lnet/minecraft/sounds/SoundEvent;
        // 19912: new             Lnet/minecraft/world/item/Item$Properties;
        // 19915: dup            
        // 19916: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19919: iconst_1       
        // 19920: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19923: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19926: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19929: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19932: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19935: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19938: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19941: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_MALL:Lnet/minecraft/world/item/Item;
        // 19944: ldc_w           "music_disc_mellohi"
        // 19947: new             Lnet/minecraft/world/item/RecordItem;
        // 19950: dup            
        // 19951: bipush          7
        // 19953: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_MELLOHI:Lnet/minecraft/sounds/SoundEvent;
        // 19956: new             Lnet/minecraft/world/item/Item$Properties;
        // 19959: dup            
        // 19960: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 19963: iconst_1       
        // 19964: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 19967: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 19970: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 19973: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 19976: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 19979: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 19982: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 19985: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_MELLOHI:Lnet/minecraft/world/item/Item;
        // 19988: ldc_w           "music_disc_stal"
        // 19991: new             Lnet/minecraft/world/item/RecordItem;
        // 19994: dup            
        // 19995: bipush          8
        // 19997: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_STAL:Lnet/minecraft/sounds/SoundEvent;
        // 20000: new             Lnet/minecraft/world/item/Item$Properties;
        // 20003: dup            
        // 20004: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20007: iconst_1       
        // 20008: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20011: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20014: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20017: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 20020: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20023: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 20026: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20029: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_STAL:Lnet/minecraft/world/item/Item;
        // 20032: ldc_w           "music_disc_strad"
        // 20035: new             Lnet/minecraft/world/item/RecordItem;
        // 20038: dup            
        // 20039: bipush          9
        // 20041: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_STRAD:Lnet/minecraft/sounds/SoundEvent;
        // 20044: new             Lnet/minecraft/world/item/Item$Properties;
        // 20047: dup            
        // 20048: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20051: iconst_1       
        // 20052: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20055: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20058: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20061: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 20064: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20067: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 20070: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20073: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_STRAD:Lnet/minecraft/world/item/Item;
        // 20076: ldc_w           "music_disc_ward"
        // 20079: new             Lnet/minecraft/world/item/RecordItem;
        // 20082: dup            
        // 20083: bipush          10
        // 20085: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_WARD:Lnet/minecraft/sounds/SoundEvent;
        // 20088: new             Lnet/minecraft/world/item/Item$Properties;
        // 20091: dup            
        // 20092: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20095: iconst_1       
        // 20096: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20099: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20102: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20105: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 20108: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20111: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 20114: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20117: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_WARD:Lnet/minecraft/world/item/Item;
        // 20120: ldc_w           "music_disc_11"
        // 20123: new             Lnet/minecraft/world/item/RecordItem;
        // 20126: dup            
        // 20127: bipush          11
        // 20129: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_11:Lnet/minecraft/sounds/SoundEvent;
        // 20132: new             Lnet/minecraft/world/item/Item$Properties;
        // 20135: dup            
        // 20136: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20139: iconst_1       
        // 20140: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20143: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20146: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20149: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 20152: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20155: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 20158: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20161: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_11:Lnet/minecraft/world/item/Item;
        // 20164: ldc_w           "music_disc_wait"
        // 20167: new             Lnet/minecraft/world/item/RecordItem;
        // 20170: dup            
        // 20171: bipush          12
        // 20173: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_WAIT:Lnet/minecraft/sounds/SoundEvent;
        // 20176: new             Lnet/minecraft/world/item/Item$Properties;
        // 20179: dup            
        // 20180: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20183: iconst_1       
        // 20184: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20187: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20190: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20193: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 20196: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20199: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 20202: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20205: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_WAIT:Lnet/minecraft/world/item/Item;
        // 20208: ldc_w           "music_disc_pigstep"
        // 20211: new             Lnet/minecraft/world/item/RecordItem;
        // 20214: dup            
        // 20215: bipush          13
        // 20217: getstatic       net/minecraft/sounds/SoundEvents.MUSIC_DISC_PIGSTEP:Lnet/minecraft/sounds/SoundEvent;
        // 20220: new             Lnet/minecraft/world/item/Item$Properties;
        // 20223: dup            
        // 20224: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20227: iconst_1       
        // 20228: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20231: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20234: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20237: getstatic       net/minecraft/world/item/Rarity.RARE:Lnet/minecraft/world/item/Rarity;
        // 20240: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20243: invokespecial   net/minecraft/world/item/RecordItem.<init>:(ILnet/minecraft/sounds/SoundEvent;Lnet/minecraft/world/item/Item$Properties;)V
        // 20246: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20249: putstatic       net/minecraft/world/item/Items.MUSIC_DISC_PIGSTEP:Lnet/minecraft/world/item/Item;
        // 20252: ldc_w           "trident"
        // 20255: new             Lnet/minecraft/world/item/TridentItem;
        // 20258: dup            
        // 20259: new             Lnet/minecraft/world/item/Item$Properties;
        // 20262: dup            
        // 20263: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20266: sipush          250
        // 20269: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20272: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 20275: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20278: invokespecial   net/minecraft/world/item/TridentItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20281: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20284: putstatic       net/minecraft/world/item/Items.TRIDENT:Lnet/minecraft/world/item/Item;
        // 20287: ldc_w           "phantom_membrane"
        // 20290: new             Lnet/minecraft/world/item/Item;
        // 20293: dup            
        // 20294: new             Lnet/minecraft/world/item/Item$Properties;
        // 20297: dup            
        // 20298: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20301: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BREWING:Lnet/minecraft/world/item/CreativeModeTab;
        // 20304: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20307: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20310: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20313: putstatic       net/minecraft/world/item/Items.PHANTOM_MEMBRANE:Lnet/minecraft/world/item/Item;
        // 20316: ldc_w           "nautilus_shell"
        // 20319: new             Lnet/minecraft/world/item/Item;
        // 20322: dup            
        // 20323: new             Lnet/minecraft/world/item/Item$Properties;
        // 20326: dup            
        // 20327: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20330: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20333: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20336: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20339: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20342: putstatic       net/minecraft/world/item/Items.NAUTILUS_SHELL:Lnet/minecraft/world/item/Item;
        // 20345: ldc_w           "heart_of_the_sea"
        // 20348: new             Lnet/minecraft/world/item/Item;
        // 20351: dup            
        // 20352: new             Lnet/minecraft/world/item/Item$Properties;
        // 20355: dup            
        // 20356: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20359: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20362: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20365: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 20368: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20371: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20374: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20377: putstatic       net/minecraft/world/item/Items.HEART_OF_THE_SEA:Lnet/minecraft/world/item/Item;
        // 20380: ldc_w           "crossbow"
        // 20383: new             Lnet/minecraft/world/item/CrossbowItem;
        // 20386: dup            
        // 20387: new             Lnet/minecraft/world/item/Item$Properties;
        // 20390: dup            
        // 20391: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20394: iconst_1       
        // 20395: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20398: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_COMBAT:Lnet/minecraft/world/item/CreativeModeTab;
        // 20401: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20404: sipush          326
        // 20407: invokevirtual   net/minecraft/world/item/Item$Properties.durability:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20410: invokespecial   net/minecraft/world/item/CrossbowItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20413: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20416: putstatic       net/minecraft/world/item/Items.CROSSBOW:Lnet/minecraft/world/item/Item;
        // 20419: ldc_w           "suspicious_stew"
        // 20422: new             Lnet/minecraft/world/item/SuspiciousStewItem;
        // 20425: dup            
        // 20426: new             Lnet/minecraft/world/item/Item$Properties;
        // 20429: dup            
        // 20430: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20433: iconst_1       
        // 20434: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20437: getstatic       net/minecraft/world/food/Foods.SUSPICIOUS_STEW:Lnet/minecraft/world/food/FoodProperties;
        // 20440: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 20443: invokespecial   net/minecraft/world/item/SuspiciousStewItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20446: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20449: putstatic       net/minecraft/world/item/Items.SUSPICIOUS_STEW:Lnet/minecraft/world/item/Item;
        // 20452: getstatic       net/minecraft/world/level/block/Blocks.LOOM:Lnet/minecraft/world/level/block/Block;
        // 20455: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20458: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20461: putstatic       net/minecraft/world/item/Items.LOOM:Lnet/minecraft/world/item/Item;
        // 20464: ldc_w           "flower_banner_pattern"
        // 20467: new             Lnet/minecraft/world/item/BannerPatternItem;
        // 20470: dup            
        // 20471: getstatic       net/minecraft/world/level/block/entity/BannerPattern.FLOWER:Lnet/minecraft/world/level/block/entity/BannerPattern;
        // 20474: new             Lnet/minecraft/world/item/Item$Properties;
        // 20477: dup            
        // 20478: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20481: iconst_1       
        // 20482: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20485: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20488: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20491: invokespecial   net/minecraft/world/item/BannerPatternItem.<init>:(Lnet/minecraft/world/level/block/entity/BannerPattern;Lnet/minecraft/world/item/Item$Properties;)V
        // 20494: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20497: putstatic       net/minecraft/world/item/Items.FLOWER_BANNER_PATTERN:Lnet/minecraft/world/item/Item;
        // 20500: ldc_w           "creeper_banner_pattern"
        // 20503: new             Lnet/minecraft/world/item/BannerPatternItem;
        // 20506: dup            
        // 20507: getstatic       net/minecraft/world/level/block/entity/BannerPattern.CREEPER:Lnet/minecraft/world/level/block/entity/BannerPattern;
        // 20510: new             Lnet/minecraft/world/item/Item$Properties;
        // 20513: dup            
        // 20514: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20517: iconst_1       
        // 20518: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20521: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20524: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20527: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 20530: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20533: invokespecial   net/minecraft/world/item/BannerPatternItem.<init>:(Lnet/minecraft/world/level/block/entity/BannerPattern;Lnet/minecraft/world/item/Item$Properties;)V
        // 20536: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20539: putstatic       net/minecraft/world/item/Items.CREEPER_BANNER_PATTERN:Lnet/minecraft/world/item/Item;
        // 20542: ldc_w           "skull_banner_pattern"
        // 20545: new             Lnet/minecraft/world/item/BannerPatternItem;
        // 20548: dup            
        // 20549: getstatic       net/minecraft/world/level/block/entity/BannerPattern.SKULL:Lnet/minecraft/world/level/block/entity/BannerPattern;
        // 20552: new             Lnet/minecraft/world/item/Item$Properties;
        // 20555: dup            
        // 20556: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20559: iconst_1       
        // 20560: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20563: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20566: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20569: getstatic       net/minecraft/world/item/Rarity.UNCOMMON:Lnet/minecraft/world/item/Rarity;
        // 20572: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20575: invokespecial   net/minecraft/world/item/BannerPatternItem.<init>:(Lnet/minecraft/world/level/block/entity/BannerPattern;Lnet/minecraft/world/item/Item$Properties;)V
        // 20578: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20581: putstatic       net/minecraft/world/item/Items.SKULL_BANNER_PATTERN:Lnet/minecraft/world/item/Item;
        // 20584: ldc_w           "mojang_banner_pattern"
        // 20587: new             Lnet/minecraft/world/item/BannerPatternItem;
        // 20590: dup            
        // 20591: getstatic       net/minecraft/world/level/block/entity/BannerPattern.MOJANG:Lnet/minecraft/world/level/block/entity/BannerPattern;
        // 20594: new             Lnet/minecraft/world/item/Item$Properties;
        // 20597: dup            
        // 20598: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20601: iconst_1       
        // 20602: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20605: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20608: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20611: getstatic       net/minecraft/world/item/Rarity.EPIC:Lnet/minecraft/world/item/Rarity;
        // 20614: invokevirtual   net/minecraft/world/item/Item$Properties.rarity:(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;
        // 20617: invokespecial   net/minecraft/world/item/BannerPatternItem.<init>:(Lnet/minecraft/world/level/block/entity/BannerPattern;Lnet/minecraft/world/item/Item$Properties;)V
        // 20620: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20623: putstatic       net/minecraft/world/item/Items.MOJANG_BANNER_PATTERN:Lnet/minecraft/world/item/Item;
        // 20626: ldc_w           "globe_banner_pattern"
        // 20629: new             Lnet/minecraft/world/item/BannerPatternItem;
        // 20632: dup            
        // 20633: getstatic       net/minecraft/world/level/block/entity/BannerPattern.GLOBE:Lnet/minecraft/world/level/block/entity/BannerPattern;
        // 20636: new             Lnet/minecraft/world/item/Item$Properties;
        // 20639: dup            
        // 20640: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20643: iconst_1       
        // 20644: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20647: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20650: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20653: invokespecial   net/minecraft/world/item/BannerPatternItem.<init>:(Lnet/minecraft/world/level/block/entity/BannerPattern;Lnet/minecraft/world/item/Item$Properties;)V
        // 20656: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20659: putstatic       net/minecraft/world/item/Items.GLOBE_BANNER_PATTER:Lnet/minecraft/world/item/Item;
        // 20662: ldc_w           "piglin_banner_pattern"
        // 20665: new             Lnet/minecraft/world/item/BannerPatternItem;
        // 20668: dup            
        // 20669: getstatic       net/minecraft/world/level/block/entity/BannerPattern.PIGLIN:Lnet/minecraft/world/level/block/entity/BannerPattern;
        // 20672: new             Lnet/minecraft/world/item/Item$Properties;
        // 20675: dup            
        // 20676: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20679: iconst_1       
        // 20680: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 20683: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MISC:Lnet/minecraft/world/item/CreativeModeTab;
        // 20686: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20689: invokespecial   net/minecraft/world/item/BannerPatternItem.<init>:(Lnet/minecraft/world/level/block/entity/BannerPattern;Lnet/minecraft/world/item/Item$Properties;)V
        // 20692: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20695: putstatic       net/minecraft/world/item/Items.PIGLIN_BANNER_PATTERN:Lnet/minecraft/world/item/Item;
        // 20698: getstatic       net/minecraft/world/level/block/Blocks.COMPOSTER:Lnet/minecraft/world/level/block/Block;
        // 20701: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20704: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20707: putstatic       net/minecraft/world/item/Items.COMPOSTER:Lnet/minecraft/world/item/Item;
        // 20710: getstatic       net/minecraft/world/level/block/Blocks.BARREL:Lnet/minecraft/world/level/block/Block;
        // 20713: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20716: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20719: putstatic       net/minecraft/world/item/Items.BARREL:Lnet/minecraft/world/item/Item;
        // 20722: getstatic       net/minecraft/world/level/block/Blocks.SMOKER:Lnet/minecraft/world/level/block/Block;
        // 20725: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20728: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20731: putstatic       net/minecraft/world/item/Items.SMOKER:Lnet/minecraft/world/item/Item;
        // 20734: getstatic       net/minecraft/world/level/block/Blocks.BLAST_FURNACE:Lnet/minecraft/world/level/block/Block;
        // 20737: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20740: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20743: putstatic       net/minecraft/world/item/Items.BLAST_FURNACE:Lnet/minecraft/world/item/Item;
        // 20746: getstatic       net/minecraft/world/level/block/Blocks.CARTOGRAPHY_TABLE:Lnet/minecraft/world/level/block/Block;
        // 20749: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20752: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20755: putstatic       net/minecraft/world/item/Items.CARTOGRAPHY_TABLE:Lnet/minecraft/world/item/Item;
        // 20758: getstatic       net/minecraft/world/level/block/Blocks.FLETCHING_TABLE:Lnet/minecraft/world/level/block/Block;
        // 20761: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20764: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20767: putstatic       net/minecraft/world/item/Items.FLETCHING_TABLE:Lnet/minecraft/world/item/Item;
        // 20770: getstatic       net/minecraft/world/level/block/Blocks.GRINDSTONE:Lnet/minecraft/world/level/block/Block;
        // 20773: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20776: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20779: putstatic       net/minecraft/world/item/Items.GRINDSTONE:Lnet/minecraft/world/item/Item;
        // 20782: getstatic       net/minecraft/world/level/block/Blocks.LECTERN:Lnet/minecraft/world/level/block/Block;
        // 20785: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        // 20788: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20791: putstatic       net/minecraft/world/item/Items.LECTERN:Lnet/minecraft/world/item/Item;
        // 20794: getstatic       net/minecraft/world/level/block/Blocks.SMITHING_TABLE:Lnet/minecraft/world/level/block/Block;
        // 20797: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20800: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20803: putstatic       net/minecraft/world/item/Items.SMITHING_TABLE:Lnet/minecraft/world/item/Item;
        // 20806: getstatic       net/minecraft/world/level/block/Blocks.STONECUTTER:Lnet/minecraft/world/level/block/Block;
        // 20809: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20812: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20815: putstatic       net/minecraft/world/item/Items.STONECUTTER:Lnet/minecraft/world/item/Item;
        // 20818: getstatic       net/minecraft/world/level/block/Blocks.BELL:Lnet/minecraft/world/level/block/Block;
        // 20821: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20824: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20827: putstatic       net/minecraft/world/item/Items.BELL:Lnet/minecraft/world/item/Item;
        // 20830: getstatic       net/minecraft/world/level/block/Blocks.LANTERN:Lnet/minecraft/world/level/block/Block;
        // 20833: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20836: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20839: putstatic       net/minecraft/world/item/Items.LANTERN:Lnet/minecraft/world/item/Item;
        // 20842: getstatic       net/minecraft/world/level/block/Blocks.SOUL_LANTERN:Lnet/minecraft/world/level/block/Block;
        // 20845: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20848: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20851: putstatic       net/minecraft/world/item/Items.SOUL_LANTERN:Lnet/minecraft/world/item/Item;
        // 20854: ldc_w           "sweet_berries"
        // 20857: new             Lnet/minecraft/world/item/ItemNameBlockItem;
        // 20860: dup            
        // 20861: getstatic       net/minecraft/world/level/block/Blocks.SWEET_BERRY_BUSH:Lnet/minecraft/world/level/block/Block;
        // 20864: new             Lnet/minecraft/world/item/Item$Properties;
        // 20867: dup            
        // 20868: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20871: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 20874: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20877: getstatic       net/minecraft/world/food/Foods.SWEET_BERRIES:Lnet/minecraft/world/food/FoodProperties;
        // 20880: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 20883: invokespecial   net/minecraft/world/item/ItemNameBlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 20886: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20889: putstatic       net/minecraft/world/item/Items.SWEET_BERRIES:Lnet/minecraft/world/item/Item;
        // 20892: getstatic       net/minecraft/world/level/block/Blocks.CAMPFIRE:Lnet/minecraft/world/level/block/Block;
        // 20895: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20898: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20901: putstatic       net/minecraft/world/item/Items.CAMPFIRE:Lnet/minecraft/world/item/Item;
        // 20904: getstatic       net/minecraft/world/level/block/Blocks.SOUL_CAMPFIRE:Lnet/minecraft/world/level/block/Block;
        // 20907: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20910: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20913: putstatic       net/minecraft/world/item/Items.SOUL_CAMPFIRE:Lnet/minecraft/world/item/Item;
        // 20916: getstatic       net/minecraft/world/level/block/Blocks.SHROOMLIGHT:Lnet/minecraft/world/level/block/Block;
        // 20919: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20922: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20925: putstatic       net/minecraft/world/item/Items.SHROOMLIGHT:Lnet/minecraft/world/item/Item;
        // 20928: ldc_w           "honeycomb"
        // 20931: new             Lnet/minecraft/world/item/Item;
        // 20934: dup            
        // 20935: new             Lnet/minecraft/world/item/Item$Properties;
        // 20938: dup            
        // 20939: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20942: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_MATERIALS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20945: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 20948: invokespecial   net/minecraft/world/item/Item.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 20951: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 20954: putstatic       net/minecraft/world/item/Items.HONEYCOMB:Lnet/minecraft/world/item/Item;
        // 20957: getstatic       net/minecraft/world/level/block/Blocks.BEE_NEST:Lnet/minecraft/world/level/block/Block;
        // 20960: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20963: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20966: putstatic       net/minecraft/world/item/Items.BEE_NEST:Lnet/minecraft/world/item/Item;
        // 20969: getstatic       net/minecraft/world/level/block/Blocks.BEEHIVE:Lnet/minecraft/world/level/block/Block;
        // 20972: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 20975: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 20978: putstatic       net/minecraft/world/item/Items.BEEHIVE:Lnet/minecraft/world/item/Item;
        // 20981: ldc_w           "honey_bottle"
        // 20984: new             Lnet/minecraft/world/item/HoneyBottleItem;
        // 20987: dup            
        // 20988: new             Lnet/minecraft/world/item/Item$Properties;
        // 20991: dup            
        // 20992: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 20995: getstatic       net/minecraft/world/item/Items.GLASS_BOTTLE:Lnet/minecraft/world/item/Item;
        // 20998: invokevirtual   net/minecraft/world/item/Item$Properties.craftRemainder:(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item$Properties;
        // 21001: getstatic       net/minecraft/world/food/Foods.HONEY_BOTTLE:Lnet/minecraft/world/food/FoodProperties;
        // 21004: invokevirtual   net/minecraft/world/item/Item$Properties.food:(Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/Item$Properties;
        // 21007: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_FOOD:Lnet/minecraft/world/item/CreativeModeTab;
        // 21010: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 21013: bipush          16
        // 21015: invokevirtual   net/minecraft/world/item/Item$Properties.stacksTo:(I)Lnet/minecraft/world/item/Item$Properties;
        // 21018: invokespecial   net/minecraft/world/item/HoneyBottleItem.<init>:(Lnet/minecraft/world/item/Item$Properties;)V
        // 21021: invokestatic    net/minecraft/world/item/Items.registerItem:(Ljava/lang/String;Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/item/Item;
        // 21024: putstatic       net/minecraft/world/item/Items.HONEY_BOTTLE:Lnet/minecraft/world/item/Item;
        // 21027: getstatic       net/minecraft/world/level/block/Blocks.HONEY_BLOCK:Lnet/minecraft/world/level/block/Block;
        // 21030: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21033: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21036: putstatic       net/minecraft/world/item/Items.HONEY_BLOCK:Lnet/minecraft/world/item/Item;
        // 21039: getstatic       net/minecraft/world/level/block/Blocks.HONEYCOMB_BLOCK:Lnet/minecraft/world/level/block/Block;
        // 21042: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21045: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21048: putstatic       net/minecraft/world/item/Items.HONEYCOMB_BLOCK:Lnet/minecraft/world/item/Item;
        // 21051: getstatic       net/minecraft/world/level/block/Blocks.LODESTONE:Lnet/minecraft/world/level/block/Block;
        // 21054: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21057: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21060: putstatic       net/minecraft/world/item/Items.LODESTONE:Lnet/minecraft/world/item/Item;
        // 21063: new             Lnet/minecraft/world/item/BlockItem;
        // 21066: dup            
        // 21067: getstatic       net/minecraft/world/level/block/Blocks.NETHERITE_BLOCK:Lnet/minecraft/world/level/block/Block;
        // 21070: new             Lnet/minecraft/world/item/Item$Properties;
        // 21073: dup            
        // 21074: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 21077: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21080: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 21083: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        // 21086: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 21089: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 21092: putstatic       net/minecraft/world/item/Items.NETHERITE_BLOCK:Lnet/minecraft/world/item/Item;
        // 21095: new             Lnet/minecraft/world/item/BlockItem;
        // 21098: dup            
        // 21099: getstatic       net/minecraft/world/level/block/Blocks.ANCIENT_DEBRIS:Lnet/minecraft/world/level/block/Block;
        // 21102: new             Lnet/minecraft/world/item/Item$Properties;
        // 21105: dup            
        // 21106: invokespecial   net/minecraft/world/item/Item$Properties.<init>:()V
        // 21109: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21112: invokevirtual   net/minecraft/world/item/Item$Properties.tab:(Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item$Properties;
        // 21115: invokevirtual   net/minecraft/world/item/Item$Properties.fireResistant:()Lnet/minecraft/world/item/Item$Properties;
        // 21118: invokespecial   net/minecraft/world/item/BlockItem.<init>:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;)V
        // 21121: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/item/BlockItem;)Lnet/minecraft/world/item/Item;
        // 21124: putstatic       net/minecraft/world/item/Items.ANCIENT_DEBRIS:Lnet/minecraft/world/item/Item;
        // 21127: getstatic       net/minecraft/world/level/block/Blocks.TARGET:Lnet/minecraft/world/level/block/Block;
        // 21130: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_REDSTONE:Lnet/minecraft/world/item/CreativeModeTab;
        // 21133: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21136: putstatic       net/minecraft/world/item/Items.TARGET:Lnet/minecraft/world/item/Item;
        // 21139: getstatic       net/minecraft/world/level/block/Blocks.CRYING_OBSIDIAN:Lnet/minecraft/world/level/block/Block;
        // 21142: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21145: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21148: putstatic       net/minecraft/world/item/Items.CRYING_OBSIDIAN:Lnet/minecraft/world/item/Item;
        // 21151: getstatic       net/minecraft/world/level/block/Blocks.BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        // 21154: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21157: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21160: putstatic       net/minecraft/world/item/Items.BLACKSTONE:Lnet/minecraft/world/item/Item;
        // 21163: getstatic       net/minecraft/world/level/block/Blocks.BLACKSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        // 21166: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21169: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21172: putstatic       net/minecraft/world/item/Items.BLACKSTONE_SLAB:Lnet/minecraft/world/item/Item;
        // 21175: getstatic       net/minecraft/world/level/block/Blocks.BLACKSTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        // 21178: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21181: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21184: putstatic       net/minecraft/world/item/Items.BLACKSTONE_STAIRS:Lnet/minecraft/world/item/Item;
        // 21187: getstatic       net/minecraft/world/level/block/Blocks.GILDED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        // 21190: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21193: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21196: putstatic       net/minecraft/world/item/Items.GILDED_BLACKSTONE:Lnet/minecraft/world/item/Item;
        // 21199: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        // 21202: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21205: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21208: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE:Lnet/minecraft/world/item/Item;
        // 21211: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_SLAB:Lnet/minecraft/world/level/block/Block;
        // 21214: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21217: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21220: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_SLAB:Lnet/minecraft/world/item/Item;
        // 21223: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_STAIRS:Lnet/minecraft/world/level/block/Block;
        // 21226: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21229: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21232: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_STAIRS:Lnet/minecraft/world/item/Item;
        // 21235: getstatic       net/minecraft/world/level/block/Blocks.CHISELED_POLISHED_BLACKSTONE:Lnet/minecraft/world/level/block/Block;
        // 21238: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21241: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21244: putstatic       net/minecraft/world/item/Items.CHISELED_POLISHED_BLACKSTONE:Lnet/minecraft/world/item/Item;
        // 21247: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        // 21250: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21253: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21256: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_BRICKS:Lnet/minecraft/world/item/Item;
        // 21259: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_BRICK_SLAB:Lnet/minecraft/world/level/block/Block;
        // 21262: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21265: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21268: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_BRICK_SLAB:Lnet/minecraft/world/item/Item;
        // 21271: getstatic       net/minecraft/world/level/block/Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS:Lnet/minecraft/world/level/block/Block;
        // 21274: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21277: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21280: putstatic       net/minecraft/world/item/Items.POLISHED_BLACKSTONE_BRICK_STAIRS:Lnet/minecraft/world/item/Item;
        // 21283: getstatic       net/minecraft/world/level/block/Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS:Lnet/minecraft/world/level/block/Block;
        // 21286: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_BUILDING_BLOCKS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21289: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21292: putstatic       net/minecraft/world/item/Items.CRACKED_POLISHED_BLACKSTONE_BRICKS:Lnet/minecraft/world/item/Item;
        // 21295: getstatic       net/minecraft/world/level/block/Blocks.RESPAWN_ANCHOR:Lnet/minecraft/world/level/block/Block;
        // 21298: getstatic       net/minecraft/world/item/CreativeModeTab.TAB_DECORATIONS:Lnet/minecraft/world/item/CreativeModeTab;
        // 21301: invokestatic    net/minecraft/world/item/Items.registerBlock:(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/CreativeModeTab;)Lnet/minecraft/world/item/Item;
        // 21304: putstatic       net/minecraft/world/item/Items.RESPAWN_ANCHOR:Lnet/minecraft/world/item/Item;
        // 21307: return         
        // 
        // The error that occurred was:
        // 
        // java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 0
        //     at java.base/java.util.Vector.get(Vector.java:749)
        //     at com.strobel.assembler.metadata.MetadataResolver.resolve(MetadataResolver.java:82)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.resolve(CoreMetadataFactory.java:616)
        //     at com.strobel.assembler.metadata.MetadataHelper.isRawType(MetadataHelper.java:1581)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2361)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visitClassType(MetadataHelper.java:2322)
        //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedType.accept(CoreMetadataFactory.java:577)
        //     at com.strobel.assembler.metadata.MetadataHelper$SameTypeVisitor.visit(MetadataHelper.java:2336)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1412)
        //     at com.strobel.assembler.metadata.MetadataHelper.isSameType(MetadataHelper.java:1399)
        //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypesForVariables(TypeAnalysis.java:575)
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
