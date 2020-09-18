package net.minecraft.util.datafix.fixes;

import java.util.Objects;
import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.OpticFinder;
import com.mojang.datafixers.types.Type;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class ChunkStatusFix extends DataFix {
    public ChunkStatusFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    @Override
    protected TypeRewriteRule makeRule() {
        final Type<?> type2 = this.getInputSchema().getType(References.CHUNK);
        final Type<?> type3 = type2.findFieldType("Level");
        final OpticFinder<?> opticFinder4 = DSL.fieldFinder("Level", type3);
        return this.fixTypeEverywhereTyped("ChunkStatusFix", type2, this.getOutputSchema().getType(References.CHUNK), (Function<Typed<?>, Typed<?>>)(typed -> typed.updateTyped(opticFinder4, typed -> {
            Dynamic<?> dynamic2 = typed.<Dynamic<?>>get(DSL.remainderFinder());
            final String string3 = dynamic2.get("Status").asString("empty");
            if (Objects.equals(string3, "postprocessed")) {
                dynamic2 = dynamic2.set("Status", dynamic2.createString("fullchunk"));
            }
            return typed.<Dynamic<?>>set(DSL.remainderFinder(), dynamic2);
        })));
    }
}
