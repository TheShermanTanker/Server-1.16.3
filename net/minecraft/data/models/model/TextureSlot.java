package net.minecraft.data.models.model;

import javax.annotation.Nullable;

public final class TextureSlot {
    public static final TextureSlot ALL;
    public static final TextureSlot TEXTURE;
    public static final TextureSlot PARTICLE;
    public static final TextureSlot END;
    public static final TextureSlot BOTTOM;
    public static final TextureSlot TOP;
    public static final TextureSlot FRONT;
    public static final TextureSlot BACK;
    public static final TextureSlot SIDE;
    public static final TextureSlot NORTH;
    public static final TextureSlot SOUTH;
    public static final TextureSlot EAST;
    public static final TextureSlot WEST;
    public static final TextureSlot UP;
    public static final TextureSlot DOWN;
    public static final TextureSlot CROSS;
    public static final TextureSlot PLANT;
    public static final TextureSlot WALL;
    public static final TextureSlot RAIL;
    public static final TextureSlot WOOL;
    public static final TextureSlot PATTERN;
    public static final TextureSlot PANE;
    public static final TextureSlot EDGE;
    public static final TextureSlot FAN;
    public static final TextureSlot STEM;
    public static final TextureSlot UPPER_STEM;
    public static final TextureSlot CROP;
    public static final TextureSlot DIRT;
    public static final TextureSlot FIRE;
    public static final TextureSlot LANTERN;
    public static final TextureSlot PLATFORM;
    public static final TextureSlot UNSTICKY;
    public static final TextureSlot TORCH;
    public static final TextureSlot LAYER0;
    public static final TextureSlot LIT_LOG;
    private final String id;
    @Nullable
    private final TextureSlot parent;
    
    private static TextureSlot create(final String string) {
        return new TextureSlot(string, null);
    }
    
    private static TextureSlot create(final String string, final TextureSlot ja) {
        return new TextureSlot(string, ja);
    }
    
    private TextureSlot(final String string, @Nullable final TextureSlot ja) {
        this.id = string;
        this.parent = ja;
    }
    
    public String getId() {
        return this.id;
    }
    
    @Nullable
    public TextureSlot getParent() {
        return this.parent;
    }
    
    public String toString() {
        return "#" + this.id;
    }
    
    static {
        ALL = create("all");
        TEXTURE = create("texture", TextureSlot.ALL);
        PARTICLE = create("particle", TextureSlot.TEXTURE);
        END = create("end", TextureSlot.ALL);
        BOTTOM = create("bottom", TextureSlot.END);
        TOP = create("top", TextureSlot.END);
        FRONT = create("front", TextureSlot.ALL);
        BACK = create("back", TextureSlot.ALL);
        SIDE = create("side", TextureSlot.ALL);
        NORTH = create("north", TextureSlot.SIDE);
        SOUTH = create("south", TextureSlot.SIDE);
        EAST = create("east", TextureSlot.SIDE);
        WEST = create("west", TextureSlot.SIDE);
        UP = create("up");
        DOWN = create("down");
        CROSS = create("cross");
        PLANT = create("plant");
        WALL = create("wall", TextureSlot.ALL);
        RAIL = create("rail");
        WOOL = create("wool");
        PATTERN = create("pattern");
        PANE = create("pane");
        EDGE = create("edge");
        FAN = create("fan");
        STEM = create("stem");
        UPPER_STEM = create("upperstem");
        CROP = create("crop");
        DIRT = create("dirt");
        FIRE = create("fire");
        LANTERN = create("lantern");
        PLATFORM = create("platform");
        UNSTICKY = create("unsticky");
        TORCH = create("torch");
        LAYER0 = create("layer0");
        LIT_LOG = create("lit_log");
    }
}
