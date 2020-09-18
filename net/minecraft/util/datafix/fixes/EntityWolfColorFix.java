package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityWolfColorFix extends NamedEntityFix {
    public EntityWolfColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityWolfColorFix", References.ENTITY, "minecraft:wolf");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        return dynamic.update("CollarColor", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.createByte((byte)(15 - dynamic.asInt(0)))));
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)this::fixTag);
    }
}
