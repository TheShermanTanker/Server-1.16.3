package net.minecraft.util.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V2551 extends NamespacedSchema {
    public V2551(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, map2, map3);
        schema.registerType(false, References.WORLD_GEN_SETTINGS, (Supplier<TypeTemplate>)(() -> DSL.fields("dimensions", DSL.compoundList(DSL.constType(NamespacedSchema.namespacedString()), DSL.fields("generator", (TypeTemplate)DSL.<String>taggedChoiceLazy("type", DSL.string(), (java.util.Map<String, Supplier<TypeTemplate>>)ImmutableMap.<String, Object>of("minecraft:debug", DSL::remainder, "minecraft:flat", (() -> DSL.optionalFields("settings", DSL.optionalFields("biome", References.BIOME.in(schema), "layers", DSL.list(DSL.optionalFields("block", References.BLOCK_NAME.in(schema)))))), "minecraft:noise", (() -> DSL.optionalFields("biome_source", (TypeTemplate)DSL.<String>taggedChoiceLazy("type", DSL.string(), (java.util.Map<String, Supplier<TypeTemplate>>)ImmutableMap.<String, Object>of("minecraft:fixed", (Object)(() -> DSL.fields("biome", References.BIOME.in(schema))), "minecraft:multi_noise", (Object)(() -> DSL.list(DSL.fields("biome", References.BIOME.in(schema)))), "minecraft:checkerboard", (Object)(() -> DSL.fields("biomes", DSL.list(References.BIOME.in(schema)))), "minecraft:vanilla_layered", (Object)DSL::remainder, "minecraft:the_end", (Object)DSL::remainder)), "settings", DSL.or(DSL.constType(DSL.string()), DSL.optionalFields("default_block", References.BLOCK_NAME.in(schema), "default_fluid", References.BLOCK_NAME.in(schema))))))))))));
    }
}
