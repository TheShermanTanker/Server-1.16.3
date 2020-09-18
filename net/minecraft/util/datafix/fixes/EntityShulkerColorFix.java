package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class EntityShulkerColorFix extends NamedEntityFix {
    public EntityShulkerColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "EntityShulkerColorFix", References.ENTITY, "minecraft:shulker");
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        if (!dynamic.get("Color").map((java.util.function.Function<? super Dynamic<?>, Object>)Dynamic::asNumber).result().isPresent()) {
            return dynamic.set("Color", dynamic.createByte((byte)10));
        }
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)this::fixTag);
    }
}
