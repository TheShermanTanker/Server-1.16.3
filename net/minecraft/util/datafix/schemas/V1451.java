package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1451 extends NamespacedSchema {
    public V1451(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = super.registerBlockEntities(schema);
        schema.register(map3, "minecraft:trapped_chest", (Supplier<TypeTemplate>)(() -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)))));
        return map3;
    }
}
