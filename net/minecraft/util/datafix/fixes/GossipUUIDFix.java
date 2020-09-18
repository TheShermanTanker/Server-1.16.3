package net.minecraft.util.datafix.fixes;

import java.util.stream.Stream;
import java.util.Optional;
import com.mojang.datafixers.DataFixUtils;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class GossipUUIDFix extends NamedEntityFix {
    public GossipUUIDFix(final Schema schema, final String string) {
        super(schema, false, "Gossip for for " + string, References.ENTITY, string);
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.update("Gossips", dynamic -> DataFixUtils.<Dynamic>orElse((java.util.Optional<? extends Dynamic>)dynamic.asStreamOpt().result().map(stream -> stream.map(dynamic -> (Dynamic)AbstractUUIDFix.replaceUUIDLeastMost(dynamic, "Target", "Target").orElse(dynamic))).map(dynamic::createList), dynamic))));
    }
}
