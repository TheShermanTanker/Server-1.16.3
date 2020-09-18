package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.List;
import java.util.stream.Stream;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityShulkerRotationFix extends NamedEntityFix {
    public EntityShulkerRotationFix(final Schema schema) {
        super(schema, false, "EntityShulkerRotationFix", References.ENTITY, "minecraft:shulker");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        final List<Double> list3 = dynamic.get("Rotation").<Double>asList((java.util.function.Function<Dynamic<?>, Double>)(dynamic -> dynamic.asDouble(180.0)));
        if (!list3.isEmpty()) {
            list3.set(0, ((double)list3.get(0) - 180.0));
            return dynamic.set("Rotation", dynamic.createList(list3.stream().map(dynamic::createDouble)));
        }
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)this::fixTag);
    }
}
