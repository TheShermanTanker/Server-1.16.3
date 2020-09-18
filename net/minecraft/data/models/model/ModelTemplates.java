package net.minecraft.data.models.model;

import java.util.stream.IntStream;
import net.minecraft.resources.ResourceLocation;
import java.util.Optional;

public class ModelTemplates {
    public static final ModelTemplate CUBE;
    public static final ModelTemplate CUBE_DIRECTIONAL;
    public static final ModelTemplate CUBE_ALL;
    public static final ModelTemplate CUBE_MIRRORED_ALL;
    public static final ModelTemplate CUBE_COLUMN;
    public static final ModelTemplate CUBE_COLUMN_HORIZONTAL;
    public static final ModelTemplate CUBE_TOP;
    public static final ModelTemplate CUBE_BOTTOM_TOP;
    public static final ModelTemplate CUBE_ORIENTABLE;
    public static final ModelTemplate CUBE_ORIENTABLE_TOP_BOTTOM;
    public static final ModelTemplate CUBE_ORIENTABLE_VERTICAL;
    public static final ModelTemplate BUTTON;
    public static final ModelTemplate BUTTON_PRESSED;
    public static final ModelTemplate BUTTON_INVENTORY;
    public static final ModelTemplate DOOR_BOTTOM;
    public static final ModelTemplate DOOR_BOTTOM_HINGE;
    public static final ModelTemplate DOOR_TOP;
    public static final ModelTemplate DOOR_TOP_HINGE;
    public static final ModelTemplate FENCE_POST;
    public static final ModelTemplate FENCE_SIDE;
    public static final ModelTemplate FENCE_INVENTORY;
    public static final ModelTemplate WALL_POST;
    public static final ModelTemplate WALL_LOW_SIDE;
    public static final ModelTemplate WALL_TALL_SIDE;
    public static final ModelTemplate WALL_INVENTORY;
    public static final ModelTemplate FENCE_GATE_CLOSED;
    public static final ModelTemplate FENCE_GATE_OPEN;
    public static final ModelTemplate FENCE_GATE_WALL_CLOSED;
    public static final ModelTemplate FENCE_GATE_WALL_OPEN;
    public static final ModelTemplate PRESSURE_PLATE_UP;
    public static final ModelTemplate PRESSURE_PLATE_DOWN;
    public static final ModelTemplate PARTICLE_ONLY;
    public static final ModelTemplate SLAB_BOTTOM;
    public static final ModelTemplate SLAB_TOP;
    public static final ModelTemplate LEAVES;
    public static final ModelTemplate STAIRS_STRAIGHT;
    public static final ModelTemplate STAIRS_INNER;
    public static final ModelTemplate STAIRS_OUTER;
    public static final ModelTemplate TRAPDOOR_TOP;
    public static final ModelTemplate TRAPDOOR_BOTTOM;
    public static final ModelTemplate TRAPDOOR_OPEN;
    public static final ModelTemplate ORIENTABLE_TRAPDOOR_TOP;
    public static final ModelTemplate ORIENTABLE_TRAPDOOR_BOTTOM;
    public static final ModelTemplate ORIENTABLE_TRAPDOOR_OPEN;
    public static final ModelTemplate CROSS;
    public static final ModelTemplate TINTED_CROSS;
    public static final ModelTemplate FLOWER_POT_CROSS;
    public static final ModelTemplate TINTED_FLOWER_POT_CROSS;
    public static final ModelTemplate RAIL_FLAT;
    public static final ModelTemplate RAIL_CURVED;
    public static final ModelTemplate RAIL_RAISED_NE;
    public static final ModelTemplate RAIL_RAISED_SW;
    public static final ModelTemplate CARPET;
    public static final ModelTemplate CORAL_FAN;
    public static final ModelTemplate CORAL_WALL_FAN;
    public static final ModelTemplate GLAZED_TERRACOTTA;
    public static final ModelTemplate CHORUS_FLOWER;
    public static final ModelTemplate DAYLIGHT_DETECTOR;
    public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE;
    public static final ModelTemplate STAINED_GLASS_PANE_NOSIDE_ALT;
    public static final ModelTemplate STAINED_GLASS_PANE_POST;
    public static final ModelTemplate STAINED_GLASS_PANE_SIDE;
    public static final ModelTemplate STAINED_GLASS_PANE_SIDE_ALT;
    public static final ModelTemplate COMMAND_BLOCK;
    public static final ModelTemplate ANVIL;
    public static final ModelTemplate[] STEMS;
    public static final ModelTemplate ATTACHED_STEM;
    public static final ModelTemplate CROP;
    public static final ModelTemplate FARMLAND;
    public static final ModelTemplate FIRE_FLOOR;
    public static final ModelTemplate FIRE_SIDE;
    public static final ModelTemplate FIRE_SIDE_ALT;
    public static final ModelTemplate FIRE_UP;
    public static final ModelTemplate FIRE_UP_ALT;
    public static final ModelTemplate CAMPFIRE;
    public static final ModelTemplate LANTERN;
    public static final ModelTemplate HANGING_LANTERN;
    public static final ModelTemplate TORCH;
    public static final ModelTemplate WALL_TORCH;
    public static final ModelTemplate PISTON;
    public static final ModelTemplate PISTON_HEAD;
    public static final ModelTemplate PISTON_HEAD_SHORT;
    public static final ModelTemplate SEAGRASS;
    public static final ModelTemplate TURTLE_EGG;
    public static final ModelTemplate TWO_TURTLE_EGGS;
    public static final ModelTemplate THREE_TURTLE_EGGS;
    public static final ModelTemplate FOUR_TURTLE_EGGS;
    public static final ModelTemplate SINGLE_FACE;
    public static final ModelTemplate FLAT_ITEM;
    public static final ModelTemplate FLAT_HANDHELD_ITEM;
    public static final ModelTemplate FLAT_HANDHELD_ROD_ITEM;
    public static final ModelTemplate SHULKER_BOX_INVENTORY;
    public static final ModelTemplate BED_INVENTORY;
    public static final ModelTemplate BANNER_INVENTORY;
    public static final ModelTemplate SKULL_INVENTORY;
    
    private static ModelTemplate create(final TextureSlot... arr) {
        return new ModelTemplate((Optional<ResourceLocation>)Optional.empty(), (Optional<String>)Optional.empty(), arr);
    }
    
    private static ModelTemplate create(final String string, final TextureSlot... arr) {
        return new ModelTemplate((Optional<ResourceLocation>)Optional.of(new ResourceLocation("minecraft", "block/" + string)), (Optional<String>)Optional.empty(), arr);
    }
    
    private static ModelTemplate createItem(final String string, final TextureSlot... arr) {
        return new ModelTemplate((Optional<ResourceLocation>)Optional.of(new ResourceLocation("minecraft", "item/" + string)), (Optional<String>)Optional.empty(), arr);
    }
    
    private static ModelTemplate create(final String string1, final String string2, final TextureSlot... arr) {
        return new ModelTemplate((Optional<ResourceLocation>)Optional.of(new ResourceLocation("minecraft", "block/" + string1)), (Optional<String>)Optional.of(string2), arr);
    }
    
    static {
        CUBE = create("cube", TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN);
        CUBE_DIRECTIONAL = create("cube_directional", TextureSlot.PARTICLE, TextureSlot.NORTH, TextureSlot.SOUTH, TextureSlot.EAST, TextureSlot.WEST, TextureSlot.UP, TextureSlot.DOWN);
        CUBE_ALL = create("cube_all", TextureSlot.ALL);
        CUBE_MIRRORED_ALL = create("cube_mirrored_all", "_mirrored", TextureSlot.ALL);
        CUBE_COLUMN = create("cube_column", TextureSlot.END, TextureSlot.SIDE);
        CUBE_COLUMN_HORIZONTAL = create("cube_column_horizontal", "_horizontal", TextureSlot.END, TextureSlot.SIDE);
        CUBE_TOP = create("cube_top", TextureSlot.TOP, TextureSlot.SIDE);
        CUBE_BOTTOM_TOP = create("cube_bottom_top", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
        CUBE_ORIENTABLE = create("orientable", TextureSlot.TOP, TextureSlot.FRONT, TextureSlot.SIDE);
        CUBE_ORIENTABLE_TOP_BOTTOM = create("orientable_with_bottom", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE, TextureSlot.FRONT);
        CUBE_ORIENTABLE_VERTICAL = create("orientable_vertical", "_vertical", TextureSlot.FRONT, TextureSlot.SIDE);
        BUTTON = create("button", TextureSlot.TEXTURE);
        BUTTON_PRESSED = create("button_pressed", "_pressed", TextureSlot.TEXTURE);
        BUTTON_INVENTORY = create("button_inventory", "_inventory", TextureSlot.TEXTURE);
        DOOR_BOTTOM = create("door_bottom", "_bottom", TextureSlot.TOP, TextureSlot.BOTTOM);
        DOOR_BOTTOM_HINGE = create("door_bottom_rh", "_bottom_hinge", TextureSlot.TOP, TextureSlot.BOTTOM);
        DOOR_TOP = create("door_top", "_top", TextureSlot.TOP, TextureSlot.BOTTOM);
        DOOR_TOP_HINGE = create("door_top_rh", "_top_hinge", TextureSlot.TOP, TextureSlot.BOTTOM);
        FENCE_POST = create("fence_post", "_post", TextureSlot.TEXTURE);
        FENCE_SIDE = create("fence_side", "_side", TextureSlot.TEXTURE);
        FENCE_INVENTORY = create("fence_inventory", "_inventory", TextureSlot.TEXTURE);
        WALL_POST = create("template_wall_post", "_post", TextureSlot.WALL);
        WALL_LOW_SIDE = create("template_wall_side", "_side", TextureSlot.WALL);
        WALL_TALL_SIDE = create("template_wall_side_tall", "_side_tall", TextureSlot.WALL);
        WALL_INVENTORY = create("wall_inventory", "_inventory", TextureSlot.WALL);
        FENCE_GATE_CLOSED = create("template_fence_gate", TextureSlot.TEXTURE);
        FENCE_GATE_OPEN = create("template_fence_gate_open", "_open", TextureSlot.TEXTURE);
        FENCE_GATE_WALL_CLOSED = create("template_fence_gate_wall", "_wall", TextureSlot.TEXTURE);
        FENCE_GATE_WALL_OPEN = create("template_fence_gate_wall_open", "_wall_open", TextureSlot.TEXTURE);
        PRESSURE_PLATE_UP = create("pressure_plate_up", TextureSlot.TEXTURE);
        PRESSURE_PLATE_DOWN = create("pressure_plate_down", "_down", TextureSlot.TEXTURE);
        PARTICLE_ONLY = create(TextureSlot.PARTICLE);
        SLAB_BOTTOM = create("slab", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
        SLAB_TOP = create("slab_top", "_top", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
        LEAVES = create("leaves", TextureSlot.ALL);
        STAIRS_STRAIGHT = create("stairs", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
        STAIRS_INNER = create("inner_stairs", "_inner", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
        STAIRS_OUTER = create("outer_stairs", "_outer", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
        TRAPDOOR_TOP = create("template_trapdoor_top", "_top", TextureSlot.TEXTURE);
        TRAPDOOR_BOTTOM = create("template_trapdoor_bottom", "_bottom", TextureSlot.TEXTURE);
        TRAPDOOR_OPEN = create("template_trapdoor_open", "_open", TextureSlot.TEXTURE);
        ORIENTABLE_TRAPDOOR_TOP = create("template_orientable_trapdoor_top", "_top", TextureSlot.TEXTURE);
        ORIENTABLE_TRAPDOOR_BOTTOM = create("template_orientable_trapdoor_bottom", "_bottom", TextureSlot.TEXTURE);
        ORIENTABLE_TRAPDOOR_OPEN = create("template_orientable_trapdoor_open", "_open", TextureSlot.TEXTURE);
        CROSS = create("cross", TextureSlot.CROSS);
        TINTED_CROSS = create("tinted_cross", TextureSlot.CROSS);
        FLOWER_POT_CROSS = create("flower_pot_cross", TextureSlot.PLANT);
        TINTED_FLOWER_POT_CROSS = create("tinted_flower_pot_cross", TextureSlot.PLANT);
        RAIL_FLAT = create("rail_flat", TextureSlot.RAIL);
        RAIL_CURVED = create("rail_curved", "_corner", TextureSlot.RAIL);
        RAIL_RAISED_NE = create("template_rail_raised_ne", "_raised_ne", TextureSlot.RAIL);
        RAIL_RAISED_SW = create("template_rail_raised_sw", "_raised_sw", TextureSlot.RAIL);
        CARPET = create("carpet", TextureSlot.WOOL);
        CORAL_FAN = create("coral_fan", TextureSlot.FAN);
        CORAL_WALL_FAN = create("coral_wall_fan", TextureSlot.FAN);
        GLAZED_TERRACOTTA = create("template_glazed_terracotta", TextureSlot.PATTERN);
        CHORUS_FLOWER = create("template_chorus_flower", TextureSlot.TEXTURE);
        DAYLIGHT_DETECTOR = create("template_daylight_detector", TextureSlot.TOP, TextureSlot.SIDE);
        STAINED_GLASS_PANE_NOSIDE = create("template_glass_pane_noside", "_noside", TextureSlot.PANE);
        STAINED_GLASS_PANE_NOSIDE_ALT = create("template_glass_pane_noside_alt", "_noside_alt", TextureSlot.PANE);
        STAINED_GLASS_PANE_POST = create("template_glass_pane_post", "_post", TextureSlot.PANE, TextureSlot.EDGE);
        STAINED_GLASS_PANE_SIDE = create("template_glass_pane_side", "_side", TextureSlot.PANE, TextureSlot.EDGE);
        STAINED_GLASS_PANE_SIDE_ALT = create("template_glass_pane_side_alt", "_side_alt", TextureSlot.PANE, TextureSlot.EDGE);
        COMMAND_BLOCK = create("template_command_block", TextureSlot.FRONT, TextureSlot.BACK, TextureSlot.SIDE);
        ANVIL = create("template_anvil", TextureSlot.TOP);
        STEMS = (ModelTemplate[])IntStream.range(0, 8).mapToObj(integer -> create(new StringBuilder().append("stem_growth").append(integer).toString(), new StringBuilder().append("_stage").append(integer).toString(), TextureSlot.STEM)).toArray(ModelTemplate[]::new);
        ATTACHED_STEM = create("stem_fruit", TextureSlot.STEM, TextureSlot.UPPER_STEM);
        CROP = create("crop", TextureSlot.CROP);
        FARMLAND = create("template_farmland", TextureSlot.DIRT, TextureSlot.TOP);
        FIRE_FLOOR = create("template_fire_floor", TextureSlot.FIRE);
        FIRE_SIDE = create("template_fire_side", TextureSlot.FIRE);
        FIRE_SIDE_ALT = create("template_fire_side_alt", TextureSlot.FIRE);
        FIRE_UP = create("template_fire_up", TextureSlot.FIRE);
        FIRE_UP_ALT = create("template_fire_up_alt", TextureSlot.FIRE);
        CAMPFIRE = create("template_campfire", TextureSlot.FIRE, TextureSlot.LIT_LOG);
        LANTERN = create("template_lantern", TextureSlot.LANTERN);
        HANGING_LANTERN = create("template_hanging_lantern", "_hanging", TextureSlot.LANTERN);
        TORCH = create("template_torch", TextureSlot.TORCH);
        WALL_TORCH = create("template_torch_wall", TextureSlot.TORCH);
        PISTON = create("template_piston", TextureSlot.PLATFORM, TextureSlot.BOTTOM, TextureSlot.SIDE);
        PISTON_HEAD = create("template_piston_head", TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY);
        PISTON_HEAD_SHORT = create("template_piston_head_short", TextureSlot.PLATFORM, TextureSlot.SIDE, TextureSlot.UNSTICKY);
        SEAGRASS = create("template_seagrass", TextureSlot.TEXTURE);
        TURTLE_EGG = create("template_turtle_egg", TextureSlot.ALL);
        TWO_TURTLE_EGGS = create("template_two_turtle_eggs", TextureSlot.ALL);
        THREE_TURTLE_EGGS = create("template_three_turtle_eggs", TextureSlot.ALL);
        FOUR_TURTLE_EGGS = create("template_four_turtle_eggs", TextureSlot.ALL);
        SINGLE_FACE = create("template_single_face", TextureSlot.TEXTURE);
        FLAT_ITEM = createItem("generated", TextureSlot.LAYER0);
        FLAT_HANDHELD_ITEM = createItem("handheld", TextureSlot.LAYER0);
        FLAT_HANDHELD_ROD_ITEM = createItem("handheld_rod", TextureSlot.LAYER0);
        SHULKER_BOX_INVENTORY = createItem("template_shulker_box", TextureSlot.PARTICLE);
        BED_INVENTORY = createItem("template_bed", TextureSlot.PARTICLE);
        BANNER_INVENTORY = createItem("template_banner");
        SKULL_INVENTORY = createItem("template_skull");
    }
}