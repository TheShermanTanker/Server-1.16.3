package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import net.minecraft.util.datafix.fixes.References;
import java.util.function.Function;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.function.Supplier;
import java.util.Map;
import com.mojang.datafixers.schemas.Schema;

public class V1451_2 extends NamespacedSchema {
    public V1451_2(final int integer, final Schema schema) {
        super(integer, schema);
    }
    
    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(final Schema schema) {
        final Map<String, Supplier<TypeTemplate>> map3 = super.registerBlockEntities(schema);
        schema.register(map3, "minecraft:piston", (Function<String, TypeTemplate>)(string -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(schema))));
        return map3;
    }
}
