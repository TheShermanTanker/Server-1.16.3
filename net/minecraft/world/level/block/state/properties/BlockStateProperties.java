package net.minecraft.world.level.block.state.properties;

import java.util.function.Predicate;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Direction;

public class BlockStateProperties {
    public static final BooleanProperty ATTACHED;
    public static final BooleanProperty BOTTOM;
    public static final BooleanProperty CONDITIONAL;
    public static final BooleanProperty DISARMED;
    public static final BooleanProperty DRAG;
    public static final BooleanProperty ENABLED;
    public static final BooleanProperty EXTENDED;
    public static final BooleanProperty EYE;
    public static final BooleanProperty FALLING;
    public static final BooleanProperty HANGING;
    public static final BooleanProperty HAS_BOTTLE_0;
    public static final BooleanProperty HAS_BOTTLE_1;
    public static final BooleanProperty HAS_BOTTLE_2;
    public static final BooleanProperty HAS_RECORD;
    public static final BooleanProperty HAS_BOOK;
    public static final BooleanProperty INVERTED;
    public static final BooleanProperty IN_WALL;
    public static final BooleanProperty LIT;
    public static final BooleanProperty LOCKED;
    public static final BooleanProperty OCCUPIED;
    public static final BooleanProperty OPEN;
    public static final BooleanProperty PERSISTENT;
    public static final BooleanProperty POWERED;
    public static final BooleanProperty SHORT;
    public static final BooleanProperty SIGNAL_FIRE;
    public static final BooleanProperty SNOWY;
    public static final BooleanProperty TRIGGERED;
    public static final BooleanProperty UNSTABLE;
    public static final BooleanProperty WATERLOGGED;
    public static final BooleanProperty VINE_END;
    public static final EnumProperty<Direction.Axis> HORIZONTAL_AXIS;
    public static final EnumProperty<Direction.Axis> AXIS;
    public static final BooleanProperty UP;
    public static final BooleanProperty DOWN;
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final DirectionProperty FACING;
    public static final DirectionProperty FACING_HOPPER;
    public static final DirectionProperty HORIZONTAL_FACING;
    public static final EnumProperty<FrontAndTop> ORIENTATION;
    public static final EnumProperty<AttachFace> ATTACH_FACE;
    public static final EnumProperty<BellAttachType> BELL_ATTACHMENT;
    public static final EnumProperty<WallSide> EAST_WALL;
    public static final EnumProperty<WallSide> NORTH_WALL;
    public static final EnumProperty<WallSide> SOUTH_WALL;
    public static final EnumProperty<WallSide> WEST_WALL;
    public static final EnumProperty<RedstoneSide> EAST_REDSTONE;
    public static final EnumProperty<RedstoneSide> NORTH_REDSTONE;
    public static final EnumProperty<RedstoneSide> SOUTH_REDSTONE;
    public static final EnumProperty<RedstoneSide> WEST_REDSTONE;
    public static final EnumProperty<DoubleBlockHalf> DOUBLE_BLOCK_HALF;
    public static final EnumProperty<Half> HALF;
    public static final EnumProperty<RailShape> RAIL_SHAPE;
    public static final EnumProperty<RailShape> RAIL_SHAPE_STRAIGHT;
    public static final IntegerProperty AGE_1;
    public static final IntegerProperty AGE_2;
    public static final IntegerProperty AGE_3;
    public static final IntegerProperty AGE_5;
    public static final IntegerProperty AGE_7;
    public static final IntegerProperty AGE_15;
    public static final IntegerProperty AGE_25;
    public static final IntegerProperty BITES;
    public static final IntegerProperty DELAY;
    public static final IntegerProperty DISTANCE;
    public static final IntegerProperty EGGS;
    public static final IntegerProperty HATCH;
    public static final IntegerProperty LAYERS;
    public static final IntegerProperty LEVEL_CAULDRON;
    public static final IntegerProperty LEVEL_COMPOSTER;
    public static final IntegerProperty LEVEL_FLOWING;
    public static final IntegerProperty LEVEL_HONEY;
    public static final IntegerProperty LEVEL;
    public static final IntegerProperty MOISTURE;
    public static final IntegerProperty NOTE;
    public static final IntegerProperty PICKLES;
    public static final IntegerProperty POWER;
    public static final IntegerProperty STAGE;
    public static final IntegerProperty STABILITY_DISTANCE;
    public static final IntegerProperty RESPAWN_ANCHOR_CHARGES;
    public static final IntegerProperty ROTATION_16;
    public static final EnumProperty<BedPart> BED_PART;
    public static final EnumProperty<ChestType> CHEST_TYPE;
    public static final EnumProperty<ComparatorMode> MODE_COMPARATOR;
    public static final EnumProperty<DoorHingeSide> DOOR_HINGE;
    public static final EnumProperty<NoteBlockInstrument> NOTEBLOCK_INSTRUMENT;
    public static final EnumProperty<PistonType> PISTON_TYPE;
    public static final EnumProperty<SlabType> SLAB_TYPE;
    public static final EnumProperty<StairsShape> STAIRS_SHAPE;
    public static final EnumProperty<StructureMode> STRUCTUREBLOCK_MODE;
    public static final EnumProperty<BambooLeaves> BAMBOO_LEAVES;
    
    static {
        ATTACHED = BooleanProperty.create("attached");
        BOTTOM = BooleanProperty.create("bottom");
        CONDITIONAL = BooleanProperty.create("conditional");
        DISARMED = BooleanProperty.create("disarmed");
        DRAG = BooleanProperty.create("drag");
        ENABLED = BooleanProperty.create("enabled");
        EXTENDED = BooleanProperty.create("extended");
        EYE = BooleanProperty.create("eye");
        FALLING = BooleanProperty.create("falling");
        HANGING = BooleanProperty.create("hanging");
        HAS_BOTTLE_0 = BooleanProperty.create("has_bottle_0");
        HAS_BOTTLE_1 = BooleanProperty.create("has_bottle_1");
        HAS_BOTTLE_2 = BooleanProperty.create("has_bottle_2");
        HAS_RECORD = BooleanProperty.create("has_record");
        HAS_BOOK = BooleanProperty.create("has_book");
        INVERTED = BooleanProperty.create("inverted");
        IN_WALL = BooleanProperty.create("in_wall");
        LIT = BooleanProperty.create("lit");
        LOCKED = BooleanProperty.create("locked");
        OCCUPIED = BooleanProperty.create("occupied");
        OPEN = BooleanProperty.create("open");
        PERSISTENT = BooleanProperty.create("persistent");
        POWERED = BooleanProperty.create("powered");
        SHORT = BooleanProperty.create("short");
        SIGNAL_FIRE = BooleanProperty.create("signal_fire");
        SNOWY = BooleanProperty.create("snowy");
        TRIGGERED = BooleanProperty.create("triggered");
        UNSTABLE = BooleanProperty.create("unstable");
        WATERLOGGED = BooleanProperty.create("waterlogged");
        VINE_END = BooleanProperty.create("vine_end");
        HORIZONTAL_AXIS = EnumProperty.<Direction.Axis>create("axis", Direction.Axis.class, new Direction.Axis[] { Direction.Axis.X, Direction.Axis.Z });
        AXIS = EnumProperty.<Direction.Axis>create("axis", Direction.Axis.class);
        UP = BooleanProperty.create("up");
        DOWN = BooleanProperty.create("down");
        NORTH = BooleanProperty.create("north");
        EAST = BooleanProperty.create("east");
        SOUTH = BooleanProperty.create("south");
        WEST = BooleanProperty.create("west");
        FACING = DirectionProperty.create("facing", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);
        FACING_HOPPER = DirectionProperty.create("facing", (Predicate<Direction>)(gc -> gc != Direction.UP));
        HORIZONTAL_FACING = DirectionProperty.create("facing", (Predicate<Direction>)Direction.Plane.HORIZONTAL);
        ORIENTATION = EnumProperty.<FrontAndTop>create("orientation", FrontAndTop.class);
        ATTACH_FACE = EnumProperty.<AttachFace>create("face", AttachFace.class);
        BELL_ATTACHMENT = EnumProperty.<BellAttachType>create("attachment", BellAttachType.class);
        EAST_WALL = EnumProperty.<WallSide>create("east", WallSide.class);
        NORTH_WALL = EnumProperty.<WallSide>create("north", WallSide.class);
        SOUTH_WALL = EnumProperty.<WallSide>create("south", WallSide.class);
        WEST_WALL = EnumProperty.<WallSide>create("west", WallSide.class);
        EAST_REDSTONE = EnumProperty.<RedstoneSide>create("east", RedstoneSide.class);
        NORTH_REDSTONE = EnumProperty.<RedstoneSide>create("north", RedstoneSide.class);
        SOUTH_REDSTONE = EnumProperty.<RedstoneSide>create("south", RedstoneSide.class);
        WEST_REDSTONE = EnumProperty.<RedstoneSide>create("west", RedstoneSide.class);
        DOUBLE_BLOCK_HALF = EnumProperty.<DoubleBlockHalf>create("half", DoubleBlockHalf.class);
        HALF = EnumProperty.<Half>create("half", Half.class);
        RAIL_SHAPE = EnumProperty.<RailShape>create("shape", RailShape.class);
        RAIL_SHAPE_STRAIGHT = EnumProperty.<RailShape>create("shape", RailShape.class, (java.util.function.Predicate<RailShape>)(cfh -> cfh != RailShape.NORTH_EAST && cfh != RailShape.NORTH_WEST && cfh != RailShape.SOUTH_EAST && cfh != RailShape.SOUTH_WEST));
        AGE_1 = IntegerProperty.create("age", 0, 1);
        AGE_2 = IntegerProperty.create("age", 0, 2);
        AGE_3 = IntegerProperty.create("age", 0, 3);
        AGE_5 = IntegerProperty.create("age", 0, 5);
        AGE_7 = IntegerProperty.create("age", 0, 7);
        AGE_15 = IntegerProperty.create("age", 0, 15);
        AGE_25 = IntegerProperty.create("age", 0, 25);
        BITES = IntegerProperty.create("bites", 0, 6);
        DELAY = IntegerProperty.create("delay", 1, 4);
        DISTANCE = IntegerProperty.create("distance", 1, 7);
        EGGS = IntegerProperty.create("eggs", 1, 4);
        HATCH = IntegerProperty.create("hatch", 0, 2);
        LAYERS = IntegerProperty.create("layers", 1, 8);
        LEVEL_CAULDRON = IntegerProperty.create("level", 0, 3);
        LEVEL_COMPOSTER = IntegerProperty.create("level", 0, 8);
        LEVEL_FLOWING = IntegerProperty.create("level", 1, 8);
        LEVEL_HONEY = IntegerProperty.create("honey_level", 0, 5);
        LEVEL = IntegerProperty.create("level", 0, 15);
        MOISTURE = IntegerProperty.create("moisture", 0, 7);
        NOTE = IntegerProperty.create("note", 0, 24);
        PICKLES = IntegerProperty.create("pickles", 1, 4);
        POWER = IntegerProperty.create("power", 0, 15);
        STAGE = IntegerProperty.create("stage", 0, 1);
        STABILITY_DISTANCE = IntegerProperty.create("distance", 0, 7);
        RESPAWN_ANCHOR_CHARGES = IntegerProperty.create("charges", 0, 4);
        ROTATION_16 = IntegerProperty.create("rotation", 0, 15);
        BED_PART = EnumProperty.<BedPart>create("part", BedPart.class);
        CHEST_TYPE = EnumProperty.<ChestType>create("type", ChestType.class);
        MODE_COMPARATOR = EnumProperty.<ComparatorMode>create("mode", ComparatorMode.class);
        DOOR_HINGE = EnumProperty.<DoorHingeSide>create("hinge", DoorHingeSide.class);
        NOTEBLOCK_INSTRUMENT = EnumProperty.<NoteBlockInstrument>create("instrument", NoteBlockInstrument.class);
        PISTON_TYPE = EnumProperty.<PistonType>create("type", PistonType.class);
        SLAB_TYPE = EnumProperty.<SlabType>create("type", SlabType.class);
        STAIRS_SHAPE = EnumProperty.<StairsShape>create("shape", StairsShape.class);
        STRUCTUREBLOCK_MODE = EnumProperty.<StructureMode>create("mode", StructureMode.class);
        BAMBOO_LEAVES = EnumProperty.<BambooLeaves>create("leaves", BambooLeaves.class);
    }
}
