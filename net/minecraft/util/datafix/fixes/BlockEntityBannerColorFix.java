package net.minecraft.util.datafix.fixes;

import java.util.stream.Stream;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.schemas.Schema;

public class BlockEntityBannerColorFix extends NamedEntityFix {
    public BlockEntityBannerColorFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2, "BlockEntityBannerColorFix", References.BLOCK_ENTITY, "minecraft:banner");
    }
    
    public Dynamic<?> fixTag(Dynamic<?> dynamic) {
        dynamic = dynamic.update("Base", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.createInt(15 - dynamic.asInt(0))));
        dynamic = dynamic.update("Patterns", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> DataFixUtils.<Dynamic>orElse(dynamic.asStreamOpt().map(stream -> stream.map(dynamic -> dynamic.update("Color", dynamic -> dynamic.createInt(15 - dynamic.asInt(0))))).map(dynamic::createList).result(), dynamic)));
        return dynamic;
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)this::fixTag);
    }
}
