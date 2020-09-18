package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V135 extends Schema {
    public V135(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public void registerTypes(final Schema schema, final Map<String, Supplier<TypeTemplate>> map2, final Map<String, Supplier<TypeTemplate>> map3) {
        super.registerTypes(schema, map2, map3);
        schema.registerType(false, References.PLAYER, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema)), "Inventory", DSL.list(References.ITEM_STACK.in(schema)), "EnderItems", DSL.list(References.ITEM_STACK.in(schema)))));
        schema.registerType(true, References.ENTITY_TREE, (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(schema)), References.ENTITY.in(schema))));
    }
}
