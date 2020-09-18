package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1125 extends NamespacedSchema {
    public V1125(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = super.registerBlockEntities(schema);
        schema.registerSimple(map3, "minecraft:bed");
        return map3;
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, map2, map3);
        schema.registerType(false, References.ADVANCEMENTS, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))))));
        schema.registerType(false, References.BIOME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
        schema.registerType(false, References.ENTITY_NAME, (Supplier<TypeTemplate>)(() -> DSL.constType(NamespacedSchema.namespacedString())));
    }
}
