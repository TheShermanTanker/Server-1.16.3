package net.minecraft.util.datafix.fixes;

import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class BlockEntityShulkerBoxColorFix extends NamedEntityFix {
    public BlockEntityShulkerBoxColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "BlockEntityShulkerBoxColorFix", References.BLOCK_ENTITY, "minecraft:shulker_box");
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.remove("Color")));
    }
}
