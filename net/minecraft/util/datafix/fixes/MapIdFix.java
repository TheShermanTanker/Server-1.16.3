package net.minecraft.util.datafix.fixes;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class MapIdFix extends DataFix {
    public MapIdFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.SAVED_DATA);
        final OpticFinder<?> opticFinder3 = type2.findField("data");
        return this.fixTypeEverywhereTyped("Map id fix", type2, (Function<Typed<?>, Typed<?>>)(typed -> {
            final Optional<? extends Typed<?>> optional3 = typed.getOptionalTyped(opticFinder3);
            if (optional3.isPresent()) {
                return typed;
            }
            return typed.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> dynamic.createMap((Map)ImmutableMap.<Dynamic, Dynamic>of(dynamic.createString("data"), dynamic)));
        }));
    }
}
