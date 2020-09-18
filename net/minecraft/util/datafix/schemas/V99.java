package net.minecraft.util.datafix.schemas;

import com.mojang.serialization.DynamicOps;
import java.util.function.Consumer;
import com.mojang.datafixers.DataFixUtils;
import org.apache.logging.log4j.LogManager;
import com.mojang.datafixers.types.Type;
import java.util.HashMap;
import java.util.Objects;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;
import com.google.common.collect.Maps;
import java.util.function.Supplier;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.types.templates.Hook;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.mojang.datafixers.schemas.Schema;

public class V99 extends Schema {
    private static final Logger LOGGER;
    private static final Map<String, String> ITEM_TO_BLOCKENTITY;
    protected static final Hook.HookFunction ADD_NAMES;
    
    public V99(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static TypeTemplate equipment(final Schema schema) {
        return DSL.optionalFields("Equipment", DSL.list(References.ITEM_STACK.in(schema)));
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> equipment(schema)));
    }
    
    protected static void registerThrowableProjectile(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
    }
    
    protected static void registerMinecart(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema))));
    }
    
    protected static void registerInventory(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))));
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = Maps.newHashMap();
        schema.register(map3, "Item", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema))));
        schema.registerSimple(map3, "XPOrb");
        registerThrowableProjectile(schema, map3, "ThrownEgg");
        schema.registerSimple(map3, "LeashKnot");
        schema.registerSimple(map3, "Painting");
        schema.register(map3, "Arrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
        schema.register(map3, "TippedArrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
        schema.register(map3, "SpectralArrow", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema))));
        registerThrowableProjectile(schema, map3, "Snowball");
        registerThrowableProjectile(schema, map3, "Fireball");
        registerThrowableProjectile(schema, map3, "SmallFireball");
        registerThrowableProjectile(schema, map3, "ThrownEnderpearl");
        schema.registerSimple(map3, "EyeOfEnderSignal");
        schema.register(map3, "ThrownPotion", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("inTile", References.BLOCK_NAME.in(schema), "Potion", References.ITEM_STACK.in(schema))));
        registerThrowableProjectile(schema, map3, "ThrownExpBottle");
        schema.register(map3, "ItemFrame", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema))));
        registerThrowableProjectile(schema, map3, "WitherSkull");
        schema.registerSimple(map3, "PrimedTnt");
        schema.register(map3, "FallingSand", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Block", References.BLOCK_NAME.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema))));
        schema.register(map3, "FireworksRocketEntity", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema))));
        schema.registerSimple(map3, "Boat");
        schema.register(map3, "Minecart", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        registerMinecart(schema, map3, "MinecartRideable");
        schema.register(map3, "MinecartChest", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        registerMinecart(schema, map3, "MinecartFurnace");
        registerMinecart(schema, map3, "MinecartTNT");
        schema.register(map3, "MinecartSpawner", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), References.UNTAGGED_SPAWNER.in(schema))));
        schema.register(map3, "MinecartHopper", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("DisplayTile", References.BLOCK_NAME.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema)))));
        registerMinecart(schema, map3, "MinecartCommandBlock");
        registerMob(schema, map3, "ArmorStand");
        registerMob(schema, map3, "Creeper");
        registerMob(schema, map3, "Skeleton");
        registerMob(schema, map3, "Spider");
        registerMob(schema, map3, "Giant");
        registerMob(schema, map3, "Zombie");
        registerMob(schema, map3, "Slime");
        registerMob(schema, map3, "Ghast");
        registerMob(schema, map3, "PigZombie");
        schema.register(map3, "Enderman", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("carried", References.BLOCK_NAME.in(schema), equipment(schema))));
        registerMob(schema, map3, "CaveSpider");
        registerMob(schema, map3, "Silverfish");
        registerMob(schema, map3, "Blaze");
        registerMob(schema, map3, "LavaSlime");
        registerMob(schema, map3, "EnderDragon");
        registerMob(schema, map3, "WitherBoss");
        registerMob(schema, map3, "Bat");
        registerMob(schema, map3, "Witch");
        registerMob(schema, map3, "Endermite");
        registerMob(schema, map3, "Guardian");
        registerMob(schema, map3, "Pig");
        registerMob(schema, map3, "Sheep");
        registerMob(schema, map3, "Cow");
        registerMob(schema, map3, "Chicken");
        registerMob(schema, map3, "Squid");
        registerMob(schema, map3, "Wolf");
        registerMob(schema, map3, "MushroomCow");
        registerMob(schema, map3, "SnowMan");
        registerMob(schema, map3, "Ozelot");
        registerMob(schema, map3, "VillagerGolem");
        schema.register(map3, "EntityHorse", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), equipment(schema))));
        registerMob(schema, map3, "Rabbit");
        schema.register(map3, "Villager", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)))), equipment(schema))));
        schema.registerSimple(map3, "EnderCrystal");
        schema.registerSimple(map3, "AreaEffectCloud");
        schema.registerSimple(map3, "ShulkerBullet");
        registerMob(schema, map3, "Shulker");
        return map3;
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = Maps.newHashMap();
        registerInventory(schema, map3, "Furnace");
        registerInventory(schema, map3, "Chest");
        schema.registerSimple(map3, "EnderChest");
        schema.register(map3, "RecordPlayer", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema))));
        registerInventory(schema, map3, "Trap");
        registerInventory(schema, map3, "Dropper");
        schema.registerSimple(map3, "Sign");
        schema.register(map3, "MobSpawner", (Function<String, TypeTemplate>)(string -> References.UNTAGGED_SPAWNER.in(schema)));
        schema.registerSimple(map3, "Music");
        schema.registerSimple(map3, "Piston");
        registerInventory(schema, map3, "Cauldron");
        schema.registerSimple(map3, "EnchantTable");
        schema.registerSimple(map3, "Airportal");
        schema.registerSimple(map3, "Control");
        schema.registerSimple(map3, "Beacon");
        schema.registerSimple(map3, "Skull");
        schema.registerSimple(map3, "DLDetector");
        registerInventory(schema, map3, "Hopper");
        schema.registerSimple(map3, "Comparator");
        schema.register(map3, "FlowerPot", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Item", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema)))));
        schema.registerSimple(map3, "Banner");
        schema.registerSimple(map3, "Structure");
        schema.registerSimple(map3, "EndGateway");
        return map3;
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        schema.registerType(false, References.LEVEL, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.PLAYER, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema)))));
        schema.registerType(false, References.CHUNK, (Supplier<TypeTemplate>)(() -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(References.BLOCK_ENTITY.in(schema)), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema)))))));
        schema.registerType(true, References.BLOCK_ENTITY, (Supplier<TypeTemplate>)(() -> DSL.<String>taggedChoiceLazy("id", DSL.string(), (java.util.Map<String, Supplier<TypeTemplate>>)map3)));
        schema.registerType(true, References.ENTITY_TREE, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Riding", References.ENTITY_TREE.in(schema), References.ENTITY.in(schema))));
        schema.registerType(false, References.ENTITY_NAME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(true, References.ENTITY, (Supplier<TypeTemplate>)(() -> DSL.<String>taggedChoiceLazy("id", DSL.string(), (java.util.Map<String, Supplier<TypeTemplate>>)map2)));
        schema.registerType(true, References.ITEM_STACK, (Supplier<TypeTemplate>)(() -> DSL.hook(DSL.optionalFields("id", DSL.or(DSL.constType(DSL.intType()), References.ITEM_NAME.in(schema)), "tag", DSL.optionalFields("EntityTag", References.ENTITY_TREE.in(schema), "BlockEntityTag", References.BLOCK_ENTITY.in(schema), "CanDestroy", DSL.list(References.BLOCK_NAME.in(schema)), "CanPlaceOn", DSL.list(References.BLOCK_NAME.in(schema)))), V99.ADD_NAMES, Hook.HookFunction.IDENTITY)));
        schema.registerType(false, References.OPTIONS, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.BLOCK_NAME, (Supplier<TypeTemplate>)(() -> DSL.or(DSL.constType(DSL.intType()), DSL.constType(NamespacedSchema.namespacedString()))));
        schema.registerType(false, References.ITEM_NAME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.STATS, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.SAVED_DATA, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)), "Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema))))));
        schema.registerType(false, References.STRUCTURE_FEATURE, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.OBJECTIVE, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.TEAM, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(true, References.UNTAGGED_SPAWNER, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(false, References.POI_CHUNK, (Supplier<TypeTemplate>)DSL::remainder);
        schema.registerType(true, References.WORLD_GEN_SETTINGS, (Supplier<TypeTemplate>)DSL::remainder);
    }
    
    protected static <T> T addNames(final Dynamic<T> dynamic, final Map<String, String> map, final String string) {
        return dynamic.update("tag", (Function<Dynamic<?>, Dynamic<?>>)(dynamic4 -> dynamic4.update("BlockEntityTag", dynamic3 -> {
            final String string4 = dynamic.get("id").asString("");
            final String string5 = (String)map.get(NamespacedSchema.ensureNamespaced(string4));
            if (string5 == null) {
                V99.LOGGER.warn("Unable to resolve BlockEntity for ItemStack: {}", string4);
                return dynamic3;
            }
            return dynamic3.set("id", (Dynamic)dynamic.createString(string5));
        }).update("EntityTag", dynamic3 -> {
            final String string2 = dynamic.get("id").asString("");
            if (Objects.equals(NamespacedSchema.ensureNamespaced(string2), "minecraft:armor_stand")) {
                return dynamic3.set("id", (Dynamic)dynamic.createString(string));
            }
            return dynamic3;
        }))).getValue();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ITEM_TO_BLOCKENTITY = DataFixUtils.<Map>make((Map)Maps.newHashMap(), (java.util.function.Consumer<Map>)(hashMap -> {
            hashMap.put("minecraft:furnace", "Furnace");
            hashMap.put("minecraft:lit_furnace", "Furnace");
            hashMap.put("minecraft:chest", "Chest");
            hashMap.put("minecraft:trapped_chest", "Chest");
            hashMap.put("minecraft:ender_chest", "EnderChest");
            hashMap.put("minecraft:jukebox", "RecordPlayer");
            hashMap.put("minecraft:dispenser", "Trap");
            hashMap.put("minecraft:dropper", "Dropper");
            hashMap.put("minecraft:sign", "Sign");
            hashMap.put("minecraft:mob_spawner", "MobSpawner");
            hashMap.put("minecraft:noteblock", "Music");
            hashMap.put("minecraft:brewing_stand", "Cauldron");
            hashMap.put("minecraft:enhanting_table", "EnchantTable");
            hashMap.put("minecraft:command_block", "CommandBlock");
            hashMap.put("minecraft:beacon", "Beacon");
            hashMap.put("minecraft:skull", "Skull");
            hashMap.put("minecraft:daylight_detector", "DLDetector");
            hashMap.put("minecraft:hopper", "Hopper");
            hashMap.put("minecraft:banner", "Banner");
            hashMap.put("minecraft:flower_pot", "FlowerPot");
            hashMap.put("minecraft:repeating_command_block", "CommandBlock");
            hashMap.put("minecraft:chain_command_block", "CommandBlock");
            hashMap.put("minecraft:standing_sign", "Sign");
            hashMap.put("minecraft:wall_sign", "Sign");
            hashMap.put("minecraft:piston_head", "Piston");
            hashMap.put("minecraft:daylight_detector_inverted", "DLDetector");
            hashMap.put("minecraft:unpowered_comparator", "Comparator");
            hashMap.put("minecraft:powered_comparator", "Comparator");
            hashMap.put("minecraft:wall_banner", "Banner");
            hashMap.put("minecraft:standing_banner", "Banner");
            hashMap.put("minecraft:structure_block", "Structure");
            hashMap.put("minecraft:end_portal", "Airportal");
            hashMap.put("minecraft:end_gateway", "EndGateway");
            hashMap.put("minecraft:shield", "Banner");
        }));
        ADD_NAMES = new Hook.HookFunction() {
            public <T> T apply(final DynamicOps<T> dynamicOps, final T object) {
                return V99.<T>addNames(new Dynamic<T>(dynamicOps, object), (Map<String, String>)V99.ITEM_TO_BLOCKENTITY, "ArmorStand");
            }
        };
    }
}
