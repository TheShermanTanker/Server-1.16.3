package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import java.util.stream.Stream;
import java.util.Optional;
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

public abstract class PoiTypeRename extends DataFix {
    public PoiTypeRename(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<Pair<String, Dynamic<?>>> type2 = DSL.<Dynamic<?>>named(References.POI_CHUNK.typeName(), DSL.remainderType());
        if (!Objects.equals(type2, this.getInputSchema().getType(References.POI_CHUNK))) {
            throw new IllegalStateException("Poi type is not what was expected.");
        }
        return this.<Pair<String, Dynamic<?>>>fixTypeEverywhere("POI rename", type2, (java.util.function.Function<DynamicOps<?>, java.util.function.Function<Pair<String, Dynamic<?>>, Pair<String, Dynamic<?>>>>)(dynamicOps -> pair -> pair.mapSecond(this::cap)));
    }
    
    private <T> Dynamic<T> cap(final Dynamic<T> dynamic) {
        return dynamic.update("Sections", (Function<Dynamic<?>, Dynamic<?>>)(dynamic -> dynamic.updateMapValues(pair -> pair.mapSecond(dynamic -> dynamic.update("Records", dynamic -> DataFixUtils.<Dynamic<Object>>orElse(this.renameRecords((Dynamic<Object>)dynamic), dynamic))))));
    }
    
    private <T> Optional<Dynamic<T>> renameRecords(final Dynamic<T> dynamic) {
        return dynamic.asStreamOpt().<Dynamic<T>>map((java.util.function.Function<? super java.util.stream.Stream<Dynamic<T>>, ? extends Dynamic<T>>)(stream -> dynamic.createList(stream.map(dynamic -> dynamic.update("type", dynamic -> DataFixUtils.<Dynamic>orElse(dynamic.asString().map(this::rename).map(dynamic::createString).result(), dynamic)))))).result();
    }
    
    protected abstract String rename(final String string);
}
