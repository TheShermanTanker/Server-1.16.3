package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V703 extends Schema {
    public V703(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = super.registerEntities(schema);
        map3.remove("EntityHorse");
        schema.register(map3, "Horse", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.register(map3, "Donkey", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.register(map3, "Mule", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.register(map3, "ZombieHorse", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        schema.register(map3, "SkeletonHorse", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema), V100.equipment(schema))));
        return map3;
    }
}
