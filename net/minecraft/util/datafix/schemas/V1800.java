package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import java.util.function.Function;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1800 extends NamespacedSchema {
    public V1800(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    protected static void registerMob(final Schema schema, final Map<String, Supplier<TypeTemplate>> map, final String string) {
        schema.register(map, string, (Supplier<TypeTemplate>)(() -> V100.equipment(schema)));
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = super.registerEntities(schema);
        registerMob(schema, map3, "minecraft:panda");
        schema.register(map3, "minecraft:pillager", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), V100.equipment(schema))));
        return map3;
    }
}
