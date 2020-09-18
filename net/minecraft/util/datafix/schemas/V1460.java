package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.templates.Hook;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import java.util.function.Function;
import com.google.common.collect.Maps;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1460 extends NamespacedSchema {
    public V1460(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> V100.equipment(schema)));
    }
    
    protected static void registerInventory(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))));
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = Maps.newHashMap();
        schema.registerSimple(map3, "minecraft:area_effect_cloud");
        registerMob(schema, map3, "minecraft:armor_stand");
        schema.register(map3, "minecraft:arrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema))));
        registerMob(schema, map3, "minecraft:bat");
        registerMob(schema, map3, "minecraft:blaze");
        schema.registerSimple(map3, "minecraft:boat");
        registerMob(schema, map3, "minecraft:cave_spider");
        schema.register(map3, "minecraft:chest_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        registerMob(schema, map3, "minecraft:chicken");
        schema.register(map3, "minecraft:commandblock_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema))));
        registerMob(schema, map3, "minecraft:cow");
        registerMob(schema, map3, "minecraft:creeper");
        schema.register(map3, "minecraft:donkey", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.registerSimple(map3, "minecraft:dragon_fireball");
        schema.registerSimple(map3, "minecraft:egg");
        registerMob(schema, map3, "minecraft:elder_guardian");
        schema.registerSimple(map3, "minecraft:ender_crystal");
        registerMob(schema, map3, "minecraft:ender_dragon");
        schema.register(map3, "minecraft:enderman", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:endermite");
        schema.registerSimple(map3, "minecraft:ender_pearl");
        schema.registerSimple(map3, "minecraft:evocation_fangs");
        registerMob(schema, map3, "minecraft:evocation_illager");
        schema.registerSimple(map3, "minecraft:eye_of_ender_signal");
        schema.register(map3, "minecraft:falling_block", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema))));
        schema.registerSimple(map3, "minecraft:fireball");
        schema.register(map3, "minecraft:fireworks_rocket", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema))));
        schema.register(map3, "minecraft:furnace_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema))));
        registerMob(schema, map3, "minecraft:ghast");
        registerMob(schema, map3, "minecraft:giant");
        registerMob(schema, map3, "minecraft:guardian");
        schema.register(map3, "minecraft:hopper_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        schema.register(map3, "minecraft:horse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:husk");
        schema.registerSimple(map3, "minecraft:illusion_illager");
        schema.register(map3, "minecraft:item", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema))));
        schema.register(map3, "minecraft:item_frame", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema))));
        schema.registerSimple(map3, "minecraft:leash_knot");
        schema.register(map3, "minecraft:llama", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.registerSimple(map3, "minecraft:llama_spit");
        registerMob(schema, map3, "minecraft:magma_cube");
        schema.register(map3, "minecraft:minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema))));
        registerMob(schema, map3, "minecraft:mooshroom");
        schema.register(map3, "minecraft:mule", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:ocelot");
        schema.registerSimple(map3, "minecraft:painting");
        schema.registerSimple(map3, "minecraft:parrot");
        registerMob(schema, map3, "minecraft:pig");
        registerMob(schema, map3, "minecraft:polar_bear");
        schema.register(map3, "minecraft:potion", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema))));
        registerMob(schema, map3, "minecraft:rabbit");
        registerMob(schema, map3, "minecraft:sheep");
        registerMob(schema, map3, "minecraft:shulker");
        schema.registerSimple(map3, "minecraft:shulker_bullet");
        registerMob(schema, map3, "minecraft:silverfish");
        registerMob(schema, map3, "minecraft:skeleton");
        schema.register(map3, "minecraft:skeleton_horse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:slime");
        schema.registerSimple(map3, "minecraft:small_fireball");
        schema.registerSimple(map3, "minecraft:snowball");
        registerMob(schema, map3, "minecraft:snowman");
        schema.register(map3, "minecraft:spawner_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), References.UNTAGGED_SPAWNER.in(schema))));
        schema.register(map3, "minecraft:spectral_arrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema))));
        registerMob(schema, map3, "minecraft:spider");
        registerMob(schema, map3, "minecraft:squid");
        registerMob(schema, map3, "minecraft:stray");
        schema.registerSimple(map3, "minecraft:tnt");
        schema.register(map3, "minecraft:tnt_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema))));
        registerMob(schema, map3, "minecraft:vex");
        schema.register(map3, "minecraft:villager", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)))), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:villager_golem");
        registerMob(schema, map3, "minecraft:vindication_illager");
        registerMob(schema, map3, "minecraft:witch");
        registerMob(schema, map3, "minecraft:wither");
        registerMob(schema, map3, "minecraft:wither_skeleton");
        schema.registerSimple(map3, "minecraft:wither_skull");
        registerMob(schema, map3, "minecraft:wolf");
        schema.registerSimple(map3, "minecraft:xp_bottle");
        schema.registerSimple(map3, "minecraft:xp_orb");
        registerMob(schema, map3, "minecraft:zombie");
        schema.register(map3, "minecraft:zombie_horse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:zombie_pigman");
        registerMob(schema, map3, "minecraft:zombie_villager");
        return map3;
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = Maps.newHashMap();
        registerInventory(schema, map3, "minecraft:furnace");
        registerInventory(schema, map3, "minecraft:chest");
        registerInventory(schema, map3, "minecraft:trapped_chest");
        schema.registerSimple(map3, "minecraft:ender_chest");
        schema.register(map3, "minecraft:jukebox", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema))));
        registerInventory(schema, map3, "minecraft:dispenser");
        registerInventory(schema, map3, "minecraft:dropper");
        schema.registerSimple(map3, "minecraft:sign");
        schema.register(map3, "minecraft:mob_spawner", (Function<String, TypeTemplate>)(string -> References.UNTAGGED_SPAWNER.in(schema)));
        schema.register(map3, "minecraft:piston", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(schema))));
        registerInventory(schema, map3, "minecraft:brewing_stand");
        schema.registerSimple(map3, "minecraft:enchanting_table");
        schema.registerSimple(map3, "minecraft:end_portal");
        schema.registerSimple(map3, "minecraft:beacon");
        schema.registerSimple(map3, "minecraft:skull");
        schema.registerSimple(map3, "minecraft:daylight_detector");
        registerInventory(schema, map3, "minecraft:hopper");
        schema.registerSimple(map3, "minecraft:comparator");
        schema.registerSimple(map3, "minecraft:banner");
        schema.registerSimple(map3, "minecraft:structure_block");
        schema.registerSimple(map3, "minecraft:end_gateway");
        schema.registerSimple(map3, "minecraft:command_block");
        registerInventory(schema, map3, "minecraft:shulker_box");
        schema.registerSimple(map3, "minecraft:bed");
        return map3;
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        schema.registerType(false, References.LEVEL, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.RECIPE, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.PLAYER, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema)), "Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema)), DSL.optionalFields("ShoulderEntityLeft", References.ENTITY_TREE.in(schema), "ShoulderEntityRight", References.ENTITY_TREE.in(schema), "recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema)))))));
        schema.registerType(false, References.CHUNK, (Supplier<TypeTemplate>)(() -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(References.BLOCK_ENTITY.in(schema)), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(schema))))))));
        schema.registerType(true, References.BLOCK_ENTITY, (Supplier<TypeTemplate>)(() -> DSL.<String>taggedChoiceLazy("id", NamespacedSchema.namespacedString(), (java.util.Map<String, Supplier<TypeTemplate>>)map3)));
        schema.registerType(true, References.ENTITY_TREE, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(schema)), References.ENTITY.in(schema))));
        schema.registerType(true, References.ENTITY, (Supplier<TypeTemplate>)(() -> DSL.<String>taggedChoiceLazy("id", NamespacedSchema.namespacedString(), (java.util.Map<String, Supplier<TypeTemplate>>)map2)));
        schema.registerType(true, References.ITEM_STACK, (Supplier<TypeTemplate>)(() -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema), "BlockEntityTag", References.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema)))), V705.ADD_NAMES, Hook.HookFunction.IDENTITY)));
        schema.registerType(false, References.HOTBAR, (Supplier<TypeTemplate>)(() -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema)))));
        schema.registerType(false, References.OPTIONS, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.STRUCTURE, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema)))));
        schema.registerType(false, References.BLOCK_NAME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.ITEM_NAME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.BLOCK_STATE, (Supplier<TypeTemplate>)DSL::remainder);
        final Supplier<TypeTemplate> supplier5 = (Supplier<TypeTemplate>)(() -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType())));
        schema.registerType(false, References.STATS, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("stats", DSL.optionalFields("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType())), "minecraft:crafted", (TypeTemplate)supplier5.get(), "minecraft:used", (TypeTemplate)supplier5.get(), "minecraft:broken", (TypeTemplate)supplier5.get(), "minecraft:picked_up", (TypeTemplate)supplier5.get(), DSL.optionalFields("minecraft:dropped", (TypeTemplate)supplier5.get(), "minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType())), "minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType())), "minecraft:custom", DSL.compoundList(DSL.constType(NamespacedSchema.namespacedString()), DSL.constType(DSL.intType())))))));
        schema.registerType(false, References.SAVED_DATA, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)), "Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema))))));
        schema.registerType(false, References.STRUCTURE_FEATURE, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Children", DSL.list(DSL.optionalFields("CA", References.BLOCK_STATE.in(schema), "CB", References.BLOCK_STATE.in(schema), "CC", References.BLOCK_STATE.in(schema), "CD", References.BLOCK_STATE.in(schema))))));
        schema.registerType(false, References.OBJECTIVE, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.TEAM, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(true, References.UNTAGGED_SPAWNER, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(schema))), "SpawnData", References.ENTITY_TREE.in(schema))));
        schema.registerType(false, References.ADVANCEMENTS, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))))));
        schema.registerType(false, References.BIOME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.ENTITY_NAME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.POI_CHUNK, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(true, References.WORLD_GEN_SETTINGS, (Supplier<TypeTemplate>)DSL::remainder);
    }
}
