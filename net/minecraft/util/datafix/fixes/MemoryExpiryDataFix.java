package net.minecraft.util.datafix.fixes;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.function.Function;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import com.mojang.datafixers.schemas.Schema;

public class MemoryExpiryDataFix extends NamedEntityFix {
    public MemoryExpiryDataFix(final Schema schema, final String string) {
        super(schema, false, "Memory expiry data fix (" + string + ")", References.ENTITY, string);
    }
    
    @Override
    protected Typed<?> fix(final Typed<?> typed) {
        return typed.<Dynamic<?>>update(DSL.remainderFinder(), (java.util.function.Function<Dynamic<?>, Dynamic<?>>)this::fixTag);
    }
    
    public Dynamic<?> fixTag(final Dynamic<?> dynamic) {
        return dynamic.update("Brain", (Function<Dynamic<?>, Dynamic<?>>)this::updateBrain);
    }
    
    private Dynamic<?> updateBrain(final Dynamic<?> dynamic) {
        return dynamic.update("memories", (Function<Dynamic<?>, Dynamic<?>>)this::updateMemories);
    }
    
    private Dynamic<?> updateMemories(final Dynamic<?> dynamic) {
        return dynamic.updateMapValues((Function<Pair<Dynamic<?>, Dynamic<?>>, Pair<Dynamic<?>, Dynamic<?>>>)this::updateMemoryEntry);
    }
    
    private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(final Pair<Dynamic<?>, Dynamic<?>> pair) {
        return pair.<Dynamic<?>>mapSecond((java.util.function.Function<? super Dynamic<?>, ? extends Dynamic<?>>)this::wrapMemoryValue);
    }
    
    private Dynamic<?> wrapMemoryValue(final Dynamic<?> dynamic) {
        return dynamic.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<?>>of(dynamic.createString("value"), dynamic));
    }
}
