package net.minecraft.util.datafix.fixes;

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

public class ForcePoiRebuild extends DataFix {
    public ForcePoiRebuild(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = DSL.<Dynamic<?>>named(References.POI_CHUNK.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.POI_CHUNK))) {
            throw new IllegalStateException("Poi type is not what was expected.");
        }
        return this.<Pair<String, Dynamic<?>>>fixTypeEverywhere("POI rebuild", type2, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, Dynamic<?>>, Pair<String, Dynamic<?>>>>)(dynamicOps -> pair -> pair.mapSecond(ForcePoiRebuild::cap)));
    }
    
    private static <T> Dynamic<T> cap(final Dynamic<T> dynamic) {
        return dynamic.update("Sections", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> dynamic.remove("Valid")))));
    }
}
