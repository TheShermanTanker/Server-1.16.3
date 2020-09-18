package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import java.util.function.Function;
import com.google.common.collect.Maps;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.Hook;

public class V705 extends NamespacedSchema {
    protected static final Hook.HookFunction ADD_NAMES;
    
    public V705(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> V100.equipment(schema)));
    }
    
    protected static void registerThrowableProjectile(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = Maps.newHashMap();
        schema.registerSimple(map3, "minecraft:area_effect_cloud");
        registerMob(schema, map3, "minecraft:armor_stand");
        schema.register(map3, "minecraft:arrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
        registerMob(schema, map3, "minecraft:bat");
        registerMob(schema, map3, "minecraft:blaze");
        schema.registerSimple(map3, "minecraft:boat");
        registerMob(schema, map3, "minecraft:cave_spider");
        schema.register(map3, "minecraft:chest_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        registerMob(schema, map3, "minecraft:chicken");
        schema.register(map3, "minecraft:commandblock_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema))));
        registerMob(schema, map3, "minecraft:cow");
        registerMob(schema, map3, "minecraft:creeper");
        schema.register(map3, "minecraft:donkey", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.registerSimple(map3, "minecraft:dragon_fireball");
        registerThrowableProjectile(schema, map3, "minecraft:egg");
        registerMob(schema, map3, "minecraft:elder_guardian");
        schema.registerSimple(map3, "minecraft:ender_crystal");
        registerMob(schema, map3, "minecraft:ender_dragon");
        schema.register(map3, "minecraft:enderman", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:endermite");
        registerThrowableProjectile(schema, map3, "minecraft:ender_pearl");
        schema.registerSimple(map3, "minecraft:eye_of_ender_signal");
        schema.register(map3, "minecraft:falling_block", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema))));
        registerThrowableProjectile(schema, map3, "minecraft:fireball");
        schema.register(map3, "minecraft:fireworks_rocket", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema))));
        schema.register(map3, "minecraft:furnace_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema))));
        registerMob(schema, map3, "minecraft:ghast");
        registerMob(schema, map3, "minecraft:giant");
        registerMob(schema, map3, "minecraft:guardian");
        schema.register(map3, "minecraft:hopper_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        schema.register(map3, "minecraft:horse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:husk");
        schema.register(map3, "minecraft:item", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema))));
        schema.register(map3, "minecraft:item_frame", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema))));
        schema.registerSimple(map3, "minecraft:leash_knot");
        registerMob(schema, map3, "minecraft:magma_cube");
        schema.register(map3, "minecraft:minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema))));
        registerMob(schema, map3, "minecraft:mooshroom");
        schema.register(map3, "minecraft:mule", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:ocelot");
        schema.registerSimple(map3, "minecraft:painting");
        schema.registerSimple(map3, "minecraft:parrot");
        registerMob(schema, map3, "minecraft:pig");
        registerMob(schema, map3, "minecraft:polar_bear");
        schema.register(map3, "minecraft:potion", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema), "inTile", References.BLOCK_NAME.in(schema))));
        registerMob(schema, map3, "minecraft:rabbit");
        registerMob(schema, map3, "minecraft:sheep");
        registerMob(schema, map3, "minecraft:shulker");
        schema.registerSimple(map3, "minecraft:shulker_bullet");
        registerMob(schema, map3, "minecraft:silverfish");
        registerMob(schema, map3, "minecraft:skeleton");
        schema.register(map3, "minecraft:skeleton_horse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:slime");
        registerThrowableProjectile(schema, map3, "minecraft:small_fireball");
        registerThrowableProjectile(schema, map3, "minecraft:snowball");
        registerMob(schema, map3, "minecraft:snowman");
        schema.register(map3, "minecraft:spawner_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), References.UNTAGGED_SPAWNER.in(schema))));
        schema.register(map3, "minecraft:spectral_arrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
        registerMob(schema, map3, "minecraft:spider");
        registerMob(schema, map3, "minecraft:squid");
        registerMob(schema, map3, "minecraft:stray");
        schema.registerSimple(map3, "minecraft:tnt");
        schema.register(map3, "minecraft:tnt_minecart", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema))));
        schema.register(map3, "minecraft:villager", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)))), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:villager_golem");
        registerMob(schema, map3, "minecraft:witch");
        registerMob(schema, map3, "minecraft:wither");
        registerMob(schema, map3, "minecraft:wither_skeleton");
        registerThrowableProjectile(schema, map3, "minecraft:wither_skull");
        registerMob(schema, map3, "minecraft:wolf");
        registerThrowableProjectile(schema, map3, "minecraft:xp_bottle");
        schema.registerSimple(map3, "minecraft:xp_orb");
        registerMob(schema, map3, "minecraft:zombie");
        schema.register(map3, "minecraft:zombie_horse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        registerMob(schema, map3, "minecraft:zombie_pigman");
        registerMob(schema, map3, "minecraft:zombie_villager");
        schema.registerSimple(map3, "minecraft:evocation_fangs");
        registerMob(schema, map3, "minecraft:evocation_illager");
        schema.registerSimple(map3, "minecraft:illusion_illager");
        schema.register(map3, "minecraft:llama", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.registerSimple(map3, "minecraft:llama_spit");
        registerMob(schema, map3, "minecraft:vex");
        registerMob(schema, map3, "minecraft:vindication_illager");
        return map3;
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, map2, map3);
        schema.registerType(true, References.ENTITY, (Supplier<TypeTemplate>)(() -> DSL.<String>taggedChoiceLazy("id", NamespacedSchema.namespacedString(), (java.util.Map<String, Supplier<TypeTemplate>>)map2)));
        schema.registerType(true, References.ITEM_STACK, (Supplier<TypeTemplate>)(() -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema), "BlockEntityTag", References.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema)))), V705.ADD_NAMES, Hook.HookFunction.IDENTITY)));
    }
    
    static {
        ADD_NAMES = new Hook.HookFunction() {
            public <T> T apply(final DynamicOps<T> dynamicOps, final T object) {
                return V99.<T>addNames(new Dynamic<T>(dynamicOps, object), V704.ITEM_TO_BLOCKENTITY, "minecraft:armor_stand");
            }
        };
    }
}
