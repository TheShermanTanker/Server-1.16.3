package net.minecraft.util.datafix.fixes;

import java.util.Optional;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.types.Type;
import com.mojang.serialization.DynamicOps;
import java.util.function.Function;
import com.mojang.datafixers.util.Pair;
import java.util.Objects;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ReorganizePoi extends DataFix {
    public ReorganizePoi(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = DSL.<Dynamic<?>>named(References.POI_CHUNK.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.POI_CHUNK))) {
            throw new IllegalStateException("Poi type is not what was expected.");
        }
        return this.<Pair<String, Dynamic<?>>>fixTypeEverywhere("POI reorganization", type2, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, Dynamic<?>>, Pair<String, Dynamic<?>>>>)(dynamicOps -> pair -> pair.mapSecond(ReorganizePoi::cap)));
    }
    
    private static <T> Dynamic<T> cap(Dynamic<T> dynamic) {
        final Map<Dynamic<T>, Dynamic<T>> map2 = Maps.newHashMap();
        for (int integer3 = 0; integer3 < 16; ++integer3) {
            final String string4 = String.valueOf(integer3);
            final Optional<Dynamic<T>> optional5 = dynamic.get(string4).result();
            if (optional5.isPresent()) {
                final Dynamic<T> dynamic2 = (Dynamic<T>)optional5.get();
                final Dynamic<T> dynamic3 = dynamic.createMap(ImmutableMap.<Dynamic<Object>, Dynamic<T>>of(dynamic.createString("Records"), dynamic2));
                map2.put(dynamic.createInt(integer3), dynamic3);
                dynamic = dynamic.remove(string4);
            }
        }
        return dynamic.set("Sections", dynamic.createMap(map2));
    }
}
