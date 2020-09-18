package net.minecraft.util.datafix.fixes;

import com.mojang.serialization.Dynamic;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.Typed;
import java.util.function.Function;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.DataFix;

public class OptionsForceVBOFix extends DataFix {
    public OptionsForceVBOFix(final Schema schema, final boolean boolean2) {
        super(schema, boolean2);
    }
    
    public TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("OptionsForceVBOFix", this.getInputSchema().getType(References.OPTIONS), (Function<Typed<?>, Typed<?>>)(typed -> typed.<Dynamic<?>>update(DSL.remainderFinder(), dynamic -> dynamic.set("useVbo", (Dynamic)dynamic.createString("true")))));
    }
}
