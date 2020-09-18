package net.minecraft.util.datafix.fixes;

import java.util.function.Consumer;
import com.mojang.datafixers.DataFixUtils;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Optional;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.util.Unit;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Either;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import java.util.Map;
import com.mojang.datafixers.DataFix;

public class EntityBlockStateFix extends DataFix {
    private static final Map<String, Integer> MAP;
    
    public EntityBlockStateFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public static int getBlockId(final String string) {
        final Integer integer2 = (Integer)EntityBlockStateFix.MAP.get(string);
        return (integer2 == null) ? 0 : integer2;
    }
    
    public TypeRewriteRule makeRule() {
        final Schema schema2 = this.getInputSchema();
        final Schema schema3 = this.getOutputSchema();
        final Function<Typed<?>, Typed<?>> function4 = (Function<Typed<?>, Typed<?>>)(typed -> this.updateBlockToBlockState(typed, "DisplayTile", "DisplayData", "DisplayState"));
        final Function<Typed<?>, Typed<?>> function5 = (Function<Typed<?>, Typed<?>>)(typed -> this.updateBlockToBlockState(typed, "inTile", "inData", "inBlockState"));
        final Type<Pair<Either<Pair<String, Either<Integer, String>>, Unit>, Dynamic<?>>> type6 = DSL.<Either<Pair<String, Either<Integer, String>>, Unit>, Dynamic<?>>and(DSL.<Pair<String, Either<Integer, String>>>optional(DSL.<Pair<String, Either<Integer, String>>>field("inTile", DSL.<Either<Integer, String>>named(References.BLOCK_NAME.typeName(), DSL.<Integer, String>or(DSL.intType(), NamespacedSchema.namespacedString())))), DSL.remainderType());
        final Function<Typed<?>, Typed<?>> function6 = (Function<Typed<?>, Typed<?>>)(typed -> typed.<Object, Dynamic<?>>update(type6.finder(), DSL.remainderType(), Pair::getSecond));
        return this.fixTypeEverywhereTyped("EntityBlockStateFix", schema2.getType(References.ENTITY), schema3.getType(References.ENTITY), (Function<Typed<?>, Typed<?>>)(typed -> {
            typed = this.updateEntity(typed, "minecraft:falling_block", (Function<Typed<?>, Typed<?>>)this::updateFallingBlock);
            typed = this.updateEntity(typed, "minecraft:enderman", (Function<Typed<?>, Typed<?>>)(typed -> this.updateBlockToBlockState(typed, "carried", "carriedData", "carriedBlockState")));
            typed = this.updateEntity(typed, "minecraft:arrow", (Function<Typed<?>, Typed<?>>)function5);
            typed = this.updateEntity(typed, "minecraft:spectral_arrow", (Function<Typed<?>, Typed<?>>)function5);
            typed = this.updateEntity(typed, "minecraft:egg", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:ender_pearl", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:fireball", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:potion", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:small_fireball", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:snowball", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:wither_skull", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:xp_bottle", (Function<Typed<?>, Typed<?>>)function6);
            typed = this.updateEntity(typed, "minecraft:commandblock_minecart", (Function<Typed<?>, Typed<?>>)function4);
            typed = this.updateEntity(typed, "minecraft:minecart", (Function<Typed<?>, Typed<?>>)function4);
            typed = this.updateEntity(typed, "minecraft:chest_minecart", (Function<Typed<?>, Typed<?>>)function4);
            typed = this.updateEntity(typed, "minecraft:furnace_minecart", (Function<Typed<?>, Typed<?>>)function4);
            typed = this.updateEntity(typed, "minecraft:tnt_minecart", (Function<Typed<?>, Typed<?>>)function4);
            typed = this.updateEntity(typed, "minecraft:hopper_minecart", (Function<Typed<?>, Typed<?>>)function4);
            typed = this.updateEntity(typed, "minecraft:spawner_minecart", (Function<Typed<?>, Typed<?>>)function4);
            return typed;
        }));
    }
    
    private Typed<?> updateFallingBlock(final Typed<?> typed) {
        final Type<Either<Pair<String, Either<Integer, String>>, Unit>> type3 = DSL.<Pair<String, Either<Integer, String>>>optional(DSL.<Pair<String, Either<Integer, String>>>field("Block", DSL.<Either<Integer, String>>named(References.BLOCK_NAME.typeName(), DSL.<Integer, String>or(DSL.intType(), NamespacedSchema.namespacedString()))));
        final Type<Either<Pair<String, Dynamic<?>>, Unit>> type4 = DSL.<Pair<String, Dynamic<?>>>optional(DSL.<Pair<String, Dynamic<?>>>field("BlockState", DSL.<Dynamic<?>>named(References.BLOCK_STATE.typeName(), DSL.remainderType())));
        final Dynamic<?> dynamic5 = typed.<Dynamic<?>>get(DSL.remainderFinder());
        return typed.<Either<Pair<String, Either<Integer, String>>, Unit>, Either<Pair<String, Dynamic<?>>, Unit>>update(type3.finder(), type4, (java.util.function.Function<Either<Pair<String, Either<Integer, String>>, Unit>, Either<Pair<String, Dynamic<?>>, Unit>>)(either -> {
            final int integer3 = either.<Integer>map(pair -> pair.getSecond().<Integer>map((java.util.function.Function<? super Object, ? extends Integer>)(integer -> integer), (java.util.function.Function<? super Object, ? extends Integer>)EntityBlockStateFix::getBlockId), unit -> {
                final Optional<Number> optional3 = (Optional<Number>)dynamic5.get("TileID").asNumber().result();
                return (Integer)optional3.map(Number::intValue).orElseGet(() -> dynamic5.get("Tile").asByte((byte)0) & 0xFF);
            });
            final int integer4 = dynamic5.get("Data").asInt(0) & 0xF;
            return Either.<Pair<String, Dynamic<?>>, Object>left(Pair.<String, Dynamic<?>>of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(integer3 << 4 | integer4)));
        })).<Dynamic<?>>set(DSL.remainderFinder(), dynamic5.remove("Data").remove("TileID").remove("Tile"));
    }
    
    private Typed<?> updateBlockToBlockState(final Typed<?> typed, final String string2, final String string3, final String string4) {
        final Type<Pair<String, Either<Integer, String>>> type6 = DSL.<Pair<String, Either<Integer, String>>>field(string2, DSL.<Either<Integer, String>>named(References.BLOCK_NAME.typeName(), DSL.<Integer, String>or(DSL.intType(), NamespacedSchema.namespacedString())));
        final Type<Pair<String, Dynamic<?>>> type7 = DSL.<Pair<String, Dynamic<?>>>field(string4, DSL.<Dynamic<?>>named(References.BLOCK_STATE.typeName(), DSL.remainderType()));
        final Dynamic<?> dynamic8 = typed.<Dynamic<?>>getOrCreate(DSL.remainderFinder());
        return typed.<Pair<String, Either<Integer, String>>, Pair<String, Dynamic<?>>>update(type6.finder(), type7, (java.util.function.Function<Pair<String, Either<Integer, String>>, Pair<String, Dynamic<?>>>)(pair -> {
            final int integer4 = pair.getSecond().<Integer>map((java.util.function.Function<? super Object, ? extends Integer>)(integer -> integer), (java.util.function.Function<? super Object, ? extends Integer>)EntityBlockStateFix::getBlockId);
            final int integer5 = dynamic8.get(string3).asInt(0) & 0xF;
            return Pair.<String, Dynamic<?>>of(References.BLOCK_STATE.typeName(), BlockStateData.getTag(integer4 << 4 | integer5));
        })).<Dynamic<?>>set(DSL.remainderFinder(), dynamic8.remove(string3));
    }
    
    private Typed<?> updateEntity(final Typed<?> typed, final String string, final Function<Typed<?>, Typed<?>> function) {
        final Type<?> type5 = this.getInputSchema().getChoiceType(References.ENTITY, string);
        final Type<?> type6 = this.getOutputSchema().getChoiceType(References.ENTITY, string);
        return typed.updateTyped(DSL.namedChoice(string, type5), type6, function);
    }
    
    static {
        MAP = DataFixUtils.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("minecraft:air", 0);
            hashMap.put("minecraft:stone", 1);
            hashMap.put("minecraft:grass", 2);
            hashMap.put("minecraft:dirt", 3);
            hashMap.put("minecraft:cobblestone", 4);
            hashMap.put("minecraft:planks", 5);
            hashMap.put("minecraft:sapling", 6);
            hashMap.put("minecraft:bedrock", 7);
            hashMap.put("minecraft:flowing_water", 8);
            hashMap.put("minecraft:water", 9);
            hashMap.put("minecraft:flowing_lava", 10);
            hashMap.put("minecraft:lava", 11);
            hashMap.put("minecraft:sand", 12);
            hashMap.put("minecraft:gravel", 13);
            hashMap.put("minecraft:gold_ore", 14);
            hashMap.put("minecraft:iron_ore", 15);
            hashMap.put("minecraft:coal_ore", 16);
            hashMap.put("minecraft:log", 17);
            hashMap.put("minecraft:leaves", 18);
            hashMap.put("minecraft:sponge", 19);
            hashMap.put("minecraft:glass", 20);
            hashMap.put("minecraft:lapis_ore", 21);
            hashMap.put("minecraft:lapis_block", 22);
            hashMap.put("minecraft:dispenser", 23);
            hashMap.put("minecraft:sandstone", 24);
            hashMap.put("minecraft:noteblock", 25);
            hashMap.put("minecraft:bed", 26);
            hashMap.put("minecraft:golden_rail", 27);
            hashMap.put("minecraft:detector_rail", 28);
            hashMap.put("minecraft:sticky_piston", 29);
            hashMap.put("minecraft:web", 30);
            hashMap.put("minecraft:tallgrass", 31);
            hashMap.put("minecraft:deadbush", 32);
            hashMap.put("minecraft:piston", 33);
            hashMap.put("minecraft:piston_head", 34);
            hashMap.put("minecraft:wool", 35);
            hashMap.put("minecraft:piston_extension", 36);
            hashMap.put("minecraft:yellow_flower", 37);
            hashMap.put("minecraft:red_flower", 38);
            hashMap.put("minecraft:brown_mushroom", 39);
            hashMap.put("minecraft:red_mushroom", 40);
            hashMap.put("minecraft:gold_block", 41);
            hashMap.put("minecraft:iron_block", 42);
            hashMap.put("minecraft:double_stone_slab", 43);
            hashMap.put("minecraft:stone_slab", 44);
            hashMap.put("minecraft:brick_block", 45);
            hashMap.put("minecraft:tnt", 46);
            hashMap.put("minecraft:bookshelf", 47);
            hashMap.put("minecraft:mossy_cobblestone", 48);
            hashMap.put("minecraft:obsidian", 49);
            hashMap.put("minecraft:torch", 50);
            hashMap.put("minecraft:fire", 51);
            hashMap.put("minecraft:mob_spawner", 52);
            hashMap.put("minecraft:oak_stairs", 53);
            hashMap.put("minecraft:chest", 54);
            hashMap.put("minecraft:redstone_wire", 55);
            hashMap.put("minecraft:diamond_ore", 56);
            hashMap.put("minecraft:diamond_block", 57);
            hashMap.put("minecraft:crafting_table", 58);
            hashMap.put("minecraft:wheat", 59);
            hashMap.put("minecraft:farmland", 60);
            hashMap.put("minecraft:furnace", 61);
            hashMap.put("minecraft:lit_furnace", 62);
            hashMap.put("minecraft:standing_sign", 63);
            hashMap.put("minecraft:wooden_door", 64);
            hashMap.put("minecraft:ladder", 65);
            hashMap.put("minecraft:rail", 66);
            hashMap.put("minecraft:stone_stairs", 67);
            hashMap.put("minecraft:wall_sign", 68);
            hashMap.put("minecraft:lever", 69);
            hashMap.put("minecraft:stone_pressure_plate", 70);
            hashMap.put("minecraft:iron_door", 71);
            hashMap.put("minecraft:wooden_pressure_plate", 72);
            hashMap.put("minecraft:redstone_ore", 73);
            hashMap.put("minecraft:lit_redstone_ore", 74);
            hashMap.put("minecraft:unlit_redstone_torch", 75);
            hashMap.put("minecraft:redstone_torch", 76);
            hashMap.put("minecraft:stone_button", 77);
            hashMap.put("minecraft:snow_layer", 78);
            hashMap.put("minecraft:ice", 79);
            hashMap.put("minecraft:snow", 80);
            hashMap.put("minecraft:cactus", 81);
            hashMap.put("minecraft:clay", 82);
            hashMap.put("minecraft:reeds", 83);
            hashMap.put("minecraft:jukebox", 84);
            hashMap.put("minecraft:fence", 85);
            hashMap.put("minecraft:pumpkin", 86);
            hashMap.put("minecraft:netherrack", 87);
            hashMap.put("minecraft:soul_sand", 88);
            hashMap.put("minecraft:glowstone", 89);
            hashMap.put("minecraft:portal", 90);
            hashMap.put("minecraft:lit_pumpkin", 91);
            hashMap.put("minecraft:cake", 92);
            hashMap.put("minecraft:unpowered_repeater", 93);
            hashMap.put("minecraft:powered_repeater", 94);
            hashMap.put("minecraft:stained_glass", 95);
            hashMap.put("minecraft:trapdoor", 96);
            hashMap.put("minecraft:monster_egg", 97);
            hashMap.put("minecraft:stonebrick", 98);
            hashMap.put("minecraft:brown_mushroom_block", 99);
            hashMap.put("minecraft:red_mushroom_block", 100);
            hashMap.put("minecraft:iron_bars", 101);
            hashMap.put("minecraft:glass_pane", 102);
            hashMap.put("minecraft:melon_block", 103);
            hashMap.put("minecraft:pumpkin_stem", 104);
            hashMap.put("minecraft:melon_stem", 105);
            hashMap.put("minecraft:vine", 106);
            hashMap.put("minecraft:fence_gate", 107);
            hashMap.put("minecraft:brick_stairs", 108);
            hashMap.put("minecraft:stone_brick_stairs", 109);
            hashMap.put("minecraft:mycelium", 110);
            hashMap.put("minecraft:waterlily", 111);
            hashMap.put("minecraft:nether_brick", 112);
            hashMap.put("minecraft:nether_brick_fence", 113);
            hashMap.put("minecraft:nether_brick_stairs", 114);
            hashMap.put("minecraft:nether_wart", 115);
            hashMap.put("minecraft:enchanting_table", 116);
            hashMap.put("minecraft:brewing_stand", 117);
            hashMap.put("minecraft:cauldron", 118);
            hashMap.put("minecraft:end_portal", 119);
            hashMap.put("minecraft:end_portal_frame", 120);
            hashMap.put("minecraft:end_stone", 121);
            hashMap.put("minecraft:dragon_egg", 122);
            hashMap.put("minecraft:redstone_lamp", 123);
            hashMap.put("minecraft:lit_redstone_lamp", 124);
            hashMap.put("minecraft:double_wooden_slab", 125);
            hashMap.put("minecraft:wooden_slab", 126);
            hashMap.put("minecraft:cocoa", 127);
            hashMap.put("minecraft:sandstone_stairs", 128);
            hashMap.put("minecraft:emerald_ore", 129);
            hashMap.put("minecraft:ender_chest", 130);
            hashMap.put("minecraft:tripwire_hook", 131);
            hashMap.put("minecraft:tripwire", 132);
            hashMap.put("minecraft:emerald_block", 133);
            hashMap.put("minecraft:spruce_stairs", 134);
            hashMap.put("minecraft:birch_stairs", 135);
            hashMap.put("minecraft:jungle_stairs", 136);
            hashMap.put("minecraft:command_block", 137);
            hashMap.put("minecraft:beacon", 138);
            hashMap.put("minecraft:cobblestone_wall", 139);
            hashMap.put("minecraft:flower_pot", 140);
            hashMap.put("minecraft:carrots", 141);
            hashMap.put("minecraft:potatoes", 142);
            hashMap.put("minecraft:wooden_button", 143);
            hashMap.put("minecraft:skull", 144);
            hashMap.put("minecraft:anvil", 145);
            hashMap.put("minecraft:trapped_chest", 146);
            hashMap.put("minecraft:light_weighted_pressure_plate", 147);
            hashMap.put("minecraft:heavy_weighted_pressure_plate", 148);
            hashMap.put("minecraft:unpowered_comparator", 149);
            hashMap.put("minecraft:powered_comparator", 150);
            hashMap.put("minecraft:daylight_detector", 151);
            hashMap.put("minecraft:redstone_block", 152);
            hashMap.put("minecraft:quartz_ore", 153);
            hashMap.put("minecraft:hopper", 154);
            hashMap.put("minecraft:quartz_block", 155);
            hashMap.put("minecraft:quartz_stairs", 156);
            hashMap.put("minecraft:activator_rail", 157);
            hashMap.put("minecraft:dropper", 158);
            hashMap.put("minecraft:stained_hardened_clay", 159);
            hashMap.put("minecraft:stained_glass_pane", 160);
            hashMap.put("minecraft:leaves2", 161);
            hashMap.put("minecraft:log2", 162);
            hashMap.put("minecraft:acacia_stairs", 163);
            hashMap.put("minecraft:dark_oak_stairs", 164);
            hashMap.put("minecraft:slime", 165);
            hashMap.put("minecraft:barrier", 166);
            hashMap.put("minecraft:iron_trapdoor", 167);
            hashMap.put("minecraft:prismarine", 168);
            hashMap.put("minecraft:sea_lantern", 169);
            hashMap.put("minecraft:hay_block", 170);
            hashMap.put("minecraft:carpet", 171);
            hashMap.put("minecraft:hardened_clay", 172);
            hashMap.put("minecraft:coal_block", 173);
            hashMap.put("minecraft:packed_ice", 174);
            hashMap.put("minecraft:double_plant", 175);
            hashMap.put("minecraft:standing_banner", 176);
            hashMap.put("minecraft:wall_banner", 177);
            hashMap.put("minecraft:daylight_detector_inverted", 178);
            hashMap.put("minecraft:red_sandstone", 179);
            hashMap.put("minecraft:red_sandstone_stairs", 180);
            hashMap.put("minecraft:double_stone_slab2", 181);
            hashMap.put("minecraft:stone_slab2", 182);
            hashMap.put("minecraft:spruce_fence_gate", 183);
            hashMap.put("minecraft:birch_fence_gate", 184);
            hashMap.put("minecraft:jungle_fence_gate", 185);
            hashMap.put("minecraft:dark_oak_fence_gate", 186);
            hashMap.put("minecraft:acacia_fence_gate", 187);
            hashMap.put("minecraft:spruce_fence", 188);
            hashMap.put("minecraft:birch_fence", 189);
            hashMap.put("minecraft:jungle_fence", 190);
            hashMap.put("minecraft:dark_oak_fence", 191);
            hashMap.put("minecraft:acacia_fence", 192);
            hashMap.put("minecraft:spruce_door", 193);
            hashMap.put("minecraft:birch_door", 194);
            hashMap.put("minecraft:jungle_door", 195);
            hashMap.put("minecraft:acacia_door", 196);
            hashMap.put("minecraft:dark_oak_door", 197);
            hashMap.put("minecraft:end_rod", 198);
            hashMap.put("minecraft:chorus_plant", 199);
            hashMap.put("minecraft:chorus_flower", 200);
            hashMap.put("minecraft:purpur_block", 201);
            hashMap.put("minecraft:purpur_pillar", 202);
            hashMap.put("minecraft:purpur_stairs", 203);
            hashMap.put("minecraft:purpur_double_slab", 204);
            hashMap.put("minecraft:purpur_slab", 205);
            hashMap.put("minecraft:end_bricks", 206);
            hashMap.put("minecraft:beetroots", 207);
            hashMap.put("minecraft:grass_path", 208);
            hashMap.put("minecraft:end_gateway", 209);
            hashMap.put("minecraft:repeating_command_block", 210);
            hashMap.put("minecraft:chain_command_block", 211);
            hashMap.put("minecraft:frosted_ice", 212);
            hashMap.put("minecraft:magma", 213);
            hashMap.put("minecraft:nether_wart_block", 214);
            hashMap.put("minecraft:red_nether_brick", 215);
            hashMap.put("minecraft:bone_block", 216);
            hashMap.put("minecraft:structure_void", 217);
            hashMap.put("minecraft:observer", 218);
            hashMap.put("minecraft:white_shulker_box", 219);
            hashMap.put("minecraft:orange_shulker_box", 220);
            hashMap.put("minecraft:magenta_shulker_box", 221);
            hashMap.put("minecraft:light_blue_shulker_box", 222);
            hashMap.put("minecraft:yellow_shulker_box", 223);
            hashMap.put("minecraft:lime_shulker_box", 224);
            hashMap.put("minecraft:pink_shulker_box", 225);
            hashMap.put("minecraft:gray_shulker_box", 226);
            hashMap.put("minecraft:silver_shulker_box", 227);
            hashMap.put("minecraft:cyan_shulker_box", 228);
            hashMap.put("minecraft:purple_shulker_box", 229);
            hashMap.put("minecraft:blue_shulker_box", 230);
            hashMap.put("minecraft:brown_shulker_box", 231);
            hashMap.put("minecraft:green_shulker_box", 232);
            hashMap.put("minecraft:red_shulker_box", 233);
            hashMap.put("minecraft:black_shulker_box", 234);
            hashMap.put("minecraft:white_glazed_terracotta", 235);
            hashMap.put("minecraft:orange_glazed_terracotta", 236);
            hashMap.put("minecraft:magenta_glazed_terracotta", 237);
            hashMap.put("minecraft:light_blue_glazed_terracotta", 238);
            hashMap.put("minecraft:yellow_glazed_terracotta", 239);
            hashMap.put("minecraft:lime_glazed_terracotta", 240);
            hashMap.put("minecraft:pink_glazed_terracotta", 241);
            hashMap.put("minecraft:gray_glazed_terracotta", 242);
            hashMap.put("minecraft:silver_glazed_terracotta", 243);
            hashMap.put("minecraft:cyan_glazed_terracotta", 244);
            hashMap.put("minecraft:purple_glazed_terracotta", 245);
            hashMap.put("minecraft:blue_glazed_terracotta", 246);
            hashMap.put("minecraft:brown_glazed_terracotta", 247);
            hashMap.put("minecraft:green_glazed_terracotta", 248);
            hashMap.put("minecraft:red_glazed_terracotta", 249);
            hashMap.put("minecraft:black_glazed_terracotta", 250);
            hashMap.put("minecraft:concrete", 251);
            hashMap.put("minecraft:concrete_powder", 252);
            hashMap.put("minecraft:structure_block", 255);
        }));
    }
}
